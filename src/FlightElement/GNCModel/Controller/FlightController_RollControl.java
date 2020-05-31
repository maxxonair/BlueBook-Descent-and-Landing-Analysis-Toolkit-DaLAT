package FlightElement.GNCModel.Controller;

import FlightElement.SpaceShip;


public class FlightController_RollControl extends FlightController {

	private double Kp=5;		// Proportional Coefficient
	private double Ki=0;		// Integrative Coefficient
	private double Kd=0;		// Derivative Coefficient
	public FlightController_RollControl() {
		
	}
	
	@Override
	public void setCommand( SpaceShip spaceShip) {

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
	   	if(Math.toDegrees(spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getPQR()[0][0]) > 5) { // Fast reduce rotational rate
			   double CTRL_ERROR =  spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getPQR()[0][0];
			   	  RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/spaceShip.getProperties().getoBC().getControllerFrequency(), Kp, Ki, Kd, 1, -1);
	   	} else { // Reduce roll angle to 0
			   double CTRL_ERROR =  Math.toDegrees(spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getEulerAngle().roll) ; 
			    RCS_X_CMD   = -  PID_01.PID_001(CTRL_ERROR,1/spaceShip.getProperties().getoBC().getControllerFrequency(), 0.5, 0.001, 2.5, 1, -1);
	   	}	
	   	spaceShip.getgNCModel().getControlCommandSet().setMomentumRCS_X_cmd(RCS_X_CMD);
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
