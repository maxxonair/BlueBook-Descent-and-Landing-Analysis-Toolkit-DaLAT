package Model.Aerodynamic;

import Plotter.DataContainer;
import Plotter.DataSetXY;
import Plotter.Pair;
import Plotter.PlotXY;

public class testScript {
	 static DataContainer dataContainer = new DataContainer();
	
	public static void main(String[] args) {
		double tDeploy=2;
		
		for(double x=0;x<tDeploy+5;x+=0.01) {
		 DataSetXY dataSet =  new DataSetXY();	
			double y = 1/( 1 + Math.pow((x/(tDeploy-x)),-2));	
			dataSet.addPair(new Pair(x, y));
			dataContainer.addDataSet(dataSet);
		}
		
	dataContainer.setxAxisLabel("time");
	dataContainer.setyAxisLabel("value");
	
	PlotXY.plot(dataContainer);
	}
}
