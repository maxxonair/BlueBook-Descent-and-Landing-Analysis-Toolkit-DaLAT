package FlightElement.GNCModel.Controller;

import FlightElement.SpaceShip;

public class FligthController_PrimaryThrust extends FlightController {

	public FligthController_PrimaryThrust() {
		
	}
	@Override
	public void setCommand(SpaceShip spaceShip) {
		
		spaceShip.getgNCModel().getControlCommandSet().setPrimaryThrustThrottleCmd(1);
	}
}
