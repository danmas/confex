package net.confex.extension1.tree;

import net.confex.directedit.NodePropertyDialog;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CompositeDocPropertyDialog extends NodePropertyDialog {

	protected Label label_file_name = null;
	protected Text file_name = null;
	protected Label label_header = null;
	protected Text header = null;
	protected Label label_content = null;
	protected Text content = null;
	protected Label label_footer = null;
	protected Text footer = null;
	
	CompositeDoc element;  
	

	public CompositeDocPropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof CompositeDoc) ) {
			System.err.println("!!! Parametr element must be instanceof HtmlComposite [PropertyDialog.setNodeElement()]");
			return; 
		}
		this.element = (CompositeDoc)element; 
		this.file_name.setText(this.element.getFileName());
		
		this.header.setText(this.element.getHeader());
		this.content.setText(this.element.getContent());
		this.footer.setText(this.element.getFooter());
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		CompositeDoc tree_node = (CompositeDoc)element;
		
		tree_node.setFileName(file_name.getText());
		tree_node.setHeader(header.getText());
		tree_node.setFooter(footer.getText());
	}
	
	
	protected void retOk() {
		super.retOk();
		element.saveFile();
	}

	
	protected void runTest() {
		prepareRetOk(element);
		element.makeContent();
		content.setText(element.getContent());
		//element.run(view);
	}
	
	
	protected void createPart1SShell() {
		{
			label_tooltip = new Label(sShell, SWT.NONE);
			tooltip = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			tooltip.setLayoutData(gridData);
		}
		{
			label_file_name = new Label(sShell, SWT.NONE);
			file_name = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			file_name.setLayoutData(gridData);
		}
	}

	
	protected void createPart2SShell() {
		{
			label_header = new Label(sShell, SWT.NONE);
			GridData gridData = new GridData(GridData.FILL_VERTICAL);
			label_header.setLayoutData(gridData);

			header = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			//GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL 
			//		| GridData.VERTICAL_ALIGN_FILL);
			GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
			//gridData2.verticalSpan = 4;
			header.setLayoutData(gridData2);
		}
		{
			label_content = new Label(sShell, SWT.NONE);
			GridData gridData = new GridData(GridData.FILL_VERTICAL);
			label_content.setLayoutData(gridData);

			content = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			//GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL 
			//		| GridData.VERTICAL_ALIGN_FILL);
			GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
			//gridData2.verticalSpan = 4;
			content.setLayoutData(gridData2);
			content.setEditable(false);
			content.setForeground(new Color(null,0,0,250));
		}
		{
			label_footer = new Label(sShell, SWT.NONE);
			GridData gridData = new GridData(GridData.FILL_VERTICAL);
			label_footer.setLayoutData(gridData);

			footer = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			//GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL 
			//		| GridData.VERTICAL_ALIGN_FILL);
			GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
			//gridData2.verticalSpan = 4;
			footer.setLayoutData(gridData2);
			}
			
	}
	
	
	protected void initLabel() {
		super.initLabel();
		
		label_file_name.setText("file:");
		label_header.setText("header:");
		label_content.setText("Содержание:");
		label_footer.setText("footer:");
		
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+480));
		
	}
	
	

}
