package net.confex.schema.directedit;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridLayout;
import javax.swing.JButton;

import net.confex.schema.model.NodeElement;
import net.confex.schema.part.NodeElementPart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class NodeElementPropertyDialog implements IElementPropertyDialog {

	protected Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="264,12"
	protected Button okButton = null;
	protected Button cancelButton = null;
	protected CLabel cLabel = null;
	protected Label labelText = null;
	protected Text text = null;
	protected Label tool_tip_label = null;
	protected Text tooltip_textArea = null;
	
	NodeElement element;  //  @jve:decl-index=0:
	public void setNodeElement(NodeElement element) { 
		this.element = element; 
		this.text.setText(element.getText());
		tooltip_textArea.setText(element.getTooltipText());
		cLabel.setText(this.element.getAboutString());
	}
	protected void retOk() {
		ret = RETURN_COMMIT;
		this.element.setText(text.getText());
		this.element.setTooltipText(tooltip_textArea.getText());
		edit_part.CommitDirectEditChanges(element);
	}
	
	protected NodeElementPart edit_part;  //  @jve:decl-index=0:
	public void setEditPart(NodeElementPart edit_part ) {this.edit_part = edit_part; }
	
	protected int ret;
	public int getReturn() { return ret; }

	public static final int RETURN_COMMIT = 1;
	
	public NodeElementPropertyDialog() {
		createSShell();
	}
	
	
	/**
	 * This method initializes sShell
	 */
	protected void createSShell() {
		sShell = new Shell();
		sShell.setText("Shell");
		sShell.setBackground(new Color(Display.getCurrent(), 212, 208, 200));
		sShell.setLayout(null);
		sShell.setSize(new Point(434, 237));
		okButton = new Button(sShell, SWT.NONE);
		okButton.setBounds(new Rectangle(118, 169, 91, 26));
		okButton.setText("Ok");
		okButton.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				//System.out.println("keyPressed()"); // TODO Auto-generated Event stub keyPressed()
				retOk();
				sShell.setVisible(false);
			}
		});
		okButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				//System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
				retOk();
				sShell.setVisible(false);
			}
		});
		
		cancelButton = new Button(sShell, SWT.NONE);
		cancelButton.setBounds(new Rectangle(240, 170, 96, 26));
		cancelButton.setText("Cancel");
		cancelButton.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				//System.out.println("keyPressed()"); // TODO Auto-generated Event stub keyPressed()
				ret = 0;
				sShell.setVisible(false);
			}
		});
		cancelButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				//System.out.println("mouseUp()"); // TODO Auto-generated Event stub mouseUp()
				ret = 0;
				sShell.setVisible(false);
			}
		});
			
		cLabel = new CLabel(sShell, SWT.NONE);
		cLabel.setText(""); //this.element.getAboutString());
		cLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		cLabel.setBounds(new Rectangle(5, 5, 416, 36));
		labelText = new Label(sShell, SWT.NONE);
		labelText.setBounds(new Rectangle(5, 50, 71, 21));
		labelText.setText("Text:");
		text = new Text(sShell, SWT.BORDER);
		text.setBounds(new Rectangle(80, 50, 341, 26));
		tool_tip_label = new Label(sShell, SWT.NONE);
		tool_tip_label.setBounds(new Rectangle(0, 90, 76, 21));
		tool_tip_label.setText("ToolTip:");
		tooltip_textArea = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		tooltip_textArea.setBounds(new Rectangle(80, 90, 341, 41));
	}
	
	
	public void dispose() {
		sShell.dispose();
	}
	
	
	public void show() {
		sShell.setVisible(true);
		sShell.setActive();
		sShell.forceActive();
		
	}
	

}
