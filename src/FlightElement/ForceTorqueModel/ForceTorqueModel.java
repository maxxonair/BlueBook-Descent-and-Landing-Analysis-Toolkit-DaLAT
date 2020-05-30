package FlightElement.ForceTorqueModel;

public class ForceTorqueModel {
	
	ActuatorModel actuatorModel = new ActuatorModel();
	ActuatorSet actuatorSet = new ActuatorSet();
	
	public ForceTorqueModel() {
		
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
