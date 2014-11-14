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
package net.confex.editor.actions;

import net.confex.action.TranslatableAction;
import net.confex.editor.tree.JavaNode;
import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;

/**
 * Действие "добавить Узел Java класса"
 */
public class AddJavaNodeAction extends TranslatableAction implements IViewActionDelegate  {

	private NavigationView navigation_view;
	
	protected String getID() {return "net.confex.tree.AddJavaNodeAction";}
	protected String getTextKey() {return "ACTION_ADD_JAVANODE";}
	protected String getIconFileName() {return JavaNode.getDefaultImageName();}

	
	public AddJavaNodeAction() {
		super();
	}

	
	public AddJavaNodeAction(ViewPart view_part) {
		if(view_part instanceof NavigationView) {
			this.navigation_view = (NavigationView) view_part;
		}
	}
	
	
	public void init(IViewPart view) {
		if (view instanceof NavigationView) {
			navigation_view = (NavigationView) view;
		} else {
			navigation_view = null;
		}
	}
	
	
	public void selectionChanged(IAction action, ISelection selection) {
	}

	
    public void run(IAction action) {
    	if(navigation_view==null)
    		return;
    	
		ISelection selection = navigation_view.getTreeViewer().getSelection();
    	TreeSelection ss = (TreeSelection)selection;

    	//-- Запретить в случае выделения более чем одного
    	if(!navigation_view.cantDoWhenNotOneSelected(ss))
    		return;
    	
    	Object obj = ss.getFirstElement();
    	
    	JavaNode tree_node = new JavaNode(
        		navigation_view.getConfigTree(),navigation_view);
    	
    	if(obj instanceof ITreeNode) {
    		((ITreeNode)obj).addChild(tree_node);
    		tree_node.openPropertyDialog(navigation_view
    				,navigation_view.getSite().getShell(),true);
    		navigation_view.getTreeViewer().refresh(obj);
    		navigation_view.getTreeViewer().setExpandedState(obj, true);
    		//navigation_view.setDirty(true);
    	}
	}
	
	
	public void run() {
		ISelection selection = navigation_view.getTreeViewer().getSelection();
		TreeSelection ss = (TreeSelection) selection;

		Object obj = null;

		if (ss.size() == 0) {
			obj = navigation_view.getConfigTree().getRoot();
		} else {
			// -- Запретить в случае выделения более чем одного
			if (!navigation_view.cantDoWhenNotOneSelected(ss))
				return;
			obj = ss.getFirstElement();
		}
		JavaNode tree_node = new JavaNode(navigation_view.getConfigTree(),
				navigation_view);

		if (obj instanceof ITreeNode) {
			((ITreeNode) obj).addChild(tree_node);
			tree_node.openPropertyDialog(navigation_view, navigation_view
					.getSite().getShell(), true);
			navigation_view.getTreeViewer().refresh(obj);
			navigation_view.getTreeViewer().setExpandedState(obj, true);
		}
	}
	
	
}
