package Simulation.Noise;

import java.util.Random;

public class RandomWalker {
	
	private Random generator;
	
	public RandomWalker(long seed) {
		
		generator = new Random(seed);
		
	}
	
	
	public  double randomWalker1D(double value, double upperLimit, double lowerLimit, double change) {
		
		value = getWalker( value,  change, 0.5);
		
		if(value>upperLimit) {
			value=upperLimit;
		} else if(value<lowerLimit) {
			value=lowerLimit;
		}
		return value;
	}
	
	public double randomWalker1D(double value, double upperLimit, double lowerLimit, double change, double randomVariation) {
		change += generator.nextDouble()*randomVariation;

		value = getWalker( value,  change, 0.5);
		
		if(value>upperLimit) {
			value=upperLimit;
		} else if(value<lowerLimit) {
			value=lowerLimit;
		}
		
		return value;
	}
	
	public double randomWalker1D(double value, double upperLimit, double lowerLimit, double change, double randomVariation, double percentage) {
		change += generator.nextDouble()*randomVariation;

		value = getWalker( value,  change, percentage);
		
		if(value>upperLimit) {
			value=upperLimit;
		} else if(value<lowerLimit) {
			value=lowerLimit;
		}
		
		return value;
	}
	
	public  double randomWalker1D(double value, double upperLimit, double lowerLimit, double change, double randomVariation, double percentage, double stepVariation) {
		change += generator.nextDouble()*randomVariation;

		if(generator.nextDouble()>0.5) {
			percentage=0.5+stepVariation;
		} else {
			percentage=0.5-stepVariation;
		}
		value = getWalker( value,  change, percentage);
		
		if(value>upperLimit) {
			value=upperLimit;
		} else if(value<lowerLimit) {
			value=lowerLimit;
		}
		
		return value;
	}
	
	private double getWalker(double value, double change, double percentage) {
		if(generator.nextDouble()>percentage) {
			value+=change;
		} else {
			value-=change;
		}
		return value;
	}

}
