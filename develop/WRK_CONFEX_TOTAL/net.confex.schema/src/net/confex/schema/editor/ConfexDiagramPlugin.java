package net.confex.schema.editor;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The plugin implementation class
 */
public class ConfexDiagramPlugin extends AbstractUIPlugin
{

	/** the plugin id */
	public static final String PLUGIN_ID = "net.confex.schema.ConfexDiagramPlugin";   

	//The shared instance.
	private static ConfexDiagramPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;


	/**
	 * The constructor.
	 */
	public ConfexDiagramPlugin()
	{
		super();
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle.getBundle("net.confex.schema.CodemarkDiagramPluginResources");
		}
		catch (MissingResourceException x)
		{
			resourceBundle = null;
		}
	}

	/**
	 * The constructor.
	 */
	public ConfexDiagramPlugin(IPluginDescriptor descriptor)
	{
		super(descriptor);
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle.getBundle("net.confex.schema.CodemarkDiagramPluginResources");
		}
		catch (MissingResourceException x)
		{
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static ConfexDiagramPlugin getDefault()
	{
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace()
	{
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = ConfexDiagramPlugin.getDefault().getResourceBundle();
		try
		{
			return bundle.getString(key);
		}
		catch (MissingResourceException e)
		{
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	}
}