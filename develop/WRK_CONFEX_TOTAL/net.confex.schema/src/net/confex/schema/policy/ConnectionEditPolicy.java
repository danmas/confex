package net.confex.schema.policy;

import net.confex.schema.command.DeleteConnectionCommand;
import net.confex.schema.model.ModelConnection;
import net.confex.schema.model.NodeElement;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;


public class ConnectionEditPolicy extends ComponentEditPolicy {

	protected Command createDeleteCommand(GroupRequest request)
	{
		ModelConnection relationship = (ModelConnection) getHost().getModel();
		NodeElement primaryKeyTarget = relationship.getInElement();
		NodeElement foreignKeySource = relationship.getOutElement();
		DeleteConnectionCommand deleteCmd = new DeleteConnectionCommand(foreignKeySource, primaryKeyTarget, relationship);
		return deleteCmd;
	}
	
}
