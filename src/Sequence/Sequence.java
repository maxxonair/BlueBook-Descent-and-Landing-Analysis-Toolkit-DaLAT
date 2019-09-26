package Sequence;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Controller.Flight_CTRL_PitchCntrl;
import Controller.Flight_CTRL_ThrustMagnitude;
import FlightElement.SpaceShip;
import Simulator_main.Simulation;

public class Sequence {
	
    private static double ctrl_vel =0;			// Active Flight Controller target velocity [m/s]
    private static double ctrl_alt = 0 ; 			// Active Flight Controller target altitude [m]
    private static double cntr_h_init=0;
    private static double cntr_v_init=0;
    private static double cntr_t_init=0;
    private static double cntr_fpa_init=0;
    private static boolean Sequence_RES_closed=false;
    private static List<Flight_CTRL_ThrustMagnitude> Flight_CTRL_ThrustMagnitude = new ArrayList<Flight_CTRL_ThrustMagnitude>(); 
    private static List<Flight_CTRL_PitchCntrl> Flight_CTRL_PitchCntrl 			 = new ArrayList<Flight_CTRL_PitchCntrl>(); 
    private static ArrayList<String> CTRL_steps 								 = new ArrayList<String>();
    private static int ctrl_curve;
    public static double TTM_max = 5.0;
    public static boolean const_isFirst =true; 
    public static double const_tzer0=0;
    private static int activeSequence=0;
    public static boolean isFirstSequence=true;
    public static double cmd_min = 0;
    
    private static ControlElements controlElements = new ControlElements();
    
