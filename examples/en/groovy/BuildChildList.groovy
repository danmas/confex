import net.confex.tree.VarsNode

class BuildChildList {
	def thisGroovyNode

	void run() {
		   
		//-- get root 
		def root = thisGroovyNode.getRoot()
		
		println "\n List vars for root:"
		def children = root.getChildren()
		(0..children.length-1).each() { i->
			println children[i].name // or .getName()
		}
		
		println "\n Childrens list of VarsNode type for this node:"
		children = thisGroovyNode.getChildren()
		(0..children.length-1).each() { i->
			if(children[i] instanceof VarsNode)
				println children[i].name 
		}
		
		def node = thisGroovyNode.getChildWithName("vars3")
		println node.name 
	} 

}