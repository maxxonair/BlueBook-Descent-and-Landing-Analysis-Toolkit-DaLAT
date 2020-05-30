package FlightElement.GNCModel.Controller;

import FlightElement.SpaceShip;
import FlightElement.GNCModel.ControlCommandSet;
import Model.DataSets.SensorSet;

public class FlightController_DescentController extends FlightController{
	
	public FlightController_DescentController() {
		
	}
	
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		
		// Roll Control 
		//controlCommandSet = addRollControl( controlCommandSet, sensorSet,  spaceShip,  CtrlFrequency);
		// Pitch Control 
		//controlCommandSet = addPitchControl( controlCommandSet, sensorSet,  spaceShip,  CtrlFrequency);
		// Yaw Control 
		//controlCommandSet = addYawControl( controlCommandSet, sensorSet,  spaceShip,  CtrlFrequency);
		
		
		return controlCommandSet;
	}
	
	private ControlCommandSet addRollControl(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {

		double RCS_X_CMD =0;
		 double Kp=20.8;		  // Proportional Coefficient
		 double Ki=0.0001;	  // Integrative Coefficient
		 double Kd=3;		      // Derivative Coefficient
	   	if(Math.toDegrees(sensorSet.getRealTimeResultSet().getPQR()[0][0]) > 5) { // Fast reduce rotational rate
			   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getPQR()[0][0];
			   	  RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, Kp, Ki, Kd, 1, -1);
	   	} else { // Reduce roll angle to 0
			   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getEulerAngle().roll ; 
			    RCS_X_CMD   = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.8, 0.0001, 3, 1, -1);
	   	}	
		   	 controlCommandSet.setMomentumRCS_X_cmd(RCS_X_CMD);
		    return controlCommandSet;

	}
	
	private ControlCommandSet addPitchControl(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		
		 double Kp=20.8;		  // Proportional Coefficient
		 double Ki=0.0001;	  // Integrative Coefficient
		 double Kd=3;		      // Derivative Coefficient

		double RCS_Y_CMD =0;
		/**
		 *  Setup: 
		 *  Fast reduce pitch rate -> then switch to reduce pitch angle
		 * 
		 * 
		 */
	   	if(Math.toDegrees(sensorSet.getRealTimeResultSet().getPQR()[1][0]) > 5) { 
			   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getPQR()[1][0];
			   double response = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, Kp, Ki, Kd, 1, -1);
			   if(Double.isNaN(response)) {
				   response =0;
				   System.err.println("Cntrl error > returned NaN");
			   }
			   RCS_Y_CMD = response;
	   	} else { // pitch angle to 0
			   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getEulerAngle().pitch ;
			   double response  = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.8, 0.0001, 3, 1, -1);
			   if(Double.isNaN(response)) {
				   response =0;
				   System.err.println("Cntrl error > returned NaN");
			   }
			   RCS_Y_CMD = response;
	   	}	
		   	 controlCommandSet.setMomentumRCS_Y_cmd(RCS_Y_CMD);
		
		return controlCommandSet;	
	}
	
	private ControlCommandSet addYawControl(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		
		 double Kp=20.8;		  // Proportional Coefficient
		 double Ki=0.0001;	  // Integrative Coefficient
		 double Kd=3;		      // Derivative Coefficient

		double RCS_Z_CMD =0;
		/**
		 *  Setup: 
		 *  Fast reduce yaw rate -> then switch to reduce yaw angle
		 * 
		 * 
		 */
	   	if(Math.toDegrees(sensorSet.getRealTimeResultSet().getPQR()[2][0]) > 5) { 
			   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getPQR()[2][0];
			   double response = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, Kp, Ki, Kd, 1, -1);
			   if(Double.isNaN(response)) {
				   response =0;
				   System.err.println("Cntrl error > returned NaN");
			   }
			   RCS_Z_CMD = response;
	   	} else { // yaw angle to 0
			   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getEulerAngle().yaw ;
			   double response  = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.8, 0.0001, 3, 1, -1);
			   if(Double.isNaN(response)) {
				   response =0;
				   System.err.println("Cntrl error > returned NaN");
			   }
			   RCS_Z_CMD = response;
	   	}	
		   	 controlCommandSet.setMomentumRCS_Z_cmd(RCS_Z_CMD);
		
		return controlCommandSet;	
	}
}
