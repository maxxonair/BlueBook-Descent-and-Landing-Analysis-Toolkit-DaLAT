package FlightElement.GNCModel.Controller;

import FlightElement.SpaceShip;
import utils.UConst;

public class FlightController_AscentController extends FlightController {
	
	PID pitch;
	
	public FlightController_AscentController() {
		 pitch =  new PID(.4, 0.001,3,-1, 1);
	}
	
	@Override
	public void setCommand(SpaceShip spaceShip) {
		//-------------------------------------------------------------------------------------------
		// 			Set primary Propulsion to full thrust:
		//-------------------------------------------------------------------------------------------
		
		spaceShip.getgNCModel().getControlCommandSet().setPrimaryThrustThrottleCmd(1); // Full Thrust
		
		//-------------------------------------------------------------------------------------------
		// 			Pitch over via Y axis control;
		//-------------------------------------------------------------------------------------------
		
		/**
		 * 
		 *  Inertia guided gravity turn pitch guidance law
		 *  
		 *  Ends at a flight path inclination of 5 degrees
		 */
		
		double flightPathInclination = spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getFpa();
		double pitchIs = spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getEulerAngle().pitch;
		double ctrlError;
		
		if(Math.toDegrees(flightPathInclination) > 5 ) {
			ctrlError =  flightPathInclination - pitchIs;
	
		} else {
			ctrlError  = 0;
		}
		
		/**
		 * 
		 * 	SQRT pitching 
		 * 
		 * 		that(t) = A * sqrt(t) ;
		 */
		double tMECO = 175; //[s]
		double thetaMECO  = 20*UConst.PI/180 ; //[rad]
		double A = thetaMECO / Math.sqrt(tMECO);
		double t = spaceShip.getSensorModel().getSensorSet().getGlobalTime() -5;
		double theta = A * Math.sqrt(t);
		double pitchTarget = UConst.PI/2 - theta;
		ctrlError = pitchTarget - pitchIs;
		//System.out.println(t+"|"+(Math.toDegrees(ctrlError)));
		//-----------------------------------
		boolean isTVC = true;
		boolean AoAControl = false;
		
		if(AoAControl) {
			
			double aoaTarget = 0;
			double aoaIs = Math.toDegrees( spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getEulerAngle().pitch - spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getFpa() );
			
			ctrlError = aoaTarget - aoaIs;
			
			//System.out.println(ctrlError);
			
			pitch.P = 0.1;
			pitch.I = 0.01;
			pitch.D = 0.2;
			double Ycmd =  - PID_01.PID_001(ctrlError,1/spaceShip.getProperties().getoBC().getControllerFrequency(), pitch.P , pitch.I , pitch.D , pitch.max, pitch.min);
			//System.out.println(ctrlError+"|"+Ycmd);
			spaceShip.getgNCModel().getControlCommandSet().setTVC_alpha(Ycmd);
			
		} else {
			if(isTVC) {
				/**
				 * 
				 * Pitch controlled by TVC (alpha)
				 */
				pitch.P = 0.001;
				pitch.I = 0.0001;
				pitch.D = 0.8;
				double Ycmd =   PID_01.PID_001(ctrlError,1/spaceShip.getProperties().getoBC().getControllerFrequency(), pitch.P , pitch.I , pitch.D , pitch.max, pitch.min);
				//System.out.println(ctrlError+"|"+Ycmd);
				spaceShip.getgNCModel().getControlCommandSet().setTVC_alpha(Ycmd);
			} else {
				/**
				 * 
				 * Pitch controlled by RCS (thruster MY)
				 */
				double Ycmd = - PID_01.PID_001(ctrlError,1/spaceShip.getProperties().getoBC().getControllerFrequency(), pitch.P , pitch.I , pitch.D , pitch.max, pitch.min);
				spaceShip.getgNCModel().getControlCommandSet().setMomentumRCS_Y_cmd(Ycmd);
			}
		}
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
	}

}
