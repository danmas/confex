package net.confex.app;

import net.confex.app.core.ConfexException;
import net.confex.app.core.IElement;
import net.confex.app.core.IModel;
import net.confex.app.core.internal.ConfexModel;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Confex extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "net.confex.app";

	public static final String APP_ID = "net.confex.app.Application";

	// The shared instance
	private static Confex plugin;

	private static IModel confexModel;

	/**
	 * The constructor
	 */
	public Confex() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		ConfexModel model = new ConfexModel();
		model.init();
		confexModel = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		confexModel = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Confex getDefault() {
		return plugin;
	}

	public static IModel getModel() {
		return confexModel;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static IElement create(IResource resource) throws ConfexException {
		if (resource.getType() == IResource.PROJECT)
			return confexModel.getTree(resource);

		return getModel();
	}

}
