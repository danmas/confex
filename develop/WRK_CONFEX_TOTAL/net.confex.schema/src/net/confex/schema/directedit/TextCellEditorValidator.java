package net.confex.schema.directedit;

import org.eclipse.jface.viewers.ICellEditorValidator;

	/**
	 * ICellValidator to validate direct edit values in the NodeElement label. 
	 * Collaborates with an instance of ValidationMessageHandler
	 * @author Roman Eremeev
	 */
	public class TextCellEditorValidator implements ICellEditorValidator {

		private ValidationMessageHandler handler;

		/**
		 * @param validationMessageHandler
		 *            the validation message handler to pass error information to
		 */
		public TextCellEditorValidator(ValidationMessageHandler validationMessageHandler)
		{
			this.handler = validationMessageHandler;
		}

		
		/**
		 * @param validation
		 *            of column type
		 * @return the error message if an error has occurred, otherwise null
		 */
		public String isValid(Object value) {
			String name = (String) value;

			if (name.length() == 0)	{
				String text = "Table name should include at least one character";
				return setMessageText(text);
			}
			unsetMessageText();
			return null;
		}

		
		private String unsetMessageText(){
			handler.reset();
			return null;
		}

		
		private String setMessageText(String text) {
			handler.setMessageText(text);
			return text;
		}

	}