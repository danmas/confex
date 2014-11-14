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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;


public class Utils {

	
	
	/**
	 * Special Entities
	 * 
	 * Преобразование текста в текст HTML
	 * 
	 * URL http://www.htmlhelp.com/reference/html40/entities/special.html
	 * 
	 * @param text
	 * @return
	 */
	public static String toHtmlSpecialEntities(String text) {
		String s = Strings.replace(text,"<", "&lt;");
		s = Strings.replace(s,">", "&gt;");
		s = Strings.replace(s,"\"", "&quot;");
		s = Strings.replace(s,"&","&amp;");
		//s = Strings.replace(s,"\n", "\\n");
		return s;
	}
	
	
	/**
	 * Функция обратная к toHtmlSpecialEntities
	 * @param text
	 * @return
	 */
	public static String fromHtmlSpecialEntities(String text) {
		//String s = Strings.replace(text,"\\n","\n");
		//String s = Strings.replace(text,"&nbsp;&nbsp;&nbsp;&nbsp;","\t");
		String s = Strings.replace(text,"&amp;","&");
		s = Strings.replace(s,"&lt;","<");
		s = Strings.replace(s,"&gt;",">");
		s = Strings.replace(s,"&quot;","\"");
		return s;
	}
	
	//private static String[] keyWords = new String[] {"package,public,class,protected,return,super"};
	//private static String[] keyWordsBefore = new String[] {"<b><font color=\"FF0000\">"};
	//private static String[] keyWordsAfter = new String[] {"</font></b>"};
	
	
	/*
	protected static void initJavaKeyWords() {
		keyWords = new String[1];
		keyWords[0] = "package,public,class,protected,return,super";
		keyWordsBefore = new String[1];
		keyWordsBefore[0] = "<b><font color=\"FF0000\">";
		keyWordsAfter = new String[1];
		keyWordsAfter[0] = "</font></b>";
	}*/
	

	public static String java2html(String text) {
		//FIXME: убрать запуск этой статической функции
		//initJavaKeyWords();
		
		String s = Strings.replace(text,"<", "&lt;");
		s = Strings.replace(s,">", "&gt;");
		s = Strings.replace(s,"\"", "&quot;");
		//s = Strings.replace(s,"\n", "<BR>\n");
		//s = Strings.replace(s,"\t", "\t&nbsp;&nbsp;&nbsp;&nbsp;");
		
		/*
		for(int i=0;i<keyWords.length;i++) {
			String[] tok = tokenize(keyWords[i], ",");
	        for (int j = 0; j < tok.length; j++) {
	            s = Strings.replace(s,tok[j], keyWordsBefore[i]+tok[j]+keyWordsAfter[i]);
	        }
		}*/
		return s;
	}
	
	
	
	public static String xml2html(String text) {
		String s = Strings.replace(text,"<", "&lt;");
		s = Strings.replace(s,">", "&gt;");
		s = Strings.replace(s,"\"", "&quot;");
		//s = Strings.replace(s,"\n", "<BR>\n");
		//s = Strings.replace(s,"\t", "\t&nbsp;&nbsp;&nbsp;&nbsp;");
		
		String key_words_attrib = "name,icon";

		String[] tok = tokenize(key_words_attrib, ",");
        for (int i = 0; i < tok.length; i++) {
            s = Strings.replace(s,tok[i], "<b><font color=\"FF0000\">"+tok[i]+"</font></b>");
        }
        
		String key_words_classes = "ConfigTree,UrlNode,TreeNode,";

        tok = tokenize(key_words_classes, ",");
        for (int i = 0; i < tok.length; i++) {
            s = Strings.replace(s,tok[i], "<b><font color=\"0000FF\">"+tok[i]+"</font></b>");
        }

		String key_words_properties = "properties";

        tok = tokenize(key_words_properties, ",");
        for (int i = 0; i < tok.length; i++) {
            s = Strings.replace(s,tok[i], "<b><font color=\"00AAFF\">"+tok[i]+"</font></b>");
        }

		String key_words_internal = "tooltiptext,url,sql";

        tok = tokenize(key_words_internal, ",");
        for (int i = 0; i < tok.length; i++) {
            s = Strings.replace(s,tok[i], "<b><font color=\"00AAAA\">"+tok[i]+"</font></b>");
        }

        
		return s;
	}
	
	
	//-- проверка существования файла 
    // R:   true - файл существует  false - не найден
    public static boolean isFileExist(String fullpathname)  {
        File file = new File(fullpathname);
        if(file.exists())
            return true;
        else 
            return false;
    }


    
    /**
	 *  Deletes all files and subdirectories under dir.
     *  Returns true if all deletions were successful.
     *  If a deletion fails, the method stops attempting to delete and returns false.
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }
    
    
    /**
     *  Copies all files under srcDir to dstDir.
     *  If dstDir does not exist, it will be created.
     */
    public static void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
    
