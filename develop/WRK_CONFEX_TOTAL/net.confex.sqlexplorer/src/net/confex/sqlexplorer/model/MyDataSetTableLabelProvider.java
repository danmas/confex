package net.confex.sqlexplorer.model;

import net.sourceforge.sqlexplorer.dataset.DataSetRow;
import net.sourceforge.sqlexplorer.dataset.DataSetTableLabelProvider;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Table;

public class MyDataSetTableLabelProvider extends DataSetTableLabelProvider
	implements IColorProvider {
	
	private TableViewer tableViewer;
	
	//-- можно установить внешний IColorProvider
	IColorProvider colorProvider=null;
	
	public void setColorProvider(IColorProvider colorProvider) {
		this.colorProvider = colorProvider;
	}

	
	MyDataSetTableLabelProvider(TableViewer tableViewer) {
		super();
		this.tableViewer = tableViewer;
	}

	
	public Color getBackground(Object element) {
		if(colorProvider!=null)
			return colorProvider.getBackground(element);
		return null;
		//System.err.println("colorProvider==null");
		//return 
		/*
			Table t = this.tableViewer.getTable();
			t.indexOf(element);
			
			int index = list.indexOf(element);
			if ((index % 2) == 0) {
				return new Color(null,220,220,255);
			} else {
				return null;
			}
		*/	
		/*
		try {
			DataSetRow row = (DataSetRow)element;
			row.
			if(row.getObjectValue(4)instanceof Double) {
				Double level = (Double)row.getObjectValue(4);
				if(level.intValue() >1 && level.intValue() < 25)
					return new Color(null,255,220,220);
				else if(level.intValue()==45 )
					return new Color(null,220,255,220);
				else if(level.intValue()== 1 )
					return new Color(null,240,240,255);
				else 
				return null;
			}
			return null;
		} catch (Exception e){
			return null;
		}*/
	}

	
	public Color getForeground(Object element) {
		if(colorProvider!=null)
			return colorProvider.getForeground(element);
		// TODO Auto-generated method stub
		return null;
	}


	
	
	
}
