package Model;

import Model.DataSets.SensorSet;

public class SensorModel {

	
	
	public static SensorSet addVelocitySensorUncertainty(SensorSet sensorSet, double magnitude) {
	
		double normVariation = Math.random();
		double variation = normVariation * magnitude;

		if(Math.random()>0.5) {
			sensorSet.getRealTimeResultSet().setVelocity(sensorSet.getRealTimeResultSet().getVelocity() + variation);
		} else {
			sensorSet.getRealTimeResultSet().setVelocity(sensorSet.getRealTimeResultSet().getVelocity() - variation);	
		}
		return sensorSet;
	}
	
	
	public static SensorSet addAltitudeSensorUncertainty(SensorSet sensorSet, double magnitude) {
		
		double normVariation = Math.random();
		double variation = normVariation * magnitude;

		if(Math.random()>0.5) {
			sensorSet.getRealTimeResultSet().setAltitude(sensorSet.getRealTimeResultSet().getAltitude() + variation);
		} else {
			sensorSet.getRealTimeResultSet().setAltitude(sensorSet.getRealTimeResultSet().getAltitude() - variation);	
		}
		return sensorSet;
	}
}
