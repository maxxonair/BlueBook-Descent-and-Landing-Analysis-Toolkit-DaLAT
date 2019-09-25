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
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import Model.Atmosphere;
import Model.AtmosphereModel;
import Model.Gravity;
import Model.atm_dataset;
import Sequence.SequenceElement;
import Toolbox.Mathbox;
import Controller.Flight_CTRL_ThrustMagnitude;
import Controller.Flight_CTRL_PitchCntrl;
import Controller.LandingCurve;
import FlightElement.SpaceShip;

public class Simulation implements FirstOrderDifferentialEquations {
		//----------------------------------------------------------------------------------------------------------------------------
		//				                              !!!  Control variables  !!!
		//----------------------------------------------------------------------------------------------------------------------------
		public static boolean HoverStop          = false; 
	    public static boolean ctrl_callout       = false; 
	    public static boolean ISP_Throttle_model = false; 
	    public static boolean stophandler_ON     = true; 
	    
	    public static 	    boolean spherical = false;	  // If true -> using spherical coordinates in EoM for velocity vector, else -> cartesian coordinates
	    public static 		boolean is_6DOF   = false;    // Switch 3DOF to 6DOF: If true -> 6ODF, if false -> 3DOF
		public static 		int SixDoF_Option = 1;  
		public static       boolean FlatEarther = false;
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
			public static double mu    = 0;    	    // Standard gravitational constant (Mars)   [m3/s2]
			public static double rm    = 0;    	    // Planets average radius                   [m]
			public static double omega = 0 ;        // Planets rotational rate                  [rad/sec]
			public static double g0 = 9.81;         // For normalized ISP 			
		    public static int TARGET=0;						// Target body index
		    public static double Throttle_CMD=0;				// Main engine throttle command [-]
		    public static double m_propellant_init = 0;     	// Initial propellant mass [kg]
		    public static double M0=0; 
		    public static double Tx=0;
		    public static double Ty=0;
		    public static double Tz=0;
		    public static double Thrust_max=0; 
		    public static double Thrust_min=0;
		    public static double Thrust_is=0;
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
	        public static double ctrl_alt = 0 ; 			// Active Flight Controller target altitude [m]
	        public static double v_touchdown=0; 			// Global touchdown velocity constraint [m/s]
	        public static boolean isFirstSequence=true;
	        public static boolean Sequence_RES_closed=false;
	        public static double groundtrack = 0; 
	        public static double phimin=0;
	        public static double tetamin=0;
	      	public static double fpa_dot =0;
	      	public static double integ_t =0;
	      	public static double Lt = 0;    		// Average collision diameter (CO2)         [m]
	        public static double Xfo = 0 ;
	        public static double Yfo = 0 ; 
	        public static double Zfo = 0 ; 
	        public static double ISP_is=0;
	        
	        static double azimuth_inertFrame = 0 ;
	        static double fpa_inertFrame     = 0 ;
	        static double vel_inertFrame     = 0 ;
	        
	    	
			public static double[] V_NED_ECEF_spherical = {0,0,0};			// Velocity vector in NED system with respect to ECEF in spherical coordinates  [m/s]
			public static double[] V_NED_ECEF_cartesian = {0,0,0};			// Velocity vector in NED system with respect to ECEF in cartesian coordinates [m/s]
			
			public static double[] r_ECEF_cartesian = {0,0,0};				// position coordinates with respect to ECEF in cartesian coordinates [m/s]
			public static double[] r_ECEF_spherical = {0,0,0};				// position coordinates with respect to ECEF in spherical coordinates [m/s]
			
			static CoordinateTransformation coordinateTransformation;
			
			public static double[][] F_Aero_A    = {{0},{0},{0}};						// Aerodynamic Force with respect to Aerodynamic coordinate frame [N]
			public static double[][] F_Aero_NED  = {{0},{0},{0}};						// Aerodynamic Force with respect to NED frame [N]
			public static double[][] F_Thrust_B  = {{0},{0},{0}};						// Thrust Force in body fixed system     [N]
			public static double[][] F_Thrust_NED  = {{0},{0},{0}};						// Thrust Force in NED frame    		 [N]
			public static double[][] F_Gravity_G   = {{0},{0},{0}};						// Gravity Force in ECEF coordinates     [N]
			public static double[][] F_Gravity_NED = {{0},{0},{0}};						// Gravity Force in NED Frame            [N]
			public static double[][] F_total_NED   = {{0},{0},{0}};						// Total force vector in NED coordinates [N]
			
			public static double[][] M_Aero_NED      = {{0},{0},{0}};
			public static double[][] M_Thrust_NED    = {{0},{0},{0}};
			
			public static double[][] M_Aero_A      = {{0},{0},{0}};
			public static double[][] M_Aero_B      = {{0},{0},{0}};
			public static double[][] M_Thrust_B    = {{0},{0},{0}};
			
			// 6 DOF Attitude variables: 
			public static double[][] q_vector        = {{1},
														{0},
														{0},
														{0}}; 							// Quarternion vector
			public static double[][] AngularRate     = {{0},
														{0},
														{0}};							 // Angular Velcity {P, Q, R}T [rad/s] 
			public static double[][] EulerAngle      = {{0},{0},{0}};				     // Euler Angle Vector [rad]
			public static double[][] InertiaTensor   = {{   0    ,    0    ,   0},
													    {   0    ,    0    ,   0},
													    {   0    ,    0    ,   0}};  // Inertia Tensor []
			public static double[][] AngularMomentum_B = {{0},
														  {0},
														  {0}};					 // Angular Momentum (Total) [Nm] (Do not touch!)
			
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
	        
