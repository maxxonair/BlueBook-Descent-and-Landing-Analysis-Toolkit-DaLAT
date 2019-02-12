package Controller;

public class PID_01{
	
	static double ERROR_Tminus;
	static double ERROR_INTEGRATOR=0;
	
	public static double FlightController_001(double ERROR, double P_GAIN, double I_GAIN, double D_GAIN, double deltat, double cmd_max, double cmd_min){
		double ACT_CMD=0;							// Actuator Command
		double P_CMD=0;								// Proportional tuning command
		double I_CMD=0;								// Integral tuning command
		double D_CMD=0;								// Derivative tuning command 
		P_CMD = P_GAIN * ERROR;
		I_CMD = I_GAIN*(ERROR_INTEGRATOR+ERROR);
		D_CMD = D_GAIN*(ERROR-ERROR_Tminus)/deltat;
		ACT_CMD=P_CMD + I_CMD + D_CMD;
		if (ACT_CMD>cmd_max)       {
			ACT_CMD=cmd_max;
		} else if(ACT_CMD<cmd_min) {
			ACT_CMD=cmd_min;
		} 
		System.out.println(ERROR + " - "+ACT_CMD);
		return ACT_CMD;
	}
}