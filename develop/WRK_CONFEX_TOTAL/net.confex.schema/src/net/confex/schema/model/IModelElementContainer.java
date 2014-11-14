package net.confex.schema.model;

import java.util.List;

public interface IModelElementContainer {
	
	public void addModelElement(NodeElement obj);
	public void addModelElement(NodeElement obj, int i);
	
	public void removeModelElement(NodeElement obj);

	public List getChildren();

	public List getLastAddedList();
	
	public void clearPasteList();
}
