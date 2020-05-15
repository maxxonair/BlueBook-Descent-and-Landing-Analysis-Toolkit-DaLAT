package Noise;

import Model.DataSets.AerodynamicSet;
import NoiseSet.AerodynamicNoiseSet;

public class AerodynamicNoiseModel {
	
	private static double dragCoeffNoise 	  = 0;
	private static double liftCoeffNoise 	  = 0;
	private static double sideforceCoeffNoise = 0;
	
	private static double parachuteCoeffNoise = 0;
	
	private static double variation = 0.02;
	
	public AerodynamicNoiseModel() {}
	
	public static void setDragCoeffNoise(AerodynamicSet aerodynamicSet, AerodynamicNoiseSet aerodynamicNoiseSet) {
		double maxVariation =  1 + variation;
		double minVariation =  1 - variation;
		dragCoeffNoise = RandomWalker.randomWalker1D(dragCoeffNoise,maxVariation,-minVariation, 0.001, 0.01);
		aerodynamicNoiseSet.setDragCoeffNoise(dragCoeffNoise);	
		aerodynamicSet.setAerodynamicNoiseSet(aerodynamicNoiseSet);
	}
	
	public static void setLiftCoeffNoise(AerodynamicSet aerodynamicSet, AerodynamicNoiseSet aerodynamicNoiseSet) {
		double maxVariation =  1 + variation;
		double minVariation =  1 - variation;
		liftCoeffNoise = RandomWalker.randomWalker1D(liftCoeffNoise,maxVariation,-minVariation, 0.001, 0.01);
		aerodynamicNoiseSet.setLiftCoeffNoise(liftCoeffNoise);	
		aerodynamicSet.setAerodynamicNoiseSet(aerodynamicNoiseSet);
	}
	
	public static void setsideforceCoeffNoise(AerodynamicSet aerodynamicSet, AerodynamicNoiseSet aerodynamicNoiseSet) {
		double maxVariation =  1 + variation;
		double minVariation =  1 - variation;
		sideforceCoeffNoise = RandomWalker.randomWalker1D(sideforceCoeffNoise,maxVariation,-minVariation, 0.001, 0.01);
		aerodynamicNoiseSet.setSideCoeffNoise(sideforceCoeffNoise);	
		aerodynamicSet.setAerodynamicNoiseSet(aerodynamicNoiseSet);
	}
	
	
	public static void setParachuteCoeffNoise(AerodynamicSet aerodynamicSet, AerodynamicNoiseSet aerodynamicNoiseSet) {
		double maxVariation =  1 + variation;
		double minVariation =  1 - variation;  
		parachuteCoeffNoise = RandomWalker.randomWalker1D(parachuteCoeffNoise,maxVariation,-minVariation, 0.001, 0.01);
		aerodynamicNoiseSet.setParachuteDragCoeffNoise(parachuteCoeffNoise);	
		aerodynamicSet.setAerodynamicNoiseSet(aerodynamicNoiseSet);
	}
	 
}
