package net.confex.schema.policy;

import net.confex.schema.command.ConnectionCreateCommand;
import net.confex.schema.command.ReconnectInCommand;
import net.confex.schema.command.ReconnectOutCommand;
import net.confex.schema.model.ModelConnection;
import net.confex.schema.part.NodeElementPart;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;



public class ModelNodeEditPolicy extends GraphicalNodeEditPolicy {

	/**
	 * @see GraphicalNodeEditPolicy#getConnectionCreateCommand(CreateConnectionRequest)
	 */
	protected Command getConnectionCreateCommand(CreateConnectionRequest request){
		NodeElementPart part = (NodeElementPart) getHost();
		ConnectionCreateCommand cmd = new ConnectionCreateCommand(part.getSchema());
		cmd.setOutElement(part.getNodeElement());
		request.setStartCommand(cmd);
		return cmd;
	}

	
	/**
	 * @see GraphicalNodeEditPolicy#getConnectionCompleteCommand(CreateConnectionRequest)
	 */
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request)	{
		ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
		NodeElementPart part = (NodeElementPart) request.getTargetEditPart();
		cmd.setInElement(part.getNodeElement());
		return cmd;
	}

	
	/**
	 * @see GraphicalNodeEditPolicy#getReconnectSourceCommand(ReconnectRequest)
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		NodeElementPart part = (NodeElementPart) getHost();
		ReconnectOutCommand cmd = new ReconnectOutCommand(); //part.getSchema());
		cmd.setConnection((ModelConnection) request.getConnectionEditPart().getModel());
		cmd.setSourceOutElement(part.getNodeElement());
		return cmd;
	}

	
	/**
	 * @see GraphicalNodeEditPolicy#getReconnectTargetCommand(ReconnectRequest)
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		NodeElementPart part = (NodeElementPart) getHost();
		ReconnectInCommand cmd = new ReconnectInCommand(); //part.getSchema());
		cmd.setConnection((ModelConnection) request.getConnectionEditPart().getModel());
		cmd.setInElement(part.getNodeElement());
		return cmd;
	}


}
