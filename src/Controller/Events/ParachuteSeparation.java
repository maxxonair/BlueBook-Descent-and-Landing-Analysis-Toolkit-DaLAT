package Controller.Events;

import Controller.FlightController;
import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;

public class ParachuteSeparation extends FlightController{

	
	public ParachuteSeparation() {
		
	}
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
	controlCommandSet.setParachuteEjectCMD(true);
		return controlCommandSet;
	}
}
