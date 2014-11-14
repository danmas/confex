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

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.confex.application.ConfexPlugin;
import net.confex.directedit.IPropertyDialog;
import net.confex.directedit.NodePropertyDialog;
import net.confex.html.IHtmlPart;
import net.confex.html.ITextPart;
import net.confex.translations.Logger;
import net.confex.translations.Translator;
import net.confex.utils.ImageResource;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.confex.views.NavigationView;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;



public class TreeNode  implements ITreeNode  { 

	public static final String STRING_KEY_SEPARATOR = "~…~";
	
	private ArrayList<ITreeNode> children;

	private ITreeNode parent;

	private String name = "";

	private String icon_file_name = getDefaultImage();
	
	
	protected long id = -1; //-- текущий номер элемента в схеме
	public long getId(){ return id; }

	//protected int origin_id = -1; //-- уникальный номер элемента в схеме в момент создания
	//public int getOriginId(){ return origin_id; }
	
	protected static String default_image_name = "non_eclipse/crystal-folder-box-1.png"; 
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	
	private String tooltiptext = "";      //-- всплывающа подсказка 

	private ConfigTree configTree = null; //-- собственник дерева
	
	private boolean not_run_in_batch = false; //-- не выполнять в пакете
	
	public boolean isNotRunInBatch() {
		return not_run_in_batch;
	}
	
	
	public boolean isUsed() {
		return !not_run_in_batch;
	}
	
	

	public void setNotRunInBatch(boolean not_run_in_batch) {
		this.not_run_in_batch = not_run_in_batch;
	}

	
	public ITreeNode getRoot() {
		if(configTree==null)
			return null;
		return configTree.getRoot();
	}
	
	
	private boolean not_vis_in_user_mode = false;
	
	public boolean isNotVisInUserMode() {
		return not_vis_in_user_mode;
	}
	
	public void setNotVisInUserMode(boolean not_vis_in_user_mode) {
		this.not_vis_in_user_mode = not_vis_in_user_mode;
	}
	
	
	
