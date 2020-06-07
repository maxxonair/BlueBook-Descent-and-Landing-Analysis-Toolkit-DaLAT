package GUI.Maps;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

import GUI.BlueBookVisual;
import GUI.FilePaths;


public class MercatorMap {
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements
	private JPanel mercatorMapPanel;
	private JPanel mapPanel;
	//private JLabel INDICATOR_PageMap_LAT,INDICATOR_PageMap_LONG;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
    @SuppressWarnings("unused")
	private Color labelColor = Color.BLACK;
    
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	Font labelfont_small       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
	
	Font tickMarkerFont       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
	Font axisLabelFont       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	
    DecimalFormat df_X4 		  = new DecimalFormat("#####.###");
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:	  
    public JFreeChart Chart_MercatorMap;
	//-------------------------------------------------------------------------------------------------------------
    // Panel Strings:

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	public static double PI    = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862;
    private static Crosshair xCrosshair_x;
    private static Crosshair yCrosshair_x; 
    private XYSeriesCollection ResultSet_MercatorMap = new XYSeriesCollection();
    
    
	public MercatorMap() {
		
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
		mercatorMapPanel = new JPanel();
		mercatorMapPanel.setLayout(new BorderLayout());
		
		mapPanel = new JPanel();
		mapPanel.setLayout(new BorderLayout());
		mercatorMapPanel.add(mapPanel, BorderLayout.CENTER);
		/*
		JPanel SouthPanel = new JPanel();
		SouthPanel.setLayout(null);
		//mainPanelh1.setLocation(0, 0);
		SouthPanel.setBackground(backgroundColor);
		SouthPanel.setForeground(labelColor);
		SouthPanel.setPreferredSize(new Dimension(1200, 120));
		mercatorMapPanel.add(SouthPanel, BorderLayout.SOUTH);
	    
        int uy2 = 10; 

       
        JLabel LABEL_PageMapLONG = new JLabel("Longitude [deg]");
        LABEL_PageMapLONG.setLocation(425, uy2 + 0 );
        LABEL_PageMapLONG.setSize(250, 20);
        LABEL_PageMapLONG.setBackground(backgroundColor);
        LABEL_PageMapLONG.setForeground(labelColor);
        SouthPanel.add(LABEL_PageMapLONG);
        JLabel LABEL_PageMapLAT = new JLabel("Latitude [deg]");
        LABEL_PageMapLAT.setLocation(825, uy2 + 0 );
        LABEL_PageMapLAT.setSize(250, 20);
        LABEL_PageMapLAT.setBackground(backgroundColor);
        LABEL_PageMapLAT.setForeground(labelColor);
        SouthPanel.add(LABEL_PageMapLAT);	
        
         INDICATOR_PageMap_LONG = new JLabel();
        INDICATOR_PageMap_LONG.setLocation(425, uy2 + 30 );
        INDICATOR_PageMap_LONG.setText("");
        INDICATOR_PageMap_LONG.setForeground(labelColor);
        INDICATOR_PageMap_LONG.setSize(80, 20);
        SouthPanel.add(INDICATOR_PageMap_LONG);
         INDICATOR_PageMap_LAT = new JLabel();
        INDICATOR_PageMap_LAT.setLocation(825, uy2 + 30 );
        INDICATOR_PageMap_LAT.setText("");
        INDICATOR_PageMap_LAT.setForeground(labelColor);
        INDICATOR_PageMap_LAT.setSize(80, 20);
        SouthPanel.add(INDICATOR_PageMap_LAT);
        */
	}
	
