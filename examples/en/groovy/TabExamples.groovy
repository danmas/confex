import groovy.swt.guibuilder.ApplicationGuiBuilder;
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.widgets.Composite

import groovy.swt.SwtBuilder
import org.eclipse.swt.SWT

import net.confex.tree.ITreeNode
import net.confex.tree.ICompositeProvider
import org.eclipse.ui.part.ViewPart


class TabExamples {
	def Composite parent
	def ITreeNode thisGroovyNode
	def ViewPart  runViewPart
	def swt = new SwtBuilder()
	
	
	public void run() {
		swt.setCurrent(parent);

		swt.composite {
         	gridLayout()
         	label()
			label(text:"Простые закладки tabFolder,tabItem")	
			tabFolder( style:"none" ) {
				gridData( style:"fill_both" )
				tabItem( style:"none", text:"Item4" ) {
					text( style:"border, multi", text:"Content for Item4" ) 
				}
				tabItem( style:"none", text:"Item5" ) {
					text( style:"border, multi", text:"Content for Item5" ) 
				}
				tabItem( style:"none", text:"Item6" ) {
					text( style:"border, multi", text:"Content for Item6" ) 
				}
			}
         	label()
			label(text:"Сложные (custom) закладки cTabFolder, cTabItem")	
			cTabFolder( style:"flat" ) {
				gridData( style:"fill_both" )
				cTabItem( style:"none", text:"Item1" ) {
					text( style:"border, multi", text:"Content for Item1" ) 
				}
				cTabItem( style:"none", text:"Item2" ) {
					text( style:"border, multi", text:"Content for Item2" ) 
				}
				cTabItem( style:"none", text:"Item3" ) {
					text( style:"border, multi", text:"Content for Item3" ) 
				}
			}
         	label()
			label(text:"Сложные (custom) цветные закладки cTabFolder, cTabItem")	
			def сtab = cTabFolder( style:"flat" ) {
				gridData( style:"fill_both" )
				cTabItem( style:"none", text:"Item1" ) {
					text( style:"border, multi", text:"Content for Item1" ) 
				}
				cTabItem( style:"none", text:"Item2" ) {
					text( style:"border, multi", text:"Content for Item2" ) 
				}
				cTabItem( style:"none", text:"Item3" ) {
					text( style:"border, multi", text:"Content for Item3" ) 
				}
			}
			сtab.simple = false
			сtab.borderVisible = true
			
			Display display = Display.getCurrent(); 
			int colorCount = 3; 
			def Color[] colors = new Color[colorCount]; 
			colors = new Color[colorCount]; 
			colors[0] = new Color(Display.getCurrent(), 200, 200, 255);
			//display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND); 
			colors[1] = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT); //new Color(Display.getCurrent(), 200, 0, 0);
			//display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT); 
			colors[2] = colors[0]; 
			int[] percents = new int[colorCount - 1]; 
			percents[0] = 4; 
			percents[1] = 60; 
			сtab.setSelectionBackground(colors, percents, true); 
			сtab.setSelectionForeground(display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
		}	 
	} //-- run()
}