package net.confex.schema.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import net.confex.schema.command.NodeElementDirectEditCommand;
import net.confex.schema.directedit.IElementPropertyDialog;
import net.confex.schema.editor.SchemaImages;
import net.confex.schema.figures.IModelElementFigure;
import net.confex.schema.figures.NodeElementFigure;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.PropertyAwareObject;
import net.confex.schema.policy.ModelElementDirectEditPolicy;
import net.confex.schema.policy.ModelNodeEditPolicy;
import net.confex.schema.policy.NodeComponentEditPolicy;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.swt.graphics.Color;


/**
 * Represents the editable/resizable label which can have some text.
 * 
 * @author Roman Eremeev
 */

public class NodeElementPart extends ModelElementPart implements NodeEditPart {
	
	//protected DirectEditManager manager;
	//protected NodeElementPropertyDialog property_dialog;


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
	public NodeElement getNodeElement()	{
		return (NodeElement) getModel();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections() {
		return getNodeElement().getOutConnections();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections() {
		return getNodeElement().getInConnections();
	}

	
	//******************* Editing related methods *********************/

	/**
	 * Creates edit policies and associates these with roles
	 */
	protected void createEditPolicies()	{
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeComponentEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ModelNodeEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ModelElementDirectEditPolicy());
		//SelectionEditPolicy
	}

	
	//******************* Direct editing related methods *********************/

	/**
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request request)	{
		if (request.getType() == RequestConstants.REQ_OPEN) {
			getNodeElement().doDefaultAction(this);
		}
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			if (request instanceof DirectEditRequest
					&& !directEditHitTest(((DirectEditRequest) request).getLocation().getCopy()))
				return;
			performDirectEdit();
		}
		if (request.getType() == RequestConstants.REQ_SELECTION) {
			//!!! 
			System.out.println("REQ_SELECTION");
		}
	}

	
	/**
	 * Проверка попадания точки события в область элемента
	 * @param requestLoc 
	 * @return
	 */
	private boolean directEditHitTest(Point requestLoc)	{
		IFigure figure = (IFigure) getFigure();
		figure.translateToRelative(requestLoc);
		if (figure.containsPoint(requestLoc))
			return true;
		return false;
	}

	
	/**
	 * Выполнение редактирования текста
	 * используется TextCellEditor класс.
	 *
	 */
	protected void performDirectEdit()	{
		//if (property_dialog == null) {
		//	property_dialog = new ActiveElementPropertyDialog();
		//}
		//-- создаем копию элемента и запускаем диалог свойств для него
		NodeElement element = getNodeElement();
		IElementPropertyDialog property_dialog = element.getPropertyDialog();
		
		property_dialog.setEditPart(this);
		property_dialog.show();
		/*
		if (manager == null) {
			ValidationEnabledGraphicalViewer viewer = (ValidationEnabledGraphicalViewer) getViewer();
			ValidationMessageHandler handler = viewer.getValidationHandler();

			IModelElementFigure mylabelFigure = (IModelElementFigure) getFigure();
			manager = new ExtendedDirectEditManager(this, TextCellEditor.class
					, new LabelCellEditorLocator(mylabelFigure),
					mylabelFigure, new TextCellEditorValidator(handler));
		}
		manager.show();
		*/
	}

	
	/**
	 * Создаем новую команду
	 * заносим её в стек команд и выполняем
	 * (вызывается из диалога)
	 * @param new_element
	 */
	public void CommitDirectEditChanges(NodeElement new_element) {
		//-- заносим команду в стек команд и выполняем
		CommandStack stack = this.getViewer().getEditDomain().getCommandStack();
		stack.execute(this.getDirectEditCommand(new_element));
	}

	
	/**
	 * Создаем комманду для прямого редактирования
	 * @return
	 */
	protected Command getDirectEditCommand(NodeElement element) {
		NodeElementDirectEditCommand command = new NodeElementDirectEditCommand();
		command.setNodeElement(this.getNodeElement());
		command.setNewNodeElementLike(element);
		
		return command;
	}
	
	
	
	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)	{

		String property = evt.getPropertyName();

		if (PropertyAwareObject.BOUNDS.equals(property))	{
			handleBoundsChange(evt);
		} else if (NodeElement.WIDTH.equals(property)) {
			this.handleBoundsChange(evt);
		} else if (NodeElement.HEIGHT.equals(property)) {
			this.handleBoundsChange(evt);
		} else if (NodeElement.X_POS.equals(property)) {
			this.handleBoundsChange(evt);
		} else if (NodeElement.Y_POS.equals(property)) {
			this.handleBoundsChange(evt);
		} else {
			super.propertyChange(evt);
		}
	}

	
	//******************* Listener related methods *********************/
	
