/**
*   Examples_Vars_1.groovy
*   
*   ������������� �������-���������� 
*	������ ����������
*
*   �����: ����� �������, 2007 

  � ���� ������� ���������� ������ ���������� �� ����������
  label( style:"none", text:"#{var 1}: " )
  text( style:"border",text:'#{����� 2}')

  ����� ���������� "var 1" � "����� 2" ������������ � ���� � �������
  ��� ������������ ����� �� ������� ������ ���� �� ����� �������.
  ����� �������, ������ ������������� ���� �� ������ ����������.
  
  //-- ������ ���������� �� ����� ����������
  thisGroovyNode.getStrVarUp("var1")
  
*/


import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode


/**
*    ����������� ������
*/
class Examples_Vars_1 {
	//-- ���������� ��� ����� ����� ������� � ���������� �������
	//   � ������ ����������
	def Composite parent         //-- ������������ ��������
	def ITreeNode thisGroovyNode //-- ���� ���� � ������
	def ViewPart  runViewPart    //-- ��� � ������� ����������� �����
	def IProgressMonitor monitor //-- ������� ���������� 

	//-- ������� ����������� SWT 	
	def swt = new SwtBuilder() 
	
	//-- ������� �������� ����� 
	def shell

	
	// ��������� ��� ������� �����
	public void run() {
		try {
			//-- �������� swt ����������� � ������������ ��������
			swt.setCurrent(parent);
			
			println thisGroovyNode.getStrVarUp("var 1")
			println "CONFEX_DIR = " + thisGroovyNode.getStrVarUp("CONFEX_DIR")
			
			shell = swt.composite {	
				//-- ������ ��� ������������
			    gridLayout(numColumns:2)         //-- ������� 2 
				gridData( style:"fill_both" )    //-- ����������� �� ��� �������
				//-- ������ �������
				label( style:"none", text:"#{var 1}: " )
				text( style:"border",text:'#{����� 2}')
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}
