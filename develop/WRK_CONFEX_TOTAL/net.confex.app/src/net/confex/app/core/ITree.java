package net.confex.app.core;

import org.eclipse.core.runtime.IProgressMonitor;

public interface ITree extends IElement {

	void create(IProgressMonitor monitor) throws ConfexException;

	void open(IProgressMonitor monitor) throws ConfexException;

}
