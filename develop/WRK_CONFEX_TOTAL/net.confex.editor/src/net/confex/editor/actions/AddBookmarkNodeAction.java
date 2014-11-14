package net.confex.editor.actions;


import net.confex.directedit.IPropertyDialog;
import net.confex.editor.tree.BookmarkNode;
import net.confex.editor.tree.BookmarkNodePropertyDialog;
import net.confex.editor.tree.MarkerUtil;
import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class AddBookmarkNodeAction implements IViewActionDelegate {
	private NavigationView navigation_view;

	
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
		if (navigation_view == null)
			return;
		ISelection selection = navigation_view.getTreeViewer().getSelection();
		TreeSelection ss = (TreeSelection) selection;

		// -- Запретить в случае выделения более чем одного
		if (!navigation_view.cantDoWhenNotOneSelected(ss))
			return;

		Object obj = ss.getFirstElement();

		BookmarkNode tree_node = new BookmarkNode(
			navigation_view.getConfigTree(),navigation_view);

		IMarker marker = MarkerUtil.makeBookmark(tree_node);
		
		if(marker==null) {
			MessageDialog.openError(navigation_view.getSite().getShell(), "Error", 
			"Can't create marker!"); 
			//-- "Не удалось создать метку!"); 
			return;
		}
		
		if (obj instanceof ITreeNode) {
			((ITreeNode) obj).addChild(tree_node);
			tree_node.openPropertyDialog(navigation_view, navigation_view
					.getSite().getShell(),true);
			navigation_view.getTreeViewer().refresh(obj);
			navigation_view.getTreeViewer().setExpandedState(obj, true);
			//navigation_view.setDirty(true);

			IPropertyDialog property_dialog = tree_node.getPropertyDialog();
			if (property_dialog instanceof BookmarkNodePropertyDialog) {
				((BookmarkNodePropertyDialog) property_dialog)
						.setMarker(marker);
			}
		}
	}

	
}
