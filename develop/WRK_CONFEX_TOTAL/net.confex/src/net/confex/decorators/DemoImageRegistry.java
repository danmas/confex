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
package net.confex.decorators;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author balajik
 * 
 * Image registry for Resources. This class is a utility class to get the image
 * given the image key.
 * 
 */
public class DemoImageRegistry {
	private ImageRegistry imageRegistry;

	/**
	 * Constructor for DemoImageRegistry.
	 */
	public DemoImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
		}
	}

	/**
	 * Get the image from image registry given the key
	 * 
	 * @param key
	 *            Image key
	 * @return Image
	 */
	public Image getImage(String key) {
		return imageRegistry.get(key);
	}

	/**
	 * Assosiate the image with image key
	 * 
	 * @param key
	 *            Image key
	 * @param image
	 *            Image to be assosiated with image key
	 * 
	 */
	public void setImage(String key, Image image) {
		imageRegistry.put(key, image);
	}
}
