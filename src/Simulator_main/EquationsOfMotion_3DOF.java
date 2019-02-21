package Simulator_main; 

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
import Controller.Flight_CTRL;

public class EquationsOfMotion_3DOF implements FirstOrderDifferentialEquations {
	//----------------------------------------------------------------------------------------------------------------------------
	//				Control variables
	public static boolean HoverStop = false; 
    public static boolean ShowWorkDirectory = true; 
    public static boolean macrun = false;
    public static boolean ctrl_callout = false; 
	//............................................                                       .........................................
	//
    //	                                                         Constants
	//
	//----------------------------------------------------------------------------------------------------------------------------
	   public static double[][] DATA_MAIN = { // RM (average radius) [m] || Gravitational Parameter [m3/s2] || Rotational speed [rad/s] || Average Collision Diameter [m]
				{6371000,3.9860044189E14,7.2921150539E-5,1.6311e-9}, 	// Earth
				{1737000,4903E9,2.661861E-6,320},						// Moon (Earth)
				{3389500,4.2838372E13,7.0882711437E-5,1.6311e-9},		// Mars
				{0,0,0,0},												// Venus
		};
	   public static int[] trigger_type_translator = {0,2,3};
	   	public static String[] str_target = {"Earth","Moon","Mars", "Venus"};
	   	public static String[] IntegratorInputPath = {".\\INP\\INTEG\\00_DormandPrince853Integrator.inp",
	   												  ".\\INP\\INTEG\\01_ClassicalRungeKuttaIntegrator.inp",
	   												  ".\\INP\\INTEG\\02_GraggBulirschStoerIntegrator.inp",
	   												  ".\\INP\\INTEG\\03_AdamsBashfordIntegrator.inp"
	   	};
	   	public static String[] IntegratorInputPath_mac = {"/LandingSim-3DOF/INP/INTEG/00_DormandPrince853Integrator.inp",
					  "/LandingSim-3DOF/INP/INTEG/01_ClassicalRungeKuttaIntegrator.inp",
					  "/LandingSim-3DOF/INP/INTEG/02_GraggBulirschStoerIntegrator.inp",
					  "/LandingSim-3DOF/INP/INTEG/03_AdamsBashfordIntegrator.inp"
};
	   	public static String PropulsionInputFile        = ".\\INP\\PROP\\prop.inp";
	    public static String PropulsionInputFile_mac    = "/LandingSim-3DOF/INP/PROP/prop.inp"  ;  		// Input: target and environment
	    
	  //----------------------------------------------------------------
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
		    public static double acc_deltav2= 0;
		    public static double twrite=0;
		    public static double local_delta_altitude = -4000;
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
		    static double Throttle_CMD=0;				// Main engine throttle command [-]
		    static double m_propellant_init = 0;     	// Initial propellant mass [kg]
		    static double M0=5000; 
		    static double Thrust_max=0; 
		    static double Thrust_min=0;
		    public static double cntr_h_init=0;
		    public static double cntr_v_init=0;
		    public static double ctrl_add;
		    public static int ctrl_curve;
	        private static List<atm_dataset> ATM_DATA = new ArrayList<atm_dataset>(); 
	    	private static List<SequenceElement> SEQUENCE_DATA_main = new ArrayList<SequenceElement>(); 
	    	private static List<Flight_CTRL> Flight_Controller = new ArrayList<Flight_CTRL>(); 
	        //private static List<Flight_CTRL> FlightController_LIST = new ArrayList<Flight_CTRL>(); 
	        static boolean PROPread = false; 
	        public static int active_sequence = 0 ; 
	        static DecimalFormat decf = new DecimalFormat("###.#");
	        
	    public int getDimension() {
	        return 7;
	    }
	    
