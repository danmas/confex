package net.confex.action;

import net.confex.translations.Translator;
import net.confex.tree.IClipboardView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;

public class CopyWithoutChildAction extends TranslatableAction {
    
	private IClipboardView clipboard_view;

    protected String getID() { return "net.confex.action.CopyWithoutChildAction"; }
    protected String getTextKey() { return "MENU_EDIT_CopyWithoutChildAction"; }
    protected String getIconFileName() { return "eclipse icons/etool16/copy_edit_one.gif"; }

    
	public CopyWithoutChildAction(IClipboardView clipboard_view) {
		super();
		this.clipboard_view = clipboard_view;
	}
    
	
	public void run() {
		if(clipboard_view==null && window!= null) {
			//-- получем последнюю активную Part (нам нужен вид IClipboardView)
			IWorkbenchPart activePart = window.getActivePage().getActivePart();
			if(activePart instanceof IClipboardView) {
				clipboard_view = (IClipboardView) activePart;
			}
		}
		if(clipboard_view!=null) {
			clipboard_view.runCopyWithoutChild();
		} else {
			if(window!= null) {
				MessageDialog.openError(window.getShell(), 
						Translator.getString("MESSAGEBOX_TITLE_ERROR"),
						Translator.getString("MSG_SELECT_VIEW_FIRST"));
			}
		}
	}


}
