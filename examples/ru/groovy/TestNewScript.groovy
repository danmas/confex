

class TestNewScript  {

	//-- переменные для связи груви скрипта с конфексным деревом
	//   и средой выполнения
	def parent         //-- родительский композит
	def thisGroovyNode //-- этот узел в дереве
	def runViewPart    //-- Вид в котором выполняется форма
	def monitor //-- Монитор выполнения
 
	void run() {
		//...
		//-- используем переменную     		
		def node = thisGroovyNode.getChildWithName("run_test_node");
		if(node==null) {
			System.err.println("Не могу найти узел  'run_test_node' ")
			return
		}
		println "Узел найден!"
		//...
		node.run(runViewPart,null)
	}
	
	

}
