import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseEvent;

import groovy.jface.JFaceBuilder
import groovy.swt.SwtBuilder
import groovy.swt.CustomSwingBuilder
import groovy.sql.Sql
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.*

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import net.confex.tree.ITreeNode
import net.confex.tree.ICompositeProvider
import org.eclipse.ui.part.ViewPart
import org.eclipse.swt.widgets.DateTime


class CalendarDialog { 

	def parent
	def thisGroovyNode
	def runViewPart
	def swt = new SwtBuilder() 
	def calendar
	def Composite comp_main
	def Composite comp0,comp1,comp2
	def call_date_but, ret_date_but
	def date_field
	def shell
	
	public void run() {
		try {
			Utils.thisGroovyNode = thisGroovyNode
	
			swt.setCurrent(parent);

			comp0 = swt.composite {
				gridData( style:"fill_horizontal" )
				gridLayout(numColumns:2)
	         	date_field = text( enabled:true, editable:false, style:"border",
	         			foreground:[0,0,0],background:[250,250,250]) {
 				gridData( style:"fill_horizontal", grabExcessHorizontalSpace:true, widthHint:50 )}

	         	call_date_but=button( style:"push",text:"Дата" )  {
					onEvent(type:"Selection", closure:{
						try{
							openCalendar()
						} catch(Exception e1){
							System.err.println e.getMessage()	
						}
					})  
				}
			}
		} catch(Exception e) {System.err.println e.getMessage()}

    } //-- run()

    
    public void openCalendar() {
//		println "openCalendar()"
    	try {
			swt.setCurrent(parent);  
 			shell = swt.shell ( text:'Календарь',style:"APPLICATION_MODAL"/* | SWT.NO_TRIM*/ ) {
		    	fillLayout()
				gridData( style:"fill_both" )
				gridLayout(numColumns:1)

	         	ret_date_but=button( style:"push",text:"OK" )  {
					onEvent(type:"Selection", closure:{
						try{
							println calendar.getDay()+ "-" + (calendar.getMonth()+1) + "-"+calendar.getYear()
							date_field.text = calendar.getDay()+ "-" + (calendar.getMonth()+1) + "-"+calendar.getYear()
							shell.dispose()
						} catch(Exception e1){
							System.err.println e.getMessage()	
						}
					})  
				}
			}
			calendar = new DateTime(shell, SWT.CALENDAR) 
			shell.open()
	    	shell.setSize(new Point(170,200));
		//	shell.setVisible(true);
		//	shell.setActive();
		//	shell.forceActive();

		} catch(Exception e) {System.err.println e.getMessage()}
	}    
} //-- class