package FlightElement.GNCModel.Controller;

import FlightElement.SpaceShip;
import FlightElement.GNCModel.ControlCommandSet;
import Model.DataSets.SensorSet;

public class FligthController_PrimaryThrust extends FlightController {

	public FligthController_PrimaryThrust() {
		
	}
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		
		controlCommandSet.setPrimaryThrustThrottleCmd(1);
		return controlCommandSet;
	}
}
