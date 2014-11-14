package net.confex.directedit;

//import LayoutExample;

import java.text.Collator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import java.util.Map.Entry;

import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;
import net.confex.tree.VarsNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

//import FormLayoutTab.AttachDialog;

public class VarsPropertyDialog extends NodePropertyDialog {

	protected Label label_xxx = null;

	TableColumn column0, column1, column2, column3, column4;

	VarsNode element;

	// TODO: Вставить проверку существования переменной с тем-же именем

	public VarsPropertyDialog(Shell shell) {
		super(shell, (ITreeNode)null);
	}

	public void setTreeNode(ITreeNode element) {
		super.setTreeNode(element);

		if (!(element instanceof VarsNode)) {
			System.err.println("!!! Parameter element must be instanceof VarsNode ");
			return;
		}
		this.element = (VarsNode) element;

		varslist2table(this.element);
	}

	protected void prepareRetOk(ITreeNode element) {
		super.prepareRetOk(element);
		VarsNode tree_node = (VarsNode) element;

		table2varslist(tree_node);
	}

	protected void initLabel() {
		super.initLabel();
		label_xxx.setText(Translator.getString("LABEL_VARIABLES"));
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH+100,
				PROPERTY_DIALOG_HEIGHT + 200));
		m_value = null;
	}

	void table2varslist(VarsNode element) {
		/* Remove children that are already laid out */
		element.getVarsMap().clear();
		/* Add all children listed in the table */

		resetEditors();

		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			String name = items[i].getText(2);
			String value = items[i].getText(3);

			element.getVarsMap().put(name, value);
		}
	}

	void varslist2table(VarsNode element) {
		table.removeAll();
		data.clear();

		Iterator iterator = element.getVarsMap().entrySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Entry ent = (Entry) iterator.next();
			TableItem item = new TableItem(table, i++);
			String[] insert = new String[] {
					String.valueOf(table.indexOf(item)+1), "String",
					(String) ent.getKey(), (String) ent.getValue() };
			item.setText(insert);
			data.addElement(insert);
			resetEditors();
		}
	}

	protected void createPart1SShell() {
		label_tooltip = new Label(sShell, SWT.NONE);
		label_tooltip.setText("Tooltip:");
		tooltip = new Text(sShell, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		tooltip.setLayoutData(gridData);
	}

	protected void createPart2SShell() {
		label_xxx = new Label(sShell, SWT.NONE);
		label_xxx.setText("Properties:");
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		label_xxx.setLayoutData(gridData);

		// sql = new Text(sShell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		// GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL
		// | GridData.VERTICAL_ALIGN_FILL);
		// gridData2.verticalSpan = 4;
		// sql.setLayoutData(gridData2);

		createChildGroup();
	}

	String m_value = null;

	// ================================================================================================

	Group controlGroup, childGroup;

	Button size, add, delete, clear, code;

	Button valueEditorButton;

	/* Common values for working with TableEditors */
	Table table;

	Vector data = new Vector();

	int index;

	TableItem newItem, lastSelected;

	void createChildGroup() {
		childGroup = new Group(sShell, SWT.NONE);
		// childGroup.setText("Children");
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		childGroup.setLayout(layout);
		GridData grid_data = new GridData(GridData.FILL_BOTH);
		grid_data.verticalSpan = 4;
		childGroup.setLayoutData(grid_data);
		createChildWidgets();
	}

	void createChildWidgets() {
		/* Controls for adding and removing children */
		add = new Button(childGroup, SWT.PUSH);
		add.setText("Add");
		add.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		delete = new Button(childGroup, SWT.PUSH);
		delete.setText("Delete");
		delete.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resetEditors();
				int[] selected = table.getSelectionIndices();
				table.remove(selected);
				/* Refresh the control indices of the table */
				for (int i = 0; i < table.getItemCount(); i++) {
					table.getItem(i).setText(0, String.valueOf(i)+1);
				}
				// refreshLayoutComposite();
				// layoutComposite.layout(true);
				// layoutGroup.layout(true);
			}
		});
		clear = new Button(childGroup, SWT.PUSH);
		clear.setText("Clear");
		clear.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		clear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resetEditors();
				/*
				 * children = layoutComposite.getChildren(); for (int i = 0; i <
				 * children.length; i++) { children[i].dispose(); }
				 */
				table.removeAll();
				data.clear();
				// children = new Control[0];
				// layoutGroup.layout(true);
			}
		});
		/* Create the "children" table */
		table = new Table(childGroup, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		gridData.heightHint = 150;
		table.setLayoutData(gridData);
		table.addTraverseListener(traverseListener);

		/* Add columns to the table */
		String[] columnHeaders = getLayoutDataFieldNames();
		column0 = new TableColumn(table, SWT.NONE);
		column0.setText(columnHeaders[0]);
		column0.setWidth(20);
		column1 = new TableColumn(table, SWT.NONE);
		column1.setText(columnHeaders[1]);
		column1.setWidth(80);
		column2 = new TableColumn(table, SWT.NONE);
		column2.setText(columnHeaders[2]);
		column2.setWidth(200);
		column3 = new TableColumn(table, SWT.NONE);
		column3.setText(columnHeaders[3]);
		column3.setWidth(200);
		column4 = new TableColumn(table, SWT.NONE);
		column4.setText(columnHeaders[4]);
		column4.setWidth(30);
			
		column1.addListener(SWT.Selection, new Listener() {
			boolean column_sort_flag = true;
		    public void handleEvent(Event e) {
		    // sort column 1
		    	sortByColumn(1,column_sort_flag);
		    	if (column_sort_flag) column_sort_flag=false;
		    	else column_sort_flag=true;
		      }
	    });
		column2.addListener(SWT.Selection, new Listener() {
			boolean column_sort_flag = true;
		    public void handleEvent(Event e) {
		    // sort column 2
		    	sortByColumn(2,column_sort_flag);
		    	if (column_sort_flag) column_sort_flag=false;
		    	else column_sort_flag=true;
		      }
	    });
		column3.addListener(SWT.Selection, new Listener() {
			boolean column_sort_flag = true;
		    public void handleEvent(Event e) {
		    // sort column 3
		    	sortByColumn(3,column_sort_flag);
		    	if (column_sort_flag) column_sort_flag=false;
		    	else column_sort_flag=true;
		      }
	    });

		/*		for (int i = 0; i < columnHeaders.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columnHeaders[i]);
			if (i == 0)
				column.setWidth(20);
			else if (i == COMBO_COL)
				column.setWidth(80);
			else if (i == NAME_COL)
				column.setWidth(200);
			else if (i == VALUE_COL)
				column.setWidth(200);
			else if (i == EDITOR_COL)
				column.setWidth(30);
			else
				column.pack();
		}
*/
		/* Add TableEditors */
		comboEditor = new TableEditor(table);
		widthEditor = new TableEditor(table);
		heightEditor = new TableEditor(table);
		valueEditor = new TableEditor(table);
		table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				resetEditors();
				index = table.getSelectionIndex();
				Point pt = new Point(e.x, e.y);
				newItem = table.getItem(pt);
				if (newItem == null)
					return;
				TableItem oldItem = comboEditor.getItem();
				if (newItem == oldItem || newItem != lastSelected) {
					lastSelected = newItem;
					return;
				}
				table.showSelection();

				combo = new CCombo(table, SWT.READ_ONLY);
				createComboEditor(combo, comboEditor);
				combo.setVisible(false);

				nameText = new Text(table, SWT.SINGLE);
				nameText.setText(((String[]) data.elementAt(index))[NAME_COL]);
				createTextEditor(nameText, widthEditor, NAME_COL);

				valueText = new Text(table, SWT.SINGLE);
				String s = ((String[]) data.elementAt(index))[VALUE_COL];
				//System.err.println("set valueText.setText "+s);
				valueText.setText(s);
				
				if(s.indexOf('\n')==-1)
					createTextEditor(valueText, heightEditor, VALUE_COL);

				valueEditorButton = new Button(table, SWT.PUSH);
				valueEditorButton.setText("...");
				valueEditor.horizontalAlignment = SWT.LEFT;
				valueEditor.grabHorizontal = true;
				valueEditor.minimumWidth = valueEditorButton.computeSize(
						SWT.DEFAULT, SWT.DEFAULT).x;
				valueEditor.setEditor(valueEditorButton, newItem, EDITOR_COL);
				valueEditorButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						// Shell shell = tabFolderPage.getShell();
						AttachDialog dialog = new AttachDialog(sShell);
						dialog.setText("Value Editor");
						dialog.setValueText(((String[]) data.elementAt(index))[VALUE_COL]);
						String result = dialog.open();
						m_value = result;
						// newItem.setText(EDITOR_COL, attach);
						resetEditors();
					}
				});

				for (int i = 0; i < table.getColumnCount(); i++) {
					Rectangle rect = newItem.getBounds(i);
					if (rect.contains(pt)) {
						switch (i) {
						case COMBO_COL:
							combo.setFocus();
							break;
						case NAME_COL:
							nameText.setFocus();
							break;
						case VALUE_COL:
							valueText.setFocus();
							break;
						// case EDITOR_COL:
						// leftAttach.setFocus();
						// break;
						default:
							resetEditors();
							break;
						}
					}
				}
			}
		});

		/* Add listener to add an element to the table */
		add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem item = new TableItem(table, 0);
				String[] insert = new String[] {
						String.valueOf(table.indexOf(item)+1), "String",
						"<name>", "<value>" };
				item.setText(insert);
				data.addElement(insert);
				resetEditors();
			}
		});

	}

	/**
	 * Returns the layout data field names.
	 */
	String[] getLayoutDataFieldNames() {
		return new String[] { "", "Type", "Name", "Value", "Editor" };
	}

	TraverseListener traverseListener = new TraverseListener() {
		public void keyTraversed(TraverseEvent e) {
			if (e.detail == SWT.TRAVERSE_RETURN) {
				e.doit = false;
				resetEditors();
			}
		}
	};

	void sortByColumn(int column, boolean sort_flag){
		resetEditors();
		switch (column){
			case 1:
				table.setSortColumn(column1);
				break;
			case 2:
				table.setSortColumn(column2);
				break;
			case 3:
				table.setSortColumn(column3);
				break;
		}
		if (sort_flag){
			table.setSortDirection(SWT.UP);
	    	TableItem[] items = table.getItems();
	        Collator collator = Collator.getInstance(Locale.getDefault());
	        for (int i = 1; i < items.length; i++) {
	          String value1 = items[i].getText(column);
	          for (int j = 0; j < i; j++) {
	            String value2 = items[j].getText(column);
	            if (collator.compare(value1, value2) < 0) {
	              String[] values1={ items[i].getText(0),
	            		  			items[i].getText(1),
	            		  			items[i].getText(2),
	            		  			items[i].getText(3),
	            		  			items[i].getText(4) };
	              String[] values2={ items[j].getText(0),
	            		  			items[j].getText(1),
	            		  			items[j].getText(2),
	            		  			items[j].getText(3),
	            		  			items[j].getText(4) };
	              items[i].setText(values2);
	              data.setElementAt(values2, i);
	              items[j].setText(values1);
	              data.setElementAt(values1, j);
	              /*
	              items[i].dispose();
	              TableItem item = new TableItem(table, SWT.NONE, j);
	              item.setText(values);
	              data.setElementAt(values, i);*/
	              items = table.getItems();
	              //break;
	            }
	         }
	       }
		}
		else{
			table.setSortDirection(SWT.DOWN);
	        TableItem[] items = table.getItems();
	        Collator collator = Collator.getInstance(Locale.getDefault());
	        for (int i = 1; i < items.length; i++) {
	          String value1 = items[i].getText(column);
	          for (int j = 0; j < i; j++) {
	            String value2 = items[j].getText(column);
	            if (collator.compare(value1, value2) > 0) {
	              String[] values1={ items[i].getText(0),
	            		  			items[i].getText(1),
	            		  			items[i].getText(2),
	            		  			items[i].getText(3),
	            		  			items[i].getText(4) };
	              String[] values2={ items[j].getText(0),
	            		  			items[j].getText(1),
	            		  			items[j].getText(2),
	            		  			items[j].getText(3),
	            		  			items[j].getText(4) };
	              items[i].setText(values2);
	              data.setElementAt(values2, i);
	              items[j].setText(values1);
	              data.setElementAt(values1, j);
	              
	              /*items[i].dispose();
	              TableItem item = new TableItem(table, SWT.NONE, j);
	              item.setText(values);
	              data.setElementAt(values, i);*/
	              items = table.getItems();
	              
	              //break;
	            }
	         }
	       }
		}
		/* Refresh the control indices of the table */
		for (int i = 0; i < table.getItemCount(); i++) {
			table.getItem(i).setText(0, String.valueOf(i+1));
		}

	}
	
	
	/**
	 * Creates the TableEditor with a CCombo in the first column of the table.
	 * This CCombo lists all the controls that the user can select to place on
	 * their layout.
	 */
	void createComboEditor(CCombo combo, TableEditor comboEditor) {
		combo.setItems(new String[] { "SubstString", "Canvas", "Combo",
				"Composite", "CoolBar", "Group", "Label", "List",
				"ProgressBar", "Scale", "Slider", "StyledText", "Table",
				"Text", "ToolBar", "Tree" });
		combo.setText(newItem.getText(1));

		/* Set up editor */
		comboEditor.horizontalAlignment = SWT.LEFT;
		comboEditor.grabHorizontal = true;
		comboEditor.minimumWidth = 50;
		comboEditor.setEditor(combo, newItem, 1);

		/* Add listener */
		combo.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT
						|| e.detail == SWT.TRAVERSE_RETURN) {
					resetEditors();
				}
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					disposeEditors();
				}
			}
		});
	}

	/**
	 * Creates the TableEditor with a Text in the given column of the table.
	 */
	void createTextEditor(Text text, TableEditor textEditor, int column) {
		text.setFont(table.getFont());
		text.selectAll();
		textEditor.horizontalAlignment = SWT.LEFT;
		textEditor.grabHorizontal = true;
		textEditor.setEditor(text, newItem, column);

		text.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
					resetEditors(true);
				}
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					disposeEditors();
				}
			}
		});
	}

	/**
	 * Refreshes the composite and draws all controls in the layout example.
	 */
	void refreshLayoutComposite() {
	}

	/* Controls for setting layout parameters */
	Button horizontal, vertical;

	Button wrap, pack, justify;

	Combo marginRight, marginLeft, marginTop, marginBottom, spacing;

	/* The example layout instance */
	RowLayout rowLayout;

	Text nameText, valueText;

	/* Constants */
	final int COMBO_COL = 1;

	final int NAME_COL = 2;

	final int VALUE_COL = 3;

	final int EDITOR_COL = 4;

	final int TOTAL_COLS = 5;

	CCombo combo;

	TableEditor comboEditor, widthEditor, heightEditor;

	void resetEditors() {
		resetEditors(false);
	}

	void resetEditors(boolean tab) {
		TableItem oldItem = comboEditor.getItem();
		if (oldItem != null) {
			int row = table.indexOf(oldItem);
			/* Make sure user has entered valid data */
			/*
			 * try { new Integer(nameText.getText()).intValue(); } catch
			 * (NumberFormatException e) {
			 * nameText.setText(oldItem.getText(NAME_COL)); } try { new
			 * Integer(valueText.getText()).intValue(); } catch
			 * (NumberFormatException e) {
			 * valueText.setText(oldItem.getText(VALUE_COL)); }
			 */
			String s = valueText.getText();
			if(m_value!=null) {
				//System.err.println(" oldItem.setText "+m_value);
				s = m_value;
				//oldItem.setText(VALUE_COL, m_value);
				m_value = null;
			}/**/
			String[] insert = new String[] { String.valueOf(row+1),
					combo.getText(), nameText.getText(), s }; // valueText.getText()
			//System.err.println("insert valueText.getText()="+valueText.getText());
																				// };
			data.setElementAt(insert, row);
			for (int i = 0; i < TOTAL_COLS - 1; i++) {
				
				oldItem.setText(i, ((String[]) data.elementAt(row))[i]);
			}
			/**/
			if (!tab)
				disposeEditors();
		}
		// setLayoutState();
		// refreshLayoutComposite();
		// setLayoutData();
		// layoutComposite.layout(true);
		// layoutGroup.layout(true);
	}

	/**
	 * Disposes the editors without placing their contents into the table.
	 */
	void disposeEditors() {
		comboEditor.setEditor(null, null, -1);
		combo.dispose();
		nameText.dispose();
		//System.err.println("dispose");
		valueText.dispose();
		valueEditorButton.dispose();
	}

	TableEditor valueEditor;

	/**
	 * <code>AttachDialog</code> is the class that creates a dialog specific
	 * for this example. It creates a dialog with controls to set the values in
	 * a FormAttachment.
	 */
	public class AttachDialog extends Dialog {
		String result = "";

		String controlInput, positionInput, alignmentInput, offsetInput;

		int col = 0;

		public AttachDialog(Shell parent, int style) {
			super(parent, style);
		}

		public AttachDialog(Shell parent) {
			this(parent, 0);
		}

		public void setValueText(String value) {
			result = value;
		}

		public String open() {
			Shell parent = getParent();
			
			final Shell shell = new Shell(sShell, 
					/* SWT.BORDER | */ SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL /*| SWT.MIN | SWT.MAX */); //| SWT.CLOSE);
			shell.setSize(new Point(400, 600));

			shell.setText(getText());
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.makeColumnsEqualWidth = false;
			shell.setLayout(layout);

			/* Find out what was previously set as an attachment */
			//TableItem newItem = valueEditor.getItem();

			/* Add offset field */
			Composite comp = new Composite(shell,SWT.NONE);
			GridLayout layout2 = new GridLayout();
			layout2.numColumns = 1;
			comp.setLayout(layout2);
			comp.setSize(200, 300);
			
			new Label(comp, SWT.NONE).setText("Value");
			
			final Text text = new Text(shell,  SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL );
			text.setText(result);
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
			
			//GridData data = new GridData(SWT.DEFAULT);
			
			data.grabExcessHorizontalSpace = true;
			data.grabExcessVerticalSpace = true;
			data.widthHint = 100;
		    data.heightHint = 100;

			//data.horizontalSpan = 2;
			text.setLayoutData(data);
	
			Button clear = new Button(comp, SWT.PUSH);
			clear.setText("Clear");
			clear.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END ));
			//GridData data1 = new GridData ();
			//data.verticalAlignment = GridData.END;
			//data.grabExcessHorizontalSpace = true;
			//data.grabExcessVerticalSpace = true;
			//clear.setLayoutData (data1);
			
			clear.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					result = "";
					shell.close();
				}
			});
			/* OK button sets data into table */
			Button ok = new Button(comp, SWT.PUSH);
			ok.setText("OK");
			ok.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END));
			//GridData data2 = new GridData ();
			//data2.verticalAlignment = GridData.END;
			//data2.grabExcessHorizontalSpace = true;
			//data2.grabExcessVerticalSpace = true;
			//ok.setLayoutData (data2);
			
			ok.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					result = text.getText();
					shell.close();
				}
			});
			Button cancel = new Button(comp, SWT.PUSH);
			cancel.setText("Cancel");
			cancel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END));
			//GridData data3 = new GridData ();
			//data3.verticalAlignment = GridData.END;
			//data3.grabExcessHorizontalSpace = true;
			//data3.grabExcessVerticalSpace = true;
			//cancel.setLayoutData (data3);
			
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					shell.close();
				}
			});

			shell.setDefaultButton(ok);
			shell.pack();
			/* Center the dialog */
			Point center = parent.getLocation();
			center.x = center.x + (parent.getBounds().width / 2)
					- (shell.getBounds().width / 2);
			center.y = center.y + (parent.getBounds().height / 2)
					- (shell.getBounds().height / 2);
			shell.setLocation(center);
			shell.open();
			Display display = shell.getDisplay();
			while (!shell.isDisposed()) {
				if (display.readAndDispatch())
					display.sleep();
			}

			return result;
		}
	}
}
