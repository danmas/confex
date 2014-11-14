package net.confex.app.core;

public interface IConfexDelta {

	public static final int NO_CHANGE = 0x0;

	public static final int ADDED = 0x1;

	public static final int REMOVED = 0x2;

	public static final int CHANGED = 0x4;

	public static final int CREATED = 0x8;

	int getKind();

	IElement getElement() throws ConfexException;

}
