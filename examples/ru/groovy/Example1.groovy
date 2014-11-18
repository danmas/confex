/**
*   Example1.groovy
*   
*   Связь груви скрипта с конфекс средой
*
*   Автор: Роман Еремеев, 2007 
*/
import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode


/**
*    Определение класса
*/
class Example1 {
	//-- переменные для связи груви скрипта с конфексным деревом
	//   и средой выполнения
	def Composite parent         //-- родительский композит
	def ITreeNode thisGroovyNode //-- этот узел в дереве
	def ViewPart  runViewPart    //-- Вид в котором выполняется форма
	def IProgressMonitor monitor //-- Монитор выполнения 

	//-- создаем построитель SWT 	
	def swt = new SwtBuilder() 

	
	// Процедура для запуска формы
	public void run() {
		try {
			//-- включаем swt построитель в родительский композит
			swt.setCurrent(parent);
			swt.composite {	
				//-- задаем тип планировщика
			    gridLayout(numColumns:2)         //-- колонок 2 
				gridData( style:"fill_both" )    //-- растягивать во все стороны
				//-- задаем надпись
				label( style:"none", text:"Пример ---: " )
				text( style:"border",text:'Текст')
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}
