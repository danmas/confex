package net.confex.app.core;

import org.eclipse.core.resources.IResource;

public interface IModel extends IElement {

	ITree[] getTrees() throws ConfexException;

	ITree getTree(String name) throws ConfexException;

	ITree getTree(IResource resource) throws ConfexException;

	void addChangeListener(IConfexChangeListener listener);

	void removeChangeListener(IConfexChangeListener listener);

}