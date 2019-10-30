package Noise;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

public class NoiseTester {
    public static Color labelColor = new Color(220,220,220);    					// Label Color
   	public static Color backgroundColor = new Color(41,41,41);				    // Background Color
   	public static Color valueColor =  new Color(65,105,225);
   	public static Color valueColor2 =  new Color(255,140,0);
	
	public static void main(String[] args) throws IOException {
		DefaultTableXYDataset dataSet = new DefaultTableXYDataset();
		//for(float k=0.05f;k<0.3f;k+=0.05f) {
		XYSeries xyseries11 = new XYSeries("", false, false); 
			for(int i=0; i<100;i++) {
				double value = (1 + PerlinNoise.PerlinNoise1D(i, 0, 1))/2;
				xyseries11.add(i, value);
				System.out.println(i+"|"+value);
			}
		XYSeries xyseries2 = new XYSeries("", false, false); 
			for(int i=0; i<100;i++) {
				double value = Math.random();
				xyseries2.add(i, value);
				System.out.println(i+"|"+value);
		//	}
		dataSet.addSeries(xyseries11); 
		dataSet.addSeries(xyseries2); 
		}
		//-----------------------------------------------------------------------------------
        JFrame.setDefaultLookAndFeelDecorated(false);
        JFrame MAIN_frame = new JFrame("");
        JPanel chartPanel = createChartPanel(dataSet);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 600));
        MAIN_frame.add(chartPanel, BorderLayout.CENTER);
        MAIN_frame.pack();
        MAIN_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MAIN_frame.setLocationRelativeTo(null);
        MAIN_frame.setVisible(true);
	}
	
	
	public static JPanel createChartPanel(DefaultTableXYDataset dataSet) throws IOException {
		
		JFreeChart chart = ChartFactory.createStackedXYAreaChart("", "", "", dataSet);//("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity, PlotOrientation.VERTICAL, true, false, false); 
		XYPlot plot = (XYPlot)chart.getXYPlot(); 
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    plot.setRenderer(0, renderer); 
	    plot.setRenderer(1, renderer); 
	    plot.setRenderer(2, renderer); 
	    renderer.setSeriesPaint( 0 , labelColor );	
	    renderer.setSeriesPaint( 1 , valueColor );	
	    renderer.setSeriesPaint( 2 , valueColor2 );	
	    chart.setBackgroundPaint(backgroundColor);
		Font font3 = new Font("Dialog", Font.PLAIN, 12); 	
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelPaint(labelColor);
		plot.getDomainAxis().setLabelPaint(labelColor);
		plot.setForegroundAlpha(0.8f);
		plot.setBackgroundPaint(backgroundColor);
		plot.setDomainGridlinePaint(labelColor);
		plot.setRangeGridlinePaint(labelColor); 
		//chart.removeLegend();
		//chart.getLegend().setBackgroundPaint(backgroundColor);
		//chart.getLegend().setItemPaint(labelColor);;
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

		rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
		//final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		//domainAxis.setInverted(true);
		
		//Shape cross = ShapeUtilities.createDiagonalCross(1, 1) ;
	    double size = 2.0;
	    double size2 = 1.0;
	    double delta = size / 2.0;
	    double delta2 = size2 / 2.0;
		Shape dot1 = new Ellipse2D.Double(-delta, -delta, size, size);
		Shape dot2 = new Ellipse2D.Double(-delta2, -delta2, size2, size2);
		//renderer.setSeriesShape(0, dot1);
		//renderer.setSeriesShape(1, dot2);
		
		JPanel plotPanel = new JPanel();
		plotPanel.setLayout(new BorderLayout());
		plotPanel.setPreferredSize(new Dimension(900, 900));
		plotPanel.setBackground(backgroundColor);
	
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setMaximumDrawHeight(50000);
		chartPanel.setMaximumDrawWidth(50000);
		chartPanel.setMinimumDrawHeight(0);
		chartPanel.setMinimumDrawWidth(0);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setPreferredSize(new Dimension(900, 900));

		plotPanel.add(chartPanel,BorderLayout.CENTER);
		return plotPanel;
	}

}
