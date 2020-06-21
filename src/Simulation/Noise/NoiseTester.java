package Simulation.Noise;

import utils.Plotter.DataContainer;
import utils.Plotter.DataSetXY;
import utils.Plotter.Pair;
import utils.Plotter.PlotXY;

public class NoiseTester {

	
	@SuppressWarnings("unused")
	public static void main(String[] args)  {
		DataContainer dataContainer = new DataContainer();
		dataContainer.setTitle("Noise Test Mark1");
		dataContainer.setxAxisLabel("Steps");
		
		/*
		DataSetXY dataSet = new DataSetXY();
     	double currentValue =0.0;
		for(int i=0; i<100;i++) {
			double value = RandomWalker.randomWalker1D(currentValue,0.1,-0.1, 0.002, 0.00000, 0.45);
			currentValue = value;
			dataSet.addPair(new Pair(i, value));
			dataSet.setName("Walker Test 1");
		}
		dataContainer.addDataSet(dataSet);
*/
		@SuppressWarnings("unused")
		DataSetXY muSet =  new DataSetXY();
		for(int k=0;k<2;k++) {
		DataSetXY set2 =  new DataSetXY();
	    double currentValue =Math.random()/5-0.1;
	    @SuppressWarnings("unused")
		double upDown = Math.random()/10;
	    @SuppressWarnings("unused")
		double sumValues=0;
	    double mu = 0;
	    double sigma=0;
	    double sumSigma=0;
	    RandomWalker walker = new RandomWalker(5);
				for(int i=0; i<500;i++) {
					
					double value = walker.randomWalker1D(currentValue,0.1,-0.1, 0.002, 0.00000, 0.0, 0);
					currentValue = value;
					set2.addPair(new Pair(i, value));
					
					/*
					sumValues+=value;
					mu=sumValues/(i+1);
					sumSigma+=(value-mu)*(value-mu);
					if(i>1) {
					sigma=Math.sqrt(1/(i-1)*sumSigma);
					}
					*/
					//set2.setName(""+i+" Mu="+mu+" , Sigma="+sigma);
				}
		//muSet.addPair(new Pair(k, mu));
		dataContainer.addDataSet(set2);
		}
		PlotXY plot = new PlotXY();
		plot.plot(dataContainer);
	}
	

}
