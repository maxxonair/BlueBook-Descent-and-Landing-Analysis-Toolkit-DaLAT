package GUI.GeometryModel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Canvas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements

	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	/*
    private Color backgroundColor;
    private Color labelColor = Color.BLACK;
    private Color canvasColor = Color.WHITE;
    */
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
     //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 
	 private List<Element> elementList;
    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
 	private double boxGap = 10;
 	private double zeroGap = 100; 
 	
 	
 	
 	private double elementTip;
	
	
	public Canvas() {
		elementList = new ArrayList<Element>();
		this.setLayout(null);
	}
	
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        /**
         * Draw basic canvas elements
         */
        g2d = drawBox(g2d);
        g2d = drawMeasure(g2d);
        g2d = drawCenterline(g2d);
        g2d = drawZeroLine(g2d);
        
        /**
         * 	Draw vehicle elements
         */
        elementTip = 0;
       // g2d = drawCylinder(g2d, 100, 50);
        /*
        for(Element e : elementList) {
	        	if(e.getElementClass()==1) {
	        		g2d = drawCylinder(g2d, e.getDiameter1(), e.getLength());
	        	} else {
	        		g2d = drawCone(g2d, e.getDiameter1(), e.getDiameter2(), e.getLength());
	        	}
        }
        */
        this.removeAll();
        int k=0;
        for(Element e : elementList) {
			int width = this.getWidth();
	        	int height = 	this.getHeight();
	        	int d1 = (int)  e.getDiameter1();
	        	int d2 = (int)  e.getDiameter2();
	        	int length = (int) e.getLength();
	        	int sizeY=0;
	        	if(d1>d2) {sizeY=d1;} else {sizeY=d2;}
	        	
			        JPanel rectangle = new RectanglePanel(k, width, d1, d2 , length);
			        //rectangle.setSize(new Dimension(length,sizeY) );
			        rectangle.setSize(new Dimension(1000,1000) );
			       // rectangle.setLocation((int) (width - boxGap - zeroGap - elementTip), (int) height/2);
			        rectangle.setLocation((int) (width - boxGap - zeroGap - elementTip - length),(int) height/2 - sizeY/2);
			        this.add(rectangle);
			        elementTip += (int) e.getLength();
			        k=k+1;
        }
        g2d.dispose();
    }
    
    
    		
    		public void addElement(double diameter1, double diameter2, double length, int type) {
	    	Element element = new Element();
	    	element.setDiameter1(diameter1);
	   	element.setDiameter2(diameter2);
	    	element.setLength(length);
	    	element.setElementClass(type);
	    	
	    	elementList.add(element);
    }
    
    private Graphics2D drawBox(Graphics2D g2d) {
        double height = 	this.getHeight();
     	double width  = this.getWidth();
        /**
         * 
         * Draw box line 
         */
     	
        g2d.drawLine((int) boxGap, (int) boxGap, (int) (width-boxGap), (int) boxGap); // Top
        g2d.drawLine((int) (width-boxGap), (int) boxGap, (int) (width-boxGap), (int) (height-boxGap)); // Right 
        g2d.drawLine((int) (width-boxGap), (int) (height-boxGap), (int) (boxGap), (int) (height-boxGap)); // Bottom
        g2d.drawLine((int) (boxGap), (int) (height-boxGap), (int) boxGap, (int) boxGap); // Left
        return g2d;
    }
    
    private Graphics2D drawMeasure(Graphics2D g2d) {
       // double height = 	this.getHeight();
     	double width  = this.getWidth();
     	
     	int dl = 8;
     	int leng = 4;
     	int leng2 = 8;
     	int prog =0;
     	int k=0;
     	int labelcount=1;
     	int labelDist = 13;
     	int maxDistance = (int) (width - 2*boxGap - zeroGap);
     	
    	g2d.drawLine((int) boxGap, (int) (2*boxGap), (int) (width-boxGap), (int) (2*boxGap) ); // Top
    	while(prog < maxDistance) {
    		int zeroAt = (int) (width - boxGap - zeroGap);
    		int x = zeroAt - prog;

    		if(k==5) {
    			g2d.drawLine(x, (int) (2*boxGap), x, (int) (2*boxGap+leng2) ); // Long
    			
    			g2d.drawString(""+labelcount, x-3, (int) (2*boxGap+leng2 + labelDist));
    			labelcount++;
    			k=0;
    		} else {
    			g2d.drawLine(x, (int) (2*boxGap), x, (int) (2*boxGap+leng) ); // Short
    		}
    		prog += dl;
    		k++;
    	}
   // 	int minDistance = (int) (width + boxGap + zeroGap);
    	prog = 0;
    	labelcount=1;
    	k=0;
    	while(prog < zeroGap) {
    		int zeroAt = (int) (width - boxGap - zeroGap);
    		int x = (int) (zeroAt + prog);
    		if(k==5) {
    			g2d.drawLine(x, (int) (2*boxGap), x, (int) (2*boxGap+leng2) ); 			 // Long   			
    			g2d.drawString("-"+labelcount, x+2, (int) (2*boxGap+leng2 + labelDist));  // Label 
    			labelcount++;
    			k=0;
    		} else {
    			g2d.drawLine(x, (int) (2*boxGap), x, (int) (2*boxGap+leng) ); // Short
    		}
    		prog += dl;
    		k++;
    	}
    	return g2d;
    }
    
    private Graphics2D drawCenterline(Graphics2D g2d) {
        double height = 	this.getHeight();
     	double width  = this.getWidth();
        /**
         * 
         * Draw center line  
         */
        float[] dash4 = { 21.0f, 9.0f, 3.0f, 9.0f } ;
        BasicStroke centerLineStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 1.0f, dash4, 2f);

        g2d.setStroke(centerLineStroke);
        g2d.drawLine((int) boxGap,(int) height/2, (int) (width-boxGap), (int) height/2);

        return g2d;
    }
    
    private Graphics2D drawZeroLine(Graphics2D g2d) {
        double height = 	this.getHeight();
     	double width  = this.getWidth();
        /**
         * 
         * Draw center line  
         */
        //float[] dash4 = { 0.2f, 0.2f} ;
        float[] dash4 = { 21.0f, 9.0f, 3.0f, 9.0f } ;
        BasicStroke centerLineStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 1.0f, dash4, 2f);

        g2d.setStroke(centerLineStroke);
        int zeroAt = (int) (width-boxGap-zeroGap);
        g2d.drawLine((int) zeroAt,(int) boxGap, (int) zeroAt, (int) (height-boxGap));

        return g2d;
    }
    
    
    class RectanglePanel extends JPanel implements MouseListener{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Polygon polygon;
		
		private int ID ;

        public RectanglePanel(int ID, int masterWidth, int diameter1, int diameter2, int length){
            //Create polygon
        	this.ID = ID;
        	
        	int sizeY=0;
        	if(diameter1>diameter2) {        	
             	polygon = new Polygon();
             	polygon.addPoint(0, (int) 0);				// top left 
             	polygon.addPoint(length, (diameter1 - diameter2)/2);		// top right
             	polygon.addPoint(length,  (diameter1 - diameter2)/2 + diameter2);		// bottom right
             	polygon.addPoint(0,        diameter1);		// bottom left
        	} else {        		
             	polygon = new Polygon();
             	polygon.addPoint(0, (diameter2 - diameter1 )/2);				// top left 
             	polygon.addPoint(length, 0);		// top right
             	polygon.addPoint(length, diameter2);		// bottom right
             	polygon.addPoint(0,   (diameter2 - diameter1 )/2 + diameter1);		// bottom left
        	}
        	

            //Add mouse Listener
            addMouseListener(this);

            //Set size to make sure that the whole triangle is shown
            setPreferredSize(new Dimension(length, sizeY));
        }

        /** Draws the triangle as this frame's painting */
        public void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            g2d.draw(polygon);
        }

        //Required methods for MouseListener, though the only one you care about is click
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}

        /** Called whenever the mouse clicks.
          * Could be replaced with setting the value of a JLabel, etc. */
        public void mouseClicked(MouseEvent e) {
            @SuppressWarnings("unused")
			Point p = e.getPoint();
           // if(polygon.contains(p)) System.out.println("Triangle contains point. ID = "+ ID);
            //else System.out.println("Triangle Doesn't contain point");
            new ElementWindow((int) elementList.get(ID).getElementClass(), ID);
        }
    }
    
    
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

	public List<Element> getElementList() {
		return elementList;
	}

	public void setElementList(List<Element> elementList) {
		this.elementList = elementList;
	}
    
    

}
