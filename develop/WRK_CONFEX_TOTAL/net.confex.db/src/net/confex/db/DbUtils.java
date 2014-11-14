package net.confex.db;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import net.confex.db.tree.JdbcConnectionNode;
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;
import net.confex.utils.TreeUtils;


public class DbUtils {

	
	
	public static JdbcConnection searchJdbcConnectionUp(ITreeNode node) {
		ITreeNode p = node.getParent();

		boolean isOk = false;
		JdbcConnection connection = null;

		p = TreeUtils.searchNodeUp(node,JdbcConnectionNode.class);
		if(p!=null) {
			isOk = ((JdbcConnectionNode)p).createConnection();
			connection = ((JdbcConnectionNode)p).getConnection();
		}
		if (!isOk || connection == null || connection.isClosed()) {
			System.err.println(Translator.getString("MSG_CANT_FIND_AND_SETUP_JDBC")); //$NON-NLS-1$
			return null;
		}
		return connection;
	
	}

	
	public static void executeSQL(Connection connection, String s_call) {
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			ocs.execute(s_call);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (ocs != null)
					ocs.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	public static boolean executeSql(Connection connection, String s_call) {
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			ocs.execute(s_call);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		} finally {
			try {
				if (ocs != null)
					ocs.close();
			} catch (SQLException e) {
				return false;
			}
		}
		return true;
	}

