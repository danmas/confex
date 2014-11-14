/*******************************************************************************
 * Copyright (c) 2006,2007 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *     
 * Version 0.9.5
 *     
 *******************************************************************************/
package net.confex.views;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import net.confex.action.AddNewFolderAction;
import net.confex.action.CopyAction;
import net.confex.action.DeleteAction;
import net.confex.action.GoUpFolderAction;
import net.confex.action.PasteAction;
import net.confex.action.PropertyClipboardViewAction;
import net.confex.translations.Logger;
import net.confex.translations.Translator;
import net.confex.tree.IClipboardView;
import net.confex.utils.Executor;
import net.confex.utils.ImageResource;
import net.confex.utils.Utils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.PluginDropAdapter;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.ViewPart;

public class FolderView  extends ViewPart implements  IClipboardView { 

	public static final int PROPERTY_DIALOG_WIDTH = 620;
	public static final int PROPERTY_DIALOG_HEIGHT = 200;
	
	public static final String VIEW_ID = "net.confex.view.folderView";
	
	private TreeViewer treeViewer;

	
	Clipboard clipboard; 

	//-- TODO current_drag_source источник Drag события это как-то неправильно. 
	//private static FolderView current_drag_source = null;
	
	//-- действия будем создавать не сразу а только в момент открытия Вида
    private CopyAction     copyAction   = null;
    //private CutAction      cutAction    = null;
    private PasteAction    pasteAction  = null;
    
    private PropertyClipboardViewAction propertyAction = null;
    private DeleteAction   deleteAction = null;
    
    private AddNewFolderAction addNewFolderAction = null;
    private GoUpFolderAction goUpFolderAction = null;
    
    
    private static final String TAG_PATH = "path";
    //private static final String TAG_EXPANDED = "expanded"; 
    //private static final String TAG_ELEMENT = "element"; 
    //private static final String TAG_PATH = "path"; 
    //private static final String TAG_SELECTION = "selection"; 
    //private static final String TAG_RUNNING = "running";
    //private static final String TAG_RUN_OBJECT_KEY = "run_object_key";

