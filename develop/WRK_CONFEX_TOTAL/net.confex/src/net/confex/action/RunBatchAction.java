package net.confex.action;

import java.util.Iterator;

import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;

public class RunBatchAction  extends TranslatableAction {
	
    private NavigationView navigation_view;

    protected String getID() { return "net.confex.action.RunChildrenAction"; }
    protected String getTextKey() { return "ACTION_RUN_BATCH"; }
    protected String getIconFileName() { return "clcl16/nav_go.gif"; }
    
    
    public RunBatchAction(NavigationView navigation_view) {
		super();
		this.navigation_view = navigation_view;
	}
    
    
	@SuppressWarnings("unchecked")
	public void run() {
    	//Thread t = new Thread() {
    	//	public void run() {
				ISelection selection = navigation_view.getTreeViewer().getSelection();
		    	TreeSelection ss = (TreeSelection)selection;
		    	//--  цикл по всем выделенным
				for(Iterator iter= ss.iterator();iter.hasNext();) {
					Object obj = iter.next(); 	
		    	
			    	if(obj instanceof ITreeNode) {
			    		((ITreeNode)obj).runBatch(navigation_view);
			    		navigation_view.getTreeViewer().refresh(obj);
			    		//navigation_view.getTreeViewer().setExpandedState(obj, true);
			    	}
				}
    		}
    	//};
    	//t.start();
	//}
	
}
