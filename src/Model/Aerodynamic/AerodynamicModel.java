package Model.Aerodynamic;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import Model.AtmosphereModel;
import Model.AtmosphereSet;
import Simulator_main.Simulation;

public class AerodynamicModel {
		
	public static AerodynamicSet getAerodynamicSet(AtmosphereSet atmosphereSet) {
		double Lt = Simulation.getLt();
		AerodynamicSet aerodynamicSet = new AerodynamicSet();
		aerodynamicSet.setCdC(AtmosphereModel.get_CdC(Simulation.getR_ECEF_spherical()[2],0));                           							// Continuum flow drag coefficient [-]
		aerodynamicSet.setDragCoefficient(AtmosphereModel.load_Drag(Simulation.getV_NED_ECEF_spherical()[0], 
				Simulation.getR_ECEF_spherical()[2], atmosphereSet.getStaticPressure(), atmosphereSet.getStaticTemperature(), 
        aerodynamicSet.getCdC(), Lt, atmosphereSet.getGasConstant())); 		    	// Lift coefficient                [-]
		aerodynamicSet.setFlowzone(AtmosphereModel.calc_flowzone(Simulation.getV_NED_ECEF_spherical()[0], 
				Simulation.getR_ECEF_spherical()[2], atmosphereSet.getStaticPressure(), atmosphereSet.getStaticTemperature(), Lt)); 
	 	//-----------------------------------------------------------------------------------------------
		aerodynamicSet.setDragForce(atmosphereSet.getDynamicPressure() * Simulation.getSpaceShip().getAeroElements().getSurfaceArea() * aerodynamicSet.getDragCoefficient());  		// Aerodynamic drag Force 		   [N]
		aerodynamicSet.setLiftForce(atmosphereSet.getDynamicPressure() * Simulation.getSpaceShip().getAeroElements().getSurfaceArea() * aerodynamicSet.getLiftCoefficient() * cos( aerodynamicSet.getBankAngle() ) );               // Aerodynamic lift Force 		   [N]
		aerodynamicSet.setSideForce(atmosphereSet.getDynamicPressure() * Simulation.getSpaceShip().getAeroElements().getSurfaceArea() * aerodynamicSet.getC_SF() * sin( aerodynamicSet.getBankAngle() )); 	                  // Aerodynamic side Force 		   [N]
		//----------------------------------------------------------------------------------------------
		return aerodynamicSet;
	}

}
