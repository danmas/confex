package net.confex.app.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

public class ConfexException extends CoreException {

	public ConfexException(IStatus status) {
		super(status);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1115203890905094991L;

}
