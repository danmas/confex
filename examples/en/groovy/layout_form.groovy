/**
*   layout_form.groovy
*   
*   ������� ������ ������� ��������� Layout  
*
*   �����: ������ �����, 2007 
*
*	� ���� ������� �������� ������� Layout � ���������
*	� �� ���� ���������� ��������� ��������
**/

import groovy.jface.JFaceBuilder
import groovy.swt.SwtBuilder
import groovy.swt.CustomSwingBuilder
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.*

import net.confex.tree.ITreeNode
import net.confex.tree.ICompositeProvider
import org.eclipse.ui.part.ViewPart

import org.eclipse.swt.widgets.Text

// \groovy\layout_form.groovy

//-- � ����� ����� ���������� ���������� ������� groovy
//-- ��������� �������� ����� � ������������ �� �������� �����������
LayoutFormTest form = new LayoutFormTest(parent,thisGroovyNode,runViewPart);
//-- ����������� �� ���������� ����� run() 
form.run();

/**
*    ����������� ������
*/
class LayoutFormTest {

//-- ���������� ��� ����� ����� ������� � ���������� �������
//   � ������ ����������
	def Composite parent         //-- ������������ ��������
	def ITreeNode thisGroovyNode //-- ���� ���� � ������
	def ViewPart  runViewPart    //-- ��� � ������� ����������� �����

//-- ������� ����������� SWT 	
	def swt = new SwtBuilder() 

	def v_testVar11
	def v_testVar12 

	def Composite comp0
	def Composite comp1
	def Composite comp2
	def Composite compTraceLog0

	def combo_values = []
    def textcomp               	
// ����������� � ������� ������������ ����������
	LayoutFormTest(Composite p_parent, ITreeNode p_thisGroovyNode, ViewPart p_runViewPart) {
		parent = p_parent
		thisGroovyNode = p_thisGroovyNode
		runViewPart = p_runViewPart
		println "Layout test form"
	}
	
	public static void main(String[] args) {
	}
	
	public void run() {
		def tree1
		
		try{
		swt.setCurrent(parent);
		compTraceLog0 = swt.composite {
		gridData( style:"fill_both" )
		gridLayout(numColumns:1)
	
		label( style:"none", text:"--------------------------- ������� �����  ---------------------------" )
			
		comp0 = composite {
			gridData( style:"fill_both" )
			gridLayout(numColumns:3)
			comp1 = composite {
				gridData( style:"fill_both" )
				gridLayout(numColumns:1) 
			    label( style:"none", text:"������� 1 - Tree" ,foreground:[250,250,250],background:[250,0,0])
			    
			    tree1 = tree( 	toolTipText:"This is a tree!",
			    		style:"check,border,multi",
			    		foreground:[100,100,100],
			    		background:[0,150,250]
			    		)
			    {
					gridData( style:"fill_both"
							,heightHint:300, widthHint:300 )
					treeItem( text:"AAA", style:"none" ) {
						treeItem( text:"1A/A" ){
							treeItem( text:"1A/A/A" )
							treeItem( text:"1A/A/B" )}
						treeItem( text:"2A/B" )
						treeItem( text:"3A/C" )
					}
					def t = treeItem( text:"BBB" ) {
						def t1 = treeItem( text:"4B/A" )
						t1.setText("sdfgsdfgsd")
						t1 = treeItem( text:"5B/B" )
						t1.setText("5B/B---")
						t1 = treeItem( text:"6B/C" )
						t1.setText("6B/C---")
					}
					t.setText("BaBuBy")
				}
	      	} //-- comp1
			comp2 = composite {
				gridData( style:"fill_horizontal" )
	    		label( style:"none", text:"" )
	    		label( style:"none", text:"������� 2" )
	    		label( style:"none", text:"" )
				gridLayout(numColumns:3) 
	
				label( style:"none", text:"���� 11" )
				combo_values = ['�������� 1','�������� 2','�������� 3']
				v_testVar11 = combo(text:"----", items:combo_values ) {
					onEvent(type:"Selection", closure:{
		    		try {
		    			v_testVar12.text = "� ���� 11 ������� " + v_testVar11.text
		        		} catch(Exception e) {
						System.err.println e.getMessage()
						}
		        	});         		
				}
				label( style:"none", text:"������������ ����� 11" )
				
				label( style:"none", text:"���� 12")
				v_testVar12 = text( editable:false,style:"border" ) {  
						gridData( style:"fill_horizontal",
								grabExcessHorizontalSpace:true,heightHint:"300")
				}
				label( style:"none", text:"������������ ����� 12" )

				label( style:"none", text:"" )
				button( style:"push",text:"������ 11" )  {
				onEvent(type:"Selection", closure:{
							println "�� ������ �� ������ 11"
							println "�������� ���� 11="+v_testVar11.text
							println "�������� ���� 12="+v_testVar12.text
					})  
				}
			} //-- comp2 
	
			gridData( style:"fill_horizontal" )
			label( style:"none", text:"������� 3" )
			}//--comp0
		label( style:"none", text:"--------------------------- ������ �����  ----------------------------" )
		} //-- compTraceLog0
		tree1.layout()
		} catch(Exception e) {
			System.err.println e.getMessage()
		}
	} //-- run()
	
} //-- class


/*			    Tree tree = new Tree(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
tree.setHeaderVisible(true);
TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
column1.setText("Column 1");
column1.setWidth(200);
TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
column2.setText("Column 2");
column2.setWidth(200);
TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
column3.setText("Column 3");
column3.setWidth(200);

for (int i = 0; i < 4; i++) {
  TreeItem item = new TreeItem(tree, SWT.NONE);
  item.setText(new String[] { "item " + i, "abc", "defghi" });
  for (int j = 0; j < 4; j++) {
    TreeItem subItem = new TreeItem(item, SWT.NONE);
    subItem
        .setText(new String[] { "subitem " + j, "jklmnop",
            "qrs" });
    for (int k = 0; k < 4; k++) {
      TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
      subsubItem.setText(new String[] { "subsubitem " + k, "tuv",
          "wxyz" });
    }
  }*/

  

/*
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class CreateTreeWithcolumns {
  public static void main(String[] args) {
    Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    Tree tree = new Tree(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    tree.setHeaderVisible(true);
    TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
    column1.setText("Column 1");
    column1.setWidth(200);
    TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
    column2.setText("Column 2");
    column2.setWidth(200);
    TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
    column3.setText("Column 3");
    column3.setWidth(200);
    for (int i = 0; i < 4; i++) {
      TreeItem item = new TreeItem(tree, SWT.NONE);
      item.setText(new String[] { "item " + i, "abc", "defghi" });
      for (int j = 0; j < 4; j++) {
        TreeItem subItem = new TreeItem(item, SWT.NONE);
        subItem
            .setText(new String[] { "subitem " + j, "jklmnop",
                "qrs" });
        for (int k = 0; k < 4; k++) {
          TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
          subsubItem.setText(new String[] { "subsubitem " + k, "tuv",
              "wxyz" });
        }
      }
    }
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }
}*/