package net.confex.app.core.internal;

import java.util.HashMap;

import net.confex.app.core.ConfexException;
import net.confex.app.core.IConfexChangeListener;
import net.confex.app.core.IConfexDelta;
import net.confex.app.core.IElement;
import net.confex.app.core.IModel;
import net.confex.app.core.ITree;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;

public class ConfexModel implements IModel {

	private IConfexChangeListener[] confexChangeListeners = new IConfexChangeListener[5];
	private int confexChangeListenerCount = 0;

	private final HashMap<String, ITree> treesCache = new HashMap<String, ITree>();

	public void init() {
		subscribeToResourcesChanges();
		fillTreesCache();
	}

	private void fillTreesCache() {
		for (IProject project : getResourcesRoot().getProjects()) {
			String name = project.getName();
			treesCache.put(name, createTree(name));
		}
	}

	private void subscribeToResourcesChanges() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new IResourceChangeListener() {

					@Override
					public void resourceChanged(IResourceChangeEvent event) {
						IResourceDelta delta = event.getDelta();
						// if (delta.getKind() == IResourceDelta.CHANGED)
						// 
						try {
							delta.accept(new IResourceDeltaVisitor() {

								@Override
								public boolean visit(IResourceDelta delta)
										throws CoreException {
									IResource res = delta.getResource();
									switch (delta.getKind()) {
									case IResourceDelta.ADDED:
										if (res.getType() == IResource.PROJECT)
											fire(res, IConfexDelta.CREATED);
										// System.out.print("Resource ");
										// System.out.print(res.getFullPath());
										// System.out.println(" was added.");
										break;
									case IResourceDelta.REMOVED:
										// System.out.print("Resource ");
										// System.out.print(res.getFullPath());
										// System.out.println(" was removed.");
										break;
									case IResourceDelta.CHANGED:
										// System.out.print("Resource ");
										// System.out.print(res.getFullPath());
										// System.out.println(" has changed.");
										break;
									}
									return true; // visit the children
								}

							});
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
	}

	private void fire(IResource resource, int kind) {
		ConfexDelta deltaToNotify = new ConfexDelta(resource, kind);
		notifyListeners(deltaToNotify);
	}

	@Override
	public synchronized void addChangeListener(IConfexChangeListener listener) {
		// may need to grow, no need to clone, since iterators will have cached
		// original arrays and max boundary and we only add to the end.
		int length;
		if ((length = this.confexChangeListeners.length) == this.confexChangeListenerCount)
			System
					.arraycopy(
							this.confexChangeListeners,
							0,
							this.confexChangeListeners = new IConfexChangeListener[length * 2],
							0, length);

		this.confexChangeListeners[this.confexChangeListenerCount] = listener;
		this.confexChangeListenerCount++;

	}

	@Override
	public synchronized void removeChangeListener(IConfexChangeListener listener) {
		for (int i = 0; i < this.confexChangeListenerCount; i++) {

			if (this.confexChangeListeners[i] == listener) {

				// need to clone defensively since we might be in the middle of
				// listener notifications (#fire)
				int length = this.confexChangeListeners.length;
				IConfexChangeListener[] newListeners = new IConfexChangeListener[length];
				System.arraycopy(this.confexChangeListeners, 0, newListeners,
						0, i);

				// copy trailing listeners
				int trailingLength = this.confexChangeListenerCount - i - 1;
				if (trailingLength > 0)
					System.arraycopy(this.confexChangeListeners, i + 1,
							newListeners, i, trailingLength);

				// update manager listener state (#fire need to iterate over
				// original listeners through a local variable to hold onto
				// the original ones)
				this.confexChangeListeners = newListeners;
				this.confexChangeListenerCount--;
				return;
			}
		}

	}

	private void notifyListeners(IConfexDelta deltaToNotify) {
		final ConfexChangeEvent extraEvent = new ConfexChangeEvent(
				deltaToNotify);
		for (int i = 0; i < confexChangeListenerCount; i++) {
			final IConfexChangeListener listener = confexChangeListeners[i];

			// wrap callbacks with Safe runnable for subsequent listeners to be
			// called when some are causing grief
			SafeRunner.run(new ISafeRunnable() {
				public void handleException(Throwable exception) {

				}

				public void run() throws Exception {
					listener.confexChanged(extraEvent);
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.confex.app.core.IModel#getTrees()
	 */
	public ITree[] getTrees() throws ConfexException {
		return treesCache.values().toArray(new ITree[treesCache.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.confex.app.core.IModel#getTree(org.eclipse.core.resources.IResource)
	 */
	public ITree getTree(IResource resource) throws ConfexException {
		if (!(resource.getType() == IResource.PROJECT))
			return null;

		String treeName = resource.getName();
		if (!treesCache.containsKey(treeName))
			treesCache.put(treeName, createTree(treeName));

		return treesCache.get(treeName);
	}

	@Override
	public String getElementName() {
		return "Confex ConfexModel";
	}

	@Override
	public IElement getParent() {
		return null;
	}

	@Override
	public IResource getResource() {
		return getResourcesRoot();
	}

	private IWorkspaceRoot getResourcesRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	@Override
	public ITree getTree(String name) throws ConfexException {
		if (treesCache.containsKey(name))
			return treesCache.get(name);

		ITree tree = createTree(name);
		treesCache.put(name, tree);
		return tree;
	}

	private ITree createTree(String name) {
		return new ConfexTree(getResourcesRoot().getProject(name), this);
	}

}
