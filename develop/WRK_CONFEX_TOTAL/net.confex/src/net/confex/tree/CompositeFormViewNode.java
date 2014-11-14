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

import net.confex.directedit.FormViewNodePropertyDialog;
import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.utils.Strings;
import net.confex.utils.TreeUtils;
import net.confex.views.CompositeFormView;
import net.confex.views.NavigationView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.w3c.dom.Node;

public class CompositeFormViewNode  extends TreeNode {
	
	//-- path at folder
	//private String path = "";
	
	public String getAboutString() { return Translator.getString("ABOUT_FORMVIEWNODE"); }
	
	protected static String default_image_name = "eview16/report.png"; 
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	public CompositeFormViewNode(ConfigTree configTree, IStateObserver stateObserver) {
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
		return new CompositeFormViewNode(getConfigTree(),null); 
	}
	

	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof CompositeFormViewNode)) {
			System.err.println("[CompositeFormViewNode.setPropertyLike] prototype NOT instanceof CompositeFormViewNode!");
			return;
		}
		//setPath(((FolderNode)prototype).getPath());
	}

	
	class MyRunnable implements Runnable {
		String id;
		IWorkbenchPage p;
		CompositeFormView v;
		
		public CompositeFormView getV() {
			return v;
		}

		MyRunnable(String _id, IWorkbenchPage _p) {
			id = _id;
			p = _p;
		}
		
		public void run() {
			try {
        	v = (CompositeFormView) p.showView(CompositeFormView.VIEW_ID, id,
        			IWorkbenchPage.VIEW_ACTIVATE);
	        } catch(Exception e1) {
	        	System.err.println("[CompositeFormView.runBrowser]-->" + e1.getMessage());
	        }
		}
	}
	
	
	public CompositeFormView runBrowser(IWorkbenchPage page
			,IViewPart view
			,IProgressMonitor monitor) {
		CompositeFormView viewer = null;
		CompositeFormView v;
		final IWorkbenchPage p = page;
		final IProgressMonitor m = monitor;
        try {
    		if(monitor!=null) {
    			monitor.worked(1);
    			monitor.subTask("Substitute all variables... " + 1);
    			if (monitor.isCanceled())
    				return viewer; // Status.CANCEL_STATUS;			
    		}
        	String id = getName();
        	id = id.replaceAll(":", "_");
        	id = id.replaceAll("/", "_");
        	id = Strings.replace(id,"\\", "_");
        	final String final_id = id; 
    		if(monitor!=null) {
    			monitor.worked(5);
    			monitor.subTask("Build view... ");
    			if (monitor.isCanceled())
    				return viewer; // Status.CANCEL_STATUS;			
    		}
			final MyRunnable mr = new MyRunnable(id,p);
			final CompositeFormViewNode thi = this; 
    		if(monitor!=null) {
    			monitor.worked(7);
    			monitor.subTask("Open view... ");
    			if (monitor.isCanceled())
    				return viewer; // Status.CANCEL_STATUS;			
	    		view.getSite().getShell().getDisplay().syncExec(mr);
	    		viewer = mr.getV();
    		} else {
    			String s = TreeUtils.doAllSubstitutions(this,final_id);
    			viewer = (CompositeFormView)p.showView(CompositeFormView.VIEW_ID, s,
	        			IWorkbenchPage.VIEW_ACTIVATE);
    			//viewer = null;
    		}
            if (viewer != null) {
            	String s = TreeUtils.doAllSubstitutions(this,getName());
            	viewer.setTitlePartName(s); //url.trim());
        		if(monitor!=null) {
        			monitor.worked(15);
        			monitor.subTask("Make Form content... ");
        			if (monitor.isCanceled())
        				return viewer; // Status.CANCEL_STATUS;	
        			
    	    		//navigation_view.getShell().getDisplay().syncExec(new Runnable() {
    	    		//	public void run() {
    	                    mr.getV().makeContent(thi,m);
    	                    mr.getV().setFocus();
    	    		//	}
    	    		//});
        		} else {
        			viewer.makeContent(thi,monitor);
                    viewer.setFocus();
        		}
    			if(monitor!=null) {
  			      monitor.done();
    			}
            }
        } catch(Exception e1) {
        	System.err.println("[CompositeFormView.runBrowser]-->" + e1.getMessage());
        }
        return viewer;
	}

	
	public IStatus run(IViewPart view,IProgressMonitor monitor) {
		if(monitor==null) {
			IWorkbenchPage page = view.getSite().getPage();
			runBrowser(page,view,monitor);
		}
		return Status.OK_STATUS;
	}
	
	
	/*
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	*/
	

	/**
	 * Если текущий url не начинается с http: то конструируем 
	 * составной url из всех родительских
	 * @return
	 */
	public String getFullUrl() {
		/*if(path.toLowerCase().startsWith("http:", 0)) {
			return path;
		} else*/ {
			String parent_url = "";
			ITreeNode p = getParent();
			while(p!=null) {
				if(p instanceof FolderNode) {
					parent_url = ((FolderNode)p).getFullUrl();
					break;
				}
				p = p.getParent();
			}
			String s_url = parent_url; // + path;
			
			return s_url;
		}
	}
	
	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		/*
		if(path!=null 
				&& !path.equals("")) {
			str_xml += "<path>\n"
				+Utils.toHtmlSpecialEntities(path)+"\n"; 
			str_xml += "</path>\n"; 
		}*/
		return str_xml;
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		/*
		if (property.getNodeName().equals("path")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setPath(Utils.fromHtmlSpecialEntities(text.trim()));
		}*/ 
	}
	
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new FormViewNodePropertyDialog(shell);
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
