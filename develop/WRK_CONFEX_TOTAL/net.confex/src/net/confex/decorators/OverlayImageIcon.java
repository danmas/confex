/*******************************************************************************
 * Copyright (c) 2006 Roman Eremeev and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     balajik - initial API and implementation
 *     Roman Eremeev - small modifications
 *******************************************************************************/
package net.confex.decorators;

import java.util.Vector;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * 
 * This class is used for overlaying image icons
 *
 * 
 */
public class OverlayImageIcon extends CompositeImageDescriptor
{
  /**
   * Base image of the object
   */ 
  private Image baseImage_;
  
  /**
   * Size of the base image 
   */ 
  private Point sizeOfImage_;
  
  /**
   * Vector of image keys
   */
  private Vector<String> imageKey_; 
  
  /**
   * Demo Image instance 
   */
  private DemoImages demoImage_;
  
  private static final int TOP_LEFT = 0;
  private static final int TOP_RIGHT = 1;
  private static final int BOTTOM_LEFT = 2;
  private static final int BOTTOM_RIGHT = 3;
  
  /**
   * Constructor for overlayImageIcon.
   */
  public OverlayImageIcon(Image baseImage, 
                          DemoImages demoImage,
                          Vector<String> imageKey)
  {
    // Base image of the object
    baseImage_ = baseImage;
    // Demo Image Object 
    demoImage_ = demoImage;
    imageKey_ = imageKey;
    sizeOfImage_ = new Point(baseImage.getBounds().width, 
                             baseImage.getBounds().height);
  }

  /**
   * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int, int)
   * DrawCompositeImage is called to draw the composite image.
   * 
   */
  protected void drawCompositeImage(int arg0, int arg1)
  {
    // Draw the base image
     drawImage(baseImage_.getImageData(), 0, 0); 
     int[] locations = organizeImages();
     for (int i=0; i < imageKey_.size(); i++)
     {
        ImageData imageData = demoImage_.getImageData((String)imageKey_.get(i));
        switch(locations[i])  {
          // Draw on the top left corner
          case TOP_LEFT:
            drawImage(imageData, 0, 0);
            break;
          // Draw on top right corner  
          case TOP_RIGHT:
            drawImage(imageData, sizeOfImage_.x - imageData.width, 0);
            break;
          // Draw on bottom left  
          case BOTTOM_LEFT:
            drawImage(imageData, 0, sizeOfImage_.y - imageData.height);
            break;
          // Draw on bottom right corner  
          case BOTTOM_RIGHT:
            drawImage(imageData, sizeOfImage_.x - imageData.width,
                      sizeOfImage_.y - imageData.height);
            break;
            
        }
     }
   
  }
  
  /**
   * Organize the images. This function scans through the image key and 
   * finds out the location of the images
   */ 
  private int [] organizeImages()
  {
    int[] locations = new int[imageKey_.size()];
    String imageKeyValue;
    for (int i = 0; i < imageKey_.size(); i++)
    {
      imageKeyValue = (String)imageKey_.get(i);
      if (imageKeyValue.equals("Lock")) {
        locations[i] = TOP_LEFT;
      }
      if (imageKeyValue.equals("Error")) {
        locations[i] = BOTTOM_RIGHT;
      }
      if (imageKeyValue.equals("Success")) {
        locations[i] = BOTTOM_RIGHT;
      }
      if (imageKeyValue.equals("Run")) {
        locations[i] = BOTTOM_RIGHT;
      }
      if (imageKeyValue.equals("LockRunInBatch")) {
        locations[i] = BOTTOM_RIGHT;
      }
      
    }
    return locations;
  }
      

  /**
   * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
   * get the size of the object
   */
  protected Point getSize()
  {
    return sizeOfImage_;
  }
  
  /**
   * Get the image formed by overlaying different images on the base image
   * 
   * @return composite image
   */ 
  public Image getImage()
  {
    return createImage();
  }


}






