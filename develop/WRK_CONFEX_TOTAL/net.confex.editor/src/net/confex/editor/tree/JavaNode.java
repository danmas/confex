package net.confex.editor.tree;

import java.io.File;
import java.lang.reflect.Method;

import net.confex.directedit.IPropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.ICompositeProvider;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.tree.TreeNode;
import net.confex.utils.FileClassLoader;
import net.confex.utils.MyURLClassLoader;
import net.confex.utils.TreeUtils;
import net.confex.utils.Utils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.Node;

public class JavaNode extends TreeNode implements ICompositeProvider {

	public JavaNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree, stateObserver);

	}

	public String getAboutString() {
		return Translator.getString("ABOUT_JavaNode");
	}

	protected static String default_image_name = "eclipse icons/java_file.gif";

	public String getDefaultImage() {
		return default_image_name;
	}

	public static String getDefaultImageName() {
		return default_image_name;
	}
	
	
	protected String getPropertiesXml(boolean read_src_text) {
		String str_xml = super.getPropertiesXml(read_src_text);
		if (src_text != null && !src_text.equals("")) {
			str_xml += "<java>\n" + Utils.toHtmlSpecialEntities(src_text)
					+ "\n";
			str_xml += "</java>\n";
		}
		return str_xml;
	}

	protected void parsePropertyXml(Node property, boolean new_node) {
		super.parsePropertyXml(property, new_node);

		if (property.getNodeName().equals("java")) {
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
		return new JavaNode(getConfigTree(), null);
	}

	public void setPropertyLike(ITreeNode prototype) {
		super.setPropertyLike(prototype);
		if (!(prototype instanceof JavaNode)) {
			System.err
					.println("[JavaNode.setPropertyLike] prototype NOT instanceof JavaNode!");
			return;
		}
	}

	protected IPropertyDialog newPropertyDialog(Shell shell) {
		readFromSrcFile();
		return new JavaPropertyDialog(shell, this);
	}

	public String getFullText() {
		String java_text = getChildrensText(this.getClass());
		return TreeUtils.doAllSubstitutions(this, java_text);
	}

	// DFIXME: dispose m_parent!!!
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

	/**
	 * Вход через метод run()
	 * 
	 * @param parent
	 * @param viewPart
	 * @param monitor
	 */
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
			 * GroovyShell groovy_shell = new GroovyShell(binding);
			 * //groovy_shell.evaluate(new File("d:/tmp/Screen1.groovy"));
			 * 
			 * String groovy_text = getChildrensText(this.getClass()); String s =
			 * TreeUtils.doAllSubstitutions(this, groovy_text);
			 * 
			 * //System.out.println(s);
			 * 
			 * if(monitor==null) setRunState(); Object o =
			 * groovy_shell.evaluate(s); if(monitor==null) setSuccessState();
			 */
		} catch (Exception e) {
			System.err.println("[JavaNode.makeComposite()] " + e.getMessage());
			setErrorState();
		}
	}

	/**
	 * Просто выполняется файл Вход через метод main()
	 * 
	 * @param view
	 * @param monitor
	 * @return
	 */
	public IStatus run(IViewPart view, IProgressMonitor monitor) {
		if (monitor != null) {
			monitor.worked(1);
			monitor.subTask("reading src... " + 1);
			if (monitor.isCanceled())
				return Status.CANCEL_STATUS;
		}
		readFromSrcFile();

		try {
			if (monitor != null) {
				monitor.worked(5);
				monitor.subTask("Bind java vars... " + 5);
				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;
			}
			/*
			 * Binding binding = new Binding();
			 * binding.setVariable("thisGroovyNode", this);
			 * binding.setVariable("runViewPart",navigation_view);
			 * binding.setVariable("monitor",monitor);
			 * 
			 * if(monitor!=null) { monitor.worked(10); monitor.subTask("Shell
			 * groovy... " + 10); if (monitor.isCanceled()) return
			 * Status.CANCEL_STATUS; } GroovyShell groovy_shell = new
			 * GroovyShell(binding);
			 * 
			 * String groovy_text = getChildrensText(this.getClass()); String s =
			 * TreeUtils.doAllSubstitutions(this, groovy_text);
			 */
			if (monitor == null)
				setRunState();

			if (monitor != null) {
				monitor.worked(15);
				monitor.subTask("Evaluate java... " + 15);
				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;
			}

			doMain();
			// doRun();

			if (monitor == null)
				setSuccessState();

			if (monitor != null) {
				monitor.done();
			}
		} catch (Exception e) {
			System.err.println("[JavaNode.run()] " + e.getMessage());
			setErrorState();
			return Status.OK_STATUS;
		}
		return Status.OK_STATUS;
	}

	public String getChildrensText(Class _interface) {
		String ret_str = "";
		if (_interface == this.getClass()) {
			readFromSrcFile();
			ret_str += src_text;
		}
		return ret_str += super.getChildrensText(_interface);
	}

	
	/* (non-Javadoc)
	 * Changes getSrcFileNameGui to get file_name without /src/
	 * @see net.confex.tree.TreeNode#getSrcFileNameGui()
	 */
	public String getSrcFileNameGui() {
		String nameInGui = super.getSrcFileNameGui();
		if (nameInGui.startsWith(File.pathSeparator + "src" + File.separator)){
			nameInGui = nameInGui.substring(4);
		}
		return nameInGui;
	}
	
	/* (non-Javadoc)
	 * Changes setSrcFileNameGui to keep simple file_name with /src/
	 * @see net.confex.tree.TreeNode#setSrcFileNameGui(java.lang.String)
	 */
	public void setSrcFileNameGui(String file_name) {
		String nameInXml = file_name;
		if (!nameInXml.contains(File.pathSeparator)){
			nameInXml = File.pathSeparator + "src" + File.separator + nameInXml; 
		}
		super.setSrcFileNameGui(nameInXml);
	}
	
	String filename;
	String classname;
	String path_at_src;
	String path_at_class;
	File java_file;

	protected boolean setup_internal_vars() {
		java_file = getSrcFile();
		if (java_file == null || !java_file.exists()) {
			System.err.println("Src file not exist");
			return false;
		}

		filename = java_file.getName();
		classname = filename.substring(0, filename.length() - 5);
		String s = java_file.getAbsolutePath();
		int l = (int) (java_file.getAbsolutePath().length() - filename.length());
		path_at_src = java_file.getAbsolutePath().substring(0, l); // "C:\\Confex\\java\\bin\\";
																	// // ;
		path_at_class = path_at_src + ".." + File.separator + "classes"
				+ File.separator;
		return true;
	}

	/**
	 * Переопределяем метод saveSrcFile() так чтобы при каждом сохранении класс
	 * перекомпилировался
	 * 
	 */
	/*
	 * public void saveSrcFile() { super.saveSrcFile();
	 * 
	 * if(!setup_internal_vars()) return;
	 * 
	 * //-- удаляем class файл File fclass = new File(path_at_class + classname +
	 * ".class"); if(fclass.exists()) { fclass.delete(); } //-- компилируем
	 * compile(); }
	 */

	/**
	 * Проверка нужно ли компилировать если да то компилируем
	 * 
	 * Если есть откомпилированный и его время изменения меньше чем время
	 * модификации исходного то перекомпилируем
	 * 
	 * @return true - if Ok
	 */
	protected boolean compaleIfNeed() {
		if (!setup_internal_vars())
			return false;
		// -- проверяем есть ли откомпиллированный класс
		File fclass = new File(path_at_class + classname + ".class");
		if (fclass.exists()) {
			if (java_file.lastModified() > fclass.lastModified())
				return compile();
			return true;
		}
		// -- компилируем
		return compile();
		//return true;
	}

	/**
	 * W! Может вызываться только после setup_internal_vars()!
	 * 
	 * @return
	 */
	protected boolean compile() {

		String lp = "";

		File lib_dir = new File(getConfigTree().getConfexDir() + "lib"
				+ File.separator);
		if (lib_dir.exists()) {
			final File[] files = lib_dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				final File file = files[i];
				if (file.getName().endsWith(".jar")) {
					String path = file.getAbsolutePath();
					System.out.println("Local CLASSPATH add jar " + path);
					lp += path + File.pathSeparatorChar;
				}
			}
		}

		String s = getConfigTree().getConfexDir() + "classes" + File.separator;
		lp += s;
		
		// Compile
		String[] args = new String[] { "-d", path_at_class, "-classpath", lp,
				java_file.getAbsolutePath() };

		System.out.println("before compile " + args[1] + "  " + args[2]);
		com.sun.tools.javac.Main m_javac = new com.sun.tools.javac.Main();
		int status = m_javac.compile(args);
		System.out.println("after compile");
		// Run
		switch (status) {
		case 0: // OK
			return true;
		case 1:
			System.err.println("Compile status: ERROR");
			return false;
		case 2:
			System.err.println("Compile status: CMDERR");
			return false;
		case 3:
			System.err.println("Compile status: SYSERR");
			return false;
		case 4:
			System.err.println("Compile status: ABNORMAL");
			return false;
		default:
			System.err.println("Compile status: Unknown exit status");
			return false;
		}
	}

	/**
	 * Запускает main метод класса
	 * 
	 * W! Может вызываться только после setup_internal_vars()!
	 */
	protected void doMain() { // throws IOException {
		/**/
		if (!compaleIfNeed()) {
			return;
		}
		Class testClass = null;
		try {
			// FileClassLoader loader = new FileClassLoader(path_at_class);
			MyURLClassLoader loader = new MyURLClassLoader(path_at_class);

			String s = getConfigTree().getConfexDir() + "classes"
					+ File.separator;
			loader.addClasspath(s);

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

			// A series of tests:
			testClass = loader.loadClass(classname);
		} catch (Exception ex) {
			System.out.println("Load failed");
			ex.printStackTrace();
			return;
		}

		// Object[] argz = {};
		// Object ret = testClass.getMethod(name, parameterTypes)
		// .invokeMethod("main", argz);

		System.out.println("Loaded class " + testClass.getName());
		try {
			Method main = testClass.getMethod("main",
					new Class[] { String[].class });
			main.invoke(null, new Object[] { new String[0] });
		} catch (NoSuchMethodException e) {
			System.err.println("Method main not found in class " + classname
					+ " !");
			return;
		} catch (Exception ex) {
			System.err.println("[JavaNode.doMain()] ");
			ex.printStackTrace();
		}
		/**/
	}

	/**
	 * runs the класс.
	 * 
	 * W! Может вызываться только после setup_internal_vars()!
	 */
	protected void doRun() { // throws IOException {
		if (!compaleIfNeed()) {
			return;
		}
		FileClassLoader loader = new FileClassLoader(path_at_class);
		Class testClass = null;
		try {
			testClass = loader.loadClass(classname);
		} catch (Exception ex) {
			System.out.println("Load failed");
			ex.printStackTrace();
			return;
		}
		System.out.println("Loaded class " + testClass.getName());
		try {
			Runnable instance = (Runnable) testClass.newInstance();
			instance.run();
		} catch (Exception ex) {
			System.err.println("Failed to instantiate");
			ex.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main2222(String[] args) {
		// TODO Auto-generated method stub
		// call groovy expressions from Java code
		/*
		 * Binding binding = new Binding(); binding.setVariable("foo", new
		 * Integer(2)); GroovyShell groovy_shell = new GroovyShell(binding);
		 * 
		 * String s = "println 'Hello World!'; x = 123; return foo * 10";
		 * groovy_shell.evaluate(s);
		 * 
		 * s = "ant = new AntBuilder(); "+
		 * "ant.mkdir(dir:\"d:/tmp/dev/projects/ighr/binaries/\")";
		 * 
		 * groovy_shell.evaluate(s);
		 * 
		 * 
		 * s = "import org.eclipse.swt.SWT; "+ "import
		 * org.eclipse.swt.widgets.*; "+ "import
		 * org.eclipse.swt.layout.RowLayout as Layout; "+
		 * 
		 * "def display = new Display(); "+ "def shell = new Shell(display); "+
		 * 
		 * "shell.layout = new Layout(SWT.VERTICAL); "+
		 * 
		 * "shell.text = 'Groovy / SWT Test'; "+
		 * 
		 * "def label = new Label(shell, SWT.NONE); "+ "label.text = 'Simple
		 * demo of Groovy and SWT'; "+ "shell.defaultButton = new Button(shell,
		 * SWT.PUSH); "+ "shell.defaultButton.text = ' Push Me '; "+
		 * 
		 * "shell.pack(); "+ "shell.open(); "+
		 * 
		 * "while (!shell.disposed) { "+ " if (!shell.display.readAndDispatch())
		 * shell.display.sleep(); "+ "}";
		 * 
		 * groovy_shell.evaluate(s);
		 */
	}

	/*
	 * public static void main(String[] args) { String basePath = "d:/tmp"; try {
	 * ApplicationGuiBuilder guiBuilder = new ApplicationGuiBuilder(basePath);
	 * 
	 * Display display = new Display(); Shell shell = new Shell(display);
	 * shell.setLayout(new FillLayout()); Button b = new Button(shell,SWT.NONE);
	 * b.setText("direct build button");
	 * 
	 * guiBuilder.setCurrent(shell);
	 * 
	 * //GroovyScriptEngine scriptEngine = new GroovyScriptEngine(basePath);
	 * Binding binding = new Binding(); binding.setVariable("guiBuilder",
	 * guiBuilder);
	 * 
	 * binding.setVariable("i2", new Integer(555));
	 * 
	 * //scriptEngine.run("Screen1.groovy", binding); GroovyShell groovy_shell =
	 * new GroovyShell(binding); //groovy_shell.evaluate("println i2");
	 * groovy_shell.evaluate(new File("d:/tmp/Screen1.groovy"));
	 * 
	 * shell.pack(); shell.open();
	 * 
	 * while (!shell.isDisposed()) { if (!shell.getDisplay().readAndDispatch())
	 * shell.getDisplay().sleep(); } } catch(Exception e) {
	 * System.err.println(e.getMessage()); } }
	 */

	/**
	 * Ищем вверх по всем наборам пременных пока не найдем переменную с именем
	 * var_name ей присваиваем значение value
	 */
	// public void setVarUp(String var_name, String value) {
	// TreeUtils.setVarUp(this,var_name, value);
	// }
}
