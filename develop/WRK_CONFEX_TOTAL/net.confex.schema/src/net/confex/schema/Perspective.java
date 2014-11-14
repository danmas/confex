package net.confex.schema;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


public class Perspective implements IPerspectiveFactory {

	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET); 
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE); 
		//layout.addStandaloneView(IPageLayout.ID_OUTLINE,  false, IPageLayout.LEFT, 1.0f, editorArea);
		layout.addStandaloneView(IPageLayout.ID_RES_NAV,  false, IPageLayout.LEFT, 1.0f, editorArea);
		//layout.addStandaloneView(IPageLayout.ID_NAVIGATE_ACTION_SET,  false, IPageLayout.LEFT, 1.0f, editorArea);
		//layout.addShowViewShortcut(IPageLayout.ID_NAVIGATE_ACTION_SET);
	}

	
}
