/**
*   EmbededGraph.groovy
*   
*   ������ ���������� �������
*
*   �����: ����� �������, 2007 

	������� � ������� ������� �� ������ ����������
#{GRAPH_TITLE}", "#{X_TITLE}", "#{Y_TITLE}"


	������ ������� �������� �� ������-����� � ������ 
value - �������� �� Y
mi    - ������
hour  - ���
day   - ����
month - �����
year  - ���

	����� �������� ����� ������ �� ������ �����:  

1. ������� �������-���� tableXX
2. � ����� ���������� �������� ���������� categoryXX,

3. � ��������� makeGraph() �������� ������
...
final TimeSeries seriesXX = createDataset("#{categoryXX}","tableXX");
dataset.addSeries(seriesXX)
...

*/ 
import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder

import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor

import net.confex.tree.ITreeNode

import net.confex.tree.ICompositeProvider

import groovy.sql.Sql

import org.jfree.chart.ChartFactory
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.plot.PlotOrientation as Orientation
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart;

import org.jfree.chart.ChartFactory
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.plot.PlotOrientation as Orientation
import org.jfree.data.xy.XYDataset
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import java.text.SimpleDateFormat;
import org.jfree.data.time.TimeSeriesCollection
import org.jfree.data.time.TimeSeries

import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WC
import javax.swing.JPanel
import java.awt.BorderLayout
import groovy.swt.CustomSwingBuilder

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

import org.jfree.ui.RectangleInsets; 
import java.awt.Color; 

//import org.jCharts.encoders.PNGEncoder
import org.jfree.chart.ChartUtilities
import java.io.*


//-- � ����� ����� ���������� ���������� ������� groovy

//-- ��������� �������� ����� � ������������ �� �������� �����������
//EmbededGraph form = new EmbededGraph(parent,thisGroovyNode,runViewPart,monitor);
//-- ����������� �� ���������� ����� run() 
//form.run();


/**
*    ����������� ������
*/
class EmbededGraph {
	//-- ���������� ��� ����� ����� ������� � ���������� �������
	//   � ������ ����������
	def Composite parent         //-- ������������ ��������
	def ITreeNode thisGroovyNode //-- ���� ���� � ������
	def ViewPart  runViewPart    //-- ��� � ������� ����������� �����
	def IProgressMonitor monitor //-- ������� ���������� 

	
	//def embeded_table   //-- 1) ������� ���������� ��� ���������� ������� 
	def comp_for_graph  //--    ������� ���������� ��� �������� ���������� ������
	def jpanel_for_graph
	
	def m_sql

	Sql getSql() {
		if(m_sql==null) {
			m_sql = Sql.newInstance("jdbc:oracle:thin:@#{server_db}:1521:#{sid}", "#{owner}", "#{owner_pwd}", "oracle.jdbc.driver.OracleDriver")
			println "��������� ��������� "+"jdbc:oracle:thin:@#{server_db}:1521:#{sid}"+",#{owner}"+",oracle.jdbc.driver.OracleDriver"
		}
		return m_sql
	}
	
	//-- ������� ����������� SWT 	
	def swt = new SwtBuilder() 
	def swing = new CustomSwingBuilder()
	
	
	EmbededGraph() {
	}
	
	
	// ����������� � ������� ������������ ����������
	EmbededGraph(Composite p_parent
			, ITreeNode p_thisGroovyNode
			, ViewPart p_runViewPart
			,IProgressMonitor p_monitor
			) {
		parent = p_parent
		thisGroovyNode = p_thisGroovyNode
		runViewPart = p_runViewPart
		monitor = p_monitor
	}

	
	public static void main(String[] args) {
	}

	
	// ��������� ��� ������� �����
	public void run() {
		try {
			//-- �������� swt ����������� � ������������ ��������
			swt.setCurrent(parent);
			
			swt.composite {	
				//-- ������ ��� ������������
			    gridLayout(numColumns:1)          
				gridData( style:"fill_both" )    //-- ����������� �� ��� �������
				//-- ������� �������� ���������� �������
				comp_for_graph = composite(style:"border,embedded") {
					gridData( style:"fill_both" )
		         	gridLayout(numColumns:1) 
		         	swing.current = awtFrame()
		         	swing.current.layout = new BorderLayout()
	        		jpanel_for_graph = swing.panel(id:'canvas') //{ swing.rigidArea(width:400, height:400) }
	        		jpanel_for_graph.layout = new BorderLayout()
				}
			} //-- swt.composite
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
		makeGraph(true,"#{report_path}test.png")
	} //-- run()		


	/*
	*  ������ ����� ��������� ������
	*/
	TimeSeries createDataset(p_label,p_table) {
		//-- ������� ���������� �������     		
		def embeded_table = thisGroovyNode.getChildWithName("$p_table");
		if(embeded_table==null)
			System.err.println("�� ���� ����� ���������� ������� '$p_table' ")
		TimeSeries series = new TimeSeries("$p_label", Minute.class);
		try {
			//-- ����� ������ �� ���������� �������
			def sql_req = embeded_table.getFullText()
			//println "sql= "+sql_req
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
			//println "end"
		} catch(Exception e) {
			System.err.println(e.getMessage())
		}
	    return series;
	}
	
	
	/*
	*   ������ ������
	*/
	void makeGraph(p_flag_show,p_file_name) {
	    TimeSeriesCollection dataset = new TimeSeriesCollection();
	    final TimeSeries series1 = createDataset("#{category1}","table1");
	    dataset.addSeries(series1)
	    final TimeSeries series2 = createDataset("#{category2}","table2");
	    dataset.addSeries(series2)
	    
	    def JFreeChart chart = ChartFactory.createTimeSeriesChart("#{GRAPH_TITLE}",
	    		"#{X_TITLE}", "#{Y_TITLE}",
	            dataset, 
	            true,
	            true,
	            true);
	    
		def ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

		if(p_flag_show) {
			if(jpanel_for_graph==null)
				System.err.println "jpanel_for_graph==null!"
			jpanel_for_graph.add chartPanel
		}
		
        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
        }
		
        if(p_file_name!=null) {
        	ChartUtilities.writeChartAsPNG( new FileOutputStream(p_file_name),
                    chart, 400, 400);
        }
	}

	
	
}