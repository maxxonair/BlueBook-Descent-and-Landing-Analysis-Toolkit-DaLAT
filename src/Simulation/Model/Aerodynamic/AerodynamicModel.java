package Simulation.Model.Aerodynamic;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import FlightElement.SpaceShip;
import Simulation.Model.DataSets.AerodynamicSet;
import Simulation.Model.DataSets.AtmosphereSet;
import Simulation.Model.DataSets.HypersonicSet;
import Simulation.Noise.AerodynamicNoiseModel;
import Simulation.NoiseSet.AerodynamicNoiseSet;
import utils.UConst;
import Simulator_main.DataSets.IntegratorData;

public class AerodynamicModel {
    public static double sigma = 1.6311e-9;
    
    private static int sequenceIs=-1;
    private static double lastTimeMark=0;
    
    private static long noiseSeed = 5;
    private static AerodynamicNoiseModel aerodynamicNoiseModel = new AerodynamicNoiseModel(new AerodynamicNoiseSet(),noiseSeed);
    
	public static AerodynamicSet getAerodynamicSet(AtmosphereSet atmosphereSet, SpaceShip spaceShip, IntegratorData integratorData) {
		int dragModelSelection = integratorData.getAeroDragModel();
		
		// Update Aerodynamic Noise Model:
		aerodynamicNoiseModel.setDragCoeffNoise();
		aerodynamicNoiseModel.setLiftCoeffNoise();
		aerodynamicNoiseModel.setsideforceCoeffNoise();
		aerodynamicNoiseModel.setParachuteCoeffNoise();
		
		// Get Aerodynamic Set
			   if(dragModelSelection  == 0) {  // standard model / CD = constant 
			return getAerodynamicSetStandard( atmosphereSet,  spaceShip,  integratorData);
		} else if (dragModelSelection == 1) {  // standard model / CD from panel method (valid in hypersonics only)
			return getAerodynamicSetStandard( atmosphereSet,  spaceShip,  integratorData);
		} else if (dragModelSelection == 2) { // launcher model 
			return getAerodynamicSetLauncher( atmosphereSet,  spaceShip,  integratorData);
		} else if (dragModelSelection == 3) {  // AeroModel off -> No AERODYNAMIC FORCES
			return getAerodynamicSetVoid() ;
		} else { 							  // fallback standard model 
			return getAerodynamicSetStandard( atmosphereSet,  spaceShip,  integratorData);
		}
		
	}
	
	private static AerodynamicSet getAerodynamicSetVoid() {
		return new AerodynamicSet();
	}
	
	private static AerodynamicSet getAerodynamicSetLauncher(AtmosphereSet atmosphereSet, SpaceShip spaceShip, IntegratorData integratorData) {
		AerodynamicSet aerodynamicSet = new AerodynamicSet();
		//-------------------------------------------------------------------------------------------
		if(sequenceIs!=spaceShip.getgNCModel().getControlCommandSet().getActiveSequence()) {
			sequenceIs=spaceShip.getgNCModel().getControlCommandSet().getActiveSequence();
			lastTimeMark = spaceShip.getState().getGlobalTime();
		}
		spaceShip.getState().setSequenceTime(spaceShip.getState().getGlobalTime()-lastTimeMark);
		//-------------------------------------------------------------------------------------------
		double CDNoise = 1;
		double CLNoise = 1;
		//double CyNoise = 1;
		if(integratorData.getNoiseModel().isAerodynamicNoiseModel()) {
			 CDNoise = aerodynamicNoiseModel.getAerodynamicNoiseSet().getDragCoeffNoise();
			 CLNoise = aerodynamicNoiseModel.getAerodynamicNoiseSet().getLiftCoeffNoise();
			 //CyNoise = aerodynamicNoiseSet.getSideCoeffNoise();
		}
		aerodynamicSet.setDragCoefficient(AerodynamicKitLauncher.getCD(atmosphereSet, spaceShip) * CDNoise);
		aerodynamicSet.setLiftCoefficient(AerodynamicKitLauncher.getCL(spaceShip) * CLNoise);
		
		double alpha = spaceShip.getState().getEulerAngle().pitch - spaceShip.getState().getV_NED_ECEF_spherical()[1];
		aerodynamicSet.setAerodynamicAngleOfAttack(alpha);
		//-----------------------------------------------------------------------------------------------
		aerodynamicSet.setDragForce(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * aerodynamicSet.getDragCoefficient());  		// Aerodynamic drag Force 		   [N]
		aerodynamicSet.setLiftForce(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * aerodynamicSet.getLiftCoefficient() * cos( aerodynamicSet.getAerodynamicBankAngle() ) );               // Aerodynamic lift Force 		   [N]
		aerodynamicSet.setSideForce(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * aerodynamicSet.getC_SF() * sin( aerodynamicSet.getAerodynamicBankAngle() )); 	                  // Aerodynamic side Force 		   [N]
		//----------------------------------------------------------------------------------------------
		/**
		 * 
		 * No parachute model implemented in this mode 
		 */
		//-----------------------------------------------------------------------------------------------
		double referenceLength = 1;
		/**
		 * 
		 * Simplified aerodynamic momentum model 
		 * -> no wind 
		 * -> no bank angle 
		 */
		double leverAero = AerodynamicKitLauncher.getCoP(spaceShip) - spaceShip.getProperties().getMassAndInertia().getCoM();
		double My = (aerodynamicSet.getDragForce() * Math.sin(alpha) + aerodynamicSet.getLiftForce() * Math.cos(alpha)) 
				    * ( leverAero );
		//System.out.println(AerodynamicKitLauncher.getCoP(spaceShip.getVehicleLength(), currentDataSet)+"|"+My);
		aerodynamicSet.setMx(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * referenceLength * aerodynamicSet.getCMx());  		
		aerodynamicSet.setMy(My);              
		aerodynamicSet.setMz(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * referenceLength * aerodynamicSet.getCMz()); 	                 
		//----------------------------------------------------------------------------------------------
		
		return aerodynamicSet;
	}
	
