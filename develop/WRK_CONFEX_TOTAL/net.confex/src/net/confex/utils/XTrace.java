/*******************************************************************************
 * Copyright (c) 2006,2007 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Roman Eremeev - initial API and implementation
 *******************************************************************************/
package net.confex.utils;

import java.io.File;


/**
 * Class for writing simple trace information into XML file  
 * 
 * Starting XTrace session
 * 	XTrace.startXTrace()
 * or
 * 	XTrace.startXTrace("/trace_logs/trace.xml")
 *  
 * Stopping XTrace session
 * 	XTrace.stopXTrace()
 * 	
 * 
 * in the begin of traced method you must call 
 *	XTrace.enterMethod(Thread.currentThread().getStackTrace());
 *
 * before exit from traced method you must call 
 *	XTrace.exitMethod(Thread.currentThread().getStackTrace());
 *
 * @author Roman_Eremeev
 */

public class XTrace {

	private static boolean traceFlag = false;
	private static File file = null;
	
	
	public static boolean isTraceFlag() {
		return traceFlag;
	}

	public static void setTraceFlag(boolean traceFlag_) {
		traceFlag = traceFlag_;
	}

	
	/**
	 * Start XTrace session without writing into file
	 */
	public static void startXTrace() {
		file = null;
		setTraceFlag(true);
	}
	
	
	/**
	 * Start XTrace session with writing into file
	 * @param file_name
	 */
	public static void startXTrace(String file_name) {
		file = new File(file_name);
		Utils.writeStringToFile("<XTrace>\n", file);
		setTraceFlag(true);
	}
	
	
	/**
	 * Stop XTrace session
	 */
	public static void stopXTrace() {
		if(isTraceFlag()) {
			if(file!=null) {
				Utils.appendStringToFile("</XTrace>\n", file);
				file = null;
			}
			setTraceFlag(false);
		}
	}

	
	/**
	 * Enter into traced method
	 *  
	 * startXTrace(Thread.currentThread().getStackTrace())
	 * @param e
	 */
	public static void enterMethod(StackTraceElement e[]) {
		if(isTraceFlag()) {
			StackTraceElement te = getCurTraceElement(e);
			if(e.length>=4) {
				enterMethod(te.getClassName(), te.getMethodName(),
					e[3].getClassName(),e[3].getMethodName());
			} else {
				enterMethod(te.getClassName(), te.getMethodName(),"-","-");
			}
		}
	}
	
	
	/**
	 * Exit from traced method
	 * 
	 * @param e
	 */
	public static void exitMethod(StackTraceElement e[]) {
		if(isTraceFlag()) {
			StackTraceElement te = getCurTraceElement(e);
			exitMethod(te.getClassName(),te.getMethodName());
		}
	}
	
	
	public static void enterMethod(String cls, String method
			, String caller_cls, String caller_method) {
		if(isTraceFlag()) {
			//String s = "<" + cls+"."+method + "(…)" +
			//" caller=\""+caller_cls+"."+caller_method+"(…)\">"; 
			String s = "<" + cls+"-"+method + 
			" caller=\""+caller_cls+"-"+caller_method+"\">\n"; 
			if(file!=null) {
				Utils.appendStringToFile(s, file);
			}
			System.out.println(s);
		}
	}

	
	public static void exitMethod(String cls, String method) {
		if(isTraceFlag()) {
			//String s = "</"+cls+"."+method+"(…)>";
			String s = "</"+cls+"-"+method+">\n";
			if(file!=null) {
				Utils.appendStringToFile(s, file);
			}
			System.out.println(s);
		}
	}

	
	/**
	 * Return current executed Stack Trace Element
	 * if can't understand return null
	 * 
	 * Ex: 
	 * 		getCurMethod(Thread.currentThread().getStackTrace());
	 */	
	private static StackTraceElement getCurTraceElement(StackTraceElement e[]) {
		boolean doNext = false;
		for (StackTraceElement s : e) {
			if (doNext) {
				return s;
			}
			doNext = s.getMethodName().equals("getStackTrace");
		}
		return null;
	}

	
	/**
	 * Return current executed method 
	 * if can't understand return ""
	 * 
	 * Ex: 
	 * 		getCurMethod(Thread.currentThread().getStackTrace());
	 */	
	/*private static String getCurMethod(StackTraceElement e[]) {
		boolean doNext = false;
		for (StackTraceElement s : e) {
			System.out.println(s.getMethodName());
			if (doNext) {
				System.out.println(e[3].getMethodName()+"->");
				return s.getClassName()+"."+s.getMethodName();
			}
			doNext = s.getMethodName().equals("getStackTrace");
		}
		return "";
	}*/
	
	
	public static void main(String args[]) {
		startXTrace("d:/tmp/9/log.xml");
		
		enterMethod(Thread.currentThread().getStackTrace());
		
		XTrace t = new XTrace();
		t.test();
		
		exitMethod(Thread.currentThread().getStackTrace());

		stopXTrace();
	}
	
	
	public void test() {
		enterMethod(Thread.currentThread().getStackTrace());
		test2();
		exitMethod(Thread.currentThread().getStackTrace());
	}
	
	
	public void test2() {
		enterMethod(Thread.currentThread().getStackTrace());
		exitMethod(Thread.currentThread().getStackTrace());
	}
	
}
