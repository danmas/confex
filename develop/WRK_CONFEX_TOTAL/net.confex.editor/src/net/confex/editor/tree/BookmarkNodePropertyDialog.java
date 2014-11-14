package net.confex.editor.tree;


import net.confex.directedit.NodePropertyDialog;
import net.confex.tree.ITreeNode;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class BookmarkNodePropertyDialog extends NodePropertyDialog {

	protected Label label_resource = null;
	protected Text resource = null;

	protected Label label_path = null;
	protected Text path = null;

	protected Label label_location = null;
	protected Text location = null;

	protected Label label_selection = null;
	protected Text selection = null;

	protected Label label_marker_id = null;
	protected Text marker_id = null;
	
	BookmarkNode element; 

	private IMarker marker = null;

	
	public BookmarkNodePropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}

	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element);

		if (!(element instanceof BookmarkNode)) {
			System.err
					.println("!!! Parametr element must be instanceof BookmarkNode PropertyDialog.setNodeElement()]");
			return;
		}
		this.element = (BookmarkNode) element;

		resource.setText(this.element.getResource());
		path.setText(this.element.getPath());
		location.setText(this.element.getLocation());
		selection.setText(this.element.getSelection());
		String s = new Long(this.element.getMarkerId()).toString();
		marker_id.setText(s);
	}

	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		BookmarkNode tree_node = (BookmarkNode) element;
		tree_node.setResource(resource.getText());
		tree_node.setPath(path.getText());
		tree_node.setLocation(location.getText());
		tree_node.setSelection(selection.getText());
		marker = null;
		if(!marker_id.getText().trim().equals("")
				&& !marker_id.getText().trim().equals("-1") ) {
			long mrk_id = -1;
			try {
				mrk_id = Long.valueOf(marker_id.getText());
				marker = MarkerUtil.getMarkerById(mrk_id);
			} catch (Throwable t) {
				MessageDialog.openError(sShell, "Error", 
						"Not correct value ["+marker_id.getText()+"]");
				//"Неправильно задана величина ["+marker_id.getText()+"]");
			}
		} else {
			tree_node.setMarkerId(-1);
		}
		if (marker != null) {
			tree_node.setMarkerId(marker.getId());
			try {
				marker.setAttribute(IMarker.MESSAGE, name.getText());
			} catch (CoreException e) {
				System.err.println(" " + e.getMessage());
			}
		}

	}

	
	protected void initLabel() {
		super.initLabel();

		//label_description.setText("Description:");
		label_resource.setText("Resource:");
		label_path.setText("Path:");
		label_location.setText("Line number:");
		label_selection.setText("Selection:");
		
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT + 30));
	}

	
	protected void createPart1SShell() {
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
			resource.setEnabled(true);
		}
		{
			label_path = new Label(sShell, SWT.NONE);
			path = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			path.setLayoutData(gridData);
			path.setEnabled(true);
		}
		{
			label_selection = new Label(sShell, SWT.NONE);
			selection = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			selection.setLayoutData(gridData);
		}
		{
			label_location = new Label(sShell, SWT.NONE);
			
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.numColumns = 3;
			gridLayout1.marginWidth = 0;
			gridLayout1.marginHeight = 0;
			gridLayout1.verticalSpacing = 1;
			gridLayout1.horizontalSpacing = 1;
			GridData gridData1 = new GridData();
			gridData1.horizontalAlignment = GridData.FILL;
			gridData1.grabExcessHorizontalSpace = true;
			Composite composite = new Composite(sShell, SWT.NONE);
			composite.setLayoutData(gridData1);
			composite.setLayout(gridLayout1);
			
			GridData gridData2 = new GridData();
			gridData2.horizontalAlignment = GridData.FILL;
			gridData2.grabExcessHorizontalSpace = true;
			location = new Text(composite, SWT.BORDER);
			location.setLayoutData(gridData2);
			
			label_marker_id = new Label(composite, SWT.NONE);
			label_marker_id.setText("marker id:");
			marker_id = new Text(composite, SWT.BORDER);
		}
	}

	
	public void setMarker(IMarker marker) {
		this.marker = marker;
	}

}
