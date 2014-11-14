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
package net.confex.action;

import net.confex.translations.ITranslatable;
import net.confex.translations.Translator;
import net.confex.utils.ImageResource;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWorkbenchWindow;

public abstract class TranslatableAction  extends Action implements ITranslatable {
    
	protected IWorkbenchWindow window;

    protected abstract String getTextKey();
    protected abstract String getIconFileName();
    protected abstract String getID();
    
    
    protected TranslatableAction() {
		init0();
    }
    
    
	public TranslatableAction(IWorkbenchWindow window) {
		this.window = window;
		init0();
	}
	
	
	private void init0() {
		setText(Translator.getString(getTextKey()));
		setImageDescriptor(ImageResource
				.getImageDescriptor(getIconFileName()));
		setId(getID());
		setActionDefinitionId(getID()); 
		
		Translator.addTranslatable(this);
	}
	
	
	/**
	 * Переопределяем метод removePropertyChangeListener
	 * чтобы удалить эту команду из списка локализуемых
	 */
	public void removePropertyChangeListener(
			final IPropertyChangeListener listener) {
		super.removeListenerObject(listener);
		
		Translator.removeTranslatable(this);
	}
	
	
	public void updateLang() {
		setText(Translator.getString(getTextKey()));
		//System.out.println("Translator.getTranslation(getLocationKey()) "+Translator.getString(getLocationKey()));
	}
}
