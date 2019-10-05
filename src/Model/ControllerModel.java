package Model;

import java.util.ArrayList;
import java.util.List;

import Controller.Flight_CTRL_PitchCntrl;
import Controller.Flight_CTRL_ThrustMagnitude;
import Sequence.SequenceElement;
import Simulator_main.Simulation;

public class ControllerModel {
	
    private static double ctrl_vel =0;			// Active Flight Controller target velocity [m/s]
    private static double ctrl_alt = 0 ; 			// Active Flight Controller target altitude [m]
    private static  double cntr_h_init=0;
    private static double cntr_v_init=0;
    private static double cntr_t_init=0;
    private static double cntr_fpa_init=0;
    private static List<Flight_CTRL_ThrustMagnitude> Flight_CTRL_ThrustMagnitude = new ArrayList<Flight_CTRL_ThrustMagnitude>(); 
    private static List<Flight_CTRL_PitchCntrl> Flight_CTRL_PitchCntrl 			 = new ArrayList<Flight_CTRL_PitchCntrl>(); 
    private static ArrayList<String> CTRL_steps 								 = new ArrayList<String>();
    public static double cmd_min = 0;
    private static int ctrl_curve;
    
    
	public static void initializeFlightController(double[]x, List<SequenceElement> SEQUENCE_DATA_main) {
		for(int i=0;i<SEQUENCE_DATA_main.size();i++) {
			int ctrl_ID = SEQUENCE_DATA_main.get(i).get_sequence_controller_ID()-1;
			 ctrl_vel = SEQUENCE_DATA_main.get(i).get_ctrl_target_vel();
			 ctrl_alt = SEQUENCE_DATA_main.get(i).get_ctrl_target_alt();
			 double ctrl_fpa = SEQUENCE_DATA_main.get(i).get_TVC_ctrl_target_fpa();
			 double ctrl_tend = SEQUENCE_DATA_main.get(i).get_TVC_ctrl_target_time();
			 int TVC_ctrl_ID = SEQUENCE_DATA_main.get(i).get_sequence_TVCController_ID()-1;
			// -> Create new Flight controller 
			 Flight_CTRL_ThrustMagnitude NewFlightController_ThrustMagnitude = new Flight_CTRL_ThrustMagnitude(ctrl_ID, 
					 true, x, 0,  Simulation.getSpaceShip().getPropulsion().getPrimaryPropellant(),  cntr_v_init,  cntr_h_init,  -1,   ctrl_vel, ctrl_alt,  
					 Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax(),  
					 Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMin(),  0,  0,  ctrl_curve,  
					 Simulation.getVal_dt(),0,0,0,0,0, Simulation.getRm(), Simulation.getRef_ELEVATION());
			UPDATE_FlightController_ThrustMagnitude(NewFlightController_ThrustMagnitude);
			
			Flight_CTRL_PitchCntrl NewFlightController_PitchCntrl = new Flight_CTRL_PitchCntrl( TVC_ctrl_ID, 
					true, -1, 0, ctrl_tend, ctrl_fpa, Simulation.getRm() , Simulation.getRef_ELEVATION());
			UPDATE_FlightController_PitchControl(NewFlightController_PitchCntrl);
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

public static double getCtrl_vel() {
	return ctrl_vel;
}

public static void setCtrl_vel(double ctrl_vel) {
	ControllerModel.ctrl_vel = ctrl_vel;
}

public static double getCtrl_alt() {
	return ctrl_alt;
}

public static void setCtrl_alt(double ctrl_alt) {
	ControllerModel.ctrl_alt = ctrl_alt;
}

public static double getCntr_h_init() {
	return cntr_h_init;
}

public static void setCntr_h_init(double cntr_h_init) {
	ControllerModel.cntr_h_init = cntr_h_init;
}

public static double getCntr_v_init() {
	return cntr_v_init;
}

public static void setCntr_v_init(double cntr_v_init) {
	ControllerModel.cntr_v_init = cntr_v_init;
}

public static double getCntr_t_init() {
	return cntr_t_init;
}

public static void setCntr_t_init(double cntr_t_init) {
	ControllerModel.cntr_t_init = cntr_t_init;
}

public static double getCntr_fpa_init() {
	return cntr_fpa_init;
}

public static void setCntr_fpa_init(double cntr_fpa_init) {
	ControllerModel.cntr_fpa_init = cntr_fpa_init;
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
