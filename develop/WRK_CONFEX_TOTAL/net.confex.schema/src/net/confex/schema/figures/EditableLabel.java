/*
 * Created on Jul 13, 2004
 */
package net.confex.schema.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A customized StickyNote based on the label used in the flow example. 
 * Primary selection is denoted by highlight and focus rectangle. 
 * Normal selection is denoted by highlight only. Borrowed from the Flow Editor example
 * @author Daniel Lee
 */
public class EditableLabel extends Label
{

	private boolean selected;
	private Label tooltip_label= new Label("ывавы");
	
	public EditableLabel(String text){
		super(text);
		setToolTip(tooltip_label);
	}
	
	private Rectangle getSelectionRectangle()
	{
		Rectangle bounds = getTextBounds().getCopy();
		bounds.expand(new Insets(2, 2, 0, 0));
		translateToParent(bounds);
		bounds.intersect(getBounds());
		return bounds;
	}

	
	/**
	 * sets the text of the label
	 */
	public void setText(String s)
	{
		super.setText(s);
	}
	
	public String getToolTipText() {
		return tooltip_label.getText();
	}

	
	public void setToolTipText(String txt) {
		tooltip_label.setText(txt);
	}
	
	
	/**
	 * paints figure differently depends on the whether the figure has focus or is selected 
	 */
	protected void paintFigure(Graphics graphics)
	{
		if (selected)
		{
			graphics.pushState();
			graphics.setBackgroundColor(ColorConstants.menuBackgroundSelected);
			graphics.fillRectangle(getSelectionRectangle());
			graphics.popState();
			graphics.setForegroundColor(ColorConstants.white);
		}
		super.paintFigure(graphics);
	}

	/**
	 * Sets the selection state of this SimpleActivityLabel
	 * 
	 * @param b
	 *            true will cause the label to appear selected
	 */
	public void setSelected(boolean b)
	{
		selected = b;
		repaint();
	}


}

