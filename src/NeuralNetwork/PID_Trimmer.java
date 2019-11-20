package NeuralNetwork;


import java.io.IOException;

import NeuralNetwork.Network;
import NeuralNetwork.NeuronLayer;
import Plotter.DataContainer;
import Plotter.DataSetXY;
import Plotter.Pair;
import Plotter.PlotXY;

public class PID_Trimmer {

	static long trainingRuns = 500;
	
private  double[][] training_set_inputs = {{0, 0, 1}, {0, 1, 1}, {1, 0, 1}};	
	
private  double[] training_set_outputs = new double[61];


public Network network = new Network();

double[][] targetCurve = new double[61][2];
double tzero=20;
double rateStart=60;

double[][] set = new double[61][2];

static DataContainer dataContainer = new DataContainer();
static DataSetXY dataSet = new DataSetXY();
static DataSetXY[] dataSet2 = new DataSetXY[(int) trainingRuns];
static DataSetXY dataSet3 = new DataSetXY();
static DataSetXY dataSet4 = new DataSetXY();

	public PID_Trimmer(Network network) {
	// Set up neurons
	this.network = network;
	}
	
	private  double sigmoid(double value) {
		return 1 / (1 + Math.exp(-value));
	}
	
	private  double sigmoidDerivative(double value) {
		return value * (1 - value);
	}
	
	private  void createOutput(double[] input, Network network) {

	for(int m=0;m<network.getNumberOfLayers();m++) {

		if(m==0) {
			// Layer 1 
			for(int i=0;i<network.getLayer(m).getNumberOfNeurons();i++) {
				double summedActivation=0;
					for(int j=0;j<input.length;j++) {
						summedActivation += input[j]*network.getLayer(m).getNeuron(i).getWeight(j);
					}
					double output = sigmoid(summedActivation);		
				network.getLayer(m).getNeuron(i).setOutput(output);
			}
		} else {
			// Layer 2,3,4 ... 
			for(int i=0;i<network.getLayer(m).getNumberOfNeurons();i++) {
				double summedActivation=0;
					for(int j=0;j<network.getLayer(m-1).getNumberOfNeurons();j++) {
						summedActivation += network.getLayer(m-1).getNeuron(j).getOutput()*network.getLayer(m).getNeuron(i).getWeight(j);
					}
				double output= sigmoid(summedActivation);		
				network.getLayer(m).getNeuron(i).setOutput(output);
			}
		}
	}
	}

