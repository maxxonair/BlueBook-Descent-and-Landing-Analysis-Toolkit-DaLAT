package Sequence;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import Model.ControlCommandSet;
import Model.ControllerModel;
import Simulator_main.Simulation;

public class Sequence {
	
    public static double TTM_max = 5.0;
    public static boolean const_isFirst =true; 
    public static double const_tzer0=0;
    private static int activeSequence=0;
    public static boolean isFirstSequence=true;
    private static boolean Sequence_RES_closed=false;
    private static ControlCommandSet controlCommandSet = new ControlCommandSet();
    
	public static ControlCommandSet getControlCommandSet(double t, double[] x, double[] V_NED_ECEF_spherical, double[] r_ECEF_spherical, ControlCommandSet controlCommandSet) {
		//-------------------------------------------------------------------------------------------------------------
		//		 WriteOut conditions at sequence to sequence hand-over: 
		//-------------------------------------------------------------------------------------------------------------
    	if(activeSequence<Simulation.getSEQUENCE_DATA_main().size()-1) {
			int trigger_type = Simulation.getSEQUENCE_DATA_main().get(activeSequence).get_trigger_end_type();
			double trigger_value = Simulation.getSEQUENCE_DATA_main().get(activeSequence).get_trigger_end_value();
			if(isFirstSequence) {
				ControllerModel.setCntr_v_init(V_NED_ECEF_spherical[0]);
				ControllerModel.setCntr_h_init(r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION());
				ControllerModel.setCntr_t_init(t);
				ControllerModel.setCntr_fpa_init(V_NED_ECEF_spherical[1]);
				SequenceWriteOut_addRow(activeSequence,Simulation.getSEQUENCE_DATA_main());
				isFirstSequence=false; 
			}
			if(trigger_type==0) {
				//System.out.println(Flight_CTRL_ThrustMagnitude.get(activeSequence).get_CTRL_TIME());
					if(ControllerModel.getFlight_CTRL_ThrustMagnitude().get(activeSequence).get_CTRL_TIME()>trigger_value) {
						activeSequence++;
						ControllerModel.setCntr_v_init(V_NED_ECEF_spherical[0]);
						ControllerModel.setCntr_h_init(r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION());
						ControllerModel.setCntr_t_init(t);
						ControllerModel.setCntr_fpa_init(V_NED_ECEF_spherical[1]);
						SequenceWriteOut_addRow(activeSequence,Simulation.getSEQUENCE_DATA_main());
					}
			} else if (trigger_type==1) {
					if( (x[2]-Simulation.getRm()-Simulation.getRef_ELEVATION())<trigger_value) {
						activeSequence++;
						ControllerModel.setCntr_v_init(V_NED_ECEF_spherical[0]);
						ControllerModel.setCntr_h_init(r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION());
						ControllerModel.setCntr_t_init(t);
						ControllerModel.setCntr_fpa_init(V_NED_ECEF_spherical[1]);
						SequenceWriteOut_addRow(activeSequence,Simulation.getSEQUENCE_DATA_main());}
			} else if (trigger_type==2) {
					if( V_NED_ECEF_spherical[0]<trigger_value) {
						activeSequence++;
						ControllerModel.setCntr_v_init(V_NED_ECEF_spherical[0]);
						ControllerModel.setCntr_h_init(r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION());
						ControllerModel.setCntr_t_init(t);
						ControllerModel.setCntr_fpa_init(V_NED_ECEF_spherical[1]);
						SequenceWriteOut_addRow(activeSequence,Simulation.getSEQUENCE_DATA_main());}
     		}
    	}
    	//
    	//                   Last Sequence reached -> write SEQU.res
    	//-------------------------------------------------------------------------------------------------------------
    	if(activeSequence ==  (Simulation.getSEQUENCE_DATA_main().size()-1) && Sequence_RES_closed==false){
			ControllerModel.setCntr_v_init(V_NED_ECEF_spherical[0]);
			ControllerModel.setCntr_h_init(r_ECEF_spherical[2]-Simulation.getRm()-Simulation.getRef_ELEVATION());
			ControllerModel.setCntr_t_init(t);
			ControllerModel.setCntr_fpa_init(x[4]);
    		System.out.println("Write: Sequence result file ");
    		try {
            String resultpath="";
            	String dir = System.getProperty("user.dir");
            	resultpath = dir + "/SEQU.res";
            PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
            for(String step: ControllerModel.getCTRL_steps()) {
                writer.println(step);
            }
            writer.close();
            Sequence_RES_closed=true; 
        } catch(Exception e) {};
    	}
    	//System.out.println("Altitude "+decf.format((x[2]-rm))+" | " + activeSequence);
    	int sequence_type_TM = Simulation.getSEQUENCE_DATA_main().get(activeSequence).get_sequence_type();
//System.out.println(sequence_type_TM);
    ControllerModel.getFlight_CTRL_ThrustMagnitude().get(activeSequence).Update_Flight_CTRL(true, x, t, Simulation.getSpaceShip().getMass(), Simulation.getSpaceShip().getPropulsion().getPrimaryPropellant(), 
    		ControllerModel.getCntr_v_init(), ControllerModel.getCntr_h_init(), t, t, Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax(), Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMin(), 
    		sequence_type_TM, Simulation.getVal_dt());
    	//Flight_CTRL_PitchCntrl.get(activeSequence).Update_Flight_CTRL(true, x, t, cntr_t_init, cntr_fpa_init, SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_curve(), val_dt);	    	
    	if(sequence_type_TM==3) {
    		
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          Flight Controller ON - Controlled Fight Sequence
		    	//-------------------------------------------------------------------------------------------------------------
		    	controlCommandSet.setPrimaryThrustThrottleCmd(ControllerModel.getFlight_CTRL_ThrustMagnitude().get(activeSequence).get_ctrl_throttle_cmd());
		    	
    	} else if (sequence_type_TM==2) { // Continuous propulsive Flight Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          TM-FC OFF | TVC-FC ON - Continuous thrust
		    	//-------------------------------------------------------------------------------------------------------------
	    		if((Simulation.getSpaceShip().getPropulsion().getPrimaryPropellant()-(Simulation.getSpaceShip().getMass()-x[6]))>0) {

	    			controlCommandSet.setPrimaryThrustThrottleCmd(1);
	    			
	    		}else { // Empty tanks
 
		    		controlCommandSet.setPrimaryThrustThrottleCmd(0); 
		    		
	    		}
	    		
    	} else if (sequence_type_TM==1) { // Coasting Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          Flight Controller OFF - Coasting - Thrust OFF
		    	//-------------------------------------------------------------------------------------------------------------
    		 
	    		controlCommandSet.setPrimaryThrustThrottleCmd(0); 
	    		
	    		
    	} else if (sequence_type_TM==4) { // Constrained continuous thrust 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//            Open Slot
		    	//-------------------------------------------------------------------------------------------------------------
    		
	    		//controlCommandSet.setPrimaryThrustThrottleCmd(actuatorSet.getPrimaryThrust_is()/Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax()); 
   
    	
    	} else if (sequence_type_TM==5) {
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//            Open Slot
		    	//-------------------------------------------------------------------------------------------------------------
		   

			//controlCommandSet.setPrimaryThrustThrottleCmd(actuatorSet.getPrimaryThrust_is()/Simulation.getSpaceShip().getPropulsion().getPrimaryThrustMax());	
   
    	
    	} else { 
    		
    		System.out.println("ERROR: Sequence type out of range");
    		}
	return controlCommandSet;
	}
	
