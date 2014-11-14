package net.confex.schema.figures;

import java.util.List;

import net.confex.schema.layout.GraphXYLayout;
import net.confex.schema.model.StateContainer;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;



public class SimpleContainerFigure extends RoundedRectangle implements IModelElementFigure {

	private final static int MIN_CORNER_SIZE = 10;
	private final static int MAX_CORNER_SIZE = 24;
	private final static int LIMIT_FOR_CHANGE_CORNER_SIZE = 50;

	public final static int SELECTION_LINE_WIDTH = 4;
	public final static int NORMAL_LINE_WIDTH = 1;
	
	private boolean selected;
	private Label tooltip_label= new Label("");
	
	private EditableLabel nameLabel;
	//private ChildrenFigure childrenFigure = new ChildrenFigure();

	private IFigure pane;
	ScrollPane scrollpane;
	
	//private Color fore_color = ColorConstants.black;
	private Color name_back_color =  new Color(Display.getCurrent(),236,255,255); //-- ColorConstants.listBackground;

	
	public IFigure getContentsPane(){
		return pane;
	}

	//-- для отображениях в GridLayout
	// устанавливается при создании фигуры
	private GridData gridData;
	
	
	public void validate() {
		super.validate();
	}
	
	
	public SimpleContainerFigure(EditableLabel name) {
		this(name, null);
	}


	public SimpleContainerFigure(EditableLabel name, List colums) {
		
		RoundedRectangleBorder border = new RoundedRectangleBorder();
		setBorder(border);
		setOutline(false);
		
		setToolTip(tooltip_label);
		
		//-- устанавливаем радиус скругления
		corner=new Dimension(MIN_CORNER_SIZE, MIN_CORNER_SIZE);
		setCornerDimensions(corner);
		
		//-- заголовок
		nameLabel = name;
		nameLabel.setBackgroundColor(name_back_color);
		nameLabel.setOpaque(false);
		nameLabel.setToolTip(tooltip_label);
		
		pane = new FreeformLayer();
		pane.setOpaque(true);
		pane.setLayoutManager(new FreeformLayout());

		//ToolbarLayout pane_manager = new ToolbarLayout();
		//pane_manager.setVertical(true);
		//pane_manager.setStretchMinorAxis(false);
		//pane_manager.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		//pane.setLayoutManager(pane_manager);
		
		scrollpane = new ScrollPane();
		scrollpane.setViewport(new FreeformViewport());
		scrollpane.setContents(pane);
		scrollpane.setPreferredSize(-1, 50);
		
		
		ToolbarLayout layout = new ToolbarLayout();
		layout.setVertical(true);
		layout.setStretchMinorAxis(true);
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		setLayoutManager(layout);
		
		add(nameLabel);
		add(scrollpane);
		
		super.setBackgroundColor(name_back_color);

		setOpaque(true);
	}

	
	/**
	 * Sets the dimensions of each corner. This will form the radii of the arcs which form the
	 * corners.
	 *
	 * @param d the dimensions of the corner
	 */
	public void setCornerDimensions(Dimension d) {
		super.setCornerDimensions(d);
		if(getBorder() instanceof RoundedRectangleBorder) {
			((RoundedRectangleBorder)getBorder()).setCornerDimensions(d);
		}
	}

	
	public void setForegroundColor(Color fore_color) {
		//-- set fore color on working pane
		if(pane != null)
			pane.setForegroundColor(fore_color);
		//-- change line border color
		if(getBorder() instanceof LineBorder) {
			((LineBorder)getBorder()).setColor(fore_color);
			((LineBorder)getBorder()).setWidth(1);
		} else {
			super.setForegroundColor(fore_color);
			setLineWidth(1);
		}
	}

	
	public void setBackgroundColor(Color back_color) {
		pane.setBackgroundColor(back_color); 
		scrollpane.setBackgroundColor(back_color);
	}
	
	
	public void modifyState( String state) {
		if(state.equals(StateContainer.COMPACT_STATE)) {
			this.setVisible(true);
			scrollpane.setVisible(false);
			pane.setVisible(false);
			//-- переприсодиняем к родительскому чтобы фигура всегда была наверху
			
			IFigure parent = this.getParent();
			if(parent!=null && parent.getLayoutManager() instanceof GraphXYLayout) {
				if(parent!=null) {
					parent.remove(this);
					parent.add(this);
				}
			}
		} else if(state.equals(StateContainer.STANDART_STATE)) {
			this.setVisible(true);
			scrollpane.setVisible(true);
			pane.setVisible(true);

			IFigure parent = this.getParent();
			if(parent!=null && parent.getLayoutManager() instanceof GraphXYLayout) {
				putOnTop();
			}
			//this.revalidate();
		}
	}
	
	
	/**
	 * Делаем фигуру верхней
	 */
	protected void putOnTop() {
		//-- переприсодиняем к родительскому чтобы фигура всегда была наверху
		IFigure parent = this.getParent();
		if(parent!=null) {
			parent.remove(this);
			parent.add(this);
		}
	}
	
	
	public void setText(String text) {
		nameLabel.setText(text);
	}
	public String getText() {
		return nameLabel.getText();
	}

	
	/**
	 * @return returns the label used to edit the name
	 */
	public EditableLabel getNameLabel()	{
		return nameLabel;
	}

	
	/**
	 * Returns <code>true</code> if this Figure uses local coordinates. This means its
	 * children are placed relative to this Figure's top-left corner.
	 * @return <code>true</code> if this Figure uses local coordinates
	 * @since 2.0
	 */
	protected boolean useLocalCoordinates() {
		return true;
	}

	
	/**
	 * @return the figure containing the column lables
	 */
	//public IFigure getContentsPane()	{
	//	return childrenFigure;
	//	//return pane;
	//}
	
	
	public void paintFigure(Graphics graphics) {
		//if(selected) {
		calcCornerSize();	
		//} else {
			//-- Каждый раз изменяем размер scrollpane чтобы точно вписаться в прямоугольник без скруглений
			int h = getBounds().height;
			scrollpane.setPreferredSize(-1, h-nameLabel.getBounds().height-this.corner.height/2);
			super.paintFigure(graphics);
		//}
	}

	
	/**
	 * Переопределяем размер скругления узлов в зависимости от размера
	 * фигуры.
	 */
	private void calcCornerSize() {
		int min_size= (getBounds().width<getBounds().height)?getBounds().width:getBounds().height;
		if(min_size<LIMIT_FOR_CHANGE_CORNER_SIZE) {
			if(corner.height!=MIN_CORNER_SIZE) {
				corner.height = MIN_CORNER_SIZE;
				corner.width = MIN_CORNER_SIZE;
				//-- устанавливаем радиус скругления MIN_CORNER_SIZE
				setCornerDimensions(corner);
				//System.out.println("-- устанавливаем радиус скругления MIN_CORNER_SIZE");
			}
		} else {
			if(corner.height!=MAX_CORNER_SIZE) {
				corner.height = MAX_CORNER_SIZE;
				corner.width = MAX_CORNER_SIZE;
				//-- устанавливаем радиус скругления MAX_CORNER_SIZE
				setCornerDimensions(corner);
				//System.out.println("-- устанавливаем радиус скругления MAX_CORNER_SIZE");
			}
		}
	}
	
	
/*	
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		//Rectangle rect = getBounds().getCopy();
		//rect.crop(new Insets(2,0,2,0));
		//graphics.fillRectangle(rect);
		//this.paintBorder(graphics); 
	}
	
	
	
	public void paint(Graphics graphics) {
		super.paint(graphics);
	}
*/
	
	
	public Rectangle getSelectionRectangle() {
		return this.getBounds(); 
	}
	
