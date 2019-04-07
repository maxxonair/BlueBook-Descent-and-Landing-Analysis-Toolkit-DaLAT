package Simulator_main; 

import static java.lang.Math.cos;
import static java.lang.Math.sin;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import Model.AtmosphereModel;
import Model.GravityModel;
import Model.atm_dataset;
import Sequence.SequenceElement;
import Toolbox.Tool;
import Controller.Flight_CTRL_ThrustMagnitude;
import Controller.Flight_CTRL_PitchCntrl;
import Controller.LandingCurve;

public class Ascent_3DOF implements FirstOrderDifferentialEquations {
		//----------------------------------------------------------------------------------------------------------------------------
		//				Control variables
		//----------------------------------------------------------------------------------------------------------------------------
		public static boolean HoverStop = true; 
	    public static boolean ctrl_callout = false; 
		//............................................                                       .........................................
		//
	    //	                                                         Constants
		//
		//----------------------------------------------------------------------------------------------------------------------------
	    public static double[][] DATA_MAIN = { // RM (average radius) [m] || Gravitational Parameter [m3/s2] || Rotational speed [rad/s] || Average Collision Diameter [m]
				{6371000,3.9860044189E14,7.2921150539E-5,1.6311e-9}, 	// Earth
				{1737400,4.90486959E12,2.661861E-6,320},						// Moon (Earth)
				{3389500,4.2838372E13,7.0882711437E-5,1.6311e-9},		// Mars
				{0,0,0,0},												// Venus
		 };
	    public static double PI = 3.141592653589793238462643383279502884197169399375;
	    public static int[] trigger_type_translator = {0,2,3};
	    	public static String[] str_target = {"Earth","Moon","Mars","Venus"};
	    	static double deg2rad = PI/180.0; 		//Convert deg2radrees to radians
	    	static double rad2deg = 180.0/PI; 		//Convert radians to deg2radrees
		//............................................                                       .........................................
		//
	    //	                                                         File Paths
		//
		//----------------------------------------------------------------------------------------------------------------------------
	   	public static String[] IntegratorInputPath = {"/INP/INTEG/00_DormandPrince853Integrator.inp",
					  "/INP/INTEG/01_ClassicalRungeKuttaIntegrator.inp",
					  "/INP/INTEG/02_GraggBulirschStoerIntegrator.inp",
					  "/INP/INTEG/03_AdamsBashfordIntegrator.inp"
	   					};
	    public static String PropulsionInputFile    = "/INP/PROP/prop.inp"  ;  		// Input: target and environment
	    
		//............................................                                       .........................................
		//
	    //	                                             Simulator public variables
		//
		//----------------------------------------------------------------------------------------------------------------------------
		    public static double tminus=0;
		    public static double tis=0;
		    public static double mminus=0;
		    public static double vminus=0;
		    public static double val_dt=0;
		    public static boolean switcher=true; 
		    public static double cmd_min = 0;
		    public static double cmd_max = 0;
		    public static boolean cntrl_on ; 
		    public static double acc_deltav = 0; 
		    public static double twrite=0;
		    public static double ref_ELEVATION = 0;
	 	//----------------------------------------------------------------
			public static double Lt = 0;    		// Average collision diameter (CO2)         [m]
			public static double mu    = 0;    	    // Standard gravitational constant (Mars)   [m3/s2]
			public static double rm    = 0;    	    // Planets average radius                   [m]
			public static double omega = 0 ;        // Planets rotational rate                  [rad/sec]
			public static double g0 = 9.81;         // For normalized ISP 			
			public static double gn = 0;
			public static double gr = 0;
			public static double D = 0;
			public static double L = 0;
			public static double Ty = 0; 
		    public static double qinf=0;
		    public static double S =0;
		    public static double bank =0;
		    public static double CdPar=0;
		    public static double Thrust=0;
		    public static double Cl=0;
		    public static double Cd=0;
		    public static double Spar=0;
		    public static double rho =0;
		    public static double gamma =0;
		    public static double R=0;
		    public static double Ma=0;
		    public static double T=0;
		    public static double CdC=0;
		    public static double ISP = 0 ; 
		    public static double P = 0 ;      			// static pressure [Pa]
		    public static double cp = 0;				// 
		    public static int flowzone=0; 				// Flow zone continuum - transitional - free molecular flwo
		    public static double Cdm = 0; 				// Drag coefficient in contiuum flow; 
		    public static int TARGET=0;					// Target body index
		    public static double Throttle_CMD=0;				// Main engine throttle command [-]
		    public static double m_propellant_init = 0;     	// Initial propellant mass [kg]
		    public static double M0=0; 
		    public static double Thrust_max=0; 
		    public static double Thrust_min=0;
		    public static double cntr_h_init=0;
		    public static double cntr_v_init=0;
		    public static double cntr_t_init=0;
		    public static double cntr_fpa_init=0;
		    public static int ctrl_curve;
	        private static List<atm_dataset> ATM_DATA = new ArrayList<atm_dataset>(); 
	        private static List<SequenceElement> SEQUENCE_DATA_main = new ArrayList<SequenceElement>(); 
	        private static List<StopCondition> STOP_Handler = new ArrayList<StopCondition>(); 
	        private static List<Flight_CTRL_ThrustMagnitude> Flight_CTRL_ThrustMagnitude = new ArrayList<Flight_CTRL_ThrustMagnitude>(); 
	        private static List<Flight_CTRL_PitchCntrl> Flight_CTRL_PitchCntrl = new ArrayList<Flight_CTRL_PitchCntrl>(); 
	        private static ArrayList<String> CTRL_steps = new ArrayList<String>();
	        static boolean PROPread = false; 
	        public static int active_sequence = 0 ; 
	        public static double ctrl_vel =0;			// Active Flight Controller target velocity [m/s]
	        public static double ctrl_alt = 0 ; 		// Active Flight Controller target altitude [m]
	        public static double v_touchdown=0; 		// Global touchdown velocity constraint [m/s]
	        public static boolean isFirstSequence=true;
	        public static boolean Sequence_RES_closed=false;
	        public static double groundtrack = 0; 
	        public static double phimin=0;
	        public static double tetamin=0;
	        
