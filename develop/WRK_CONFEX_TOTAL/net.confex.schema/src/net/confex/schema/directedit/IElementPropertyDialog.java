package net.confex.schema.directedit;

import net.confex.schema.model.NodeElement;
import net.confex.schema.part.NodeElementPart;




public interface IElementPropertyDialog {

	public void setNodeElement(NodeElement element); 
	
	public void setEditPart(NodeElementPart edit_part );

	public void dispose();
	
	public void show();


}
