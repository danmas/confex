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

import java.util.Vector;

import net.confex.tree.ITreeNodeState;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class ConfexRunningDecorator extends LabelProvider implements
		ILabelDecorator {

	// private static final ImageDescriptor runningDescriptor = ImageDescriptor.
	// createFromFile (ConfexRunningDecorator.class, "dec-running.gif");

	public String decorateText(String text, Object element) {
		if (element instanceof ITreeNodeState) {
			return "" + text;
		}
		return text;
	}

	
	@SuppressWarnings("unchecked")
	public Image decorateImage(Image baseImage, Object element) {
		Image image = baseImage;

		if (element instanceof ITreeNodeState) {
			Vector image_keys = new Vector();
			if (((ITreeNodeState) element).isInRuningState(ITreeNodeState.STATE_RUN)) {
				image_keys.add("Run");
				OverlayImageIcon overlayIcon = new OverlayImageIcon(image,
						new DemoImages(), image_keys);
				image = overlayIcon.getImage();
				// return image;
			}
			if (((ITreeNodeState) element)
					.isLocked()) {
				image_keys.add("Lock");
				OverlayImageIcon overlayIcon = new OverlayImageIcon(image,
						new DemoImages(), image_keys);
				image = overlayIcon.getImage();
				// return image;
			}
			if (((ITreeNodeState) element)
					.isNotRunInBatch()) {
				image_keys.add("LockRunInBatch");
				OverlayImageIcon overlayIcon = new OverlayImageIcon(image,
						new DemoImages(), image_keys);
				image = overlayIcon.getImage();
				// return image;
			}
			if (((ITreeNodeState) element)
					.isInRuningState(ITreeNodeState.STATE_ERROR)) {
				image_keys.add("Error");
				OverlayImageIcon overlayIcon = new OverlayImageIcon(image,
						new DemoImages(), image_keys);
				image = overlayIcon.getImage();
				// return image;
			}
			if (((ITreeNodeState) element)
					.isInRuningState(ITreeNodeState.STATE_SUCCESS)) {
				image_keys.add("Success");
				OverlayImageIcon overlayIcon = new OverlayImageIcon(image,
						new DemoImages(), image_keys);
				image = overlayIcon.getImage();
				// return image;
			}
		} else {
			System.err.println("Element not instanceof ITreeNodeState!");
		}
		return image;
	}

	/*
	 * public void decorate(Object element, IDecoration decoration) {
	 * ITreeNodeState node_state = (ITreeNodeState)element; try { if
	 * (node_state.isInState(ITreeNodeState.STATE_RUN)) {
	 * decoration.addOverlay(runningDescriptor); } } catch (Exception ce) {
	 * System.err.println(ce.getMessage()); } }
	 * 
	 * 
	 * public static void updateDecorators(ITreeNodeState node_state) {
	 * IDecoratorManager dm = PlatformUI.getWorkbench().getDecoratorManager();
	 * 
	 * dm = ConfexPlugin.getDefault().getWorkbench().getDecoratorManager();
	 * 
	 * ConfexRunningDecorator decorator = (ConfexRunningDecorator)dm
	 * .getBaseLabelProvider("net.confex.decorators.confexRunningDecorator");
	 * if(decorator==null) { System.err.println( "Can't find decorator for
	 * net.confex.decorators.confexRunningDecorator"); return; }
	 * decorator.fireUpdateDecorators(node_state); }
	 * 
	 * 
	 * private void fireUpdateDecorators(ITreeNodeState node_state) { // I can
	 * generate my own event to update the decorators for a given resource final
	 * LabelProviderChangedEvent ev = new LabelProviderChangedEvent(this,
	 * node_state); Display.getDefault().asyncExec(new Runnable() { public void
	 * run() { fireLabelProviderChanged(ev); } }); }
	 * 
	 * 
	 * public static void updateDecorators() { // I can also let the workbench
	 * generate events to update all resources affected by a decorator
	 * Display.getDefault().asyncExec(new Runnable() { public void run() {
	 * PlatformUI.getWorkbench().getDecoratorManager()
	 * .update("net.confex.decorators.confexRunningDecorator"); } }); }
	 */

}
