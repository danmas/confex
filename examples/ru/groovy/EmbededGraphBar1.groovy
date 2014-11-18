import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor

import net.confex.tree.ITreeNode

import net.confex.tree.ICompositeProvider

import groovy.sql.Sql
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.text.NumberFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.Layer;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WC
import javax.swing.JPanel
import java.awt.BorderLayout
import groovy.swt.CustomSwingBuilder

//-- — этого места начинаетс€ исполнение скрипта groovy

//-- —оздаетс€ тестовый класс и св€зывваетс€ со внешними переменными
//EmbededGraphBar1 form = new EmbededGraphBar1(parent,thisGroovyNode,runViewPart,monitor);
//-- запускаетс€ на выполнение метод run() 
//form.run();


/**
*    ќпределение класса
*/
class EmbededGraphBar1 {
	//-- переменные дл€ св€зи груви скрипта с конфексным деревом
	//   и средой выполнени€
	def Composite parent         //-- родительский композит
	def ITreeNode thisGroovyNode //-- этот узел в дереве
	def ViewPart  runViewPart    //-- ¬ид в котором выполн€етс€ форма
	def IProgressMonitor monitor //-- ћонитор выполнени€ 

	def comp_for_graph  //--    создаем переменную под композит содержащий график
	def jpanel_for_graph
	
	def m_sql

	Sql getSql() {
		if(m_sql==null) {
			m_sql = Sql.newInstance("jdbc:oracle:thin:@62.105.129.249:1521:MODEL1", "IUS", "IUS", "oracle.jdbc.driver.OracleDriver")
			println "ќткрываем коннекцию "+"jdbc:oracle:thin:@62.105.129.249:1521:MODEL1"+",IUS"+",oracle.jdbc.driver.OracleDriver"
		}
		return m_sql
	}
	
	//-- создаем построитель SWT 	
	def swt = new SwtBuilder() 
	def swing = new CustomSwingBuilder()
	
	EmbededGraphBar1() {}
	
	/*
	//  онструктор в котором инициируютс€ переменные
	EmbededGraphBar1(Composite p_parent
			, ITreeNode p_thisGroovyNode
			, ViewPart p_runViewPart
			,IProgressMonitor p_monitor
			) {
		parent = p_parent
		thisGroovyNode = p_thisGroovyNode
		runViewPart = p_runViewPart
		monitor = p_monitor

		try {
			//-- включаем swt построитель в родительский композит
			swt.setCurrent(parent);
			
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	}*/

	
	public static void main(String[] args) {
	}

	
	// ѕроцедура дл€ запуска формы
	public void run() {
		try {
			//-- включаем swt построитель в родительский композит
			swt.setCurrent(parent);
			
			swt.composite {	
				//-- задаем тип планировщика
			    gridLayout(numColumns:1)          
				gridData( style:"fill_both" )    //-- раст€гивать во все стороны
				//-- создаем композит содержащий таблицу
				comp_for_graph = composite(style:"border,embedded") {
					gridData( style:"fill_both" )
		         	gridLayout(numColumns:1) 
		         	swing.current = awtFrame()
		         	swing.current.layout = new BorderLayout()
	        		jpanel_for_graph = swing.panel(id:'canvas') //{ swing.rigidArea(width:400, height:400) }
	        		jpanel_for_graph.layout = new BorderLayout()
				}
			} //-- swt.composite
			makeGraph()
		} catch( Exception e) {
			System.err.println(" -run()- "+e.getMessage())
		}
	} //-- run()
/*
	*  —троим серию временных данных
	*/
/*
	TimeSeries createSeries(p_label,p_table) {
		//-- находим встроенную таблицу     		
		def embeded_table = thisGroovyNode.getChildWithName("$p_table");
		if(embeded_table==null)
			System.err.println("Ќе могу найти встроенную таблицу '$p_table' ")
		//println embeded_table.getFullText() //-- печать текста запроса
		
		TimeSeries series = new TimeSeries("$p_label", Minute.class);
	
		try {
			//-- берем запрос из встроенной таблицы
			def sql_req = embeded_table.getFullText()
			getSql().eachRow( sql_req as String  ) {
		    // For each row output detail 
		    row -> 
		    	//println row.mt.toString() + " " + row.value;
		    	int m = Integer.valueOf(row.mi)*1
		    	int h = Integer.valueOf(row.hour)*1
		    	int d = Integer.valueOf(row.day)*1
		    	int mo = Integer.valueOf(row.month)*1
		    	int ye = Integer.valueOf(row.year)*1
		        series.add(new Minute(m, h, d, mo, ye), row.value);
			}
		} catch(Exception e) {
			System.err.println(e.getMessage())
		}
	    return series;
	}
*/

