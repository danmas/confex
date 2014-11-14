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

import java.util.List;

public interface ITreeNodeState {

	
	public static final int STATE_ANY = 0;
	
	public static final int STATE_NORMAL = 0;

	public static final int STATE_ERROR = 1;
	
	public static final int STATE_SUCCESS = 2;
	
	public static final int STATE_RUN = 4;
	
	public static final int STATE_LOCKED = 8;
	
	
	public boolean isInRuningState(int for_state);

	//public boolean isInLockingState(int for_state);
	public boolean isLocked();
	
	
	public void setLockState();
	public void clearLockState();

	public void setRunState();
	public void setNormalState();
	public void setSuccessState();
	
	public void setErrorState();
	public void clearErrorState();
	/**
	 * 	проверяем родителя если нет больше ошибок то он
	 *  становится нормальным
	 */
	public void resetErrorStateByChildren();

	
	/**
	 * Заполнить список для всех объектов дерева 
	 * в определенном состоянии
	 * STATE_ANY - значит состояние не важно
	 * 
	 * @param sate_list
	 * @param for_state
	 */
	@SuppressWarnings("unchecked")
	public void fillStateList(List sate_list,int for_state);
	
	//TODO: change at inNotUsed()
	public boolean isNotRunInBatch();

	public boolean isUsed();
	
	public void setNotRunInBatch(boolean not_run_in_batch);
	
	//-- невидим в пользовательском режиме 
	public boolean isNotVisInUserMode();


}
