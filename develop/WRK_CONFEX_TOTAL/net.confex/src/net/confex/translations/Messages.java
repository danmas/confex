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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages implements IMessages  {
	private static final String BUNDLE_NAME = "net.confex.translations.messages"; //$NON-NLS-1$

	
	private ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME, new Locale("en"));

	
	public Messages() {
	}

	
	public void switchLocale(String lang) {
		Locale locale = new Locale(lang);
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}
	
	
	public String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
			//return resourceBundle.getRESOURCE_BUNDLE().getString(key);
		} catch (MissingResourceException e) {
			return null; //'!' + key + '!';
		}
	}
	
	
	/*
	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
			//return resourceBundle.getRESOURCE_BUNDLE().getString(key);
		} catch (MissingResourceException e) {
			return null; //'!' + key + '!';
		}
	}*/
	
}
