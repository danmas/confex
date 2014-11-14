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
package net.confex.html;

import net.confex.directedit.NodePropertyDialog;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class HtmlCompositePropertyDialog  extends NodePropertyDialog {

	protected Label label_file_name = null;
	protected Text file_name = null;
	//protected Label label_header = null;
	protected Text header = null;
	//protected Label label_content = null;
	protected Text content = null;
	//protected Label label_footer = null;
	protected Text footer = null;
	protected Text fulltext = null;
	
	HtmlComposite element;  
	

	public HtmlCompositePropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof HtmlComposite) ) {
			System.err.println("!!! Parametr element must be instanceof HtmlComposite [PropertyDialog.setNodeElement()]");
			return; 
		}
		this.element = (HtmlComposite)element; 
		this.file_name.setText(this.element.getFileName());
		
		this.header.setText(this.element.getHeader());
		this.content.setText(this.element.getContent());
		this.footer.setText(this.element.getFooter());
		this.fulltext.setText(this.element.getFullHtmltext());
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		HtmlComposite tree_node = (HtmlComposite)element;
		
		tree_node.setFileName(file_name.getText());
		tree_node.setHeader(header.getText());
		tree_node.setFooter(footer.getText());
	}
	
	
	protected void retOk() {
		super.retOk();
		element.saveFile();
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

	
	/*
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
			
	}*/
	
	protected void createPart2SShell() {
		label_src_text = new Label(sShell, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		label_src_text.setLayoutData(gridData);

	    TabFolder tf = new TabFolder(sShell, SWT.BORDER);
		GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData2.verticalSpan = 4;
		tf.setLayoutData(gridData2);

	    TabItem ti1 = new TabItem(tf, SWT.BORDER);
	    ti1.setText("header");
	    Composite c1 = new Composite(tf, SWT.BORDER);
	    c1.setLayout(new FillLayout());
	    header = new Text(c1, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL| SWT.H_SCROLL);
	    ti1.setControl(c1);

	    TabItem ti2 = new TabItem(tf, SWT.BORDER);
	    ti2.setText("content");
	    Composite c2 = new Composite(tf, SWT.BORDER);
	    c2.setLayout(new FillLayout());
	    content = new Text(c2, SWT.READ_ONLY | SWT.BORDER | SWT.MULTI | SWT.V_SCROLL| SWT.H_SCROLL);
	    ti2.setControl(c2);

	    TabItem ti3 = new TabItem(tf, SWT.BORDER);
	    ti3.setText("footer");
	    Composite c3 = new Composite(tf, SWT.BORDER);
	    c3.setLayout(new FillLayout());
	    footer = new Text(c3, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	    ti3.setControl(c3);

	    TabItem ti4 = new TabItem(tf, SWT.BORDER);
	    ti4.setText("full text");
	    Composite c4 = new Composite(tf, SWT.BORDER);
	    c4.setLayout(new FillLayout());
	    fulltext = new Text(c4, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	    ti4.setControl(c4);

		/*
		src_text = new Text(sShell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData2.verticalSpan = 4;
		src_text.setLayoutData(gridData2);
		*/
				
		createSrcButtonPartSShell();
	}
	
	
	
	protected void initLabel() {
		super.initLabel();
		
		label_file_name.setText("file:");
		//label_header.setText("header:");
		//label_content.setText("Содержание:");
		//label_footer.setText("footer:");
		
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+400));
		
	}
	
	
}
