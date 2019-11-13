package Controller.Events;

import Controller.FlightController;
import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
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
