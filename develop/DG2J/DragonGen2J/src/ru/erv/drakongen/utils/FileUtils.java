package ru.erv.drakongen.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class FileUtils {
	
	public static void fileWrite(String name, String text) {
		Writer w = null;
	    try {
	        File file = new File(name);
	        FileOutputStream is = new FileOutputStream(file);
	        OutputStreamWriter osw = new OutputStreamWriter(is);    
	        w = new BufferedWriter(osw);
	        w.write(text);
	        w.close();
	    } catch (IOException e) {
	        System.err.println("Problem writing to the file "+name +"\n" + e.getMessage());
	    } finally {
	    	if(w != null) {
	    		try { w.close(); } catch(Exception e) {} 
	    		w = null;
	    	}
	    }
	}

	
	public static String fileRead(String name) {
		BufferedReader br = null;
	    try {
	        File file = new File(name);
	        FileInputStream is = new FileInputStream(file);
	        
	        StringBuilder out = new StringBuilder();
	        br = new BufferedReader(new InputStreamReader(is));
	        for(String line = br.readLine(); line != null; line = br.readLine()) 
	          out.append(line);
	        br.close();
	        return out.toString();
	        
	    } catch (IOException e) {
	        System.err.println("Problem reading the file "+name +"\n" + e.getMessage());
	        return "";
	    } finally {
	    	if(br != null) {
	    		try { br.close(); } catch(Exception e) {} 
	    		br = null;
	    	}
	    }
	}


}
