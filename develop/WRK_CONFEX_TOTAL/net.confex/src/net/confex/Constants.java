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
package net.confex;


public class Constants {
	
	
	//public static final String DEFAULT_CONFEX_TREE_PKG = 
	//	"net.confex.tree.,net.confex.db.tree.";
	
	public static final String[] CONFEX_EXTENSIONS = {"*.confex","*.xml","*.*"};
	
	public static final String[] CONFEX_COPY_FILE_EXTENSIONS = {"*.copy.xml","*.confex","*.xml","*.*"};
	
	public static final String DEFAULT_CONFEX_FILE_EXTENSION = "confex";
	public static final String DEFAULT_COPY_FILE_EXTENSION = "copy.xml";
	
	//FIXME !!! Build OS dependency for two next vars
	public static final String DEFAULT_CONFEX_FILE = "~/Confex/confex.confex";
	public static final String DEFAULT_TMP_CONFEX_FILE = "~/Confex/tmp.confex";
	
	public static final String PREFERED_LANG = "PreferedLang";
	
	public static final String EDIT_MODE = "net.confex.EditMode";
	
}