	        public static double Xfo = 0 ;
	        public static double Yfo = 0 ; 
	        public static double Zfo = 0 ; 
	        
	        static double azimuth_inertFrame = 0 ;
	    	static double fpa_inertFrame     = 0 ;
	    	static double vel_inertFrame     = 0 ;
	    	
	    	public static double fpa_dot =0;
	        
	        public static double Thrust_Deviation=0; 
	        public static double Thrust_Elevation=0;
	        public static double Thrust_Deviation_mo = 0; 
	        public static double Thrust_Deviation_dot =0;
	        public static double TE_save =0;
	        public static double const_tzer0=0;
	        public static boolean const_isFirst =true; 
	        
	        public static double t_engine_loss = 0; 			// [s]
	        public static double thrust_loss_perc = 0;		// Thrust loss due to engine loss [%]
	        public static boolean engine_loss_switch=true; 
	        
	        public static boolean engine_loss_indicator=false;
	        
	        public static double TTM_max = 5.0;
	        
	        static DecimalFormat decf = new DecimalFormat("###.#");
	        static DecimalFormat df_X4 = new DecimalFormat("#.###");
	      //----------------------------------------------------------------------------------------------------------------------------
	    public int getDimension() {
	        return 7;
	    }
	    public static void Velocity_Rotating2Inertial(double[] x) {
	    	try {
	    	double vel_rotating = x[3];
	    	double fpa_rotating = x[4];
	    	double azi_rotating = x[5];
	    	double lat_rotating = x[1];
	    	double radius = x[2];
	    	 azimuth_inertFrame = Math.atan((vel_rotating*Math.cos(fpa_rotating)*Math.sin(azi_rotating)+omega*radius+Math.cos(lat_rotating))/(vel_rotating*Math.cos(fpa_rotating)*Math.cos(azi_rotating)));
	    	 fpa_inertFrame = Math.atan(Math.tan(fpa_rotating)*Math.cos(azimuth_inertFrame)/Math.cos(azi_rotating));
	    	 vel_inertFrame = vel_rotating * Math.sin(fpa_rotating)/Math.sin(fpa_inertFrame);
	    	} catch(java.lang.NumberFormatException enfe) {System.out.println(enfe);}
	    }
	    public static void SequenceWriteOut_addRow() {
			CTRL_steps.add(SEQUENCE_DATA_main.get(active_sequence).get_sequence_ID()+ " " + 
				   SEQUENCE_DATA_main.get(active_sequence).get_sequence_type()+" "+
			       SEQUENCE_DATA_main.get(active_sequence).get_sequence_controller_ID()+ " "+
			       cntr_v_init+" "+
			       cntr_h_init+" "+
			       Flight_CTRL_ThrustMagnitude.get(active_sequence).get_ctrl_vel()+" "+
			       Flight_CTRL_ThrustMagnitude.get(active_sequence).get_ctrl_alt()+" "+
			       SEQUENCE_DATA_main.get(active_sequence).get_ctrl_target_curve()+" "+
			       SEQUENCE_DATA_main.get(active_sequence).get_TVC_ctrl_target_curve()  +" "+
			       cntr_fpa_init+" "+
			       SEQUENCE_DATA_main.get(active_sequence).get_TVC_ctrl_target_time()+" "+
			       SEQUENCE_DATA_main.get(active_sequence).get_TVC_ctrl_target_fpa()+" "
		  );
	    }
		public static void SET_Constants(int TARGET) throws IOException{
		    Lt    = DATA_MAIN[TARGET][3];    	// Average collision diameter (CO2)         [m]
		    mu    = DATA_MAIN[TARGET][1];    	// Standard gravitational constant          []
		    rm    = DATA_MAIN[TARGET][0];    	// Planets mean radius                      [m]
		    omega = DATA_MAIN[TARGET][2];		// Planets rotational speed     		    [rad/s]
		}
		
