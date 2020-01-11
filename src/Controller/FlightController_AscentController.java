package Controller;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;

public class FlightController_AscentController extends FlightController {
	
	
	PID pitch;
	
	public FlightController_AscentController() {
		 pitch =  new PID(2.6, 0.001,3,-1, 1);
	}
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		//-------------------------------------------------------------------------------------------
		// 			Set primary Propulsion to full thrust:
		//-------------------------------------------------------------------------------------------
		
		controlCommandSet.setPrimaryThrustThrottleCmd(1); // Full Thrust
		
		//-------------------------------------------------------------------------------------------
		// 			Pitch over via Y axis control;
		//-------------------------------------------------------------------------------------------
		
		/**
		 * 
		 *  Inertia guided gravity turn pitch guidance law
		 *  
		 *  Ends at a flight path inclination of 5 degrees
		 */
		
		double flightPathInclination = sensorSet.getRealTimeResultSet().getFpa();
		double pitchIs = sensorSet.getRealTimeResultSet().getEulerY();
		double ctrlError;
		
		if(Math.toDegrees(flightPathInclination) > 5 ) {
			ctrlError =  flightPathInclination - pitchIs;
			//System.out.println(ctrlError);
		} else {
			ctrlError  = 0;
		}
		
		double Ycmd = -  PID_01.PID_001(ctrlError,1/CtrlFrequency, pitch.P , pitch.I , pitch.D , pitch.max, pitch.min);
		   
		   
			controlCommandSet.setMomentumRCS_Y_cmd(Ycmd);
		
		//-------------------------------------------------------------------------------------------
		// 			Roll Controll - Maintain horizontal attitude without bank angle
		//-------------------------------------------------------------------------------------------
			/*
		double RCS_X_CMD =0;
	   	if(Math.toDegrees(sensorSet.getRealTimeResultSet().getPQR()[0][0]) > 10) { // Fast reduce rotational rate
			    CTRL_ERROR =  sensorSet.getRealTimeResultSet().getPQR()[0][0];
			   	  RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 5, 0.0001, 3, 1, -1);
	   	} else { // Reduce roll angle to 0
			    CTRL_ERROR =  sensorSet.getRealTimeResultSet().getEulerX() ; 
			   RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.8, 0.0001, 3, 1, -1);
	   	}
	   	// controlCommandSet.setMomentumRCS_X_cmd(RCS_X_CMD);
	   	 
	   	 */
	    //-------------------------------------------------------------------------------------------
		return controlCommandSet;
	}

}
