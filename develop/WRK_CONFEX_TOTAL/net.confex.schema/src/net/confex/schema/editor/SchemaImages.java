package net.confex.schema.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class SchemaImages {

	public static Image BOOKMARK;
	public static Image ADD_BOOKMARK;

	//public static ImageDescriptor ADD_BOOKMARK_IMAGEDESCR;

	public static Image WEBDOC;
	public static Image ANTDOC;
	public static Image JAVABOOKMARK;
	public static Image STANDART_STATE;
	public static Image COMPACT_STATE;
	
	static {
		// InputStream stream =
		// SchemaDiagramPlugin.class.getResourceAsStream("icons/bookmark.gif");
		// BOOKMARK = new Image(null, stream);
		// try {
		// stream.close();
		// } catch (IOException ioe) {
		// }
		try {
			//System.out.println("Загружаем изображения.");
			ImageDescriptor imaged;  
			imaged = AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID,"icons/code_bookmark.gif");
			BOOKMARK = (Image) imaged.createResource(null);

			imaged = AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID,"icons/addbkmrk_co.gif");
			ADD_BOOKMARK = (Image) imaged.createResource(null);

			imaged = AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID,"icons/webdoc.png");
			WEBDOC = (Image) imaged.createResource(null);

			imaged = AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID,"icons/antdoc.png");
			ANTDOC = (Image) imaged.createResource(null);
			
			imaged = AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID,"icons/code_bookmark.gif");
			JAVABOOKMARK = (Image) imaged.createResource(null);
			
			imaged = AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID,"icons/compact_state.png");
			COMPACT_STATE = (Image) imaged.createResource(null);
			
			imaged = AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID,"icons/standart_state.png");
			STANDART_STATE = (Image) imaged.createResource(null);
			
		} catch (Exception ex) {
			System.out.println("Error images were not loaded. "
					+ ex.getMessage());
		}

	}

}
