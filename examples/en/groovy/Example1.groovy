/**
*   Example1.groovy
*   Shows launch the form inside grace application
*   Author: Roman Eremeev, 2007 
*/
import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode
 

/**
*    Groovy class Example1 
*/
class Example1 {
	//-- some variables for the link with application and confex tree
	def Composite parent         //-- parent composite
	def ITreeNode thisGroovyNode //-- this groovy node in confex tree
	def ViewPart  runViewPart    //-- Eclipse View 
	def IProgressMonitor monitor //-- Monitor (optional) 

	//-- create SWT builder 	
	def swt = new SwtBuilder() 
 
	
	// run() - is start method for launch form 
	public void run() {
		try {
			//-- 
			swt.setCurrent(parent);
			swt.composite {
				
			    gridLayout(numColumns:2)         //-- columns - 2 
				gridData( style:"fill_both" )    //--
				
				//-- make some GUI elemnts
				label( style:"none", text:"The example ---: " )
				text( style:"border",text:'Any text.')
				
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}