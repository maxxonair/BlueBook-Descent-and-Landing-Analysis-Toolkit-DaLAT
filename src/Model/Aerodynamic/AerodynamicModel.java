package Model.Aerodynamic;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import FlightElement.SpaceShip;
import Model.AtmosphereModel;
import Model.AtmosphereSet;
import Simulator_main.CurrentDataSet;

public class AerodynamicModel {
		
	public static AerodynamicSet getAerodynamicSet(AtmosphereSet atmosphereSet, SpaceShip spaceShip, CurrentDataSet currentDataSet) {
		double Lt = 1.6311e-9;
		AerodynamicSet aerodynamicSet = new AerodynamicSet();
		aerodynamicSet.setCdC(AtmosphereModel.get_CdC(currentDataSet.getR_ECEF_spherical()[2],0));                           							// Continuum flow drag coefficient [-]
		aerodynamicSet.setDragCoefficient(AtmosphereModel.load_Drag(currentDataSet.getV_NED_ECEF_spherical()[0], 
				currentDataSet.getR_ECEF_spherical()[2], atmosphereSet.getStaticPressure(), atmosphereSet.getStaticTemperature(), 
        aerodynamicSet.getCdC(), Lt, atmosphereSet.getGasConstant())); 		    	// Lift coefficient                [-]
		aerodynamicSet.setFlowzone(AtmosphereModel.calc_flowzone(currentDataSet.getV_NED_ECEF_spherical()[0], 
				currentDataSet.getR_ECEF_spherical()[2], atmosphereSet.getStaticPressure(), atmosphereSet.getStaticTemperature(), Lt)); 
	 	//-----------------------------------------------------------------------------------------------
		aerodynamicSet.setDragForce(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * aerodynamicSet.getDragCoefficient());  		// Aerodynamic drag Force 		   [N]
		aerodynamicSet.setLiftForce(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * aerodynamicSet.getLiftCoefficient() * cos( aerodynamicSet.getBankAngle() ) );               // Aerodynamic lift Force 		   [N]
		aerodynamicSet.setSideForce(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * aerodynamicSet.getC_SF() * sin( aerodynamicSet.getBankAngle() )); 	                  // Aerodynamic side Force 		   [N]
		//----------------------------------------------------------------------------------------------
		return aerodynamicSet;
	}

}
