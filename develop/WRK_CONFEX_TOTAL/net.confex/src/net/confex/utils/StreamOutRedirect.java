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
package net.confex.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;


class StreamOutRedirect extends Thread {
    transient InputStream   input_stream;
    transient String        type;
    transient OutputStream  output_stream;
    transient String        out_string="";
    
    public static final String ERR_STREAM = "ERROR"; 
    public static final String OUT_STREAM = "OUT"; 
    
    //-- кодировка вывода в консоль
    public String charset = "cp866"; //"Cp1251");
    
    
    StreamOutRedirect(InputStream is, String type, String charset) {
        this(is, type, null,charset);
    }


    StreamOutRedirect(InputStream input_stream, String type, OutputStream output_stream, String charset)   {
        this.input_stream = input_stream;
        this.type = type;
        this.output_stream = output_stream;
        this.charset = charset;
    }
    
    
    //public void setOutTextString(String s) { 
   //     m_out_string=s; 
   // }
   
    
    public final String getOutTextString() { 
        return out_string;
    }
    
    private boolean running; 
    
    public void run() {
        try   {
        	running = true;
            PrintWriter pw = null;
            if (output_stream != null)
                pw = new PrintWriter(output_stream);
            InputStreamReader isr = new InputStreamReader(input_stream, charset); 
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(line);
                if(out_string!=null) {
                    out_string+= type + ">" + line + "\n";
                }
                if(type.equals(ERR_STREAM))
                	System.err.println(type + ">" + line);
                else {
                	System.out.println(type + ">" + line);
                }
                //sleep(10);
            }
            running = false;
            if (pw != null)
                pw.flush();
        } catch (Exception ioe)  {
        	running = false;
            ioe.printStackTrace();  
        }
    }


	public boolean isRunning() {
		return running;
	}
    
    
}

