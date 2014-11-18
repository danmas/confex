/**
*   Example1.groovy
*   
*   Пример встроенной таблицы с выделением 
*   строк цветом и обработки события перехода по строкам
*
*   Автор: Роман Еремеев, 2007

	Создаем дополнительный класс для обеспечения цветов 
	и обработки события выбора в таблице
	
    class TableProvider extends SelectionAdapter implements IColorProvider {

	--- Работа с событием выбора строки ---------------------------------

	Используется конструктор
	public TableProvider(ExampleEmbedColorTable main_,TableCursor cursor_) 

	в методе widgetSelected() происходит обработка события выбора строки
	
	
	--- Работа с цветом -------------------------------------------------
	
	Используется конструктор
	public TableProvider(ExampleEmbedColorTable main_) 

	в методах getForeground() и getBackground() происходит 
	выставление цвета строки
	
*/
import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode

import net.confex.tree.ICompositeProvider

import net.confex.tree.ITableProvider;
import org.eclipse.jface.viewers.IColorProvider
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.custom.TableCursor
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.graphics.Color

//-- С этого места начинается исполнение скрипта groovy

//-- Создается тестовый класс и связыввается со внешними переменными
//ExampleEmbedColorTable form = new ExampleEmbedColorTable(parent,thisGroovyNode,runViewPart,monitor);
//-- запускается на выполнение метод run() 
//form.run();


/**
*    Определение класса
*/
class ExampleEmbedColorTable {
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
	
	
	/*
	// Конструктор в котором инициируются переменные
	ExampleEmbedColorTable(Composite p_parent
			, ITreeNode p_thisGroovyNode
			, ViewPart p_runViewPart
			,IProgressMonitor p_monitor
			) {
		parent = p_parent
		thisGroovyNode = p_thisGroovyNode
		runViewPart = p_runViewPart
		monitor = p_monitor
	}*/


	protected void init() {
		//-- включаем swt построитель в родительский композит
		swt.setCurrent(parent);
		
		//-- 2) связываем переменную с дочерним узлом     		
		embeded_table = thisGroovyNode.getChildWithName("table1");
		if(embeded_table==null)
			System.err.println("Не могу найти встроенную таблицу 'table1' ")
		if(!embeded_table instanceof ICompositeProvider) 	
			System.err.println("Встроенная таблица не является типом ICompositeProvider")
	}
	
	
	// Процедура для запуска формы
	public void run() {
		try {
			init()
			
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
        	
			//-- Устанавливаем обработчик выбора строк и цветов        	
			setupColorProvider()
			setupSelectionListener()
        	
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
	
	
	/**
	*	Здесь устанавливается слушатель выбора строк
	*/
	private void setupSelectionListener() {
		if(embeded_table instanceof ITableProvider) {
			def cursor = ((ITableProvider)embeded_table).getCursor()
			if(cursor==null) {
				System.err.println " cursor == null"
				return
			}
			cursor.addSelectionListener(new TableProvider(this,cursor)); 
		} else {
			System.err.println "  embeded NOT instanceof ITableProvider"
		}
	}
	

	/**
	*	Здесь устанавливается рисователь цветов
	*/
	private void setupColorProvider() {
		if(embeded_table instanceof ITableProvider) {
			println "устанавливается рисователь цветов"
			embeded_table.setColorProvider(new TableProvider(this)); 
		} else {
			System.err.println "  embeded NOT instanceof ITableProvider"
		}
	}
}	
	
	
	//==============================================================================
		
	/**
	*   Класс для обеспечения цветов и обработки события выбора в таблице 
	*/
	public class TableProvider extends SelectionAdapter implements IColorProvider {
	def ExampleEmbedColorTable main

	def TableCursor cursor

	public TableProvider(ExampleEmbedColorTable main_,TableCursor cursor_) {
		main = main_
		cursor = cursor_
	}


	/**
	*   Метод вызываемый при выборе в таблице	
	*/
	public void widgetSelected(SelectionEvent e) {
		try {
			println cursor.getRow().getText(1) + " - " + cursor.getRow().getText(5)
			//main.task_combo.text = cursor.getRow().getText(0) 
		} catch(Exception ex) {
			System.err.println( ex.getMessage() )
			MessageDialog.openError(main.parent.getShell(), "Ошибка!", eх.getMessage()+ "\n\n")
			return 
		}
	}

	
	//
	// Работа с цветом
	//
	public TableProvider(ExampleEmbedColorTable main_) {
		main = main_
	}

	def blue_color = new Color(Display.getCurrent(), 0, 0, 250)
	def red_color = new Color(Display.getCurrent(), 250, 0, 0)
	def black_color = new Color(Display.getCurrent(), 0, 0, 0)
	def white_color = new Color(Display.getCurrent(), 255, 255, 255)
	
	
	public Color getForeground(Object element) {
		try {
			def sallary = element.getObjectValue(5)
			if(sallary<1250)
				return blue_color
			if(sallary>2900)
				return red_color
			return black_color
		} catch (Exception e){
			System.err.println( e.getMessage() )
			return new Color(null,155,0,0)
		}
	}

	
	public Color getBackground(Object element) {
		/*try {
			//def DataSetRow row = element;
			//if(element.getObjectValue(1) instanceof String) {
				println "element.getObjectValue(17)  "+element.getObjectValue(17)
				if((element.getObjectValue(17)).toString()!="<null>")
					return new Color(null,255,200,200);
				else
					return new Color(null,255,255,255);
			//}
			return null;
		} catch (Exception e){
			System.err.println( e.getMessage() )
			return new Color(null,255,0,0)
		}
		if(curDopParams==main.gisDopParams)
			return new Color(null,250,250,255);
		else if(curDopParams==main.mgpDopParams)
			return new Color(null,250,255,250);
		else if(curDopParams==main.gmlDopParams)
			return new Color(null,255,250,250);
		*/
		return white_color;
	}

}