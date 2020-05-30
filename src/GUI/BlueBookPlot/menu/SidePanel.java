package GUI.BlueBookPlot.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import GUI.BlueBookPlot.main.BlueBookPlot;

public class SidePanel {
	
    private static Color labelColor = new Color(220,220,220);    
    private static Color backgroundColor = new Color(41,41,41);
	
	public static JPanel create() {
		
		
     	ImageIcon iconPlus =null;
     	ImageIcon iconMinus =null;
     	int sizeUpperBar=25;
     	try {
     		iconPlus  = new ImageIcon("images/iconPlus.png","");
     		iconMinus = new ImageIcon("images/iconMinus.png","");
     		if(!BlueBookPlot.isDarkTemplate()) {
     		iconPlus.setImage(invertImage(iconPlus.getImage()));
     		iconMinus.setImage(invertImage(iconMinus.getImage()));
     		}
     		iconPlus  = new ImageIcon(getScaledImage(iconPlus.getImage(),sizeUpperBar,sizeUpperBar));
     		iconMinus = new ImageIcon(getScaledImage(iconMinus.getImage(),sizeUpperBar,sizeUpperBar));
     	} catch (Exception e) {
     		System.err.println("Error: Loading image icons failed");
     	}
		
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new GridLayout(0,1));
        sideBar.setBackground(backgroundColor);
        sideBar.setForeground(labelColor);
        sideBar.setPreferredSize(new Dimension(25,1200));
        sideBar.setMinimumSize(new Dimension(25,50));
        sideBar.setMaximumSize(new Dimension(25,1200));
        
        JButton addPlot = new JButton("");
        addPlot.setSize(25,25);
        addPlot.setBackground(backgroundColor);
        addPlot.setForeground(Color.black);
        addPlot.setFont(BlueBookPlot.getSmallFont());
        addPlot.setOpaque(true);
        addPlot.setBorderPainted(false);
        addPlot.setIcon(iconPlus);
        addPlot.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
           	   BlueBookPlot.setPlotNumber(BlueBookPlot.getPlotNumber()+1);
           	   if(BlueBookPlot.getPlotNumber()>4) {
           		BlueBookPlot.setPlotNumber(4);
           	   }
           	BlueBookPlot.getPlotPanelManager().setPlotNumber(BlueBookPlot.getPlotNumber());
           	BlueBookPlot.getPlotPanelManager().refresh(BlueBookPlot.getVariableList(), BlueBookPlot.getResultFilePath());
        	}} );
        sideBar.add(addPlot);
        
        JButton deletePlot = new JButton("");
        deletePlot.setSize(25,25);
        deletePlot.setBackground(backgroundColor);
        deletePlot.setForeground(Color.black);
        deletePlot.setFont(BlueBookPlot.getSmallFont());
        deletePlot.setOpaque(true);
        deletePlot.setBorderPainted(false);
        deletePlot.setIcon(iconMinus);
        deletePlot.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
        		  BlueBookPlot.setPlotNumber(BlueBookPlot.getPlotNumber()-1);
           	   if(BlueBookPlot.getPlotNumber()<1) {
           		BlueBookPlot.setPlotNumber(1);
           	   }
           	BlueBookPlot.getPlotPanelManager().setPlotNumber(BlueBookPlot.getPlotNumber());
           	BlueBookPlot.getPlotPanelManager().refresh(BlueBookPlot.getVariableList(), BlueBookPlot.getResultFilePath());
        	}} );
        sideBar.add(deletePlot);
        
        return sideBar;
	}
	
    public static Image invertImage(Image image) {
        // BufferedImage inputFile = null;
     		BufferedImage bufferedImage = toBufferedImage(image);
         for (int x = 0; x < bufferedImage.getWidth(); x++) {
             for (int y = 0; y < bufferedImage.getHeight(); y++) {
                 int rgba = bufferedImage.getRGB(x, y);
                 Color col = new Color(rgba, true);
                 col = new Color(255 - col.getRed(),
                                 255 - col.getGreen(),
                                 255 - col.getBlue());
                 bufferedImage.setRGB(x, y, col.getRGB());
             }
         }
         return (Image) bufferedImage;
     }
     public static BufferedImage toBufferedImage(Image img)
     {
         if (img instanceof BufferedImage)
         {
             return (BufferedImage) img;
         }

         // Create a buffered image with transparency
         BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

         // Draw the image on to the buffered image
         Graphics2D bGr = bimage.createGraphics();
         bGr.drawImage(img, 0, 0, null);
         bGr.dispose();

         // Return the buffered image
         return bimage;
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
