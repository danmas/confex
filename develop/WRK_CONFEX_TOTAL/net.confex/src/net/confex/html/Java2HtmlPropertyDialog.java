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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Java2HtmlPropertyDialog   extends NodePropertyDialog {

	//protected Label label_file_name = null;
	//protected Text file_name = null;
	protected Label label_java_text = null;
	protected Text java_text = null;
	//protected Label label_html_text = null;
	//protected Text html_text = null;
	protected Label label_header = null;
	protected Text header = null;
	protected Label label_footer = null;
	protected Text footer = null;
	
	protected Text selected_words;
	protected Text prefix;
	protected Text postfix;
	
	
	Java2HtmlNode element;  
	

	public Java2HtmlPropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof Java2HtmlNode) ) {
			System.err.println("!!! Parametr element must be instanceof Java2HtmlNode [PropertyDialog.setNodeElement()]");
			return; 
		}
		this.element = (Java2HtmlNode)element; 
		//this.file_name.setText(this.element.getFileName());
		
		this.java_text.setText(this.element.getCodeText());
		//this.html_text.setText(this.element.getFullHtmltext());
		this.header.setText(this.element.getHeader());
		this.footer.setText(this.element.getFooter());
		
		this.selected_words.setText(this.element.getSelectedWords());
		this.prefix.setText(this.element.getPrefix());
		this.postfix.setText(this.element.getPostfix());
	}
	
	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		Java2HtmlNode tree_node = (Java2HtmlNode)element;
		
		//tree_node.setFileName(file_name.getText());
		tree_node.setCodeText(java_text.getText());
		tree_node.setHeader(header.getText());
		tree_node.setFooter(footer.getText());
		
		tree_node.setSelectedWords(selected_words.getText());
		tree_node.setPrefix(prefix.getText());
		tree_node.setPostfix(postfix.getText());
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
	}


	protected void createPart2SShell() {
		{
			label_header = new Label(sShell, SWT.NONE);
			GridData gridData = new GridData(GridData.FILL_VERTICAL);
			label_header.setLayoutData(gridData);

			header = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
			header.setLayoutData(gridData2);
		}
		{
			Label label_subst = new Label(sShell, SWT.NONE);
			selected_words = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			selected_words.setLayoutData(gridData);
			label_subst.setText("Выделенные слова");
		}
		{
			/*Label label_subst = */new Label(sShell, SWT.NONE);
			
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
			Label label1 = new Label(composite, SWT.NONE);
			label1.setText("prefix"); 
			prefix = new Text(composite, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			prefix.setLayoutData(gridData);
			}
			{
			Label label2 = new Label(composite, SWT.NONE);
			label2.setText("postfix"); 
			postfix = new Text(composite, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			postfix.setLayoutData(gridData);
			}
		}
		
		{
			label_java_text = new Label(sShell, SWT.NONE);
			GridData gridData = new GridData(GridData.FILL_VERTICAL);
			label_java_text.setLayoutData(gridData);

			java_text = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
			java_text.setLayoutData(gridData2);
		}
		{
			label_footer = new Label(sShell, SWT.NONE);
			footer = new Text(sShell, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			footer.setLayoutData(gridData);
		}
		/*{
			label_html_text = new Label(sShell, SWT.NONE);
			GridData gridData = new GridData(GridData.FILL_VERTICAL);
			label_html_text.setLayoutData(gridData);

			html_text = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
			//gridData2.verticalSpan = 4;
			html_text.setLayoutData(gridData2);
			html_text.setEditable(false);
			html_text.setForeground(new Color(null,0,0,250));
		}*/
			
	}
	
	
	protected void initLabel() {
		super.initLabel();
		
		label_java_text.setText("Java:");
		//label_html_text.setText("Html:");
		label_header.setText("header:");
		label_footer.setText("footer:");
		
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+480));
	}
	
	
}
