package Model;

import FlightElement.SpaceShip;
import Model.DataSets.ActuatorSet;
import Model.DataSets.ControlCommandSet;
import Noise.ActuatorNoiseModel;
import Simulator_main.DataSets.CurrentDataSet;
import Simulator_main.DataSets.IntegratorData;

public class ActuatorModel {
	
	public static ActuatorSet getActuatorSet(ControlCommandSet controlCommandSet, SpaceShip spaceShip, CurrentDataSet currentDataSet, IntegratorData integratorData) {
		
		
		ActuatorSet actuatorSet = new ActuatorSet();
		//double deltaPropellant = spaceShip.getMass() - currentDataSet.getxIS()[6];
		if(integratorData.isActuatorNoiseModel()) {
			ActuatorNoiseModel.setPrimaryThrustNoise(actuatorSet.getActuatorNoiseSet());
			ActuatorNoiseModel.setRCSXNoise(actuatorSet.getActuatorNoiseSet());
			ActuatorNoiseModel.setRCSYNoise(actuatorSet.getActuatorNoiseSet());
			ActuatorNoiseModel.setRCSZNoise(actuatorSet.getActuatorNoiseSet());
		} else {
			actuatorSet.getActuatorNoiseSet().setPrimaryThrustNoise(0);
			actuatorSet.getActuatorNoiseSet().setRCSMomentumX(0);
			actuatorSet.getActuatorNoiseSet().setRCSMomentumY(0);
			actuatorSet.getActuatorNoiseSet().setRCSMomentumZ(0);
		}
		
		//double primaryPropellant = spaceShip.getPropulsion().getPrimaryPropellant()-deltaPropellant;
		double primaryPropellant = spaceShip.getPropulsion().getPrimaryPropellantFillingLevel();
		//System.out.println(primaryPropellant);
		// Set Propellant in tanks: 
		actuatorSet.setPrimaryPropellant_is(primaryPropellant);
		// Set ISP
		if(spaceShip.getPropulsion().isPrimaryThrottleModel()) {actuatorSet.setPrimaryISP_is(ThrottleMODEL_get_ISP(spaceShip, 
				controlCommandSet.getPrimaryThrustThrottleCmd()));} else {
    				actuatorSet.setPrimaryISP_is(spaceShip.getPropulsion().getPrimaryISPMax());
    			}
		// Set Thrust 
		if(primaryPropellant>0) {
			double thrustNominal = controlCommandSet.getPrimaryThrustThrottleCmd()*spaceShip.getPropulsion().getPrimaryThrustMax();
			double randomThrustVariation = thrustNominal * actuatorSet.getActuatorNoiseSet().getPrimaryThrustNoise();
			double thrustIs = thrustNominal + randomThrustVariation;
			//thrustIs = getMainEngineResponseDelay(double timeSinceCMD, double timeToThrustLevel);
		actuatorSet.setPrimaryThrust_is(thrustIs);
		} else {
			actuatorSet.setPrimaryThrust_is(0);
		}
		
		
		actuatorSet.setTVC_alpha(controlCommandSet.getTVC_alpha());
		actuatorSet.setTVC_beta(controlCommandSet.getTVC_beta());
		
		
		if(!Double.isNaN(controlCommandSet.getMomentumRCS_X_cmd()*spaceShip.getPropulsion().getRCSMomentumX())) {
			double momentumNominal = controlCommandSet.getMomentumRCS_X_cmd()*spaceShip.getPropulsion().getRCSMomentumX(); 
			double momentumIs = momentumNominal + actuatorSet.getActuatorNoiseSet().getRCSMomentumX()*momentumNominal; 
		actuatorSet.setMomentumRCS_X_is(momentumIs);
		actuatorSet.setRCS_X_ISP(spaceShip.getPropulsion().getSecondaryISP_RCS_X());
		} else {
			actuatorSet.setMomentumRCS_X_is(0);	
			actuatorSet.setRCS_X_ISP(0);	
			System.out.println("ERROR: Roll control failed - reset RCS X");
		}
		if(!Double.isNaN(controlCommandSet.getMomentumRCS_Y_cmd()*spaceShip.getPropulsion().getRCSMomentumY())) {
			double momentumNominal = controlCommandSet.getMomentumRCS_Y_cmd()*spaceShip.getPropulsion().getRCSMomentumY(); 
			double momentumIs = momentumNominal + actuatorSet.getActuatorNoiseSet().getRCSMomentumY()*momentumNominal; 
		actuatorSet.setMomentumRCS_Y_is(momentumIs);
		actuatorSet.setRCS_Y_ISP(spaceShip.getPropulsion().getSecondaryISP_RCS_Y());
		} else {
		actuatorSet.setMomentumRCS_Y_is(0);
		actuatorSet.setRCS_Y_ISP(0);
		System.out.println("ERROR: Pitch control failed - reset RCS Y");
		}
		
		double momentumNominal = controlCommandSet.getMomentumRCS_Z_cmd()*spaceShip.getPropulsion().getRCSMomentumZ(); 
		double momentumIs = momentumNominal + actuatorSet.getActuatorNoiseSet().getRCSMomentumZ()*momentumNominal; 
		actuatorSet.setMomentumRCS_Z_is(momentumIs);
		actuatorSet.setRCS_Z_ISP(spaceShip.getPropulsion().getSecondaryISP_RCS_Z());
		
		
		if(controlCommandSet.isParachuteDeployedCMD()) {
			actuatorSet.setParachuteDeployed(true);
		}
		
		if(controlCommandSet.isParachuteEjectCMD()) {
			actuatorSet.setParachuteEject(true);
		}
		
		return actuatorSet;
	}

	 public static double ThrottleMODEL_get_ISP(SpaceShip spaceShip, double Throttle_CMD) {
		 	double IspOut=0;
		 	if(Throttle_CMD>1 || Throttle_CMD<0) {
		 		System.out.println("ERROR: ISP model - throttle command out of range" );
		 		IspOut =  spaceShip.getPropulsion().getPrimaryISPMax();
		 	} else if (spaceShip.getPropulsion().getPrimaryISPMin()>spaceShip.getPropulsion().getPrimaryISPMax()) {
		 		System.out.println("ERROR: ISP model - minimum ISP is larger than maximum ISP" );
		 		IspOut=spaceShip.getPropulsion().getPrimaryISPMax();
		 	} else if (spaceShip.getPropulsion().getPrimaryISPMin()<0 || spaceShip.getPropulsion().getPrimaryISPMax()<0) {
		 		System.out.println("ERROR: ISP model - ISP below 0");
		 		IspOut=0; 
		 	} else {
		 		double m = (spaceShip.getPropulsion().getPrimaryISPMax() - spaceShip.getPropulsion().getPrimaryISPMin())/(1);
		 		double n = spaceShip.getPropulsion().getPrimaryISPMax() - m ; 
		 		IspOut = m * Throttle_CMD + n; 
		 	}
		 	return IspOut; 
		 }
	 
		private static double getMainEngineResponseDelay(double timeSinceCMD, double timeToThrustLevel) {
			if(timeSinceCMD<timeToThrustLevel) {
				double y = 1/( 1 + Math.pow((timeSinceCMD/(timeToThrustLevel-timeSinceCMD)),-2));
				return y;
			} else {
				return 1;
			}
		}
}
