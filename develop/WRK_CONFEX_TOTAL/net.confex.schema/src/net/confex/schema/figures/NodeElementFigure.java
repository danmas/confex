package net.confex.schema.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;


public class NodeElementFigure extends Label implements IModelElementFigure {

		private boolean selected;
		private Label tooltip_label= new Label("");
			
		
		public NodeElementFigure() {
			super();
		}
		
		
		public NodeElementFigure(String text) {
			super();
			setText(text);
			setToolTip(tooltip_label);
		}
		
		
		/**
		 * paints figure differently depends on the whether the figure has focus or is selected 
		 */
		public void paintFigure(Graphics graphics) {
			if (selected){
				//graphics.pushState();
				//graphics.setBackgroundColor(ColorConstants.menuBackgroundSelected);
				//graphics.fillRectangle(getSelectionRectangle());
				//graphics.popState();
				//graphics.setForegroundColor(ColorConstants.white);
			}
			super.paintFigure(graphics); 
		}

		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// IModelElementFigure
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		
		public Rectangle getSelectionRectangle() {
			Rectangle bounds = getTextBounds().getCopy();
			bounds.expand(new Insets(2, 2, 0, 0));
			translateToParent(bounds);
			bounds.intersect(getBounds());
			return bounds;
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
			return tooltip_label.getText();
		}

		
		public void setToolTipText(String txt) {
			tooltip_label.setText(txt);
		}
		
		
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
		}
		 */
		
}
