package Simulator_main; 

//import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

import Model.AtmosphereModel;
import Model.Gravity;
import Model.atm_dataset;
import Toolbox.Mathbox;
import Controller.LandingCurve;
import FlightElement.SpaceShip;

public class RealTimeSimulation implements FirstOrderDifferentialEquations {
		//----------------------------------------------------------------------------------------------------------------------------
		//				                              !!!  Control variables  !!!
		//----------------------------------------------------------------------------------------------------------------------------
		public static boolean HoverStop          = false; 
	    public static boolean ctrl_callout       = false; 
	    public static boolean ISP_Throttle_model = false; 
	    public static boolean stophandler_ON     = true; 
	    
	    public static 	    boolean spherical = false;	  // If true -> using spherical coordinates in EoM for velocity vector, else -> cartesian coordinates
	    public static 		boolean is_6DOF   = false;    // Switch 3DOF to 6DOF: If true -> 6ODF, if false -> 3DOF 
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
		    public static double P = 0 ;      				// static pressure [Pa]
		    public static double cp = 0;						// 
		    public static double m0; 
		    public static int flowzone=0; 					// Flow zone continuum - transitional - free molecular flwo
		    public static double Cdm = 0; 					// Drag coefficient in contiuum flow; 
		    public static int TARGET=0;						// Target body index
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
			
			public static double[][] M_Aero_NED      = {{0},{0},{0}};
			public static double[][] M_Thrust_NED    = {{0},{0},{0}};
			public static double[][] M_Thrust_B      = {{0},{0},{0}};
			
