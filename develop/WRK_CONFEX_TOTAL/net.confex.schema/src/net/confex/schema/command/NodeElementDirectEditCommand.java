package net.confex.schema.command;

import net.confex.schema.model.NodeElement;

import org.eclipse.gef.commands.Command;



public class NodeElementDirectEditCommand  extends Command {

	private NodeElement element;
	public void setNodeElement(NodeElement element) { this.element = element; }
	
	private NodeElement old_element;
	public void setOldNodeElement(NodeElement element) { 
		old_element = new NodeElement();
		old_element.setPropertyLike(element);
	}

	private NodeElement new_element;
	public void setNewNodeElementLike(NodeElement element) { 
		new_element = new NodeElement();
		new_element.setPropertyLike(element);
	}

	
	public NodeElementDirectEditCommand() {}
	
	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		setOldNodeElement(element);
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

}
