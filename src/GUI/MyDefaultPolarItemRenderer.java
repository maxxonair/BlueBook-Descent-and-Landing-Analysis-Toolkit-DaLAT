package GUI;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYDataset;

public class MyDefaultPolarItemRenderer extends DefaultPolarItemRenderer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void drawSeries(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D dataArea, PlotRenderingInfo info, PolarPlot plot, XYDataset dataset, int seriesIndex) {


        int numPoints = dataset.getItemCount(seriesIndex);
        for (int i = 0; i < numPoints; i++) {

            double theta = dataset.getXValue(seriesIndex, i);
            double radius = dataset.getYValue(seriesIndex, i);
            @SuppressWarnings("deprecation")
			Point p = plot.translateValueThetaRadiusToJava2D(theta, radius,
                    dataArea);
           
            Ellipse2D el = new Ellipse2D.Double(p.x, p.y, 2, 2);
            g2.fill(el);
            g2.draw(el);
        }
    }
}