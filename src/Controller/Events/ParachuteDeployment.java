package Controller.Events;

import Controller.FlightController;
import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;

public class ParachuteDeployment extends FlightController{
	
	ControlCommandSet controlCommandSet; 
	
	public ParachuteDeployment() {
		
	}
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		controlCommandSet.setParachuteDeployed(true);
		return controlCommandSet;
	}
}
