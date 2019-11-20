package Model.Aerodynamic;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import FlightElement.SpaceShip;
import Model.DataSets.ActuatorSet;
import Model.DataSets.AerodynamicSet;
import Model.DataSets.AtmosphereSet;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.HypersonicSet;
import Simulator_main.DataSets.CurrentDataSet;
import Simulator_main.DataSets.IntegratorData;

public class AerodynamicModel {
    public static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;
    public static double kB    = 1.380650424e-23;              // Boltzmann constant                         [SI]    
    public static double sigma = 1.6311e-9;
    
    private static int sequenceIs=-1;
    private static double lastTimeMark=0;
    
	public static AerodynamicSet getAerodynamicSet(AtmosphereSet atmosphereSet, SpaceShip spaceShip, CurrentDataSet currentDataSet, IntegratorData integratorData, ActuatorSet actuatorSet, ControlCommandSet controlCommandSet) {
		
		if(sequenceIs!=controlCommandSet.getActiveSequence()) {
			sequenceIs=controlCommandSet.getActiveSequence();
			lastTimeMark = currentDataSet.getGlobalTime();
		}
		currentDataSet.setSequenceTime(currentDataSet.getGlobalTime()-lastTimeMark);
		
		
		AerodynamicSet aerodynamicSet = new AerodynamicSet();
		
		getAerodynamicCoefficients(atmosphereSet,  aerodynamicSet,  spaceShip, integratorData, currentDataSet); 
		
		
		aerodynamicSet.setDragCoefficient(calcDrag(atmosphereSet, currentDataSet, aerodynamicSet, spaceShip, integratorData)); 		    	// Lift coefficient                [-]
		//-----------------------------------------------------------------------------------------------
		aerodynamicSet.setDragForce(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * aerodynamicSet.getDragCoefficient());  		// Aerodynamic drag Force 		   [N]
		aerodynamicSet.setLiftForce(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * aerodynamicSet.getLiftCoefficient() * cos( aerodynamicSet.getAerodynamicBankAngle() ) );               // Aerodynamic lift Force 		   [N]
		aerodynamicSet.setSideForce(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * aerodynamicSet.getC_SF() * sin( aerodynamicSet.getAerodynamicBankAngle() )); 	                  // Aerodynamic side Force 		   [N]
		//----------------------------------------------------------------------------------------------
		if(actuatorSet.isParachuteDeployed() && !actuatorSet.isParachuteEject()) {
			//System.out.println(integratorData.getAeroParachuteModel()+"|"+integratorData.getConstParachuteCd());

		  if(integratorData.getAeroParachuteModel()==1) {
				double CdP = 0 ;
				if(atmosphereSet.getMach()<1) {
					CdP = 0.7;
				} else {
					double Ma = atmosphereSet.getMach();
					CdP = -0.16666667*Ma+(0.866666667);
					if(CdP<0.2) {
						CdP=0.2;
					}
					//System.out.println(Ma+"|"+CdP);
				}
				double parachuteDeploymentEffect = getParachuteDeploymentMode(currentDataSet.getSequenceTime(), 3) ;
				CdP = CdP * parachuteDeploymentEffect;
				
				aerodynamicSet.setDragCoefficientParachute(CdP);
				// Linear model derived from data provided by: 
				// I. Clarke, Supersonic Inflatable Aerodynamic Decelerators For Use On Future Robotic Missions to Mars 
			} else if (integratorData.getAeroParachuteModel()==0) {
				aerodynamicSet.setDragCoefficientParachute(integratorData.getConstParachuteCd());
			} 
			
			aerodynamicSet.setDragForceParachute(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getParachuteSurfaceArea() * aerodynamicSet.getDragCoefficientParachute());
		} else {
			aerodynamicSet.setDragCoefficientParachute(0.0);
			aerodynamicSet.setDragForceParachute(0);	
		}
		//-----------------------------------------------------------------------------------------------
		double referenceLength = 1;
		aerodynamicSet.setMx(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * referenceLength * aerodynamicSet.getCMx());  		
		aerodynamicSet.setMy(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * referenceLength * aerodynamicSet.getCMy());              
		aerodynamicSet.setMz(atmosphereSet.getDynamicPressure() * spaceShip.getAeroElements().getSurfaceArea() * referenceLength * aerodynamicSet.getCMz()); 	                 
		//----------------------------------------------------------------------------------------------
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
				double CDC=aerodynamicSet.getCdC();
				//System.out.println(currentDataSet.gettIS()+"|"+CDC);
				//double Lt = read_data(file_cship,2);
				double Kn = kB * atmosphereSet.getStaticTemperature() / ( Math.sqrt(2) * PI * sigma * sigma * atmosphereSet.getStaticPressure() * 1 );
				aerodynamicSet.setKnudsenNumber(Kn);
				//System.out.println(Kn);
				if(Kn<0.1){
			//	               Continuum flow        <---------------
					CD = CDC;
					aerodynamicSet.setFlowzone(1);
				}
				if(Kn>0.1 && Kn<10){
			//	               Transtional zone      <---------------
				double S = currentDataSet.getV_NED_ECEF_spherical()[0] / Math.sqrt(2 * atmosphereSet.getGasConstant() * atmosphereSet.getStaticTemperature());
				double Cdfm= 1.75 + Math.sqrt(PI)/(2 * S);
				CD= CDC + ( Cdfm - CDC ) * ( 1/3 * Math.log10( Kn / Math.sin( PI / 6 ) ) * 0.5113 ) ;
				aerodynamicSet.setFlowzone(2);
				}
				if(Kn>10){
			//	               Free molecular zone   <---------------
				double S = currentDataSet.getV_NED_ECEF_spherical()[0] / Math.sqrt(2 * atmosphereSet.getGasConstant() * atmosphereSet.getStaticTemperature());
				CD= 1.75 + Math.sqrt(PI)/(2 * S);
				aerodynamicSet.setFlowzone(3);
				}
		}
	return CD;
	}
	//----------------------------------------------------------------------------------------------------------------------------

	public static double getAerodynamicCoefficients(AtmosphereSet atmosphereSet, AerodynamicSet aerodynamicSet, SpaceShip spaceShip, IntegratorData integratorData, CurrentDataSet currentDataSet){
		double CdC = 1.55 ; 
		if(integratorData.getAeroDragModel()==0) {
			 CdC = 1.55 ; 
		} else if (integratorData.getAeroDragModel()==1) {
		    	HypersonicSet hypersonicSet = HypersonicModel.hypersonicFlowModel(atmosphereSet, aerodynamicSet, spaceShip, currentDataSet);
		    	CdC = hypersonicSet.getCD();
		    	aerodynamicSet.setLiftCoefficient(hypersonicSet.getCL());
		    	aerodynamicSet.setSideForceCoefficient(hypersonicSet.getCY());
		    	
		    	aerodynamicSet.setCMx(hypersonicSet.getCMx());
		    	aerodynamicSet.setCMy(hypersonicSet.getCMy());
		    	aerodynamicSet.setCMz(hypersonicSet.getCMz());
		}
		
		aerodynamicSet.setCdC(CdC);
		return CdC; 
	}
	
	
	private static double getParachuteDeploymentMode(double timeSinceMortar, double timeToFullDeployment) {
		if(timeSinceMortar<timeToFullDeployment) {
			double y = 1/( 1 + Math.pow((timeSinceMortar/(timeToFullDeployment-timeSinceMortar)),-2));
			return y;
		} else {
			return 1;
		}
	}

}
