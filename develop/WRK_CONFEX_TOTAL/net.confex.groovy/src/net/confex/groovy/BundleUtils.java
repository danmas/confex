package net.confex.groovy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

public class BundleUtils {

	/**
	 * Compute the classpath entries for the symbolic plugin name provided.
	 * 
	 * @param pluginID
	 *            The plugin on which the classpath is to be calculated.
	 * @return A collection of classpath entries in URL format such as <code>
	 * file:///C:/path/app.jar </code>
	 * @throws IOException
	 *             when a plugin dependency is not found.
	 * @throws BundleException
	 *             when a plugin dependency is not found.
	 */
	public static Collection<String> getBundleClassPath(String pluginID)
			throws IOException, BundleException {
		Collection<String> result = new LinkedHashSet<String>(); // no
																	// duplicates,
																	// preserve
																	// order
		getBundleClassPath(pluginID, result, 1);  
		return result;
	}

	private static void getBundleClassPath(String pluginID,
			Collection<String> result, int nestinglevel) throws IOException,
			BundleException {
		Bundle bundle = Platform.getBundle(pluginID);
		if (bundle == null)
			throw new BundleException(pluginID
					+ " cannot be retrieved from the Platform");
		// first the entries from this plugin itself
		result.addAll(getClassPath(bundle));
		// next the entries from dependent plugins
		String requires = (String) bundle.getHeaders().get(
				Constants.REQUIRE_BUNDLE);
		ManifestElement[] elements = ManifestElement.parseHeader(
				Constants.REQUIRE_BUNDLE, requires);
		if (elements != null) {
			// ignore elements that are not reexported?
			for (int i = 0; i < elements.length; ++i) {
				ManifestElement element = elements[i];
				Bundle requiredBundle = Platform.getBundle(element.getValue());
				if (requiredBundle == null)
					throw new BundleException(pluginID + " requires bundle "
							+ requiredBundle.getSymbolicName()
							+ " which cannot be retrieved from the Platform");
				if (nestinglevel == 1) {
					getBundleClassPath(requiredBundle.getSymbolicName(),
							result, nestinglevel + 1);
				} else {
					String[] visibility = element
							.getDirectives(Constants.VISIBILITY_DIRECTIVE);
					if (visibility != null
							&& visibility[0].equalsIgnoreCase("reexport"))
						getBundleClassPath(requiredBundle.getSymbolicName(),
								result, nestinglevel + 1);
				}
			}
		}
	}

	private static Collection<String> getClassPath(Bundle bundle)
			throws IOException, BundleException {
		Collection<String> result = new ArrayList<String>();
		String requires = (String) bundle.getHeaders().get(
				Constants.BUNDLE_CLASSPATH);
		if (requires == null)
			requires = ".";
		ManifestElement[] elements = ManifestElement.parseHeader(
				Constants.BUNDLE_CLASSPATH, requires);
		if (elements != null) {
			for (int i = 0; i < elements.length; ++i) {
				ManifestElement element = elements[i];
				String value = element.getValue();
				if (".".equals(value))
					value = "/";
				URL url = bundle.getEntry(value);
				if (url != null) {
					URL resolvedURL = FileLocator.resolve(url);
					String filestring = FileLocator.toFileURL(resolvedURL)
							.getFile();
					File f = new File(filestring);
					// URL requires trailing / if a directory
					if (f.isDirectory() && !filestring.endsWith("/"))
						filestring += "/";
					result.add("file://" + filestring);
					// System.out.println(" added "+"file://"+filestring);
				}
			}
		}
		return result;
	}

	
	/**
	 * TODO !!! УДАЛИТЬ!
	 * 
	 * @param pluginID
	 * @return
	 * @throws Exception
	 */
	public static List getClasspathPaths(String pluginID)
    {
      List result = new ArrayList();
      try
      {
        Bundle bundle = Platform.getBundle(pluginID);
        
        
        String requires = (String)bundle.getHeaders().get(org.eclipse.osgi.framework.internal.core.Constants.BUNDLE_CLASSPATH);
        if (requires == null)
        {
          requires = ".";
        }
        ManifestElement[] elements = ManifestElement.parseHeader(org.eclipse.osgi.framework.internal.core.Constants.BUNDLE_CLASSPATH, requires);
        if (elements != null)
        {
          for (int i = 0; i < elements.length; ++i)
          {
            ManifestElement element = elements[i];
            String value = element.getValue();
            if (".".equals(value))
            {
              value = "/";
            }
            try
            {
              URL url = bundle.getEntry(value);
              if (url != null)
              {
                URL resolvedURL = Platform.resolve(url);
                String resolvedURLString = resolvedURL.toString();
                if (resolvedURLString.endsWith("!/"))
                {
                  resolvedURLString = resolvedURL.getFile();
                  resolvedURLString = resolvedURLString.substring(0,resolvedURLString.length() - "!/".length());
                }
                if (resolvedURLString.startsWith("file:"))
                {
                	String s = resolvedURLString.substring("file:".length());
                	
                	result.add(s);
                }
                else
                {
                	String s = Platform.asLocalURL(url).getFile(); 
                  result.add(s);
                }
              }
            }
            catch (IOException exception)
            {
              exception.printStackTrace();
            }
            break;
          }
        }
      }
      catch (BundleException exception)
      {
    	  exception.printStackTrace();
      }
      return result;
    }
	
}
