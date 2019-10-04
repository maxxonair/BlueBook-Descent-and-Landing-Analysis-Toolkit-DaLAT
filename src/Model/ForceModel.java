package Model;

import Sequence.Sequence;
import Simulator_main.Simulation;
import Toolbox.Mathbox;

public class ForceModel {
	
	
	public static void FORCE_MANAGER(ForceMomentumSet forceMomentumSet, GravitySet gravitySet, AtmosphereSet atmosphereSet, AerodynamicSet aerodynamicSet) {
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
    	//-------------------------------------------------------------------------------------------------------------------
    	//								    	Gravitational environment
    	//-------------------------------------------------------------------------------------------------------------------  
		  
		  gravitySet.setG_NED(Mathbox.Multiply_Matrices(Simulation.getCoordinateTransformation().getC_ECEF2NED(), 
        				Gravity.getGravity3D(Simulation.getxIS(), Simulation.getR_ECEF_cartesian())));	
		  
        		// Gravitational Force (wrt NED System)
	    	    	F_Gravity_NED[0][0] = Simulation.getxIS()[6]*gravitySet.getG_NED()[0][0];
	    	    	F_Gravity_NED[1][0] = Simulation.getxIS()[6]*gravitySet.getG_NED()[1][0];
	    	    	F_Gravity_NED[2][0] = Simulation.getxIS()[6]*gravitySet.getG_NED()[2][0];
	    	    	
	    	    	forceMomentumSet.setF_Gravity_NED(F_Gravity_NED);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Atmosphere
    	//-------------------------------------------------------------------------------------------------------------------
    	AtmosphereModel.ATMOSPHERE_MANAGER(Simulation.getxIS(), Simulation.getRm(), Simulation.getTARGET(), 
    			Simulation.getLt(), atmosphereSet, Simulation.getSpaceShip(), Simulation.getV_NED_ECEF_spherical());
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Aerodynamic
    	//-------------------------------------------------------------------------------------------------------------------  
    	
    	AerodynamicModel.AERODYNAMIC_MANAGER(aerodynamicSet, atmosphereSet);
    	
    	// 					    Force Definition - Aerodynamic Forces | Aerodynamic Frame |
    	
	   	F_Aero_A[0][0] = -  aerodynamicSet.getDragForce()  ;
	   	F_Aero_A[1][0] =    aerodynamicSet.getSideForce()  ;
	   	F_Aero_A[2][0] = -  aerodynamicSet.getLiftForce()  ;
	   	
	   	forceMomentumSet.setF_Aero_A(F_Aero_A);
    	//-------------------------------------------------------------------------------------------------------------------
    	//					SpaceShip Force Management  - 	Sequence management and Flight controller 
    	//-------------------------------------------------------------------------------------------------------------------
	   	
    Sequence.sequenceManager(Simulation.gettIS(),  Simulation.getxIS(), Simulation.getV_NED_ECEF_spherical() , 
    							 Simulation.getR_ECEF_spherical() );
    
    forceMomentumSet.setThrustTotal(Sequence.getControlElements().getPrimaryThrust_is());
    
    	//-------------------------------------------------------------------------------------------------------------------
    	// 					    Force Definition - Thrust Forces | Body fixed Frame |
    	//-------------------------------------------------------------------------------------------------------------------
    
	   	F_Thrust_B[0][0] =  forceMomentumSet.getThrustTotal() * Math.cos(Simulation.getTVC_alpha())*Math.cos(Simulation.getTVC_beta());  
	   	F_Thrust_B[1][0] =  forceMomentumSet.getThrustTotal() * Math.cos(Simulation.getTVC_alpha())*Math.sin(Simulation.getTVC_beta());   
	   	F_Thrust_B[2][0] =  forceMomentumSet.getThrustTotal() * Math.sin(Simulation.getTVC_alpha());  
	   	
	   	forceMomentumSet.setF_Thrust_B(F_Thrust_B);
    	//-------------------------------------------------------------------------------------------------------------------
    	// 									           Set up force vector in NED  
    	//------------------------------------------------------------------------------------------------------------------- 
    	F_Aero_NED   	= Mathbox.Multiply_Matrices(Simulation.getCoordinateTransformation().getC_A2NED(), F_Aero_A) ; 
    	forceMomentumSet.setF_Aero_NED(F_Aero_NED);
    	
    	F_Thrust_NED 	= Mathbox.Multiply_Matrices(Simulation.getCoordinateTransformation().getC_B2NED(), F_Thrust_B) ;
    	forceMomentumSet.setF_Thrust_NED(F_Thrust_NED);
    	
    	F_total_NED   	= Mathbox.Addup_Matrices(F_Aero_NED , F_Thrust_NED );
    	forceMomentumSet.setF_total_NED(F_total_NED);
    	//-------------------------------------------------------------------------------------------------------------------
	}

}
