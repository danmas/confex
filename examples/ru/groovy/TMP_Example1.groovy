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


//-- � ����� ����� ���������� ���������� ������� groovy

//-- ��������� �������� ����� � ������������ �� �������� �����������
//Example1 form = new Example1(parent,thisGroovyNode,runViewPart,monitor);
//-- ����������� �� ���������� ����� run() 
//form.run();


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
	
	//-- ������� �������� ����� 
	def shell
	
	
	// ����������� � ������� ������������ ����������
	Example1(Composite p_parent
			, ITreeNode p_thisGroovyNode
			, ViewPart p_runViewPart
			,IProgressMonitor p_monitor
			) {
		parent = p_parent
		thisGroovyNode = p_thisGroovyNode
		runViewPart = p_runViewPart
		monitor = p_monitor
		
		//-- �������� swt ����������� � ������������ ��������
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

	
	// ��������� ��� ������� �����
	public void run() {
		try {
			shell = swt.composite {	
				//-- ������ ��� ������������
			    gridLayout(numColumns:1)         //-- ������� 1 
				gridData( style:"fill_both" )    //-- ����������� �� ��� �������
				//-- ������ �������
				label( style:"none", text:"������: " )
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	} //-- run()		
	
}
