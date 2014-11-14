package net.confex.schema.policy;

import net.confex.schema.command.DeleteNodeElementCommand;
import net.confex.schema.model.IModelElementContainer;
import net.confex.schema.model.NodeElement;
import net.confex.schema.part.NodeElementPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;


public class NodeComponentEditPolicy extends ComponentEditPolicy {

	public static final String MY_REQ_COPY = "MY_REQ_COPY";

	protected Command createDeleteCommand(GroupRequest request)	{
		NodeElementPart model_element_part = (NodeElementPart) getHost();
		Rectangle bounds = model_element_part.getFigure().getBounds().getCopy();
		IModelElementContainer container = (IModelElementContainer)model_element_part.getParent().getModel();
		DeleteNodeElementCommand deleteCmd = new DeleteNodeElementCommand();
		deleteCmd.setContainer(container);
		deleteCmd.setNodeElement((NodeElement) (model_element_part.getModel()));
		deleteCmd.setOriginalBounds(bounds);
		
		return deleteCmd;
	}
	
/*	
	protected Command createCopyCommand(GroupRequest request)	{
		NodeElementPart model_element_part = (NodeElementPart) getHost();
		Rectangle bounds = model_element_part.getFigure().getBounds().getCopy();
		IModelElementContainer container = (IModelElementContainer)model_element_part.getParent().getModel();
		CopyNodeElementCommand copyCmd = new CopyNodeElementCommand();
		copyCmd.setContainer(container);
		copyCmd.setNodeElement((NodeElement) (model_element_part.getModel()));
		copyCmd.setOriginalBounds(bounds);
		
		return copyCmd;
	}

	
	public Command getCommand(Request request) {
		Command cmd = super.getCommand(request);
		if (cmd != null)
			return cmd;
		if (MY_REQ_COPY.equals(request.getType()))
			return createCopyCommand((GroupRequest)request);
		return null;
	}
*/	
	
}