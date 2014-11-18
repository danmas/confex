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

import com.hexapixel.widgets.calendarcombo.CalendarCombo
import com.hexapixel.widgets.ganttchart.ISettings
import com.hexapixel.widgets.calendarcombo.IColorManager

class calendar_combo { 

	def parent
	def thisGroovyNode
	def runViewPart

	def swt = new SwtBuilder() 
	def comp0
	def shell
	
	public void run() {
		try {
			Utils.thisGroovyNode = thisGroovyNode
	
			swt.setCurrent(parent);

			comp0 = swt.composite {
				gridData( style:"fill_horizontal" )
				gridLayout(numColumns:1)

				CalendarCombo cCombo = new CalendarCombo(parent, SWT.READ_ONLY)
//				CalendarCombo startCombo = new CalendarCombo(parent, SWT.READ_ONLY);
//				CalendarCombo endCombo = new CalendarCombo(parent, SWT.READ_ONLY, "", true, startCombo);

				button( style:"push",text:"Œ ", background:[0, 255, 255] )  {
					onEvent(type:"Selection", closure:{
						println cCombo.getDateAsString()  

					})
				}

			}
		} catch(Exception e) {System.err.println e.getMessage()}

    } //-- run()
   
} //-- class