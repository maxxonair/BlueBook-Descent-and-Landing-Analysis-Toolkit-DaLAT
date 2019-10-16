package Model;

import FlightElement.SpaceShip;
import Model.DataSets.ActuatorSet;
import Model.DataSets.ControlCommandSet;
import Simulator_main.CurrentDataSet;

public class ActuatorModel {
	
	public static ActuatorSet getActuatorSet(ControlCommandSet controlCommandSet, SpaceShip spaceShip, CurrentDataSet currentDataSet) {
		ActuatorSet actuatorSet = new ActuatorSet();
		double deltaPropellant = spaceShip.getMass() - currentDataSet.getxIS()[6];
		
		double primaryPropellant = spaceShip.getPropulsion().getPrimaryPropellant()-deltaPropellant;
		// Set Propellant in tanks: 
		actuatorSet.setPrimaryPropellant_is(primaryPropellant);
		// Set ISP
		if(spaceShip.getPropulsion().isPrimaryThrottleModel()) {actuatorSet.setPrimaryISP_is(ThrottleMODEL_get_ISP(spaceShip, 
				controlCommandSet.getPrimaryThrustThrottleCmd()));} else {
    				actuatorSet.setPrimaryISP_is(spaceShip.getPropulsion().getPrimaryISPMax());
    			}
		// Set Thrust 
		if(primaryPropellant>0) {
		actuatorSet.setPrimaryThrust_is(controlCommandSet.getPrimaryThrustThrottleCmd()*spaceShip.getPropulsion().getPrimaryThrustMax());
		}
		
		
		actuatorSet.setTVC_alpha(controlCommandSet.getTVC_alpha());
		actuatorSet.setTVC_beta(controlCommandSet.getTVC_beta());
		
		
		if(!Double.isNaN(controlCommandSet.getMomentumRCS_X_cmd()*spaceShip.getPropulsion().getRCSMomentumX())) {
		actuatorSet.setMomentumRCS_X_is(controlCommandSet.getMomentumRCS_X_cmd()*spaceShip.getPropulsion().getRCSMomentumX());
		} else {
			actuatorSet.setMomentumRCS_X_is(0);	
			System.out.println("ERROR: Roll control failed - reset RCS X");
		}
		if(!Double.isNaN(controlCommandSet.getMomentumRCS_Y_cmd()*spaceShip.getPropulsion().getRCSMomentumY())) {
		actuatorSet.setMomentumRCS_Y_is(controlCommandSet.getMomentumRCS_Y_cmd()*spaceShip.getPropulsion().getRCSMomentumY());
		} else {
		actuatorSet.setMomentumRCS_Y_is(0);
		System.out.println("ERROR: Pitch control failed - reset RCS Y");
		}
		actuatorSet.setMomentumRCS_Z_is(controlCommandSet.getMomentumRCS_Z_cmd()*spaceShip.getPropulsion().getRCSMomentumZ());
		
		
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
		 		double m = (spaceShip.getPropulsion().getPrimaryISPMax() - spaceShip.getPropulsion().getPrimaryISPMin())/(1 - ControllerModel.getCmd_min());
		 		double n = spaceShip.getPropulsion().getPrimaryISPMax() - m ; 
		 		IspOut = m * Throttle_CMD + n; 
		 	}
		 	return IspOut; 
		 }
}
