package net.confex.schema.policy;

import net.confex.schema.command.ContainerElementCreateCommand;
import net.confex.schema.command.PasteCommand;
import net.confex.schema.figures.SimpleContainerFigure;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.StateContainer;
import net.confex.schema.part.SimpleContainerPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;



public class SimpleContainerContainerEditPolicy  extends ContainerEditPolicy {

	
	/**
	 * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getAddCommand(org.eclipse.gef.requests.GroupRequest)
	 * вызывается в ContainerEditPolicy.getCommand()
	 * тип запроса - REQ_ADD   
	 */
	//protected Command getAddCommand(GroupRequest request) {
	//	//EditPart host = getTargetEditPart(request);
	//	return null;
	//}

	
	protected Command getCreateCommand(CreateRequest request) {
		Object newObject = request.getNewObject();
		SimpleContainerPart containerPart = (SimpleContainerPart) getHost();
		StateContainer container = (StateContainer)containerPart.getSimpleContainer();
		//-- Когда контейнер находится в закрытом состоянии в него нельзя добавлять новые элементы.
		if(container.isCompact()) {
			return null;
		}
		Rectangle rect;
		if(request.getSize()==null) {
			rect = new Rectangle(request.getLocation().x,request.getLocation().y,0,0);
		} else {
			//-- сюда попадет когда начнется выделение области для создания элемента
			rect = new Rectangle(request.getLocation().x,request.getLocation().y,request.getSize().width,request.getSize().height);
		}
		SimpleContainerFigure fig=(SimpleContainerFigure)containerPart.getFigure();
		IFigure fig2 = fig.getContentsPane();
		if(fig2.getParent() != null) {
	 		//System.out.println(" old rect.x ="+ rect.x +" rect.y ="+ rect.y);
	 		fig2.translateFromParent(rect);
	 		//System.out.println(" -2- rect.x ="+ rect.x +" rect.y ="+ rect.y);
	 		fig2.translateToRelative(rect);
			//System.out.println(" new rect.x ="+ rect.x +" rect.y ="+ rect.y);
		}
		if(newObject instanceof PasteCommand ) {
			PasteCommand paste_command = (PasteCommand) newObject;
			paste_command.setSchema(container.getSchema());
			paste_command.setHostEditPart(this.getHost());
			paste_command.setContainer(container);
			paste_command.setLocation(rect);
			return (PasteCommand) newObject;
		}
		if (newObject instanceof NodeElement) {
			NodeElement element = (NodeElement) newObject;
	
			ContainerElementCreateCommand command = new ContainerElementCreateCommand(
					container, element, container.getSchema(), rect);
			return command;
		}
		return null;
	}
	

	/**
	 * @see ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 * Вызывается каждый раз при движении мыши по контейнеру. 
	 * Если возвращает null то курсор будет "disable insert"
	 * вызывается в ContainerEditPolicy.getCommand()
	 * тип запроса - REQ_CREATE   
	 */
	/*
	protected Command getCreateCommand(CreateRequest request) {
		Object newObject = request.getNewObject();
		EditPart host = getTargetEditPart(request);
		SimpleContainerPart schemaPart = (SimpleContainerPart)getHost();
		Schema schema = schemaPart.getSchema();
		if(newObject instanceof Table) { 
			Table table = (Table) newObject;
			TableAddCommand tableAddCommand = new TableAddCommand();
			tableAddCommand.setSchema(schema);
			tableAddCommand.setTable(table);
			return tableAddCommand;
		} else {
			Rectangle rect;
			if(request.getSize()==null) {
				rect = new Rectangle(request.getLocation().x,request.getLocation().y,0,0);
			} else {
				//-- сюда попадет когда начнется выделение области для создания элемента
				rect = new Rectangle(request.getLocation().x,request.getLocation().y,request.getSize().width,request.getSize().height);
			}
			AddShapeCommand shapeAddCommand = new AddShapeCommand(request.getNewObject(),schema	,rect);
			if(shapeAddCommand==null) {
				System.out.println("shapeAddCommand is NULL");
			}
			return shapeAddCommand;
		}
	}*/

		/*
		 * 	
protected IFigure getLayoutContainer() {
return ((GraphicalEditPart)getHost()).getContentPane();
}	 		 * 
		 * 
		 * figure
public final void translateToRelative(Translatable t) {
if (getParent() != null) {
getParent().translateToRelative(t);
getParent().translateFromParent(t);
}
}
		 * 
		 * 
		 * for (int i = 0; i < editParts.size(); i++) {
			childPart = (GraphicalEditPart)editParts.get(i);
			r = childPart.getFigure().getBounds().getCopy();
			//convert r to absolute from childpart figure
			childPart.getFigure().translateToAbsolute(r);
			r = request.getTransformedRectangle(r);
			//convert this figure to relative 
			getLayoutContainer().translateToRelative(r);
			getLayoutContainer().translateFromParent(r);
			r.translate(getLayoutOrigin().getNegated());
			constraint = getConstraintFor(r);
			command.add(createAddCommand(childPart,
			translateToModelConstraint(constraint)));
	}

		 */
	
	
	/**
	 * @see AbstractEditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	/*
	public EditPart getTargetEditPart(Request request)
	{
		if (REQ_CREATE.equals(request.getType()))
			return getHost();
		if (REQ_ADD.equals(request.getType()))
			return getHost();
		if (REQ_MOVE.equals(request.getType())) {
			//System.out.println(" REQ_MOVE ");
			return getHost();
		}
		return super.getTargetEditPart(request);
	}*/
	
}
