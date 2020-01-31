package GUI.Maps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.plot.XYPlot;

import GUI.BlueBookVisual;
import GUI.FilePaths;

public class MapSetting {
	
	public MapSetting() {
		
	}

	public static void setMap(int TARGET) throws URISyntaxException, IOException{
		final XYPlot plot2 = (XYPlot) BlueBookVisual.mercatorMap.Chart_MercatorMap.getPlot();
		final PolarPlot plot_polar = (PolarPlot) BlueBookVisual.polarMap.chart_PolarMap.getPlot();
		  if (TARGET==0){ 
			  try {
		         BufferedImage myImage = ImageIO.read(new File(FilePaths.MAP_EARTH));
		         plot2.setBackgroundImage(myImage);  
			  } catch(IIOException eIIO) {
				  System.out.println(eIIO);System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if (TARGET==1){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(FilePaths.MAP_MOON));
		         BufferedImage myImage_Polar = ImageIO.read(new File(FilePaths.MAP_SOUTHPOLE_MOON));
		         plot2.setBackgroundImage(myImage);  
		         plot_polar.setBackgroundImage(myImage_Polar);
			  } catch(IIOException eIIO) {
				  System.out.println(eIIO);System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if(TARGET==2){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(FilePaths.MAP_MARS));
		         plot2.setBackgroundImage(myImage); 
			  } catch(IIOException eIIO) {
				  //System.out.println(eIIO);
				  System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if(TARGET==3){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(FilePaths.MAP_VENUS));
		         plot2.setBackgroundImage(myImage); 
		  } catch(IIOException eIIO) {
			  //System.out.println(eIIO);
			  System.out.println("ERROR: Reading maps failed.");
		  }
		  }
	}
	
}
