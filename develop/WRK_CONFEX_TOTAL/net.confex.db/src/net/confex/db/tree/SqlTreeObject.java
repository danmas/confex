package net.confex.db.tree;

/**
 * 
 * Узел для выполнения SQL запроса к БД. 
 * 
 */

import java.sql.Connection;

import net.confex.db.DbUtils;
import net.confex.db.JdbcConnection;
import net.confex.db.directedit.SqlPropertyDialog;
import net.confex.directedit.IPropertyDialog;
import net.confex.html.IHtmlPart;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.Strings;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.w3c.dom.Node;

	public class SqlTreeObject extends TreeNode {

		private String sql = "";

		public String getAboutString() { return Translator.getString("ABOUT_SQLTREEOBJECT"); }
		
		
		/**
		 *   Установка иконки по умолчанию для класса
		 */
		public String getDefaultImage() {
			return "eview16/sql.png";
		}
		
		
		public SqlTreeObject(ConfigTree configTree, IStateObserver stateObserver) {
			super(configTree,stateObserver);
		}

		
		/*
		public SqlTreeObject(ConfigTree configTree, String name) {
			super(configTree,name);
		}*/

		
		/**
		 * 	Cоздает экземпляр того же класса
		 * !!! Должен быть переопределен в дочерних классах 
		 * @return ITreeNode
		 */
		public ITreeNode createNewITreeNode() {
			return new SqlTreeObject(getConfigTree(),null); 
		}
		
		
		/**
		 * Устанавливает все свойства элемента такими как у прототипа
		 * @param prototype
		 */
		public void setPropertyLike(ITreeNode prototype) {
			super.setPropertyLike(prototype);
			if(!(prototype instanceof SqlTreeObject)) {
				System.err
					.println(Translator.getString("MSG_PROP_NOT_INSTANCEOF")+" SqlTreeObject"); //$NON-NLS-1$
				return;
			}
			setSql(((SqlTreeObject)prototype).getSql());
		}
		
		
		public void loadAttribFromXml_0_0_1(Node node, ITreeNode parent) {
			super.loadAttribFromXml_0_0_1(node, parent);

			Node attr = node.getAttributes().getNamedItem("sql");
			if (attr == null) {
				System.err.println(Translator.getString("MSG_CANT_READ_SQL_FOR") + this.toString());
			} else {
				String s_sql = attr.getNodeValue();
				this.sql = Utils.fromHtmlSpecialEntities(s_sql);
			}
		}

		
		protected String getPropertiesXml(boolean read_src_text) {
			String str_xml = super.getPropertiesXml(read_src_text);
			if(sql!=null 
					&& !sql.equals("")) {
				str_xml += "<sql>\n"
					+Utils.toHtmlSpecialEntities(sql)+"\n"; 
				str_xml += "</sql>\n"; 
			}
			return str_xml;
		}
		
		
		protected void parsePropertyXml(Node property, boolean new_node) {
			super.parsePropertyXml(property,new_node);
			
			if (property.getNodeName().equals("sql")) {
				Node nd=property.getFirstChild();
				String text="";
				if(nd!=null)
					text = nd.getNodeValue();
				this.setSql(Utils.fromHtmlSpecialEntities(text.trim()));
			} 
		}
		
		
		/*public String getAttribXml() {
			String str_xml = super.getAttribXml();
			str_xml += "sql=\""+Utils.toHtmlSpecialEntities(sql)+"\"\n";
			//str_xml = str_xml.replaceAll("<", "&lt;");
			//str_xml = str_xml.replaceAll(">", "&gt;");
			return str_xml;
		}*/
		
		
		/**
		 * Конструирует строку sql производим подстановку переменных  
		 * @return
		 */
		public String getFullText() {
	    	//-- производим подстановку переменных
			return TreeUtils.doAllSubstitutions(this,getSql());
		}
		
		
		public void run(IViewPart view) {
			IWorkbenchPage page = view.getSite().getPage();

			//-- сначала находим и получаем jdbc коннекцию
			JdbcConnection connection = DbUtils.searchJdbcConnectionUp(this);

			if(connection==null) {
				//System.err.println(Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC"));
	        	MessageDialog.openError(page.getWorkbenchWindow().getShell()
	        			,this.getName()
	        			,Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC"));
	        	setErrorState();
	        	return;
			}
			try {
				//System.out.println("<"+sql+">");
				
	        	//-- формируем html страницу 
	        	Connection conn = connection.getConnection();
	        	if(conn==null) {
	        		MessageDialog.openError(page.getWorkbenchWindow().getShell()
		        			,this.getName()
		        			,Translator.getString("MSG_ERR_CANT_SETUP_CONNECTION"));
	        		setErrorState();
	        		return;
	        	}
	        	
	        	String sql_str;
	        	//System.out.println("sql=["+sql+"]");
	        	
	        	sql_str = Strings.replace(sql, "\n", " ");
	        	sql_str = Strings.replace(sql_str, "\r", " ");
	        	
	        	
	        	//-- выполняем  
	        	DbUtils.executeSqlEx(conn,TreeUtils.doAllSubstitutions(this, sql_str)); 
	        	
	        	//-- всегда принудительно комитим!!!
	        	if(connection!=null)
	        		connection.commitConnection();
	        	
	        	setSuccessState();
				MessageDialog.openInformation(
						page.getWorkbenchWindow().getShell()
						,this.getName()
						,"SQL:"+sql+"\n\n"+Translator.getString("MSG_DONE"));
				
	        } catch(Exception ex) {
	        	MessageDialog.openError(page.getWorkbenchWindow().getShell()
	        			,this.getName()
	        			,Translator.getString("MSG_ERR_ON_EXECUTE_SQL")+sql+". \n\n"+ex.getMessage());
	        	setErrorState();
	        }
	            
		}


		public String getSql() {
			return sql;
		}


		public void setSql(String sql) {
			this.sql = sql;
		}
		
		
		protected IPropertyDialog newPropertyDialog(Shell shell) {
			return new SqlPropertyDialog(shell);
		}
		
		
		/*
		public void openPropertyDialog(NavigationView view, Shell shell,
				boolean new_flg) {
			property_dialog.setNewFlg(new_flg);
			
			if(property_dialog==null) {
				property_dialog = new SqlPropertyDialog(shell);
			}
			property_dialog.createSShell(); 
			property_dialog.setView(view);
			property_dialog.show();
			property_dialog.setTreeNode(this);
		}*/
		
	}
