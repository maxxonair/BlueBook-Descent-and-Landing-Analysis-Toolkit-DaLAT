package GUI.Maps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import GUI.MyDefaultPolarItemRenderer;


public class PolarMap {
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements
	private JPanel polarMapPanel;
	private JPanel PolarMapContainer ;
    private  XYSeriesCollection ResultSet_PolarMap ;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
    @SuppressWarnings("unused")
	private Color labelColor = Color.BLACK;
    
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	Font labelfont_small       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	public JFreeChart chart_PolarMap;	  
	//-------------------------------------------------------------------------------------------------------------
    // Panel Strings:

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	public static double PI    = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862;
	
	public PolarMap() {
		polarMapPanel = new JPanel();
		polarMapPanel.setBackground(BlueBookVisual.getBackgroundColor());
		
		ResultSet_PolarMap = new XYSeriesCollection();
		
	       PolarMapContainer = new JPanel(new GridBagLayout());
	       PolarMapContainer.setPreferredSize(new Dimension(BlueBookVisual.extx_main, BlueBookVisual.exty_main-100));
	       PolarMapContainer.setBackground(backgroundColor);
	       polarMapPanel.add(PolarMapContainer, BorderLayout.CENTER);
	}

	public XYSeriesCollection AddDataset_Polar_MAP() throws IOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
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
		           
		           while (x>360 || x<0 || y>90 || y<-90){
				                  if (x > 360){x=x-360;
				           } else if (x <   0){x=x+360; }
				           
				                  if (y>90){ y=y-180;
				           } else if (y<-90){ y=y+180;}
		           }
		           //System.out.println(x + " | " + y);
		         	xyseries10.add(x,y);
		           }
           in.close();
           br.close();
           ResultSet_PolarMap.addSeries(xyseries10); 
                  } catch(NullPointerException eNPI) { System.out.print(eNPI); }
        return ResultSet_PolarMap;          
       }
	
	public void CreateChart_PolarMap() throws IOException {
		ResultSet_PolarMap.removeAllSeries();
        try {
        	ResultSet_PolarMap = AddDataset_Polar_MAP(); 
        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {
        	System.out.println(" Error read for plot X40");
        }

        chart_PolarMap = ChartFactory.createPolarChart("", ResultSet_PolarMap, false, false, false);
  
		PolarPlot plot =  (PolarPlot) chart_PolarMap.getPlot();
		MyDefaultPolarItemRenderer renderer = new MyDefaultPolarItemRenderer();
		renderer.setSeriesFillPaint(0, Color.red);
        plot.setRenderer(0, renderer);
	
		chart_PolarMap.setBackgroundPaint(Color.white);

		
        plot.getAxis().setLabelFont(labelfont_small);
   
       ValueAxis domain2 = plot.getAxis();
       domain2.setRange(-90, -70);
       domain2.setInverted(false);
       domain2.setTickLabelPaint(Color.white);
       plot.setAngleGridlinePaint(Color.white);
       plot.setAngleLabelPaint(Color.white);
       // change the auto tick unit selection to integer units only...
       
       ChartPanel CPXX4 = new ChartPanel(chart_PolarMap);
       CPXX4.setBackground(backgroundColor);
       CPXX4.setLayout(new BorderLayout());
       CPXX4.setDomainZoomable(false);
       CPXX4.setRangeZoomable(false);
       CPXX4.setPreferredSize(new Dimension(800, 800));
       CPXX4.setMaximumDrawHeight(50000);
       CPXX4.setMaximumDrawWidth(50000);
       CPXX4.setMinimumDrawHeight(0);
       CPXX4.setMinimumDrawWidth(0);
       JPanel innerPanel = new JPanel();
       innerPanel.setLayout(new BorderLayout());
       innerPanel.setPreferredSize(new Dimension(800, 800));
       innerPanel.add(CPXX4, BorderLayout.CENTER);
       PolarMapContainer.add(innerPanel);
       PolarMapContainer.addComponentListener(new ComponentAdapter() {
           @Override
           public void componentResized(ComponentEvent e) {
        	   KeepAspectRatio_Map(innerPanel, PolarMapContainer);
           }
       });
	}
	
	/**
	 * Unit Tester 
	 * @param main
	 */
	public static void main(String[] main) {
	    JFrame frame = new JFrame("Polar Map  Setting Test ");
	    frame.setLayout(new BorderLayout());
	    
		PolarMap map = new PolarMap();
     	try {
			map.CreateChart_PolarMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	try {
			MapSetting.setMap(1);
		} catch (Exception exp) {
				System.err.println(exp);
		}
		frame.add(map.getPolarMapPanel(), BorderLayout.CENTER);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}

	public JPanel getPolarMapPanel() {
		return polarMapPanel;
	}
	
	private static void KeepAspectRatio_Map(JPanel innerPanel, JPanel container) {
        int w = container.getWidth();
        int h = container.getHeight();
        int size =  Math.min(w, h);
        innerPanel.setPreferredSize(new Dimension(size, size));
        container.revalidate();
    }
}