			public static double[][] C_ECI_ECEF = {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix ECEF to ECI system
			public static double[][] C_NED_A 	= {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix Aerodynamic to NED system
			public static double[][] C_NED_B 	= {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix body fixed to NED system
			public static double[][] C_NED_ECEF = {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix ECEF to NED system
			public static double[][] C_B_A 		= {{0,0,0},{0,0,0},{0,0,0}}; 			// Rotational matrix Bodyfixed to Aerodynamic
			
			
			// 6 DOF Attitude variables: 
			public static double[][] q_vector        = {{0},
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
			public static double[][] AngularMomentum = {{0},
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
	        
	        public static boolean IsThrust=false;
	        
	        static DecimalFormat decf = new DecimalFormat("###.#");
	        static DecimalFormat df_X4 = new DecimalFormat("#.###");
	      //-------------------------------------------------------------------------------
	    public int getDimension() {
	    	if(is_6DOF) {
	    		return 14; // 6 DOF model 
	        } else {
	    		return 7; // 3 DOF model 
	    	}
	    }
	    
		public static double getRm() {
			return rm;
		}

		public static double[][] Quaternions2Euler(double[][] Quaternions){
			double[][] EulerAngles = {{0},{0},{0}};
			double a = Quaternions[0][0];
			double b = Quaternions[1][0];
			double c = Quaternions[2][0];
			double d = Quaternions[3][0];
			EulerAngles[0][0] = Math.atan2(2*(c*d+a*b),(a*a-b*b-c*c+d*d));
			EulerAngles[1][0] = Math.asin(-2*(b*d - a*c));
			EulerAngles[2][0] = Math.atan2( 2*(b*c + a*d),(a*a + b*b - c*c - d*d));
			return EulerAngles;
		}
		
		public static double[][] Quaternions2Euler2(double[][] Quaternions){
			
			double[][] EulerAngles = {{0},{0},{0}};
			double w = Quaternions[0][0];
			double x = Quaternions[1][0];
			double y = Quaternions[2][0];
			double z = Quaternions[3][0];

	        double sqw = w * w;
	        double sqx = x * x;
	        double sqy = y * y;
	        double sqz = z * z;
	        double unit = sqx + sqy + sqz + sqw; // if normalized is one, otherwise
	        // is correction factor
	        double test = x * y + z * w;
	        if (test > 0.499 * unit) { // singularity at north pole
	        	EulerAngles[1][0] = 2 * Math.atan2(x, w);
	        	EulerAngles[0][0] = FastMath.PI/2;
	        	EulerAngles[2][0] = 0;
	        } else if (test < -0.499 * unit) { // singularity at south pole
	        	EulerAngles[1][0] = -2 * FastMath.atan2(x, w);
	        	EulerAngles[0][0] = -FastMath.PI/2;
	        	EulerAngles[2][0] = 0;
	        } else {
	        	EulerAngles[1][0] = FastMath.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw); // roll or heading 
	        	EulerAngles[0][0] = FastMath.asin(2 * test / unit); // pitch or attitude
	        	EulerAngles[2][0] = FastMath.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw); // yaw or bank
	        }
			//-------------------------
			/*
		    EulerAngles[0][0] = Math.atan2(2*(qw*qx+qy*qz), 1-2*(qx*qx+qy*qy));
		    EulerAngles[1][0] = Math.asin(qw*qy - qz*qx);
		    EulerAngles[2][0] = Math.atan2(2*(qw*qz+qx*qy), 1-2*(qy*qy + qz*qz));
			*/
		    
 			return EulerAngles;
		}

		public static void SET_Constants(int TARGET) throws IOException{
		    Lt    = DATA_MAIN[TARGET][3];    	// Average collision diameter (CO2)         [m]
		    mu    = DATA_MAIN[TARGET][1];    	// Standard gravitational constant          []
		    rm    = DATA_MAIN[TARGET][0];    	// Planets mean radius                      [m]
		    omega = DATA_MAIN[TARGET][2];		// Planets rotational speed     		    [rad/s]
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
    		if(spherical) {}// 2D model for shperical velcotity vector computation
    		else {
    			g = Gravity.getGravity3D(x, r_ECEF_cartesian); 
        		g_NED   =  Mathbox.Multiply_Matrices(C_NED_ECEF, g);	 
        		// Gravitational Force (wrt NED System)
	    	    	F_Gravity_NED[0][0] = x[6]*g_NED[0][0];
	    	    	F_Gravity_NED[1][0] = x[6]*g_NED[1][0];
	    	    	F_Gravity_NED[2][0] = x[6]*g_NED[2][0];
    		}
    	//-------------------------------------------------------------------------------------------------------------------
    	//								Sequence management and Flight controller 
    	//-------------------------------------------------------------------------------------------------------------------

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
	   	
	   	if(IsThrust) {
	   		Thrust = Thrust_max;
	   		System.out.println("THRUST");
	   		//F_Aero_A[0][0] -= Thrust;
	   	} else {
	   		Thrust=0;
	   	}
	   	
	   	F_Thrust_B[0][0] =  0;//Thrust * Math.cos(TVC_alpha)*Math.cos(TVC_beta);  
	   	F_Thrust_B[1][0] =  0;//Thrust * Math.cos(TVC_alpha)*Math.sin(TVC_beta);   
	   	F_Thrust_B[2][0] =  -Thrust;//Thrust * Math.sin(TVC_alpha);   
	   	
	   // 	M_Thrust_NED = Mathbox.Multiply_Matrices(C_NED_B, M_Thrust_B);
	   	M_Thrust_NED = M_Thrust_B;
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Set up force vector in NED  
    	//-------------------------------------------------------------------------------------------------------------------
	   	INITIALIZE_ROTATIONAL_MATRICES(x, t, BankAngle, EulerAngle); 
    	F_Aero_NED   	= Mathbox.Multiply_Matrices(C_NED_A, F_Aero_A) ;    	
    	F_Thrust_NED 	= Mathbox.Multiply_Matrices(C_NED_B, F_Thrust_B) ;
    	F_total_NED   	= Mathbox.Addup_Matrices(F_Aero_NED , F_Thrust_NED );
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    				ISP model for engine throttling
    	//-------------------------------------------------------------------------------------------------------------------
    //	if(ISP_Throttle_model) {ThrottleMODEL_get_ISP( ISP_min,  ISP_max,  Throttle_CMD);}
    	//-------------------------------------------------------------------------------------------------------------------
    	// 										Delta-v integration
    	//-------------------------------------------------------------------------------------------------------------------
    	acc_deltav = acc_deltav + ISP*g0*Math.log(mminus/x[6]);
    	mminus=x[6];
    	//-------------------------------------------------------------------------------------------------------------------
    	// 										  Ground track 
    	//-------------------------------------------------------------------------------------------------------------------
    	double r=rm;  // <-- reference radius for projection. Current projection set for mean radius 
    	double phi    = x[0];
    	double theta  = x[1];
    double dphi   = Math.abs(phi-phimin);
    	double dtheta = Math.abs(theta-tetamin); 
    	double ds     = r*Math.sqrt(LandingCurve.squared(dphi) + LandingCurve.squared(dtheta));
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
	   //System.out.println(Thrust);
	    dxdt[6] = - Thrust/(ISP*g0) ;   
	    System.out.println(Thrust+" | "+ISP);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 						   Rotataional motion
    	//-------------------------------------------------------------------------------------------------------------------
	    if(is_6DOF) {
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
		    		double[][] Q = {{ 0    , x[13],-x[12], x[11]}, 
		    				        {-x[13], 0    , x[11], x[12]},
		    				        { x[12],-x[11], 0    , x[13]},
		    				        {-x[11],-x[12],-x[13], 0    }}; 
		    		
		    	    q_vector[0][0] = x[7];
		    	    q_vector[1][0] = x[8];
		    	    q_vector[2][0] = x[9];
		    	    q_vector[3][0] = x[10];
		    	    
		    	    EulerAngle = Quaternions2Euler2(q_vector);
		    		double[][] q_vector_dot =  Mathbox.Multiply_Scalar_Matrix(0.5, Mathbox.Multiply_Matrices(Q, q_vector)); 
		    		dxdt[7] =  q_vector_dot[0][0];  // e1 dot
		    		dxdt[8] =  q_vector_dot[1][0];  // e2 dot 
		    		dxdt[9] =  q_vector_dot[2][0];  // e3 dot
		    		dxdt[10] = q_vector_dot[3][0];  // e4 dot
	
		    	    EulerAngle = Quaternions2Euler2(q_vector);
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
		    		//----------------------------------------------------------------------------------------
AngularMomentum[0][0] = M_Aero_NED[0][0] + M_Thrust_NED[0][0] ;
AngularMomentum[1][0] = M_Aero_NED[1][0] + M_Thrust_NED[1][0] ;
AngularMomentum[2][0] = M_Aero_NED[2][0] + M_Thrust_NED[2][0] ;	
AngularRate[0][0] = x[11];
AngularRate[1][0] = x[12];
AngularRate[2][0] = x[13];
//AngleOfAttack = EulerAngle[0][0] - V_NED_ECEF_spherical[1];
	    }
}
    
public static RealTimeResultSet Launch_Integrator( int INTEGRATOR, int target, double x0, double x1, double x2, 
		double x3, double x4, double x5, double x6, double t, double dt_write, double reference_elevation, 
		double[][] Init_Quarternions , double[][] InitPQR, boolean isThrust, double[][] MRCS, SpaceShip spaceShip){
//----------------------------------------------------------------------------------------------
// 						Prepare integration 
//----------------------------------------------------------------------------------------------
	try {
			SET_Constants(target);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
//----------------------------------------------------------------------------------------------
//	
//----------------------------------------------------------------------------------------------	
	
	M_Thrust_B = MRCS;
	
	q_vector    = Init_Quarternions;
	AngularRate = InitPQR;
	InertiaTensor = spaceShip.getInertiaTensorMatrix();
	
	spherical=false;
	is_6DOF=true;
	
	IsThrust = isThrust;
	
//----------------------------------------------------------------------------------------------
//	Gravity Setup	
//----------------------------------------------------------------------------------------------
Gravity.setMu(mu);
Gravity.setRm(rm);
Gravity.setTARGET(target);	
//----------------------------------------------------------------------------------------------
	    cntr_h_init=x2-rm;
	    cntr_v_init=x3;
	    	 ISP          	   = spaceShip.getPropulsion().getPrimaryISPMax();
	    	 m_propellant_init = spaceShip.getPropulsion().getPrimaryPropellant();
	    	 Thrust_max 	       = spaceShip.getPropulsion().getPrimaryThrustMax();
	    	 Thrust_min        = spaceShip.getPropulsion().getPrimaryThrustMin();
	    	 SurfaceArea  	   = spaceShip.getAeroElements().getSurfaceArea();
	    	 M0 			  = x6  ; 
	    	 mminus	  	  = M0  ;
	    	 vminus		  = x3  ;
	    	 v_touchdown	  = 0   ;
	    	 PROPread		  = true; 
    	 phimin=x0;
    	 tetamin=x1;
    	 groundtrack=0;
    	 ref_ELEVATION =  reference_elevation;
//----------------------------------------------------------------------------------------------
//					Integrator setup	
//----------------------------------------------------------------------------------------------
		FirstOrderIntegrator dp853;
	         dp853 = new ClassicalRungeKuttaIntegrator(dt_write);
//----------------------------------------------------------------------------------------------
	        FirstOrderDifferentialEquations ode = new RealTimeSimulation();
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
	  		        y[2] = x2+rm;
	  		        
	  		// Velocity
			  		        if(spherical) {
			  		        	
			  		        y[3] = x3;
			  		        y[4] = x4;
			  		        y[5] = x5;
			  		        
			  		        } else {
			  		        	V_NED_ECEF_spherical[0]=x3;
			  		        	V_NED_ECEF_spherical[1]=x4;
			  		        	V_NED_ECEF_spherical[2]=x5;
			  		        	V_NED_ECEF_cartesian = Mathbox.Spherical2Cartesian_Velocity(V_NED_ECEF_spherical);
			  			        y[3] = V_NED_ECEF_cartesian[0];
			  			        y[4] = V_NED_ECEF_cartesian[1];
			  			        y[5] = V_NED_ECEF_cartesian[2];
			  			        //V_NED_ECEF_spherical = Cartesian2Spherical_Velocity(V_NED_ECEF_cartesian);
			  			        //System.out.println(x3+"|"+x4+"|"+x5+"|"+V_NED_ECEF_cartesian[0]+"|"+V_NED_ECEF_cartesian[1]+"|"+V_NED_ECEF_cartesian[2]+"|"+V_NED_ECEF_spherical[0]+"|"+V_NED_ECEF_spherical[1]+"|"+V_NED_ECEF_spherical[2]+"|");
			  		        }
	  		// S/C Mass        
	  		        y[6] = x6;
	  		        m0 = x6;
	  				// Attitude and Rotational Motion
	  				y[7]  = Init_Quarternions[0][0];
	  				y[8]  = Init_Quarternions[1][0];
	  				y[9]  = Init_Quarternions[2][0];
	  				y[10] = Init_Quarternions[3][0];
	  				y[11] = InitPQR[0][0];
	  				y[12] = InitPQR[1][0];
	  				y[13] = InitPQR[2][0];
	  				
  				
  			} else { // 3DOF case
			      //  double[] y = new double[7]; // Result vector
		        	V_NED_ECEF_spherical[0]=x3;
		        	V_NED_ECEF_spherical[1]=x4;
		        	V_NED_ECEF_spherical[2]=x5;
			// Position 
			        y[0] = x0;
			        y[1] = x1;
			        y[2] = x2+rm;
			        
			// Velocity
			        if(spherical) {
			        	
			        y[3] = x3;
			        y[4] = x4;
			        y[5] = x5;
			        
			        } else {
			        	V_NED_ECEF_spherical[0]=x3;
			        	V_NED_ECEF_spherical[1]=x4;
			        	V_NED_ECEF_spherical[2]=x5;
			        	V_NED_ECEF_cartesian = Mathbox.Spherical2Cartesian_Velocity(V_NED_ECEF_spherical);
				        y[3] = V_NED_ECEF_cartesian[0];
				        y[4] = V_NED_ECEF_cartesian[1];
				        y[5] = V_NED_ECEF_cartesian[2];
				        //V_NED_ECEF_spherical = Cartesian2Spherical_Velocity(V_NED_ECEF_cartesian);
				        //System.out.println(x3+"|"+x4+"|"+x5+"|"+V_NED_ECEF_cartesian[0]+"|"+V_NED_ECEF_cartesian[1]+"|"+V_NED_ECEF_cartesian[2]+"|"+V_NED_ECEF_spherical[0]+"|"+V_NED_ECEF_spherical[1]+"|"+V_NED_ECEF_spherical[2]+"|");
			        }
			// S/C Mass        
			        y[6] = x6;
			        m0 = x6;
  			}
//----------------------------------------------------------------------------------------------
  			RealTimeResultSet realTimeResultSet = new RealTimeResultSet();
	        StepHandler WriteOut = new StepHandler() {

	            public void init(double t0, double[] y0, double t) {

	            }
	            
	            public void handleStep(StepInterpolator interpolator, boolean isLast) {
	                double   t = interpolator.getCurrentTime();
	                if(switcher) {tis=t;switcher=false;}else {tminus=t;switcher=true;};if(tis!=tminus) {val_dt=Math.abs(tis-tminus);}else{val_dt=0.01;};
	                double[] y     = interpolator.getInterpolatedState();

	                if(isLast) {
	                	realTimeResultSet.setAltitude((float) (y[2]-rm-ref_ELEVATION));
	                	realTimeResultSet.setAzi((float) V_NED_ECEF_spherical[2]);
	                	realTimeResultSet.setFpa((float) V_NED_ECEF_spherical[1]);
	                	realTimeResultSet.setVelocity((float) V_NED_ECEF_spherical[0]);
	                	realTimeResultSet.setSCMass((float) y[6]);
	                	realTimeResultSet.setPQR(AngularRate);
	                	realTimeResultSet.setEulerX((float) EulerAngle[0][0]);
	                	realTimeResultSet.setEulerY((float) EulerAngle[1][0]);
	                	realTimeResultSet.setEulerZ((float) EulerAngle[2][0]);
	                	realTimeResultSet.setQuarternions(q_vector);
	                }
	            }
	            
	        };

	        dp853.addStepHandler(WriteOut);
	        /*
	        System.out.println("----------------------");
	        for(int i=0;i<y.length;i++) {
	        	System.out.println(y[i]);
	        }
	        */
	        try {
	        dp853.integrate(ode, 0.0, y, t, y);
	        } catch(NoBracketingException eNBE) {
	        	System.out.println("ERROR: Integrator failed:");
	        }

			return realTimeResultSet;
	       
		}
public static double getRef_ELEVATION() {
	return ref_ELEVATION;
}

}