import groovy.sql.Sql
 
 
public class Utils {
    static def m_sql
    static def thisGroovyNode
    
    static Sql getSql() {
    	try {
                if(m_sql==null) {
                           def server_db = thisGroovyNode.getStrVarUp("server_db")
                           def sid = thisGroovyNode.getStrVarUp("sid")
                           def owner = thisGroovyNode.getStrVarUp("owner")
                           def owner_pwd = thisGroovyNode.getStrVarUp("owner_pwd")
                           
                    	println "Подключаемся к базе данных... "+"jdbc:oracle:thin:@$server_db:1521:$sid, $owner"
                           
                           m_sql = Sql.newInstance("jdbc:oracle:thin:@$server_db:1521:$sid", "$owner", "$owner_pwd", "oracle.jdbc.driver.OracleDriver")
                           if(m_sql==null) {
                        	   System.err.println "Не удалось соединиться с базой!"
                           }
                }
                return m_sql
    	} catch(Exception e) {   System.err.println(" "+e.getMessage()) }
    }
}//-- class Utils
