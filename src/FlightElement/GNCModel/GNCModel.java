package FlightElement.GNCModel;

import FlightElement.SpaceShip;

public class GNCModel {
	
	private ControlCommandSet controlCommandSet;
	private MasterController masterController ;
	
	private int activeSequence = 0;
	
	double CTRLfrequencyIN;
	double CTRLfrequencyOUT;
	
	
	public GNCModel(SpaceShip spaceShip) {
		masterController = new MasterController(spaceShip);
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

	public int getActiveSequence() {
		return activeSequence;
	}

	public void setActiveSequence(int activeSequence) {
		this.activeSequence = activeSequence;
	}

	public double getCTRLfrequencyIN() {
		return CTRLfrequencyIN;
	}

	public void setCTRLfrequencyIN(double cTRLfrequencyIN) {
		CTRLfrequencyIN = cTRLfrequencyIN;
	}

	public double getCTRLfrequencyOUT() {
		return CTRLfrequencyOUT;
	}

	public void setCTRLfrequencyOUT(double cTRLfrequencyOUT) {
		CTRLfrequencyOUT = cTRLfrequencyOUT;
	}
	
}
