/*
 * Created on Jul 20, 2004
 */
package net.confex.schema.policy;

import net.confex.schema.command.ModelElementTransferCommand;
import net.confex.schema.command.ShapeMoveCommand;
import net.confex.schema.model.IModelElementContainer;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.StateContainer;
import net.confex.schema.part.NodeElementPart;
import net.confex.schema.part.SimpleContainerPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;



/**
 * Handles manual layout editing for schema diagram. Only available for
 * XYLayoutManagers, not for automatic layout
 * 
 * @author Roman Eremeev
 */
public class SchemaXYLayoutPolicy extends XYLayoutEditPolicy {

	
	protected Command createAddCommand(Request request, EditPart childEditPart, 	Object constraint) {
		return null;
	}
	
	
	/*
	 * Вызывается при изменении перетаскивании элемента из контейнера в контейнер
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createAddCommand(EditPart elementPart, Object constraint) {
		try {
			
			//-- Когда контейнер находится в закрытом состоянии в него нельзя добавлять новые элементы.
			if(this.getHost() instanceof SimpleContainerPart) {
				if( ((StateContainer)this.getHost().getModel()).isCompact() ) {
					return null;
				}
			}
			
			Rectangle rect = (Rectangle)constraint;
				
			//System.out.println("-1- [SchemaXYLayoutPolicy.createAddCommand] x, y" + rect.x + "," + rect.y);
			if(!(elementPart.getParent().getModel() instanceof IModelElementContainer)) {
				//System.out.println("-!- [SchemaXYLayoutPolicy.createAddCommand]");
				return null;
			}
			if( !(elementPart.getModel() instanceof NodeElement) ) {
				//System.out.println("-!- [SchemaXYLayoutPolicy.createAddCommand]");
				return null;
			}
			NodeElement element = (NodeElement) elementPart.getModel();
			IModelElementContainer originalContainer = (IModelElementContainer) elementPart.getParent().getModel();
			EditPart part=getHost();
			IModelElementContainer newContainer = (IModelElementContainer) part.getModel();
			
			ModelElementTransferCommand trans_command = new ModelElementTransferCommand
				( element, originalContainer, newContainer, rect);
			//System.out.println("-2- [SchemaXYLayoutPolicy.createAddCommand]" + trans_command);
			return trans_command;
		} catch(Exception ex) {
			System.err.println(" !!! "+ ex.getMessage());
			return null;
		}
	}

	
	/**
	 * Вызывается при изменении геометрии элемента перемещении или изменении размеров
	 */
	protected Command createChangeConstraintCommand(EditPart child, Object constraint){
		//System.out.println("-- [SchemaXYLayoutPolicy.createChangeConstraintCommand] " + child);
		Command command=null;
		
		//if (!(child instanceof TablePart) && !(child instanceof NodeElementPart))
		//	return null;
		if (!(constraint instanceof Rectangle))
			return null;
		Rectangle oldBounds=null;
		Rectangle newBounds=null;
		if (child instanceof NodeElementPart) {
			NodeElementPart labelPart = (NodeElementPart) child;
			NodeElement label = labelPart.getNodeElement();
			IFigure figure =  labelPart.getFigure();
			oldBounds = figure.getBounds();
			newBounds = (Rectangle) constraint;
			//-- отключаем ограничение на изменение размеров
			//if (oldBounds.width != newBounds.width && newBounds.width != -1)
			//	return null;
			//if (oldBounds.height != newBounds.height && newBounds.height != -1)
			//	return null;
			//if(!(labelPart.getParent() instanceof SchemaDiagramPart)) {
			//	System.err.println("!!! [SchemaXYLayoutPolicy.createChangeConstraintCommand] NOT SchemaDiagramPart " + labelPart.getParent());
			//}
			//SchemaDiagramPart schemaPart = (SchemaDiagramPart) labelPart.getParent();
			command = new ShapeMoveCommand(label, oldBounds.getCopy(), newBounds.getCopy());
			//System.out.println("-!- [SchemaXYLayoutPolicy.createChangeConstraintCommand] " + child);
		} else {
			System.err.println("!!! [SchemaXYLayoutPolicy.createChangeConstraintCommand] UNKNOWN Part " + child);
			return null;
		}

		return command;
	}

	
	/**
	 * Returns the current bounds as the constraint if none can be found in the
	 * figures Constraint object
	 */
	public Rectangle getCurrentConstraintFor(GraphicalEditPart child) {
		IFigure fig = child.getFigure();
		Rectangle rectangle = (Rectangle) fig.getParent().getLayoutManager().getConstraint(fig);
		if (rectangle == null)
		{
			rectangle = fig.getBounds();
		}
		return rectangle;
	}
	

	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	
	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}

}