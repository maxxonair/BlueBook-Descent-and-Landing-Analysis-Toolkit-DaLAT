package FlightElement.Properties;

public class Propulsion {

	
	private double primaryISPMax;
	private double primaryISPMin;
	private double primaryThrustMax;
	private double primaryThrustMin;
	private double primaryPropellant;
	private boolean isPrimaryThrottleModel;
	
	private double secondaryISP_RCS_X;
	private double secondaryISP_RCS_Y;
	private double secondaryISP_RCS_Z;
	private double secondaryISP_AUX;
	private double secondaryThrust_RCS_X;
	private double secondaryThrust_RCS_Y;
	private double secondaryThrust_RCS_Z;
	private double secondaryThrust_AUX;
	private double secondaryPropellant;
	
	// Thruster with pulse-width-modulation minimum achievable on-time [ms]
	private double secondaryMinOnTime_X=-1;		
	private double secondaryMinOnTime_Y=-1;
	private double secondaryMinOnTime_Z=-1;
	// Reaction Control System Torque
	private double RCSMomentumX;
	private double RCSMomentumY;
	private double RCSMomentumZ;
	// Tank filling level for primary and secondary propulsion systems
	private double primaryPropellantFillingLevel;
	private double secondaryPropellantFillingLevel; 
	// Accumulated delta-v for primary and secondary propulsion sytem
	private double accumulatedDeltaVPrimary=0;
	private double accumulatedDeltaVSecondary=0;
	// Propellant mass flow rate primary propulsion system
	private double massFlowPrimary=0;
	// Thrust Vector Control - maximum achievable control angles
    private  double tvc_alpha_MAX_deg = 5;
    private  double tvc_beta_MAX_deg  = 5;
	
	
	public double getMassFlowPrimary() {
		return massFlowPrimary;
	}


	public void setMassFlowPrimary(double massFlowPrimary) {
		this.massFlowPrimary = massFlowPrimary;
	}


	public double getAccumulatedDeltaVPrimary() {
		return accumulatedDeltaVPrimary;
	}


	public void setAccumulatedDeltaVPrimary(double accumulatedDeltaVPrimary) {
		this.accumulatedDeltaVPrimary = accumulatedDeltaVPrimary;
	}


	public double getAccumulatedDeltaVSecondary() {
		return accumulatedDeltaVSecondary;
	}


	public void setAccumulatedDeltaVSecondary(double accumulatedDeltaVSecondary) {
		this.accumulatedDeltaVSecondary = accumulatedDeltaVSecondary;
	}


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

	public double getSecondaryISP_RCS_X() {
		return secondaryISP_RCS_X;
	}


	public void setSecondaryISP_RCS_X(double secondaryISP_RCS_X) {
		this.secondaryISP_RCS_X = secondaryISP_RCS_X;
	}


	public double getSecondaryISP_RCS_Y() {
		return secondaryISP_RCS_Y;
	}


	public void setSecondaryISP_RCS_Y(double secondaryISP_RCS_Y) {
		this.secondaryISP_RCS_Y = secondaryISP_RCS_Y;
	}


	public double getSecondaryISP_RCS_Z() {
		return secondaryISP_RCS_Z;
	}


	public void setSecondaryISP_RCS_Z(double secondaryISP_RCS_Z) {
		this.secondaryISP_RCS_Z = secondaryISP_RCS_Z;
	}


	public double getSecondaryISP_AUX() {
		return secondaryISP_AUX;
	}


	public void setSecondaryISP_AUX(double secondaryISP_AUX) {
		this.secondaryISP_AUX = secondaryISP_AUX;
	}



	public double getSecondaryThrust_RCS_X() {
		return secondaryThrust_RCS_X;
	}


	public void setSecondaryThrust_RCS_X(double secondaryThrust_RCS_X) {
		this.secondaryThrust_RCS_X = secondaryThrust_RCS_X;
	}


	public double getSecondaryThrust_RCS_Y() {
		return secondaryThrust_RCS_Y;
	}


	public void setSecondaryThrust_RCS_Y(double secondaryThrust_RCS_Y) {
		this.secondaryThrust_RCS_Y = secondaryThrust_RCS_Y;
	}


	public double getSecondaryThrust_RCS_Z() {
		return secondaryThrust_RCS_Z;
	}


	public void setSecondaryThrust_RCS_Z(double secondaryThrust_RCS_Z) {
		this.secondaryThrust_RCS_Z = secondaryThrust_RCS_Z;
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


	public  double getTvc_alpha_MAX_deg() {
		return tvc_alpha_MAX_deg;
	}


	public  void setTvc_alpha_MAX_deg(double tvc_alpha_MAX_deg) {
		this.tvc_alpha_MAX_deg = tvc_alpha_MAX_deg;
	}


	public  double getTvc_beta_MAX_deg() {
		return tvc_beta_MAX_deg;
	}


	public  void setTvc_beta_MAX_deg(double tvc_beta_MAX_deg) {
		this.tvc_beta_MAX_deg = tvc_beta_MAX_deg;
	}


	public double getSecondaryMinOnTime_X() {
		return secondaryMinOnTime_X;
	}


	public void setSecondaryMinOnTime_X(double secondaryMinOnTime_X) {
		this.secondaryMinOnTime_X = secondaryMinOnTime_X;
	}


	public double getSecondaryMinOnTime_Y() {
		return secondaryMinOnTime_Y;
	}


	public void setSecondaryMinOnTime_Y(double secondaryMinOnTime_Y) {
		this.secondaryMinOnTime_Y = secondaryMinOnTime_Y;
	}


	public double getSecondaryMinOnTime_Z() {
		return secondaryMinOnTime_Z;
	}


	public void setSecondaryMinOnTime_Z(double secondaryMinOnTime_Z) {
		this.secondaryMinOnTime_Z = secondaryMinOnTime_Z;
	}


	
}