	private TreeNode(ConfigTree configTree) {
		super();
		this.configTree = configTree;
		children = new ArrayList<ITreeNode>();
		//-- здесь задается иконка по умолчанию для класса
		setIcon(getDefaultImage());
	}

	
	public TreeNode(ConfigTree configTree, IStateObserver stateObserver) {
		this(configTree);
		if(stateObserver!=null)
			addStateObserver(stateObserver);
	}

	
	public TreeNode(ITreeNode tree_node) {
		this(tree_node.getConfigTree());
	}
	
	
	//public TreeNode(ConfigTree configTree, String name) {
	//	this(configTree);
	//	this.name = name;
		//children = new ArrayList();
	//}
	
	
	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new TreeNode(getConfigTree()); 
	}
	
	
	//public String getDefaultImage() {
	//	return "non_eclipse/crystal-folder-box-1.png";   //ISharedImages.IMG_OBJ_FOLDER;
	//}

	
	public String getAboutString() { return Translator.getString("ABOUT_TREE_NODE"); }
	

	public void setTooltipText(String tooltiptext) {
		this.tooltiptext = tooltiptext;
	}
	public String getTooltipText()	{
		return tooltiptext;
	}

	
	public int getChildNumber(ITreeNode node) {
		return children.indexOf(node);
	}
	
	
	/**
	 * Составляет ключ из имен
	 * Пример ключа:  C:\Config\confex.confex~…~Первый вариант~…~Эклипс~…~
	 *	
	 * @return
	 */
	public String getStringKey() {
		if(parent!=null) {
			return parent.getStringKey() 
			  + STRING_KEY_SEPARATOR + this.toString()
			  +"_"+parent.getChildNumber(this);
		} else {
			return this.toString();
		}
	}
	
	
	/** ===================================================================
	 *   STATE
	 */
	
	private int run_state = STATE_NORMAL;
	private int lock_state = STATE_NORMAL;
	private int not_run_in_batch_state = STATE_NORMAL; 
	
	/**
	 * Возвращает true если объект находится в состоянии for_state
	 */
	public boolean isInRuningState(int for_state) {
		if(for_state==STATE_ANY)
			return true;
		
		return run_state==for_state;
		/*
		if(for_state == STATE_LOCKED)
		if((for_state & getState())!=0) 
			return true;
		
		return false;
		*/
	}

	
	/*
	public boolean isInLockingState(int for_state) {
		if(for_state==STATE_ANY)
			return true;
		
		return lock_state==for_state;
	}*/
	
	
	public boolean isLocked() {
		ITreeNode parent = getParent(); 
		if(parent != null && parent.isLocked())
			return true;
		return (lock_state & STATE_LOCKED)!=0;
	}

	
	//public int  getState() {
	//	return state;
	//}

	//private void setState(int state) {
	//	this.state = state;
	//	//ConfexRunningDecorator.updateDecorators(this);
	//}

	public void setLockState() {
		lock_state = STATE_LOCKED;
		updateStateOservers();
		//-- все дочерние тоже должны стать заблокированы
		//ITreeNode[] children = getChildren();
		//for(int i=0; i<children.length;i++) {
		//	children[i].setLockState();
		//}
	}
	public void clearLockState() {
		lock_state = STATE_NORMAL;
		updateStateOservers();
		//-- все дочерние тоже должны быть разаблокированы
		//ITreeNode[] children = getChildren();
		//for(int i=0; i<children.length;i++) {
		//	children[i].clearLockState();
		//}
	}

	
	public void setNotRunBatchState() {
		setNotRunInBatch(true);
		updateStateOservers();
	}
	public void clearNotRunBatchState() {
		setNotRunInBatch(false);
		updateStateOservers();
	}

	
	public void setRunState() {
		if(isInRuningState(STATE_ERROR)) {
			clearErrorState();
		}
		run_state = STATE_RUN;
		updateStateOservers();
	}
	
	public void setNormalState() {
		if(isInRuningState(STATE_ERROR)) {
			clearErrorState();
		}
		run_state = STATE_NORMAL; //state & (~STATE_RUN);
		updateStateOservers();
	}

	
	public void setSuccessState() {
		if(isInRuningState(STATE_ERROR)) {
			clearErrorState();
		}
		run_state = STATE_SUCCESS;
		updateStateOservers();
	}

	
	public void setErrorState() {
		run_state = STATE_ERROR;
		updateStateOservers();
		//-- по цепочке передаем ошибку родителю
		if(getParent()!=null)
			getParent().setErrorState();
	}
	public void clearErrorState() {
		run_state = STATE_NORMAL;
		updateStateOservers();
		//-- проверяем родителя если нет больше ошибок то он
		//   становится нормальным
		if(getParent()!=null)
			getParent().resetErrorStateByChildren();
	}

	
	//-- проверяем родителя если нет больше ошибок то он
	//   становится нормальным
	public void resetErrorStateByChildren() {
		ITreeNode[]	children = getChildren();
		for(int i=0;i<children.length;i++) {
			if(children[i].isInRuningState(STATE_ERROR)) {
				return;
			}
		}
		clearErrorState();
	}

	
	private ArrayList<IStateObserver> observer_list = new ArrayList<IStateObserver>();
	
	
	public void addStateObserver(IStateObserver observer) {
		observer_list.add(observer);
	}

	
	/*
	private void removeStateObserver(IStateObserver observer) {
		observer_list.remove(observer);
	}*/
	
	
	public void removeAllStateObservers()  {
		observer_list.clear();
	}
	
	
	/**
	 * Устанавливаем для всех дочерних наблюдателя
	 * @param observer
	 */
	public void setObserverForAll(IStateObserver observer) {
		ITreeNode[] children = getChildren();
		for(int i=0; i<children.length;i++) {
			children[i].addStateObserver(observer);
			children[i].setObserverForAll(observer);
		}

	}
	

	/**
	 * Известить всех наблюдателей о изменении состояния
	 */
	public void updateStateOservers() {
		for(int i=0; i<observer_list.size(); i++) {
			((IStateObserver)observer_list.get(i)).update(this);
		}
	}
	
	
	/**
	 * Заполнить список для всех объектов дерева 
	 * в определенном состоянии
	 * STATE_ANY - значит состояние не важно
	 * 
	 * @param sate_list
	 * @param for_state
	 */
	@SuppressWarnings("unchecked")
	public void fillStateList(List sate_list,int for_state) {
		if(isInRuningState(for_state)) {
			sate_list.add(this);
		} 
		
		for(Iterator iter= children.iterator();iter.hasNext();) {
			Object obj = iter.next();
        	if(obj instanceof ITreeNodeState) {
        		((ITreeNodeState)obj).fillStateList(sate_list,for_state);
        	}
		}
	}
	
	
	
	/**
	 * Возвращает детский лепет этого узла
	 */
	public String getChildrensText(Class _interface) {
		String ret_str = "";
		for(Iterator iter= children.iterator();iter.hasNext();) {
			ITreeNode obj = (ITreeNode)iter.next();
       		ret_str += obj.getChildrensText(_interface);
		}
		return ret_str;
	}
	
	
	/**
	 * Ищет сына по имени
	 * @param name
	 * @return
	 */
	public ITreeNode getChildWithName(String name) {
		for(Iterator iter= children.iterator();iter.hasNext();) {
			
			ITreeNode obj = (ITreeNode)iter.next();
       		if(obj.getName().equals(name))
       			return obj;
		}
		return null;
	}

    
	public ITreeNode searchNodeUp(String node_name) {
		return TreeUtils.searchNodeUp(this,node_name);
	}
	
	
	/**
	 * Обработка закрытия вида
	 */
	protected void onClosedPart(IWorkbenchPart part) {
    	//setState(STATE_NORMAL); 
    	//System.out.println("STATE_NORMAL!!!!!!!!!");
    	//((WebBrowserView)part).removePartListener(this);
	}
	

	/**
	 * Заполнить hash map для всех объектов дерева 
	 * в определенном состоянии
	 * STATE_ANY - значит состояние не важно
	 * 
	 * @param hash_map
	 * @param for_state
	 */
	public void fillHashMap(HashMap<String,ITreeNode> hash_map) {
		hash_map.put(getStringKey(), this);
		
		for(Iterator<ITreeNode> iter= children.iterator();iter.hasNext();) {
			Object obj = iter.next();
        	if(obj instanceof ITreeNode) {
        		((ITreeNode)obj).fillHashMap(hash_map);
        	}
		}
	}

	
	public void run(IViewPart view) {
		run(view,null);
	}


	public IStatus run(IViewPart view,IProgressMonitor monitor) {
		return Status.OK_STATUS;
	}


	public void runNodeAsyncBatch(IViewPart view) {
		String job_name = "Asynchrous running: "+this.getName();
		runNodeAsyncBatch(view,job_name);
	}
	

	public void runNodeAsyncBatch(IViewPart view, String job_name) {
		final ITreeNode this_node = this;
		final IViewPart local_view = view;
		final String local_job_name = job_name;
		
	    try {
		    Display d = view.getSite().getShell().getDisplay();
	        d.asyncExec(new Runnable() {
	        	public void run() {
	            	Job job = new Job(local_job_name) {
	          		  	protected IStatus run(IProgressMonitor monitor) {
		          			//final IProgressMonitor m = monitor;  
		        	        if(monitor==null) {
		      	        	    System.err.println("monitor==null");
		      	        	    return Status.CANCEL_STATUS;
		      	        	}
		          		    monitor.beginTask(local_job_name, 100);
		          		    this_node.runBatch(local_view, monitor);  
		          		    monitor.done();
		          		    return Status.OK_STATUS;
	          		  	}
	        		};
	       		job.setUser(true);
	       		job.schedule();    	
	        	}
	        });
	    } catch(Exception e) {
	    	System.err.println(e.getMessage());
	    }
	}
	
	
	public IStatus runBatch(IViewPart view,IProgressMonitor monitor) {
		ITreeNode[] children = getChildren();
		for(int i=0; i<children.length;i++) {
			if(!children[i].isNotRunInBatch())
				children[i].runBatch(view,monitor);
		}
		//-- теперь для самого 
		runInBatch(view,monitor);
		return Status.OK_STATUS;
	}
	
	
	public void runBatch(IViewPart view) {
		runBatch(view,null);
	}
	
	
	public void runInBatch(IViewPart view) {
		runInBatch(view,null); 
	}
	
	
	public IStatus runInBatch(IViewPart view,IProgressMonitor monitor) {
		if(!isNotRunInBatch())
			run(view,monitor);
		return Status.OK_STATUS;
	}
	
	
	protected IPropertyDialog property_dialog = null;
	

	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new NodePropertyDialog(shell, this);
	}
	
	
	public void openPropertyDialog(NavigationView view, Shell shell,
			boolean new_flg) {
		if(property_dialog==null) {
			property_dialog = newPropertyDialog(shell);
			property_dialog.createSShell(); 
			property_dialog.setView(view);
		}
		property_dialog.setNewFlg(new_flg);
		property_dialog.setTreeNode(this);
		property_dialog.show();
	}

	
	/**
	 * Opens source editor for this node if src file exists
	 *  
	 * @return true if open editor
	 */
	public boolean openSrcEditor() {
		if(getSrcFileNameXml().trim().equals(""))
			return false;
		File file = getFile(getSrcFileNameXml());
		if(!file.exists()) {
			System.err.println("File ["+getSrcFileNameXml()+"] not exists!");
			return false;
		}
		try {
			IWorkbenchPage page = ConfexPlugin.getDefault()
				.getWorkbench().getActiveWorkbenchWindow().getActivePage();  
			//Utils.openExternalFile(page, file);
			Utils.openSrcFile(page, file);
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	
	public void setPropertyDialogNull() {
		property_dialog = null;
	}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		Assert.isLegal(prototype instanceof TreeNode);
		/*
		if(!(prototype instanceof TreeNode)) {
			System.err.println("[TreeNode.setPropertyLike] prototype NOT instanceof TreeNode!");
			return;
		}*/
		setName(prototype.getName());
		setTooltipText(((TreeNode)prototype).getTooltipText());
		setIcon_file_name(prototype.getIconFileName());
		if(prototype.isLocked()) {
			setLockState();
		}
		if(prototype.isNotRunInBatch()) {
			setNotRunInBatch(true);
		}
		if(prototype.isNotVisInUserMode()) {
			setNotVisInUserMode(true);
		}
		setSrcText(((TreeNode) prototype).getSrcText());
		setSrcFileNameXml(((TreeNode) prototype).getSrcFileNameXml());
	}
	
	
	public void addChild(ITreeNode child) {
		children.add(child);
		child.setParent(this);
	}
	
	
	/**
	 * Вставить new_child перед before_child
	 * Если before_child не указан то просто добавляем new_child
	 *  
	 * @param new_child
	 * @param before_child
	 */
	public void addChild(ITreeNode new_child, ITreeNode before_child) {
		if(before_child==null) {
			addChild(new_child);
		} else {
			int index = children.indexOf(before_child);
			if(index == -1) {
				addChild(new_child);
			} else {
				children.add(index, new_child);
				new_child.setParent(this);
			}
		}
	}
	
	
	/**
	 * Добавляем дочерние узлы из xml кода
	 * 
	 * Если задан before_child то вставить узлы из xml перед before_child
	 * @param xml
	 */
	public void addChildXml(String xml, ITreeNode before_child) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			
			Document doc = db.parse(is);
			
			//-- грузим 
			for (int i = 0; i < doc.getDocumentElement().getChildNodes().getLength(); i++) {
				Node child = doc.getDocumentElement().getChildNodes().item(i);
				getConfigTree().parseXml(child, this, before_child,true);
		}
		} catch (Exception ex) {
			System.err.println("ERROR TreeNode.addChildXml() "+ ex.getMessage());
		}
	}

	
	/**
	 * Ищем вверх по всем наборам пременных пока не найдем переменную с 
	 * именем var_name ей присваиваем значение value
	 */
	public void setVarUp(String var_name, String value) {
		TreeUtils.setVarUp(this,var_name, value);
	}
	
	
	public String getStrVarUp(String var_name) {
		return TreeUtils.getStrVarUp(this,var_name);
	}
	

	
	/**
	 * Вставить new_child перед before_child
	 * 
	 * @param new_child
	 * @param before_child
	 */
	/*public void addBeforeChild(ITreeNode new_child, ITreeNode before_child) {
		int index = children.indexOf(before_child);
		if(index == -1) {
			addChild(new_child);
		} else {
			children.add(index, new_child);
			new_child.setParent(this);
		}
	}*/
	
	
	/**
	 * Вставить узлы из xml перед before_child
	 * 
	 * @param xml
	 * @param before_child
	 */
	/*public void addBeforeChildXml(String xml, ITreeNode before_child) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			
			Document doc = db.parse(is);
			
			//-- грузим 
			for (int i = 0; i < doc.getDocumentElement().getChildNodes().getLength(); i++) {
				Node child = doc.getDocumentElement().getChildNodes().item(i);
				getConfigTree().parseXml(child, this, before_child);
			}
		} catch (Exception ex) {
			System.err.println("ERROR TreeNode.addChildXml() "+ ex.getMessage());
		}
	}*/
	
	
	/**
	 * Является ли текущий одним из предков узла node
	 * @param node
	 * @return
	 */
	public boolean isParentOf(ITreeNode node) {
		ITreeNode p = node.getParent();
		
		while(p!=null) {
			if(p==this)
				return true;
			p = p.getParent();
		}
		return false;
	}

	
	/*
	public TreeNode addTreeParent(String name) {
		TreeNode child = new TreeNode(getConfigTree(),name);
		this.addChild(child);
		return child;
	}*/
	
	
	public String getName() {
		return name;
	}

	
	public void setParent(ITreeNode parent) {
		this.parent = parent;
	}

	
	public ITreeNode getParent() {
		return parent;
	}

	
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 *   Здесь производится подстановка для текста узла в пользовательской моде
	 */
	public String toString(boolean edit_mode) {
		if(edit_mode) {
			return getName();
		} else {
			return TreeUtils.doAllSubstitutions(this, getName());
		}
	}

	
	public String toString() {
		return getName();
	}
	
	
	public void removeChild(ITreeNode child) {
		if(child.isInRuningState(ITreeNode.STATE_ERROR)) {
			child.clearErrorState();
		}
		child.removeAllStateObservers();
		child.removeChildren();
		children.remove(child);
		child.setParent(null);
	}
	
	
	public void removeChildren() {
		//-- удаляем все дочерние узлы 
		ITreeNode[] children = getChildren();
		for(int i=0; i<children.length;i++) {
			children[i].removeAllStateObservers();
			children[i].removeChildren();
		}
		this.children.removeAll(this.children);
	}

	
	public ITreeNode[] getChildren() {
		return (ITreeNode[]) children.toArray(new ITreeNode[children.size()]);
	}
	
	
	public boolean hasChildren() {
		return children.size()>0;
	}

	
	public String getAttribXml() {
		String str_xml = "";
		str_xml += "id=\""+getId()+"\"\n"; 
		str_xml += "name=\""+Utils.toHtmlSpecialEntities(name)+"\"\n"; 
		if(this.icon_file_name!=null 
				&& !icon_file_name.equals("") 
				&& !icon_file_name.equals(this.getDefaultImage())) {
			str_xml += "icon=\""+this.icon_file_name+"\"\n"; 
		}
		if(this.not_run_in_batch) {
			str_xml += "not_run_in_batch=\"true\"\n"; 
		}

		if(this.not_vis_in_user_mode) {
			str_xml += "not_vis_in_user_mode=\"true\"\n"; 
		}

		return str_xml;
	}
	
	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = "";
		if(tooltiptext!=null 
				&& !tooltiptext.equals("")) {
			str_xml += "<tooltiptext>\n"
				+Utils.toHtmlSpecialEntities(tooltiptext)+"\n"; 
			str_xml += "</tooltiptext>\n"; 
		}
		if (getSrcFileNameXml() != null && !getSrcFileNameXml().equals("")) {
			str_xml += "<src_file_name>\n";
			str_xml += Utils.toHtmlSpecialEntities(getSrcFileNameXml()) + "\n";
			str_xml += "</src_file_name>\n";
			if(read_src_text) {
				//-- read source text from file !
				readFromSrcFile();
			}
		}
		return str_xml;
	}
	
	
	public String getClassName() {
		return getClass().getSimpleName();
	}
	
	
	public String getXml(boolean read_src_text) {
		String str_xml = "<"+getClassName()+"\n";
		
		str_xml += getAttribXml();
		str_xml += ">\n";
		
		str_xml += "<properties>\n"; 
		str_xml += getPropertiesXml(read_src_text);
		str_xml += "</properties>\n"; 
			
		str_xml += getChildrenXml(read_src_text);
		
		str_xml +="</"+getClassName()+">\n";

		return str_xml;
	}
	
	
	/**
	 * Return the exactly this xml presentation without child
	 * @param read_src_text
	 * @return
	 */
	public String getXmlTextWithoutChild(boolean read_src_text) {
		String str_xml = "<"+getClassName()+"\n";
		
		str_xml += getAttribXml();
		str_xml += ">\n";
		
		str_xml += "<properties>\n"; 
		str_xml += getPropertiesXml(read_src_text);
		str_xml += "</properties>\n"; 
			
		//!str_xml += getChildrenXml(read_src_text);
		
		str_xml +="</"+getClassName()+">\n";

		return str_xml;
	}
	
	
	public String getChildrenXml(boolean read_src_text) {
		String str_xml = "";
		//-- сохраняем все дочерние узлы 
		ITreeNode[] children = getChildren();
		for(int i=0; i<children.length;i++) {
			str_xml +=  children[i].getXml(read_src_text); 
		}
		return str_xml;
	}
	
	
	public void loadAttribFromXml_0_0_1(Node node,ITreeNode parent) {
		//super.loadAttribFromXml(node,parent);
		
		//-- get id saved in file
		Node attr_id=node.getAttributes().getNamedItem("id");
		if(attr_id==null) {
			System.err.println(" Can't read id for " + this.toString());
			id=configTree.getIdForUsing();
		} else {
			String s_id=attr_id.getNodeValue();
			int t_id= Integer.valueOf(s_id).intValue();
			//origin_id = t_id;
			id=configTree.getIdForUsing();
			//schema.setIdForUsing(t_id);
		}
		
		Node attr_name=node.getAttributes().getNamedItem("name");
		if(attr_name==null) {
		} else {
			String s_name=attr_name.getNodeValue();
			this.setName(Utils.fromHtmlSpecialEntities(s_name));
		}
		
		Node attr_tooltiptext=node.getAttributes().getNamedItem("tooltiptext");
		if(attr_tooltiptext==null) {
		} else {
			String s_tooltiptext=attr_tooltiptext.getNodeValue();
			this.setTooltipText(Utils.fromHtmlSpecialEntities(s_tooltiptext));
		}
		
		Node attr_icon=node.getAttributes().getNamedItem("icon");
		if(attr_icon==null) {
		} else {
			String s_icon=attr_icon.getNodeValue();
			this.setIcon(s_icon);
		}
				
		Node attr_not_run_in_batch=node.getAttributes().getNamedItem("not_run_in_batch");
		if(attr_not_run_in_batch==null) {
		} else {
			String s_not_run_in_batch=attr_not_run_in_batch.getNodeValue();
			if(s_not_run_in_batch.toLowerCase().equals("true")) {
				this.setNotRunInBatch(true);
			} else {
				this.setNotRunInBatch(false);
			}
		}
		Node attr_not_vis_in_user_mode=node.getAttributes().getNamedItem("not_vis_in_user_mode");
		if(attr_not_vis_in_user_mode==null) {
		} else {
			String s_not_vis_in_user_mode=attr_not_vis_in_user_mode.getNodeValue();
			if(s_not_vis_in_user_mode.toLowerCase().equals("true")) {
				this.setNotVisInUserMode(true);
			} else {
				this.setNotVisInUserMode(false);
			}
		}
	}

	
	public void loadAttribFromXml(Node node,ITreeNode parent) {
		//super.loadAttribFromXml(node,parent);
		
		Node attr_name=node.getAttributes().getNamedItem("name");
		if(attr_name==null) {
		} else {
			String s_name=attr_name.getNodeValue();
			this.setName(Utils.fromHtmlSpecialEntities(s_name));
		}
		
		Node attr_icon=node.getAttributes().getNamedItem("icon");
		if(attr_icon==null) {
		} else {
			String s_icon=attr_icon.getNodeValue();
			this.setIcon(s_icon);
		}
		
		Node attr_not_run_in_batch=node.getAttributes().getNamedItem("not_run_in_batch");
		
		if(attr_not_run_in_batch==null) {
		} else {
			String s_not_run_in_batch=attr_not_run_in_batch.getNodeValue();
			if(s_not_run_in_batch.toLowerCase().equals("true")) {
				this.setNotRunInBatch(true);
			} else {
				this.setNotRunInBatch(false);
			}
		}
		Node attr_not_vis_in_user_mode=node.getAttributes().getNamedItem("not_vis_in_user_mode");
		
		if(attr_not_vis_in_user_mode==null) {
		} else {
			String s_not_vis_in_user_mode=attr_not_vis_in_user_mode.getNodeValue();
			if(s_not_vis_in_user_mode.toLowerCase().equals("true")) {
				this.setNotVisInUserMode(true);
			} else {
				this.setNotVisInUserMode(false);
			}
		}
	}
	

	protected void parsePropertyXml(Node property, boolean new_node) {
		//super.parsePropertyXml(property);
		if (property.getNodeName().equals("tooltiptext")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setTooltipText(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("src_file_name")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setSrcFileNameXml(Utils.fromHtmlSpecialEntities(text.trim()));
			
			//-- save source text into file
			//if(new_node) {
			//	System.out.println("new! save source text into file"); 
			//	saveSrcFile(true);
			//}
			
		} else if (property.getNodeName().equals("file_name")) {  // FIXME: REMOVE This part
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setSrcFileNameXml(Utils.fromHtmlSpecialEntities(text.trim()));
		}
	}
	
	
	protected void parseProperties(Node properties, boolean new_node) {
		//-- грузим дочерние
		for (int i = 0; i < properties.getChildNodes().getLength(); i++) {
			Node child = properties.getChildNodes().item(i);
			parsePropertyXml(child, new_node);
		}
	}
	

	public void loadFromXml(Node node,ITreeNode parent, boolean new_node) {
		if(getConfigTree().getInputVersion().equals("0.0.1")) {
			loadFromXml_0_0_1(node,parent, new_node);
		} else {
			loadFromXml_0_0_2(node,parent, new_node);
		}
	}

	
	public void loadFromXml_0_0_2(Node node,ITreeNode parent, boolean new_node) {
		loadAttribFromXml(node,parent);
		//-- грузим дочерние
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node child = node.getChildNodes().item(i);
			if(child.getNodeName().equals("properties")) {
				//-- разбираем своиства
				parseProperties(child,new_node);
				//-- save source text into file
				if(new_node &&(getSrcFileNameXml()!=null&&!getSrcFileNameXml().equals(""))) {
					System.out.println("new! save source text into file"); 
					saveSrcFile(true);
				}
			} else {
				getConfigTree().parseXml(child, this,new_node);
			}
		}
	}


	public void loadFromXml_0_0_1(Node node,ITreeNode parent, boolean new_node) {
		loadAttribFromXml_0_0_1(node,parent);
		
		//-- грузим дочерние
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node child = node.getChildNodes().item(i);
			getConfigTree().parseXml(child, this,new_node);
		}
	}

	
	/**
	 * Регистрирует иконку для объекта
	 * @param icon_file_name
	 */
	public void setIcon(String icon_file_name) {
		this.icon_file_name = icon_file_name;
		if(ImageResource.getImageDescriptor(icon_file_name)==null)
			ImageResource.registerImage(icon_file_name,icon_file_name);
		//setImageKey(icon_file_name);
	}

	
	/**
	 * Конструирует контент проходя по всем дочерним узлам HtmlTextNode
	 * 
	 * @return
	 */
	public String getFullHtmltext() {

		String text = "";

		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] instanceof IHtmlPart) {
				text += ((IHtmlPart) getChildren()[i]).getFullHtmltext();
			}
		}
		return text;
	}

	
	
	/**
	 * Конструирует контент проходя по всем дочерним узлам ITextPart
	 * 
	 * @return
	 */
	public String getFullText() {

		String text = "";

		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] instanceof ITextPart) {
				text += ((ITextPart) getChildren()[i]).getFullText();
			}
		}
		return text;
	}

	
	
	public String getIconFileName() {
		return icon_file_name;
	}


	public void setIcon_file_name(String icon_file_name) {
		this.icon_file_name = icon_file_name;
	}


	public ConfigTree getConfigTree() {
		return configTree;
	}


	public void setConfigTree(ConfigTree configTree) {
		this.configTree = configTree;
	}


	public IPropertyDialog getPropertyDialog() {
		return property_dialog;
	}
	
	
	/**
	 *   Source File
	 */
	private String src_file_name_xml = "";
	protected String src_text = "";
	
	public File getSrcFile() {
		return getFile(getSrcFileNameXml());
	}

	
	/**
	 * Открывает редактор исходного текста
	 * @return true если удалось открыть
	 */
	/*
	public boolean runEditor() {
		if(src_file_name.trim().equals(""))
			return false;
		File file = getFile(src_file_name);
		if(!file.exists()) {
			saveSrcFile();
		}
		if(file.exists()) {
			try {
				IWorkbenchPage page = ConfexPlugin.getDefault()
					.getWorkbench().getActiveWorkbenchWindow().getActivePage();  
				Utils.openExternalFile(page, file);
			} catch(Exception e) {
				System.err.println(e.getMessage());
				return false;
			}
		} else {
			System.err.println("File ["+src_file_name+"] not exists!");
			return false;
		}
		return true;
	}*/
	
	
	public File getFile(String file_name) {
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
	
	
	public void saveSrcFile(boolean ask_overwrite) {
		File file = getSrcFile();
		
		if (file== null) {
			return;
		}
		//-- make src dir if not exists
		//String filename = file.getName();
		int l = (int)(file.getAbsolutePath().length()-file.getName().length());
		String path_at_src = file.getAbsolutePath().substring(0,l); 
	    File f_dest = new File(path_at_src); 
	    if(!f_dest.exists()) {
	    	f_dest.mkdir();
	    }
		
		boolean overwrite = true;
		if(ask_overwrite&&file.exists()) {
			//overwrite =  this.getConfigTree().g
			Shell shell = ConfexPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(); 
			overwrite = Logger.questionDialog(shell,
					"MESSAGEBOX_TITLE_SAVE_FILE", "QUEST_OVERWRITE_FILE", file.getAbsolutePath());
		}
		if(overwrite) {
			Utils.writeStringToFile(src_text, file);
			System.out.println("  File "+file.getAbsolutePath()+ " was writen.");
		}
	}

	
	public void readFromSrcFile() {
		File file = getSrcFile();
		
		if (file== null) {
			return;
		}
		if(!file.exists()) {
			System.err.println("Sources File "+file.getAbsolutePath()+ " not exists!");
			src_text = "";
			return;
		}
		src_text = Utils.readTextFromFile(file.getAbsolutePath());
		System.out.println("Sources File "+file.getAbsolutePath()+ " was readed.");
	}
	

	/**
	 * Возвращает имя файла, как оно хранится в XML
	 * @return
	 */
	public String getSrcFileNameXml() {
		return src_file_name_xml;
	}

	
	/**
	 * Установить имя файла, как оно хранится в XML
	 * @param file_name
	 */
	public void setSrcFileNameXml(String file_name) {
		this.src_file_name_xml = file_name;
	}
	
	
	
	/**
	 * Возвращает имя файла, как оно отображается в GUI (переводит из XML)
	 * Для реализации нестандартного поведения этот метод необходимо 
	 * переопределить в классе-наследнике
	 * @return
	 */
	public String getSrcFileNameGui() {
		return getSrcFileNameXml();
	}
	
	/**
	 * Установить имя файла, взятое из GUI (переводит в XML)
	 * Для реализации нестандартного поведения этот метод необходимо 
	 * переопределить в классе-наследнике
	 * @param file_name имя класса, взятое из GUI.
	 */
	public void setSrcFileNameGui(String file_name){
		setSrcFileNameXml(file_name);
	}
	
	/**
	 * сохраняет содержимое groovy текста в файл. Если не задан абсолютный путь, то
	 * текущим считается каталог confex файла
	 * 
	 */
	public void saveTestFile(String file_name) {
		File file = getFile(file_name);
		
		if (file== null) {
			return;
		}
		Utils.writeStringToFile(getFullText(), file);
		System.out.println("  File "+file.getAbsolutePath()+ " was writen.");
	}

	
	public String getSrcText() {
		return src_text;
	}

	public void setSrcText(String src_text) {
		this.src_text = src_text;
	}

	
	/**
	 * Custom method with two params  
	 * @param par1
	 * @param par2
	 * @return
	 */
	public Object customMethod3(Object par1, Object par2, Object par3) {
		System.err.println("Method customMethod3() not defined for TreeNode!");
		return null;
	}
	
}
