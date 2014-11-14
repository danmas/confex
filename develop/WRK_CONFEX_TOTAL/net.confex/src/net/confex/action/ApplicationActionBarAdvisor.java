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
package net.confex.action;

import net.confex.application.ConfexPlugin;
import net.confex.translations.Translator;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;


/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 * 
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor /* implements ITranslatable */
{
    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.

	private IWorkbenchAction exitAction;
    private IWorkbenchAction newWindowAction;
    private IWorkbenchAction nextPerspectiveAction;
    private IWorkbenchAction customizePerspectivesAction;
    
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction showViewAction;
    private IContributionItem showViewMenu;
    private IContributionItem perspectivesMenu;
    
    private UpdateAction updateAction;
    private AddExtensionsAction addExtensionsAction;
    private ConfigurationManagerAction configurationManagerAction;
    
    private IWorkbenchAction openPreferencesAction;
    private IWorkbenchAction openPerspectivesAction;
    
    //private OpenViewAction openBrowserViewAction;
    private OpenNavigationViewAction openNavigationViewAction;
    private NewConfexFileAction newConfexFileAction;
    
    private SaveAction saveAction;
    private SaveAsAction saveAsAction;
    private PropertyAction propertyAction;
    private OpenSrcEditorsAction openSrcEditorsAction;
    
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
        //Translator.addTranslatable(this);
    }
     

    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        exitAction = ActionFactory.QUIT.create(window);
        //FIXME –азобратьс€ со стандартным механизмом локализации названи€ команд
        //exitAction.setText("¬ыход");
        register(exitAction);
        
        aboutAction = ActionFactory.ABOUT.create(window);
        //FIXME –азобратьс€ со стандартным механизмом локализации названи€ команд
        //aboutAction.setText("ќ программе");
        register(aboutAction);
        
    	updateAction = new UpdateAction(window);
        register(updateAction);
        
        configurationManagerAction = new ConfigurationManagerAction(window);
        register(configurationManagerAction);
        
        addExtensionsAction = new AddExtensionsAction(window);
        register(addExtensionsAction);
        
        showViewAction = ActionFactory.SHOW_VIEW_MENU.create(window);
        register(showViewAction);
        
        showViewMenu = ContributionItemFactory.VIEWS_SHORTLIST.create(window);

        
        perspectivesMenu = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
        
        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        register(newWindowAction);
        
        nextPerspectiveAction = ActionFactory.EDIT_ACTION_SETS .NEXT_PERSPECTIVE.create(window);
        register(nextPerspectiveAction);
        
        customizePerspectivesAction = ActionFactory.EDIT_ACTION_SETS.create(window);
        register(customizePerspectivesAction);
        
        openPreferencesAction = ActionFactory.PREFERENCES.create(window);
        register(openPreferencesAction);
        
        openPerspectivesAction = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
        register(openPerspectivesAction);
        
        //openBrowserViewAction = new OpenViewAction(window, "Open Browser View"
        //		, WebBrowserView.VIEW_ID,"eview16/internal_browser.gif");
        //register(openBrowserViewAction);
        
        newConfexFileAction = new NewConfexFileAction(window);
        register(newConfexFileAction);
        
        openNavigationViewAction = new OpenNavigationViewAction(window);
        register(openNavigationViewAction);

        saveAction = new SaveAction(window);
        register(saveAction);

        propertyAction = new PropertyAction(window);
        register(propertyAction);
        
        openSrcEditorsAction = new OpenSrcEditorsAction(window);
        register(openSrcEditorsAction);
        
        saveAsAction = new SaveAsAction(window);
        register(saveAsAction);
        
        //-- регистрируем стандартные действи€
        register(ActionFactory.COPY.create(window));

        register(ActionFactory.CUT.create(window));

        register(ActionFactory.PASTE.create(window));

        register(ActionFactory.DELETE.create(window));

   		register(ActionFactory.SAVE.create(window));
        
        register(ActionFactory.SAVE_AS.create(window));
    }
    
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager(Translator.getString("MENU_FILE"), "MY_MENU_FILE"); //IWorkbenchActionConstants.M_FILE);
        MenuManager windowMenu = new MenuManager("&"+Translator.getString("MENU_WINDOW"), "MY_MENU_WINDOW"); //IWorkbenchActionConstants.M_WINDOW);
        //MenuManager helpMenu = new MenuManager(Translator.getString("MENU_HELP"), "MY_MENU_HELP"); //IWorkbenchActionConstants.M_HELP);
        //MenuManager searchMenu = new MenuManager(Translator.getString("MENU_SEARCH"), IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu;
        if(ConfexPlugin.getDefault().isEnableEditMode()) {
        	helpMenu = new MenuManager(Translator.getString("MENU_HELP"), IWorkbenchActionConstants.M_HELP);
        	helpMenu.add(updateAction);
        	helpMenu.add(addExtensionsAction);
        	helpMenu.add(configurationManagerAction);
       } else {
        	helpMenu = new MenuManager(Translator.getString("MENU_HELP"), "MY_MENU_HELP"); //IWorkbenchActionConstants.M_HELP);
        }
        
        menuBar.removeAll();
        //if(ConfexPlugin.getDefault().isEnableEditMode()) {
        	menuBar.add(fileMenu);
        //}
        
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        if(ConfexPlugin.getDefault().isEnableEditMode()) {
        	menuBar.add(windowMenu);
        }
        menuBar.add(helpMenu);
        
        //fileMenu.removeAll(); // .setRemoveAllWhenShown(true);
        
        // File
        if(ConfexPlugin.getDefault().isEnableEditMode()) {
        	fileMenu.add(newConfexFileAction);
            fileMenu.add(openNavigationViewAction);
        }
        //fileMenu.add(openBrowserViewAction);
        
        fileMenu.add(new Separator());

        if(ConfexPlugin.getDefault().isEnableEditMode()) {
        	fileMenu.add(saveAction);
        	fileMenu.add(saveAsAction);
        }
        
        fileMenu.add(new Separator());

        if(ConfexPlugin.getDefault().isEnableEditMode())
        	fileMenu.add(newWindowAction);
        
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        
        // Window
        if(ConfexPlugin.getDefault().isEnableEditMode()) {
        	MenuManager showViewMenuMgr = new MenuManager("showView", "showView"); //$NON-NLS-1$
        	showViewMenuMgr.add(showViewMenu);
        	windowMenu.add(showViewMenuMgr);
        
	        MenuManager showPerspectivesMenu = new MenuManager("Perspectives", "Perspectives"); //$NON-NLS-1$
	        showPerspectivesMenu.add(perspectivesMenu);
	        windowMenu.add(showPerspectivesMenu);
        
	        ActionContributionItem openPerspectivesItem = new ActionContributionItem(openPerspectivesAction);
	        openPerspectivesItem.setVisible(!"carbon".equals(SWT.getPlatform())); //$NON-NLS-1$
	        windowMenu.add(openPerspectivesItem);
        
	        ActionContributionItem customizePerspectivesItem = new ActionContributionItem(customizePerspectivesAction);
	        customizePerspectivesItem.setVisible(!"carbon".equals(SWT.getPlatform())); //$NON-NLS-1$
	        windowMenu.add(customizePerspectivesItem);
        }

        /**/
        ActionContributionItem openPreferencesItem = new ActionContributionItem(openPreferencesAction);
        openPreferencesItem.setVisible(!"carbon".equals(SWT.getPlatform())); //$NON-NLS-1$
        windowMenu.add(openPreferencesItem);
        /**/
        
        // Help
        helpMenu.add(aboutAction);
    }
    
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        
        if(ConfexPlugin.getDefault().isEnableEditMode()) {
        	toolbar.add(newConfexFileAction);
        }
        toolbar.add(openNavigationViewAction);
        //toolbar.add(customizePerspectivesAction);
        
        //toolbar.add(saveAction);
    }


}
