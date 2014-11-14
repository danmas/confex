
package net.confex.schema.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import net.confex.schema.directedit.IElementPropertyDialog;
import net.confex.schema.figures.EditableLabel;
import net.confex.schema.figures.SimpleContainerFigure;
import net.confex.schema.layout.MyGridLayout;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.SimpleContainer;
import net.confex.schema.model.StateContainer;
import net.confex.schema.policy.SchemaXYLayoutPolicy;
import net.confex.schema.policy.SimpleContainerContainerEditPolicy;

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Color;


public class SimpleContainerPart extends NodeElementPart implements IModelElementContainerPart { 
	
	protected DirectEditManager manager;

	
	//******************* Life-cycle related methods *********************/

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		super.activate();
	}

	
	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
	}

	
	//******************* Model related methods *********************/

	/**
	 * Returns the SimpleContainer model object represented by this EditPart
	 */
	public SimpleContainer getSimpleContainer()	{
		return (SimpleContainer) getModel();
	}

	
	/**
	 * @return the children Model objects as a new ArrayList
	 */
	protected List getModelChildren()	{
		return getSimpleContainer().getChildren();
	}

	
	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections()	{
		return getSimpleContainer().getOutConnections();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections()	{
		return getSimpleContainer().getInConnections();
	}

	
	
	/**
	 * Сделать все figure и все дочерние Visible
	 * @param vis_flag - true все видимы
	 */
	public void setAllVisible(boolean visible) {
		if(children != null) {
			for(int i=0; i<children.size();i++) {
				((ModelElementPart)children.get(i)).setAllVisible(visible);
			}
		}
		this.getFigure().setVisible(visible);
	}
	
	
	public void changeState(String state) {
   		if(getModel() instanceof StateContainer) {
   			//-- если модель позволяет применить состояние state
			((StateContainer)getModel()).modifyState(state);
   		}
	}
	
	
	/**
	 * If modified, sets states and fires off event notification
   	 * создаем новую фигуру чтобы она была наверху остальных !!!
	 */
	 public void modifyState(String state)	{
		//SimpleContainerFigure containerFigure = (SimpleContainerFigure) create_figure();
		
   		if(getModel() instanceof StateContainer) {
   			//-- если модель позволяет применить состояние state
			SimpleContainerFigure figure = (SimpleContainerFigure)this.getFigure();
			if(state.equals(StateContainer.COMPACT_STATE)) {
				//-- делаем невидимыми все дочерние (только те у которых есть EditPart)
				setAllVisible(false);
				figure.modifyState(state);
				//-- Переопределяем размеры GridData
				figure.getGridData().widthHint = 100;
				figure.getGridData().heightHint =50;
			} else {
				setAllVisible(true);
				figure.modifyState(state);
				int w = ((StateContainer)getModel()).getBounds().width;
				int h = ((StateContainer)getModel()).getBounds().height;
				//-- Переопределяем размеры GridData
				figure.getGridData().widthHint = w;
				figure.getGridData().heightHint = h;
			}
			//-- Устанавливает размеры фигуры по размерам сохраненным в модели
			updateBoundsFromModel();
   		}
   		//return containerFigure;
	}

	 
	//******************* Editing related methods *********************/

	/**
	 * Creates edit policies and associates these with roles
	 */
	protected void createEditPolicies()
	{
		super.createEditPolicies();
		//installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new SimpleContainerNodeEditPolicy());
		//installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ModelNodeEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new SchemaXYLayoutPolicy());  // SimpleContainerLayoutEditPolicy2());
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, null);  
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new SimpleContainerContainerEditPolicy());
		//installEditPolicy(EditPolicy.COMPONENT_ROLE, new SimpleContainerEditPolicy());
		//installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new SimpleContainerDirectEditPolicy());
	}

	
	//******************* Direct editing related methods *********************/

	/**
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	public void performRequest(Request request)	{
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)	{
			if (request instanceof DirectEditRequest
					&& !directEditHitTest(((DirectEditRequest) request).getLocation().getCopy()))
				return;
			performDirectEdit();
		}
	}
	 */

	
	private boolean directEditHitTest(Point requestLoc)	{
		SimpleContainerFigure figure = (SimpleContainerFigure) getFigure();
		EditableLabel nameLabel = figure.getNameLabel();
		nameLabel.translateToRelative(requestLoc);
		if (nameLabel.containsPoint(requestLoc))
			return true;
		return false;
	}

	
	/**
	 * Редактирование свойств через диалог свойств
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
	}

	
	/*
	protected void performDirectEdit() {
		if (manager == null) {
			ValidationEnabledGraphicalViewer viewer = (ValidationEnabledGraphicalViewer) getViewer();
			ValidationMessageHandler handler = viewer.getValidationHandler();

			SimpleContainerFigure figure = (SimpleContainerFigure) getFigure();
			EditableLabel nameLabel = figure.getNameLabel();
			/ *
			manager = new ExtendedDirectEditManager(this
					, TextCellEditor.class, new LabelCellEditorLocator(nameLabel)
					, nameLabel
					, new SimpleContainerNameCellEditorValidator(handler));
			* /
			return;
		}
		manager.show();
	}*/
	
	
	/**
	 * Sets layout constraint only if XYLayout is active
	 */
	public void setLayoutConstraint(EditPart child, IFigure childFigure, Object constraint)	{
			super.setLayoutConstraint(child, childFigure, constraint);
	}

	
	
	/**
	 * @param handles
	 *            the name change during an edit
	 */
	/*
	public void handleNameChange(String value)	{
		SimpleContainerFigure tableFigure = (SimpleContainerFigure) getFigure();
		EditableLabel label = tableFigure.getNameLabel();
		label.setVisible(false);
		refreshVisuals();
	}*/

	
	/**
	 * Passes on to the delegating layout manager that the layout type has
	 * changed. The delegating layout manager will then decide whether to
	 * delegate layout to the XY or Graph layout
	 */
	//protected void handleChildChange(PropertyChangeEvent evt) {
	//	super.handleChildChange(evt);
	//}

	
	/**
	 * Reverts to existing name in model when exiting from a direct edit
	 * (possibly before a commit which will result in a change in the label
	 * value)
	public void revertNameChange()
	{
		SimpleContainerFigure tableFigure = (SimpleContainerFigure) getFigure();
		EditableLabel label = tableFigure.getNameLabel();
		SimpleContainer container = getSimpleContainer();
		label.setText(container.getText());
		label.setVisible(true);
		refreshVisuals();
	}
	 */


	//******************* Listener related methods *********************/

	
	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)	{
		
		String property = evt.getPropertyName();

		if (StateContainer.CONTAINER_STATE.equals(property)) {
			this.handleStateChange(evt);
		} else {
			super.propertyChange(evt);
		}
	}

	/**
	 * handles change in bounds, to be overridden by subclass
	 */
	protected void handleBoundsChange(PropertyChangeEvent evt) {
		super.handleBoundsChange(evt);
	}
	
	
	/**
	 * Обрабатываем изменение состояния контейнера 
	 * @param evt
	 */
	protected void handleStateChange(PropertyChangeEvent evt) {
		String state = (String) evt.getNewValue();
		this.modifyState(state);
	}
	
	
	/**
	 * Handles change in name when committing a direct edit
	 */
	protected void handleNameChange(PropertyChangeEvent evt) {
		SimpleContainerFigure tableFigure = (SimpleContainerFigure) getFigure();
		EditableLabel label = tableFigure.getNameLabel();
		label.setText(getSimpleContainer().getText());
		label.setVisible(true);
		refreshVisuals();
	}

	
	protected void handleForeColorChange(PropertyChangeEvent evt) {
		super.handleForeColorChange(evt);
	}
	
	
	protected void handleBackColorChange(PropertyChangeEvent evt) {
		super.handleBackColorChange(evt);
	}
	
	
	//******************* Layout related methods *********************/

	/**
	 * Creates a figure only
	 */
	private IFigure create_figure() {
		SimpleContainer container = getSimpleContainer();
		EditableLabel label = new EditableLabel(container.getText());
		SimpleContainerFigure containerFigure = new SimpleContainerFigure(label);
		containerFigure.setBounds(container.getBounds());
		
		containerFigure.setForegroundColor(new Color(null,container.getForeColor()));
		containerFigure.setBackgroundColor(new Color(null,container.getBackColor()));
		
		//-- считаем что каждый контейнер имеет gridDate!!!

		//-- устанавливаем gridData
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
		  		/*| GridData.GRAB_HORIZONTAL*/ );
		gridData.widthHint=150;
		gridData.heightHint=50;
		containerFigure.setGridData(gridData);
		containerFigure.getLayoutManager().setConstraint(containerFigure, gridData);

		//-- set Grid layout 
		MyGridLayout layout = new MyGridLayout();
		layout.numColumns = 2;
		containerFigure.getContentsPane().setLayoutManager(layout);

		
		//GraphXYLayout xyLayoutManager = new GraphXYLayout();
		//containerFigure.setLayoutManager(xyLayoutManager);

		this.setFigure(containerFigure); //-- чтобы сработал метод modifyState() нужно установить figure !!! А можно ли так?

		return containerFigure;
	}
	
	
	/**
	 * Creates a figure and modify by state
	 */
	protected IFigure createFigure() {
		SimpleContainerFigure figure = (SimpleContainerFigure) create_figure();
		
   		if(getModel() instanceof StateContainer) {
   			modifyState(((StateContainer)getModel()).getCurrentState());	
   		}
   		SimpleContainer element = getSimpleContainer();
		figure.setToolTipText(element.getTooltipText());

		return figure;
	}

	
	public IFigure getFigure() {
		if (figure == null)
			setFigure(createFigure());
		return figure;
	}
	

	/**
	 * Переопределяем контекст для вставки новых фигур
	 *  
	 * @return the Content pane for adding or removing child figures
	 */
	public IFigure getContentPane()	{
		SimpleContainerFigure figure = (SimpleContainerFigure) getFigure();
		return figure.getContentsPane();
	}


	/**
	 * Reset the layout constraint, and revalidate the content pane
	*/
	protected void refreshVisuals() {
		//super.refreshVisuals();
		/*
		IModelElementContainerPart parent = (IModelElementContainerPart) getParent();
		Rectangle constraint = new Rectangle(location.x, location.y, -1, -1);
		parent.setLayoutConstraint(this, containerFigure, constraint);
		*/
	}

	
	/**
	 * Пререрисовать все немедленно
	 */
	public void immediateRepaint() {
		getFigure().getUpdateManager().performUpdate();
	}
	
	
	/**
	 * @see NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	//public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
	//	return new ChopboxAnchor(getFigure());
	//}

	
	/**
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	//public ConnectionAnchor getSourceConnectionAnchor(Request request)	{
	//	return new ChopboxAnchor(getFigure());
	//}

	
	/**
	 * @see NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	//public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
	//	return new ChopboxAnchor(getFigure());
	//}

	
	/**
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	//public ConnectionAnchor getTargetConnectionAnchor(Request request) {
	//	return new ChopboxAnchor(getFigure());
	//}

	
	/**
	 * Sets the width of the line when selected
	 */
	public void setSelected(int value) {
		super.setSelected(value);
		SimpleContainerFigure figure = (SimpleContainerFigure) getFigure();
		if (value != EditPart.SELECTED_NONE)
			figure.setSelected(true);
		else
			figure.setSelected(false);
		figure.repaint();
	}
	
	
	//******************* Miscellaneous stuff *********************/

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
	public String toString() {
		return getModel().toString();
	}

	
	protected void addSourceConnection(ConnectionEditPart connection, int index) {
		connection.setParent(this);
		super.addSourceConnection(connection, index);
		connection.setParent(this);
	}
}
