package Controller;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;

public class FlightController_AscentController extends FlightController {
	
	public FlightController_AscentController() {
		
	}
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		//-------------------------------------------------------------------------------------------
		// 			Set primary Propulsion to full thrust:
		//-------------------------------------------------------------------------------------------
		
		controlCommandSet.setPrimaryThrustThrottleCmd(1);
		
		//-------------------------------------------------------------------------------------------
		// 			Pitch over via Y axis controll;
		//-------------------------------------------------------------------------------------------
		
		   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getEulerY() ; 
		   double Ycmd = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.00001, 0.00000001, 0.0008, 1, -1);
		   
		   
			controlCommandSet.setMomentumRCS_Y_cmd(Ycmd);
		
		//-------------------------------------------------------------------------------------------
		// 			Roll Controll - Maintain horizontal attitude without bank angle
		//-------------------------------------------------------------------------------------------
		double RCS_X_CMD =0;
	   	if(Math.toDegrees(sensorSet.getRealTimeResultSet().getPQR()[0][0]) > 10) { // Fast reduce rotational rate
			    CTRL_ERROR =  sensorSet.getRealTimeResultSet().getPQR()[0][0];
			   	  RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 5, 0.0001, 3, 1, -1);
	   	} else { // Reduce roll angle to 0
			    CTRL_ERROR =  sensorSet.getRealTimeResultSet().getEulerX() ; 
			   RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.8, 0.0001, 3, 1, -1);
	   	}
	   	// controlCommandSet.setMomentumRCS_X_cmd(RCS_X_CMD);
	    //-------------------------------------------------------------------------------------------
		return controlCommandSet;
	}

}