            String[] children = srcDir.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(srcDir, children[i]),
                                     new File(dstDir, children[i]));
            }
        } else {
            // This method is implemented in e1071 Copying a File
            copyFile(srcDir, dstDir);
        }
    }
    
    
    /**
     * Copies src file to dst file.
     * If the dst file does not exist, it is created
     */
    public static void copyFile(File src, File dst) throws IOException {
    	if(!dst.exists()) {
    		boolean success = dst.createNewFile();
            if (success) {
                // File did not exist and was created
            } else {
                // File already exists
            }
    	}
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // читает текст из файла 
    //  Возвращает null если ошибка
    public static String readTextFromFile(String s_full_filename) {
        FileReader rdr=null;
        BufferedReader br=null;
        String s_text="";
        if(!isFileExist(s_full_filename))
            return null;
        try {
            rdr=new FileReader(s_full_filename);
            br=new BufferedReader(rdr);
            String s="";
            while((s=br.readLine())!=null)   {
                s_text+=s +"\n";
             }
        } catch(Exception e)  {
            s_text=null;
        } finally {
            try { rdr.close(); br.close(); } catch(Exception e) { 
                System.err.println(e.getMessage()); 
            }
        }
        
        try {
        	s_text = new String(s_text.getBytes(),"UTF-8");
        } catch (Exception e) {
			System.err.println("Error in readTextFromFile() " + e.getMessage());
		}

        return s_text;
    }


	/**
	 * Записывает строку в файл. Файл всегда перезаписывается. 
	 * 
	 * @param str
	 * @param file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void reWriteStringToFileEx(String str, File file ) throws FileNotFoundException, IOException{
		//File file = new File(fullpathname);
		OutputStream out = null;; 
		if (file.exists())
			file.delete();
		try {
			out = new FileOutputStream(file);
			out = new BufferedOutputStream(out);
			out.write(str.getBytes());
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
		}
	}
	

	/**
	 * Записывает строку в файл. Файл всегда перезаписывается. 
	 * Производится перекодировка
	 * 
	 * @param str
	 * @param file
	 * @param charset - Cp1251, UTF-16, UTF-8
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static void reWriteStringToFileEx(String str, File file, String charset ) 
		throws FileNotFoundException, IOException, UnsupportedEncodingException {
		//File file = new File(fullpathname);
		OutputStream out = null;; 
		if (file.exists())
			file.delete();
		try {
			out = new FileOutputStream(file);
			out = new BufferedOutputStream(out);
			byte[] Bytes;
	        Bytes=str.getBytes(charset);
			out.write(Bytes);
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * Write string into the file. File will be overwriten. 
	 * 
	 * @param str
	 * @param file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean writeStringToFile(String str, File file )  {
		return stringToFile(str, file, false);
	}
	
	/**
	 * Append string to the end of file. 
	 * 
	 * @param str
	 * @param file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean appendStringToFile(String str, File file )  {
		return stringToFile(str, file, true);
	}

	
	/**
	 * Write string into the file. File will be overwriten if  
	 * append_flg not true.
	 * 
	 * @param str
	 * @param file
	 * @param append_flg - - if true, then bytes will be written to the end of 
	 * 			the file rather than the beginning
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected static boolean stringToFile(String str, File file, boolean append_flg)  {
		//File file = new File(fullpathname);
		OutputStream out = null;; 
		if (!append_flg && file.exists())
			file.delete();
		try {
			out = new FileOutputStream(file,append_flg);
			out = new BufferedOutputStream(out);
			
			str = new String(str.getBytes(),"UTF-8");
			
			out.write(str.getBytes());
		}catch(FileNotFoundException e) {
			System.err.println("Файл "+file.toString()+" не найден ");
			return false;
		}catch(IOException ex) {
			System.err.println(ex.getMessage());
			return false;
		} finally {
			if(out != null) {
				try {
					out.flush();
					out.close();
				} catch(Exception e) {}
			}
		}
		return true;
	}
	
	
    /**
     *  Превращает строку в массив строк
     * @param s - входная строка с резделителями
     * @param delimiter -  разделитель
     * @return  массив строк
     * 
     *  Example:
     *  String[] tok = tokenize(to_addr, ",");
     *  for (int i = 0; i < tok.length; i++) {
     *             print(tok[i]);
     *  }
     */
    public static String[] tokenize(String s, String delimiter) {
		Assert.isLegal(s != null);
		StringTokenizer tokenizer = new StringTokenizer(s, delimiter);
		String[] tok = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreElements()) {
			tok[i++] = tokenizer.nextToken();
		}
		return tok;
	}
	
    
	/**
	 * Получить иконку для файла по его расширению
	 */
	public static Icon getIconByExtension(String ext) {
		File file = null;
		Icon icon = null;
		try {
			//Create a temporary file with the specified extension
			file = File.createTempFile("icon", "." + ext);

			FileSystemView view = FileSystemView.getFileSystemView();
			icon = view.getSystemIcon(file);

			//Delete the temporary file
			file.delete();
		} catch (IOException ioe) {
			return null;
		}
		return icon;
	}
    
	
	/**
	 * Возвращает расширение файла
	 * Возвращает null если расширение файла не найдено 
	 * @param file_name
	 * @return
	 */
	public static String getFileNameExtension(String file_name) {
		if(file_name==null)
			return null;
		
		String ret = null;
		
		int pos = file_name.lastIndexOf('.', file_name.length());
		if(pos!=-1) {
			ret = file_name.substring(pos+1);
		}
		return ret;
	}
	
	
	/**
	 * Copies src file to dst file.
	 * If the dst file does not exist, it is created
	 * 
	 * @throws IOException
	 */
	public static void copyFileEx(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    
    /*
	public void openExternalFile2(IWorkbenchPage page) {
		   IFile file = null; //...;
		   IEditorDescriptor desc = PlatformUI.getWorkbench().
		      getEditorRegistry().getDefaultEditor(file.getName());
		   page.openEditor(
		      new FileEditorInput(file),
		      desc.getId());
		}
	*/
	
	
	
	public static void openSrcFile(IWorkbenchPage page, File file)
			throws Exception {
		if (file == null || !file.exists())
			return;
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IProject project = ws.getRoot().getProject("Confex Project");
		if (!project.exists())
			project.create(null);
		if (!project.isOpen())
			project.open(null);
		IPath location = new Path(file.getAbsolutePath());
		IFile i_file = project.getFile(location.lastSegment());

		// if ((updateFlags & REPLACE) != 0) {
		IResource existing = ws.getRoot().findMember(i_file.getFullPath());
		if (existing == null)
			i_file.createLink(location, IResource.NONE, null);
		else
			i_file.createLink(location, IResource.REPLACE, null);
		// workspace.deleteResource(existing);
		// }

		// IWorkbenchPage page = window.getActivePage();
		if (page != null) {
			IEditorDescriptor desc = PlatformUI.getWorkbench()
					.getEditorRegistry().getDefaultEditor(i_file.getName());
			if (desc == null) {
				throw new Exception("Not found default editor for file!");
			}
			IEditorPart ep = page.openEditor(new FileEditorInput(i_file), desc
					.getId());

		}
		// IEditorPart ep = page.openEditor(IEditorInput input, String editorId)
	}
	
	
	/*
	public static void openExternalFile(IWorkbenchPage page, File file)
			throws Exception {
		if (file == null || !file.exists())
			return;
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IProject project = ws.getRoot().getProject("External Files");
		if (!project.exists())
			project.create(null);
		if (!project.isOpen())
			project.open(null);
		IPath location = new Path(file.getAbsolutePath());
		IFile i_file = project.getFile(location.lastSegment());

		// if ((updateFlags & REPLACE) != 0) {
		IResource existing = ws.getRoot().findMember(i_file.getFullPath());
		if (existing == null)
			i_file.createLink(location, IResource.NONE, null);
		else
			i_file.createLink(location, IResource.REPLACE, null);
		// workspace.deleteResource(existing);
		// }

		// IWorkbenchPage page = window.getActivePage();
		if (page != null) {
			IEditorDescriptor desc = PlatformUI.getWorkbench()
					.getEditorRegistry().getDefaultEditor(i_file.getName());
			if (desc == null) {
				throw new Exception("Not found default editor for file!");
			}
			IEditorPart ep = page.openEditor(new FileEditorInput(i_file), desc
					.getId());

		}
		// IEditorPart ep = page.openEditor(IEditorInput input, String editorId)
	}*/
	
	
	public static void main(String[] arg) {
		
		String s = "";
		/*
		 * s = readTextFromFile("c:\\config\\confex.confex"); //s =
		 * toHtmlSpecialEntities(s); s = xml2html(s); //s =
		 * fromHtmlSpecialEntities(s); File file_out = new
		 * File("c:\\config\\confex.html"); writeStringToFile(s,file_out);
		 */
		s = getFileNameExtension("c:\\config\\confex.confex");
		
		System.out.println(s);
	}
	

	
}
