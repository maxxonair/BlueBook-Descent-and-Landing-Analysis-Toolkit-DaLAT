package FlightElement.GNCModel.Controller;

import FlightElement.SpaceShip;

public class FlightController {

	private double Kp=0.8;		// Proportional Coefficient
	private double Ki=0.0001;		// Integrative Coefficient
	private double Kd=3;		// Derivative Coefficient

	public void setCommand( SpaceShip spaceShip) {
		// TODO Auto-generated method stub
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
