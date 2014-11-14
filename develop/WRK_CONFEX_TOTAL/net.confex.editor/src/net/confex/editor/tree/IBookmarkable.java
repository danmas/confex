package net.confex.editor.tree;

public interface IBookmarkable {

	public void addBookmark(IBookmark bookmark);
	
	public boolean hasBookmark();
	
	
	public IBookmark getBookmark();
	
}
