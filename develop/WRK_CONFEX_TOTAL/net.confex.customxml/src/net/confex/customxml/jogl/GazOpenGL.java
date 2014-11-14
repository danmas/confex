package net.confex.customxml.jogl;


import java.awt.*;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import com.sun.opengl.util.*;

/**
 * Gears.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */

public class GazOpenGL implements 
	GLEventListener, MouseListener, MouseMotionListener, KeyListener {
	
	GLCanvas canvas;
	
	public GazOpenGL(GLCanvas canvas) {
		this.canvas = canvas;
	}
	
	   /** Called by the drawable to initiate OpenGL rendering by the client.
     * After all GLEventListeners have been notified of a display event, the 
     * drawable will swap its buffers if necessary.
     * @param gLDrawable The GLDrawable object.
     */    
    public void display(GLAutoDrawable gLDrawable)
    {
      final GL gl = gLDrawable.getGL();
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
      gl.glLoadIdentity();
      gl.glTranslatef(-1.5f, 0.0f, -6.0f);
      gl.glBegin(GL.GL_TRIANGLES);		    // Drawing Using Triangles
        gl.glColor3f(1.0f, 0.0f, 0.0f);   // Set the current drawing color to red
        gl.glVertex3f( 0.0f, 1.0f, 0.0f);	// Top
        gl.glColor3f(0.0f, 1.0f, 0.0f);   // Set the current drawing color to green
        gl.glVertex3f(-1.0f,-1.0f, 0.0f);	// Bottom Left
        gl.glColor3f(0.0f, 0.0f, 1.0f);   // Set the current drawing color to blue
        gl.glVertex3f( 1.0f,-1.0f, 0.0f);	// Bottom Right
      gl.glEnd();				// Finished Drawing The Triangle
      gl.glTranslatef(3.0f, 0.0f, 0.0f);
      gl.glBegin(GL.GL_QUADS);           	// Draw A Quad
        gl.glColor3f(0.5f, 0.5f, 1.0f);   // Set the current drawing color to light blue
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);	// Top Left
        gl.glVertex3f( 1.0f, 1.0f, 0.0f);	// Top Right
        gl.glVertex3f( 1.0f,-1.0f, 0.0f);	// Bottom Right
        gl.glVertex3f(-1.0f,-1.0f, 0.0f);	// Bottom Left
      gl.glEnd();				// Done Drawing The Quad
      gl.glFlush();
    }
    
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
    
    
	 /** Called by the drawable immediately after the OpenGL context is 
    * initialized for the first time. Can be used to perform one-time OpenGL 
    * initialization such as setup of lights and display lists.
    * @param gLDrawable The GLDrawable object.
    */
   public void init(GLAutoDrawable gLDrawable)
    {
      final GL gl = gLDrawable.getGL();
      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
      gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
      gLDrawable.addKeyListener(this);
    }
    
  
	 /** Called by the drawable during the first repaint after the component has 
    * been resized. The client can update the viewport and view volume of the 
    * window appropriately, for example by a call to 
    * GL.glViewport(int, int, int, int); note that for convenience the component
    * has already called GL.glViewport(int, int, int, int)(x, y, width, height)
    * when this method is called, so the client may not have to do anything in
    * this method.
    * @param gLDrawable The GLDrawable object.
    * @param x The X Coordinate of the viewport rectangle.
    * @param y The Y coordinate of the viewport rectanble.
    * @param width The new width of the window.
    * @param height The new height of the window.
    */
	 public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
      final GL gl = gLDrawable.getGL();
      final GLU glu = new GLU();

      if (height <= 0) // avoid a divide by zero error!
        height = 1;
      final float h = (float)width / (float)height;
      gl.glViewport(0, 0, width, height);
      gl.glMatrixMode(GL.GL_PROJECTION);
      gl.glLoadIdentity();
      glu.gluPerspective(45.0f, h, 1.0, 20.0);
      gl.glMatrixMode(GL.GL_MODELVIEW);
      gl.glLoadIdentity();
    }

	 
	 
  // Methods required for the implementation of MouseListener
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}

  public void mousePressed(MouseEvent e) {
	  System.out.println("mousePressed");
    //if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
    //  mouseRButtonDown = true;
    //}
  }
    
  public void mouseReleased(MouseEvent e) {
    //if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
    //  mouseRButtonDown = false;
    //}
  }
    
  public void mouseClicked(MouseEvent e) {}
    
  // Methods required for the implementation of MouseMotionListener
  public void mouseDragged(MouseEvent e) {
    canvas.repaint();
  }
    
  public void mouseMoved(MouseEvent e) {
    canvas.repaint();
  }
  
  
  public void keyTyped(KeyEvent key)  {
	  canvas.repaint();
  }

  public void keyPressed(KeyEvent key) {
	  System.out.println("keyPressed");
    switch (key.getKeyCode()) {
    case KeyEvent.VK_A:
    	//z_position += 0.5;
    	//  System.out.println("z = "+z_position);
    	  break;
    case KeyEvent.VK_Z:
    	//z_position -= 0.5;
    	//  System.out.println("z = "+z_position);
    	  break;
  	  /*
      new Thread(){
        public void run() {
          //animator.stop();
        }
      }.start();
      //System.exit(0);
       */
      default:
        break;
    }
  }

  public void keyReleased(KeyEvent key)
  {
  }  
}
