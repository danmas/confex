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
package net.confex.tree;

import java.util.HashMap;

import net.confex.html.IHtmlPart;
import net.confex.html.ITextPart;
import net.confex.views.NavigationView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.w3c.dom.Node;

public interface ITreeNode extends IStateObservable, 
	ITreeNodeState, IHtmlPart, ITextPart, IScaner 
{
	
	/**
	 * Return confex tree contains this node.
	 * @return  
	 */
	public ConfigTree getConfigTree();
	
	public long getId();
	
	public void loadAttribFromXml(Node node,ITreeNode parent);
	
	/**
	 * Загружает внутренние параметры из DOM дерева
	 * @param node 
	 * @param parent 
	 */
	public void loadFromXml(Node node,ITreeNode parent, boolean new_node);

	/*
	 * Return full xml for this node include children 
	 * 
	 * @param read_src_text - read from source files before 
	 */
	public String getXml(boolean read_src_text);
	
	/**
	 * Return the exactly this xml presentation without child
	 * @param read_src_text
	 * @return
	 */
	public String getXmlTextWithoutChild(boolean read_src_text);
	
	public void setParent(ITreeNode parent);

	public ITreeNode getParent();
	
	public ITreeNode getRoot();

	public void removeChild(ITreeNode child);

	public void removeChildren();
	
	public ITreeNode[] getChildren();
	
	
	/**
	 * Search child by name
	 * @param child name
	 * @return
	 */
	public ITreeNode getChildWithName(String name);
	
	public boolean hasChildren();
	
	public String getIconFileName();

	public String getName();
	
	/**
	 *   Здесь производится подстановка для текста узла в пользовательской моде
	 */
	public String toString(boolean edit_mode);
	
	/**
	 * run() node in foreground
	 */
	public void run(IViewPart view);
	
	/**
	 * Run node in foreground if monitor is null 
	 * otherwise in background async mode
	 * 
	 * @param view
	 * @param monitor
	 * @return IStatus - status of node after executing
	 */
	public IStatus run(IViewPart view,IProgressMonitor monitor);
	
	
	/**
	 * Build new monitor(IProgressMonitor) and run node as batch 
	 * in background 
	 * @param view
	 */
	public void runNodeAsyncBatch(IViewPart view);
	
	
	/**
	 * Build new monitor(IProgressMonitor) and run node as batch 
	 * in background 
	 * @param view
	 * @param name the name of the job.
	 */
	public void runNodeAsyncBatch(IViewPart view, String job_name);
	
	
	/**
	 * Run node with children in foreground 
	 * first of all executing children after itself 
	 * @param view  
	 */
	public void runBatch(IViewPart view);
	
	/**
	 * Run node with children in background async mode
	 * first of all executing children after itself 
	 * @param view  
	 */
	public IStatus runBatch(IViewPart view,IProgressMonitor monitor);
	
	public void runInBatch(IViewPart view);

	public IStatus runInBatch(IViewPart view,IProgressMonitor monitor);
	
	public void openPropertyDialog(NavigationView viewer, Shell shell, 
			boolean new_flg);
	
	/**
	 * Opens source editor for this node if src file exists
	 *  
	 * @return true if open editor
	 */
	public boolean openSrcEditor();
	
	public void addChild(ITreeNode child);
	
	/**
	 * Вставить new_child перед before_child
	 * Если before_child не указан то просто добавляем new_child
	 *  
	 * @param new_child
	 * @param before_child
	 */
	public void addChild(ITreeNode new_child, ITreeNode before_child);
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype);
	
	/**
	 * Является ли текущий одним из предков узла node
	 * 
	 * @param node
	 * @return
	 */
	public boolean isParentOf(ITreeNode node);
	
	/**
	 * Добавляем дочерние узлы из xml кода
	 * 
	 * Если задан before_child то вставить узлы из xml перед before_child
	 * @param xml
	 */
	public void addChildXml(String xml, ITreeNode before_child);
	
	/**
	 * Вставить узлы из xml перед before_child
	 * 
	 * @param xml
	 * @param before_child
	 */
	//public void addBeforeChildXml(String xml, ITreeNode before_child);
	
	/**
	 * 	Cоздает экземпляр того же класса 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode();

	
	public int getChildNumber(ITreeNode node);

	/**
	 * Составляет ключ из имен
	 * Пример ключа:  C:\Config\confex.confex~…~Первый вариант~…~Эклипс~…~
	 *	
	 * @return
	 */
	public String getStringKey();

	/**
	 * Заполнить hash map для всех объектов дерева 
	 * @param for_state
	 */
	public void fillHashMap(HashMap<String,ITreeNode> hash_map);
	
	/**
	 * Search node with given name up to tree.
	 *  
	 * @param node_name
	 * @return null if not found
	 */
	public ITreeNode searchNodeUp(String node_name); 
	
	/**
	 * Set value of raviable with var_name in up direction
	 * 
	 * @param var_name
	 * @param value     
	 */
	public void setVarUp(String var_name, String value);
	
	/**
	 * Returns value of variable with given name up to tree.
	 * 
	 * @param var_name
	 * @return
	 */ 
	public String getStrVarUp(String var_name);
	
	/**
	 * Custom method with two params  
	 * @param par1
	 * @param par2
	 * @param par3
	 * @return
	 */
	public Object customMethod3(Object par1, Object par2, Object par3);
}
