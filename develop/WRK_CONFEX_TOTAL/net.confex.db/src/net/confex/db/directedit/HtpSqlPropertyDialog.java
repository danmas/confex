
package net.confex.db.directedit;

import net.confex.db.tree.HtmlSqlTableTreeObject;
import net.confex.directedit.NodePropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class HtpSqlPropertyDialog extends NodePropertyDialog {
	
	protected Label label_sql = null;
	protected Text sql = null;

	HtmlSqlTableTreeObject element;  //  @jve:decl-index=0:
	

	public HtpSqlPropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof HtmlSqlTableTreeObject) ) {
			System.err.println("!!! Parametr element must be instanceof HtpSqlTableTreeObject PropertyDialog.setNodeElement()]");
			return; 
		}
		this.element = (HtmlSqlTableTreeObject)element;
		String s = this.element.getSql();
		this.sql.setText(s);
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		HtmlSqlTableTreeObject tree_node = (HtmlSqlTableTreeObject)element;
		tree_node.setSql(sql.getText());
	}

	
	protected void initLabel() {
		super.initLabel();
		
		label_sql.setText(Translator.getString("LABEL_SQL"));
	}
	
	
	protected void createPart1SShell() {
		label_tooltip = new Label(sShell, SWT.NONE);
		label_tooltip.setText("Tooltip:");
		tooltip = new Text(sShell, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		tooltip.setLayoutData(gridData);
	}

	
	protected void createPart2SShell() {
		label_sql = new Label(sShell, SWT.NONE);
		label_sql.setText("Sql:");
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		label_sql.setLayoutData(gridData);
		sql = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL 
				| GridData.VERTICAL_ALIGN_FILL);
		gridData2.verticalSpan = 4;
		sql.setLayoutData(gridData2);
	}
	
	
}
