/*******************************************************************************
 * Copyright (c) 2006,2007 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package net.confex.html;

import java.io.File;

import net.confex.action.RunBrowserAction;
import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.ICompositeProvider;
import net.confex.tree.IRunBrowser;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.confex.views.WebBrowserView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.Node;

/**
 * HTML с динамическим контентом Он состоит из трех частей header/footer/content
 * 
 * header и footer сохраняются в этом элементе а content динамический
 * составляется во время прохождения по дочерним HtmlTextNode узлам
 * 
 * Файл записывается по команде Run и в диалоге свойств по команде Test  
 * 
 * @author Roman_Eremeev
 * 
 * ver 0.9.5
 * 
 */

// HtmlWithContent -> HtmlComposite
public class HtmlComposite extends TreeNode implements IHtmlPart, IRunBrowser, ICompositeProvider { 

	
	private String header = defaultHeader;
	private String content = "";
	private String footer = defaultFooter;
	private String file_name = "";

	protected static String defaultHeader = "";
	/* 
	 "<html>\n" +
"<head>\n" +
"<meta http-equiv=\"Content-Type\"\n" +
"	content=\"text/html; charset=\"windows-1251\" />\n" + 
"<meta name=\"Content-Language\" content=\"russian\"></meta>\n" +
"<meta name=\"description\"\n" +
"	content=\"\"></meta>\n" +
"<meta name=\"keywords\"\n" +
"  content=\"eclipse,Rich,Client,Platform,rcp,java,книги,статьи,приложения\"></meta>\n" +
"<meta name=\"expires\" content=\"0\"></meta>\n" +
"<meta name=\"resource-type\" content=\"document\"></meta>\n" +
"<meta name=\"author\" content=\"Автор\"></meta>\n" +
"<meta name=\"copyright\"\n" +
"	content=\"(C) 2006\"></meta>\n" +
"<meta name=\"robots\" content=\"index,all\"></meta>\n" +
"<meta name=\"revisit\" content=\"5 Days\"></meta>\n" +
"<meta name=\"revisit-after\" content=\"5 Days\"></meta>\n" +
"<meta name=\"distribution\" content=\"global\"></meta>\n" +
"<meta name=\"rating\" content=\"general\"></meta>\n" +
"<meta name=\"audience\" content=\"all\"></meta>\n" +
"<title>Title</title>\n" +
"\n" +
"<style type=\"text/css\">\n"+
"< !-- \n"+
".text_my	 { \n"+
"	color: black; \n"+
"	font-size: 100%; \n"+   
"	table-layout: auto; \n"+
"	font-family: serif;  \n"+
"} \n"+
".code { border-color: #CFDCFF; border-style: solid; border-width: 3px; \n"+
"   color: #0000EF; background-color: #EFEFEF;  \n"+
"   margin: 3px; margin-left: 2px; margin-right: 2px; margin-top: 2px; margin-bottom: 2px;\n" +
"} \n"+
".h_line { \n"+
"	color: blue; \n"+
"	font-size: 100%; font: italic; color: blue; \n"+
"	table-layout: auto; \n"+
"	font-family: serif; font-style: italic; \n"+ 
"} \n"+
".output {\n" +
"   color: #FFFFFF; background: #837A67;\n" +
"} \n"+
".xml { border-color: #CFDCFF; border-style: solid; border-width: 3px; \n"+
"   color: #0000EF; background-color: #EFEFEF; \n"+
"   margin: 3px; margin-left: 2px; margin-right: 2px; margin-top: 2px; margin-bottom: 2px;\n"+
"}\n" +
"-- >\n"+ 
"</style>\n"+
"\n" +
"<!--  link href=\"./bf.css\" rel=\"stylesheet\" type=\"text/css\" /-->\n" +
"<!--  link rel=\"shortcut icon\" href=\"../im/logo.ico\"></link -->\n" +
"\n" +
"</head>\n" +
"<body>\n" +
""; */
	
