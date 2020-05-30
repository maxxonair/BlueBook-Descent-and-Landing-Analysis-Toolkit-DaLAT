package FlightElement.GNCModel.Events;

import FlightElement.SpaceShip;
import FlightElement.GNCModel.ControlCommandSet;
import FlightElement.GNCModel.Controller.FlightController;
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
