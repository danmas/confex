/*
 * Created on Jul 13, 2004
 */
package net.confex.schema.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.confex.schema.editor.SchemaClipboard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;


/**
 * Represents a Schema in the model. Note that this class also includes
 * diagram specific information (layoutManualDesired and layoutManualAllowed fields)
 * although ideally these should be in a separate model hiearchy 
 * @author Phil Zoio
 * 
 * 
 */
public class Schema extends PropertyAwareObject implements IModelElementContainer {

	//private static Schema instance;
	
	//private static final int delta_size=30;
	//private static final int initial_size=30;
	
	private String name;
	
	//TODO: исследовать вопрос производительности при изменинии размеров массива
	//private Vector child = new Vector(initial_size,delta_size); 
	private ArrayList children = new ArrayList(); 
	
	private ArrayList paste_list = new ArrayList();  //-- список последних вставленных элементов
	public  ArrayList getPasteList() { return paste_list; }
	public List getLastAddedList() { return paste_list; }
	public void clearPasteList() { paste_list.clear(); }
	
	//private Map tablesMap = new HashMap();
	private boolean layoutManualDesired = true;
	private boolean layoutManualAllowed = true;
	//private SimpleContainer mainConainer;        //-- осовной контейнер содержит все элементы схемы

	//-- буфрер обмена получаем из редактора
	private SchemaClipboard clipboard;
	public SchemaClipboard getClipboard() { return clipboard; } 
	public void setClipboard(SchemaClipboard clipboard) { this.clipboard = clipboard; } 
	
	private double zoom = 1.0;

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	
	
	//private Point shift_point = new Point(0,0);    //-- точка сдвига для всех загружаемых элементов
	//public  Point getShiftPoint() { return shift_point; }
	//public  void  setShiftPoint(Point p) { shift_point=p; }
	
	private  int current_id=0;
	
	public int getIdForUsing() {
		return current_id++;
	}
	public void setMaxIdForUsing(int i) {
		if(i>=current_id) current_id = i+1;
	}
	

