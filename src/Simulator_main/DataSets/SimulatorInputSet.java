package Simulator_main.DataSets;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;

public class SimulatorInputSet {
	
	SpaceShip spaceShip;
	IntegratorData integratorData;
	ControlCommandSet controlCommandSet;
	
	public SimulatorInputSet() {
		
	}

	public SpaceShip getSpaceShip() {
		return spaceShip;
	}

	public void setSpaceShip(SpaceShip spaceShip) {
		this.spaceShip = spaceShip;
	}

	public IntegratorData getIntegratorData() {
		return integratorData;
	}

	public void setIntegratorData(IntegratorData integratorData) {
		this.integratorData = integratorData;
	}

	public ControlCommandSet getControlCommandSet() {
		return controlCommandSet;
	}

	public void setControlCommandSet(ControlCommandSet controlCommandSet) {
		this.controlCommandSet = controlCommandSet;
	}

}
