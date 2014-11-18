/**
*   Example1.groovy
*   
*   ����� ����� ������� � ������� ������
*
*   �����: ����� �������, 2007 
*/
import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode


/**
*    ����������� ������
*/
class Example1 {
	//-- ���������� ��� ����� ����� ������� � ���������� �������
	//   � ������ ����������
	def Composite parent         //-- ������������ ��������
	def ITreeNode thisGroovyNode //-- ���� ���� � ������
	def ViewPart  runViewPart    //-- ��� � ������� ����������� �����
	def IProgressMonitor monitor //-- ������� ���������� 

	//-- ������� ����������� SWT 	
	def swt = new SwtBuilder() 

	
	// ��������� ��� ������� �����
	public void run() {
		try {
			//-- �������� swt ����������� � ������������ ��������
			swt.setCurrent(parent);
			swt.composite {	
				//-- ������ ��� ������������
			    gridLayout(numColumns:2)         //-- ������� 2 
				gridData( style:"fill_both" )    //-- ����������� �� ��� �������
				//-- ������ �������
				label( style:"none", text:"������ ---: " )
				text( style:"border",text:'�����')
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}
