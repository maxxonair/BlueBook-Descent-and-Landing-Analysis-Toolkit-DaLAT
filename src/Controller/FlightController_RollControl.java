package Controller;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;


public class FlightController_RollControl extends FlightController {

	
	public FlightController_RollControl() {
		
	}
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getEulerX() ;     

		   	 double RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.8, 0.0001, 3, 1, -1);

		   	 controlCommandSet.setMomentumRCS_X_cmd(RCS_X_CMD);
		    return controlCommandSet;

	}
}