	private static AerodynamicSet getAerodynamicSetStandard(AtmosphereSet atmosphereSet, SpaceShip spaceShip, IntegratorData integratorData) {
		if(sequenceIs!=spaceShip.getgNCModel().getControlCommandSet().getActiveSequence()) {
			sequenceIs=spaceShip.getgNCModel().getControlCommandSet().getActiveSequence();
			lastTimeMark = spaceShip.getState().getGlobalTime();
		}
		spaceShip.getState().setSequenceTime(spaceShip.getState().getGlobalTime()-lastTimeMark);
		
		AerodynamicSet aerodynamicSet = new AerodynamicSet();
		
		getAerodynamicCoefficients(atmosphereSet,  aerodynamicSet,  spaceShip, integratorData); 
		
		double CDNoise = 1;
		if(integratorData.getNoiseModel().isAerodynamicNoiseModel()) {
			 CDNoise = aerodynamicNoiseModel.getAerodynamicNoiseSet().getDragCoeffNoise();
		}
		double cD_nominal = calcDrag(atmosphereSet, aerodynamicSet, spaceShip, integratorData);
		aerodynamicSet.setDragCoefficient( cD_nominal * CDNoise); 		    	// Drag coefficient                [-]
		//-----------------------------------------------------------------------------------------------
		aerodynamicSet.setDragForce(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * aerodynamicSet.getDragCoefficient() );  														  // Aerodynamic drag Force 		   [N]
		aerodynamicSet.setLiftForce(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * aerodynamicSet.getLiftCoefficient() * cos( aerodynamicSet.getAerodynamicBankAngle() ) );         // Aerodynamic lift Force 		   [N]
		aerodynamicSet.setSideForce(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * aerodynamicSet.getC_SF() * sin( aerodynamicSet.getAerodynamicBankAngle() ) ); 	                  // Aerodynamic side Force 		   [N]
		//----------------------------------------------------------------------------------------------
		/**
		 *  Condition no wind! 
		 */
		//System.out.println(currentDataSet.getEulerAngle()[1][0]*180/PI+"|"+currentDataSet.getV_NED_ECEF_spherical()[1]*180/PI);
		aerodynamicSet.setAerodynamicAngleOfAttack(spaceShip.getState().getEulerAngle().pitch - spaceShip.getState().getV_NED_ECEF_spherical()[1] );
		//----------------------------------------------------------------------------------------------
		
		double ParachuteNoise = 1;
		if(integratorData.getNoiseModel().isAerodynamicNoiseModel()) {
			ParachuteNoise = aerodynamicNoiseModel.getAerodynamicNoiseSet().getParachuteDragCoeffNoise();
		}
		
		if(spaceShip.getForceTorqueModel().getActuatorSet().isParachuteDeployed() && !spaceShip.getForceTorqueModel().getActuatorSet().isParachuteEject()) {
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
				}
				double parachuteDeploymentEffect = getParachuteDeploymentMode(spaceShip.getState().getSequenceTime(), 3) ;
				CdP = CdP * parachuteDeploymentEffect;
				
				aerodynamicSet.setDragCoefficientParachute(CdP);
				// Linear model derived from data provided by: 
				// I. Clarke, Supersonic Inflatable Aerodynamic Decelerators For Use On Future Robotic Missions to Mars 
			} else if (integratorData.getAeroParachuteModel()==0) {
				aerodynamicSet.setDragCoefficientParachute(integratorData.getConstParachuteCd() * ParachuteNoise);
			} 
			
