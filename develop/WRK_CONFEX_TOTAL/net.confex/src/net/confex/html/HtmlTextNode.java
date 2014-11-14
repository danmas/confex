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
package net.confex.html;


import net.confex.action.RunBrowserAction;
import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IRunBrowser;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.confex.views.WebBrowserView;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.w3c.dom.Node;

/**
 * Узел HTML текста. При запуске текст выводится в браузер 
 * 
 * @author Roman_Eremeev
 *
 */

public class HtmlTextNode  extends TreeNode implements IHtmlPart, IRunBrowser  {
	
	//private String htmltext = "";
	
	public String getAboutString() { return Translator.getString("ABOUT_HTMLTREENODE"); }
	
	
	protected static String default_image_name = "non_eclipse/htmlpart16x16.gif"; 

	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	
	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	
	public HtmlTextNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new HtmlTextNode(getConfigTree(),null); 
	}

	
	public WebBrowserView runBrowser(IWorkbenchPage page/*, IStateObserver observer*/) {
		WebBrowserView browser = null;
        try {
        	String htmltext = this.getFullHtmltext(); // .getFullUrl();

        	String id = String.valueOf(getStringKey().hashCode());
        	String title = getName();
			if(title==null||title.trim().equals("")) {
				title=this.getClassName().toString();
			}
            browser = (WebBrowserView) page.showView(
            		WebBrowserView.VIEW_ID, id, IWorkbenchPage.VIEW_ACTIVATE);
            if (browser != null) {
            	browser.setTitlePartName(title);
            	browser.setUrl(" ");
                browser.setPage(htmltext);
            	browser.refresh();
            }
        } catch(PartInitException e1) {
        	System.err.println("-->" + e1.getMessage());
        }
        return browser;
	}
	
	
	public void run(IViewPart view) { 
		RunBrowserAction runBrowserAction = new RunBrowserAction(view,this);
		runBrowserAction.run();
	}

	
	public void runInBatch(IViewPart view) {
		//run(navigation_view);
	}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof HtmlTextNode)) {
			System.err.println("[HtmlTextNode.setPropertyLike] prototype NOT instanceof HtmlTextNode!");
			return;
		}
		//setHtmltext(((HtmlTextNode)prototype).getSrcText());
	}

	
	
	/**
	 * Конструирует контент проходя по 
	 * всем дочерним узлам HtmlTextNode  
	 * @return
	 */
	public String getFullText() {
		readFromSrcFile();
    	//-- производим подстановку переменных
		String text = TreeUtils.doAllSubstitutions(this,getSrcText());
		
		for(int i=0; i<getChildren().length; i++) {
			if(getChildren()[i] instanceof IHtmlPart) {
				text += ((IHtmlPart)getChildren()[i]).getFullHtmltext(); 
			}
		}
		return text;
	}
	
	
	/**
	 * Конструирует контент проходя по 
	 * всем дочерним узлам HtmlTextNode  
	 * @return
	 */
	public String getFullHtmltext() {
		if(isNotRunInBatch())
			return "";

		readFromSrcFile();
    	//-- производим подстановку переменных
		String text = TreeUtils.doAllSubstitutions(this,getSrcText());
		
		for(int i=0; i<getChildren().length; i++) {
			if(getChildren()[i] instanceof IHtmlPart) {
				text += ((IHtmlPart)getChildren()[i]).getFullHtmltext(); 
			}
		}
		return text;
	}
	
	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("htmltext")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setSrcText(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
	}
	
	
	public String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if(getSrcText()!=null 
				&& !getSrcText().trim().equals("")) {
			str_xml += "<htmltext>\n";
			str_xml += Utils.toHtmlSpecialEntities(getSrcText())+"\n"; 
			str_xml += "</htmltext>\n"; 
		}
		return str_xml;
	}
	
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		readFromSrcFile();
		return new HtmltextPropertyDialog(shell);
	}
	
	
}
