package net.confex.schema.model;

import java.util.Vector;

import net.confex.schema.part.NodeElementPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.w3c.dom.Node;

/**
 * Контейнер который может иметь несколько геометрических состояний
 * 1 - compact  - компактное состояние виден только заголовок
 * 2 - standart - обычное состояние
 */
 
 
public class StateContainer extends SimpleContainer {

	public static final String STANDART_STATE = "STANDART";
	public static final String COMPACT_STATE = "COMPACT";
	
	protected String state = STANDART_STATE; //-- текущее состояние
	
	protected Rectangle compact_bounds = new Rectangle(20,20,100,30);      //-- положение и размер элемента в компактном состоянии

	protected String about = "This is Container";
	public String getAboutString() { return about; }
	
	/**
	 * Конструктор по умолчанию необходим
	 * для фабрики классов, иначе не будет происходить вставки элемента в схему!!!
	 */
	public StateContainer() {
		super();
	}
	
	
	/**
	 * 
	 * @param schema      схема которой принадлежит элемент
	 */
	public StateContainer(Schema schema) {
		super(schema);
	}
	
	
	/**
	 * 
	 * @param schema      схема которой принадлежит элемент
	 * @param without_id  признак того что не присваивать новый идентификатор id при создании,
	 *                    используется во время загрузки id потом подставляется из загруженных данных
	 */
	public StateContainer(Schema schema, boolean without_id) {
		super(schema,without_id);
	}

	
	/*
	 * Возвращает истину если контейнер закрыт
	 */
	public boolean isCompact() {
		return state.equals(COMPACT_STATE);
	}
	
	
	/**
	 * @return Returns the bounds.
	 */
	public Rectangle getBounds() {
		if(state.equals(STANDART_STATE))
			return bounds;
		if(state.equals(COMPACT_STATE))
			return compact_bounds;
		return null;
	}

	
	/**
	 * Sets bounds without firing off any event notifications
	 * 
	 * @param bounds
	 *            The bounds to set.
	 */
	public void setBounds(Rectangle bounds)	{
		if(state.equals(STANDART_STATE))
			this.bounds = bounds;
		if(state.equals(COMPACT_STATE))
			this.compact_bounds = bounds;
	}

	
	protected void set_compact_bounds(Rectangle bounds)	{
			this.compact_bounds = bounds;
	}
	
	
	public String getCurrentState() {
		return state;
	}
	
	
	/**
	 * If modified, sets bounds and fires off event notification
	 * 
	 * @param bounds
	 *            The bounds to set.
	 */
	public void modifyBounds(Rectangle bounds)	{
		if(state.equals(STANDART_STATE)) {
			Rectangle oldBounds = this.bounds;
			if (!bounds.equals(oldBounds))	{
				this.bounds = bounds;
				firePropertyChange(BOUNDS, null, bounds);
			}
		} else if(state.equals(COMPACT_STATE)) {
			Rectangle oldBounds = this.compact_bounds;
			if (!bounds.equals(oldBounds))	{
				this.compact_bounds = bounds;
				firePropertyChange(BOUNDS, null, compact_bounds);
			}
		}
			
	}

	
	/**
	 * If modified, fires off event notification
	 */
	 /*protected void modifyState()	{
		if(state.equals(STANDART_STATE)) {
			firePropertyChange(BOUNDS, null, bounds);
		} else if(state.equals(COMPACT_STATE)) {
			firePropertyChange(BOUNDS, null, compact_bounds);
		}
		firePropertyChange(CONTAINER_STATE, null, state);
	}*/

	 
	/**
	 * If modified, sets states and fires off event notification
	 */
	 public boolean modifyState(String state)	{
		if(state.equals(STANDART_STATE) || state.equals(COMPACT_STATE)) {
			String old_state = this.state;
			
			this.state = state;
			
			if(state.equals(STANDART_STATE)) {
				if(!old_state.equals(state)) {
					bounds.x=compact_bounds.x;
					bounds.y=compact_bounds.y;
				}
				firePropertyChange(BOUNDS, null, bounds);
			} else if(state.equals(COMPACT_STATE)) {
				if(!old_state.equals(state)) {
					compact_bounds.x=bounds.x;
					compact_bounds.y=bounds.y;
				}
				firePropertyChange(BOUNDS, null, compact_bounds);
			}
			firePropertyChange(CONTAINER_STATE, null, state);

			return true;
		} else {
			System.err.println(" Can't set this state "+ state + " for StateContainer!");
			return false;
		}
	}
		 
		 
	/**
	 * If modified, sets states without firing any events
	 */
	 protected boolean set_state(String state)	{
		if(state.equals(STANDART_STATE) || state.equals(COMPACT_STATE)) { 
			this.state = state;
			return true;
		} else {
			System.err.println(" Can't set this state "+ state + " for StateContainer!");
			return false;
		}
	}
	 
			 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Property
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final String CONTAINER_STATE = "CONTAINER_STATE";

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

	
	protected static void init_prop_descriptors(Vector pda) {
		NodeElement.init_prop_descriptors(pda);

		//System.out.println("STATIC!  StateContainer.init_prop_descriptors()");
	
		TextPropertyDescriptor pd = new TextPropertyDescriptor(CONTAINER_STATE, "State of container:");
		pd.setCategory("Layout");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				if(value instanceof String) {
					String s = (String) value;
					if(    !s.equals(STANDART_STATE) 
							&& !s.equals(COMPACT_STATE) 
						)
						return "Must be one of: " + STANDART_STATE + ";" + COMPACT_STATE ; 
					return null;
				}
				return "Must be string!";
			}
		});
		
		pda.add(pd);
		
		//-- add validators here
		
		//-- Пререопределяем массив описателей свойств т.к. для дочернего элемента он другой

		int i= NodeElement.getPropertyDescriptorCount(); 

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
		if (CONTAINER_STATE.equals(propertyId)) {
			if(state==null)
				state="";	
			return this.state;
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
		
		if (CONTAINER_STATE.equals(propertyId)) {
			state = (String) value;
			modifyState(state);
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
		str = "  <StateContainer id=\""+ getId() +"\">\n";
		str += getPropertyXml();
		str += get_child_xml();
		str +="  </StateContainer>\n";
		return str;
	}

	
	protected String getPropertyXml() {
		String str = super.getPropertyXml();
		str += getStateXml();
		return str;
	}

	
	protected String getBoundsXml() {
		String str= super.getBoundsXml();
		str +="    <compact_bounds x=\""+ compact_bounds.x + "\" y=\""+compact_bounds.y 
		+"\" height=\"" +compact_bounds.height + "\" width=\"" + compact_bounds.width + "\"/>\n";
		return str;
	}
	
	
	protected String getStateXml() {
		String str ="    <state>"+state+"</state>\n";
		return str;
	}

	
	protected void loadAttribsFromXml(Node child) {
		super.loadAttribsFromXml(child);
		
		if (child.getNodeName().equals("compact_bounds")) {
			String x = child.getAttributes().getNamedItem("x").getNodeValue();
			String y = child.getAttributes().getNamedItem("y").getNodeValue();
			String height = child.getAttributes().getNamedItem("height").getNodeValue();
			String width = child.getAttributes().getNamedItem("width").getNodeValue();
			Rectangle rect = new Rectangle(Integer.parseInt(x),
					Integer.parseInt(y),
					Integer.parseInt(width),
					Integer.parseInt(height));  
			set_compact_bounds(rect);
		} else if (child.getNodeName().equals("state")) {
			Node nd=child.getFirstChild();
			if(nd!=null) {
				this.set_state(nd.getNodeValue());
			}
		}
	}
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Активность
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void doDefaultAction(NodeElementPart part) {
		//-- Будет переключать состояния
		if(state.equals(STANDART_STATE)) {
			this.modifyState(COMPACT_STATE);
		} else {
			this.modifyState(STANDART_STATE);
		}
	}


}
