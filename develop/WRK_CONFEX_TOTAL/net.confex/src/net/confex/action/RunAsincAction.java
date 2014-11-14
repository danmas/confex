package net.confex.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;

public class RunAsincAction  extends TranslatableAction {
	
    private NavigationView navigation_view;

    protected String getID() { return "net.confex.action.RunAsincAction"; }
    protected String getTextKey() { return "ACTION_RUN_ASINC"; }
    protected String getIconFileName() { return "clcl16/nav_go.gif"; }
    
    
    public RunAsincAction(NavigationView navigation_view) {
		super();
		this.navigation_view = navigation_view;
	}
    

    public void run() {
		ISelection selection = navigation_view.getTreeViewer().getSelection();
    	TreeSelection ss = (TreeSelection)selection;
    	
    	//--  цикл по всем выделенным
		for(Iterator iter= ss.iterator();iter.hasNext();) {
			final ITreeNode obj = (ITreeNode)iter.next(); 	
    		    try {
    			    Display d = navigation_view.getSite().getShell().getDisplay();
    		        d.asyncExec(new Runnable() {
    		        	public void run() {
    		            	Job job = new Job("Asinchrous running:"+obj.getName()) {
    		          		  	protected IStatus run(IProgressMonitor monitor) {
	    		          			final IProgressMonitor m = monitor;  
	    		        	           	if(monitor==null) {
	    		      	        	    System.err.println("monitor==null");
	    		      	        	    return Status.OK_STATUS;
	    		      	        	}
	    		          		    monitor.beginTask("Asinchrous running:"+obj.getName(), 100);
	    		          		    run0(m,obj);  
	    		          		    return Status.OK_STATUS;
    		          		  	}
    		        		};
    		       		job.setUser(true);
    		       		job.schedule();    	
    		        	}
    		        });
    		    } catch(Exception e) {
    		    	System.err.println(e.getMessage());
    		    }
		}
    }
    
    
	protected void run0(final IProgressMonitor monitor,ITreeNode obj) {
		try {
			obj.run(navigation_view,monitor);
	    } catch(Exception e) {
	    	System.err.println(e.getMessage());
	    }
	}
    
    
	/*
	@SuppressWarnings("unchecked")
	protected void run0() {
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
	}*/
	
}
