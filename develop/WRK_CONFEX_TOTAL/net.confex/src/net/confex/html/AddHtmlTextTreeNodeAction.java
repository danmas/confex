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
package net.confex.html;

import net.confex.action.TranslatableAction;
import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;


/**
 * Действие "добавить HTML text"
 */

public class AddHtmlTextTreeNodeAction extends TranslatableAction {

	private NavigationView navigation_view;

	protected String getID() { return "net.confex.html.AddHtmlTextTreeNodeAction"; }
	protected String getTextKey() {	return "ACTION_ADD_HTMLTEXTNODE"; }
	protected String getIconFileName() { return HtmlTextNode.getDefaultImageName();}
	
	
	public AddHtmlTextTreeNodeAction(NavigationView navigation_view) {
		super();
		this.navigation_view = navigation_view;
	}

	
	public void run() {
		ISelection selection = navigation_view.getTreeViewer().getSelection();
		TreeSelection ss = (TreeSelection) selection;

		Object obj = null;

		if (ss.size() == 0) {
			obj = navigation_view.getConfigTree().getRoot();
		} else {
			//-- Запретить в случае выделения более чем одного
			if (!navigation_view.cantDoWhenNotOneSelected(ss))
				return;
			obj = ss.getFirstElement();
		}

		HtmlTextNode tree_node = new HtmlTextNode(navigation_view
				.getConfigTree(), navigation_view);

		if (obj instanceof ITreeNode) {
			((ITreeNode) obj).addChild(tree_node);
			tree_node.openPropertyDialog(navigation_view, navigation_view
					.getSite().getShell(), true);
			navigation_view.getTreeViewer().refresh(obj);
			navigation_view.getTreeViewer().setExpandedState(obj, true);
			//navigation_view.setDirty(true);
		}
	}

}