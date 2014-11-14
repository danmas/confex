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
package net.confex.action;

import net.confex.tree.IRunBrowser;
import net.confex.tree.ITreeNode;
import net.confex.views.NavigationView;
import net.confex.views.WebBrowserView;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

public class RunBrowserAction extends Action  {
	private static final String ID = "net.confex.action.RunAction";

	//private IWorkbenchWindow window;

	private IViewPart view;

	private ITreeNode tree_node;

	public RunBrowserAction(IViewPart view, ITreeNode tree_node) {
		this.view = view;
		this.tree_node = tree_node;
		init();
	}

	
	IPartListener partListener = new PartListener(); 

	class PartListener implements  IPartListener {
	/**
	 * Cлушатель чтобы отследить закрытие и изменить состояние
	 */
	public void partOpened(IWorkbenchPart part) {
	}

	public void partBroughtToTop(IWorkbenchPart part) {
	}

	public void partDeactivated(IWorkbenchPart part) {
	}

	public void partActivated(IWorkbenchPart part) {
	}

	/**
	 * Обработка закрытия вида
	 */
	public void partClosed(IWorkbenchPart part) {
		if (part instanceof WebBrowserView) {
			if(part == browser) {
				/*
				System.out.println(
						"Обработка закрытия вида removePartListener()"
						+ "tree_node="+tree_node.getName() + "browser= "+part
						);
				*/
				tree_node.setNormalState();
				//tree_node.removeStateObserver(navigation_view);
				((WebBrowserView) part).removePartListener(partListener);
			}
		}
	}
	};
	
	
	private void init() {
		// setText("Run Browser Action");
		// setImageDescriptor(ImageResource
		// .getImageDescriptor("eclipse icons\\etool16\\copy_edit.gif"));
		setId(ID);
		setActionDefinitionId(ID);
	}

	
	private WebBrowserView browser = null;
	
	
	public void run() {
		IWorkbenchPage page = view.getSite().getPage();

		//PlatformUI.getPreferenceStore().putValue(IWorkbenchPreferenceConstants. .INITIAL_FAST_VIEW_BAR_LOCATION,
        //        IWorkbenchPreferenceConstants.LEFT); 
		
		if (tree_node instanceof IRunBrowser) {
			browser = ((IRunBrowser) tree_node).runBrowser(page); //,navigation_view);
			if(browser==null)
				return;
		}
		
		//if (tree_node.isInState(ITreeNodeState.STATE_RUN)) {
			// -- добавляем слушателя чтобы отследить
			// закрытие и изменить состояние
			browser.addPartListener(partListener);
			/*
			System.err.println(
					"открытие вида addPartListener() "
					+"browser= "+browser);
			*/
			// -- устанавливаем состояние RUN
			//tree_node.addStateObserver(navigation_view);
			tree_node.setRunState();
		//}
	}
	

}
