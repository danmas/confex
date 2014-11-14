package net.confex.sqlexplorer.model;

import java.sql.ResultSet;
import java.sql.Statement;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.dataset.DataSet;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.plugin.editors.SQLEditor;
import net.sourceforge.sqlexplorer.sqlpanel.SQLResult;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IViewPart;

public class SQLExecutionMy extends AbstractSQLExecutionMy {

	protected int _maxRows;

	protected SQLResult _sqlResult;

	protected Statement _stmt;

	// -- Курсор из таблицы для подключения слушателей!
	// protected TableCursor cursor=null;

	// public TableCursor getCursor() {
	// return cursor;
	// }

	MyDataSetTable table = null;

	public MyDataSetTable getTable() {
		return table;
	}

	public SQLExecutionMy(SQLEditor editor, IViewPart resultsView,
			String sqlString, int maxRows, SQLConnection connection
	/* SessionTreeNode sessionTreeNode */) {

		_connection = connection;
		_editor = editor;
		_sqlStatement = sqlString;
		_maxRows = maxRows;
		// _session = sessionTreeNode;
		_resultsView = resultsView;
		_sqlResult = new SQLResult();
		_sqlResult.setSqlStatement(_sqlStatement);

		// set initial message
		setProgressMessage(Messages.getString("SQLResultsView.ConnectionWait"));

	}

	public void setSqlStatement(String statement) {
		_sqlStatement = statement;
		_sqlResult.setSqlStatement(_sqlStatement);
	}

	IColorProvider colorProvider = null;
	
	public void setColorProvider(IColorProvider colorProvider) {
		//System.out.println("SQLExecutionMy.setColorProvider() "+colorProvider);
		this.colorProvider = colorProvider;
	}

