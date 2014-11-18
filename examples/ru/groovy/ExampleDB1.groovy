import groovy.sql.Sql
//import oracle.jdbc.driver.OracleDriver


class ExamleDB1 {

	def m_sql
	
	public run() {
		main(null)
	}
	
	public static void main(String[] args) {
		try { 
		def exam = new ExamleDB1()
		
		//-- ������� ������ �������
		exam.readDepartms()
		//-- �������� �����
		exam.updateDepartm("20","R1")
		//-- ������� ������ �������
		exam.readDepartms()
		//-- �������� �����
		exam.updateDepartm("20","RESEARCH")
		//-- ������� ������ �������
		exam.readDepartms()
		
		} catch(Exception e) {
			System.err.println e.getMessage() 
		}
	}
	
	
	public Sql getSql() {
		if(m_sql==null) {
			m_sql = Sql.newInstance("jdbc:oracle:thin:@#{server_db}:1521:#{sid}", "#{owner}", "#{owner_pwd}", "oracle.jdbc.driver.OracleDriver")
			println "��������� ��������� "+"jdbc:oracle:thin:@#{server_db}:1521:#{sid}"+",oracle.jdbc.driver.OracleDriver"
		}
		return m_sql
	}

	
	void readDepartms() {
		try {
			def sql_str = """
				SELECT * FROM dept
			"""
			println sql_str
		
			getSql().eachRow( sql_str as String  ) {
				row->println row.DEPTNO + " " + row.DNAME;
			}
		} catch(Exception e) {
			System.err.println( e.getMessage() )
		}
	}	 

	
	void updateDepartm(p_deptno,p_dname) {
		try {
			def sql_str = """
				UPDATE dept SET dname='$p_dname' WHERE deptno = $p_deptno
			"""
			println sql_str
		
			getSql().execute( sql_str as String  )
			
		} catch(Exception e) {
			System.err.println( e.getMessage() )
		}
	}	 
	
	
	
}