    public static void SequenceWriteOut_addRow(int activeSequence, List<SequenceElement> SEQUENCE_DATA_main) {
		ControllerModel.getCTRL_steps().add(SEQUENCE_DATA_main.get(activeSequence).get_sequence_ID()+ " " + 
			   SEQUENCE_DATA_main.get(activeSequence).get_sequence_type()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_sequence_controller_ID()+ " "+
		       ControllerModel.getCntr_v_init()+" "+
		       ControllerModel.getCntr_h_init()+" "+
		       ControllerModel.getFlight_CTRL_ThrustMagnitude().get(activeSequence).get_ctrl_vel()+" "+
		       ControllerModel.getFlight_CTRL_ThrustMagnitude().get(activeSequence).get_ctrl_alt()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_ctrl_target_curve()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_curve()  +" "+
		       ControllerModel.getCntr_fpa_init()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_time()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_fpa()+" "
	  );
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


	public static int getActiveSequence() {
		return activeSequence;
	}

	public static void setActiveSequence(int activeSequence) {
		Sequence.activeSequence = activeSequence;
	}
	public static boolean isSequence_RES_closed() {
		return Sequence_RES_closed;
	}

	public static void setSequence_RES_closed(boolean sequence_RES_closed) {
		Sequence_RES_closed = sequence_RES_closed;
	}

	public static ControlCommandSet getControlCommandSet() {
		return controlCommandSet;
	}	
	
	
}