	protected void displayResultsSync() {
		clearCanvas();

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 2;
		gLayout.marginLeft = 0;
		gLayout.horizontalSpacing = 0;
		gLayout.verticalSpacing = 0;
		gLayout.marginWidth = 0;
		gLayout.marginHeight = 0;
		_composite.setLayout(gLayout);
		try {
			int resultCount = _sqlResult.getDataSet().getRows().length;
			String statusMessage = Messages
					.getString("SQLResultsView.Time.Prefix")
					+ " "
					+ _sqlResult.getExecutionTimeMillis()
					+ " "
					+ Messages.getString("SQLResultsView.Time.Postfix");
			if (resultCount > 0) {
				statusMessage = statusMessage + "  "
						+ Messages.getString("SQLResultsView.Count.Prefix")
						+ " " + resultCount;
			}
			// !!!erv
			table = new MyDataSetTable(_composite, _sqlResult.getDataSet(),
					statusMessage, colorProvider);
			//table.setColorProvider(colorProvider);
			
			_composite.setData("parenttab", _parentTab);
		} catch (Exception e) {
			// add message
			String message = e.getMessage();
			Label errorLabel = new Label(_composite, SWT.FILL);
			errorLabel.setText(message);
			errorLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
					false));
			SQLExplorerPlugin.error("Error creating result tab", e);
		}
		_composite.layout();
		_composite.redraw();
		// reset to start message in case F5 will be used
		setProgressMessage(Messages.getString("SQLResultsView.ConnectionWait"));
	}

	/**
	 * Display SQL Results in result pane
	 */
	protected void displayResultsAsync() {
		_resultsView.getSite().getShell().getDisplay().asyncExec(
				new Runnable() {
					public void run() {
						displayResultsSync();
					};
				});
	}

	private void closeStatement() {

		if (_stmt == null) {
			return;
		}
		if (_stmt != null) {
			try {
				_stmt.close();
			} catch (Exception e) {
				SQLExplorerPlugin.error("Error closing statement.", e);
			}
		}
		_stmt = null;

	}

	/**
	 * Remove all items from parent
	 */
	public void clearComposite() {
		if (_composite == null)
			return;
		Control[] children = _composite.getChildren();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				children[i].dispose();
			}
		}
	}

	
	/**
	 * По умолчанию вызывается асинхронно
	 */
	protected void doExecution() throws Exception {
		doExecution(true);
	}

	
	protected void doExecutionAsync() throws Exception {
		doExecution(true);
	}

	protected void doExecutionSync() throws Exception {
		doExecution(false);
	}

	
	protected void doExecution(boolean async_flg) throws Exception {
		final long startTime = System.currentTimeMillis();

		try {
			_stmt = _connection.createStatement();

			setProgressMessage(Messages.getString("SQLResultsView.Executing"));

			_stmt.setMaxRows(_maxRows);

			if (_isCancelled) {
				return;
			}

			boolean b = _stmt.execute(_sqlStatement);

			if (_isCancelled) {
				closeStatement();
				return;
			}

			if (b) {

				final ResultSet rs = _stmt.getResultSet();
				if (rs != null) {

					if (_isCancelled) {
						closeStatement();
						return;
					}

					// create new dataset from results
					DataSet dataSet = new DataSet(null, rs, null);
					final long endTime = System.currentTimeMillis();

					// update sql result
					_sqlResult.setDataSet(dataSet);
					_sqlResult.setExecutionTimeMillis(endTime - startTime);

					// save successfull query
					// SQLExplorerPlugin.getDefault().getSQLHistory().addSQL(_sqlStatement,
					// _session.toString());

					closeStatement();

					if (_isCancelled) {
						return;
					}

					// show results..
					if (async_flg)
						displayResultsAsync();
					else
						displayResultsSync();

					// update text on editor
					_composite.getDisplay().asyncExec(new Runnable() {

						public void run() {

							String message = Messages
									.getString("SQLEditor.TotalTime.Prefix")
									+ " "
									+ (int) (endTime - startTime)
									+ " "
									+ Messages
											.getString("SQLEditor.TotalTime.Postfix");
							if (_editor != null) {
								_editor.setMessage(message);
							}
						}
					});
				}

			} else {

				final long endTime = System.currentTimeMillis();
				final int updateCount = _stmt.getUpdateCount();

				// update text on editor
				_composite.getDisplay().asyncExec(new Runnable() {

					public void run() {

						String message = ""
								+ updateCount
								+ " "
								+ Messages.getString("SQLEditor.Update.Prefix")
								+ " "
								+ (int) (endTime - startTime)
								+ " "
								+ Messages
										.getString("SQLEditor.Update.Postfix");
						if (_editor != null) {
							_editor.setMessage(message);
						}

						// close tab
						_parentTab.dispose();
					}
				});

				closeStatement();

				if (_isCancelled) {
					return;
				}

				// save successfull query
				// SQLExplorerPlugin.getDefault().getSQLHistory().addSQL(_sqlStatement,
				// _session.toString());

			}

			_stmt = null;

		} catch (Exception e) {

			closeStatement();
			throw e;
		}

	}

	/**
	 * Cancel sql execution and close execution tab.
	 */
	public void doStop() {

		if (_stmt != null) {

			try {
				_stmt.cancel();
			} catch (Exception e) {
				SQLExplorerPlugin.error("Error cancelling statement.", e);
			}
			try {
				closeStatement();
			} catch (Exception e) {
				SQLExplorerPlugin.error("Error closing statement.", e);
			}
		}

	}
	

	/*
	protected void displayResultsMy() {

		// _resultsView.getSite().getShell().getDisplay().asyncExec(new
		// Runnable() {

		// public void run() {

		clearCanvas();

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 2;
		gLayout.marginLeft = 0;
		gLayout.horizontalSpacing = 0;
		gLayout.verticalSpacing = 0;
		gLayout.marginWidth = 0;
		gLayout.marginHeight = 0;
		_composite.setLayout(gLayout);

		try {
			int resultCount = _sqlResult.getDataSet().getRows().length;
			String statusMessage = Messages
					.getString("SQLResultsView.Time.Prefix")
					+ " "
					+ _sqlResult.getExecutionTimeMillis()
					+ " "
					+ Messages.getString("SQLResultsView.Time.Postfix");

			if (resultCount > 0) {
				statusMessage = statusMessage + "  "
						+ Messages.getString("SQLResultsView.Count.Prefix")
						+ " " + resultCount;
			}
			new MyDataSetTable(_composite, _sqlResult.getDataSet(),
					statusMessage);

			_composite.setData("parenttab", _parentTab);

		} catch (Exception e) {

			// add message
			String message = e.getMessage();
			Label errorLabel = new Label(_composite, SWT.FILL);
			errorLabel.setText(message);
			errorLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
					false));

			SQLExplorerPlugin.error("Error creating result tab", e);
		}

		_composite.layout();
		_composite.redraw();

		// reset to start message in case F5 will be used
		setProgressMessage(Messages.getString("SQLResultsView.ConnectionWait"));
		// };
		// });
	}*/
	

}
