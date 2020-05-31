package FlightElement.GNCModel.Controller;

import FlightElement.SpaceShip;

public class FlightController_PitchControl extends FlightController {
	
	private double Kp=9.8;		// Proportional Coefficient
	private double Ki=0.0001;		// Integrative Coefficient
	private double Kd=5;		// Derivative Coefficient
	
	PID pitch;
	
	public FlightController_PitchControl() {
		 pitch =  new PID(0.8, 0.001,3,-1, 1);
	}
	
	@Override
	public void setCommand( SpaceShip spaceShip) {

		double RCS_Y_CMD =0;
		/**
		 *  Setup: 
		 *  Fast reduce pitch rate -> then switch to reduce pitch angle
		 * 
		 * 
		 */
	   	if(Math.toDegrees( spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getPQR()[1][0] ) > 5) { 
			   double CTRL_ERROR =  spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getPQR()[1][0];
			   double response = -  PID_01.PID_001(CTRL_ERROR,1/spaceShip.getProperties().getoBC().getControllerFrequency(), Kp, Ki, Kd, 1, -1);
			   if(Double.isNaN(response)) {
				   response =0;
				   System.err.println("Cntrl error > returned NaN");
			   }
			   RCS_Y_CMD = response;
	   	} else { // pitch angle to 0
			   double CTRL_ERROR =  spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getEulerAngle().pitch ;
			   double response  = -  PID_01.PID_001(CTRL_ERROR,1/spaceShip.getProperties().getoBC().getControllerFrequency(), pitch.P , pitch.I , pitch.D , pitch.max, pitch.min);
			   if(Double.isNaN(response)) {
				   response =0;
				   System.err.println("Cntrl error > returned NaN");
			   }
			   RCS_Y_CMD = response;
	   	}	
	   	spaceShip.getgNCModel().getControlCommandSet().setMomentumRCS_Y_cmd(RCS_Y_CMD);
		
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
