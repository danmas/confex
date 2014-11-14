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
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class HtmltextPropertyDialog extends NodePropertyDialog {

	//protected Label label_htmltext = null;
	//protected Text htmltext = null;
	
	HtmlTextNode element;  
	

	public HtmltextPropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}
	
	
	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element); 
		
		if(!(element instanceof HtmlTextNode) ) {
			System.err.println("!!! Parametr element must be instanceof HtmlTextNode [BrowserUrlBookmarkPropertyDialog.setNodeElement()]");
			return; 
		}
		this.element = (HtmlTextNode)element; 
		//String s = this.element.getHtmltext();
		//this.htmltext.setText(this.element.getSrcText());

	}
	

	protected void initLabel() {
		super.initLabel();
		label_src_text.setText(Translator.getString("LABEL_HTMLTEXT"));
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT+100));
	}

	
	protected void initActions() {
		super.initActions();

		initActionsSrcFileButtons();
	}

	
	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		//HtmlTextNode tree_node = (HtmlTextNode)element;
		//tree_node.setSrcText(htmltext.getText());
	}
	
	
	//HtmlTextNode new_element = new HtmlTextNode(element.getConfigTree());
	
	
	protected void createPart1SShell() {
		label_tooltip = new Label(sShell, SWT.NONE);
		label_tooltip.setText("Tooltip:");
		tooltip = new Text(sShell, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		tooltip.setLayoutData(gridData);

		createSrcFilePartSShell();
		
		createTestFilePartSShell();
	}

	
	protected void createPart2SShell() {
		label_src_text = new Label(sShell, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		label_src_text.setLayoutData(gridData);
		//src_text = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		src_text = new Text(sShell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		//GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL 
		//		| GridData.VERTICAL_ALIGN_FILL);
		GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData2.verticalSpan = 4;
		src_text.setLayoutData(gridData2);

		createSrcButtonPartSShell();
	}
	
	
}
