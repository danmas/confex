package net.confex.schema.command;

import java.util.List;

import net.confex.schema.model.ModelConnection;
import net.confex.schema.model.NodeElement;

import org.eclipse.gef.commands.Command;



public class ReconnectOutCommand  extends Command {

	/** source Table * */
	protected NodeElement source_out_element;
	/** target Table * */
	protected NodeElement target_in_element;
	/** Relationship between source and target * */
	protected ModelConnection connection;
	/** previous source prior to command execution * */
	protected NodeElement old_source_out_element;

	/**
	 * Makes sure that primary key doesn't reconnect to itself or try to create
	 * a connection which already exists
	 * ----
	 * Если не произойдет самоконнекция или не будет создана коннекция которая уже существует
	 * то можновыполнить
	 */
	public boolean canExecute() {
		boolean returnVal = true;

		NodeElement in_element = connection.getInElement();

		//cannot connect to itself
		if (in_element.equals(source_out_element)) {
			returnVal = false;
		} else {
			List relationships = source_out_element.getOutConnections();
			for (int i = 0; i < relationships.size(); i++)	{
				ModelConnection connection = ((ModelConnection) (relationships.get(i)));
				if (connection.getInElement().equals(target_in_element)
						&& connection.getOutElement().equals(source_out_element))	{
					returnVal = false;
					break;
				}
			}
		}
		return returnVal;
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()	{
		if (source_out_element != null)	{
			//-- удаляем из старого
			old_source_out_element.removeOutConnections(connection);
			//-- вставляем в новый
			connection.setOutElement(source_out_element);
			source_out_element.addOutConnections(connection);
		}
	}

	
	/**
	 * @return Returns the source_out_element.
	 */
	//public NodeElement getSourceOutKey() {
	//	return source_out_element;
	//}

	
	/**
	 * @param source_out_element
	 *            The source_out_element to set.
	 */
	public void setSourceOutElement(NodeElement sourceOutKey) {
		this.source_out_element = sourceOutKey;
	}

	
	/**
	 * @return Returns the target_in_element.
	 */
	//public NodeElement getTargetPrimaryKey() {
	//	return target_in_element;
	//}

	
	/**
	 * @param target_in_element
	 *            The target_in_element to set.
	 */
	//public void setTargetPrimaryKey(NodeElement targetPrimaryKey) {
	//	this.target_in_element = targetPrimaryKey;
	//}

	
	/**
	 * @return Returns the connection.
	 */
	//public ModelConnection getRelationship() {
	//	return connection;
	//}

	
	/**
	 * Sets the Relationship associated with this
	 * 
	 * @param connection
	 *            the Relationship
	 */
	public void setConnection(ModelConnection relationship) {
		this.connection = relationship;
		target_in_element = relationship.getInElement();
		old_source_out_element = relationship.getOutElement();
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		source_out_element.removeOutConnections(connection);
		connection.setOutElement(old_source_out_element);
		old_source_out_element.addOutConnections(connection);
	}
}