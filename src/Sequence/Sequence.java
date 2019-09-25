package Sequence;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import Simulator_main.Simulation;

public class Sequence {
	

	public static void SEQUENCE_MANAGER(double t, double[] x, boolean isFirstSequence, 
			int activeSequence, List<SequenceElement> SEQUENCE_DATA_main, double[] V_NED_ECEF_spherical, 
			double[] r_ECEF_spherical) {
		//-------------------------------------------------------------------------------------------------------------
		//		 WriteOut conditions at sequence to sequence hand-over: 
		//-------------------------------------------------------------------------------------------------------------
    	if(activeSequence<SEQUENCE_DATA_main.size()-1) {
			int trigger_type = SEQUENCE_DATA_main.get(activeSequence).get_trigger_end_type();
			double trigger_value = SEQUENCE_DATA_main.get(activeSequence).get_trigger_end_value();
			if(isFirstSequence) {
				cntr_v_init = V_NED_ECEF_spherical[0];
				cntr_h_init = r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION();
				cntr_t_init = t;
				cntr_fpa_init = V_NED_ECEF_spherical[1];
				SequenceWriteOut_addRow();
				isFirstSequence=false; 
			}
			if(trigger_type==0) {
					if(Flight_CTRL_ThrustMagnitude.get(activeSequence).get_CTRL_TIME()>trigger_value) {
						activeSequence++;
						cntr_v_init = V_NED_ECEF_spherical[0];
						cntr_h_init = r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION();
						cntr_t_init = t;
						cntr_fpa_init = V_NED_ECEF_spherical[1];
						SequenceWriteOut_addRow(activeSequence,SEQUENCE_DATA_main);
					}
			} else if (trigger_type==1) {
					if( (x[2]-Simulation.getRm()-Simulation.getRef_ELEVATION())<trigger_value) {
						activeSequence++;
						cntr_v_init = V_NED_ECEF_spherical[0];
						cntr_h_init = r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION();
						cntr_t_init = t;
						cntr_fpa_init = V_NED_ECEF_spherical[1];
						SequenceWriteOut_addRow(activeSequence,SEQUENCE_DATA_main);}
			} else if (trigger_type==2) {
					if( V_NED_ECEF_spherical[0]<trigger_value) {
						activeSequence++;
						cntr_v_init = V_NED_ECEF_spherical[0];
						cntr_h_init = r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION();
						cntr_t_init = t;
						cntr_fpa_init = V_NED_ECEF_spherical[1];
						SequenceWriteOut_addRow(activeSequence,SEQUENCE_DATA_main);}
     		}
    	}
    	//
    	//                   Last Sequence reached -> write SEQU.res
    	//-------------------------------------------------------------------------------------------------------------
    	if(activeSequence ==  (SEQUENCE_DATA_main.size()-1) && Sequence_RES_closed==false){
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
    	int sequence_type_TM = SEQUENCE_DATA_main.get(activeSequence).get_sequence_type();
 
    	Flight_CTRL_ThrustMagnitude.get(activeSequence).Update_Flight_CTRL( true, x, M0, m_propellant_init,  cntr_v_init,  cntr_h_init,  t,  SEQUENCE_DATA_main.get(activeSequence).get_ctrl_target_vel(),SEQUENCE_DATA_main.get(activeSequence).get_ctrl_target_alt(),  Thrust_max,  Thrust_min,  SEQUENCE_DATA_main.get(activeSequence).get_ctrl_target_curve(),  val_dt) ;
    	//Flight_CTRL_PitchCntrl.get(activeSequence).Update_Flight_CTRL(true, x, t, cntr_t_init, cntr_fpa_init, SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_curve(), val_dt);	    	
    	if(sequence_type_TM==3) { // Controlled Flight Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          Flight Controller ON - Controlled Fight Sequence
		    	//-------------------------------------------------------------------------------------------------------------
		    	if (ctrl_callout) {System.out.println("Altitude "+decf.format((x[2]-rm))+" | Controller " + Flight_CTRL_ThrustMagnitude.get(activeSequence).get_ctrl_ID() +" set ON");}
		    	Thrust_is        = Flight_CTRL_ThrustMagnitude.get(activeSequence).get_thrust_cmd();
		    	Throttle_CMD  = Flight_CTRL_ThrustMagnitude.get(activeSequence).get_ctrl_throttle_cmd();
		    	
		    	elevationangle = Flight_CTRL_PitchCntrl.get(activeSequence).get_TVC_cmd();
    	} else if (sequence_type_TM==2) { // Continuous propulsive Flight Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          TM-FC OFF | TVC-FC ON - Continuous thrust
		    	//-------------------------------------------------------------------------------------------------------------
    		elevationangle = 0.0;
    		elevationangle = Flight_CTRL_PitchCntrl.get(activeSequence).get_TVC_cmd();
	    		if((m_propellant_init-(M0-x[6]))>0) {
	    			//System.out.println((m_propellant_init-(M0-x[6])));
	    			Thrust_is = Thrust_max; 
	    			Throttle_CMD = 1; 
	    		}else { // Empty tanks
	    			Thrust_is = 0; 
	        		Throttle_CMD = 0; 
	    		}
	    		
    	} else if (sequence_type_TM==1) { // Coasting Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          Flight Controller OFF - Coasting - Thrust OFF
		    	//-------------------------------------------------------------------------------------------------------------
	    		Thrust_is = 0; 
	    		Throttle_CMD = 0;
    	} else if (sequence_type_TM==4) { // Constrained continuous thrust 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                Flight Controller OFF - Uncontrolled/Constrained, continuous thrust TTM constraint
		    	//-------------------------------------------------------------------------------------------------------------
    		    if(const_isFirst){const_tzer0=t;}
	    		if((TTM_max * x[6])>Thrust_max) {
	    			Thrust_is = Thrust_max;
	    		} else {
	    			Thrust_is = TTM_max * x[6]; 
	    		}
	    			Throttle_CMD = Thrust_is/Thrust_max;
    	} else if (sequence_type_TM==5) {
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//            Flight Controller OFF - Uncontrolled/Constrained, continuous thrust Thrust vector turn
		    	//-------------------------------------------------------------------------------------------------------------
		    if(const_isFirst){const_tzer0=t;}
	    	
		    Thrust_is = Thrust_max;
			Throttle_CMD = Thrust_is/Thrust_max;	
    	} else { System.out.println("ERROR: Sequence type out of range");}
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
	
}
