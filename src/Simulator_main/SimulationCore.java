package Simulator_main; 

//import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import Model.ForceModel;
import Model.GravityModel;
import Model.AtmosphereModel;
import Model.DataSets.ActuatorSet;
import Model.DataSets.AerodynamicSet;
import Model.DataSets.AtmosphereSet;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.ErrorSet;
import Model.DataSets.ForceMomentumSet;
import Model.DataSets.GravitySet;
import Model.DataSets.MasterSet;
import Simulator_main.DataSets.IntegratorData;
import Simulator_main.DataSets.PrevailingDataSet;
import Simulator_main.DataSets.RealTimeContainer;
import Simulator_main.DataSets.RealTimeResultSet;
import Simulator_main.DataSets.RotElements;
import Simulator_main.DataSets.SimulationConstants;
import utils.Mathbox;
import utils.Quaternion;
import utils.EulerAngle;
import Controller.LandingCurve;
import FlightElement.SpaceShip;

public class SimulationCore implements FirstOrderDifferentialEquations {
//----------------------------------------------------------------------------------------------------------------------------
//				                              !!!  Control variables  !!!
//----------------------------------------------------------------------------------------------------------------------------
 static boolean HoverStop          = false; 
 static boolean ctrl_callout       = false; 
 static boolean ISP_Throttle_model = false; 
 static boolean stophandler_ON     = true; 

