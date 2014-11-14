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
package net.confex.translations;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class Logger {
	//private static Shell shell;
	
	
	Logger() {
	}
	
	
    public static void informationDialog(Shell shell, String key, String additional_str) { 
    	MessageDialog.openInformation(shell,
    		Translator.getString("MESSAGEBOX_TITLE_INFORMATION"),
    		Translator.getString(key)+additional_str);
    }


    public static void errorDialog(Shell shell, String key, String additional_str) { 
    	MessageDialog.openError(shell,
    		Translator.getString("MESSAGEBOX_TITLE_ERROR"),
    		Translator.getString(key)+additional_str);
    }


    public static boolean questionDialog(Shell shell, 
    		String title_key, 
    		String message_key, 
    		String additional_str) {
    	return MessageDialog.openQuestion(shell,
    		Translator.getString(title_key),
    		Translator.getString(message_key)+additional_str);
    }
    
				
	//public static void setShell(Shell shell) {
	//	Logger.shell = shell;
	//}

}
