/*
 * Created on Jul 15, 2004
 */
package net.confex.schema.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.confex.schema.model.PropertyAwareObject;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;



		/**
		 * An ConnectionEditPart base class which is property aware, that is, can handle property change notification events
		 * All our ConnectionEditPart are subclasses of this
		 * @author Phil Zoio
		 */
		public abstract class PropertyAwareConnectionPart extends AbstractConnectionEditPart 
			implements PropertyChangeListener {
		
			
		/**
		 * @see org.eclipse.gef.EditPart#activate()
		 */
		public void activate()	{
			super.activate();
			PropertyAwareObject propertyAwareObject = (PropertyAwareObject) getModel();
			propertyAwareObject.addPropertyChangeListener(this);
		}
		
		
		/**
		 * @see org.eclipse.gef.EditPart#deactivate()
		 */
		public void deactivate()
		{
			super.deactivate();
			PropertyAwareObject propertyAwareObject = (PropertyAwareObject) getModel();
			propertyAwareObject.removePropertyChangeListener(this);
		}
		
		
		/**
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt)	{
		
			String property = evt.getPropertyName();
		
			if (PropertyAwareObject.CHILD.equals(property))
				refreshChildren();
			else if (PropertyAwareObject.INPUT.equals(property))
				refreshTargetConnections();
			else if (PropertyAwareObject.OUTPUT.equals(property)) {
				refreshSourceConnections();
			} else if (PropertyAwareObject.TEXT.equals(property)) {
				handleTextChange(evt);
			} else if (PropertyAwareObject.TOOLTIP_TEXT.equals(property)) {
				handleTooltipTextChange(evt);
			} else if (PropertyAwareObject.LAYOUT.equals(property))	{
				handleTooltipTextChange(evt);
			} else if (PropertyAwareObject.FORE_COLOR.equals(property)) {
				handleForeColorChange(evt);
		
				/*
				 * if (FlowElement.CHILDREN.equals(prop)) refreshChildren(); else if
				 * (FlowElement.INPUTS.equals(prop)) refreshTargetConnections(); else if
				 * (FlowElement.OUTPUTS.equals(prop)) refreshSourceConnections(); else
				 * if (Activity.NAME.equals(prop)) refreshVisuals(); // Causes Graph to
				 * re-layout
				 */
				((GraphicalEditPart) (getViewer().getContents())).getFigure().revalidate();
			}
		}

		
		protected void handleTextChange(PropertyChangeEvent evt) {
		}
	
		
		protected void handleTooltipTextChange(PropertyChangeEvent evt) {
		}
		
		
		protected void handleForeColorChange(PropertyChangeEvent evt) {
		}
		
}