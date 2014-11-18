/**
*   ������������ �����������    

*   �����: ����� �������, 2007 
*/  
import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import net.confex.tree.ITreeNode
import org.eclipse.ui.IActionBars;

 
 
/**
*    ����������� ������
*/
class DynamicActionBarExample {
	//-- ���������� ��� ����� ����� ������� � ���������� �������
	//   � ������ ����������
	def  parent         //-- ������������ ��������
	//def  thisGroovyNode //-- ���� ���� � ������
	def  runViewPart    //-- ��� � ������� ����������� �����
	//def  monitor 		//-- ������� ���������� 

	//-- ������� ����������� SWT 	
	def swt = new SwtBuilder() 
	
	// ��������� ��� ������� �����
	public void run() {
		//new GC2().run()
		
		try { 
			//-- �������� swt ����������� � ������������ ��������
			swt.setCurrent(parent);
			 
			def action = new ActionMy(); 

			swt.composite {	
				//-- ������ ��� ������������
			    gridLayout(numColumns:2)         //-- ������� 2 
				gridData( style:"fill_both" )    //-- ����������� �� ��� �������
				//-- ������ �������
				label( style:"none", text:"������ ������������� ����������� ������ � ������ ����. " )
				button(text:'Push me') {
					onEvent(type:'Selection',closure:{
						try {
						IActionBars bars = runViewPart.getViewSite().getActionBars()
						bars.getToolBarManager().add(action)
						bars.getToolBarManager().update(true);
						} catch( Exception e) {
							System.err.println(" "+e.getMessage())
						}
					})
				}
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}