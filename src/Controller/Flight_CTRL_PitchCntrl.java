package Controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Simulator_main.DataSets.CurrentDataSet;
import Toolbox.Mathbox;

public class Flight_CTRL_PitchCntrl{
        public static double PI = 3.141592653589793238462643383279502884197169399375;
    	static double deg2rad = PI/180.0; 		//Convert deg2radrees to radians
    	static double rad2deg = 180.0/PI; 		//Convert radians to deg2radrees
	//--------------------------------------------------------------------------------------------------------
	//
	//		Thrust vector controller:
	//
	//			x - > Time [s] (Sequence time!)
	//			y - > Flight Path Angle [rad]
	//
	//--------------------------------------------------------------------------------------------------------
	private int     ctrl_ID;				// Controller ID					[-]
	private boolean ctrl_on;				// Switch on/off (ctrl)				[true/false]
	private double  ctr_init_x;
	private double  ctr_init_y;
	private double  ctr_end_x;
	private double  ctr_end_y;
	private double  x_is; 
	private double  y_is;
	private double  y_ideal; 
	private int     ctrl_curve;				// Control curve 					[-]
	private double  ctrl_dt;				// Delta-t controller				[s]
	private double  ctrl_type; 
	private double  P_GAIN; 				// controller p-gain				[-]
	private double  I_GAIN;					// controller i-gain		   		[-]
	private double  D_GAIN;					// controller d-gain				[-]
	private double  cmd_min;				// controller output cmd min		[-]
	private double  cmd_max;				// controller output cmd max    	[-]
	private double  CTRL_ERROR;				// controller Errror				[ ]
	private double  CTRL_TIME;				// controller Time 					[s]	
	private double  CTRL_Thrust;
	private boolean engine_lost=false; 
	private double t_engine_lost; 
	private double  altitude_is;				// Acutall Altitude ref. mean radius [m]
	private double rm; 						// Mean radius [m]
	private double elevation; 				// Lodacl elevation [m] 
	private double global_time; 			// Global ascent time [s]
		
	private double pitchCMD;					// thrust vector angle cmd			[rad] 
	
	private double  tzero; 					// Sequence Time 						[s]
	private boolean tswitch =true;          // Time switch to start controller time [s]
	
	private double tvc_was =0; 
	private boolean tvc_switch=true; 
	private double tvcwas=0; 
	
	private static double[][] reference_fpa_profile = new double[541][3];
	//-----------------------------------------------------------------------------
	static String INPUT_FILE = null; 
	public static String ControllerInputFile = "/CTRL/ctrl_main.inp"; 
	public static String ControllerReferenceFile = "/INP/TVC_reference.csv";
	//-----------------------------------------------------------------------------
	public Flight_CTRL_PitchCntrl(int ctrl_ID, boolean ctrl_on, double ctr_init_x, double ctr_init_y, double ctr_end_x, double ctr_end_y, double rm, double elevation) {

		this.ctrl_ID	      = ctrl_ID;
		this.ctrl_on 	  = ctrl_on;
		this.ctr_init_x   = ctr_init_x;
		this.ctr_init_y   = ctr_init_y;
		this.ctr_end_x    = ctr_end_x;
		this.ctr_end_y    = ctr_end_y; 
		this.rm			  = rm; 
		this.elevation    = elevation; 

		if (ctrl_ID==0) {
			this.ctrl_on=false;
		} else {
		double[] readINP;
		try {
			readINP = READ_CTRL_INPUT(ctrl_ID);
		 this.ctrl_type = readINP[0];
		 this.P_GAIN  = readINP[1];
		 this.I_GAIN  = readINP[2];
		 this.D_GAIN  = readINP[3];
		 this.cmd_min = readINP[4];
		 this.cmd_max = readINP[5];

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			System.out.println("ERROR: Controller 01 setting read failed.");
		}
		}
		try {
			READ_CTRL_REFERENCE_INPUT();
		} catch (IOException e) {System.out.println("ERROR: Controller reference filed not read.");}
	}
	
    public static double[] READ_CTRL_INPUT(int Controller_ID) throws IOException{
        double[] readCTRL = new double[6]; 
    	if(Controller_ID!=0) {
        String dir = System.getProperty("user.dir");
        INPUT_FILE = dir+ControllerInputFile;
   	    FileInputStream fstream = null;
       	try {
   	    fstream = new FileInputStream(INPUT_FILE);
   	    } catch(IOException eIO) { System.out.println(eIO);}
            DataInputStream in = new DataInputStream(fstream);
   			BufferedReader br = new BufferedReader(new InputStreamReader(in));
   			String strLine;
           try {
           while ((strLine = br.readLine()) != null )   {
              	String[] tokens = strLine.split(" ");
        	   if(Integer.parseInt(tokens[0])==Controller_ID) {
			           	readCTRL[0]= Double.parseDouble(tokens[1]);
			           	readCTRL[1]= Double.parseDouble(tokens[2]);
			           	readCTRL[2]= Double.parseDouble(tokens[3]);
			           	readCTRL[3]= Double.parseDouble(tokens[4]);
			           	readCTRL[4]= Double.parseDouble(tokens[5]);
			           	readCTRL[5]= Double.parseDouble(tokens[6]);
        	   }
           }
           fstream.close();
           in.close();
           br.close();
           } catch(NullPointerException eNPE) { System.out.println(eNPE);}
        }
    	return readCTRL; 
       }
    
