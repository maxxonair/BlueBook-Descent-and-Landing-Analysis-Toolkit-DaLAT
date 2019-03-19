package Simulator_main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Sequence.SequenceElement;
import Simulator_main.EDL_3DOF;

public class Launch_Simulation implements ActionListener{
	
   	static double[] x_init =new double[20];	
   	public static String INPUT_FILE;
    public static double PI    = 3.14159265359;                 // PI                                       [-]
    public static double kB    = 1.380650424e-23;               // Boltzmann constant                         [SI]    
    public static double G     = 1.48808E-34;
    public static int TARGET; 
	static double deg = PI/180.0; 		//Convert degrees to radians
	static double rad = 180/PI; 		//Convert radians to degrees
	
   	//System.out.println(INPUT_FILE);
	
	public static void UPDATE_SequenceElements(SequenceElement NewElement, List<SequenceElement> SEQUENCE_DATA){	   
		   if (SEQUENCE_DATA.size()==0){
				  SEQUENCE_DATA.add(NewElement); 
		   } else {
			boolean element_exist = false   ;
			  for(int i=0; i<SEQUENCE_DATA.size(); i++){
				  int ID_LIST    = SEQUENCE_DATA.get(i).get_sequence_ID();
				  int ID_ELEMENT = NewElement.get_sequence_ID();
						  if (ID_LIST == ID_ELEMENT){
							  // item exists -> Update
							  SEQUENCE_DATA.get(i).Update(NewElement.get_sequence_ID(),NewElement.get_trigger_end_type(), NewElement.get_trigger_end_value(),NewElement.get_sequence_type(),NewElement.get_sequence_controller_ID(), NewElement.get_ctrl_target_vel(), NewElement.get_ctrl_target_alt(), NewElement.get_ctrl_target_curve(),NewElement.get_sequence_TVCController_ID(),NewElement.get_TVC_ctrl_target_time(),NewElement.get_TVC_ctrl_target_fpa(),NewElement.get_TVC_ctrl_target_curve());
							  element_exist = true;
						  } 
			  }
			if (element_exist == false ){
				  // New item -> add to list  
				SEQUENCE_DATA.add(NewElement);
			}	  
		   } 
	   }
	
