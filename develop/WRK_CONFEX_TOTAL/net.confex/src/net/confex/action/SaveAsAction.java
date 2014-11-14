/*******************************************************************************
 * Copyright (c) 2006 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package net.confex.action;

import net.confex.translations.Translator;
import net.confex.views.NavigationView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;


public class SaveAsAction extends TranslatableAction {
    
	private NavigationView navigation_view;

    protected String getID() { return "net.confex.action.SaveAsAction"; }
    protected String getTextKey() { return "SaveAsAction.MENU_SAVE_AS"; }
    protected String getIconFileName() { return "eclipse icons/etool16/save_edit.gif"; }

    
	public SaveAsAction(IWorkbenchWindow window) {
		super(window);
	}
	
    
	//public SaveAsAction(NavigationView navigation_view) {
	//	super();
	//	this.navigation_view = navigation_view;
	//}
    
	
	public void run() {
		navigation_view =null;
		
		if(window!= null) {
			//-- получем последнюю активную Part (нам нужен вид NavigationView)
			IWorkbenchPart activePart = window.getActivePage().getActivePart();
			if(activePart instanceof NavigationView) {
				navigation_view = (NavigationView) activePart;
			}
		}
		if(navigation_view!=null) {
			navigation_view.runSaveAs();
		} else {
			if(window!= null) {
				MessageDialog.openError(window.getShell(), 
						Translator.getString("MESSAGEBOX_TITLE_ERROR"),
						Translator.getString("MSG_SELECT_VIEW_FIRST"));
			}
		}
	}

}
