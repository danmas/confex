package net.confex.schema.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;


public class RoundedRectangleBorder	extends LineBorder {

	//private int width = 1;
	//private Color color;
	
	/** The width and height radii applied to each corner. */
	protected Dimension corner = new Dimension(8, 8);

	
	/**
	 * Constructs a LineBorder with the specified color and of the specified width.
	 *
	 * @param color The color of the border.
	 * @param width The width of the border in pixels.
	 */
	public RoundedRectangleBorder(Color color, int width) {
		super(color,width);
	}

	
	/**
	 * Constructs a LineBorder with the specified color and a width of 1 pixel.
	 *
	 * @param color The color of the border.
	 */
	public RoundedRectangleBorder(Color color) {
		this(color, 1);
	}

	
	/**
	 * Constructs a black LineBorder with the specified width.
	 *
	 * @param width The width of the border in pixels.
	 */
	public RoundedRectangleBorder(int width) {
		this(null, width);
	}

	
	/**
	 * Constructs a default black LineBorder with a width of one pixel.
	 * 
	 */
	public RoundedRectangleBorder() { }

	
	/**
	 * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
	 */
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));
		
		if (getWidth() % 2 == 1) {
			tempRect.width--;
			tempRect.height--;
		}
		//tempRect.shrink(getWidth() / 2, getWidth() / 2);
		graphics.setLineWidth(getWidth());
		if (getColor() != null)
			graphics.setForegroundColor(getColor());
		//graphics.drawRectangle(tempRect);
		
		Rectangle f = Rectangle.SINGLETON;
		Rectangle r = tempRect;
		f.x = r.x + getWidth() / 2;
		f.y = r.y + getWidth() / 2;
		f.width = r.width - getWidth();
		f.height = r.height - getWidth();
		graphics.drawRoundRectangle(f, corner.width, corner.height);
	}

	
	/**
	 * Sets the dimensions of each corner. This will form the radii of the arcs which form the
	 * corners.
	 *
	 * @param d the dimensions of the corner
	 */
	public void setCornerDimensions(Dimension d) {
		corner.width = d.width;
		corner.height = d.height;
	}
	
}
