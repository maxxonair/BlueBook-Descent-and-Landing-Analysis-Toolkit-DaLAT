package Simulation.Model.ForceTorque;

import FlightElement.SpaceShip;
import Simulation.Model.Aerodynamic.AerodynamicModel;
import Simulation.Model.Atmosphere.AtmosphereModel;
import Simulation.Model.DataSets.AerodynamicSet;
import Simulation.Model.DataSets.AtmosphereSet;
import Simulation.Model.DataSets.ErrorSet;
import Simulation.Model.DataSets.ForceMomentumSet;
import Simulation.Model.DataSets.GravitySet;
import Simulation.Model.DataSets.MasterSet;
import Simulation.Model.Gravity.GravityModel;
import Simulation.Noise.RandomWalker;
import Simulator_main.DataSets.IntegratorData;
import utils.Mathbox;

public class EnvironmentForceTorqueModel {
	
	private static double genRCSMomentumNoise=0;							// General, uniform (all axes) momentum noise by RCS torque [Nm]
	private static RandomWalker walker = new RandomWalker(5);
	
	public static MasterSet FORCE_MANAGER(ForceMomentumSet forceMomentumSet, GravitySet gravitySet, AtmosphereSet atmosphereSet, 
										  AerodynamicSet aerodynamicSet, SpaceShip spaceShip, 
										  IntegratorData integratorData, ErrorSet errorSet, boolean isAutoPilot) {
		  double[][] F_Aero_A      = {{0},{0},{0}};						// Aerodynamic Force with respect to Aerodynamic frame [N]
		  double[][] F_Aero_NED    = {{0},{0},{0}};						// Aerodynamic Force with respect to NED frame 		   [N]
		  double[][] F_Thrust_B    = {{0},{0},{0}};						// Thrust Force in body fixed system     			   [N]
		  double[][] F_Thrust_NED  = {{0},{0},{0}};						// Thrust Force in NED frame    		     			   [N]
		  //double[][] F_Gravity_G   = {{0},{0},{0}};					// Gravity Force in ECEF coordinates     			   [N]
		  double[][] F_Gravity_NED = {{0},{0},{0}};						// Gravity Force in NED Frame            			   [N]
		  double[][] F_total_NED   = {{0},{0},{0}};						// Total force vector in NED coordinates 			   [N]
		
		  //double[][] M_Aero_NED      = {{0},{0},{0}};
		  //double[][] M_Thrust_NED    = {{0},{0},{0}};
		  
		  double[][] M_total_B     = {{0},{0},{0}};
		
		  double[][] M_Aero_B      = {{0},{0},{0}};
		  //double[][] M_Aero_B      = {{0},{0},{0}};
		  double[][] M_Thrust_B    = {{0},{0},{0}};
		  
		  
		  MasterSet masterSet = new MasterSet();
    	//-------------------------------------------------------------------------------------------------------------------
    	//								    	Gravitational environment
    	//-------------------------------------------------------------------------------------------------------------------  
		  
		  gravitySet = GravityModel.getGravitySet(spaceShip)	;
		  
        		// Gravitational Force (wrt NED System)
	    	    	F_Gravity_NED[0][0] = spaceShip.getState().getxIS()[6]*gravitySet.getG_NED()[0][0];
	    	    	F_Gravity_NED[1][0] = spaceShip.getState().getxIS()[6]*gravitySet.getG_NED()[1][0];
	    	    	F_Gravity_NED[2][0] = spaceShip.getState().getxIS()[6]*gravitySet.getG_NED()[2][0];
	    	    	
	    forceMomentumSet.setF_Gravity_NED(F_Gravity_NED);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Atmosphere
    	//-------------------------------------------------------------------------------------------------------------------
	    atmosphereSet = AtmosphereModel.getAtmosphereSet(spaceShip, integratorData);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Aerodynamic
    	//-------------------------------------------------------------------------------------------------------------------  
    	
    	aerodynamicSet = AerodynamicModel.getAerodynamicSet(atmosphereSet, spaceShip, integratorData );
    	
    	// 					    Force Definition - Aerodynamic Forces | Aerodynamic Frame |

	   	F_Aero_A[0][0] = -  aerodynamicSet.getDragForce() - aerodynamicSet.getDragForceParachute() ;
	   	F_Aero_A[1][0] =    aerodynamicSet.getSideForce()  ;
	   	F_Aero_A[2][0] = -  aerodynamicSet.getLiftForce()  ;
	   	
	   	forceMomentumSet.setF_Aero_A(F_Aero_A);
	   	
	   	M_Aero_B[0][0] = aerodynamicSet.getMx(); 
	   	M_Aero_B[1][0] = aerodynamicSet.getMy();
	   	M_Aero_B[2][0] = aerodynamicSet.getMz();
	   	
	   	forceMomentumSet.setM_Aero_B(M_Aero_B);
    	//-------------------------------------------------------------------------------------------------------------------
    	//					SpaceShip Force Management  - 	Sequence management and Flight controller 
    	//-------------------------------------------------------------------------------------------------------------------
	    spaceShip.getForceTorqueModel().setActuatorSet( spaceShip.getForceTorqueModel().getActuatorModel().getActuatorSet(integratorData) );
	    
	    
	    //spaceShip = spaceShip.getForceTorqueModel().getActuatorSet().getSpaceShip();
        
	    forceMomentumSet.setThrustTotal(spaceShip.getForceTorqueModel().getActuatorSet().getPrimaryThrust_is());
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    Force/Torque Definition - Thrust Forces | Body fixed Frame |
    	//-------------------------------------------------------------------------------------------------------------------    
	    double tvcMomentum = forceMomentumSet.getThrustTotal() * Math.sin(spaceShip.getForceTorqueModel().getActuatorSet().getTVC_alpha()) * ( spaceShip.getProperties().getMassAndInertia().getCoM() - spaceShip.getProperties().getGeometry().getCoT() );

    	// Thrust Forces from Main Propulsion System:
    	F_Thrust_B[0][0] =  forceMomentumSet.getThrustTotal() * Math.cos(spaceShip.getForceTorqueModel().getActuatorSet().getTVC_alpha()) * Math.cos(spaceShip.getForceTorqueModel().getActuatorSet().getTVC_beta()) ;  
	   	F_Thrust_B[1][0] =  forceMomentumSet.getThrustTotal() * Math.cos(spaceShip.getForceTorqueModel().getActuatorSet().getTVC_alpha()) * Math.sin(spaceShip.getForceTorqueModel().getActuatorSet().getTVC_beta()) ;   
	   	F_Thrust_B[2][0] =  forceMomentumSet.getThrustTotal() * Math.sin(spaceShip.getForceTorqueModel().getActuatorSet().getTVC_alpha());  
	   	// Thrust Forces from RCS TBD:
    	F_Thrust_B[0][0] =  F_Thrust_B[0][0] ;  
	   	F_Thrust_B[1][0] =  F_Thrust_B[1][0] ;   
	   	F_Thrust_B[2][0] =  F_Thrust_B[2][0] ;     	
	   	
	   	// Torque from RCS:
	   	if(spaceShip.getForceTorqueModel().getActuatorSet().getMomentumRCS_X_is() != 0 || spaceShip.getForceTorqueModel().getActuatorSet().getMomentumRCS_Y_is() != 0 || spaceShip.getForceTorqueModel().getActuatorSet().getMomentumRCS_Z_is()  != 0) {
	   		// If a momentum for any axis is commanded => add momentum noise
	   		genRCSMomentumNoise = walker.randomWalker1D(genRCSMomentumNoise,0.01,-0.01, 0.0005, 0.01);
	   	}
	   	M_Thrust_B[0][0] =  spaceShip.getForceTorqueModel().getActuatorSet().getMomentumRCS_X_is() + genRCSMomentumNoise;
	    M_Thrust_B[1][0] =  spaceShip.getForceTorqueModel().getActuatorSet().getMomentumRCS_Y_is() + genRCSMomentumNoise;
	   	M_Thrust_B[2][0] =  spaceShip.getForceTorqueModel().getActuatorSet().getMomentumRCS_Z_is() + genRCSMomentumNoise;
	   	// Torque from TVC
	   	M_Thrust_B[0][0] =  M_Thrust_B[0][0] ;
	    M_Thrust_B[1][0] =  M_Thrust_B[1][0] + tvcMomentum;
	   	M_Thrust_B[2][0] =  M_Thrust_B[2][0] ;
	   	
	   	M_total_B = Mathbox.Addup_Matrices(M_Thrust_B , M_Aero_B);

	   	// Set RCS Thrust for propellant consumption
	   	// Number of thrusters to produce force free torque manoeuver:
	   	int nrThrusterTrq = 4; // [-] TBD 
	   	if(Math.abs(M_total_B[0][0])>0) {	   		
	   		forceMomentumSet.setRCSThrustX( nrThrusterTrq  * spaceShip.getProperties().getPropulsion().getSecondaryThrust_RCS_X()*Math.abs(spaceShip.getgNCModel().getControlCommandSet().getMomentumRCS_X_cmd()) );
	   	} else {
	   		forceMomentumSet.setRCSThrustX(0);
	   	}
	   	if(Math.abs(M_total_B[1][0])>0) {
	   		
	   		forceMomentumSet.setRCSThrustY( nrThrusterTrq  * spaceShip.getProperties().getPropulsion().getSecondaryThrust_RCS_Y()*Math.abs(spaceShip.getgNCModel().getControlCommandSet().getMomentumRCS_Y_cmd())  );
	   	} else {
	   		forceMomentumSet.setRCSThrustY(0);
	   	}
	   	if(Math.abs(M_total_B[2][0])>0) {
	   		forceMomentumSet.setRCSThrustZ( nrThrusterTrq  * spaceShip.getProperties().getPropulsion().getSecondaryThrust_RCS_Z()*Math.abs(spaceShip.getgNCModel().getControlCommandSet().getMomentumRCS_Z_cmd()));
	   	} else {
	   		forceMomentumSet.setRCSThrustZ(0);
	   	}
	   	//-------------------------------------------------------------------------------------------------------------------
	   	forceMomentumSet.setF_Thrust_B(F_Thrust_B);
	   	forceMomentumSet.setM_Thrust_B(M_Thrust_B);
	   	forceMomentumSet.setM_total_NED(M_total_B);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 				Finalize Force Setup -> Transform vectors to NED  
    	//------------------------------------------------------------------------------------------------------------------- 
    	F_Aero_NED   	= Mathbox.Multiply_Matrices(spaceShip.getState().getCoordinateTransformation().getC_A2NED(), F_Aero_A) ; 
    	forceMomentumSet.setF_Aero_NED(F_Aero_NED);
    	
    	F_Thrust_NED 	= Mathbox.Multiply_Matrices(spaceShip.getState().getCoordinateTransformation().getC_B2NED(), F_Thrust_B) ;
    	forceMomentumSet.setF_Thrust_NED(F_Thrust_NED);
    	
    	F_total_NED   	= Mathbox.Addup_Matrices(F_Aero_NED , F_Thrust_NED );
    	forceMomentumSet.setF_total_NED(F_total_NED);  	
    	//-------------------------------------------------------------------------------------------------------------------
    	// Prepare ResultSet
    	//-------------------------------------------------------------------------------------------------------------------
    	masterSet.setActuatorSet(spaceShip.getForceTorqueModel().getActuatorSet());
    	masterSet.setAerodynamicSet(aerodynamicSet);
    	masterSet.setAtmosphereSet(atmosphereSet);
    	masterSet.setForceMomentumSet(forceMomentumSet);
    	masterSet.setGravitySet(gravitySet);
    	masterSet.setSpaceShip(spaceShip);
    	return masterSet;
	}

}
