package net.confex.app.perspectives;

import net.confex.app.views.View;
import net.confex.app.wizards.TreeWizard;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "net.confex.app.perspective";

	public void createInitialLayout(IPageLayout layout) {

		layout.addNewWizardShortcut(TreeWizard.ID);

		String editorArea = layout.getEditorArea();

		layout.addView(View.ID, IPageLayout.LEFT, 0.3f, editorArea);
	}
}
