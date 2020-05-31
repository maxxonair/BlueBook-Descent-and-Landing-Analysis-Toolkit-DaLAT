package FlightElement.GNCModel.Events;

import FlightElement.SpaceShip;
import FlightElement.GNCModel.Controller.FlightController;

public class ParachuteDeployment extends FlightController{
	
	
	public ParachuteDeployment() {
		
	}
	
	@Override
	public void setCommand( SpaceShip spaceShip) {
		spaceShip.getgNCModel().getControlCommandSet().setParachuteDeployed(true);
	}
}
