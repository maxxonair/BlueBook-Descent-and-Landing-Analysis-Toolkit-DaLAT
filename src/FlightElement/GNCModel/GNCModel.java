package FlightElement.GNCModel;

import FlightElement.SpaceShip;

public class GNCModel {
	
	ControlCommandSet controlCommandSet;
	MasterController masterController ;
	
	
	public GNCModel(SpaceShip spaceShip) {
		masterController = new MasterController();
		controlCommandSet = new ControlCommandSet();
	}

	public ControlCommandSet getControlCommandSet() {
		return controlCommandSet;
	}

	public MasterController getMasterController() {
		return masterController;
	}

	public void setControlCommandSet(ControlCommandSet controlCommandSet) {
		this.controlCommandSet = controlCommandSet;
	}

}
