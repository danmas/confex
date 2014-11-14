package net.confex.schema.model;

import net.confex.schema.part.NodeElementPart;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.widgets.Display;

public class TestJob {

	
	static boolean started = false;
	static NodeElement element;
	static Display display;
	static NodeElementPart part;
	public static void stop() {
		started = false;
	}
	
	public static void test(NodeElement element_,Display display_,NodeElementPart part_) {
 	   started = true;
 	  element = element_;
 	 display = display_;
 	part = part_;
 	
 /* 	  
 	display.asyncExec(new Runnable() {
        	public void run() {
            	Job job = new Job("Long Running Job") {
          		  	protected IStatus run(IProgressMonitor monitor) {
	          			final IProgressMonitor m = monitor;  
	        	           	if(monitor==null) {
	      	        	    System.err.println("monitor==null");
	      	        	    return Status.OK_STATUS;
	      	        	}
	          		    //monitor.beginTask("Asinchrous running:", 100);
	              	  System.out.println("I'm alive!");
	            	  //Display.getDefault().wait(1000l);
	            	  //sleep();
	              	  if(element.getText().equals("RUNING"))
	              		  element.setText("STOPED");
	              	  else
	              		  element.setText("RUNING");
	              	part.CommitDirectEditChanges(element);
	          		    return Status.OK_STATUS;
          		  	}
        		};
       		job.setUser(true);
       		job.schedule();    
       		
      	  job.addJobChangeListener(new JobChangeAdapter() {
  	        public void done(IJobChangeEvent event) {
  	        if (event.getResult().isOK())
  	        	System.out.println("Job completed successfully");
  	           else
  	        	   System.err.println("Job did not complete successfully");
  	        }
  	     });
  	  job.setSystem(false);
       job.schedule(); // start as soon as possible
        	}
        });
*/
	
 	 
    final Job job = new Job("Long Running Job") {
        protected IStatus run(IProgressMonitor monitor) {
           try {
              //while(started) {
                 // do some work
                 // ...
            	  System.out.println("I'm alive!");
            	  //Display.getDefault().wait(1000l);
            	  //sleep();
              	  if(element.getText().equals("RUNING"))
              		  element.setText("STOPED");
              	  else
              		  element.setText("RUNING");
              	part.CommitDirectEditChanges(element);
              	
              if (monitor.isCanceled()) return Status.CANCEL_STATUS;
             //}
              return Status.OK_STATUS;
           } catch (Exception e) {
        	   System.err.println(e.getMessage());
        	   return Status.OK_STATUS;
          } finally {
        	  if(started)
        		  schedule(1000); // start again in an 1 second
           }
        }
     };
	
	  job.addJobChangeListener(new JobChangeAdapter() {
	        public void done(IJobChangeEvent event) {
	        if (event.getResult().isOK())
	        	System.out.println("Job completed successfully");
	           else
	        	   System.err.println("Job did not complete successfully");
	        }
	     });
	  job.setSystem(false);
     job.schedule(); // start as soon as possible
 	   
	}
	
	
	public static void test1() {
		Job job = new Job("My First Job") {
		     protected IStatus run(IProgressMonitor monitor) {
		           System.out.println("Hello World (from a background job)");
		           return Status.OK_STATUS;
		        }
		     };
		  job.setPriority(Job.SHORT);
		  job.schedule(); // start as soon as possible
		  
	}
	
	
}
