package net.confex.tree;

import net.confex.directedit.ConfexNodePropertyDialog;
import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.utils.Strings;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;
import net.confex.views.NavigationView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.w3c.dom.Node;


public class ConfexNode extends TreeNode {
	
	private String confex_filename= "";

	ConfigTree config_tree = null;

	public String getAboutString() { 
		return Translator.getString("ABOUT_CONFEXNODE"); 
	}

	protected static String default_image_name = "non_eclipse/confex_nav.gif"; 
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	public static String getDefaultImageName() {
		return default_image_name;
	}


	public ConfexNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}

	
	public ITreeNode createNewITreeNode() {
		return new ConfexNode(getConfigTree(),null); 
	}

	
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof ConfexNode)) {
			System.err.println("[ConfexNode.setPropertyLike] prototype NOT instanceof ConfexNode!");
			return;
		}
		setConfexFilename(((ConfexNode)prototype).getConfexFilename());
	}

	
	public String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if(confex_filename!=null 
				&& !confex_filename.equals("")) {
			str_xml += "<confex_filename>\n";
			str_xml += Utils.toHtmlSpecialEntities(confex_filename)+"\n"; 
			str_xml += "</confex_filename>\n"; 
		}
		return str_xml;
	}

	
	/**
	 * Redefine parent method to avoid saving all children!
	 */
	public String getChildrenXml() {
		return "";
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("confex_filename")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setConfexFilename(Utils.fromHtmlSpecialEntities(text.trim()));
			
			//loadFile();
		} 
	}

	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new ConfexNodePropertyDialog(shell);
	}
	

	/*
	public FolderView runBrowser(IWorkbenchPage page) {
		FolderView viewer = null;
        try {
        	String url = this .getPath();
        	String id = url;
        	id = id.replaceAll(":", "_");
        	id = id.replaceAll("/", "_");
        	id = Strings.replace(id,"\\", "_");
        	//System.out.println("-->" + url + "   " + id);
        	viewer = (FolderView) page.showView(FolderView.VIEW_ID, id,
        			IWorkbenchPage.VIEW_ACTIVATE);
            if (viewer != null) {
            	viewer.setTitlePartName(getName()); //url.trim());
                viewer.setPath(url.trim());
            }
        //} catch(PartInitException e1) {
        } catch(Exception e1) {
        	System.err.println("-->" + e1.getMessage());
        }
        return viewer;
	}*/
	
	
	public void run(IViewPart view) {
		//RunBrowserAction runBrowserAction = new RunBrowserAction(navigation_view,this);
		//runBrowserAction.run();
		IWorkbenchPage page = view.getSite().getPage();
		//runBrowser(page);
		try {
        	String id = getConfexFilename();
        	id = id.replaceAll(":", "_");
        	id = id.replaceAll("/", "_");
        	id = Strings.replace(id,"\\", "_");
			NavigationView navigationView = (NavigationView)page.showView(
					NavigationView.ID, id, 
					IWorkbenchPage.VIEW_ACTIVATE);
			String s = TreeUtils.doAllSubstitutions(this, getConfexFilename());
			navigationView.loadFile(s);
		} catch (PartInitException e) {
			MessageDialog.openError(view.getSite().getShell(), 
					Translator.getString("MESSAGEBOX_TITLE_ERROR"),
					Translator.getString("MESSAGE_ERR_OPEN_VIEW") + e.getMessage());
		}
	}

	
	/*
	public void loadFile() {
		if(config_tree!=null) {
			config_tree.removeAll();
		}
		if(confex_filename!=null && !confex_filename.equals("")) {
			config_tree = new ConfigTree();
			config_tree.setRoot(this);
			config_tree.loadXmlFromFile(confex_filename);
			//-- блокируем изменения
			this.setLockState();
		}
	}*/

	
	public String getConfexFilename() {
		return confex_filename;
	}

	public void setConfexFilename(String confex_filename) {
		this.confex_filename = confex_filename;
	}


	/*
	public ITreeNode[] getChildren() {
		return (ITreeNode[]) children.toArray(new ITreeNode[children.size()]);
	}
	
	
	public boolean hasChildren() {
		return children.size()>0;
	}*/

	
}
