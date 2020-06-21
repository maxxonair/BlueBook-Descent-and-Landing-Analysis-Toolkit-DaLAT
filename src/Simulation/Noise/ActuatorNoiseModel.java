package Simulation.Noise;

import Simulation.NoiseSet.ActuatorNoiseSet;

public class ActuatorNoiseModel {
	
	private  double MainThrustNoise=0;
	private  double RCSXNoise=0;
	private  double RCSYNoise=0;
	private  double RCSZNoise=0;
	
	private RandomWalker walker ;
	
	public ActuatorNoiseModel(long seed) {
		walker = new RandomWalker(seed);
	}
	
	
	public  void setPrimaryThrustNoise(ActuatorNoiseSet actuatorNoiseSet) {
		MainThrustNoise = walker.randomWalker1D(MainThrustNoise,0.01,-0.01, 0.0005, 0.00);
		actuatorNoiseSet.setPrimaryThrustNoise(MainThrustNoise);
	}
	
	public  void setRCSXNoise(ActuatorNoiseSet actuatorNoiseSet) {
		RCSXNoise = walker.randomWalker1D(RCSXNoise,0.007,-0.007, 0.005, 0.005);
		actuatorNoiseSet.setRCSMomentumX(RCSXNoise);
	}
	
	public  void setRCSYNoise(ActuatorNoiseSet actuatorNoiseSet) {
		RCSYNoise = walker.randomWalker1D(RCSYNoise,0.01,-0.01, 0.008, 0.005);
		actuatorNoiseSet.setRCSMomentumY(RCSYNoise);
	}
	
	public  void setRCSZNoise(ActuatorNoiseSet actuatorNoiseSet) {
		RCSZNoise = walker.randomWalker1D(RCSXNoise,0.01,-0.01, 0.008, 0.005);
		actuatorNoiseSet.setRCSMomentumZ(RCSZNoise);
	}

}
