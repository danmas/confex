

class TestNewScript  {

	//-- ���������� ��� ����� ����� ������� � ���������� �������
	//   � ������ ����������
	def parent         //-- ������������ ��������
	def thisGroovyNode //-- ���� ���� � ������
	def runViewPart    //-- ��� � ������� ����������� �����
	def monitor //-- ������� ����������
 
	void run() {
		//...
		//-- ���������� ����������     		
		def node = thisGroovyNode.getChildWithName("run_test_node");
		if(node==null) {
			System.err.println("�� ���� ����� ����  'run_test_node' ")
			return
		}
		println "���� ������!"
		//...
		node.run(runViewPart,null)
	}
	
	

}
