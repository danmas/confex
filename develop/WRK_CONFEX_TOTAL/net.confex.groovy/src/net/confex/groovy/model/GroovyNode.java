package net.confex.groovy.model;

/**
 * 
 * 0.9.11
 * @author Roman_Eremeev
 */

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.swt.guibuilder.ApplicationGuiBuilder;

import java.io.File;

import net.confex.application.ConfexPlugin;
import net.confex.directedit.IPropertyDialog;
import net.confex.html.IHtmlPart;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.ICompositeProvider;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.listener.AnsiColorLogger;
import org.apache.tools.ant.types.Path;
import org.codehaus.groovy.ant.Groovyc;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.Node;

public class GroovyNode extends TreeNode implements ICompositeProvider,
		IHtmlPart {

	public GroovyNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree, stateObserver);
	}

	public String getAboutString() {
		return Translator.getString("ABOUT_GroovyNode");
	}

	protected static String default_image_name = "groovy_file.gif";

	public String getDefaultImage() {
		return default_image_name;
	}

	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if (src_text != null && !src_text.equals("")) {
			str_xml += "<groovy>\n";
			String s = "";
			// -- если есть файла с исх текстом и не читать то
			// текста источника не будет
			if (!read_src_text
					&& (getSrcFile() != null && getSrcFile().exists()))
				s = "";
			else
				s = Utils.toHtmlSpecialEntities(src_text);
			str_xml += s + "\n";
			str_xml += "</groovy>\n";
		}
		return str_xml;
	}

	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property, new_node);

		if (property.getNodeName().equals("groovy")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setSrcText(Utils.fromHtmlSpecialEntities(text.trim()));
		} else if (property.getNodeName().equals("file_name")) {
			Node nd = property.getFirstChild();
			String text = "";
			if (nd != null)
				text = nd.getNodeValue();
			this.setSrcFileNameXml(Utils.fromHtmlSpecialEntities(text.trim()));
		}
	}

	public ITreeNode createNewITreeNode() {
		return new GroovyNode(getConfigTree(), null);
	}

	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if (!(prototype instanceof GroovyNode)) {
			System.err
					.println("[GroovyNode.setPropertyLike] prototype NOT instanceof GroovyNode!");
			return;
		}
	}

	protected IPropertyDialog newPropertyDialog(Shell shell) {
		readFromSrcFile();
		return new GroovyPropertyDialog(shell);
	}

	public String getFullText() {
		String groovy_text = getChildrensText(this.getClass());
		return TreeUtils.doAllSubstitutions(this, groovy_text);
	}

	/**
	 * Возвращает HTML текст из Groovy скрипта и собирает текст из всех дочерних
	 * 
	 * @return
	 */
	public String getFullHtmltext() {
		if (isNotRunInBatch())
			return "";

		readFromSrcFile();
		Object ret = doRun("getHtmlText", null, null, null);
		String text;
		if (ret instanceof String)
			text = (String) ret;
		else
			text = "";

		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] instanceof IHtmlPart) {
				text += ((IHtmlPart) getChildren()[i]).getFullHtmltext();
			}
		}
		text = TreeUtils.doAllSubstitutions(this, text);
		return text;
	}

	Composite m_parent = null;

	public void refreshComposite() {
		if (m_parent != null) {
			m_parent.layout();
			m_parent.redraw();
			System.out.println("refreshComposite()");
		}
	}

	public void disposeComposite() {
		m_parent = null;
	}

	public void makeComposite(Composite parent, ViewPart viewPart,
			IProgressMonitor monitor) {
		m_parent = parent;
		try {
			/*
			 * Binding binding = new Binding(); binding.setVariable("parent",
			 * parent); binding.setVariable("thisGroovyNode", this);
			 * binding.setVariable("runViewPart",viewPart);
			 * binding.setVariable("monitor",monitor);
			 * 
			 * evaluate_groovy(binding, monitor);
			 */
			doRun("run", viewPart, monitor, parent);
		} catch (Exception e) {
			System.err.println("[GroovySwtNode.makeComposite()] "
					+ e.getMessage());
			setErrorState();
		}
	}

	/**
	 * Executes method "method_name" scripts of this GroovyNode compiling it
	 * into class
	 * 
	 * @param method_name
	 *            -- method name to call
	 * @param viewPart
	 * @param monitor
	 * @param parent
	 * @return
	 */
	/*
	 * protected Object doRun(String method_name, IViewPart viewPart,
	 * IProgressMonitor monitor, Composite parent) { try {
	 * //if(getSrcFileName()!=null&&!getSrcFileName().equals("")) //
	 * compileToClass(); //-- for this EMPTY method
	 * 
	 * return eval(method_name, viewPart, monitor, parent); } catch (Exception
	 * e) { System.err.println("[GroovyNode.run()] " + e.getMessage());
	 * setErrorState(); return Status.OK_STATUS; } }
	 */

	protected Composite getMainComposite(Composite t_comp) {
		Composite ret = t_comp;
		if (ret == null)
			return null;

		while (ret.getParent() != null) {
			ret = ret.getParent();
		}
		return ret;
	}

	/**
	 * Executes method "method_name" scripts of this GroovyNode compiling it
	 * into class
	 * 
	 * @param method_name
	 *            -- method name to call
	 * @param viewPart
	 * @param monitor
	 * @param parent
	 * @return
	 */
	protected Object doRun(String method_name, IViewPart viewPart,
			IProgressMonitor monitor, Composite parent) {
		Object ret;

		Display display = null;
		Cursor old_cursor = null;
		Composite m_comp = null;

		if (parent == null)
			display = ConfexPlugin.getDefault().getWorkbench().getDisplay();
		else {
			display = parent.getDisplay();
			m_comp = getMainComposite(parent);
			old_cursor = m_comp.getCursor();
		}
		Cursor wait_cursor = new Cursor(display, SWT.CURSOR_WAIT);

		try {
			// if(getSrcFileName()!=null&&!getSrcFileName().equals(""))
			// compileToClass(); //-- for this EMPTY method
			if (m_comp != null)
				m_comp.setCursor(wait_cursor);
			ret = eval(method_name, viewPart, monitor, parent);
			if (m_comp != null) {
				m_comp.setCursor(old_cursor);
				wait_cursor.dispose();
			}
			return ret;
		} catch (Exception e) {
			System.err.println("[GroovyNode.run()] " + e.getMessage());
			setErrorState();
			if (m_comp != null) {
				m_comp.setCursor(old_cursor);
				wait_cursor.dispose();
			}
			return Status.OK_STATUS;
		}
	}

	protected Object eval(String method_name, IViewPart viewPart,
			IProgressMonitor monitor, Composite parent) {
		try {
			GroovyClassLoader loader = getGroovyClassLoader();

			// if(getSrcFileName()!=null&&!getSrcFileName().equals(""))
			// compileToClass(); //-- for this EMPTY method

			String groovy_text = getFullText(); // getChildrensText(this.getClass());

			File file = getSrcFile();
			String name = "";
			if (file != null)
				name = file.getName();
			Class<GroovyObject> groovyClass = null;
			try {
				groovyClass = loader.parseClass(groovy_text, name);
			} catch (ClassFormatError e) {
				// -- если не получилось компилировать в класс
				// пробуем запустить evaluator

				Binding binding = new Binding();
				binding.setVariable("thisGroovyNode", this);
				binding.setVariable("runViewPart", viewPart);
				binding.setVariable("monitor", monitor);
				binding.setVariable("parent", parent);

				evaluate_groovy(groovy_text, loader, binding, monitor);
				return Status.OK_STATUS;
			}

			GroovyObject groovyObject = (GroovyObject) groovyClass
					.newInstance();

			// -- setup property in groovy script
			try {
				groovyObject.setProperty("parent", parent);
			} catch (MissingPropertyException e) {
				System.out
						.println("WARNING Property \"parent\" in groovy script not defined.");
			}
			try {
				groovyObject.setProperty("thisGroovyNode", this);
			} catch (MissingPropertyException e) {
				System.out
						.println("WARNING Property \"thisGroovyNode\" in groovy script not defined.");
			}
			try {
				groovyObject.setProperty("runViewPart", viewPart);
			} catch (MissingPropertyException e) {
				System.out
						.println("WARNING Property \"runViewPart\" in groovy script not defined.");
			}
			try {
				groovyObject.setProperty("monitor", monitor);
			} catch (MissingPropertyException e) {
				System.out
						.println("WARNING Property \"monitor\" in groovy script not defined.");
			}

			Object[] argz = {};
			Object ret = groovyObject.invokeMethod(method_name, argz);
			if (method_name.equals("run")) {
				if (monitor == null)
					setSuccessState();
				return Status.OK_STATUS;
			} else {
				if (monitor == null)
					setSuccessState();
				return ret;
			}
		} catch (Exception e) {
			System.err.println("[GroovyNode.eval()] " + e.getMessage());
			if (monitor == null)
				setErrorState();
			return Status.OK_STATUS;
		}
	}

	/**
	 * Create GroovyClassLoader with classpath at "classes" and all lib/*.jar
	 * 
	 * @return
	 */
	protected GroovyClassLoader getGroovyClassLoader() {
		ClassLoader parent_cl = GroovyNode.class.getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent_cl);

		// List<String> classpath = new ArrayList<String>();
		/*
		 * javassist.Loader cl = (Loader)GroovyNode.class.getClassLoader();
		 * ReflectLoader loader = new ReflectLoader();
		 * loader.makeReflective("Person", MyRtMetaobj.class,
		 * ClassMetaobject.class); cl.getClassPath().addPath(loader);
		 * cl.run("MyApp", args); parent_cl.
		 */

		// List li = getClasspathPaths("net.confex.groovy");
		/*
		 * try { Dictionary dict =
		 * Activator.getDefault().getBundle().getHeaders(); String classpath =
		 * (String)dict
		 * .get(org.eclipse.osgi.framework.internal.core.Constants.BUNDLE_CLASSPATH
		 * ); System.err.println("BUNDLE_CLASSPATH="+classpath);
		 * 
		 * String location = (String)dict
		 * .get(org.eclipse.osgi.framework.internal
		 * .core.Constants.SYSTEM_BUNDLE_LOCATION);
		 * System.err.println("SYSTEM_BUNDLE_LOCATION="+location);
		 * 
		 * File f =
		 * Activator.getDefault().getBundle().getBundleContext().getDataFile
		 * (""); System.err.println("f.getAbsolutePath()="+f.getAbsolutePath());
		 * 
		 * StringTokenizer tok = new StringTokenizer(classpath, ",");
		 * List<String> list = new ArrayList<String>();
		 * 
		 * while (tok.hasMoreTokens()) { try { String s = tok.nextToken();
		 * //System.err.println(s); System.out.println("Local CLASSPATH add " +
		 * s); loader.addClasspath(s); } catch (Exception x) {} } }
		 * catch(Exception e) {System.err.println(e.getMessage()); }
		 */

		String s = getConfigTree().getConfexDir() + "groovy" + File.separator;
		loader.addClasspath(s);
		s = getConfigTree().getConfexDir() + "classes" + File.separator;
		loader.addClasspath(s);

		/*
		 * Bundle b = Platform.getBundle("net.confex.customxml"); if(b==null)
		 * System.err.println("Bundle net.confex.customxml not found! "); else
		 * System.out.println("Bundle net.confex.customxml found!"); b =
		 * Platform.getBundle("net.confex"); if(b==null)
		 * System.err.println("Bundle net.confex not found! "); else
		 * System.out.println("Bundle net.confex found!");
		 */

		/*
		 * try { Collection<String> col =
		 * BundleUtils.getBundleClassPath("net.confex");
		 * System.out.println("----- "+col.size());
		 * 
		 * Iterator it = col.iterator(); while(it.hasNext()) {
		 * System.err.println("col  "+(String)it.next()); } } catch (Exception
		 * e) { System.err.println(" Bundle Utils "+e.getMessage()); }
		 * 
		 * List li = BundleUtils.getClasspathPaths("net.confex"); Iterator it =
		 * li.iterator(); while(it.hasNext()) {
		 * System.err.println("li  "+(String)it.next()); }
		 */

		File lib_dir = new File(getConfigTree().getConfexDir() + "lib"
				+ File.separator);
		if (lib_dir.exists()) {
			final File[] files = lib_dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				final File file = files[i];
				if (file.getName().endsWith(".jar")) {
					String path = file.getAbsolutePath();
					System.out.println("Local CLASSPATH add jar " + path);
					loader.addClasspath(path);
				}
			}
		}
		return loader;
	}

	protected void compileToClass() {

		File file = getSrcFile();
		if (file != null && file.exists()) {
			Project project = new Project();
			project.addBuildListener(new AnsiColorLogger());

			Groovyc compiler = new Groovyc();
			compiler.setProject(project);
			Path p = compiler.createClasspath();

			File lib_dir = new File(getConfigTree().getConfexDir() + "lib"
					+ File.separator);
			if (lib_dir.exists()) {
				final File[] files = lib_dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					final File jar_file = files[i];
					if (jar_file.getName().endsWith(".jar")) {
						String path = jar_file.getAbsolutePath();
						System.out.println("Compiling Local CLASSPATH add jar "
								+ path);
						p.add(new Path(project, path));
					}
				}
			}

			String src = getConfigTree().getConfexDir() + "groovy"
					+ File.separator;
			compiler.setSrcdir(new Path(project, src));
			compiler.setIncludes(file.getName());
			String dest = getConfigTree().getConfexDir() + "classes"
					+ File.separator;
			compiler.setDestdir(project.resolveFile(dest));
			compiler.setListfiles(true);
			File f_dest = new File(dest);
			if (!f_dest.exists()) {
				f_dest.mkdir();
			}
			p.add(new Path(project, src));
			p.add(new Path(project, dest));
			compiler.execute();
			System.out.println("File " + file.getName() + " was compiled.");
		}
	}

	protected void evaluate_groovy(String groovy_text,
			GroovyClassLoader loader, Binding binding, IProgressMonitor monitor) {
		// GroovyClassLoader loader = getGroovyClassLoader();

		GroovyShell groovy_shell = new GroovyShell(loader, binding);

		// String groovy_text = getChildrensText(this.getClass());
		// String s = TreeUtils.doAllSubstitutions(this, groovy_text);

		if (monitor == null)
			setRunState();

		Object o = groovy_shell.evaluate(groovy_text);

		if (monitor == null)
			setSuccessState();
	}

	public IStatus run(IViewPart view, IProgressMonitor monitor) {
		readFromSrcFile();
		Object ret = doRun("run", view, monitor, null);
		if (ret instanceof IStatus)
			return (IStatus) ret;
		return null;
	}

	public IStatus runInBatch(IViewPart view, IProgressMonitor monitor) {
		// System.out.println("!!! Groovy runAsChild(monitor) executed.");
		run(view, monitor);
		return Status.OK_STATUS;
	}

	/**
	 * I have made this method empty that it was possible to collect
	 * compositions of groovy-scripts.
	 */
	public void runInBatch(IViewPart view) {
		// System.err.println("!!! Groovy runAsChild() Empty!");
		// run(navigation_view);
		return;
	}

	/**
	 * Return composite text combined from all child for the children hasn't
	 * NotRunInBatch property set
	 * 
	 */
	public String getChildrensText(Class _interface) {
		if (isNotRunInBatch())
			return "";
		String ret_str = "";
		if (_interface == GroovyNode.class) {
			readFromSrcFile();
			ret_str += src_text + "\n";
		}
		ret_str += super.getChildrensText(_interface) + "\n";
		return TreeUtils.doAllSubstitutions(this, ret_str);
	}

	/**
	 * @param args
	 */
	public static void main2222(String[] args) {
		// TODO Auto-generated method stub
		// call groovy expressions from Java code
		Binding binding = new Binding();
		binding.setVariable("foo", new Integer(2));
		GroovyShell groovy_shell = new GroovyShell(binding);

		String s = "println 'Hello World!'; x = 123; return foo * 10";
		groovy_shell.evaluate(s);

		s = "ant = new AntBuilder(); "
				+ "ant.mkdir(dir:\"d:/tmp/dev/projects/ighr/binaries/\")";

		groovy_shell.evaluate(s);

		s = "import org.eclipse.swt.SWT; "
				+ "import org.eclipse.swt.widgets.*; "
				+ "import org.eclipse.swt.layout.RowLayout as Layout; "
				+

				"def display = new Display(); "
				+ "def shell = new Shell(display); "
				+

				"shell.layout = new Layout(SWT.VERTICAL); "
				+

				"shell.text = 'Groovy / SWT Test'; "
				+

				"def label = new Label(shell, SWT.NONE); "
				+ "label.text = 'Simple demo of Groovy and SWT'; "
				+ "shell.defaultButton = new Button(shell, SWT.PUSH); "
				+ "shell.defaultButton.text = '  Push Me  '; "
				+

				"shell.pack(); "
				+ "shell.open(); "
				+

				"while (!shell.disposed) { "
				+ "    if (!shell.display.readAndDispatch()) shell.display.sleep(); "
				+ "}";

		groovy_shell.evaluate(s);

	}

	public static void main(String[] args) {
		String basePath = "d:/tmp";
		try {
			ApplicationGuiBuilder guiBuilder = new ApplicationGuiBuilder(
					basePath);

			Display display = new Display();
			Shell shell = new Shell(display);
			shell.setLayout(new FillLayout());
			Button b = new Button(shell, SWT.NONE);
			b.setText("direct build button");

			guiBuilder.setCurrent(shell);

			// GroovyScriptEngine scriptEngine = new
			// GroovyScriptEngine(basePath);
			Binding binding = new Binding();
			binding.setVariable("guiBuilder", guiBuilder);

			binding.setVariable("i2", new Integer(555));

			// scriptEngine.run("Screen1.groovy", binding);
			GroovyShell groovy_shell = new GroovyShell(binding);
			// groovy_shell.evaluate("println i2");
			groovy_shell.evaluate(new File("d:/tmp/Screen1.groovy"));

			shell.pack();
			shell.open();

			while (!shell.isDisposed()) {
				if (!shell.getDisplay().readAndDispatch())
					shell.getDisplay().sleep();
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/*
	 * public IStatus run_evaluate(IViewPart view ,IProgressMonitor monitor,
	 * Composite parent) { if(monitor!=null) { monitor.worked(1);
	 * monitor.subTask("reading src... " + 1); if (monitor.isCanceled()) return
	 * Status.CANCEL_STATUS; } readFromSrcFile();
	 * 
	 * try { if(monitor!=null) { monitor.worked(5);
	 * monitor.subTask("Bind groovy vars... " + 5); if (monitor.isCanceled())
	 * return Status.CANCEL_STATUS; } Binding binding = new Binding();
	 * binding.setVariable("thisGroovyNode", this);
	 * binding.setVariable("runViewPart",view);
	 * binding.setVariable("monitor",monitor);
	 * binding.setVariable("parent",parent);
	 * 
	 * evaluate_groovy(binding, monitor);
	 * 
	 * if(monitor!=null) { monitor.done(); } } catch(Exception e) {
	 * System.err.println("[GroovyNode.run()] "+e.getMessage());
	 * setErrorState(); return Status.OK_STATUS; } return Status.OK_STATUS; }
	 */

	/**/
	/**/

}
