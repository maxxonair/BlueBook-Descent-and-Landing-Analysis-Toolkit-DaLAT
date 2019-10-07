package Model;

import FlightElement.SpaceShip;
import Sequence.Sequence;
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
    			Sequence.getControlCommandSet().getPrimaryThrustThrottleCmd()));} else {
    				actuatorSet.setPrimaryISP_is(spaceShip.getPropulsion().getPrimaryISPMax());
    			}
		// Set Thrust 
		if(primaryPropellant>0) {
		actuatorSet.setPrimaryThrust_is(controlCommandSet.getPrimaryThrustThrottleCmd()*spaceShip.getPropulsion().getPrimaryThrustMax());
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
