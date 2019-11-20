package Controller;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;

public class FlightController {

	private double Kp=0.8;		// Proportional Coefficient
	private double Ki=0.0001;		// Integrative Coefficient
	private double Kd=3;		// Derivative Coefficient

	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getKp() {
		return Kp;
	}

	public void setKp(double kp) {
		Kp = kp;
	}

	public double getKi() {
		return Ki;
	}

	public void setKi(double ki) {
		Ki = ki;
	}

	public double getKd() {
		return Kd;
	}

	public void setKd(double kd) {
		Kd = kd;
	}
	
	
}
