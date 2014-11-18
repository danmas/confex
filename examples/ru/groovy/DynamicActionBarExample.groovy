/**
*   Динамическое встраивание    

*   Автор: Роман Еремеев, 2007 
*/  
import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import net.confex.tree.ITreeNode
import org.eclipse.ui.IActionBars;

 
 
/**
*    Определение класса
*/
class DynamicActionBarExample {
	//-- переменные для связи груви скрипта с конфексным деревом
	//   и средой выполнения
	def  parent         //-- родительский композит
	//def  thisGroovyNode //-- этот узел в дереве
	def  runViewPart    //-- Вид в котором выполняется форма
	//def  monitor 		//-- Монитор выполнения 

	//-- создаем построитель SWT 	
	def swt = new SwtBuilder() 
	
	// Процедура для запуска формы
	public void run() {
		//new GC2().run()
		
		try { 
			//-- включаем swt построитель в родительский композит
			swt.setCurrent(parent);
			 
			def action = new ActionMy(); 

			swt.composite {	
				//-- задаем тип планировщика
			    gridLayout(numColumns:2)         //-- колонок 2 
				gridData( style:"fill_both" )    //-- растягивать во все стороны
				//-- задаем надпись
				label( style:"none", text:"Пример динамического встраивания кнопки в панель вида. " )
				button(text:'Push me') {
					onEvent(type:'Selection',closure:{
						try {
						IActionBars bars = runViewPart.getViewSite().getActionBars()
						bars.getToolBarManager().add(action)
						bars.getToolBarManager().update(true);
						} catch( Exception e) {
							System.err.println(" "+e.getMessage())
						}
					})
				}
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}