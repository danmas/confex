/*
 * Created on Jul 13, 2004
 */
package net.confex.schema.factory;

import net.confex.schema.model.ActiveElement;
import net.confex.schema.model.ModelConnection;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.Schema;
import net.confex.schema.model.SimpleContainer;
import net.confex.schema.model.StickyNote;
import net.confex.schema.part.ActiveElementPart;
import net.confex.schema.part.LabelElementPart;
import net.confex.schema.part.ModelConnectionPart;
import net.confex.schema.part.NodeElementPart;
import net.confex.schema.part.SchemaDiagramPart;
import net.confex.schema.part.SimpleContainerPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;



/**
 * Edit part factory for creating EditPart instances as delegates for model objects
 * 
 * @author Phil Zoio
 */
public class SchemaEditPartFactory implements EditPartFactory
{
	
	/**
	 * !!! Важно чтобы дочерние класс стояли выше родительских
	 */
	public EditPart createEditPart(EditPart context, Object model)	{
		EditPart part = null;
		if (model instanceof Schema)
			part = new SchemaDiagramPart();
		else if (model instanceof SimpleContainer)
			part = new SimpleContainerPart();
		else if (model instanceof ModelConnection)
			part = new ModelConnectionPart();
		else if (model instanceof StickyNote)
			part = new LabelElementPart();
		else if (model instanceof ActiveElement)
			part = new ActiveElementPart(); 
		else if (model instanceof NodeElement)
			part = new NodeElementPart();
		part.setModel(model);
		return part;
	}
}