		public static boolean GroundClearance_Manager(double[] x) {
	    	boolean GT_switch=false;
	    	if(x[2]-rm-ref_ELEVATION>300){GT_switch=true;}
	    	return GT_switch;
		}
		public static void UPDATE_FlightController_ThrustMagnitude(Flight_CTRL_ThrustMagnitude NewElement){	   
			   if (Flight_CTRL_ThrustMagnitude.size()==0){ Ascent_3DOF.Flight_CTRL_ThrustMagnitude.add(NewElement); 
			   } else {Ascent_3DOF.Flight_CTRL_ThrustMagnitude.add(NewElement); } 
		   }
		public static void UPDATE_FlightController_PitchControl(Flight_CTRL_PitchCntrl NewElement){	   
			   if (Flight_CTRL_PitchCntrl.size()==0){ Ascent_3DOF.Flight_CTRL_PitchCntrl.add(NewElement); 
			   } else {Ascent_3DOF.Flight_CTRL_PitchCntrl.add(NewElement); } 
		   }
		public static void INITIALIZE_FlightController(double[]x) {
			for(int i=0;i<SEQUENCE_DATA_main.size();i++) {
				int ctrl_ID = SEQUENCE_DATA_main.get(i).get_sequence_controller_ID()-1;
				 ctrl_vel = SEQUENCE_DATA_main.get(i).get_ctrl_target_vel();
				 ctrl_alt = SEQUENCE_DATA_main.get(i).get_ctrl_target_alt();
				 double ctrl_fpa = SEQUENCE_DATA_main.get(i).get_TVC_ctrl_target_fpa();
				 double ctrl_tend = SEQUENCE_DATA_main.get(i).get_TVC_ctrl_target_time();
				 int TVC_ctrl_ID = SEQUENCE_DATA_main.get(i).get_sequence_TVCController_ID()-1;
				// -> Create new Flight controller 
				 Flight_CTRL_ThrustMagnitude NewFlightController_ThrustMagnitude = new Flight_CTRL_ThrustMagnitude(ctrl_ID, true, x, 0,  m_propellant_init,  cntr_v_init,  cntr_h_init,  -1,   ctrl_vel, ctrl_alt,  Thrust_max,  Thrust_min,  0,  0,  ctrl_curve,  val_dt,0,0,0,0,0, rm, ref_ELEVATION);
				UPDATE_FlightController_ThrustMagnitude(NewFlightController_ThrustMagnitude);
				
				Flight_CTRL_PitchCntrl NewFlightController_PitchCntrl = new Flight_CTRL_PitchCntrl( TVC_ctrl_ID, true, -1, 0, ctrl_tend, ctrl_fpa, rm, ref_ELEVATION);
				UPDATE_FlightController_PitchControl(NewFlightController_PitchCntrl);
			}
		}
		public static void SEQUENCE_MANAGER(double t, double[] x) {
			//-------------------------------------------------------------------------------------------------------------
			//		 WriteOut conditions at sequence to sequence hand-over: 
			//-------------------------------------------------------------------------------------------------------------
	    	if(active_sequence<SEQUENCE_DATA_main.size()-1) {
				int trigger_type = SEQUENCE_DATA_main.get(active_sequence).get_trigger_end_type();
				double trigger_value = SEQUENCE_DATA_main.get(active_sequence).get_trigger_end_value();
				if(isFirstSequence) {
					cntr_v_init = x[3];
					cntr_h_init = x[2]-rm-ref_ELEVATION;
					cntr_t_init = t;
					cntr_fpa_init = x[4];
					SequenceWriteOut_addRow();
					isFirstSequence=false; 
				}
				//System.out.println(Flight_CTRL_ThrustMagnitude.get(active_sequence).get_CTRL_TIME()+" | "+trigger_value);
				if(trigger_type==0) {
						if(Flight_CTRL_ThrustMagnitude.get(active_sequence).get_CTRL_TIME()>trigger_value) {
							active_sequence++;
							cntr_v_init = x[3];
							cntr_h_init = x[2]-rm-ref_ELEVATION;
							cntr_t_init = t;
							cntr_fpa_init = x[4];
							SequenceWriteOut_addRow();
						}
				} else if (trigger_type==1) {
						if( (x[2]-rm-ref_ELEVATION)>trigger_value) {
							active_sequence++;
							cntr_v_init = x[3];
							cntr_h_init = x[2]-rm-ref_ELEVATION;
							cntr_t_init = t;
							cntr_fpa_init = x[4];
							SequenceWriteOut_addRow();}
				} else if (trigger_type==2) {
						if( x[3]>trigger_value) {
							active_sequence++;
							cntr_v_init = x[3];
							cntr_h_init = x[2]-rm-ref_ELEVATION;
							cntr_t_init = t;
							cntr_fpa_init = x[4];
							SequenceWriteOut_addRow();}
	     		} else if (trigger_type==3) {
					if(  (Thrust_Deviation)<trigger_value || Flight_CTRL_ThrustMagnitude.get(active_sequence).get_CTRL_TIME()>130) {
						active_sequence++;
						cntr_v_init = x[3];
						cntr_h_init = x[2]-rm-ref_ELEVATION;
						cntr_t_init = t;
						cntr_fpa_init = x[4];
						SequenceWriteOut_addRow();}
     		}
	    	}
	    	//-------------------------------------------------------------------------------------------------------------
	    	//                   Last Sequence reached -> write SEQU.res
	    	//-------------------------------------------------------------------------------------------------------------
	    	if(active_sequence ==  (SEQUENCE_DATA_main.size()-1) && Sequence_RES_closed==false){
				cntr_v_init = x[3];
				cntr_h_init = x[2]-rm-ref_ELEVATION;
				cntr_t_init = t;
				cntr_fpa_init = x[4];
	    		System.out.println("Write: Sequence result file ");
	    		try {
	            String resultpath="";
	            	String dir = System.getProperty("user.dir");
	            	resultpath = dir + "/SEQU.res";
	            PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
	            for(String step: CTRL_steps) {
	                writer.println(step);
	            }
	            writer.close();
	            Sequence_RES_closed=true; 
	        } catch(Exception e) {};
	    	}
	    	//System.out.println("Altitude "+decf.format((x[2]-rm))+" | " + active_sequence);
	    	int sequence_type_TM = SEQUENCE_DATA_main.get(active_sequence).get_sequence_type();
	 //System.out.println(sequence_type_TM);
	    	Flight_CTRL_ThrustMagnitude.get(active_sequence).Update_Flight_CTRL( true, x, M0, m_propellant_init,  cntr_v_init,  cntr_h_init,  t,  SEQUENCE_DATA_main.get(active_sequence).get_ctrl_target_vel(),SEQUENCE_DATA_main.get(active_sequence).get_ctrl_target_alt(),  Thrust_max,  Thrust_min,  SEQUENCE_DATA_main.get(active_sequence).get_ctrl_target_curve(),  val_dt) ;
	    	
	    	Flight_CTRL_PitchCntrl.get(active_sequence).Update_Flight_CTRL(true, x, t, cntr_t_init, cntr_fpa_init, SEQUENCE_DATA_main.get(active_sequence).get_TVC_ctrl_target_curve(), val_dt,Thrust_Deviation, Thrust_max, engine_loss_indicator);	    	
	    	// ((boolean) Flight_CTRL_PitchCntrl.get(active_sequence).get_engine_lost() ? 1 : 0)
	    	if(Flight_CTRL_PitchCntrl.get(active_sequence).get_engine_lost()&&engine_loss_indicator==false) {engine_loss_indicator=true;}
	    	// Check sync. controller timers 
	    	// System.out.println(Flight_CTRL_ThrustMagnitude.get(active_sequence).get_CTRL_TIME()-Flight_CTRL_PitchCntrl.get(active_sequence).get_CTRL_TIME());
	    	if(sequence_type_TM==3) { // Controlled Flight Sequence 
			    	//-------------------------------------------------------------------------------------------------------------	
			    	//                          Flight Controller ON - Controlled Fight Sequence
			    	//-------------------------------------------------------------------------------------------------------------
			    	if (ctrl_callout) {System.out.println("Altitude "+decf.format((x[2]-rm))+" | Controller " + Flight_CTRL_ThrustMagnitude.get(active_sequence).get_ctrl_ID() +" set ON");}
			    	//Thrust        = Flight_CTRL_ThrustMagnitude.get(active_sequence).get_thrust_cmd();
			    	//Throttle_CMD  = Flight_CTRL_ThrustMagnitude.get(active_sequence).get_ctrl_throttle_cmd();
			    	Thrust = Thrust_max; 
			    	Thrust_Deviation = Flight_CTRL_PitchCntrl.get(active_sequence).get_Pitchover_cmd();
	    	} else if (sequence_type_TM==2) { // Continuous propulsive Flight Sequence 
			    	//-------------------------------------------------------------------------------------------------------------	
			    	//                          TM-FC OFF | TVC-FC ON - Continuous thrust
			    	//-------------------------------------------------------------------------------------------------------------
	    		   Thrust_Deviation = 0.0;
	    		  double  fpa_is=x[4]*rad2deg;
		   			if(fpa_is>180) {
		   				Thrust_Deviation = -15*deg2rad; 
					}
		    		if((m_propellant_init-(M0-x[6]))>0) {
		    			//System.out.println((m_propellant_init-(M0-x[6])));
		    			Thrust = Thrust_max; 
		    			Throttle_CMD = 1; 
		    		}else { // Empty tanks
		        		Thrust = 0; 
		        		Throttle_CMD = 0; 
		    		}
		    		
	    	} else if (sequence_type_TM==1) { // Coasting Sequence 
			    	//-------------------------------------------------------------------------------------------------------------	
			    	//                          Flight Controller OFF - Coasting - Thrust OFF
			    	//-------------------------------------------------------------------------------------------------------------
	    		   Thrust_Deviation=0;
		    		Thrust = 0; 
		    		Throttle_CMD = 0;
	    	} else if (sequence_type_TM==4) { // Constrained continuous thrust 
			    	//-------------------------------------------------------------------------------------------------------------	
			    	//                Flight Controller OFF - Uncontrolled/Constrained, continuous thrust TTM constraint
			    	//-------------------------------------------------------------------------------------------------------------
	    		    if(const_isFirst){const_tzer0=t;}
		    		if((TTM_max * x[6])>Thrust_max) {
		    			Thrust = Thrust_max;
		    		} else {
		    			Thrust = TTM_max * x[6]; 
		    		}
		    			Throttle_CMD = Thrust/Thrust_max;
	    	} else if (sequence_type_TM==5) {
				    	//-------------------------------------------------------------------------------------------------------------	
				    	//            Flight Controller OFF - Uncontrolled/Constrained, continuous thrust Thrust vector turn
				    	//-------------------------------------------------------------------------------------------------------------
		    	Thrust = Thrust_max; 
		    	Thrust_Deviation = Flight_CTRL_PitchCntrl.get(active_sequence).get_maintain_horizontal_TVC_cmd();

	    		
	    	} else if (sequence_type_TM==6) {
			    	//-------------------------------------------------------------------------------------------------------------	
			    	//            Full Reference control
			    	//-------------------------------------------------------------------------------------------------------------
	    		//double ctr_time_0 = Flight_CTRL_ThrustMagnitude.get(active_sequence-1).get_CTRL_TIME();
		    	Thrust = Thrust_max; 
		    	Thrust_Deviation = Flight_CTRL_PitchCntrl.get(active_sequence).get_FULL_reference_TVC_cmd();
    		
    	}
	    	else { System.out.println("ERROR: Sequence type out of range");}
		} 
		
		
		public static void GRAVITY_MANAGER(double[] x) {
			//------------------------------------------------------------------------------
			//     simplified 2D atmosphere model (J2 only) 
			//------------------------------------------------------------------------------
	    	gr = GravityModel.get_gr( x[2],  x[1],  rm,  mu, TARGET);
	    	gn = GravityModel.get_gn(x[2], x[1],  rm,  mu, TARGET); 
		}
		
