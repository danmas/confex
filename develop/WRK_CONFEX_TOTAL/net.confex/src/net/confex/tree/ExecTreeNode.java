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

import net.confex.directedit.ExecPropertyDialog;
import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.utils.Executor;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.w3c.dom.Node;

public class ExecTreeNode extends TreeNode {
	
	private String command = "";
	private String arguments = "";
	private String work_dir = "";
	
	
	public String getAboutString() { 
		return Translator.getString("ABOUT_RUNTREEOBJECT"); 
	}
	
	
	protected static String default_image_name = "etool16/run.gif"; 
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	
	
	public ExecTreeNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	/*
	public ExecTreeNode(ConfigTree configTree, String name, String command, String arguments, String current_dir) {
		super(configTree,name);
		this.command = command;
		this.arguments = arguments;
		this.work_dir = current_dir;
	}*/

	
	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new ExecTreeNode(getConfigTree(),null); 
	}
	
	
	public void run(IViewPart view) {
		Executor executor = new Executor();
		
		String c = TreeUtils.doAllSubstitutions(this,command);
		String a = TreeUtils.doAllSubstitutions(this,arguments);
		String wd = TreeUtils.doAllSubstitutions(this,work_dir);
		
		/*int ret = */executor.userRunAsync(c, a, wd,
		view.getSite().getShell());
		
		/*
		if(ret!= -1) {
			String s_msg = command + " " + arguments + "\n\n" + 
			Translator.getTranslation("MESSAGE_DONE");
		
			MessageDialog.openInformation(navigation_view.getSite().getShell(),
				Translator.getTranslation("MESSAGEBOX_TITLE_INFORMATION"), s_msg);
		}*/
	}

	
	public IStatus run(IViewPart view,IProgressMonitor monitor) {
		run(view);
		return Status.OK_STATUS;
	}
	
	//public void run(IViewSite view_site) {
	//}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof ExecTreeNode)) {
			System.err.println("[ExecTreeNode.setPropertyLike] prototype NOT instanceof ExecTreeNode!");
			return;
		}
		setWorkDir(((ExecTreeNode)prototype).getWorkDir());
		setCommand(((ExecTreeNode)prototype).getCommand());
		setArguments(((ExecTreeNode)prototype).getArguments());
		
	}

	
	/*
	public void loadAttribFromXml(Node node,ITreeNode parent) {
		super.loadAttribFromXml(node,parent);
		
		Node attr_command=node.getAttributes().getNamedItem("command");
		if(attr_command==null) {
			System.err.println(" Can't read attribute command for " + this.toString());
		} else {
			String s_command=attr_command.getNodeValue();
			this.setHtmltext(Utils.fromHtmlSpecialEntities(s_htmltext));
			//System.out.println("attrib htmltext= "+s_htmltext);
		}
	}*/
	
	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("command")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setCommand(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
		if (property.getNodeName().equals("arguments")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setArguments(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
		if (property.getNodeName().equals("work_dir")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setWorkDir(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
	}
	
	
	public String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if(command!=null 
				&& !command.equals("")) {
			str_xml += "<command>\n";
			str_xml += Utils.toHtmlSpecialEntities(command)+"\n"; 
			str_xml += "</command>\n"; 
		}
		if(work_dir!=null 
				&& !work_dir.equals("")) {
			str_xml += "<work_dir>\n";
			str_xml += Utils.toHtmlSpecialEntities(work_dir)+"\n"; 
			str_xml += "</work_dir>\n"; 
		}
		if(arguments!=null 
				&& !arguments.equals("")) {
			str_xml += "<arguments>\n";
			str_xml += Utils.toHtmlSpecialEntities(arguments)+"\n"; 
			str_xml += "</arguments>\n"; 
		}
		
		return str_xml;
	}
	
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new ExecPropertyDialog(shell);
	}
	
	
	public String getCommand() {
		return command;
	}

	
	public void setCommand(String command) {
		this.command = command;
	}
	

	public String getWorkDir() {
		return work_dir;
	}

	
	public void setWorkDir(String current_dir) {
		this.work_dir = current_dir;
	}

	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}
	

}
