package Controller;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;

public class FlightController_YawControl extends FlightController{

	
	
	public FlightController_YawControl() {
		
	}
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		   double CTRL_ERROR =  sensorSet.getRealTimeResultSet().getEulerZ() ;     

		   	 double RCS_Z_CMD = -  PID_01.PID_001(CTRL_ERROR,1/CtrlFrequency, 0.8, 0.000, 2, 1, -1);

		   	 controlCommandSet.setMomentumRCS_Z_cmd(RCS_Z_CMD);
		    return controlCommandSet;

	}
}
