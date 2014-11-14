package net.confex.customxml;
 
import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class AddCustomXmlNodeAction implements IViewActionDelegate {

		private NavigationView navigation_view;

		protected String getID() { return "net.confex.customxml.AddCustomXmlNodeAction"; }
		protected String getTextKey() {	return "ACTION_AddCustomXmlNodeAction"; }
		protected String getIconFileName() { return CustomXmlNode.getDefaultImageName();}
		
		
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
	    	
	    	CustomXmlNode tree_node = new CustomXmlNode(
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
	    	TreeSelection ss = (TreeSelection)selection;
	    	
	    	Object obj = null; 
	    	
	    	if(ss.size()==0) {
	    		obj = navigation_view.getConfigTree().getRoot();
	    	} else {
		    	//-- Запретить в случае выделения более чем одного
		    	if(!navigation_view.cantDoWhenNotOneSelected(ss))
		    		return;
		    	obj = ss.getFirstElement();
	    	}
	    	
	    	CustomXmlNode tree_node = 
	    		new CustomXmlNode(navigation_view.getConfigTree(),navigation_view);
	    	
	    	if(obj instanceof ITreeNode) {
	    		((ITreeNode)obj).addChild(tree_node);
	    		tree_node.openPropertyDialog(navigation_view,navigation_view.getSite().getShell(),true);
	    		navigation_view.getTreeViewer().refresh(obj);
	    		navigation_view.getTreeViewer().setExpandedState(obj, true);
	    		//navigation_view.setDirty(true);
	    	}
		}
}
