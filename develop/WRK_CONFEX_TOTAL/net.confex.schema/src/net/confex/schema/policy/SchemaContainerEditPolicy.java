/*
 * Created on Jul 15, 2004
 */
package net.confex.schema.policy;

import net.confex.schema.command.AddElementCommand;
import net.confex.schema.command.PasteCommand;
import net.confex.schema.figures.SchemaFigure;
import net.confex.schema.model.Schema;
import net.confex.schema.part.SchemaDiagramPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;



/**
 * Handles creation of new Shapes using drag and drop or point and click from the palette
 * @author Eremeev Roman
 */
public class SchemaContainerEditPolicy extends ContainerEditPolicy
{

	/**
	 * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getAddCommand(org.eclipse.gef.requests.GroupRequest)
	 * вызывается в ContainerEditPolicy.getCommand()
	 * тип запроса - REQ_ADD   
	 */
	protected Command getAddCommand(GroupRequest request) {
		//EditPart host = getTargetEditPart(request);
		return null;
	}
	

	/**
	 * @see ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 * Вызывается каждый раз при движении мыши по контейнеру. Если возвращает null то курсор будет "disable insert"
	 * вызывается в ContainerEditPolicy.getCommand()
	 * тип запроса - REQ_CREATE   
	 */
	protected Command getCreateCommand(CreateRequest request) {
		System.out.println(" ---> shapeAddCommand.getCreateCommand()");
		Object newObject = request.getNewObject();
		EditPart host = getTargetEditPart(request);
		SchemaDiagramPart schemaPart = (SchemaDiagramPart)getHost();
		Schema schema = schemaPart.getSchema();
		Rectangle rect;
		if(request.getSize()==null) {
			rect = new Rectangle(request.getLocation().x,request.getLocation().y,0,0);
		} else {
			//-- сюда попадет когда начнется выделение области для создания элемента
			rect = new Rectangle(request.getLocation().x,request.getLocation().y,request.getSize().width,request.getSize().height);
		}
		//System.err.println("--  "+request.getLocation().x+","+request.getLocation().y);
		SchemaFigure fig2=(SchemaFigure)schemaPart.getFigure();
		//IFigure fig2 = fig.getc .getc .getContentsPane();
		if(fig2.getParent() != null) {
	 		//System.err.println(" old rect.x ="+ rect.x +" rect.y ="+ rect.y);
	 		fig2.translateFromParent(rect);
	 		//System.err.println(" -2- rect.x ="+ rect.x +" rect.y ="+ rect.y);
	 		fig2.translateToRelative(rect);
			//System.err.println(" new rect.x ="+ rect.x +" rect.y ="+ rect.y);
		}
		
		if(newObject instanceof PasteCommand ) {
			PasteCommand paste_command = (PasteCommand) newObject;
			paste_command.setSchema(schema);
			paste_command.setHostEditPart(this.getHost());
			paste_command.setContainer(schema);
			paste_command.setLocation(rect);
			return (PasteCommand) newObject;
		}
		AddElementCommand shapeAddCommand = new AddElementCommand(request.getNewObject(),schema	,rect);
		if(shapeAddCommand==null) {
			System.out.println("shapeAddCommand is NULL");
		}
		return shapeAddCommand;
	}

	
	/**
	 * @see AbstractEditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
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
	}

}