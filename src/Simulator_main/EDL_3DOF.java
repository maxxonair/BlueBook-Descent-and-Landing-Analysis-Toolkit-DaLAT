package Simulator_main; 

//import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

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

public class EDL_3DOF implements FirstOrderDifferentialEquations {
		//----------------------------------------------------------------------------------------------------------------------------
		//				                              !!!  Control variables  !!!
		//----------------------------------------------------------------------------------------------------------------------------
		public static boolean HoverStop = false; 
	    public static boolean ctrl_callout = false; 
	    public static boolean ISP_Throttle_model= false; 
	    public static boolean stophandler_ON = true; 
	    
	    public static 	    boolean spherical=false;	  // If true -> using spherical coordinates in EoM for velocity vector, else -> cartesian coordinates
	    public static 		boolean is_6DOF = false;      // Switch 3DOF to 6DOF: If true -> 6ODF, if false -> 3DOF
		//............................................                                       .........................................
		//
	    //	                                                         Constants
		//
		//----------------------------------------------------------------------------------------------------------------------------
	    public static double[][] DATA_MAIN = { // RM (average radius) [m] || Gravitational Parameter [m3/s2] || Rotational speed [rad/s] || Average Collision Diameter [m]
				{6371000,3.9860044189E14,7.2921150539E-5,1.6311e-9}, 	// Earth
				{1737400,4903E9,2.661861E-6,320},						// Moon (Earth)
				{3389500,4.2838372E13,7.0882711437E-5,1.6311e-9},		// Mars
				{0,0,0,0},												// Venus
		 };
	    public static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;
	    public static int[] trigger_type_translator = {0,2,3};
	    	public static String[] str_target = {"Earth","Moon","Mars","Venus"};
	    	static double deg2rad = PI/180.0; 		//Convert degrees to radians
	    	static double rad2deg = 180/PI; 		//Convert radians to degrees
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
			public static double[][] g = {{0},{0},{0}};		
			public static double[][] g_NED = {{0},{0},{0}};  // Gravity vector in NED frame 				[m/s�]
			public static double Lt = 0;    		// Average collision diameter (CO2)         [m]
			public static double mu    = 0;    	    // Standard gravitational constant (Mars)   [m3/s2]
			public static double rm    = 0;    	    // Planets average radius                   [m]
			public static double omega = 0 ;        // Planets rotational rate                  [rad/sec]
			public static double g0 = 9.81;         // For normalized ISP 			
			public static double gn = 0;
			public static double gr = 0;
			public static double DragForce = 0;
			public static double SideForce = 0;
			public static double LiftForce = 0;
		    public static double Cd=0;
		    public static double C_SF=0;
		    public static double Cl=0;
		    public static double qinf=0;
		    public static double SurfaceArea =0;
		    public static double BankAngle =0;
		    public static double AngleOfAttack=0;
		    public static double AngleOfSideslip=0; 
		    public static double CdPar=0;
		    public static double Thrust=0;
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
		    public static double m0; 
		    public static int flowzone=0; 				// Flow zone continuum - transitional - free molecular flwo
		    public static double Cdm = 0; 				// Drag coefficient in contiuum flow; 
		    public static int TARGET=0;					// Target body index
		    public static double Throttle_CMD=0;				// Main engine throttle command [-]
		    public static double m_propellant_init = 0;     	// Initial propellant mass [kg]
		    public static double M0=0; 
		    public static double Tx=0;
		    public static double Ty=0;
		    public static double Tz=0;
		    public static double Thrust_max=0; 
		    public static double Thrust_min=0;
		    public static double cntr_h_init=0;
		    public static double cntr_v_init=0;
		    public static double cntr_t_init=0;
		    public static double cntr_fpa_init=0;
		    public static int ctrl_curve;
	        private static List<atm_dataset> ATM_DATA 									 = new ArrayList<atm_dataset>(); 
	        private static List<SequenceElement> SEQUENCE_DATA_main 					 = new ArrayList<SequenceElement>(); 
	        private static List<StopCondition> STOP_Handler 							 = new ArrayList<StopCondition>(); 
	        private static List<Flight_CTRL_ThrustMagnitude> Flight_CTRL_ThrustMagnitude = new ArrayList<Flight_CTRL_ThrustMagnitude>(); 
	        private static List<Flight_CTRL_PitchCntrl> Flight_CTRL_PitchCntrl 			 = new ArrayList<Flight_CTRL_PitchCntrl>(); 
	        private static ArrayList<String> CTRL_steps 								 = new ArrayList<String>();
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
	      	public static double fpa_dot =0;
	      	public static double integ_t =0;
	      	
	        public static double Xfo = 0 ;
	        public static double Yfo = 0 ; 
	        public static double Zfo = 0 ; 
	        
	        static double azimuth_inertFrame = 0 ;
	        static double fpa_inertFrame     = 0 ;
	        static double vel_inertFrame     = 0 ;
	        
	    	
			public static double[] V_NED_ECEF_spherical = {0,0,0};			// Velocity vector in NED system with respect to ECEF in spherical coordinates  [m/s]
			public static double[] V_NED_ECEF_cartesian = {0,0,0};			// Velocity vector in NED system with respect to ECEF in cartesian coordinates [m/s]
			
