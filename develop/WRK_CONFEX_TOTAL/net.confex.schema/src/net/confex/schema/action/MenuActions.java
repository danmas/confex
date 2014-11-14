package net.confex.schema.action;


import java.util.List;

import net.confex.schema.command.PasteCommand;
import net.confex.schema.dnd.DataElementFactory;
import net.confex.schema.editor.ConfexDiagramEditor;
import net.confex.schema.editor.ConfexDiagramPlugin;
import net.confex.schema.editor.SchemaImages;
import net.confex.schema.model.StateContainer;
import net.confex.schema.model.StickyNote;
import net.confex.schema.part.SimpleContainerPart;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;



public class MenuActions {

	public static class StandartStateAction extends StateAction {
	    public static final String ID = MenuActions.StandartStateAction.class.getName() + "_id";
	    public static final String LABEL = "Standart state";
	    public static final String TOOL_TIP = "Set standart state for all selected StateContainers";

		public StandartStateAction() {
			super(LABEL,StateContainer.STANDART_STATE);
	        setId(ID);
	        setToolTipText(TOOL_TIP);
	        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
	        		ConfexDiagramPlugin.PLUGIN_ID, "icons/standart_state.png"));
		}
	}
	
		
	
	
	public static class CompactStateAction extends StateAction {
	    public static final String ID = MenuActions.CompactStateAction.class.getName() + "_id";
	    public static final String LABEL = "Compact state";
	    public static final String TOOL_TIP = "Set compact state for all selected StateContainers";

		public CompactStateAction() {
			super(LABEL,StateContainer.COMPACT_STATE);
	        setId(ID);
	        setToolTipText(TOOL_TIP);
	        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
	        		ConfexDiagramPlugin.PLUGIN_ID, "icons/compact_state.png"));
		}
	}

	
	public static class StateAction extends Action {
		protected String state;

		
	    public StateAction(String label, String setting_state) {
	        super(label);
	        state = setting_state;
	    }

	    
	    /**
	     * 
	     */
	    public void run() {
	        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	        
	        IStructuredSelection selection = (IStructuredSelection)page.getSelection();
	        
	        List list = selection.toList();
	        for(int i=0; i<list.size();i++) {
	        	Object o = list.get(i);
	        	if(o instanceof SimpleContainerPart) {
	        		((SimpleContainerPart)o).changeState(state);
	        	}
	        }
	    }


	    /**
	     *  Возвращает true если есть хотя бы один StateContainer
	     */	
	    public boolean isEnabled() {
	        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage();
	        
	        IStructuredSelection selection = (IStructuredSelection)page.getSelection();
	        
	        List list = selection.toList();
	        for(int i=0; i<list.size();i++) {
	        	Object o = list.get(i);
	        	if(o instanceof SimpleContainerPart) {
	        		if(((SimpleContainerPart)o).getModel() instanceof StateContainer) {
	        			return true;
	        		}
	        	}
	        }
	    	
	    	return false;
	    }
		
	}

	
	public static class AddLabelAction extends Action {
	    public static final String ID = MenuActions.AddLabelAction.class.getName() + "_id";
	    public static final String LABEL = "Add StickyNote";
	    public static final String TOOL_TIP = "Add label";

	    private ConfexDiagramEditor graph_editor;
	    
	    
	    public AddLabelAction(ConfexDiagramEditor graph_editor) {
	        super(LABEL);
	        this.graph_editor = graph_editor;
	        setId(ID);
	        setToolTipText(TOOL_TIP);
	        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
	        		ConfexDiagramPlugin.PLUGIN_ID, "icons/label16.gif"));
	    }

	    
	    /**
	     * Создаем инструмент и делаем его текущим
	     */
	    public void run() {
	    	//
			EditDomain domain = graph_editor.getEditDomain();
			
			CombinedTemplateCreationEntry elementEntry = new CombinedTemplateCreationEntry("New StickyNote", "Create a new label",
					StickyNote.class, 
					new DataElementFactory(StickyNote.class), 
					AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID, "icons/label16.gif"), 
					AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID, "icons/label24.gif"));

			domain.setActiveTool(elementEntry.createTool());
	    }

	    
	    public boolean isEnabled() {
	    	return true;
	    }
		
	}
	
	
	public static class AddContainerAction extends Action {
	    public static final String ID = MenuActions.AddContainerAction.class.getName() + "_id";
	    public static final String LABEL = "Add Container";
	    public static final String TOOL_TIP = "Add container";

	    private ConfexDiagramEditor graph_editor;
	    
	    
	    public AddContainerAction(ConfexDiagramEditor graph_editor) {
	        super(LABEL);
	        this.graph_editor = graph_editor;
	        setId(ID);
	        setToolTipText(TOOL_TIP);
	        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
	        		ConfexDiagramPlugin.PLUGIN_ID, "icons/container.png"));
	    }

	    
	    /**
	     * Создаем инструмент и делаем его текущим
	     */
	    public void run() {
	    	//
			EditDomain domain = graph_editor.getEditDomain();
			
			CombinedTemplateCreationEntry elementEntry = new CombinedTemplateCreationEntry("New Container", "Create a new container",
					StateContainer.class, 
					new DataElementFactory(StateContainer.class), 
					AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID, "icons/container.png"), 
					AbstractUIPlugin.imageDescriptorFromPlugin(ConfexDiagramPlugin.PLUGIN_ID, "icons/container.png"));

			domain.setActiveTool(elementEntry.createTool());
	    }

	    
	    public boolean isEnabled() {
	    	return true;
	    }
		
	}
	
	
	public static class PasteAction extends Action {
	    public static final String ID = MenuActions.PasteAction.class.getName() + "_id";
	    public static final String LABEL = "Paste";
	    public static final String TOOL_TIP = "Paste Actions";

	    private ConfexDiagramEditor graph_editor;


	    public PasteAction(ConfexDiagramEditor graph_editor) {
	        super(LABEL);
	        this.graph_editor = graph_editor;
 	        setId(ID);
	        setToolTipText(TOOL_TIP);
			ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
			setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
			setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
	    }

	    
	    /**
	     * Создаем инструмент и делаем его текущим
	     */
	    public void run() {
	    	//
			EditDomain domain = graph_editor.getEditDomain();
			
			CombinedTemplateCreationEntry elementEntry = new CombinedTemplateCreationEntry("PasteActions", "PasteActions",
					PasteCommand.class, 
					new DataElementFactory(PasteCommand.class),
					getImageDescriptor(),
					getDisabledImageDescriptor());

			domain.setActiveTool(elementEntry.createTool());
	    }

	    
	    public boolean isEnabled() {
	    	return true;
	    }
		
	}
	
}
