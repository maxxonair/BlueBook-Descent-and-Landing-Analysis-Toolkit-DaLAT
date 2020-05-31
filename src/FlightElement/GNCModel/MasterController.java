package FlightElement.GNCModel;



import java.util.List;

import FlightElement.SpaceShip;
import FlightElement.SensorModel.SensorSet;

public class MasterController {
	
	// double sequence=0;	

	
	public MasterController () {

	}
	
	public ControlCommandSet createMasterCommand(SpaceShip spaceShip) {
			//------------------------------------------------------------------------------------------------------------
			// 					Set active elements default values:
			//------------------------------------------------------------------------------------------------------------	
			ControlCommandSet intCCS =  null;
			try {
				
				
				intCCS = (ControlCommandSet) spaceShip.getgNCModel().getControlCommandSet().clone();
			
			intCCS.setMomentumRCS_X_cmd(0);
			intCCS.setMomentumRCS_Y_cmd(0);
			intCCS.setMomentumRCS_Z_cmd(0);
			
			intCCS.setPrimaryThrustThrottleCmd(0);
			
			
			List<SequenceContent> SequenceSet = spaceShip.getProperties().getSequence().getSequenceSet();
			SensorSet sensorSet = spaceShip.getSensorModel().getSensorSet();
			
			try {
			//------------------------------------------------------------------------------------------------------------
			// 				Get Controller Response for active Sequence
			//------------------------------------------------------------------------------------------------------------
			for(int ctrIndx=0;ctrIndx<spaceShip.getProperties().getSequence().getSequenceSet().get(intCCS.getActiveSequence()).getControllerSets().size();ctrIndx++) {
				
		    spaceShip.getProperties().getSequence().getSequenceSet().get(intCCS
		    		.getActiveSequence()).getControllerSets().get(ctrIndx).setCommand(spaceShip) ;
				
			}
			} catch (IndexOutOfBoundsException e) {
			//	System.out.println("ERROR: MasterController detected Index out of Bounds. Flight Controller Set");
			}
			try {
			//------------------------------------------------------------------------------------------------------------
			// 				Get Event Response for active Sequence
			//------------------------------------------------------------------------------------------------------------			
			for(int ctrIndx=0;ctrIndx<spaceShip.getProperties().getSequence().getSequenceSet().get(intCCS.getActiveSequence()).getEventSets().size();ctrIndx++) {
						
			spaceShip.getProperties().getSequence().getSequenceSet().get( intCCS
					.getActiveSequence()).getEventSets().get(ctrIndx).setCommand( spaceShip);
			
			}
			} catch (IndexOutOfBoundsException e) {
				//System.out.println("ERROR: MasterController detected Index out of Bounds. Event Set");
			}
			try {
			//------------------------------------------------------------------------------------------------------------
			// 				Set Sequence end Trigger
			//------------------------------------------------------------------------------------------------------------
			if(SequenceSet.get(intCCS.getActiveSequence()).isTriggerEnd(sensorSet)) {
				int activeSequence=intCCS.getActiveSequence();
				activeSequence++;
				intCCS.setActiveSequence(activeSequence);
			}
			} catch (IndexOutOfBoundsException e) {
				//System.out.println("ERROR: MasterController detected Index out of Bounds. Sequence Set");
			}

			try {
				 spaceShip.getgNCModel().setControlCommandSet( (ControlCommandSet) intCCS.clone() );
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			} catch (CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return intCCS;

	}
	

}
