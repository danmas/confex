package net.confex.schema.editor;


import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SchemaClipboard extends Clipboard {

	   /**
	    * The event name used for {@link Clipboard#fireContentsSet()}
	    */
	   public static final String CONTENTS_SET_EVENT = "ContentsSet";

	   protected static SchemaClipboard _instance = new SchemaClipboard();
	   private PropertyChangeSupport listeners = new PropertyChangeSupport( this );

	   /**
	    * Do not allow direct instantiation of a Clipboard
	    */
	   private SchemaClipboard() {
	       super();
	   }

	   /**
	    * Get the default Clipboard
	    * @return - The default Clipboard
	    */
	   public static Clipboard getDefault() {
	       return _instance;
	   }
	   
	   
	     /**
	    * Add a {@link PropertyChangeListener} to this Clipboard
	    * @param l
	    */
	   public void addPropertyChangeListener(PropertyChangeListener l){
	       listeners.addPropertyChangeListener(l);
	   }

	   /**
	    * Remove a {@link PropertyChangeListener} to this Clipboard
	    * @param l
	    */
	   public void removePropertyChangeListener(PropertyChangeListener l){
	       listeners.removePropertyChangeListener(l);
	   }

	   /**
	    * Fires a {@link PropertyChangeEvent} anytime the contents of the <code>Clipboard</code> are set.
	    *
	    */
	   protected void fireContentsSet() {
	       PropertyChangeEvent event = new PropertyChangeEvent(this, CONTENTS_SET_EVENT, null, getContents() );
	       listeners.firePropertyChange( event );
	   }
	   
	   
	     /* (non-Javadoc)
	    * @see org.eclipse.gef.ui.actions.Clipboard#setContents(java.lang.Object)
	    */
	   public void setTextContents(String contents) {
			org.eclipse.swt.dnd.Clipboard cb = new org.eclipse.swt.dnd.Clipboard(null);
			cb.setContents(new Object[] {contents},new Transfer[] {TextTransfer.getInstance()});
			cb.dispose();
			fireContentsSet();
	   }


	   public String getTextContents() {
			org.eclipse.swt.dnd.Clipboard cb = new org.eclipse.swt.dnd.Clipboard(null);
			String contents = (String) cb.getContents(TextTransfer.getInstance());
			cb.dispose();
			return contents;
		}
	   
}
