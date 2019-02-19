package Controller;

public class PID_01{
	
	public static double ERROR_Tminus=0;
	public static double ERROR_INTEGRAL=0;
	
    public static void FlightController_001_RESET() {
    	ERROR_INTEGRAL = 0 ;
    	ERROR_Tminus   = 0 ;
    }
	
	public static double FlightController_001(double ERROR, double deltat, double P_GAIN, double I_GAIN, double D_GAIN, double cmd_max, double cmd_min){
		double ACT_CMD=0;							// Actuator Command
		double P_CMD=0;								// Proportional tuning command
		double I_CMD=0;								// Integral tuning command
		double D_CMD=0;								// Derivative tuning command 
		P_CMD = P_GAIN * ERROR;
		ERROR_INTEGRAL = ERROR_INTEGRAL + (ERROR * deltat);
		I_CMD = I_GAIN*(ERROR_INTEGRAL);
		if(deltat>0) {
		D_CMD = D_GAIN*(ERROR-ERROR_Tminus)/deltat;} else {
		D_CMD = 0;
		}
		ACT_CMD=P_CMD + I_CMD + D_CMD;
		if (ACT_CMD>cmd_max)       {
			ACT_CMD=cmd_max;				// Upper limit
		} else if(ACT_CMD<cmd_min) {
			ACT_CMD=cmd_min;				// Lower limit
		} 
		ERROR_Tminus = ERROR; 
		return ACT_CMD;
	}
}