package Simulation.Model.DataSets;

import FlightElement.SpaceShip;
import FlightElement.ForceTorqueModel.ActuatorSet;
import FlightElement.GNCModel.ControlCommandSet;

public class MasterSet extends Object implements Cloneable{

    public  AtmosphereSet atmosphereSet = new AtmosphereSet();
    public  ForceMomentumSet forceMomentumSet= new ForceMomentumSet();
    public  GravitySet gravitySet = new GravitySet();
    public  AerodynamicSet aerodynamicSet = new AerodynamicSet();
    public  ControlCommandSet controlCommandSet = new ControlCommandSet();
    public  ActuatorSet actuatorSet = new ActuatorSet();
    public 	 SpaceShip spaceShip = new SpaceShip(); 
    
    
    
	public SpaceShip getSpaceShip() {
		return spaceShip;
	}
	public void setSpaceShip(SpaceShip spaceShip) {
		this.spaceShip = spaceShip;
	}
	public  AtmosphereSet getAtmosphereSet() {
		return atmosphereSet;
	}
	public  void setAtmosphereSet(AtmosphereSet atmosphereSet) {
		try {
			this.atmosphereSet = (AtmosphereSet) atmosphereSet.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  ForceMomentumSet getForceMomentumSet() {
		return forceMomentumSet;
	}
	public  void setForceMomentumSet(ForceMomentumSet forceMomentumSet) {
		try {
			this.forceMomentumSet = (ForceMomentumSet) forceMomentumSet.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  GravitySet getGravitySet() {
		return gravitySet;
	}
	public  void setGravitySet(GravitySet gravitySet) {
		this.gravitySet = gravitySet;
	}
	public  AerodynamicSet getAerodynamicSet() {
		return aerodynamicSet;
	}
	public  void setAerodynamicSet(AerodynamicSet aerodynamicSet) {
		try {
			this.aerodynamicSet = (AerodynamicSet) aerodynamicSet.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  ControlCommandSet getControlCommandSet() {
		return controlCommandSet;
	}
	public  void setControlCommandSet(ControlCommandSet controlCommandSet) {
		try {
			this.controlCommandSet = (ControlCommandSet) controlCommandSet.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  ActuatorSet getActuatorSet() {
		return actuatorSet;
	}
	public  void setActuatorSet(ActuatorSet actuatorSet) {
		try {
			this.actuatorSet = (ActuatorSet) actuatorSet.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
}
