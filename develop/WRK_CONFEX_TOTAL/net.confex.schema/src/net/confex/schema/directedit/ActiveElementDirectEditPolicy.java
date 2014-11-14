package net.confex.schema.directedit;

import net.confex.schema.command.ActiveElementDirectEditCommand;
import net.confex.schema.model.NodeElement;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;


public class ActiveElementDirectEditPolicy  extends DirectEditPolicy
{

	//private String oldValue;

	/**
	 * @see DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected Command getDirectEditCommand(DirectEditRequest request)	{
		ActiveElementDirectEditCommand cmd = new ActiveElementDirectEditCommand();
		NodeElement mylabel = (NodeElement) getHost().getModel();
		//cmd.setMyLabel(mylabel);
		//cmd.setOldName(mylabel.getText());
		CellEditor cellEditor = request.getCellEditor();
		//cmd.setText((String) cellEditor.getValue());
		return cmd;
	}

	
	/**
	 * @see DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected void showCurrentEditValue(DirectEditRequest request)	{
		System.out.println(" ------------- showCurrentEditValue");
		//String value = (String) request.getCellEditor().getValue();
		//NodeElementPart mylabelPart = (NodeElementPart) getHost();
		//mylabelPart.handleTextChange(value);
	}

	
	/**
	 * @param to
	 *            Saves the initial text value so that if the user's changes are not committed then 
	 */
	protected void storeOldEditValue(DirectEditRequest request)	{
		System.out.println(" ------------- storeOldEditValue");
		//CellEditor cellEditor = request.getCellEditor();
		//oldValue = (String) cellEditor.getValue();
	}

	
	/**
	 * @param request
	 */
	protected void revertOldEditValue(DirectEditRequest request){
		System.out.println(" ------------- revertOldEditValue");
		//CellEditor cellEditor = request.getCellEditor();
		//cellEditor.setValue(oldValue);
		//NodeElementPart mylabelPart = (NodeElementPart) getHost();
		//mylabelPart.revertTextChange();
	}
	
}
