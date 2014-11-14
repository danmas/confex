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

import net.confex.views.WebBrowserView;

import org.eclipse.ui.IWorkbenchPage;

public interface IRunBrowser {

	public WebBrowserView runBrowser(IWorkbenchPage page/*, IStateObserver observer*/);

}
