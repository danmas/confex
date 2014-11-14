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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;

/**
 * Set of images that are used for decorating resources are maintained here.
 * This acts as a image registry and hence there is a single copy of the image
 * files floating around the project.
 * 
 */
public class DemoImages {
	/**
	 * Lock Image Descriptor
	 */
	private static final ImageDescriptor lockDescriptor = ImageDescriptor
			.createFromFile(DemoImages.class, "read_only.gif");

	/**
	 * Dirty Image Descriptor
	 */
	private static final ImageDescriptor errorDescriptor = ImageDescriptor
			.createFromFile(DemoImages.class, "error_ovr.gif");

	/**
	 * Key Image Descriptor
	 */
	private static final ImageDescriptor keyDescriptor = ImageDescriptor
			.createFromFile(DemoImages.class, "run_ovr.gif");

	/**
	 * Extract Image Descriptor
	 */
	private static final ImageDescriptor successDescriptor = ImageDescriptor
			.createFromFile(DemoImages.class, "success_ovr.gif");

	
	private static final ImageDescriptor lockRunInBatchDescriptor = ImageDescriptor
			.createFromFile(DemoImages.class, "lock_run_in_batch_ovr.gif");
	
	
	/**
	 * Constructor for DemoImages.
	 */
	public DemoImages() {
		super();
	}

	/**
	 * Get the lock image data
	 * 
	 * @return image data for the lock flag
	 */
	public ImageData getLockImageData() {
		return lockDescriptor.getImageData();
	}

	/**
	 * Get the dirty flag image data
	 * 
	 * @return iamge data for the dirty flag
	 */
	public ImageData getErrorImageData() {
		return errorDescriptor.getImageData();
	}

	/**
	 * Get the extract image data
	 * 
	 * @return image data for the extract flag
	 * 
	 */
	public ImageData getSuccessImageData() {
		return successDescriptor.getImageData();
	}

	/**
	 * Get the key image data
	 * 
	 * @return image data for the extract flag
	 * 
	 */
	public ImageData getRunningImageData() {
		return keyDescriptor.getImageData();
	}

	public ImageData getLockRunInBatchImageData() {
		return lockRunInBatchDescriptor.getImageData();
	}
	
	
	/**
	 * Get the image data depending on the key
	 * 
	 * @return image data
	 * 
	 */
	public ImageData getImageData(String imageKey) {
		if (imageKey.equals("Lock")) {
			return getLockImageData();
		}
		if (imageKey.equals("Error")) {
			return getErrorImageData();
		}
		if (imageKey.equals("Success")) {
			return getSuccessImageData();
		}
		if (imageKey.equals("Run")) {
			return getRunningImageData();
		}
		if (imageKey.equals("LockRunInBatch")) {
			return getLockRunInBatchImageData();
		}
		
		return null;
	}

	// public ImageDescriptor

}
