package net.confex.schema.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.graphics.Color;

public class ActiveElementFigure extends NodeElementFigure 
								 implements IActiveElementFigure {
	
	public final static int ACTIVE_LINE_WIDTH = 4;
	public final static int ACT_NORMAL_LINE_WIDTH = 1;
	
	public final static Color PASSIVE_COLOR = ColorConstants.blue;
	public final static Color ACTIVE_COLOR = ColorConstants.yellow;
	public final static Color ERROR_COLOR = ColorConstants.red;
	public final static Color OK_COLOR = ColorConstants.green;
	
	
	public ActiveElementFigure(String text) {
		super(text);
		if(getBorder()== null)
			setBorder(new LineBorder(PASSIVE_COLOR, ACT_NORMAL_LINE_WIDTH));
	}

	
	/**
	 * Рисует элемент в пассивном состоянии
	 */
	public void drawPassiveState() {
		if( getBorder() instanceof LineBorder) {
			((LineBorder)getBorder()).setWidth(ACT_NORMAL_LINE_WIDTH); 
			((LineBorder)getBorder()).setColor(PASSIVE_COLOR); 
		} else {
			System.out.println("Не определен LineBorder для элемента! [ActiveElementFigure.drawPassiveState]");
		}
	}
	
	
	/**
	 * Рисует элемент в активном состоянии
	 */
	public void drawActiveState() {
		if( getBorder() instanceof LineBorder) {
			((LineBorder)getBorder()).setWidth(ACTIVE_LINE_WIDTH); 
			((LineBorder)getBorder()).setColor(ACTIVE_COLOR); 
		} else {
			System.out.println("Не определен LineBorder для элемента! [ActiveElementFigure.drawPassiveState]");
		}
	}
	
	
	/**
	 * Рисует элемент в состоянии ошибочного завершения
	 */
	public void drawErrorState() {
		if( getBorder() instanceof LineBorder) {
			((LineBorder)getBorder()).setWidth(ACTIVE_LINE_WIDTH); 
			((LineBorder)getBorder()).setColor(ERROR_COLOR); 
		} else {
			System.out.println("Не определен LineBorder для элемента! [ActiveElementFigure.drawPassiveState]");
		}
	}
	
	
	/**
	 * Рисует элемент в cостоянии удачного заваршения 
	 */
	public void drawOkState() {
		if( getBorder() instanceof LineBorder) {
			((LineBorder)getBorder()).setWidth(ACTIVE_LINE_WIDTH); 
			((LineBorder)getBorder()).setColor(OK_COLOR); 
		} else {
			System.out.println("Не определен LineBorder для элемента! [ActiveElementFigure.drawPassiveState]");
		}
	}
	
	

}
