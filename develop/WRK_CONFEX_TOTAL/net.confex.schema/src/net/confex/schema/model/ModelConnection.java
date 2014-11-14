package net.confex.schema.model;

import java.util.Vector;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Node;

/**
 * Базовый класс для всех связей схемы  
 * Обладает след. свойствами:
 * 	 - имеет все атрибуты ModelElement
 *   - имеет узел начала и конца связи
 *   - имеет тип который задает вид связи 
 * 
 * @author Roman Eremeev
 */

public class ModelConnection  extends ModelElement {
	
	public static final String DEFAULT = "DEFAULT";
	public static final String NOTE = "NOTE";
	public static final String GENERALISATION = "GENERALISATION";

	private NodeElement in_element;
	private NodeElement out_element;

	protected String connection_type = DEFAULT;
	public String getConnectionType() { return connection_type; }
	
	/**
	 * @param out_connection
	 * @param in_element
	 */
	public ModelConnection(Schema schema, boolean without_id,NodeElement out_element, NodeElement in_element) {
		super(schema, without_id);
		
		this.in_element = in_element;
		this.out_element = out_element;
		this.in_element.addInConnections(this);
		this.out_element.addOutConnections(this);
	}
	

	public NodeElement getOutElement()	{
		return out_element;
	}
	public NodeElement getInElement() {
		return in_element;
	}

	
	public void setInElement(NodeElement in_element) {
		this.in_element = in_element;
	}	
	
	
	public void setOutElement(NodeElement out_element)	{
		this.out_element = out_element;
	}
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Property
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static final String CONNECTION_TYPE = "CONNECTION_TYPE"; 	
	
	
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
		ModelElement.init_prop_descriptors(pda);
		
		System.out.println("STATIC!  ModelConnection.init_prop_descriptors()");
		TextPropertyDescriptor pd;
		pd = new TextPropertyDescriptor(CONNECTION_TYPE, "Connection type");
		pd.setCategory("Connection");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				if(value instanceof String) {
					String s = (String) value;
					if(    !s.equals(NOTE) 
							&& !s.equals(GENERALISATION) 
							&& !s.equals(DEFAULT) 
						)
						return "Must be one of: " + DEFAULT + ";" + NOTE + ";" + GENERALISATION; 
					return null;
				}
				return "Must be string!";
			}
		});
		pda.add(pd);
		
		int i= ModelElement.getPropertyDescriptorCount(); 

		property_descriptor = new IPropertyDescriptor[i+1];
		
		for(int j=0;j<i+1;j++) {
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
		if (CONNECTION_TYPE.equals(propertyId)) {
			return this.connection_type;
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
		
		if (CONNECTION_TYPE.equals(propertyId)) {
			modifyConnectionType((String) value);
		}
	}
	
	
	/**
	 * If modified, sets text and fires off event notification
	 * 
	 * @param value
	 *            The text to set.
	 */
	public void modifyConnectionType(String value) {
		String old = this.connection_type;
		if (!value.equals(old)) {
			this.connection_type = value;
			firePropertyChange(CONNECTION_TYPE, null, value);
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
		str = "  <ModelConnection id=\""+ getId() +"\" id_out=\""+ out_element.getId() +"\" id_in=\"" +
			in_element.getId() + "\">\n";
		str += getPropertyXml();
		str +="  </ModelConnection>\n";
		return str;
	}

	
	protected String getPropertyXml() {
		String str="";
		str += super.getPropertyXml();
		str += getConnectionValuesXml();
		return str;
	}
	
	
	protected String getConnectionValuesXml() {
		String str="";
		if(this.connection_type!=null)
			str += "    <connection_type>"+connection_type+"</connection_type>\n"; 
		return str;
	}

	
	protected void loadAttribsFromXml(Node child) {
		super.loadAttribsFromXml(child);
		
		if (child.getNodeName().equals("connection_type")) {
			Node nd=child.getFirstChild();
			String s="";
			if(nd!=null) {
				s = nd.getNodeValue();
			}
			connection_type = s;
		} 
			
	}
	
}