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
import net.confex.utils.ImageResource;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;


public class OpenViewAction extends Action {
	
	private static final String ID = "net.confex.action.OpenViewAction";
	
	private final IWorkbenchWindow window;
	private int instanceNum = 0;
	private final String viewId;

	
	public OpenViewAction(IWorkbenchWindow window, String label, String viewId, String icon_file_name) {
		//if(window.getActivePage()!=null) {
		//	IWorkbenchPart part = window.getActivePage().getActivePart();
		//}
		this.window = window; 
		this.viewId = viewId;
        setText(label);
        // The id is used to refer to the action in a menu or toolbar
		setId(ID); 
        // Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(ID); 
		setImageDescriptor(ImageResource.getImageDescriptor(icon_file_name));
	}

	
	public void run() {
		if(window != null) {	
			try {
				window.getActivePage().showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), 
						Translator.getString("MESSAGEBOX_TITLE_ERROR"),
						Translator.getString("MESSAGE_ERR_OPEN_VIEW") + e.getMessage());
			}
		}
	}
	
	
}
