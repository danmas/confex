package net.confex.editor.tree;



import net.confex.utils.Utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.w3c.dom.Node;


public class BookmarkImpl implements IBookmark {

	private String name = "";
	private String description = "";
	private String resource = "";
	private String path = "";
	private String location = "";
	private String selection = "";
	private long markerId = -1;

	
	
	public BookmarkImpl() {
	}
	
	
	protected String getPropertiesXml() {
		updatePropertiesFromMarker();
		
		String str_xml = ""; //super.getPropertiesXml();
		if (name != null && !name.equals("")) {
			str_xml += "<name>\n"
					+ Utils.toHtmlSpecialEntities(name) + "\n";
			str_xml += "</name>\n";
		}
		if (description != null && !description.equals("")) {
			str_xml += "<description>\n"
					+ Utils.toHtmlSpecialEntities(description) + "\n";
			str_xml += "</description>\n";
		}
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

	
	public void loadFromXml(Node node) {
		loadFromXml_0_0_2(node);
		
		//-- устанавливаем связь с маркером через ID
		if(!connectWithMarker()) {
			connectWithNewBookmark();
		}
	}
	
	public void loadFromXml_0_0_2(Node node) {
		//loadAttribFromXml(node,parent);
		//-- грузим дочерние
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node child = node.getChildNodes().item(i);
			System.out.println(child.getNodeName());
			if(child.getNodeName().equals("#text"))
				continue;
			if(child.getLocalName().equals("properties")) {
				//-- разбираем своиства
				parseProperties(child);
			} else {
				//getConfigTree().parseXml(child, this);
			}
		}
	}
	
	protected void parseProperties(Node properties) {
		//-- грузим дочерние
		for (int i = 0; i < properties.getChildNodes().getLength(); i++) {
			Node child = properties.getChildNodes().item(i);
			parsePropertyXml(child);
		}
	}
	
	
	protected void parsePropertyXml(Node property) {
		//super.parsePropertyXml(property);

		if(property.getNodeName().equals("#text"))
			return;
		
		if (property.getLocalName().equals("name")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setName(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getLocalName().equals("description")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setDescription(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getLocalName().equals("resource")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setResource(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getLocalName().equals("path")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setPath(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getLocalName().equals("location")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setLocation(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getLocalName().equals("selection")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setSelection(Utils.fromHtmlSpecialEntities(text.trim()));
		}
		
	}

	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", resource: ");
		result.append(resource);
		result.append(", path: ");
		result.append(path);
		result.append(", selection: ");
		result.append(selection);
		result.append(", markerId: ");
		result.append(markerId);
		result.append(')');
		return result.toString();
	}
	

	
	/**
	 * Переходим по маркеру
	 */
	public void run() {
		if(markerId==0 || MarkerUtil.getMarkerById(markerId)==null) {
			//-- устанавливаем связь с маркером через ID
			if(!connectWithMarker()) {
				connectWithNewBookmark();
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
	protected void connectWithNewBookmark() {
		// -- если есть номер линии то переходим на нее
		if (!this.getLocation().trim().equals("")) {
			if (MarkerUtil.gotoLine(path, resource, Integer.valueOf(location)
					.intValue())) {
				// -- и создаем новый маркер
				IMarker marker = MarkerUtil.makeBookmark(this);
				if (marker != null) {
					setMarkerId(marker.getId());
				}
			} else {
				//-- не удалось приконнектиться 
			}
		} else if (!this.getSelection().trim().equals("")) {
			// -- переходим по выделенному тексту
			if (MarkerUtil.goAtSelection(path, resource, selection)) {
				// -- и создаем новый маркер
				IMarker marker = MarkerUtil.makeBookmark(this);
				if (marker != null) {
					setMarkerId(marker.getId());
				}
			}
		}
		//-- если не связали с реальным маркером то состояние - ошибка
		// TODO: сделать состояние ошибка загрузки
		if(markerId==-1) {
			System.err.println("Can't connect with real marker!!!");
			//setErrorState();
		}
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
					return;
				}
				gotomarker.gotoMarker(marker);
			}
		} catch (CoreException ex) {
			System.err.println(ex.getMessage());
		}
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

	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getDescription() {
		return description;
	}
	
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
