package net.confex.schema.tools;

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.palette.ToolEntry;

public class SchemaSelectionToolEntry  extends ToolEntry {

	
	/**
	 * Creates a new SelectionToolEntry.
	 */
	public SchemaSelectionToolEntry() {
		this(null);
	}

	
	/**
	 * Constructor for SelectionToolEntry.
	 * @param label the label
	 */
	public SchemaSelectionToolEntry(String label) {
		this(label, null);
	}

	
	/**
	 * Constructor for SelectionToolEntry.
	 * @param label the label
	 * @param shortDesc the description
	 */
	public SchemaSelectionToolEntry(String label, String shortDesc) {
		super(label, shortDesc, SharedImages.DESC_SELECTION_TOOL_16,
				SharedImages.DESC_SELECTION_TOOL_24, SchemaSelectionTool.class);
		if (label == null || label.length() == 0)
			setLabel(GEFMessages.SelectionTool_Label);
		setUserModificationPermission(PERMISSION_NO_MODIFICATION);
	}

	}
