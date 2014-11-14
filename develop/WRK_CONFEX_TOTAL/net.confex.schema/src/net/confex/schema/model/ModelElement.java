package net.confex.schema.model;


import java.util.Vector;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Node;

/**
 * Базовый класс для всех элементов схемы  
 * Обладает след. свойствами:
 *   - имеет цвет фона и текста
 *   - имеет текст для отображения
 *   - имеет всплывающую подсказку
 *   - модет отображать свойства в окне свойств 
 * 
 * @author Roman Eremeev
 */
public class ModelElement extends PropertyAwareObject implements IPropertySource {
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	protected Schema schema; //-- схема которой принадлежит элемент 
	//public Schema getSchema() { return schema; }
	
	protected int id = -1; //-- текущий номер элемента в схеме
	public int getId(){ return id; }

	protected int origin_id = -1; //-- уникальный номер элемента в схеме в момент создания
	public int getOriginId(){ return origin_id; }
	
	protected String text = "";              //-- 
	private   String tooltip_text = "";      //-- 
	private   RGB fore_color = new RGB(0,0,255);
	private   RGB back_color = new RGB(255,255,255);
	
	protected String about = "This is ModelElement";
	public String getAboutString() { return about; }
	
	/**
	 * Конструктор по умолчанию необходим
	 * для фабрики классов, иначе не будет происходить вставки элемента в схему
	 */
	public ModelElement() {
		super();
		System.out.println(" new NodeElement()");
	}
	
	
	/**
	 * 
	 * @param schema - схема которой принадлежит элемент
	 */
	public ModelElement(Schema schema) {
		super();
		this.schema = schema;
		id=schema.getIdForUsing();
	}

	
	/**
	 * 
	 * @param schema      схема которой принадлежит элемент
	 * @param without_id  признак того что не присваивать новый идентификатор id при создании
	 *                    используется во время загрузки 
	 */
	public ModelElement(Schema schema, boolean without_id) {
		super();
		this.schema = schema;
		if(!without_id)
			id=schema.getIdForUsing();
	}

	
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public Schema getSchema() {
		return this.schema;
	}

	
	/**
	 * Необходимо присвоить уникальный id 
	 *
	 */
	public void setIdForUsing() {
		id = schema.getIdForUsing();
	}
	

