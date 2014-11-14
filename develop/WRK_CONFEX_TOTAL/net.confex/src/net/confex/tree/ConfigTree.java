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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.confex.Constants;
import net.confex.translations.Logger;
import net.confex.translations.Translator;
import net.confex.utils.Strings;
import net.confex.utils.Utils;
import net.confex.views.NavigationView;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.FileDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Класс владелец дерева. 
 * Обеспечивает загрузку и сохранение confex файла. 
 * 
 * @author Roman_Eremeev
 *
 */

public class ConfigTree {

	
	//private String current_tree_packages;  

	//private String addition_packages = null;

	//-- root должен быть всегда!
	protected TreeNode root = new TreeNode(this,null);
	
	private boolean dirty = false;
	private boolean error_state = false;
	
	private String file_name = "";
	public String getFileName() { return file_name; }
	
	private String input_version  = "0.0.2"; //-- по умолчанию входная версия 0.0.2
	private String output_version = "0.0.2"; //-- выходная версия 0.0.2 
	
	
	private  int current_id=0;
	
	public int getIdForUsing() {
		return current_id++;
	}
	public void setIdForUsing(int i) {
		if(i>=current_id) current_id = i+1;
	}
	
	public boolean isDirty() {
		return dirty;
	}


	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	
	public void setErrorState(boolean error_state) {
		this.error_state = error_state;
	}

	
	public boolean isErrorState() {
		return error_state;
	}

	
	public ConfigTree() {
		//setCurrentTreePackages(Constants.DEFAULT_CONFEX_TREE_PKG);
		
		//-- создаем список фабрик узлов
		//   cначала базовую фабрику
		treenode_factorys.add(new TreeNodeFactory());
		//-- затем подключаем все фабрики (подключенные к точке расширения) 
		getFactorysFromExtensionPoints();
		
		//Constants.setupProperties();
	}

	
	public void save() throws Exception {
		File file = new File(file_name);
		saveAsXml(file, null);
	}
	
	
	/**
	 * Нужно найти объект в дереве по ключу
	 * Пример ключа:  C:\Config\confex.confex~…~Первый вариант~…~Эклипс~…~
	 * 
	 * @param key
	 * @return
	 */
	public Object findMember(String key) {
		return hash_map.get(key);
	}

	private HashMap<String,ITreeNode> hash_map = new HashMap<String,ITreeNode>(); 
	
	
	public void fillMomenoHashMap() {
		root.fillHashMap(hash_map);
	}
	
	
	protected ArrayList<ITreeNodeState> state_list = new ArrayList<ITreeNodeState>();
	
	public List<ITreeNodeState> getStateList() {
		return state_list;
	}
	
	
	/**
	 * Заполнить список всех выполняемых на данный момент 
	 */
	public void fillRunningStateList() {
		state_list.clear();
		root.fillStateList(state_list,ITreeNodeState.STATE_RUN);
	}
	
	
	/**
	 * Сохранить файл как...
	 * 
	 * @param navigation_view
	 * @return true - если было сохранение иначе - false
	 * @throws Exception
	 */
	public boolean saveAs(NavigationView navigation_view) throws Exception {
		NavigationView navigationView = navigation_view;
		FileDialog fileDialog = new FileDialog(navigationView.getSite().getShell());
    	
		fileDialog.setFileName(getFileName());
		fileDialog.setFilterExtensions( Constants.CONFEX_EXTENSIONS );
	
    	String file_name = fileDialog.open();
    	if(file_name==null) {
    		return false;
    	}
    	if(file_name.indexOf('.')==-1) {
    		file_name += "." + Constants.DEFAULT_CONFEX_FILE_EXTENSION;
    	}
    	this.file_name = file_name;
    	navigationView.setPartName(file_name);
    	//this.setContentDescription("This is content description! ХЗ что это. :)");

		File file = new File(file_name);
		if(file.exists()) {
			if(!Logger.questionDialog(navigationView.getSite().getShell(),
				"MESSAGEBOX_TITLE_SAVE_FILE", "QUEST_OVERWRITE_FILE", "")) {
				return saveAs(navigation_view);
			}
		}
		saveAsXml(file, null);
		return true;
	}
	
	
	/**
	 * Сохраняем файл в виде XML 
	 * 
	 * @param file - в который сохраняем
	 * 
	 * @param monitor
	 * @throws Exception
	 */
	public void saveAsXml(File file, IProgressMonitor monitor) throws Exception {
		String str_xml ="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
		str_xml += getXml(false);
		//try {
			Utils.reWriteStringToFileEx(str_xml,file,"UTF-8");
		//} catch (Exception e) {
		//	MessageDialog.openError(getSite().getShell()
		//			, "Сохранение файла.", "\n\n Дерево имеет ошибки и не может быть сохранено!");
		//	System.err.println(e.getMessage());
		//}
		setDirty(false);
	}
	

