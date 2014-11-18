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


// \groovy\easy_form.groovy


FormTest form = new FormTest(parent,thisGroovyNode,runViewPart);
form.run();


class FormTest {

	def Composite parent
	def ITreeNode thisGroovyNode
	def ViewPart  runViewPart
	def swt = new SwtBuilder() 

	FormTest(Composite p_parent, ITreeNode p_thisGroovyNode, ViewPart p_runViewPart) {
		parent = p_parent
		thisGroovyNode = p_thisGroovyNode
		runViewPart = p_runViewPart
		println "Простая форма с вкладками"
	}
	
	public static void main(String[] args) {
	}
	
	public void run() {
		swt.setCurrent(parent);
		
		def compTraceLog0 = swt.composite {	
      	gridLayout(numColumns:1)
		//gridData( style:"fill_horizontal" )
		gridData( style:"fill_both" )
		def comp0 = composite {
			gridData( style:"fill_both" )
	      	gridLayout(numColumns:1) 
			def compTraceLog1 = composite {
			gridData( style:"fill_both" )
	      	gridLayout(numColumns:1)
	      	
	 		def tab = tabFolder( style:"none" ) {
				gridData( style:"fill_horizontal" )
				tabItem( style:"none", text:"Вкладка 1" ) {
					 composite {
						gridData( style:"fill_both" )
			         	gridLayout(numColumns:3) 
 			        	label( style:"none", text:"Поле 11" )
			        	def v_testVar11 = text( style:"border" ) {  
				        	gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
			        	}
						text( editable:false, enabled:true, style:"none", text:"произвольный текст 11" )
			        	
			        	label( style:"none", text:"Поле 12" )
			        	def v_testVar12 = text( style:"border" ) {  
				        	gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
			        	}
						text( editable:false, enabled:true, style:"none", text:"произвольный текст 12" )
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
			        	label( style:"none", text:"Поле 21" )
			        	def v_testVar21 = text( style:"border" ) {  
				        	gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
			        	}
						text( editable:false, enabled:true, style:"none", text:"произвольный текст 21" )
			        	
			        	label( style:"none", text:"Поле 22" )
			        	def v_testVar22 = text( style:"border" ) {  
				        	gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true )
			        	}
						text( editable:false, enabled:true, style:"none", text:"произвольный текст 22" )		        	
						button( style:"push",text:"Кнопка 21", background:[0, 255, 255] )  {
							onEvent(type:"Selection", closure:{
								println "Вы нажали на кнопку 21"
								println "Значение поля 21="+v_testVar21.text
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