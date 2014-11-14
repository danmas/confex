package net.confex.schema.policy;

import net.confex.schema.command.ModelElementMoveCommand;
import net.confex.schema.command.ModelElementTransferCommand;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.SimpleContainer;
import net.confex.schema.part.NodeElementPart;
import net.confex.schema.part.SimpleContainerPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;


public class SimpleContainerLayoutEditPolicy2 extends FlowLayoutEditPolicy {

	/**
	 * Creates command to transfer child column to after column (in another
	 * table)
	 */
	protected Command createAddCommand(EditPart child, EditPart after) {

		if (!(child instanceof NodeElementPart))
			return null;
		//if (!(after instanceof NodeElementPart))
		//	return null;

		NodeElement toMove = (NodeElement) child.getModel();
		//NodeElement afterModel = (NodeElement) after.getModel();

		if( !(child.getParent() instanceof SimpleContainerPart) ) {
			System.err.println(child.getParent() + " NOT instanceof SimpleContainerPart [SimpleContainerLayoutEditPolicy2.createAddCommand]");
			return null;
		}
		SimpleContainerPart originalContainerPart = (SimpleContainerPart) child.getParent();
		SimpleContainer originalContainer = (SimpleContainer) originalContainerPart.getModel();
		SimpleContainerPart newContainerPart = (SimpleContainerPart) after.getParent();
		SimpleContainer newContainer = newContainerPart.getSimpleContainer();

		int oldIndex = originalContainerPart.getChildren().indexOf(child);
		int newIndex = newContainerPart.getChildren().indexOf(after);

		ModelElementTransferCommand command = new ModelElementTransferCommand(
				toMove,  originalContainer, newContainer,null);
		//System.out.println("SimpleContainerLayoutEditPolicy2.createAddCommand() -> command");
		return command;
	}

	
	/**
	 * Creates command to transfer child column to after specified column
	 * (within table)
	 */
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		
		if (after != null) {
			NodeElement childModel = (NodeElement) child.getModel();
			NodeElement afterModel = (NodeElement) after.getModel();

			SimpleContainer parentContainer = (SimpleContainer) getHost().getModel();
			int oldIndex = getHost().getChildren().indexOf(child);
			int newIndex = getHost().getChildren().indexOf(after);

			ModelElementMoveCommand command = new ModelElementMoveCommand(childModel, parentContainer, oldIndex, newIndex);
			System.out.println("SimpleContainerLayoutEditPolicy2.createMoveChildCommand() -> command");
			return command;
		}
		System.err.println("SimpleContainerLayoutEditPolicy2.createMoveChildCommand() -> null");
		return null;
	}

	
	
	/**
	 * @param request
	 * @return
	 */
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	
	/**
	 * @param request
	 * @return
	 */
	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}


}
