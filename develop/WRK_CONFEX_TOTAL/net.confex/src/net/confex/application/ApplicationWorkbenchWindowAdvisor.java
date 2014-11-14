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

import java.io.File;

import net.confex.action.ApplicationActionBarAdvisor;
import net.confex.translations.Translator;
import net.confex.views.NavigationView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;


public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        //configurer.setInitialSize(new Point(800, 600));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(true);
        configurer.setShowFastViewBars(false);
        //-- set anable show progress indicator
        configurer.setShowProgressIndicator(true);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */
	@Override
	public void postWindowOpen() {
		// TODO Auto-generated method stub
		super.postWindowOpen();
		System.out.println("[ApplicationWorkbenchWindowAdvisor.postWindowOpen()]");
		String start_confex = ConfexPlugin.getDefault().getStartConfex();
		if(start_confex!=null) {
			System.out.println("Starting Confex "+ start_confex);
			File confex_file = new File(start_confex);
			if(confex_file.exists()) {
				createStartConfex(start_confex);
			} else {
				System.err.println("File not found! " + start_confex);
			}
		}
	}
    

	private void createStartConfex(String start_confex) {
		try {
			IWorkbenchPage page = ConfexPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();//obtain the active page
			NavigationView navigationView = (NavigationView)page.showView(
					NavigationView.ID, Long.toString(System.currentTimeMillis()), 
					IWorkbenchPage.VIEW_ACTIVATE);
			// DFIXME -- old error (resolved by using System.currentTimeMillis() ?)
			if(!navigationView.getTitle().equals("Confex Navigation")) {
				System.err.println(" Ошибка создается вид поверх старого "+navigationView.getTitle());
				return;
			}
			navigationView.loadFile(start_confex);
		} catch (PartInitException e) {
			System.err.println(Translator.getString("MESSAGEBOX_TITLE_ERROR")+
					"  " + Translator.getString("MESSAGE_ERR_OPEN_VIEW") + e.getMessage());
			//MessageDialog.openError(window.getShell(), 
			//		Translator.getString("MESSAGEBOX_TITLE_ERROR"),
			//		Translator.getString("MESSAGE_ERR_OPEN_VIEW") + e.getMessage());
		}
	}
	
    /*
    public void createWindowContents(Shell shell) {
    	  IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    	  Menu menu = configurer.createMenuBar();
    	  shell.setMenuBar(menu);
    	  FormLayout layout = new FormLayout();
    	  layout.marginWidth = 0;
    	  layout.marginHeight = 0;
    	  shell.setLayout(layout);
    	  toolbar = configurer.createCoolBarControl(shell);
    	  page = configurer.createPageComposite(shell);
    	  statusline = configurer.createStatusLine(shell);
    	}  
    */  
    
}
