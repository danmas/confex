/**
*   Examples_Vars_1.groovy
*   
*   Использование конфекс-переменных 
*	Чтение переменной
*
*   Автор: Роман Еремеев, 2007 

  В этом примере происходит замена переменных их значениями
  label( style:"none", text:"#{var 1}: " )
  text( style:"border",text:'#{перем 2}')

  поиск переменных "var 1" и "перем 2" производится с узла в котором
  они используются вверх по конфекс дереву пока не будут найдены.
  Таким образом, наборы расположенные выше по дереву скрываются.
  
  //-- Чтение переменной во время выполнения
  thisGroovyNode.getStrVarUp("var1")
  
*/


import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode


/**
*    Определение класса
*/
class Examples_Vars_1 {
	//-- переменные для связи груви скрипта с конфексным деревом
	//   и средой выполнения
	def Composite parent         //-- родительский композит
	def ITreeNode thisGroovyNode //-- этот узел в дереве
	def ViewPart  runViewPart    //-- Вид в котором выполняется форма
	def IProgressMonitor monitor //-- Монитор выполнения 

	//-- создаем построитель SWT 	
	def swt = new SwtBuilder() 
	
	//-- верхний композит формы 
	def shell

	
	// Процедура для запуска формы
	public void run() {
		try {
			//-- включаем swt построитель в родительский композит
			swt.setCurrent(parent);
			
			println thisGroovyNode.getStrVarUp("var 1")
			println "CONFEX_DIR = " + thisGroovyNode.getStrVarUp("CONFEX_DIR")
			
			shell = swt.composite {	
				//-- задаем тип планировщика
			    gridLayout(numColumns:2)         //-- колонок 2 
				gridData( style:"fill_both" )    //-- растягивать во все стороны
				//-- задаем надпись
				label( style:"none", text:"#{var 1}: " )
				text( style:"border",text:'#{перем 2}')
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}
