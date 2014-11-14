package net.confex.schema.part;

import java.beans.PropertyChangeEvent;

import net.confex.schema.figures.ModelConnectionFigure;
import net.confex.schema.model.ModelConnection;
import net.confex.schema.policy.ConnectionEditPolicy;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;



public class ModelConnectionPart extends PropertyAwareConnectionPart  {

	
	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		super.activate();
	}
	
	
	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void deactivate() {
		super.deactivate();
	}
	
	
	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies()	{
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectionEditPolicy());
	}

	
	/**
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		//PolylineConnection conn = (PolylineConnection) super.createFigure();
		ModelConnectionFigure conn = new ModelConnectionFigure();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		//conn.setTargetDecoration(new PolygonDecoration());
		//-- to avoid recursion we must setup figure before setStyle
		//setFigure(conn);
		
		setStyle(conn);
		
		/*
		PolygonDecoration decoration = new PolygonDecoration();
		PointList decorationPointList = new PointList();
		decorationPointList.addPoint(0,0);
		decorationPointList.addPoint(-2,2);
		decorationPointList.addPoint(-4,0);
		decorationPointList.addPoint(-2,-2);
		decoration.setTemplate(decorationPointList);
		conn.setSourceDecoration(decoration);
		*/
		
		return conn;
	}

	
	/**
	 * Устанавливает стиль связи
	 *
	 */
	protected void setStyle(PolylineConnection conn) {
		ModelConnection model_connection = (ModelConnection)this.getModel();

		String type = model_connection.getConnectionType();
		if(type.equals(ModelConnection.DEFAULT)) {
			//conn.setConnectionRouter(new BendpointConnectionRouter());
			//conn.setTargetDecoration(new PolygonDecoration());

			conn.setTargetDecoration(new PolylineDecoration());
			conn.setConnectionRouter(new ManhattanConnectionRouter());
			
			conn.setLineStyle(Graphics.LINE_SOLID);
		} else if(type.equals(ModelConnection.NOTE)) {
			conn.setConnectionRouter(new BendpointConnectionRouter());
			conn.setTargetDecoration(null);
			conn.setLineStyle(Graphics.LINE_DOT);
		} else if(type.equals(ModelConnection.GENERALISATION)) {
			conn.setConnectionRouter(new BendpointConnectionRouter());
			PolygonDecoration poligon = new PolygonDecoration();
			//conn.setConnectionRouter(new ManhattanConnectionRouter());
			poligon.setFill(false);
			poligon.setScale(9,5);
			poligon.setOpaque(true);
			conn.setTargetDecoration(poligon);
			conn.setLineStyle(Graphics.LINE_SOLID);
		}
		conn.setForegroundColor(
				new Color(PlatformUI.getWorkbench().getDisplay(), model_connection.getForeColor()));
		conn.setBackgroundColor(
				new Color(PlatformUI.getWorkbench().getDisplay(), model_connection.getBackColor()));
	}
	
	
	public void propertyChange(PropertyChangeEvent evt)	{
		super.propertyChange(evt);
		
		String property = evt.getPropertyName();

		if (ModelConnection.CONNECTION_TYPE.equals(property)) {
			handleConnectionTypeChange(evt);
		}

	}
	
	
	/**
	 * Reset the layout constraint, and revalidate the content pane
	 */
	protected void refreshVisuals()	{
		super.refreshVisuals();
		
	}
	
	
	protected void handleConnectionTypeChange(PropertyChangeEvent evt) {
		setStyle((PolylineConnection) getFigure());
		refreshVisuals();
	}

	
	protected void handleForeColorChange(PropertyChangeEvent evt) {
		setStyle((PolylineConnection) getFigure());
		refreshVisuals();
	}

	
	/**
	 * Sets the width of the line when selected
	 */
	public void setSelected(int value){
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE)
			((PolylineConnection) getFigure()).setLineWidth(2);
		else
			((PolylineConnection) getFigure()).setLineWidth(1);
	}

	
	/**
	 * Called <em>after</em> the EditPart has been added to its parent. This is used to
	 * indicate to the EditPart that it should refresh itself for the first time.
	 */
	public void addNotify() {
		super.addNotify();
		Object parent = this.getParent();
		System.out.println("addNotify");
	}
	
	
	public void setParent(EditPart parent) {
		super.setParent(parent);
	}

	
	/*
	protected void activateFigure() {
		//getLayer(CONNECTION_LAYER).add(getFigure());
		getLayer(GRID_LAYER).add(getFigure());
		
	}

	
	protected void deactivateFigure() {
		//getLayer(CONNECTION_LAYER).remove(getFigure());
		getLayer(GRID_LAYER).remove(getFigure());
		getConnectionFigure().setSourceAnchor(null);
		getConnectionFigure().setTargetAnchor(null);
	}*/

}
