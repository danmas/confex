/**
 * При переходе на groovy 1.5 пришлось 
 * заменить groovy.swt.CustomSwingBuilder на 
 * groovy.swing.SwingBuilder но при этом формы отображаются неправильно
 * 
 */
import groovy.swt.SwtBuilder
//import groovy.swt.CustomSwingBuilder
import groovy.swing.SwingBuilder
import org.eclipse.swt.SWT

class ex_modal_dialog {
	def parent
	def shell
	
	void run() {
       def swt = new SwtBuilder()
       def swing = new SwingBuilder() //new CustomSwingBuilder()        
       swt.setCurrent(parent);	
       shell = swt.shell ( text:'The Modal Dialog Demo',style:"APPLICATION_MODAL"/* | SWT.NO_TRIM*/ ) {
         	fillLayout()
         	
         	label( text:"this is a swt label" )
     		button(text:"Exit")  {
     			onEvent(type:"Selection", closure:{
     				println "Exit!!!"
     				shell.dispose()
     			})
     		}
         	
         	composite ( style:"border, embedded" ) {
         		fillLayout()
	         	swing.current = awtFrame()
				swing.tree()
			}
        }
        
		shell.open()
	
		/*
		while(! shell.isDisposed()) { 
			if (! shell.display.readAndDispatch()) {
				shell.display.sleep();
			}
		}*/
	}

}