		public static void SET_Constants(int TARGET) throws IOException{
		    Lt    = DATA_MAIN[TARGET][3];    	// Average collision diameter (CO2)         [m]
		    mu    = DATA_MAIN[TARGET][1];    	// Standard gravitational constant (Mars)   [m3/s2]
		    rm    = DATA_MAIN[TARGET][0];    	// Planets average radius                   [m]
		    omega = DATA_MAIN[TARGET][2];		// Planets roational speed     				[rad/s]
		    //--------------------------------------------------------------------
		    // 			Write env.inp : 
			 PrintWriter writer = new PrintWriter(new File(".\\INP\\env.inp"), "UTF-8");
			for (int i = 0; i < 6; i++) {
				if(i==0) {
				writer.println(""+TARGET);
				} else if (i<5){
				writer.println(""+DATA_MAIN[TARGET][i-1]);
				}
			}
			writer.close();
		}
		public static void UPDATE_FlightController(Flight_CTRL NewElement){	   
			   if (Flight_Controller.size()==0){ EquationsOfMotion_3DOF.Flight_Controller.add(NewElement); 
			   } else {EquationsOfMotion_3DOF.Flight_Controller.add(NewElement); } 
		   }
		
		public static void INITIALIZE_FlightController() {
			for(int i=0;i<SEQUENCE_DATA_main.size();i++) {
				int ctrl_ID = SEQUENCE_DATA_main.get(i).get_sequence_controller_ID();
				// -> Create new Flight controller 
				Flight_CTRL NewFlightController = new Flight_CTRL(ctrl_ID, true, 0,  0,  0,  0,  0,  m_propellant_init,  cntr_v_init,  cntr_h_init,  0,  ctrl_add,  Thrust_max,  Thrust_min,  0,  0,  ctrl_curve,  val_dt,0,0,0,0,0);
				UPDATE_FlightController(NewFlightController);
			}
		}
    public void computeDerivatives(double t, double[] x, double[] dxdt) {
    	//-------------------------------------------------------------------------------------------------------------------
    	//								    	Gravitational environment
    	//-------------------------------------------------------------------------------------------------------------------
    	gr = GravityModel.get_gr( x[2],  x[1],  rm,  mu, TARGET);
    	gn = GravityModel.get_gn(x[2], x[1],  rm,  mu, TARGET); 
    	//-------------------------------------------------------------------------------------------------------------------
    	//								Sequence management and Flight controller 
    	//-------------------------------------------------------------------------------------------------------------------
    	if(active_sequence<SEQUENCE_DATA_main.size()-1) {
			int trigger_type = SEQUENCE_DATA_main.get(active_sequence).get_trigger_end_type();
			double trigger_value = SEQUENCE_DATA_main.get(active_sequence).get_trigger_end_value();
			if(trigger_type==0) {
					if(t>trigger_value) {active_sequence++;cntr_v_init = x[3];cntr_h_init=x[2]-rm-local_delta_altitude;}
					cntr_v_init = x[3];
			} else if (trigger_type==1) {
					if( (x[2]-rm-local_delta_altitude)<trigger_value) {active_sequence++;}
			} else if (trigger_type==2) {
					if( x[3]<trigger_value) {active_sequence++;}
     		}
    	}
    	//System.out.println("Altitude "+decf.format((x[2]-rm))+" | " + active_sequence);
    	int sequence_type = SEQUENCE_DATA_main.get(active_sequence).get_sequence_type();
    	if(sequence_type==3) { // Controlled Flight Sequence 
    	if (ctrl_callout) {System.out.println("Altitude "+decf.format((x[2]-rm))+" | Controller " + Flight_Controller.get(active_sequence).get_ctrl_ID() +" set ON");}
    	// Update Controller inputs:
    	double alt = (x[2]-rm-local_delta_altitude);
		Flight_Controller.get(active_sequence).Update_Flight_CTRL(true,  alt, x[3],  x[4],  M0,  x[6],  m_propellant_init,  cntr_v_init,  cntr_h_init,  0,  ctrl_add,  Thrust_max,  Thrust_min,  ctrl_curve,  val_dt) ;
    	// Compile controller output: 
    	Thrust        = Flight_Controller.get(active_sequence).get_thrust_cmd();
    	Throttle_CMD  = Flight_Controller.get(active_sequence).get_ctrl_throttle_cmd();
    	} else if (sequence_type==2) { // Continous Propulsive Flight Sequence 
    		if((m_propellant_init-(M0-x[6]))>0) {
    			Thrust = Thrust_max; 
    			Throttle_CMD = 1; 
    		}else { // Empty tanks
        		Thrust = 0; 
        		Throttle_CMD = 0; 
    		}
    	} else if (sequence_type==1) { // Coasting Sequence 
    		Thrust = 0; 
    		Throttle_CMD = 0;
    	} else {
    		System.out.println("ERROR: Sequence type out of range");
    	}
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									Atmosphere and (external) Force Definition
    	//-------------------------------------------------------------------------------------------------------------------
    	if (x[2]-rm>200000 || TARGET == 1){ // In space conditions: 
    		// Set atmosphere properties to zero: 
	    		rho   = 0; 																// Density 							[kg/m3]
	    		qinf  = 0;																// Dynamic pressure 				[Pa]
	    		T     = 0 ; 															// Temperature 						[K]
	    		gamma = 0 ; 															// Heat capacity ratio 				[-]
	    		R	  = 0; 																// Gas constant 					[J/kgK]
	    		Ma 	  = 0; 																// Mach number 						[-]
	       //-----------------------------------------------------------------------------------------------
	      	  D  =  - Thrust  ;            				  								// Drag Force 						[N] --> From Thrust only
	      	  L  =    	  0   ;                           								// Aerodynamic lift Force 			[N]
	      	  Ty =        0   ;                           								// Aerodynamic side Force 			[N]
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
    	acc_deltav = acc_deltav + ISP*g0*Math.log(mminus/x[6]);
    	mminus=x[6];
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									Equations of Motion
    	//-------------------------------------------------------------------------------------------------------------------	
    	// Position vector
	    dxdt[0] = x[3] * cos( x[4] ) * sin( x[5] ) / ( x[2] * cos( x[1] ) );
	    dxdt[1] = x[3] * cos( x[4] ) * cos( x[5] ) / x[2] ;
	    dxdt[2] = x[3] * sin( x[4] );
	    // Velocity vector
	    dxdt[3] = -gr * sin( x[4] ) + gn * cos( x[5] ) * cos( x[4] ) + D / x[6] + omega * omega * x[2] * cos( x[1] ) * ( sin( x[4] ) * cos( x[1] ) - cos( x[1] ) * cos( x[5] ) * sin( x[1] ) ) ;
	    dxdt[4] = ( x[3] / x[2] - gr/ x[3] ) * cos( x[4] ) - gn * cos( x[5] ) * sin( x[4] ) / x[3] + L / ( x[3] * x[6] ) + 2 * omega * sin( x[5] ) * cos( x[1] ) + omega * omega * x[2] * cos( x[1] ) * ( cos( x[4] ) * cos( x[1] ) + sin( x[4] ) * cos( x[5] ) * sin( x[1] ) ) / x[3] ;
	    dxdt[5] = x[3] * sin( x[5] ) * tan( x[1] ) * cos( x[4] ) / x[2] - gn * sin( x[5] ) / x[3] - Ty / ( x[3] - cos( x[4] ) * x[6] ) - 2 * omega * ( tan( x[4] ) * cos( x[5] ) * cos( x[1] ) - sin( x[1] ) ) + omega * omega * x[2] * sin( x[5] ) * sin( x[1] ) * cos( x[1] ) / ( x[3] * cos( x[4] ) ) ;
	    // System mass [kg]
	    dxdt[6] = - Thrust/(ISP*g0) ;                
}
    
public static void Launch_Integrator( int INTEGRATOR, int target, double x0, double x1, double x2, double x3, double x4, double x5, double x6, double t, double dt_write, double cmd_add, int ctrl_targetcurve, List<SequenceElement> SEQUENCE_DATA){
//----------------------------------------------------------------------------------------------
    if(ShowWorkDirectory) { }
    if(macrun) {
   	 String dir = System.getProperty("user.dir");
     	PropulsionInputFile = dir + PropulsionInputFile_mac;
   	 //System.out.println(PropulsionInputFile);
    }	
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
	    ctrl_curve=ctrl_targetcurve;
		try {
		prop_read = Tool.READ_PROPULSION_INPUT(PropulsionInputFile);
    	 ISP          	  = prop_read[0];
    	 m_propellant_init= prop_read[1];
    	 Thrust_max 	  = prop_read[2];
    	 Thrust_min		  = prop_read[3];
    	 M0 			  = x6  ; 
    	 mminus			  = M0  ;
    	 vminus			  = x3  ;
    	 ctrl_add 	      = cmd_add;
    	 PROPread		  = true; 
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("Error: Propulsion setting read failed. ISP set to zero. Propellant mass set to zero. Thrust set to zero. ");
			PROPread =false;
		}
//----------------------------------------------------------------------------------------------
//					Sequence Setup	
//----------------------------------------------------------------------------------------------
		SEQUENCE_DATA_main = SEQUENCE_DATA;
		INITIALIZE_FlightController() ;
//----------------------------------------------------------------------------------------------
//					Integrator setup	
//----------------------------------------------------------------------------------------------
		FirstOrderIntegrator dp853;
		String IntegInput ="";
		if(macrun){
			String dir = System.getProperty("user.dir");
			IntegInput = dir + IntegratorInputPath_mac[INTEGRATOR];
		} else {
			IntegInput = IntegratorInputPath[INTEGRATOR];
		}
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
	        FirstOrderDifferentialEquations ode = new EquationsOfMotion_3DOF();
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
	                if( t > twrite ) {
	                	twrite = twrite + dt_write; 
	                    steps.add(t + " " + 
	                    		  y[0] + " " + 
	                    		  y[1] + " " + 
	                    		  (y[2]-rm-local_delta_altitude) + " " + 
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
	                    		  Thrust+ " " +
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
	                    		  active_sequence+" "
	                    		  );
	                }
	                if(isLast) {
	                    try{
	        	        	//DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	        	        	//Date date = new Date();
	        	           // String time = "" ;//+ dateFormat.format(date) ; 
	        	            String resultpath="";
	        	            if(macrun) {
	        	            	String dir = System.getProperty("user.dir");
	        	            	resultpath = dir + "/LandingSim-3DOF/results.txt";
	        	            } else {
	        	            	resultpath = "results.txt";
	        	            }
	                        PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
	                        for(String step: steps) {
	                            writer.println(step);
	                        }
	                        writer.close();
	                    } catch(Exception e) {};
	                }
	            }
	            
	        };
	        
