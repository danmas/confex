/*******************************************************************************
 * Copyright (c) 2006 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package net.confex.application;

import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class ConfexConsole extends MessageConsole {

	private MessageConsoleStream outMessageStream;

	private MessageConsoleStream errMessageStream;

	// private MessageConsoleStream inMessageStream;

	private static final ConfexConsole debugConsole = new ConfexConsole();

	public static ConfexConsole getInstanse() {
		return debugConsole;
	}

	// private PacketListener outListener = new PacketListener() {
	// public void processPacket(Packet arg0) {
	// outMessageStream.println(arg0.toXML());
	// }
	// };

	// private PacketListener inListener = new PacketListener() {
	// public void processPacket(Packet arg0) {
	// inMessageStream.println(arg0.toXML());
	// }
	// };

	private ConfexConsole() {
		super("Confex console", null);

		this.outMessageStream = newMessageStream();
		this.outMessageStream.setColor(Display.getCurrent().getSystemColor(
				SWT.COLOR_BLACK));

		this.errMessageStream = newMessageStream();
		this.errMessageStream.setColor(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		// this.inMessageStream = newMessageStream();
		// this.inMessageStream.setColor(Display.getCurrent().getSystemColor(
		// SWT.COLOR_BLUE));

		// Session.getInstance().getConnection().addPacketWriterListener(outListener,
		// null);
		// Session.getInstance().getConnection().addPacketListener(inListener,
		// null);

		// -- переключаем поток ошибок на нашу консоль!
		System.out.println("Switch console output at Confex Console.");
		PrintStream errPrintStream = new PrintStream(errMessageStream);
		System.setErr(errPrintStream);

		// -- переключаем поток вывода на нашу консоль!
		PrintStream outPrintStream = new PrintStream(outMessageStream);
		System.setOut(outPrintStream);
		
		// -- переключаем поток ввода на нашу консоль!
		// InputStream inInputStream = new InputStream(inMessageStream);
		// System.setIn(inInputStream);

	}

	protected void dispose() {
		// Session.getInstance().getConnection().removePacketWriterListener(outListener);
		// Session.getInstance().getConnection().removePacketListener(inListener);
	}

	/*
	 * public static MessageConsoleStream getOutStream() { return
	 * getInstanse().outMessageStream; } public static MessageConsoleStream
	 * getErrStream() { return getInstanse().errMessageStream; }
	 */
}
