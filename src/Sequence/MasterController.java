package Sequence;

import Controller.PID_01;
import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Simulator_main.DataSets.IntegratorData;
import Simulator_main.DataSets.RealTimeContainer;
import Simulator_main.DataSets.RealTimeResultSet;

public class MasterController {

	
	public static ControlCommandSet getControlCommandSet(ControlCommandSet controlCommandSet, 
			RealTimeContainer realTimeContainer, SpaceShip spaceShip, IntegratorData integratorData ) {
		
		RealTimeResultSet realTimeResultSet = realTimeContainer.getRealTimeResultSet();
		
		controlCommandSet = rollControl( controlCommandSet, realTimeResultSet, integratorData);
		if(realTimeResultSet.getVelocity()<400) {
			controlCommandSet.setParachuteDeployedCMD(true);
		}
		if(realTimeResultSet.getAltitude()<1500) {
			controlCommandSet.setParachuteEjectCMD(true);
		}
		
		
		
		
		return controlCommandSet;
	}
	
	
	public static ControlCommandSet rollControl(ControlCommandSet controlCommandSet, RealTimeResultSet realTimeResultSet, IntegratorData integratorData) {
		   double CTRL_ERROR =  realTimeResultSet.getEulerX() ;     

		   	 double RCS_X_CMD = -  PID_01.PID_001(CTRL_ERROR,integratorData.getMaxIntegTime(), 0.8, 0.0001, 3, 1, -1);
		   
		   	 //System.out.println(CTRL_ERROR+"|"+RCS_X_CMD);
		   	 controlCommandSet.setMomentumRCS_X_cmd(RCS_X_CMD);
		    return controlCommandSet;
		}
}
