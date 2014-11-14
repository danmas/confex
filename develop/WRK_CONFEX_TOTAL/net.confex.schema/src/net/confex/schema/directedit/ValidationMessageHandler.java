/*
 * Created on Jul 25, 2004
 */
package net.confex.schema.directedit;

/**
 * Represents interface for outputting validation error messages to some widget
 * @author Eremeev Roman
 */
public interface ValidationMessageHandler
{

	public void setMessageText(String text);

	/**
	 * Resets so that the validation message is no longer shown
	 */
	public void reset();
}