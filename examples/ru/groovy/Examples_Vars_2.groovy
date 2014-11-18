/**
*   Examples_Vars_2.groovy
*   
*   Использование конфекс-переменных 
*	Запись переменной
*
*   Автор: Роман Еремеев, 2007 



В этом примере по нажатию кнопки перезаписывается
переменная "var 1"

thisGroovyNode.setVarUp("var 1",t.text)

*/


import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode


/**
*    Определение класса
*/
class Examples_Vars_2 {
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
			
			shell = swt.composite {	
				//-- задаем тип планировщика
			    gridLayout(numColumns:3)         //-- колонок 3 
				gridData( style:"fill_both" )    //-- растягивать во все стороны
				//-- задаем надпись
				label( style:"none", text:"Введите новое значение переменной var 1: " )
				def t = text( style:"border",text:'#{var 1}')
				button(text:"Выполнить") {
						onEvent(type:"Selection",closure:{
							thisGroovyNode.setVarUp("var 1",t.text)
						})
				}
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}
