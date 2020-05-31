package FlightElement.GNCModel.Events;

import FlightElement.SpaceShip;
import FlightElement.GNCModel.Controller.FlightController;

public class HeatShieldEjection extends FlightController {

	public HeatShieldEjection() {
		
	}
	
	@Override
	public void setCommand(SpaceShip spaceShip) {
		spaceShip.getgNCModel().getControlCommandSet().setHeatShieldEjectionCMD(true);
	}
}
