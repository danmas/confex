package net.confex.groovy.model;

import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.ViewPart;

public class GroovyClassNode extends GroovyNode {

	public String getAboutString() {
		return Translator.getString("ABOUT_GroovyClassNode");
	}

	protected static String default_image_name = "groovy_class_file.gif";

	public String getDefaultImage() {
		return default_image_name;
	}

	public GroovyClassNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree, stateObserver);
	}

	public ITreeNode createNewITreeNode() {
		return new GroovyClassNode(getConfigTree(), null);
	}

	/**
	 * Этот класс не отдает свой текст исходника
	 */
	public String getChildrensText(Class _interface) {
		return "";
	}

	/**
	 * Возвращает текст из класса без подстановок
	 */
	public String getFullText() {
		readFromSrcFile();
		return src_text;
	}

	/**
	 * Закрываем этот метод для класса
	 */
	public String getFullHtmltext() {
		return "";
	}

	public IStatus run(IViewPart view, IProgressMonitor monitor) {
		// readFromSrcFile();
		return doRun();
	}

	protected IStatus doRun() {
		try {
			if (getSrcFileNameXml() != null && !getSrcFileNameXml().equals(""))
				compileToClass(); // -- for this EMPTY method
			else {
				System.err.println("Source File not defined!");
				setErrorState();
				return Status.OK_STATUS;
			}
		} catch (Exception e) {
			System.err.println("[GroovyClassNode.run()] " + e.getMessage());
			setErrorState();
			return Status.OK_STATUS;
		}
		setSuccessState();
		return Status.OK_STATUS;
	}

	/**
	 * Только в фоновом режиме(monitor!=null) выполняется компиляция
	 */
	public IStatus runInBatch(IViewPart view, IProgressMonitor monitor) {
		System.out.println("!!! Groovy Class runAsChild(monitor) executed.");
		if (monitor != null)
			return run(view, monitor);
		return Status.OK_STATUS;
	}

	/**
	 * Groovy Class never executed as child!
	 */
	public void runInBatch(IViewPart view) {
		// System.err.println("!!! Groovy runAsChild() Empty!");
		// run(navigation_view);
		return;
	}

	public void makeComposite(Composite parent, ViewPart viewPart,
			IProgressMonitor monitor) {
		return;
	}

}
