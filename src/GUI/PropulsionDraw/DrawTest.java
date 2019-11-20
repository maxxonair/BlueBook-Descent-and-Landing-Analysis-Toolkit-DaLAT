package GUI.PropulsionDraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import GUI.BlueBookVisual;
import GUI.PostProcessing.CreateCustomChart.BackgroundMenuBar;

public class DrawTest {
	static JFrame frame = new JFrame("Propulsion Block Draw Mk1");
	
	public static JPanel Canvas = new JPanel();
	static JLabel canvasBackground;
	
	private static String backgroundImagePath = "images/blueprintBackground.jpeg";
	
	private static String imageMoon = "images/moon.png";
	private static String imageRocketEngine = "images/rocketEngine.png";
	
	static Font smallFont  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	
  public static void main(String[] args) {
   // JFrame frame = new JFrame("Propulsion Block Draw Mk1");

    // by doing this, we prevent Swing from resizing
    // our nice component
    frame.setLayout(new BorderLayout());
    
	BackgroundMenuBar menuBar = new BackgroundMenuBar();
    menuBar.setColor(new Color(250,250,250));
    menuBar.setOpaque(true);
    menuBar.setPreferredSize(new Dimension(1200, 25));
    frame.add(menuBar, BorderLayout.NORTH);

    JMenu menuMain = new JMenu("File");
    menuMain.setForeground(BlueBookVisual.getLabelColor());
    menuMain.setBackground(BlueBookVisual.getBackgroundColor());
    menuMain.setFont(smallFont);
    menuMain.setMnemonic(KeyEvent.VK_A);
    menuBar.add(menuMain);
    
    JSplitPane splitPaneVertical = new JSplitPane();
    splitPaneVertical.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
    splitPaneVertical.setBackground(BlueBookVisual.getBackgroundColor());
    splitPaneVertical.setDividerSize(3);
    splitPaneVertical.setUI(new BasicSplitPaneUI() {
           @SuppressWarnings("serial")
			public BasicSplitPaneDivider createDefaultDivider() {
           return new BasicSplitPaneDivider(this) {
               @SuppressWarnings("unused")
				public void setBorder( Border b) {
               }
               @Override
                   public void paint(Graphics g) {
                   g.setColor(Color.gray);
                   g.fillRect(0, 0, getSize().width, getSize().height);
                       super.paint(g);
                   }
           };
           }
       });
    splitPaneVertical.setDividerLocation(200); 
   // splitPaneVertical.setDividerLocation(0.1);
    frame.add(splitPaneVertical, BorderLayout.CENTER);
    		
    
    JPanel OperatorPanel = new JPanel();
    OperatorPanel.setSize(500, 900);
    OperatorPanel.setBackground(BlueBookVisual.getBackgroundColor());
    OperatorPanel.setLayout(null);
    
    JScrollPane scrollPane = new JScrollPane(OperatorPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(300,500));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    splitPaneVertical.add(scrollPane, JSplitPane.LEFT);
    	    

    	    ButtonElement moon = new ButtonElement(imageMoon);
    	    moon.setLocation(10, 10);
    	    moon.setSize(100,100);
    	    moon.addMouseListener(new MouseListener() {

    		      @Override
    		      public void mouseClicked(MouseEvent e) {        

    		      }

    		      @Override
    		      public void mousePressed(MouseEvent e) {
					BoxElement mc = new BoxElement(imageMoon);
				    //mc.setPosX((int) Math.random()*100);
				   // mc.setPosY((int) Math.random()*100);
				    Canvas.add(mc.getElement());
				    Canvas.revalidate();
				    Canvas.repaint();
				    resizeBackgroundImage();
    		      }

    		      @Override
    		      public void mouseReleased(MouseEvent e) {
    		    	  
    		      }

    		      @Override
    		      public void mouseEntered(MouseEvent e) { }

    		      @Override
    		      public void mouseExited(MouseEvent e) { }
    		      

    		    });
    	    OperatorPanel.add(moon);
    	    
    	    ButtonElement rocketEngine = new ButtonElement(imageRocketEngine);
    	    rocketEngine.setLocation(10, 120);
    	    rocketEngine.setSize(100,100);
    	    rocketEngine.addMouseListener(new MouseListener() {

    		      @Override
    		      public void mouseClicked(MouseEvent e) {        

    		      }

    		      @Override
    		      public void mousePressed(MouseEvent e) {
					BoxElement mc = new BoxElement(imageRocketEngine);
				    //mc.setPosX((int) Math.random()*100);
				   // mc.setPosY((int) Math.random()*100);
				    Canvas.add(mc.getElement());
				    Canvas.revalidate();
				    Canvas.repaint();
				    resizeBackgroundImage();
    		      }

    		      @Override
    		      public void mouseReleased(MouseEvent e) {
    		    	  
    		      }

    		      @Override
    		      public void mouseEntered(MouseEvent e) { }

    		      @Override
    		      public void mouseExited(MouseEvent e) { }
    		      

    		    });
    	    OperatorPanel.add(rocketEngine);
    	    
    Canvas.setSize(new Dimension(500, 500));
    Canvas.setBorder(new LineBorder(Color.BLACK, 1));
    Canvas.setOpaque(false);
    Canvas.setLayout(null);
    splitPaneVertical.add(Canvas,  JSplitPane.RIGHT);
    frame.setSize(900, 600);
    
    ImageIcon image;
		image = new ImageIcon(backgroundImagePath,"");
		image = new ImageIcon(getScaledImage(image.getImage(),(int) Canvas.getSize().getWidth(), (int)Canvas.getHeight()));
         canvasBackground = new JLabel(image);
         canvasBackground.setSize((int) Canvas.getSize().getWidth(), (int)Canvas.getHeight());
         canvasBackground.setLocation(0, 0);
         Canvas.add(canvasBackground); 
    
   // BoxElement mc = new BoxElement(imageRocketEngine);
   // Canvas.add(mc.getElement());
	Canvas.remove(canvasBackground);
	resizeBackgroundImage();
    
  //  mc.getElement().setPosX(185);
   // mc.getElement().setPosY(185);


    //f.pack();
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
    frame.addComponentListener(new ComponentAdapter() 
    {  
            public void componentResized(ComponentEvent evt) {
            	resizeBackgroundImage();
            }
    });
    resizeBackgroundImage();
  }
public static JFrame getFrame() {
	return frame;
}
public static void setFrame(JFrame frame) {
	DrawTest.frame = frame;
}
  
static Image getScaledImage(Image srcImg, int w, int h){
    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = resizedImg.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(srcImg, 0, 0, w, h, null);
    g2.dispose();

    return resizedImg;
} 

public static void resizeBackgroundImage() {
	Canvas.remove(canvasBackground);
    ImageIcon image;
	image = new ImageIcon(backgroundImagePath,"");
	image = new ImageIcon(getScaledImage(image.getImage(),(int) Canvas.getSize().getWidth(), (int)Canvas.getHeight()));
     canvasBackground = new JLabel(image);
     canvasBackground.setSize((int) Canvas.getSize().getWidth(), (int)Canvas.getHeight());
     canvasBackground.setLocation(0, 0);
     Canvas.add(canvasBackground); 
     Canvas.revalidate();
     Canvas.repaint();
}

}