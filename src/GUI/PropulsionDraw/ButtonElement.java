package GUI.PropulsionDraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

class ButtonElement extends JComponent {
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int sizeX = 100;
	  private int sizeY = 100;
	  
	  private boolean isImage=true;
	
	public ButtonElement(String imageFilePath) {
	    setBounds(0, 0, sizeX, sizeY);
	    setLayout(new BorderLayout());
	    setOpaque(false);
	    
	    
	    if(isImage) {
	        ImageIcon image;
				image = new ImageIcon(imageFilePath,"");
				image = new ImageIcon(getScaledImage(image.getImage(),sizeX,sizeY));
		        JLabel label = new JLabel(image);
		        add(label, BorderLayout.CENTER);      
	    } else {
	    	setBorder(new LineBorder(Color.BLACK, 1));
	    	JLabel background = new JLabel();
			Color color = new Color((int) (Math.random()*255),
					(int) (Math.random()*255), 
					(int) (Math.random()*255));
	        background.setBackground(color);
	        background.setForeground(color);
	        add(background, BorderLayout.CENTER);
	    }

	}
	
	
	  
	  public void setImage(boolean isImage) {
		this.isImage = isImage;
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