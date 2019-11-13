package Model.Aerodynamic;

import Plotter.DataContainer;
import Plotter.DataSetXY;
import Plotter.Pair;
import Plotter.PlotXY;

public class testScript {
	 static DataContainer dataContainer = new DataContainer();
	
	public static void main(String[] args) {
		double timeToThrustLevel=2;
		double OldThrustLevel = 0.36;
		double CMDThrustLevel = 0.73;
		for(double timeSinceCMD=0;timeSinceCMD<timeToThrustLevel;timeSinceCMD+=0.01) {
		 DataSetXY dataSet =  new DataSetXY();	
			double y=0;
			double x=0;
			if(OldThrustLevel>CMDThrustLevel) {
				double amplitude =	OldThrustLevel - CMDThrustLevel;
				 x = timeToThrustLevel - timeSinceCMD;
				 y = OldThrustLevel - amplitude/( 1 + Math.pow((x/(timeSinceCMD)),-2)) ;
			} else {
				double amplitude =	CMDThrustLevel - OldThrustLevel ;
				 x = timeSinceCMD;
				 y = OldThrustLevel + amplitude/( 1 + Math.pow((x/(timeToThrustLevel-x)),-2));	
			}
			dataSet.addPair(new Pair(x, y));
			dataContainer.addDataSet(dataSet);
		}
		
	dataContainer.setxAxisLabel("time");
	dataContainer.setyAxisLabel("value");
	
	PlotXY.plot(dataContainer);
	}
}