		public static void ATMOSPHERE_MANAGER(double[] x) {
	    	if (x[2]-rm>200000 || TARGET == 1){ // In space conditions: 
	    		// Set atmosphere properties to zero: 
		    		rho   = 0; 																// Density 							[kg/m3]
		    		qinf  = 0;																// Dynamic pressure 				[Pa]
		    		T     = 0 ; 															// Temperature 						[K]
		    		gamma = 0 ; 															// Heat capacity ratio 				[-]
		    		R	  = 0; 																// Gas constant 					[J/kgK]
		    		Ma 	  = 0; 																// Mach number 						[-]
		      	//----------------------------------------------------------------------------------------------
	    	} else { // In atmosphere conditions (if any)
		    	 rho    = AtmosphereModel.atm_read(1, x[2] - rm ) ;       					// density                         [kg/m3]
		    	 qinf   = 0.5 * rho * ( x[3] * x[3]) ;               		         		// Dynamic pressure                [Pa]
		    	 T      = AtmosphereModel.atm_read(2, x[2] - rm) ;                   		// Temperature                     [K]
		    	 gamma  = AtmosphereModel.atm_read(4, x[2] - rm) ;              	        // Heat capacity ratio			   [-]
		    	 R      = AtmosphereModel.atm_read(3,  x[2] - rm ) ;                        // Gas Constant                    [J/kgK]
		    	 P      = rho * R * T;														// Ambient pressure 			   [Pa]
		    	 Ma     = x[3] / Math.sqrt( T * gamma * R);                  		 		// Mach number                     [-]
	    	     //CdPar  = load_Cdpar( x[3], qinf, Ma, x[2] - rm);   		             	// Parachute Drag coefficient      [-]
	    	     CdC    = AtmosphereModel.get_CdC(x[2]-rm,0);                       		// Continuum flow drag coefficient [-]
		    	 Cd 	= AtmosphereModel.load_Drag(x[3], x[2]-rm, P, T, CdC, Lt, R);    	// Lift coefficient                [-]
		    	 S 		= 4;																// Projected surface area 		   [m2]
		     	//-----------------------------------------------------------------------------------------------
		    	 D  = - qinf * S * Cd  - Thrust ;//- qinf * Spar * CdPar;        			// Aerodynamic drag Force 		   [N]
		    	 L  =   qinf * S * Cl * cos( bank ) ;                            			// Aerodynamic lift Force 		   [N]
		    	 Ty =   qinf * S * Cl * sin( bank ) ;                            			// Aerodynamic side Force 		   [N]
		    	//----------------------------------------------------------------------------------------------
		    	  flowzone = AtmosphereModel.calc_flowzone(x[3], x[2]-rm, P, T, Lt);        // Continous/Transition/Free molecular flow [-]
	    	}
		}
		
