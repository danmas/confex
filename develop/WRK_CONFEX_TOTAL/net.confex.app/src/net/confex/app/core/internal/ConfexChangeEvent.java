package net.confex.app.core.internal;

import net.confex.app.core.IConfexChangeEvent;
import net.confex.app.core.IConfexDelta;

public class ConfexChangeEvent implements IConfexChangeEvent {

	IConfexDelta delta;

	public ConfexChangeEvent(IConfexDelta deltaToNotify) {
		delta = deltaToNotify;
	}

	@Override
	public IConfexDelta getDelta() {
		return delta;
	}

}
