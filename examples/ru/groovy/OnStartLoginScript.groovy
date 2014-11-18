import net.confex.application.ConfexPlugin
import net.confex.views.ConfexView


class OnStartLoginScript {
	def parent, thisGroovyNode, runViewPart
	
	void run() {
		if(runViewPart instanceof ConfexView) {
			((ConfexView)runViewPart)
				.setApplicationStatusLine("New status line message!") 
		}
		println ConfexPlugin.getDefault().getStatusLine()	
	}

}