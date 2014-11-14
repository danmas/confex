package ru.erv.drakongen;

import java.util.HashMap;
import java.util.Map;

public class Settings {

	private static boolean debug = false;
	
	
	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		Settings.debug = debug;
	}

	protected static Map<String,String> proprties = new HashMap<String,String>();
	
	
	public static void setProperty(String prop_name,String prop_value) {
		proprties.put(prop_name,prop_value);
	}
	
	public static String getProperty(String prop_name) {
		return proprties.get(prop_name);
	}
	
}
