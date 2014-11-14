/*******************************************************************************
 * Copyright (c) 2006,2007 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *     Andrey Skrypnik 
 *******************************************************************************/
package net.confex.directedit;

import java.io.File;

import net.confex.application.ConfexPlugin;
import net.confex.translations.Logger;
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.ImageResource;
import net.confex.utils.Utils;
import net.confex.views.NavigationView;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;

public class tmp implements IPropertyDialog {
	
	public static final int PROPERTY_DIALOG_WIDTH = 620;
	public static final int PROPERTY_DIALOG_HEIGHT = 276;

	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="68,15"
	protected Button okButton = null;
	protected Button cancelButton = null;
	protected Button saveButton = null;
	protected CLabel about = null;
	protected Label label_name = null;
	protected Text name = null;
	protected Label label_tooltip = null;
	protected Text tooltip = null;
	
	public static final int RETURN_CANCEL = 0;
	public static final int RETURN_COMMIT = 1;

	protected Text icon_file_name = null;
	protected Label label_icon_file_name = null;
	protected Button buttonTest = null;

	protected Button buttonSrcEdit = null;
	
	protected Label label_not_run_in_batch = null;
	protected Button button_not_run_in_batch = null;
	
	protected Label label_not_vis_in_user_mode = null;
	protected Button button_not_vis_in_user_mode = null;
	
	TreeNode  element;     
	ITreeNode new_element; //-- используется для запуска Тестов
	
	protected int ret;
	public int getReturn() { return ret; }

	private TreeViewer     viewer = null;
	protected NavigationView view = null;
	
	public tmp(Shell shell, ITreeNode element) {
		sShell = shell;
		this.element = (TreeNode)element;
	}

	
	/**
	 * This method initializes sShell
	 */
	public void createSShell() {
		if(sShell==null) {
			System.err.println("Shell must be defined!!! in createSShell()");
			return;
		}
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		sShell = new Shell(sShell, 
				SWT.BORDER | SWT.RESIZE | SWT.TITLE | SWT.MODELESS | SWT.MIN | SWT.MAX ); //| SWT.CLOSE);
		//sShell.setText("Свойства…");
		sShell.setLayout(gridLayout);

		createPartSetShellSize();
		
		about = new CLabel(sShell, SWT.NONE);
		//about.setText("");
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		gridData.verticalSpan = 2;
		about.setLayoutData(gridData);

		label_name = new Label(sShell, SWT.NONE);
		//label_name.setText("Name:");
		name = new Text(sShell, SWT.BORDER);
		GridData name_gridData = new GridData();
		name_gridData.grabExcessHorizontalSpace = true;
		name_gridData.verticalAlignment = GridData.CENTER;
		name_gridData.horizontalAlignment = GridData.FILL;
		name.setLayoutData(name_gridData);
		name.setFocus();

		label_icon_file_name = new Label(sShell, SWT.NONE);
		//label_icon_file_name.setText("Icon:");
		icon_file_name = new Text(sShell, SWT.BORDER);
		GridData icon_gridData = new GridData();
		icon_gridData.horizontalAlignment = GridData.FILL;
		icon_gridData.grabExcessHorizontalSpace = true;
		icon_gridData.verticalAlignment = GridData.CENTER;
		icon_file_name.setLayoutData(icon_gridData);
		
		createStatePartSShell();
		
		createPart1SShell();

		createPart2SShell();

		createMainButtonPartSShell();
		
		createSrcButtonPartSShell();

		initLabel();
		initActions();
	}
	
	
	