	/**
	 * Возвращает xml confex текст соответствующий структуре дерева
	 * 
	 * @return xml confex текст
	 */
	public String getXml(boolean read_src_text) {
		String str_xml = "<ConfigTree ";
		//if(addition_packages!=null) {
		//	str_xml += "packages=\""+addition_packages+"\" ";
		//}
		if(output_version!=null) {
			str_xml += "version=\""+output_version+"\"";
		}
		str_xml += ">\n";
		
		ITreeNode[] children = root.getChildren();
		for(int i=0; i<children.length;i++) {
			str_xml +=  children[i].getXml(read_src_text); 
		}
		str_xml +="</ConfigTree>\n";
		return str_xml;
	}
	
	
	public ITreeNode createNew(String file_name) {
		if(root==null) {
			root = new TreeNode(this,null);
			root.setName(file_name);
		} else {
			root.removeChildren();
		}
		//loadXmlFromFile(file_name);
        this.file_name = file_name;
        this.confex_dir = new File(file_name).getParent();
		internal_vars.put("CONFEX_DIR", Strings.replace(confex_dir,"\\","/"));
        return root;
	}

	
	public ITreeNode openFile(String file_name) {
		if(root==null) {
			root = new TreeNode(this,null);
			root.setName(file_name);
		} else {
			root.removeChildren();
		}
		loadXmlFromFile(file_name);
        this.file_name = file_name;
        return root;
	}

	
	/**
	 *  чистим содержимое
	 */
	public void removeAll() {
		if(root!=null) {
			root.removeChildren();
		}
	}

	
	/**
	 * Загрузка xml файла confex
	 *  
	 * @param file_name
	 * @return корневой элемент дерева
	 */
	public ITreeNode loadXmlFromFile(String file_name) {
		//List factorys = getFactorysFromExtensionPoints();
		
        //this.root = new TreeNode(this,file_name);
        //this.file_name = file_name;
        //createNew(file_name);
		if(root==null) {
			root = new TreeNode(this,null);
		}
		root.setName(file_name);
        this.file_name = file_name;
        
		File file = new File(file_name);
		if(!file.exists()) {
			String s = Translator.getString("MSG_ERR_FILE_NOT_FOUND")+ "  "+file.toString();
			TreeNode tree_node = new TreeNode(this,null);
			tree_node.setName(s); 
			root.addChild(tree_node); 
		} else {
			try {
				loadAsXml(file,root,false);
				
				//-- вычисляем директорию confex файла
				confex_dir = file.getParent();
				if(!confex_dir.endsWith(File.separator) )
					confex_dir += File.separator;
				internal_vars.put("CONFEX_DIR", Strings.replace(confex_dir,"\\","/"));
				
			} catch(Exception ex) {
				TreeNode tree_node = new TreeNode(this,null);
				tree_node.setName(ex.getMessage()); 
				root.addChild(tree_node); 
				System.err.println(ex.getMessage());
			}
		}
		return root;
	}

	
	private String confex_dir = "";
	
	
	public String getConfexDir() {
		return confex_dir;
	}
	
	
	/**
	 * Загрузка xml файла confex
	 *  
	 * @param file  - загружаемый файл
	 * @param root  - корневой элемент
	 * 
	 * @throws Exception
	 */
	public void loadAsXml(File file, TreeNode root, boolean new_node) throws Exception {
		//-- Create a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//-- Create a DocumentBuilder 
		DocumentBuilder db = dbf.newDocumentBuilder();

		InputStream fins = new FileInputStream(file);
		
		//-- Parse the input file to get a Document object
		Document doc = db.parse(fins);
		fins.close();
		
		for (int i = 0; i < doc.getDocumentElement().getChildNodes().getLength(); i++) {
			Element e = doc.getDocumentElement();
			/*
			String packages = e.getAttribute("packages");
			if(packages!=null && !packages.equals("") ) {
				addition_packages = packages; 
				setCurrentTreePackages(Constants.DEFAULT_CONFEX_TREE_PKG + "," + packages );
			}*/
			String version = e.getAttribute("version");
			if(version!=null && !version.equals("") ) {
				this.input_version = version; 
			}
			Node node = doc.getDocumentElement().getChildNodes().item(i);
			parseXml(node,root,new_node);
		}
		//-- забываем входную версию устанавливаем ее равной выходной
		input_version = output_version;
	}
	
	
	private List<ITreeNodeFactory> treenode_factorys = new ArrayList<ITreeNodeFactory>();
	
	
	/**
	 * По имени класса возвращает класс. Производится поиск по 
	 * фарикам узлов.
	 * 
	 * @param class_name
	 * @return Class
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Class getClassForName(String class_name) {
		Class cl = null;
		
		for(Iterator iter= treenode_factorys.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			cl = ((ITreeNodeFactory) obj).getClassForName(class_name);
			if(cl!=null)
				return cl;
		}
		
		return cl;
	}
	
	
	/**
 	 * Создает список Фабрик классов узлов
	 * 
	 *  net.confex.treeNodeFactorys
	 */
	private void getFactorysFromExtensionPoints() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(
				"net.confex.treeNodeFactorys");

