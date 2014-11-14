/*
 * Created on Jul 15, 2004
 */
package net.confex.schema.editor;

import java.util.ArrayList;
import java.util.List;

import net.confex.schema.dnd.DataElementFactory;
import net.confex.schema.model.ActiveElement;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.StateContainer;
import net.confex.schema.model.StickyNote;
import net.confex.schema.tools.SchemaSelectionToolEntry;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Encapsulates functionality to create the PaletteViewer
 * 
 * @author Eremeev Roman
 */
public class PaletteViewerCreator {

	/** the palette root */
	private PaletteRoot paletteRoot;

	/**
	 * Returns the <code>PaletteRoot</code> this editor's palette uses.
	 * 
	 * @return the <code>PaletteRoot</code>
	 */
	public PaletteRoot createPaletteRoot() {
		// create root
		paletteRoot = new PaletteRoot();

		// a group of default control tools
		PaletteGroup controls = new PaletteGroup("Controls");
		paletteRoot.add(controls);

		// the selection tool
		ToolEntry tool = new SchemaSelectionToolEntry();
		controls.add(tool);

		// use selection tool as default entry
		paletteRoot.setDefaultEntry(tool);

		// the marquee selection tool
		controls.add(new MarqueeToolEntry());

		// a separator
		PaletteSeparator separator = new PaletteSeparator(
				ConfexDiagramPlugin.PLUGIN_ID + ".palette.seperator");
		separator
				.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		controls.add(separator);

		controls.add(new ConnectionCreationToolEntry("Connection",
				"Create Connections", null, AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/connection16.gif"), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/connection24.gif")));

		PaletteDrawer drawer = new PaletteDrawer("Component",
				AbstractUIPlugin.imageDescriptorFromPlugin(
						ConfexDiagramPlugin.PLUGIN_ID, "icons/connection.gif"));

		List entries = new ArrayList();

		CombinedTemplateCreationEntry myLabelEntry = new CombinedTemplateCreationEntry(
				"Element", "Create a new Element", NodeElement.class,
				new DataElementFactory(NodeElement.class), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/table.gif"), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/table.gif"));

		CombinedTemplateCreationEntry myActiveEntry = new CombinedTemplateCreationEntry(
				"Active Element", "Create a new Active Element",
				ActiveElement.class,
				new DataElementFactory(ActiveElement.class), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/table.gif"), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/table.gif"));

		CombinedTemplateCreationEntry labelEntry = new CombinedTemplateCreationEntry(
				"Sticky Note", "Create a new label", StickyNote.class,
				new DataElementFactory(StickyNote.class), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/label16.gif"), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/label24.gif"));

		/*
		 * CombinedTemplateCreationEntry simpleContainerEntry = new
		 * CombinedTemplateCreationEntry("New SimpleContainer",
		 * "Create a new container for elements.", SimpleContainer.class, new
		 * DataElementFactory(SimpleContainer.class),
		 * AbstractUIPlugin.imageDescriptorFromPlugin
		 * (ConfexDiagramPlugin.PLUGIN_ID, "icons/simpleContainer.gif"),
		 * AbstractUIPlugin
		 * .imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID,
		 * "icons/simpleContainer.gif"));
		 */
		CombinedTemplateCreationEntry stateContainerEntry = new CombinedTemplateCreationEntry(
				"Container",
				"Create a new state container for elements.",
				StateContainer.class, new DataElementFactory(
						StateContainer.class), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/container.png"), AbstractUIPlugin
						.imageDescriptorFromPlugin(
								ConfexDiagramPlugin.PLUGIN_ID,
								"icons/container.png"));

		entries.add(myLabelEntry);
		entries.add(myActiveEntry);
		entries.add(labelEntry);
		// entries.add(simpleContainerEntry);
		entries.add(stateContainerEntry);

		drawer.addAll(entries);

		paletteRoot.add(drawer);

		// todo add your palette drawers and entries here
		return paletteRoot;
	}

	/**
	 * @return Returns the paletteRoot.
	 */
	public PaletteRoot getPaletteRoot() {
		return paletteRoot;
	}

}