package net.confex.db.mysql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

import net.confex.db.JdbcConnection;


public class MysqlConnection extends JdbcConnection  {
	
	
	protected Connection cereate_connection_on_server() throws Exception {
		return null;
	}
	
	
	/**
	 * Создается JDBC коннекция специфичная для каждого драйвера
	 * @return
	 * 
	 */
	protected Connection cereate_connection(String url) throws Exception {
        DriverManager.registerDriver((Driver)Class.forName(
        		"com.mysql.jdbc.Driver").newInstance());
		return DriverManager.getConnection(url);
	}

	
	//protected Connection cereate_connection() throws Exception {
    //    DriverManager.registerDriver((Driver)Class.forName(
	//	"com.mysql.jdbc.Driver").newInstance());
    //    return DriverManager.getConnection(getUrl(),getUser(),getPassword());
	//}

	
	protected Connection cereate_connection(
			String url, String user, String password) throws Exception {
        DriverManager.registerDriver((Driver)Class.forName(
		"com.mysql.jdbc.Driver").newInstance());
        return DriverManager.getConnection(url,user,password);
	}

	
	public static void main(String[] arg) {
	  MysqlConnection con = new MysqlConnection();

      try {
    	  con.createConnection("jdbc:mysql://localhost/test","root","uiaait");
          System.out.println ("Database connection established");
      } catch (Exception e) {
          System.err.println ("Cannot connect to database server. "+
        		  e.getMessage() );
      } finally  {
          try {
              con.closeConnection();
              System.out.println ("Database connection terminated");
          } catch (Exception e) { /* ignore close errors */ }
      }
	}
	

}
