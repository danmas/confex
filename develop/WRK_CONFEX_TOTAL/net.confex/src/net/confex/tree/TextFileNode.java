package net.confex.tree;

import net.confex.directedit.IPropertyDialog;
import net.confex.directedit.TextFileNodePropertyDialog;
import net.confex.translations.Translator;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Node;

public class TextFileNode extends TreeNode  {
	
	
	public TextFileNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree, stateObserver);
	}

	public String getAboutString() {
		return Translator.getString("ABOUT_TextFileNode");
	}

	protected static String default_image_name = "/eclipse icons/obj16/file_obj.gif";

	public String getDefaultImage() {
		return default_image_name;
	}

	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if (src_text != null && !src_text.equals("")) {
			str_xml += "<text>\n";
			String s = "";
			//-- если есть файла с исх текстом и не читать то 
			//   текста источника не будет 
			if(!read_src_text&&(getSrcFile()!=null&&getSrcFile().exists()))
				s = "";
			else
				s = Utils.toHtmlSpecialEntities(src_text);
			str_xml += s + "\n";
			str_xml += "</text>\n";
		}
		return str_xml;
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);

		if (property.getNodeName().equals("text")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setSrcText(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("file_name")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setSrcFileNameXml(Utils.fromHtmlSpecialEntities(text.trim()));
		}
	}

	
	public ITreeNode createNewITreeNode() {
		return new TextFileNode(getConfigTree(), null);
	}

	
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if (!(prototype instanceof TextFileNode)) {
			System.err
					.println("[TextFileNode.setPropertyLike] prototype NOT instanceof TextFileNode!");
			return;
		}
	}

	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		readFromSrcFile();
		return new TextFileNodePropertyDialog(shell);
	}
	
	
	public String getFullText() {
		String groovy_text = getChildrensText(this.getClass());
		return TreeUtils.doAllSubstitutions(this, groovy_text); 
	}
	
	
	Composite m_parent = null;
	
	
	public void refreshComposite() {
		if(m_parent!=null) {
			m_parent.layout();
			m_parent.redraw();
			System.out.println("refreshComposite()");
		}
	}
	
	
}