    public void computeDerivatives(double t, double[] x, double[] dxdt) {
    	//-------------------------------------------------------------------------------------------------------------------
    	//								    	Artificial engine loss scenario
    	//-------------------------------------------------------------------------------------------------------------------
    	if(t>t_engine_loss && engine_loss_switch) {Thrust_max = Thrust_max * (1-thrust_loss_perc);engine_loss_switch=false;}
    	//-------------------------------------------------------------------------------------------------------------------
    	//								    	Gravitational environment
    	//-------------------------------------------------------------------------------------------------------------------
    	GRAVITY_MANAGER(x);
    	//-------------------------------------------------------------------------------------------------------------------
    	//								Sequence management and Flight controller 
    	//-------------------------------------------------------------------------------------------------------------------
    	SEQUENCE_MANAGER(t,  x);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Atmosphere
    	//-------------------------------------------------------------------------------------------------------------------
    	ATMOSPHERE_MANAGER(x);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    Force Definition - Aerodynamik Forces | Kinematic Frame |
    	//-------------------------------------------------------------------------------------------------------------------
    	D  = - qinf * S * Cd ;     			    // Aerodynamic drag Force 		   [N]
	   	L  =   qinf * S * Cl * cos( bank ) ;    // Aerodynamic lift Force 		   [N]
	   	Ty =   qinf * S * Cl * sin( bank ) ;    // Aerodynamic side Force 		   [N]
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    		Force Definition  | BodyFixed Frame |
    	//-------------------------------------------------------------------------------------------------------------------
	   	double fT = Thrust;
	   	 Xfo = fT*Math.cos(Thrust_Deviation)-D;
	   	 Yfo = 0;
	   	 Zfo = fT*Math.sin(Thrust_Deviation); 
    	//-------------------------------------------------------------------------------------------------------------------
    	// 										Delta-v integration
    	//-------------------------------------------------------------------------------------------------------------------
    	acc_deltav = acc_deltav + ISP*g0*Math.log(mminus/x[6]);
    	mminus=x[6];
    	//-------------------------------------------------------------------------------------------------------------------
    	// 										Thrust Elevation rate 
    	//-------------------------------------------------------------- -----------------------------------------------------
    	double TE = (PI-x[4]-Thrust_Deviation);
    	Thrust_Deviation_dot = (TE - Thrust_Deviation_mo)/val_dt;
    	Thrust_Deviation_mo = TE; 
    	//-------------------------------------------------------------------------------------------------------------------
    	// 										  Ground track 
    	//-------------------------------------------------------------------------------------------------------------------
    	double r=rm;  // <-- reference radius for projection. Current projection set for mean radius 
    	double phi=x[0];
    	double theta = x[1];
        double dphi = Math.abs(phi-phimin);
    	double dtheta = Math.abs(theta-tetamin); 
    	double ds = r*Math.sqrt(LandingCurve.squared(dphi) + LandingCurve.squared(dtheta));
    	//System.out.println(ds);
    	groundtrack = groundtrack + ds;
    	phimin=phi;
    	tetamin=theta; 
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									     Equations of Motion - Preparations
    	//-------------------------------------------------------------------------------------------------------------------
    	double cos_fpa = Math.cos(x[4]); 
    	double sin_fpa = Math.sin(x[4]);
    	double tan_fpa = Math.tan(x[4]);
    	double cos_A = Math.cos(x[5]);
    	double sin_A = Math.sin(x[5]);
    	double omega_sqr = omega * omega; 
    	double cos_lat = Math.cos(x[1]);
    	double sin_lat = Math.sin(x[1]);
    	double tan_lat = Math.tan(x[1]);

    	//-------------------------------------------------------------------------------------------------------------------
    	// 									     Equations of Motion
    	//-------------------------------------------------------------------------------------------------------------------	
    	// Position vector
	    dxdt[0] = x[3] * cos_fpa * sin_A / ( x[2] * cos_lat );
	    dxdt[1] = x[3] * cos_fpa * cos_A / x[2] ;
	    dxdt[2] = x[3] * sin_fpa;
	    // Velocity vector
	    dxdt[3] = - gr * sin_fpa + gn * cos_A * cos_fpa + Xfo / x[6] + omega_sqr * x[2] * cos_lat  * ( sin_fpa * cos_lat - cos_fpa* cos_A * sin_lat) ;
	    if(GroundClearance_Manager(x)==false) { // Get ground clearance until condition is met
	    	dxdt[4]=0;
	    	dxdt[5]=0;
	    } else {
	    	dxdt[4] = (x[3]/x[2]-gr/x[3])*cos_fpa-gn*cos_A*sin_fpa/x[3]+Zfo/(x[3]*x[6])+2*omega*sin_A*cos_lat+omega_sqr*x[2]*cos_lat*(cos_fpa*cos_lat+sin_fpa*cos_A*sin_lat)/x[3];
	    			if(Math.abs(x[4])<1E-6) {
	    				dxdt[5] = x[3] * sin_A * tan_lat * cos_fpa/x[2]-gn*sin_A/x[3]-Yfo/(x[3]*cos_fpa*x[6])-2*omega*(tan_fpa * cos_A * cos_lat - sin_lat) + omega_sqr * x[2] * sin_A*sin_lat*cos_lat/(x[3]*cos_fpa);
	    			} else {
	    				dxdt[5] =	0;
	    			}
	    }
	    dxdt[6] = - Thrust/(ISP*g0) ;   
    	//-------------------------------------------------------------------------------------------------------------------
    	// 								hah Update Event handler
    	//-------------------------------------------------------------------------------------------------------------------
	    for(int i=0;i<STOP_Handler.size();i++) {STOP_Handler.get(i).Update_StopCondition(t, x);}
    	//-------------------------------------------------------------------------------------------------------------------
    	// 								Variable conversion rotating to celestial frame 
    	//-------------------------------------------------------------------------------------------------------------------
	    fpa_dot = dxdt[4];
	    Velocity_Rotating2Inertial(x);
}
    
public static void Launch_Integrator( int INTEGRATOR, int target, double x0, double x1, double x2, double x3, double x4, double x5, double x6, double t, double dt_write, double reference_elevation, List<SequenceElement> SEQUENCE_DATA, List<StopCondition> Event_Handler, double[] engine_off){
//----------------------------------------------------------------------------------------------
// 						Prepare integration 
//----------------------------------------------------------------------------------------------
   	 String dir = System.getProperty("user.dir");
     PropulsionInputFile = dir + PropulsionInputFile;
	try {
			SET_Constants(target);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
//----------------------------------------------------------------------------------------------
//   Read propulsion setting:	Propulsion/Controller INIT
		double[] prop_read;
	    cntr_h_init=x2-rm;
	    cntr_v_init=x3;
		try {
		 prop_read = Tool.READ_PROPULSION_INPUT(PropulsionInputFile);
	    	 ISP          	  	= prop_read[0];
	    	 m_propellant_init	= prop_read[1];
	    	 Thrust_max 	  		= prop_read[2];
	    	 Thrust_min		  	= prop_read[3];
	    	 M0 			  		= x6  ; 
	    	 mminus			  = M0  ;
	    	 vminus			  = x3  ;
	    	 v_touchdown	  = 0   ;
	    	 PROPread		  = true; 
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("Error: Propulsion setting read failed. ISP set to zero. Propellant mass set to zero. Thrust set to zero. ");
			PROPread =false;
		}
    	 phimin=x0;
    	 tetamin=x1;
    	 groundtrack=0;
    	 ref_ELEVATION =  reference_elevation;
    	 
    	 if(engine_off[0]==0) {
    		 engine_loss_switch=true;
    	 } else {
    		 engine_loss_switch=false; 
    	 }
    	 t_engine_loss = engine_off[1];
    	 System.out.println(""+t_engine_loss);
    	 thrust_loss_perc = engine_off[2];
//----------------------------------------------------------------------------------------------
//					Sequence Setup	
//----------------------------------------------------------------------------------------------
		Sequence_RES_closed=false;
		SEQUENCE_DATA_main = SEQUENCE_DATA;  // Sequence data handover
		CTRL_steps.clear();
//----------------------------------------------------------------------------------------------
//					Integrator setup	
//----------------------------------------------------------------------------------------------
		FirstOrderIntegrator dp853;
		String IntegInput ="";
			//String dir = System.getProperty("user.dir");
			IntegInput = dir + IntegratorInputPath[INTEGRATOR];
		try {
			double[] IntegINP = Tool.READ_INTEGRATOR_INPUT(IntegInput);
		if (INTEGRATOR == 1) {
	         dp853 = new ClassicalRungeKuttaIntegrator(IntegINP[0]);
		} else if (INTEGRATOR == 0) {
	         dp853 = new DormandPrince853Integrator(IntegINP[0], IntegINP[1], IntegINP[2], IntegINP[3]);
		} else if (INTEGRATOR ==2){
			dp853 = new GraggBulirschStoerIntegrator(IntegINP[0], IntegINP[1], IntegINP[2], IntegINP[3]);
		} else if (INTEGRATOR == 3){
			dp853 = new AdamsBashforthIntegrator((int) IntegINP[0], IntegINP[1], IntegINP[2], IntegINP[3], IntegINP[4]);
		} else {
			// Default Value
			System.out.println("Integrator index out of range");
			System.out.println("Fallback to standard setting: DormandPrince853Integrator(1.0e-8, 1.0, 1.0e-10, 1.0e-10)");
			 dp853 = new DormandPrince853Integrator(1.0e-8, 1.0, 1.0e-10, 1.0e-10);
		}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.out.println(e2);
			System.out.println("Integrator settings read failed");
			System.out.println("Fallback to standard setting: DormandPrince853Integrator(1.0e-8, 1.0, 1.0e-10, 1.0e-10)");
			dp853 = new DormandPrince853Integrator(1.0e-8, 1.0, 1.0e-10, 1.0e-10);
		}
//----------------------------------------------------------------------------------------------
	        FirstOrderDifferentialEquations ode = new Ascent_3DOF();
	        //------------------------------
	        ATM_DATA.removeAll(ATM_DATA);
	        try {
				ATM_DATA = AtmosphereModel.INITIALIZE_ATM_DATA(target);
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  			for (int i = 0;i<ATM_DATA.size();i++){
				//System.out.println(ATM_DATA.get(i).get_altitude() + " | " + ATM_DATA.get(i).get_density());
			}
//----------------------------------------------------------------------------------------------
  			// 						Result vector setup - do not touch
	        double[] y = new double[7]; // Result vector
	// Position 
	        y[0] = x0;
	        y[1] = x1;
	        y[2] = x2;
	// Velocity
	        y[3] = x3;
	        y[4] = x4;
	        y[5] = x5;
	// S/C Mass        
	        y[6] = x6;
			INITIALIZE_FlightController(y) ;
//----------------------------------------------------------------------------------------------
	        StepHandler WriteOut = new StepHandler() {

	            ArrayList<String> steps = new ArrayList<String>();

	            public void init(double t0, double[] y0, double t) {

	            }
	            
	            public void handleStep(StepInterpolator interpolator, boolean isLast) {
	                double   t = interpolator.getCurrentTime();
	                if(switcher) {tis=t;switcher=false;}else {tminus=t;switcher=true;};if(tis!=tminus) {val_dt=Math.abs(tis-tminus);}else{val_dt=0.01;};
	                double[] y     = interpolator.getInterpolatedState();
	                double[] ymo   = interpolator.getInterpolatedDerivatives();
	                double g_total = Math.sqrt(gr*gr+gn*gn);
	                double E_total = y[6]*(g_total*(y[2]-rm)+0.5*y[3]*y[3]);
	                double CTRL_TM_Error =0;
	                double CTRL_TVC_Error =0;
	                double CTRL_Time =0;
	                if(SEQUENCE_DATA_main.get(active_sequence).get_sequence_type()==3) {CTRL_TM_Error=Flight_CTRL_ThrustMagnitude.get(active_sequence).get_CTRL_ERROR();}
	                CTRL_TVC_Error=Flight_CTRL_PitchCntrl.get(active_sequence).get_CTRL_ERROR();  
	                CTRL_Time=Flight_CTRL_ThrustMagnitude.get(active_sequence).get_CTRL_TIME();
	                if( t > twrite ) {
	                	twrite = twrite + dt_write; 
	                    steps.add(t + " " + 
	                    		  y[0] + " " + 
	                    		  y[1] + " " + 
	                    		  (y[2]-rm-ref_ELEVATION) + " " + 
	                    		  (y[2]-rm)+ " " + 
	                    		  y[2] + " " + 
	                    		  y[3]+ " " + 
	                    		  y[4] + " " + 
	                    		  y[5] + " " + 
	                    		  rho + " " + 
	                    		  D + " " +
	                    		  L + " " +
	                    		  Ty + " " +
	                    		  gr + " " +
	                    		  gn + " " +
	                    		  g_total + " " +
	                    		  T+ " " +
	                    		  Ma+ " " +
	                    		  cp+ " " +
	                    		  R+ " " +
	                    		  P+ " " +
	                    		  Cd+ " " +
	                    		  Cl+ " " +
	                    		  bank+ " " +
	                    		  flowzone+ " " +
	                    		  qinf+ " " +
	                    		  CdC+ " " +
	                    		  Cdm+ " " +
	                    		  y[6]+ " " +
	                    		  ymo[3]/9.81+ " " +
	                    		  E_total+ " " + 
	                    		  (Throttle_CMD*100)+ " "+ 
	                    		  (m_propellant_init-(M0-y[6]))/m_propellant_init*100+" "+ 
	                    		  (Thrust)+" "+
	                    		  (Thrust/y[6])+" "+
	                    		  (y[3]*Math.cos(y[4]))+" "+
	                    		  (y[3]*Math.sin(y[4]))+" "+
	                    		  (acc_deltav)+" "+
	                    		  active_sequence+" "+
	                    		  (groundtrack/1000)+" "+
	                    		  CTRL_TM_Error+" "+
	                    		  CTRL_TVC_Error+" "+
	                    		  CTRL_Time+" "+
	                    		  (PI-y[4]-Thrust_Deviation)+" "+
	                    		  Thrust_Deviation+" "+
	                    		  Xfo+" "+
	                    		  Yfo+" "+
	                    		  Zfo+" "+
	                    		  vel_inertFrame+" "+
	                    		  fpa_inertFrame+" "+
	                    		  azimuth_inertFrame+" "+
	                    		  fpa_dot*rad2deg+" "+
	                    		  Thrust_Deviation_dot*rad2deg+" "+
	                    		  ((boolean) engine_loss_indicator ? 1 : 0)+" "
	                    		  );
	                }
	                if(isLast) {
	                    try{
	        	        	//DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	        	        	//Date date = new Date();
	        	           // String time = "" ;//+ dateFormat.format(date) ; 
	        	            String resultpath="";
	        	            	String dir = System.getProperty("user.dir");
	        	            	resultpath = dir + "/results.txt";
	                        PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
	                        for(String step: steps) {
	                            writer.println(step);
	                        }
	                        System.out.println("Write: Result file. ");
	                        if(Sequence_RES_closed==false) {System.out.println("Warning: Sequence end not reached - SEQU.res not built");}
	                        writer.close();
	                    } catch(Exception e) {System.out.println("ERROR: Writing result file failed");System.out.println(e);};
	                }
	            }
	            
	        };
	        
			EventHandler AltitudeHandler =new EventHandler() {
				@Override
				public double g(double t, double[] y) {
					// TODO Auto-generated method stub
					return  y[2] - rm - ref_ELEVATION; // 
					//return 0;
				}
				public Action eventOccurred(double t, double[] y, boolean increasing) {
					  return Action.STOP;
					}


				@Override
				public void init(double arg0, double[] arg1, double arg2) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void resetState(double arg0, double[] arg1) {
					// TODO Auto-generated method stub
					
				}
	        	
	        };
	        
			EventHandler VelocityHandler =new EventHandler() {
				@Override
				public double g(double t, double[] y) {
					// TODO Auto-generated method stub
					return  y[3]; // 
					//return 0;
				}
				public Action eventOccurred(double t, double[] y, boolean increasing) {
					  return Action.STOP;
					}


				@Override
				public void init(double arg0, double[] arg1, double arg2) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void resetState(double arg0, double[] arg1) {
					// TODO Auto-generated method stub
					
				}
	        	
	        };

	        System.out.println("------------------------------------------");
	        System.out.println("READ successful");
	        System.out.println("Initialization succesful");
	        for(int i=0;i<Event_Handler.size();i++) {dp853.addEventHandler(Event_Handler.get(i).get_StopHandler(),1,1.0e-3,30);System.out.println("Handler: "+Event_Handler.get(i).get_val_condition());}
	        dp853.addEventHandler(AltitudeHandler,1,1.0e-3,30);
	        dp853.addEventHandler(VelocityHandler,1,1.0e-3,30);
	        System.out.println(""+Event_Handler.size()+" Event Handler added.");
	        dp853.addStepHandler(WriteOut);
	        System.out.println("Integrator start");
	        System.out.println("------------------------------------------");
	        long startTime   = System.nanoTime();
	        boolean integ_error=false; 
	        try {
	        dp853.integrate(ode, 0.0, y, t, y);
	        } catch(NoBracketingException eNBE) {
	        	System.out.println("ERROR: Integrator failed:");
	        	System.out.println(eNBE);
	        	integ_error=true; 
	        } catch (org.apache.commons.math3.exception.NumberIsTooSmallException eMSS) {
	        	System.out.println("ERROR: Integrator failed: Minimal stepsize reached");
	        	integ_error=true;
	        	System.out.println(eMSS);
	        }
			long endTime   = System.nanoTime();
			long totalTime = endTime - startTime;
			double  totalTime_sec = (double) (totalTime * 1E-9);
			if(integ_error==false) {
	        System.out.println("INTEGRATION SUCCESSFUL. ");   
	        System.out.println("Runtime: "+df_X4.format(totalTime_sec)+" seconds.");
			}
	       
		}
}