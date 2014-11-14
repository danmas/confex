package net.confex.extension1.tree;

import net.confex.directedit.NodePropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class IfNodePropertyDialog  extends NodePropertyDialog {
	
	protected Label label_var_name = null;
	protected Text var_name = null;
	protected Label label_var_value = null;
	protected Text var_value = null;
	
	IfNode element;  

	public IfNodePropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof IfNode) ) {
			System.err.println("!!! Parametr element must be instanceof IfNode");
			return; 
		}
		this.element = (IfNode)element; 
		this.var_name.setText(this.element.getLeftValue());
		this.var_value.setText(this.element.getRightValue());
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		IfNode tree_node = (IfNode)element;
		tree_node.setLeftValue(var_name.getText().trim());
		tree_node.setRightValue(var_value.getText().trim());
	}

	
	protected void createPart1SShell() {
		label_var_name = new Label(sShell, SWT.NONE);
		label_var_name.setText("If:");
		
		Composite comp = new Composite (sShell, SWT.NONE);
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 3;
		//gridLayout1.marginWidth = 0;
		//gridLayout1.marginHeight = 0;
		//gridLayout1.verticalSpacing = 1;
		//gridLayout1.horizontalSpacing = 1;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		comp.setLayoutData(gridData1);
		comp.setLayout(gridLayout1);
		
		var_name = new Text(comp, SWT.BORDER);
		GridData url_gridData = new GridData();
		url_gridData.horizontalAlignment = GridData.FILL;
		url_gridData.grabExcessHorizontalSpace = true;
		url_gridData.verticalAlignment = GridData.CENTER;
		var_name.setLayoutData(url_gridData);
		
		new Label(comp, SWT.NONE).setText("==");
		
		var_value = new Text(comp, SWT.BORDER);
	}

	
	protected void initLabel() {
		super.initLabel();
		
		//label_var_name.setText(Translator.getString("LABEL_URL_PATH"));
		
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+20));
	}
	
}