	/**
	 * handles change in bounds, to be overridden by subclass
	 */
	protected void handleBoundsChange(PropertyChangeEvent evt) {
		IModelElementFigure figure = (IModelElementFigure) getFigure();
		figure.setVisible(true);
		Rectangle constraint = (Rectangle) evt.getNewValue();
		if(getParent() instanceof IModelElementContainerPart) {
			((IModelElementContainerPart) getParent()).setLayoutConstraint(this, figure, constraint);
		} else {
			System.err.println(" Container not a IModelElementContainerPart!!!");
		}
		refreshVisuals();
	}

	
	/**
	 * Устанавливает размеры фигуры по размерам сохраненным в модели
	 */
	protected void updateBoundsFromModel() {
		Rectangle constraint = (Rectangle) this.getNodeElement().getBounds();
		if(getParent() instanceof IModelElementContainerPart) {
			/*((IModelElementContainerPart) getParent()).*/
			if(figure.getParent()!=null) {
				setLayoutConstraint(this, figure, constraint);
			}
		} else {
			System.err.println(" Container not a IModelElementContainerPart!!!");
		}
		refreshVisuals();
	}
	
	protected void fireSelectionChanged() {
		// TODO Auto-generated method stub
		super.fireSelectionChanged();
	}  
	
	/**
	 * Called when the selected state of an EditPart has changed. Focus changes also result
	 * in this method being called.
	 * @param editpart the part whose selection was changed
	 * @see EditPart#getSelected()
	 */
	//void selecte
	//}
	
	//******************* Layout related methods *********************/

	/**
	 * Creates a figure which represents the table
	 */
	protected IFigure createFigure(){
		NodeElement element = getNodeElement();
		NodeElementFigure figure = new NodeElementFigure(element.getText());
		figure.setForegroundColor(new Color(null, element.getForeColor()));
		figure.setBackgroundColor(new Color(null, element.getBackColor()));
		figure.setToolTipText(element.getTooltipText());
		figure.setLabelAlignment(PositionConstants.LEFT);
		figure.setIcon(element.getImage());
		if(element.getBounds()==null) {
			figure.setBounds(new Rectangle(0,0,10,20));
			System.err.println("!!! new Rectangle(0,0,10,20)");
		} else {
			figure.setBounds(element.getBounds());
		}
		return figure;
	}

	
	/**
	 * Reset the layout constraint, and revalidate the content pane
	 */
	protected void refreshVisuals()	{
		super.refreshVisuals();
		
		/*
		//IModelElementFigure figure = (IModelElementFigure) getFigure();
		Figure figure = (Figure) getFigure();
		
		Point location = figure.getLocation();
		Rectangle constraint = new Rectangle(location.x, location.y, -1, -1);
		
		if(getParent() instanceof IModelElementContainerPart) {
			((IModelElementContainerPart) getParent()).setLayoutConstraint(this, figure, constraint);
		} else {
			System.err.println(" Container not a IModelElementContainerPart!!!");
		}*/
	}

	
    /**
     * Сделать figure и все дочерние (соединения) Visible
     * @param visible - true все видимы
     */
    public void setAllVisible(boolean visible) {
        //this.getFigure().setVisible(visible);
        super.setAllVisible(visible);
        List src_connections = this.getSourceConnections();
        if(src_connections != null) {
	    	for (int i = 0; i < src_connections.size(); i++) {
	    		if(src_connections.get(i) instanceof ModelConnectionPart) {
	    			ModelConnectionPart editPart = (ModelConnectionPart)src_connections.get(i);
	    			editPart.getFigure().setVisible(visible);
	    		}
	    	}
        }
    }

	
	/**
	 * @see NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection){
		return new ChopboxAnchor(getFigure());
		//return new TopAnchor(getFigure());
	}

	
	/**
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
		//return new TopAnchor(getFigure());
	}

	
	/**
	 * @see NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection){
		return new ChopboxAnchor(getFigure());
		//return new BottomAnchor(getFigure());
	}

	
	/**
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
		//return new BottomAnchor(getFigure());
	}

	
	
	//******************* Miscellaneous stuff *********************/

    protected void addChild(EditPart child, int index) {
    	super.addChild(child, index);
    }

	

}
