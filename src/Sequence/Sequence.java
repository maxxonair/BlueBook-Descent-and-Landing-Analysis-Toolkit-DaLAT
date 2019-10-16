package Sequence;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import FlightElement.SpaceShip;
import Model.ControllerModel;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.ErrorSet;
import Simulator_main.CurrentDataSet;

public class Sequence {
	

    public static boolean const_isFirst =true; 
    public static double const_tzer0=0;
    private static int activeSequence=0;
    public static boolean isFirstSequence=true;
    private static boolean Sequence_RES_closed=false;
    private static ControlCommandSet controlCommandSet = new ControlCommandSet();
    private static double pitchMinusOne=0;
  //  private static double rollMinusOne=0;
    
	public static ControlCommandSet getControlCommandSet(CurrentDataSet currentDataSet, SpaceShip spaceShip, ErrorSet errorSet) {
		ControlCommandSet controlCommandSet = new ControlCommandSet();
		//-------------------------------------------------------------------------------------------------------------
		//		 WriteOut conditions at sequence to sequence hand-over: 
		//-------------------------------------------------------------------------------------------------------------
		controlCommandSet.setSequenceID(activeSequence);
    	if(activeSequence<currentDataSet.getSEQUENCE_DATA_main().size()-1) {
			int trigger_type = currentDataSet.getSEQUENCE_DATA_main().get(activeSequence).get_trigger_end_type();
			double trigger_value =currentDataSet.getSEQUENCE_DATA_main().get(activeSequence).get_trigger_end_value();
			if(isFirstSequence) {
				controlCommandSet.setCntr_v_init(currentDataSet.getV_NED_ECEF_spherical()[0]);
				controlCommandSet.setCntr_h_init(currentDataSet.getR_ECEF_spherical()[2]-currentDataSet.getRM()-currentDataSet.getLocalElevation());
				controlCommandSet.setCntr_t_init(currentDataSet.gettIS());
				controlCommandSet.setCntr_fpa_init(currentDataSet.getV_NED_ECEF_spherical()[1]);
				SequenceWriteOut_addRow(activeSequence,currentDataSet.getSEQUENCE_DATA_main());
				isFirstSequence=false; 
			}
			if(trigger_type==0) {
				//System.out.println(Flight_CTRL_ThrustMagnitude.get(activeSequence).get_CTRL_TIME());
					if(ControllerModel.getFlight_CTRL_ThrustMagnitude().get(activeSequence).get_CTRL_TIME()>trigger_value) {
						activeSequence++;
						controlCommandSet.setCntr_v_init(currentDataSet.getV_NED_ECEF_spherical()[0]);
						controlCommandSet.setCntr_h_init(currentDataSet.getR_ECEF_spherical()[2]-currentDataSet.getRM()-currentDataSet.getLocalElevation());
						controlCommandSet.setCntr_t_init(currentDataSet.gettIS());
						controlCommandSet.setCntr_fpa_init(currentDataSet.getV_NED_ECEF_spherical()[1]);
						SequenceWriteOut_addRow(activeSequence,currentDataSet.getSEQUENCE_DATA_main());
					}
			} else if (trigger_type==1) {
					if( (currentDataSet.getxIS()[2]-currentDataSet.getRM()-currentDataSet.getLocalElevation())<trigger_value) {
						activeSequence++;
						controlCommandSet.setCntr_v_init(currentDataSet.getV_NED_ECEF_spherical()[0]);
						controlCommandSet.setCntr_h_init(currentDataSet.getR_ECEF_spherical()[2]-currentDataSet.getRM()-currentDataSet.getLocalElevation());
						controlCommandSet.setCntr_t_init(currentDataSet.gettIS());
						controlCommandSet.setCntr_fpa_init(currentDataSet.getV_NED_ECEF_spherical()[1]);
						SequenceWriteOut_addRow(activeSequence,currentDataSet.getSEQUENCE_DATA_main());}
			} else if (trigger_type==2) {
					if( currentDataSet.getV_NED_ECEF_spherical()[0]<trigger_value) {
						activeSequence++;
						controlCommandSet.setCntr_v_init(currentDataSet.getV_NED_ECEF_spherical()[0]);
						controlCommandSet.setCntr_h_init(currentDataSet.getR_ECEF_spherical()[2]-currentDataSet.getRM()-currentDataSet.getLocalElevation());
						controlCommandSet.setCntr_t_init(currentDataSet.gettIS());
						controlCommandSet.setCntr_fpa_init(currentDataSet.getV_NED_ECEF_spherical()[1]);
						SequenceWriteOut_addRow(activeSequence,currentDataSet.getSEQUENCE_DATA_main());}
     		}
    	}
    	//
    	//                   Last Sequence reached -> write SEQU.res
    	//-------------------------------------------------------------------------------------------------------------
    	if(activeSequence ==  (currentDataSet.getSEQUENCE_DATA_main().size()-1) && Sequence_RES_closed==false){
    		controlCommandSet.setCntr_v_init(currentDataSet.getV_NED_ECEF_spherical()[0]);
    		controlCommandSet.setCntr_h_init(currentDataSet.getR_ECEF_spherical()[2]-currentDataSet.getRM()-currentDataSet.getLocalElevation());
    		controlCommandSet.setCntr_t_init(currentDataSet.gettIS());
    		controlCommandSet.setCntr_fpa_init(currentDataSet.getxIS()[4]);
    		//System.out.println("Write: Sequence result file ");
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
    	int sequence_type_TM = currentDataSet.getSEQUENCE_DATA_main().get(activeSequence).get_sequence_type();
//--------------------------------------------------------------------------------------------------------------- 
// 						update flight controllers
//--------------------------------------------------------------------------------------------------------------- 
    ControllerModel.getFlight_CTRL_ThrustMagnitude().get(activeSequence).Update_Flight_CTRL(true, currentDataSet.getxIS(), 
    		currentDataSet.gettIS(), spaceShip.getMass(), spaceShip.getPropulsion().getPrimaryPropellant(), 
    		controlCommandSet.getCntr_v_init(), controlCommandSet.getCntr_h_init(), currentDataSet.gettIS(), currentDataSet.gettIS(), 
    		spaceShip.getPropulsion().getPrimaryThrustMax(), spaceShip.getPropulsion().getPrimaryThrustMin(), 
    		sequence_type_TM, currentDataSet.getValDt());
    
    ControllerModel.getFlight_CTRL_PitchCntrl().get(activeSequence).Update_Flight_CTRL(true, currentDataSet,controlCommandSet.getCntr_v_init(), 
    		controlCommandSet.getCntr_h_init(), 0,  pitchMinusOne,  controlCommandSet.getPrimaryThrustThrottleCmd()*spaceShip.getPropulsion().getPrimaryThrustMax(),  
    		errorSet.isPrimaryPropulsionTotalLoss());
    
    ControllerModel.getFlight_CTRL_RollCntrl().get(activeSequence).Update_Flight_CTRL(true, currentDataSet,controlCommandSet.getCntr_v_init(), 
    		controlCommandSet.getCntr_h_init(), 0,  pitchMinusOne,  controlCommandSet.getPrimaryThrustThrottleCmd()*spaceShip.getPropulsion().getPrimaryThrustMax(),  
    		errorSet.isPrimaryPropulsionTotalLoss());
    
//--------------------------------------------------------------------------------------------------------------- 	
    	if(sequence_type_TM==3) {		
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//             Flight Controller ON - Controlled Fight Sequence THRUST Control
		    	//-------------------------------------------------------------------------------------------------------------
		    	controlCommandSet.setPrimaryThrustThrottleCmd(ControllerModel.getFlight_CTRL_ThrustMagnitude().get(activeSequence).get_ctrl_throttle_cmd());
		    	
    	} else if (sequence_type_TM==2) { // Continuous propulsive Flight Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          TM-FC OFF | TVC-FC ON - Continuous thrust
		    	//-------------------------------------------------------------------------------------------------------------
	    		if((spaceShip.getPropulsion().getPrimaryPropellant()-(spaceShip.getMass()-currentDataSet.getxIS()[6]))>0) {

	    			controlCommandSet.setPrimaryThrustThrottleCmd(1);
	    			
	    		}else { // Empty tanks
 
		    		controlCommandSet.setPrimaryThrustThrottleCmd(0); 
		    		
	    		}
	    		
    	} else if (sequence_type_TM==1) { // Coasting Sequence 
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//                          Flight Controller OFF - Coasting - Thrust OFF
		    	//-------------------------------------------------------------------------------------------------------------
    		 
	    		controlCommandSet.setPrimaryThrustThrottleCmd(0); 
	    		
	    		
    	} else if (sequence_type_TM==4) { // Pitch Control
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//            Flight Controller ON - PITCH control 
		    	//-------------------------------------------------------------------------------------------------------------
    			pitchMinusOne = ControllerModel.getFlight_CTRL_PitchCntrl().get(activeSequence).get_maintain_horizontal_Pitch_cmd();    
    			controlCommandSet.setMomentumRCS_Y_cmd(pitchMinusOne);
    		    //	System.out.println(pitchMinusOne);
    
    	} else if (sequence_type_TM==5) {
		    	//-------------------------------------------------------------------------------------------------------------	
		    	//           Flight Controller ON - Roll control
		    	//-------------------------------------------------------------------------------------------------------------
			/*
    			double  CTRL_ERROR = currentDataSet.getEulerAngle()[0][0];
			System.out.println(CTRL_ERROR+"|"+currentDataSet.getValDt());
			double commandOut = PID_01.PID_001(CTRL_ERROR,currentDataSet.getValDt(), 0.1, 0.0001, 0.5, 1, -1);
			System.out.println(commandOut);
			if(!Double.isNaN(commandOut)) {
				controlCommandSet.setMomentumRCS_X_cmd(-commandOut);	
			} else {
				controlCommandSet.setMomentumRCS_X_cmd(0);
			}
			*/
    			double commandOut = ControllerModel.getFlight_CTRL_RollCntrl().get(activeSequence).get_maintain_horizontal_Bank_cmd();
			//System.out.println(commandOut);
    			if(!Double.isNaN(commandOut)) {
				controlCommandSet.setMomentumRCS_X_cmd(-commandOut);	
			} else {
				controlCommandSet.setMomentumRCS_X_cmd(0);
			}
    	
    	} else if ( sequence_type_TM==6){
	    	//-------------------------------------------------------------------------------------------------------------	
	    	//          		 Parachute Deployment
	    	//-------------------------------------------------------------------------------------------------------------
    		controlCommandSet.setParachuteDeployed(true);
    		//System.out.println("parachute deployed");
    	} else if ( sequence_type_TM==7){
	    	//-------------------------------------------------------------------------------------------------------------	
	    	//          		 Parachute Ejection
	    	//-------------------------------------------------------------------------------------------------------------
    		controlCommandSet.setParachuteEjectCMD(true);
    		//System.out.println("parachute ejected");
    	} else {
    		
    		System.out.println("ERROR: Sequence type out of range: "+sequence_type_TM);
    		}
	return controlCommandSet;
	}
	
    public static void SequenceWriteOut_addRow(int activeSequence, List<SequenceElement> SEQUENCE_DATA_main) {
		ControllerModel.getCTRL_steps().add(SEQUENCE_DATA_main.get(activeSequence).get_sequence_ID()+ " " + 
			   SEQUENCE_DATA_main.get(activeSequence).get_sequence_type()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_sequence_controller_ID()+ " "+
		       controlCommandSet.getCntr_v_init()+" "+
		       controlCommandSet.getCntr_h_init()+" "+
		       controlCommandSet.getCtrl_vel()+" "+
		       controlCommandSet.getCtrl_alt()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_ctrl_target_curve()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_curve()  +" "+
		       controlCommandSet.getCntr_fpa_init()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_time()+" "+
		       SEQUENCE_DATA_main.get(activeSequence).get_TVC_ctrl_target_fpa()+" "
	  );
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
