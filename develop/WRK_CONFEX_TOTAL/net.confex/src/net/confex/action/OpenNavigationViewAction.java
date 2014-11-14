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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

public class OpenNavigationViewAction  extends TranslatableAction {
	
	private int instanceNum = 0;
	
    protected String getID() { return "net.confex.action.OpenNavigationViewAction"; }
    protected String getTextKey() { return "ACTION_OPEN_NAVIGATIONVIEW"; }
    protected String getIconFileName() { return "non_eclipse/confex_nav.gif"; } //"non eclipse\\confex_nav.gif"; non_eclipse\confex_nav.gif}

    
	public OpenNavigationViewAction(IWorkbenchWindow window) {
		super(window);
	}
	
	
	public void run() {
		if(window != null) {	
			try {
				NavigationView navigationView = (NavigationView)window.getActivePage()
					.showView(
						NavigationView.ID, Long.toString(System.currentTimeMillis()), 
						IWorkbenchPage.VIEW_ACTIVATE);
				// DFIXME -- old error (resolved by using System.currentTimeMillis() ?)
				if(!navigationView.getTitle().equals("Confex Navigation")) {
					System.err.println(" Ошибка создается вид поверх старого "+navigationView.getTitle());
					return;
				}
				navigationView.loadFile();
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), 
						Translator.getString("MESSAGEBOX_TITLE_ERROR"),
						Translator.getString("MESSAGE_ERR_OPEN_VIEW") + e.getMessage());
			}
		}
	}
	
}	

