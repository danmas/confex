/*******************************************************************************
 * Copyright (c) 2006 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package net.confex.application;

import net.confex.views.CompositeFormView;
import net.confex.views.NavigationView;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.console.IConsoleConstants;


public class Perspective implements IPerspectiveFactory {
	
	
    /**
     * Creates the default initial layout for this plugin. This method fufills
     * the contract for the IPerspectiveFactory interface
     * 
     * @param IPageLayout
     */
    public void createInitialLayout(IPageLayout layout) {
		if(ConfexPlugin.getDefault().isEnableEditMode()) {
	        defineActionsEditMode(layout);
	        defineLayoutEditMode(layout);
		} else {
			defineActions(layout);
			defineLayout(layout);
		}
    }


    private void defineActions(IPageLayout layout) {
    	
    }
    
    
    /**
     * Define the actions and views you want to make available from the menus.
     * 
     * @param IPageLayout
     */
    private void defineActionsEditMode(IPageLayout layout) {
        // You can add "new" wizards" here if you want, but none seem applicable
        // in the case of this plugin

        // Grab the list of all available views defined in the constants class
        //List views = SqlexplorerViewConstants.getInstance().getFullViewList();
        //Iterator iterator = views.iterator();

        // Iterate through those views and add them to the Show Views menu.
        //while (iterator.hasNext())
        //    layout.addShowViewShortcut((String) iterator.next());
        
    	layout.addShowViewShortcut(NavigationView.ID);
}


    
    /**
     * Controls the physical default layout of the perspective
     * 
     * @param IPageLayout
     */
    private void defineLayoutEditMode(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		IFolderLayout folder0 = layout.createFolder("left", IPageLayout.LEFT, 0.5f, editorArea);
		folder0.addPlaceholder(NavigationView.ID + ":*");
		
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(CompositeFormView.VIEW_ID + ":*");
        
        IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.35f, layout.getEditorArea());
		folder.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW + ":*");
        bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
    }

    
    /**
     * Controls the physical default layout of the perspective
     * 
     * @param IPageLayout
     */
    private void defineLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(NavigationView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		IPlaceholderFolderLayout folder0 = layout.getFolderForView(NavigationView.ID);
		folder0.addPlaceholder(NavigationView.ID + ":*");
		
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(CompositeFormView.VIEW_ID + ":*");
		
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
        
    }

    
}
