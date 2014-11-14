package net.confex.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import net.confex.translations.Translator;
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

public class RunAsincBatchAction  extends TranslatableAction {
	
    private NavigationView navigation_view;

    protected String getID() { return "net.confex.action.RunAsincAction"; }
    protected String getTextKey() { return "ACTION_RUN_ASINC_BATCH"; }
    protected String getIconFileName() { return "clcl16/nav_go.gif"; }
    
    
    public RunAsincBatchAction(NavigationView navigation_view) {
		super();
		this.navigation_view = navigation_view;
	}
    
    public void run3() {
    	System.err.println("new Job(\"Working\")");
    	Job job = new Job("Working") {
    		  protected IStatus run(IProgressMonitor monitor) {
      			final IProgressMonitor m = monitor;  
    		    monitor.beginTask("Calculating things", 100);
    		    //for (int i = 0; i < 10; i++) {
    		      //monitor.worked(1);
    		      //monitor.subTask("doing " + i);
    		      if (monitor.isCanceled())
    		        return Status.CANCEL_STATUS;
    		      	try {
    	    		    //for (int j = 0; j < 2000000; j++) {
    	    		    //	double d = java.lang.Math.pow(java.lang.Math.sqrt(j*1.1), 3);
    	    		    //}	
    	    		    Display d = navigation_view.getSite().getShell().getDisplay();
    	    	        d.asyncExec(new Runnable() {
    	    	        	public void run() {
    	    	        		//run0(m);  
    	    	        	}
    	    	        });
	    		    } catch(Exception e) {
	    		    	System.err.println(e.getMessage());
	    		    }
    		    //}
    		    monitor.done();
    		    return Status.OK_STATUS;
    		  }
    		};
    		job.setUser(true);
    		job.schedule();
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
    		            	Job job = new Job(Translator.getString("MSG_RUNNIG_IN_BACKGROUND")) {
    		          		  	protected IStatus run(IProgressMonitor monitor) {
	    		          			final IProgressMonitor m = monitor;  
	    		        	           	if(monitor==null) {
	    		      	        	    System.err.println("monitor==null");
	    		      	        	    return Status.OK_STATUS;
	    		      	        	}
	    		          		    monitor.beginTask("Asinchrous running action", 100);
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
    
    
	@SuppressWarnings("unchecked")
	protected void run0(final IProgressMonitor monitor,ITreeNode obj) {
		try {
			obj.runBatch(navigation_view,monitor);
	    } catch(Exception e) {
	    	System.err.println(e.getMessage());
	    }
	}

	
    public void run4() {
    	if(navigation_view==null) {
    	    System.err.println("navigation_view==null");
    	    return;
    	}
    	/*
	    Display d = navigation_view.getSite().getShell().getDisplay();
        d.asyncExec(new Runnable() {
        	public void run() {
        		run0(null);  
        	}
        });
        return;
        */
	    ProgressMonitorDialog pd = new ProgressMonitorDialog(navigation_view.getShell());
	    //System.err.println("e.getMessage()");
	    try {
	    	pd.run(true, true,
	      new IRunnableWithProgress() {
	        public void run(IProgressMonitor monitor) throws
	            InvocationTargetException, InterruptedException {
	        	final IProgressMonitor m = monitor;
	           	if(monitor==null) {
	        	    System.err.println("monitor==null");
	        	    return;
	        	}
	          monitor.beginTask("Long running action", 100);
	          //for (int i = 0; i < 10; i++) {
	            if (monitor.isCanceled())
	              return;
	            monitor.subTask("working on step ");
        	    Display d = navigation_view.getSite().getShell().getDisplay();
	            d.asyncExec(new Runnable() {
	            	public void run() {
	            		//run0(m);  
	            	}
	            });
	            monitor.worked(10);
	            //sleep(1000);
	          //}
	          monitor.done();
	        }
	      });
	    } catch(Exception e) {
	    	System.err.println(e.getMessage());
	    }
    }
    

    public void run2() {
    	if(navigation_view==null) {
    	    System.err.println("navigation_view==null");
    	    return;
    	}
	    new Thread(new Runnable() {
	        public void run() {
	           //while (true) {
	              try { Thread.sleep(1000); } catch (Exception e) { }
	              Display.getDefault().asyncExec(new Runnable() {
	                 public void run() {
	                    //run0(null);
	                 }
	              });
	           //}
	        }
	     }).start();
    }
    
	
}
