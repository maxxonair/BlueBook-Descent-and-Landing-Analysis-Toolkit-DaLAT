package Controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class PID_01{
	
	public static double ERROR_Tminus=0;
	public static double ERROR_INTEGRAL=0;
	static String INPUT_FILE = null; 
	public static boolean eclipse_run = true; 
	public static String ControllerInputFile = ".\\CTRL\\cntrl_01.inp"; 
	
    public static double[] READ_CTRL_INPUT() throws IOException{
     double[] readINP = new double[7];
    String dir = System.getProperty("user.dir");
       	if(eclipse_run) {
       	   	 INPUT_FILE = dir+"/LandingSim-3DOF/CTRL/cntrl_01.inp";} else {
       	   	 INPUT_FILE = ControllerInputFile;}
    	double InitialState = 0;
	    FileInputStream fstream = null;
	    try{
	    fstream = new FileInputStream(INPUT_FILE);
	    } catch(IOException eIO) { System.out.println(eIO);}
        DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int k = 0;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String[] tokens = strLine.split(" ");
        	InitialState = Double.parseDouble(tokens[0]);
        	readINP[k]= InitialState;
        	k++;
        }
        fstream.close();
        in.close();
        br.close();
        } catch(NullPointerException eNPE) { System.out.println(eNPE);}
        return readINP;
    }
    
    public static void FlightController_001_RESET() {
    	ERROR_INTEGRAL = 0 ;
    	ERROR_Tminus   = 0 ;
    }
	
	public static double FlightController_001(double ERROR, double deltat){
		double ACT_CMD=0;							// Actuator Command
		double P_CMD=0;								// Proportional tuning command
		double I_CMD=0;								// Integral tuning command
		double D_CMD=0;								// Derivative tuning command 
		double[] readINP;
		try {
			readINP = READ_CTRL_INPUT();
		double P_GAIN  = readINP[1];
		double I_GAIN  = readINP[2];
		double D_GAIN  = readINP[3];
		double cmd_max = readINP[4];
		double cmd_min = readINP[5];
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
			System.out.println("ERROR: Controller 01 setting read failed.");
		}
		return ACT_CMD;
	}
}