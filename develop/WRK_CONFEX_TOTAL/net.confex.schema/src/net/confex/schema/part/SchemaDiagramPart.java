package net.confex.schema.part;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;
import java.util.List;

import net.confex.schema.figures.SchemaFigure;
import net.confex.schema.layout.DelegatingLayoutManager;
import net.confex.schema.layout.GraphAnimation;
import net.confex.schema.layout.MyGridLayout;
import net.confex.schema.model.Schema;
import net.confex.schema.policy.SchemaContainerEditPolicy;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.CommandStackListener;



/**
 * Edit part for Schema object, and uses a SchemaDiagram figure as
 * the container for all graphical objects
 * 
 * @author Phil Zoio
 */
public class SchemaDiagramPart extends PropertyAwarePart implements IModelElementContainerPart {

	CommandStackListener stackListener = new CommandStackListener()	{

	public void commandStackChanged(EventObject event)	{
			if (delegatingLayoutManager.getActiveLayoutManager() instanceof MyGridLayout)	{
				if (!GraphAnimation.captureLayout(getFigure()))
					return;
				while (GraphAnimation.step())
					getFigure().getUpdateManager().performUpdate();
				GraphAnimation.end();
			} else{
				getFigure().getUpdateManager().performUpdate();
			} 
		}
	};
	
	
	private DelegatingLayoutManager delegatingLayoutManager;

	
	/**
	 * Adds this EditPart as a command stack listener, which can be used to call
	 * performUpdate() when it changes
	 */
	public void activate() {
		super.activate();
		getViewer().getEditDomain().getCommandStack().addCommandStackListener(stackListener);
	}

	
	/**
	 * Removes this EditPart as a command stack listener
	 */
	public void deactivate(){
		//
		getViewer().getEditDomain().getCommandStack().removeCommandStackListener(stackListener);
		super.deactivate();
	}

	
	protected IFigure createFigure() {
		/**/
		Figure f = new SchemaFigure();
		delegatingLayoutManager = new DelegatingLayoutManager(this);
		f.setLayoutManager(delegatingLayoutManager);
		
		//GridLayout gridLayout = new GridLayout();
		//gridLayout.numColumns = 2;
		//f.setLayoutManager(gridLayout);
		/**/
		
		/*
		Figure f = new SchemaFigure();
		f.setBorder(new MarginBorder(3));
		f.setLayoutManager(new FreeformLayout());
		*/
		
		//Create the static router for the connection layer
		//ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
		//connLayer.setConnectionRouter(new ShortestPathConnectionRouter(f));
		//connLayer.setConnectionRouter(new FanRouter());
		//connLayer.setConnectionRouter(new ManhattanConnectionRouter());
		//connLayer.setConnectionRouter(new BendpointConnectionRouter());
		//connLayer.setConnectionRouter( ConnectionRouter.NULL);
		return f;
	}

	
	/**
	 * Пререрисовать все немедленно
	 */
	public void immediateRepaint() {
		getFigure().getUpdateManager().performUpdate();
	}
	
	
	public Schema getSchema() {
		return (Schema) getModel();
	}

	
	/**
	 * @return the children Model objects as a new ArrayList
	 */
	protected List getModelChildren() {
		return getSchema().getChildren();
	}

	
	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	public boolean isSelectable()
	{
		return false;
	}

