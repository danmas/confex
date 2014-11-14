package net.confex.schema.editor;

import net.confex.schema.action.MenuActions;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;


//import com.evelopers.unimod.plugin.eclipse.ui.actions.ExportRuntimeXMLAction;


public class DefaultContextMenuProvider extends org.eclipse.gef.ContextMenuProvider {

	/** the action registry */
	private final ActionRegistry actionRegistry;

	
	/**
	 * Creates a new WorkflowEditorContextMenuProvider instance.
	 * @param viewer
	 */
	public DefaultContextMenuProvider(EditPartViewer viewer, ActionRegistry actionRegistry) {
		super(viewer);
		this.actionRegistry = actionRegistry;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	public void buildContextMenu(IMenuManager menuManager) {
		//GEFActionConstants.addStandardActionGroups(menuManager);
		menuManager.add(new Separator(GEFActionConstants.GROUP_UNDO));
		menuManager.add(new Separator(GEFActionConstants.GROUP_COPY));
		//menu.add(new Separator(GROUP_PRINT));
		menuManager.add(new Separator(GEFActionConstants.GROUP_EDIT));
		//menu.add(new Separator(GROUP_VIEW));
		//menu.add(new Separator(GROUP_FIND));
		menuManager.add(new Separator(GEFActionConstants.GROUP_ADD));
		menuManager.add(new Separator(GEFActionConstants.GROUP_REST));
		//menu.add(new Separator(MB_ADDITIONS));
		//menu.add(new Separator(GROUP_SAVE));

		appendActionToUndoGroup(menuManager, ActionFactory.UNDO.getId());
		appendActionToUndoGroup(menuManager, ActionFactory.REDO.getId());

		appendActionToCopyGroup(menuManager, ActionFactory.COPY.getId());
		//appendActionToEditGroup(menuManager, ActionFactory.PASTE.getId());
		appendActionToCopyGroup(menuManager, MenuActions.PasteAction.ID);
		/*
		appendActionToCopyGroup(menuManager, ActionFactory.CUT.getId());
		*/
		appendActionToEditGroup(menuManager, ActionFactory.DELETE.getId());
		
		appendActionToEditGroup(menuManager, GEFActionConstants.DIRECT_EDIT);
		
		
		appendActionToAddGroup(menuManager, MenuActions.AddLabelAction.ID);
		appendActionToAddGroup(menuManager, MenuActions.AddContainerAction.ID);
		
		appendActionToRestGroup(menuManager, MenuActions.StandartStateAction.ID);		
		appendActionToRestGroup(menuManager, MenuActions.CompactStateAction.ID);		
		
        //appendActionToSaveGroup(menuManager, ExportRuntimeXMLAction.ID);

        //appendActionToMenu(menuManager, IWorkbenchActionConstants.SAVE, GEFActionConstants.GROUP_SAVE);
        
        //MarkerResolutionSelectionDialog
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
	private void appendActionToCopyGroup(IMenuManager menu, String actionId) {
		IAction action = actionRegistry.getAction(actionId);
		
		if (null != action && action.isEnabled()) {
			menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);
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
    
}
