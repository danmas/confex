package net.confex.editor.tree;
//package org.eclipse.ui.views.bookmarkexplorer;


/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Roman Eremeev
 *******************************************************************************/


import java.text.DateFormat;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.internal.NavigationHistory;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Utility class for accessing marker attributes.
 */
public class MarkerUtil {
	
	/**
	 * Don't allow instantiation.
	 */
	private MarkerUtil() {
	}
	/**
	 * Returns the ending character offset of the given marker.
	 */
	static int getCharEnd(IMarker marker) {
		return marker.getAttribute(IMarker.CHAR_END, -1);
	}
	/**
	 * Returns the starting character offset of the given marker.
	 */
	static int getCharStart(IMarker marker) {
		return marker.getAttribute(IMarker.CHAR_START, -1);
	}
	
	/**
	 * Returns the container name if it is defined, or empty string if not.
	 */
	public static String getContainerName(IMarker marker) {
		IPath path = marker.getResource().getFullPath();
		int n = path.segmentCount() - 1; // n is the number of segments in container, not path
		if (n <= 0)
			return ""; //$NON-NLS-1$
		int len = 0;
		for (int i = 0; i < n; ++i)
			len += path.segment(i).length();
		// account for /'s
		if (n > 1)
			len += n-1;
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < n; ++i) {
			if (i != 0)
				sb.append('/');
			sb.append(path.segment(i));
		}
		return sb.toString();
	}
		
	/**
	 * Returns the line number of the given marker.
	 */
	static int getLineNumber(IMarker marker) {
		return marker.getAttribute(IMarker.LINE_NUMBER, -1);
	}
	/**
	 * Returns the text for the location field.
	 */
	static String getLocation(IMarker marker) {
		return marker.getAttribute(IMarker.LOCATION, "");//$NON-NLS-1$
	}
	/**
	 * Returns the message attribute of the given marker,
	 * or the empty string if the message attribute is not defined.
	 */
	static String getMessage(IMarker marker) {
		return marker.getAttribute(IMarker.MESSAGE, "");//$NON-NLS-1$
	}
	/**
	 * Returns the numeric value of the given string, which is assumed to represent a numeric value.
	 *
	 * @return <code>true</code> if numeric, <code>false</code> if not
	 */
	static int getNumericValue(String value) {
		boolean negative = false;
		int i = 0;
		int len = value.length();
		
		// skip any leading '#'
		// workaround for 1GCE69U: ITPJCORE:ALL - Java problems should not have '#' in location.
		if (i < len && value.charAt(i) == '#') 
			++i;
	
		if (i < len && value.charAt(i) == '-') {
			negative = true;
			++i;
		}
		
		int result = 0;
		while (i < len) {
			int digit = Character.digit(value.charAt(i++), 10);
			if (digit < 0) {
				return result;
			}
			result = result * 10 + digit;
		}
		if (negative) {
			result = -result;
		}
		return result;
	}
	
	/**
	 * Implements IProvider interface by supporting a number of
	 * properties required for visual representation of markers
	 * in the tasklist.
	 */
	
	/**
	 * Returns name if it is defined, or
	 * blank string if not.
	 */
	public static String getResourceName(IMarker marker) {
		return marker.getResource().getName();
	}
	
	/**
	 * Returns the creation time of the marker as a string.
	 */
	static String getCreationTime(IMarker marker) {
		try {
			return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM).format(new Date(marker.getCreationTime()));
		} catch (CoreException e) {
			return null;
		}
	}
	
	
	public static void setNodeProperties(IMarker marker, IBookmark bookmark_node) {
		if (marker != null) {

			String s = MarkerUtil.getResourceName(marker);
			bookmark_node.setResource(s);

			s = MarkerUtil.getCreationTime(marker);

			s = MarkerUtil.getLocation(marker);

			Integer line2 = MarkerUtil.getLineNumber(marker);
			if (line2 != null && line2 instanceof Integer) {
				// String location =
				// BookmarkMessages.format("LineIndicator.text", new
				// String[] {line.toString()}); //$NON-NLS-1$
				//String location = "line: " + line2.toString(); //$NON-NLS-1$
				bookmark_node.setLocation(line2.toString());
			}

			IResource resource = marker.getResource();

			if (resource != null) {
				s = resource.getName();

				IPath path = resource.getFullPath();
				int n = path.segmentCount() - 1; // n is the number
				// of segments in
				// container, not
				// path
				if (n > 0) {
					int len = 0;
					for (int i = 0; i < n; ++i)
						len += path.segment(i).length();
					// account for /'s
					if (n > 1)
						len += n - 1;
					StringBuffer sb = new StringBuffer(len);
					for (int i = 0; i < n; ++i) {
						if (i != 0)
							sb.append('/');
						sb.append(path.segment(i));
					}
					s = sb.toString();
					bookmark_node.setPath(s);
				}
			}

		}
	}
	

	
	/**
	 * Открывает редактор для файла в проекте и переходит на номер линии
	 * @return true - if success 
	 */
	public static boolean gotoLine(String path, String resource,
			int line_number) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		String relPath = path + "/" + resource;
		// -- Ищем файл
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				relPath);
		try {
			if (resource != null && res.exists() && res.getType() == IResource.FILE) {
				// -- open editor for this file
				IEditorPart part = IDE.openEditor(page, (IFile) res, true);
				part.setFocus();
				if (part instanceof ITextEditor) {
					ITextEditor editor = (ITextEditor)part;
					IDocumentProvider provider= editor.getDocumentProvider();
					IDocument document= provider.getDocument(editor.getEditorInput());
	
					try {
						int start= document.getLineOffset(line_number-1);
						editor.selectAndReveal(start, 0);
	
					} catch (BadLocationException x) {
						//System.err.println("MarkerUtil.gotoLine() "+x.getMessage());
						return false;
					}				
				} else {
				}
			}
		} catch(Exception ex) {
			System.err.println("MarkerUtil.gotoLine() "+ex.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * Открывает редактор для файла в проекте и переходит на выделенный текст
	 */
	public static boolean goAtSelection(String path, String resource, String selection) { 
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
			String relPath = path + "/" + resource; 
			//-- Ищем файл
			IResource res = ResourcesPlugin.getWorkspace()
				.getRoot().findMember(relPath); 
		   if (resource!=null && res.exists() 
				   && res.getType()==IResource.FILE) { 
			  //-- open editor for this file
			  IEditorPart part = IDE.openEditor(page, (IFile) res, true);
		      part.setFocus();
		      if(part instanceof ITextEditor) {
		    	  IFindReplaceTarget frt= part == null ? null : 
		    		  (IFindReplaceTarget) part.getAdapter(IFindReplaceTarget.class);
		    	  
		    	  if(frt==null) {
		    		  System.err.println("MarkerUtil.gotoLine() IFindReplaceTarget is null!");
		    		  return false;
		    	  }
			      if(frt.findAndSelect(0, selection, true, true, false) == -1) {
			    	  System.err.println("MarkerUtil.gotoLine() Position for text "+selection+"not found");
		    		  return false;
			      }
		    	  
		      } else {
		      } 
		   }
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			return false;
		}
		return true;
	}

	
	/**
	 * Для предидущего JavaEditor-а находит выделенный текст и
	 * запоминает
	 * 
	 * @return true - if bookmark was made
	 * 
	 * !Now only for JavaEditor
	 */
	/*
	public boolean makeBookMarkEdt() {
		//-- find active page
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage();
		
		final NavigationHistory history = (NavigationHistory) page.getNavigationHistory();
		INavigationLocation inloc=null, inloc_old = history.getCurrentLocation();
		//-- переходим к предидущему редактору
	    history.backward();
	    inloc = history.getCurrentLocation();
	    if(inloc_old==inloc) {
			//-- nothig happens!
			return false;
		}
	    //-- gets name of file and projects //получаем имя файла и проекта
		IEditorPart edit_part=page.getActiveEditor();
		IFileEditorInput input = (IFileEditorInput)edit_part.getEditorInput() ;
		IFile file = input.getFile();		
    	m_file_name=file.getName();
    	IProject proj = file.getProject(); 
    	m_proj_name=proj.getName();
		//-- switch at our editor //возвращаемся к нашему редактору
		history.forward();
		//-- gets selected text //получаем выделенный текст
		if(edit_part instanceof JavaEditor) {
			//System.err.println("part is CompilationUnitEditor"); 
			JavaEditor ce = (JavaEditor)edit_part;
			//AbstractTextEditor ce = (AbstractTextEditor)edit_part;
			ISourceViewer sw = ce.getViewer();
    	  
			Point selection=sw.getSelectedRange();

			//IInformationProvider provider = new JavaElementProvider(edit_part, true);
			//Region reg=new Region(selection.x,selection.y);
			//Object element=provider.getInformation(sw, reg);
			
			//-- remember selected text
			m_selected_text=null;
			if (selection.y == 0){ 
		    	MessageDialog.openInformation(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(),
							"","Text was not selected!");
				return false;
			}
		    try {
		  	  IDocument doc = sw.getDocument();
		  	  m_selected_text= doc.get(selection.x, selection.y);
		    } catch (BadLocationException e) {}
	    } else {
	    	  MessageDialog.openInformation(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						"","Unknown type of IEditPart!!! [BookmarkJavaEditText.makeBookMark]");
	    	  System.err.println("Unknown type of IEditPart!!! [BookmarkJavaEditText.makeBookMark]");
	    	  return false;
	    }
		return true;
	}
	*/
	

	public static IMarker makeBookmarkPrevEditor(IBookmark bookmark_node) {
		// -- find active page
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		
		//-- переходим в предидущий редактор
		 final NavigationHistory history = (NavigationHistory) page.getNavigationHistory(); 
		 INavigationLocation inloc = null, inloc_old = history .getCurrentLocation(); // -- переходим к предидущему
		 //!!!My3.3M7 history.backward(); 
		 inloc = history.getCurrentLocation();
		 if (inloc_old == inloc) { 
			 // -- nothig happens! return; }
		 }
		 
 		 return make_bookmark(bookmark_node);
		
	}

	
	
	/**
	 * По тексту в последнем активном редакторе строим маркер Bookmark
	 * и связываем его с нашим BookmarkNode по id 
	 *  
	 * @param bookmark_node
	 * @return
	 */
	public static IMarker makeBookmark(IBookmark bookmark_node) {
		return make_bookmark(bookmark_node);
	}
	
	
	
	/**
	 * По тексту в последнем активном редакторе строим маркер Bookmark
	 * и связываем его с нашим BookmarkNode по id 
	 *  
	 * @param bookmark_node
	 * @return
	 */
	private static IMarker make_bookmark(IBookmark bookmark_node) {
		IMarker marker = null;

		// -- find active page
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		
		// -- gets name of file and projects //получаем имя файла и проекта
		IEditorPart edit_part = page.getActiveEditor();
		if(!(edit_part instanceof ITextEditor)) {
			System.err.println("edit_part Not instanceof ITextEditor");
			return null;
		}
		if(!(edit_part.getEditorInput() instanceof IFileEditorInput)) {
			System.err.println("edit_part.getEditorInput() Not instanceof IFileEditorInput");
			return null;
		}
		IFileEditorInput input = (IFileEditorInput) edit_part.getEditorInput();
		IFile file = input.getFile();

		ITextEditor editor = (ITextEditor)edit_part;

		try {
			// * Мы создаем маркер на корневом ресурсе, чтобы не привязываться к 
			// * конкретному файлу
			//marker = ResourcesPlugin.getWorkspace().getRoot().createMarker(IMarker.BOOKMARK);
			marker = file.createMarker(IMarker.BOOKMARK);
			
			if (marker.exists()) {
				marker.setAttribute(IMarker.MESSAGE, bookmark_node
						.getDescription());
				
					IDocumentProvider provider= editor.getDocumentProvider();
					IDocument document= provider.getDocument(editor.getEditorInput());

					TextSelection selection = (TextSelection )editor.getSelectionProvider().getSelection();
					int offset = selection.getOffset();
					
					try {
						int line = document.getLineOfOffset(offset)+1;
						marker.setAttribute(IMarker.LINE_NUMBER, line);
						long id = marker.getId();
						bookmark_node.setMarkerId(id);
						
						if (selection.getLength() >0 ){ 
							//-- устанавливаем выделенный текст если не был уже установлен
							if(bookmark_node.getSelection()==null
									||bookmark_node.getSelection().equals("")) {
								bookmark_node.setSelection(selection.getText());
							}
							//-- устанавливаем название если не было уже установлено
							if(bookmark_node.getName()==null
									||bookmark_node.getName().equals("")) {
								bookmark_node.setName(selection.getText());
							}
						}
						
						/*
						int start= document.getLineOffset(line);
						editor.selectAndReveal(start, 0);

						IWorkbenchPage page= editor.getSite().getPage();
						page.activate(editor);
						*/
					} catch (BadLocationException x) {
						// ignore
					}				
			
					MarkerUtil.setNodeProperties(marker, bookmark_node);
			} else {
				return null;
			}
			
		} catch (CoreException e) {
			System.err.println(" " + e.getMessage());
			// You need to handle the case where the marker no longer exists
			// }
			return null;
		}
		return marker;
	}


	/**
	 * Открывает редактор для файла в проекте и переходит на выделенный текст
	 */
	public static void goAtBookmark(IBookmark bookmark) {
		try {

			IMarker marker = MarkerUtil.getMarkerById(bookmark.getMarkerId());
			
			if (marker == null) {
				//System.err.println("Маркер не найден!");
				System.err.println("Marker not found!");
				return;
			}

			IResource resource = marker.getResource();
			if (resource != null && resource.exists()
					&& resource.getType() == IResource.FILE) {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				// -- open editor for this file
				IEditorPart part = IDE.openEditor(page, (IFile) resource, true);
				part.setFocus();

				IGotoMarker gotomarker = (IGotoMarker) part
						.getAdapter(IGotoMarker.class);
				if (gotomarker == null) {
					System.err
					.println("Adapter IGotoMarker for this editor not found!");
					//.println("Адаптер IGotoMarker для данного редактора не  найден!");
					return;
				}
				gotomarker.gotoMarker(marker);
			}
		} catch (CoreException ex) {
			System.err.println(ex.getMessage());
		}
	}

	
	public static IMarker getMarkerById(long marker_id) {
		if(marker_id==-1)
			return null;
		// -- сначала находим маркер по id
		IMarker marker = null;

		try {
		IMarker[] markers = ResourcesPlugin.getWorkspace().getRoot()
				.findMarkers(null, false, IResource.DEPTH_INFINITE);
		if (markers.length == 0) {
			//System.err.println("Маркер не найден!");
			System.err.println("Marker not found!");
			return null;
		}
		for (int i = 0; i < markers.length; i++) {
			if (markers[i].getId() == marker_id) {
				marker = markers[i];
				return marker;
			}
		}
		} catch(CoreException ex) {
			System.err.println(ex.getMessage());
			return null;
		}
		return marker;
	}
	

	
	
}

