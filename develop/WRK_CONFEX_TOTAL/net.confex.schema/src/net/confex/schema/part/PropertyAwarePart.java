/*
 * Created on Jul 15, 2004
 */
package net.confex.schema.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import net.confex.schema.model.PropertyAwareObject;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;



/**
 * An abstract EditPart implementation which is property aware and responds to
 * PropertyChangeEvents fired from the model
 * @author Phil Zoio
 */
public abstract class PropertyAwarePart extends AbstractGraphicalEditPart 
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
	public void deactivate() {
		super.deactivate();
		PropertyAwareObject propertyAwareObject = (PropertyAwareObject) getModel();
		propertyAwareObject.removePropertyChangeListener(this);
	}

	
	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)	{

		String property = evt.getPropertyName();

		if (PropertyAwareObject.CHILD.equals(property)) {
			handleChildChange(evt);
		} else if (PropertyAwareObject.REORDER.equals(property)){
			handleReorderChange(evt);
		} else if (PropertyAwareObject.OUTPUT.equals(property))	{
			handleOutputChange(evt);
		} else if (PropertyAwareObject.INPUT.equals(property)) {
			handleInputChange(evt);
		} else if (PropertyAwareObject.NAME.equals(property))	{
			handleNameChange(evt);
		//} else if (PropertyAwareObject.BOUNDS.equals(property))	{
		//	handleBoundsChange(evt);
		} else if (PropertyAwareObject.LAYOUT.equals(property))	{
			handleLayoutChange(evt);
		} else if (PropertyAwareObject.TEXT.equals(property)) {
			handleTextChange(evt);
		} else if (PropertyAwareObject.TOOLTIP_TEXT.equals(property)) {
			handleTooltipTextChange(evt);
		} else if (PropertyAwareObject.LAYOUT.equals(property))	{
			handleTooltipTextChange(evt);
		} else if (PropertyAwareObject.FORE_COLOR.equals(property)) {
			handleForeColorChange(evt);
		} else if (PropertyAwareObject.BACK_COLOR.equals(property)) {
			handleBackColorChange(evt);
		} else if (PropertyAwareObject.SELECTED.equals(property)) {
			handleSelected(evt);
		} else

		//we want direct edit name changes to update immediately
		//not use the Graph animation, if automatic layout is being used
		if (PropertyAwareObject.NAME.equals(property))
		{
			GraphicalEditPart graphicalEditPart = (GraphicalEditPart) (getViewer().getContents());
			IFigure partFigure = graphicalEditPart.getFigure();
			partFigure.getUpdateManager().performUpdate();
		}

	}

	
	/**
	 * Called when change to one of the inputs occurs
	 */
	private void handleInputChange(PropertyChangeEvent evt)	{

		//this works but is not efficient
		//refreshTargetConnections();

		//a more efficient implementation should either remove or add the
		//relevant target connection
		//using the removeTargetConnection(ConnectionEditPart connection) or
		//addTargetConnection(ConnectionEditPart connection, int index)

		Object newValue = evt.getNewValue();
		Object oldValue = evt.getOldValue();

		if (!((oldValue != null) ^ (newValue != null)))	{
			throw new IllegalStateException("Exactly one of old or new values must be non-null for INPUT event");
		}
		if (newValue != null) {
			//add new connection
			ConnectionEditPart editPart = createOrFindConnection(newValue);
			int modelIndex = getModelTargetConnections().indexOf(newValue);
			addTargetConnection(editPart, modelIndex);
		} else	{
			//remove connection
			List children = getTargetConnections();

			ConnectionEditPart partToRemove = null;
			for (Iterator iter = children.iterator(); iter.hasNext();)	{
				ConnectionEditPart part = (ConnectionEditPart) iter.next();
				if (part.getModel() == oldValue) {
					partToRemove = part;
					break;
				}
			}
			if (partToRemove != null)
				removeTargetConnection(partToRemove);
		}
		
		getContentPane().revalidate();
	}

	
	/**
	 * Called when change to one of the outputs occurs
	 */
	private void handleOutputChange(PropertyChangeEvent evt) {

		//this works but is not efficient
		//refreshSourceConnections();

		// a more efficient implementation should either remove or add the
		// relevant target connect
		//using the removeSourceConnection(ConnectionEditPart connection) or
		//addSourceConnection(ConnectionEditPart connection, int index)

		Object newValue = evt.getNewValue();
		Object oldValue = evt.getOldValue();

		if (!((oldValue != null) ^ (newValue != null)))	{
			throw new IllegalStateException("Exactly one of old or new values must be non-null for INPUT event");
		}
		if (newValue != null) {
			//add new connection
			ConnectionEditPart editPart = createOrFindConnection(newValue);
			int modelIndex = getModelSourceConnections().indexOf(newValue);
			addSourceConnection(editPart, modelIndex);
		} else {
			//remove connection
			List children = getSourceConnections();

			ConnectionEditPart partToRemove = null;
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				ConnectionEditPart part = (ConnectionEditPart) iter.next();
				if (part.getModel() == oldValue) {
					partToRemove = part;
					break;
				}
			}
			if (partToRemove != null)
				removeSourceConnection(partToRemove);
		}
		getContentPane().revalidate();
	}

	
	/**
	 * called when child added or removed
	 */
	protected void handleChildChange(PropertyChangeEvent evt) {
		//we could do this but it is not very efficient
		//refreshChildren();
		Object newValue = evt.getNewValue();
		Object oldValue = evt.getOldValue();

		if (!((oldValue != null) ^ (newValue != null)))	{
			throw new IllegalStateException("Exactly one of old or new values must be non-null for CHILD event");
		}
		if (newValue != null) {
			//add new child
			EditPart editPart = createChild(newValue);
			int modelIndex = getModelChildren().indexOf(newValue);
			addChild(editPart, modelIndex);
		} else {
			List children = getChildren();

			EditPart partToRemove = null;
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				EditPart part = (EditPart) iter.next();
				if (part.getModel() == oldValue) {
					partToRemove = part;
					break;
				}
			}
			if (partToRemove != null)
				removeChild(partToRemove);
		}
		getContentPane().revalidate();
	}

	
	/**
	 * Called when columns are re-ordered within
	 */
	protected void handleReorderChange(PropertyChangeEvent evt)	{
		refreshChildren();
		refreshVisuals();
	}

	
	/**
	 * Обработчик события выбора от элемента модели
	 * @param evt
	 */
	protected void handleSelected(PropertyChangeEvent evt) {
		EditPart hostEditPart = (EditPart)evt.getOldValue();
		hostEditPart.getViewer().select(this);
		//this.setSelected(EditPart.SELECTED_PRIMARY);
	}
	
	
	
    /**
     * Сделать figure и все дочерние Visible
     * @param visible - true все видимы
     */
    public void setAllVisible(boolean visible) {
        this.getFigure().setVisible(visible);
    }


	protected void handleTextChange(PropertyChangeEvent evt) {
	}

	
	protected void handleTooltipTextChange(PropertyChangeEvent evt) {
	}
	
	
	protected void handleForeColorChange(PropertyChangeEvent evt) {
	}
	
	protected void handleBackColorChange(PropertyChangeEvent evt) {
	}
	
	/**
	 * @param subclass
	 *            decides what to do if layout property event is fired
	 */
	protected void handleLayoutChange(PropertyChangeEvent evt)	{
	}

	
	/**
	 * handles change in bounds, to be overridden by subclass
	 */
	protected void handleBoundsChange(PropertyChangeEvent evt)	{
	}

	
	/**
	 * Handles change in name - to be overridden by subclasses
	 */
	protected void handleNameChange(PropertyChangeEvent evt)	{
	}

}