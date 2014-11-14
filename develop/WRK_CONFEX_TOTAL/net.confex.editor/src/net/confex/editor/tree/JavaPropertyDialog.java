package net.confex.editor.tree;

import java.io.File;

import net.confex.directedit.NodePropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class JavaPropertyDialog   extends NodePropertyDialog {
	
	
	JavaNode element;    
	

	public JavaPropertyDialog(Shell shell, ITreeNode element) {
		super(shell, element);
		this.element = (JavaNode)element;
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof JavaNode) ) {
			System.err.println("!!! Parametr element must be instanceof JavaNode PropertyDialog.setNodeElement()]");
			return; 
		}
		
		this.element = (JavaNode)element;
		
		/*
		String s = this.element.getSrcText();
		this.src_text.setText(s);
		
		this.src_file_name.setText(this.element.getSrcFileName());
		*/
	}
	
	
	protected void initLabel() {
		super.initLabel();
		
		label_src_text.setText(Translator.getString("LABEL_JAVA"));
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+200));
	}
	
	
	protected void initActions() {
		super.initActions();

		initActionsSrcFileButtons();
	}

	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		//GroovyNode tree_node = (GroovyNode)element;
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
	 * Get absolute path to java file for tool tip
	 * @see net.confex.directedit.NodePropertyDialog#getTooltipTextForSrcFileName(java.lang.String)
	 */
	public String getTooltipTextForSrcFileName(String srcFileNameText) {
		String retText = srcFileNameText;
		if (element == null || srcFileNameText == null
				|| srcFileNameText.equals("")) {
			retText = null;
		} else {
			try {
				//element.setSrcFileNameGui(srcFileNameText);
				File f = element.getFile(srcFileNameText);
				retText = "path for save in XML: " + element.getSrcFileNameXml() + "\n"; 
				retText = retText + "Absolute path: " + f.getAbsolutePath();
			} catch (Exception e) {
				// do nothing
			}
		}
		return retText;
	}
	
	
	/**
	 * Get default directory for src_file_name
	 * @see net.confex.directedit.NodePropertyDialog#getDefaultSrcDir()
	 */
	public String getDefaultSrcDir(){
		return "/src/";
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

