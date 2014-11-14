package net.confex.schema.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.confex.schema.editor.ConfexDiagramEditor;
import net.confex.schema.editor.SchemaClipboard;
import net.confex.schema.model.ModelElement;
import net.confex.schema.model.NodeElement;
import net.confex.schema.model.Schema;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;


//import org.eclipse.gef.ui.actions.


/**
 * Копирует в clipboard выделенные объекты
 * Идея такая: 
 *   - создается специальный XML копирования 
 *     для этого проходим по всем выделенным объектам и создаем XML как в случае сохранения
 * 
 */
public class CopyAction extends SelectionAction
{

	/* * @deprecated Use ActionFactory.COPY.getId() instead. * */
	//public static final String ID = ActionFactory.COPY.getId();
	
	private String contents="";  //-- специальный XML копирования
	
	
	/**
	 * Constructs a <code>CopyAction</code> using the specified part.
	 * @param part The part for this action
	 */
	public CopyAction(ConfexDiagramEditor part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	
	/**
	 * Initializes this action's text and images.
	 */
	protected void init() {
		super.init();
		setText(GEFMessages.CopyAction_Label);
		setToolTipText(GEFMessages.CopyAction_Tooltip);
		setId(ActionFactory.COPY.getId());
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(
				ISharedImages.IMG_TOOL_COPY_DISABLED));
		setEnabled(false);
	}

	
	/**
	 * Returns <code>true</code> if the selected objects can
	 * be deleted.  Returns <code>false</code> if there are
	 * no objects selected or the selected objects are not
	 * {@link EditPart}s.
	 * @return <code>true</code> if the command should be enabled
	 */
	protected boolean calculateEnabled() {
		List objects = getSelectedObjects();
		
		if (objects.isEmpty())
			return false;
		if (!(objects.get(0) instanceof EditPart))
			return false;
		
		return true;
	}

	
	/**
	 * Performs the copy action on the selected objects.
	 *			//-- если родитель не выбран тогда добавляем в буфер обмена
	 *			//-- добавляем элементы только с одного уровня иерархии
	 *			
	 *			//-- ищем верхний уровень родителя который не выделен
	 */
	public void run() {
		ArrayList copied = new ArrayList();  //-- список всех копируемых элементов
		
		//execute(createCopyCommand(getSelectedObjects()));
		if(this.getWorkbenchPart() instanceof ConfexDiagramEditor) {
			ConfexDiagramEditor sde = (ConfexDiagramEditor) this.getWorkbenchPart();
			SchemaClipboard clipboard = sde.getClipboard();
			boolean flag_err_msg = false;
			
			List objects = getSelectedObjects();
			contents = "<?xml version=\"1.0\" encoding=\"windows-1251\" ?>\n"+
						"<SchemaCopyPart>\n";
			EditPart first_parent = null; 
			for (int i = 0; i < objects.size(); i++) {
				EditPart editpart = (EditPart) objects.get(i);
				//-- если родитель не выбран тогда добавляем в буфер обмена
				//-- добавляем элементы только с одного уровня иерархии
				
				//-- ищем верхний уровень родителя который не выделен
				if(first_parent==null) {
					EditPart ep = editpart.getParent();
					while(ep.getSelected()!=EditPart.SELECTED_NONE) {
						ep = ep.getParent();
					}
					first_parent = ep;
				}
				
				if(editpart.getParent()!=first_parent && editpart.getParent().getSelected() == EditPart.SELECTED_NONE ) {
					flag_err_msg = true;
					//System.err.println("Не все элементы могут быть скопированы в буфер обмена!");
					//sde.getGraphicalViewer().deselect(editpart);
				} else  if(editpart.getParent().getSelected()== EditPart.SELECTED_NONE) {
					Object object = editpart.getModel();
					if(object instanceof ModelElement ) {
						copied.add(object);
						contents += ((ModelElement)object).getXml();
					} else if(object instanceof Schema ) {
						copied.add(object);
						contents += ((Schema)object).getXml();
					} else{
						MessageDialog.openInformation(PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getShell(),
								"","Can not copy to clipboard non ModelElement!");
						System.err.println("Can not copy to clipboard non ModelElement! ");
					}
					//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().
					//editpart.setSelected(EditPart.SELECTED_NONE);
				} else {
				}
			}
			
			//-- затем сохраняем ребра для копируемых элементов у которых in элементы 
			//   тоже попали в список копирования
			for(Iterator iter=copied.iterator();iter.hasNext();) {
				Object obj = iter.next(); 	
				if(obj instanceof NodeElement) {
					NodeElement element = (NodeElement) obj;
					contents += element.getEdgesXmlForInList(copied);
				}
			}
			
			contents += "</SchemaCopyPart>";
			//-- заносим в clipboard как текст
			clipboard.setTextContents(contents); 
			
			if(flag_err_msg) {
				MessageDialog.openInformation(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						"","Не все элементы могут быть скопированы в буфер обмена!");
				System.err.println("Не все элементы могут быть скопированы в буфер обмена!");
				//TODO Вставить окно диалога
			}
			//sde.getGraphicalViewer().deselectAll();
		}
	}

}
