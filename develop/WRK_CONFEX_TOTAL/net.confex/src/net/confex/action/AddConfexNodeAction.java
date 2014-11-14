package net.confex.action;

import net.confex.tree.ConfexNode;
import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;

public class AddConfexNodeAction extends TranslatableAction {

	private NavigationView navigation_view;

	protected String getID() { return "net.confex.tree.AddConfexNodeAction"; }
	protected String getTextKey() {	return "ACTION_ADD_CONFEXVIEWNODE"; }
	protected String getIconFileName() { return ConfexNode.getDefaultImageName();}
	
	
	public AddConfexNodeAction(NavigationView navigation_view) {
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
			// -- Запретить в случае выделения более чем одного
			if (!navigation_view.cantDoWhenNotOneSelected(ss))
				return;
			obj = ss.getFirstElement();
		}

		ConfexNode tree_node = new ConfexNode(navigation_view.getConfigTree(),
				navigation_view);

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