    public static boolean READ_INPUT() throws IOException{
   String dir = System.getProperty("user.dir");
       	   	 INPUT_FILE = dir+"/INP/init.inp";
    	double InitialState = 0;
    	boolean read_state = false;
	    FileInputStream fstream = null;
	    try{
	    	fstream = new FileInputStream(INPUT_FILE);
	    } catch(IOException eIO) { System.out.println(eIO);}
        DataInputStream in = new DataInputStream(fstream);
        @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int k = 0;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String[] tokens = strLine.split(" ");
        	InitialState = Double.parseDouble(tokens[0]);
        	x_init[k]= InitialState;
        	//System.out.println("" +k+"   "+InitialState);
        	k++;
        }
        fstream.close();
        in.close();
        br.close();
        read_state = true;
        } catch(NullPointerException eNPE) { System.out.println(eNPE);}
        return read_state;
    }
    
    public static List<SequenceElement> READ_SEQUENCE() throws IOException{	
    	 List<SequenceElement> SEQUENCE_DATA = new ArrayList<SequenceElement>(); 
    	 String dir = System.getProperty("user.dir");
       	   	 INPUT_FILE = dir+"/INP/sequence_1.inp";
		BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
        String strLine;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String[] tokens = strLine.split(" ");
        	SequenceElement newSequenceElement = new SequenceElement( 0, 0,  0,  0,  0, 0, 0, 0, 0, 0, 0, 0);
        	int sequence_ID 					= Integer.parseInt(tokens[0]);
        	int trigger_end_type 				= Integer.parseInt(tokens[1]);
        	double trigger_end_value 			= Double.parseDouble(tokens[2]);
        	int sequence_type		 			= Integer.parseInt(tokens[3]);
        	int sequence_controller_ID 			= Integer.parseInt(tokens[4]);
        	double ctrl_target_vel      		= Double.parseDouble(tokens[5]);
        	double ctrl_target_alt 				= Double.parseDouble(tokens[6]);
        	int ctrl_target_curve    			= Integer.parseInt(tokens[7]);
        	int sequence_TVCcontroller_ID 		= 0;
        	double TVCctrl_target_t      		= 0;
        	double TVCctrl_target_alt 			= 0;
        	int ctrl_TVC_target_curve    		= 0;
        	try {
        	 sequence_TVCcontroller_ID 	= Integer.parseInt(tokens[8]);
        	 TVCctrl_target_t     	    = Double.parseDouble(tokens[9]);
        	 TVCctrl_target_alt 		= Double.parseDouble(tokens[10]);
        	 ctrl_TVC_target_curve      = Integer.parseInt(tokens[11]);
        	}catch(java.lang.ArrayIndexOutOfBoundsException eAIOO) {System.out.println("No TVC controller found.");}
        	newSequenceElement.Update( sequence_ID,trigger_end_type,trigger_end_value,sequence_type,sequence_controller_ID,ctrl_target_vel,ctrl_target_alt,ctrl_target_curve,sequence_TVCcontroller_ID,TVCctrl_target_t,TVCctrl_target_alt,ctrl_TVC_target_curve);
            UPDATE_SequenceElements(newSequenceElement, SEQUENCE_DATA);
        }
        System.out.println("READ: Sequence setup sucessful. ");
        System.out.println(""+SEQUENCE_DATA.size()+" Sequences added");
        br.close();
        } catch(NullPointerException eNPE) { System.out.println(eNPE);}
        return SEQUENCE_DATA;
    }
    
    public static List<StopCondition> READ_EventHandler(double rm, double refElevation) throws IOException{
         	 List<StopCondition> STOP_Handler = new ArrayList<StopCondition>(); 
		   	 String dir = System.getProperty("user.dir");
      	   	 INPUT_FILE = dir+"/INP/eventhandler.inp";
      	   	 BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
      	   	 String strLine;
       try { 
	       while ((strLine = br.readLine()) != null )   {
	       	String[] tokens = strLine.split(" ");
	    	int event_type 			= Integer.parseInt(tokens[0]);
	    	double event_value 		= Double.parseDouble(tokens[1]);
	    	System.out.println(event_type+" | "+event_value);
	    	STOP_Handler.add(new StopCondition(event_value,event_type, rm, refElevation));
	       }
	       for(int i=0;i<STOP_Handler.size();i++) {
	    	   STOP_Handler.get(i).create_StopHandler();
	       }
       }catch(NullPointerException eNPE) { System.out.println(eNPE);}
       br.close();
       return STOP_Handler; 
    }
    
    public static double  READ_ErrorFile() throws IOException{
    	double engine_toff =0;
	   	 String dir = System.getProperty("user.dir");
 	   	 INPUT_FILE = dir+"/INP/ErrorFile.inp";
 	   	 BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
 	   	 String strLine;
      try { 
			      while ((strLine = br.readLine()) != null )   {
			      	String[] tokens = strLine.split(" ");
			   	   engine_toff 	= Double.parseDouble(tokens[0]);
			
			      }
	  }catch(NullPointerException eNPE) { System.out.println(eNPE);}
	  br.close();
	  return engine_toff; 
}

    public static void main(String[] args) throws IOException {
    	System.out.println("Simulation started");
    	System.out.println("Start READ :");
    	System.out.println("------------------------------------------");
    	 List<SequenceElement> SEQUENCE_DATA = new ArrayList<SequenceElement>(); 
    	SEQUENCE_DATA = READ_SEQUENCE();
    	//-----------------------------------------
    	//          INTEGRATOR
    	//	0 Dormand Prince 853 Integrator
    	//  1 Standard Runge Kutta Integrator
    	//  2 Gragg-Bulirsch-Stoer Integrator
    	//  3 Adams-Bashforth Integrator
    	//-----------------------------------------
    	//           target
    	//  0 Earth
    	//  1 Moon
    	//  2 Mars
    	//  3 Venus
    	//-----------------------------------------
	boolean inp_read_success = READ_INPUT();
		if(inp_read_success) { 
	    	int INTEGRATOR=(int) x_init[8];
	    	int target=(int) x_init[9];
	    	double t_engine_off = READ_ErrorFile();
	    	double rm = EDL_3DOF.DATA_MAIN[target][0];
	    	List<StopCondition> STOP_Handler = READ_EventHandler( rm, x_init[11]) ;
	    	System.out.println("READ: "+STOP_Handler.size()+" EventHandler found.");
	    	//System.out.println(target+" "+ rm);
	    	int descent_ascent_switch = (int) x_init[12]; 
			//System.out.println("Start init: \n"+INTEGRATOR+"\n"+target+"\n"+(x_init[0]*deg)+"\n"+(x_init[1]*deg)+"\n"+(x_init[2]+rm)+"\n"+x_init[3]+"\n"+(x_init[4]*deg)+"\n"+(x_init[5]*deg)+"\n"+(x_init[6])+"\n"+x_init[7]+"\n End init \n");
	    	if(descent_ascent_switch==0 ) {
	    	EDL_3DOF.Launch_Integrator(INTEGRATOR, 				 // Integrator Index 					 [-]
														target, 				 // Target index 						 	 [-]
														x_init[0]*deg, 			 // Longitude 							 [rad]
														x_init[1]*deg, 			 // Latitude 							 [rad]
														x_init[2]+x_init[11]+rm, // Radius 								 [m]
														x_init[3], 				 // Velocity 							 [m/s]
														x_init[4]*deg, 			 // Flight path angle 					 [rad]
														x_init[5]*deg, 			 // Local Azimuth 						 [rad]
														x_init[6], 				 // Initial S/C mass 					 [kg]
														x_init[7], 			   	 // Maximum integ. time 				 	 [s]
														x_init[10],				 // Write out delta time 				 [s]
														x_init[11],				 // Reference Elevation  				 [m]
														SEQUENCE_DATA,			 // Sequence data set	LIST				 [-]
												  (int) x_init[12],				 // Descent/Ascent Thrust vector switch  [-]   1 accelerate (ascent) , 0 decelerate (descent)
														STOP_Handler			     // Event Handler 	LIST					 [-]
														);
	    	} else {
		    	Ascent_3DOF.Launch_Integrator(INTEGRATOR, 				 // Integrator Index 					 [-]
						target, 				 // Target index 						 	 [-]
						x_init[0]*deg, 			 // Longitude 							 [rad]
						x_init[1]*deg, 			 // Latitude 							 [rad]
						x_init[2]+x_init[11]+rm, // Radius 								 [m]
						x_init[3], 				 // Velocity 							 [m/s]
						x_init[4]*deg, 			 // Flight path angle 					 [rad]
						x_init[5]*deg, 			 // Local Azimuth 						 [rad]
						x_init[6], 				 // Initial S/C mass 					 [kg]
						x_init[7], 			   	 // Maximum integ. time 				 [s]
						x_init[10],				 // Write out delta time 				 [s]
						x_init[11],				 // Reference Elevation  				 [m]
						SEQUENCE_DATA,			 // Sequence data set	LIST		     [-]
				                0,				 // Descent/Ascent Thrust vector switch  [-]   1 accelerate (ascent) , 0 decelerate (descent)
						STOP_Handler	,		 // Event Handler 	LIST			     [-]
						t_engine_off
						);
	    	}
		}else {
			System.out.println("Reading Input file failed -> Forced Integrator Stop.");
		}
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}