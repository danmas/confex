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
package net.confex.tree;


import net.confex.action.RunBrowserAction;
import net.confex.directedit.IPropertyDialog;
import net.confex.directedit.UrlPropertyDialog;
import net.confex.translations.Translator;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.confex.views.WebBrowserView;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.w3c.dom.Node;



public class UrlNode  extends TreeNode implements IRunBrowser {
	
	private String url = "";
	
	public String getAboutString() { return Translator.getString("ABOUT_URLTREEOBJECT"); }
	
	
	protected static String default_image_name = "eview16/internal_browser.gif"; 
	
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	
	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	
	public UrlNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	/*
	public UrlNode(ConfigTree configTree, String name, String url) {
		super(configTree,name);
		this.url = url;
	}*/

	
	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new UrlNode(getConfigTree(),null); 
	}
	

	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof UrlNode)) {
			System.err.println("[UrlNode.setPropertyLike] prototype NOT instanceof UrlNode!");
			return;
		}
		setUrl(((UrlNode)prototype).getPath());
	}


	public WebBrowserView runBrowser(IWorkbenchPage page/*, IStateObserver observer*/) {
		WebBrowserView browser = null;
        try {
        	String url = this.getFullUrl();
        	/*
        	String id = url;
        	id = id.replaceAll(":", "_");
        	id = id.replaceAll("/", "_");
        	//System.out.println("-->" + url + "   " + id);
        	*/
        	String id = String.valueOf(getStringKey().hashCode());
        	String title = getName();
			if(title==null||title.trim().equals("")) {
				title=this.getClassName().toString();
			}
        	browser = (WebBrowserView) page.showView(WebBrowserView.VIEW_ID, id,
        			IWorkbenchPage.VIEW_ACTIVATE);
            if (browser != null) {
            	
            	browser.setTitlePartName(TreeUtils.doAllSubstitutions(this,title)); //url.trim());
            	String path = TreeUtils.doAllSubstitutions(this,url.trim());
                browser.setUrl(path);
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

	
	public String getPath() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	//public UrlNode addUrlTreeObject(String name, String url) {
	//	UrlNode child = new UrlNode(getConfigTree(),name,url);
	//	this.addChild(child);
	//	return child;
	//}
	

	/**
	 * Если текущий url не начинается с http: то конструируем 
	 * составной url из всех родительских
	 * @return
	 */
	public String getFullUrl() {
		if(url.toLowerCase().startsWith("http:", 0)) {
			return url;
		} else {
			String parent_url = "";
			ITreeNode p = getParent();
			while(p!=null) {
				if(p instanceof UrlNode) {
					parent_url = ((UrlNode)p).getFullUrl();
					break;
				}
				p = p.getParent();
			}
			String s_url = parent_url + TreeUtils.doAllSubstitutions(this,url);
			return s_url;
		}
	}
	
	
	public void loadAttribFromXml_0_0_1(Node node,ITreeNode parent) {
		super.loadAttribFromXml_0_0_1(node,parent);
		
		Node attr_url=node.getAttributes().getNamedItem("url");
		if(attr_url==null) {
			//System.err.println(" Can't read url for " + this.toString());
		} else {
			String s_url=attr_url.getNodeValue();
			this.setUrl(Utils.fromHtmlSpecialEntities(s_url));
			//System.out.println("attrib url= "+s_url);
		}
	}
	
	
	/*public String getAttribXml() {
		String str_xml = super.getAttribXml();
		str_xml += "url=\""+Utils.toHtmlSpecialEntities(url)+"\"\n"; 
		return str_xml;
	}*/

	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if(url!=null 
				&& !url.equals("")) {
			str_xml += "<url>\n"
				+Utils.toHtmlSpecialEntities(url)+"\n"; 
			str_xml += "</url>\n"; 
		}
		return str_xml;
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("url")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setUrl(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
	}
	
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new UrlPropertyDialog(shell);
	}
	
	
	/*
	public void openPropertyDialog(NavigationView view, Shell shell,
			boolean new_flg) {
		if(property_dialog==null) {
			property_dialog = new UrlPropertyDialog(shell);
		}
		property_dialog.setNewFlg(new_flg);
		property_dialog.createSShell(); 
		property_dialog.setView(view);
		property_dialog.show();
		property_dialog.setTreeNode(this);
	}*/
	
	
	/**
	 * Загружает внутренние параметры из DOM дерева
	 * @param node Начинается с <NodeElement id="4">
	 */
	/*public void loadFromXml(Node node,TreeNode parent) {
		super.loadFromXml(node,parent);
		
		Node attr_url=node.getAttributes().getNamedItem("url");
		if(attr_url==null) {
			System.err.println(" Can't read name for " + this.toString());
		} else {
			String s_url=attr_url.getNodeValue();
			this.setUrl(s_url);
			System.out.println(s_url);
		}
		//-- грузим дочерние
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node child = node.getChildNodes().item(i);
			ConfigTree.parseXml(child, this);
			//if (child.getNodeName().equals("children")) {
			//	loadChildrenFromXml(child,parent);
			//}
		}
	}*/

	
}
