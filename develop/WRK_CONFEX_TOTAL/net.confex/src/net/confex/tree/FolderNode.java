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

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import net.confex.directedit.FolderNodePropertyDialog;
import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.utils.Strings;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.confex.views.FolderView;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.w3c.dom.Node;

/**
 * Узел "Каталог" 
 * 
 * Представляет собой ссылку на реальный каталог файловой системы.
 * При его активации происходит открытие Вида Каталога
 * 
 * 
 * @author Roman_Eremeev
 *
 */

public class FolderNode extends TreeNode  {
	
	//-- path at folder
	private String path = "";
	
	public String getAboutString() { return Translator.getString("ABOUT_FOLDERNODE"); }
	
	protected static String default_image_name = "eclipse icons/obj16/fldr_obj.gif"; 
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	public FolderNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	/*
	public FolderNode(ConfigTree configTree, String name, String path) {
		super(configTree,name);
		this.path = path;
	}*/

	
	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new FolderNode(getConfigTree(),null); 
	}
	

	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof FolderNode)) {
			System.err.println("[FolderNode.setPropertyLike] prototype NOT instanceof FolderNode!");
			return;
		}
		setPath(((FolderNode)prototype).getPath());
	}

	
	/**
	 * Folder node not running in batch mode 
	 */
	public void runInBatch(IViewPart view) {
		//run(navigation_view);
	}
	
	
	public FolderView runBrowser(IWorkbenchPage page) {
		FolderView viewer = null;
        try {
        	String url = this.getPath();
        	String id = url;
        	id = id.replaceAll(":", "_");
        	id = id.replaceAll("/", "_");
        	id = Strings.replace(id,"\\", "_");
        	//System.out.println("-->" + url + "   " + id);
        	viewer = (FolderView) page.showView(FolderView.VIEW_ID, id,
        			IWorkbenchPage.VIEW_ACTIVATE);
            if (viewer != null) {
            	viewer.setTitlePartName(getName()); //url.trim());
            	String path = TreeUtils.doAllSubstitutions(this,url.trim());
                viewer.setPath(path);
            }
        //} catch(PartInitException e1) {
        } catch(Exception e1) {
        	System.err.println("-->" + e1.getMessage());
        }
        return viewer;
	}

	
	public void run(IViewPart view) {
		//RunBrowserAction runBrowserAction = new RunBrowserAction(navigation_view,this);
		//runBrowserAction.run();
		IWorkbenchPage page = view.getSite().getPage();
		runBrowser(page);
	}

	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	

	/**
	 * Если текущий url не начинается с http: то конструируем 
	 * составной url из всех родительских
	 * @return
	 */
	public String getFullUrl() {
		if(path.toLowerCase().startsWith("http:", 0)) {
			return path;
		} else {
			String parent_url = ""; 
			ITreeNode p = getParent();
			while(p!=null) {
				if(p instanceof FolderNode) {
					parent_url = ((FolderNode)p).getFullUrl();
					break;
				}
				p = p.getParent();
			}
			String s_url = parent_url + TreeUtils.doAllSubstitutions(this,path);
			return s_url;
		}
	}
	
	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if(path!=null 
				&& !path.equals("")) {
			str_xml += "<path>\n"
				+Utils.toHtmlSpecialEntities(path)+"\n"; 
			str_xml += "</path>\n"; 
		}
		return str_xml;
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("path")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setPath(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
	}
	
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new FolderNodePropertyDialog(shell);
	}


	
	
	/*
	public void openPropertyDialog(NavigationView view, Shell shell,
			boolean new_flg) {
		if(property_dialog==null) {
			property_dialog = new FolderNodePropertyDialog(shell);
		}
		property_dialog.setNewFlg(new_flg);
		property_dialog.createSShell(); 
		property_dialog.setView(view);
		property_dialog.show();
		property_dialog.setTreeNode(this);
	}*/
	

}
