package net.confex.app.workbench;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.BaseNewWizardMenu;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.provisional.application.IActionBarConfigurer2;

/**
 * Fills a workbench window with an actions - menus, coolbar
 */
public class ActionBarBuilder extends ActionBarAdvisor {
	// TODO: basic menu set

	private final IWorkbenchWindow window;

	IWorkbenchAction newWizardAction;

	IWorkbenchAction newWizardDropDownAction;

	private IWorkbenchAction quitAction;

	private BaseNewWizardMenu newWizardMenu;

	public ActionBarBuilder(IActionBarConfigurer configurer) {
		super(configurer);
		window = configurer.getWindowConfigurer().getWindow();
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IActionBarConfigurer2 configurer = (IActionBarConfigurer2) getActionBarConfigurer();

		coolBar.add(new GroupMarker("group.file"));
		IToolBarManager fileToolBar = configurer.createToolBarManager();
		fileToolBar.add(new Separator("new.group"));
		fileToolBar.add(newWizardDropDownAction);
		fileToolBar.add(new GroupMarker("new.ext"));

		fileToolBar.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		coolBar.add(fileToolBar);

	}

	protected void makeActions(final IWorkbenchWindow window) {
		newWizardAction = ActionFactory.NEW.create(window);
		register(newWizardAction);

		newWizardDropDownAction = MyActionFactory.NEW_WIZARD_DROP_DOWN
				.create(window);
		register(newWizardDropDownAction);

		quitAction = ActionFactory.QUIT.create(window);
		register(quitAction);

	}

	protected void fillMenuBar(IMenuManager menuBar) {
		menuBar.add(createFileMenu());
	}

	private MenuManager createFileMenu() {
		MenuManager menu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		{
			String newText = "&New";
			String newId = ActionFactory.NEW.getId();
			MenuManager newMenu = new MenuManager(newText, newId);
			newMenu.setActionDefinitionId("org.eclipse.ui.file.newQuickMenu"); //$NON-NLS-1$
			newMenu.add(new Separator(newId));
			this.newWizardMenu = new BaseNewWizardMenu(getWindow(), null);
			newMenu.add(this.newWizardMenu);
			newMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			menu.add(newMenu);
		}

		menu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
		menu.add(new Separator());

		ActionContributionItem quitItem = new ActionContributionItem(quitAction);
		quitItem.setVisible(!"carbon".equals(SWT.getPlatform())); //$NON-NLS-1$
		menu.add(quitItem);
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		return menu;
	}

	private IWorkbenchWindow getWindow() {
		return window;
	}

}
