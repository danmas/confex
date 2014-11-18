

class TestSearchNode  {

	//-- переменные для связи груви скрипта с конфексным деревом
	//   и средой выполнения
	def parent         //-- родительский композит
	def thisGroovyNode //-- этот узел в дереве
	def runViewPart    //-- Вид в котором выполняется форма
	def monitor //-- Монитор выполнения
 
	
	void run() {
		test_searchUp(thisGroovyNode,"Node 1")
		test_searchUp(thisGroovyNode,"Node 2")
		test_searchUp(thisGroovyNode,"Node 3")
		
		def node0 = test_searchUp(thisGroovyNode,"Node 0")
		if(node0==null) 
			return
			
		def node02 = test_searchChild(node0,"Node 02")
		if(node02==null)
			return
		test_searchChild(node02,"Node 022")
	}
	
	
	Object test_searchUp(from_node,node_name) {
		def node = from_node.searchNodeUp(node_name)
		if(node!=null)
			println node_name+" found with searchNodeUp()" 
		else 	
			System.err.println node_name+" Not found with searchNodeUp()"
		return node	
	}

	
	Object test_searchChild(from_node,node_name) {
		def node = from_node.getChildWithName(node_name)
		if(node!=null)
			println node_name+" found with getChildWithName()" 
		else 	
			System.err.println node_name+" Not found with getChildWithName()"
		return node	
	}
	
}
