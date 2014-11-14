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
import net.confex.tree.IClipboardView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Действие "Скопировать ветку в буфер обмена"
 */
public class CopyAction extends TranslatableAction {
    
	private IClipboardView clipboard_view;

    protected String getID() { return "net.confex.action.CopyAction"; }
    protected String getTextKey() { return "MENU_EDIT_COPY"; }
    protected String getIconFileName() { return "eclipse icons/etool16/copy_edit.gif"; }

    
	public CopyAction(IClipboardView clipboard_view) {
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
			clipboard_view.runCopy();
		} else {
			if(window!= null) {
				MessageDialog.openError(window.getShell(), 
						Translator.getString("MESSAGEBOX_TITLE_ERROR"),
						Translator.getString("MSG_SELECT_VIEW_FIRST"));
			}
		}
	}


}
