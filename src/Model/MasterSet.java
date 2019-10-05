package Model;

import Model.Aerodynamic.AerodynamicSet;

public class MasterSet {

    public  AtmosphereSet atmosphereSet = new AtmosphereSet();
    public  ForceMomentumSet forceMomentumSet= new ForceMomentumSet();
    public  GravitySet gravitySet = new GravitySet();
    public  AerodynamicSet aerodynamicSet = new AerodynamicSet();
    public  ControlCommandSet controlCommandSet = new ControlCommandSet();
    public  ActuatorSet actuatorSet = new ActuatorSet();
	public  AtmosphereSet getAtmosphereSet() {
		return atmosphereSet;
	}
	public  void setAtmosphereSet(AtmosphereSet atmosphereSet) {
		this.atmosphereSet = atmosphereSet;
	}
	public  ForceMomentumSet getForceMomentumSet() {
		return forceMomentumSet;
	}
	public  void setForceMomentumSet(ForceMomentumSet forceMomentumSet) {
		this.forceMomentumSet = forceMomentumSet;
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
		this.aerodynamicSet = aerodynamicSet;
	}
	public  ControlCommandSet getControlCommandSet() {
		return controlCommandSet;
	}
	public  void setControlCommandSet(ControlCommandSet controlCommandSet) {
		this.controlCommandSet = controlCommandSet;
	}
	public  ActuatorSet getActuatorSet() {
		return actuatorSet;
	}
	public  void setActuatorSet(ActuatorSet actuatorSet) {
		this.actuatorSet = actuatorSet;
	}
    
    
    
}
