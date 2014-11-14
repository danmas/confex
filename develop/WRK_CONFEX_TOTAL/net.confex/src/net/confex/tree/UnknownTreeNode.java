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

import net.confex.translations.Translator;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


public class UnknownTreeNode  extends TreeNode {
	
	public String getAboutString() { return Translator.getString("ABOUT_UNKNOWNTREENODE"); }
	
	
	private String class_name = "";
	private String attributes_string = "";
	//-- строка для сохранения свойств неопознанного класса
	private String property_string = "";
	
	
	
	protected static String default_image_name = "unknown.gif"; 
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	public static String getDefaultImageName() {
		return default_image_name;
	}


	
	public UnknownTreeNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	public void saveUnknownClassName(String class_name) {
		this.class_name = class_name;
	}

	
	public void loadAttribFromXml(Node node,ITreeNode parent) {
		//super.loadAttribFromXml(node,parent);
		
		NamedNodeMap attribute_map = node.getAttributes();
		for(int i=0; i<attribute_map.getLength(); i++) {
			Node attrib = attribute_map.item(i);
			attributes_string += attrib.getNodeName();
			attributes_string += "=\""+attrib.getNodeValue() + "\" ";
		}
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		//super.parsePropertyXml(property);
		if(!property.getNodeName().equals("#text")
				&& !property.getNodeName().equals("#comment")) {
			property_string += "<"+property.getNodeName()+">\n";
			Node nd=property.getFirstChild();
			if(nd!=null)
				property_string += nd.getNodeValue()==null?"":nd.getNodeValue();
			property_string += "</"+property.getNodeName()+">\n";
		}
	}

	
	public String getXml(boolean read_src_text) {
		String str_xml = "<"+class_name+"\n";
		
		str_xml += attributes_string;
		str_xml += ">\n";
		
		str_xml += "<properties>\n"; 
		str_xml += property_string;
		str_xml += "</properties>\n"; 
			
		//-- сохраняем все дочерние узлы 
		ITreeNode[] children = getChildren();
		for(int i=0; i<children.length;i++) {
			str_xml +=  children[i].getXml(read_src_text); 
		}
		
		str_xml +="</"+class_name+">\n";

		return str_xml;
	}
	
	
}
