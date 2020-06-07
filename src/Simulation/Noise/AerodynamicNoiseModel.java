package Simulation.Noise;

import Simulation.NoiseSet.AerodynamicNoiseSet;

public class AerodynamicNoiseModel {
	
	private  double dragCoeffNoise 	  = 1;  // Noise = 1 -> no noise
	private  double liftCoeffNoise 	  = 1;
	private  double sideforceCoeffNoise = 1;
	
	private  double parachuteCoeffNoise = 1;
	
	private  double variation = 0.025;
	private double stepSize = 0.0001;
	private double randomStepSizeVariation = 0.0007;
	
	private  AerodynamicNoiseSet aerodynamicNoiseSet = new AerodynamicNoiseSet();
	
	public AerodynamicNoiseModel(AerodynamicNoiseSet aerodynamicNoiseSet) {
		this.aerodynamicNoiseSet = aerodynamicNoiseSet;
	}
	
	public  void setDragCoeffNoise() {
		double maxVariation =  1 + variation;
		double minVariation =  1 - variation;
		dragCoeffNoise = RandomWalker.randomWalker1D(dragCoeffNoise,maxVariation,minVariation, stepSize, randomStepSizeVariation);
		aerodynamicNoiseSet.setDragCoeffNoise(dragCoeffNoise);	
	}
	
	public  void setLiftCoeffNoise() {
		double maxVariation =  1 + variation;
		double minVariation =  1 - variation;
		liftCoeffNoise = RandomWalker.randomWalker1D(liftCoeffNoise,maxVariation,minVariation, stepSize, randomStepSizeVariation);
		aerodynamicNoiseSet.setLiftCoeffNoise(liftCoeffNoise);	
	}
	
	public  void setsideforceCoeffNoise() {
		double maxVariation =  1 + variation;
		double minVariation =  1 - variation;
		sideforceCoeffNoise = RandomWalker.randomWalker1D(sideforceCoeffNoise,maxVariation,minVariation, stepSize, randomStepSizeVariation);
		aerodynamicNoiseSet.setSideCoeffNoise(sideforceCoeffNoise);	
	}
	
	
	public  void setParachuteCoeffNoise() {
		double maxVariation =  1 + variation;
		double minVariation =  1 - variation;  
		parachuteCoeffNoise = RandomWalker.randomWalker1D(parachuteCoeffNoise,maxVariation,minVariation, stepSize, randomStepSizeVariation);
		aerodynamicNoiseSet.setParachuteDragCoeffNoise(parachuteCoeffNoise);	
	}

	public  AerodynamicNoiseSet getAerodynamicNoiseSet() {
		return aerodynamicNoiseSet;
	}
	
	 
}
