package net.confex.schema.part;

import java.beans.PropertyChangeEvent;

import net.confex.schema.command.ActiveElementDirectEditCommand;
import net.confex.schema.directedit.ActiveElementDirectEditPolicy;
import net.confex.schema.directedit.ActiveElementPropertyDialog;
import net.confex.schema.directedit.IElementPropertyDialog;
import net.confex.schema.figures.ActiveElementFigure;
import net.confex.schema.figures.IActiveElementFigure;
import net.confex.schema.model.ActiveElement;
import net.confex.schema.model.NodeElement;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.graphics.Color;


public class ActiveElementPart extends NodeElementPart {
	
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

	
	//******************* Model related methods *********************/

	/**
	 * Returns the Table model object represented by this EditPart
	 */
	public ActiveElement getActiveElement()	{
		return (ActiveElement) getModel();
	}

	
	//******************* Editing related methods *********************/

	/**
	 * Creates edit policies and associates these with roles
	 */
	protected void createEditPolicies()	{
		super.createEditPolicies();
		//installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeComponentEditPolicy());
		//installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ModelNodeEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ActiveElementDirectEditPolicy());
	}
	

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)	{

		String property = evt.getPropertyName();

		if (ActiveElement.ACTIVE_STATE.equals(property))	{
			handleActiveStateChange(evt);
		} else {
			super.propertyChange(evt);
		}
	}

	
	//******************* Listener related methods *********************/
	
	/**
	 * handles change in Active State
	 */
	protected void handleActiveStateChange(PropertyChangeEvent evt) {
		drawState();
		refreshVisuals();
		immediateRepaint();
	}

	
	//******************* Layout related methods *********************/

	/**
	 * Creates a figure which represents the table
	 */
	protected IFigure createFigure(){
		ActiveElement element = getActiveElement();
		ActiveElementFigure figure = new ActiveElementFigure(element.getText());
		figure.setForegroundColor(new Color(null, element.getForeColor()));
		figure.setBackgroundColor(new Color(null, element.getBackColor()));
		figure.setToolTipText(element.getTooltipText());
		figure.setLabelAlignment(PositionConstants.LEFT);
		figure.setIcon(element.getImage());
		figure.setOpaque(true);
		if(element.getBounds()==null) {
			figure.setBounds(new Rectangle(0,0,10,20));
			System.err.println("!!! new Rectangle(0,0,10,20)");
		} else {
			figure.setBounds(element.getBounds());
			setFigure(figure);
			drawState();
		}
		return figure;
	}

	
	private void drawState() {
		if( getFigure() instanceof IActiveElementFigure ) {
			IActiveElementFigure a_figure = (IActiveElementFigure) getFigure();
			if( getActiveElement().getActiveState().equals(ActiveElement.ACTIVE)) {
				a_figure.drawActiveState();
			} else if( getActiveElement().getActiveState().equals(ActiveElement.PASSIVE)) {
				a_figure.drawPassiveState();
			} else if( getActiveElement().getActiveState().equals(ActiveElement.ERROR)) {
				a_figure.drawErrorState();
			} else if( getActiveElement().getActiveState().equals(ActiveElement.OK)) {
				a_figure.drawOkState();	
			}  
		}
	}
	
	
	/**
	 * Reset the layout constraint, and revalidate the content pane
	 */
	protected void refreshVisuals()	{
		super.refreshVisuals();
		getFigure().repaint();
	}

	
	/**
	 * Немедленно перерисовать элемент
	 * (на самом деле перерисовывается родительский элемент).
	 */
	protected void immediateRepaint() {
		if(this.getParent() instanceof IModelElementContainerPart) {
			IModelElementContainerPart container_part = (IModelElementContainerPart) getParent();
			container_part.immediateRepaint();
		}
	}
	
	
	//protected ActiveElementPropertyDialog dialog = null;
	
	/**
	 * Редактирование свойств через диалог свойств
	 *
	 */
	protected void performDirectEdit()	{
		//if (property_dialog == null) {
		//	property_dialog = new ActiveElementPropertyDialog();
		//}
		//-- создаем копию элемента и запускаем диалог свойств для него
		ActiveElement element = getActiveElement();
		IElementPropertyDialog property_dialog = element.getPropertyDialog();
		property_dialog.setEditPart(this);
		property_dialog.show();
	}

	
	/**
	 * Создаем новую команду
	 * заносим её в стек команд и выполняем
	 * (вызывается из диалога)
	 * @param new_element
	 */
	public void CommitDirectEditChanges(NodeElement new_element) {
		if(new_element instanceof ActiveElement) {
			//-- заносим команду в стек команд и выполняем
			CommandStack stack = this.getViewer().getEditDomain().getCommandStack();
			stack.execute(this.getDirectEditCommand((ActiveElement)new_element));
		} else {
			super.CommitDirectEditChanges(new_element);
		}
	}
	
	
	/**
	 * Создаем комманду для прямого редактирования
	 * @return
	 */
	protected Command getDirectEditCommand(ActiveElement element) {
		ActiveElementDirectEditCommand command = new ActiveElementDirectEditCommand();
		command.setActiveElement(this.getActiveElement());
		command.setNewActiveElementLike(element);
		
		return command;
	}
	
	
	
}
