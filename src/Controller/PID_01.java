package Controller;

public class PID_01{
	
	static double ERROR_Tminus;
	static double ERROR_INTEGRATOR=0;
	
	public static double FlightController_001(double INPUT_CMD, double OUTPUT_STATE, double deltat, double cmd_max, double cmd_min){
		double ERROR = INPUT_CMD-OUTPUT_STATE;		// Current Error State
		double ACT_CMD=0;							// Actuator Command
		double P_TUNE=0.1;							// Proportional tuning factor
		double I_TUNE=0.1;							// Integral tuning factor
		double D_TUNE=0.1;							// Derivative tuning factor
		double P_CMD=0;								// Proportional tuning command
		double I_CMD=0;								// Integral tuning command
		double D_CMD=0;								// Derivative tuning command 
		P_CMD = P_TUNE * ERROR;
		I_CMD = I_TUNE*(ERROR_INTEGRATOR+ERROR);
		D_CMD = D_TUNE*(ERROR-ERROR_Tminus)/deltat;
		ACT_CMD=P_CMD + I_CMD + D_CMD;
		if (ACT_CMD>cmd_max)       {
			ACT_CMD=cmd_max;
		} else if(ACT_CMD<cmd_min) {
			ACT_CMD=cmd_min;
		} 
		return ACT_CMD;
	}
}