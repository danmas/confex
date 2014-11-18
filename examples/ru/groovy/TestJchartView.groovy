import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.plot.PlotOrientation as Orientation
import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WC
import java.awt.BorderLayout
import groovy.swt.CustomSwingBuilder
import groovy.swt.SwtBuilder


class TestJchartView {
	
	def parent
	
	//-- создаем построитель SWT 	
	def swt = new SwtBuilder() 
	def swing = new CustomSwingBuilder()
	
	
	void run() {
		def jpanel_for_graph 
		
		swt.setCurrent(parent);
		
		swt.composite {	
			//-- задаем тип планировщика
		    gridLayout(numColumns:1)          
			gridData( style:"fill_both" )    //-- растягивать во все стороны
			//-- создаем композит содержащий таблицу
			def comp_for_graph = composite(style:"border,embedded") {
				gridData( style:"fill_both" )
	         	gridLayout(numColumns:1) 
	         	swing.current = awtFrame()
	         	swing.current.layout = new BorderLayout()
	    		jpanel_for_graph = swing.panel(id:'canvas') //{ swing.rigidArea(width:400, height:400) }
	    		jpanel_for_graph.layout = new BorderLayout()
			}
		} //-- swt.composite
		
		makeGraph(jpanel_for_graph)
	}

	
	void makeGraph(jpanel_for_graph) {
		def dataset = new DefaultCategoryDataset()
		dataset.addValue 150, "no.1", "Jan"
		dataset.addValue 210, "no.1", "Feb"
		dataset.addValue 390, "no.1", "Mar"
		dataset.addValue 300, "no.2", "Jan"
		dataset.addValue 400, "no.2", "Feb"
		dataset.addValue 200, "no.2", "Mar"

		def labels = ["Bugs", "Month", "Count"]
		def options = [true, true, true]
		def chart = ChartFactory.createLineChart(*labels, dataset,
		                Orientation.VERTICAL, *options)
		
		def chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		
		jpanel_for_graph.add chartPanel
	}
	
}