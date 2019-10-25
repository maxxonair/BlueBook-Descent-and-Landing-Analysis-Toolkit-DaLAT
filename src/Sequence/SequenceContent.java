package Sequence;

import java.util.ArrayList;
import java.util.List;

import Controller.FlightController;
import Controller.FlightController_RollControl;
import Controller.FlightController_YawControl;
import Controller.FligthController_PrimaryThrust;
import Controller.Events.ParachuteDeployment;
import Controller.Events.ParachuteSeparation;
import Model.DataSets.SensorSet;

public class SequenceContent {
	
	private int ID; 
	
	private List<FlightController> controllerSets = new ArrayList<FlightController>();	
	private List<FlightController> EventSets = new ArrayList<FlightController>();
	
	private boolean triggerEnd=false;	
	private double triggerValue=0;
	private int triggerType=0;
	
	private boolean tswitch=true;
	private double tzero=0;

	
	public SequenceContent() {
		
	}
	
	public void deleteAllContent() {
		for(int i=controllerSets.size()-1;i>=0;i--) {
			controllerSets.remove(i);
		}
		for(int i=EventSets.size()-1;i>=0;i--) {
			EventSets.remove(i);
		}
		triggerEnd=false;
		triggerValue=0;
		triggerType=0;
		tswitch=true;
		tzero=0;
	}
		
	public void addRollControl() {
		FlightController_RollControl flightController_RollControl = new FlightController_RollControl();
		controllerSets.add(flightController_RollControl);
	}
	
	public void addPitchControl() {
		
	}
	
	public void addYawControl() {
		FlightController_YawControl flightController_YawControl = new FlightController_YawControl();
		controllerSets.add(flightController_YawControl);
	}
	
	public void addPrimaryThrustControl() {
		FligthController_PrimaryThrust fligthController_PrimaryThrust = new FligthController_PrimaryThrust();
		controllerSets.add(fligthController_PrimaryThrust);
	}
	
	public void addParachuteDeployment() {
		ParachuteDeployment parachuteDeployment = new ParachuteDeployment();
		EventSets.add(parachuteDeployment);
	}
	
	public void addParachuteSeparation() {
		ParachuteSeparation parachuteSeparation = new ParachuteSeparation();
		EventSets.add(parachuteSeparation);
	}
	
	public void setTriggerEnd(int triggerType, double triggerValue) {
		this.triggerType = triggerType;
		this.triggerValue = triggerValue;
	}

	public boolean isTriggerEnd(SensorSet sensorSet) {
		if(triggerType == 0) {		// Time trigger
			if(sensorSet.getGlobalTime()>triggerValue) {
				triggerEnd=true;
			}
		} else if (triggerType == 1) {
			//System.out.println(ID+"|"+sensorSet.getControllerTime()+"|"+triggerValue);
			if(sensorSet.getControllerTime()>triggerValue) {
				triggerEnd=true;
			}
		} else if (triggerType == 2) {	
			if(sensorSet.getRealTimeResultSet().getVelocity()<triggerValue && sensorSet.getGlobalTime()>5) {
				triggerEnd=true;
			}
		} else if (triggerType == 3 ) {	
			if(sensorSet.getRealTimeResultSet().getAltitude()<triggerValue) {
				triggerEnd=true;
			}
		}
		if(tswitch) {this.tzero=sensorSet.getGlobalTime();tswitch=false;} 
		((SensorSet) sensorSet).setControllerTime((double) sensorSet.getGlobalTime()-tzero);

		return triggerEnd;
	}

	public List<FlightController> getControllerSets() {
		return controllerSets;
	}

	public List<FlightController> getEventSets() {
		return EventSets;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}	 

}
