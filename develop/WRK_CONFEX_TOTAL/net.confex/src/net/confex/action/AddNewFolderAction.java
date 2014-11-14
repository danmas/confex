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

import net.confex.tree.FolderNode;
import net.confex.views.FolderView;

public class AddNewFolderAction extends TranslatableAction {
	
    private FolderView folderView;

    protected String getID() { return "net.confex.tree.AddNewFolderAction"; }
    protected String getTextKey() { return "ACTION_ADD_NEW_FOLDER"; }
    protected String getIconFileName() { return FolderNode.getDefaultImageName(); }
    
    public AddNewFolderAction(FolderView folderView) {
    	super();
		this.folderView = folderView;
	}
    
    
	public void run() {
		folderView.runNewFolder();
	}

	
}