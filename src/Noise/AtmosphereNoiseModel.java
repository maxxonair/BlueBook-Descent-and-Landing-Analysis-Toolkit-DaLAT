package Noise;

import NoiseSet.AtmosphereNoiseSet;

public class AtmosphereNoiseModel {
	
	private static double densityNoise=0;
	private static double staticTemperatureNoise=0;
	
	public static void setDensityNoise(AtmosphereNoiseSet atmosphereNoiseSet) {
		 densityNoise = RandomWalker.randomWalker1D(densityNoise,0.03,-0.03, 0.0008, 0.005);
		atmosphereNoiseSet.setDensityNoise(densityNoise);	
	}
	
	public static void setStaticTemperatureNoise(AtmosphereNoiseSet atmosphereNoiseSet) {
		staticTemperatureNoise = RandomWalker.randomWalker1D(staticTemperatureNoise,0.03,-0.03, 0.0008, 0.005);
		atmosphereNoiseSet.setStaticTemperatureNoise(staticTemperatureNoise);	
	}

}
