/*
 * Created on Jul 22, 2004
 */
package net.confex.schema.action;

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;


/**
 * Provides a context menu for the schema diagram editor. A virtual cut and paste from the flow example
 * @author Daniel Lee
 */
public class SchemaContextMenuProvider extends ContextMenuProvider
{

	private ActionRegistry actionRegistry;

	
	/**
	 * Creates a new FlowContextMenuProvider assoicated with the given viewer
	 * and action registry.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param registry
	 *            the action registry
	 */
	public SchemaContextMenuProvider(EditPartViewer viewer, ActionRegistry registry)
	{
		super(viewer);
		setActionRegistry(registry);
	}

	
	/**
	 * @see ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	public void buildContextMenu(IMenuManager menu)
	{
		//GEFActionConstants.addStandardActionGroups(menu);

		IAction action;
		action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.REDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.COPY.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);

		action = getActionRegistry().getAction(ActionFactory.CUT.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);

        appendActionToCopyGroup(menu, MenuActions.PasteAction.ID);	
        
		//action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
		//menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);

		//action = getActionRegistry().getAction(IWorkbenchActionConstants.DELETE);
		//if (action.isEnabled())
		//	menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

        appendActionToRestGroup(menu, MenuActions.AddLabelAction.ID);
        appendActionToRestGroup(menu, MenuActions.AddContainerAction.ID);	
	}

	
	/**
	 * Appends the specified action to the specified menu group
	 * @param actionId
	 * @param menuGroup
	 */
	private void appendActionToMenu(IMenuManager menu, String actionId, String menuGroup) {
		IAction action = actionRegistry.getAction(actionId);

		if (null != action && action.isEnabled()) {
			menu.appendToGroup(menuGroup, action);
		}
	}

	
	/**
	 * Appends the specified action to the specified menu group
	 * @param actionId
	 * @param menu
	 */
	private void appendActionToUndoGroup(IMenuManager menu, String actionId) {
		IAction action = actionRegistry.getAction(actionId);
		
		if (null != action && action.isEnabled()) {
			menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		}
	}

	
	/**
	 * Appends the specified action to the specified menu group
	 * @param actionId
	 * @param menu
	 */
	private void appendActionToEditGroup(IMenuManager menu, String actionId) {
		IAction action = actionRegistry.getAction(actionId);
		
		if (null != action && action.isEnabled()) {
			menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		}
	}

	
	/**
	 * Appends the specified action to the specified menu group
	 * @param actionId
	 * @param menu
	 */
	private void appendActionToAddGroup(IMenuManager menu, String actionId) {
		IAction action = actionRegistry.getAction(actionId);
		
		if (null != action && action.isEnabled()) {
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);
		}
	}

	
    /**
     * Appends the specified action to the specified menu group
     * @param actionId
     * @param menu
     */
    private void appendActionToSaveGroup(IMenuManager menu, String actionId) {
        IAction action = actionRegistry.getAction(actionId);

        if (null != action && action.isEnabled()) {
            menu.appendToGroup(GEFActionConstants.GROUP_SAVE, action);
        }
    }

    
    /**
     * Appends the specified action to the specified menu group
     * @param actionId
     * @param menu
     */
    private void appendActionToRestGroup(IMenuManager menu, String actionId) {
        IAction action = actionRegistry.getAction(actionId);

        if (null != action && action.isEnabled()) {
            menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
        }
    }

    
    /**
     * Appends the specified action to the specified menu group
     * @param actionId
     * @param menu
     */
    private void appendActionToCopyGroup(IMenuManager menu, String actionId) {
        IAction action = actionRegistry.getAction(actionId);

        if (null != action && action.isEnabled()) {
            menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);
        }
    }
    
    
    private ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	
	/**
	 * Sets the action registry
	 * 
	 * @param registry
	 *            the action registry
	 */
	public void setActionRegistry(ActionRegistry registry) {
		actionRegistry = registry;
	}

}