/*******************************************************************************
 * Copyright (c) 2006 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package net.confex.directedit;

import net.confex.translations.Translator;
import net.confex.tree.ExecTreeNode;
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

public class ExecPropertyDialog  extends NodePropertyDialog {
	
	protected Label label_dir = null;
	protected Text dir = null;
	protected Label label_command = null;
	protected Text command = null;
	protected Label label_arguments = null;
	protected Text arguments = null;
	protected Button browseDialog = null;

	
	ExecTreeNode element;  

	private String[] extensions = new String[] {"*.exe","*.com","*.bat","*.*"};
	
	public ExecPropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof ExecTreeNode) ) {
			System.err.println("!!! Parametr element must be instanceof ExecTreeNode [PropertyDialog.setNodeElement()]");
			return; 
		}
		this.element = (ExecTreeNode)element; 
		this.dir.setText(this.element.getWorkDir());
		this.command.setText(this.element.getCommand());
		this.arguments.setText(this.element.getArguments());
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		ExecTreeNode tree_node = (ExecTreeNode)element;
		tree_node.setWorkDir(dir.getText());
		tree_node.setCommand(command.getText());
		tree_node.setArguments(arguments.getText());
	}

	
	protected void createPart1SShell() {
		/*
		label_command = new Label(sShell, SWT.NONE);
		//label_command.setText("Command:");
		command = new Text(sShell, SWT.BORDER);
		GridData cmd_gridData = new GridData();
		cmd_gridData.horizontalAlignment = GridData.FILL;
		cmd_gridData.grabExcessHorizontalSpace = true;
		cmd_gridData.verticalAlignment = GridData.CENTER;
		command.setLayoutData(cmd_gridData);
		*/
		label_command = new Label(sShell, SWT.NONE);
		
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
		command = new Text(composite, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		command.setLayoutData(gridData);
		}
		{
			browseDialog = new Button(composite, SWT.NONE);
			browseDialog.setText("…");
			GridData gridData = new GridData();
			//ok_gridData.horizontalAlignment = GridData.FILL;
			gridData.verticalAlignment = GridData.CENTER;
			browseDialog.setLayoutData(gridData);
			}
		
		
		label_arguments = new Label(sShell, SWT.NONE);
		//label_command.setText("Command:");
		arguments = new Text(sShell, SWT.BORDER);
		GridData cmd_arguments = new GridData();
		cmd_arguments.horizontalAlignment = GridData.FILL;
		cmd_arguments.grabExcessHorizontalSpace = true;
		cmd_arguments.verticalAlignment = GridData.CENTER;
		arguments.setLayoutData(cmd_arguments);

		label_dir = new Label(sShell, SWT.NONE);
		//label_dir.setText("Dir:");
		dir = new Text(sShell, SWT.BORDER);
		GridData dir_gridData = new GridData();
		dir_gridData.horizontalAlignment = GridData.FILL;
		dir_gridData.grabExcessHorizontalSpace = true;
		dir_gridData.verticalAlignment = GridData.CENTER;
		dir.setLayoutData(dir_gridData);
		
	}

	
	/**
	 * Устанавливает размеры окна свойств
	 */
	protected void createPartSetShellSize() {
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+60));
	}

	
	protected void initLabel() {
		super.initLabel();
		
		label_dir.setText(Translator.getString("LABEL_DIR"));
		label_command.setText(Translator.getString("LABEL_COMMAND")); //Constants.NODEPROPERTYDIALOG_COMMAND_LABEL);
		label_arguments.setText(Translator.getString("LABEL_ARGS")); //Constants.NODEPROPERTYDIALOG_ARGUMENTS_LABEL);
	}


	protected void initActions() {
		super.initActions();

		browseDialog.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected( final SelectionEvent event ) {
		    	  select_path();
		        }
		      } );
		
		/*browseDialog.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
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
    	
		fileDialog.setFilterPath(command.getText());
		fileDialog.setFilterExtensions(extensions);
	
    	String  file = fileDialog.open();
    	if(file==null) {
    		//System.err.println("Файл не выбран!");
    		return;
    	}
    	command.setText(file);
    	if(name.getText()==null || name.getText().equals("")) {
    		name.setText(file);
    	}
	}
	
	public Text getArguments() {
		return arguments;
	}


	public void setArguments(Text arguments) {
		this.arguments = arguments;
	}
	

}
