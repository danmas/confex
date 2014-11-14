package net.confex.db.directedit;

import net.confex.db.tree.JdbcConnectionNode;
import net.confex.directedit.NodePropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class JdbcConnectionPropertyDialog extends NodePropertyDialog {
	
	protected Label label_url = null;
	protected Text url = null;
	protected Label label_user = null;
	protected Text user = null;
	protected Label label_password = null;
	protected Text password = null;

	protected Label label_state = null;
	protected Text state = null;
	protected Button connect_disconnect = null;
	
	JdbcConnectionNode element;  //  @jve:decl-index=0:
	

	public JdbcConnectionPropertyDialog(Shell shell) {
		super(shell, null);
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof JdbcConnectionNode) ) {
			System.err.println("!!! Parametr element must be instanceof AnyJdbcConnectionNode [PropertyDialog.setNodeElement()]");
			return; 
		}
		this.element = (JdbcConnectionNode)element; 
		this.url.setText(this.element.getUrl());
		this.user.setText(this.element.getUser());
		this.password.setText(this.element.getPassword());
		
		showState();
		
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		JdbcConnectionNode tree_node = (JdbcConnectionNode)element;
		tree_node.setUrl(url.getText());
		tree_node.setUser(user.getText());
		tree_node.setPassword(password.getText());
	}
	
	
	protected void createPart1SShell() {
		createCompositeUrl();
		
		createCompositeState();
	}

	
	private void createCompositeUrl() {
		label_url = new Label(sShell, SWT.NONE);
		label_url.setText("Url:");
		
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
		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		//gridData2.verticalAlignment = GridData.CENTER;
		url = new Text(composite, SWT.BORDER);
		url.setLayoutData(gridData2);
		
		label_user = new Label(composite, SWT.NONE);
		label_user.setText("User:");
		user = new Text(composite, SWT.BORDER);
		
		label_password = new Label(composite, SWT.NONE);
		label_password.setText("Password:");
		password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
	}

	
	private void createCompositeState() {
		label_state = new Label(sShell, SWT.NONE);
		label_state.setText("State:");
		
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
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
		gridData2.verticalAlignment = GridData.CENTER;
		
		state = new Text(composite, SWT.BORDER);
		state.setLayoutData(gridData2);

		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = GridData.CENTER;
		connect_disconnect = new Button(composite, SWT.NORMAL);
		connect_disconnect.setLayoutData(gridData3);
		
		connect_disconnect.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				connectDisconnect();
			}
		});
		connect_disconnect.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				connectDisconnect();			
			}
		});

	}


	protected void showState() {
		if(element.isClosed()) {
			state.setText("Disconnected");
			state.setForeground(new Color(null,255,0,0));
			connect_disconnect.setText("Connect"); 
		} else {
			state.setText("Connected");
			state.setForeground(new Color(null,0,200,0));
			connect_disconnect.setText("Disconnect"); 
		}
		
	}
	
	
	protected void connectDisconnect() {
		if(element.getJdbcConnection().isClosed()) {
			prepareRetOk(element);
			element.createConnection();
		} else {
			element.closeConnection();
		}
		
		showState();
		
		sShell.redraw();
	}
	
	
	protected void initLabel() {
		super.initLabel();
		
		label_url.setText(Translator.getString("LABEL_URL"));
		label_user.setText(Translator.getString("LABEL_USERNAME"));
		label_password.setText(Translator.getString("LABEL_PASSWORD"));
		
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT + 24));
	}
	
	
}
