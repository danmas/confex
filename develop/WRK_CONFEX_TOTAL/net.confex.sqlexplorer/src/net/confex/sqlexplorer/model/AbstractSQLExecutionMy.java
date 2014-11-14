package net.confex.sqlexplorer.model;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.plugin.editors.SQLEditor;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IViewPart;


/**
 *   Может быть более правильно работать через public OpenConnectionJob
 *   а здесь мы убираем работу с сессиями коннекций 
 */

public abstract class AbstractSQLExecutionMy {

    private class LocalThread extends Thread {

        public void run() {

            try {

            	/*!!!erv
                while (_connection == null) {

                    if (_isCancelled) {
                        break;
                    }
                    _connection = _session.getQueuedConnection(_connectionNumber);

                    if (_connection == null) {
                        sleep(100);
                    }
                }!!!erv*/

                if ((!_isCancelled) && _connection != null) {
                    doExecution();
                }

            } catch (final Exception e) {

                if (!(e instanceof java.sql.SQLException || e instanceof InterruptedException)) {
                    // only log non-sql errors
                    SQLExplorerPlugin.error("Error executing.", e);
                }

                final Shell shell = _resultsView.getSite().getShell();
                shell.getDisplay().asyncExec(new Runnable() {

                    public void run() {

                        clearCanvas();
                        if (!(e instanceof InterruptedException)) {
                            MessageDialog.openError(shell, Messages.getString("SQLResultsView.Error.Title"),
                                    e.getMessage());
                        }
                        if (_parentTab != null) {
                            _parentTab.dispose();
                        }
                    }
                });

            } finally {

            	/*!!!erv
                _session.releaseQueuedConnection(_connectionNumber);
                _connection = null;
                !!!erv*/

            }
        }
    }

    private Integer _connectionNumber;

    protected boolean _isCancelled = false;

    protected Composite _composite;

    protected SQLEditor _editor;

    private LocalThread _executionThread;

    private Group _group;

    protected TabItem _parentTab;

    private String _progressMessage;

    protected IViewPart _resultsView;

    //protected SessionTreeNode _session;

    protected String _sqlStatement;

    protected SQLConnection _connection;


    /**
     * Clear progress bar or results.
     */
    protected final void clearCanvas() {

        if (_parentTab == null || _parentTab.isDisposed()) {
            return;
        }

        if (_isCancelled) {
            return;
        }

        // restore correct label
        _parentTab.setText((String) _parentTab.getData("tabLabel"));

        Control[] children = _composite.getChildren();

        if (children != null) {

            for (int i = 0; i < children.length; i++) {
                children[i].dispose();
            }
        }

        _group = null;
    }


    /**
     * Display progress bar on tab until results are ready.
     */
    protected final void displayProgress() {

        clearCanvas();

        // set label to running
        _parentTab.setText(Messages.getString("SQLResultsView.Running"));

        GridLayout gLayout = new GridLayout();
        gLayout.numColumns = 2;
        gLayout.marginLeft = 0;
        gLayout.horizontalSpacing = 0;
        gLayout.verticalSpacing = 0;
        gLayout.marginWidth = 0;
        gLayout.marginHeight = 50;
        _composite.setLayout(gLayout);

        _group = new Group(_composite, SWT.NULL);
        _group.setLayout(new GridLayout());
        _group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        _group.setText(_progressMessage);

        // add progress bar
        Composite pbComposite = new Composite(_group, SWT.FILL);
        FillLayout pbLayout = new FillLayout();
        pbLayout.marginHeight = 2;
        pbLayout.marginWidth = 5;
        pbComposite.setLayout(pbLayout);
        pbComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        ProgressBar pb = new ProgressBar(pbComposite, SWT.HORIZONTAL | SWT.INDETERMINATE | SWT.BORDER);
        pb.setVisible(true);
        pb.setEnabled(true);

        pbComposite.layout();
        _composite.layout();

    }


    /**
     * Main execution method. This method is called from a background thread.
     * 
     * @throws Exception
     */
    protected abstract void doExecution() throws Exception;


    /**
     * This method will be called from the UI thread when execution is cancelled
     * and the tab will be disposed. Do any cleanups required in here.
     */
    protected abstract void doStop() throws Exception;


    public final String getSqlStatement() {
        return _sqlStatement;
    }


    public final void setComposite(Composite composite) {
    	//_composite = new Composite(composite,SWT.FILL);
        _composite = composite;
    }


    public final void setParentTab(TabItem parentTab) {

        _parentTab = parentTab;
    }


    /**
     * @param progressMessage
     */
    public final void setProgressMessage(String progressMessage) {

        _progressMessage = progressMessage;
        if (_group != null) {

            _resultsView.getSite().getShell().getDisplay().asyncExec(new Runnable() {

                public void run() {

                    _group.setText(_progressMessage);
                    _group.redraw();
                }
            });
        }
    }


    /**
     * Start exection
     */
    public final void startExecution() {

    	/*!!!erv
        _connectionNumber = _session.getQueuedConnectionNumber();
        !!!erv*/
        
        // start progress bar
        displayProgress();

        // start execution in seperate thread
        _executionThread = new LocalThread();
        _executionThread.start();

    }


    /**
     * Cancel execution.
     */
    public final void stop() {

        try {

            _isCancelled = true;

            doStop();

        } catch (final Exception e) {

            final Shell shell = _resultsView.getSite().getShell();
            shell.getDisplay().asyncExec(new Runnable() {

                public void run() {

                    MessageDialog.openError(shell, Messages.getString("SQLResultsView.Error.Title"), e.getMessage());
                }
            });

        }

    }

}
