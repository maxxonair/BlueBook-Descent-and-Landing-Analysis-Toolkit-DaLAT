package Sequence;



import java.util.List;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;
import Simulator_main.DataSets.RealTimeContainer;

public class MasterController {
	
	private static ControlCommandSet controlCommandSet = new ControlCommandSet(); 

double sequence=0;	
	public static ControlCommandSet createMasterCommand(ControlCommandSet controlCommandSet, 
			RealTimeContainer realTimeContainer, SpaceShip spaceShip, SensorSet sensorSet, 
			List<SequenceContent> SequenceSet, double CtrlFrequency ) {
			//------------------------------------------------------------------------------------------------------------
			// 					Set active elements default values:
			//------------------------------------------------------------------------------------------------------------		
			controlCommandSet.setMomentumRCS_X_cmd(0);
			controlCommandSet.setMomentumRCS_Y_cmd(0);
			controlCommandSet.setMomentumRCS_Z_cmd(0);
			
			controlCommandSet.setPrimaryThrustThrottleCmd(0);
			//------------------------------------------------------------------------------------------------------------
			// 				Get Controller Response for active Sequence
			//------------------------------------------------------------------------------------------------------------
			for(int ctrIndx=0;ctrIndx<SequenceSet.get(controlCommandSet.getActiveSequence()).getControllerSets().size();ctrIndx++) {
				controlCommandSet = SequenceSet.get(controlCommandSet.getActiveSequence()).getControllerSets().get(ctrIndx).getCommand(controlCommandSet, sensorSet, spaceShip, CtrlFrequency);
			}
			//------------------------------------------------------------------------------------------------------------
			// 				Get Event Response for active Sequence
			//------------------------------------------------------------------------------------------------------------			
			for(int ctrIndx=0;ctrIndx<SequenceSet.get(controlCommandSet.getActiveSequence()).getEventSets().size();ctrIndx++) {
				controlCommandSet = SequenceSet.get(controlCommandSet.getActiveSequence()).getEventSets().get(ctrIndx).getCommand(controlCommandSet, sensorSet, spaceShip, CtrlFrequency);
			}
			//------------------------------------------------------------------------------------------------------------
			// 				Set Sequence end Trigger
			//------------------------------------------------------------------------------------------------------------
			if(SequenceSet.get(controlCommandSet.getActiveSequence()).isTriggerEnd(sensorSet)) {
				int activeSequence=controlCommandSet.getActiveSequence();
				activeSequence++;
				controlCommandSet.setActiveSequence(activeSequence);
			}

		MasterController.controlCommandSet = controlCommandSet; 
		return controlCommandSet;
	}
	
	public static ControlCommandSet getControlCommandSet() {
		return controlCommandSet; 
	}

}
