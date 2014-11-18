
class TestRunNode  {

	//-- переменные дл€ св€зи груви скрипта с конфексным деревом
	//   и средой выполнени€
	def parent         //-- родительский композит
	def thisGroovyNode //-- этот узел в дереве
	def runViewPart    //-- ¬ид в котором выполн€етс€ форма
	def monitor //-- ћонитор выполнени€

	void run() {
		try {
			//-- св€зываем переменную с дочерним узлом     		
			def node = thisGroovyNode.getChildWithName("run_test_node");
		
			if(node==null) {
				System.err.println("Ќе могу найти узел  'run_test_node' ")
				return
			}
			//-- run is here!
			node.run(runViewPart);
		} catch (Exception ex) {
			System.err.println " "+ ex.getMessage()
		}
	}
	
	

}