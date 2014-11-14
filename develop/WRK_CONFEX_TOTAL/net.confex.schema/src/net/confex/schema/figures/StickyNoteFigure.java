package net.confex.schema.figures;


import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;

/**
 * A Figure with a bent corner and an embedded TextFlow within a FlowPage that contains
 * text.
 */
public class StickyNoteFigure extends BentCornerFigure {

/** The inner TextFlow **/
private TextFlow textFlow;
private Label tooltip_label= new Label("");


/**
 *  Creates a new StickyNoteFigure with a default MarginBorder size of DEFAULT_CORNER_SIZE
 *  - 3 and a FlowPage containing a TextFlow with the style WORD_WRAP_SOFT.
 */
public StickyNoteFigure() {
	this(BentCornerFigure.DEFAULT_CORNER_SIZE - 3);
	setToolTip(tooltip_label);
}

/** 
 * Creates a new StickyNoteFigure with a MarginBorder that is the given size and a
 * FlowPage containing a TextFlow with the style WORD_WRAP_SOFT.
 * 
 * @param borderSize the size of the MarginBorder
 */
public StickyNoteFigure(int borderSize) {
	setBorder(new MarginBorder(borderSize));
	FlowPage flowPage = new FlowPage();

	textFlow = new TextFlow();

	textFlow.setLayoutManager(new ParagraphTextLayout(textFlow,
					ParagraphTextLayout.WORD_WRAP_SOFT));

	flowPage.add(textFlow);

	setLayoutManager(new StackLayout());
	add(flowPage);
	setToolTip(tooltip_label);
}

/**
 * Returns the text inside the TextFlow.
 * 
 * @return the text flow inside the text.
 */
public String getText() {
	return textFlow.getText();
}

/**
 * Sets the text of the TextFlow to the given value.
 * 
 * @param newText the new text value.
 */
public void setText(String newText) {
	textFlow.setText(newText);
}


public String getToolTipText() {
	return tooltip_label.getText();
}


public void setToolTipText(String txt) {
	tooltip_label.setText(txt);
}


/**
 * @see IFigure#setSize(Dimension)
 */
public void setSize(int w, int h) {
	super.setSize(w,h);
}


public void setBounds(Rectangle rect) {
	super.setBounds(rect);
}

}
