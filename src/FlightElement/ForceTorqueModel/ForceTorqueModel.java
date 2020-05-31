package FlightElement.ForceTorqueModel;

import FlightElement.SpaceShip;

public class ForceTorqueModel {
	
	private ActuatorModel actuatorModel;
	private ActuatorSet actuatorSet;
	
	public ForceTorqueModel(SpaceShip spaceShip) {
		 actuatorModel = new ActuatorModel(spaceShip);
		 actuatorSet = new ActuatorSet();
	}

	public ActuatorModel getActuatorModel() {
		return actuatorModel;
	}

	public ActuatorSet getActuatorSet() {
		return actuatorSet;
	}

	public void setActuatorSet(ActuatorSet actuatorSet) {
		this.actuatorSet = actuatorSet;
	}
	
}
