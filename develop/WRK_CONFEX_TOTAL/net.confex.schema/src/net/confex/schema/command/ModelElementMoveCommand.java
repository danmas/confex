package net.confex.schema.command;

import net.confex.schema.model.NodeElement;
import net.confex.schema.model.SimpleContainer;

import org.eclipse.gef.commands.Command;


public class ModelElementMoveCommand extends Command {

	private int oldIndex, newIndex;
	private NodeElement childElement;
	private SimpleContainer parentContainer;

	
	public ModelElementMoveCommand(NodeElement child, SimpleContainer parent, int oldIndex, int newIndex)	{
		this.childElement = child;
		this.parentContainer = parent;
		this.oldIndex = oldIndex;
		this.newIndex = newIndex;
		if (newIndex > oldIndex)
			newIndex--; //this is because the column is deleted before it is
						// added
	}

	
	public void execute() {
		parentContainer.switchChild(childElement, newIndex);
	}

	
	public void undo()	{
		parentContainer.switchChild(childElement, oldIndex);
	}

}