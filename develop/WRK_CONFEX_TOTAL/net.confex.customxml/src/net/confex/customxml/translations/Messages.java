package net.confex.customxml.translations;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.confex.translations.IMessages;

public class Messages  implements IMessages {
	private static final String BUNDLE_NAME = "net.confex.customxml.translations.messages"; //$NON-NLS-1$

	
	private ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME, new Locale("en"));

	
	public Messages() {
	}

	
	public void switchLocale(String lang) {
		Locale locale = new Locale(lang);
		
		RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME, locale);
		//-- System.out.println("Локализация "+lang+" установлена.");
	}
	

	public String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
			//return resourceBundle.getRESOURCE_BUNDLE().getString(key);
		} catch (MissingResourceException e) {
			return null; //'!' + key + '!';
		}
	}

	
	/*
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
			//return resourceBundle.getRESOURCE_BUNDLE().getString(key);
		} catch (MissingResourceException e) {
			return null; //'!' + key + '!';
		}
	}*/
	
}
