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
	
	public static SensorSet addIMUGiro(SensorSet sensorSet, double magnitude) {
		
		double normVariation = Math.random();
		double normVariation1 = Math.random();
		double normVariation2 = Math.random();
		double variation = normVariation * magnitude;
		double variation1 = normVariation1 * magnitude;
		double variation2 = normVariation2 * magnitude;
		double isEulerX = Math.toDegrees(sensorSet.getRealTimeResultSet().getEulerX());
		double isEulerY = Math.toDegrees(sensorSet.getRealTimeResultSet().getEulerY());
		double isEulerZ = Math.toDegrees(sensorSet.getRealTimeResultSet().getEulerZ());

		if(Math.random()>0.5) {
			isEulerX += variation;
		} else {
			isEulerX -= variation;	
		}
		if(Math.random()>0.5) {
			isEulerY += variation1;
		} else {
			isEulerY -= variation1;	
		}
		if(Math.random()>0.5) {
			isEulerZ += variation2;
		} else {
			isEulerZ -= variation2;	
		}
		double[][] Euler = {{isEulerX},{isEulerY},{isEulerZ}};
		double[][] quarternions = Toolbox.Mathbox.Euler2Quarternions(Euler);
		sensorSet.getRealTimeResultSet().setQuarternions(quarternions);
		return sensorSet;
	}
}
