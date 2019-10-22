package FlightElement;

public class Propulsion {

	
	private double primaryISPMax;
	private double primaryISPMin;
	private double primaryThrustMax;
	private double primaryThrustMin;
	private double primaryPropellant;
	private boolean isPrimaryThrottleModel;
	
	private double secondaryISP_RCS;
	private double secondaryISP_AUX;
	private double secondaryThrust_RCS;
	private double secondaryThrust_AUX;
	private double secondaryPropellant;
	
	private double RCSMomentumX;
	private double RCSMomentumY;
	private double RCSMomentumZ;
	
	private double primaryPropellantFillingLevel;
	private double secondaryPropellantFillingLevel; 
	
	
	
	public double getPrimaryPropellantFillingLevel() {
		return primaryPropellantFillingLevel;
	}


	public void setPrimaryPropellantFillingLevel(double primaryPropellantFillingLevel) {
		this.primaryPropellantFillingLevel = primaryPropellantFillingLevel;
	}


	public double getSecondaryPropellantFillingLevel() {
		return secondaryPropellantFillingLevel;
	}


	public void setSecondaryPropellantFillingLevel(double secondaryPropellantFillingLevel) {
		this.secondaryPropellantFillingLevel = secondaryPropellantFillingLevel;
	}


	public double getRCSMomentumX() {
		return RCSMomentumX;
	}


	public void setRCSMomentumX(double rCSMomentumX) {
		RCSMomentumX = rCSMomentumX;
	}


	public double getRCSMomentumY() {
		return RCSMomentumY;
	}


	public void setRCSMomentumY(double rCSMomentumY) {
		RCSMomentumY = rCSMomentumY;
	}


	public double getRCSMomentumZ() {
		return RCSMomentumZ;
	}


	public void setRCSMomentumZ(double rCSMomentumZ) {
		RCSMomentumZ = rCSMomentumZ;
	}


	public Propulsion() {

	}


	public double getPrimaryISPMax() {
		return primaryISPMax;
	}


	public void setPrimaryISPMax(double primaryISPMax) {
		this.primaryISPMax = primaryISPMax;
	}


	public double getPrimaryISPMin() {
		return primaryISPMin;
	}


	public void setPrimaryISPMin(double primaryISPMin) {
		this.primaryISPMin = primaryISPMin;
	}


	public double getPrimaryThrustMax() {
		return primaryThrustMax;
	}


	public void setPrimaryThrustMax(double primaryThrustMax) {
		this.primaryThrustMax = primaryThrustMax;
	}


	public double getPrimaryThrustMin() {
		return primaryThrustMin;
	}


	public void setPrimaryThrustMin(double primaryThrustMin) {
		this.primaryThrustMin = primaryThrustMin;
	}


	public double getPrimaryPropellant() {
		return primaryPropellant;
	}


	public void setPrimaryPropellant(double primaryPropellant) {
		this.primaryPropellant = primaryPropellant;
		this.primaryPropellantFillingLevel = primaryPropellant;
	}


	public boolean isPrimaryThrottleModel() {
		return isPrimaryThrottleModel;
	}

	public void setIsPrimaryThrottleModel(boolean value) {
	this.isPrimaryThrottleModel=value;
	}	

	public void setPrimaryThrottleModel(boolean isPrimaryThrottleModel) {
		this.isPrimaryThrottleModel = isPrimaryThrottleModel;
	}


	public double getSecondaryISP_RCS() {
		return secondaryISP_RCS;
	}


	public void setSecondaryISP_RCS(double secondaryISP_RCS) {
		this.secondaryISP_RCS = secondaryISP_RCS;
	}


	public double getSecondaryISP_AUX() {
		return secondaryISP_AUX;
	}


	public void setSecondaryISP_AUX(double secondaryISP_AUX) {
		this.secondaryISP_AUX = secondaryISP_AUX;
	}


	public double getSecondaryThrust_RCS() {
		return secondaryThrust_RCS;
	}


	public void setSecondaryThrust_RCS(double secondaryThrust_RCS) {
		this.secondaryThrust_RCS = secondaryThrust_RCS;
	}


	public double getSecondaryThrust_AUX() {
		return secondaryThrust_AUX;
	}


	public void setSecondaryThrust_AUX(double secondaryThrust_AUX) {
		this.secondaryThrust_AUX = secondaryThrust_AUX;
	}


	public double getSecondaryPropellant() {
		return secondaryPropellant;
	}


	public void setSecondaryPropellant(double secondaryPropellant) {
		this.secondaryPropellant = secondaryPropellant;
		this.secondaryPropellantFillingLevel = secondaryPropellant;
	}


	
}
