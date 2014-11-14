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

//import net.confex.tree.FolderNode;
import net.confex.tree.CompositeFormViewNode;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

public class FormViewNodePropertyDialog  extends NodePropertyDialog {
	
	//protected Label label_path = null;
	//protected Text path = null;
	protected Button browseDialog = null;
	
	CompositeFormViewNode element;  

	public FormViewNodePropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
		if(new_flg) {
			//select_path();
		}
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof CompositeFormViewNode) ) {
			System.err.println("!!! Parametr element must be instanceof CompositeFormViewNode [setNodeElement()]");
			return; 
		}
		this.element = (CompositeFormViewNode)element; 
		//this.path.setText(this.element.getPath());
		//-- создаем тестовый элемент
		//new_element = new UrlNode(((UrlNode)element).getConfigTree()); 
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		//CompositeFormViewNode tree_node = (CompositeFormViewNode)element;
		//tree_node.setPath(path.getText());
	}

	
	protected void createPart1SShell() {
		/*
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
		*/

	}

	
	protected void initLabel() {
		super.initLabel();
		
		//label_path.setText("Path:");
		
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT));
	}
	

	protected void initActions() {
		super.initActions();
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
		});
		*/
	}
		
	
	/*
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
	}*/
}
