package Noise;

public class RandomWalker {
	
	
	public static double randomWalker1D(double value, double upperLimit, double lowerLimit, double change) {
		
		value = getWalker( value,  change, 0.5);
		
		if(value>upperLimit) {
			value=upperLimit;
		} else if(value<lowerLimit) {
			value=lowerLimit;
		}
		return value;
	}
	
	public static double randomWalker1D(double value, double upperLimit, double lowerLimit, double change, double randomVariation) {
		change += Math.random()*randomVariation;

		value = getWalker( value,  change, 0.5);
		
		if(value>upperLimit) {
			value=upperLimit;
		} else if(value<lowerLimit) {
			value=lowerLimit;
		}
		
		return value;
	}
	
	public static double randomWalker1D(double value, double upperLimit, double lowerLimit, double change, double randomVariation, double percentage) {
		change += Math.random()*randomVariation;

		value = getWalker( value,  change, percentage);
		
		if(value>upperLimit) {
			value=upperLimit;
		} else if(value<lowerLimit) {
			value=lowerLimit;
		}
		
		return value;
	}
	
	private static double getWalker(double value, double change, double percentage) {
		if(Math.random()>percentage) {
			value+=change;
		} else {
			value-=change;
		}
		return value;
	}

}