 static boolean spherical    = false;	 // If true -> using spherical coordinates in EoM for velocity vector, else -> cartesian coordinates
 static boolean is_6DOF      = true;     // Switch 3DOF to 6DOF: If true -> 6ODF, if false -> 3DOF 
 static int SixDoF_Option    = 1;  
 static boolean FlatEarther  = false;	 // Reduced complexity (no rot. Earth/no rot. NED) for attitude equations
 static int eulerConvention  = 321;	     // Rotational order for Euler angles 
//............................................                                       .........................................
//
//	                                                         Constants
//
//----------------------------------------------------------------------------------------------------------------------------    
private static SimulationConstants constants = new SimulationConstants();
//----------------------------------------------------------------------------------------------------------------------------
//............................................                                       .........................................
//
//	                                             Simulator public variables
//
//----------------------------------------------------------------------------------------------------------------------------
public static double mminus=0;
public static double vminus=0;
public static double val_dt=0;
public static double acc_deltav = 0; 
public static double ref_ELEVATION = 0;
//----------------------------------------
// Groundtrack estimation variables 
private static double groundtrack = 0; 
private static double phimin=0;
private static double tetamin=0;
//----------------------------------------------------------------------------------------------------------------------
//			Position and Velocity 
//----------------------------------------------------------------------------------------------------------------------
private static double[] V_NED_ECEF_spherical = {0,0,0};			// Velocity vector in NED system with respect to ECEF in spherical coordinates  [m/s]
private static double[] V_NED_ECEF_cartesian = {0,0,0};			// Velocity vector in NED system with respect to ECEF in cartesian coordinates [m/s]

private static double[] r_ECEF_cartesian = {0,0,0};				// position coordinates with respect to ECEF in cartesian coordinates [m/s]
private static double[] r_ECEF_spherical = {0,0,0};				// position coordinates with respect to ECEF in spherical coordinates [m/s]
//----------------------------------------------------------------------------------------------------------------------
//			Forces 
//----------------------------------------------------------------------------------------------------------------------
private static double[][] F_total_NED = {{0},{0},{0}};						// Total force vector in NED coordinates [N]
//----------------------------------------------------------------------------------------------------------------------
// 				Attitude variables 
//----------------------------------------------------------------------------------------------------------------------
private static Quaternion q_B2IN = new Quaternion(1,0,0,0);

private static EulerAngle eulerAngle = new EulerAngle();

private static double[][] AngularRate     = {{0},
											 {0},
											 {0}};					 // Angular Velocity {P, Q, R}T [rad/s] 

private static double[][] AngularMomentum_B = {{0},
											   {0},
											   {0}};					 // Angular Momentum (Total) [Nm] (Do not touch!)
private static CoordinateTransformation coordinateTransformation ;
//-----------------------------------------------------------------------------------------------------------------------
//				Mass and Inertia
//-----------------------------------------------------------------------------------------------------------------------
public static double[][] InertiaTensor   = {{   0    ,    0    ,   0},
	    										{   0    ,    0    ,   0},
	    										{   0    ,    0    ,   0}};  // Inertia Tensor []
//-----------------------------------------------------------------------------------------------------------------------
//				Main Data containers
//-----------------------------------------------------------------------------------------------------------------------
private static SpaceShip spaceShip = new SpaceShip();
private static IntegratorData integratorData = new IntegratorData();
private static AtmosphereSet atmosphereSet = new AtmosphereSet();
private static ForceMomentumSet forceMomentumSet= new ForceMomentumSet();
private static GravitySet gravitySet = new GravitySet();
private static AerodynamicSet aerodynamicSet = new AerodynamicSet();
private static ControlCommandSet controlCommandSet = new ControlCommandSet();
private static ActuatorSet actuatorSet = new ActuatorSet();
private static PrevailingDataSet prevailingDataSet = new PrevailingDataSet();
private static ErrorSet errorSet = new ErrorSet();
private static MasterSet masterSet = new MasterSet();
//-----------------------------------------------------------------------------------------------------------------------
    public void computeDerivatives(double t, double[] x, double[] dxdt) {
    	//-------------------------------------------------------------------------------------------------------------------
    	//
    	//
    	//-------------------------------------------------------------------------------------------------------------------
    	prevailingDataSet.setxIS(x);
    	prevailingDataSet.settIS(t);
    	prevailingDataSet.setGlobalTime(integratorData.getGlobalTime()+t);
    	prevailingDataSet.setValDt(val_dt);
    	prevailingDataSet.setR_ECEF_spherical(r_ECEF_spherical);
    	prevailingDataSet.setR_ECEF_cartesian(r_ECEF_cartesian);
    	prevailingDataSet.setV_NED_ECEF_spherical(V_NED_ECEF_spherical);
    	prevailingDataSet.setEulerAngle(eulerAngle);
    	coordinateTransformation.initializeTranformationMatrices(x, t, constants.getOmega(), atmosphereSet, aerodynamicSet, 
															 q_B2IN, r_ECEF_spherical, V_NED_ECEF_spherical);
		prevailingDataSet.setCoordinateTransformation(coordinateTransformation);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 										Delta-v integration
    	//-------------------------------------------------------------------------------------------------------------------
		double g0 = 9.81 ;  // Gravitational acceleration (Earth) for ISP equations only! 
    	acc_deltav = acc_deltav + actuatorSet.getPrimaryISP_is()*g0*Math.log(mminus/x[6]);
    	mminus=x[6];
    	//-------------------------------------------------------------------------------------------------------------------
    	// 										  Ground track 
    	//-------------------------------------------------------------------------------------------------------------------
    	double r=constants.getRm();  // <-- reference radius for projection. Current projection set for mean radius 
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
    	// 									    		 Force Model 
    	//-------------------------------------------------------------------------------------------------------------------
    	masterSet = ForceModel.FORCE_MANAGER(forceMomentumSet, gravitySet, atmosphereSet, aerodynamicSet,actuatorSet, 
    							 controlCommandSet, spaceShip, prevailingDataSet, integratorData, errorSet, false);
    	forceMomentumSet 	= masterSet.getForceMomentumSet();
    	gravitySet 			= masterSet.getGravitySet();
    	atmosphereSet 		= masterSet.getAtmosphereSet();
    	aerodynamicSet 		= masterSet.getAerodynamicSet();
    	actuatorSet 		= masterSet.getActuatorSet();
    	spaceShip 			= masterSet.getSpaceShip();
    //	System.out.println(spaceShip.getMass());
    //	controlCommandSet = masterSet.getControlCommandSet();
    	if(prevailingDataSet.getxIS()[6]!=spaceShip.getMass()) {
    		x[6]= spaceShip.getMass();
    	}
    	//-------------------------------------------------------------------------------------------------------------------
    	/**
    	 * 
    	 *  									     Equations of Motion
    	 */
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
	    dxdt[3] = -GravityModel.getGravity2D(prevailingDataSet)[0] * sin( x[4] ) + GravityModel.getGravity2D(prevailingDataSet)[1] * cos( x[5] ) * cos( x[4] ) + forceMomentumSet.getF_Aero_A()[0][0] / x[6] + constants.getOmega() * constants.getOmega() * x[2] * cos( x[1] ) * ( sin( x[4] ) * cos( x[1] ) - cos( x[1] ) * cos( x[5] ) * sin( x[1] ) ) ;
	    	// flight path angle 
	    dxdt[4] = ( x[3] / x[2] - GravityModel.getGravity2D(prevailingDataSet)[0]/ x[3] ) * cos( x[4] ) - GravityModel.getGravity2D(prevailingDataSet)[1] * cos( x[5] ) * sin( x[4] ) / x[3] - forceMomentumSet.getF_Aero_A()[2][0] / ( x[3] * x[6] ) + 2 * constants.getOmega() * sin( x[5] ) * cos( x[1] ) + constants.getOmega() * constants.getOmega() * x[2] * cos( x[1] ) * ( cos( x[4] ) * cos( x[1] ) + sin( x[4] ) * cos( x[5] ) * sin( x[1] ) ) / x[3] ;
	    	// local azimuth
	    dxdt[5] = x[3] * sin( x[5] ) * tan( x[1] ) * cos( x[4] ) / x[2] - GravityModel.getGravity2D(prevailingDataSet)[1] * sin( x[5] ) / x[3] + forceMomentumSet.getF_Aero_A()[1][0] / ( x[3] - cos( x[4] ) * x[6] ) - 2 * constants.getOmega() * ( tan( x[4] ) * cos( x[5] ) * cos( x[1] ) - sin( x[1] ) ) + constants.getOmega() * constants.getOmega() * x[2] * sin( x[5] ) * sin( x[1] ) * cos( x[1] ) / ( x[3] * cos( x[4] ) ) ;

	    	V_NED_ECEF_spherical[0]=x[3];
	    	V_NED_ECEF_spherical[1]=x[4];
	    	V_NED_ECEF_spherical[2]=x[5];
	    	
	    	V_NED_ECEF_cartesian = Mathbox.Spherical2Cartesian_Velocity(V_NED_ECEF_spherical);
    	
	    } else {
	    		// Derived from Titterton, Strapdown Navigation: 								
	    		// u - North
	    		dxdt[3] = forceMomentumSet.getF_total_NED()[0][0]/x[6] + gravitySet.getG_NED()[0][0] - 2 * constants.getOmega() * x[4]   * Math.sin(x[1])   						  + (x[3]*x[5] - x[4]*x[4]*Math.tan(x[1]))/x[2] 	     - constants.getOmega()*constants.getOmega()*x[2]/2*Math.sin(2*x[1])       ;
	    		// v - East
	    		dxdt[4] = forceMomentumSet.getF_total_NED()[1][0]/x[6] + gravitySet.getG_NED()[1][0] + 2 * constants.getOmega() * ( x[3] * Math.sin(x[1]) + x[5] * Math.cos(x[1]))   + x[4]/x[2] * (x[5] + x[3] * Math.tan(x[1])) 											     ;
	    		// w - Down
	    		dxdt[5] = forceMomentumSet.getF_total_NED()[2][0]/x[6] + gravitySet.getG_NED()[2][0] - 2 * constants.getOmega() * x[4]   * Math.cos(x[1])   						  - (x[4]*x[4] + x[3]*x[3])/x[2]   					 - constants.getOmega()*constants.getOmega()*x[2]/2*(1 + Math.cos(2*x[1])) ;
	    		//System.out.println(forceMomentumSet.getF_total_NED()[0][0]);
	    		//System.out.println(gravitySet.getG_NED()[2][0]);
		    	V_NED_ECEF_cartesian[0]=x[3];
		    	V_NED_ECEF_cartesian[1]=x[4];
		    	V_NED_ECEF_cartesian[2]=x[5];
		    	
		    	V_NED_ECEF_spherical = Mathbox.Cartesian2Spherical_Velocity(V_NED_ECEF_cartesian);
    	
	    }	    
	    /**
	     * 
	     * 				System mass properties 
	     * 
	     */
	    // Overall system mass 
	    dxdt[6]  = - forceMomentumSet.getThrustTotal()/(actuatorSet.getPrimaryISP_is()*g0)
	    			   - forceMomentumSet.getRCSThrustX()/(actuatorSet.getRCS_X_ISP()*g0) 
	    			   - forceMomentumSet.getRCSThrustY()/(actuatorSet.getRCS_Y_ISP()*g0)
	    			   - forceMomentumSet.getRCSThrustZ()/(actuatorSet.getRCS_Z_ISP()*g0); 
	    			   
