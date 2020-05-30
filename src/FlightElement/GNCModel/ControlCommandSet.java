package FlightElement.GNCModel;


public class ControlCommandSet extends Object implements Cloneable{

    private double primaryThrustThrottleCmd=0;				// Main engine throttle command [-]
    private double TVC_alpha=0;
    private double TVC_beta=0; 
	private double MomentumRCS_X_cmd=0;
	private double MomentumRCS_Y_cmd=0;
	private double MomentumRCS_Z_cmd=0;
	
	private int SequenceID = 0;
    
    private  double ctrl_vel =0;			// Active Flight Controller target velocity [m/s]
    private  double ctrl_alt = 0 ; 			// Active Flight Controller target altitude [m]
    private  double cntr_h_init=0;
    private  double cntr_v_init=0;
    private  double cntr_t_init=0;
    private  double cntr_fpa_init=0;
    
    private boolean ParachuteDeployedCMD=false; 
    private boolean ParachuteEjectCMD=false;
    private boolean HeatShieldEjectionCMD=false;
    
    private int activeSequence;
    
    
	public boolean isHeatShieldEjectionCMD() {
		return HeatShieldEjectionCMD;
	}


	public void setHeatShieldEjectionCMD(boolean heatShieldEjectionCMD) {
		HeatShieldEjectionCMD = heatShieldEjectionCMD;
	}


	public int getActiveSequence() {
		return activeSequence;
	}


	public void setActiveSequence(int activeSequence) {
		this.activeSequence = activeSequence;
	}

	public boolean isParachuteEjectCMD() {
		return ParachuteEjectCMD;
	}


	public void setParachuteEjectCMD(boolean parachuteEjectCMD) {
		ParachuteEjectCMD = parachuteEjectCMD;
	}


	public void setParachuteDeployedCMD(boolean parachuteDeployedCMD) {
		ParachuteDeployedCMD = parachuteDeployedCMD;
	}


	public boolean isParachuteDeployedCMD() {
		return ParachuteDeployedCMD;
	}


	public void setParachuteDeployed(boolean parachuteDeployed) {
		ParachuteDeployedCMD = parachuteDeployed;
	}


	public double getCtrl_vel() {
		return ctrl_vel;
	}


	public void setCtrl_vel(double ctrl_vel) {
		this.ctrl_vel = ctrl_vel;
	}


	public double getCtrl_alt() {
		return ctrl_alt;
	}


	public void setCtrl_alt(double ctrl_alt) {
		this.ctrl_alt = ctrl_alt;
	}


	public double getCntr_h_init() {
		return cntr_h_init;
	}


	public void setCntr_h_init(double cntr_h_init) {
		this.cntr_h_init = cntr_h_init;
	}


	public double getCntr_v_init() {
		return cntr_v_init;
	}


	public void setCntr_v_init(double cntr_v_init) {
		this.cntr_v_init = cntr_v_init;
	}


	public double getCntr_t_init() {
		return cntr_t_init;
	}


	public void setCntr_t_init(double cntr_t_init) {
		this.cntr_t_init = cntr_t_init;
	}


	public double getCntr_fpa_init() {
		return cntr_fpa_init;
	}


	public void setCntr_fpa_init(double cntr_fpa_init) {
		this.cntr_fpa_init = cntr_fpa_init;
	}


	public int getSequenceID() {
		return SequenceID;
	}


	public void setSequenceID(int sequenceID) {
		SequenceID = sequenceID;
	}


	public double getTVC_alpha() {
		return TVC_alpha;
	}


	public void setTVC_alpha(double tVC_alpha) {
		TVC_alpha = tVC_alpha;
	}


	public double getTVC_beta() {
		return TVC_beta;
	}


	public void setTVC_beta(double tVC_beta) {
		TVC_beta = tVC_beta;
	}


	public double getPrimaryThrustThrottleCmd() {
		return primaryThrustThrottleCmd;
	}


	public void setPrimaryThrustThrottleCmd(double primaryThrustThrottleCmd) {
		this.primaryThrustThrottleCmd = primaryThrustThrottleCmd;
	}


	public double getMomentumRCS_X_cmd() {
		return MomentumRCS_X_cmd;
	}


	public void setMomentumRCS_X_cmd(double momentumRCS_X_cmd) {
		MomentumRCS_X_cmd = momentumRCS_X_cmd;
	}


	public double getMomentumRCS_Y_cmd() {
		return MomentumRCS_Y_cmd;
	}


	public void setMomentumRCS_Y_cmd(double momentumRCS_Y_cmd) {
		MomentumRCS_Y_cmd = momentumRCS_Y_cmd;
	}


	public double getMomentumRCS_Z_cmd() {
		return MomentumRCS_Z_cmd;
	}


	public void setMomentumRCS_Z_cmd(double momentumRCS_Z_cmd) {
		MomentumRCS_Z_cmd = momentumRCS_Z_cmd;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
}
