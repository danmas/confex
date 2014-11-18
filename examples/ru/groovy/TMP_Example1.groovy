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


//-- С этого места начинается исполнение скрипта groovy

//-- Создается тестовый класс и связыввается со внешними переменными
//Example1 form = new Example1(parent,thisGroovyNode,runViewPart,monitor);
//-- запускается на выполнение метод run() 
//form.run();


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
	
	//-- верхний композит формы 
	def shell
	
	
	// Конструктор в котором инициируются переменные
	Example1(Composite p_parent
			, ITreeNode p_thisGroovyNode
			, ViewPart p_runViewPart
			,IProgressMonitor p_monitor
			) {
		parent = p_parent
		thisGroovyNode = p_thisGroovyNode
		runViewPart = p_runViewPart
		monitor = p_monitor
		
		//-- включаем swt построитель в родительский композит
		swt.setCurrent(parent);
	}

	
	Example1() {
		
	}
	
	
	public static void main(String[] args) {
		Example1 form = new Example1()
		
		form.run()
		
		form.shell.getShell().open()
		
		while(! form.shell.getShell().isDisposed()) { 
			if (! form.shell.getShell().display.readAndDispatch()) {
				form.shell.getShell().display.sleep();
			}
		}
	}

	
	// Процедура для запуска формы
	public void run() {
		try {
			shell = swt.composite {	
				//-- задаем тип планировщика
			    gridLayout(numColumns:1)         //-- колонок 1 
				gridData( style:"fill_both" )    //-- растягивать во все стороны
				//-- задаем надпись
				label( style:"none", text:"Задача: " )
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}
