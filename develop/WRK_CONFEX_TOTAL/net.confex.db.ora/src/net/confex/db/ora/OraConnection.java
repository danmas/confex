package net.confex.db.ora;



import java.sql.Connection;
import java.sql.DriverManager;

import net.confex.db.JdbcConnection;
import oracle.jdbc.driver.OracleDriver;



//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
// Класс коннекции
// !!! с коннекцией нужно работать только через getConnection.
// Если коннекции еще нет или закрыта то она будет создана.
//
// История изменений:
// ФИО         Дата     Комментарии
// ---------   ------   -------------------------------------------
// Еремеев     25.06.04 Создана
//
// Версия 0.1
//
// RDTEX JSC
// WWW http://www.rdtex.ru
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


public class OraConnection extends JdbcConnection {

	
	/**
	 * Создается JDBC коннекция специфичная для каждого драйвера
	 * @return
	 * 
	 */
	protected Connection cereate_connection_on_server() throws Exception {
        return DriverManager.getConnection("jdbc:default:connection:");
	}

	protected Connection cereate_connection(String url) throws Exception {
		DriverManager.registerDriver(new OracleDriver());
		return DriverManager.getConnection(url);
	}
	
	
	protected Connection cereate_connection(String url, String user, String password) throws Exception {
        DriverManager.registerDriver(new OracleDriver());
        return DriverManager.getConnection(url,user,password);
	}
	
}
