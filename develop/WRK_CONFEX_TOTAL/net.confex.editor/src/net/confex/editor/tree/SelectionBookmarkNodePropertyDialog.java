package net.confex.editor.tree;

import net.confex.directedit.NodePropertyDialog;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SelectionBookmarkNodePropertyDialog extends NodePropertyDialog {

	//protected Label label_description = null;

	//protected Text description = null;

	protected Label label_resource = null;

	protected Text resource = null;

	protected Label label_path = null;

	protected Text path = null;

	protected Label label_location = null;

	protected Text location = null;

	SelectionBookmarkNode element; // @jve:decl-index=0:
	
	
	public SelectionBookmarkNodePropertyDialog(Shell shell) {
		super(shell, null);
		// createSShell();
	}

	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element);

		if (!(element instanceof SelectionBookmarkNode)) {
			System.err
					.println("!!! Parametr element must be instanceof SelectionBookmarkNode PropertyDialog.setNodeElement()]");
			return;
		}
		this.element = (SelectionBookmarkNode) element;

		//description.setText(this.element.getDescription());
		resource.setText(this.element.getResource());
		path.setText(this.element.getPath());
		location.setText(this.element.getLocation());
	}

	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		SelectionBookmarkNode tree_node = (SelectionBookmarkNode) element;
		//tree_node.setDescription(description.getText());
		tree_node.setResource(resource.getText());
		tree_node.setPath(path.getText());
		tree_node.setLocation(location.getText());
	}

	
	protected void initLabel() {
		super.initLabel();

		//label_description.setText("Description:");
		label_resource.setText("Resource:");
		label_path.setText("Path:");
		label_location.setText("Location:");
		
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+80));
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
		{
			label_resource = new Label(sShell, SWT.NONE);
			resource = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			resource.setLayoutData(gridData);
		}
		{
			label_path = new Label(sShell, SWT.NONE);
			path = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			path.setLayoutData(gridData);
		}
		{
			label_location = new Label(sShell, SWT.NONE);
			location = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData(GridData.FILL_VERTICAL);
			label_location.setLayoutData(gridData);
			GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL 
					| GridData.VERTICAL_ALIGN_FILL);
			gridData2.verticalSpan = 4;
			location.setLayoutData(gridData2);
		}
	}

	
}
