package net.confex.schema.command;

import net.confex.schema.model.NodeElement;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;



/**
 * Command to move the bounds of an existing label. Only used with
 * XYLayoutEditPolicy (manual layout)
 * 
 * @author Roman Eremeev
 */
public class ShapeMoveCommand extends Command {

	private NodeElement label;
	private Rectangle oldBounds;
	private Rectangle newBounds;

	public ShapeMoveCommand(NodeElement label, Rectangle oldBounds, Rectangle newBounds)
	{
		super();
		this.label = label;
		this.oldBounds = oldBounds;
		this.newBounds = newBounds;
	}

	public void execute()
	{
		label.modifyBounds(newBounds);
	}

	public void undo()
	{
		label.modifyBounds(oldBounds);
	}

}