	public Schema() {
		super();
		this.name = "";
		//mainConainer = new SimpleContainer(this);
		//instance = this;
	}
	
	
	/**
	 * @param name
	 */
	public Schema(String name) {
		this();
		if (name == null)
			throw new NullPointerException("Name cannot be null");
		this.name = name;
		//instance = this;
	}

	
	/**
	 * Сохраняем файл в виде XML 
	 * 
	 * @param file
	 * @param monitor
	 * @throws Exception
	 */
	public void saveAsXml(IFile file,IProgressMonitor monitor) throws Exception {
		String str_xml ="<?xml version=\"1.0\" encoding=\"windows-1251\" ?>\n";
		str_xml += getXml();
		ByteArrayInputStream out = new ByteArrayInputStream(str_xml.getBytes()); 
		file.setContents(out, true, false, monitor);
	}
	
	
	public String getXml() {
		String str_xml = "<Schema>\n";
		//-- сначала сохраняем все узлы 
		for(Iterator iter=children.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof NodeElement) {
				NodeElement label = (NodeElement) obj;
				str_xml += label.getXml();
			}
		}
		//-- затем сохраняем все ребра
		for(Iterator iter=children.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof NodeElement) {
				NodeElement element = (NodeElement) obj;
				str_xml += element.getEdgesXml();
			}
		}
		
		str_xml +="</Schema>\n";

		return str_xml;
	}
	
	
	public void loadAsXml(IFile file) throws Exception {
		//System.out.println("---> Real entering loadAsXml");
		
		// Create a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// Create a DocumentBuilder 
		DocumentBuilder db = dbf.newDocumentBuilder();

		InputStream is=file.getContents(true);
		// Parse the input file to get a Document object
		Document doc = db.parse(is);
		is.close();
		for (int i = 0; i < doc.getDocumentElement().getChildNodes().getLength(); i++) {
			Node node = doc.getDocumentElement().getChildNodes().item(i);
			Schema.parseXml(node,this,this,LOAD_OPERATION);
			if (node.getNodeName().equals("ModelConnection")) {
				Schema.loadEdge(node,this,LOAD_OPERATION);
			}
		}
	}
	
	
	public static void parseXml(Node node, Schema schema, IModelElementContainer container, int operation) {
        //-- проходим по всем ветвям документа
		if (node.getNodeName().equals("SimpleContainer")) {
			//SimpleContainer element = new SimpleContainer(this,true);
			//-- Грузим все как StateContainer
			StateContainer element = new StateContainer(schema,true);
			element.loadFromXml(node,operation);
			container.addModelElement(element);
		} else if (node.getNodeName().equals("StateContainer")) {
			StateContainer element = new StateContainer(schema,true);
			element.loadFromXml(node,operation);
			container.addModelElement(element);
			
		} else if (node.getNodeName().equals("NodeElement")  //FIXME remove it!
			) {
			NodeElement element = new NodeElement(schema,true);
			element.loadFromXml(node,operation);
			container.addModelElement(element);
		} else if (node.getNodeName().equals("ActiveElement")       //FIXME remove it!
			) {
			ActiveElement element = new ActiveElement(schema,true);
			element.loadFromXml(node,operation);
			container.addModelElement(element);
		} else if (node.getNodeName().equals("StickyNote")
				) {
			StickyNote element = new StickyNote(schema,true);
			element.loadFromXml(node,operation);
			container.addModelElement(element);
		//} else if (node.getNodeName().equals("ModelConnection")
		//		) {
		//	loadEdge(node,schema);
		} else if (!node.getNodeName().equals("ModelConnection")) {
			if (!node.getNodeName().equals("#text")) {
				System.err.println("Unknown tag in doc! "+node.getNodeName() );
			}
		}
	}
	
	public static int PASTE_OPERATION = 0;
	public static int LOAD_OPERATION = 1;
	
	/**
	 * Вставить из буфера обмена часть схемы в виде XML.
	 * 
	 * @param contents - текст xml
	 * @param shift - смещение на которое будут смещены елементы 
	 */
	public void pasteAsXml(Point shift,IModelElementContainer container)  throws Exception {
		if(getClipboard()==null) {
			System.err.println("Error: Clipboard not defined!");
			return;
		}
		// Create a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// Create a DocumentBuilder 
		DocumentBuilder db = dbf.newDocumentBuilder();

		InputStream is = new ByteArrayInputStream(getClipboard().getTextContents().getBytes());
		
		container.clearPasteList();

		try {
			Document doc = db.parse(is);
			for (int i = 0; i < doc.getDocumentElement().getChildNodes().getLength(); i++) {
				Node node = doc.getDocumentElement().getChildNodes().item(i);
				parseXml(node,this,container,PASTE_OPERATION);
				if (node.getNodeName().equals("ModelConnection")) {
					Schema.pasteEdge(node,this);
				}
			}
		} catch (SAXParseException ex) {
		}
		
		moveAtShift(container,shift);
		
		is.close();
	}
	
	
	/**
	 * Сдвигаем все элементы из списка на схеме
	 * @param object_list
	 * @param shift     
	 */
	protected void moveAtShift(IModelElementContainer container, Point shift) {
		
		List object_list = container.getLastAddedList();
		
		//-- сначала находим прямоугольник границы всей группы
		Rectangle group_bounds = null;
		for(Iterator iter=object_list.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof NodeElement) {
				NodeElement element = (NodeElement) obj;
				if(group_bounds==null) {
					group_bounds = new Rectangle(element.getBounds());
				} else {
					if(element.getBounds().x<group_bounds.x) {
						group_bounds.width +=  group_bounds.x - element.getBounds().x;
						group_bounds.x = element.getBounds().x;
					}
					if(element.getBounds().y<group_bounds.y) {
						group_bounds.height +=  group_bounds.y - element.getBounds().y;
						group_bounds.y = element.getBounds().y;
					}
				}
			}
		}
		System.err.println(" Location "+shift.x+","+shift.y+"  group_bounds "+group_bounds.x+","+group_bounds.y+"" );
		if(group_bounds!= null) {
			for(Iterator iter=object_list.iterator();iter.hasNext();) {
				Object obj = iter.next(); 	
				if(obj instanceof NodeElement) {
					NodeElement element = (NodeElement) obj;
					element.moveAtShift(new Point(shift.x-group_bounds.x,shift.y-group_bounds.y));
				}
			}
		}
	}
	
	
	/**
	 * Выделить все элементы из списка вставленных
	 *
	 */
	public void selectPasteList(EditPart hostEditPart) {
		for(Iterator iter=paste_list.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof ModelElement) {
				((ModelElement) obj).setSelected(hostEditPart) ;
			}
		}
	}

	
	/**
	 * Грузит все дочерние узлы
	 *
	 */
	protected void loadChildrenAsXml() {
		
	}
	
	
	protected static void loadEdge(Node node,Schema schema,int operation) {
		String s_out_id=node.getAttributes().getNamedItem("id_out").getNodeValue();
		NodeElement out_element=(NodeElement)schema.getChildWithId(Integer.parseInt(s_out_id));
		String s_in_id=node.getAttributes().getNamedItem("id_in").getNodeValue();
		NodeElement in_element=(NodeElement)schema.getChildWithId(Integer.parseInt(s_in_id));
		//-- теперь создаем саму коннекцию
		if(out_element==null || in_element==null) {
			System.err.println(" Can't make connection. Source or target is null.");
			return;
		}
		ModelConnection con=new ModelConnection(schema,true,out_element, in_element);
		con.loadFromXml(node,operation);
	}
	
	
	protected static void pasteEdge(Node node,Schema schema) {
		String s_out_id=node.getAttributes().getNamedItem("id_out").getNodeValue();
		NodeElement out_element=(NodeElement)schema.getChildWithOriginIdFromPaste(Integer.parseInt(s_out_id));
		String s_in_id=node.getAttributes().getNamedItem("id_in").getNodeValue();
		NodeElement in_element=(NodeElement)schema.getChildWithOriginIdFromPaste(Integer.parseInt(s_in_id));
		//-- теперь создаем саму коннекцию
		if(out_element==null || in_element==null) {
			System.err.println(" Can't make connection. Source or target is null.");
			return;
		}
		ModelConnection con=new ModelConnection(schema,true,out_element, in_element);
		con.loadFromXml(node,Schema.PASTE_OPERATION);
	}
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  Container 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Возвращает элемент схемы по id
	 * @param id
	 * @return
	 */
	protected NodeElement getChildWithId(int id) {
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
	 * @param id
	 * @return
	 */
	protected NodeElement getChildWithOriginIdFromPaste(int id) {
		for(Iterator iter=paste_list.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
			if(obj instanceof NodeElement) {
				NodeElement element = (NodeElement) obj;
				NodeElement ret_node = element.getChildWithOriginIdFromPaste(id,paste_list);
				if(ret_node!=null)
					return ret_node;
			}
		}
		return null;
	}
	
	
	/**
	 * Добавляем элемент модели в список по индексу модели 
	 * id для быстрого доступа
	 * @param model_element
	 */
	public void addModelElement(NodeElement model_element)	{
		System.out.println("+ child.size="+children.size()+"   model_element.getId()="+model_element.getId());
		try {
			children.add(model_element);
			paste_list.add(model_element);
			firePropertyChange(CHILD, null, model_element);
		} catch(Exception ex) {
			System.err.println("[Schema.addModelElement()] "+ ex.getMessage());
		}
	}

	
	public void addModelElement(NodeElement model_element, int i) {
		if(i == -1) {
			System.err.println("index = -1 [Schema.addModelElement(NodeElement obj, int i)]");
			return;
		}
		children.add(i, model_element);
		paste_list.add(model_element);
		firePropertyChange(CHILD, null, model_element);
	}
	

	public void removeModelElement(NodeElement obj)
	{
		children.remove(obj);
		firePropertyChange(CHILD, obj, null);
	}

	/**
	 * @return the Tables for the current schema
	 */
	public List getTables()
	{
		return children;
	}

	public List getChildren() {
		return children;
	}
	
	/**
	 * @return the name of the schema
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param layoutManualAllowed
	 *            The layoutManualAllowed to set.
	 */
	public void setLayoutManualAllowed(boolean layoutManualAllowed)
	{
		this.layoutManualAllowed = layoutManualAllowed;
	}

	/**
	 * @return Returns the layoutManualDesired.
	 */
	public boolean isLayoutManualDesired()
	{
		return layoutManualDesired;
	}

	/**
	 * @param layoutManualDesired
	 *            The layoutManualDesired to set.
	 */
	public void setLayoutManualDesired(boolean layoutManualDesired)
	{
		this.layoutManualDesired = layoutManualDesired;
		firePropertyChange(LAYOUT, null, new Boolean(layoutManualDesired));
	}

	/**
	 * @return Returns whether we can lay out individual child manually using the XYLayout
	 */
	public boolean isLayoutManualAllowed()
	{
		return layoutManualAllowed;
	}

}