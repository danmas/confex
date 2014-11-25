/*
 * Created on Jul 13, 2004
 */
package net.confex.schema.layout;

import java.util.Map;

import net.confex.schema.model.Schema;
import net.confex.schema.part.SchemaDiagramPart;
import net.confex.schema.policy.SchemaXYLayoutPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

/**
 * Used to delegate between the GraphyLayoutManager and the GraphXYLayout
 * classes
 * 
 * @author Phil Zoio
 */
public class DelegatingLayoutManager implements LayoutManager {

	private SchemaDiagramPart diagram;
	private Map figureToBoundsMap;
	private Map partsToNodeMap;

	private LayoutManager activeLayoutManager;
	// private GraphLayoutManager graphLayoutManager;
	private GraphXYLayout xyLayoutManager;
	private MyGridLayout gridLayoutManager;

	public DelegatingLayoutManager(SchemaDiagramPart diagram) {
		this.diagram = diagram;
		// this.graphLayoutManager = new GraphLayoutManager(diagram);
		this.xyLayoutManager = new GraphXYLayout();
		
		this.gridLayoutManager = new MyGridLayout(diagram);
		this.gridLayoutManager.numColumns = 2;
		
		// use the graph layout manager as the initial delegate
		this.activeLayoutManager = this.xyLayoutManager;
		//this.activeLayoutManager = this.gridLayoutManager;
	}

	// ********************* layout manager methods methods
	// ****************************/

	public void layout(IFigure container) {

		//Schema schema = diagram.getSchema();
		// setLayoutManager(container, xyLayoutManager);
		setLayoutManager(container, activeLayoutManager);
		activeLayoutManager.layout(container);

		/*
		if (schema.isLayoutManualDesired()) {

			if (activeLayoutManager != xyLayoutManager) {

				if (schema.isLayoutManualAllowed()) {

					// yes we are okay to start populating the table bounds
					setLayoutManager(container, xyLayoutManager);
					activeLayoutManager.layout(container);

				} else {

					// we first have to set the constraint data
					if (diagram.setTableFigureBounds(true)) {
						// we successfully set bounds for all the existing
						// tables so we can start using xyLayout immediately
						setLayoutManager(container, xyLayoutManager);
						activeLayoutManager.layout(container);
					} else {
						// we did not - we still need to run autolayout once
						// before we can set xyLayout
						activeLayoutManager.layout(container);

						// run this again so that it will work again next time
						setLayoutManager(container, xyLayoutManager);
					}

				}

			} else {
				setLayoutManager(container, xyLayoutManager);
				activeLayoutManager.layout(container);
			}
		} else {
			// setLayoutManager(container, graphLayoutManager);
			// activeLayoutManager.layout(container);
		}
		*/

	}

	public Object getConstraint(IFigure child) {
		return activeLayoutManager.getConstraint(child);
	}

	public Dimension getMinimumSize(IFigure container, int wHint, int hHint) {
		return activeLayoutManager.getMinimumSize(container, wHint, hHint);
	}

	public Dimension getPreferredSize(IFigure container, int wHint, int hHint) {
		return activeLayoutManager.getPreferredSize(container, wHint, hHint);
	}

	public void invalidate() {
		activeLayoutManager.invalidate();
	}

	public void remove(IFigure child) {
		activeLayoutManager.remove(child);
	}

	public void setConstraint(IFigure child, Object constraint) {
		activeLayoutManager.setConstraint(child, constraint);
	}

	public void setXYLayoutConstraint(IFigure child, Rectangle constraint) {
		xyLayoutManager.setConstraint(child, constraint);
	}

	// ********************* protected and private methods
	// ****************************/

	/**
	 * Sets the current active layout manager
	 */
	private void setLayoutManager(IFigure container, LayoutManager layoutManager) {
		container.setLayoutManager(layoutManager);
		this.activeLayoutManager = layoutManager;
		if (layoutManager == xyLayoutManager) {
			diagram.installEditPolicy(EditPolicy.LAYOUT_ROLE,
					new SchemaXYLayoutPolicy());
		} else {
			diagram.installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
		}
	}

	public LayoutManager getActiveLayoutManager() {
		return activeLayoutManager;
	}

}