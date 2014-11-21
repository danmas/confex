/*******************************************************************************
 * Copyright (c) 2006,2007 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *     
 *     
 * 0.9.17 Roman Eremeev Version with ENABLE EDIT MODE had been prepared
 *   
 *******************************************************************************/
package net.confex.application;


import net.confex.Constants;
import net.confex.translations.Translator;
 
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ConfexPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static ConfexPlugin plugin;
	public static final String ID = "net.confex";
	
	//-- confex file to start on rcp startup
	private String start_confex = null; 
	
	/*
	 * Возможны два режима редактирования приложения
	 * с разрешением редактирования конфексного дерева (enable_edit_mode = true)
	 * и без (enable_edit_mode = false)
	 * 
	 * В дизайн моде в каждом виде навигатора может быть выключен пользовательский режим
	 * и тогда дерево можно изменять.
	 * 
	 * Управление разрешением на редактирование открывается из панели свойств
	 * 
	 */
	private static boolean  enable_edit_mode = true;  //--
	
	public static boolean isEnableEditMode() { return enable_edit_mode; }
	public static void setEnableEditMode(boolean enable_edit_mode_) { 
		enable_edit_mode=enable_edit_mode_; 
	}

	
	/**
	 * The constructor.
	 */
	public ConfexPlugin() {
		plugin = this;
	}

	
	/**
	 * This method is called upon plug-in activation
	 */	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		if(Display.getCurrent()!=null) {
			ConsolePlugin.getDefault().getConsoleManager()
				.addConsoles(new IConsole[] { ConfexConsole.getInstanse() });
		}
		
		//-- устанавливаем локализацию при запуске плагина
        IEclipsePreferences preferences = 
        	new	ConfigurationScope().getNode(ConfexPlugin.ID);
        
        String lang = preferences.get(Constants.PREFERED_LANG, "en");
    	Translator.setLang(lang);

    	//-- устанавливаем допустимость режима редактирования
        boolean edit_mode = preferences.get(Constants.EDIT_MODE, "true").equals("true")?true:false;
        setEnableEditMode(edit_mode);
        
        IPath p = getStateLocation();
        
        //-- get Preferences
        Preferences pref = getPluginPreferences();
        
		System.out.println("Plugin ConfexPlugin started. Start location is "+p.toString());
	}
                                                
	
	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		//-- чистим все списки переводчика 
		Translator.removeAllMessages();
		Translator.removeAllTranslatables();
	}

	
	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance.
	 */
	public static ConfexPlugin getDefault() {
		return plugin;
	}

	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("ConfEx", path);
	}
	
	/**
	 *  Status line's mesaage common for all views 
	 */
	private String statusLine = "Status line ready";
	
	/**
	 * @return the statusLine
	 */
	public String getStatusLine() {
		return statusLine;
	}
	
	
	/**
	 * @param statusLine the statusLine to set
	 */
	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}
	/**
	 * @return the start_confex
	 */
	public String getStartConfex() {
		return start_confex;
	}
	/**
	 * @param start_confex the start_confex to set
	 */
	public void setStartConfex(String start_confex) {
		this.start_confex = start_confex;
	}
	
}
