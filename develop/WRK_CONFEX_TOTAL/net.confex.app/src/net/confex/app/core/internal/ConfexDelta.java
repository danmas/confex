package net.confex.app.core.internal;

import net.confex.app.Confex;
import net.confex.app.core.ConfexException;
import net.confex.app.core.IConfexDelta;
import net.confex.app.core.IElement;

import org.eclipse.core.resources.IResource;

public class ConfexDelta implements IConfexDelta {

	private IResource resource;
	private int kind;

	public ConfexDelta(IResource resource, int kind) {
		this.resource = resource;
		this.kind = kind;
	}

	@Override
	public int getKind() {
		return kind;
	}

	@Override
	public IElement getElement() throws ConfexException {
		return Confex.create(resource);
	}

}
