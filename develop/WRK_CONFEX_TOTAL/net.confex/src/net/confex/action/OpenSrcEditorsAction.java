/*******************************************************************************
 * Copyright (c) 2007 Roman Eremeev and others.
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
import net.confex.tree.IClipboardView;
import net.confex.views.NavigationView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;


/**
 * 
 */
public class OpenSrcEditorsAction   extends TranslatableAction {
    
	private IClipboardView clipboard_view;

    protected String getID() { return "net.confex.action.OpenSrcEditorsAction"; }
    protected String getTextKey() { return "OpenSrcEditorsAction"; }
    protected String getIconFileName() { return "eclipse icons/eview16/prop_ps.gif"; }

    
	public OpenSrcEditorsAction(IWorkbenchWindow window) {
		super(window);
	}
	
	
	//public SaveAction(NavigationView navigation_view) {
	//	super();
	//	this.navigation_view = navigation_view;
	//}
    
	
	public void run() {
		clipboard_view = null;
		if(window!= null) {
			//-- получем последнюю активную Part (нам нужен вид NavigationView)
			IWorkbenchPart activePart = window.getActivePage().getActivePart();
			if(activePart instanceof IClipboardView) {
				clipboard_view = (IClipboardView) activePart;
			}
		}
		if(clipboard_view!=null) {
			clipboard_view.openSrcEditors();
		} else {
			if(window!= null) {
				MessageDialog.openError(window.getShell(), 
						Translator.getString("MESSAGEBOX_TITLE_ERROR"),
						Translator.getString("MSG_SELECT_VIEW_FIRST"));
			}
		}
		
	}

	
}
