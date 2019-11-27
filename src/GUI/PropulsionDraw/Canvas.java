package GUI.PropulsionDraw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.PropulsionDraw.Relationship;


public class Canvas extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private List<Relationship> relationships;
	private List<BoxElement> canvasElements;
	
	private Point backgroundLocation = new Point();
	
	private Image backgroundImage;
	
	private static String backgroundImagePath = "images/propulsionElements/blueprintBackground.png";
	
	static Font smallFont  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	
	public Canvas() {
		relationships = new ArrayList<>();
		canvasElements = new ArrayList<>();
	    
	    ImageIcon imageIn;
			imageIn = new ImageIcon(backgroundImagePath,"");
		Image	image = getScaledImage(imageIn.getImage(),(int) getSize().getWidth(), (int) getHeight());
	         setBackgroundImage(image);
	         	/*
				BoxElement Tank = new BoxElement("Tank", imageTankBasic, this);
			    add(Tank.getElement());
			    addCanvasElement(Tank);
				BoxElement Tank2 = new BoxElement("Tank2", imageTankBasic, this);
			    add(Tank2.getElement());
			    addCanvasElement(Tank2);
			    Tank.getElement().setLocation(50, 10);
			    Tank2.getElement().setLocation(150, 10);
				BoxElement Engine2 = new BoxElement("Engine", imageRocketEngine, this);	
			    add(Engine2.getElement());
			    addCanvasElement(Engine2);
			    Engine2.getElement().setLocation(100, 200);
			    relate(Tank.getElement(), Engine2.getElement());
			    //relate(Tank2.getElement(), Engine2.getElement());
	         	 */
        setPreferredSize(new Dimension(500,500));
	}
	
    protected List<BoxElement> getShapes() {
        ArrayList<BoxElement> shapes = new ArrayList<>();
       // shapes.addAll(mainEngineElements);
        return shapes;
    }
    
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        if (getBackgroundImage() != null) {
            g.drawImage(getBackgroundImage(), backgroundLocation.x, backgroundLocation.y, this);
        }

        for (Relationship relationship : relationships) {

            Point2D p1 = new Point2D.Double(relationship.getParent().getBounds().getCenterX(), relationship.getParent().getBounds().getCenterY());
            Point2D p2 = new Point2D.Double(relationship.getChild().getBounds().getCenterX(), relationship.getChild().getBounds().getCenterY());
            g2.setStroke(new BasicStroke(5));
            g2.setColor(Color.WHITE);
            Line2D line = new Line2D.Float(p1, p2);
            
            g2.draw(line);

        }


        g2.dispose();
    }
    
    public void addTankElement(String name, String iconFilePath, int x, int y) {
		BoxElement Tank = new BoxElement(name, iconFilePath, this);;
	    add(Tank.getElement());
	    addCanvasElement(Tank);
		Tank.getElement().setLocation(x, y);
		revalidate();
	    repaint();
    }
    
    public void addTankElement(BoxElement Tank, int x, int y) {
	    add(Tank.getElement());
	    addCanvasElement(Tank);
		Tank.getElement().setLocation(x, y);
		revalidate();
	    repaint();
    }
    
    public void addMainEngine(String name, String iconFilePath, int x, int y) {
		BoxElement Engine = new BoxElement("Engine", iconFilePath, this);	
	    add(Engine.getElement());
	    addCanvasElement(Engine);
	    Engine.getElement().setLocation(x, y);
		revalidate();
	    repaint();
    }
    
    public void addMainEngine(BoxElement Engine, int x, int y) {	
	    add(Engine.getElement());
	    addCanvasElement(Engine);
	    Engine.getElement().setLocation(x, y);
		revalidate();
	    repaint();
    }
    
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        repaint();
    }
    
    public Point getBackgroundLocation() {
        return backgroundLocation;
    }

    public void setBackgroundLocation(Point backgroundLocation) {
        this.backgroundLocation = backgroundLocation;
        repaint();
    }
    
	
	public void addCanvasElement(BoxElement element) {
		canvasElements.add(element);
	    repaint();
	}
	
	public List<BoxElement> getCanvasElements() {
		return canvasElements;
	}
	
	protected void relate(JComponent parent, JComponent child) {
	    relationships.add(new Relationship(parent, child));
	}
	
	public void setRelationships(List<Relationship> relationships) {
		this.relationships = relationships;
	}

	public List<Relationship> getRelationships() {
		return relationships;
	}

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame f = new JFrame();
                f.add(new Canvas());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }
    
    public void resizeBackgroundImage() {
	    ImageIcon imageIn;
			imageIn = new ImageIcon(backgroundImagePath,"");
		Image	image = getScaledImage(imageIn.getImage(),(int) getSize().getWidth(), (int) getHeight());
	         setBackgroundImage(image);
         revalidate();
         repaint();
    }
    
    
    static Image getScaledImage(Image srcImg, int w, int h){
    	try {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    	} catch (IllegalArgumentException e) {
    		//System.out.println("ERROR/getScaledImage > Illegal Argument");
    		return srcImg;
    	}
    } 
    


}
