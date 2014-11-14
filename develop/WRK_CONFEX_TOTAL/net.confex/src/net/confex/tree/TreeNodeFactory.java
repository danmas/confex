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
package net.confex.tree;

import net.confex.utils.Utils;


public class TreeNodeFactory implements ITreeNodeFactory {
	
	//TODO убрать net.confex.html
	private String current_tree_packages = "net.confex.tree,net.confex.html";  
	
	
	/**
	 * По имени класса возвращает класс.
	 * если класс не найден возвращается null
	 * 
	 * @param class_name
	 * @return Class 
	 */
	@SuppressWarnings("unchecked")
	public Class getClassForName(String class_name) {
	     String[] tok = Utils.tokenize(current_tree_packages, ",");
	     Class cl = null;
	     for (int i = 0; i < tok.length; i++) {
	    	 String tree_package = tok[i];
	    	 
	 		if(!tree_package.endsWith("."))
	 			tree_package += ".";
	    	 
	 		try {
	 			cl = Class.forName(tree_package+class_name);
	 			break;
			} catch(ClassNotFoundException ex) {}
	     }
	     //-- если класс не найден то ищем в точках расширения
	     //if(cl==null) {
	     //	 cl = getClassFromExtensionPoints(class_name);
	     //}
	     return cl;
	}

	
}
