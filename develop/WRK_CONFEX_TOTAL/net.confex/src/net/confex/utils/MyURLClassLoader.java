package net.confex.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;


public class MyURLClassLoader extends URLClassLoader {


    public MyURLClassLoader(String fileName) throws IOException {
        this(new File(fileName).toURL()); 
    }


    public MyURLClassLoader(URL url) {
        this(new URL[] {url});
    }


    public MyURLClassLoader(URL[] urls) {
        super(urls, MyURLClassLoader.class.getClassLoader());
    }

    /**
     * adds a classpath to this classloader.  
     * @param path is a jar file or a directory.
     * @see #addURL(URL)
     */
    public void addClasspath(final String path) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    File f = new File(path);
                    URL newURL = f.toURI().toURL();
                    URL[] urls = getURLs();
                    for (int i=0; i<urls.length; i++) {
                        if (urls[i].equals(newURL)) return null;
                    }
                    addURL(newURL);
                } catch (MalformedURLException e) {
                    //TODO: fail through ?
                }
                return null;
            }
        });
    }

    
    /*
    public Class[] getAssignableClasses(Class type) throws IOException {
        List classes = new ArrayList();
        URL[] urls = getURLs();
        for (int i = 0; i < urls.length; ++i) {
            URL url = urls[i];
            File file = new File(url.getFile());
            if (!file.isDirectory() && file.exists() && file.canRead()) {
                ZipFile zipFile = null;
                try {
                    zipFile = new ZipFile(file);
                } catch (IOException ex) {
                }
                for (Iterator it = new EnumerationIterator(zipFile.entries()); it.hasNext();) {
                    Class cls = null;
                    String entryName = ((ZipEntry) it.next()).getName();
                    String className = Utilities.changeFileNameToClassName(entryName);
                    if (className != null) {

                        try {
                            cls = loadClass(className);
                        } catch (Throwable th) {

                        }
                        if (cls != null) {
                            if (type.isAssignableFrom(cls)) {
                                classes.add(cls);
                            }
                        }
                    }
                }
            }
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }


    protected synchronized Class findClass(String className) throws ClassNotFoundException {
        Class cls = (Class) _classes.get(className);
        if (cls == null) {
            cls = super.findClass(className);
            _classes.put(className, cls);
        }
        return cls;
    }


    protected void classHasBeenLoaded(Class cls) {
    }
    */
}
