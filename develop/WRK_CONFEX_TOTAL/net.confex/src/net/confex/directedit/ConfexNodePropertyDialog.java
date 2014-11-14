package net.confex.directedit;

import net.confex.Constants;
import net.confex.translations.Translator;
import net.confex.tree.ConfexNode;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ConfexNodePropertyDialog  extends NodePropertyDialog {
	
	protected Label label_confex_filename = null;
	protected Text confex_filename = null;

	protected Button browseDialog = null;

	ConfexNode element;  

	
	public ConfexNodePropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}

	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof ConfexNode) ) {
			System.err.println("!!! Parametr element must be instanceof ConfexNode ");
			return; 
		}
		this.element = (ConfexNode)element; 
		this.confex_filename.setText(this.element.getConfexFilename());
	}


	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		ConfexNode tree_node = (ConfexNode)element;
		tree_node.setConfexFilename(confex_filename.getText());
		//tree_node.loadFile();
	}


	protected void initLabel() {
		super.initLabel();
		label_confex_filename.setText(Translator.getString("LABEL_CONFEX_FILENAME"));
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+20));
	}


	protected void createPart1SShell() {

		label_confex_filename = new Label(sShell, SWT.NONE);
		
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 5;
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
		
		{
		confex_filename = new Text(composite, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		confex_filename.setLayoutData(gridData);
		}
		
		{
		browseDialog = new Button(composite, SWT.NONE);
		browseDialog.setText("…");
		GridData gridData = new GridData();
		//ok_gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.CENTER;
		browseDialog.setLayoutData(gridData);
		}

	}

	
	protected void initActions() {
		super.initActions();

		browseDialog.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected( final SelectionEvent event ) {
		    	  select_path();
		        }
		      } );
/*		
		browseDialog.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				select_path();
			}
		});
		browseDialog.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				select_path();
			}
		});*/
	}
		
	
	protected void select_path() {
		FileDialog fileDialog = new FileDialog(sShell);
		
		fileDialog.setFileName(confex_filename.getText());
		fileDialog.setFilterExtensions( Constants.CONFEX_EXTENSIONS );
	
    	String file_name = fileDialog.open();
    	if(file_name==null) {
    		return;
    	}
    	if(file_name.indexOf('.')==-1) {
    		file_name += "." + Constants.DEFAULT_CONFEX_FILE_EXTENSION;
    	}
    	confex_filename.setText(file_name);
    	if(name.getText()==null || name.getText().equals("")) {
    		name.setText(file_name);
    	}
	}
	
}
