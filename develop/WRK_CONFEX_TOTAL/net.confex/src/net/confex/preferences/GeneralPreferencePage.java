package net.confex.preferences;

import java.io.IOException;

import net.confex.Constants;
import net.confex.application.ConfexPlugin;
import net.confex.translations.ITranslatable;
import net.confex.translations.Translator;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class GeneralPreferencePage extends PreferencePage 
	implements IWorkbenchPreferencePage, ITranslatable {

    Button fEditModeBox;

    /*public final OverlayPreferenceStore.OverlayKey[] fKeys = 
    	new OverlayPreferenceStore.OverlayKey[] {
    		new OverlayPreferenceStore.OverlayKey(
    				OverlayPreferenceStore.BOOLEAN, Constants.EDIT_MODE
    			)
    };*/

    //OverlayPreferenceStore fOverlayStore;
	private ScopedPreferenceStore preferences;


    /*public GeneralPreferencePage() {
    	System.err.println(" GeneralPreferencePage() ");
        //fOverlayStore = new OverlayPreferenceStore(
        //		ConfexPlugin.getDefault().getPreferenceStore(), fKeys);

        //fOverlayStore.load();
        //fOverlayStore.start();
    };*/

    //PREF_EDIT_MODE
    /*public GeneralPreferencePage(OverlayPreferenceStore fOverlayStore) {

        this.setTitle(Translator.getString("GeneralPreferencePage.CONFEX_PREFS")); //$NON-NLS-1$
        this.fOverlayStore = fOverlayStore;
    }*/

    //private boolean edit_mode;
	Composite composite = null;
	private boolean edit_mode;
    
    protected Control createContents(Composite parent) {
    	Translator.addTranslatable(this);
    	
    	composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        composite.setLayout(layout);

        fEditModeBox = new Button(composite, SWT.CHECK);
        fEditModeBox.setText(Translator.getString("GeneralPreferencePage.EDIT_MODE")); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalAlignment = GridData.BEGINNING;
        gd.horizontalSpan = 2;
        fEditModeBox.setLayoutData(gd);
        
        fEditModeBox.setSelection(edit_mode);
        fEditModeBox.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }


            public void widgetSelected(SelectionEvent e) {
                //fOverlayStore.setValue(Constants.EDIT_MODE, fEditModeBox.getSelection());
                if (fEditModeBox.getSelection()) {
                    edit_mode = true;
                } else
                    edit_mode = false;
            }
        });
        
        //initialize();

        return composite;
    }


    public void dispose() {
        //this.setPreferenceStore(null);
		Translator.removeTranslatable(this);

        super.dispose();
    }


	/**
	 * Hook method to get a page specific preference store. Reimplement this
	 * method if a page don't want to use its parent's preference store.
	 */
	protected IPreferenceStore doGetPreferenceStore() {
		// return WorkbenchPlugin.getDefault().getPreferenceStore();
		return preferences;
	}

	
	/**
	 * @see IWorkbenchPreferencePage
	 */
    public void init(IWorkbench workbench) {
		preferences = new ScopedPreferenceStore(new ConfigurationScope(),
				ConfexPlugin.ID);
		setPreferenceStore(preferences);
		edit_mode = ConfexPlugin.getDefault().isEnableEditMode(); 
    }


    /*private void initialize() {
        fEditModeBox.getDisplay().asyncExec(new Runnable() {

            public void run() {
            	System.err.println(" fEditModeBox.setSelection ("+fOverlayStore.getBoolean(Constants.EDIT_MODE));
                fEditModeBox.setSelection(fOverlayStore.getBoolean(Constants.EDIT_MODE));//$NON-NLS-1$
                //if (fAutoCommitBox.getSelection()) {
                //    fCommitOnCloseBox.setEnabled(false);
                //} else
                //    fCommitOnCloseBox.setEnabled(true);
            }
        });

    }*/


    protected void performDefaults() {
        //((OverlayPreferenceStore) fOverlayStore).loadDefaults();
        edit_mode = true;
        fEditModeBox.setSelection(true);
        ConfexPlugin.getDefault().setEnableEditMode(true);
		// -- сохраняем свойство PreferedLang
		preferences.setValue(Constants.EDIT_MODE, edit_mode?"true":"false");
		try {
			preferences.save();
		} catch (IOException e) {System.err.println(e.getMessage());}
        
        super.performDefaults();
    }


    public boolean performOk() {
        //((OverlayPreferenceStore) fOverlayStore).propagate();
        ConfexPlugin.getDefault().setEnableEditMode(edit_mode);
		// -- сохраняем свойство EDIT_MODE
		preferences.setValue(Constants.EDIT_MODE, edit_mode?"true":"false");
		try {
			preferences.save();
		} catch (IOException e) {System.err.println(e.getMessage());}
        return true;
    }

    
	public void updateLang() {
        fEditModeBox.setText(Translator.getString("GeneralPreferencePage.EDIT_MODE")); //$NON-NLS-1$
        this.setTitle(Translator.getString("GeneralPreferencePage.CONFEX_PREFS")); //$NON-NLS-1$
	}
    

}
