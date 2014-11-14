package net.confex.app.views;

import net.confex.app.core.ConfexException;
import net.confex.app.core.IConfexChangeEvent;
import net.confex.app.core.IConfexChangeListener;
import net.confex.app.core.IConfexDelta;
import net.confex.app.core.IModel;
import net.confex.app.core.ITree;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

public class ViewContentProvider implements ITreeContentProvider,
		IConfexChangeListener {

	protected static final Object[] NO_CHILDREN = new Object[0];

	IModel content;

	TreeViewer viewer;

	@Override
	public Object[] getChildren(Object parentElement) {
		try {
			if (parentElement instanceof IModel)
				return getConfexTrees((IModel) parentElement);

			// if (parentElement instanceof IConfexProject)
			// return getPackageFragmentRoots((IConfexProject) element);
			//
			// if (parentElement instanceof IFolder)
			// return getFolderContent((IFolder) element);
			//
			// if (parentElement instanceof IParent) {
			// return ((IParent) element).getChildren();
			// }
		} catch (ConfexException e) {
			return NO_CHILDREN;
		}

		return NO_CHILDREN;
	}

	private ITree[] getConfexTrees(IModel model) throws ConfexException {
		return model.getTrees();
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IModel)
			return null;

		if (element instanceof ITree) {
			ITree tree = (ITree) element;
			return tree.getParent();
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
		Assert.isLegal(newInput instanceof IModel);

		if (content != null)
			content.removeChangeListener(this);

		content = (IModel) newInput;
		content.addChangeListener(this);
	}

	@Override
	public void confexChanged(IConfexChangeEvent event) {
		IConfexDelta delta = event.getDelta();
		try {
			switch (delta.getKind()) {
			case IConfexDelta.NO_CHANGE:
				break;
			case IConfexDelta.ADDED:
				break;
			case IConfexDelta.CHANGED:
				break;
			case IConfexDelta.REMOVED:
				break;
			case IConfexDelta.CREATED:
				addToViewer(delta);
				break;
			}
		} catch (ConfexException e) {
			;
		}
	}

	private void addToViewer(final IConfexDelta delta) throws ConfexException {
		execute(new Runnable() {

			@Override
			public void run() {
				try {
					viewer.add(delta.getElement().getParent(), delta
							.getElement());
				} catch (ConfexException e) {
					;
				}
			}

		});
	}

	private void execute(Runnable runnable) {
		Control control = viewer.getControl();
		if (!control.isDisposed()) {
			control.getDisplay().asyncExec(runnable);
		}

	}

}
