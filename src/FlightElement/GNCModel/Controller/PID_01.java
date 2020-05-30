package FlightElement.GNCModel.Controller;

public class PID_01{
	//--------------------------------------------------------------------------------------------------------
	//           				                PID controller 
	//  -> This class contains a set of PID controller to be used in any abitrary setup as flight controller
	// 
	// PID_001 - constrained PID controller
	// PID_002 - unconstrained PID controller 
	//
	//
	//--------------------------------------------------------------------------------------------------------
	public static double ERROR_Tminus=0;
	public static double ERROR_INTEGRAL=0;
	
    public static void FlightController_001_RESET() {
    	ERROR_INTEGRAL = 0 ;
    	ERROR_Tminus   = 0 ;
    }
	
	public static double PID_001(double ERROR, double deltat, double P_GAIN, double I_GAIN, double D_GAIN, double cmd_max, double cmd_min){
		//--------------------------------------------------------------------------------------------------------
		//           				Constrained PID controller 
		//  -> Given maximum and minimum constraints can not be exceeded by the controller output command 
		//--------------------------------------------------------------------------------------------------------
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
		ACT_CMD = P_CMD + I_CMD + D_CMD;

		if (ACT_CMD>cmd_max)       {
			ACT_CMD=cmd_max;				// Upper limit
		} else if(ACT_CMD<cmd_min) {
			ACT_CMD=cmd_min;				// Lower limit
		} 
		ERROR_Tminus = ERROR; 
		return ACT_CMD;
	}
public static double PID_002(double ERROR, double deltat, double P_GAIN, double I_GAIN, double D_GAIN){
	//--------------------------------------------------------------------------------------------------------
	//           				Unconstrained PID controller 
	//  -> No constrains to output command 
	//--------------------------------------------------------------------------------------------------------
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
	ERROR_Tminus = ERROR; 
	return ACT_CMD;
}
}