	/**
	 * @see IFigure#setConstraint(IFigure, Object)
	 */
	public void setConstraint(IFigure child, Object constraint) {
		if (child.getParent() != this)
			throw new IllegalArgumentException(
				"Figure must be a child"); //$NON-NLS-1$
		
		if (this.getLayoutManager() != null)
			this.getLayoutManager().setConstraint(child, constraint);
		revalidate();
	}
	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// IModelElementFigure
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the selection state of this SimpleActivityLabel
	 * 
	 * @param b
	 *            true will cause the label to appear selected
	 */
	public void setSelected(boolean isSelected)	{
		selected = isSelected;
		if( getBorder() instanceof LineBorder ) {
			LineBorder lineBorder = (LineBorder) getBorder();
			if (isSelected)	{
				lineBorder.setWidth(SELECTION_LINE_WIDTH);
			} else	{
				lineBorder.setWidth(NORMAL_LINE_WIDTH);
			}
		} else {
			if (isSelected)	{
				setLineWidth(SELECTION_LINE_WIDTH);
			} else	{
				setLineWidth(NORMAL_LINE_WIDTH);
			}
		}
		repaint();
	}

	
	public String getToolTipText() {
		return tooltip_label.getText();
	}

	
	public void setToolTipText(String txt) {
		tooltip_label.setText(txt);
	}


	public GridData getGridData() {
		return gridData;
	}


	public void setGridData(GridData gridData) {
		if(this.gridData!=null) {
			System.err.println(" gridData may be setted only once!");
		}
		this.gridData = gridData;
	}
}