/**
*   ExampleEmbedTable.groovy
*   
*   ������ ���������� �������
*
*   �����: ����� �������, 2007 
*/
import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
import net.confex.tree.ITreeNode

import net.confex.tree.ICompositeProvider


/**
*    ����������� ������
*/
class ExampleEmbedTable {
	//-- ���������� ��� ����� ����� ������� � ���������� �������
	//   � ������ ����������
	def Composite parent         //-- ������������ ��������
	def ITreeNode thisGroovyNode //-- ���� ���� � ������
	def ViewPart  runViewPart    //-- ��� � ������� ����������� �����
	def IProgressMonitor monitor //-- ������� ���������� 

	
	def embeded_table   //-- 1) ������� ���������� ��� ���������� ������� 
	def comp_for_table  //--    ������� ���������� ��� �������� ���������� ������� 
	
	//-- ������� ����������� SWT 	
	def swt = new SwtBuilder() 

	
	// ��������� ��� ������� �����
	public void run() {
		try {
			//-- �������� swt ����������� � ������������ ��������
			swt.setCurrent(parent);
			
			//-- 2) ��������� ���������� � �������� �����     		
			embeded_table = thisGroovyNode.getChildWithName("table1");
			if(embeded_table==null)
				System.err.println("�� ���� ����� ���������� ������� 'table1' ")
			if(!embeded_table instanceof ICompositeProvider) 	
				System.err.println("���������� ������� �� �������� ����� ICompositeProvider")
			
			swt.composite {	
				//-- ������ ��� ������������
			    gridLayout(numColumns:1)         //-- ������� 2 
				gridData( style:"fill_both" )    //-- ����������� �� ��� �������
				//-- ������� �������� ���������� �������
				comp_for_table = composite {
							gridData( style:"fill_both" )
				         	gridLayout(numColumns:1) 
				}
			} //-- swt.composite
			
			//-- ���������� ������� � �� �������� 
			((ICompositeProvider)embeded_table).makeComposite(comp_for_table,runViewPart,monitor)
			//-- ������������� �������� ��� ������� 
        	comp_for_table.layout()
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}