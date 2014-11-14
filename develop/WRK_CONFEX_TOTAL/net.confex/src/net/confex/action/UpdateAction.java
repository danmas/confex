package net.confex.action;

import net.confex.application.ConfexPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.update.ui.UpdateJob;
import org.eclipse.update.ui.UpdateManagerUI;

public class UpdateAction extends Action implements IAction {
	  private IWorkbenchWindow window;

	  public UpdateAction(IWorkbenchWindow window) {
	    this.window = window;
	    setId("net.confex.action.newUpdates");
	    setText("&Update...");
	    setToolTipText("Search for updates to Confex");
	    //setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
	    //		ConfexPlugin.ID, "icons/if_node.gif"));
	    //window.getWorkbench().getHelpSystem().setHelp(this,
	    //    "net.confex.action.updates");
	  }
	  public void run() {
	    BusyIndicator.showWhile(window.getShell().getDisplay(),
	        new Runnable() {
	          public void run() {
	        	  //UpdateManagerUI.openConfigurationManager(window.getShell());
	        	  UpdateManagerUI.openInstaller(window.getShell());
	        	  /*
	            UpdateJob job = new UpdateJob(
	                "Searching for updates", false, false);
	            UpdateManagerUI.openInstaller(window.getShell(), job);
	            */
	          }
	        });
	  }
	}