package net.confex.schema.layout;

import net.confex.schema.figures.SimpleContainerFigure;
import net.confex.schema.part.SchemaDiagramPart;

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;


// TODO: Remove diagram
public class MyGridLayout extends GridLayout {

	//private SchemaDiagramPart diagram;

	public MyGridLayout(SchemaDiagramPart diagram) {
		//this.diagram = diagram;
		super();
	}

	
	public MyGridLayout() {
		//this.diagram = diagram;
		super();
	}

	
	/**/
	public void layout(IFigure container)	{
		GraphAnimation.recordInitialState(container);
		if (GraphAnimation.playbackState(container))
			return;
		super.layout(container);
		//diagram.setElementModelBounds();
	}/**/

	
	public Object getConstraint(IFigure child) {
		Object constraint = constraints.get(child);
		if (constraint != null && constraint instanceof GridData) {
			return (GridData) constraint;
		} else {
			//GridData gridData = new GridData(100,50);
			//gridData.horizontalAlignment = GridData.FILL;
			//gridData.grabExcessHorizontalSpace = true;
			
			//-- считаем что каждый контейнер имеет gridDate
			if(child instanceof SimpleContainerFigure) {
				return ((SimpleContainerFigure)child).getGridData();
			}
			
			//GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
			//		  		| GridData.GRAB_HORIZONTAL);
			GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
			
			gridData.widthHint=100;
			gridData.heightHint=50;
			
			// associate the figure to the GridData object
			setConstraint(child, gridData);
			
			return gridData;
			
			// -- было так
			// return new Rectangle(currentBounds.x, currentBounds.y, -1, -1);
			// это приводило к тоу что выполняется getPrefferedSize и
			// фигура выводится с минимальной шириной и высотой
		}
	}

	// GridLayout gridLayout = new GridLayout();
	// gridLayout.numColumns = 2;

}
