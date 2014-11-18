/**
*  AdmUnits.groovy
*   
*   Форма для просмотра/редактирования данных об административных единицах
*   (пример использования MVC)
*
*   Авторы: Михаил Горин, Еремеев Р. 2008
*
**/

import groovy.jface.JFaceBuilder
import org.eclipse.jface.dialogs.MessageDialog
import groovy.swt.SwtBuilder
import groovy.swt.CustomSwingBuilder
import groovy.sql.Sql
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.*
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.graphics.Point

import net.confex.tree.ITreeNode
import net.confex.tree.ICompositeProvider

import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Listener
import org.eclipse.swt.widgets.Shell

/**
*    Определение класса
*/
class AdmUnits {

//-- переменные для связи груви скрипта с конфексным деревом
//   и средой выполнения
	def parent         //-- родительский композит
	def thisGroovyNode //-- этот узел в дереве
	def runViewPart    //-- Вид в котором выполняется форма

//-- создаем построитель SWT 	
	def swt = new SwtBuilder() 
	def selected_object
	def comp0, comp1, comp2, comp3, comp_temp
	def cur_item
	def tree1
	def t0, t1, ti_null
	
	
	AdmUnits() {
		println "Административные единицы!!!!!!!!!!!!!!!!!!!"
	}

	
	public void run() {
		try{
	
			swt.setCurrent(parent);
	
	// Создаем главный композит (состоит из 2 вертикальных частей)
			comp0 = swt.composite {
				gridData( style:"fill_both" )
				gridLayout(numColumns:2) 
	
	// Создаем композит для дерева и кнопок
				comp1 = composite {
					gridData( style:"fill_both" )
					gridLayout(numColumns:1)
			    	
					label( style:"none", text:"Структура ..." )
					
					tree1 = tree(toolTipText:"Структура газопроводов",
				    		style:"border,multi,full_selection", //check,
				    		foreground:[0,0,0],
				    		background:[250,250,250]
				    		)
				 	{
						gridData( style:"fill_both", heightHint:650, widthHint:150)
// Обрабатываем события в дереве 					

// Выбор текущего элемента
// Записываем в cur_item текущий элемент дерева
// Копируем в поле справа данные текущего элемента
						onEvent(type:"Selection", closure:{
				    		try {
				    			cur_item = tree1.getFocusItem()
								comp3.dispose()
								if (cur_item.getText()=='Node 1' || cur_item.getText()=='Node 2'){
									selected_object = new ViewLevel_1(comp2, swt, cur_item.getText())
								} else {
									selected_object = new ViewLevel_2(comp2, swt, cur_item.getText())
								}
				    			comp3=selected_object.getViewLevel()
				    			comp2.layout()
				    		} catch(Exception e) {
								System.err.println e.getMessage()
							}
				        })

//При раскрытии узла подгружаем потомков
						onEvent(type:"Expand", closure:{ event ->
				    		try {
				    			println "Expand"
				    		} catch(Exception e) {
								System.err.println e.getMessage()
							}
				        })
						onEvent(type:"Collapse", closure:{ event ->
				    		try {
				    			println "Collapse"
				    		} catch(Exception e) {
								System.err.println e.getMessage()
							}
				        })
					}//-- tree1
// Заполняем дерево элементами (treeItem) и присваиваем им значения (setData)
					populateTree(tree1)
// Включаем отображение линий в дереве (при желании)					
				 	tree1.setLinesVisible(true)
				}
				comp2 = composite {
					gridData( style:"fill_both" )
					gridLayout(numColumns:1)
			    	
					label( style:"none", text:"Данные ..." )
					text( editable:false,style:"border", text:"...какие-то данные...",
	         			foreground:[0,0,0],background:[250,250,250]) {  
						gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
					}
					comp3 = composite {
						gridData( style:"fill_both" )
						gridLayout(numColumns:1)
					}
				}
			}
		} catch(Exception e) {
			System.err.println e.getMessage()
		}
	}

	public void populateTree(Tree parent) {
		try{
//			println "Начало populateTree(Tree parent)"
			t0 = new TreeItem(parent,0) 
			t0.setText("Node 1")
			ti_null = new TreeItem(t0,0)
			ti_null.setText("Node 1-1")
			
			t1 = new TreeItem(parent,0) 
			t1.setText("Node 2")
			ti_null = new TreeItem(t1,0)
			ti_null.setText("Node 2-1")
//			println "Конец populateTree(Tree parent)"
		}	catch(Exception e) {System.err.println e.getMessage()}
	} //-- populateTree()
} //-- class



class ViewLevel_1 {
	def comp
	public ViewLevel_1(Composite parent, SwtBuilder swt,String Node_text){
		println "ViewLevel_1(Composite parent, SwtBuilder swt,String Node_text)"
		try{
			comp = swt.composite(parent) { 
				gridData( style:"fill_both" )
				gridLayout(numColumns:2)
				label( style:"none", text:"Label for " + Node_text )
				text( style:"border", text:Node_text)
			}
		}	catch(Exception e) {System.err.println e.getMessage()}
	}

	public Composite getViewLevel(){
		return comp
	}
}

class ViewLevel_2 {
	def comp
	public ViewLevel_2(Composite parent, SwtBuilder swt,String Node_text){
		println "ViewLevel_2(Composite parent, SwtBuilder swt,String Node_text)"
		try{
			comp = swt.composite(parent) { 
				gridData( style:"fill_both" )
				gridLayout(numColumns:2)
				label( style:"none", text:"Label for second level" )
				text( style:"border", text:Node_text)
				button( style:"push",text:"OK" )  {
					onEvent(type:"Selection", closure:{
						try{
							println ("Нажата кнопка OK...") 
						} catch(Exception e1){
							System.err.println e.getMessage()	
						}
					})
				}
			}
		}	catch(Exception e) {System.err.println e.getMessage()}
	}

	public Composite getViewLevel(){
		return comp
	}
}