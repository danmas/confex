import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class TestDisplayMode {

	public static void main(String[] args) {
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int x = graphicsDevice.getDisplayMode().getWidth();
		System.err.println(" x= "+x);
		int y = graphicsDevice.getDisplayMode().getHeight();
		System.err.println(" y= "+y);
	}
	
/*
	public void t() {
	ImageData pureWhiteIdeaImageData =
        new ImageData("C:/temp/Idea_PureWhite.jpg");
	pureWhiteIdeaImageData.transparentPixel =
        pureWhiteIdeaImageData.palette.getPixel(new RGB(255,255,255));
    final Image transparentIdeaImage = new Image(display,pureWhiteIdeaImageData);
    Canvas canvas = new Canvas(shell,SWT.NONE);
    Label l;
    Text t;
    t.setForeground(new Color(255,0,0)) .setEditable(editable)
    l.setImage(image)
    canvas.addPaintListener(new PaintListener() {
        public void paintControl(PaintEvent e) {
            e.gc.drawImage(transparentIdeaImage,0,0);
        }
    }); 
	}
*/
}
