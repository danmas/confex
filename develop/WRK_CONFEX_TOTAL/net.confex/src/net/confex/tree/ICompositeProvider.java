package net.confex.tree;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public interface ICompositeProvider {

	public void makeComposite(Composite parent, ViewPart viewPart, IProgressMonitor monitor);
	
	public void refreshComposite();
	
	public void disposeComposite();

}
