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

public interface IStateObservable {

	public void addStateObserver(IStateObserver observer);

	public void removeAllStateObservers();

	/**
	 * Известить всех наблюдателей о изменении состояния
	 */
	public void updateStateOservers();

	/**
	 * Устанавливаем для всех дочерних наблюдателя
	 * 
	 * @param observer
	 */
	public void setObserverForAll(IStateObserver observer);

}