	/**
	 * Устанавливает размеры окна свойств
	 */
	protected void createPartSetShellSize() {
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT));
	}

	
	protected void createStatePartSShell() {
		new Label(sShell, SWT.NONE); //-- empty
		
		Composite comp = new Composite (sShell, SWT.NONE);
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 4;
		gridLayout1.horizontalSpacing = 7;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL_HORIZONTAL;
		gridData1.grabExcessHorizontalSpace = true;
		comp.setLayoutData(gridData1);
		comp.setLayout(gridLayout1);
		
		button_not_run_in_batch = new Button(comp, SWT.CHECK);
		label_not_run_in_batch = new Label(comp, SWT.NONE);

		button_not_vis_in_user_mode = new Button(comp, SWT.CHECK);
		label_not_vis_in_user_mode = new Label(comp, SWT.NONE);
	}
	

	public void setTreeNode(ITreeNode element) { 
		if(!(element instanceof TreeNode) ) {
			System.err.println("!!! Parameter element must be instanceof TreeNode [TreeNode.setNodeElement()]");
			return;  
		}
		this.element = (TreeNode)element; 
		this.name.setText(element.getName());
		this.icon_file_name.setText(this.element.getIconFileName());
		if(tooltip!=null)
			tooltip.setText(this.element.getTooltipText());
		about.setText(this.element.getAboutString());
		//-- создаем тестовый элемент того же класса что и элемент
		new_element = element.createNewITreeNode();
		new_element.setPropertyLike(element);
		button_not_run_in_batch.setSelection(new_element.isNotRunInBatch());
		
		button_not_vis_in_user_mode.setSelection(new_element.isNotVisInUserMode());
		
		sShell.setImage(ImageResource.getImage(element.getIconFileName()));

		String s = this.element.getSrcText();
		if(src_text!=null)
			this.src_text.setText(s);
		if(src_file_name!=null)
			this.src_file_name.setText(this.element.getSrcFileNameXml());
		
		initEnabledDiasabled();
	}
	
	
	protected void retOk() {
		ret = RETURN_COMMIT;
		prepareRetOk(element);
		if(viewer != null) {
			viewer.refresh(element);
		}
		view.setDirty(true);
	}
	
	protected void saveNodeProperties(){
		prepareRetOk(element);
	}
	
	protected void prepareRetOk(ITreeNode element) {
		//super.prepareRetOk(element);
		TreeNode tree_node = (TreeNode)element;
		tree_node.setName(name.getText().trim());
		if(tooltip!=null)
			tree_node.setTooltipText(tooltip.getText());
		String s = icon_file_name.getText();
		tree_node.setIcon_file_name(s);
		if(button_not_run_in_batch.getSelection()) {
			tree_node.setNotRunBatchState();
		} else {
			tree_node.clearNotRunBatchState();
		}
		if(button_not_vis_in_user_mode.getSelection()) {
			tree_node.setNotVisInUserMode(true);
		} else {
			tree_node.setNotVisInUserMode(false); //.clearNotRunBatchState();
		}
		if(src_file_name!=null && !src_file_name.getText().trim().equals("")) {
			File file = tree_node.getFile(src_file_name.getText());
			if(!file.exists()) {
				saveSrcFile(tree_node);
			}
			tree_node.setSrcFileNameXml(src_file_name.getText());
		} else {
			if(src_text!=null)
				tree_node.setSrcText(src_text.getText());
		}
	}

	
	protected void saveSrcFile(TreeNode tree_node) {
		if(src_text!=null)
			tree_node.setSrcText(src_text.getText());
		if(src_file_name!=null)
			tree_node.setSrcFileNameXml(src_file_name.getText());
	
		tree_node.saveSrcFile(false);
	}

	/**
	 * Восстанавливаем свойства и закрываем диалог
	 */
	//protected void retCancel() {
	//	element.setPropertyLike(new_element);
	//	ret = RETURN_CANCEL;
	//	sShell.setVisible(false);
	//}
	
	
	/**
	 * Восстанавливаем свойства и закрываем диалог
	 * исправлено Андрей Срыпник
	 */
	protected void retCancel() {
        if(new_flg){
            viewer.reveal(element);
            viewer.setSelection(new StructuredSelection(element), true);
            view.runDelete(false);
        }
        ret = RETURN_CANCEL;
        sShell.setVisible(false);
	}

	
	protected void runTest() {
		prepareRetOk(element);
		element.run(view);
	}

	
	/*
	 * Собственно блокировка разблокировка происходит 
	 * в repareRetOk 
	 */
	/*
	protected void runLock() {
		if(button_not_run_in_batch.getSelection()) {
			//element.setLockState();
		} else {
			if(element.getParent().isLocked()) {
				Logger.errorDialog(sShell, "MSG_ERR_CANTUNLOCK_PARENTLOCKED", "\"" + element.getParent().getName() +"\"");
				button_not_run_in_batch.setSelection(true);
			} else {
				//element.clearLockState();
			}
		}
	}*/

	protected Label label_src_file_name = null;
	protected Text src_file_name = null;
	protected Label label_src_text = null;
	protected Text src_text = null;
	protected Text test_file_name = null;
	
	private Button readFileButton = null;
	private Button saveFileButton = null;
	
	protected void createPart1SShell() {}
	
	protected void createSrcFilePartSShell() {
		label_src_file_name = new Label(sShell, SWT.NONE);
		src_file_name = new Text(sShell, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		src_file_name.setLayoutData(gridData);
		
		
		/* 
		 * changed by Ivan Nepogodin 01/28/2008
		 * added get default directory for src_file_name
		 * 
		 */
		
		FocusListener focus_listener = new FocusListener(){
			public void focusGained(FocusEvent e) {
				if (new_flg){
					if (src_file_name.getText() == null  || src_file_name.getText().equalsIgnoreCase(""))
					src_file_name.setText(getDefaultSrcDir());
				}
			}
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		src_file_name.addFocusListener(focus_listener);

		/* 
		 * changed by Ivan Nepogodin 01/23/2008
		 * added tooltip for src_file_name
		 * 
		 */
		/*
		ModifyListener modif_listener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String toolTipValue = getTooltipTextForSrcFileName(src_file_name.getText());
				if (toolTipValue != null && !toolTipValue.equals("")){
					src_file_name.setToolTipText(toolTipValue);
				}
			}
		};
		src_file_name.addModifyListener(modif_listener);
		*/
	}
	
	
	/**
	 * Stub. If you want to show Tool Tip for src_file_name, 
	 * you should override this method in child class 
	 * @param srcFileNameText
	 * @return null
	 */
	public String getTooltipTextForSrcFileName (String srcFileNameText){
		return null;
	}
	

	/**
	 * Stub. If you want to get default directory for src_file_name, 
	 * you should override this method in child class 
	 * @return null
	 */
	public String getDefaultSrcDir(){
		return null;
	}
	
	
	protected void createTestFilePartSShell() {
			Label label_test_file_name = new Label(sShell, SWT.NONE);
			label_test_file_name.setText("test file");
			
			Composite comp = new Composite (sShell, SWT.NONE);
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.numColumns = 2;
			gridLayout1.marginWidth = 0;
			gridLayout1.marginHeight = 0;
			gridLayout1.verticalSpacing = 1;
			gridLayout1.horizontalSpacing = 1;
			GridData gridData1 = new GridData();
			gridData1.horizontalAlignment = GridData.FILL;
			gridData1.grabExcessHorizontalSpace = true;
			comp.setLayoutData(gridData1);
			comp.setLayout(gridLayout1);
			
			//comp.setLayoutData(new GridData(GridData.FILL_BOTH));
			test_file_name = new Text(comp, SWT.BORDER);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.verticalAlignment = GridData.CENTER;
			test_file_name.setLayoutData(gridData);
			
			Button testFileButton = new Button(comp, SWT.NONE);
			testFileButton.setText("Make Test File");
			GridData testFileButton_gridData = new GridData();
			testFileButton_gridData.horizontalAlignment = GridData.FILL;
			testFileButton_gridData.verticalAlignment = GridData.CENTER;
			testFileButton.setLayoutData(testFileButton_gridData);
			
			testFileButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
				public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
					if(test_file_name.getText()!=null && !test_file_name.getText().trim().equals("")) {
						element.saveTestFile(test_file_name.getText());
					} else {
						System.out.println(element.getFullText());
					}
				}
			});
	}


	protected void createPart2SShell() {
			label_tooltip = new Label(sShell, SWT.NONE);
			//label_tooltip.setText("Tooltip:");
			GridData gridData = new GridData(GridData.FILL_VERTICAL);
			//GridData gridData = new GridData(int, SWT.FILL, boolean, true);
			label_tooltip.setLayoutData(gridData);

			tooltip = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
			
			gridData2.verticalSpan = 4;
			tooltip.setLayoutData(gridData2);
	}
	
	protected Composite buttonComposit = null;
	
	
	
	protected void createMainButtonPartSShell() {
		buttonComposit = new Composite(sShell, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.verticalSpan = 2;
		buttonComposit.setLayoutData(gridData);
		
		okButton = new Button(buttonComposit, SWT.NONE);
		//okButton.setText("Ok");
		GridData ok_gridData = new GridData();
		ok_gridData.horizontalAlignment = GridData.FILL;
		ok_gridData.verticalAlignment = GridData.CENTER;
		okButton.setLayoutData(ok_gridData);
		
		buttonTest = new Button(buttonComposit, SWT.NONE);
		//buttonTest.setText("Тест");
		GridData test_gridData = new GridData();
		test_gridData.horizontalAlignment = GridData.FILL;
		test_gridData.verticalAlignment = GridData.CENTER;
		buttonTest.setLayoutData(test_gridData);
		
		cancelButton = new Button(buttonComposit, SWT.NONE);
		//cancelButton.setText("Cancel"); 
		GridData cancel_gridData = new GridData();
		cancel_gridData.horizontalAlignment = GridData.FILL;
		cancel_gridData.verticalAlignment = GridData.CENTER;
		cancelButton.setLayoutData(cancel_gridData);
		
		saveButton = new Button (buttonComposit, SWT.NONE);
		GridData save_gridData = new GridData();
		save_gridData.horizontalAlignment = GridData.FILL;
		save_gridData.verticalAlignment = GridData.CENTER;
		saveButton.setLayoutData(save_gridData);
	}

	protected void createSrcButtonPartSShell() {
		readFileButton = new Button(buttonComposit, SWT.NONE);
		readFileButton.setText("Read Src File");
		GridData readFileButton_gridData = new GridData();
		readFileButton_gridData.horizontalAlignment = GridData.FILL;
		readFileButton_gridData.verticalAlignment = GridData.CENTER;
		readFileButton.setLayoutData(readFileButton_gridData);
		
		saveFileButton = new Button(buttonComposit, SWT.NONE);
		saveFileButton.setText("Save Src File");
		GridData saveFileButton_gridData = new GridData();
		saveFileButton_gridData.horizontalAlignment = GridData.FILL;
		saveFileButton_gridData.verticalAlignment = GridData.CENTER;
		saveFileButton.setLayoutData(saveFileButton_gridData);
		
		buttonSrcEdit = new Button(buttonComposit, SWT.NONE);
		buttonSrcEdit.setText("Edit");
		GridData srcEditButton_gridData = new GridData();
		srcEditButton_gridData.horizontalAlignment = GridData.FILL;
		srcEditButton_gridData.verticalAlignment = GridData.CENTER;
		saveFileButton.setLayoutData(srcEditButton_gridData);
	}

	
	protected void initActions() {
		
		sShell.addShellListener(this);
		
		okButton.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				retOk();
				sShell.setVisible(false);
			}
		});
		okButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				retOk();
				sShell.setVisible(false);
			}
		});
		
		buttonTest.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				runTest();
			}
		});
		buttonTest.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				runTest();			
			}
		});
		
		saveButton.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				saveNodeProperties();
			}
		});
		saveButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				saveNodeProperties();
			}
		});
		
		cancelButton.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				retCancel();
			}
		});
		cancelButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				retCancel();
			}
		});
		/*
		button_not_run_in_batch.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				runLock();
			}
		});
		button_not_run_in_batch.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				runLock();
			}
		});*/
	}

	
	/**
	 * Выставление доступных/недоступных элементов
	 * 
	 *  В случае переопределения нужно переопределить и setTreeNode(ITreeNode element)!!!
	 */
	protected void initEnabledDiasabled() {
		if(src_text!=null) {
			if(src_file_name==null || src_file_name.getText().trim().equals("")) 
				src_text.setEditable(true);
			else
				src_text.setEditable(false);
		}
	}

	
	protected void initActionsSrcFileButtons() {

		readFileButton.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				element.setSrcFileNameXml(src_file_name.getText());
				element.readFromSrcFile();
				src_text.setText(element.getSrcText());
			}
		});
		readFileButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				element.setSrcFileNameXml(src_file_name.getText());
				element.readFromSrcFile();
				src_text.setText(element.getSrcText());
			}
		});
		saveFileButton.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				element.setSrcFileNameXml(src_file_name.getText());
				element.setSrcText(src_text.getText());
				element.saveSrcFile(true);
			}
		});
		saveFileButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				element.setSrcFileNameXml(src_file_name.getText());
				element.setSrcText(src_text.getText());
				element.saveSrcFile(true);
			}
		});
		buttonSrcEdit.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				runEditor();
			}
		});
		buttonSrcEdit.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				runEditor();			
			}
		});
	}
	
	
	protected void initLabel() {
		if(about!=null)
			about.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		if(label_name!=null)
			label_name.setText(Translator.getString("LABEL_TITLE"));
		if(label_tooltip!=null)
			label_tooltip.setText(Translator.getString("LABEL_TOOLTIP"));
		if(label_icon_file_name!=null)
			label_icon_file_name.setText(Translator.getString("LABEL_ICON"));
		if(okButton!=null)
			okButton.setText(Translator.getString("LABEL_OK"));
		if(cancelButton!=null)
			cancelButton.setText(Translator.getString("LABEL_CANCEL"));
		if(buttonTest!=null)
			buttonTest.setText(Translator.getString("LABEL_TEST"));
		if(saveButton!=null)
			saveButton.setText(Translator.getString("LABEL_SAVE_PROPERTIES"));
		if(label_not_run_in_batch!=null)
			label_not_run_in_batch.setText(Translator.getString("LABEL_NOT_RUN_IN_BATCH"));
		if(label_not_vis_in_user_mode!=null)
			label_not_vis_in_user_mode.setText(Translator.getString("LABEL_NOT_VIS_IN_USER_MODE"));
		if(label_src_file_name!=null)
			label_src_file_name.setText("source file:");
	}
	

	public void show() {
		sShell.setVisible(true);
		sShell.setActive();
		sShell.forceActive();
		if(sShell.getMinimized()) {
			sShell.setMinimized(false);
		}
	}
	
	
	public NavigationView getView() {
		return view;
	}
	public void setView(NavigationView view) {
		this.view = view;
		this.viewer = view.getTreeViewer();
	}


	public TreeViewer getViewer() {
		return viewer;
	}
	//public void setViewer(TreeViewer viewer) {
	//	this.viewer = viewer;
	//}
	
	public Shell getShell() {
		return sShell;
	}


	public void setShell(Shell shell) {
		sShell = shell;
	}


	protected boolean new_flg = false;
	
	
	public void setNewFlg(boolean new_flg) {
		this.new_flg = new_flg;
	}


	public void dispose() {
		//System.out.println("NodePropertyDialog onDispose();");
		//sShell.dispose();
		if(Logger.questionDialog(sShell
				, "MESSAGE_BOX_TITLE_DELETE"
				, "QUEST_SAVE_CHANGES","")) {
			retOk();
		} else {
			retCancel();
		}
		element.setPropertyDialogNull();
	}
	

	//class  implements ShellListener {
		/**
		 * Sent when a shell is closed.
		 *
		 * @param e an event containing information about the close
		 */
		public void shellClosed(ShellEvent e) {
			//System.out.println("shellClosed()");
			sShell.removeShellListener(this);
			this.dispose();
		}		

		/**
		 * Sent when a shell becomes the active window.
		 *
		 * @param e an event containing information about the activation
		 */
		public void shellActivated(ShellEvent e) {
			//System.out.println("shellActivated()");
		}		

		/**
		 * Sent when a shell stops being the active window.
		 *
		 * @param e an event containing information about the deactivation
		 */
		public void shellDeactivated(ShellEvent e) {
			//System.out.println("shellDeactivated()");
		}		


		/**
		 * Sent when a shell is un-minimized.
		 *
		 * @param e an event containing information about the un-minimization
		 */
		public void shellDeiconified(ShellEvent e) {
			//System.out.println("shellDeiconified()");
		}		


		/**
		 * Sent when a shell is minimized.
		 *
		 * @param e an event containing information about the minimization
		 */
		public void shellIconified(ShellEvent e) {
			//System.out.println("shellIconified()");
		}		

		
		public boolean runEditor() {
			if(src_file_name.getText().trim().equals(""))
				return false;
			File file = element.getFile(src_file_name.getText());
			if(!file.exists()) {
				saveSrcFile(element);
			}
			if(file.exists()) {
				try {
					IWorkbenchPage page = ConfexPlugin.getDefault()
						.getWorkbench().getActiveWorkbenchWindow().getActivePage();  
					//Utils.openExternalFile(page, file);
					Utils.openSrcFile(page, file);
				} catch(Exception e) {
					System.err.println(e.getMessage());
					return false;
				}
			} else {
				System.err.println("File ["+src_file_name.getText()+"] not exists!");
				return false;
			}
			return true;
		}
		
}
