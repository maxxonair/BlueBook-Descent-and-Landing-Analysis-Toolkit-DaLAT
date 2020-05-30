package FlightElement;

import FlightElement.ForceTorqueModel.ForceTorqueModel;
import FlightElement.GNCModel.GNCModel;
import FlightElement.Properties.Properties;
import FlightElement.State.State;
import FlightElement.Target.Target;

public class SpaceShip {

	private int SpaceShipID;
	private Properties properties = new Properties();
	private State state = new State();
	private Target target = new Target();
	private ForceTorqueModel forceTorqueModel = new ForceTorqueModel();
	private GNCModel gNCModel = new GNCModel();
	
	
	double CoP  = 0;    // Center of (aerodynamic) Pressure; 
	
	

	public double getCoP() {
		return CoP;
	}


	public void setCoP(double coP) {
		CoP = coP;
	}


	public SpaceShip() {
		super();
	}


	public int getSpaceShipID() {
		return SpaceShipID;
	}


	public void setSpaceShipID(int spaceShipID) {
		SpaceShipID = spaceShipID;
	}


	public Properties getProperties() {
		return properties;
	}


	public State getState() {
		return state;
	}


	public Target getTarget() {
		return target;
	}


	public ForceTorqueModel getForceTorqueModel() {
		return forceTorqueModel;
	}


	public GNCModel getgNCModel() {
		return gNCModel;
	}
	
	
	
}
