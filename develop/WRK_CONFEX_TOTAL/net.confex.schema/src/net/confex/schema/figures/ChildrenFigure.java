package net.confex.schema.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;


public class ChildrenFigure  extends FreeformLayer { 

	
	public ChildrenFigure()	{
		setOpaque(true);
		this.setBorder(new ChildrenFigureBorder());
		FreeformLayout layout = new FreeformLayout();
		setLayoutManager(layout);
	}

	
	/* Figure {

	public ChildrenFigure()	{
		FlowLayout layout = new FlowLayout();
		layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setStretchMinorAxis(false);
		layout.setHorizontal(false);
		setLayoutManager(layout);
		setBorder(new ChildrenFigureBorder());
		setBackgroundColor(ColorConstants.tooltipBackground);
		setForegroundColor(ColorConstants.blue);
		setOpaque(true);
	}*/
	
	
	class ChildrenFigureBorder extends AbstractBorder {
		public Insets getInsets(IFigure figure)	{
			return new Insets(5, 3, 3, 1);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
		}
	}
	
	
}