package net.confex.schema.model;

import java.util.Vector;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;


public class StickyNote extends NodeElement {

	
	
	public StickyNote() {
		super();
		init();
	}
	
	
	public StickyNote(Schema schema) {
		super(schema);
		init();
	}

	
	public StickyNote(Schema schema, boolean without_id) {
		super(schema,without_id);
		init();
	}
	
	
	protected void init() {
		this.setBackColor(new RGB(255,255,234));
		this.setForeColor(new RGB(50,50,255));
	}
	
	
	public void setLabelContents(String s){
		modifyText(s);
	}

	
	public String getLabelContents(){
		return text;
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

		System.out.println("STATIC!  StickyNote.init_prop_descriptors()");
		
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
		str = "  <StickyNote id=\""+ getId() +"\">\n";
		str += getPropertyXml();
		str +="  </StickyNote>\n";
		return str;
	}
	
	
}
