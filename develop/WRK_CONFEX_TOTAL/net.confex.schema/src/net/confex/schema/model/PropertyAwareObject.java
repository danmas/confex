/*
 * Created on Jul 15, 2004
 */
package net.confex.schema.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Provides base class support for model objects to participate in event handling framework
 * @author Roman Eremeev
 */
public abstract class PropertyAwareObject extends Object implements Serializable {

	public static final String CHILD = "CHILD";
	public static final String REORDER = "REORDER";
	public static final String BOUNDS = "BOUNDS";
	public static final String INPUT = "INPUT";
	public static final String OUTPUT = "OUTPUT";
	public static final String NAME = "NAME";
	public static final String LAYOUT = "LAYOUT";
	//-- saved in xml file properties 
	public static final String TEXT = "text";
	public static final String TOOLTIP_TEXT = "tooltip_text";
	public static final String ID = "id";
	public static final String X_POS = "x_pos";
	public static final String Y_POS = "y_pos";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String FORE_COLOR = "fore_color"; 	
	public static final String BACK_COLOR = "back_color"; 	
	public static final String SELECTED = "SELECTED"; 	
	
	protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	protected PropertyAwareObject()	{
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}
	
	protected void firePropertyChange(String prop, Object old, Object newValue)	{
		listeners.firePropertyChange(prop, old, newValue);
	}	

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException 	{
		in.defaultReadObject();
		listeners = new PropertyChangeSupport(this);
	}

}

