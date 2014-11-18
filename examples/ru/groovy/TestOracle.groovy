import groovy.sql.Sql

class TestOracle {
	
	def thisGroovyNode
	
	void run() {
		try {
			
		def conn_node = thisGroovyNode.searchNodeUp("DB_CONNECTION")
		if(conn_node==null) {
			System.err.println "Can't found 'DB_CONNECTION'"
			return
		}
		
		def conn = conn_node.getJdbcConnection()
		if(conn==null) {
			System.err.println "Can't get JdbcConnection"
			return
		}
		
		Sql sql = new Sql(conn.getConnection());
		sql.eachRow("select * from dept", {
			row-> println row.dname
		}) 
		
        sql.call("""declare
                begin
               select count(*) into ${Sql.INTEGER} from dept ;
               end;
               """
               ){t ->
                println "found ${t} "
                       // eachRow is a new method on GroovyResultSet
               }

             sql.call("""
declare
n number;
begin
insert into dept values (57,'qqq','aaa') returning deptno into n;                 
${Sql.INTEGER} := n;
end;
               """
               ){c ->
                println "Inserted ${c} "
               }
		} catch(Exception e) {System.err.println "Возможно коннекция не открыта "+e.getMessage()}
	}
 		
}
