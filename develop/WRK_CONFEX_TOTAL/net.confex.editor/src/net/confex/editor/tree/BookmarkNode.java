package net.confex.editor.tree;

import net.confex.directedit.IPropertyDialog;
import net.confex.editor.Constants;
import net.confex.html.IHtmlPart;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.Utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.w3c.dom.Node;

public class BookmarkNode extends TreeNode implements IHtmlPart, IBookmark {

	//private String description = "";

	private String resource = "";

	private String path = "";

	private String location = "";

	private String selection = "";
	
	private long markerId = -1;

	//private String description = "";
	
	
	public String getAboutString() {
		return Constants.ABOUT_BOOKMARKNODE;
	}

	
	/**
	 * Установка иконки по умолчанию для класса
	 */
	public String getDefaultImage() {
		return "eclipse icons/code_bookmark.gif";
	}

	
	public BookmarkNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
	}
	
	
	//public BookmarkNode(ConfigTree configTree, String name) {
	//	super(configTree, name);
	//}

	
	public void test() {
	}

	
	/**
	 * Cоздает экземпляр того же класса !!! Должен быть переопределен в дочерних
	 * классах
	 * 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new BookmarkNode(getConfigTree(),null);
	}

	
	/**
	 * Устанавливает все свойства элемента такими как у прототипа
	 * 
	 * @param prototype
	 */
	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if (!(prototype instanceof BookmarkNode)) {
			System.err
					.println("[BookmarkNode.setPropertyLike] prototype NOT instanceof BookmarkNode!");
			return;
		}
		//setDescription(((BookmarkNode) prototype).getDescription());
		setResource(((BookmarkNode) prototype).getResource());
		setPath(((BookmarkNode) prototype).getPath());
		setLocation(((BookmarkNode) prototype).getLocation());
		markerId = ((BookmarkNode) prototype).markerId;
	}

	
	/*
	 * public void loadAttribFromXml_0_0_1(Node node, ITreeNode parent) {
	 * super.loadAttribFromXml_0_0_1(node, parent);
	 * 
	 * Node attr = node.getAttributes().getNamedItem("descriptiomsql"); if (attr ==
	 * null) { System.err.println(" Can't read SQL for " + this.toString()); }
	 * else { String s_sql = attr.getNodeValue(); this.description =
	 * Utils.fromHtmlSpecialEntities(s_sql); } }
	 */

	
	protected String getPropertiesXml(boolean read_src_text) {
		//-- перед сохранением обновляем маркеры т.к. возможно были изменения
		updatePropertiesFromMarker();
		
		String str_xml = super.getPropertiesXml(read_src_text);
		/*
		if (description != null && !description.equals("")) {
			str_xml += "<description>\n"
					+ Utils.toHtmlSpecialEntities(description) + "\n";
			str_xml += "</description>\n";
		}*/
		if (resource != null && !resource.equals("")) {
			str_xml += "<resource>\n" + Utils.toHtmlSpecialEntities(resource)
					+ "\n";
			str_xml += "</resource>\n";
		}
		if (path != null && !path.equals("")) {
			str_xml += "<path>\n" + Utils.toHtmlSpecialEntities(path) + "\n";
			str_xml += "</path>\n";
		}
		if (location != null && !location.equals("")) {
			str_xml += "<location>\n" + Utils.toHtmlSpecialEntities(location)
					+ "\n";
			str_xml += "</location>\n";
		}
		if (selection != null && !selection.equals("")) {
			str_xml += "<selection>\n" + Utils.toHtmlSpecialEntities(selection)
					+ "\n";
			str_xml += "</selection>\n";
		}
		
		/*
		 * { str_xml += "<marker_id>\n"
		 * +Utils.toHtmlSpecialEntities(location)+"\n"; str_xml += "</marker_id>\n"; }
		 */
		return str_xml;
	}

	
	public void loadFromXml(Node node,ITreeNode parent, boolean new_node) {
		super.loadFromXml(node,parent,new_node);
		
		//-- устанавливаем связь с маркером через ID
		if(!connectWithMarker()) {
			if(!connectWithNewBookmark()) {
				setErrorState();
				return;
			}
		}
	}
	
	
	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property,new_node);

		/*if (property.getNodeName().equals("description")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setDescription(Utils.fromHtmlSpecialEntities(text.trim()));
		} else */
		if (property.getNodeName().equals("resource")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setResource(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("path")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setPath(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("location")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setLocation(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("selection")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setSelection(Utils.fromHtmlSpecialEntities(text.trim()));
		}
		
	}

	
	protected IPropertyDialog newPropertyDialog(Shell shell) {
		return new BookmarkNodePropertyDialog(shell);
	}
	
	
	/*
	public void openPropertyDialog(NavigationView view, Shell shell,
			boolean new_flg) {
		property_dialog.setNewFlg(new_flg);

		if (property_dialog == null) {
			property_dialog = new BookmarkNodePropertyDialog(shell);
		}
		property_dialog.createSShell();
		property_dialog.setView(view);
		property_dialog.show();
		property_dialog.setTreeNode(this);
		((BookmarkNodePropertyDialog)property_dialog)
			.setMarker(MarkerUtil.getMarkerById(getMarkerId()));
	}*/
	
	
	/**
	 * Переходим по маркеру
	 */
	public void run(IViewPart view) {
		if(markerId==-1 || MarkerUtil.getMarkerById(markerId)==null) {
			//-- устанавливаем связь с маркером через ID
			if(!connectWithMarker()) {
				if(!connectWithNewBookmark()) {
					setErrorState();
					System.err.println("Can not setup Bookmark! Source file's editor not open, рrobably.");
					return;				}
			}
		}
		goAtBookmark();
	}

	
	/**
	 * Восстанавливаем свойства BookmarkNode из метки IMarker
	 * 
	 */
	protected void updatePropertiesFromMarker() {
		IMarker marker = MarkerUtil.getMarkerById(markerId);
		if(marker==null)
			return;
		MarkerUtil.setNodeProperties(marker, this);
	}

	
	/**
	 * Создоем новый bookmark и связываем его с нашим маркером Если есть
	 * выделенный тект то ориентируемся по нему
	 */
	protected boolean connectWithNewBookmark() {
		//-- сначала открываем файл в редакторе
		//if(!runEditor()) {
		//	setErrorState();
		//	return;
		//}
		// -- если есть номер линии то переходим на нее
		if (!this.getLocation().trim().equals("")) {
			if (MarkerUtil.gotoLine(path, resource, Integer.valueOf(location)
					.intValue())) {
				// -- и создаем новый маркер
				IMarker marker = MarkerUtil.makeBookmark(this);
				if (marker != null) {
					setMarkerId(marker.getId());
				}
			} else { //-- не удалось перейти по линии скорее всего файл не открыт
				return false;
			}
		} else if (!this.getSelection().trim().equals("")) {
			// -- переходим по выделенному тексту
			if (MarkerUtil.goAtSelection(path, resource, selection)) {
				// -- и создаем новый маркер
				IMarker marker = MarkerUtil.makeBookmark(this);
				if (marker != null) {
					setMarkerId(marker.getId());
					return true;
				}
			}
		}
		//-- если не связали с реальным маркером то состояние - ошибка
		// TODO: сделать состояние ошибка загрузки
		if(markerId==-1) {
			//setErrorState();
			return false;
		}
		return true;
	}
	
	
	/**
	 * Устанавливаем связь с маркером через ID. Ищем по всем маркерам, если
	 * совпали description, resource, path и location то считаем,
	 * что это тот самый маркер берем его id 
	 * 
	 * @return true если удалось связать с маркером
	 */
	protected boolean connectWithMarker() {

		// -- сначала находим маркер по id
		IMarker marker = null;
		IMarker[] markers;

		try {
			markers = ResourcesPlugin.getWorkspace().getRoot().findMarkers(
					null, false, IResource.DEPTH_INFINITE);
			if (markers.length == 0) {
				//System.err.println("Маркеры не найдены!");
				System.err.println("Markers not found!");
				return false;
			}
			if(this.getLocation().trim().equals("")) {
				return false;
			}
			for (int i = 0; i < markers.length; i++) {
				String description = (String) markers[i]
						.getAttribute(IMarker.MESSAGE);
				if(description != null && description.equals(this.getDescription())) {
					String res_name = MarkerUtil.getResourceName(markers[i]);
					if(res_name != null && res_name.equals(this.getResource())) {
						String path = MarkerUtil.getContainerName(markers[i]);
						if(path != null && path.equals(this.getPath())) {
						
							Integer line_number = (Integer) markers[i]
									.getAttribute(IMarker.LINE_NUMBER);
							//String s = this.getLocation();
							if (line_number != null && line_number.intValue()==
									Integer.valueOf(this.getLocation()).intValue()) {
								marker = markers[i];
								break;
							}
						}
					}
				}
			}

			if (marker == null) {
				// -- маркер не найден
				return false;
			} else {
				markerId = marker.getId();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	
	/**
	 * Открывает редактор для файла в проекте и переходит на выделенный текст
	 */
	public void goAtBookmark() {
		try {

			IMarker marker = MarkerUtil.getMarkerById(markerId);
			
			if (marker == null) {
				//System.err.println("Маркер не найден!");
				System.err.println("Marker not found!");
				setErrorState();
				return;
			}

			IResource resource = marker.getResource();
			if (resource != null && resource.exists()
					&& resource.getType() == IResource.FILE) {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				// -- open editor for this file
				IEditorPart part = IDE.openEditor(page, (IFile) resource, true);
				part.setFocus();

				IGotoMarker gotomarker = (IGotoMarker) part
						.getAdapter(IGotoMarker.class);
				if (gotomarker == null) {
					System.err
					.println("Adapter IGotoMarker for this editor not found!");
					//.println("Адаптер IGotoMarker для данного редактора не  найден!");
					setErrorState();
					return;
				}
				gotomarker.gotoMarker(marker);
				clearErrorState();
			}
		} catch (CoreException ex) {
			System.err.println(ex.getMessage());
			setErrorState();
		}

		/*
		 * 
		 * IWorkbenchPage page = PlatformUI.getWorkbench()
		 * .getActiveWorkbenchWindow().getActivePage(); marker.getAttribute("")
		 * String relPath = path + "/" + resource; //-- Ищем файл IResource
		 * resource = ResourcesPlugin.getWorkspace()
		 * .getRoot().findMember(relPath); if (resource!=null &&
		 * resource.exists() && resource.getType()==IResource.FILE) { //-- open
		 * editor for this file IEditorPart part = IDE.openEditor(page, (IFile)
		 * resource, true); part.setFocus();
		 * 
		 * IGotoMarker gotomarker =
		 * (IGotoMarker)part.getAdapter(IGotoMarker.class); if(gotomarker==null) {
		 * System.err.println("Адаптер IGotoMarker для данного редактора не
		 * найден!"); return; } gotomarker.gotoMarker(marker);
		 * 
		 * //if(part instanceof TextEditor) { //
		 * ((TextEditor)part).getAdapter(IGotoMarker.class) .gotoMarker(marker);
		 * //}
		 * 
		 * if(part instanceof ITextEditor) {
		 * 
		 * IFindReplaceTarget frt= part == null ? null : (IFindReplaceTarget)
		 * part.getAdapter(IFindReplaceTarget.class);
		 * 
		 * if(frt==null) { System.err.println("IFindReplaceTarget is null!");
		 * return; } if(frt.findAndSelect(0, location, true, true, false) == -1) {
		 * System.err.println("Position for text "+location+"not found"); } }
		 * else { System.err.println("Проблемы с редактором!"); } } }
		 * catch(Exception ex) { System.err.println(ex.getMessage()); }
		 */
	}

	
	/**
	 * Конструирует контент проходя по всем дочерним узлам HtmlTextNode
	 * implements IHtmlPart
	 * 
	 * @return
	 */
	public String getFullHtmltext() {
		if(isNotRunInBatch())
			return "";

		String text = "";

		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] instanceof IHtmlPart) {
				text += ((IHtmlPart) getChildren()[i]).getFullHtmltext();
			}
		}

		return text;
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

	
	public long getMarkerId() {
		return markerId;
	}

	
	public void setMarkerId(long markerId) {
		this.markerId = markerId;
	}


	public String getSelection() {
		return selection;
	}


	public void setSelection(String selection) {
		this.selection = selection;
	}

	//public String getDescription() {
	//	return getName();
	//}
	
	
	public String getDescription() {
		return getName();
	}
	
	
	public void setDescription(String description) {
		this.setName(description);
	}
	
	
}
