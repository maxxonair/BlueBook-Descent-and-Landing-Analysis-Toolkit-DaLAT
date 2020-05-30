package FlightElement.GNCModel.Events;

import FlightElement.SpaceShip;
import FlightElement.GNCModel.ControlCommandSet;
import FlightElement.GNCModel.Controller.FlightController;
import Model.DataSets.SensorSet;

public class HeatShieldEjection extends FlightController {

	public HeatShieldEjection() {
		
	}
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
	controlCommandSet.setHeatShieldEjectionCMD(true);
		return controlCommandSet;
	}
}
