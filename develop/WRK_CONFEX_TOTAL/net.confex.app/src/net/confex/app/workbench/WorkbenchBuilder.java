package net.confex.app.workbench;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class WorkbenchBuilder extends WorkbenchAdvisor {

	private static final String DEFAULT_PERSPECTIVE_ID = "net.confex.app.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new WorkbenchWindowBuilder(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return DEFAULT_PERSPECTIVE_ID;
	}

}
