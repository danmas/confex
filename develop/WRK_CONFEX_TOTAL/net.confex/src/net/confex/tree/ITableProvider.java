package net.confex.tree;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.custom.TableCursor;

public interface ITableProvider {

	public TableCursor getCursor();
	
	public void setColorProvider(IColorProvider colorProvider);
	
	public void setCursorAtRowColumn(int row, int column);

}
