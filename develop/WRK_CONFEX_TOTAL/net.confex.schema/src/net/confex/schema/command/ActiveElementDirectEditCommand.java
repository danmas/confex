package net.confex.schema.command;

import net.confex.schema.model.ActiveElement;
import net.confex.schema.model.NodeElement;

import org.eclipse.gef.commands.Command;


public class ActiveElementDirectEditCommand  extends Command {

	private ActiveElement element;
	public void setActiveElement(ActiveElement element) { this.element = element; }
	
	private ActiveElement old_element;
	public void setOldActiveElement(ActiveElement element) { 
		try {
			old_element = (ActiveElement) element.getClass().newInstance(); //new ActiveElement(); 
			old_element.setPropertyLike(element);
		} catch (Exception ex) {
			System.err.println("[ActiveElementDirectEditCommand.setOldActiveElement] ex"+ex.getMessage());
		}
	}

	private ActiveElement new_element;
	public void setNewActiveElementLike(ActiveElement element) {
		try {
			new_element = (ActiveElement) element.getClass().newInstance(); //new ActiveElement();
			new_element.setPropertyLike(element);
		} catch (Exception ex) {
			System.err.println("[ActiveElementDirectEditCommand.setNewActiveElementLike] ex"+ex.getMessage());
		}
	}

	
	public ActiveElementDirectEditCommand() {}
	
	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		setOldActiveElement(element);
		element.modifyPropertyLike(new_element);
	}

	
	/**
	 * @return whether we can apply changes
	 */
	public boolean canExecute()	{
		if (new_element != null)		{
			return true;
		} else	{
			return false;
		}
	}

	
	public void undo(){
		element.modifyPropertyLike(old_element);
	}
	

	public void redo(){
		element.modifyPropertyLike(new_element);
	}

	
	/*	
	private String text, old_text;
	public void setText(String string){	this.text = string;	}
	public void setOldText(String string) { old_text = string; }

	private String tooltip_text, old_tooltip_text;
	public void setTooltipText(String string){	this.tooltip_text = string;	}
	public void setOldTooltipText(String string) { old_tooltip_text = string; }
*/

}
