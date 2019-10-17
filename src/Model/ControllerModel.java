package Model;

import java.util.ArrayList;
import java.util.List;

import Controller.Flight_CTRL_PitchCntrl;
import Controller.Flight_CTRL_RollCntrl;
import Controller.Flight_CTRL_ThrustMagnitude;
import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Simulator_main.DataSets.CurrentDataSet;

public class ControllerModel {
	
    private static List<Flight_CTRL_ThrustMagnitude> Flight_CTRL_ThrustMagnitude = new ArrayList<Flight_CTRL_ThrustMagnitude>(); 
    private static List<Flight_CTRL_PitchCntrl> Flight_CTRL_PitchCntrl 			 = new ArrayList<Flight_CTRL_PitchCntrl>(); 
    private static List<Flight_CTRL_RollCntrl> Flight_CTRL_RollCntrl 			 = new ArrayList<Flight_CTRL_RollCntrl>(); 
    private static ArrayList<String> CTRL_steps 								 = new ArrayList<String>();
    public static double cmd_min = 0;
    private static int ctrl_curve;
    
    
	public static void initializeFlightController(SpaceShip spaceShip, CurrentDataSet currentDataSet, ControlCommandSet controlCommandSet) {
		for(int i=0;i<currentDataSet.getSEQUENCE_DATA_main().size();i++) {
			int ctrl_ID = currentDataSet.getSEQUENCE_DATA_main().get(i).get_sequence_controller_ID()-1;
			 controlCommandSet.setCtrl_vel(currentDataSet.getSEQUENCE_DATA_main().get(i).get_ctrl_target_vel());
			 controlCommandSet.setCtrl_alt(currentDataSet.getSEQUENCE_DATA_main().get(i).get_ctrl_target_alt());
			 double ctrl_fpa  = currentDataSet.getSEQUENCE_DATA_main().get(i).get_TVC_ctrl_target_fpa();
			 double ctrl_tend = currentDataSet.getSEQUENCE_DATA_main().get(i).get_TVC_ctrl_target_time();
			 int TVC_ctrl_ID  = currentDataSet.getSEQUENCE_DATA_main().get(i).get_sequence_TVCController_ID();
			// -> Create new Flight controller 
			 Flight_CTRL_ThrustMagnitude NewFlightController_ThrustMagnitude = new Flight_CTRL_ThrustMagnitude(ctrl_ID, 
					 true, currentDataSet.getxIS(), 0,  spaceShip.getPropulsion().getPrimaryPropellant(),  controlCommandSet.getCntr_v_init(),  
					 controlCommandSet.getCntr_h_init(),  -1,   controlCommandSet.getCtrl_vel(), controlCommandSet.getCtrl_alt(),  
					 spaceShip.getPropulsion().getPrimaryThrustMax(),  
					 spaceShip.getPropulsion().getPrimaryThrustMin(),  0,  0,  ctrl_curve,  
					 currentDataSet.getValDt(),0,0,0,0,0, currentDataSet.getRM(), currentDataSet.getLocalElevation());
			UPDATE_FlightController_ThrustMagnitude(NewFlightController_ThrustMagnitude);

			Flight_CTRL_PitchCntrl NewFlightController_PitchCntrl = new Flight_CTRL_PitchCntrl( TVC_ctrl_ID, 
					true, -1, 0, ctrl_tend, ctrl_fpa, currentDataSet.getRM() , currentDataSet.getLocalElevation());
			UPDATE_FlightController_PitchControl(NewFlightController_PitchCntrl);
			
			Flight_CTRL_RollCntrl NewFlightController_RollCntrl = new Flight_CTRL_RollCntrl( TVC_ctrl_ID, 
					true, -1, 0, ctrl_tend, ctrl_fpa, currentDataSet.getRM() , currentDataSet.getLocalElevation());
			UPDATE_FlightController_RollControl(NewFlightController_RollCntrl);
		}	
	}
	
	public static void UPDATE_FlightController_ThrustMagnitude(Flight_CTRL_ThrustMagnitude NewElement){	   
		   if (Flight_CTRL_ThrustMagnitude.size()==0){ Flight_CTRL_ThrustMagnitude.add(NewElement); 
		   } else {Flight_CTRL_ThrustMagnitude.add(NewElement); } 
	   }
	public static void UPDATE_FlightController_PitchControl(Flight_CTRL_PitchCntrl NewElement){	   
		   if (Flight_CTRL_PitchCntrl.size()==0){ Flight_CTRL_PitchCntrl.add(NewElement); 
		   } else {Flight_CTRL_PitchCntrl.add(NewElement); } 
	   }
	public static void UPDATE_FlightController_RollControl(Flight_CTRL_RollCntrl NewElement){	   
		   if (Flight_CTRL_RollCntrl.size()==0){ Flight_CTRL_RollCntrl.add(NewElement); 
		   } else {Flight_CTRL_RollCntrl.add(NewElement); } 
	   }


public static List<Flight_CTRL_RollCntrl> getFlight_CTRL_RollCntrl() {
		return Flight_CTRL_RollCntrl;
	}

	public static void setFlight_CTRL_RollCntrl(List<Flight_CTRL_RollCntrl> flight_CTRL_RollCntrl) {
		Flight_CTRL_RollCntrl = flight_CTRL_RollCntrl;
	}

public static List<Flight_CTRL_ThrustMagnitude> getFlight_CTRL_ThrustMagnitude() {
	return Flight_CTRL_ThrustMagnitude;
}

public static void setFlight_CTRL_ThrustMagnitude(List<Flight_CTRL_ThrustMagnitude> flight_CTRL_ThrustMagnitude) {
	Flight_CTRL_ThrustMagnitude = flight_CTRL_ThrustMagnitude;
}
 
public static List<Flight_CTRL_PitchCntrl> getFlight_CTRL_PitchCntrl() {
	return Flight_CTRL_PitchCntrl;
}

public static void setFlight_CTRL_PitchCntrl(List<Flight_CTRL_PitchCntrl> flight_CTRL_PitchCntrl) {
	Flight_CTRL_PitchCntrl = flight_CTRL_PitchCntrl;
}

public static ArrayList<String> getCTRL_steps() {
	return CTRL_steps;
}

public static void setCTRL_steps(ArrayList<String> cTRL_steps) {
	CTRL_steps = cTRL_steps;
}

public static double getCmd_min() {
	return cmd_min;
}

public static void setCmd_min(double cmd_min) {
	ControllerModel.cmd_min = cmd_min;
}

public static int getCtrl_curve() {
	return ctrl_curve;
}

public static void setCtrl_curve(int ctrl_curve) {
	ControllerModel.ctrl_curve = ctrl_curve;
}
 

}
