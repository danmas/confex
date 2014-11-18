import groovy.jface.JFaceBuilder
import groovy.swt.SwtBuilder
import groovy.swt.CustomSwingBuilder
import groovy.sql.Sql
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.*

import net.confex.tree.ITreeNode
import net.confex.tree.ICompositeProvider
import org.eclipse.ui.part.ViewPart


// \groovy\improved_form.groovy


FormTest form = new FormTest(parent,thisGroovyNode,runViewPart);
form.run();


class FormTest {

	def Composite parent
	def ITreeNode thisGroovyNode
	def ViewPart  runViewPart
	def swt = new SwtBuilder() 

	def v_testVar11
	def v_testVar12 
	def v_testVar21
	def v_testVar22 

	def Composite comp_tbl
	def Composite comp0

	def Composite compTraceLog1
	def Composite compTraceLog
	def Composite compTraceLog0
	
	def tab
	def combo_values = []
	                    
	FormTest(Composite p_parent, ITreeNode p_thisGroovyNode, ViewPart p_runViewPart) {
		parent = p_parent
		thisGroovyNode = p_thisGroovyNode
		runViewPart = p_runViewPart
		println "Улучшенная форма с вкладками"
	}
	
	public static void main(String[] args) {
	}
	
	public void run() {
		swt.setCurrent(parent);
		
		compTraceLog0 = swt.composite {	
      	gridLayout(numColumns:1)
		gridData( style:"fill_both" )
		comp0 = composite {
			gridData( style:"fill_both" )
	      	gridLayout(numColumns:1) 
			compTraceLog1 = composite {
			gridData( style:"fill_both" )
	      	gridLayout(numColumns:1)
	      	
	 		tab = tabFolder( style:"none" ) {
				gridData( style:"fill_horizontal" )
				tabItem( style:"none", text:"Вкладка 1" ) {
					 composite {
						gridData( style:"fill_both" )
			         	gridLayout(numColumns:3) 

			         	label( style:"none", text:"Поле 11" )
						combo_values = ['Значение 1','Значение 2','Значение 3']
		                v_testVar11 = combo(text:"----", items:combo_values ) {
							onEvent(type:"Selection", closure:{
	        					try {
	        						v_testVar12.text = "В поле 11 выбрано " + v_testVar11.text
	        					} catch(Exception e) {
									System.err.println e.getMessage()
								}
	        				});         		
						}
						label( style:"none", text:"произвольный текст 11" )

			         	label( style:"none", text:"Поле 12")
						v_testVar12 = text( editable:false,style:"border" ) {  
							gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
						}
						label( style:"none", text:"произвольный текст 12" )
						
						button( style:"push",text:"Кнопка 11", background:[0, 255, 255] )  {
							onEvent(type:"Selection", closure:{
								println "Вы нажали на кнопку 11"
								println "Значение поля 11="+v_testVar11.text
								println "Значение поля 12="+v_testVar12.text
								})  
						}
					} //-- composite
				} //-- tabItem
				tabItem( style:"none", text:"Вкладка 2" ) {
					 composite {
						gridData( style:"fill_both" )
			         	gridLayout(numColumns:3) 
			  /*      	label( style:"none", text:"Поле 21" )
			        	v_testVar21 = text( style:"border" ) {  
				        	gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
			        	}
						text( editable:false, enabled:true, style:"none", text:"произвольный текст 21" )
			  */      	
			        	
			        	label( style:"none", text:"Переключатель 21" )
			        	v_testVar21 = button( style:"check" ) {  
				        	gridData( style:"fill_horizontal", horizontalSpan:1, grabExcessHorizontalSpace:false )
			        	} 
/*	        						if (v_testVar21.selection) { 
	        							v_testVar22.text = "Переключатель 21 включен"
	        						} else {v_testVar22.text = "Переключатель 21 выключен"}
*/
						label( style:"none", text:"" )
		        	
			        	label( style:"none", text:"Поле 22" )
			        	v_testVar22 = text( style:"border" ) {  
				        	gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
			        	}
						text( editable:false, enabled:true, style:"none", text:"произвольный текст 22" )		        	
						button( style:"push",text:"Кнопка 21", background:[0, 255, 255] )  {
							onEvent(type:"Selection", closure:{
								println "Вы нажали на кнопку 21"
								println "Значение поля 21="+v_testVar21.selection
								println "Значение поля 22="+v_testVar22.text
								})  
						}
					} //-- composite
				} //-- tabItem
				} //-- tab
			} //-- compTraceLog1
			} //-- comp0 
		}
	} //-- run()
	
} //-- class