	public static void sequenceManager(double t, double[] x, double[] V_NED_ECEF_spherical, double[] r_ECEF_spherical ) {
		//-------------------------------------------------------------------------------------------------------------
		//		 WriteOut conditions at sequence to sequence hand-over: 
		//-------------------------------------------------------------------------------------------------------------
    	if(activeSequence<Simulation.getSEQUENCE_DATA_main().size()-1) {
			int trigger_type = Simulation.getSEQUENCE_DATA_main().get(activeSequence).get_trigger_end_type();
			double trigger_value = Simulation.getSEQUENCE_DATA_main().get(activeSequence).get_trigger_end_value();
			if(isFirstSequence) {
				cntr_v_init = V_NED_ECEF_spherical[0];
				cntr_h_init = r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION();
				cntr_t_init = t;
				cntr_fpa_init = V_NED_ECEF_spherical[1];
				SequenceWriteOut_addRow(activeSequence,Simulation.getSEQUENCE_DATA_main());
				isFirstSequence=false; 
			}
			if(trigger_type==0) {
				System.out.println(Flight_CTRL_ThrustMagnitude.get(activeSequence).get_CTRL_TIME());
					if(Flight_CTRL_ThrustMagnitude.get(activeSequence).get_CTRL_TIME()>trigger_value) {
						activeSequence++;
						cntr_v_init = V_NED_ECEF_spherical[0];
						cntr_h_init = r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION();
						cntr_t_init = t;
						cntr_fpa_init = V_NED_ECEF_spherical[1];
						SequenceWriteOut_addRow(activeSequence,Simulation.getSEQUENCE_DATA_main());
					}
			} else if (trigger_type==1) {
					if( (x[2]-Simulation.getRm()-Simulation.getRef_ELEVATION())<trigger_value) {
						activeSequence++;
						cntr_v_init = V_NED_ECEF_spherical[0];
						cntr_h_init = r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION();
						cntr_t_init = t;
						cntr_fpa_init = V_NED_ECEF_spherical[1];
						SequenceWriteOut_addRow(activeSequence,Simulation.getSEQUENCE_DATA_main());}
			} else if (trigger_type==2) {
					if( V_NED_ECEF_spherical[0]<trigger_value) {
						activeSequence++;
						cntr_v_init = V_NED_ECEF_spherical[0];
						cntr_h_init = r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION();
						cntr_t_init = t;
						cntr_fpa_init = V_NED_ECEF_spherical[1];
						SequenceWriteOut_addRow(activeSequence,Simulation.getSEQUENCE_DATA_main());}
     		}
    	}
    	//
    	//                   Last Sequence reached -> write SEQU.res
    	//-------------------------------------------------------------------------------------------------------------
    	if(activeSequence ==  (Simulation.getSEQUENCE_DATA_main().size()-1) && Sequence_RES_closed==false){
			cntr_v_init = V_NED_ECEF_spherical[0];
			cntr_h_init = r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION();
			cntr_t_init = t;
			cntr_fpa_init = x[4];
    		System.out.println("Write: Sequence result file ");
    		try {
            String resultpath="";
            	String dir = System.getProperty("user.dir");
            	resultpath = dir + "/SEQU.res";
            PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
            for(String step: CTRL_steps) {
                writer.println(step);
            }
            writer.close();
            Sequence_RES_closed=true; 
        } catch(Exception e) {};
    	}
    	//System.out.println("Altitude "+decf.format((x[2]-rm))+" | " + activeSequence);
    	int sequence_type_TM = Simulation.getSEQUENCE_DATA_main().get(activeSequence).get_sequence_type();
//System.out.println(sequence_type_TM);
    Flight_CTRL_ThrustMagnitude.get(activeSequence).Update_Flight_CTRL(true, x, Simulation.getSpaceShip().getMass(), Simulation.getSpaceShip().getPropulsion().getPrimaryPropellant(), 
    		cntr_v_init, cntr_h_init, cntr_t_init, t, t, Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax(), Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMin(), 
    		sequence_type_TM, Simulation.getVal_dt());
    	//Flight_CTRL_PitchCntrl.get(activeSequence).Update_Flight_CTRL(true, x, t, cntr_t_init, cntr_fpa_init, SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_curve(), val_dt);	    	
    	if(sequence_type_TM==3) { // Controlled Flight Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          Flight Controller ON - Controlled Fight Sequence
		    	//-------------------------------------------------------------------------------------------------------------
		    	controlElements.setPrimaryThrust_is(Flight_CTRL_ThrustMagnitude.get(activeSequence).get_thrust_cmd());
		    	controlElements.setPrimaryThrustThrottleCmd(Flight_CTRL_ThrustMagnitude.get(activeSequence).get_ctrl_throttle_cmd());
		    	
    	} else if (sequence_type_TM==2) { // Continuous propulsive Flight Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          TM-FC OFF | TVC-FC ON - Continuous thrust
		    	//-------------------------------------------------------------------------------------------------------------
System.out.println("Sequence Thrust reached");
	    		if((Simulation.getSpaceShip().getPropulsion().getPrimaryPropellant()-(Simulation.getSpaceShip().getMass()-x[6]))>0) {
	    			//System.out.println((m_propellant_init-(M0-x[6])));
	    			controlElements.setPrimaryThrust_is(Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax()); 
	    			controlElements.setPrimaryThrustThrottleCmd(1);
	    		}else { // Empty tanks
		    		controlElements.setPrimaryThrust_is(0);  
		    		controlElements.setPrimaryThrustThrottleCmd(0); 
	    		}
	    		
    	} else if (sequence_type_TM==1) { // Coasting Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          Flight Controller OFF - Coasting - Thrust OFF
		    	//-------------------------------------------------------------------------------------------------------------
    		
    		
	    		controlElements.setPrimaryThrust_is(0);  
	    		controlElements.setPrimaryThrustThrottleCmd(0); 
	    		
	    		
    	} else if (sequence_type_TM==4) { // Constrained continuous thrust 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                Flight Controller OFF - Uncontrolled/Constrained, continuous thrust TTM constraint
		    	//-------------------------------------------------------------------------------------------------------------
    		
    		
    		    if(const_isFirst){const_tzer0=t;}
	    		if((TTM_max * x[6])>Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax()) {
	    			controlElements.setPrimaryThrust_is(Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax());
	    		} else {
	    			controlElements.setPrimaryThrust_is(TTM_max * x[6]);
	    		}
	    			controlElements.setPrimaryThrustThrottleCmd(controlElements.getPrimaryThrust_is()/Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax()); 
   
    	
    	} else if (sequence_type_TM==5) {
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//            Flight Controller OFF - Uncontrolled/Constrained, continuous thrust Thrust vector turn
		    	//-------------------------------------------------------------------------------------------------------------
		   
    		
    		if(const_isFirst){const_tzer0=t;}
	    	
		    controlElements.setPrimaryThrust_is(Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax());
			controlElements.setPrimaryThrustThrottleCmd(controlElements.getPrimaryThrust_is()/Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax());	
   
    	
    	} else { 
    		
    		System.out.println("ERROR: Sequence type out of range");}
	
	}
	
