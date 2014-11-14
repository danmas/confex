package net.confex.app.workbench;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.BaseNewWizardMenu;
import org.eclipse.ui.actions.NewWizardDropDownAction;

public class MyActionFactory {

	public static final ActionFactory NEW_WIZARD_DROP_DOWN = new ActionFactory(
			"newWizardDropDown") { //$NON-NLS-1$

		/* (non-javadoc) method declared on ActionFactory */
		public IWorkbenchAction create(IWorkbenchWindow window) {
			if (window == null) {
				throw new IllegalArgumentException();
			}
			IWorkbenchAction innerAction = ActionFactory.NEW.create(window);
			BaseNewWizardMenu newWizardMenu = new BaseNewWizardMenu(window,
					null);
			IWorkbenchAction action = new NewWizardDropDownAction(window,
					innerAction, newWizardMenu);
			action.setId(getId());
			return action;
		}
	};

}