			public static double[] r_ECEF_cartesian = {0,0,0};				// position coordinates with respect to ECEF in cartesian coordinates [m/s]
			public static double[] r_ECEF_spherical = {0,0,0};				// position coordinates with respect to ECEF in spherical coordinates [m/s]
			
			
			public static double[][] F_Aero_A    = {{0},{0},{0}};						// Aerodynamic Force with respect to Aerodynamic coordinate frame [N]
			public static double[][] F_Aero_NED  = {{0},{0},{0}};						// Aerodynamic Force with respect to NED frame [N]
			public static double[][] F_Thrust_B  = {{0},{0},{0}};						// Thrust Force in body fixed system     [N]
			public static double[][] F_Thrust_NED= {{0},{0},{0}};						// Thrust Force in NED frame    		 [N]
			public static double[][] F_Gravity_G = {{0},{0},{0}};						// Gravity Force in ECEF coordinates     [N]
			public static double[][] F_Gravity_NED = {{0},{0},{0}};						// Gravity Force in NED Frame            [N]
			public static double[][] F_total_NED = {{0},{0},{0}};						// Total force vector in NED coordinates [N]
			
			
			public static double[][] C_ECI_ECEF = {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix ECEF to ECI system
			public static double[][] C_NED_A 	= {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix Aerodynamic to NED system
			public static double[][] C_NED_B 	= {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix body fixed to NED system
			public static double[][] C_NED_ECEF = {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix ECEF to NED system
			public static double[][] C_B_A 		= {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix Bodyfixed to Aerodynamic
			
			
			// 6 DOF Attitude variables: 
			public static double[][] q_vector = {{0},{0},{0},{1}}; 						// Quarternion vector
			public static double[][] AngularVelocity = {{0},{0},{0}};					// Angular Velcity {P, Q, R}T [rad/s] 
			public static double[][] EulerAngle = {{0},{0},{0}};							    // Euler Angle Vector [rad]
			public static double[][] InertiaTensor = {{1,0,0},{0,1,0},{0,0,1}};         // Inertia Tensor []
			public static double[][] AngularMomentum = {{1},{0},{0}};					// Angular Momentum [Nm]
			
			// Equation Elements for angular velocity equations: 
			static double det_I = 0;
			static double EE_I01 = 0;
			static double EE_I02 = 0;
			static double EE_I03 = 0;
			static double EE_I04 = 0;
			static double EE_I05 = 0;
			static double EE_I06 = 0;
			
			static double EE_P_pp = 0;
			static double EE_P_pq = 0;
			static double EE_P_pr = 0;
			static double EE_P_qq = 0;
			static double EE_P_qr = 0;
			static double EE_P_rr = 0;
			static double EE_P_x  = 0;
			static double EE_P_y  = 0;
			static double EE_P_z  = 0;
			
			static double EE_Q_pp = 0;
			static double EE_Q_pq = 0;
			static double EE_Q_pr = 0;
			static double EE_Q_qq = 0;
			static double EE_Q_qr = 0;
			static double EE_Q_rr = 0;
			static double EE_Q_x  = 0;
			static double EE_Q_y  = 0;
			static double EE_Q_z  = 0;
			
			static double EE_R_pp = 0;
			static double EE_R_pq = 0;
			static double EE_R_pr = 0;
			static double EE_R_qq = 0;
			static double EE_R_qr = 0;
			static double EE_R_rr = 0;
			static double EE_R_x  = 0;
			static double EE_R_y  = 0;
			static double EE_R_z  = 0;
			
			//__________________________
			
	        public static double elevationangle; 
	        public static double const_tzer0=0;
	        public static boolean const_isFirst =true; 
	        
	        // TVC control angles: 
	        public static double TVC_alpha =0;					// TVC angle alpha [rad]
	        public static double TVC_beta  =0;					// TVC angle beta [rad]
	        
	        public static double tvc_alpha_MAX = 15;
	        public static double tvc_beta_MAX  = 15;
	        //____________________________
	        
	        public static double Thrust_Deviation=0; 
	        public static double Thrust_Elevation=0;
	        public static double Thrust_Deviation_mo = 0; 
	        public static double Thrust_Deviation_dot =0;
	        public static double TE_save =0;
	        
	        public static double ISP_min = 0; 
	        public static double ISP_max = 0; 
	        
	        public static double TTM_max = 5.0;
	        public static boolean engine_loss_indicator=false;
	        
	        static DecimalFormat decf = new DecimalFormat("###.#");
	        static DecimalFormat df_X4 = new DecimalFormat("#.###");
	      //----------------------------------------------------------------------------------------------------------------------------
	    public double ThrottleMODEL_get_ISP(double ISP_min, double ISP_max, double Throttle_CMD) {
	        	// input check
	        	if(Throttle_CMD>1 || Throttle_CMD<0) {
	        		System.out.println("ERROR: ISP model - throttle command out of range" );
	        		ISP =  ISP_max;
	        	} else if (ISP_min>ISP_max) {
	        		System.out.println("ERROR: ISP model - minimum ISP is larger than maximum ISP" );
	        		ISP=ISP_max;
	        	} else if (ISP_min<0 || ISP_max<0) {
	        		System.out.println("ERROR: ISP model - ISP below 0");
	        		ISP=0; 
	        	} else {
	        		double m = (ISP_max - ISP_min)/(1 - cmd_min);
	        		double n = ISP_max - m ; 
	        		ISP = m * Throttle_CMD + n; 
	        	}
	        	return ISP; 
	        }
	    public int getDimension() {
	    	if(is_6DOF) {
	    		return 14; // 6 DOF model 
	        } else {
	    		return 7; // 3 DOF model 
	    	}
	    }
	    
		public static double[] Spherical2Cartesian_Velocity(double[] X) {
			double[] result = new double[3];
			result[0]  =  X[0] * Math.cos(X[1]) * Math.cos(X[2]);
			result[1]  =  X[0] * Math.cos(X[1]) * Math.sin(X[2]);
			result[2]  = -X[0] * Math.sin(X[1]);
			// Filter small errors from binary conversion: 
			for(int i=0;i<result.length;i++) {if(Math.abs(result[i])<1E-9) {result[i]=0; }}
			if(result[0]==-0.0) {result[0]=0;}
			if(result[1]==-0.0) {result[1]=0;}
			if(result[2]==-0.0) {result[2]=0;}
			return result; 
			}		
		public static double[] Cartesian2Spherical_Velocity(double[] X) {
			double[] result = new double[3];
			result[1] = -Math.atan(X[2]/(Math.sqrt(X[0]*X[0] + X[1]*X[1])));
			result[0] =  Math.sqrt(X[0]*X[0] + X[1]*X[1] + X[2]*X[2]);
			result[2] =  Math.atan2(X[1],X[0]);
			/*
			if(X[1]<0 && X[0]>0) {
				result[2]= PI + Math.abs(result[2]);
			} else if (X[1]<0 && X[0]<0) {
				result[2]= PI/2 + Math.abs(result[2]);
			}
			*/
			// Filter small errors from binary conversion: 
			for(int i=0;i<result.length;i++) {if(Math.abs(result[i])<1E-9) {result[i]=0; }}
			return result; 
			}
		public static double[] Spherical2Cartesian_Position(double[] r_spherical) {
			double[] result = new double[3];
			result[0] = r_spherical[2]*Math.cos(r_spherical[0])*Math.cos(r_spherical[1]);
			result[1] = r_spherical[2]*Math.cos(r_spherical[1])*Math.sin(r_spherical[0]);
			result[2] = r_spherical[2]*Math.sin(r_spherical[1]);
			return result;
		}
		public static double[][] Quaternions2Euler(double[][] Quaternions){
			double[][] EulerAngles = {{0},{0},{0}};
			double a = Quaternions[0][0];
			double b = Quaternions[1][0];
			double c = Quaternions[2][0];
			double d = Quaternions[3][0];
			EulerAngles[0][0] = Math.atan(2*(c*d+a*b)/(a*a-b*b-c*c+d*d));
			EulerAngles[1][0] = Math.asin(-2*(b*d - a*c));
			EulerAngles[2][0] = Math.atan( 2*(b*d - a*c)/(a*a + b*b - c*c - d*d));
			return EulerAngles;
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
		public static void UPDATE_FlightController_ThrustMagnitude(Flight_CTRL_ThrustMagnitude NewElement){	   
			   if (Flight_CTRL_ThrustMagnitude.size()==0){ EDL_3DOF.Flight_CTRL_ThrustMagnitude.add(NewElement); 
			   } else {EDL_3DOF.Flight_CTRL_ThrustMagnitude.add(NewElement); } 
		   }
		public static void UPDATE_FlightController_PitchControl(Flight_CTRL_PitchCntrl NewElement){	   
			   if (Flight_CTRL_PitchCntrl.size()==0){ EDL_3DOF.Flight_CTRL_PitchCntrl.add(NewElement); 
			   } else {EDL_3DOF.Flight_CTRL_PitchCntrl.add(NewElement); } 
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
				
				Flight_CTRL_PitchCntrl NewFlightController_PitchCntrl = new Flight_CTRL_PitchCntrl( TVC_ctrl_ID, true, -1, 0, ctrl_tend, ctrl_fpa, rm , ref_ELEVATION);
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
					cntr_v_init = V_NED_ECEF_spherical[0];
					cntr_h_init = r_ECEF_spherical[2]-rm-ref_ELEVATION;
					cntr_t_init = t;
					cntr_fpa_init = V_NED_ECEF_spherical[1];
					SequenceWriteOut_addRow();
					isFirstSequence=false; 
				}
				if(trigger_type==0) {
						if(Flight_CTRL_ThrustMagnitude.get(active_sequence).get_CTRL_TIME()>trigger_value) {
							active_sequence++;
							cntr_v_init = V_NED_ECEF_spherical[0];
							cntr_h_init = r_ECEF_spherical[2]-rm-ref_ELEVATION;
							cntr_t_init = t;
							cntr_fpa_init = V_NED_ECEF_spherical[1];
							SequenceWriteOut_addRow();
						}
				} else if (trigger_type==1) {
						if( (x[2]-rm-ref_ELEVATION)<trigger_value) {
							active_sequence++;
							cntr_v_init = V_NED_ECEF_spherical[0];
							cntr_h_init = r_ECEF_spherical[2]-rm-ref_ELEVATION;
							cntr_t_init = t;
							cntr_fpa_init = V_NED_ECEF_spherical[1];
							SequenceWriteOut_addRow();}
				} else if (trigger_type==2) {
						if( V_NED_ECEF_spherical[0]<trigger_value) {
							active_sequence++;
							cntr_v_init = V_NED_ECEF_spherical[0];
							cntr_h_init = r_ECEF_spherical[2]-rm-ref_ELEVATION;
							cntr_t_init = t;
							cntr_fpa_init = V_NED_ECEF_spherical[1];
							SequenceWriteOut_addRow();}
	     		}
	    	}
	    	//-------------------------------------------------------------------------------------------------------------
	    	//                   Last Sequence reached -> write SEQU.res
	    	//-------------------------------------------------------------------------------------------------------------
	    	if(active_sequence ==  (SEQUENCE_DATA_main.size()-1) && Sequence_RES_closed==false){
				cntr_v_init = V_NED_ECEF_spherical[0];
				cntr_h_init = r_ECEF_spherical[2]-rm-ref_ELEVATION;
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
	 
	    	Flight_CTRL_ThrustMagnitude.get(active_sequence).Update_Flight_CTRL( true, x, M0, m_propellant_init,  cntr_v_init,  cntr_h_init,  t,  SEQUENCE_DATA_main.get(active_sequence).get_ctrl_target_vel(),SEQUENCE_DATA_main.get(active_sequence).get_ctrl_target_alt(),  Thrust_max,  Thrust_min,  SEQUENCE_DATA_main.get(active_sequence).get_ctrl_target_curve(),  val_dt) ;
	    	//Flight_CTRL_PitchCntrl.get(active_sequence).Update_Flight_CTRL(true, x, t, cntr_t_init, cntr_fpa_init, SEQUENCE_DATA_main.get(active_sequence).get_TVC_ctrl_target_curve(), val_dt);	    	
	    	if(sequence_type_TM==3) { // Controlled Flight Sequence 
			    	//-------------------------------------------------------------------------------------------------------------	
			    	//                          Flight Controller ON - Controlled Fight Sequence
			    	//-------------------------------------------------------------------------------------------------------------
			    	if (ctrl_callout) {System.out.println("Altitude "+decf.format((x[2]-rm))+" | Controller " + Flight_CTRL_ThrustMagnitude.get(active_sequence).get_ctrl_ID() +" set ON");}
			    	Thrust        = Flight_CTRL_ThrustMagnitude.get(active_sequence).get_thrust_cmd();
			    	Throttle_CMD  = Flight_CTRL_ThrustMagnitude.get(active_sequence).get_ctrl_throttle_cmd();
			    	
			    	elevationangle = Flight_CTRL_PitchCntrl.get(active_sequence).get_TVC_cmd();
	    	} else if (sequence_type_TM==2) { // Continuous propulsive Flight Sequence 
			    	//-------------------------------------------------------------------------------------------------------------	
			    	//                          TM-FC OFF | TVC-FC ON - Continuous thrust
			    	//-------------------------------------------------------------------------------------------------------------
	    		elevationangle = 0.0;
	    		elevationangle = Flight_CTRL_PitchCntrl.get(active_sequence).get_TVC_cmd();
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
    		    if(const_isFirst){const_tzer0=t;}
		    	
				Thrust = Thrust_max;
    			Throttle_CMD = Thrust/Thrust_max;	
	    	} else { System.out.println("ERROR: Sequence type out of range");}
		}
		
		public static double[][] GRAVITY_MANAGER_3D(double[] x) {
			//------------------------------------------------------------------------------
			//     Full 3D gravity model (work TBD on complexity) 
			//------------------------------------------------------------------------------
	    	//g = GravityModel.get_g( x[2],  x[0], x[1],  rm,  mu, TARGET);
	    	g = GravityModel.get_g_ECEF(x[2], r_ECEF_cartesian, rm, mu, TARGET);
	    	return g; 
		}
		
		public static void GRAVITY_MANAGER_2D(double[] x) {
			//------------------------------------------------------------------------------
			//     simplified 2D atmosphere model (J2 only) 
			//------------------------------------------------------------------------------
	    	gr = GravityModel.get_gr( x[2],  x[1],  rm,  mu, TARGET);
	    	gn = GravityModel.get_gn( x[2],  x[1],  rm,  mu, TARGET); 
		}
		
		public static void ATMOSPHERE_MANAGER(double[] x) {
			double altitude = x[2] - rm ; 
	    	if (altitude>200000 || TARGET == 1){ // In space conditions: 
	    		// Set atmosphere properties to zero: 
		    		rho   = 0; 																// Density 							[kg/m3]
		    		qinf  = 0;																// Dynamic pressure 					[Pa]
		    		T     = 0 ; 																// Temperature 						[K]
		    		gamma = 0 ; 																// Heat capacity ratio 				[-]
		    		R	  = 0; 																// Gas constant 						[J/kgK]
		    		Ma 	  = 0; 																// Mach number 						[-]
		      	//----------------------------------------------------------------------------------------------
	    	} else { // In atmosphere conditions (if any)
		    	 rho    = AtmosphereModel.atm_read(1, altitude) ;       					// density                         [kg/m3]
		    	 qinf   = 0.5 * rho * ( V_NED_ECEF_spherical[0] * V_NED_ECEF_spherical[0]) ;               	// Dynamic pressure                [Pa]
		    	 T      = AtmosphereModel.atm_read(2, altitude) ;                   		// Temperature                     [K]
		    	 gamma  = AtmosphereModel.atm_read(4, altitude) ;              	        	// Heat capacity ratio			   [-]
		    	 R      = AtmosphereModel.atm_read(3, altitude ) ;                        						// Gas Constant                    [J/kgK]
		    	 P      = rho * R * T;																			// Ambient pressure 			   [Pa]
		    	 Ma     = V_NED_ECEF_spherical[0] / Math.sqrt( T * gamma * R);                  		 			// Mach number                     [-]
	    	     //CdPar  = load_Cdpar( x[3], qinf, Ma, x[2] - rm);   		             						// Parachute Drag coefficient      [-]
	    	     CdC    = AtmosphereModel.get_CdC(altitude,0);                       							// Continuum flow drag coefficient [-]
		    	 Cd 		= AtmosphereModel.load_Drag(V_NED_ECEF_spherical[0], altitude, P, T, CdC, Lt, R);    	// Lift coefficient                [-]
		    	 flowzone = AtmosphereModel.calc_flowzone(V_NED_ECEF_spherical[0], altitude, P, T, Lt);        	// Continous/Transition/Free molecular flow [-]
	    	}
	     	//-----------------------------------------------------------------------------------------------
	    	 DragForce  			=   qinf * SurfaceArea * Cd     		       ;								// Aerodynamic drag Force 		   [N]
	    	 LiftForce 		    	=   qinf * SurfaceArea * Cl * cos( BankAngle ) ;                        // Aerodynamic lift Force 		   [N]
	    	 SideForce 		    	=   qinf * SurfaceArea * Cl * sin( BankAngle ) ;                        // Aerodynamic side Force 		   [N]
	    	//----------------------------------------------------------------------------------------------
		}

		public static void INITIALIZE_ROTATIONAL_MATRICES(double[] x, double t, double bank_angle, double[][] euler_angle) {
			//-------------------------------------------------------------------------------------------
			//              Aerodynamic frame to North-East-Down
			//-------------------------------------------------------------------------------------------
			C_NED_A[0][0] =  Math.cos(V_NED_ECEF_spherical[2])*Math.cos(V_NED_ECEF_spherical[1]);
			C_NED_A[1][0] =  Math.sin(V_NED_ECEF_spherical[2])*Math.cos(V_NED_ECEF_spherical[1]);
			C_NED_A[2][0] = -Math.sin(V_NED_ECEF_spherical[1]);
			
			C_NED_A[0][1] = -Math.sin(V_NED_ECEF_spherical[1])*Math.cos(bank_angle) - Math.cos(V_NED_ECEF_spherical[2])*Math.sin(V_NED_ECEF_spherical[1])*Math.sin(bank_angle);
			C_NED_A[1][1] =  Math.cos(V_NED_ECEF_spherical[2])*Math.cos(bank_angle) - Math.sin(V_NED_ECEF_spherical[2])*Math.sin(V_NED_ECEF_spherical[1])*Math.sin(bank_angle);
			C_NED_A[2][1] = -Math.cos(V_NED_ECEF_spherical[1])*Math.sin(bank_angle);
			
			C_NED_A[0][2] = -Math.sin(V_NED_ECEF_spherical[2])*Math.sin(bank_angle) + Math.cos(V_NED_ECEF_spherical[2])*Math.sin(V_NED_ECEF_spherical[1])*Math.cos(bank_angle);
			C_NED_A[1][2] =  Math.cos(V_NED_ECEF_spherical[2])*Math.sin(bank_angle) + Math.sin(V_NED_ECEF_spherical[2])*Math.sin(V_NED_ECEF_spherical[1])*Math.cos(bank_angle);
			C_NED_A[2][2] =  Math.cos(V_NED_ECEF_spherical[1])*Math.cos(bank_angle);
			//-------------------------------------------------------------------------------------------
			//             Body fixed frame to North-East-Down
			//-------------------------------------------------------------------------------------------
			// Euler Angle Representation: 
			/*
			C_NED_B[0][0] =  Math.cos(euler_angle[2])*Math.cos(euler_angle[1]); 
			C_NED_B[1][0] =  Math.sin(euler_angle[2])*Math.cos(euler_angle[1]);
			C_NED_B[2][0] = -Math.sin(euler_angle[1]);
			
			C_NED_B[0][1] = Math.cos(euler_angle[2])*Math.sin(euler_angle[1])*Math.sin(euler_angle[0])-Math.sin(euler_angle[2])*Math.cos(euler_angle[0]); 
			C_NED_B[1][1] = Math.cos(euler_angle[2])*Math.cos(euler_angle[0])+Math.sin(euler_angle[2])*Math.sin(euler_angle[1])*Math.sin(euler_angle[0]);
			C_NED_B[2][1] = Math.cos(euler_angle[1])*Math.sin(euler_angle[0]);
			
			C_NED_B[0][2] = Math.sin(euler_angle[2])*Math.sin(euler_angle[0])+Math.cos(euler_angle[2])*Math.sin(euler_angle[1])*Math.cos(euler_angle[0]); 
			C_NED_B[1][2] = Math.sin(euler_angle[2])*Math.sin(euler_angle[1])*Math.cos(euler_angle[0])-Math.cos(euler_angle[2])*Math.sin(euler_angle[0]);
			C_NED_B[2][2] = Math.cos(euler_angle[1])*Math.cos(euler_angle[0]);
			*/
			//-----------------------------------------------------------------------
			// Quaternion Representation: 
			
			C_NED_B[0][0] =    (q_vector[0][0]*q_vector[0][0] + q_vector[1][0]*q_vector[1][0] - q_vector[2][0]*q_vector[2][0] - q_vector[3][0]*q_vector[3][0]); 
			C_NED_B[1][0] =  2*(q_vector[1][0]*q_vector[2][0] + q_vector[0][0]*q_vector[3][0]);
			C_NED_B[2][0] =  2*(q_vector[1][0]*q_vector[3][0] - q_vector[0][0]*q_vector[2][0]);
			
			C_NED_B[0][1] =  2*(q_vector[1][0]*q_vector[2][0] - q_vector[0][0]*q_vector[3][0]); 
			C_NED_B[1][1] =    (q_vector[0][0]*q_vector[0][0] - q_vector[1][0]*q_vector[1][0] + q_vector[2][0]*q_vector[2][0] - q_vector[3][0]*q_vector[3][0]);
			C_NED_B[2][1] =  2*(q_vector[2][0]*q_vector[3][0] - q_vector[0][0]*q_vector[1][0]);
			
			C_NED_B[0][2] =  2*(q_vector[1][0]*q_vector[3][0] - q_vector[0][0]*q_vector[2][0]); 
			C_NED_B[1][2] =  2*(q_vector[2][0]*q_vector[3][0] - q_vector[0][0]*q_vector[1][0]);
			C_NED_B[2][2] =    (q_vector[0][0]*q_vector[0][0] - q_vector[1][0]*q_vector[1][0] - q_vector[2][0]*q_vector[2][0] + q_vector[3][0]*q_vector[3][0]);
			
			//-------------------------------------------------------------------------------------------
			//             ECEF frame to North-East-Down
			//-------------------------------------------------------------------------------------------
			C_NED_ECEF[0][0] = -Math.cos(r_ECEF_spherical[0])*Math.sin(r_ECEF_spherical[1]);
			C_NED_ECEF[1][0] = -Math.sin(r_ECEF_spherical[0]);
			C_NED_ECEF[2][0] = -Math.cos(r_ECEF_spherical[0])*Math.cos(r_ECEF_spherical[1]);
			
			C_NED_ECEF[0][1] = -Math.sin(r_ECEF_spherical[0])*Math.sin(r_ECEF_spherical[1]);
			C_NED_ECEF[1][1] =  Math.cos(r_ECEF_spherical[0]);
			C_NED_ECEF[2][1] = -Math.sin(r_ECEF_spherical[0])*Math.cos(r_ECEF_spherical[1]);
			
			C_NED_ECEF[0][2] =  Math.cos(r_ECEF_spherical[1]);
			C_NED_ECEF[1][2] =  0;
			C_NED_ECEF[2][2] = -Math.sin(r_ECEF_spherical[1]);
			//-------------------------------------------------------------------------------------------
			//             Bodyfixed frame to Aerodynamic frame
			//-------------------------------------------------------------------------------------------
			C_B_A[0][0] =  Math.cos(AngleOfAttack)*Math.cos(AngleOfSideslip);
			C_B_A[1][0] =  Math.sin(AngleOfSideslip);
			C_B_A[2][0] =  Math.sin(AngleOfAttack)*Math.cos(AngleOfSideslip);
			
			C_B_A[0][1] =  -Math.cos(AngleOfAttack)*Math.sin(AngleOfSideslip);
			C_B_A[1][1] =   Math.cos(AngleOfSideslip);
			C_B_A[2][1] =  -Math.sin(AngleOfAttack)*Math.sin(AngleOfSideslip);
			
			C_B_A[0][2] =  -Math.sin(AngleOfAttack);
			C_B_A[1][2] =  0;
			C_B_A[2][2] =   Math.cos(AngleOfAttack);
			
		}
		
		public static void Set_AngularVelocityEquationElements(double[] x) {
			double Ixx = InertiaTensor[0][0];
			double Iyy = InertiaTensor[1][1];
			double Izz = InertiaTensor[2][2];
			double Ixy = InertiaTensor[0][1];
			double Ixz = InertiaTensor[0][2];
			//  double Iyx = InertiaTensor[][];
			double Iyz = InertiaTensor[2][1];
			//double Izx = InertiaTensor[][];
			//double Izy = InertiaTensor[][];
			 det_I = Ixx*Iyy*Izz - 2*Ixy*Ixz*Iyz - Ixx*Iyz*Iyz - Iyy*Ixz*Ixz - Izz*Iyz*Iyz;
			 EE_I01 = Iyy*Izz - Iyz*Iyz;
			 EE_I02 = Ixy*Izz + Iyz*Ixz;
			 EE_I03 = Ixy*Iyz + Iyy*Ixz;
			 EE_I04 = Ixx*Izz - Ixz*Ixz;
			 EE_I05 = Ixx*Iyz + Ixy*Ixz;
			 EE_I06 = Ixx*Iyy - Ixy*Ixy;
			
			 EE_P_pp = -(Ixz*EE_I02 - Ixy*EE_I03)/det_I;
			 EE_P_pq =  (Ixz*EE_I01 - Iyz*EE_I02 - (Iyy - Ixx)*EE_I03)/det_I;
			 EE_P_pr = -(Ixy*EE_I01 + (Ixx-Izz)*EE_I02 - Iyz*EE_I03)/det_I;
			 EE_P_qq =  (Iyz*EE_I01 - Ixy*EE_I03)/det_I;
			 EE_P_qr = -((Izz-Iyy)*EE_I01 - Ixy*EE_I02 + Ixz*EE_I03)/det_I;
			 EE_P_rr = -(Iyz*EE_I01 - Ixz*EE_I02)/det_I;
			 EE_P_x  =   EE_I01/det_I;
			 EE_P_y  =   EE_I02/det_I;
			 EE_P_z  =   EE_I03/det_I;
			
			 EE_Q_pp = -(Ixz*EE_I02 - Ixy*EE_I05)/det_I;
			 EE_Q_pq =  (Ixz*EE_I02 - Iyz*EE_I04 - (Iyy - Ixx)*EE_I05)/det_I;
			 EE_Q_pr = -(Ixy*EE_I02 + (Ixx-Izz)*EE_I04 - Iyz*EE_I05)/det_I;
			 EE_Q_qq =  (Iyz*EE_I02 - Ixy*EE_I05)/det_I;
			 EE_Q_qr = -((Izz-Iyy)*EE_I02 - Ixy*EE_I04 + Ixz*EE_I05)/det_I;
			 EE_Q_rr = -(Iyz*EE_I02 - Ixz*EE_I04)/det_I;
			 EE_Q_x  = EE_I02/det_I;
			 EE_Q_y  = EE_I04/det_I;
			 EE_Q_z  = EE_I05/det_I;
			
			 EE_R_pp = -(Ixz*EE_I05 - Ixy*EE_I06)/det_I;
			 EE_R_pq =  (Ixz*EE_I03 - Iyz*EE_I05 - (Iyy - Ixx)*EE_I06)/det_I;
			 EE_R_pr = -(Ixy*EE_I03 + (Ixx-Izz)*EE_I05 - Iyz*EE_I06)/det_I;
			 EE_R_qq =  (Iyz*EE_I03 - Ixy*EE_I06)/det_I;
			 EE_R_qr = -((Izz-Iyy)*EE_I03 - Ixy*EE_I05 + Ixz*EE_I06)/det_I;
			 EE_R_rr = -(Iyz*EE_I03 - Ixz*EE_I05)/det_I;
			 EE_R_x  = EE_I03/det_I;
			 EE_R_y  = EE_I05/det_I;
			 EE_R_z  = EE_I06/det_I;
			
		}
    public void computeDerivatives(double t, double[] x, double[] dxdt) {
    	integ_t=t;
    	//-------------------------------------------------------------------------------------------------------------------
    	//								    	Gravitational environment
    	//-------------------------------------------------------------------------------------------------------------------   
    		if(spherical) {GRAVITY_MANAGER_2D(x);}// 2D model for shperical velcotity vector computation
    		else {
    			g = GRAVITY_MANAGER_3D(x); 
        		g_NED   =  Tool.Multiply_Matrices(C_NED_ECEF, g);	 
        		// Gravitational Force (wrt NED System)
	    	    	F_Gravity_NED[0][0] = x[6]*g_NED[0][0];
	    	    	F_Gravity_NED[1][0] = x[6]*g_NED[1][0];
	    	    	F_Gravity_NED[2][0] = x[6]*g_NED[2][0];
    		}
    	//-------------------------------------------------------------------------------------------------------------------
    	//								Sequence management and Flight controller 
    	//-------------------------------------------------------------------------------------------------------------------
    	SEQUENCE_MANAGER(t,  x);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Atmosphere
    	//-------------------------------------------------------------------------------------------------------------------
    	ATMOSPHERE_MANAGER(x);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    Force Definition - Aerodynamic Forces | Aerodynamic Frame |
    	//-------------------------------------------------------------------------------------------------------------------
	   	F_Aero_A[0][0] = -  DragForce  ;
	   	F_Aero_A[1][0] =    SideForce  ;
	   	F_Aero_A[2][0] = -  LiftForce  ;
	   	//System.out.println(F_Aero_A[0][0] + " | "+F_Aero_A[1][0] + " | "+F_Aero_A[2][0] + " | ");
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    Force Definition - Thrust Forces | Body fixed Frame |
    	//-------------------------------------------------------------------------------------------------------------------
	   	F_Thrust_B[0][0] =  Thrust * Math.cos(TVC_alpha)*Math.cos(TVC_beta);  
	   	F_Thrust_B[1][0] =  Thrust * Math.cos(TVC_alpha)*Math.sin(TVC_beta);   
	   	F_Thrust_B[2][0] =  Thrust * Math.sin(TVC_alpha);   
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Rotational Matrices 
    	//-------------------------------------------------------------------------------------------------------------------
    	INITIALIZE_ROTATIONAL_MATRICES(x, t, BankAngle, EulerAngle); 
    	F_Aero_NED   	= Tool.Multiply_Matrices(C_NED_A, F_Aero_A) ;    	
    	F_Thrust_NED 	= Tool.Multiply_Matrices(C_NED_B, F_Thrust_B) ;
    	F_total_NED   	= Tool.Addup_Matrices(F_Aero_NED , F_Thrust_NED );
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    				ISP model for engine throttling
    	//-------------------------------------------------------------------------------------------------------------------
    	if(ISP_Throttle_model) {ThrottleMODEL_get_ISP( ISP_min,  ISP_max,  Throttle_CMD);}
    	//-------------------------------------------------------------------------------------------------------------------
    	// 										Delta-v integration
    	//-------------------------------------------------------------------------------------------------------------------
    	acc_deltav = acc_deltav + ISP*g0*Math.log(mminus/x[6]);
    	mminus=x[6];
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
    	// 									     Equations of Motion
    	//-------------------------------------------------------------------------------------------------------------------
    	//
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									     Translational Motion
    	//-------------------------------------------------------------------------------------------------------------------
    	// Position vector with respect to spherical velocity vector
    	int optionR = 1; 
	    if(optionR==1) {
	   	 	  // Longitude
		    dxdt[0] = V_NED_ECEF_spherical[0] * Math.cos(V_NED_ECEF_spherical[1] )  * Math.sin( V_NED_ECEF_spherical[2] ) / ( x[2] * Math.cos( x[1] ) ); 
		          // Latitude
		    dxdt[1] = V_NED_ECEF_spherical[0] * Math.cos( V_NED_ECEF_spherical[1] ) * Math.cos( V_NED_ECEF_spherical[2] ) / ( x[2] 			   );
		    	  // Radius 
		    dxdt[2] = V_NED_ECEF_spherical[0] * Math.sin( V_NED_ECEF_spherical[1] );	
	   	
	    } else if (optionR==2) {
	 	 	  // longitude/ tau
		    dxdt[0] = 	V_NED_ECEF_cartesian[1] / (x[2]*Math.cos(x[1]));
	         // latitude - delta
		    dxdt[1] = 	V_NED_ECEF_cartesian[0] /  x[2];
	  	     // radius - r
		    dxdt[2] = - V_NED_ECEF_cartesian[2];
	    }
	    r_ECEF_spherical[0] = x[0];
	    r_ECEF_spherical[1] = x[1];
	    r_ECEF_spherical[2] = x[2];
	    r_ECEF_cartesian = Spherical2Cartesian_Position(r_ECEF_spherical);
	
	    // Velocity vector
	    //--------------------------------
	    // Don't TOUCH!
	    			int optionV = 3; 
	    	//--------------------------------
	    if(spherical) {
	    	// velocity
	    dxdt[3] = -gr * sin( x[4] ) + gn * cos( x[5] ) * cos( x[4] ) + F_Aero_A[0][0] / x[6] + omega * omega * x[2] * cos( x[1] ) * ( sin( x[4] ) * cos( x[1] ) - cos( x[1] ) * cos( x[5] ) * sin( x[1] ) ) ;
	    	// flight path angle 
	    dxdt[4] = ( x[3] / x[2] - gr/ x[3] ) * cos( x[4] ) - gn * cos( x[5] ) * sin( x[4] ) / x[3] - F_Aero_A[2][0] / ( x[3] * x[6] ) + 2 * omega * sin( x[5] ) * cos( x[1] ) + omega * omega * x[2] * cos( x[1] ) * ( cos( x[4] ) * cos( x[1] ) + sin( x[4] ) * cos( x[5] ) * sin( x[1] ) ) / x[3] ;
	    	// local azimuth
	    dxdt[5] = x[3] * sin( x[5] ) * tan( x[1] ) * cos( x[4] ) / x[2] - gn * sin( x[5] ) / x[3] + F_Aero_A[1][0] / ( x[3] - cos( x[4] ) * x[6] ) - 2 * omega * ( tan( x[4] ) * cos( x[5] ) * cos( x[1] ) - sin( x[1] ) ) + omega * omega * x[2] * sin( x[5] ) * sin( x[1] ) * cos( x[1] ) / ( x[3] * cos( x[4] ) ) ;

    	V_NED_ECEF_spherical[0]=x[3];
    	V_NED_ECEF_spherical[1]=x[4];
    	V_NED_ECEF_spherical[2]=x[5];
    	
    	V_NED_ECEF_cartesian = Spherical2Cartesian_Velocity(V_NED_ECEF_spherical);
    	
	    }else {
	    	if(optionV == 1) {
	    		// Lisa:
		    	// u
	    		//dxdt[3] = 	  F_total_NED[0][0]/x[6] + g_NED[0][0] + 1/x[2]*(x[3]*x[5] - x[4]*x[4]*Math.tan(x[1]) 	- 2*omega*x[4]*Math.sin(x[1]));
	    		// v
	    		//dxdt[4] = 	  F_total_NED[1][0]/x[6] + g_NED[1][0] + 1/x[2]*(x[3]*x[4]*Math.tan(x[1] + x[4]*x[5]) 	+ 2*omega*(x[5]*Math.cos(x[1] + x[3]*Math.sin(x[1]))));
	    		// w
	    		//dxdt[5] = 	  F_total_NED[2][0]/x[6] + g_NED[2][0] - 1/x[2]*(x[3]*x[3] + x[4]*x[4]) 				- 2*omega*x[4]*Math.cos(x[1]);
	    	} else if (optionV == 2) {
	    		// Mooij:
	    		//F_total_NED[0][0]=0;
	    		//F_total_NED[1][0]=0;
	    		//F_total_NED[2][0]=0;
		    	// u - North 
			   // dxdt[3] =     F_total_NED[0][0]/x[6]  -   2 * omega * x[4] * Math.sin(x[1]) - omega * omega * x[2] * Math.sin(x[1]) * Math.cos(x[1]) - (x[4]*x[4] * Math.tan(x[1]) + x[3]*x[5])/x[2];
		    	// v - East
			    //dxdt[4] =     F_total_NED[1][0]/x[6]  -   2 * omega * (x[5]*Math.cos(x[1])  - x[3] * Math.sin(x[1])) + x[4]/x[2] * ( x[3] * Math.tan(x[1]) - x[5]);
		    	// w - Down
			   // dxdt[5] =   - F_total_NED[2][0]/x[6]  +   2 * omega * x[4] * Math.cos(x[1]) + omega * omega * x[2] * Math.cos(x[1])*Math.cos(x[1]) + (x[4]*x[4] + x[3]*x[3])/x[2];
	    	} else if (optionV==3) {
	    		// Titterton:
	    								
	    		// u - North
	    		dxdt[3] = F_total_NED[0][0]/x[6] + g_NED[0][0] - 2 * omega * x[4]   * Math.sin(x[1])   						  + (x[3]*x[5] - x[4]*x[4]*Math.tan(x[1]))/x[2] 	     - omega*omega*x[2]/2*Math.sin(2*x[1])       ;
	    		// v - East
	    		dxdt[4] = F_total_NED[1][0]/x[6] + g_NED[1][0] + 2 * omega * ( x[3] * Math.sin(x[1]) + x[5] * Math.cos(x[1]))   + x[4]/x[2] * (x[5] + x[3] * Math.tan(x[1])) 											     ;
	    		// w - Down
	    		dxdt[5] = F_total_NED[2][0]/x[6] + g_NED[2][0] - 2 * omega * x[4]   * Math.cos(x[1])   						  - (x[4]*x[4] + x[3]*x[3])/x[2]   					 - omega*omega*x[2]/2*(1 + Math.cos(2*x[1])) ;
	    		
	    	}
    	V_NED_ECEF_cartesian[0]=x[3];
    	V_NED_ECEF_cartesian[1]=x[4];
    	V_NED_ECEF_cartesian[2]=x[5];
    	
    	V_NED_ECEF_spherical =Cartesian2Spherical_Velocity(V_NED_ECEF_cartesian);
    	
	    }	    
	    // System mass [kg]
	    dxdt[6] = - Thrust/(ISP*g0) ;   
    	//-------------------------------------------------------------------------------------------------------------------
    	// 						   Rotataional motion
    	//-------------------------------------------------------------------------------------------------------------------
	    if(is_6DOF) {
	    // Quaternions:
	    	//------------------------------------------------------------------
	    	double SUB_A = x[11] - 1/x[13] * (C_NED_B[0][0]*x[4] - C_NED_B[0][1]*x[3] - C_NED_B[0][2]*x[4]*Math.tan(x[1])) - omega * (C_NED_B[0][0]*Math.cos(x[1]) - C_NED_B[0][2] * Math.sin(x[1]));
	    	double SUB_B = x[12] - 1/x[13] * (C_NED_B[1][0]*x[4] - C_NED_B[1][1]*x[3] - C_NED_B[1][2]*x[4]*Math.tan(x[1])) - omega * (C_NED_B[1][0]*Math.cos(x[1]) - C_NED_B[1][2] * Math.sin(x[1]));
	    	double SUB_C = x[13] - 1/x[13] * (C_NED_B[2][0]*x[4] - C_NED_B[2][1]*x[3] - C_NED_B[2][2]*x[4]*Math.tan(x[1])) - omega * (C_NED_B[2][0]*Math.cos(x[1]) - C_NED_B[2][2] * Math.sin(x[1]));
	    dxdt[7] = 0.5 * (-x[8]  * SUB_A  - x[9]  * SUB_B  - x[10]  * SUB_C);
	    dxdt[8] = 0.5 * ( x[7]  * SUB_A  - x[10] * SUB_B  + x[9]   * SUB_C);
	    dxdt[9] = 0.5 * ( x[10] * SUB_A  + x[7]  * SUB_B  - x[8]   * SUB_C);
	    dxdt[10]= 0.5 * (-x[9]  * SUB_A  + x[8]  * SUB_B  + x[7]   * SUB_C);
	    
	    q_vector[0][0] = x[7];
	    q_vector[1][0] = x[8];
	    q_vector[2][0] = x[9];
	    q_vector[3][0] = x[10];
	    EulerAngle = Quaternions2Euler(q_vector);
	    	// Angular Velocity: 
	    //------------------------------------------------------------------
	    Set_AngularVelocityEquationElements(x);
	    dxdt[11] = EE_P_pp*x[11]*x[11] + EE_P_pq*x[11]*x[12] + EE_P_pr*x[11]*x[13] + EE_P_qq*x[12]*x[12] + EE_P_qr*x[11]*x[13] + EE_P_rr*x[13]*x[13] + EE_P_x*AngularMomentum[0][0] + EE_P_y*AngularMomentum[1][0] + EE_P_y*AngularMomentum[2][0];
	    dxdt[12] = EE_Q_pp*x[11]*x[11] + EE_Q_pq*x[11]*x[12] + EE_Q_pr*x[11]*x[13] + EE_Q_qq*x[12]*x[12] + EE_Q_qr*x[11]*x[13] + EE_Q_rr*x[13]*x[13] + EE_Q_x*AngularMomentum[0][0] + EE_Q_y*AngularMomentum[1][0] + EE_Q_y*AngularMomentum[2][0];
	    dxdt[13] = EE_R_pp*x[11]*x[11] + EE_R_pq*x[11]*x[12] + EE_R_pr*x[11]*x[13] + EE_R_qq*x[12]*x[12] + EE_R_qr*x[11]*x[13] + EE_R_rr*x[13]*x[13] + EE_R_x*AngularMomentum[0][0] + EE_R_y*AngularMomentum[1][0] + EE_R_y*AngularMomentum[2][0];
	   
	    AngularVelocity[0][0] = x[11];
	    AngularVelocity[1][0] = x[12];
	    AngularVelocity[2][0] = x[13];
	    }
    	//-------------------------------------------------------------------------------------------------------------------
    	// 						   Update Event handler
    	//-------------------------------------------------------------------------------------------------------------------
	    if(stophandler_ON) {
	    for(int i=0;i<STOP_Handler.size();i++) {STOP_Handler.get(i).Update_StopCondition(t, x);}
	    }
}
    
public static void Launch_Integrator( int INTEGRATOR, int target, double x0, double x1, double x2, double x3, double x4, double x5, double x6, double t, double dt_write, double reference_elevation, List<SequenceElement> SEQUENCE_DATA, int ThrustSwitch,List<StopCondition> Event_Handler, double SurfaceArea_INP){
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
//	
//	Initialise Simulation: 
//	
//   - Read propulsion setting:	Propulsion/Controller INIT
//   - Initialise ground track computation
//----------------------------------------------------------------------------------------------	
		double[] prop_read;
	    cntr_h_init=x2-rm;
	    cntr_v_init=x3;
		try {
		 prop_read = Tool.READ_PROPULSION_INPUT(PropulsionInputFile);
	    	 ISP          	  = prop_read[0];
	    	 m_propellant_init= prop_read[1];
	    	 Thrust_max 	  = prop_read[2];
	    	 Thrust_min		  = prop_read[3];
	    	 if((int) prop_read[4]==1) {
	    		 ISP_max = ISP;
	    		 ISP_min = prop_read[5];
	    		 ISP_Throttle_model=true; 
	    	 }
	    	 SurfaceArea 			  = SurfaceArea_INP;
	    	 M0 			  = x6  ; 
	    	 mminus	  	  = M0  ;
	    	 vminus		  = x3  ;
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
 		if(stophandler_ON) {
    	 STOP_Handler = Event_Handler; 
 		}
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
	        FirstOrderDifferentialEquations ode = new EDL_3DOF();
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
  			// 						Result vector setup - DO NOT TOUCH
  			int dimension = 0;
	    	if(is_6DOF) { dimension =  14; // 6 DOF model 
	        } else {dimension =  7; // 3 DOF model 
	        }
		        double[] y = new double[dimension]; // Result vector
  			if(is_6DOF) {
	  		       // double[] y = new double[13]; // Result vector
	  	        	V_NED_ECEF_spherical[0]=x3;
	  	        	V_NED_ECEF_spherical[1]=x4;
	  	        	V_NED_ECEF_spherical[2]=x5;
	  		// Position 
	  		        y[0] = x0;
	  		        y[1] = x1;
	  		        y[2] = x2;
	  		        
	  		// Velocity
			  		        if(spherical) {
			  		        	
			  		        y[3] = x3;
			  		        y[4] = x4;
			  		        y[5] = x5;
			  		        
			  		        } else {
			  		        	V_NED_ECEF_spherical[0]=x3;
			  		        	V_NED_ECEF_spherical[1]=x4;
			  		        	V_NED_ECEF_spherical[2]=x5;
			  		        	V_NED_ECEF_cartesian = Spherical2Cartesian_Velocity(V_NED_ECEF_spherical);
			  			        y[3] = V_NED_ECEF_cartesian[0];
			  			        y[4] = V_NED_ECEF_cartesian[1];
			  			        y[5] = V_NED_ECEF_cartesian[2];
			  			        //V_NED_ECEF_spherical = Cartesian2Spherical_Velocity(V_NED_ECEF_cartesian);
			  			        //System.out.println(x3+"|"+x4+"|"+x5+"|"+V_NED_ECEF_cartesian[0]+"|"+V_NED_ECEF_cartesian[1]+"|"+V_NED_ECEF_cartesian[2]+"|"+V_NED_ECEF_spherical[0]+"|"+V_NED_ECEF_spherical[1]+"|"+V_NED_ECEF_spherical[2]+"|");
			  		        }
	  		// S/C Mass        
	  		        y[6] = x6;
	  		        m0 = x6;
	  				INITIALIZE_FlightController(y) ;
	  				
	  				y[7]  = 0;
	  				y[8]  = 0;
	  				y[9]  = 0;
	  				y[10] = 0;
	  				y[11] = 0;	
  				
  			} else { // 3DOF case
			      //  double[] y = new double[7]; // Result vector
		        	V_NED_ECEF_spherical[0]=x3;
		        	V_NED_ECEF_spherical[1]=x4;
		        	V_NED_ECEF_spherical[2]=x5;
			// Position 
			        y[0] = x0;
			        y[1] = x1;
			        y[2] = x2;
			        
			// Velocity
			        if(spherical) {
			        	
			        y[3] = x3;
			        y[4] = x4;
			        y[5] = x5;
			        
			        } else {
			        	V_NED_ECEF_spherical[0]=x3;
			        	V_NED_ECEF_spherical[1]=x4;
			        	V_NED_ECEF_spherical[2]=x5;
			        	V_NED_ECEF_cartesian = Spherical2Cartesian_Velocity(V_NED_ECEF_spherical);
				        y[3] = V_NED_ECEF_cartesian[0];
				        y[4] = V_NED_ECEF_cartesian[1];
				        y[5] = V_NED_ECEF_cartesian[2];
				        //V_NED_ECEF_spherical = Cartesian2Spherical_Velocity(V_NED_ECEF_cartesian);
				        //System.out.println(x3+"|"+x4+"|"+x5+"|"+V_NED_ECEF_cartesian[0]+"|"+V_NED_ECEF_cartesian[1]+"|"+V_NED_ECEF_cartesian[2]+"|"+V_NED_ECEF_spherical[0]+"|"+V_NED_ECEF_spherical[1]+"|"+V_NED_ECEF_spherical[2]+"|");
			        }
			// S/C Mass        
			        y[6] = x6;
			        m0 = x6;
					INITIALIZE_FlightController(y) ;
  			}
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
	                double E_total = y[6]*(g_total*(y[2]-rm)+0.5*vel_inertFrame*vel_inertFrame);
	                double CTRL_TM_Error =0;
	                double CTRL_TVC_Error =0;
	                double CTRL_Time =0;
	                if(SEQUENCE_DATA_main.get(active_sequence).get_sequence_type()==3) {
	                CTRL_TM_Error=Flight_CTRL_ThrustMagnitude.get(active_sequence).get_CTRL_ERROR();
	                CTRL_TVC_Error=Flight_CTRL_PitchCntrl.get(active_sequence).get_CTRL_ERROR();  
	                }
	                CTRL_Time=Flight_CTRL_ThrustMagnitude.get(active_sequence).get_CTRL_TIME();
	                if( t > twrite ) {
	                	twrite = twrite + dt_write; 
	                    steps.add(t + " " + 
	                    		  y[0] + " " + 
	                    		  y[1] + " " + 
	                    		  (y[2]-rm-ref_ELEVATION) + " " + 
	                    		  (y[2]-rm)+ " " + 
	                    		  y[2] + " " + 
	                    		  V_NED_ECEF_spherical[0]+ " " + 
	                    		  V_NED_ECEF_spherical[1] + " " + 
	                    		  V_NED_ECEF_spherical[2] + " " + 
	                    		  rho + " " + 
	                    		  DragForce + " " +
	                    		  LiftForce + " " +
	                    		  SideForce + " " +
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
	                    		  BankAngle+ " " +
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
	                    		  C_NED_B[0][0]+" "+
	                    		  C_NED_B[1][0]+" "+
	                    		  C_NED_B[2][0]+" "+
	                    		  vel_inertFrame+" "+
	                    		  fpa_inertFrame+" "+
	                    		  azimuth_inertFrame+" "+
	                    		  fpa_dot*rad2deg+" "+
	                    		  Thrust_Deviation_dot*rad2deg+" "+
	                    		  ((boolean) engine_loss_indicator ? 1 : 0)+" "+
	                    		  V_NED_ECEF_cartesian[0]+ " " + 
	                    		  V_NED_ECEF_cartesian[1] + " " + 
	                    		  V_NED_ECEF_cartesian[2] + " " + 
	                    		  q_vector[0][0]+" "+
	                    		  q_vector[1][0]+" "+
	                    		  q_vector[2][0]+" "+
	                    		  q_vector[3][0]+" "+
	                    		  ISP+" "+
	                    		  F_total_NED[0][0]+" "+
	                    		  F_total_NED[1][0]+" "+
	                    		  F_total_NED[2][0]+" "+
	                    		  g_NED[0][0]+" "+
	                    		  g_NED[1][0]+" "+
	                    		  g_NED[2][0]+" "+
	                    		  Math.sqrt(g_NED[0][0]*g_NED[0][0] + g_NED[1][0]*g_NED[1][0] + g_NED[2][0]*g_NED[2][0])+" "+
	                    		  F_Aero_NED[0][0]+" "+
	                    		  F_Aero_NED[1][0]+" "+
	                    		  F_Aero_NED[2][0]+" "+
	                    		  F_Thrust_NED[0][0]+" "+
	                    		  F_Thrust_NED[1][0]+" "+
	                    		  F_Thrust_NED[2][0]+" "+
	                    		  F_Gravity_NED[0][0]+" "+
	                    		  F_Gravity_NED[1][0]+" "+
	                    		  F_Gravity_NED[2][0]+" "+
	                    		  r_ECEF_cartesian[0]+" "+
	                    		  r_ECEF_cartesian[1]+" "+
	                    		  r_ECEF_cartesian[2]+" "+
	                    		  AngularVelocity[0][0]+" "+
	                    		  AngularVelocity[1][0]+" "+
	                    		  AngularVelocity[2][0]+" "+
	                    		  AngularMomentum[0][0]+" "+
	                    		  AngularMomentum[1][0]+" "+
	                    		  AngularMomentum[2][0]+" "+
	                    		  EulerAngle[0][0]+" "+
	                    		  EulerAngle[1][0]+" "+
	                    		  EulerAngle[2][0]+" "
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
					//return  Math.sqrt(V_NED_ECEF_cartesian[0]*V_NED_ECEF_cartesian[0] + V_NED_ECEF_cartesian[1]*V_NED_ECEF_cartesian[1] + V_NED_ECEF_cartesian[2]*V_NED_ECEF_cartesian[2]); // 
					return V_NED_ECEF_spherical[0];
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
	        if(stophandler_ON) {
	        for(int i=0;i<STOP_Handler.size();i++) {
	        	EventHandler interimEvent = STOP_Handler.get(i).get_StopHandler();
	        	dp853.addEventHandler(interimEvent,0.1,1.0e-2,100);
	        	System.out.println("Handler: "+STOP_Handler.get(i).get_val_condition());
	        }
	        }
	        dp853.addEventHandler(AltitudeHandler,1,1.0e-4,50);
	        dp853.addEventHandler(VelocityHandler,1,1.0e-3,30);
	        System.out.println(""+STOP_Handler.size()+" Event Handler added.");
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
	        	System.out.println(eMSS);
	        	integ_error=true; 
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