package FlightElement.GNCModel.Events;

import FlightElement.SpaceShip;
import FlightElement.GNCModel.Controller.FlightController;

public class ParachuteSeparation extends FlightController{

	
	public ParachuteSeparation() {
		
	}
	
	@Override
	public void setCommand( SpaceShip spaceShip) {
		spaceShip.getgNCModel().getControlCommandSet().setParachuteEjectCMD(true);

	}
}
