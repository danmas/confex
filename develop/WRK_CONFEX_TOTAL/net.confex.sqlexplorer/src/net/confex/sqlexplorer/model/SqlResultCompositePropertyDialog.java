package net.confex.sqlexplorer.model;

import net.confex.db.directedit.SqlPropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SqlResultCompositePropertyDialog extends SqlPropertyDialog {
	
	protected Label label_maxrows = null;
	protected Text maxrows = null;

	SqlResultCompositeNode element;  //  @jve:decl-index=0:
	

	public SqlResultCompositePropertyDialog(Shell shell) {
		super(shell);
		//createSShell();
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof SqlResultCompositeNode) ) {
			System.err.println("!!! Parametr element must be instanceof SqlResultCompositeNode PropertyDialog.setNodeElement()]");
			return; 
		}
		this.element = (SqlResultCompositeNode)element;
		String s = this.element.getMaxrows();
		this.maxrows.setText(s);
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		SqlResultCompositeNode tree_node = (SqlResultCompositeNode)element;
		tree_node.setMaxrows(maxrows.getText());
	}
	
	
	protected void initLabel() {
		super.initLabel();
		
		label_maxrows.setText(Translator.getString("LABEL_MAXROWS"));
	}
	
	
	protected void createPart1SShell() {
		super.createPart1SShell();
		/**/
		label_tooltip = new Label(sShell, SWT.NONE);
		label_tooltip.setText("Tooltip:");
		tooltip = new Text(sShell, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		tooltip.setLayoutData(gridData);
		/**/
	}

	
	protected void createPart2SShell() {
		label_maxrows = new Label(sShell, SWT.NONE);
		label_maxrows.setText("Sql:");
		maxrows = new Text(sShell, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		maxrows.setLayoutData(gridData);
		super.createPart2SShell();
	}

}	
	
