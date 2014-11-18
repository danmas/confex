/**
*   ExampleEmbedTable.groovy
*   
*   Пример встроенной таблицы
*
*   Автор: Роман Еремеев, 2007 
*/
import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode

import net.confex.tree.ICompositeProvider


/**
*    Определение класса
*/
class ExampleEmbedTable {
	//-- переменные для связи груви скрипта с конфексным деревом
	//   и средой выполнения
	def Composite parent         //-- родительский композит
	def ITreeNode thisGroovyNode //-- этот узел в дереве
	def ViewPart  runViewPart    //-- Вид в котором выполняется форма
	def IProgressMonitor monitor //-- Монитор выполнения 

	
	def embeded_table   //-- 1) создаем переменную для встроенной таблицы 
	def comp_for_table  //--    создаем переменную под композит содержащий таблицу 
	
	//-- создаем построитель SWT 	
	def swt = new SwtBuilder() 

	
	// Процедура для запуска формы
	public void run() {
		try {
			//-- включаем swt построитель в родительский композит
			swt.setCurrent(parent);
			
			//-- 2) связываем переменную с дочерним узлом     		
			embeded_table = thisGroovyNode.getChildWithName("table1");
			if(embeded_table==null)
				System.err.println("Не могу найти встроенную таблицу 'table1' ")
			if(!embeded_table instanceof ICompositeProvider) 	
				System.err.println("Встроенная таблица не является типом ICompositeProvider")
			
			swt.composite {	
				//-- задаем тип планировщика
			    gridLayout(numColumns:1)         //-- колонок 2 
				gridData( style:"fill_both" )    //-- растягивать во все стороны
				//-- создаем композит содержащий таблицу
				comp_for_table = composite {
							gridData( style:"fill_both" )
				         	gridLayout(numColumns:1) 
				}
			} //-- swt.composite
			
			//-- встраиваем таблицу в ее композит 
			((ICompositeProvider)embeded_table).makeComposite(comp_for_table,runViewPart,monitor)
			//-- перестраиваем композит под таблицу 
        	comp_for_table.layout()
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}