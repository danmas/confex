package net.confex.db.actions;

import net.confex.db.tree.HtmlSqlTableTreeObject;
import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Действие "добавить HTP SQL таблицу"
 */
public class AddHtpSqlTableTreeNodeAction implements IViewActionDelegate {
    private NavigationView navigation_view;

    
    public void init(IViewPart view) {
    	if(view instanceof NavigationView) {
    		navigation_view = (NavigationView)view;
    	} else {
    		navigation_view = null;
    	}
    }
    
    	
    public void selectionChanged(IAction action, ISelection selection) {
    }
   
	//public AddHtpSqlTableTreeNodeAction() { //NavigationView navigation_view) {
		//this.navigation_view = navigation_view;

		//setText("добавить HTP SQL таблицу");
		//setImageDescriptor(ImageResource
		//		.getImageDescriptor(new HtmlSqlTableTreeObject(navigation_view.getConfigTree()).getDefaultImage()));
	//}
	
	
    public void run(IAction action) {
    	if(navigation_view==null)
    		return;
    	
		ISelection selection = navigation_view.getTreeViewer().getSelection();
    	TreeSelection ss = (TreeSelection)selection;
    	
    	//-- Запретить в случае выделения более чем одного
    	if(!navigation_view.cantDoWhenNotOneSelected(ss))
    		return;
    	
    	Object obj = ss.getFirstElement();
    	
    	HtmlSqlTableTreeObject tree_node = new HtmlSqlTableTreeObject(
    		navigation_view.getConfigTree(),navigation_view);
    	
    	if(obj instanceof ITreeNode) {
    		((ITreeNode)obj).addChild(tree_node);
    		tree_node.openPropertyDialog(navigation_view,navigation_view.getSite().getShell(),true);
    		navigation_view.getTreeViewer().refresh(obj);
    		navigation_view.getTreeViewer().setExpandedState(obj, true);
    		//navigation_view.setDirty(true);
    	}
	}
	
	
    	
    
}
    
