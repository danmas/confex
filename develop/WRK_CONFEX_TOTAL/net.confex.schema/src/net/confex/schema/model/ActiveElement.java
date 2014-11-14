package net.confex.schema.model;

import java.util.Vector;

import net.confex.schema.directedit.IElementPropertyDialog;
import net.confex.schema.directedit.NodeElementPropertyDialog;
import net.confex.schema.part.NodeElementPart;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;



/**
 * Класс для элементов схемы которые могут менять свое состояние
 * на следующие:
 * - ACTIVE   - элемент активен (работает, выполняется в данный момент)
 * - PASSIVE  - элемент пассивен (или его состояние было очищено)
 * - ERROR    - отработал и в процессе работы возникла ошибка
 * - OK       - отработал без ошибки
 * 
 * Обладает след. свойствами:
 * 	 - имеет все атрибуты NodeElement
 *   - имеет текущее состояние cur_active_state
 * 
 * @author Roman Eremeev
 */
public class ActiveElement extends NodeElement {

	protected String about = "This is ActiveElement";
	public String getAboutString() { return about; }
	
	public static final String PASSIVE = "PASSIVE";
	public static final String ACTIVE = "ACTIVE";
	public static final String ERROR = "ERROR";
	public static final String OK = "OK";
	
	protected String active_state = PASSIVE;

	public static final String ACTIVE_STATE = "active_state";


	//-- диалог свойст
	protected NodeElementPropertyDialog property_dialog;
	
	public IElementPropertyDialog getPropertyDialog() {
		if(property_dialog==null) {
			property_dialog = new NodeElementPropertyDialog();
		}
		ActiveElement new_element = new ActiveElement();
		new_element.setPropertyLike(this);
		property_dialog.setNodeElement(new_element);
	
		return property_dialog;
	}
	
	
	/**
	 * Конструктор по умолчанию необходим
	 * для фабрики классов, иначе не будет происходить вставки элемента в схему
	 */
	public ActiveElement() {
		super();
	}
	
	
	/**
	 * 
	 * @param schema - схема которой принадлежит элемент
	 */
	public ActiveElement(Schema schema) {
		super(schema);
	}

	
	/**
	 * 
	 * @param schema      схема которой принадлежит элемент
	 * @param without_id  признак того что не присваивать новый идентификатор id при создании,
	 *                    используется во время загрузки id потом подставляется из загруженных данных
	 */
	public ActiveElement(Schema schema, boolean without_id) {
		super(schema,without_id);
	}
	
	
	/**
	 * @return Returns the bounds.
	 */
	public String getActiveState() {
		return active_state;
	}

	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ActiveElement prototype) {
		super.setPropertyLike(prototype);	
		//this.setText(prototype.getText());
		//this.setTooltipText(prototype.getTooltipText());
	}
	
	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * и на каждое свойство запускается событие обновления
	 * @param prototype
	 */
	public void modifyPropertyLike(ActiveElement prototype) {
		super.modifyPropertyLike(prototype);
		//this.modifyText(prototype.getText());
		//this.modifyTooltipText(prototype.getTooltipText());
	}
	
	
	
	/**
	 * Sets active state without firing off any event notifications
	 * 
	 * @param act_state
	 *            The active state to set.
	 */
	public void setActiveState(String act_state)	{
		this.active_state = act_state;
	}

	
	/**
	 * If modified, sets active state and fires off event notification
	 * 
	 * @param act_state
	 *            The active state to set.
	 */
	public void modifyActiveState(String act_state)	{
		String oldBounds = this.active_state;
		if (!act_state.equals(oldBounds))	{
			this.active_state = act_state;
			firePropertyChange(ACTIVE_STATE, null, act_state);
		}
	}

	
	/**
	 * If modified, sets active state and fires off event notification
	 */
	public void modifyActiveState()	{
		firePropertyChange(ACTIVE_STATE, null, active_state);
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
		NodeElement.init_prop_descriptors(pda);
		
		System.out.println("STATIC!  ActiveElemnt.init_prop_descriptors()");
		
		PropertyDescriptor pd;
		pd = new TextPropertyDescriptor(ACTIVE_STATE, "Active State:");
		pd.setCategory("State");
		pd.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				if(value instanceof String)
					return null;
				return "Must be string!";
			}
		});
		pda.add(pd);
		//-- Инициализация массива описателей свойств
		//TODO!!! Если изметяется набор свойств в этом класс то необходимо произвести замену и во всех дочерних!
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
		
		if (ACTIVE_STATE.equals(propertyId)) {
			return this.active_state;
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
		if (ACTIVE_STATE.equals(propertyId))	{
			this.modifyActiveState((String) value);
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
		str = "  <ActiveElement id=\""+ getId() +"\">\n";
		str += getPropertyXml();
		str +="  </ActiveElement>\n";
		return str;
	}

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Активность
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void doDefaultAction(NodeElementPart part) {
		if(this.getActiveState().equals(ActiveElement.PASSIVE)) {
			modifyActiveState(ActiveElement.ACTIVE);
			
			TestJob.test(this,PlatformUI.getWorkbench().getDisplay(), part );
		}
		else if(this.getActiveState().equals(ActiveElement.ACTIVE)) {
			
			TestJob.stop();
			modifyActiveState(ActiveElement.PASSIVE);
		}
	}
	
}
