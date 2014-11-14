package net.confex.schema.command;

import net.confex.schema.model.IModelElementContainer;
import net.confex.schema.model.NodeElement;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;


public class ModelElementTransferCommand  extends Command
{

	private NodeElement elementToMove;
	//private NodeElement elementAfter;
	private IModelElementContainer originalContainer;
	private IModelElementContainer newContainer;
	private Rectangle new_constraint;
	private Rectangle old_bounds;
	//private int oldIndex, newIndex;

	
	public ModelElementTransferCommand(NodeElement elementToMove
			, IModelElementContainer originalContainer, IModelElementContainer newContainer
			, Rectangle new_constraint  //-- new position in new container
			) { // , int oldIndex, int newIndex)	{
		super();
		this.elementToMove = elementToMove;
		//this.elementAfter = columnAfter;
		this.originalContainer = originalContainer;
		this.newContainer = newContainer;
		this.new_constraint=new_constraint;
		//this.oldIndex = oldIndex;
		//this.newIndex = newIndex;
	}

	
	/**
	 * allows for transfer only if there are one or more columns
	 */
	public boolean canExecute()	{
		//if (originalContainer.getChildren().size() > 1)
			return true;
		//else
		//	return false;
	}

	
	public void execute() {
		try {
			System.out.println(" ModelElementTransferCommand.execute() "); 
			old_bounds=elementToMove.getBounds();
			elementToMove.modifyBounds(new_constraint);
			originalContainer.removeModelElement(elementToMove);
			//newContainer.addModelElement(elementToMove, newIndex);
			newContainer.addModelElement(elementToMove);
		} catch(Exception ex) {
			System.err.println(" !!! "+ ex.getMessage());
		}

	}

	
	public void undo() {
		elementToMove.modifyBounds(old_bounds);
		newContainer.removeModelElement(elementToMove);
		//originalContainer.addModelElement(elementToMove, oldIndex);
		originalContainer.addModelElement(elementToMove);
	}

}