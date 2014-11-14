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
package net.confex.directedit;

import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.swt.events.ShellListener;


public interface IPropertyDialog extends ShellListener {

	public void setTreeNode(ITreeNode element); 
	
	//public void setEditPart(NodeElementPart edit_part );

	public void dispose();
	
	public void show();
	
	public void setView(NavigationView view);

	public void createSShell();
	
	public void setNewFlg(boolean new_flg);
}