	protected static String defaultFooter = ""; 
/*	"<hr>\n" +
"</body>\n" +
"</html>\n"; */

	
	public String getAboutString() {
		return Translator.getString("ABOUT_HTMLWITHCONTENT");
	}

	
	protected static String default_image_name = "non_eclipse/htmlcomposite16x16.gif"; 

	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	
	public static String getDefaultImageName() {
		return default_image_name;
	}

	
	public HtmlComposite(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	/**
	 * Cоздает экземпляр того же класса !!! Должен быть переопределен в дочерних
	 * классах
	 * 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new HtmlComposite(getConfigTree(),null);
	}

	
	public WebBrowserView runBrowser(IWorkbenchPage page/*, IStateObserver observer*/) {
		WebBrowserView browser = null;
		try {
			String htmltext = TreeUtils.doAllSubstitutions(this, this.getFullHtmltext()); 
        	String id = String.valueOf(getStringKey().hashCode());
        	String title = getName();
			if(title==null||title.trim().equals("")) {
				title=this.getClassName().toString();
			}
			browser = (WebBrowserView) page.showView(
					WebBrowserView.VIEW_ID, id, IWorkbenchPage.VIEW_ACTIVATE);
			if (browser != null) {
				browser.setTitlePartName(title);

				browser.setPage(htmltext);
				browser.refresh();
				
				File file = getFile();
				if(file!=null) {
					browser.getCombo().setText("file:///"+file.getAbsolutePath());
					saveFile(htmltext);
				} else {
					//browser.setUrl(" ");
				}
					
			}
		} catch (PartInitException e1) {
			System.err.println("-->" + e1.getMessage());
		}
        return browser;
	}
	
	
	public void run(IViewPart view) {
		RunBrowserAction runBrowserAction = new RunBrowserAction(view,this);
		runBrowserAction.run(); 
	}
	
	
	public IStatus runInBatch(IViewPart view,IProgressMonitor monitor) {
		if(!isNotRunInBatch()) {
			//run(view,monitor);
			saveFile();
		}
		return Status.OK_STATUS;
	}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * 
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if (!(prototype instanceof HtmlComposite)) {
			System.err
					.println("[HtmlComposite.setPropertyLike] prototype NOT instanceof HtmlComposite!");
			return;
		}
		setHeader(((HtmlComposite) prototype).getHeader());
		setFooter(((HtmlComposite) prototype).getFooter());
		setFileName(((HtmlComposite) prototype).getFileName());
	}

	
	/**
	 * Конструирует контент проходя по 
	 * всем дочерним узлам 
	 */
	protected void makeContent() {
		content = "";

		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] instanceof IHtmlPart) {
				content += ((IHtmlPart) getChildren()[i]).getFullHtmltext();
			}
		}

	}

	// DFIXME: dispose m_parent!!!
	Composite m_parent = null;
	
	public void refreshComposite() {
		if(m_parent!=null) {
			m_parent.layout();
			m_parent.redraw();
			System.out.println("refreshComposite()");
		}
	}
	
	
	public void disposeComposite() {
		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] instanceof ICompositeProvider) {
				((ICompositeProvider) getChildren()[i]).disposeComposite();
			}
		}
		m_parent = null;
	}
	
	
	/**
	 * Конструирует контент проходя по всем дочерним узлам ICompositeProvider
	 */
	public void makeComposite(Composite parent, ViewPart viewPart,IProgressMonitor monitor) {
		m_parent = parent;
		
        Browser browser = new Browser(parent, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		browser.setText(getFullHtmltext());
		browser.refresh();
        
		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] instanceof ICompositeProvider) {
				((ICompositeProvider) getChildren()[i]).makeComposite(parent,viewPart,monitor);
			}
		}
	}

	
	public File getFile() {
		if (file_name == null || file_name.equals("")) {
			return null;
		}
		String fn = TreeUtils.doAllSubstitutions(this,file_name);
		File file = new File(fn);
		if (!file.isAbsolute()) {
			// -- строим файл от текущего каталога
			String file_nm = getConfigTree().getConfexDir() + fn;
			file = new File(file_nm);
		}
		return file;
	}
	
	
	/**
	 * Rebuild full html text and save it into file
	 */
	public void saveFile() {
		File file = getFile();
		
		if (file== null) {
			return;
		}
		Utils.writeStringToFile(getFullHtmltext(), file);
	}
	
	
	/**
	 * Save Html text into file
	 * @param full_html_text
	 */
	protected void saveFile(String full_html_text) {
		File file = getFile();
		
		if (file== null) {
			return;
		}
		Utils.writeStringToFile(full_html_text, file);
	}

	
	/**
	 * Возвращает полный текст html составленный из трех частей
	 * header+content+footer
	 * 
	 * составной htmltext из всех родительских
	 * 
	 * @return
	 */
	public String getFullHtmltext() {
		if(isNotRunInBatch())
			return "";
		makeContent();
		String ret_str = header + content + footer;
		return TreeUtils.doAllSubstitutions(this, ret_str);
	}


	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);

		if (property.getNodeName().equals("header")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setHeader(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("footer")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setFooter(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("file_name")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setFileName(Utils.fromHtmlSpecialEntities(text.trim()));
		}
	}

	
	public String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if (header != null && !header.equals("")) {
			str_xml += "<header>\n";
			str_xml += Utils.toHtmlSpecialEntities(header) + "\n";
			str_xml += "</header>\n";
		}
		if (footer != null && !footer.equals("")) {
			str_xml += "<footer>\n";
			str_xml += Utils.toHtmlSpecialEntities(footer) + "\n";
			str_xml += "</footer>\n";
		}
		if (file_name != null && !file_name.equals("")) {
			str_xml += "<file_name>\n";
			str_xml += Utils.toHtmlSpecialEntities(file_name) + "\n";
			str_xml += "</file_name>\n";
		}

		return str_xml;
	}
	

	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new HtmlCompositePropertyDialog(shell);
	}
	
	
	public String getContent() {
		return content;
	}
	

	public void setContent(String content) {
		this.content = content;
	}

	
	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFileName() {
		return file_name;
	}

	public void setFileName(String file_name) {
		this.file_name = file_name;
	}

}
