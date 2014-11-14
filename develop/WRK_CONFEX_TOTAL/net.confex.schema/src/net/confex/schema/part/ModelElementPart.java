package net.confex.schema.part;

import java.beans.PropertyChangeEvent;

import net.confex.schema.figures.IModelElementFigure;
import net.confex.schema.layout.GraphXYLayout;
import net.confex.schema.model.ModelElement;
import net.confex.schema.model.Schema;
import net.confex.schema.policy.NodeComponentEditPolicy;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;


public abstract class ModelElementPart extends PropertyAwarePart {

	
	public Schema getSchema() {
		return getModelElement().getSchema();
	}

	
	// ******************* Life-cycle related methods *********************/

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		super.activate();
	}

	
	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
	}

	
	// ******************* Model related methods *********************/

	/**
	 * Returns the Table model object represented by this EditPart
	 */
	public ModelElement getModelElement() {
		return (ModelElement) getModel();
	}

	
	// ******************* Editing related methods *********************/

	/**
	 * Creates edit policies and associates these with roles
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new NodeComponentEditPolicy());
	}

	
	// ******************* Direct editing related methods *********************/

	// ******************* Listener related methods *********************/

	protected void handleChildChange(PropertyChangeEvent evt) {
		super.handleChildChange(evt);
	}

	
	/**
	 * Handles change in name when committing a direct edit
	 */
	protected void commitTextChange(PropertyChangeEvent evt) {
		IModelElementFigure figure = (IModelElementFigure) getFigure();
		figure.setText(getModelElement().getText());
		figure.setVisible(true);
		refreshVisuals();
	}

	
	/**
	 * @param handles
	 *            the text change during an edit
	 */
	public void handleTextChange(String value) {
		IModelElementFigure mylabelFigure = (IModelElementFigure) getFigure();
		mylabelFigure.setVisible(false);
		refreshVisuals();
	}

	
	protected void handleTextChange(PropertyChangeEvent evt) {
		commitTextChange(evt);
	}

	
	protected void handleTooltipTextChange(PropertyChangeEvent evt) {
		IModelElementFigure figure = (IModelElementFigure) getFigure();
		figure.setToolTipText(getModelElement().getTooltipText());
		figure.setVisible(true);
		refreshVisuals();
	}

	
	protected void handleForeColorChange(PropertyChangeEvent evt) {
		IModelElementFigure figure = (IModelElementFigure) getFigure();

		figure.setForegroundColor(new Color(PlatformUI.getWorkbench()
				.getDisplay(), getModelElement().getForeColor()));

		figure.setVisible(true);
		refreshVisuals();
	}

	
	protected void handleBackColorChange(PropertyChangeEvent evt) {
		IModelElementFigure figure = (IModelElementFigure) getFigure();

		figure.setBackgroundColor(new Color(PlatformUI.getWorkbench()
				.getDisplay(), getModelElement().getBackColor()));

		figure.setVisible(true);
		refreshVisuals();
	}

	
	/**
	 * Reverts to existing text in model when exiting from a direct edit
	 * (possibly before a commit which will result in a change in the figure
	 * value)
	 */
	public void revertTextChange() {
		IModelElementFigure figure = (IModelElementFigure) getFigure();
		ModelElement element = getModelElement();
		figure.setText(element.getText());
		figure.setVisible(true);
		refreshVisuals();
	}

	
	// ******************* Layout related methods *********************/

	/**
	 * Reset the layout constraint, and revalidate the content pane
	 */
	protected void refreshVisuals() {
		/*
		 * //IModelElementFigure figure = (IModelElementFigure) getFigure();
		 * Figure figure = (Figure) getFigure();
		 * 
		 * Point location = figure.getLocation(); Rectangle constraint = new
		 * Rectangle(location.x, location.y, -1, -1);
		 * 
		 * if(getParent() instanceof IModelElementContainerPart) {
		 * ((IModelElementContainerPart) getParent()).setLayoutConstraint(this,
		 * figure, constraint); } else { System.err.println(" Container not a
		 * IModelElementContainerPart!!!"); }
		 */
	}
	

	/**
	 * @return the Content pane for adding or removing child figures
	 */
	public IFigure getContentPane() {
		IModelElementFigure figure = (IModelElementFigure) getFigure();
		return figure;
	}

	
	/**
	 * Sets the width of the line when selected
	 */
	public void setSelected(int value) {
		super.setSelected(value);
		IModelElementFigure figure = (IModelElementFigure) getFigure();
		if (value != EditPart.SELECTED_NONE) {
			if(figure.getParent().getLayoutManager() instanceof GraphXYLayout) {
				putFigureOnTop();
			}
			figure.setSelected(true);
		} else
			figure.setSelected(false);
		figure.repaint();
	}

	
	/**
	 * Делаем фигуру верхней
	 * 
	 */
	public void putFigureOnTop() {
		//-- помещаем наверх родительский элемент
		if(getParent() instanceof ModelElementPart) {
			((ModelElementPart) getParent()).putFigureOnTop();
		}
		//-- помещаем наверх текущий элемент
		IModelElementFigure figure = (IModelElementFigure) getFigure();
		IFigure parent = figure.getParent();
		if (parent != null) {
			parent.remove(figure);
			parent.add(figure);
		}
	}
	

	// ******************* Miscellaneous stuff *********************/

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
	public String toString() {
		return getModel().toString();
	}

}