			aerodynamicSet.setDragForceParachute(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getParachuteSurfaceArea() * aerodynamicSet.getDragCoefficientParachute());
		} else {
			aerodynamicSet.setDragCoefficientParachute(0.0);
			aerodynamicSet.setDragForceParachute(0);	
		}
		//-----------------------------------------------------------------------------------------------
		double referenceLength = 1;
		aerodynamicSet.setMx(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * referenceLength * aerodynamicSet.getCMx());  		
		aerodynamicSet.setMy(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * referenceLength * aerodynamicSet.getCMy());              
		aerodynamicSet.setMz(atmosphereSet.getDynamicPressure() * spaceShip.getProperties().getAeroElements().getSurfaceArea() * referenceLength * aerodynamicSet.getCMz()); 	                 
		//----------------------------------------------------------------------------------------------
		//System.out.println(aerodynamicSet.getDragCoefficient());
		return aerodynamicSet;
	}
	//----------------------------------------------------------------------------------------------------------------------------
	//
	//	                                        Calculate Drag with three flow zones approach
	//	                                      Free molecular -> transitional -> Continuum flow
	//
	//----------------------------------------------------------------------------------------------------------------------------
	public static double calcDrag(AtmosphereSet atmosphereSet, AerodynamicSet aerodynamicSet, SpaceShip spaceShip, IntegratorData integratorData)
	{
	double CD = 0;
		if(atmosphereSet.getDensity()!=0) {
				double CDC=aerodynamicSet.getCdC();
				//System.out.println(currentDataSet.gettIS()+"|"+CDC);
				//double Lt = read_data(file_cship,2);
				double Kn = UConst.kB * atmosphereSet.getStaticTemperature() / ( Math.sqrt(2) * UConst.PI * sigma * sigma * atmosphereSet.getStaticPressure() * 1 );
				aerodynamicSet.setKnudsenNumber(Kn);
				//System.out.println(Kn);
				if(Kn<0.1){
			//	               Continuum flow        <---------------
					CD = CDC;
					aerodynamicSet.setFlowzone(1);
				}
				if(Kn>0.1 && Kn<10){
			//	               Transtional zone      <---------------
				double S = spaceShip.getState().getV_NED_ECEF_spherical()[0] / Math.sqrt(2 * atmosphereSet.getGasConstant() * atmosphereSet.getStaticTemperature());
				double Cdfm= 1.75 + Math.sqrt(UConst.PI)/(2 * S);
				CD= CDC + ( Cdfm - CDC ) * ( 1/3 * Math.log10( Kn / Math.sin( UConst.PI / 6 ) ) * 0.5113 ) ;
				aerodynamicSet.setFlowzone(2);
				}
				if(Kn>10){
			//	               Free molecular zone   <---------------
				double S = spaceShip.getState().getV_NED_ECEF_spherical()[0] / Math.sqrt(2 * atmosphereSet.getGasConstant() * atmosphereSet.getStaticTemperature());
				CD= 1.75 + Math.sqrt(UConst.PI)/(2 * S);
				aerodynamicSet.setFlowzone(3);
				}
		}
	return CD;
	}
	//----------------------------------------------------------------------------------------------------------------------------

	public static double getAerodynamicCoefficients(AtmosphereSet atmosphereSet, AerodynamicSet aerodynamicSet, SpaceShip spaceShip, IntegratorData integratorData){
		double CdC = 1.55 ;
		double CDNoise = 1;
		double CLNoise = 1;
		double CyNoise = 1;
		if(integratorData.getNoiseModel().isAerodynamicNoiseModel()) {
			 CDNoise = aerodynamicNoiseModel.getAerodynamicNoiseSet().getDragCoeffNoise();
			 CLNoise = aerodynamicNoiseModel.getAerodynamicNoiseSet().getLiftCoeffNoise();
			 CyNoise = aerodynamicNoiseModel.getAerodynamicNoiseSet().getSideCoeffNoise();
		}
		if(integratorData.getAeroDragModel()==0) {
			 CdC = 1.55 ; 
		} else if (integratorData.getAeroDragModel()==1) {
		    	HypersonicSet hypersonicSet = HypersonicModel.hypersonicFlowModel(atmosphereSet, aerodynamicSet, spaceShip);
		    	CdC = hypersonicSet.getCD();
		    	aerodynamicSet.setLiftCoefficient(hypersonicSet.getCL() * CLNoise);
		    	aerodynamicSet.setSideForceCoefficient(hypersonicSet.getCY() * CyNoise);
		    	
		    	aerodynamicSet.setCMx(hypersonicSet.getCMx());
		    	aerodynamicSet.setCMy(hypersonicSet.getCMy());
		    	aerodynamicSet.setCMz(hypersonicSet.getCMz());
		}
		CdC = CdC * CDNoise;
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
