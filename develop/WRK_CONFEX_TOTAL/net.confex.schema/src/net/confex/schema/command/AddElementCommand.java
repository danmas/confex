package net.confex.schema.command;

import net.confex.schema.model.NodeElement;
import net.confex.schema.model.Schema;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;


public class AddElementCommand extends Command {

	private Schema schema;
	private NodeElement element;
	/** The bounds of the new Shape. */
	private Rectangle bounds;

	
	public AddElementCommand(Object newShape, Schema schema, Rectangle bounds) {
		if(!(newShape instanceof NodeElement) ) {
			System.err.println("Not NodeElement!  [AddShapeCommand.AddShapeCommand()]");
		} else 
			this.element = (NodeElement)newShape;
		this.schema = schema;
		this.bounds = bounds;
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		System.out.println("-- AddShapeCommand.execute() ");
			element.setSchema(schema);
			element.setIdForUsing();
			element.onShapeAddCommand();
			element.setBounds(bounds);
			schema.addModelElement(element);
	}
	

	/**
	 * Sets the parent ActivityDiagram
	 * 
	 * @param sa
	 *            the parent
	 */
	public void setSchema(Schema schema){
		this.schema = schema;
	}
	public void setObject(NodeElement obj) {
		this.element = obj;
	}
	

	public void undo()	{
		schema.removeModelElement(element);
	}

}