package net.confex.app.core;

import org.eclipse.core.resources.IResource;

public interface IElement {

	String getElementName();

	IElement getParent();

	IResource getResource();

}
