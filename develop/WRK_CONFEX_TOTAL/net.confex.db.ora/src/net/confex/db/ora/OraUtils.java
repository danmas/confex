package net.confex.db.ora;

//import java.sql.Connection;

import java.sql.SQLException;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class OraUtils {


	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
	 * Insert with returning number // //ins_sql = "INSERT INTO PSI_ATTRIBUTES
	 * (AID, NAME, Data_Type) "+ // " VALUES ('115','ttt',2) RETURNING ID INTO
	 * :1";
	 */
	public static long insertWithReturningNum(OraConnection connection,
			String ins_sql) throws SQLException {
		String sql = "begin ? := COM_W3_UPLOAD.insertWithReturningNum(?);  end;";
		OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection()
				.prepareCall(sql);
		ocs.setString(2, ins_sql);
		ocs.registerOutParameter(1, OracleTypes.NUMBER);
		ocs.execute();
		long ret = ocs.getLong(1);
		ocs.close();
		return ret;
	}

	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
	 * Insert with returning varchar2 // //ins_sql = "INSERT INTO PSI_ATTRIBUTES
	 * (AID, NAME, Data_Type) "+ // " VALUES ('115','ttt',2) RETURNING AID INTO
	 * :1";
	 */
	public static String insertWithReturningStr(OraConnection connection,
			String ins_sql) throws SQLException {
		String sql = "begin ? := COM_W3_UPLOAD.insertWithReturningStr(?);  end;";
		OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection()
				.prepareCall(sql);
		ocs.setString(2, ins_sql);
		ocs.registerOutParameter(1, OracleTypes.VARCHAR);
		ocs.execute();
		String ret = ocs.getString(1);
		ocs.close();
		return ret;
	}

	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	public static String execSqlFunc0RSEx(OraConnection connection, String sql)
			throws SQLException {
		// String sql="begin ? := COM_W3_UPLOAD.insertWithReturningStr; end;";
		OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection()
				.prepareCall(sql);
		ocs.registerOutParameter(1, OracleTypes.VARCHAR);
		ocs.execute();
		String ret = ocs.getString(1);
		ocs.close();
		return ret;
	}

	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	public static String execSqlFunc1I2IRSEx(OraConnection connection, String sql,
			int p1, int p2) throws SQLException {
		// String sql="begin ? := COM_W3_UPLOAD.insertWithReturningStr; end;";
		OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection()
				.prepareCall(sql);
		ocs.registerOutParameter(1, OracleTypes.VARCHAR);
		ocs.setInt(2, p1);
		ocs.setInt(3, p2);
		ocs.execute();
		String ret = ocs.getString(1);
		ocs.close();
		return ret;
	}

	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	public static long execSqlFunc1LRLEx(OraConnection connection, String sql,
			long p1) throws SQLException {
		// String sql="begin ? := package.Func(?); end;";
		OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection()
				.prepareCall(sql);
		ocs.registerOutParameter(1, OracleTypes.NUMBER);
		ocs.setLong(2, p1);
		ocs.execute();
		long ret = ocs.getLong(1);
		ocs.close();
		return ret;
	}

	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	public static String execSqlFunc1S2SRSEx(OraConnection connection, String sql,
			String p1, String p2) throws SQLException {
		// String sql="begin ? := COM_W3_UPLOAD.insertWithReturningStr; end;";
		OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection()
				.prepareCall(sql);
		ocs.registerOutParameter(1, OracleTypes.VARCHAR);
		ocs.setString(2, p1);
		ocs.setString(3, p2);
		ocs.execute();
		String ret = ocs.getString(1);
		ocs.close();
		return ret;
	}

	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	public static long execSqlFuncP0RL(OraConnection connection, String sql)
			throws SQLException {
		// String sql="begin ? := COM_W3_UPLOAD.insertWithReturningStr; end;";
		OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection()
				.prepareCall(sql);
		ocs.registerOutParameter(1, OracleTypes.NUMBER);
		ocs.execute();
		long ret = ocs.getLong(1);
		ocs.close();
		return ret;
	}

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	public static void execSqlProcP1S2SR0(OraConnection connection, String sql,
			String sp1, String sp2) throws SQLException {
		// String sql="begin COM_W3_UPLOAD.insertWithReturningStr(?,?); end;";
		OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection()
				.prepareCall(sql);
		ocs.setString(1, sp1);
		ocs.setString(2, sp2);
		ocs.execute();
		ocs.close();
	}

	/**
	 * Достает пакет из БД и кладет в out_dir с префиксом "cre_pkg_"
	 * 
	 * @param pkg_name -
	 *            имя пакета
	 * @param out_dir -
	 *            выходная директория если директории нет то будет создана REM 2 -
	 *            type of PL/SQL program unit (FUNCTION, PACKAGE, PACKAGE BODY)
	 */
	/*
	 * public static void extractDdlPackage(String pkg_name, String out_dir)
	 * throws Exception { String s = "SELECT 'CREATE OR REPLACE ' FROM dual \n" +
	 * "UNION ALL \n" + "SELECT text \n" + "FROM (SELECT text \n" + " FROM
	 * user_source \n" + " WHERE NAME = UPPER('%PROC_NAME%') \n" + " AND TYPE =
	 * UPPER('PACKAGE') \n" + " ORDER BY line) \n "; s =
	 * Strings.replace(s,"%PROC_NAME%",pkg_name.toUpperCase()); String
	 * text=OraUtils.getTextFromSqlSel(s,1,""); if(text==null) { throw new
	 * Exception("Procedute not found!"); } s = "SELECT 'CREATE OR REPLACE '
	 * FROM dual \n" + "UNION ALL \n" + "SELECT text \n" + "FROM (SELECT text
	 * \n" + " FROM user_source \n" + " WHERE NAME = UPPER('%PROC_NAME%') \n" + "
	 * AND TYPE = UPPER('PACKAGE BODY') \n" + " ORDER BY line) \n "; s =
	 * Strings.replace(s,"%PROC_NAME%",pkg_name.toUpperCase()); String
	 * text2=OraUtils.getTextFromSqlSel(s,1,""); if(text2==null) { throw new
	 * Exception("Procedute not found!"); } text = text + "\n/\n" + text2 +
	 * "\n/\n";
	 * 
	 * //-- если директории нет то создаем File f = new File(out_dir);
	 * if(!f.exists()) { f.mkdirs(); }
	 * 
	 * String tot_name=out_dir+"cre_pkg_"+pkg_name.toUpperCase()+".sql";
	 * 
	 * //Utils.writeStringToFileEx(text,tot_name); System.out.println(" Выгружен
	 * пакет "+ pkg_name.toUpperCase() +" в " + tot_name); }
	 */

}