	/**
	 * Creates EditPolicy objects for the EditPart. The LAYOUT_ROLE policy is
	 * left to the delegating layout manager
	 */
	protected void createEditPolicies()	{
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new SchemaContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
	}

	
	/**
	 * Updates the table bounds in the model so that the same bounds can be
	 * restored after saving
	 * 
	 * @return whether the procedure execute successfully without any omissions.
	 *         The latter occurs if any TableFigure has no bounds set for any of
	 *         the Table model objects
	 */
	public boolean setElementModelBounds() {
		/*
		List chidren_parts = getChildren();
		Schema schema = getSchema();

		for (Iterator iter = chidren_parts.iterator(); iter.hasNext();) {
			Object obj=iter.next();
			if(obj instanceof TablePart){
				TablePart tablePart = (TablePart) obj;
				TableFigure tableFigure = (TableFigure) tablePart.getFigure();
	
				//if we don't find a node for one of the children then we should
				// continue
				if (tableFigure == null)
					continue;
	
				Rectangle bounds = tableFigure.getBounds().getCopy();
				Table table = tablePart.getTable();
				table.setBounds(bounds);
			} else 	if(obj instanceof NodeElementPart){
				NodeElementPart elementPart = (NodeElementPart) obj;
				IModelElementFigure elementFigure = (IModelElementFigure) elementPart.getFigure();
	
				//if we don't find a node for one of the children then we should
				// continue
				if (elementFigure == null)
					continue;
	
				Rectangle bounds = elementFigure.getBounds().getCopy();
				NodeElement element = elementPart.getNodeElement();
				element.setBounds(bounds);
			} 
				
		}
		*/
		return true;
	}

	
	/**
	 * Updates the bounds of the table figure (without invoking any event
	 * handling), and sets layout constraint data
	 * 
	 * @return whether the procedure execute successfully without any omissions.
	 *         The latter occurs if any Table objects have no bounds set or if
	 *         no figure is available for the TablePart
	 */
	public boolean setTableFigureBounds(boolean updateConstraint) {
/*
		List childParts = getChildren();
		Schema schema = getSchema();

		for (Iterator iter = childParts.iterator(); iter.hasNext();)
		{
			Object obj=iter.next();
			if(obj instanceof TablePart){
				TablePart tablePart = (TablePart) obj;
				Table table = tablePart.getTable();
				//now check whether we can find an entry in the tableToNodesMap
				Rectangle bounds = table.getBounds();
				if (bounds == null)	{
					//TODO handle this better
					return false;
				} else {
					TableFigure tableFigure = (TableFigure) tablePart.getFigure();
					if (tableFigure == null){
						return false;
					}else {
						if (updateConstraint)	{
							//pass the constraint information to the xy layout
							//setting the width and height so that the preferred size will be applied
							delegatingLayoutManager.setXYLayoutConstraint(tableFigure, new Rectangle(bounds.x, bounds.y,
									-1, -1));
						}
					}
				}
			} else if(obj instanceof NodeElementPart){
				NodeElementPart labelPart = (NodeElementPart) obj;
				NodeElement label = labelPart.getNodeElement();
				//now check whether we can find an entry in the tableToNodesMap
				Rectangle bounds = label.getBounds();
				if (bounds == null)	{
					//TODO handle this better
					return false;
				} else {
					IModelElementFigure labelFigure = (IModelElementFigure) labelPart.getFigure();
					if (labelFigure == null){
						return false;
					}else {
						if (updateConstraint)	{
							//pass the constraint information to the xy layout
							//setting the width and height so that the preferred size will be applied
							delegatingLayoutManager.setXYLayoutConstraint(labelFigure, new Rectangle(bounds.x, bounds.y,
									-1, -1));
						}
					}
				}
			} 
				
		}*/
		return true;

	}

	
	/**
	 * Passes on to the delegating layout manager that the layout type has
	 * changed. The delegating layout manager will then decide whether to
	 * delegate layout to the XY or Graph layout
	 */
	protected void handleLayoutChange(PropertyChangeEvent evt) {
		Boolean layoutType = (Boolean) evt.getNewValue();
		boolean isManualLayoutDesired = layoutType.booleanValue();
		getFigure().setLayoutManager(delegatingLayoutManager);
	}

	
	/**
	 * Sets layout constraint only if XYLayout is active
	 */
	public void setLayoutConstraint(EditPart child, IFigure childFigure, Object constraint)	{
			super.setLayoutConstraint(child, childFigure, constraint);
	}

	
	/**
	 * Passes on to the delegating layout manager that the layout type has
	 * changed. The delegating layout manager will then decide whether to
	 * delegate layout to the XY or Graph layout
	 */
	protected void handleChildChange(PropertyChangeEvent evt) {
		super.handleChildChange(evt);
	}

    /**
     * Сделать все figure и все дочерние Visible
     * @param vis_flag - true все видимы
     */
    public void setAllVisible(boolean visible) {
            for(int i=0; i<children.size();i++) {
                    ((ModelElementPart)children.get(i)).setAllVisible(visible);
            }
            this.getFigure().setVisible(visible);
    }

    
    protected void addChild(EditPart child, int index) {
    	super.addChild(child, index);
    }
    
}