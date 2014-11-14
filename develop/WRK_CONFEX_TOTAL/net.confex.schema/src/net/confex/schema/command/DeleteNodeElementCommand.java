// 
/*
 * Created on Jul 17, 2004
 */
package net.confex.schema.command;

import java.util.ArrayList;
import java.util.List;

import net.confex.schema.model.IModelElementContainer;
import net.confex.schema.model.ModelConnection;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.Schema;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;



/**
 * Command to delete tables from the container
 * 
 * @author Roman Eremeev
 */
public class DeleteNodeElementCommand extends Command {

	private NodeElement element;
	private IModelElementContainer container;
	//private int index = -1;
	private List out_connections = new ArrayList();
	private List in_connections = new ArrayList();
	private Rectangle bounds;
	
	/**
	 * Удаляем все связи входящие и выходящие из элемента
	 * @param t
	 */
	private void deleteRelationships(NodeElement t) {
		this.out_connections.addAll(t.getOutConnections());
		//for all relationships where current element is foreign key
		for (int i = 0; i < out_connections.size(); i++) {
			ModelConnection out_conn = (ModelConnection) out_connections.get(i);
			out_conn.getInElement().removeInConnections(out_conn);
			t.removeOutConnections(out_conn);
		}
		//for all relationships where current element is primary key
		this.in_connections.addAll(t.getInConnections());
		for (int i = 0; i < in_connections.size(); i++) {
			ModelConnection in_conn = (ModelConnection) in_connections.get(i);
			in_conn.getOutElement().removeOutConnections(in_conn);
			t.removeInConnections(in_conn);
		}
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		primExecute();
	}

	
	/**
	 * Invokes the execution of this command.
	 */
	protected void primExecute() {
		deleteRelationships(element);
		//index = element.getId();
		container.removeModelElement(element);
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo()	{
		primExecute();
	}

	
	private void restoreRelationships()	{
		for (int i = 0; i < out_connections.size(); i++) {
			ModelConnection out_conn = (ModelConnection) out_connections.get(i);
			out_conn.getOutElement().addOutConnections(out_conn);
			out_conn.getInElement().addInConnections(out_conn);
		}
		out_connections.clear();
		for (int i = 0; i < in_connections.size(); i++) {
			ModelConnection in_conn = (ModelConnection) in_connections.get(i);
			in_conn.getOutElement().addOutConnections(in_conn);
			in_conn.getInElement().addInConnections(in_conn);
		}
		in_connections.clear();
	}

	
	/**
	 * Sets the child to the passed Table
	 * 
	 * @param a
	 *            the child
	 */
	public void setNodeElement(NodeElement a) {
		element = a;
	}

	
	/**
	 * Sets the parent to the passed Schema
	 * 
	 * @param sa
	 *            the parent
	 */
	public void setContainer(IModelElementContainer sa) {
		container = sa;
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo()	{
		container.addModelElement(element); //, index);
		restoreRelationships();
		element.modifyBounds(bounds);
	}

	
	/**
	 * Sets the original bounds for the element so that these can be restored
	 */
	public void setOriginalBounds(Rectangle bounds)	{
		this.bounds = bounds;
	}

}

