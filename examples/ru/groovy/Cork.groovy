import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
 
import org.eclipse.swt.widgets.Composite
import net.confex.tree.ITreeNode
import net.confex.views.NavigationView
 
class Cork{
            def parent         //-- родительский композит
            def thisGroovyNode //-- этот узел в дереве
            def runViewPart    //-- Вид в котором выполняется форма
            def monitor //-- Монитор выполнения 
            
            //Cork(){}
            
            /*
            Cork(Composite p_parent
                                   , ITreeNode p_thisGroovyNode
                                   , ViewPart p_runViewPart
                                   , IProgressMonitor p_monitor
                                   ) {
                        parent = p_parent
                        thisGroovyNode = p_thisGroovyNode
                        runViewPart = p_runViewPart
                        monitor = p_monitor
            }*/
 
            
            public static void main(String[] args) {
            }
 
            public void run() {
			try {
			   def node = thisGroovyNode
			   def String l_name = ""
			   node = node.getParent()
			   l_name = node.getName()
			   println node.getName()
			   node = node.getChildWithName(l_name)
			   println node.getName() +"  "+ node.getCommand()
			   node.run(runViewPart)
			} catch( Exception e) {
			   System.err.println(" "+e.getMessage())
			}
	} //-- run()                      
            
}
 
//Cork form = new Cork(parent,thisGroovyNode,runViewPart,monitor);
//println runViewPart
//form.run();