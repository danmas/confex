/*******************************************************************************
 * Copyright (c) 2006 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package net.confex.translations;

import java.io.IOException;
import java.util.Locale;

import net.confex.Constants;
import net.confex.application.ConfexPlugin;
import net.confex.utils.ImageResource;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Class displays a PropertyPage to select the language used in RSSOwl
 * 
 */
public class LanguagePreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage, ITranslatable {

	private String selectedLanguage;

	private ScopedPreferenceStore preferences;

	/**
	 * Method declared on IPreferencePage. Subclasses should override
	 */
	public boolean performOk() {
		if (selectedLanguage != null) { // &&
										// !selectedLanguage.equals(Dictionary.selectedLanguage))
										// {
			// Translator.setLangDictionary(new ConfexDictionary(new
			// Locale(selectedLanguage)));
			Translator.setLang(selectedLanguage);
			Translator.updateTranslatables();
			// -- сохраняем свойство PreferedLang
			preferences.setValue(Constants.PREFERED_LANG, selectedLanguage);
			try {
				preferences.save();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		return super.performOk();
	}

	Composite composite = null;

	public void dispose() {
		super.dispose();
		Translator.removeTranslatable(this);
	}

	protected Control createContents(Composite parent) {
		Translator.addTranslatable(this);

		if (composite == null) {
			composite = new Composite(parent, SWT.NULL);
			composite.getShell().setText(
					Translator.getString("PROPERTIES"));
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			parent.getShell().setText(Translator.getString("PROPERTIES"));
			setTitle(Translator.getString("LANG_PROPERTIES"));

			composite.setLayout(gridLayout);

			// for (int a = 0; a < Dictionary.languages.size(); a++) {
			// if(button_ru_RU==null) {
			addLangButton("ru", "RU");
			addLangButton("en", "EN");
			// addLangButton("de","DE");
			// }
			// }
		}
		return composite;
	}

	private Button addLangButton(String lang, String country) {
		// -- Locale of the language
		final Locale locale = new Locale(lang, country);
		String icon_file_name = locale.getCountry();
		icon_file_name = "flags/" + icon_file_name.toLowerCase() + ".gif";
		if (ImageResource.getImageDescriptor(icon_file_name) == ImageDescriptor
				.getMissingImageDescriptor()) {
			ImageResource.registerImage(icon_file_name, icon_file_name);
		}

		final Button selectLanguageButton = new Button(composite, SWT.RADIO);
		// selectLanguageButton.setFont(dialogFont);
		/*
		 * //-- добавляем слушателя на удаление
		 * selectLanguageButton.addDisposeListener(DisposeListenerImpl.getInstance());
		 */
		if (Translator.getLang().equals(lang))
			selectLanguageButton.setSelection(true);
		try {
			Image img = ImageResource.getImage(icon_file_name);
			// Image new_img = new
			// Image(composite.getDisplay(),img,SWT.IMAGE_COPY);
			selectLanguageButton.setImage(img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectLanguageButton.setText(Translator.getString(locale
				.getLanguage()));
		// -- добавляем слушателя на выбор
		selectLanguageButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (selectLanguageButton.getSelection())
					selectedLanguage = locale.getLanguage();
			}
		});

		return selectLanguageButton;
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
	}

	public void updateLang() {
		if (composite != null) {
			composite.getShell().setText(
					Translator.getString("PROPERTIES"));
		}
		this.setTitle(Translator.getString("LANG_PROPERTIES"));

	}

}