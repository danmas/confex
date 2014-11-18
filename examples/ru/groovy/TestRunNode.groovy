
class TestRunNode  {

	//-- ���������� ��� ����� ����� ������� � ���������� �������
	//   � ������ ����������
	def parent         //-- ������������ ��������
	def thisGroovyNode //-- ���� ���� � ������
	def runViewPart    //-- ��� � ������� ����������� �����
	def monitor //-- ������� ����������

	void run() {
		try {
			//-- ��������� ���������� � �������� �����     		
			def node = thisGroovyNode.getChildWithName("run_test_node");
		
			if(node==null) {
				System.err.println("�� ���� ����� ����  'run_test_node' ")
				return
			}
			//-- run is here!
			node.run(runViewPart);
		} catch (Exception ex) {
			System.err.println " "+ ex.getMessage()
		}
	}
	
	

}