/**
 * 
 */

package net.confex.editor.tree;



public interface IBookmark  {

	public String getResource();
	
	public void setResource(String resource);
	
	public String getPath();
	
	public void setPath(String path);
	
	public String getLocation();
	
	public void setLocation(String location);
	
	public long getMarkerId();
	
	public void setMarkerId(long markerId);

	public String getSelection();

	public void setSelection(String selection);
	
	public String getName();
	
	public void setName(String name);

	public String getDescription();
	
	public void setDescription(String description);

}
