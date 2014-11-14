package net.confex.db.tree;

import net.confex.db.JdbcConnection;
import net.confex.db.directedit.JdbcConnectionPropertyDialog;
import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.Utils;

import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Node;

/**
 * Узел для создания и поддержания JDBC коннекции к БД
 * 
 * @author Roman_Eremeev
 * 
 */
public abstract class JdbcConnectionNode extends TreeNode {

	private JdbcConnection jdbc_connection = null;

	private String user = "";

	private String password = "";

	private String url = "";

	
	/**
	 * Установка иконки по умолчанию для класса
	 */
	public String getDefaultImage() {
		return "etool16/database_add.png";
	}

	
	public JdbcConnectionNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	/*
	public AnyJdbcConnectionNode(ConfigTree configTree, String name) {
		super(configTree, name);
	}*/

	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Открывается коннекция с клиентской машины
	// Если коннекции еще нет или закрыта то она будет создана
	// R: true - ok
	public abstract boolean createConnection();

	
	public void closeConnection() {
		if(jdbc_connection!=null) {
			jdbc_connection.closeConnection();
			setSuccessState();
		}
	}
	
	
	public boolean isClosed() {
		if(jdbc_connection==null)
			return true;
		return jdbc_connection.isClosed();
	}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * 
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if (!(prototype instanceof JdbcConnectionNode)) {
			System.err
			.println(Translator.getString("MSG_PROP_NOT_INSTANCEOF")+" AnyJdbcConnectionNode"); //$NON-NLS-1$
			return;
		}
		setUrl(((JdbcConnectionNode) prototype).getUrl());
		setUser(((JdbcConnectionNode) prototype).getUser());
		setPassword(((JdbcConnectionNode) prototype).getPassword());
	}

	
	public void loadAttribFromXml_0_0_1(Node node, ITreeNode parent) {
		super.loadAttribFromXml_0_0_1(node, parent);

		Node attr = node.getAttributes().getNamedItem("url");
		if (attr == null) {
			System.err.println(" Can't read name for " + this.toString());
		} else {
			String s_url = attr.getNodeValue();
			this.url = s_url;
		}
		attr = node.getAttributes().getNamedItem("user");
		if (attr == null) {
			// System.err.println(" Can't read name for " + this.toString());
		} else {
			String s_user = attr.getNodeValue();
			this.user = s_user;
		}
		attr = node.getAttributes().getNamedItem("password");
		if (attr == null) {
		} else {
			String s_password = attr.getNodeValue();
			this.password = s_password;
		}
	}

	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if (user != null && !user.equals("")) {
			str_xml += "<user>\n" + Utils.toHtmlSpecialEntities(user) + "\n";
			str_xml += "</user>\n";
		}
		if (password != null && !password.equals("")) {
			str_xml += "<password>\n" + Utils.toHtmlSpecialEntities(password)
					+ "\n";
			str_xml += "</password>\n";
		}
		if (url != null && !url.equals("")) {
			str_xml += "<url>\n" + Utils.toHtmlSpecialEntities(url) + "\n";
			str_xml += "</url>\n";
		}
		return str_xml;
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);

		if (property.getNodeName().equals("user")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setUser(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("password")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setPassword(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("url")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setUrl(Utils.fromHtmlSpecialEntities(text.trim()));
		}
	}

	
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new JdbcConnectionPropertyDialog(shell);
	}
	
	
	
	/*
	 * public String getAttribXml() { String str_xml = super.getAttribXml();
	 * str_xml += "url=\""+url+"\"\n"; str_xml += "user=\""+user+"\"\n"; str_xml +=
	 * "password=\""+password+"\"\n"; return str_xml; }
	 */

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Получение коннекции
	// Если коннекции еще нет или закрыта то она будет создана
	//
	public abstract JdbcConnection getConnection();

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JdbcConnection getJdbcConnection() {
		return jdbc_connection;
	}

	public void setJdbcConnection(JdbcConnection jdbc_connection) {
		this.jdbc_connection = jdbc_connection;
	}

}
