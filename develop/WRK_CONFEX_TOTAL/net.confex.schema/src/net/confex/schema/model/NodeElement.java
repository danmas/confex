/**
 * 
 */
package net.confex.schema.model;

//import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.confex.schema.directedit.IElementPropertyDialog;
import net.confex.schema.directedit.NodeElementPropertyDialog;
import net.confex.schema.part.NodeElementPart;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Node;


/**
 * Базовый класс для всех элементов схемы  
 * Обладает след. свойствами:
 * 	 - имеет все атрибуты ModelElement
 *   - имеет прямоугольные размеры
 *   - имеет входящие и исходящие связи
 * 
 * @author Roman Eremeev
 */
public class NodeElement extends ModelElement implements IPropertySource {
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//private Schema schema; //-- схема которой принадлежит элемент 
	
	protected Rectangle bounds = new Rectangle(0,0,200,20);         	 //-- положение и размер элемента
	
	private ArrayList outConnections = new ArrayList(); //-- Выходящие связи
	private ArrayList inConnections = new ArrayList();  //-- Входящие связи

	protected String about = "This is NodeElement";
	public String getAboutString() { return about; }
	
	//-- диалог свойст
	protected NodeElementPropertyDialog property_dialog;
	
	public IElementPropertyDialog getPropertyDialog() {
		if(property_dialog==null) {
			property_dialog = new NodeElementPropertyDialog();
		}
		NodeElement new_element = new NodeElement();
		new_element.setPropertyLike(this);
		property_dialog.setNodeElement(new_element);
		
		return property_dialog;
	}
	
	
	/**
	 * Конструктор по умолчанию необходим
	 * для фабрики классов, иначе не будет происходить вставки элемента в схему
	 */
	public NodeElement() {
		super();
	}
	
	
	/**
	 * 
	 * @param schema - схема которой принадлежит элемент
	 */
	public NodeElement(Schema schema) {
		super(schema);
	}

	
	/**
	 * 
	 * @param schema      схема которой принадлежит элемент
	 * @param without_id  признак того что не присваивать новый идентификатор id при создании
	 *                    используется во время загрузки 
	 */
	public NodeElement(Schema schema, boolean without_id) {
		super(schema, without_id);
	}

	
	
	
	public NodeElement(Schema schema, String text) {
		super(schema,text);
	}

	
	public Image getImage() {
		return null; 
	}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(NodeElement prototype) {
		super.setPropertyLike(prototype);
		//this.setText(prototype.getText());
		//this.setTooltipText(prototype.getTooltipText());
	}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * и на каждое свойство запускается событие обновления
	 * @param prototype
	 */
	public void modifyPropertyLike(NodeElement prototype) {
		super.modifyPropertyLike(prototype);
		//this.modifyText(prototype.getText());
		//this.modifyTooltipText(prototype.getTooltipText());
	}

	
	/**
	 * Возвращает элемент схемы по id
	 * @param id
	 * @return
	 */
	protected NodeElement getChildWithId(int id) {
		if(id==this.id)
			return this;
		return null;
	}

	
	/**
	 * Возвращает элемент схемы по origin_id
	 * @param id
	 * @return
	 */
	protected NodeElement getChildWithOriginIdFromPaste(int origin_id, ArrayList list) {
		if(origin_id==this.origin_id)
			return this;
		return null;
	}


	/**
	 * @return Returns the bounds.
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	
	/**
	 * Sets bounds without firing off any event notifications
	 * 
	 * @param bounds
	 *            The bounds to set.
	 */
	public void setBounds(Rectangle bounds)	{
		this.bounds = bounds;
	}

	
	/**
	 * If modified, sets bounds and fires off event notification
	 * 
	 * @param bounds
	 *            The bounds to set.
	 */
	public void modifyBounds(Rectangle bounds)	{
		Rectangle oldBounds = this.bounds;
		if (!bounds.equals(oldBounds))	{
			this.bounds = bounds;
			firePropertyChange(BOUNDS, null, bounds);
		}
	}

	
	/**
	 * If modified, sets bounds and fires off event notification
	 */
	public void modifyBounds()	{
		firePropertyChange(BOUNDS, null, bounds);
	}

	
	/**
	 * Сдвигаем элемент на схеме 
	 * @param shift     
	 */
	protected void moveAtShift(Point shift) {
		Rectangle old_bounds = this.getBounds();
		Rectangle new_bounds =  new Rectangle(
				old_bounds.x + shift.x,
				old_bounds.y + shift.y,
				old_bounds.width,
				old_bounds.height);
		modifyBounds(new_bounds);
	}	
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Connections 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public List getOutConnections()	{
		return outConnections;
	}
	
