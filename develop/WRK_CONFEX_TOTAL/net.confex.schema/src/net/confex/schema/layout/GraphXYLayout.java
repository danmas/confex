/*
 * Created on Jul 21, 2004
 */
package net.confex.schema.layout;

import net.confex.schema.part.SchemaDiagramPart;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;



/**
 * Subclass of XYLayout which can use the child figures actual bounds as a constraint
 * when doing manual layout (XYLayout)
 * @author Eremeev Roman
 */
public class GraphXYLayout extends FreeformLayout {

	//private SchemaDiagramPart diagram;
	
	public GraphXYLayout(/*SchemaDiagramPart diagram*/)	{
		super();
		//this.diagram = diagram;
	}
	
	
	public void layout(IFigure container) {
		//System.out.println(" -- GraphXYLayout.layout ");
		super.layout(container);
		//diagram.setElementModelBounds();
	}
	

	public Object getConstraint(IFigure child) 	{
		Object constraint = constraints.get(child);
		if (constraint != null || constraint instanceof Rectangle)	{
			return (Rectangle)constraint;
		} else {
			Rectangle currentBounds = child.getBounds();
			return new Rectangle(currentBounds.x, currentBounds.y, currentBounds.width, currentBounds.height);
			//-- было так 
			//return new Rectangle(currentBounds.x, currentBounds.y, -1, -1);
			// это приводило к тоу что выполняется getPrefferedSize и 
			// фигура выводится с минимальной шириной и высотой 
		}
	}
	
}
