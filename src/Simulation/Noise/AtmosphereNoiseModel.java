package Simulation.Noise;

import Simulation.Model.DataSets.AtmosphereSet;
import Simulation.NoiseSet.AtmosphereNoiseSet;

public class AtmosphereNoiseModel {
	
	private static double densityNoise=(3 * Math.random())-1.5;
	private static double staticTemperatureNoise=0;
	RandomWalker walker ;
	
	public AtmosphereNoiseModel(long seed) {
		walker = new RandomWalker(seed);
	}
	
	
	public void setDensityNoise(AtmosphereSet atmosphereSet, double altitude) {
		AtmosphereNoiseSet atmosphereNoiseSet = atmosphereSet.getAtmosphereNoiseSet();
		double maximumExpectedVariation = 1.5;
		double altitudeAtMaximumExptectedVariation=160000;
		double expectedRandomVariation = createSymmetricHyperbolic(maximumExpectedVariation, altitudeAtMaximumExptectedVariation, altitude) ;
		 densityNoise = walker.randomWalker1D(densityNoise,expectedRandomVariation,-expectedRandomVariation, 0.03*expectedRandomVariation, 0.008);
		atmosphereNoiseSet.setDensityNoise(densityNoise);	
		atmosphereSet.setAtmosphereNoiseSet(atmosphereNoiseSet);
	}
	
	public void setStaticTemperatureNoise(AtmosphereSet atmosphereSet, double altitude) {
		AtmosphereNoiseSet atmosphereNoiseSet = atmosphereSet.getAtmosphereNoiseSet();
		double altitudeAtMaximumExptectedVariation=160000;
		double expectedRandomVariation = createSymmetricLinear(atmosphereNoiseSet.getStaticTemperatureMaximumExpectedVariation(), altitudeAtMaximumExptectedVariation, altitude) ;
		staticTemperatureNoise = walker.randomWalker1D(staticTemperatureNoise,expectedRandomVariation,-expectedRandomVariation, 0.005*expectedRandomVariation, 0.005);
		atmosphereNoiseSet.setStaticTemperatureNoise(staticTemperatureNoise);	
		atmosphereSet.setAtmosphereNoiseSet(atmosphereNoiseSet);
	}


private  double createSymmetricHyperbolic(double percentualMaximum, double RefAlt, double altitude ) {
	double expectedRandomVariation = (percentualMaximum/(RefAlt * RefAlt)) 
			  *  altitude * altitude ;
	return expectedRandomVariation;
}

private  double createSymmetricLinear(double percentualMaximum, double RefAlt, double altitude ) {
	double expectedRandomVariation = (percentualMaximum/(RefAlt)) * altitude ;
	return expectedRandomVariation;
}

}