    public static void READ_CTRL_REFERENCE_INPUT() throws IOException{
        String dir = System.getProperty("user.dir");
        INPUT_FILE = dir+ControllerReferenceFile;
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
           	String[] tokens = strLine.split(",");
           	reference_fpa_profile[k][0] =  Double.parseDouble(tokens[0]);
           	reference_fpa_profile[k][1] =  Double.parseDouble(tokens[1]);
           	reference_fpa_profile[k][2] =  Double.parseDouble(tokens[2]);
           	k++;
           }
           fstream.close();
           in.close();
           br.close();
           } catch(NullPointerException eNPE) { System.out.println(eNPE);}
       }
       
	
	public int get_ctrl_ID() {
		return ctrl_ID;
	}
	public boolean get_ctrl_on() {
		return ctrl_on;
	}

	public double get_TVC_cmd(){
    	if(ctrl_on) {
 		    //------------------------------------------------------------------------------------------------------------------
 		    //									Select Reference Landing Path 
 		    //------------------------------------------------------------------------------------------------------------------
    			boolean ContinuousBurn = false;
    		if        (ctrl_curve==0) {
		         ContinuousBurn = true;
		    } else if (ctrl_curve==1) {
 		         y_ideal =    PitchCurve.SquareRootPitchCurve(ctr_init_x, ctr_init_y, ctr_end_y, ctr_end_x, x_is);
 		    } else if (ctrl_curve==2) {
 		    		y_ideal =   0; // TBD
 		    } else if (ctrl_curve==3) {
 		    		y_ideal =   PitchCurve.LinearPitchCurve(ctr_init_x, ctr_init_y, ctr_end_y, ctr_end_x, x_is);
 		    } 
 		    //------------------------------------------------------------------------------------------------------------------
 		    //									No Reference Landing curve set -> Continuous burn 
 		    //------------------------------------------------------------------------------------------------------------------
     		    //------------------------------------------------------------------------------------------------------------------
     		    //									No Reference Landing curve set -> Continuous burn 
     		    //------------------------------------------------------------------------------------------------------------------
     		   if(ContinuousBurn){  } else {
     	 		//------------------------------------------------------------------------------------------------------------------
     	 		//									No Reference Lanidng curve set -> Continuous burn 
     	 		//------------------------------------------------------------------------------------------------------------------
     		     CTRL_ERROR = y_is - y_ideal ;
     		    // Select Controller and compute output command: 
     		     if (ctrl_type==0){
     		    	 pitchCMD = PID_01.PID_001(CTRL_ERROR,ctrl_dt, P_GAIN, I_GAIN, D_GAIN, cmd_max, cmd_min)*deg2rad;
     		     } else if (ctrl_type==1){
     		    	 pitchCMD = PID_01.PID_002(CTRL_ERROR,ctrl_dt, P_GAIN, I_GAIN, D_GAIN);
     		     }
     		    // Check if controller provides usable values: 
     		   if(Double.isNaN(pitchCMD)) {System.out.println("Controller Error: Returned NaN.-> Controller OFF"); pitchCMD=0;}
     		//   if(1400<alt && alt<1500) {System.out.println(alt+" | "+target_velocity + " | "+ CTRL_ERROR + " | "+ throttle_cmd);}

     		   }
        	} else {
        		pitchCMD = 0; // Controller off 
        	}
    		return (-pitchCMD);
    	}
	public double get_Pitchover_cmd() {
		if(ctrl_on) {
			double turn_rate_rad = ctr_end_y; 
			if(engine_lost&&turn_rate_rad>0) {turn_rate_rad=turn_rate_rad*0.3;}
			if(engine_lost&&turn_rate_rad<0) {turn_rate_rad=turn_rate_rad*2;}
			if(turn_rate_rad<0) {
				if(tvc_switch){tvcwas = tvc_was;tvc_switch=false;}} else {tvcwas=0;}
				if(engine_lost) {pitchCMD=4.5*deg2rad;} else {pitchCMD= tvcwas + turn_rate_rad*CTRL_TIME;}
			double fpa_is = y_is*rad2deg;
			if(fpa_is>180) {
				pitchCMD = -5*deg2rad; 
			}
		}
		return pitchCMD; 
	}
	
	public double get_maintain_horizontal_Pitch_cmd() {
		double fpa_is = y_is*rad2deg;
	     CTRL_ERROR =  fpa_is ;
	     
	     //if(Math.abs(Math.abs(fpa_is)-180)>0.5){
	     if (ctrl_type==0){
	    	 pitchCMD = PID_01.PID_001(CTRL_ERROR,ctrl_dt, P_GAIN, I_GAIN, D_GAIN, cmd_max, cmd_min);
	    	 //System.out.println(CTRL_ERROR+" | "+ctrl_dt+" | "+ cmd_max+" | "+cmd_min +" | "+pitchCMD);
	     } else if (ctrl_type==1){
	    	 pitchCMD = PID_01.PID_002(CTRL_ERROR,ctrl_dt, P_GAIN, I_GAIN, D_GAIN);
	     }
	     //System.out.println(pitchCMD);
		return pitchCMD; 
	}
	
	public double get_FULL_reference_TVC_cmd() {
		//-------------------------------------------------------------------------------------------------
		//  							Full reference profile TVC control
		//-------------------------------------------------------------------------------------------------
		double fpa_is_rad = y_is;
		double alt_is_m = altitude_is; 
		double[] data_x = new double[reference_fpa_profile.length];
		double[] data_y1 = new double[reference_fpa_profile.length];
		double[] data_y2 = new double[reference_fpa_profile.length];
		for (int i=0;i<reference_fpa_profile.length;i++) {
			data_x[i]  = reference_fpa_profile[i][0];	// Altitude ref. mean [m]
			data_y1[i] = reference_fpa_profile[i][1];	// Reference curve for 36kN - FPA [rad]
			data_y2[i] = reference_fpa_profile[i][2];	// Reference curve for 30kN - FPA [rad]
		}
		//-------------------------------------------------------------------------------------------------
		double fpa_ideal_rad = 0 ;
		double t_split = ctr_end_x; 
		double y2 = Mathbox.LinearInterpolate( data_x , data_y2 , alt_is_m);
		double y1 = Mathbox.LinearInterpolate( data_x , data_y1 , alt_is_m);
		//System.out.println(""+global_time+" | "+ (y1-y2)*rad2deg);
		if(engine_lost&& t_engine_lost<t_split) {  fpa_ideal_rad = y2;}
		else 			{  fpa_ideal_rad = y1;}
		//-------------------------------------------------------------------------------------------------
		// If altitude is outside reference profile :
		if(alt_is_m>50000) {
			fpa_ideal_rad=180*rad2deg; 
		} else if (alt_is_m<-4200) {
			fpa_ideal_rad=90*rad2deg; 
		}
		//-------------------------------------------------------------------------------------------------
	     CTRL_ERROR = fpa_ideal_rad - fpa_is_rad ;
	     if (ctrl_type==0){
	    	 pitchCMD = PID_01.PID_001(CTRL_ERROR,ctrl_dt, P_GAIN, I_GAIN, D_GAIN, cmd_max, cmd_min)*deg2rad;
	     } else if (ctrl_type==1){
	    	 pitchCMD = PID_01.PID_002(CTRL_ERROR,ctrl_dt, P_GAIN, I_GAIN, D_GAIN);
	     }
		return pitchCMD; 
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
	public void Update_Flight_CTRL(boolean ctrl_on, CurrentDataSet currentDataSet, double ctr_init_x, double ctr_init_y, int ctrl_curve, double tvc_was, double CTRL_Thrust, boolean engine_loss_detected) {
		this.ctrl_on 	  = ctrl_on;
		if(this.ctrl_ID==0) {this.ctrl_on=false;} // overwrite : controller off with no FC set
		this.y_is 		  = currentDataSet.getEulerAngle()[1][0];
		this.altitude_is  = currentDataSet.getxIS()[2]-rm-elevation;
        this.ctr_init_x   = ctr_init_x;
        this.ctr_init_y   = ctr_init_y;
		this.ctrl_curve   = ctrl_curve; 
		this.ctrl_dt	      = currentDataSet.getValDt(); 
		this.tvc_was      = tvc_was; 
		this.global_time  = currentDataSet.gettIS(); 
		
		if(tswitch) {this.tzero=global_time;tswitch=false;}
		this.CTRL_TIME = global_time-tzero; 
		
		this.x_is 		  = CTRL_TIME;
		
		if(this.CTRL_Thrust>CTRL_Thrust&&engine_loss_detected==false) {engine_lost=true; t_engine_lost=global_time;	} 
		else if(engine_loss_detected) {engine_lost=true;}
		this.CTRL_Thrust=CTRL_Thrust;
	}
	public void set_ctrl_on(boolean NewValue){
		ctrl_on = NewValue;
	}
	public void set_ctrl_curve(int NewValue) {
		ctrl_curve = NewValue;
	}
	public void set_ctrl_dt(double NewValue) {
		ctrl_dt = NewValue; 
	}	
	public double get_ctr_end_y() {
		return ctr_end_y; 
	}
	public boolean get_engine_lost() {
		return engine_lost;
	}
	public double get_altitude() {
		return altitude_is;
	}
	public double get_ctrl_type() {
		return ctrl_type; 
	}
	public double get_global_time() {
		return global_time; 
	}
	public double get_t_engine_lost() {
		return t_engine_lost; 
	}

	public double getPitchCMD() {
		return pitchCMD;
	}

	
}