	public ModelElement(Schema schema, String text) {
		super();
		this.schema = schema;
		fore_color=new RGB(0,0,255);
		back_color=new RGB(255,255,255);
		if (text == null) text = ""; //throw new NullPointerException("Text cannot be null");
		this.text = text;
	}


	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ModelElement prototype) {
		this.setText(prototype.getText());
		this.setTooltipText(prototype.getTooltipText());
	}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * и на каждое свойство запускается событие обновления
	 * @param prototype
	 */
	public void modifyPropertyLike(ModelElement prototype) {
		this.modifyText(prototype.getText());
		this.modifyTooltipText(prototype.getTooltipText());
	}
	
	
	/**
	 * Вызывается после вставки пользователем элемента в диаграмму 
	 */
	public void onShapeAddCommand() {
		makeDefaultPropertyes();
	}
		

	/**
	 * Создаем параметры по умолчанию
	 *
	 */
	protected void makeDefaultPropertyes() {
		setText("TEXT!");
	}

		
	/**
	 * Set text without firing off any event notifications
	 *  * use modifyText if need fire events
	 *  
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	public String getText()	{
		return text;
	}
	
	
	/**
	 * Set tooltip_text without firing off any event notifications
	 *  * use modifyText if need fire events
	 *  
	 * @param text
	 */
	public void setTooltipText(String tooltip_text) {
		this.tooltip_text = tooltip_text;
	}
	public String getTooltipText()	{
		return tooltip_text;
	}
	

	/**
	 * Конструирует всплывающую подсказку по умолчанию
	 * @return
	 */
	protected String makeDefaultToolTip() {
		return "";
	}

	
	/**
	 * If modified, sets text and fires off event notification
	 * 
	 * @param text
	 *            The text to set.
	 */
	public void modifyText(String text) {
		String oldName = this.text;
		if (!text.equals(oldName)) {
			this.text = text;
			firePropertyChange(TEXT, null, text);
		}
	}


	/**
	 * Выделяет элемент 
	 */
	public void setSelected(EditPart hostEditPart) {
		firePropertyChange(SELECTED, hostEditPart, null);
	}


	/**
	 * If modified, sets tooltip text and fires off event notification
	 * 
	 * @param tooltip_text
	 *            The tooltip text to set.
	 */
	public void modifyTooltipText(String tooltip_text) {
		String oldName = this.tooltip_text;
		if (!tooltip_text.equals(oldName)) {
			this.tooltip_text= tooltip_text;
			firePropertyChange(TOOLTIP_TEXT, null, tooltip_text);
		}
	}


	public RGB getForeColor() {
		return fore_color;
	}
	
	
	public void setForeColor(RGB rgb) {
		fore_color=rgb;
	}

	
	public RGB getBackColor() {
		return back_color;
	}
	
	
	public void setBackColor(RGB rgb) {
		back_color=rgb;
	}
	
	/**
	 * If modified, sets fore_color and fires off event notification
	 * 
	 * @param new_color
	 */
	public void modifyForeColor(RGB new_color) {
		RGB oldColor = this.fore_color;
		if (!new_color.equals(oldColor)) {
			this.fore_color = new_color;
			firePropertyChange(FORE_COLOR, null, new_color);
		}
	}

	
	/**
	 * If modified, sets fore_color and fires off event notification
	 * 
	 * @param new_color
	 */
	public void modifyBackColor(RGB new_color) {
		RGB oldColor = this.back_color;
		if (!new_color.equals(oldColor)) {
			this.back_color = new_color;
			firePropertyChange(BACK_COLOR, null, new_color);
		}
	}
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Property
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** An empty property descriptor. */
	//private static final IPropertyDescriptor[] EMPTY_ARRAY = new IPropertyDescriptor[0];

	
	/**
	 * Children should override this. The default implementation returns an empty array.
	 */
	//public IPropertyDescriptor[] getPropertyDescriptors() {
	//	return EMPTY_ARRAY;
	//}
	

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
	
	
	/**
	 * Returns an array of IPropertyDescriptors for this shape.
	 * <p>The returned array is used to fill the property view, when the edit-part corresponding
	 * to this model element is selected.</p>
	 * @see #descriptors
	 * @see #getPropertyValue(Object)
	 * @see #setPropertyValue(Object, Object)
	 */
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
		//System.out.println("STATIC!  ModelElemnt.init_prop_descriptors()");
		
		PropertyDescriptor pd;
		pd = new TextPropertyDescriptor(ID, "Id");
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

		pd = new TextPropertyDescriptor(TEXT, "Text");
		pd.setCategory("Text");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				if(value instanceof String)
					return null;
				return "Must be string!";
			}
		});
		pda.add(pd);
		pd = new TextPropertyDescriptor(TOOLTIP_TEXT, "Tooltip Text");
		pd.setCategory("Text");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				if(value instanceof String)
					return null;
				return "Must be string!";
			}
		});
		pda.add(pd);
		pd = new ColorPropertyDescriptor(FORE_COLOR, "Foreground Color");
		pd.setCategory("Layout");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				if(value instanceof RGB)
					return null;
				return "Must be Color!";
			}
		});
		pda.add(pd);
		pd = new ColorPropertyDescriptor(BACK_COLOR, "Background Color");
		pd.setCategory("Layout");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				if(value instanceof RGB)
					return null;
				return "Must be Color!";
			}
		});
		pda.add(pd);
		
		//System.out.println(" prop size="+prop_descriptors_array.size()+" capacity="
		//		+ prop_descriptors_array.capacity());
		
		//-- Инициализация массива описателей свойств
		//TODO!!! Если изметяется набор свойств в этом класс то необходимо произвести замену и во всех дочерних! 
		property_descriptor = new IPropertyDescriptor[] { 
				(IPropertyDescriptor)pda.get(0),
				(IPropertyDescriptor)pda.get(1),
				(IPropertyDescriptor)pda.get(2),
				(IPropertyDescriptor)pda.get(3),
				(IPropertyDescriptor)pda.get(4),
		};
		
	}
	
	
	/**
	 * Return the property value for the given propertyId, or null.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array 
	 * to obtain the value of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
	public Object getPropertyValue(Object propertyId) {
		if (ID.equals(propertyId)) {
			return Integer.toString(id);
		}
		if (TEXT.equals(propertyId)) {
			return this.text;
		}
		if (TOOLTIP_TEXT.equals(propertyId)) {
			if(tooltip_text==null)
				tooltip_text="";
			return this.tooltip_text;
		}
		if (FORE_COLOR.equals(propertyId)) {
			return this.fore_color;
		}
		if (BACK_COLOR.equals(propertyId)) {
			return this.back_color;
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
		if (TEXT.equals(propertyId))	{
			this.modifyText((String) value);
		} else if (TOOLTIP_TEXT.equals(propertyId))	{
			this.modifyTooltipText((String) value);
		} else if (FORE_COLOR.equals(propertyId))	{
			this.modifyForeColor((RGB) value);
		} else if (BACK_COLOR.equals(propertyId))	{
			this.modifyBackColor((RGB) value);
		} else {
			;
		}
	}

	
	/**
	 * Children should override this. The default implementation returns false.
	 */
	public boolean isPropertySet(Object id) {
		return false;
	}
	
	
	/**
	 * Children should override this. The default implementation does nothing.
	 */
	public void resetPropertyValue(Object id) {
		// do nothing
	}


	/**
	 * Returns a value for this property source that can be edited in a property sheet.
	 * <p>My personal rule of thumb:</p>
	 * <ul>
	 * <li>model elements should return themselves and</li> 
	 * <li>custom IPropertySource implementations (like DimensionPropertySource in the GEF-logic
	 * example) should return an editable value.</li>
	 * </ul>
	 * <p>Override only if necessary.</p>
	 * @return this instance
	 */
	public Object getEditableValue() {
		return this;
	}
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  XML 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Выводит представление в виде хмл
	 * @return
	 */
	public String getXml() {
		return "";
	}

	
	protected String getPropertyXml() {
		String str = "";
		str += getTextXml();   
		str += getTooltipTextXml();
		str += getColorsXml();
		return str;
	}

	
	public String getEdgesXml() {
		return "";
	}
	
	
	protected String getTextXml() {
		String str=""; 
		if(tooltip_text!=null) {
			String s = net.confex.schema.utils.Strings.replace(text,"<", "&lt;");
			s = net.confex.schema.utils.Strings.replace(s, ">", "&gt;");
			str="    <"+TEXT+">"+s+"</"+TEXT+">\n";
		}
		return str;
	}
	

	protected String getTooltipTextXml() {
		String str="";
		if(tooltip_text!=null) {
			String s = net.confex.schema.utils.Strings.replace(tooltip_text,"<", "&lt;");
			s = net.confex.schema.utils.Strings.replace(s, ">", "&gt;");
			str="    <"+TOOLTIP_TEXT+">"+s+"</"+TOOLTIP_TEXT+">\n";
		}
		return str;
	}

	
	protected String getColorsXml() {
		String str="    <colors "+
		FORE_COLOR+"=\""+ this.fore_color+ "\" " +
		BACK_COLOR+"=\""+ this.back_color+ "\" " +
		"/>\n";
		return str;
	}
	
	
	protected void loadAttribsFromXml(Node child) {
		if (child.getNodeName().equals("colors")) {
			Node node = child.getAttributes().getNamedItem(FORE_COLOR);
			if(node==null) {
				System.err.println(" attribute " + FORE_COLOR + " is absent!");
			} else {
				String rgb = node.getNodeValue();
				if( rgb!= null) {
					int i1 = rgb.indexOf('{');
					int i2 = rgb.indexOf(',');
					int r=Integer.parseInt(rgb.substring(i1+1,i2));
					i1 = rgb.indexOf(',',i2+1);
					int g=Integer.parseInt(rgb.substring(i2+2,i1));
					i2 = rgb.indexOf('}',i1+1);
					int b=Integer.parseInt(rgb.substring(i1+2,i2));
					fore_color = new RGB(r,g,b);
				}
			}
			node = child.getAttributes().getNamedItem(BACK_COLOR);
			if(node==null) {
				System.err.println(" attribute " + BACK_COLOR + " is absent!");
			} else {
				String rgb = node.getNodeValue();
				if( rgb!= null) {
					int i1 = rgb.indexOf('{');
					int i2 = rgb.indexOf(',');
					int r=Integer.parseInt(rgb.substring(i1+1,i2));
					i1 = rgb.indexOf(',',i2+1);
					int g=Integer.parseInt(rgb.substring(i2+2,i1));
					i2 = rgb.indexOf('}',i1+1);
					int b=Integer.parseInt(rgb.substring(i1+2,i2));
					back_color = new RGB(r,g,b);
				}
			}
		} else if (child.getNodeName().equals(TEXT)) {
			Node nd=child.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			setText(text);	
		} else if (child.getNodeName().equals(TOOLTIP_TEXT)) {
			Node nd=child.getFirstChild();
			String tooltip_text="";
			if(nd!=null) {
				tooltip_text = nd.getNodeValue();
			}
			setTooltipText(tooltip_text);	
		}
	}

	
	/**
	 * Загружает внутренние параметры из DOM дерева
	 * @param node Начинается с <NodeElement id="4">
	 * @param operation PASTE_OPERATION/LOAD_OPERATION
	 */
	public void loadFromXml(Node node, int operation) {
		//-- get id saved in file
		Node attr_id=node.getAttributes().getNamedItem(ID);
		if(attr_id==null) {
			System.err.println(" Can't read id for " + this.toString());
			id=schema.getIdForUsing();
		} else {
			String s_id=attr_id.getNodeValue();
			int t_id= Integer.valueOf(s_id).intValue();
			origin_id = t_id;
			if(operation==Schema.PASTE_OPERATION) {
				id=schema.getIdForUsing();
				//	//schema.setIdForUsing(t_id);
			} else { // Schema.LOAD_OPERATION
				id = t_id;
				schema.setMaxIdForUsing(id);
			}
		}
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node child = node.getChildNodes().item(i);
			loadAttribsFromXml(child);
		}
	}
	
}	
