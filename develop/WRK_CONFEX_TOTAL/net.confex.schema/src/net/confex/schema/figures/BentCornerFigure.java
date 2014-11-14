package net.confex.schema.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A figure that has a bent corner in the top right hand. Typically used for sticky notes.
 */
public abstract class BentCornerFigure extends Figure  implements IModelElementFigure {

/**
 * The default amount of pixels subtracted from the figure's height and width to determine
 * the size of the corner.
 */
protected static int DEFAULT_CORNER_SIZE = 10;

private int cornerSize;

private boolean selected;
private Label tooltip_label= new Label("");
	

public void setToolTipText(String txt) {
	tooltip_label.setText(txt);
}

/**
 * Constructs an empty BentCornerFigure with default background color of 
 * ColorConstants.tooltipBackground and default corner size.
 */
public BentCornerFigure() {
	setBackgroundColor(ColorConstants.tooltipBackground);
	setForegroundColor(ColorConstants.tooltipForeground);
	setCornerSize(DEFAULT_CORNER_SIZE);
}

/**
 * Returns the size, in pixels, that the figure should use to draw its bent corner.
 * 
 * @return size of the corner
 */
public int getCornerSize() {
	return cornerSize;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
public void paintFigure(Graphics graphics) {
	Rectangle rect = getBounds().getCopy();

	graphics.translate(getLocation());

	// fill the note
	PointList outline = new PointList();
	
	outline.addPoint(0, 0);
	outline.addPoint(rect.width - cornerSize, 0);
	outline.addPoint(rect.width - 1, cornerSize);
	outline.addPoint(rect.width - 1, rect.height - 1);
	outline.addPoint(0, rect.height - 1);
	
	graphics.fillPolygon(outline); 
	
	// draw the inner outline
	PointList innerLine = new PointList();
	
	innerLine.addPoint(rect.width - cornerSize - 1, 0);
	innerLine.addPoint(rect.width - cornerSize - 1, cornerSize);
	innerLine.addPoint(rect.width - 1, cornerSize);
	innerLine.addPoint(rect.width - cornerSize - 1, 0);
	innerLine.addPoint(0, 0);
	innerLine.addPoint(0, rect.height - 1);
	innerLine.addPoint(rect.width - 1, rect.height - 1);
	innerLine.addPoint(rect.width - 1, cornerSize);
	
	graphics.drawPolygon(innerLine);
	
	graphics.translate(getLocation().getNegated());
}


/**
 * Sets the size of the figure's corner to the given offset.
 * 
 * @param newSize the new size to use.
 */
public void setCornerSize(int newSize) {
	cornerSize = newSize;
}


/**
 * Sets the selection state of this SimpleActivityLabel
 * 
 * @param b
 *            true will cause the label to appear selected
 */
public void setSelected(boolean b) {
	selected = b;
	repaint();
}


public String getToolTipText() {
	return "getToolTipText()";
}


public Rectangle getSelectionRectangle() {
	return this.getBounds();
}


/**
 * sets the text of the label
 */
public abstract void setText(String s);


public abstract String getText();


/**
 * Делаем фигуру верхней
 *
 
public void putOnTop() {
	//-- переприсодиняем к родительскому чтобы фигура всегда была наверху
	if(this.getParent() instanceof IModelElementFigure) {
		((IModelElementFigure)this.getParent()).putOnTop();
	}
	IFigure parent = this.getParent();
	if(parent!=null) {
		parent.remove(this);
		parent.add(this);
	}
	
}*/


}
