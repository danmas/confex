package net.confex.schema.command;

import java.util.Iterator;
import java.util.List;

import net.confex.schema.model.IModelElementContainer;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.Schema;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;


public class PasteCommand extends Command {

	private Schema schema;
	public void setSchema(Schema schema){ this.schema = schema;	}

	IModelElementContainer container;
	public void setContainer(IModelElementContainer container) { this.container = container; }
	
	private List   elements;   //-- список вставленных элементов
	
	private Rectangle  location;
	public  void  setLocation(Rectangle  location) { this.location = location; } 
	
	private EditPart hostEditPart;
	public void setHostEditPart(EditPart edit_part) { hostEditPart = edit_part; }
	
	//private String     paste_xml;
	//public void setPasteXml(Schema paste_xml){ this.paste_xml = paste_xml;	}

	
	public PasteCommand() {}
	
	
	/**/
	public PasteCommand(String paste_xml, Schema schema) {
		this.schema = schema;
		//this.location = location;
		//this.paste_xml = paste_xml;
	}/**/

	
	/**
	 * @return whether we can apply changes
	 */
	public boolean canExecute()	{
		return true;
	}
	
	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		System.out.println("-- PasteCommand.execute() ");
		try {
			schema.pasteAsXml(new Point(location.x,location.y),container);
			elements = schema.getPasteList();
			
			//-- оставляем выделенными только вставленные элементы
			hostEditPart.getViewer().deselectAll();
			//-- TODO выделить вставленные элементы   -> schema.selectPasteList(hostEditPart);
			
		} catch(Exception ex) {
			System.err.println("Error: "+ ex.getMessage());
		}
	}
	
	
	public void undo()	{
		for(Iterator iter=elements.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof NodeElement) {
				schema.removeModelElement((NodeElement) obj);
			}
		}
	}


	/**
	 * Sets the parent ActivityDiagram
	 * 
	 * @param sa
	 *            the parent
	 */
	//public void setSchema(Schema schema){
	//	this.schema = schema;
	//}
	//public void setObject(NodeElement obj) {
	//	this.element = obj;
	//}
	

}
