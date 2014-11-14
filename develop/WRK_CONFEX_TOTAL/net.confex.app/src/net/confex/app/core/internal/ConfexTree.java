package net.confex.app.core.internal;

import net.confex.app.core.ConfexException;
import net.confex.app.core.IElement;
import net.confex.app.core.ITree;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

public class ConfexTree implements ITree {

	ConfexModel parent;

	IProject project;

	public ConfexTree(IProject project, ConfexModel parent) {
		this.project = project;
		this.parent = parent;
	}

	@Override
	public void create(IProgressMonitor monitor) throws ConfexException {
		if (monitor == null)
			monitor = new NullProgressMonitor();

		try {
			project.create(monitor);
			monitor.worked(1);
		} catch (CoreException e) {
			throw new ConfexException(e.getStatus());
		}
	}

	@Override
	public String toString() {
		return getElementName();
	}

	@Override
	public void open(IProgressMonitor monitor) throws ConfexException {
		if (monitor == null)
			monitor = new NullProgressMonitor();

		try {
			project.open(monitor);
			monitor.worked(1);
		} catch (CoreException e) {
			throw new ConfexException(e.getStatus());
		}
	}

	@Override
	public String getElementName() {
		return project.getName();
	}

	@Override
	public IElement getParent() {
		return parent;
	}

	@Override
	public IResource getResource() {
		return project;
	}

}
