package Simulator_main.DataSets;

public class NoiseModel extends Object implements Cloneable {
	
	// Environment Uncertainty Model 
	private boolean isAtmosphereNoiseModel;
	private boolean isAerodynamicNoiseModel;
	private boolean isGravityNoiseModel;
	
	private boolean isRadiationNoiseModel; 
	
	// Spacecraft Noise Model 
	private boolean isSensorNoiseModel;
	private boolean isActuatorNoiseModel; 
	
	public NoiseModel() {
		
	}

	public boolean isAtmosphereNoiseModel() {
		return isAtmosphereNoiseModel;
	}

	public void setAtmosphereNoiseModel(boolean isAtmosphereNoiseModel) {
		this.isAtmosphereNoiseModel = isAtmosphereNoiseModel;
	}

	public boolean isAerodynamicNoiseModel() {
		return isAerodynamicNoiseModel;
	}

	public void setAerodynamicNoiseModel(boolean isAerodynamicNoiseModel) {
		this.isAerodynamicNoiseModel = isAerodynamicNoiseModel;
	}

	public boolean isGravityNoiseModel() {
		return isGravityNoiseModel;
	}

	public void setGravityNoiseModel(boolean isGravityNoiseModel) {
		this.isGravityNoiseModel = isGravityNoiseModel;
	}

	public boolean isRadiationNoiseModel() {
		return isRadiationNoiseModel;
	}

	public void setRadiationNoiseModel(boolean isRadiationNoiseModel) {
		this.isRadiationNoiseModel = isRadiationNoiseModel;
	}

	public boolean isSensorNoiseModel() {
		return isSensorNoiseModel;
	}

	public void setSensorNoiseModel(boolean isSensorNoiseModel) {
		this.isSensorNoiseModel = isSensorNoiseModel;
	}

	public boolean isActuatorNoiseModel() {
		return isActuatorNoiseModel;
	}

	public void setActuatorNoiseModel(boolean isActuatorNoiseModel) {
		this.isActuatorNoiseModel = isActuatorNoiseModel;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
}