	    // Main propulsion repository
	    			   
	    dxdt[14] = - forceMomentumSet.getThrustTotal()/(actuatorSet.getPrimaryISP_is()*g0) ; 
	    
	    // RCS and AUX repository 
	    
	    dxdt[15] = - forceMomentumSet.getRCSThrustX()/(actuatorSet.getRCS_X_ISP()*g0) 
	    			   - forceMomentumSet.getRCSThrustY()/(actuatorSet.getRCS_Y_ISP()*g0)
	    		       - forceMomentumSet.getRCSThrustZ()/(actuatorSet.getRCS_Z_ISP()*g0); 
	    
	    spaceShip.setMass(x[6]);
	    spaceShip.getPropulsion().setPrimaryPropellantFillingLevel(x[14]);
	    spaceShip.getPropulsion().setSecondaryPropellantFillingLevel(x[15]);

    	//-------------------------------------------------------------------------------------------------------------------
    	// 						   Rotational motion / Attitude equations
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
	    		RotElements rotE = new RotElements();
	    		rotE.Set_AngularVelocityEquationElements(x, InertiaTensor);
	    		//----------------------------------------------------------------------------------------
	    		// Quaternions:
	    		if(FlatEarther) {		// Disregard Earth's rotation and the rotation of the NED System 
		    		double[][] Q = {{ 0    , x[13],-x[12], x[11]}, 
		    				        {-x[13], 0    , x[11], x[12]},
		    				        { x[12],-x[11], 0    , x[13]},
		    				        {-x[11],-x[12],-x[13], 0    }}; 
		    		
		    	    q_B2IN.w = x[7];
		    	    q_B2IN.x = x[8];
		    	    q_B2IN.y = x[9];
		    	    q_B2IN.z = x[10];
		    	    
		    		double[][] q_vector_dot =  Mathbox.Multiply_Scalar_Matrix(0.5, Mathbox.Multiply_MQuat(Q, q_B2IN)); 
		    		dxdt[7] =  q_vector_dot[0][0];  // e1 dot
		    		dxdt[8] =  q_vector_dot[1][0];  // e2 dot 
		    		dxdt[9] =  q_vector_dot[2][0];  // e3 dot
		    		dxdt[10] = q_vector_dot[3][0];  // e4 dot

	    		} else {
		    		double[][] ElementMatrix = {{  -q_B2IN.x , -q_B2IN.y  , -q_B2IN.z  }, 
		    				         			{   q_B2IN.w , -q_B2IN.z  ,  q_B2IN.y  },
		    				         			{   q_B2IN.z ,  q_B2IN.w  , -q_B2IN.x  },
		    				         			{  -q_B2IN.y ,  q_B2IN.x  ,  q_B2IN.w  }};
		    		
		    		double[][] PQR = {{x[11]},
		    						  {x[12]},
		    						  {x[13]}};
		    		
		    		double[][] Omega_NED = {{ 1/r_ECEF_spherical[2] * V_NED_ECEF_cartesian[1] },
		    								{-1/r_ECEF_spherical[2] * V_NED_ECEF_cartesian[0] },
		    								{-1/r_ECEF_spherical[2] * V_NED_ECEF_cartesian[1] * Math.tan(r_ECEF_spherical[1])}};
		    		
		    		double[][] q_vector_dot = {{1},{0},{0},{0}};
		    		double[][] OMEGA_ECEF = {{constants.getOmega()*Math.cos(r_ECEF_spherical[1])},
		    							  	 {0},
		    							  	 {-constants.getOmega()*Math.sin(r_ECEF_spherical[1])}};
		    		
		    		double[][] Element_10 =  Mathbox.Multiply_Scalar_Matrix(0.5, ElementMatrix);
		    		// Rotation of the NED Frame  
		    		double[][] Element_21 =  Mathbox.Multiply_Matrices(coordinateTransformation.getC_NED2B(), Omega_NED);
		    		// Rotation of the ECEF Frame 
		    		double[][] Element_20 =  Mathbox.Substract_Matrices(Mathbox.Substract_Matrices(PQR, Element_21), OMEGA_ECEF);;
		    			    		
		    		q_vector_dot = Mathbox.Multiply_Matrices(Element_10, Element_20);

		    		dxdt[7]  =  q_vector_dot[0][0];  // e1 dot
		    		dxdt[8]  =  q_vector_dot[1][0];  // e2 dot 
		    		dxdt[9]  =  q_vector_dot[2][0];  // e3 dot
		    		dxdt[10] =  q_vector_dot[3][0];  // e4 dot
		    		//-----------------------------------------
		    		// Postprocessing Euler and Quarternions 
		    	    q_B2IN.w = x[7];
		    	    q_B2IN.x = x[8];
		    	    q_B2IN.y = x[9];
		    	    q_B2IN.z = x[10];
	    		}
	    	    //----------------------------------------------------------------------------------------
	    	    // System.out.println("model 1 running");
	    	    double Lb = forceMomentumSet.getM_total_NED()[0][0];
	    	    double Mb = forceMomentumSet.getM_total_NED()[1][0];
	    	    double Nb = forceMomentumSet.getM_total_NED()[2][0];
	    	    
