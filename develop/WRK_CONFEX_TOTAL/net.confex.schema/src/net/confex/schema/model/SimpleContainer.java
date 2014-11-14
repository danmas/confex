package net.confex.schema.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.w3c.dom.Node;


/**
 * Простой контейнер для элементов NodeElement 
 * иницирует след события:
 * 	- CHILD
 *  - REORDER
 * 
 * @author Roman_Eremeev
 */

public class SimpleContainer extends ActiveElement implements IModelElementContainer {

	protected ArrayList children = new ArrayList();

	private ArrayList paste_list = new ArrayList();  //-- список последних вставленных элементов
	public  ArrayList getPasteList() { return paste_list; }
	public List getLastAddedList() { return paste_list; }
	public void clearPasteList() { paste_list.clear(); }

	
	/**
	 * Конструктор по умолчанию необходим
	 * для фабрики классов, иначе не будет происходить вставки элемента в схему!!!
	 */
	public SimpleContainer() {
		super();
	}
	
	
	/**
	 * 
	 * @param schema - схема которой принадлежит элемент
	 */
	public SimpleContainer(Schema schema) {
		super(schema);
	}

	
	/**
	 * 
	 * @param schema      схема которой принадлежит элемент
	 * @param without_id  признак того что не присваивать новый идентификатор id при создании,
	 *                    используется во время загрузки id потом подставляется из загруженных данных
	 */
	public SimpleContainer(Schema schema, boolean without_id) {
		super(schema,without_id);
	}
	
	
	/**
	 * Возвращает элемент схемы по id
	 * @param id
	 * @return
	 */
	protected NodeElement getChildWithId(int id) {
		if(id==this.id)
			return this;
		for(Iterator iter=children.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof NodeElement) {
				NodeElement element = (NodeElement) obj;
				NodeElement ret_node = element.getChildWithId(id);
				if(ret_node!=null)
					return ret_node;
			}
		}
		return null;
	}

	
	/**
	 * Возвращает элемент схемы по origin_id
	 * @param origin_id
	 * @return
	 */
	protected NodeElement getChildWithOriginIdFromPaste(int origin_id,ArrayList list) {
		if(origin_id==this.origin_id)
			return this;
		for(Iterator iter=paste_list.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof NodeElement) {
				NodeElement element = (NodeElement) obj;
				NodeElement ret_node = element.getChildWithOriginIdFromPaste(id,list);
				if(ret_node!=null)
					return ret_node;
			}
		}
		return null;
	}

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Property
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
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

		//System.out.println("STATIC!  SimpleContainer.init_prop_descriptors()");
		
		//-- add validators here
		
		int i= NodeElement.getPropertyDescriptorCount(); 

		property_descriptor = new IPropertyDescriptor[i];
		
		for(int j=0;j<i;j++) {
			property_descriptor[j]=(IPropertyDescriptor)pda.get(j);
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
		str = "  <SimpleContainer id=\""+ getId() +"\">\n";
		str += getPropertyXml();
		str += get_child_xml();
		str +="  </SimpleContainer>\n";
		return str;
	}

	
	/**
	 * Возвращает xml представление для дочерних элементов содержащихся в контейнере 
	 * @return
	 */
	protected String get_child_xml() {
		String str = "    <children>\n";
		//-- сначала сохраняем все узлы
		for(Iterator iter=children.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof NodeElement) {
				NodeElement element = (NodeElement) obj;
				str += element.getXml();
			}
		}
		str += "    </children>\n";
		return str;
	}

	
	public String getEdgesXml() {
		//-- сохраняем все исходящие ребра данного контейнера
		String str= super.getEdgesXml();
		//-- для всех узлов получаем xml представление их связей
		for(Iterator iter=children.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof NodeElement) {
				NodeElement element = (NodeElement) obj;
				str += element.getEdgesXml();
			}
		}
		return str;
	}
	
	
	public void loadFromXml(Node node,int operation){
		//-- грузим свойства 
		super.loadFromXml(node,operation);
		//-- грузим дочерние
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node child = node.getChildNodes().item(i);
			if (child.getNodeName().equals("children")) {
				loadChildrenFromXml(child,operation);
			}
		}
	}
	
	
	/**
	 * Грузит все дочерние
	 * @param node
	 */
	protected void loadChildrenFromXml(Node node, int operation) {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node nd = node.getChildNodes().item(i);
			Schema.parseXml(nd,getSchema(),this,operation);
			if (nd.getNodeName().equals("ModelConnection")) {
				Schema.loadEdge(nd,getSchema(),operation);
			}
		}
	}

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Container 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void addModelElement(NodeElement Child) {
		if (children.contains(Child))	{
			throw new IllegalArgumentException("Child already present");
		}
		children.add(Child);
		paste_list.add(Child);
		firePropertyChange(CHILD, null, Child);
	}

	
	public void addModelElement(NodeElement Child, int index)	{
		if (children.contains(Child))
		{
			throw new IllegalArgumentException("Child already present");
		}
		children.add(index, Child);
		paste_list.add(Child);
		firePropertyChange(CHILD, null, Child);
	}

	
	public void removeModelElement(NodeElement Child)	{
		children.remove(Child);
		firePropertyChange(CHILD, Child, null);
	}

	
	public void switchChild(NodeElement Child, int index)	{
		children.remove(Child);
		children.add(index, Child);
		firePropertyChange(REORDER, this, Child);
	}

	
	public List getChildren() {
		return children;
	}
	
	
}