	        EventHandler EventHandler_Touchdown = new EventHandler() {
				@Override
				public double g(double t, double[] y) {
					// TODO Auto-generated method stub
					// return y[2] - rm; // Altitude = 0 relative to mean elevation         -> integration stop 
					return y[2] - rm - local_delta_altitude; // Altitude = 0 relative to landing site elevation -> integration stop 
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
	        EventHandler EventHandler_NegVelocity = new EventHandler() {
				@Override
				public double g(double t, double[] y) {
					// TODO Auto-generated method stub
					return y[3]; // Altitude = 0 -> integration stop 
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
	        
	        dp853.addStepHandler(WriteOut);
	        dp853.addEventHandler(EventHandler_Touchdown,1,1.0e-3,30);
	        if(HoverStop) {dp853.addEventHandler(EventHandler_NegVelocity,1,1.0e-3,30);}
	        try {
	        dp853.integrate(ode, 0.0, y, t, y);
	        } catch(NoBracketingException eNBE) {
	        	System.out.println("ERROR: Integrator failed:");
	        	System.out.println(eNBE);
	        } catch (org.apache.commons.math3.exception.NumberIsTooSmallException eMSS) {
	        	System.out.println("ERROR: Integrator failed: Minimal stepsize reached");
	        	System.out.println(eMSS);
	        }

	       System.out.println("Success --> Integrator stop. ");      
		}
	

}