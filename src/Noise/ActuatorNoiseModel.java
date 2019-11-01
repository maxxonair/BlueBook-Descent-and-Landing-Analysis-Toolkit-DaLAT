package Noise;

import NoiseSet.ActuatorNoiseSet;

public class ActuatorNoiseModel {
	
	private static double MainThrustNoise=0;
	private static double RCSXNoise=0;
	private static double RCSYNoise=0;
	private static double RCSZNoise=0;
	
	
	public static void setPrimaryThrustNoise(ActuatorNoiseSet actuatorNoiseSet) {
		MainThrustNoise = RandomWalker.randomWalker1D(MainThrustNoise,0.01,-0.01, 0.0005, 0.00);
		actuatorNoiseSet.setPrimaryThrustNoise(MainThrustNoise);
	}
	
	public static void setRCSXNoise(ActuatorNoiseSet actuatorNoiseSet) {
		RCSXNoise = RandomWalker.randomWalker1D(RCSXNoise,0.05,-0.05, 0.008, 0.005);
		actuatorNoiseSet.setRCSMomentumX(RCSXNoise);
	}
	
	public static void setRCSYNoise(ActuatorNoiseSet actuatorNoiseSet) {
		RCSYNoise = RandomWalker.randomWalker1D(RCSYNoise,0.05,-0.05, 0.008, 0.005);
		actuatorNoiseSet.setRCSMomentumY(RCSYNoise);
	}
	
	public static void setRCSZNoise(ActuatorNoiseSet actuatorNoiseSet) {
		RCSZNoise = RandomWalker.randomWalker1D(RCSXNoise,0.05,-0.05, 0.008, 0.005);
		actuatorNoiseSet.setRCSMomentumZ(RCSZNoise);
	}

}