		if (point == null) return;
		
		IExtension[] extensions = point.getExtensions();
		
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] elements = 
				extensions[i].getConfigurationElements();
			for(int j=0; j < elements.length; j++) {
				try {
					Object obj = elements[j].createExecutableExtension("class");
					if(obj instanceof ITreeNodeFactory) {
						//-- добавляем фабрику
						treenode_factorys.add((ITreeNodeFactory)obj);
					}
				} catch(CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
	 * Разбор xml структуры и подключение веток к родительскому узлу
	 * 
	 * Имя тэга должно совпадать с именем одного из классов 
	 * пакета net.confex.tree, реализующим интерфейс ITreeNode.
	 * Имя пакета задается в Constants.DEFAULT_TREE_PACKAGE
	 * 
	 * Если класс не найден то создается класс TreeNode в имени которого 
	 * будет добавлено сообщение об ошибке. 
	 * Например: "(Класс TreeNode2 не найден!)IBM статьи" 
	 *  - TreeNode2 это класс которого нет в пакетах.  
	 * 
	 * @param node DOM-узел который парсится
	 * @param parent TreeNode - узел к которому подлючаются дочерние
	 */
	public void parseXml(Node node, TreeNode parent, boolean new_node) {
		parseXml(node,parent,null,new_node);
	}
	
	
	@SuppressWarnings("unchecked")
	protected void parseXml(Node node, TreeNode parent, ITreeNode before_child, boolean new_node) {
		Class [] classParm = { ConfigTree.class, IStateObserver.class };
		Object [] objectParm = { this, null };
		
		String class_name = node.getNodeName();   
		
		//-- сначала идет явный парсинг устаревших тэгов (для которых изменено имя класса)
		if (node.getNodeName().equals("FormViewNode")
				|| node.getNodeName().equals("UrlTreeObject") ) {
			CompositeFormViewNode tree_object = new CompositeFormViewNode(this,null);
			//System.out.println("new UrlTreeObject");
			tree_object.loadFromXml(node,tree_object, new_node);
			parent.addChild(tree_object,before_child);
		} else if ( !(node.getNodeName().equals("#text")) 
				&& !(node.getNodeName().equals("#comment"))) {
			//-- пытаемся создать класс динамически
			Class cl = getClassForName(class_name);
			if(cl==null) {
				UnknownTreeNode tree_parent = new UnknownTreeNode(this,null);
				tree_parent.saveUnknownClassName(class_name); 
				tree_parent.loadFromXml(node,tree_parent, new_node);
				String s = "(Class "+class_name+" not found!) Parent: "+tree_parent.getName();
				System.err.println(s);
				tree_parent.setName(s);
				parent.addChild(tree_parent,before_child);
				parent.setErrorState();
				tree_parent.setErrorState();
				setErrorState(true);
			} else {
				try {
					java.lang.reflect.Constructor co = cl.getConstructor(classParm);
					Object obj = co.newInstance(objectParm);
					if( obj instanceof ITreeNode ) {
						// System.out.println("Класс "+class_name+" ");
						((ITreeNode)obj).loadFromXml(node,(ITreeNode)obj, new_node);
						parent.addChild((ITreeNode)obj,before_child);
					} else {
						UnknownTreeNode tree_parent = new UnknownTreeNode(this,null);
						System.err.println(Translator.getString("MSG_ERR_NOTINSTANCEOF_ITREENODE")+" ["+class_name+"] ");
						
						tree_parent.loadFromXml(node,tree_parent, new_node);
						tree_parent.setName("(Class ["+class_name+"] not ITreeNode!)"+tree_parent.getName());
						parent.addChild(tree_parent,before_child);
						setErrorState(true);
					}
				} catch(Exception e) {
					UnknownTreeNode tree_parent = new UnknownTreeNode(this,null);
					tree_parent.loadFromXml(node,tree_parent, new_node);
					tree_parent.setName("(Class "+class_name+" not found!)"+tree_parent.getName());
					parent.addChild(tree_parent,before_child);
					setErrorState(true);
				}
			}
		} else   {
			if (!node.getNodeName().equals("#text")
					&& !node.getNodeName().equals("#comment")
					) {
				System.err.println(Translator.getString("MSG_ERR_UNKNOWN_TAG_IN_DOC") +"["+ node.getNodeName()+"]" );
			}
		}
	}

	
	protected HashMap internal_vars = new HashMap(); 
	
	public HashMap getInternalVars() {
		return internal_vars;
	}
	
	public String getInputVersion() {
		return input_version;
	}


	public TreeNode getRoot() {
		return root;
	}
	
	public void setRoot(TreeNode root) {
		this.root = root;
	}

}