    public static void SequenceWriteOut_addRow(int activeSequence, List<SequenceElement> SEQUENCE_DATA_main) {
		CTRL_steps.add(SEQUENCE_DATA_main.get(activeSequence).get_sequence_ID()+ " " + 
			   SEQUENCE_DATA_main.get(activeSequence).get_sequence_type()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_sequence_controller_ID()+ " "+
		       cntr_v_init+" "+
		       cntr_h_init+" "+
		       Flight_CTRL_ThrustMagnitude.get(activeSequence).get_ctrl_vel()+" "+
		       Flight_CTRL_ThrustMagnitude.get(activeSequence).get_ctrl_alt()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_ctrl_target_curve()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_curve()  +" "+
		       cntr_fpa_init+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_time()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_fpa()+" "
	  );
    }
    
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
		   if (Flight_CTRL_ThrustMagnitude.size()==0){ Sequence.Flight_CTRL_ThrustMagnitude.add(NewElement); 
		   } else {Sequence.Flight_CTRL_ThrustMagnitude.add(NewElement); } 
	   }
	public static void UPDATE_FlightController_PitchControl(Flight_CTRL_PitchCntrl NewElement){	   
		   if (Flight_CTRL_PitchCntrl.size()==0){ Sequence.Flight_CTRL_PitchCntrl.add(NewElement); 
		   } else {Sequence.Flight_CTRL_PitchCntrl.add(NewElement); } 
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
    		double m = (spaceShip.getPropulsion().getPrimaryISPMax() - spaceShip.getPropulsion().getPrimaryISPMin())/(1 - cmd_min);
    		double n = spaceShip.getPropulsion().getPrimaryISPMax() - m ; 
    		IspOut = m * Throttle_CMD + n; 
    	}
    	return IspOut; 
    }
    
	public static ControlElements getControlElements() {
		return controlElements;
	}

	public static double getCtrl_vel() {
		return ctrl_vel;
	}

	public static void setCtrl_vel(double ctrl_vel) {
		Sequence.ctrl_vel = ctrl_vel;
	}

	public static double getCtrl_alt() {
		return ctrl_alt;
	}

	public static void setCtrl_alt(double ctrl_alt) {
		Sequence.ctrl_alt = ctrl_alt;
	}

	public static double getCntr_h_init() {
		return cntr_h_init;
	}

	public static void setCntr_h_init(double cntr_h_init) {
		Sequence.cntr_h_init = cntr_h_init;
	}

	public static double getCntr_v_init() {
		return cntr_v_init;
	}

	public static void setCntr_v_init(double cntr_v_init) {
		Sequence.cntr_v_init = cntr_v_init;
	}

	public static double getCntr_t_init() {
		return cntr_t_init;
	}

	public static void setCntr_t_init(double cntr_t_init) {
		Sequence.cntr_t_init = cntr_t_init;
	}

	public static double getCntr_fpa_init() {
		return cntr_fpa_init;
	}

	public static void setCntr_fpa_init(double cntr_fpa_init) {
		Sequence.cntr_fpa_init = cntr_fpa_init;
	}

	public static boolean isSequence_RES_closed() {
		return Sequence_RES_closed;
	}

	public static void setSequence_RES_closed(boolean sequence_RES_closed) {
		Sequence_RES_closed = sequence_RES_closed;
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

	public static int getCtrl_curve() {
		return ctrl_curve;
	}

	public static void setCtrl_curve(int ctrl_curve) {
		Sequence.ctrl_curve = ctrl_curve;
	}

	public static double getTTM_max() {
		return TTM_max;
	}

	public static void setTTM_max(double tTM_max) {
		TTM_max = tTM_max;
	}

	public static boolean isConst_isFirst() {
		return const_isFirst;
	}

	public static void setConst_isFirst(boolean const_isFirst) {
		Sequence.const_isFirst = const_isFirst;
	}

	public static double getConst_tzer0() {
		return const_tzer0;
	}

	public static void setConst_tzer0(double const_tzer0) {
		Sequence.const_tzer0 = const_tzer0;
	}

	public static void setControlElements(ControlElements controlElements) {
		Sequence.controlElements = controlElements;
	}

	public static int getActiveSequence() {
		return activeSequence;
	}

	public static void setActiveSequence(int activeSequence) {
		Sequence.activeSequence = activeSequence;
	}
	
	
	
}
