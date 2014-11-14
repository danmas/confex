package net.confex.action;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import net.confex.application.ConfexPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.update.search.BackLevelFilter;
import org.eclipse.update.search.EnvironmentFilter;
import org.eclipse.update.search.UpdateSearchRequest;
import org.eclipse.update.search.UpdateSearchScope;
import org.eclipse.update.ui.UpdateJob;
import org.eclipse.update.ui.UpdateManagerUI;

public class AddExtensionsAction extends Action implements IAction {
	  private IWorkbenchWindow window;


	  public AddExtensionsAction(IWorkbenchWindow window) {
	    this.window = window;
	    setId("net.confex.action.newExtensions");
	    setText("&Search for new confex extensions...");
	    setToolTipText("Search for new extensions for Confex");
	    //setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
	    //		ConfexPlugin.ID, "icons/if_node.gif"));
	    //window.getWorkbench().getHelpSystem().setHelp(this,
	    //   "net.confex.action.updates");
	  }
	  public void run() {
	    BusyIndicator.showWhile(window.getShell().getDisplay(),
	        new Runnable() {
	          public void run() {
	            UpdateJob job = new UpdateJob(
	                "Search for new extensions", getSearchRequest());
	            UpdateManagerUI.openInstaller(window.getShell(), job);
	          }
	        });
	  }

	  private UpdateSearchRequest getSearchRequest() {
	    UpdateSearchRequest result = new UpdateSearchRequest(
	        UpdateSearchRequest.createDefaultSiteSearchCategory(),
	        new UpdateSearchScope());
	    result.addFilter(new BackLevelFilter());
	    result.addFilter(new EnvironmentFilter());
	    UpdateSearchScope scope = new UpdateSearchScope();
	    
	    try {
	      String homeBase = System.getProperty("confex.homebase",
	    		  "http://confex.sourceforge.net/update");
          //"http://eclipsercp.org/updates");
	      URL url = new URL(homeBase);
	      scope.addSearchSite("Confex site", url, null);
	      //File f = new File("G:/WRK/WRK_CONFEX_NEW/net.confex.site");
	      //scope.addSearchSite("Confex site", f.toURL(), null);
	      
	    } catch (MalformedURLException e) {
	      // skip bad URLs
	    }
	    
	    result.setScope(scope);
	    return result;
	  }
	}