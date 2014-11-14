package net.confex.editor.tree;

import net.confex.directedit.IPropertyDialog;
import net.confex.editor.Constants;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.Utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.w3c.dom.Node;

public class SelectionBookmarkNode   extends TreeNode {

	//private String description = "";
	private String resource = "";
	private String path = "";
	private String location = "";

	public String getAboutString() { return Constants.ABOUT_SELECTIONBOOKMARKNODE; }
	
	
	/**
	 *   Установка иконки по умолчанию для класса
	 */
	public String getDefaultImage() {
		return "eclipse icons/code_bookmark.gif";
	}
	
	
	public SelectionBookmarkNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	/*
	public SelectionBookmarkNode(ConfigTree configTree, String name) {
		super(configTree,name);
	}*/

	
	public void test() {
	}
	
	
	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new SelectionBookmarkNode(getConfigTree(),null); 
	}

	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof SelectionBookmarkNode)) {
			System.err.println("[BookmarkNode.setPropertyLike] prototype NOT instanceof BookmarkNode!");
			return;
		}
		//setDescription(((SelectionBookmarkNode)prototype).getDescription());
		setResource(((SelectionBookmarkNode)prototype).getResource());
		setPath(((SelectionBookmarkNode)prototype).getPath());
		setLocation(((SelectionBookmarkNode)prototype).getLocation());
	}
	
	
	/*
	public void loadAttribFromXml_0_0_1(Node node, ITreeNode parent) {
		super.loadAttribFromXml_0_0_1(node, parent);

		Node attr = node.getAttributes().getNamedItem("descriptiomsql");
		if (attr == null) {
			System.err.println(" Can't read SQL for " + this.toString());
		} else {
			String s_sql = attr.getNodeValue();
			this.description = Utils.fromHtmlSpecialEntities(s_sql);
		}
	}*/

	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		/**/
		if(getName()!=null 
				&& !getName().equals("")) {
			str_xml += "<description>\n"
				+Utils.toHtmlSpecialEntities(getName())+"\n"; 
			str_xml += "</description>\n"; 
		}/**/
		if(resource!=null 
				&& !resource.equals("")) {
			str_xml += "<resource>\n"
				+Utils.toHtmlSpecialEntities(resource)+"\n"; 
			str_xml += "</resource>\n"; 
		}
		if(path!=null 
				&& !path.equals("")) {
			str_xml += "<path>\n"
				+Utils.toHtmlSpecialEntities(path)+"\n"; 
			str_xml += "</path>\n"; 
		}
		/*
		if(location!=null 
				&& !location.equals("")) {
			str_xml += "<location>\n"
				+Utils.toHtmlSpecialEntities(location)+"\n"; 
			str_xml += "</location>\n"; 
		}*/
		if(location!=null 
				&& !location.equals("")) {
			str_xml += "<selection>\n"
				+Utils.toHtmlSpecialEntities(location)+"\n"; 
			str_xml += "</selection>\n"; 
		}
		return str_xml;
	}
	
	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		/*
		if (property.getNodeName().equals("description")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setDescription(Utils.fromHtmlSpecialEntities(text.trim()));
		} else */
		if (property.getNodeName().equals("resource")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setResource(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("path")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setPath(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("location")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setLocation(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
	}
	
	
	/**
	 * Переходим по маркеру  
	 */
	public void run(IViewPart view) {
		MarkerUtil.goAtSelection(path,resource,location);
	}

	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new SelectionBookmarkNodePropertyDialog(shell);
	}
	
	
	/*
	public void openPropertyDialog(NavigationView view, Shell shell,
			boolean new_flg) {
		property_dialog.setNewFlg(new_flg);
		
		if(property_dialog==null) {
			property_dialog = new SelectionBookmarkNodePropertyDialog(shell);
		}
		property_dialog.createSShell(); 
		property_dialog.setView(view);
		property_dialog.show();
		property_dialog.setTreeNode(this);
	}*/

	
	/**
	 * Для предидущего JavaEditor-а находит выделенный текст и
	 * запоминает
	 * 
	 * @return true - if bookmark was made
	 * 
	 * !Now only for JavaEditor
	 */
	public boolean makeBookMark() {
		//-- find active page
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage();
		/*
		final NavigationHistory history = (NavigationHistory) page.getNavigationHistory();
		INavigationLocation inloc=null, inloc_old = history.getCurrentLocation();
		//-- переходим к предидущему редактору
	    history.backward();
	    inloc = history.getCurrentLocation();
	    if(inloc_old==inloc) {
			//-- nothig happens!
			return false;
		}
		*/
	    //-- gets name of file and projects //получаем имя файла и проекта
		IEditorPart edit_part=page.getActiveEditor();
		IFileEditorInput input = (IFileEditorInput)edit_part.getEditorInput() ;
		IFile file = input.getFile();		
		resource=file.getName();
		
		if (file != null) {
			String s = file.getName();

			IPath path = file.getFullPath();
			int n = path.segmentCount() - 1; // n is the number
												// of segments in
												// container, not
												// path
			if (n > 0) {
				int len = 0;
				for (int i = 0; i < n; ++i)
					len += path.segment(i).length();
				// account for /'s
				if (n > 1)
					len += n - 1;
				StringBuffer sb = new StringBuffer(len);
				for (int i = 0; i < n; ++i) {
					if (i != 0)
						sb.append('/');
					sb.append(path.segment(i));
				}
				s = sb.toString();
				setPath(s);
			}
		}
    	
		//-- switch at our editor //возвращаемся к нашему редактору
		//history.forward();

		//-- gets selected text //получаем выделенный текст
		if(edit_part instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor)edit_part;
			
			TextSelection selection = (TextSelection)editor.getSelectionProvider().getSelection();
			//-- remember selected text
			location=null;
			if (selection.getLength() == 0){ 
		    	MessageDialog.openInformation(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(),
							"","Text was not selected!");
				return false;
			}
			location= selection.getText();
	    } else {
	    	  MessageDialog.openInformation(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						"","Unknown type of IEditPart!!! [BookmarkJavaEditText.makeBookMark]");
	    	  System.err.println("Unknown type of IEditPart!!! [BookmarkJavaEditText.makeBookMark]");
	    	  return false;
	    }
		return true;
	}

	
	public String getResource() {
		return resource;
	}


	public void setResource(String resource) {
		this.resource = resource;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}
	
}