	public void CreateChart_MercatorMap() throws IOException{
		 try {
		        ResultSet_MercatorMap = AddDataset_Mercator_MAP(); 
		        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {
		        	System.out.println(" Error read for plot X40");
		        }

		        Chart_MercatorMap = ChartFactory.createScatterPlot("", "Longitude [deg]", "Latitude [deg] ", ResultSet_MercatorMap, PlotOrientation.VERTICAL, false, false, false); 
				XYPlot plot = (XYPlot)Chart_MercatorMap.getXYPlot(); 
		        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
		        
			    double size = 2.0;
			    double delta = size / 2.0;
				Shape dot = new Ellipse2D.Double(-delta, -delta, size, size);
				renderer.setSeriesShape(0, dot);
				renderer.setSeriesLinesVisible(0, false);
		        plot.setRenderer(0, renderer);
		        
		        Chart_MercatorMap.setBackgroundPaint(backgroundColor);
				
		        plot.getDomainAxis().setLabelFont(axisLabelFont);
		        plot.getDomainAxis().setTickLabelFont(tickMarkerFont);
		        plot.getDomainAxis().setLabelPaint(labelColor);
		        plot.getDomainAxis().setTickLabelPaint(labelColor);
		        plot.getDomainAxis().setAxisLinePaint(labelColor);
		        plot.getDomainAxis().setTickMarkPaint(labelColor);
		        
		        plot.getRangeAxis().setLabelFont(axisLabelFont);
		        plot.getRangeAxis().setTickLabelFont(tickMarkerFont);
		        plot.getRangeAxis().setLabelPaint(labelColor);
		        plot.getRangeAxis().setTickLabelPaint(labelColor);
		        plot.getRangeAxis().setAxisLinePaint(labelColor);
		        plot.getRangeAxis().setTickMarkPaint(labelColor);
		        
		       final XYPlot plot2 = (XYPlot) Chart_MercatorMap.getPlot();
		       plot2.setForegroundAlpha(0.5f);
		       plot2.setBackgroundPaint(backgroundColor);
		       plot2.setDomainGridlinePaint(Color.black);
		       plot2.setRangeGridlinePaint(new Color(220,220,220));

		       ValueAxis domain2 = plot.getDomainAxis();
		       domain2.setRange(-180, 180);
		       domain2.setInverted(false);
		       // change the auto tick unit selection to integer units only...
		       final NumberAxis rangeAxis2 = (NumberAxis) plot2.getRangeAxis();
		       rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		       rangeAxis2.setRange(-90, 90);
		       ChartPanel CPXX4 = new ChartPanel(Chart_MercatorMap);
		       CPXX4.setBackground(backgroundColor);
		       CPXX4.setDomainZoomable(false);
		       CPXX4.setRangeZoomable(false);
		       CPXX4.setMaximumDrawHeight(50000);
		       CPXX4.setMaximumDrawWidth(50000);
		       CPXX4.setMinimumDrawHeight(0);
		       CPXX4.setMinimumDrawWidth(0);
				//CP2.setMouseWheelEnabled(tru
		       CPXX4.addChartMouseListener(new ChartMouseListener() {
		           @Override
		           public void chartMouseClicked(ChartMouseEvent event) {
		        	   //Rectangle2D dataArea2 = CPXX4.getScreenDataArea();
		              // Point2D p = CPXX4.translateScreenToJava2D(event.getTrigger().getPoint());
		               //double x = Chart_MercatorMap.getXYPlot().getDomainAxis().java2DToValue(event.getTrigger().getX(), dataArea2, RectangleEdge.BOTTOM);
		               //double y = plot2.getRangeAxis().java2DToValue(p.getY(), dataArea2, plot2.getRangeAxisEdge());
		               //INDICATOR_PageMap_LONG.setText("" + df_X4.format(x));
		              // INDICATOR_PageMap_LAT.setText("" + df_X4.format(y));
		           }

		           @Override
		           public void chartMouseMoved(ChartMouseEvent event) {
		        	   Rectangle2D dataArea2 = CPXX4.getScreenDataArea();
		        	   Point2D p = CPXX4.translateScreenToJava2D(event.getTrigger().getPoint());
		               ValueAxis xAxis2 = Chart_MercatorMap.getXYPlot().getDomainAxis();
		               double x = xAxis2.java2DToValue(event.getTrigger().getX(), dataArea2, RectangleEdge.BOTTOM);
		              // double y = yAxis2.java2DToValue(event.getTrigger().getYOnScreen(), dataArea3, RectangleEdge.BOTTOM);
		               double y = plot2.getRangeAxis().java2DToValue(p.getY(), dataArea2, plot2.getRangeAxisEdge());
		               MercatorMap.xCrosshair_x.setValue(x);
		               MercatorMap.yCrosshair_x.setValue(y);
		           }
		   });
		       CrosshairOverlay crosshairOverlay2 = new CrosshairOverlay();
		       xCrosshair_x = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		       xCrosshair_x.setLabelVisible(true);
		       yCrosshair_x = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		       yCrosshair_x.setLabelVisible(true);
		       crosshairOverlay2.addDomainCrosshair(xCrosshair_x);
		       crosshairOverlay2.addRangeCrosshair(yCrosshair_x);
		       CPXX4.addOverlay(crosshairOverlay2);
		       CPXX4.setPreferredSize(new Dimension(1300, 660));
		       mapPanel.add(CPXX4, BorderLayout.CENTER);	
	}
	
	public void update() {
	    	ResultSet_MercatorMap.removeAllSeries();
	    	try {
	    	ResultSet_MercatorMap = AddDataset_Mercator_MAP();
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {}
	}
	
	public  XYSeriesCollection AddDataset_Mercator_MAP() throws IOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
       	XYSeries xyseries10 = new XYSeries("", false, true); 

            FileInputStream fstream = null;
            		try{ fstream = new FileInputStream(FilePaths.RES_File);} catch(IOException eIO) { System.out.println(eIO);}
                  DataInputStream in = new DataInputStream(fstream);
                  BufferedReader br = new BufferedReader(new InputStreamReader(in));
                  String strLine;
                  try {
                  while ((strLine = br.readLine()) != null )   {
		           String[] tokens = strLine.split(" ");
		           double y = Double.parseDouble(tokens[2])*180/PI;  // Latitude [deg[
		           double x = Double.parseDouble(tokens[1])*180/PI;	 // Longitude [deg]
		           while (x>180 || x<-180 || y>90 || y<-90){
		           if (x>180){
		        	   x=x-360;
		           } else if (x<-180){
		        	   x=x+360;
		           }
		           if (y>90){
		        	   y=y-180;
		           } else if (y<-90){
		        	   y=y+180;
		           }
		           }
		           //System.out.println(x + " | " + y);
		         	xyseries10.add(x,y);
		           }
           in.close();
           br.close();
        ResultSet_MercatorMap.addSeries(xyseries10); 
                  } catch(NullPointerException eNPI) { System.out.print(eNPI); }
        return ResultSet_MercatorMap;          
       }
	/**
	 * Unit Tester
	 * @param main
	 * @throws IOException 
	 */
	public static void main(String[] main) throws IOException {
	    JFrame frame = new JFrame("Center Panel Right Setting Test ");
	    frame.setLayout(new BorderLayout());
	    
		MercatorMap map = new MercatorMap();
		map.CreateChart_MercatorMap();
		
		frame.add(map.getMercatorMapPanel(), BorderLayout.CENTER);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}

	public JPanel getMercatorMapPanel() {
		return mercatorMapPanel;
	}

	
}
