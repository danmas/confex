package net.confex.customxml;

import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.Utils;
import org.eclipse.swt.widgets.Shell;

import org.w3c.dom.Node;

public class CustomXmlNode extends TreeNode {
	
	
	public String getAboutString() { return Translator.getString("ABOUT_CustomXmlNode"); }
	
	protected static String default_image_name = "custom_xml_node.gif"; 
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	public CustomXmlNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}
	
	private String custom_xml = "";
	
	public String getCustomXml() {
		return custom_xml;
	}

	public void setCustomXml(String custom_xml) {
		this.custom_xml = custom_xml;
	}

	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new CustomXmlNode(getConfigTree(),null); 
	}
	

	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof CustomXmlNode)) {
			System.err.println("[CustomXmlNode.setPropertyLike] prototype NOT instanceof CustomXmlNode!");
			return;
		}
		setCustomXml(((CustomXmlNode)prototype).getCustomXml());
	}

	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if(custom_xml!=null 
				&& !custom_xml.equals("")) {
			str_xml += "<custom_xml>\n"
				+Utils.toHtmlSpecialEntities(custom_xml)+"\n"; 
			str_xml += "</custom_xml>\n"; 
		}
		return str_xml;
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("custom_xml")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setCustomXml(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
	}
	 
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new CustomXmlNodePropertyDialog(shell);
	}
	
	

}
