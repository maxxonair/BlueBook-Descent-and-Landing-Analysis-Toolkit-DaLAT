package Model;

import FlightElement.SpaceShip;
import Model.Aerodynamic.AerodynamicModel;
import Model.Aerodynamic.AerodynamicSet;
import Sequence.Sequence;
import Simulator_main.CurrentDataSet;
import Simulator_main.IntegratorData;
import Toolbox.Mathbox;

public class ForceModel {
	
	
	public static MasterSet FORCE_MANAGER(ForceMomentumSet forceMomentumSet, GravitySet gravitySet, AtmosphereSet atmosphereSet, AerodynamicSet aerodynamicSet, 
									 ActuatorSet actuatorSet, ControlCommandSet controlCommandSet, SpaceShip spaceShip, CurrentDataSet currentDataSet, IntegratorData integratorData) {
		  double[][] F_Aero_A      = {{0},{0},{0}};						// Aerodynamic Force with respect to Aerodynamic frame [N]
		  double[][] F_Aero_NED    = {{0},{0},{0}};						// Aerodynamic Force with respect to NED frame 		   [N]
		  double[][] F_Thrust_B    = {{0},{0},{0}};						// Thrust Force in body fixed system     			   [N]
		  double[][] F_Thrust_NED  = {{0},{0},{0}};						// Thrust Force in NED frame    		     			   [N]
		  //double[][] F_Gravity_G   = {{0},{0},{0}};					// Gravity Force in ECEF coordinates     			   [N]
		  double[][] F_Gravity_NED = {{0},{0},{0}};						// Gravity Force in NED Frame            			   [N]
		  double[][] F_total_NED   = {{0},{0},{0}};						// Total force vector in NED coordinates 			   [N]
		
		  //double[][] M_Aero_NED      = {{0},{0},{0}};
		  //double[][] M_Thrust_NED    = {{0},{0},{0}};
		
		  //double[][] M_Aero_A      = {{0},{0},{0}};
		  //double[][] M_Aero_B      = {{0},{0},{0}};
		  //double[][] M_Thrust_B    = {{0},{0},{0}};
		  
		  MasterSet masterSet = new MasterSet();
    	//-------------------------------------------------------------------------------------------------------------------
    	//								    	Gravitational environment
    	//-------------------------------------------------------------------------------------------------------------------  
		  
		  gravitySet = GravityModel.getGravitySet(currentDataSet)	;
		  
        		// Gravitational Force (wrt NED System)
	    	    	F_Gravity_NED[0][0] = currentDataSet.getxIS()[6]*gravitySet.getG_NED()[0][0];
	    	    	F_Gravity_NED[1][0] = currentDataSet.getxIS()[6]*gravitySet.getG_NED()[1][0];
	    	    	F_Gravity_NED[2][0] = currentDataSet.getxIS()[6]*gravitySet.getG_NED()[2][0];
	    	    	
	    	    	forceMomentumSet.setF_Gravity_NED(F_Gravity_NED);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Atmosphere
    	//-------------------------------------------------------------------------------------------------------------------
	atmosphereSet = AtmosphereModel.getAtmosphereSet(spaceShip, currentDataSet);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Aerodynamic
    	//-------------------------------------------------------------------------------------------------------------------  
    	
    	aerodynamicSet = AerodynamicModel.getAerodynamicSet(atmosphereSet, spaceShip, currentDataSet, integratorData);
    	
    	// 					    Force Definition - Aerodynamic Forces | Aerodynamic Frame |
    	
	   	F_Aero_A[0][0] = -  aerodynamicSet.getDragForce()  ;
	   	F_Aero_A[1][0] =    aerodynamicSet.getSideForce()  ;
	   	F_Aero_A[2][0] = -  aerodynamicSet.getLiftForce()  ;
	   	
	   	forceMomentumSet.setF_Aero_A(F_Aero_A);
    	//-------------------------------------------------------------------------------------------------------------------
    	//					SpaceShip Force Management  - 	Sequence management and Flight controller 
    	//-------------------------------------------------------------------------------------------------------------------
	   	
    controlCommandSet = Sequence.getControlCommandSet(currentDataSet, spaceShip);
    
    actuatorSet = ActuatorModel.getActuatorSet(controlCommandSet, spaceShip, currentDataSet);
    
    forceMomentumSet.setThrustTotal(actuatorSet.getPrimaryThrust_is());
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    Force Definition - Thrust Forces | Body fixed Frame |
    	//-------------------------------------------------------------------------------------------------------------------
    
	   	F_Thrust_B[0][0] =  forceMomentumSet.getThrustTotal() * Math.cos(ActuatorSet.getTVC_alpha())*Math.cos(ActuatorSet.getTVC_beta());  
	   	F_Thrust_B[1][0] =  forceMomentumSet.getThrustTotal() * Math.cos(ActuatorSet.getTVC_alpha())*Math.sin(ActuatorSet.getTVC_beta());   
	   	F_Thrust_B[2][0] =  forceMomentumSet.getThrustTotal() * Math.sin(ActuatorSet.getTVC_alpha());  
	   	
	   	forceMomentumSet.setF_Thrust_B(F_Thrust_B);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Finalize Force Setup -> Transfrom vectors to NED  
    	//------------------------------------------------------------------------------------------------------------------- 
    	F_Aero_NED   	= Mathbox.Multiply_Matrices(currentDataSet.getCoordinateTransformation().getC_A2NED(), F_Aero_A) ; 
    	forceMomentumSet.setF_Aero_NED(F_Aero_NED);
    	
    	F_Thrust_NED 	= Mathbox.Multiply_Matrices(currentDataSet.getCoordinateTransformation().getC_B2NED(), F_Thrust_B) ;
    	forceMomentumSet.setF_Thrust_NED(F_Thrust_NED);
    	
    	F_total_NED   	= Mathbox.Addup_Matrices(F_Aero_NED , F_Thrust_NED );
    	forceMomentumSet.setF_total_NED(F_total_NED);
    	//-------------------------------------------------------------------------------------------------------------------
    	// Prepare ResultSet
    	//-------------------------------------------------------------------------------------------------------------------
    	masterSet.setActuatorSet(actuatorSet);
    	masterSet.setAerodynamicSet(aerodynamicSet);
    	masterSet.setAtmosphereSet(atmosphereSet);
    	masterSet.setForceMomentumSet(forceMomentumSet);
    	masterSet.setGravitySet(gravitySet);
    	masterSet.setControlCommandSet(controlCommandSet);
    	return masterSet;
	}

}
