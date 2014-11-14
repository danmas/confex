package net.confex.schema.command;

import net.confex.schema.model.NodeElement;

import org.eclipse.gef.commands.Command;


/**
 * Команда на изменение текста маркера
 * 
 * @author Roman Eremeev
 */
public class ChangeMyLabelTextCommand  extends Command {

	private NodeElement label;
	private String text, old_text;

	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		//System.out.println("-- ChangeTableNameCommand.execute() ");
		label.modifyText(text);
	}

	
	/**
	 * @return whether we can apply changes
	 */
	public boolean canExecute()	{
		if (text != null)		{
			return true;
		} else	{
			text = old_text;
			return false;
		}
	}

	
	/**
	 * 
	 * @param string new text
	 */
	public void setText(String string){
		this.text = string;
	}

	
	/**
	 * Sets the old text
	 * 
	 * @param string
	 *            the old text
	 */
	public void setOldName(String string) {
		old_text = string;
	}

	
	/**
	 * @param table
	 *            The table to set.
	 */
	public void setMyLabel(NodeElement mylabel)	{
		this.label = mylabel;
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo(){
		label.modifyText(old_text);
	}

}