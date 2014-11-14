package net.confex.action;

import net.confex.translations.Translator;
import net.confex.views.NavigationView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

public class NewConfexFileAction  extends TranslatableAction {
	
	private int instanceNum = 0;
	
    protected String getID() { return "net.confex.action.NewConfexFileAction"; }
    protected String getTextKey() { return "ACTION_NEW_CONFEXFILE"; }
    protected String getIconFileName() { return "plus.gif"; } //"non eclipse/confex_nav.gif"; non_eclipse\confex_nav.gif}

    
	public NewConfexFileAction(IWorkbenchWindow window) {
		super(window);
	}
	
	
	public void run() {
		if(window != null) {	
			try {
				NavigationView navigationView = (NavigationView)window.getActivePage().showView(
						NavigationView.ID, Integer.toString(instanceNum++), 
						IWorkbenchPage.VIEW_ACTIVATE);
				navigationView.newFile();
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), 
						Translator.getString("MESSAGEBOX_TITLE_ERROR"),
						Translator.getString("MESSAGE_ERR_OPEN_VIEW") + e.getMessage());
			}
		}
	}
}
