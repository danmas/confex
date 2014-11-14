package net.confex.groovy.model;

import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;

import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Node;

public class GroovyCompositeNode extends GroovyNode {

	private String header = "//-- header";
	private String content = "";
	private String footer = "//-- footer";

	public String getFullText() {
		String groovy_text = header + "\n\r" + getFullContentText() + "\n\r"
				+ footer + "\n\r";
		System.out.println(groovy_text);
		return TreeUtils.doAllSubstitutions(this, groovy_text);
	}

	public String getFullContentText() {
		String text = getChildrensText0(GroovyNode.class);
		return TreeUtils.doAllSubstitutions(this, text);
	}

	public String getChildrensText(Class _interface) {
		return getFullText();
	}

	/**
	 * Return composite text combined from all child for the children hasn't
	 * NotRunInBatch property set
	 * 
	 */
	public String getChildrensText0(Class _interface) {
		if (isNotRunInBatch())
			return "";
		String ret_str = "";
		ret_str += super.getChildrensText(_interface) + "\n";
		return TreeUtils.doAllSubstitutions(this, ret_str);
	}

	public GroovyCompositeNode(ConfigTree configTree,
			IStateObserver stateObserver) {
		super(configTree, stateObserver);
	}

	public String getAboutString() {
		return Translator.getString("ABOUT_GroovyCompositeNode");
	}

	protected static String default_image_name = "groovy_composite_file.gif";

	public String getDefaultImage() {
		return default_image_name;
	}

	public ITreeNode createNewITreeNode() {
		return new GroovyCompositeNode(getConfigTree(), null);
	}

	protected IPropertyDialog newPropertyDialog(Shell shell) {
		readFromSrcFile();
		return new GroovyCompositePropertyDialog(shell);
	}

	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if (!(prototype instanceof GroovyCompositeNode)) {
			System.err
					.println("[GroovyCompositeNode.setPropertyLike] prototype NOT instanceof GroovyCompositeNode!");
			return;
		}
		setHeader(((GroovyCompositeNode) prototype).getHeader());
		setFooter(((GroovyCompositeNode) prototype).getFooter());
	}

	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property, new_node);

		if (property.getNodeName().equals("header")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setHeader(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("footer")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setFooter(Utils.fromHtmlSpecialEntities(text.trim()));
		}
	}

	public String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if (header != null && !header.equals("")) {
			str_xml += "<header>\n";
			str_xml += Utils.toHtmlSpecialEntities(header) + "\n";
			str_xml += "</header>\n";
		}
		if (footer != null && !footer.equals("")) {
			str_xml += "<footer>\n";
			str_xml += Utils.toHtmlSpecialEntities(footer) + "\n";
			str_xml += "</footer>\n";
		}
		return str_xml;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

}
