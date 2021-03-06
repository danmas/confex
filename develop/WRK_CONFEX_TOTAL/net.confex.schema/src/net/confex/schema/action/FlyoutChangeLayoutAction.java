/*
 * Created on Jul 23, 2004
 */
package net.confex.schema.action;

import net.confex.schema.editor.ConfexDiagramEditor;
import net.confex.schema.model.Schema;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;



/**
 * Action to toggle the layout between manual and automatic
 * 
 * @author Phil Zoio
 */
public class FlyoutChangeLayoutAction extends Action {

	IEditorPart editor;

	boolean checked;

	public FlyoutChangeLayoutAction(IEditorPart editor) {
		super("My!!! Automatic Layout", Action.AS_CHECK_BOX);
		this.editor = editor;
	}

	public void run() {
		if (editor instanceof ConfexDiagramEditor) {
			ConfexDiagramEditor schemaEditor = (ConfexDiagramEditor) editor;
			Schema schema = schemaEditor.getSchema();
			boolean isManual = schema.isLayoutManualDesired();
			schema.setLayoutManualDesired(!isManual);
			checked = !isManual;
			setChecked(checked);
		}
	}

	public boolean isChecked() {
		if (editor != null)
			return isChecked(editor);
		else
			return super.isChecked();
	}

	/**
	 * @see org.eclipse.jface.action.IAction#isChecked()
	 */
	public boolean isChecked(IEditorPart editor) {

		if (editor instanceof ConfexDiagramEditor) {
			ConfexDiagramEditor schemaEditor = (ConfexDiagramEditor) editor;
			Schema schema = schemaEditor.getSchema();
			boolean checkTrue = schema.isLayoutManualDesired();
			return (!checkTrue);
		} else {
			return false;
		}

	}

	public void setActiveEditor(IEditorPart editor) {
		this.editor = editor;
		boolean localChecked = isChecked(editor);

		// there appears to be a bug in the framework which necessitates this
		if (localChecked)
			firePropertyChange(CHECKED, Boolean.FALSE, Boolean.TRUE);
		else
			firePropertyChange(CHECKED, Boolean.TRUE, Boolean.FALSE);
	}

}