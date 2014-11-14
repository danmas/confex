package net.confex.schema.command;

import java.util.List;

import net.confex.schema.model.ModelConnection;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.Schema;
import net.confex.schema.model.SimpleContainer;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;



public class ConnectionCreateCommand  extends Command {

	/** The connection between primary and foreign key tables * */
	protected ModelConnection connection;
	/** The source (foreign key) table * */
	protected NodeElement out_element;
	/** The target (primary key) table * */
	protected NodeElement in_element;

	private Schema schema;

	
	public ConnectionCreateCommand(Schema schema) {
		super();
		this.schema = schema;
	}
	
	
	/**
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute()	{
		boolean returnValue = true;
		if(out_element == null) {
			out_element = connection.getOutElement();
		}
		if (out_element.equals(in_element))	{
			returnValue = false;
		}else{
			if (in_element == null)	{
				//in_element = connection.getInElement();
				return false;
			}else{
				// Check for existence of connection already
				List relationships = in_element.getInConnections();
				for (int i = 0; i < relationships.size(); i++)	{
					ModelConnection currentConnection = (ModelConnection) relationships.get(i);
					//FIXME !!!My Что здесь означает equals !?
					if (currentConnection.getOutElement().equals(out_element))	{
						returnValue = false;
						break;
					}
				}
			}
		}
		System.out.println("canExecute() = "+returnValue);
		return returnValue;
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		connection = new ModelConnection(schema,false,out_element, in_element);
	}

	
	/**
	 * @return Returns the out_element.
	 */
	public NodeElement getOutElement(){
		return out_element;
	}

	
	/**
	 * @return Returns the in_element.
	 */
	public NodeElement getInElement(){
		return in_element;
	}

	
	/**
	 * Returns the Relationship between the primary and foreign tables
	 * 
	 * @return the transistion
	 */
	public ModelConnection getRelationship() {
		return connection;
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		out_element.addOutConnections(connection);
		in_element.addInConnections(connection);
	}

	
	/**
	 * @param out_element
	 *            The out_element to set.
	 */
	public void setOutElement(NodeElement out_element) {
		this.out_element = out_element;
	}

	
	/**
	 * @param in_element
	 *            The in_element to set.
	 */
	public void setInElement(NodeElement in_element) {
		this.in_element = in_element;
	}

	
	/**
	 * @param connection
	 *            The connection to set.
	 */
	public void setConnection(ModelConnection connection) {
		this.connection = connection;
	}

	
	/**
	 * Undo version of command
	 */
	public void undo()	{
		out_element.removeOutConnections(connection);
		in_element.removeInConnections(connection);
	}

}
