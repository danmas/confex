/*******************************************************************************
 * Copyright (c) 2006,2007 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package net.confex.application;


import java.io.IOException;

import net.confex.Constants;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.preferences.ScopedPreferenceStore;



/**
 * This class controls all aspects of the application's execution
 */
public class ConfexApplication implements IApplication  { 
	
	
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Application ConfexApplication started.");
		
		Display display = PlatformUI.createDisplay();

		String[] s = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		for( int i = 0 ; i < s.length ; i++){
			System.out.println(" args = "+s[i]);
			if( "-edit_mode_disable".equals(s[i])){
				boolean edit_mode = false;
				ConfexPlugin.getDefault().setEnableEditMode(edit_mode);
				// -- сохраняем свойство EDIT_MODE
				ScopedPreferenceStore preferences = new ScopedPreferenceStore(new ConfigurationScope(),
						ConfexPlugin.ID);
				preferences.setValue(Constants.EDIT_MODE, edit_mode?"true":"false");
				try {
					preferences.save();
				} catch (IOException e) {System.err.println(e.getMessage());}
			}
			if( "-edit_mode_enable".equals(s[i])){
				boolean edit_mode = true;
				ConfexPlugin.getDefault().setEnableEditMode(edit_mode);
				// -- сохраняем свойство EDIT_MODE
				ScopedPreferenceStore preferences = new ScopedPreferenceStore(new ConfigurationScope(),
						ConfexPlugin.ID);
				preferences.setValue(Constants.EDIT_MODE, edit_mode?"true":"false");
				try {
					preferences.save();
				} catch (IOException e) {System.err.println(e.getMessage());}
			}
			if( "-start_confex".equals(s[i])){
				//FIXME  do check index and values != null for s[i+1]
				ConfexPlugin.getDefault().setStartConfex(s[i+1]);
			}
		}
		
		//-- add console into ConsoleManager
		ConsolePlugin.getDefault().getConsoleManager()
			.addConsoles(new IConsole[] { ConfexConsole.getInstanse() });

		//Status status = new Status(IStatus.INFO,ConfexPlugin.ID,
		//					"Application ConfexApplication started.");
		//ConfexPlugin.getDefault().getLog().log(status);
		
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display
					, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	
	public void stop() {
	}

}


