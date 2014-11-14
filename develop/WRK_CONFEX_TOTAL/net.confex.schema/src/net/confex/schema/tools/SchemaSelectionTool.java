package net.confex.schema.tools;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.SelectionTool;

public class SchemaSelectionTool extends SelectionTool {

	
	/**
	 * Default constructor.
	 */
	public SchemaSelectionTool() { 
		super();
	}

	
	/**
	 * Returns the current x, y position of the mouse cursor.
	 * @return the mouse location
	 */
	public Point getLocation() {
		return new Point(getCurrentInput().getMouseLocation());
		
	}

	
	/**
	 * Возвращает текущий контроллер 
	 * @return
	 */
	public EditPart getTargetEditPart() {
		return super.getTargetEditPart();
	}


}
