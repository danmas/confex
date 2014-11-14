package net.confex.views;

import net.confex.application.ConfexPlugin;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public abstract class ConfexView extends ViewPart implements IConfexView {

	/**
	 * Set message for all application ConfexView 
	 * @param msg
	 */
	public void setApplicationStatusLine(String msg) { 
		try {
			IActionBars bars = getViewSite().getActionBars();
			ConfexPlugin.getDefault().setStatusLine(msg);
			bars.getStatusLineManager().setMessage(msg);
		} catch(Exception e) { 
			System.err.println("[NavigationView.contributeToActionBars()] "+e.getMessage());
		}
	}

	
	protected void contributeToActionBars() {
		try {
			IActionBars bars = getViewSite().getActionBars();
			fillLocalPullDown(bars.getMenuManager());
			fillLocalToolBar(bars.getToolBarManager());
			
			String s = ConfexPlugin.getDefault().getStatusLine();
			bars.getStatusLineManager().setMessage(s);
		} catch(Exception e) { 
			System.err.println("[NavigationView.contributeToActionBars()] "+e.getMessage());
		}
	}
	
	
	protected void fillLocalPullDown(IMenuManager manager) {}
	
    protected void fillLocalToolBar(IToolBarManager manager) {}
	
}