    private IMemento memento;

    
    public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		this.memento = memento; 
    }
    
    
	public Shell getShell() {
		return getSite().getShell();
	}

	
	public boolean isEditable() {
		return true;
	}
	
    
    public void saveState(IMemento memento) {
    	memento.putString(TAG_PATH, path);
    }
    
    
    /**
     * Restores the state of the receiver to the state described in the specified memento.
     *
     * @param memento the memento
     * @since 2.0
     */
    protected void restoreState(IMemento memento) {
        //TreeViewer viewer = getTreeViewer();

        //getConfigTree().fillMomenoHashMap();
        
        /*
        IMemento childMem = memento.getChild(TAG_EXPANDED);
        if (childMem != null) {
            ArrayList elements = new ArrayList();
            IMemento[] elementMem = childMem.getChildren(TAG_ELEMENT);
            for (int i = 0; i < elementMem.length; i++) {
                Object element = getConfigTree()
                	.findMember(elementMem[i].getString(TAG_PATH));
                if (element != null) {
                    elements.add(element);
        			//getTreeViewer().setExpandedState(element, true);
        			//System.out.println(" Expand --> " + ((ITreeNode)element).getName() );
                }
            }
            viewer.setExpandedElements(elements.toArray());

        }
        childMem = memento.getChild(TAG_SELECTION);
        if (childMem != null) {
            ArrayList elements = new ArrayList();
            IMemento[] elementMem = childMem.getChildren(TAG_ELEMENT);
            for (int i = 0; i < elementMem.length; i++) {
                Object element = getConfigTree()
                	.findMember(elementMem[i].getString(TAG_PATH));
                if (element != null) {
                	elements.add(element);
                }
            }
            viewer.setSelection(new StructuredSelection(elements));
        }
        */

        getTreeViewer().refresh();
    }
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
		treeViewer.refresh();
	}

	
    public void setTitlePartName(String title) {
    	setPartName(title);
    }
    
    
    //-- путь к директории для которой строится дерево
    private String path; 
    
    
    public void setPath(String path) {
    	this.path = path;
		if(path!=null) {
			File root = new File(path);
			treeViewer.setInput(root);
			treeViewer.refresh();
			setTitlePartName(path);
		}
    }
	
    
    public void dispose() {
    	clipboard.dispose();
    	
		((Tree)treeViewer.getControl()).removeSelectionListener(selection_listener);

    	super.dispose();
    }

    
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		
		((Tree)treeViewer.getControl()).addSelectionListener(selection_listener);

		/*
		 * ITreeNode root = createDummyModel(); if(root==null) { //root =
		 * getConfigTree().createNew(Constants.DEFAULT_TMP_CONFEX_FILE ); } else {
		 * //-- Устанавливаем для всех этот навигатор как обозревателя
		 * root.setObserverForAll(this); }
		 */
    	if(memento!=null) {
    		path = memento.getString(TAG_PATH);
        	this.setPartName(path);
    	} 
    	
		treeViewer.setContentProvider(new FileTreeContentProvider());
		
		ILabelProvider lp = new FileTreeLabelProvider();
		treeViewer.setLabelProvider(lp);

		if(path!=null) {
			File root = new File(path);
			treeViewer.setInput(root);
		} else {
			//File root = new File("c:\\config\\");
			//treeViewer.setInput(root);
		}
		
		treeViewer.addDoubleClickListener(dbl_click_listener);
		//-- Create menu, toolbars, filters, sorters.
		createFiltersAndSorters();
		//-- создаем действия
		createActions();
		createMenus();
		//createToolbar();
		//hookListeners();
		
		// -- Связываем стандартные действия с методами нашего Вида
		setGlobalActionHandlers();
		// -- Создаем контекстное меню
		createContextMenu();

		contributeToActionBars();
		
		// -- инициализируем операции буфера обмена DND
        initDragAndDrop();

		// -- восстанавливаем прежнее состояние
        if(memento!=null) {
        	restoreState(memento);
        	memento = null;
        }
	}
	
	
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		//fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(goUpFolderAction);
		manager.add(addNewFolderAction);
	}

	
	/**
	 * Создаем контекстное меню
	 */
	private void createContextMenu() {
		/**/
		//-- сначала создаем менеджер меню
		MenuManager menuManager = new MenuManager();
		
		//-- предидущее меню должно очищаться при вызове
		menuManager.setRemoveAllWhenShown(true); 
		
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		//-- создаем само меню 
		Menu menu = menuManager.createContextMenu(treeViewer.getControl());
		//-- и привязываем его к отображаемому элементу
		treeViewer.getControl().setMenu(menu);
		//-- регистрируем меню
		getSite().registerContextMenu(menuManager, getSite().getSelectionProvider());
		/**/
	}
	

	/**
	 * Заполняем меню элементами
	 * @param manager
	 */
	private void fillContextMenu(IMenuManager manager){
		
		manager.add(addNewFolderAction);
		manager.add(copyAction);
		//manager.add(cutAction);
		manager.add(pasteAction);
		//manager.add(deleteAction);
		manager.add(propertyAction);
		manager.add(new Separator(org.eclipse.ui.IWorkbenchActionConstants.MB_ADDITIONS));
		//addMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		//addMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS+"-end"));
	}
	
	
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}
	
	protected void createMenus() {
		IMenuManager rootMenuManager = getViewSite().getActionBars().getMenuManager();
		rootMenuManager.setRemoveAllWhenShown(true);
		rootMenuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillMenu(mgr);
			}
		});
		fillMenu(rootMenuManager);
	}


	protected void fillMenu(IMenuManager rootMenuManager) {
		IMenuManager filterSubmenu = new MenuManager("Фильтры");
		rootMenuManager.add(filterSubmenu);
		filterSubmenu.add(atExtensionFilterAction);

		IMenuManager sortSubmenu = new MenuManager("Сортировка");
		rootMenuManager.add(sortSubmenu);
		sortSubmenu.add(fileViewerSorterAction);
		
		rootMenuManager.add(goUpFolderAction);
		rootMenuManager.add(addNewFolderAction);
	}

	
	/**
	 * Фильтры и сортировка
	 */
	protected Action fileViewerSorterAction;
	protected ViewerSorter fileViewerSorter;
	
	protected ExtensionFilter extensionFilter;
	protected Action atExtensionFilterAction;
	
	protected void createActions() {
		
	    copyAction     = new CopyAction(this);
	    //cutAction      = new CutAction(this);
	    pasteAction    = new PasteAction(this);
	    deleteAction   = new DeleteAction(this);
	    propertyAction = new PropertyClipboardViewAction(this);
	    addNewFolderAction = new AddNewFolderAction(this);
	    goUpFolderAction = new GoUpFolderAction(this);
	    
		fileViewerSorterAction = new Action("Сортировать по каталогам") {
			public void run() {
				updateSorter(fileViewerSorterAction);
			}
		};
		treeViewer.setSorter(fileViewerSorter);
		fileViewerSorterAction.setChecked(true);
		
		atExtensionFilterAction = new Action("Только исполняемые файлы") {
			public void run() {
				updateFilter(atExtensionFilterAction);
			}
		};
		atExtensionFilterAction.setChecked(false);

		
		/*
		removeAction = new Action("Delete") {
			public void run() {
				removeSelected();
			}			
		};
		removeAction.setToolTipText("Delete");
		removeAction.setImageDescriptor(TreeViewerPlugin.getImageDescriptor("remove.gif"));
		*/		
	}

	
	SelectionListener selection_listener = new SelectionListener(){
		
		public void widgetSelected(SelectionEvent e) {
			treeViewer.refresh(); 
		}
		
		public void widgetDefaultSelected(SelectionEvent e) {
		}

	};
	
	
	/**
	 *  Связываем стандартные действия с методами нашего Вида
	 */
	private void setGlobalActionHandlers() {
		IActionBars actionBars= getViewSite().getActionBars();
		
		actionBars.setGlobalActionHandler( 
				ActionFactory.COPY.getId(),   copyAction);
		//actionBars.setGlobalActionHandler( 
		//		ActionFactory.CUT.getId(),    cutAction);
		actionBars.setGlobalActionHandler( 
				ActionFactory.PASTE.getId(),  pasteAction);
		
		actionBars.setGlobalActionHandler( 
				ActionFactory.DELETE.getId(), deleteAction);
		/*
		actionBars.setGlobalActionHandler( 
				ActionFactory.SAVE.getId(),   saveAction);
		actionBars.setGlobalActionHandler( 
				ActionFactory.SAVE_AS.getId(),saveAsAction);
		actionBars.setGlobalActionHandler( 
				ActionFactory.PROPERTIES.getId(),propertyAction);
		*/
	}
	
	protected void createFiltersAndSorters() {
		extensionFilter = new ExtensionFilter();
		fileViewerSorter = new FileViewerSorter();
	}

	
	/* Multiple filters can be enabled at a time. */
	protected void updateFilter(Action action) {
		if(action == atExtensionFilterAction) {
			if(action.isChecked()) {
				treeViewer.addFilter(extensionFilter);
			} else {
				treeViewer.removeFilter(extensionFilter);
			}
		}
	}
	
	/*
	protected void createToolbar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
		toolbarManager.add(addBookAction);
		toolbarManager.add(removeAction);
	}*/
	
  
	protected void updateSorter(Action action) {
		/*if(action == booksBoxesGamesAction) {
			noArticleAction.setChecked(!booksBoxesGamesAction.isChecked());
			if(action.isChecked()) {
				treeViewer.setSorter(booksBoxesGamesSorter);
			} else {
				treeViewer.setSorter(null);
			}
		} else */ if(action == fileViewerSorterAction) {
			//booksBoxesGamesAction.setChecked(!fileViewerSorterAction.isChecked());
			if(action.isChecked()) {
				treeViewer.setSorter(fileViewerSorter);
			} else {
				treeViewer.setSorter(null);
			}
		}
			
	}
	
	
	/**
	 * Viewer sorter that places folders first, then files.
	 */
	private static class FileViewerSorter extends ViewerSorter {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
		 */
		public int category(Object element) {
			if (element instanceof File
					&& !((File) element).isDirectory()) {
				return 1;
			}
			return 0;
		}
	}

	
	private class ExtensionFilter extends ViewerFilter {
		/*
		 * @see ViewerFilter#select(Viewer, Object, Object)
		 */
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if(element instanceof File) {
				if(((File)element).isDirectory())
					return true;
				String ext = ((File)element).getName().toLowerCase(); 
				if(ext.endsWith(".exe")
						|| ext.endsWith(".com")	
						|| ext.endsWith(".bat")	
						)
					return true;
			}
			return false;
		}

	}

	

	/**
     * Инициализация drag drop операций
     */
    private void initDragAndDrop() {
        int ops = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { FileTransfer.getInstance(),
                PluginTransfer.getInstance() };
        treeViewer.addDragSupport(ops, transfers,
                new FolderViewDragListener(this));
        
        int ops_drop = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers_drop = new Transfer[] { FileTransfer.getInstance(),
                PluginTransfer.getInstance() };
        treeViewer.addDropSupport(ops_drop, transfers_drop, 
        		new FolderVeiwDropListener(this));
        
		clipboard = new Clipboard(((Widget)treeViewer.getControl()).getDisplay());
		
		/*
		// Create the drag source on the tree
	    DragSource ds = new DragSource(getTreeViewer().getTree(), DND.DROP_MOVE);
	    ds.setTransfer(new Transfer[] { FileTransfer.getInstance() });
	    ds.addDragListener(new DragSourceAdapter() {
	      public void dragSetData(DragSourceEvent event) {
	        // Set the data to be the first selected item's text
	        event.data = getTreeViewer().getTree().getSelection()[0].getText();
	      }
	    });
	    */
	    /*
	    // Create the drop target on the button
	    DropTarget dt = new DropTarget(getTreeViewer().getTree(), DND.DROP_MOVE);
	    dt.setTransfer(new Transfer[] { FileTransfer.getInstance() });
	    dt.addDropListener(new DropTargetAdapter() {
	      public void drop(DropTargetEvent event) {
	    	//System.out.println("drop");  
	    	String[] files = (String[])event.data;
			run_paste(files);

	        // Set the buttons text to be the text being dropped
	        //button.setText((String) event.data);
	      }
	    });
		*/
    }

    
	public void runCut() {
		runCopy();
		//runDelete(false);
	}
	

	public void runNewFolder() {
		//runDelete(false);
	}

	
	/**
	 * Переход на каталог выше
	 */
	public void runUpNav() {
			File new_root = new File(path).getParentFile();
			if(new_root==null) {
				//MessageDialog.openError(getSite().getShell(), "Error", message)
				return;
			}
			path = new_root.getAbsolutePath();
			setPath(path);
	}
	
	
	@SuppressWarnings("unchecked")
	public void runCopy() {
		ISelection selection = treeViewer.getSelection();
    	TreeSelection ss = (TreeSelection)selection;
    	
		if( ss.size()==0) {
	        //MessageDialog.openInformation(getSite().getShell()
	        //		, "Error", "Не могу выполнить операцию! Нет выделенных объектов!");
	        Logger.errorDialog(getSite().getShell(),"MSG_CANT_DO_SELECT_ONE","");
			return;
		}
		String[] str_array = new String[ss.size()];

		int i=0;
    	//-- для всех выделенных
		for(Iterator iter= ss.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
    	
	    	if(obj instanceof File) {
	    		str_array[i++] = ((File)obj).getAbsolutePath();
	    	}
		}
        clipboard.setContents(new Object[] {str_array},
                new Transfer[] { FileTransfer.getInstance() });
	}
	

	/**
	 * Just runCopy()!
	 */
	public void runCopyWithoutChild() {
    	runCopy();
	}
	
	
	public void runDelete(boolean with_question) {
		if (with_question) {
			if (!Logger.questionDialog(getSite().getShell()
					,"MESSAGEBOX_TITLE_DELETE"
					,"QUEST_DELETE_SELECTED", "")) {
				return;
			}
		}
		delete_selected();
	}

	
	
	@SuppressWarnings("unchecked")
	public void runProperty() {
		ISelection selection = getTreeViewer().getSelection();
		TreeSelection ss = (TreeSelection)selection;
		
		//-- по всем выделенным
		for(Iterator iter= ss.iterator();iter.hasNext();) {
			Object obj = iter.next(); 	
		
	    	if(obj instanceof File) {
	    		openPropertyDialog(getSite().getShell(),((File)obj));
	    		getTreeViewer().refresh(obj);
	    		getTreeViewer().setExpandedState(obj, true);
	    	}
		}
	}

	
	/**
	 * Show properties of file
	 * @param shell
	 * @param file
	 */
	public void openPropertyDialog(Shell shell, File file) {
		if(shell==null) {
			System.err.println("Shell must be defined!!! in createSShell()");
			return;
		}
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		Shell sShell = new Shell(shell, 
				SWT.BORDER | SWT.RESIZE | SWT.TITLE | SWT.MODELESS | SWT.CLOSE);
		sShell.setText(Translator.getString("PROPERTIES"));
		sShell.setLayout(gridLayout);
		{
			Label label_name = new Label(sShell, SWT.NONE);
			label_name.setText("Name:");
			Text name = new Text(sShell, SWT.BORDER);
			GridData name_gridData = new GridData();
			name_gridData.grabExcessHorizontalSpace = true;
			name_gridData.verticalAlignment = GridData.CENTER;
			name_gridData.horizontalAlignment = GridData.FILL;
			name.setLayoutData(name_gridData);
			name.setText(file.getName());
			name.setEditable(false);
			}
		{
			Label label_name = new Label(sShell, SWT.NONE);
			label_name.setText("Absolute Path:");
			Text name = new Text(sShell, SWT.BORDER);
			GridData name_gridData = new GridData();
			name_gridData.grabExcessHorizontalSpace = true;
			name_gridData.verticalAlignment = GridData.CENTER;
			name_gridData.horizontalAlignment = GridData.FILL;
			name.setLayoutData(name_gridData);
			name.setText(file.getAbsolutePath());
			name.setEditable(false);
			}
		if(!file.isDirectory())
		{
			Label label_name = new Label(sShell, SWT.NONE);
			label_name.setText("Size:");
			Text name = new Text(sShell, SWT.BORDER);
			GridData name_gridData = new GridData();
			name_gridData.grabExcessHorizontalSpace = true;
			name_gridData.verticalAlignment = GridData.CENTER;
			name_gridData.horizontalAlignment = GridData.FILL;
			name.setLayoutData(name_gridData);
			name.setText(new Long(file.length()).toString());
			name.setEditable(false);
			}
		sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT));

		sShell.setVisible(true);
		sShell.setActive();
		sShell.forceActive();
	}
	
	
	@SuppressWarnings("unchecked")
	protected void delete_selected() {
		ISelection selection = treeViewer.getSelection();
		TreeSelection ss = (TreeSelection) selection;

		// -- для всех выделенных
		for (Iterator iter = ss.iterator(); iter.hasNext();) {
			Object obj = iter.next();

			if (obj instanceof File) {
				boolean success; 
				if(((File) obj).isDirectory()) {
					success = Utils.deleteDir(((File) obj));
				} else {
					success = ((File) obj).delete();
				}
				if(!success) {
					//MessageDialog.openError(, "Удаление",
					//		"Не удалось удалить файл! "+((File) obj).getAbsolutePath());
					Logger.errorDialog(getSite().getShell(), "MSG_CANT_DELETE_FILE", "\n\n"+ ((File) obj).getAbsolutePath());
				} else {
					treeViewer.remove(obj);
					System.out.println("File " + ((File) obj).getAbsolutePath() + " was deleted.");
				}
			}
		}
	}
	
	
	public void runPaste() {
		String[] files = (String[]) getClipboardContent(DND.CLIPBOARD);
		try {
			run_paste(files);
		} catch(IOException e) {
			e.printStackTrace();
			//MessageDialog.openError(getSite().getShell(), "Копирование",e.getMessage());
			Logger.errorDialog(getSite().getShell(), "MSG_ERROR_ON_PASTE", "\n\n"+ e.getMessage());
		}
	}

	
	/**
	 * Если выделен один и это директория
	 * то копируем в нее
	 * 
	 * Если выделен один и это файл 
	 * то копируем в каталог этого файла
	 * 
	 * Если нет выделеных
	 * то копируем в текущий головной каталог
	 * 
	 * Если выделено больше одного
	 * то это ошибка
	 * 
	 */
	public void run_paste(String[] files)  throws IOException  {
		
		ISelection selection = treeViewer.getSelection();
    	TreeSelection ss = (TreeSelection)selection;
    	
		if( ss.size()>1) {
	        //MessageDialog.openError(getSite().getShell()
	        //		, "Error", "Не могу выполнить операцию! Выделено больше одного элемента!");
	        Logger.errorDialog(getSite().getShell(), "MSG_CANT_DO_SELECT_ONE", "");
			return;
		} 
    	//String msg = "Копировать файлы ("+files.length+"шт)?";
    	//if (!MessageDialog.openQuestion(getSite().getShell(), 
    	//		"Копирование/пермещение",msg)) {
        if (!Logger.questionDialog(getSite().getShell(), "MESSAGEBOX_TITLE_COPYFILES",
        		"QUEST_COPY_N_FILES",  "("+files.length+")?")) {
    		return;
    	}
		
		//-- Если нет выделеных
		//   то копируем в текущий головной каталог
		if( ss.size()==0) {
			paste(path,files);
			getTreeViewer().refresh();
			return;
		}
    	Object obj = ss.getFirstElement();
    	String input_path = path;
    	
    	if(obj instanceof File) {
    		File destination_node = (File)obj;
    		if(destination_node.isDirectory()) {
    			input_path = destination_node.getAbsolutePath(); 
    			paste(input_path,files);
    			getTreeViewer().refresh(destination_node);
    			getTreeViewer().setExpandedState(destination_node, true);
    		} else {
    			input_path = destination_node.getAbsolutePath();
    			input_path = input_path.substring(0,
    				input_path.indexOf(destination_node.getName()) ); 
    			paste(input_path,files);
    			getTreeViewer().refresh();
    		}
    	} 
		
	}
	
	public void runCopyFile() {}
	public void runPasteFile() {}
	public void openSrcEditors() {
		System.out.println("OpenSrcEditors action not defined for FolderView!");
	}
	
	private void paste(String input_path,String[] files) throws IOException {
		if(!input_path.endsWith(File.separator)) {
			input_path += File.separator;
		}
		if(files!=null && files.length>0) {
			for(int i=0; i< files.length; i++) {
				//System.out.println(files[i]);
				File file_src = new File(files[i]);
				/*
				if(file_src.isDirectory()) {
			        MessageDialog.openError(getSite().getShell()
			        		, "Error", "Не могу копировать директории!");
					continue;
				}*/
				String dest_path = input_path  + file_src.getName();
				File file_dest = new File(dest_path);
				//try {
					Utils.copyDirectory(file_src, file_dest);
				//} catch(IOException e) {
				//	e.printStackTrace();
				//}
			}
		}
	}
	
	
	Object getClipboardContent(int clipboardType) {
		FileTransfer fileTransfer = FileTransfer.getInstance();
		return clipboard.getContents(fileTransfer, clipboardType);
	}
	
	
    private IDoubleClickListener dbl_click_listener = new IDoubleClickListener() {
        @SuppressWarnings("unchecked")
		public void doubleClick(DoubleClickEvent event) {
        	StructuredSelection ss = (StructuredSelection)event.getSelection();
        	
        	//-- запускаем все выделенные
    		for(Iterator iter= ss.iterator();iter.hasNext();) {
    			Object obj = iter.next(); 	
	        	if(obj instanceof File) {
	        		File file = ((File)obj);
	        		if(file.isDirectory()) {
	        			setPath(file.getAbsolutePath());
	        		} else {
		        		if(file.isFile()) {
		        			Executor executor = new Executor();
		        			executor.execAsync(null, file.getAbsolutePath(), null);
		        		}
	        		}
	        	}
    		}
        }
    };
    
    
	
	private class FolderVeiwDropListener extends PluginDropAdapter {
        //private FolderView folder_view;
		
        public FolderVeiwDropListener(FolderView folder_view) {
        	super(folder_view.getTreeViewer());
            //this.folder_view = folder_view;
        }
        
        public boolean validateDrop(Object target, int operation,
                TransferData transferType) {
            //currentTransfer = transferType;
            //if (currentTransfer != null
            //        && PluginTransfer.getInstance()
            //                .isSupportedType(currentTransfer)) {
                //plugin cannot be loaded without the plugin data
            //    return true;
            //}
            return true;
        }
        
        
        /**
         * Returns the position of the given event's coordinates relative to its target.
         * The position is determined to be before, after, or on the item, based on
         * some threshold value.
         *
         * @param event the event
         * @return one of the <code>LOCATION_* </code>constants defined in this class
         */
        protected int determineLocation(DropTargetEvent event) {
            if (!(event.item instanceof Item)) {
                return LOCATION_NONE;
            }
            Item item = (Item) event.item;
            Point coordinates = new Point(event.x, event.y);
            coordinates = getViewer().getControl().toControl(coordinates);
            if (item != null) {
                Rectangle bounds = getBounds(item);
                if (bounds == null) {
                    return LOCATION_NONE;
                }
                if ((coordinates.y - bounds.y) < 5) {
                    return LOCATION_BEFORE;
                }
                if ((bounds.y + bounds.height - coordinates.y) < 0) {
                    return LOCATION_AFTER;
                }
            }
            return LOCATION_ON;
        }

        
        public void drop(DropTargetEvent event){
			File destination_node = null;

			FileTransfer fileTransfer = FileTransfer.getInstance();
            String[] files = (String[])fileTransfer.nativeToJava(event.currentDataType);

            if (files != null) {
    	        if (!Logger.questionDialog(getSite().getShell(), "MESSAGEBOX_TITLE_COPYFILES",
    	        		"QUEST_COPY_N_FILES",  "("+files.length+")?")) {
    	        	return;
    	        }
    			try {
            	Object obj = event.item;
            	if(obj==null) {
    				paste(path,files);
    				getTreeViewer().refresh();
    				return;
            	} else if( obj instanceof TreeItem) {
            		Object obj2 = ((TreeItem)obj).getData();
            		if( obj2 instanceof File) {
            			destination_node = (File)obj2;
            			
            			/*
            			//-- проверяем не совпадает ли имя с одним из копируемых
            			for(int i=0;i<files.length;i++) {
                        	String input_path = path;
                			if(destination_node.isDirectory()) {
            					input_path = destination_node.getAbsolutePath(); // .getCanonicalPath();
                			} else {
                				input_path = destination_node.getAbsolutePath();
                				input_path = input_path.substring(0,
                					input_path.indexOf(destination_node.getName()) ); 
                			}
                			File file_tmp = new File(files[i]);
            				
            				if(new File(input_path+file_tmp.getName()).exists()) {
            					MessageDialog.openError(getSite().getShell(), "Копирование/пермещение",
            							"Файл "+destination_node+"\n не может быть скопирован сам в себя!");
                	            current_drag_source = null;
            					return;
            				} 
            			}*/
            			
                    	String input_path = path;
                    	
            			if(destination_node.isDirectory()) {
        					input_path = destination_node.getAbsolutePath(); // .getCanonicalPath();
        					paste(input_path,files);
        					getTreeViewer().refresh(destination_node);
        					getTreeViewer().setExpandedState(destination_node, true);
            			} else {
            				input_path = destination_node.getAbsolutePath();
            				input_path = input_path.substring(0,
            					input_path.indexOf(destination_node.getName()) ); 
            				paste(input_path,files);
            				getTreeViewer().refresh();
            			}
                        //if ( current_drag_source == navigation_view ) {
                        //	delete_selected();
                        //}
            		}
            	}
    			} catch(IOException e) {
    				e.printStackTrace();
    				//MessageDialog.openError(getSite().getShell(), "Копирование",e.getMessage());
    				Logger.errorDialog(getSite().getShell(), "MSG_ERROR_ON_PASTE", "\n\n"+ e.getMessage());
    			}
            }
            //current_drag_source = null;
        }
	}

	
	public void setDirty(boolean dirty) {
	}

	
    /**
     * A drag listener for the readme editor's content outline page.
     * Allows dragging of content segments into views that support
     * the <code>TextTransfer</code> or <code>PluginTransfer</code> transfer types.
     */
	private class FolderViewDragListener extends DragSourceAdapter {
        //private FolderView folder_view;

        /**
         * Creates a new drag listener for the given page.
         */
        public FolderViewDragListener(FolderView folder_view) {
            //this.folder_view = folder_view;
        }

        
        /* (non-Javadoc)
         * Method declared on DragSourceListener
         */
        @SuppressWarnings("unchecked")
		public void dragSetData(DragSourceEvent event) {
            if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
        		ISelection selection = treeViewer.getSelection();
            	TreeSelection ss = (TreeSelection)selection;
            	
        		if( ss.size()==0) {
        	        //MessageDialog.openInformation(getSite().getShell()
        	        //		, "Error", "Не могу выполнить операцию! Нет выделенных объектов!");
        	        Logger.errorDialog(getSite().getShell(), "MSG_CANT_DO_SELECT_ONE", "");
        			return;
        		}
        		String[] str_array = new String[ss.size()];

        		int i=0;
            	//-- для всех выделенных
        		for(Iterator iter= ss.iterator();iter.hasNext();) {
        			Object obj = iter.next(); 	
            	
        	    	if(obj instanceof File) {
        	    		str_array[i++] = ((File)obj).getAbsolutePath();
        	    	}
        		}
                event.data = str_array;
                
                //current_drag_source = navigation_view;
                return;
            }
        }
    }
	
}


	/**
	 * This class provides the content for the tree in FileTree
	 */

	class FileTreeContentProvider implements ITreeContentProvider {
	  /**
		 * Gets the children of the specified object
		 * 
		 * @param arg0
		 *            the parent object
		 * @return Object[]
		 */
	  public Object[] getChildren(Object arg0) {
	    // Return the files and subdirectories in this directory
	    return ((File) arg0).listFiles();
	  }

	  /**
		 * Gets the parent of the specified object
		 * 
		 * @param arg0
		 *            the object
		 * @return Object
		 */
	  public Object getParent(Object arg0) {
	    // Return this file's parent file
		String s = ((File) arg0).getAbsolutePath();
		System.out.println(s);
	    return ((File) arg0).getParentFile();
	  }

	  /**
		 * Returns whether the passed object has children
		 * 
		 * @param arg0
		 *            the parent object
		 * @return boolean
		 */
	  public boolean hasChildren(Object arg0) {
	    // Get the children
	    Object[] obj = getChildren(arg0);

	    // Return whether the parent has children
	    return obj == null ? false : obj.length > 0;
	  }

	  /**
		 * Gets the root element(s) of the tree
		 * 
		 * @param arg0
		 *            the input data
		 * @return Object[]
		 */
	  public Object[] getElements(Object arg0) {
	    // These are the root elements of the tree
	    // We don't care what arg0 is, because we just want all
	    // the root nodes in the file system
		  if(arg0 instanceof File) {
			File file = (File) arg0;
			return file.listFiles();
		  } else {
			  return null;
		  }
		  //return File.listRoots();
	  }

	  /**
		 * Disposes any created resources
		 */
	  public void dispose() {
	    // Nothing to dispose
	  }

	  /**
		 * Called when the input changes
		 * 
		 * @param arg0
		 *            the viewer
		 * @param arg1
		 *            the old input
		 * @param arg2
		 *            the new input
		 */
	  public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	    // Nothing to change
	  }
	
	}

	
	/**
	 * This class provides the labels for the file tree
	 */
	class FileTreeLabelProvider implements ILabelProvider {
	  // The listeners
	  //private List listeners;

	  // Images for tree nodes
	  private Image file_image;

	  private Image dir_image;

	  // Label provider state: preserve case of file names/directories
	  //boolean preserveCase = true;

	  
	  /**
		 * Constructs a FileTreeLabelProvider
		 */
	  public FileTreeLabelProvider() {
	    // Create the list to hold the listeners
	    //listeners = new ArrayList();

	    // Create the images
	    //try {
		      //file_image = new Image(null, new FileInputStream("icons/non_eclipse/default_file.png"));
		      //dir_image = new Image(null, new FileInputStream("icons/eclipse icons/obj16/fldr_obj.gif"));
		    
		      //file_image = new Image(null, new FileInputStream("icons/non_eclipse/default_file.png"));
		      //dir_image = new Image(null, new FileInputStream("eclipse icons/obj16/fldr_obj.gif"));
		    
	    
	    //-- регистрируем иконки в ресурсе ImageResource
	    String file_image_name = "eclipse icons/obj16/file_obj.gif"; 
	    //"eclipse icons/obj16/file_obj.gif";
		if(ImageResource.getImageDescriptor(file_image_name)==null)
			ImageResource.registerImage(file_image_name,file_image_name);
	    	
		String dir_image_name = "eclipse icons/obj16/fldr_obj.gif";
			if(ImageResource.getImageDescriptor(dir_image_name)==null)
				ImageResource.registerImage(dir_image_name,dir_image_name);
		
	      file_image = ImageResource.getImage(file_image_name);
	      dir_image = ImageResource.getImage(dir_image_name);
	    
	    
	    //} catch (FileNotFoundException e) {
	      // Swallow it; we'll do without images
	    //}
	    
	    
	  }

	  
	  /**
		 * Sets the preserve case attribute
		 * 
		 * @param preserveCase
		 *            the preserve case attribute
		 */
	  /*
	  public void setPreserveCase(boolean preserveCase) {
	    this.preserveCase = preserveCase;

	    // Since this attribute affects how the labels are computed,
	    // notify all the listeners of the change.
	    LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
	    for (int i = 0, n = listeners.size(); i < n; i++) {
	      ILabelProviderListener ilpl = (ILabelProviderListener) listeners
	          .get(i);
	      ilpl.labelProviderChanged(event);
	    }
	  }
	  */
	  
	  /**
		 * Gets the image to display for a node in the tree
		 * 
		 * @param arg0
		 *            the node
		 * @return Image
		 */
	  public Image getImage(Object arg0) {
		File file = ((File) arg0);   
		
		if(!file.isDirectory()) {
			String extension = Utils.getFileNameExtension(file.getName());
			if(extension==null) {
				return file_image;
			}
			//-- по расширению определяем изображение
			ImageDescriptor image = ImageResource.getImageDescriptor(extension); 
			if(image != ImageDescriptor.getMissingImageDescriptor() ) { // .getImageData().data.length>0) {
				//if(image instanceof MissingImageDescriptor ) { //.getImageData().data.length>0) {
				return ImageResource.getImage(extension);
			}
		    Program program = Program.findProgram(extension);
		    ImageData imageData = (program == null ? null : program.getImageData());
		    if (imageData != null) {
		    	//Image image = new Image(Display.getCurrent(), imageData);
		    	ImageResource.registerImage(extension,imageData);
				return ImageResource.getImage(extension);
		    } else {
				return file_image;
		    }
		} else {
		    return dir_image;
		}
	  }

	  
	  /**
		 * Gets the text to display for a node in the tree
		 * 
		 * @param arg0
		 *            the node
		 * @return String
		 */
	  public String getText(Object arg0) {
	    // Get the name of the file
	    String text = ((File) arg0).getName();

	    // If name is blank, get the path
	    if (text.length() == 0) {
	      text = ((File) arg0).getPath();
	    }

	    // Check the case settings before returning the text
	    return text;
	  }

	  
	  /**
		 * Adds a listener to this label provider
		 * 
		 * @param arg0
		 *            the listener
		 */
	  public void addListener(ILabelProviderListener arg0) {
	    //listeners.add(arg0);
	  }

	  
	  /**
		 * Called when this LabelProvider is being disposed
		 */
	  public void dispose() {
		/*  
	    // Dispose the images
	    if (dir_image != null)
	      dir_image.dispose();
	    if (file_image != null)
	      file_image.dispose();
	    */
	  }

	  
	  /**
		 * Returns whether changes to the specified property on the specified
		 * element would affect the label for the element
		 * 
		 * @param arg0
		 *            the element
		 * @param arg1
		 *            the property
		 * @return boolean
		 */
	  public boolean isLabelProperty(Object arg0, String arg1) {
	    return false;
	  }

	  
	  /**
		 * Removes the listener
		 * 
		 * @param arg0
		 *            the listener to remove
		 */
	  public void removeListener(ILabelProviderListener arg0) {
	    //listeners.remove(arg0);
	  }
      
	  
	}
	