	    	    dxdt[11] = rotE.EE_P_pp * x[11]*x[11] + rotE.EE_P_pq * x[11]*x[12] + rotE.EE_P_pr * x[11]*x[13] + rotE.EE_P_qq *x[12]*x[12] + rotE.EE_P_qr * x[12]*x[13] + rotE.EE_P_rr * x[13]*x[13] + rotE.EE_P_x*Lb + rotE.EE_P_y*Mb + rotE.EE_P_z*Nb;
	    	    dxdt[12] = rotE.EE_Q_pp * x[11]*x[11] + rotE.EE_Q_pq * x[11]*x[12] + rotE.EE_Q_pr * x[11]*x[13] + rotE.EE_Q_qq *x[12]*x[12] + rotE.EE_Q_qr * x[12]*x[13] + rotE.EE_Q_rr * x[13]*x[13] + rotE.EE_Q_x*Lb + rotE.EE_Q_y*Mb + rotE.EE_Q_z*Nb;
	    	    dxdt[13] = rotE.EE_R_pp * x[11]*x[11] + rotE.EE_R_pq * x[11]*x[12] + rotE.EE_R_pr * x[11]*x[13] + rotE.EE_R_qq *x[12]*x[12] + rotE.EE_R_qr * x[12]*x[13] + rotE.EE_R_rr * x[13]*x[13] + rotE.EE_R_x*Lb + rotE.EE_R_y*Mb + rotE.EE_R_z*Nb;
	    	
	    	}
	    else  if (SixDoF_Option == 2) {
	    		// Quaternions:
	
	    		double[][] Q = {{ 0    , x[13],-x[12], x[11]}, 
	    				        {-x[13], 0    , x[11], x[12]},
	    				        { x[12],-x[11], 0    , x[13]},
	    				        {-x[11],-x[12],-x[13], 0    }}; 
	    		
	    	    q_B2IN.w = x[7];
	    	    q_B2IN.x = x[8];
	    	    q_B2IN.y = x[9];
	    	    q_B2IN.z = x[10];
	    		double[][] q_vector_dot =  Mathbox.Multiply_Scalar_Matrix(0.5, Mathbox.Multiply_MQuat(Q, q_B2IN)); 
	    		dxdt[7] =  q_vector_dot[0][0];  // e1 dot
	    		dxdt[8] =  q_vector_dot[1][0];  // e2 dot 
	    		dxdt[9] =  q_vector_dot[2][0];  // e3 dot
	    		dxdt[10] = q_vector_dot[3][0];  // e4 dot

	    	    q_B2IN.w = x[7];
	    	    q_B2IN.x = x[8];
	    	    q_B2IN.y = x[9];
	    	    q_B2IN.z = x[10];
	    	    //----------------------------------------------------------------------------------------
	    	    double Lb = forceMomentumSet.getM_total_NED()[0][0];
	    	    double Mb = forceMomentumSet.getM_total_NED()[1][0];
	    	    double Nb = forceMomentumSet.getM_total_NED()[2][0];
	    		
				double Ixx = InertiaTensor[0][0];
				double Iyy = InertiaTensor[1][1];
				double Izz = InertiaTensor[2][2];
				// double Ixy = InertiaTensor[0][1];
				double Ixz = InertiaTensor[0][2];
				// double Iyx = InertiaTensor[][];
				// double Iyz = InertiaTensor[2][1];
				// System.out.println(Ixx+" | "+x[7]);
	    		// Angular Rates
			// p dot:
	    		dxdt[11] = (Izz * Lb + Ixz * Nb - (Ixz * (Iyy - Ixx - Izz) * x[11] + (Ixz*Ixz + Izz * (Izz - Iyy)) 
	    					* x[12]) * x[11]) / (Ixx * Izz - Ixz*Ixz); 
	    		// q dot:
	    		dxdt[12] = (Mb - (Ixx - Izz) * x[11] * x[13] - Ixz * (x[11]*x[11] - x[13]*x[13])) / Iyy; 
	    		// r dot:
	    		dxdt[13] = (Ixz * Lb + Ixx * Nb + (Ixz * (Iyy - Ixx - Izz) * x[13] + (Ixz*Ixz + Ixx * (Ixx - Iyy)) 
	    					* x[11]) * x[12]) / (Ixx * Izz - Ixz*Ixz); 

} 
	AngularMomentum_B[0][0] = forceMomentumSet.getM_total_NED()[0][0];
	AngularMomentum_B[1][0] = forceMomentumSet.getM_total_NED()[1][0] ;
	AngularMomentum_B[2][0] = forceMomentumSet.getM_total_NED()[2][0];	
	AngularRate[0][0] = x[11];
	AngularRate[1][0] = x[12];
	AngularRate[2][0] = x[13];
	}
	    eulerAngle = Mathbox.quaternion2RollPitchYaw(q_B2IN);
	    prevailingDataSet.setEulerAngle(eulerAngle);

}
//**********************************************************************************************
/*
    
  							Launch Integration Module  
    
*/    
//**********************************************************************************************
public static RealTimeContainer launchIntegrator( IntegratorData integratorData, 
	    										 	  SpaceShip spaceShip ,
	    										 	  ControlCommandSet controlCommandSet){
//----------------------------------------------------------------------------------------------
//				Prepare integration 
//----------------------------------------------------------------------------------------------
// Load and set constants 
constants.initConstants(integratorData.getTargetBody());
prevailingDataSet.setRM(constants.getRm());
prevailingDataSet.setLt(constants.getLt());
prevailingDataSet.setMu(constants.getMu());
//----------------------------------------------------------------------------------------------
//
//Initialise Simulation: 
//
//- Read propulsion setting:	Propulsion/Controller INIT
//- Initialise ground track computation

SimulationCore.spaceShip = spaceShip;
//tankContent = spaceShip.getPropulsion().getPrimaryPropellant();
try {
	SimulationCore.controlCommandSet = (ControlCommandSet) controlCommandSet.clone();
} catch (CloneNotSupportedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

SimulationCore.integratorData = integratorData;
coordinateTransformation =  new CoordinateTransformation();
gravitySet = new GravitySet();
forceMomentumSet = new ForceMomentumSet();
//----------------------------------------------------------------------------------------------	
if(integratorData.getVelocityVectorCoordSystem()==1) {
spherical = true;
} else if (integratorData.getVelocityVectorCoordSystem()==2) {
spherical = false;
}

InertiaTensor = spaceShip.getInertiaTensorMatrix();

q_B2IN      = integratorData.getInitialQuaternion();


actuatorSet.setPrimaryISP_is(spaceShip.getPropulsion().getPrimaryISPMax());
 mminus	  	  = spaceShip.getMass()   ;
 vminus		  = integratorData.getInitVelocity()  ;

phimin=integratorData.getInitLongitude();
tetamin=integratorData.getInitLatitude();
groundtrack=integratorData.getGroundtrack();
ref_ELEVATION =  integratorData.getRefElevation();
//----------------------------------------------------------------------------------------------
//Sequence Setup	
//----------------------------------------------------------------------------------------------
prevailingDataSet.setLocalElevation(integratorData.getRefElevation());
prevailingDataSet.setTARGET(integratorData.getTargetBody());
//----------------------------------------------------------------------------------------------
//Integrator setup	
//----------------------------------------------------------------------------------------------
FirstOrderIntegrator IntegratorModule = integratorData.getIntegrator();
//----------------------------------------------------------------------------------------------
FirstOrderDifferentialEquations ode = new SimulationCore();
//------------------------------
try {
AtmosphereModel.INITIALIZE_ATM_DATA(integratorData.getTargetBody());
} catch (URISyntaxException e1) {
// TODO Auto-generated catch block
e1.printStackTrace();
}

//----------------------------------------------------------------------------------------------
// 						Result vector setup - DO NOT TOUCH
int dimension = 16;

double[] y = new double[dimension]; // Result vector

 // double[] y = new double[13]; // Result vector
	V_NED_ECEF_spherical[0]= integratorData.getInitVelocity();
	V_NED_ECEF_spherical[1]= integratorData.getInitFpa();
	V_NED_ECEF_spherical[2]= integratorData.getInitAzimuth();
// Position 
  y[0] = integratorData.getInitLongitude();
  y[1] = integratorData.getInitLatitude();
  y[2] = integratorData.getInitRadius();
  r_ECEF_spherical[0] = y[0];
  r_ECEF_spherical[1] = y[1];
  r_ECEF_spherical[2] = y[2];
  r_ECEF_cartesian = Mathbox.Spherical2Cartesian_Position(r_ECEF_spherical);
// Velocity
	        if(spherical) {
	        	
	        y[3] = integratorData.getInitVelocity();
	        y[4] = integratorData.getInitFpa();
	        y[5] = integratorData.getInitAzimuth();
    	V_NED_ECEF_spherical[0]=integratorData.getInitVelocity();
    	V_NED_ECEF_spherical[1]=integratorData.getInitFpa();
    	V_NED_ECEF_spherical[2]=integratorData.getInitAzimuth();
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
	// Attitude and Rotational Motion
	y[7]  = integratorData.getInitialQuaternion().w;
	y[8]  = integratorData.getInitialQuaternion().x;
	y[9]  = integratorData.getInitialQuaternion().y;
	y[10] = integratorData.getInitialQuaternion().z;
	y[11] = integratorData.getInitRotationalRateX();
	y[12] = integratorData.getInitRotationalRateY();
	y[13] = integratorData.getInitRotationalRateZ();
	
	y[14] = spaceShip.getPropulsion().getPrimaryPropellantFillingLevel();
	y[15] = spaceShip.getPropulsion().getSecondaryPropellantFillingLevel();

prevailingDataSet.setxIS(y);
prevailingDataSet.settIS(0);
prevailingDataSet.setR_ECEF_spherical(r_ECEF_spherical);
prevailingDataSet.setR_ECEF_cartesian(r_ECEF_cartesian);
prevailingDataSet.setV_NED_ECEF_spherical(V_NED_ECEF_spherical);
//ControllerModel.initializeFlightController(spaceShip, prevailingDataSet, controlCommandSet);	
@SuppressWarnings("unused")
List<MasterSet> masterList = new ArrayList<MasterSet>();
@SuppressWarnings("unused")
List<RealTimeResultSet> realTimeList = new ArrayList<RealTimeResultSet>();
RealTimeContainer realTimeContainer = new RealTimeContainer();

//----------------------------------------------------------------------------------------------
  		
	        StepHandler WriteOut = new StepHandler() {
	        	  
	        	
	            public void init(double t0, double[] y0, double t) {
	         
	            }
	            
	            public void handleStep(StepInterpolator interpolator, boolean isLast) {
	               // double   t     = interpolator.getCurrentTime();
	                double[] ymo   = interpolator.getInterpolatedDerivatives();
	                double[] y     = interpolator.getInterpolatedState();
	                RealTimeResultSet realTimeResultSet = new RealTimeResultSet();
	                 val_dt = interpolator.getCurrentTime()-interpolator.getPreviousTime();
	                // double currentTime = integratorData.getGlobalTime() + prevailingDataSet.gettIS();
	         	    // RealTimeResultSet realTimeResultSet = new RealTimeResultSet();
	         	    // System.out.println(stepCount);
	                // currentGlobalTime = currentGlobalTime + val_dt;
	         		
	         	    // System.out.println(integratorData.getGlobalTime() + currentGlobalTime+"|"+val_dt);
	                	// realTimeResultSet.setTime(currentGlobalTime);
	                // realTimeResultSet.setGlobalTime(integratorData.getGlobalTime() + currentGlobalTime);
	                	realTimeResultSet.setGlobalTime(prevailingDataSet.getGlobalTime());
	                	realTimeResultSet.setLongitude(r_ECEF_spherical[0]);
	                	realTimeResultSet.setLatitude(r_ECEF_spherical[1]);
	                	realTimeResultSet.setRadius(r_ECEF_spherical[2]);
	                	realTimeResultSet.setAltitude((r_ECEF_spherical[2]-constants.getRm()-ref_ELEVATION));
	                	
	                	realTimeResultSet.setAzi( V_NED_ECEF_spherical[2]);
	                	realTimeResultSet.setFpa( V_NED_ECEF_spherical[1]);
	                	realTimeResultSet.setVelocity( V_NED_ECEF_spherical[0]);
	                	
	                	realTimeResultSet.setCurrentDataSet(prevailingDataSet);
	                	masterSet.getSpaceShip().setMass(y[6]);
	                	realTimeResultSet.setSCMass(y[6]);
	                	if(spherical) {
	                	realTimeResultSet.setNormalizedDeceleration(Math.abs(ymo[3])/9.80665);
	                	} else {
	                		double decel = Math.sqrt(ymo[3]*ymo[3] + ymo[4]*ymo[4] + ymo[5]*ymo[5]);
	                		realTimeResultSet.setNormalizedDeceleration(decel/9.80665);
	                	}	
	                	realTimeResultSet.setPQR(AngularRate);
	                	realTimeResultSet.setEulerAngle(eulerAngle);
	                	realTimeResultSet.setQuaternion(q_B2IN);
	                	realTimeResultSet.setCartesianPosECEF(r_ECEF_cartesian);
	                	realTimeResultSet.setThrust_NED(F_total_NED);
	                	realTimeResultSet.setGroundtrack(groundtrack);
	                	integratorData.setGroundtrack(groundtrack);
	                //if(masterSet.getActuatorSet().getPrimaryThrust_is()>10 && masterSet.getActuatorSet().getPrimaryThrust_is()<6000){	System.out.println(masterSet.getActuatorSet().getPrimaryThrust_is()); }
	                	SimulationCore.spaceShip.getPropulsion().setMassFlowPrimary(Math.abs(ymo[14]));
	                	try {
	                	realTimeResultSet.setMasterSet((MasterSet) masterSet.clone());
	                	} catch (Exception exp) {
	                		
	                	}
	                	realTimeResultSet.setIntegratorData(integratorData);
	                	//RealTimeSimulationCore.spaceShip.getPropulsion().setMassFlowPrimary(Math.abs(ymo[14]));
	                //	realTimeResultSet.setSpaceShip(masterSet.getSpaceShip());
	                	try {
	                masterList.add((MasterSet) masterSet.clone());
} 					catch (Exception exp) {
	                		
	                	}
	                realTimeList.add(realTimeResultSet);
	                
	                
	                if(isLast) {                	
		                	realTimeContainer.setRealTimeResultSet(realTimeResultSet);
		                	realTimeContainer.setRealTimeList(realTimeList);
		                //	currentGlobalTime=0;
	                }
	            }
	            
	        };

	        IntegratorModule.addStepHandler(WriteOut);

	        try {
	        	double t= integratorData.getMaxIntegTime();  // integration interval 
	        	//double t=  1;
	        			IntegratorModule.integrate(ode, 0.0, y, t, y);
	        } catch(NoBracketingException eNBE) {
	        		System.out.println("ERROR: Integrator failed:");
	        }
	        /**
	         * 
	         * 
	         * Return full result set 
	         */
				return realTimeContainer;
			//---------------------------------------
		}
	    
	    
public static double getRef_ELEVATION() {
	return ref_ELEVATION;
}

public int getDimension() {
	// Set dimension for equations of motion matrix array 
	// 6DoF model plus additional equations for change of mass 
		return 16; // 6 DOF model 
}

public static double getRm() {
	return constants.getRm();
}

}