    /**
     * Creates a sample dataset.
     *
     * @return A sample dataset.
     */
     /*
     DefaultCategoryDataset creDataSet(p_series) {
		DefaultCategoryDataset dataset
		try {
	        dataset = new DefaultCategoryDataset();
	        dataset.addValue(0.77, "$p_series", "Robert");   
	        dataset.addValue(0.93, "$p_series", "Mary");   
	        dataset.addValue(0.59, "$p_series", "John");   
	        dataset.addValue(0.75, "$p_series", "Ellen");   
	        dataset.addValue(0.63, "$p_series", "Jack");   
	        dataset.addValue(0.95, "$p_series", "David");   
	        dataset.addValue(0.71, "$p_series", "Mark");   
	        dataset.addValue(0.65, "$p_series", "Andy");   

	        dataset.addValue(0.57, "1 p_series", "Robert");   
	        dataset.addValue(0.23, "1 p_series", "Mary");   
	        dataset.addValue(0.49, "1 p_series", "John");   
		
		} catch( Exception e) {
			System.err.println(" -createDataset()- "+e.getMessage())
			return null
		}
        return dataset;
    }
*/


	DefaultCategoryDataset creDataSet(p_series,p_table) {
	//-- находим встроенную таблицу     		
	def embeded_table = thisGroovyNode.getChildWithName("$p_table");
	if(embeded_table==null)
		System.err.println("Ќе могу найти встроенную таблицу '$p_table' ")
	
		DefaultCategoryDataset dataset
		try {
	        dataset = new DefaultCategoryDataset();
			//-- берем запрос из встроенной таблицы
			def sql_req = embeded_table.getFullText()
			getSql().eachRow( sql_req as String  ) {
		    // For each row output detail 
		    row -> 
		    	println row.q + " " + row.description;
		    	def val = row.q
		    	if(val==null)
		    		val = 0
		        dataset.addValue(val, "$p_series", row.description);   
			}
		} catch( Exception e) {
			System.err.println(" -createDataset()- "+e.getMessage())
			return null
		}
    return dataset;
}
 	

	/*
	*   —троим график
	*/
	void makeGraph() {
	try {
		DefaultCategoryDataset dataset = creDataSet("Series 1","table1")
		
		if(dataset==null) {
			System.err.println(" -makeGraph()- dataset==null")
			return
		}
		JFreeChart chart = ChartFactory.createBarChart3D(
			"#{GRAPH_TITLE}",         // chart title
			"#{X_TITLE}",             // domain axis label
			"#{Y_TITLE}",             // range axis label
            dataset,                  // data
            PlotOrientation.HORIZONTAL, // orientation
            false,                    // include legend
            true,                     // tooltips
            false                     // urls
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer3D renderer = new BarRenderer3D();
        plot.setRenderer(renderer);
        renderer.setItemLabelsVisible(true);
        renderer.setMaximumBarWidth(0.05);
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(NumberFormat.getNumberInstance()) //getPercentInstance());
        
		def ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		jpanel_for_graph.add chartPanel

	} catch( Exception e) {
		System.err.println(" -makeGraph()- "+e.getMessage())
	}
        
	}
}