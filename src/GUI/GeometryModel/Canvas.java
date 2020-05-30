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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JPanel;

import GUI.BlueBookVisual;
import GUI.FilePaths;


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
    static DecimalFormat indicatorFormat =  new DecimalFormat("##.####");
    
    
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
 	
 	private int scaleFactor = 10;
 	private int unit = scaleFactor*10;
 	
 	private String[] strUnits = {"[m]", "[mm]"};
 	private int unitSetting=0;
 	
    double CoP =0;
    double CoM =0;
    double CoT =0;
	
	
	public Canvas() {
		elementList = new ArrayList<Element>();
		this.setLayout(null);
		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// TODO Auto-generated method stub
				e.getUnitsToScroll();
				int  scale = scaleFactor + e.getUnitsToScroll();
				if(scale <2) { scale =2;}
				setScaleFactor( scale ); 
			}
			
		});

	}
	
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        /**
         * Draw basic canvas elements
         */
        g2d = drawBox(g2d);
        g2d = drawMeasureHorizontal(g2d);
        g2d = drawMeasureVertical(g2d);
        g2d = drawCenterline(g2d);
        g2d = drawZeroLine(g2d);
        
        /**
         * 	Draw vehicle elements
         */
        elementTip = 0;

        this.removeAll();

        int k=0;
        for(Element e : elementList) {
			int width = this.getWidth();
	        	int height = 	this.getHeight();
	        	int sizeY=0;

	        	int d1 = (int) (e.getDiameter1() * unit);
	        	int d2 = (int) (e.getDiameter2() * unit);
	        	int length 	  = (int) (e.getLength() * unit);
	        	if(d1>d2) {sizeY=d1;} else {sizeY=d2;}
	        	
			        JPanel rectangle = new RectanglePanel(k, this, width, e.getDiameter1(), e.getDiameter2() , e.getLength());
			        //rectangle.setSize(new Dimension(length,sizeY) );
			        rectangle.setSize(new Dimension(1000,1000) );
			       // rectangle.setLocation((int) (width - boxGap - zeroGap - elementTip), (int) height/2);
			        rectangle.setLocation((int) (width - boxGap - zeroGap - elementTip - length),(int) height/2 - sizeY/2);
			        this.add(rectangle);
			        elementTip += (int) (((double) e.getLength())*unit);
			        k=k+1;
        }
        
        g2d = drawStats(g2d);
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
    
    private Graphics2D drawMeasureHorizontal(Graphics2D g2d) {
     	double width  = this.getWidth();
     	int lineCor = 0;
     	
     	int leng = 4;		// Length short marker  [px]
     	int leng1 = 7;		// Length middle marker [px]
     	int leng2 = 12;		// Length main marker   [px]
     	
     	int prog =0; 		// running position to relative zero
     	int k=0;
     	int labelcount=1;
     	int labelDist = 13;	// Distance label to marker [px] 
     	int maxDistance = (int) (width - 2*boxGap - zeroGap);
     	
    	g2d.drawString("0", (int) (width - boxGap - zeroGap)-3, (int) (boxGap+leng2 + labelDist)); 	// Zero marker
    	g2d.drawString(""+strUnits[unitSetting], (int) (2*boxGap), (int) (5*boxGap)); // unit marker
    	g2d.setStroke(new BasicStroke(lineCor+1));
    	while(prog < maxDistance) {
    		int zeroAt = (int) (width - boxGap - zeroGap);	// relative zero
    		int x = zeroAt - prog;

    		if(k==5) {
    			g2d.drawLine(x, (int) (boxGap), x, (int) (boxGap+leng1) ); // mid long
    			
    		} else if (k==10){
    			g2d.drawLine(x, (int) (boxGap), x, (int) (boxGap+leng2) ); // mid long
    			g2d.drawString(""+labelcount, x-3, (int) (boxGap+leng2 + labelDist));
    			labelcount++;
    			k=0;
    		} else {
    			g2d.drawLine(x, (int) (boxGap), x, (int) (boxGap+leng) ); // Short
    		}
    		prog += (scaleFactor-lineCor);
    		k++;
    	}

    	prog = 0;
    	labelcount=1;
    	k=0;
    	while(prog < zeroGap) {
    		int zeroAt = (int) (width - boxGap - zeroGap);
    		int x = (int) (zeroAt + prog);
    		if(k==5) {
    			g2d.drawLine(x, (int) (boxGap), x, (int) (boxGap+leng1) ); 			 // Long   			
    		} else if (k==10){
    			g2d.drawLine(x, (int) (boxGap), x, (int) (boxGap+leng2) ); 
    			g2d.drawString("-"+labelcount, x+2, (int) (boxGap+leng2 + labelDist));  // Label 
    			labelcount++;
    			k=0;
    		} else {
    			g2d.drawLine(x, (int) (boxGap), x, (int) (boxGap+leng) ); // Short
    		}
    		prog += (scaleFactor-lineCor);
    		k++;
    	}
    	return g2d;
    }
    
    private Graphics2D drawMeasureVertical(Graphics2D g2d) {
     	int leng = 4;
     	int leng1 = 7;
     	int leng2 = 12;
     	double height  = this.getHeight();
     	double width  = this.getWidth();
     	int k =0;
     	int labelcount=1;
     	int prog =(int) (height/2);
     	int maxDistance = (int) (height - boxGap);
    	g2d.drawString("0", (int) (width-2*boxGap), (int) (height/2)); 	// Zero marker

    	while(prog < maxDistance) {
    		int x = (int) (width-boxGap);

    		if(k==5) {
    			g2d.drawLine(x, prog, x-leng1, prog ); // mid long
    			
    		} else if (k==10){
    			g2d.drawLine(x, prog, x-leng2, prog) ; // mid long
	    			if(labelcount<10) {
	    				g2d.drawString(""+labelcount, x-20, prog);
	    			} else {
	    				g2d.drawString(""+labelcount, x-40, prog);
	    			}
    			labelcount++;
    			k=0;
    		} else {
    			g2d.drawLine(x, prog, x-leng, prog ); // Short
    		}
    		prog += (scaleFactor);
    		k++;
    	}
    	
    	prog = (int) (height/2);
    	labelcount=1;
    	k=0;
    	while(prog > boxGap) {
    		int x = (int) (width-boxGap);

    		if(k==5) {
    			g2d.drawLine(x, prog, x-leng1, prog ); // mid long
    			
    		} else if (k==10){
    			g2d.drawLine(x, prog, x-leng2, prog) ; // mid long
	    			if(labelcount<10) {
	    				g2d.drawString(""+labelcount, x-20, prog);
	    			} else {
	    				g2d.drawString(""+labelcount, x-40, prog);
	    			}
    			labelcount++;
    			k=0;
    		} else {
    			g2d.drawLine(x, prog, x-leng, prog ); // Short
    		}
    		prog -= (scaleFactor);
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
    
    private Graphics2D drawStats(Graphics2D g2d) {
    	
		int width = this.getWidth();
		int height = 	this.getHeight();
		
        double maxLength =0;
        double maxDiameter =0;
       //double stability=0;
        
        int k=0;
        
        /**
         * CoP variables
         */
        double Atotal =0;		// Total cross section area
        double li=0;				// length unit element i
        double Ai=0;				// Cross section area of element i
        double di=0;				// distance (x) to CoP of element i
        double sum =0;			// developing sum of (li*di)*Ai
        
        for(Element e : elementList) {
	        	maxLength += e.getLength(); 
	        	if(k==0) {
	        		if(e.getDiameter1()>e.getDiameter2()) {
	        			maxDiameter=e.getDiameter1();
	        		} else {
	        			maxDiameter=e.getDiameter2();} 
	        	} else if (e.getDiameter1() > e.getDiameter2() && e.getDiameter1() > maxDiameter){
	        		maxDiameter = e.getDiameter1();
	        	} else if (e.getDiameter1() < e.getDiameter2() && e.getDiameter2() > maxDiameter){
	        		maxDiameter = e.getDiameter2();
	        	}
	        	/**
	        	 *  CoP calc 
	        	 */
	        	if(e.getElementClass()==1) { //-> Cone
	        		di = e.getLength()/2;
	        		Ai = (di*e.getDiameter1());
	        	} else { // Frustum
	        		Ai = e.getLength() * (e.getDiameter1() + e.getDiameter2()) / 2;
	        		double dmax=0;
	        		double dmin=0;
	        		double diTriangle=0;
		        		if(e.getDiameter1()>e.getDiameter2()) {
		        			dmax = e.getDiameter1();
		        			dmin = e.getDiameter2();
		        			 diTriangle = 2 * e.getLength() / 3;
		        			 //System.out.println("case 1");
		        		} else {
		        			dmax = e.getDiameter2();
		        			dmin = e.getDiameter1();
		        			 diTriangle = 1 * e.getLength() / 3;
		        			 //System.out.println("case 2");
		        		}
        			double diAiCone =  e.getLength() * e.getLength() * dmin / 2;
        			double Atriangle = (dmax-dmin)*e.getLength() / 2;
        			di =   ( 2 * diTriangle * Atriangle + diAiCone) / Ai;
        			//System.out.println(indicatorFormat.format(diTriangle)+"|"+Atriangle);
	        	}
	        	sum += (li+di)*Ai;
	        	Atotal += Ai;
	        	li += e.getLength();
	        	//-------------------------------------------------------------
			        k=k+1;
        }
        CoP = sum/Atotal;
        int x = (int) (boxGap + 10);
        int y = height - 100;
        GeometryFrame.setDiameter(maxDiameter);
        GeometryFrame.setLength(maxLength);
        g2d.drawString("Diameter: "+indicatorFormat.format(maxDiameter)+" "+strUnits[unitSetting], x, y); 		// Diameter 
        y = height - 80;
        g2d.drawString("Length: "+indicatorFormat.format(maxLength)+" "+strUnits[unitSetting], x, y); 			// Length
        y = height - 60;
        g2d.drawString("CoP: " + indicatorFormat.format(CoP)+" "+strUnits[unitSetting], x, y); 					// Center of Pressure 
        y = height - 40;
        g2d.drawString("CoM: " + indicatorFormat.format(CoM)+" "+strUnits[unitSetting], x, y); 					// Center of Mass 
        y = height - 20;
        g2d.drawString("CoT: "+indicatorFormat.format(CoT)+" "+strUnits[unitSetting], x, y); 								// Stability
        int diameter=10;
        x = (int) (width - zeroGap - boxGap - (CoP*unit) - diameter/2);
        GeometryFrame.setCoP(CoP);
        y = height/2-diameter/2;
        
        g2d.setColor(Color.RED);
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, diameter, diameter);
        g2d.fill(circle);
        y = height - 60- diameter;
        x = (int) (boxGap);
        Ellipse2D.Double circle2 = new Ellipse2D.Double(x, y, diameter, diameter);
        g2d.fill(circle2);
        
        g2d.setColor(Color.BLACK);
        y = height - 40- diameter;
        x = (int) (boxGap);
        Ellipse2D.Double circle3 = new Ellipse2D.Double(x, y, diameter, diameter);
        g2d.fill(circle3);
        x = (int) (width - zeroGap - boxGap - (CoM*unit) - diameter/2);
        y = height/2-diameter/2;
        Ellipse2D.Double circle5 = new Ellipse2D.Double(x, y, diameter, diameter);
        
        g2d.fill(circle5);
        g2d.setColor(Color.BLUE);
        y = height - 20- diameter;
        x = (int) (boxGap);
        Ellipse2D.Double circle6 = new Ellipse2D.Double(x, y, diameter, diameter);
        g2d.fill(circle6);
        x = (int) (width - zeroGap - boxGap - (CoT*unit) - diameter/2);
        y = height/2-diameter/2;
        Ellipse2D.Double circle7 = new Ellipse2D.Double(x, y, diameter, diameter);
        g2d.fill(circle7);
        g2d.setColor(Color.BLACK);
    	return g2d; 
    }
    
    class RectanglePanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Polygon polygon;
		
		//private int ID ;

        public RectanglePanel(int ID, Canvas canvas, int masterWidth, double d1, double d2, double len){
            //Create polygon
       // 	this.ID = ID;

        	int diameter1 = (int) ((double) (d1 * unit));
        	int diameter2 = (int) ((double) (d2 * unit));
        	int length 	  = (int) ((double) (len * unit));
        	
        	int sizeY=0;
        	if(diameter1>diameter2) { 
        		sizeY=diameter1;
             	polygon = new Polygon();
             	polygon.addPoint(0, (int) 0);										// top left 
             	polygon.addPoint(length, (diameter1 - diameter2)/2);					// top right
             	polygon.addPoint(length,  (diameter1 - diameter2)/2 + diameter2); 	// bottom right
             	polygon.addPoint(0,        diameter1);		// bottom left
        	} else {   
        		sizeY = diameter2;
             	polygon = new Polygon();
             	polygon.addPoint(0, (diameter2 - diameter1 )/2);				// top left 
             	polygon.addPoint(length, 0);		// top right
             	polygon.addPoint(length, diameter2);		// bottom right
             	polygon.addPoint(0,   (diameter2 - diameter1 )/2 + diameter1);		// bottom left
        	}
        	

            //Add mouse Listener
            addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
		            @SuppressWarnings("unused")
					Point  pClick = e.getPoint();
		           // if(polygon.contains(p)) System.out.println("Triangle contains point. ID = "+ ID);
		            //else System.out.println("Triangle Doesn't contain point");
		            new ElementWindow((int) elementList.get(ID).getElementClass(), ID);
					
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {}

				@Override
				public void mouseExited(MouseEvent arg0) {}

				@Override
				public void mousePressed(MouseEvent arg0) {}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					repaint();
					canvas.repaint();	
				}
				
            	
            });


            //Set size to make sure that the whole triangle is shown
            setPreferredSize(new Dimension(length, sizeY));
        }

     
        public void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            g2d.draw(polygon);
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
		writeElementList();
	}
    
    public void setScaleFactor(int factor) {
    		this.scaleFactor = factor;
    		unit=scaleFactor*10;
    		repaint();
    }

	public int getUnit() {
		return unit;
	}

	public void setUnitSetting(int unitSetting) {
		this.unitSetting = unitSetting;
		repaint();
	}
	
	

	public int getUnitSetting() {
		return unitSetting;
	}

	public String[] getStrUnits() {
		return strUnits;
	}

	public double getCoP() {
		return CoP;
	}


	public double getCoM() {
		return CoM;
	}

	public void setCoM(double coM) {
		CoM = coM;
		repaint();
	}

	public double getCoT() {
		return CoT;
	}

	public void setCoT(double coPr) {
		CoT = coPr;
		repaint();
	}
	
	private void writeElementList() {

        try {
            File fac = new File(FilePaths.elementList);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }

            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i< elementList.size(); i++)
            {
            	int eClass = (int) elementList.get(i).getElementClass();
            	if(eClass ==1) {
            	wr.write(elementList.get(i).getElementClass()+
            			BlueBookVisual.BB_delimiter+
            			elementList.get(i).getLength()+
            			BlueBookVisual.BB_delimiter+
            			elementList.get(i).getDiameter1()+
            			BlueBookVisual.BB_delimiter+
            			elementList.get(i).getDiameter1()+
            			System.getProperty( "line.separator" ));
            	} else if (eClass == 2) {
                	wr.write(elementList.get(i).getElementClass()+
                			BlueBookVisual.BB_delimiter+
                			elementList.get(i).getLength()+
                			BlueBookVisual.BB_delimiter+
                			elementList.get(i).getDiameter1()+
                			BlueBookVisual.BB_delimiter+
                			elementList.get(i).getDiameter2()+
                			System.getProperty( "line.separator" ));
            	}
		            }               
            wr.close();
            } catch (IOException eIO) {
            //	System.out.println(eIO);
            }
	}
	
	public void readElementList() {
		List<Element> elementList = new ArrayList<>();
		double maxDiam=0;
		double maxLength=0;
	   	 BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(FilePaths.elementList));
	   	 String strLine;
			      while ((strLine = br.readLine()) != null )   {
			      	String[] tokens = strLine.split(" ");
			      	Element element = new Element();
			       double eClass   = Double.parseDouble(tokens[0]);
			       if(eClass == 1) {
				    	   element.setElementClass(1);
				    	   element.setLength(Double.parseDouble(tokens[1]));
				    	   element.setDiameter1(Double.parseDouble(tokens[2]));
				    	   element.setDiameter2(Double.parseDouble(tokens[3]));
			       } else if (eClass == 2) {
				    	   element.setElementClass(2);
				    	   element.setLength(Double.parseDouble(tokens[1]));
				    	   element.setDiameter1(Double.parseDouble(tokens[2]));
				    	   element.setDiameter2(Double.parseDouble(tokens[3]));
			       }
			    	   maxDiam = compDiam(maxDiam, element.getDiameter1());
			    	   maxDiam = compDiam(maxDiam, element.getDiameter2());
			    	   maxLength += element.getLength();
			       elementList.add(element);
			
			      }
	 
			      br.close();
		} catch (Exception excp) {
			//excp.printStackTrace();
		} 
		GeometryFrame.setDiameter(maxDiam);
		GeometryFrame.setLength(maxLength);
	 this.elementList = elementList;
	 repaint();
	}
	
	private double compDiam(double ref, double comp) {
		if(comp>ref) {
			return comp;
		} else {
			return ref;
		}
	}

}
