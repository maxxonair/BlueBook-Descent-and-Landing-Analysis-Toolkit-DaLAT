package FlightElement.GNCModel;



import java.util.List;

import FlightElement.SpaceShip;
import FlightElement.SensorModel.SensorSet;

public class MasterController {
	
	// double sequence=0;	
	private SpaceShip spaceShip;
	
	private double t;					// Time since last transport [s]
	
	public MasterController (SpaceShip spaceShip) {
		this.spaceShip = spaceShip; 
		
		t=0;
	}
	
	private boolean isControlCommand() {
		
		spaceShip.getgNCModel().setCTRLfrequencyIN(spaceShip.getProperties().getoBC().getControllerFrequency() );
		spaceShip.getgNCModel().setCTRLfrequencyOUT(spaceShip.getProperties().getoBC().getEnvironmentFrequency());
		
		double dtIN  = 1/spaceShip.getgNCModel().getCTRLfrequencyIN();    // Input time delta [s]	
		double dtOUT = 1/spaceShip.getgNCModel().getCTRLfrequencyOUT();   // Output time step [s]
		
		t+=dtIN;

		if(t >= dtOUT) { // Switching time reached -> switch next 
			t=0;	
			return true;
					
		} else {		    // Switching time not reached -> stay previous
			return false;
		}
	}
	
	public void callMasterCommand() {
		
			if(isControlCommand()) { // Implement Rate Transition and evalutate if controller cycle is reached
		
				//------------------------------------------------------------------------------------------------------------
				// 					Set active elements default values:
				//------------------------------------------------------------------------------------------------------------	
					
				// Reset all propulsive actuator commands prior to controller call: 
				spaceShip.getgNCModel().getControlCommandSet().setMomentumRCS_X_cmd(0);
				spaceShip.getgNCModel().getControlCommandSet().setMomentumRCS_Y_cmd(0);
				spaceShip.getgNCModel().getControlCommandSet().setMomentumRCS_Z_cmd(0);
				
				spaceShip.getgNCModel().getControlCommandSet().setPrimaryThrustThrottleCmd(0);
				
				// Get Mode Management Sequence:
				List<SequenceContent> SequenceSet = spaceShip.getProperties().getSequence().getSequenceSet();
				// Get Sensor Data of last timestep: 
				SensorSet sensorSet = spaceShip.getSensorModel().getSensorSet();
				
					try {
					//------------------------------------------------------------------------------------------------------------
					// 				Get Controller Response for active Sequence
					//------------------------------------------------------------------------------------------------------------
					for(int ctrIndx=0;ctrIndx<spaceShip.getProperties().getSequence().getSequenceSet().get(spaceShip.getgNCModel().getActiveSequence()).getControllerSets().size();ctrIndx++) {
						
				    spaceShip.getProperties().getSequence().getSequenceSet().get(spaceShip.getgNCModel().getActiveSequence()).getControllerSets().get(ctrIndx).setCommand(spaceShip) ;
						
					}
					} catch (IndexOutOfBoundsException e) {
					//	System.out.println("ERROR: MasterController detected Index out of Bounds. Flight Controller Set");
					}
					
					try {
					//------------------------------------------------------------------------------------------------------------
					// 				Get Event Response for active Sequence
					//------------------------------------------------------------------------------------------------------------			
					for(int ctrIndx=0;ctrIndx<spaceShip.getProperties().getSequence().getSequenceSet().get(spaceShip.getgNCModel().getActiveSequence()).getEventSets().size();ctrIndx++) {
								
					spaceShip.getProperties().getSequence().getSequenceSet().get( spaceShip.getgNCModel().getActiveSequence()).getEventSets().get(ctrIndx).setCommand(spaceShip);
					
					}
					} catch (IndexOutOfBoundsException e) {
						//System.out.println("ERROR: MasterController detected Index out of Bounds. Event Set");
					}
					
					// Move to next Sequence if trigger is reached:
					int look4ActiveSequenceIndx = spaceShip.getgNCModel().getActiveSequence();
					try {
						//------------------------------------------------------------------------------------------------------------
						// 				Set Sequence end Trigger
						//------------------------------------------------------------------------------------------------------------
						if(SequenceSet.get(look4ActiveSequenceIndx).isTriggerEnd(sensorSet)) {
							int activeSequence=spaceShip.getgNCModel().getActiveSequence();
							activeSequence++;
							spaceShip.getgNCModel().setActiveSequence(activeSequence);
							spaceShip.getgNCModel().getControlCommandSet().setActiveSequence(activeSequence);
						}
					} catch (IndexOutOfBoundsException e) {
						//System.out.println("ERROR: MasterController detected Index out of Bounds for Sequence Set evalutaion.Index= "+look4ActiveSequenceIndx);
					}
			
			}

	}
	
}
