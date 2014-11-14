	
package net.confex.extension1.tree;

import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.w3c.dom.Node;

	
public class IfNode  extends TreeNode {
	
	//-- var_name
	private String left_value = "";
	private String right_value = "";
	
	public String getAboutString() { return Translator.getString("ABOUT_IfNode"); }
	
	protected static String default_image_name = "if_node.gif"; 
	
	public String getDefaultImage() {
		return default_image_name;
	}
	
	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	public IfNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}
	
	
	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new IfNode(getConfigTree(),null); 
	}
	

	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if(!(prototype instanceof IfNode)) {
			System.err.println("[IfNode.setPropertyLike] prototype NOT instanceof IfNode!");
			return;
		}
		setLeftValue(((IfNode)prototype).getLeftValue());
	}

	
	/**
	 * Folder node not running in batch mode 
	 */
	//public void runInBatch(IViewPart view) {
		//run(navigation_view);
	//}
	
	
	//public void run(IViewPart view) {
		//RunBrowserAction runBrowserAction = new RunBrowserAction(navigation_view,this);
		//runBrowserAction.run();
		//IWorkbenchPage page = view.getSite().getPage();
	//}

	
	public String getLeftValue() {
		return left_value;
	}

	public void setLeftValue(String left_value) {
		this.left_value = left_value;
	}
	
	
	public String getRightValue() {
		return right_value;
	}

	public void setRightValue(String right_value) {
		this.right_value = right_value;
	}
	
	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if(left_value!=null 
				&& !left_value.equals("")) {
			str_xml += "<left_value>\n"
				+Utils.toHtmlSpecialEntities(left_value)+"\n"; 
			str_xml += "</left_value>\n"; 
		}
		if(right_value!=null 
				&& !right_value.equals("")) {
			str_xml += "<right_value>\n"
				+Utils.toHtmlSpecialEntities(right_value)+"\n"; 
			str_xml += "</right_value>\n"; 
		}
		return str_xml;
	}

	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);
		
		if (property.getNodeName().equals("left_value")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setLeftValue(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
		if (property.getNodeName().equals("right_value")) {
			Node nd=property.getFirstChild();
			String text="";
			if(nd!=null)
				text = nd.getNodeValue();
			this.setRightValue(Utils.fromHtmlSpecialEntities(text.trim()));
		} 
	}
	
	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new IfNodePropertyDialog(shell);
	}
	
	
	/**
	 * Redefine runBatch() for using checking if-condition  
	 *  
	 * check if(value of var_name == var_value) if true executes children
	 * 
	 * @param view
	 * @param monitor
	 * @return
	 */
	public IStatus runBatch(IViewPart view,IProgressMonitor monitor) {

		if(ifCondition()) {
			ITreeNode[] children = getChildren();
			for(int i=0; i<children.length;i++) {
				if(!children[i].isNotRunInBatch())
					children[i].runBatch(view,monitor);
			}
		}
		//-- теперь для самого 
		//runInBatch(view,monitor);
		return Status.OK_STATUS;
	}


	protected boolean ifCondition() {
		String left = TreeUtils.doAllSubstitutions(this, getLeftValue());
		String right = TreeUtils.doAllSubstitutions(this, getRightValue());
		
		return left!=null&&right!=null&&left.equals(right);
	}
	
	
	public String getFullHtmltext() {
		if(ifCondition()) {
			return super.getFullHtmltext();
		} else
			return "";
	}
	
	
	public String getFullText() {
		if(ifCondition()) {
			return super.getFullText();
		} else
			return "";
	}

}
	
