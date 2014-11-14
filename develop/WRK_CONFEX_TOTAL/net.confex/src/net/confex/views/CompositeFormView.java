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
package net.confex.views;

import net.confex.html.IHtmlPart;
import net.confex.tree.CompositeFormViewNode;
import net.confex.tree.ICompositeProvider;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;


public class CompositeFormView extends ConfexView implements ICompositeProvider {
	public static final String VIEW_ID = "net.confex.view.formView";

	// private FormToolkit toolkit;
	// private FormText rtext;

	// private ScrolledForm form;

	/**
	 * The constructor.
	 */
	public CompositeFormView() {
	}

	private Composite content, content_main;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		try {
			content_main = new Composite(parent, SWT.NONE);
			content_main.setLayout(new FillLayout());
			
			contributeToActionBars();
			//-- set common Status Line Message
			//String s = ConfexPlugin.getDefault().getStatusLine();
			//IActionBars bars = getViewSite().getActionBars();
			//bars.getStatusLineManager().setMessage(s);
		} catch(Exception e) {
			System.err.println("[CompositeFormView.createPartControl()] "+e.getMessage());
		}
	}


	// FIXME Нарушение CVM модели!!!
	private CompositeFormViewNode formViewNode = null;

	
	/**
	 *  We define this method to avoid loading View on start application.   
	 */
    public void init(IViewSite site, IMemento memento) throws PartInitException {
    	super.init(site);
    	//if(memento!=null) {
    	//}
    }
    
	
	public void makeContent(CompositeFormViewNode formViewNode,IProgressMonitor monitor) {
		this.formViewNode = formViewNode;

		if (content != null) {
			content.dispose();
			content = null;
		}
		
		if(monitor!=null) {
			Display d = content_main.getDisplay(); 
			d.syncExec(new Runnable() {
    			public void run() {
    				content = new Composite(content_main, SWT.NONE);
					GridLayout gridLayout1 = new GridLayout();
					gridLayout1.numColumns = 1;
					gridLayout1.marginWidth = 0;
					gridLayout1.marginHeight = 0;
					gridLayout1.verticalSpacing = 5;
					gridLayout1.horizontalSpacing = 5;
					content.setLayout(gridLayout1);
					
					content.setLayoutData(new GridData(GridData.FILL_BOTH));
    			}
			});
		} else {
			content = new Composite(content_main, SWT.NONE);
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.numColumns = 1;
			gridLayout1.marginWidth = 0;
			gridLayout1.marginHeight = 0;
			gridLayout1.verticalSpacing = 5;
			gridLayout1.horizontalSpacing = 5;
			content.setLayout(gridLayout1);
			
			content.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		// -- по всем дочерним строим контент
		makeComposite(content, this, monitor);

		// -- перерисовываем контент
		
		if(monitor==null) {
			content.layout();
			content_main.layout();
			content_main.redraw();
		}
	}

	
	/**
	 * Конструирует контент проходя по всем дочерним узлам HtmlTextNode
	 * 
	 */
	protected String makeContent() {
		String content = "";
		if (formViewNode != null) {
			for (int i = 0; i < formViewNode.getChildren().length; i++) {
				if (formViewNode.getChildren()[i] instanceof IHtmlPart) {
					content += ((IHtmlPart) formViewNode.getChildren()[i])
							.getFullHtmltext();
				}
			}
		}
		return content;
	}


	
	// FIXME: dispose m_parent!!!
	Composite m_parent = null;
	
	public void refreshComposite() {
		if(m_parent!=null) {
			m_parent.layout();
			m_parent.redraw();
			System.out.println("refreshComposite()");
		}
	}

	
	public void disposeComposite() {
		if (formViewNode != null) {
			for (int i = 0; i < formViewNode.getChildren().length; i++) {
				if (formViewNode.getChildren()[i] instanceof ICompositeProvider) {
					((ICompositeProvider) formViewNode.getChildren()[i])
							.disposeComposite();
				}
			}
		}
		m_parent = null;
	}
	
	
	/**
	 * Конструирует контент проходя по всем дочерним узлам ICompositeProvider
	 * 
	 */
	public void makeComposite(Composite parent, ViewPart viewPart,IProgressMonitor monitor) {
		m_parent = parent;
		if (formViewNode != null) {
			for (int i = 0; i < formViewNode.getChildren().length; i++) {
				if (formViewNode.getChildren()[i] instanceof ICompositeProvider) {
					((ICompositeProvider) formViewNode.getChildren()[i])
							.makeComposite(parent, viewPart,monitor);
				}
			}
		}
	}

	/**
	 * Passing the focus request to the form.
	 */
	public void setFocus() {
		if (content != null) {
			content.setFocus();
		}
	}

	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		if (content_main != null)
			content_main.dispose();
		super.dispose();
		disposeComposite();
	}

	public void setTitlePartName(String title) {
		setPartName(title);
	}



}