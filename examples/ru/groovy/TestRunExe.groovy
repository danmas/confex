

class TestRunExe  {

	//-- ���������� ��� ����� ����� ������� � ���������� �������
	//   � ������ ����������
	def parent         //-- ������������ ��������
	def thisGroovyNode //-- ���� ���� � ������
	def runViewPart    //-- ��� � ������� ����������� �����
	def monitor //-- ������� ����������
 
	void run() {
		//...
		//-- ���������� ����������     		
		def node = thisGroovyNode.getChildWithName("run MS Word");
		if(node==null) {
			System.err.println("�� ���� ����� ����  'run MS Word' ")
			return
		}
		println "���� ������!"
		//-- run is here!
		node.run(runViewPart);
		//...		
	}
	
	

}
