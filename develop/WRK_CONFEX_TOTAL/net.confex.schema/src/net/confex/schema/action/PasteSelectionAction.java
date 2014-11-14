package net.confex.schema.action;

import net.confex.schema.command.PasteCommand;
import net.confex.schema.editor.ConfexDiagramEditor;
import net.confex.schema.editor.SchemaClipboard;
import net.confex.schema.tools.SchemaSelectionTool;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Tool;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;


public class PasteSelectionAction extends SelectionAction {

	PasteCommand pasteCommand;
	
	/* * @deprecated Use ActionFactory.PASTE.getId() instead. * */
	//public static final String ID = ActionFactory.PASTE.getId();
	
	private String contents="";  //-- специальный XML копирования
	
	
	/**
	 * Constructs a <code>PasteAction</code> using the specified part.
	 * @param part The part for this action
	 */
	public PasteSelectionAction(ConfexDiagramEditor part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	
	/**
	 * Initializes this action's text and images.
	 */
	protected void init() {
		super.init();
		setText(GEFMessages.PasteAction_Label);
		setToolTipText(GEFMessages.PasteAction_Tooltip);
		setId(ActionFactory.PASTE.getId());
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
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
		pasteCommand = null;
		if(this.getWorkbenchPart() instanceof ConfexDiagramEditor) {
			ConfexDiagramEditor sde = (ConfexDiagramEditor) this.getWorkbenchPart();
			
			SchemaClipboard clipboard = sde.getClipboard();
			Tool tool = sde.getEditDomain().getActiveTool();
			
			if(tool instanceof SchemaSelectionTool) {
				//-- получаем координаты события
				Point location = ((SchemaSelectionTool)tool).getLocation();
				System.out.println("Paste x= "+location.x + "  y= "+location.y);

				//-- получаем текст из clipboard
				contents = clipboard.getTextContents(); 
				if(contents==null || contents.length()==0)
					return false;
				pasteCommand =  new PasteCommand(contents,sde.getSchema());
				return true;
				
				//EditPart contents_edit_part = sde.getGraphicalViewer().getContents();
			}
		}
		return false;
	}


	
	/**
	 * Выполняем команду вставки элементов буфера обмена
	 */
	public void run() {
		pasteCommand.execute();
	}

}
