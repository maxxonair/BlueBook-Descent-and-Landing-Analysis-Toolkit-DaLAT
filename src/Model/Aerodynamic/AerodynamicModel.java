package Model.Aerodynamic;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import FlightElement.SpaceShip;
import Model.DataSets.ActuatorSet;
import Model.DataSets.AerodynamicSet;
import Model.DataSets.AtmosphereSet;
import Model.DataSets.HypersonicSet;
import Simulator_main.CurrentDataSet;
import Simulator_main.IntegratorData;

public class AerodynamicModel {
    public static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;
    public static double kB    = 1.380650424e-23;              // Boltzmann constant                         [SI]    
    public static double sigma = 1.6311e-9;
    
	public static AerodynamicSet getAerodynamicSet(AtmosphereSet atmosphereSet, SpaceShip spaceShip, CurrentDataSet currentDataSet, IntegratorData integratorData, ActuatorSet actuatorSet) {
		double Lt = 1.6311e-9;
		AerodynamicSet aerodynamicSet = new AerodynamicSet();
		aerodynamicSet.setCdC(get_CdC(atmosphereSet,  aerodynamicSet,  spaceShip, integratorData));                           							// Continuum flow drag coefficient [-]
		aerodynamicSet.setDragCoefficient(calcDrag(atmosphereSet, currentDataSet, aerodynamicSet, spaceShip, integratorData)); 		    	// Lift coefficient                [-]
		aerodynamicSet.setFlowzone(calcFlowzone(currentDataSet.getV_NED_ECEF_spherical()[0], 
				currentDataSet.getR_ECEF_spherical()[2], atmosphereSet.getStaticPressure(), atmosphereSet.getStaticTemperature(), Lt)); 
		//-----------------------------------------------------------------------------------------------
		aerodynamicSet.setDragForce(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * aerodynamicSet.getDragCoefficient());  		// Aerodynamic drag Force 		   [N]
		aerodynamicSet.setLiftForce(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * aerodynamicSet.getLiftCoefficient() * cos( aerodynamicSet.getAerodynamicBankAngle() ) );               // Aerodynamic lift Force 		   [N]
		aerodynamicSet.setSideForce(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * aerodynamicSet.getC_SF() * sin( aerodynamicSet.getAerodynamicBankAngle() )); 	                  // Aerodynamic side Force 		   [N]
		//----------------------------------------------------------------------------------------------
		if(actuatorSet.isParachuteDeployed() && !actuatorSet.isParachuteEject()) {

			boolean islinearParachuteDragModel=true;
			if(islinearParachuteDragModel) {
				double CdP = 0 ;
				if(atmosphereSet.getMach()<1) {
					CdP = 0.7;
				} else {
					double Ma = atmosphereSet.getMach();
					CdP = -0.16666667*Ma+(0.866666667);
					if(CdP<0.2) {
						CdP=0.2;
					}
					System.out.println(Ma+"|"+CdP);
				}
				aerodynamicSet.setDragCoefficientParachute(CdP);
				// Linear model derived from data provided by: 
				// I. Clarke, Supersonic Inflatable Aerodynamic Decelerators For Use On Future Robotic Missions to Mars 
			} else {
				aerodynamicSet.setDragCoefficientParachute(0.6);
			}
			
			aerodynamicSet.setDragForceParachute(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getParachuteSurfaceArea() * aerodynamicSet.getDragCoefficientParachute());
		} else {
			aerodynamicSet.setDragCoefficientParachute(0.0);
			aerodynamicSet.setDragForceParachute(0);	
		}
		return aerodynamicSet;
	}
	

	//----------------------------------------------------------------------------------------------------------------------------
	//
//	                                        Calculate Drag with three flowzone approach
//	                                      Free molecular -> transitional -> Contiuum flow
	//
	//----------------------------------------------------------------------------------------------------------------------------
	public static double calcDrag(AtmosphereSet atmosphereSet, CurrentDataSet currentDataSet, AerodynamicSet aerodynamicSet, SpaceShip spaceShip, IntegratorData integratorData)
	{
	double CD = 0;
		if(atmosphereSet.getDensity()!=0) {
				double CDC=get_CdC( atmosphereSet,  aerodynamicSet,  spaceShip, integratorData);
				//System.out.println(currentDataSet.gettIS()+"|"+CDC);
				//double Lt = read_data(file_cship,2);
				double Kn = kB * atmosphereSet.getStaticTemperature() / ( Math.sqrt(2) * PI * sigma * sigma * atmosphereSet.getStaticPressure() * currentDataSet.getLt() );
				if(Kn<0.1){
			//	               Continuum flow        <---------------
					CD = CDC;
				}
				if(Kn>0.1 && Kn<10){
			//	               Transtional zone      <---------------
				double S = currentDataSet.getV_NED_ECEF_spherical()[0] / Math.sqrt(2 * atmosphereSet.getGasConstant() * atmosphereSet.getStaticTemperature());
				double Cdfm= 1.75 + Math.sqrt(PI)/(2 * S);
				CD= CDC + ( Cdfm - CDC ) * ( 1/3 * Math.log10( Kn / Math.sin( PI / 6 ) ) * 0.5113 ) ;
				}
				if(Kn>10){
			//	               Free molecular zone   <---------------
				double S = currentDataSet.getV_NED_ECEF_spherical()[0] / Math.sqrt(2 * atmosphereSet.getGasConstant() * atmosphereSet.getStaticTemperature());
				CD= 1.75 + Math.sqrt(PI)/(2 * S);
				}
		}
	return CD;
	}
	//----------------------------------------------------------------------------------------------------------------------------
	public static int calcFlowzone( double vel, double h , double P , double T, double Lt)
	{
	double Kn = kB * T / ( Math.sqrt(2) * PI * sigma * sigma * P * Lt );
	int flowzone = 0;
	if(Kn<0.1){
	flowzone = 1;
	}
	if(Kn>0.1 && Kn<10){
	flowzone = 2;
	}
	if(Kn>10){
	flowzone = 3;
	}
	return flowzone;
	}

	public static double get_CdC(AtmosphereSet atmosphereSet, AerodynamicSet aerodynamicSet, SpaceShip spaceShip, IntegratorData integratorData){
		double CdC = 1.55 ; 
		
		if(integratorData.getAeroDragModel()==0) {
			 CdC = 1.55 ; 
		} else if (integratorData.getAeroDragModel()==1) {
		    	HypersonicSet hypersonicSet = HypersonicModel.hypersonicFlowModel(atmosphereSet, aerodynamicSet, spaceShip);
		    	CdC = hypersonicSet.getCD();
		}
		
		
		return CdC; 
	}

}
