package net.confex.sqlexplorer.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.confex.db.DbUtils;
import net.confex.db.JdbcConnection;
import net.confex.db.tree.JdbcConnectionNode;
import net.confex.db.tree.SqlTreeObject;
import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.ICompositeProvider;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITableProvider;
import net.confex.tree.ITreeNode;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.sourceforge.sqlexplorer.IConstants;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.util.QueryTokenizer;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.Node;



public class SqlResultCompositeNode extends SqlTreeObject 
	implements ICompositeProvider, ITableProvider {

	private String maxrows = "100";  

	
	public String getAboutString() { return Translator.getString("ABOUT_SqlResultCompositeNode"); }
	
	
	/**
	 * Установка иконки по умолчанию для класса
	 */
	public String getDefaultImage() {
		return "dbexplorer16x16x32.png";
	}
	
	
	public SqlResultCompositeNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	/**
	 * Cоздает экземпляр того же класса !!! Должен быть переопределен в дочерних
	 * классах
	 * 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new SqlResultCompositeNode(getConfigTree(),null); 
	}
	

	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if(maxrows!=null && !maxrows.equals("")) {
			str_xml += "<maxrows>\n"
				+Utils.toHtmlSpecialEntities(maxrows)+"\n"; 
			str_xml += "</maxrows>\n"; 
		}
		return str_xml;
	}
	
	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("maxrows")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setMaxrows(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
	}
	
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new SqlResultCompositePropertyDialog(shell);
	}
	
	
	/**
	 * Одиночный запуск. Открывает вид результата запроса.
	 */
	public void run(IViewPart view) {
		IWorkbenchPage page = view.getSite().getPage();
		
		try {
			String name;
			if(getName()==null || getName().equals("")) {
				name = "Unknown";
			} else {
				name = TreeUtils.doAllSubstitutions(this, getName());
			}
			SqlResultsViewMy resultsView = (SqlResultsViewMy) view
        		.getSite().getPage().showView(SqlResultsViewMy.ID,name,IWorkbenchPage.VIEW_ACTIVATE);
        
	        resultsView.setPartName(name);
	    
	        //-- Создаем SQLConnection
	        SQLConnection sql_conn = getSQLConnection(view);
	        
	        List queryStrings = getQueryStrings();
	        
	    	String querySql = "";
	        try {
		        while (!queryStrings.isEmpty()) {
		
		        	querySql = (String) queryStrings.remove(0);
		
		        	if (querySql != null) {
		        		//System.out.println("sql ="+TreeUtils.doAllSubstitutions(this, querySql));
		        		SQLExecutionMy sql_exec = new SQLExecutionMy(null, 
		        			view, querySql, Integer.valueOf(getMaxrows()).intValue(), sql_conn);
		        		resultsView.addSQLExecution(sql_exec);
		        	}
		        }
	        } catch(Exception ex) {
	        	MessageDialog.openError(page.getWorkbenchWindow().getShell()
	        			,this.getName()
	        			,Translator.getString("MSG_ERR_ON_EXECUTE_SQL")+" " +querySql+". \n\n"+ex.getMessage());
	        	setErrorState();
	        }
		} catch(Exception e) {
        	MessageDialog.openError(page.getWorkbenchWindow().getShell()
        			,this.getName()
        			,Translator.getString("MSG_ERR_ON_EXECUTE_SQL")+"  \n\n"+e.getMessage());
        	setErrorState();
		}
	}

	
	JdbcConnection internal_connection = null;
	
	
	/**
	 * Ищет по всем родительским коннекцию и на ее основе создает SQLConnection
	 * @param navigation_view
	 * @return
	 */
	private SQLConnection getSQLConnection(IViewPart view) {
		IWorkbenchPage page = view.getSite().getPage();

		JdbcConnection connection = internal_connection;
		
		if(connection==null) {
			connection = DbUtils.searchJdbcConnectionUp(this);
			
			if(connection==null) {
				System.err.println(Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC"));
	        	MessageDialog.openError(page.getWorkbenchWindow().getShell()
	        			,this.getName()
	        			,Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC"));
	        	setErrorState();
	        	return null;
			}
		}
			
    	Connection conn = connection.getConnection();
    	if(conn==null) {
    		MessageDialog.openError(page.getWorkbenchWindow().getShell()
        			,this.getName()
        			,Translator.getString("MSG_ERR_CANT_SETUP_CONNECTION"));
    		setErrorState();
    		return null;
    	}
        SQLConnection sql_conn = new SQLConnection(conn,null);
        return sql_conn;
	}
	
    
	/**
	 * Создает массив строк запросов
	 * В sql выражение производятся все подстановки
	 *  
	 * @return
	 */
    private List getQueryStrings() {
        List queryStrings = new ArrayList();
        Preferences prefs = SQLExplorerPlugin.getDefault().getPluginPreferences();

        String queryDelimiter = prefs.getString(IConstants.SQL_QRY_DELIMITER);
        String alternateDelimiter = prefs.getString(IConstants.SQL_ALT_QRY_DELIMITER);
        String commentDelimiter = prefs.getString(IConstants.SQL_COMMENT_DELIMITER);
        //-- выполняем подстановки
        String s = TreeUtils.doAllSubstitutions(this, getSql());
        QueryTokenizer qt = new QueryTokenizer(s, queryDelimiter, alternateDelimiter, commentDelimiter);
        while (qt.hasQuery()) {
            final String querySql = qt.nextQuery();
            // ignore commented lines.
            if (!querySql.startsWith("--")) {
                queryStrings.add(querySql);
            }
        }
        return queryStrings;
    }


    /*
	private void run_lowlevel(NavigationView navigation_view) {
		IWorkbenchPage page = navigation_view.getSite().getPage();

		JdbcConnection connection = null;
		// -- сначала находим и получаем jdbc коннекцию
		ITreeNode p = getParent();
		boolean isOk=false;
		while(p!=null) {
			if(p instanceof JdbcConnectionNode) {
				isOk = ((JdbcConnectionNode)p).createConnection();
				connection = ((JdbcConnectionNode)p).getConnection();
				break;
			}
			p = p.getParent();
		}
		if(!isOk || connection==null || connection.isClosed() ) {
			System.err.println(Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC"));
        	MessageDialog.openError(page.getWorkbenchWindow().getShell()
        			,this.getName()
        			,Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC"));
        	setErrorState();
        	return;
		}
		// try {
			// System.out.println("<"+sql+">");
			
        	// -- формируем html страницу
        	Connection conn = connection.getConnection();
        	if(conn==null) {
        		MessageDialog.openError(page.getWorkbenchWindow().getShell()
	        			,this.getName()
	        			,Translator.getString("MSG_ERR_CANT_SETUP_CONNECTION"));
        		setErrorState();
        		return;
        	}
        	// sql_str = Strings.replace(getSql(), "\n", " ");
        	// sql_str = Strings.replace(sql_str, "\r", " ");
        	
        	String querySql = "";
            try {
            // } catch (Exception e) {
            // SQLExplorerPlugin.error("Error creating sql execution tab", e);
            // }
        	
			 // sql_exec.setComposite(Composite composite);
			 // sql_exec.displayResults();
        	
        } catch(Exception ex) {
        	MessageDialog.openError(page.getWorkbenchWindow().getShell()
        			,this.getName()
        			,Translator.getString("MSG_ERR_ON_EXECUTE_SQL")+" " +querySql+". \n\n"+ex.getMessage());
        	setErrorState();
        }
	}*/


	// protected IPropertyDialog newPropertyDialog(Shell shell) {
	// return new SqlPropertyDialog(shell);
	// }
	
	Composite m_parent = null;
	
	public void refreshComposite() {
		if(m_parent!=null && sql_exec!=null) {
			//System.out.println("refreshComposite()");
			try {
				//clearParent();
				sql_exec.clearComposite();
   			 	//-- каждый раз переопределяем sql выражение с учетом подстановок 
		        List queryStrings = getQueryStrings();
		        
		    	String querySql = "";
		        while (!queryStrings.isEmpty()) {
		
		        	querySql = (String) queryStrings.remove(0);
		
		        	if (querySql != null) {
		        		sql_exec.setSqlStatement(querySql);
		        		sql_exec.doExecutionSync();
		        	}
		        }
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			m_parent.layout();
			m_parent.redraw();
		}
	}
	
	
	public void disposeComposite() {
		m_parent = null;
	}

	
	SQLExecutionMy sql_exec = null;

	
	public TableCursor getCursor() {
		if(sql_exec==null) {
			System.err.println("sql_exec==null");
			return null;
		}
		
        if(sql_exec.getTable()==null) {
			System.err.println("sql_exec.getTable()==null");
			return null;
        }
		return sql_exec.getTable().getCursor();
	}

	IColorProvider colorProvider = null;
	
	public void setColorProvider(IColorProvider colorProvider) {
		this.colorProvider = colorProvider;
		/*
		if(sql_exec==null) {
			System.err.println("sql_exec==null");
			return;
		}
        if(sql_exec.getTable()==null) {
			System.err.println("sql_exec.getTable()==null");
			return;
        }
        sql_exec.getTable().setColorProvider(colorProvider);
        */
	}

	
	public void setCursorAtRowColumn(int row, int column) {
		if(sql_exec==null) {
			System.err.println("sql_exec==null");
			return;
		}
        if(sql_exec.getTable()==null) {
			System.err.println("sql_exec.getTable()==null");
			return;
        }
        //sql_exec.getTable().setSelection(row, column); //cursor.setSelection(row, column);
        
	}
	
	
	public void makeComposite(Composite parent, ViewPart view,IProgressMonitor monitor) {
		IWorkbenchPage page = view.getSite().getPage();
		
        //-- Создаем SQLConnection
        SQLConnection sql_conn = getSQLConnection(view);
        
        List queryStrings = getQueryStrings();
        
    	String querySql = "";
        try {
	        while (!queryStrings.isEmpty()) {
	
	        	querySql = (String) queryStrings.remove(0);
	
	        	if (querySql != null) {
	        		/*SQLExecutionMy*/ sql_exec = new SQLExecutionMy(null, 
	        			view, querySql, Integer.valueOf(getMaxrows()).intValue() ,sql_conn);
	        		sql_exec.setColorProvider(colorProvider);

	                Composite composite0 = new Composite(parent, SWT.NONE);
	                composite0.setLayout(new GridLayout());
	                composite0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	                
	   			 	sql_exec.setComposite(composite0);
	   			 	sql_exec.doExecutionSync();
	   			 	
	   			 	/*
	   			 Object c = sql_exec.getCursor(); //!!!
					if(c==null)
						System.err.println( " c == null" );
	   			 	*/
	   			 	composite0.layout();
	   			 	parent.layout();
	   			 	parent.redraw();
	   			 	m_parent = parent;
	        	}
	        }
        } catch(Exception ex) {
        	MessageDialog.openError(page.getWorkbenchWindow().getShell()
        			,this.getName()
        			,Translator.getString("MSG_ERR_ON_EXECUTE_SQL")+" " +querySql+". \n\n"+ex.getMessage());
        	setErrorState();
        }
	}


	public String getMaxrows() {
		return maxrows;
	}


	public void setMaxrows(String maxrows) {
		this.maxrows = maxrows;
	}


	
}
