package net.confex.tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.confex.directedit.IPropertyDialog;
import net.confex.directedit.VarsPropertyDialog;
import net.confex.translations.Translator;
import net.confex.utils.Utils;

import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class VarsNode extends TreeNode {
	
	private HashMap<String,String> varsMap = new HashMap<String,String>();
	
	
	public VarsNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	public String getAboutString() { 
		return Translator.getString("ABOUT_VarsNode"); 
	}

	protected static String default_image_name = "assign.gif"; 
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	
	public static String getDefaultImageName() {
		return default_image_name;
	}

	
	public ITreeNode createNewITreeNode() {
		return new VarsNode(getConfigTree(),null); 
	}
	

	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof VarsNode)) {
			System.err.println("[VarsNode.setPropertyLike] prototype NOT instanceof VarsNode!");
			return;
		}
		setVarsMap(((VarsNode)prototype).getVarsMap());
	}
	

	public HashMap getVarsMap() {
		return varsMap;
	}

	public void setVarsMap(HashMap varsMap) {
		 this.varsMap.putAll(varsMap);
	}	
	
	
	public String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
			str_xml += "<vars>\n";
			Iterator iterator = varsMap.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry ent = (Entry)iterator.next();
				str_xml += "<var name=\""
					+ Utils.toHtmlSpecialEntities((String)ent.getKey())+"\">" 
					+ Utils.toHtmlSpecialEntities((String)ent.getValue())
					+ "</var>";
			}
			str_xml += "</vars>\n"; 
			
		return str_xml;
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("vars")) {
			//-- грузим дочерние
			for (int i = 0; i < property.getChildNodes().getLength(); i++) {
				Node child = property.getChildNodes().item(i);
				if(child.getNodeName().equals("var")) {
					String value="";
					if(child!=null) {
						if(child.getFirstChild()==null) {
							value = "";
						} else {
							value = child.getFirstChild().getNodeValue();
							value = Utils.fromHtmlSpecialEntities(value);
						}
						Node nd = child.getAttributes().getNamedItem("name");
						if(nd==null) {
							System.err.println("Attrib [name] of [var] must exist!!!");
						} else {
							String name = nd.getNodeValue(); 
							name = Utils.fromHtmlSpecialEntities(name);
							
							getVarsMap().put(name, value);
						}
					}
				}
			}
		}
	} 

	
	//public void run(NavigationView navigation_view) {
	//}
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new VarsPropertyDialog(shell);
	}

	
	/**
	 * Изменение переменной в наборе
	 * @param var_name
	 * @param value
	 * @return true если переменная присутствует
	 *              иначе false
	 */
	public boolean changeVar(String var_name, String value) {
		if( !varsMap.containsKey(var_name) ) 
			return false;
		varsMap.put(var_name, value);
		return true;
	}


	/**
	 * Gets the String value of variable
	 * 
	 * @param var_name - variable name
	 * @return null if can't find var 
	 */
	public String getStrVar(String var_name) {
		Object o = varsMap.get(var_name);
		if(o instanceof String)
			return (String)o;
		return null;
	}

	
	/**
	 * Gets value of variable as Object
	 * 
	 * @param var_name - variable name
	 * @return null if can't find var 
	 */
	public Object getVar(String var_name) {
		return varsMap.get(var_name);
	}

	
	/**
	 * Проверка наличия в наборе пременной с именем
	 * @param var_name
	 * @return
	 */
	public boolean containsVar(String var_name) {
		return varsMap.containsKey(var_name);
	}

	
	/**
	 * Clear all vars in var set.
	 */
	public void removeAll() {
		varsMap.clear();
	}
	
	
	/**
	 * Add new var with name and value
	 * if exists var with same name updete it.
	 * 
	 * @param name
	 * @param value
	 */
	public void addVar(String name, String value) {
		getVarsMap().put(name, value);
	}
	
	
}








