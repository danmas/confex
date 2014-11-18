/*
Task: 
	Launch node with name "build test" in async mode
	from this grovy script
*/


class  TestRunAsync {

        def parent         //-- родительский композит
        def thisGroovyNode //-- этот узел в дереве
        def runViewPart //-- Вид в котором выполняется форма
        def monitor //-- Монитор выполнения 

	void run() {
       	if(runViewPart==null) {
       		System.err.println "runViewPart must be defined for launtching this method!"
       	}
        try {	
	   		def node = thisGroovyNode.getParent().getChildWithName("test")		
			node = node.getChildWithName("build test")		
			if(node==null) {
				System.err.println "Can't find \"build test\"!"
			}
	   		println " executing  node.runNodeAsyncBatch(runViewPart)"
	   		node.runNodeAsyncBatch(runViewPart)
	   		
        } catch(Exception e) {System.err.println e.getMessage() }	
	}


}