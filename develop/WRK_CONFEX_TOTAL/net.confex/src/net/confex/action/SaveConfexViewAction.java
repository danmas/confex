/*******************************************************************************
 * Copyright (c) 2006,2007 Roman Eremeev and others.
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


public class SaveConfexViewAction   extends TranslatableAction {
    
	private NavigationView navigation_view =null;

    protected String getID() { return "net.confex.action.SaveConfexViewAction"; }
    protected String getTextKey() { return "SaveAction.MENU_SAVE"; }
    protected String getIconFileName() { return "eclipse icons/etool16/save_edit.gif"; }

    
	//public SaveConfexViewAction(IWorkbenchWindow window) {
	//	super(window);
	//}
	
	
	public SaveConfexViewAction(NavigationView navigation_view) {
		super();
		this.navigation_view = navigation_view;
	}
    
	
	public void run() {
		//System.err.println("SaveConfexViewAction.run()");

		/*if(navigation_view==null && window!= null) {
			//-- получем последнюю активную Part (нам нужен вид NavigationView)
			IWorkbenchPart activePart = window.getActivePage().getActivePart();
			if(activePart instanceof NavigationView) {
				navigation_view = (NavigationView) activePart;
			}
		}*/
		if(navigation_view!=null) {
			navigation_view.runSave();
		} else {
			if(window!= null) {
				MessageDialog.openError(window.getShell(), 
						Translator.getString("MESSAGEBOX_TITLE_ERROR"),
						Translator.getString("MSG_SELECT_VIEW_FIRST"));
			}
		}
	}

	
}