	        public static SpaceShip spaceShip = new SpaceShip();
	        public static Atmosphere atmosphere = new Atmosphere();
	        
	        static DecimalFormat decf = new DecimalFormat("###.#");
	        static DecimalFormat df_X4 = new DecimalFormat("#.###");
	      //----------------------------------------------------------------------------------------------------------------------------
	    public double ThrottleMODEL_get_ISP(SpaceShip spaceShip, double Throttle_CMD) {
	        	double IspOut=0;
	        	if(Throttle_CMD>1 || Throttle_CMD<0) {
	        		System.out.println("ERROR: ISP model - throttle command out of range" );
	        		IspOut =  spaceShip.getPropulsion().getPrimaryISPMax();
	        	} else if (ISP_min>ISP_max) {
	        		System.out.println("ERROR: ISP model - minimum ISP is larger than maximum ISP" );
	        		IspOut=spaceShip.getPropulsion().getPrimaryISPMax();
	        	} else if (ISP_min<0 || ISP_max<0) {
	        		System.out.println("ERROR: ISP model - ISP below 0");
	        		IspOut=0; 
	        	} else {
	        		double m = (spaceShip.getPropulsion().getPrimaryISPMax() - spaceShip.getPropulsion().getPrimaryISPMin())/(1 - cmd_min);
	        		double n = spaceShip.getPropulsion().getPrimaryISPMax() - m ; 
	        		IspOut = m * Throttle_CMD + n; 
	        	}
	        	return IspOut; 
	        }
	    public int getDimension() {
	    	if(is_6DOF) {
	    		return 14; // 6 DOF model 
	        } else {
	    		return 7; // 3 DOF model 
	    	}
	    }
	    
