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
import net.confex.utils.Strings;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.confex.views.WebBrowserView;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.w3c.dom.Node;

public class Java2HtmlNode  extends TreeNode implements IHtmlPart, IRunBrowser  {
	
	private String codeText = "";
	private String header = defaultHeader;
	private String footer = defaultFooter;

	private String selected_words = "";
	private String prefix = "<b>";
	private String postfix = "</b>";
	
	protected static String defaultHeader = "<pre class=code>";
	protected static String defaultFooter = "</pre><br/><br/>";

	public String getAboutString() { return Translator.getString("ABOUT_JAVA2HTMLNODE"); }
	
	
	protected static String default_image_name = "non_eclipse/java2htmlpart16x16.gif"; 

	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	
	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	
	public Java2HtmlNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
		//header = defaultHeader;
		//footer = defaultFooter;
	}
	
	
	/*
	public Java2HtmlNode(ConfigTree configTree, String name, String codeText) {
		super(configTree,name);
		if(codeText==null) 
			codeText="";
		this.codeText = codeText;
	}*/

	
	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new Java2HtmlNode(getConfigTree(),null); 
	}

	
	public WebBrowserView runBrowser(IWorkbenchPage page/*, IStateObserver observer*/) {
		WebBrowserView browser = null;
        try {
        	String htmltext = this.getFullHtmltext(); 
        	String id = getName();
			if(id==null||id.trim().equals("")) {
				id=this.getClassName().toString();
			}
            browser = (WebBrowserView) page.showView(
            		WebBrowserView.VIEW_ID, id, IWorkbenchPage.VIEW_ACTIVATE);
            if (browser != null) {
            	browser.setTitlePartName(id);
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
	
	
	public String getCodeText() {
		return codeText;
	}

	
	public void setCodeText(String codeText) {
		if(codeText==null) 
			codeText="";
		this.codeText = codeText;
	}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof Java2HtmlNode)) {
			System.err.println("[JavaHtmlNode.setPropertyLike] prototype NOT instanceof HtmlTextNode!");
			return;
		}
		setCodeText(((Java2HtmlNode)prototype).getCodeText());
		setHeader(((Java2HtmlNode)prototype).getHeader());
		setFooter(((Java2HtmlNode)prototype).getFooter());
		setSelectedWords(((Java2HtmlNode)prototype).getSelectedWords());
		setPrefix(((Java2HtmlNode)prototype).getPrefix());
		setPostfix(((Java2HtmlNode)prototype).getPostfix());
	}

	
	/**
	 * Конструирует контент проходя по всем дочерним узлам HtmlTextNode
	 * 
	 * @return
	 */
	public String getFullHtmltext() {
		if(isNotRunInBatch())
			return "";

		String code_string = Utils.java2html(codeText);
		String[] tok = Utils.tokenize(selected_words, ",");
		for (int j = 0; j < tok.length; j++) {
			code_string = Strings.replace(code_string, tok[j], prefix + tok[j]
					+ postfix);
		}

		String text = getHeader();
		text += code_string;
		text += getFooter();

		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] instanceof IHtmlPart) {
				text += ((IHtmlPart) getChildren()[i]).getFullHtmltext();
			}
		}
		return TreeUtils.doAllSubstitutions(this,text);
	}
	
	
	/*
	 * public void loadAttribFromXml_0_0_1(Node node,ITreeNode parent) {
	 * super.loadAttribFromXml_0_0_1(node,parent);
	 * 
	 * Node attr_htmltext=node.getAttributes().getNamedItem("htmltext");
	 * if(attr_htmltext==null) { System.err.println(" Can't read attr_htmltext
	 * for " + this.toString()); } else { String
	 * s_htmltext=attr_htmltext.getNodeValue();
	 * this.setHtmltext(Utils.fromHtmlSpecialEntities(s_htmltext));
	 * //System.out.println("attrib htmltext= "+s_htmltext); } }
	 */
	
	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("codetext")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setCodeText(Utils.fromHtmlSpecialEntities(text.trim()));
		} else  
		if (property.getNodeName().equals("header")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setHeader(Utils.fromHtmlSpecialEntities(text.trim()));
		} else
			if (property.getNodeName().equals("footer")) {
				Node nd=property.getFirstChild();
				String text="";
				if(nd!=null)
					text = nd.getNodeValue();
				this.setFooter(Utils.fromHtmlSpecialEntities(text.trim()));
			} 
		
		if (property.getNodeName().equals("selected_words")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setSelectedWords(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
		if (property.getNodeName().equals("prefix")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setPrefix(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
		if (property.getNodeName().equals("postfix")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setPostfix(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
	}
	

	public String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if(header!=null 
				&& !header.trim().equals("")) {
			str_xml += "<header>\n";
			str_xml += Utils.toHtmlSpecialEntities(header)+"\n"; 
			str_xml += "</header>\n"; 
		}
		if(codeText!=null 
				&& !codeText.trim().equals("")) {
			str_xml += "<codetext>\n";
			str_xml += Utils.toHtmlSpecialEntities(codeText)+"\n"; 
			str_xml += "</codetext>\n"; 
		}
		if(footer!=null 
				&& !footer.trim().equals("")) {
			str_xml += "<footer>\n";
			str_xml += Utils.toHtmlSpecialEntities(footer)+"\n"; 
			str_xml += "</footer>\n"; 
		}
		
		if(selected_words!=null 
				&& !selected_words.trim().equals("")) {
			str_xml += "<selected_words>\n";
			str_xml += Utils.toHtmlSpecialEntities(selected_words)+"\n"; 
			str_xml += "</selected_words>\n"; 
		}
		if(prefix!=null 
				&& !prefix.trim().equals("")) {
			str_xml += "<prefix>\n";
			str_xml += Utils.toHtmlSpecialEntities(prefix)+"\n"; 
			str_xml += "</prefix>\n"; 
		}
		if(postfix!=null 
				&& !postfix.trim().equals("")) {
			str_xml += "<postfix>\n";
			str_xml += Utils.toHtmlSpecialEntities(postfix)+"\n"; 
			str_xml += "</postfix>\n"; 
		}
		return str_xml;
	}
	
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new Java2HtmlPropertyDialog(shell);
	}
	
	
	/*
	public void openPropertyDialog(NavigationView view, Shell shell,
			boolean new_flg) {
		
		if(property_dialog==null) {
			property_dialog = new Java2HtmlPropertyDialog(shell);
		}
		property_dialog.setNewFlg(new_flg);

		property_dialog.createSShell(); 
		property_dialog.setView(view);
		property_dialog.show();
		property_dialog.setTreeNode(this);
	}*/
	

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getSelectedWords() {
		return selected_words;
	}

	public void setSelectedWords(String selected_words) {
		this.selected_words = selected_words;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	
}
