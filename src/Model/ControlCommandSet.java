package Model;

public class ControlCommandSet {

    private double primaryThrustThrottleCmd=0;				// Main engine throttle command [-]
	private double MomentumRCS_X_cmd=0;
	private double MomentumRCS_Y_cmd=0;
	private double MomentumRCS_Z_cmd=0;
    

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
	
	
}
