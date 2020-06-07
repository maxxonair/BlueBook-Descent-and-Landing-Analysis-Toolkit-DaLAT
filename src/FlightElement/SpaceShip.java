package FlightElement;

import java.util.UUID;

import FlightElement.ForceTorqueModel.SpaceshipForceTorqueModel;
import FlightElement.GNCModel.GNCModel;
import FlightElement.Properties.Properties;
import FlightElement.SensorModel.SensorModel;
import FlightElement.State.State;
import FlightElement.Target.Target;

public class SpaceShip {

	private String SpaceShipID;
	private Properties properties;
	private State state ;
	private Target target ;
	private SpaceshipForceTorqueModel forceTorqueModel;
	private GNCModel gNCModel ;
	private SensorModel sensorModel ;
	
	
	
	double CoP  = 0;    // Center of (aerodynamic) Pressure; 	
	
	public SpaceShip() {
		super();
		
		SpaceShipID = UUID.randomUUID().toString();
		
	
		properties = new Properties();
		state = new State();
		target = new Target();
		forceTorqueModel = new SpaceshipForceTorqueModel(this);
		gNCModel = new GNCModel(this);
		sensorModel = new SensorModel();
		
	}

	public double getCoP() {
		return CoP;
	}


	public void setCoP(double coP) {
		CoP = coP;
	}


	public String getSpaceShipID() {
		return SpaceShipID;
	}


	public void setSpaceShipID(String spaceShipID) {
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


	public SpaceshipForceTorqueModel getForceTorqueModel() {
		return forceTorqueModel;
	}


	public GNCModel getgNCModel() {
		return gNCModel;
	}


	public SensorModel getSensorModel() {
		return sensorModel;
	}
	
	
	
}
