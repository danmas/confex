package net.confex.extension1.tree;

import java.io.File;

import net.confex.directedit.IPropertyDialog;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;

import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Node;

/**
 * Композитный элемент состоит из трех частей header,content,footer. Header и
 * Foooter неизменяемы а content может быть перестроен динамически из дочерних
 * узлов проходящих фильтр isContentPart()
 * 
 * @author Roman_Eremeev
 * 
 */

public abstract class CompositeDoc extends TreeNode {
	protected static String defaultHeader = "";
	protected static String defaultFooter = "";

	private String header = defaultHeader;
	private String content = "";
	private String footer = defaultFooter;
	private String file_name = "";

	
	public CompositeDoc(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree, stateObserver);
	}

	
	/**
	 * Определяет дает ли дочерний элемент вклад в content.
	 * 
	 * child_node должен быть ICompositeDocPart
	 * @override
	 */
	protected boolean isContentPart(ITreeNode child_node) {
		return child_node instanceof ICompositeDocPart;
	}

	
	/**
	 * Конструирует контент проходя по всем дочерним узлам
	 */
	protected void makeContent() {
		content = "";

		for (int i = 0; i < getChildren().length; i++) {
			if (isContentPart(getChildren()[i])) {
				content += ((ICompositeDocPart) getChildren()[i]).getCompositeDocText();
			}
		}
	}

	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * 
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if (!(prototype instanceof CompositeDoc)) {
			System.err
					.println("[CompositeDoc.setPropertyLike] prototype NOT instanceof CompositeDoc!");
			return;
		}
		setHeader(((CompositeDoc) prototype).getHeader());
		setFooter(((CompositeDoc) prototype).getFooter());
		setFileName(((CompositeDoc) prototype).getFileName());
	}

	/**
	 * Возвращает полный текст кода составленный из трех частей
	 * header+content+footer
	 * 
	 * @return
	 */
	public String getFullCodeText() {
		makeContent();
		return header + content + footer;
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
		} else if (property.getNodeName().equals("file_name")) { Node nd =
			  property.getFirstChild(); String text = ""; if (nd != null) text =
			  nd.getNodeValue();
			  this.setFileName(Utils.fromHtmlSpecialEntities(text.trim())); 
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
		if (file_name != null && !file_name.equals("")) { str_xml += "<file_name>\n";
			str_xml += Utils.toHtmlSpecialEntities(file_name) + "\n"; str_xml += "</file_name>\n"; }

		return str_xml;
	}

	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		readFromSrcFile();
		return new CompositeDocPropertyDialog(shell);
	}

	
	
	public File getFile() {
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
	
	
	/**
	 * Rebuild full_composite_text and save it into file
	 */
	public void saveFile() {
		File file = getFile();
		
		if (file== null) {
			return;
		}
		Utils.writeStringToFile(getFullHtmltext(), file);
	}
	
	
	/**
	 * Save text into file
	 * @param full__text
	 */
	protected void saveFile(String full_composite_text) {
		File file = getFile();
		
		if (file== null) {
			return;
		}
		Utils.writeStringToFile(full_composite_text, file);
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
	
	public String getFileName() {
		return file_name;
	}

	public void setFileName(String file_name) {
		this.file_name = file_name;
	}

	
}
