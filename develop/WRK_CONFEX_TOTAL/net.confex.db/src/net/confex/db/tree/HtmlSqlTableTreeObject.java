package net.confex.db.tree;

import java.sql.Connection;

import net.confex.action.RunBrowserAction;
import net.confex.db.DbUtils;
import net.confex.db.JdbcConnection;
import net.confex.db.directedit.HtpSqlPropertyDialog;
import net.confex.directedit.IPropertyDialog;
import net.confex.html.IHtmlPart;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IRunBrowser;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.confex.views.WebBrowserView;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.w3c.dom.Node;

public class HtmlSqlTableTreeObject extends TreeNode implements IRunBrowser,
		IHtmlPart {

	private String sql = ""; //$NON-NLS-1$

	public String getAboutString() {
		return Translator.getString("ABOUT_HTPSQLTREEOBJECT"); //$NON-NLS-1$
	}

	/**
	 * Установка иконки по умолчанию для класса
	 */
	public String getDefaultImage() {
		return "eview16/table.png"; //$NON-NLS-1$
	}

	public HtmlSqlTableTreeObject(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	/*
	public HtmlSqlTableTreeObject(ConfigTree configTree, String name) {
		super(configTree, name);
	}*/

	
	/**
	 * Cоздает экземпляр того же класса !!! Должен быть переопределен в дочерних
	 * классах
	 * 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new HtmlSqlTableTreeObject(getConfigTree(),null);
	}

	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * 
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if (!(prototype instanceof HtmlSqlTableTreeObject)) {
			System.err
					.println(Translator.getString("MSG_PROP_NOT_INSTANCEOF")+" HtmlSqlTableTreeObject"); //$NON-NLS-1$
			return;
		}
		setSql(((HtmlSqlTableTreeObject) prototype).getSql());
	}

	public void loadAttribFromXml_0_0_1(Node node, ITreeNode parent) {
		super.loadAttribFromXml_0_0_1(node, parent);

		Node attr = node.getAttributes().getNamedItem("sql"); //$NON-NLS-1$
		if (attr == null) {
			System.err.println(Translator.getString("MSG_CANT_READ_SQL_FOR") + this.toString()); //$NON-NLS-1$
		} else {
			String s_sql = attr.getNodeValue();
			this.sql = Utils.fromHtmlSpecialEntities(s_sql);
		}
	}

	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if (sql != null && !sql.equals("")) { //$NON-NLS-1$
			str_xml += "<sql>\n" + Utils.toHtmlSpecialEntities(sql) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			str_xml += "</sql>\n"; //$NON-NLS-1$
		}
		return str_xml;
	}

	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);

		if (property.getNodeName().equals("sql")) { //$NON-NLS-1$
			String text = ""; //$NON-NLS-1$
			Node nd = property.getFirstChild();
			if (nd != null)
				text = nd.getNodeValue();
			this.setSql(Utils.fromHtmlSpecialEntities(text.trim()));
		}
	}

	/*
	 * public String getAttribXml() { String str_xml = super.getAttribXml();
	 * str_xml += "sql=\""+Utils.toHtmlSpecialEntities(sql)+"\"\n"; //str_xml =
	 * str_xml.replaceAll("<", "&lt;"); //str_xml = str_xml.replaceAll(">",
	 * "&gt;"); return str_xml; }
	 */

	

	
	public WebBrowserView runBrowser(IWorkbenchPage page/*,IStateObserver observer*/) {
		// -- сначала находим и получаем jdbc коннекцию
		ITreeNode p = getParent();

		JdbcConnection connection = DbUtils.searchJdbcConnectionUp(this);
		
		WebBrowserView browser = null;
		try {
			// -- формируем html страницу
			String html_str = Translator.getString("MSG_EMPTY_HTML"); //$NON-NLS-1$
			Connection conn = connection.getConnection();
			if (conn == null)
				html_str = Translator.getString("MSG_ERR_CANT_CREATE_JDBC_CONNECTION"); //$NON-NLS-1$
			else
				html_str = DbUtils.retSelectAsHtmlTable(connection
						.getConnection(), TreeUtils.doAllSubstitutions(this, sql));

        	String id = String.valueOf(getStringKey().hashCode());
        	String title = getName();
			if(title==null||title.trim().equals("")) { //$NON-NLS-1$
				title=this.getClassName().toString();
			}
			
			browser = (WebBrowserView) page.showView(WebBrowserView.VIEW_ID,
					id, IWorkbenchPage.VIEW_ACTIVATE);

			if (browser != null) {
				browser.setTitlePartName(title);
				String s = "<html><body>" + html_str + "</body></html>"; //$NON-NLS-1$ //$NON-NLS-2$
				browser.setPage(s);
				browser.refresh();
			}

		} catch (PartInitException e1) {
			System.err.println("-->" + e1.getMessage()); //$NON-NLS-1$
		}
		return browser;
	}

	
	public void run(IViewPart view) { 
		RunBrowserAction runBrowserAction = new RunBrowserAction(
				view, this);
		runBrowserAction.run();
	}

	
	protected String getHtml() {
		String html_str = null;

		// -- сначала находим и получаем jdbc коннекцию
		ITreeNode p = getParent();

		boolean isOk = false;
		JdbcConnection connection = null;

		while (p != null) {
			if (p instanceof JdbcConnectionNode) {
				// p.addStateObserver(observer);

				isOk = ((JdbcConnectionNode) p).createConnection();
				connection = ((JdbcConnectionNode) p).getConnection();
				break;
			}
			p = p.getParent();
		}
		if (!isOk || connection == null || connection.isClosed()) {
			System.err.println(Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC")); //$NON-NLS-1$
			html_str = "<p><font color=#FF0000>"+Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC")+"</font></p>"; //$NON-NLS-1$
			return html_str;
		}
		// -- формируем html страницу
		Connection conn = connection.getConnection();
		if (conn == null)
			html_str = "<p><font color=#FF0000>"+Translator.getString("MSG_ERR_CANT_CREATE_JDBC_CONNECTION")+"</font></p>"; //$NON-NLS-1$
		else {
			//String s1 = TreeUtils.doAllSubstitutions(this, sql);
			html_str = DbUtils.retSelectAsHtmlTable( 
					connection.getConnection(),	TreeUtils.doAllSubstitutions(this, sql));
		}

		return html_str;
	}

	
	/**
	 * Конструирует контент проходя по всем дочерним узлам HtmlTextNode
	 * 
	 * @return
	 */
	public String getFullHtmltext() {
		if(isNotRunInBatch())
			return "";
		String ret_str = getHtml();
		return TreeUtils.doAllSubstitutions(this, ret_str);
	}

	
	/**
	 * По восходящей ищем коннекцию, если нашли то коннектимся. Затем выполняем.
	 */
	/*
	 * public void run(IViewSite view_site) { //-- сначала находим и получаем
	 * jdbc коннекцию ITreeNode p = getParent();
	 * 
	 * boolean isOk=false; JdbcConnection connection = null;
	 * 
	 * while(p!=null) { if(p instanceof AnyJdbcConnectionNode) { isOk =
	 * ((AnyJdbcConnectionNode)p).createConnection(); connection =
	 * ((AnyJdbcConnectionNode)p).getConnection(); break; } p = p.getParent(); }
	 * if(!isOk || connection==null || connection.isClosed()) {
	 * System.err.println("Can't find and setup JDBC connection!"); return; }
	 * IWorkbenchPage page = view_site.getPage(); try { //-- формируем html
	 * страницу String html_str = "Пусто"; Connection conn =
	 * connection.getConnection(); if(conn==null) html_str = "Невозможно создать
	 * коннекцию к серверу!"; else html_str =
	 * DbUtils.retSelectAsHtmlTable(connection.getConnection(),sql);
	 * 
	 * WebBrowserView browser = (WebBrowserView)
	 * page.showView(WebBrowserView.VIEW_ID, this.getName(),
	 * IWorkbenchPage.VIEW_ACTIVATE);
	 * 
	 * if (browser != null) { browser.setTitlePartName(this.getName()); String
	 * s="<html><body>"+ html_str + "</body></html>"; browser.setPage(s);
	 * //-- добавляем слушателя чтобы отследить // закрытие и изменить состояние
	 * browser.addPartListener(partListener); setRunState(); }
	 *  } catch(PartInitException e1) { System.err.println("-->" +
	 * e1.getMessage()); } }
	 */

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	

	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new HtpSqlPropertyDialog(shell);
	}
	
	
	/*
	public void openPropertyDialog(NavigationView view, Shell shell,
			boolean new_flg) {
		property_dialog.setNewFlg(new_flg);

		if (property_dialog == null) {
			property_dialog = new HtpSqlPropertyDialog(shell);
		}
		property_dialog.createSShell();
		property_dialog.setView(view);
		property_dialog.show();
		property_dialog.setTreeNode(this);
	}*/

}
