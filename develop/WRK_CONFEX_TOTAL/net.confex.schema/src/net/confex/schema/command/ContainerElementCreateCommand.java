package net.confex.schema.command;

import net.confex.schema.model.NodeElement;
import net.confex.schema.model.Schema;
import net.confex.schema.model.SimpleContainer;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;


public class ContainerElementCreateCommand extends Command
{

	private NodeElement element;
	private SimpleContainer container;
	private Schema schema;
	/** The bounds of the new Shape. */
	private Rectangle bounds;
	
	
	public ContainerElementCreateCommand(
			SimpleContainer container, NodeElement element, Schema schema, Rectangle bounds) {
		this.container = container;
		this.element = element;
		this.schema = schema;
		if(bounds==null) {
			System.err.println(" new ContainerElementCreateCommand() bounds is NULL!!!");
			this.bounds = new Rectangle(0,0,40,40);
		} else {
			this.bounds = bounds;
		}
	}

	
	public void setElement(NodeElement column) {
		this.element = column;
		this.element.setText("COLUMN " + (container.getChildren().size() + 1));
	}

	
	public void execute()	{
		
		element.setSchema(schema);
		element.setIdForUsing();
		element.onShapeAddCommand();
		element.setBounds(bounds);

		container.addModelElement(element);

		//schema.addModelElement(element);
	}

	
	public void undo()	{
		container.removeModelElement(element);
	}

}