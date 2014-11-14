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

import net.confex.utils.ImageResource;
import net.confex.views.FolderView;

import org.eclipse.jface.action.Action;

/**
 * Переход на каталог выше
 * 
 * @author Roman_Eremeev
 *
 */

public class GoUpFolderAction extends Action {
	
    private FolderView folderView;

    public GoUpFolderAction(FolderView folderView) {
		this.folderView = folderView;
		
		setText("Up");
		setImageDescriptor(ImageResource.getImageDescriptor(
				ImageResource.ECL_IMG_UP_NAV));
	}
    
    
	public void run() {
		folderView.runUpNav();
	}

}
