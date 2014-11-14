package net.confex.schema.part;

import net.confex.schema.directedit.LabelEditManager;
import net.confex.schema.directedit.StickyLabelCellEditorLocator;
import net.confex.schema.figures.StickyNoteFigure;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.StickyNote;
import net.confex.schema.policy.LabelDirectEditPolicy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Color;



public class LabelElementPart extends NodeElementPart {

	protected DirectEditManager manager;
	
	
	//******************* Life-cycle related methods *********************/

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate()	{
		super.activate();
	}

	
	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate()	{
		super.deactivate();
	}
	
	//******************* Layout related methods *********************/

	
	protected void createEditPolicies(){
		super.createEditPolicies();
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new LabelDirectEditPolicy());
		//installEditPolicy(EditPolicy.COMPONENT_ROLE,new LogicLabelEditPolicy()); 
	}
	
	
	/**
	 * Creates a figure which represents the table
	 */
	protected IFigure createFigure() {
		NodeElement element = getNodeElement();
		StickyNoteFigure figure = new StickyNoteFigure();
		figure.setText(element.getText());
		figure.setForegroundColor(new Color(null, element.getForeColor()));
		figure.setBackgroundColor(new Color(null, element.getBackColor()));
		figure.setToolTipText(element.getTooltipText());
		figure.setOpaque(true);
		if(element.getBounds()==null) {
			figure.setBounds(new Rectangle(0,0,10,20));
			System.err.println("!!! new Rectangle(0,0,10,20)");
		} else {
			figure.setBounds(element.getBounds());
		}
		return figure;
	}
	
	
	private StickyNote getLabel(){
		return (StickyNote)getModel();
	}

	
	protected void performDirectEdit(){
		if(manager == null)
			manager = new LabelEditManager(this, new StickyLabelCellEditorLocator((StickyNoteFigure)getFigure()));
		manager.show();
	}

	
	protected void refreshVisuals() {
		((StickyNoteFigure)getFigure()).setText(getLabel().getText());
		super.refreshVisuals();
	}

}
	
	

