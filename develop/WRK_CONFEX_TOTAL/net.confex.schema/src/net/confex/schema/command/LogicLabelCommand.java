package net.confex.schema.command;

import net.confex.schema.model.StickyNote;

import org.eclipse.gef.commands.Command;



public class LogicLabelCommand extends Command {
	
	private String newName, oldName;
	private StickyNote label;
	
	public LogicLabelCommand(StickyNote l, String s) {
		label = l;
		if (s != null)
			newName = s;
		else
			newName = "";  //$NON-NLS-1$
	}
	
	public void execute() {
		oldName = label.getLabelContents();
		label.setLabelContents(newName);
	}
	
	public void undo() {
		label.setLabelContents(oldName);
	}
	
}