	private  void trainNetwork( long numberOfIterations) {
	
		for(int i=0;i<numberOfIterations;i++) {	

			NeuralRealTimeSimulator simulator = new NeuralRealTimeSimulator();
			double Kp=Math.random()*5;
			double Ki=Math.random()/100;
			double Kd=Math.random()*5;
			System.out.println("Training session: "+i+" | PID: "+Kp+"|"+Ki+"|"+Kd);
			double[] PID = {Kp, Ki, Kd};
			try {
				simulator.run(Kp, Ki, Kd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 set = simulator.getResultSet();

			double cost =0;
			for(int k =0;k<training_set_inputs.length;k++) {	
				//--------------------------------------------------------------------------------------------------
				// Create training set from set of training cases :
				double[] input = PID;
				
				//--------------------------------------------------------------------------------------------------
				// Generate output result: 
				createOutput(input, network);
				dataSet2[i] = new DataSetXY();
				for(int tc=0;tc<60;tc++) {
					targetCurve[tc][0]=tc;
					if(tc<tzero) {
						targetCurve[tc][1] = - rateStart/tzero * tc + rateStart;
					} else {
						targetCurve[tc][1] = 0;
					}
					//dataSet.addPair(new Pair(targetCurve[i][0], targetCurve[i][1]));
					training_set_outputs[tc] = Math.toDegrees(set[tc][1]) - targetCurve[tc][1];
					dataSet2[i].addPair(new Pair(set[tc][0],Math.toDegrees(set[tc][1])));
				}

				//--------------------------------------------------------------------------------------------------
				// Learning process
				// Adapt weights: 
				for(int layerN=network.getNumberOfLayers()-1;layerN>=0;layerN--) {
					if(layerN==network.getNumberOfLayers()-1) {
						for (int l=0;l<network.getLayer(layerN).getNumberOfNeurons();l++) {
							network.getLayer(layerN).getNeuron(l).setError(training_set_outputs[k] - network.getLayer(layerN).getNeuron(l).getOutput());				
							network.getLayer(layerN).getNeuron(l).setDelta(network.getLayer(layerN).getNeuron(l).getError() * sigmoidDerivative(network.getLayer(layerN).getNeuron(l).getOutput()));
								for(int m=0;m<network.getLayer(layerN).getNumberOfInputs();m++) {
									double layer2Adjustment = network.getLayer(layerN).getNeuron(l).getDelta() * network.getLayer(layerN-1).getNeuron(m).getOutput();
									network.getLayer(layerN).getNeuron(l).setWeight(m, network.getLayer(layerN).getNeuron(l).getWeight(m)+layer2Adjustment);
								}
						}
					} else {
						for (int l=0;l<network.getLayer(layerN).getNumberOfNeurons();l++) {
							double layerError=0;
							for(int o=0;o<network.getLayer(layerN+1).getNumberOfNeurons();o++){
							 layerError = network.getLayer(layerN+1).getNeuron(o).getDelta() * network.getLayer(layerN+1).getNeuron(o).getWeight(l);
							}
							
							network.getLayer(layerN).getNeuron(l).setError(layerError);
							 	 
							 network.getLayer(layerN).getNeuron(l).setDelta(network.getLayer(layerN).getNeuron(l).getError() * sigmoidDerivative( network.getLayer(layerN).getNeuron(l).getOutput() ));
							for(int m=0;m<network.getLayer(layerN).getNumberOfInputs();m++) {
								try {
								double layer1Adjustment = input[m] * network.getLayer(layerN).getNeuron(l).getDelta();
								network.getLayer(layerN).getNeuron(l).setWeight(m, network.getLayer(layerN).getNeuron(l).getWeight(m) + layer1Adjustment);
								} catch (ArrayIndexOutOfBoundsException e ) {
									System.out.println(""+layerN+"|"+l+"|"+m);
								}
								}
						}
					}
				}
				//--------------------------------------------------------------------------------------------------	
				for(int cf=0;cf<network.getLayer(network.getNumberOfLayers()-1).getNumberOfNeurons();cf++) {
				double costInt = Math.sqrt((network.getLayer(network.getNumberOfLayers()-1).getNeuron(cf).getOutput() - training_set_outputs[cf]) * (network.getLayer(network.getNumberOfLayers()-1).getNeuron(cf).getOutput() - training_set_outputs[cf]));
				cost += costInt;
				}
			}
			//System.out.println(cost);	
			dataSet.addPair(new Pair(i, cost));
		}
	}

	public static void main(String[] args) {
		
		Network network = new Network();
		int numberOfHiddenLayerNeurons=20;
		network.addLayer(new NeuronLayer(numberOfHiddenLayerNeurons, 3));
		//network.addLayer(new NeuronLayer(numberOfHiddenLayerNeurons, numberOfHiddenLayerNeurons));
		network.addLayer(new NeuronLayer(60, numberOfHiddenLayerNeurons));
		
		PID_Trimmer pidTrimmer = new PID_Trimmer(network);
	
		
		
		long startTime   = System.nanoTime();
		
		pidTrimmer.trainNetwork( trainingRuns ) ;
		
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		double totalTime_sec = (double) (totalTime * 1E-6);  
		/*
		for(int i=0;i<=60;i++) {
			dataSet3.addPair(new Pair(targetCurve[i][0], Math.toDegrees(pidTrimmer.set[i][1]) - targetCurve[i][1]));
		}
		*/
	    System.out.println(""+trainingRuns+" training runs. Runtime: "+(int) totalTime_sec+" milliseconds.");
		dataContainer.addDataSet(dataSet);
		//dataContainer.addDataSet(dataSet2);
		//dataContainer.addDataSet(dataSet3);
		dataContainer.setxAxisLabel("Trainings");
		dataContainer.setyAxisLabel("Fitness");
		//PlotXY fitnessPlot = new PlotXY();
	    //fitnessPlot.plot(dataContainer);
	     DataContainer dataContainer2 = new DataContainer();
	    for(int i=0;i<20;i++) {
	    	dataContainer2.addDataSet(dataSet2[i]);
	    }
	    //PlotXY overviewPlot = new PlotXY();
	   // overviewPlot.plot(dataContainer2);
	    //-----------------------------------------------------------------------------------------
	    double[] PID = {5, 0.0001, 3};
	    pidTrimmer.createOutput(PID, pidTrimmer.network);
			//dataSet.addPair(new Pair(targetCurve[i][0], targetCurve[i][1]));
	    
	    NeuralRealTimeSimulator simulator = new NeuralRealTimeSimulator();
		try {
			simulator.run(PID[0], PID[1], PID[2]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pidTrimmer.set = simulator.getResultSet();
	    
	    double[][] resultSetTest = new double[61][2];
	    for(int i=0;i<pidTrimmer.network.getLayer(pidTrimmer.network.getNumberOfLayers()-1).getNumberOfNeurons();i++) {
	    	resultSetTest[i][0] = i;
	    	resultSetTest[i][1] = pidTrimmer.network.getLayer(pidTrimmer.network.getNumberOfLayers()-1).getNeuron(i).getOutput();
	    	dataSet3.addPair(new Pair(resultSetTest[i][0],Math.toDegrees(resultSetTest[i][1])));
	    	dataSet4.addPair(new Pair(pidTrimmer.set[i][0],Math.toDegrees(pidTrimmer.set[i][1])));
	    }
	    DataContainer dataContainerx = new DataContainer();
	    dataContainerx.addDataSet(dataSet3);
	    dataContainerx.addDataSet(dataSet4);
	    PlotXY overviewPlot = new PlotXY();
	    overviewPlot.plot(dataContainerx);
		
	}

}
