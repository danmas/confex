package net.confex.schema.command;

import java.util.List;

import net.confex.schema.model.ModelConnection;
import net.confex.schema.model.NodeElement;

import org.eclipse.gef.commands.Command;



public class ReconnectInCommand  extends Command {

	/** source NodeElement * */
	protected NodeElement out_element;
	/** target NodeElement * */
	protected NodeElement target_element;
	/** Relationship between source and target * */
	protected ModelConnection connection;
	/** previous source prior to command execution * */
	protected NodeElement old_target_element;

	
	/**
	 * Makes sure that foreign key doesn't reconnect to itself or try to create
	 * a connection which already exists
	 * ----
	 * Если не произойдет самоконнекция или не будет создана коннекция которая уже существует
	 * то можновыполнить
	 */
	public boolean canExecute()	{

		boolean returnVal = true;

		NodeElement src_element = connection.getOutElement();

		if (src_element.equals(target_element)) {
			returnVal = false;
		}	else	{
			List relationships = target_element.getInConnections();
			for (int i = 0; i < relationships.size(); i++)			{
				ModelConnection connection = ((ModelConnection) (relationships.get(i)));

				if (connection.getOutElement().equals(out_element)
						&& connection.getInElement().equals(target_element))	{
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
		if (target_element != null)		{
			old_target_element.removeInConnections(connection);
			connection.setInElement(target_element);
			target_element.addInConnections(connection);
		}
	}

	
	/**
	 * @return Returns the out_element.
	 */
	//public NodeElement getSourceForeignKey()	{
	//	return out_element;
	//}

	
	/**
	 * @param out_element
	 *            The out_element to set.
	 */
	public void setOutElement(NodeElement source_element)	{
		this.out_element = source_element;
	}

	
	/**
	 * @return Returns the target_element.
	 */
	//public NodeElement getTargetPrimaryKey()	{
	//	return target_element;
	//}
	

	/**
	 * @param target_element
	 *            The target_element to set.
	 */
	public void setInElement(NodeElement targetPrimaryKey)	{
		this.target_element = targetPrimaryKey;
	}
	

	/**
	 * @return Returns the connection.
	 */
	public ModelConnection getConnection()	{
		return connection;
	}

	
	/**
	 * Sets the ModelConnection associated with this
	 * 
	 * @param connection
	 *            the ModelConnection
	 */
	public void setConnection(ModelConnection connection)	{
		this.connection = connection;
		old_target_element = connection.getInElement();
		out_element = connection.getOutElement();
	}

	
	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo()	{
		target_element.removeInConnections(connection);
		connection.setInElement(old_target_element);
		old_target_element.addInConnections(connection);
	}
}
