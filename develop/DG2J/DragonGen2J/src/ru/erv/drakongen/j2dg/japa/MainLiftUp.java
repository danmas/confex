package ru.erv.drakongen.j2dg.japa;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;

import ru.erv.drakongen.utils.FileUtils;

public class MainLiftUp {

    private static final String JAVA_FILE = "../../../WRK/DG2J/DragonGen2J/src/ru/erv/drakongen/test/Test2.java";
    //private static final String JAVA_FILE = "../../../WRK/DG2J/DragonGen2J/src/ru/erv/drakongen/Drakongen2.java";
    //private static final String GML_FILE = "../../../WRK/DG2J/Japa/test/Test2_gen.graphml";
    private static final String GML_FILE = "../../../WRK/DG2J/DragonGen2J/src/ru/erv/drakongen/test/Test2_gen.graphml";
    
    /**
     * 
     * @
     * @param args
     */
    public static void main(String[] args) {
    	
    	File file = new File(JAVA_FILE);
    	
    	try {
    		CompilationUnit cu = JavaParser.parse(file);
    	
	    	System.out.println(cu.toString()); //-- здесь работает dump Visitor.
	    	
	    	/**/
	    	String res = cu.toGraphml(); 
	    	System.out.println(res); 
	    	
	    	FileUtils.fileWrite(GML_FILE, res);
	    	System.out.println("--> Записан "+ GML_FILE);
	    	
	    	System.out.println(res);
    	} catch(Exception e) {
    		System.err.println(e.getMessage());
    	}
	}

}
