package net.confex.groovy.model;

import java.io.File;

import net.confex.application.ConfexPlugin;
import net.confex.directedit.NodePropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;
import net.confex.utils.Utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;

public class GroovyPropertyDialog  extends NodePropertyDialog {
	
	
	GroovyNode element;  //  @jve:decl-index=0:
	

	public GroovyPropertyDialog(Shell shell) {
		super(shell, null);
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof GroovyNode) ) {
			System.err.println("!!! Parametr element must be instanceof GroovyNode PropertyDialog.setNodeElement()]");
			return; 
		}
		
		this.element = (GroovyNode)element;
		
		/*
		String s = this.element.getSrcText();
		this.src_text.setText(s);
		
		this.src_file_name.setText(this.element.getSrcFileName());
		*/
	}
	
	
	protected void initLabel() {
		super.initLabel();
		
		label_src_text.setText(Translator.getString("LABEL_GROOVY"));
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+200));
	}
	
	
	protected void initActions() {
		super.initActions();

		initActionsSrcFileButtons();
	}

	
	protected void createPart1SShell() {
		{
			label_tooltip = new Label(sShell, SWT.NONE);
			label_tooltip.setText("Tooltip:");
			tooltip = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			tooltip.setLayoutData(gridData);
		}
			createSrcFilePartSShell();
			
			createTestFilePartSShell();
	}

	
	/**
	 * Get default directory for src_file_name
	 * @see net.confex.directedit.NodePropertyDialog#getDefaultSrcDir()
	 */
	public String getDefaultSrcDir(){
		return "/groovy/";
	}
	
	
	protected void createPart2SShell() {
		label_src_text = new Label(sShell, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		label_src_text.setLayoutData(gridData);
		src_text = new Text(sShell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		//GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL 
		//		| GridData.VERTICAL_ALIGN_FILL);
		GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData2.verticalSpan = 4;
		src_text.setLayoutData(gridData2);
				
		createSrcButtonPartSShell();
	}

	
}

