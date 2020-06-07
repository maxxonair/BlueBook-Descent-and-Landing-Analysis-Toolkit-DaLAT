package Simulation.Model.Aerodynamic;

import FlightElement.SpaceShip;
import Simulation.Model.DataSets.AtmosphereSet;

public class AerodynamicKitLauncher {
	/**
	 * 
	 * 	The following code contains a (very) simplified aerodynamic class for launcher applications 
	 * 
	 * it is based on data and assumption from: 
	 * 	Data from Sim p l i c i o , P . V. M., Marcos, A., & Bennani, S. (2019). Reusable Launchers:
		Development of a Coupled Flight Mechanics, Guidance and Control
		Benchmark. Journal of Spacecraft and Rockets.
		
	 *  Valid for fairly rough approximations of small/midsized launchers
	 *  
	 *  Assumptions: 
	 *  Drag coeffiecient = f(angle of attack, Mach) -> from tabular data/ curve fit
	 *  Lift coefficient = f(angle of attack) 		 -> from tabular data (interpolated) / 
	 *  											 		Assumption CL = idenpendent from Ma for small AoA
	 *  
	 *  valid until angle of attack ~10 [deg] , Mach 0 - 5
	 */
	
	public AerodynamicKitLauncher() {
		
	}
	
	public static double getCD(AtmosphereSet atmosphereSet, SpaceShip spaceShip) {
		double CD=0;
		// input alpha, Ma 
		double Ma = atmosphereSet.Mach;
		double alpha = Math.toDegrees(spaceShip.getState().getEulerAngle().pitch - spaceShip.getState().getV_NED_ECEF_spherical()[1]);
		if(alpha>20) {
			/**
			 * recommended range up to 10 degrees
			 * 
			 * Above 20 degrees approx. functions become completely useless, hence the clamping
			 */
			alpha =20;
		}
		
		if(Ma < 1) {
			double Cd0 = getCdMa0(alpha);
			double Cd1 = getCdMa1(alpha);
			
			CD = Cd0 + (Ma - 0 ) *(Cd1 - Cd0)/(1 - 0);
			
		} else if (Ma < 2 ) {
			double Cd0 = getCdMa1(alpha);
			double Cd1 = getCdMa2(alpha);
			
			CD = Cd0 + (Ma - 1 ) *(Cd1 - Cd0)/(2 - 1);
		} else if (Ma < 4 ) {
			double Cd0 = getCdMa2(alpha);
			double Cd1 = getCdMa4(alpha);
			
			CD = Cd0 + (Ma - 2 ) *(Cd1 - Cd0)/(4 - 2);
 		} else {
 			CD = getCdMa4(alpha);
 		}
		
		return CD;
	}
	
	private static double getCdMa0(double alpha) {
		return 0.0004*alpha*alpha - 0.0053*alpha + 1.0206;
	}
	
	private static double getCdMa1(double alpha) {
		return 0.0012*alpha*alpha + 0.0233*alpha + 0.9819;
	}
	
	private static double getCdMa2(double alpha) {
		return 0.0009*alpha*alpha+0.0013*alpha+1.5076;
	}
	
	private static double getCdMa4(double alpha) {
		return 0.0009*alpha*alpha - 0.0196*alpha + 1.814;
	}
	
	public static double getCL(SpaceShip spaceShip) {
		double alpha = Math.toDegrees(spaceShip.getState().getEulerAngle().pitch - spaceShip.getState().getV_NED_ECEF_spherical()[1]);
		return 0.003*alpha*alpha + 0.0288*alpha + 0.0046;
	}
	
	public static double getCoP(SpaceShip spaceShip) {
		/**
		 * 
		 *  CoP estimation based on approximations made in aforementioned publication
		 */		
		double GeomLength = spaceShip.getProperties().getGeometry().getVehicleLength() ;
		double alpha = Math.toDegrees(spaceShip.getState().getEulerAngle().pitch - spaceShip.getState().getV_NED_ECEF_spherical()[1]);
		//System.out.println(alpha);
		if(alpha <= 2 && alpha >= 0) {
			return (-19.329*alpha + 100.0)/100 * GeomLength;	
		} else if(alpha < 0 && alpha >= 2) {
			return (19.329*alpha + 100.0)/100 * GeomLength;	
		} else {
			return 0.61 * GeomLength;
		}
	}
}
