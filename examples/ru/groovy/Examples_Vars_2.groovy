/**
*   Examples_Vars_2.groovy
*   
*   ������������� �������-���������� 
*	������ ����������
*
*   �����: ����� �������, 2007 



� ���� ������� �� ������� ������ ����������������
���������� "var 1"

thisGroovyNode.setVarUp("var 1",t.text)

*/


import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode


/**
*    ����������� ������
*/
class Examples_Vars_2 {
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
			
			shell = swt.composite {	
				//-- ������ ��� ������������
			    gridLayout(numColumns:3)         //-- ������� 3 
				gridData( style:"fill_both" )    //-- ����������� �� ��� �������
				//-- ������ �������
				label( style:"none", text:"������� ����� �������� ���������� var 1: " )
				def t = text( style:"border",text:'#{var 1}')
				button(text:"���������") {
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
