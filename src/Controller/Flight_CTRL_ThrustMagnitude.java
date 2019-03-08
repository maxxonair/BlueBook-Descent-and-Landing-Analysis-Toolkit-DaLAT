package Controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Flight_CTRL_ThrustMagnitude{
	private int     ctrl_ID;			// Controller ID					[-]
	private boolean ctrl_on;			// Switch on/off (ctrl)			[true/false]
	private double  alt; 			// Altitude 						[m]
	private double  vel;	 	    		// velocity 						[m/s]
	private double  fpa;		    		// flight path angel 			[rad]
	private double  m0;				// Initial mass 					[kg]
	private double  m;				// Actual mass 					[kg]
	private double mprop;			// Propellant mass              [kg]
	private double ctrl_vinit;		// Controller initial velocity 	[m/s]
	private double ctrl_hinit;		// Controller initial altitude 	[m]
	private double ctrl_tinit;		// Controller initial time 	   	[s]
	private double ctrl_vel;  		// Additional target velocity 	[m/s]
	private double ctrl_alt;		    // Additional target altitude 	[m]
	private double thrust_max;		// Maximum Thrust 			   	[N]
	private double thrust_min;		// Minimum Thrust 			   	[N]
	private double throttle_cmd;	    // Controller throttle command	[-]
	private double thrust_cmd;      // Controller thrust command 	[N]
	private int    ctrl_curve;		// Control curve 				[-]
	private double ctrl_dt;			// Delta-t controller			[s]
	private double P_GAIN; 			// controller p-gain				[-]
	private double I_GAIN;			// controller i-gain		   		[-]
	private double D_GAIN;			// controller d-gain				[-]
	private double cmd_min;			// controller output cmd min		[-]
	private double cmd_max;			// controller output cmd max    [-]
	private double CTRL_ERROR;		// controller Errror				[ ]
	private double CTRL_TIME;		// controller Time 				[s]
	private double rm;				// Mean Radius					[m]
	private double refElev;			// Reference Elevation			[m]
	
	private double  tzero; 					// Sequence Time 						[s]
	private boolean tswitch =true;          // Time switch to start controller time [s]
	//-----------------------------------------------------------------------------
	static String INPUT_FILE = null; 
	public static String ControllerInputFile_1 = "/CTRL/cntrl_";
    public static String ControllerInputFile_2 = ".inp"; 
	public static String ControllerInputFile; 
	//-----------------------------------------------------------------------------
	public Flight_CTRL_ThrustMagnitude(int ctrl_ID, boolean ctrl_on, double[] x, double m0, double mprop, double ctrl_vinit, double ctrl_hinit, double ctrl_tinit, double ctrl_vel, double ctrl_alt, double thrust_max, double thrust_min, double throttle_cmd, double thrust_cmd, int ctrl_curve, double ctrl_dt, double P_GAIN, double I_GAIN, double D_GAIN, double cmd_min, double cmd_max, double rm, double refElev) {
		this.ctrl_ID	  = ctrl_ID;
		this.ctrl_on 	  = ctrl_on;
		this.alt 		  = x[2]-rm-refElev;
		this.vel 		  = x[3];
		this.fpa 		  = x[4];
		this.m0  		  = m0;
		this.m   		  = x[6];
		this.mprop        = mprop; 
		this.ctrl_vinit   = ctrl_vinit;
		this.ctrl_hinit   = ctrl_hinit;
		this.ctrl_tinit   = ctrl_tinit;
		this.ctrl_vel     = ctrl_vel;
		this.ctrl_alt     = ctrl_alt;
		this.thrust_max   = thrust_max;
		this.thrust_min   = thrust_min;
		this.throttle_cmd = throttle_cmd; 
		this.thrust_cmd   = thrust_cmd; 
		this.ctrl_curve   = ctrl_curve; 
		this.ctrl_dt	      = ctrl_dt; 
		this.P_GAIN		  = P_GAIN;
		this.I_GAIN 	      = I_GAIN;
		this.D_GAIN       = D_GAIN;
		this.cmd_min      = cmd_min;
		this.cmd_max      = cmd_min; 
		this.rm			  = rm;
		this.refElev	      = refElev; 
		
		double[] readINP;
		try {
			readINP = READ_CTRL_INPUT(Integer.toString(ctrl_ID));
			if (readINP[0]==1) {this.ctrl_on=true;}else {this.ctrl_on=false;}
		 this.P_GAIN  = readINP[1];
		 this.I_GAIN  = readINP[2];
		 this.D_GAIN  = readINP[3];
		 this.cmd_max = readINP[4];
		 this.cmd_min = readINP[5];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			System.out.println("ERROR: Controller 01 setting read failed.");
		}
	}
	
    public static double[] READ_CTRL_INPUT(String Controller_ID) throws IOException{
        double[] readINP = new double[20];
        String dir = System.getProperty("user.dir");
        INPUT_FILE = dir+ControllerInputFile_1+""+Controller_ID+""+ControllerInputFile_2;
       	double InitialState = 0;
   	    FileInputStream fstream = null;
       	try {
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
       
	
	public int get_ctrl_ID() {
		return ctrl_ID;
	}
	public boolean get_ctrl_on() {
		return ctrl_on;
	}
	public double get_alt() {
		return alt;
	}
	public double get_vel() {
		return vel;
	}
	public double get_fpa() {
		return fpa;
	}
	public double get_m0() {
		return m0;
	}
	public double get_m() {
		return m;
	}
	public double get_mprop() {
		return mprop; 
	}
	public double get_ctrl_vinit() {
		return ctrl_vinit;
	}
	public double get_ctrl_hinit() {
		return ctrl_hinit;
	}
	public double get_ctrl_tinit() {
		return ctrl_tinit;
	}
	public double get_ctrl_vel() {
		return ctrl_vel;
	}
	public double get_ctrl_alt() {
		return ctrl_alt;
	}
	public double get_thrust_max() {
		return thrust_max;
	}
	public double get_thrust_min() {
		return thrust_min;
	}
	public double get_ctrl_throttle_cmd() {
		return throttle_cmd;
	}
	public double get_thrust_cmd(){
    	if(ctrl_on) {
 		    double target_velocity=0;
 		    boolean ContinuousBurn = false;
 		    //------------------------------------------------------------------------------------------------------------------
 		    //									Select Reference Landing Path 
 		    //------------------------------------------------------------------------------------------------------------------
 		    if        (ctrl_curve==0) {
		         ContinuousBurn = true;
		    } else if (ctrl_curve==1) {
 		         target_velocity =    LandingCurve.ParabolicLandingCurve(ctrl_vinit, ctrl_hinit, ctrl_vel, ctrl_alt, alt);
 		    } else if (ctrl_curve==2) {
 		    	 target_velocity =   LandingCurve.SquarerootLandingCurve(ctrl_vinit, ctrl_hinit, ctrl_vel, ctrl_alt, alt);
 		    } else if (ctrl_curve==3) {
 		    	 target_velocity =   LandingCurve.LinearLandingCurve(ctrl_vinit, ctrl_hinit, ctrl_vel, ctrl_alt, alt);
 		    } 
 		    //------------------------------------------------------------------------------------------------------------------
 		    //									No Reference Landing curve set -> Continuous burn 
 		    //------------------------------------------------------------------------------------------------------------------
 		   if(ContinuousBurn){thrust_cmd = thrust_max;throttle_cmd=cmd_max;} else {
 	 		//------------------------------------------------------------------------------------------------------------------
 	 		//									No Reference Lanidng curve set -> Continuous burn 
 	 		//------------------------------------------------------------------------------------------------------------------
 		     CTRL_ERROR = vel - target_velocity ;
 		    // Select Controller and compute output command: 
 		     throttle_cmd = PID_01.PID_001(CTRL_ERROR,ctrl_dt, P_GAIN, I_GAIN, D_GAIN, cmd_max, cmd_min);
 		    // Check if controller provides usable values: 
 		   if(Double.isNaN(throttle_cmd)) {System.out.println("Controller Error: Returned NaN.-> Controller OFF"); throttle_cmd=0;}
 		//   if(1400<alt && alt<1500) {System.out.println(alt+" | "+target_velocity + " | "+ CTRL_ERROR + " | "+ throttle_cmd);}
		    	if ((m0-m)<mprop && vel>0) {
		    		thrust_cmd = throttle_cmd*thrust_max ; 
		    		if(thrust_cmd<thrust_min) {thrust_cmd=thrust_min;}
		    		if(thrust_cmd>thrust_max) {thrust_cmd=thrust_max;}
		    	} else if (alt<0){
		    		thrust_cmd   = 0;
		    		throttle_cmd = 0;
		    		System.out.println("CTRL "+ctrl_ID+" Forced Setting: Thrust = off. (Negative altitude)");
		    	} else if (vel<0) {
		    		thrust_cmd   = 0;
		    		throttle_cmd = 0;
		    		System.out.println("CTRL "+ctrl_ID+" Forced Setting: Thrust = off. (Negative velocity)");
		    	} else {
		    		thrust_cmd   = 0; // empty tanks or failed propulsion read
		    		throttle_cmd = 0;
		    		System.out.println("CTRL "+ctrl_ID+" Forced Setting: Thrust = off. (Empty tanks)");
		    	}
 		   }
    	} else {
    		thrust_cmd = 0; // Controller off 
    		throttle_cmd=0;
    	}
		return thrust_cmd;
	}
	public int get_ctrl_curve() {
		return ctrl_curve; 
	}
	public double get_ctrl_dt() {
		return ctrl_dt; 
	}
	public double get_CTRL_ERROR() {
		return CTRL_ERROR;
	}
	public double get_CTRL_TIME() {
		return CTRL_TIME; 
	}
	public void Update_Flight_CTRL(boolean ctrl_on, double[] x, double m0,  double mprop, double ctrl_vinit, double ctrl_hinit, double ctrl_tinit,  double ctrl_vel, double ctrl_alt, double thrust_max, double thrust_min, int ctrl_curve, double ctrl_dt) {
		this.ctrl_on 	  = ctrl_on;
		this.alt 		  = x[2]-rm-refElev;
		this.vel 		  = x[3];
		this.fpa 		  = x[4];
		this.m0  		  = m0;
		this.m   		  = x[6];
		this.mprop		  = mprop; 
		this.ctrl_vinit   = ctrl_vinit;
		this.ctrl_hinit   = ctrl_hinit;
		this.ctrl_tinit   = ctrl_tinit;
		this.ctrl_vel     = ctrl_vel;
		this.ctrl_alt     = ctrl_alt;
		this.thrust_max   = thrust_max;
		this.thrust_min   = thrust_min;
		this.ctrl_curve   = ctrl_curve; 
		this.ctrl_dt	      = ctrl_dt; 
		if(ctrl_tinit!=-1&&tswitch) {tzero=ctrl_tinit;tswitch=false;}
		CTRL_TIME = ctrl_tinit-tzero; 
	}
	public void set_ctrl_on(boolean NewValue){
		ctrl_on = NewValue;
	}
	public void set_alt(double NewValue){
		alt = NewValue;
	}
	public void set_vel(double NewValue) {
		vel = NewValue;
	}
	public void set_fpa(double NewValue) {
		fpa=NewValue;
	}
	public void set_m0(double NewValue) {
		m0 = NewValue;
	}
	public void set_m(double NewValue) {
		m = NewValue;
	}
	public void set_mprop(double NewValue) {
		mprop = NewValue; 
	}
	public void set_ctrl_vinit(double NewValue) {
		ctrl_vinit = NewValue;
	}
	public void set_ctrl_hinit(double NewValue) {
		ctrl_hinit = NewValue;
	}
	public void set_ctrl_tinit(double NewValue) {
		ctrl_tinit = NewValue;
	}
	public void set_ctrl_vel(double NewValue) {
		ctrl_vel = NewValue;
	}
	public void set_ctrl_alt(double NewValue) {
		ctrl_alt = NewValue;
	}
	public void set_thrust_max(double NewValue) {
		thrust_max = NewValue;
	}
	public void set_thrust_min(double NewValue) {
		thrust_min  = NewValue;
	}
	public void set_throttle_cmd(double NewValue) {
		throttle_cmd = NewValue;
	}
	public void set_thrust_cmd(double NewValue) {
		thrust_cmd = NewValue; 
	}
	public void set_ctrl_curve(int NewValue) {
		ctrl_curve = NewValue;
	}
	public void set_ctrl_dt(double NewValue) {
		ctrl_dt = NewValue; 
	}		
}