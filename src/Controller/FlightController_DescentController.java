package Controller;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;

public class FlightController_DescentController extends FlightController{
	public static double PI    = 3.1415926535897932384626433832795028841971693993751058209749445;
	
	public FlightController_DescentController() {
		
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
		
		double pitchTarget = - sensorSet.getRealTimeResultSet().getFpa();
		double pitchIs = sensorSet.getRealTimeResultSet().getEulerAngle().pitch;
		double ctrlError= pitchTarget - pitchIs;
		
		PID pitch = new PID(0, 0, 0, -1, 1);
		pitch.P = 0.5;
		pitch.I = 0.001;
		pitch.D = 1.0;
		
		
		double Ycmd =   PID_01.PID_001(ctrlError,1/CtrlFrequency, pitch.P , pitch.I , pitch.D , pitch.max, pitch.min);
		//System.out.println(Math.toDegree(ctrlError)+"|"+Ycmd);
		//controlCommandSet.setTVC_alpha(Ycmd);
		controlCommandSet.setMomentumRCS_Y_cmd(Ycmd);
		
		/**
		 * 
		 * 
		 *  Yaw control
		 */
		
		double target = PI;
		double state = sensorSet.getRealTimeResultSet().getEulerAngle().yaw;
	    ctrlError= target - state;
		
		pitch.P = 0.05;
		pitch.I = 0.0001;
		pitch.D = 0.7;
		
		
		double Zcmd =   PID_01.PID_001(ctrlError,1/CtrlFrequency, pitch.P , pitch.I , pitch.D , pitch.max, pitch.min);
		//System.out.println(Math.toDegree(ctrlError)+"|"+Ycmd);
		//controlCommandSet.setTVC_alpha(Ycmd);
		//controlCommandSet.setMomentumRCS_Z_cmd(Zcmd);
		
		return controlCommandSet;
	}
}
