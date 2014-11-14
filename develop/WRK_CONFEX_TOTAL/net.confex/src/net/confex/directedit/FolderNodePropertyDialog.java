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

import net.confex.tree.FolderNode;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FolderNodePropertyDialog extends NodePropertyDialog {
	
	protected Label label_path = null;
	protected Text path = null;
	protected Button browseDialog = null;
	
	FolderNode element;  

	public FolderNodePropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
		if(new_flg) {
			select_path();
		}
	}
	
	
	protected void initLabel() {
		super.initLabel();
		
		label_path.setText("Path:");
		
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+20));
	}
	

	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof FolderNode) ) {
			System.err.println("!!! Parametr element must be instanceof FolderNode [setNodeElement()]");
			return; 
		}
		this.element = (FolderNode)element; 
		this.path.setText(this.element.getPath());
		//-- создаем тестовый элемент
		//new_element = new UrlNode(((UrlNode)element).getConfigTree()); 
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		FolderNode tree_node = (FolderNode)element;
		tree_node.setPath(path.getText());
	}

	
	protected void createPart1SShell() {

		label_path = new Label(sShell, SWT.NONE);
		
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
		path = new Text(composite, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		path.setLayoutData(gridData);
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
	}
		
	
	protected void select_path() {
		DirectoryDialog fileDialog = new DirectoryDialog(sShell);
    	
		fileDialog.setFilterPath(path.getText());
		//fileDialog.s .setFilterExtensions();
	
    	String  file = fileDialog.open();
    	if(file==null) {
    		//System.err.println("Файл не выбран!");
    		return;
    	}
    	path.setText(file);
    	if(name.getText()==null || name.getText().equals("")) {
    		name.setText(file);
    	}
	}

		
}
