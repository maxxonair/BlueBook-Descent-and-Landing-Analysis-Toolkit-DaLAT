package GUI.PropulsionDraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.PropulsionDraw.ComponentMetaFileTypes.ComponentMetaFile;

public class ComponentElement extends JComponent {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
  private volatile int screenX = 0;
  private volatile int screenY = 0;
  private volatile int myX = 0;
  private volatile int myY = 0;
  
  private volatile int posX=0;
  private volatile int posY=0;
  
  private int posXFrozen=0;
  private int posYFrozen=0;
  
  private boolean freezeX=false;
  private boolean freezeY=false;
  
  private int posIsX=0;
  private int posIsY=0;
  
  private int sizeX = 100;
  private int sizeY = 100;
  
  
  private Canvas canvas;
  
  JLabel labelElement;
  
  JPanel background;
  JComponent component;
  
  private JLabel label;
  private String imageFilePath;

  public  ComponentElement(String imageFilePath, Canvas canvas) {
	  
	  this.canvas=canvas;
	  this.component = this;
	  this.imageFilePath=imageFilePath;
	
    setBounds(0, 0, sizeX, sizeY);
    setLayout(new BorderLayout());
    setOpaque(false);
    
    labelElement = new JLabel("");
    labelElement.setSize(150, 25);
    labelElement.setForeground(Color.WHITE);
    labelElement.setHorizontalAlignment(JLabel.CENTER);
    add(labelElement, BorderLayout.PAGE_START);
    
        ImageIcon image;
			image = new ImageIcon(imageFilePath,"");
			image = new ImageIcon(getScaledImage(image.getImage(),sizeX,sizeY));
	         label = new JLabel(image);
	        add(label, BorderLayout.CENTER);
	        
    
    addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {        

      }

      @Override
      public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        myX = getX();
        myY = getY();
      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) { }

      @Override
      public void mouseExited(MouseEvent e) { }
      

    });
    addMouseMotionListener(new MouseMotionListener() {

      @Override
      public void mouseDragged(MouseEvent e) {  	 
    	  if(e.getButton()==1) {
	        int deltaX = e.getXOnScreen() - screenX;
	        int deltaY = e.getYOnScreen() - screenY;
	        
	        if(!freezeX) {	posXFrozen = posX;}
	        if(!freezeY) {  posYFrozen = posY;}
	        
	        posX = myX + deltaX;
	        posY = myY + deltaY;
	       // System.out.println(posX+"|"+posY);
	        
	        Point cPoint = canvas.getLocation();
	        Point fPoint = new Point(0,0);
	        Dimension cSize = canvas.getSize();
	        int fx = (int) fPoint.getX();
	        int fy = (int) fPoint.getY();
	        int cx = (int) - cPoint.getX();
	        int cy = (int) - cPoint.getY();
	        int fSy = (int) cSize.getHeight();
	        int fSx = (int) cSize.getWidth();
	        
	        int marginX = fSx-fx-cx-sizeX;
	        int marginY = fSy-fy-cy-sizeY;
	        if(posX<marginX  && posX >0 ) {
		         if (posY<marginY&& posY >0){
		        	 freezeX=false;
		        	 freezeY=false;
		        		posIsX=posX;
		        		posIsY=posY;
		        } else {
		        		freezeY=true;
		        		posIsX=posX;
		        		posIsY=posYFrozen;
		        }
	        } else if(posY<marginY&& posY >0) {
		         if (posX<marginX  && posX >0){
				        	 freezeX=false;
				        	 freezeY=false;
				        		posIsX=posX;
				        		posIsY=posY;
			        } else {
			        		freezeX=true;
			        		posIsX=posXFrozen;
			        		posIsY=posY;
			        }
	        }
	        setLocation(posIsX, posIsY);
	        updatePosition(posIsX, posIsY);
	        canvas.getReadWrite().writeFile();
	        canvas.repaint();
    	  } 

      }

      @Override
      public void mouseMoved(MouseEvent e) { }

    });
    grabFocus();
  }
  
  

public Canvas getCanvas() {
	return canvas;
}

public void setName(String name) {
	labelElement.setText(""+name);
	labelElement.repaint();
	repaint();
}

public void setCanvas(Canvas canvas) {
	this.canvas = canvas;
}

public void setElementName(String name) {
	labelElement.setText(name);
}

public int getPosX() {
	return posX;
}

public void setPosX(int posX) {
	setLocation(posX, posY);
	this.posX = posX;
}

public int getPosY() {
	return posY;
}

public void setPosY(int posY) {
	setLocation(posX, posY);
	this.posY = posY;
}

public void resizeElement(int x, int y) {
	sizeX = x;
	sizeY = y;
	setSize(x,y);
	this.remove(label);
    ImageIcon image;
		image = new ImageIcon(imageFilePath,"");
		image = new ImageIcon(getScaledImage(image.getImage(),x,y));
         label = new JLabel(image);
        add(label, BorderLayout.CENTER);
	repaint();
}


public void updatePosition(int x, int y) {
	for(BoxElement element : canvas.getCanvasElements()) {
		if(component==element.getElement()) {
			ComponentMetaFile file = element.getMetaFile();
			file.setPositionX(x);
			file.setPositionY(y);
			element.setMetaFile(file);
		}
	}
}

static Image getScaledImage(Image srcImg, int w, int h){
    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = resizedImg.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(srcImg, 0, 0, w, h, null);
    g2.dispose();

    return resizedImg;
}
  

}