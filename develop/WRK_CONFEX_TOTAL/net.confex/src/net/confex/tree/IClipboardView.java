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
package net.confex.tree;

import org.eclipse.swt.widgets.Shell;

/**
 * Вид с функциями буфера обмена
 * 
 * @author Roman_Eremeev
 *
 */
public interface IClipboardView {

	/**
	 * 
	 * @return true if View can be editable
	 */
	public boolean isEditable();

	public Shell getShell();
	
	public void runCopy();
	
	public void runCopyWithoutChild();
	
	public void runCut();
	
	public void runPaste();
	
	/**
	 * Copy to file and clipboard
	 */
	public void runCopyFile();
	
	/**
	 * Paste from file
	 */
	public void runPasteFile();
	
	/**
	 * if with_question flag is set - ask user before deleting
	 * @param notify
	 */
	public void runDelete(boolean with_question);
	
	public void runProperty();
	
	/**
	 * Action open src editors for all selected nodes
	 */
	public void openSrcEditors();
	
}
