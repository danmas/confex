package net.confex.schema.policy;

import net.confex.schema.command.DeleteSimpleContainerCommand;
import net.confex.schema.model.IModelElementContainer;
import net.confex.schema.model.Schema;
import net.confex.schema.model.SimpleContainer;
import net.confex.schema.part.SimpleContainerPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;



public class SimpleContainerEditPolicy  extends ComponentEditPolicy {

	
	protected Command createDeleteCommand(GroupRequest request)	{
		SimpleContainerPart simpleContainerPart = (SimpleContainerPart) getHost();
		Rectangle bounds = simpleContainerPart.getFigure().getBounds().getCopy();
		IModelElementContainer container = (IModelElementContainer)simpleContainerPart.getParent().getModel();
		DeleteSimpleContainerCommand deleteCmd = new DeleteSimpleContainerCommand();
		deleteCmd.setContainer(container);
		deleteCmd.setSimpleContainer((SimpleContainer) (simpleContainerPart.getModel()));
		deleteCmd.setOriginalBounds(bounds);
		return deleteCmd;
	}
	
	
}
