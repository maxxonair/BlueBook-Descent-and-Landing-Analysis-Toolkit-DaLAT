package Simulation.Noise;

import Simulation.Model.DataSets.AtmosphereSet;
import Simulation.NoiseSet.AtmosphereNoiseSet;

public class AtmosphereNoiseModel {
	
	private static double densityNoise=(3 * Math.random())-1.5;
	private static double staticTemperatureNoise=0;
	
	public static void setDensityNoise(AtmosphereSet atmosphereSet, double altitude) {
		AtmosphereNoiseSet atmosphereNoiseSet = atmosphereSet.getAtmosphereNoiseSet();
		double maximumExpectedVariation = 1.5;
		double altitudeAtMaximumExptectedVariation=160000;
		double expectedRandomVariation = createSymmetricHyperbolic(maximumExpectedVariation, altitudeAtMaximumExptectedVariation, altitude) ;
		 densityNoise = RandomWalker.randomWalker1D(densityNoise,expectedRandomVariation,-expectedRandomVariation, 0.03*expectedRandomVariation, 0.008);
		atmosphereNoiseSet.setDensityNoise(densityNoise);	
		atmosphereSet.setAtmosphereNoiseSet(atmosphereNoiseSet);
	}
	
	public static void setStaticTemperatureNoise(AtmosphereSet atmosphereSet, double altitude) {
		AtmosphereNoiseSet atmosphereNoiseSet = atmosphereSet.getAtmosphereNoiseSet();
		double altitudeAtMaximumExptectedVariation=160000;
		double expectedRandomVariation = createSymmetricLinear(atmosphereNoiseSet.getStaticTemperatureMaximumExpectedVariation(), altitudeAtMaximumExptectedVariation, altitude) ;
		staticTemperatureNoise = RandomWalker.randomWalker1D(staticTemperatureNoise,expectedRandomVariation,-expectedRandomVariation, 0.005*expectedRandomVariation, 0.005);
		atmosphereNoiseSet.setStaticTemperatureNoise(staticTemperatureNoise);	
		atmosphereSet.setAtmosphereNoiseSet(atmosphereNoiseSet);
	}


private static double createSymmetricHyperbolic(double percentualMaximum, double RefAlt, double altitude ) {
	double expectedRandomVariation = (percentualMaximum/(RefAlt * RefAlt)) 
			  *  altitude * altitude ;
	return expectedRandomVariation;
}

private static double createSymmetricLinear(double percentualMaximum, double RefAlt, double altitude ) {
	double expectedRandomVariation = (percentualMaximum/(RefAlt)) * altitude ;
	return expectedRandomVariation;
}

}
