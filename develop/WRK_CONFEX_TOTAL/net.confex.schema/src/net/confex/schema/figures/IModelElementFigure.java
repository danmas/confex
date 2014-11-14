package net.confex.schema.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;


public interface IModelElementFigure  extends IFigure {


	public void setToolTipText(String txt);

	
	public String getToolTipText();
	

	public Rectangle getSelectionRectangle();

	
	/**
	 * sets the text of the label
	 */
	public void setText(String s);

	
	public String getText();
	
	
	/**
	 * paints figure differently depends on the whether the figure has focus or is selected 
	 */
	public void paintFigure(Graphics graphics);

	
	/**
	 * Sets the selection state of this SimpleActivityLabel
	 * 
	 * @param b
	 *            true will cause the label to appear selected
	 */
	public void setSelected(boolean b);
	
	
	/**
	 * Делаем фигуру верхней
	 *
	 */
	//public void putOnTop();


	
}
