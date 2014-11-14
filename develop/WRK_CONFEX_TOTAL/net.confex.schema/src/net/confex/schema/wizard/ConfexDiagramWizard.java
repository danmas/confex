/*
 * Created on Jul 27, 2004
 */
package net.confex.schema.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard to create new schema diagram
 * @author Phil Zoio
 */
public class ConfexDiagramWizard extends Wizard implements INewWizard
{

	private ConfexDiagramWizardPage schemaPage = null;
	private IStructuredSelection selection;
	private IWorkbench workbench;

	public ConfexDiagramWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	public void addPages() {
		schemaPage = new ConfexDiagramWizardPage(workbench, selection);
		addPage(schemaPage);
	}

	public void init(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		workbench = aWorkbench;
		selection = currentSelection;
	}

	public boolean performFinish()
	{
		return schemaPage.finish();
	}

}