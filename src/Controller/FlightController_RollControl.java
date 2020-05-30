package Controller;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;


public class FlightController_RollControl extends FlightController {

	private double Kp=5;		// Proportional Coefficient
	private double Ki=0;		// Integrative Coefficient
	private double Kd=0;		// Derivative Coefficient
	public FlightController_RollControl() {
		
	}
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {

		double RCS_X_CMD =0;
		/* 		Original (tuned) setup 
		   	if(Math.toDegrees(sensorSet.getRealTimeResultSet().getPQR()[0][0]) > 10) { // Fast reduce rotational rate
				   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getPQR()[0][0];
				   	  RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 5, 0.0001, 3, 1, -1);
		   	} else { // Reduce roll angle to 0
				   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getEulerX() ; 
				    RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.8, 0.0001, 3, 1, -1);
		   	}
		*/
	   	if(Math.toDegrees(sensorSet.getRealTimeResultSet().getPQR()[0][0]) > 5) { // Fast reduce rotational rate
			   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getPQR()[0][0];
			   	  RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, Kp, Ki, Kd, 1, -1);
	   	} else { // Reduce roll angle to 0
			   double CTRL_ERROR =  Math.toDegrees(sensorSet.getRealTimeResultSet().getEulerAngle().roll) ; 
			    RCS_X_CMD   = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.5, 0.001, 2.5, 1, -1);
	   	}	
		   	 controlCommandSet.setMomentumRCS_X_cmd(RCS_X_CMD);
		    return controlCommandSet;
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