	public static void executeSqlEx(Connection connection, String s_call)
			throws Exception {
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			ocs.execute(s_call);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (ocs != null)
					ocs.close();
			} catch (SQLException e) {
			}
		}
	}

	public static long executeQueryRetLong(Connection connection, String s_call) {
		long ret = 0;
		ResultSet rs = null;
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			rs = ocs.executeQuery(s_call);
			while (rs.next())
				ret = rs.getLong(1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ocs != null)
					ocs.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
		return ret;
	}

	
	/**
	 * Выполняет запрос возвращающий строковую переменную В случае ошибки
	 * возвращается NULL
	 * 
	 * @param s_call
	 * @return
	 */
	public static String executeQueryRetString(Connection connection,
			String s_call) {
		String ret = null;
		ResultSet rs = null;
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			rs = ocs.executeQuery(s_call);
			while (rs.next())
				ret = rs.getString(1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ocs != null)
					ocs.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
		return ret;
	}

	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
	 * Делает текст из текстового поля txt_field_nm SQL запроса. // в конце
	 * строк добавляется символ разделителя line_delim // R: null если ошибка
	 */
	public static String getTextFromSqlSel(Connection connection, String sql,
			String txt_field_nm, String line_delim) {
		String ret = "";
		String s = "";
		ResultSet rs = null;
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			rs = ocs.executeQuery(sql);
			while (rs.next()) {
				s = rs.getString(txt_field_nm);
				ret += s + line_delim;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			try {
				ocs.close();
			} catch (SQLException e2) {
				System.err.println(e2.getMessage());
			}
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ocs != null)
					ocs.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
		return ret;
	}
	

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
	 * Делает текст из текстового поля txt_field_nm SQL запроса. // в конце
	 * строк добавляется символ разделителя line_delim // R: null если ошибка
	 */
	public static String getTextFromSqlSel(Connection connection, String sql,
			int txt_field_id, String line_delim) {
		String ret = "";
		String s = "";
		ResultSet rs = null;
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			rs = ocs.executeQuery(sql);
			while (rs.next()) {
				s = rs.getString(txt_field_id);
				ret += s + line_delim;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			try {
				ocs.close();
			} catch (SQLException e2) {
				System.err.println(e2.getMessage());
			}
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ocs != null)
					ocs.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
		return ret;
	}

	public static long getLongFromSqlSel(Connection connection, String sql)
			throws SQLException {
		long ret = 0;
		ResultSet rs = null;
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			rs = ocs.executeQuery(sql);
			while (rs.next()) {
				ret = rs.getLong(1);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			ocs.close();
			throw new SQLException(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ocs != null)
					ocs.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
		return ret;
	}

	
	/**
	 * Return select as html table
	 * 
	 * @param s_call -
	 *            "select * from tru_trace_temp_log"
	 *            
	 * @return error messge if error was happens 
	 */
	public static String retSelectAsHtmlTable(Connection connection,
			String s_call) {
		String ret_str = "<table border=\"1\">\n";
		ResultSet rs = null;
		try {
			rs = DbUtils.retSelectRsEx(connection, s_call);
			if (rs == null) {
				return null;
			}
			ResultSetMetaData meta = rs.getMetaData();
			int numColumns = meta.getColumnCount();
			ret_str += "<tr>\n";
			for (int j = 1; j <= numColumns; ++j) {
				ret_str += "<th>" + meta.getColumnName(j) + "</th>\n";
			}
			ret_str += "</tr>\n";

			int i = 0;
			while (rs.next()) {
				ret_str += "<tr>\n";

				for (int j = 1; j <= numColumns; ++j) {
					Object obj = rs.getObject(j);
					String val;
					if (obj != null)
						val = obj.toString();
					else
						val = ""; // "null";
					ret_str += "<td>" + val + "</td>\n";
				}
				ret_str += "</tr>\n";
				i++;
			}
			rs.getStatement().close();
			rs.close();
			rs = null;
			ret_str += "</table>\n";
			// System.out.println(ret_str);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			ret_str = "<p><font color=FF0000>"+e.getMessage()+"</font></p>";
			//return null;
		} finally {
			if (rs != null) {
				try {
					rs.getStatement().close();
					rs.close();
				} catch (Exception e) {
				}
			}
		}
		return ret_str;
	}
	

	/**
	 * Return select as xml
	 * 
	 * @param s_call -
	 *            "select * from tru_trace_temp_log"
	 * @return - null, если ошибка в SQL-запросе; - "", если не возвращает ни
	 *         одной записи.
	 */
	public static String retSelectAsXml(Connection connection, String s_call) {
		String ret_str = "";
		try {
			ret_str = retSelectAsXmlEx(connection, s_call);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}
		return ret_str;
	}

	
	/**
	 * Return select as xml
	 * 
	 * @param s_call -
	 *            "select * from tru_trace_temp_log"
	 * @return - null, если ошибка в SQL-запросе; - "", если не возвращает ни
	 *         одной записи.
	 * @throws SQLException
	 */
	public static String retSelectAsXmlEx(Connection connection, String s_call)
			throws SQLException {
		String ret_str = "";
		ResultSet rs = null;
		try {
			rs = DbUtils.retSelectRs(connection, s_call);
			if (rs == null) {
				return null;
			}
			ResultSetMetaData meta = rs.getMetaData();
			int numColumns = meta.getColumnCount();
			while (rs.next()) {
				ret_str += "<Row>\n";

				for (int j = 1; j <= numColumns; ++j) {
					Object obj = rs.getObject(j);
					String val;
					if (obj != null)
						val = obj.toString();
					else
						val = ""; // "null";
					ret_str += "<" + meta.getColumnName(j) + ">" + val + "</"
							+ meta.getColumnName(j) + ">\n";
				}
				ret_str += "</Row>\n";
			}
			rs.getStatement().close();
			rs.close();
			rs = null;
			// } catch(SQLException e) {
			// System.err.println(e.getMessage());
			// return null;
		} finally {
			if (rs != null) {
				rs.getStatement().close();
				rs.close();
			}
		}
		return ret_str;
	}

	
	/**
	 * Возвращает ResultSet для запроса типа SELECT !!! первым выражением должно
	 * стоять rs.next() !!! по окончании нужно закрыть RS
	 * 
	 * @return null в случае ошибки
	 * 
	 * Пример:
	 * ResultSet rs=null; 
	 * try { 
	 *    rs= OraUtils.retSelectRs(s_call); 
	 *    if(rs==null) { return; } 
	 *    while(rs.next()) { 
	 *    	Date dt = rs.getDate(1); 
	 *    	Float close = rs.getFloat(2); 
	 *    	Float imax = rs.getFloat(3); 
	 *    	System.out.println("dt= "+dt+" close= "+close+" imax= "+imax ); 
	 *    } rs.getStatement().close();
	 * 	  rs.close(); 
	 *    rs=null; 
	 * } catch(SQLException e) { System.err.println(e.getMessage()); }
	 * 
	 */
	public static ResultSet retSelectRs(Connection connection, String s_call) {
		ResultSet rs = null;
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			rs = ocs.executeQuery(s_call);
			if (rs == null) {
				try {
					if (rs != null)
						rs.close();
					if (ocs != null)
						ocs.close();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
				return null;
			}
			// -- ret = rs.getString(1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			// TraceLight.writeError("!!! executeSQL "+s_call+ e.getMessage() );
			try {
				if (rs != null)
					rs.close();
				if (ocs != null)
					ocs.close();
			} catch (Exception e2) {
				System.err.println(e2.getMessage());
			}
			return null;
		}
		return rs;
	}

	
	/**
	 * Возвращает ResultSet для запроса типа SELECT !!! первым выражением должно
	 * стоять rs.next() !!! по окончании нужно закрыть RS
	 * 
	 * @throws SQLException
	 * 
	 * Пример:
	 * ResultSet rs=null; 
	 * try { 
	 *    rs= OraUtils.retSelectRs(s_call); 
	 *    if(rs==null) { return; } 
	 *    while(rs.next()) { 
	 *    	Date dt = rs.getDate(1); 
	 *    	Float close = rs.getFloat(2); 
	 *    	Float imax = rs.getFloat(3); 
	 *    	System.out.println("dt= "+dt+" close= "+close+" imax= "+imax ); 
	 *    } rs.getStatement().close();
	 * 	  rs.close(); 
	 *    rs=null; 
	 * } catch(SQLException e) { System.err.println(e.getMessage()); }
	 * 
	 */
	public static ResultSet retSelectRsEx(Connection connection, String s_call) throws SQLException {
		ResultSet rs = null;
		Statement ocs = null;
		try {
			ocs = connection.createStatement();
			rs = ocs.executeQuery(s_call);
			if (rs == null) {
				try {
					if (rs != null)
						rs.close();
					if (ocs != null)
						ocs.close();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			// TraceLight.writeError("!!! executeSQL "+s_call+ e.getMessage() );
			try {
				if (rs != null)
					rs.close();
				if (ocs != null)
					ocs.close();
			} catch (Exception e2) {}
			throw new SQLException(e.getMessage());
		}
		return rs;
	}


	public static void main(String[] args) {

		// String s_a_s1 = "alter sesion set events '10046 trace name context
		// forever, level 12'";
		// String s_a_s2 = "alter sesion set events '10046 trace name context
		// off'" ;

		// String s="F581560720/newTestXML2C.xml.gz";
		// System.out.println("
		// getFileNameFromUDkey("+s+")="+getFileNameFromUDkey(s) );

		// s="F1569803778/testXML.xml";
		// System.out.println("
		// getFileNameFromUDkey("+s+")="+getFileNameFromUDkey(s) );
/*
		try {
			OraConnection connection = new OraConnection();
			connection.createConnection(
					"jdbc:oracle:thin:@onyx.srv.rdtex.ru:1521:IAP", "xxx",
					"xxx");
			// Config.initConfig();
			String ret_str = retSelectAsHtmlTable(connection.getConnection(),
					"SELECT INSTR_NAME имя, currency валюта FROM iap_tds_instruments");
			System.out.println(ret_str);
		} catch (Exception e) {
			System.out.println(e.getMessage());

			e.printStackTrace();
		}
*/

	}
	
}