		public static void SET_Constants(int TARGET) throws IOException{
		    Lt    = DATA_MAIN[TARGET][3];    	// Average collision diameter (CO2)         [m]
		    mu    = DATA_MAIN[TARGET][1];    	// Standard gravitational constant          []
		    rm    = DATA_MAIN[TARGET][0];    	// Planets mean radius                      [m]
		    omega = DATA_MAIN[TARGET][2];		// Planets rotational speed     		    [rad/s]
		}
		public static void UPDATE_FlightController_ThrustMagnitude(Flight_CTRL_ThrustMagnitude NewElement){	   
			   if (Flight_CTRL_ThrustMagnitude.size()==0){ Simulation.Flight_CTRL_ThrustMagnitude.add(NewElement); 
			   } else {Simulation.Flight_CTRL_ThrustMagnitude.add(NewElement); } 
		   }
		public static void UPDATE_FlightController_PitchControl(Flight_CTRL_PitchCntrl NewElement){	   
			   if (Flight_CTRL_PitchCntrl.size()==0){ Simulation.Flight_CTRL_PitchCntrl.add(NewElement); 
			   } else {Simulation.Flight_CTRL_PitchCntrl.add(NewElement); } 
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
	coordinateTransformation.initializeTranformationMatrices(x, t, omega, atmosphere, EulerAngle, q_vector, 
	   			r_ECEF_spherical, V_NED_ECEF_spherical);
    	//-------------------------------------------------------------------------------------------------------------------
    	//								    	Gravitational environment
    	//-------------------------------------------------------------------------------------------------------------------   
    		if(spherical) {}// 2D model for shperical velcotity vector computation
    		else {
    			g = Gravity.getGravity3D(x, r_ECEF_cartesian); 
        		g_NED   =  Mathbox.Multiply_Matrices(coordinateTransformation.getC_ECEF2NED(), g);	 
        		// Gravitational Force (wrt NED System)
	    	    	F_Gravity_NED[0][0] = x[6]*g_NED[0][0];
	    	    	F_Gravity_NED[1][0] = x[6]*g_NED[1][0];
	    	    	F_Gravity_NED[2][0] = x[6]*g_NED[2][0];
    		}
    		//Random rand = new Random();
    		M_Thrust_B[1][0] = 1;

    	//-------------------------------------------------------------------------------------------------------------------
    	//								Sequence management and Flight controller 
    	//-------------------------------------------------------------------------------------------------------------------
    //	SEQUENCE_MANAGER(t,  x);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Atmosphere
    	//-------------------------------------------------------------------------------------------------------------------
    	AtmosphereModel.ATMOSPHERE_MANAGER(x, rm, TARGET, Lt, atmosphere, spaceShip, V_NED_ECEF_spherical);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    Force Definition - Aerodynamic Forces | Aerodynamic Frame |
    	//-------------------------------------------------------------------------------------------------------------------
	   	F_Aero_A[0][0] = -  atmosphere.getDragForce()  ;
	   	F_Aero_A[1][0] =    atmosphere.getSideForce()  ;
	   	F_Aero_A[2][0] = -  atmosphere.getLiftForce()  ;
	   	//System.out.println(F_Aero_A[0][0] + " | "+F_Aero_A[1][0] + " | "+F_Aero_A[2][0] + " | ");
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    Force Definition - Thrust Forces | Body fixed Frame |
    	//-------------------------------------------------------------------------------------------------------------------
	   	F_Thrust_B[0][0] =  Thrust_is * Math.cos(TVC_alpha)*Math.cos(TVC_beta);  
	   	F_Thrust_B[1][0] =  Thrust_is * Math.cos(TVC_alpha)*Math.sin(TVC_beta);   
	   	F_Thrust_B[2][0] =  Thrust_is * Math.sin(TVC_alpha);   
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Set up force vector in NED  
    	//------------------------------------------------------------------------------------------------------------------- 
    	F_Aero_NED   	= Mathbox.Multiply_Matrices(coordinateTransformation.getC_A2NED(), F_Aero_A) ;    	
    	F_Thrust_NED 	= Mathbox.Multiply_Matrices(coordinateTransformation.getC_B2NED(), F_Thrust_B) ;
    	F_total_NED   	= Mathbox.Addup_Matrices(F_Aero_NED , F_Thrust_NED );
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    				ISP model for engine throttling
    	//-------------------------------------------------------------------------------------------------------------------
    	if(ISP_Throttle_model) {ISP_is = ThrottleMODEL_get_ISP(spaceShip, Throttle_CMD);}
    	//-------------------------------------------------------------------------------------------------------------------
    	// 										Delta-v integration
    	//-------------------------------------------------------------------------------------------------------------------
    	acc_deltav = acc_deltav + ISP_is*g0*Math.log(mminus/x[6]);
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
	    r_ECEF_cartesian = Mathbox.Spherical2Cartesian_Position(r_ECEF_spherical);
	    //--------------------------------
	    // Velocity vector
	    	//--------------------------------
	    if(spherical) {
	    	// velocity
	    dxdt[3] = -Gravity.getGravity2D(x)[0] * sin( x[4] ) + Gravity.getGravity2D(x)[1] * cos( x[5] ) * cos( x[4] ) + F_Aero_A[0][0] / x[6] + omega * omega * x[2] * cos( x[1] ) * ( sin( x[4] ) * cos( x[1] ) - cos( x[1] ) * cos( x[5] ) * sin( x[1] ) ) ;
	    	// flight path angle 
	    dxdt[4] = ( x[3] / x[2] - Gravity.getGravity2D(x)[0]/ x[3] ) * cos( x[4] ) - Gravity.getGravity2D(x)[1] * cos( x[5] ) * sin( x[4] ) / x[3] - F_Aero_A[2][0] / ( x[3] * x[6] ) + 2 * omega * sin( x[5] ) * cos( x[1] ) + omega * omega * x[2] * cos( x[1] ) * ( cos( x[4] ) * cos( x[1] ) + sin( x[4] ) * cos( x[5] ) * sin( x[1] ) ) / x[3] ;
	    	// local azimuth
	    dxdt[5] = x[3] * sin( x[5] ) * tan( x[1] ) * cos( x[4] ) / x[2] - Gravity.getGravity2D(x)[1] * sin( x[5] ) / x[3] + F_Aero_A[1][0] / ( x[3] - cos( x[4] ) * x[6] ) - 2 * omega * ( tan( x[4] ) * cos( x[5] ) * cos( x[1] ) - sin( x[1] ) ) + omega * omega * x[2] * sin( x[5] ) * sin( x[1] ) * cos( x[1] ) / ( x[3] * cos( x[4] ) ) ;

    	V_NED_ECEF_spherical[0]=x[3];
    	V_NED_ECEF_spherical[1]=x[4];
    	V_NED_ECEF_spherical[2]=x[5];
    	
    	V_NED_ECEF_cartesian = Mathbox.Spherical2Cartesian_Velocity(V_NED_ECEF_spherical);
    	
	    } else {
	    		// Derived from Titterton, Strapdown Navigation: 								
	    		// u - North
	    		dxdt[3] = F_total_NED[0][0]/x[6] + g_NED[0][0] - 2 * omega * x[4]   * Math.sin(x[1])   						  + (x[3]*x[5] - x[4]*x[4]*Math.tan(x[1]))/x[2] 	     - omega*omega*x[2]/2*Math.sin(2*x[1])       ;
	    		// v - East
	    		dxdt[4] = F_total_NED[1][0]/x[6] + g_NED[1][0] + 2 * omega * ( x[3] * Math.sin(x[1]) + x[5] * Math.cos(x[1]))   + x[4]/x[2] * (x[5] + x[3] * Math.tan(x[1])) 											     ;
	    		// w - Down
	    		dxdt[5] = F_total_NED[2][0]/x[6] + g_NED[2][0] - 2 * omega * x[4]   * Math.cos(x[1])   						  - (x[4]*x[4] + x[3]*x[3])/x[2]   					 - omega*omega*x[2]/2*(1 + Math.cos(2*x[1])) ;

    	V_NED_ECEF_cartesian[0]=x[3];
    	V_NED_ECEF_cartesian[1]=x[4];
    	V_NED_ECEF_cartesian[2]=x[5];
    	
    	V_NED_ECEF_spherical = Mathbox.Cartesian2Spherical_Velocity(V_NED_ECEF_cartesian);
    	
	    }	    
	    // System mass [kg]
	    dxdt[6] = - Thrust_is/(ISP_is*g0) ;   
    	//-------------------------------------------------------------------------------------------------------------------
    	// 						   Rotataional motion
    	//-------------------------------------------------------------------------------------------------------------------
	    if(is_6DOF) {
	    	if (SixDoF_Option ==1) {
	    		//-------------------------------------------------------------------------------------------------------------------------------------------
	    		// EoM for Angular Rate model from: 
	    		// Duke, E.L. Antoniewicz, R.F.  and Krambeer "Derivation and definition of a linear aircraft model", NASA Reference Publication 1207, 1988
	    		// The following equations for to solve the angular rates are the full set on non-linear Euler equations:
	    		// The innertial equations are separated and prepared by Set_AngularVelocityEquationElements(StateVector)
	    		// This function has to be called before each solving step.
	    		//-------------------------------------------------------------------------------------------------------------------------------------------
	    		Set_AngularVelocityEquationElements(x);
	    		//----------------------------------------------------------------------------------------
	    		// Quaternions:
	    		if(FlatEarther) {
		    		double[][] Q = {{ 0    , x[13],-x[12], x[11]}, 
		    				        {-x[13], 0    , x[11], x[12]},
		    				        { x[12],-x[11], 0    , x[13]},
		    				        {-x[11],-x[12],-x[13], 0    }}; 
		    		
		    	    q_vector[0][0] = x[7];
		    	    q_vector[1][0] = x[8];
		    	    q_vector[2][0] = x[9];
		    	    q_vector[3][0] = x[10];
		    	    

		    		double[][] q_vector_dot =  Mathbox.Multiply_Scalar_Matrix(0.5, Mathbox.Multiply_Matrices(Q, q_vector)); 
		    		dxdt[7] =  q_vector_dot[0][0];  // e1 dot
		    		dxdt[8] =  q_vector_dot[1][0];  // e2 dot 
		    		dxdt[9] =  q_vector_dot[2][0];  // e3 dot
		    		dxdt[10] = q_vector_dot[3][0];  // e4 dot
	
		    	    EulerAngle = Mathbox.Quaternions2Euler2(q_vector);
	    		} else {
		    		double[][] ElementMatrix = {{  -q_vector[1][0] , -q_vector[2][0]  , -q_vector[3][0]  }, 
		    				         			{   q_vector[0][0] , -q_vector[3][0] ,   q_vector[2][0]  },
		    				         			{   q_vector[3][0],   q_vector[0][0]  , -q_vector[1][0]  },
		    				         			{  -q_vector[2][0] ,  q_vector[1][0]  ,  q_vector[0][0]  }};
		    		
		    		
		    		double[][] PQR = {{x[11]},
		    						  {x[12]},
		    						  {x[13]}};
		    		
		    		double[][] Omega_NED = {{ 1/r_ECEF_spherical[2] * V_NED_ECEF_cartesian[1] },
		    								{-1/r_ECEF_spherical[2] * V_NED_ECEF_cartesian[0] },
		    								{-1/r_ECEF_spherical[2] * V_NED_ECEF_cartesian[1] * Math.tan(r_ECEF_spherical[1])}};
		    		/*
		    		double[][] OMEGA_ECEF = {{omega*Math.cos(r_ECEF_spherical[1])},
		    							  	 {0},
		    							  	 {-omega*Math.sin(r_ECEF_spherical[1])}};
		    		*/
		    		double[][] Element_10 =  Mathbox.Multiply_Scalar_Matrix(0.5, ElementMatrix);
		    		double[][] Element_21 =  Mathbox.Multiply_Matrices(coordinateTransformation.getC_NED2B(), Omega_NED);

		    		//double[][] Element_22 = Mathbox.Multiply_Matrices(coordinateTransformation.getC_NED2B(), OMEGA_ECEF);
		    		//double[][] Element_20 = Mathbox.Substract_Matrices(Mathbox.Substract_Matrices(PQR, Element_21), Element_22);
		    		double[][] Element_20 =  Mathbox.Substract_Matrices(PQR, Element_21);
		    			    		
		    		double[][] q_vector_dot = Mathbox.Multiply_Matrices(Element_10, Element_20);
		    	    /*
		    		for(int i=0;i<3;i++) {
		    			System.out.println(Element_10[i][0]);
		    		}
		    		System.out.println("--------------");
		    		*/
		    		dxdt[7]  =  q_vector_dot[0][0];  // e1 dot
		    		dxdt[8]  =  q_vector_dot[1][0];  // e2 dot 
		    		dxdt[9]  =  q_vector_dot[2][0];  // e3 dot
		    		dxdt[10] =  q_vector_dot[3][0];  // e4 dot
		    		//-----------------------------------------
		    		// Postprocessing Euler and Quarternions 
		    	    q_vector[0][0] = x[7];
		    	    q_vector[1][0] = x[8];
		    	    q_vector[2][0] = x[9];
		    	    q_vector[3][0] = x[10];
		    		EulerAngle = Mathbox.Quaternions2Euler(q_vector);
	    		}
	    	    //----------------------------------------------------------------------------------------
	    	    // System.out.println("model 1 running");
	    	    double Lb = M_Aero_B[0][0] + M_Thrust_B[0][0] ;
	    	    double Mb = M_Aero_B[1][0] + M_Thrust_B[1][0] ;
	    	    double Nb = M_Aero_B[2][0] + M_Thrust_B[2][0] ;
	    	    
	    	    dxdt[11] = EE_P_pp * x[11]*x[11] + EE_P_pq * x[11]*x[12] + EE_P_pr * x[11]*x[13] + EE_P_qq *x[12]*x[12] + EE_P_qr * x[12]*x[13] + EE_P_rr * x[13]*x[13] + EE_P_x*Lb + EE_P_y*Mb + EE_P_z*Nb;
	    	    dxdt[12] = EE_Q_pp * x[11]*x[11] + EE_Q_pq * x[11]*x[12] + EE_Q_pr * x[11]*x[13] + EE_Q_qq *x[12]*x[12] + EE_Q_qr * x[12]*x[13] + EE_Q_rr * x[13]*x[13] + EE_Q_x*Lb + EE_Q_y*Mb + EE_Q_z*Nb;
	    	    dxdt[13] = EE_R_pp * x[11]*x[11] + EE_R_pq * x[11]*x[12] + EE_R_pr * x[11]*x[13] + EE_R_qq *x[12]*x[12] + EE_R_qr * x[12]*x[13] + EE_R_rr * x[13]*x[13] + EE_R_x*Lb + EE_R_y*Mb + EE_R_z*Nb;
	    	
	    	}
	    else  if (SixDoF_Option == 2) {
	//System.out.println("6DoF running! Option 2");
	    		// Quaternions:
	
	    		double[][] Q = {{ 0    , x[13],-x[12], x[11]}, 
	    				        {-x[13], 0    , x[11], x[12]},
	    				        { x[12],-x[11], 0    , x[13]},
	    				        {-x[11],-x[12],-x[13], 0    }}; 
	    		
	    	    q_vector[0][0] = x[7];
	    	    q_vector[1][0] = x[8];
	    	    q_vector[2][0] = x[9];
	    	    q_vector[3][0] = x[10];
	    	    
	    	    EulerAngle = Mathbox.Quaternions2Euler(q_vector);
	    		double[][] q_vector_dot =  Mathbox.Multiply_Scalar_Matrix(0.5, Mathbox.Multiply_Matrices(Q, q_vector)); 
	    		dxdt[7] =  q_vector_dot[0][0];  // e1 dot
	    		dxdt[8] =  q_vector_dot[1][0];  // e2 dot 
	    		dxdt[9] =  q_vector_dot[2][0];  // e3 dot
	    		dxdt[10] = q_vector_dot[3][0];  // e4 dot

	    	    q_vector[0][0] = x[7];
	    	    q_vector[1][0] = x[8];
	    	    q_vector[2][0] = x[9];
	    	    q_vector[3][0] = x[10];
	    	    
	    	    EulerAngle = Mathbox.Quaternions2Euler(q_vector);
	    	    //----------------------------------------------------------------------------------------
	    	    double Lb = M_Aero_NED[0][0] + M_Thrust_NED[0][0] ;
	    	    double Mb = M_Aero_NED[1][0] + M_Thrust_NED[1][0] ;
	    	    double Nb = M_Aero_NED[2][0] + M_Thrust_NED[2][0] ;
	    		
				double Ixx = InertiaTensor[0][0];
				double Iyy = InertiaTensor[1][1];
				double Izz = InertiaTensor[2][2];
				//double Ixy = InertiaTensor[0][1];
				double Ixz = InertiaTensor[0][2];
				//  double Iyx = InertiaTensor[][];
				//double Iyz = InertiaTensor[2][1];
				//System.out.println(Ixx+" | "+x[7]);
	    		// Angular Rates
			// p dot:
	    		dxdt[11] = (Izz * Lb + Ixz * Nb - (Ixz * (Iyy - Ixx - Izz) * x[11] + (Ixz*Ixz + Izz * (Izz - Iyy)) 
	    					* x[12]) * x[11]) / (Ixx * Izz - Ixz*Ixz); 
	    		// q dot:
	    		dxdt[12] = (Mb - (Ixx - Izz) * x[11] * x[13] - Ixz * (x[11]*x[11] - x[13]*x[13])) / Iyy; 
	    		// r dot:
	    		dxdt[13] = (Ixz * Lb + Ixx * Nb + (Ixz * (Iyy - Ixx - Izz) * x[13] + (Ixz*Ixz + Ixx * (Ixx - Iyy)) 
	    					* x[11]) * x[12]) / (Ixx * Izz - Ixz*Ixz); 

} else if (SixDoF_Option == 3) {
	//System.out.println("6DoF running! Option 3");
	// Quaternions:

	double[][] Q = {{ 0    , x[13],-x[12], x[11]}, 
			        {-x[13], 0    , x[11], x[12]},
			        { x[12],-x[11], 0    , x[13]},
			        {-x[11],-x[12],-x[13], 0    }}; 
	
    q_vector[0][0] = x[7];
    q_vector[1][0] = x[8];
    q_vector[2][0] = x[9];
    q_vector[3][0] = x[10];
    
    EulerAngle = Mathbox.Quaternions2Euler2(q_vector);
	double[][] q_vector_dot =  Mathbox.Multiply_Scalar_Matrix(0.5, Mathbox.Multiply_Matrices(Q, q_vector)); 
	dxdt[7] =  q_vector_dot[0][0];  // e1 dot
	dxdt[8] =  q_vector_dot[1][0];  // e2 dot 
	dxdt[9] =  q_vector_dot[2][0];  // e3 dot
	dxdt[10] = q_vector_dot[3][0];  // e4 dot

    EulerAngle = Mathbox.Quaternions2Euler2(q_vector);
    //----------------------------------------------------------------------------------------
    double Lb = M_Aero_NED[0][0] + M_Thrust_NED[0][0] ;
    double Mb = M_Aero_NED[1][0] + M_Thrust_NED[1][0] ;
    double Nb = M_Aero_NED[2][0] + M_Thrust_NED[2][0] ;
	
	double Ixx = InertiaTensor[0][0];
	double Iyy = InertiaTensor[1][1];
	double Izz = InertiaTensor[2][2];
	//double Ixy = InertiaTensor[0][1];
	double Ixz = InertiaTensor[0][2];
	//  double Iyx = InertiaTensor[][];
	//double Iyz = InertiaTensor[2][1];
	
	// Angular Rates
	// p dot:
	dxdt[11] = 1/(Ixx + Ixz*Ixz/Izz) * (Lb + Ixz/Izz * Nb - Ixz*(Iyy-Ixx)/Izz*x[11]*x[12] - Ixz*Ixz/Izz*x[12]*x[13]);
	// q dot:
	dxdt[12] = 1/Iyy * (Mb - (Ixx-Izz)*x[11]*x[13] - Ixz * (x[11]*x[11] - x[13]*x[13])); 
	// r dot:
	dxdt[13] = 1/(Izz - Ixz/Ixx) * (Lb/Ixx + Nb + Ixz/Ixx * x[11]*x[12] - (Izz - Iyy)/Ixx * x[13]*x[12] - 
			( Iyy - Ixx )*x[11]*x[12] - Ixz*x[12]*x[13]); 

	}
	AngularMomentum_B[0][0] = M_Aero_B[0][0] + M_Thrust_B[0][0] ;
	AngularMomentum_B[1][0] = M_Aero_B[1][0] + M_Thrust_B[1][0] ;
	AngularMomentum_B[2][0] = M_Aero_B[2][0] + M_Thrust_B[2][0] ;	
	AngularRate[0][0] = x[11];
	AngularRate[1][0] = x[12];
	AngularRate[2][0] = x[13];
	//atmosphere.getAngleOfAttack() = EulerAngle[0][0] - V_NED_ECEF_spherical[1];
	}
    	//-------------------------------------------------------------------------------------------------------------------
    	// 						   Update Event handler
    	//-------------------------------------------------------------------------------------------------------------------
	    if(stophandler_ON) {
	    for(int i=0;i<STOP_Handler.size();i++) {STOP_Handler.get(i).Update_StopCondition(t, x);}
	    }
}
//********************************************************************************************************************************** 
/*
    
  										Launch Integration Module  
    
*/    
//********************************************************************************************************************************** 
    public static void launchIntegrator( IntegratorData integratorData,
    										 List<SequenceElement> SEQUENCE_DATA, 
    										 double[] error_file ,
    										 SpaceShip spaceElement ){
//----------------------------------------------------------------------------------------------
// 						Prepare integration 
//----------------------------------------------------------------------------------------------
	try {
			SET_Constants(integratorData.getTargetBody());
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

	spaceShip = spaceElement;
	coordinateTransformation =  new CoordinateTransformation();
//----------------------------------------------------------------------------------------------	
	if(integratorData.getVelocityVectorCoordSystem()==1) {
		spherical = true;
		System.out.println("READ: Spherical Velocity Vector Coordinates selected.");
	} else if (integratorData.getVelocityVectorCoordSystem()==2) {
		spherical = false;
		System.out.println("READ: Cartesian Velocity Vector Coordinates selected.");
	}
	if(integratorData.getDegreeOfFreedom()==3) {
		is_6DOF=false;
		System.out.println("READ: 3 Degree of Freedom Model selected.");
	} else if (integratorData.getDegreeOfFreedom()==6) {
		is_6DOF=true;
		System.out.println("READ: 6 Degree of Freedom Model selected.");
		InertiaTensor = spaceShip.getInertiaTensorMatrix();
		System.out.println("READ: Inertial Tensor set.");
		q_vector      = spaceShip.getInitialQuarterions();
		System.out.println("READ: Initial Attitude set.");
	}

	    cntr_h_init=integratorData.getInitRadius()-rm;
	    cntr_v_init=integratorData.getInitVelocity();

	    	 m_propellant_init = spaceShip.getPropulsion().getPrimaryPropellant();
	    	 Thrust_max 	       = spaceShip.getPropulsion().getPrimaryThrustMax();
	    	 Thrust_min		   = spaceShip.getPropulsion().getPrimaryThrustMin();
	    	 ISP_is			   = spaceShip.getPropulsion().getPrimaryISPMax();
	    	 if(spaceShip.getPropulsion().isPrimaryThrottleModel()) {
	    		 ISP_max = spaceShip.getPropulsion().getPrimaryISPMax();
	    		 ISP_min = spaceShip.getPropulsion().getPrimaryISPMin();
	    		 ISP_Throttle_model=true; 
	    	 }
	    	 M0 			  = spaceShip.getMass()  ; 
	    	 mminus	  	  = M0  ;
	    	 vminus		  = integratorData.getInitVelocity()  ;
	    	 v_touchdown	  = 0   ;
	    	 PROPread		  = true; 

    	 phimin=integratorData.getInitLongitude();
    	 tetamin=integratorData.getInitLatitude();
    	 groundtrack=0;
    	 ref_ELEVATION =  integratorData.getRefElevation();
 		if(stophandler_ON) {
    	 STOP_Handler = integratorData.getIntegStopHandler(); 
 		}
//----------------------------------------------------------------------------------------------
//					Gravity Setup	
//----------------------------------------------------------------------------------------------
 		Gravity.setMu(mu);
 		Gravity.setRm(rm);
 		Gravity.setTARGET(integratorData.getTargetBody());
//----------------------------------------------------------------------------------------------
//					Sequence Setup	
//----------------------------------------------------------------------------------------------
		Sequence_RES_closed=false;
		SEQUENCE_DATA_main = SEQUENCE_DATA;  // Sequence data handover
		CTRL_steps.clear();
//----------------------------------------------------------------------------------------------
//					Integrator setup	
//----------------------------------------------------------------------------------------------
		FirstOrderIntegrator IntegratorModule = integratorData.getIntegrator();
//----------------------------------------------------------------------------------------------
	        FirstOrderDifferentialEquations ode = new Simulation();
	        //------------------------------
	        ATM_DATA.removeAll(ATM_DATA);
	        try {
				ATM_DATA = AtmosphereModel.INITIALIZE_ATM_DATA(integratorData.getTargetBody());
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
	        } else {  dimension =  7; // 3 DOF model 
	        }
		        double[] y = new double[dimension]; // Result vector
  			if(is_6DOF) {
	  		       // double[] y = new double[13]; // Result vector
	  	        	V_NED_ECEF_spherical[0]=integratorData.getInitVelocity();
	  	        	V_NED_ECEF_spherical[1]= integratorData.getInitFpa();
	  	        	V_NED_ECEF_spherical[2]= integratorData.getInitAzimuth();
	  		// Position 
	  		        y[0] = integratorData.getInitLongitude();
	  		        y[1] = integratorData.getInitLatitude();
	  		        y[2] = integratorData.getInitRadius();
	  		        
	  		// Velocity
			  		        if(spherical) {
			  		        	
			  		        y[3] = integratorData.getInitVelocity();
			  		        y[4] = integratorData.getInitFpa();
			  		        y[5] = integratorData.getInitAzimuth();
			  		        
			  		        } else {
			  		        	V_NED_ECEF_spherical[0]=integratorData.getInitVelocity();
			  		        	V_NED_ECEF_spherical[1]=integratorData.getInitFpa();
			  		        	V_NED_ECEF_spherical[2]=integratorData.getInitAzimuth();
			  		        	V_NED_ECEF_cartesian = Mathbox.Spherical2Cartesian_Velocity(V_NED_ECEF_spherical);
			  			        y[3] = V_NED_ECEF_cartesian[0];
			  			        y[4] = V_NED_ECEF_cartesian[1];
			  			        y[5] = V_NED_ECEF_cartesian[2];
			  			        //V_NED_ECEF_spherical = Cartesian2Spherical_Velocity(V_NED_ECEF_cartesian);
			  			        //System.out.println(x3+"|"+x4+"|"+x5+"|"+V_NED_ECEF_cartesian[0]+"|"+V_NED_ECEF_cartesian[1]+"|"+V_NED_ECEF_cartesian[2]+"|"+V_NED_ECEF_spherical[0]+"|"+V_NED_ECEF_spherical[1]+"|"+V_NED_ECEF_spherical[2]+"|");
			  		        }
	  		// S/C Mass        
	  		        y[6] = spaceShip.getMass();
	  				INITIALIZE_FlightController(y) ;
	  				// Attitude and Rotational Motion
	  				y[7]  = q_vector[0][0];
	  				y[8]  = q_vector[1][0];
	  				y[9]  = q_vector[2][0];
	  				y[10] = q_vector[3][0];
	  				y[11] = AngularRate[0][0];
	  				y[12] = AngularRate[1][0];
	  				y[13] = AngularRate[2][0];
	  				
  				
  			} else { // 3DOF case
			      //  double[] y = new double[7]; // Result vector
		        	V_NED_ECEF_spherical[0]=integratorData.getInitVelocity();
		        	V_NED_ECEF_spherical[1]=integratorData.getInitFpa();
		        	V_NED_ECEF_spherical[2]=integratorData.getInitAzimuth();
			// Position 
			        y[0] = integratorData.getInitLongitude();
			        y[1] = integratorData.getInitLatitude();
			        y[2] = integratorData.getInitRadius();
			        
			// Velocity
			        if(spherical) {
			        	
			        y[3] = integratorData.getInitVelocity();
			        y[4] = integratorData.getInitFpa();
			        y[5] = integratorData.getInitAzimuth();
			        
			        } else {
			        	V_NED_ECEF_spherical[0]=integratorData.getInitVelocity();
			        	V_NED_ECEF_spherical[1]=integratorData.getInitFpa();
			        	V_NED_ECEF_spherical[2]=integratorData.getInitAzimuth();
			        	V_NED_ECEF_cartesian = Mathbox.Spherical2Cartesian_Velocity(V_NED_ECEF_spherical);
				        y[3] = V_NED_ECEF_cartesian[0];
				        y[4] = V_NED_ECEF_cartesian[1];
				        y[5] = V_NED_ECEF_cartesian[2];
				        //V_NED_ECEF_spherical = Cartesian2Spherical_Velocity(V_NED_ECEF_cartesian);
				        //System.out.println(x3+"|"+x4+"|"+x5+"|"+V_NED_ECEF_cartesian[0]+"|"+V_NED_ECEF_cartesian[1]+"|"+V_NED_ECEF_cartesian[2]+"|"+V_NED_ECEF_spherical[0]+"|"+V_NED_ECEF_spherical[1]+"|"+V_NED_ECEF_spherical[2]+"|");
			        }
			// S/C Mass        
			        y[6] = spaceShip.getMass();
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
	                double g_total = Math.sqrt(Gravity.getGravity2D(y)[0]*Gravity.getGravity2D(y)[0]+Gravity.getGravity2D(y)[1]*Gravity.getGravity2D(y)[1]);
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
	                	twrite = twrite + integratorData.getIntegTimeStep(); 
	                    steps.add(t + " " + 
	                    		  y[0] + " " + 
	                    		  y[1] + " " + 
	                    		  (y[2]-rm-ref_ELEVATION) + " " + 
	                    		  (y[2]-rm)+ " " + 
	                    		  y[2] + " " + 
	                    		  V_NED_ECEF_spherical[0]+ " " + 
	                    		  V_NED_ECEF_spherical[1] + " " + 
	                    		  V_NED_ECEF_spherical[2] + " " + 
	                    		  atmosphere.getDensity() + " " + 
	                    		  atmosphere.getDragForce() + " " +
	                    		  atmosphere.getLiftForce() + " " +
	                    		  atmosphere.getSideForce() + " " +
	                    		  Gravity.getGravity2D(y)[0] + " " +
	                    		  Gravity.getGravity2D(y)[1] + " " +
	                    		  g_total + " " +
	                    		  atmosphere.getStaticTemperature()+ " " +
	                    		  atmosphere.getMach()+ " " +
	                    		  atmosphere.getPressureCoefficient()+ " " +
	                    		  atmosphere.getGasConstant()+ " " +
	                    		  atmosphere.getStaticPressure()+ " " +
	                    		  atmosphere.getDragCoefficient()+ " " +
	                    		  atmosphere.getLiftCoefficient()+ " " +
	                    		  atmosphere.getBankAngle()+ " " +
	                    		  atmosphere.getFlowzone()+ " " +
	                    		  atmosphere.getDynamicPressure()+ " " +
	                    		  atmosphere.getDragCoefficientContinuumFlow()+ " " +
	                    		  atmosphere.getCdC()+ " " +
	                    		  y[6]+ " " +
	                    		  ymo[3]/9.81+ " " +
	                    		  E_total+ " " + 
	                    		  (Throttle_CMD*100)+ " "+ 
	                    		  (m_propellant_init-(M0-y[6]))/m_propellant_init*100+" "+ 
	                    		  (Thrust_is)+" "+
	                    		  (Thrust_is/y[6])+" "+
	                    		  (V_NED_ECEF_spherical[0]*Math.cos(V_NED_ECEF_spherical[1]))+" "+
	                    		  (V_NED_ECEF_spherical[0]*Math.sin(V_NED_ECEF_spherical[1]))+" "+
	                    		  (acc_deltav)+" "+
	                    		  active_sequence+" "+
	                    		  (groundtrack/1000)+" "+
	                    		  CTRL_TM_Error+" "+
	                    		  CTRL_TVC_Error+" "+
	                    		  CTRL_Time+" "+
	                    		  (PI-y[4]-Thrust_Deviation)+" "+
	                    		  Thrust_Deviation+" "+
	                    		  coordinateTransformation.getC_B2NED()[0][0]+" "+
	                    		  coordinateTransformation.getC_B2NED()[1][0]+" "+
	                    		  coordinateTransformation.getC_B2NED()[2][0]+" "+
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
	                    		  ISP_is+" "+
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
	                    		  AngularRate[0][0]+" "+
	                    		  AngularRate[1][0]+" "+
	                    		  AngularRate[2][0]+" "+
	                    		  AngularMomentum_B[0][0]+" "+
	                    		  AngularMomentum_B[1][0]+" "+
	                    		  AngularMomentum_B[2][0]+" "+
	                    		  EulerAngle[0][0]+" "+
	                    		  EulerAngle[1][0]+" "+
	                    		  EulerAngle[2][0]+" "+
	                    		  atmosphere.getAngleOfAttack()+" "
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
	        	IntegratorModule.addEventHandler(interimEvent,0.1,1.0e-2,100);
	        	System.out.println("Handler: "+STOP_Handler.get(i).get_val_condition());
	        }
	        }
	        IntegratorModule.addEventHandler(AltitudeHandler,1,1.0e-4,50);
	        IntegratorModule.addEventHandler(VelocityHandler,1,1.0e-3,30);
	        System.out.println(""+STOP_Handler.size()+" Event Handler added.");
	        IntegratorModule.addStepHandler(WriteOut);
	        System.out.println("Integrator start");
	        System.out.println("------------------------------------------");
	        long startTime   = System.nanoTime();
	        boolean integ_error=false; 
	        try {
	        	double t= integratorData.getMaxIntegTime();
	        	IntegratorModule.integrate(ode, 0.0, y, t, y);
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
	public static double getRef_ELEVATION() {
		return ref_ELEVATION;
	}
	public static double getRm() {
		return rm;
	}
}