	public List getInConnections()	{
		return inConnections;
	}
	
	public void addOutConnections(ModelConnection out_connection) {
		outConnections.add(out_connection);
		firePropertyChange(OUTPUT, null, out_connection);
	}

	public void addInConnections(ModelConnection in_connection) {
		inConnections.add(in_connection);
		firePropertyChange(INPUT, null, in_connection);
	}
	
	public void removeOutConnections(ModelConnection out_connection) {
		outConnections.remove(out_connection);
		firePropertyChange(OUTPUT, out_connection, null);
	}

	public void removeInConnections(ModelConnection in_connection) {
		inConnections.remove(in_connection);
		firePropertyChange(INPUT, in_connection, null);
	}
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Property
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * A static array of property descriptors.
	 * There is one IPropertyDescriptor entry per editable property.
	 * @see #getPropertyDescriptors()
	 * @see #getPropertyValue(Object)
	 * @see #setPropertyValue(Object, Object)
	 */
	//private static IPropertyDescriptor[] descriptors;
	private static Vector prop_descriptors_array = new Vector(); 
	
	
	private static IPropertyDescriptor[] property_descriptor;
	
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return property_descriptor;
	}

	public static int getPropertyDescriptorCount() { return property_descriptor.length; }  
	
	static {
		init_prop_descriptors(prop_descriptors_array);
	}
	
	
	/**
	 * 
	 * TODO Инициализация массива описателей свойств производится для каждого элемента а это неэффективно!
	 */
	protected static void init_prop_descriptors(Vector pda) {
		ModelElement.init_prop_descriptors(pda);
		
		System.out.println("STATIC!  NodeElemnt.init_prop_descriptors()");
		
		PropertyDescriptor pd;
		pd = new TextPropertyDescriptor(X_POS, "X");
		pd.setCategory("Layout");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				int intValue = -1;
				try {
					intValue = Integer.parseInt((String) value);
				} catch (NumberFormatException exc) {
					return "Not a number";
				}
				return (intValue >= 0) ? null : "Value must be >=  0";
			}
		});
		pda.add(pd);
		pd = new TextPropertyDescriptor(Y_POS, "Y");
		pd.setCategory("Layout");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				int intValue = -1;
				try {
					intValue = Integer.parseInt((String) value);
				} catch (NumberFormatException exc) {
					return "Not a number";
				}
				return (intValue >= 0) ? null : "Value must be >=  0";
			}
		});
		pda.add(pd);
		pd = new TextPropertyDescriptor(WIDTH, "Width");
		pd.setCategory("Layout");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				int intValue = -1;
				try {
					intValue = Integer.parseInt((String) value);
				} catch (NumberFormatException exc) {
					return "Not a number";
				}
				return (intValue >= 0) ? null : "Value must be >=  0";
			}
		});
		pda.add(pd);
		pd = new TextPropertyDescriptor(HEIGHT, "Height");
		pd.setCategory("Layout");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				int intValue = -1;
				try {
					intValue = Integer.parseInt((String) value);
				} catch (NumberFormatException exc) {
					return "Not a number";
				}
				return (intValue >= 0) ? null : "Value must be >=  0";
			}
		});
		pda.add(pd);
		//-- Инициализация массива описателей свойств
		//TODO!!! Если изметяется набор свойств в этом класс то необходимо произвести замену и во всех дочерних!
		int i= ModelElement.getPropertyDescriptorCount(); 

		property_descriptor = new IPropertyDescriptor[i+4];
		
		for(int j=0;j<i+4;j++) {
			property_descriptor[j]=(IPropertyDescriptor)pda.get(j);
		}
	}
	
	
	/**
	 * Return the property value for the given propertyId, or null.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array 
	 * to obtain the value of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
	public Object getPropertyValue(Object propertyId) {
		Object obj = super.getPropertyValue(propertyId); 
		if(obj!=null)
			return obj;
		
		if (X_POS.equals(propertyId)) {
			return Integer.toString(bounds.x);
		} else	if (Y_POS.equals(propertyId)) {
			return Integer.toString(bounds.y);
		}  else	if (WIDTH.equals(propertyId)) {
			return Integer.toString(bounds.width);
		}  else	if (HEIGHT.equals(propertyId)) {
			return Integer.toString(bounds.height);
		}
		return null;
	}
	
	
	/**
	 * Set the property value for the given property id.
	 * If no matching id is found, the call is forwarded to the superclass.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array to set the values
	 * of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
	public void setPropertyValue(Object propertyId, Object value) {
		super.setPropertyValue(propertyId, value);
		
		if (X_POS.equals(propertyId)) {
			int x = Integer.parseInt((String) value);
			bounds.setLocation(new Point(x, bounds.y));
			this.modifyBounds(bounds);
		} else if (Y_POS.equals(propertyId)) {
			int y = Integer.parseInt((String) value);
			bounds.setLocation(new Point(bounds.x, y));
			this.modifyBounds(bounds);
		} else if (HEIGHT.equals(propertyId)) {
			int height = Integer.parseInt((String) value);
			bounds.setSize(new Dimension(bounds.width, height));
			this.modifyBounds(bounds);
		} else if (WIDTH.equals(propertyId)) {
			int width = Integer.parseInt((String) value);
			bounds.setSize(new Dimension(width, bounds.height));
			this.modifyBounds(bounds);
		} else {
			;
		}
	}

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  XML 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Выводит представление в виде хмл
	 * @return
	 */
	public String getXml() {
		String str;
		str = "  <NodeElement id=\""+ getId() +"\">\n";
		str += getPropertyXml();
		str +="  </NodeElement>\n";
		return str;
	}

	
	protected String getPropertyXml() {
		String str = super.getPropertyXml();
		str += getBoundsXml();
		return str;
	}

	
	public String getEdgesXml() {
		String str="";
		List connections = this.getOutConnections();
		//-- сохраняем все исходящие ребра
		for(Iterator iter=connections.iterator();iter.hasNext();) {
			ModelConnection connection = (ModelConnection)iter.next(); 	
			str += connection.getXml();
		}
		return str;
	}
	
	
	/**
	 * Добавляет XML строку коннекции только если in элемент коннекции 
	 * содержится в списке in_list
	 * Это необходимо для копирования в буфер обмена
	 * @param in_list
	 * @return
	 */
	public String getEdgesXmlForInList(ArrayList in_list) {
		String str="";
		List connections = this.getOutConnections();
		//-- сохраняем все исходящие ребра
		for(Iterator iter=connections.iterator();iter.hasNext();) {
			ModelConnection connection = (ModelConnection)iter.next();
			if(in_list.contains(connection.getInElement()))
				str += connection.getXml();
		}
		return str;
	}
	
	
	protected String getBoundsXml() {
		String str="    <bounds x=\""+ bounds.x + "\" y=\""+bounds.y 
			+"\" height=\"" +bounds.height + "\" width=\"" + bounds.width + "\"/>\n";
		return str;
	}
	
	
	protected void loadAttribsFromXml(Node child) {
		super.loadAttribsFromXml(child);
		
		if (child.getNodeName().equals("bounds")) {
			String x = child.getAttributes().getNamedItem("x").getNodeValue(); 
			String y = child.getAttributes().getNamedItem("y").getNodeValue();
			String height = child.getAttributes().getNamedItem("height").getNodeValue();
			String width = child.getAttributes().getNamedItem("width").getNodeValue();
			Rectangle rect = new Rectangle(
					Integer.parseInt(x),
					Integer.parseInt(y),
					Integer.parseInt(width),
					Integer.parseInt(height));  
			setBounds(rect);
		} 
	}

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Активность
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void doDefaultAction(NodeElementPart part) {
	}

}
