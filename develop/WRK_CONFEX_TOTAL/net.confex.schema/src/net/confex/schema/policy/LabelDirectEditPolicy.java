package net.confex.schema.policy;


import net.confex.schema.command.LogicLabelCommand;
import net.confex.schema.figures.StickyNoteFigure;
import net.confex.schema.model.StickyNote;
import net.confex.schema.part.LabelElementPart;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;



public class LabelDirectEditPolicy 	extends DirectEditPolicy {

	/**
	 * @see DirectEditPolicy#getDirectEditCommand(DirectEditRequest)
	 */
	protected Command getDirectEditCommand(DirectEditRequest edit) {
		String labelText = (String)edit.getCellEditor().getValue();
		LabelElementPart label = (LabelElementPart)getHost();
		LogicLabelCommand command = new LogicLabelCommand((StickyNote)label.getModel(),labelText);
		return command;
	}
	
	/**
	 * @see DirectEditPolicy#showCurrentEditValue(DirectEditRequest)
	 */
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String)request.getCellEditor().getValue();
		((StickyNoteFigure)getHostFigure()).setText(value);
		//hack to prevent async layout from placing the cell editor twice.
		getHostFigure().getUpdateManager().performUpdate();
		
	}

}
