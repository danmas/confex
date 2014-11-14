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

import java.util.Iterator;

import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;


public class RunAction extends TranslatableAction {
	
    private NavigationView navigation_view;

    protected String getID() { return "net.confex.action.RunAction"; }
    protected String getTextKey() { return "ACTION_RUN"; }
    protected String getIconFileName() { return "clcl16/nav_go.gif"; }
    
    
    public RunAction(NavigationView navigation_view) {
		super();
		this.navigation_view = navigation_view;
	}
    
    
	@SuppressWarnings("unchecked")
	public void run() {
		ISelection selection = navigation_view.getTreeViewer().getSelection();
    	TreeSelection ss = (TreeSelection)selection;
    	
    	//--  цикл по всем выделенным
		for(Iterator iter= ss.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
    	
	    	if(obj instanceof ITreeNode) {
	    		((ITreeNode)obj).run(navigation_view);
	    		navigation_view.getTreeViewer().refresh(obj);
	    		navigation_view.getTreeViewer().setExpandedState(obj, true);
	    	}
		}
	}
	
}
