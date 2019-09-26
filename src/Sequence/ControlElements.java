package Sequence;

public class ControlElements {
	
	private double primaryThrust_is=0;
    private double primaryThrustThrottleCmd=0;				// Main engine throttle command [-]
    
    
	public ControlElements() {
		super();
	}


	public double getPrimaryThrust_is() {
		return primaryThrust_is;
	}


	public void setPrimaryThrust_is(double primaryThrust_is) {
		this.primaryThrust_is = primaryThrust_is;
	}


	public double getPrimaryThrustThrottleCmd() {
		return primaryThrustThrottleCmd;
	}


	public void setPrimaryThrustThrottleCmd(double primaryThrustThrottleCmd) {
		this.primaryThrustThrottleCmd = primaryThrustThrottleCmd;
	}
    
    

}
