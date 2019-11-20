package Toolbox;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Sequence.SequenceContent;
import Sequence.SequenceElement;
import Simulator_main.StopCondition;

public class ReadInput {

   	public static String[] IntegratorInputPath = {System.getProperty("user.dir") + "/INP/INTEG/00_DormandPrince853Integrator.inp",
   			System.getProperty("user.dir") + "/INP/INTEG/01_ClassicalRungeKuttaIntegrator.inp",
   			System.getProperty("user.dir") + "/INP/INTEG/02_GraggBulirschStoerIntegrator.inp",
   			System.getProperty("user.dir") + "/INP/INTEG/03_AdamsBashfordIntegrator.inp"
};
   	
	public static String INERTIA_File 				= System.getProperty("user.dir") + "/INP/INERTIA.inp";
	public static String InitialAttitude_File       = System.getProperty("user.dir") + "/INP/INITIALATTITUDE.inp";
	public static String INPUT_FILE                 = System.getProperty("user.dir") + "/INP/init.inp";
	public static String PropulsionInputFile        = System.getProperty("user.dir") + "/INP/PROP/prop.inp"  ; 
    public static String SC_file 					= System.getProperty("user.dir") + "/INP/SC/sc.inp";
    public static String Aero_file 					= System.getProperty("user.dir") + "/INP/AERO/aeroBasic.inp";
    public static String ERROR_File 					= System.getProperty("user.dir") + "/INP/ErrorFile.inp";
	public static String EventHandler_File			= System.getProperty("user.dir") + "/INP/eventhandler.inp";
    public static String SEQUENCE_File   			= System.getProperty("user.dir") + "/INP/sequence_1.inp";
    public static String sequenceFile 		    = System.getProperty("user.dir") + "/INP/sequenceFile.inp";
	
	public static void updateSequenceElements(SequenceElement NewElement, List<SequenceElement> SEQUENCE_DATA){	   
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
//---------------------------------------------------------------------------------------------------	
    public static List<SequenceElement> readSequence() throws IOException{	
   	 List<SequenceElement> SEQUENCE_DATA = new ArrayList<SequenceElement>(); 
		BufferedReader br = new BufferedReader(new FileReader(SEQUENCE_File));
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
       	updateSequenceElements(newSequenceElement, SEQUENCE_DATA);
       }
       System.out.println("READ: Sequence setup sucessful. ");
       System.out.println(""+SEQUENCE_DATA.size()+" Sequences added");
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}
       return SEQUENCE_DATA;
   } 
//---------------------------------------------------------------------------------------------------  
    public static List<StopCondition> readEventHandler(double rm, double refElevation) throws IOException{
    	 List<StopCondition> STOP_Handler = new ArrayList<StopCondition>(); 
 	   	 BufferedReader br = new BufferedReader(new FileReader(EventHandler_File));
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
 //---------------------------------------------------------------------------------------------------
public static double[]  readErrorFile() throws IOException{
	double[] engine_toff=new double[3];
	engine_toff[0]=1;
   	 BufferedReader br = new BufferedReader(new FileReader(ERROR_File));
   	 String strLine;
 try { 
		      while ((strLine = br.readLine()) != null )   {
		      	String[] tokens = strLine.split(" ");
		      	if(Integer.parseInt(tokens[0])==0) {
		       engine_toff[0]   = 0;
		   	   engine_toff[1] 	= Double.parseDouble(tokens[1]);
		   	   engine_toff[2] 	= Double.parseDouble(tokens[2]);
		      	}
		
		      }
 }catch(NullPointerException eNPE) { System.out.println(eNPE);}
 br.close();
 return engine_toff; 
}
//---------------------------------------------------------------------------------------------------
public static double[][] readInertia() throws NumberFormatException, IOException {
double[][] InertiaTensor   = {{   0 ,  0  ,   0},
		    					  {   0 ,  0  ,   0},
		    					  {   0 ,  0  ,   0}};  // Inertia Tensor []
	BufferedReader br = new BufferedReader(new FileReader(INERTIA_File));
       String strLine;
       int j=0;
       try {
       while ((strLine = br.readLine()) != null )   {
	       	String[] tokens = strLine.split(" ");
	       		InertiaTensor[j][0] = Double.parseDouble(tokens[0]);     
	       		InertiaTensor[j][1] = Double.parseDouble(tokens[1]);  
	       		InertiaTensor[j][2] = Double.parseDouble(tokens[2]);     
	       	j++;
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}
       for(int i=0;i<3;i++) {
	    	   for(int k=0;k<3;k++) {
	    		   // Uncommend to test the reading function:
	    		   //System.out.println(" "+InertiaTensor[i][k]+" ");;
	    	   }
       }
       return InertiaTensor;
} 
//---------------------------------------------------------------------------------------------------
public static double[][] readInitialAttitude() throws NumberFormatException, IOException {
double[][] QVector   = {{   0 },
		    			    {   0 },
		    			    {   0 },
		    				{   0 }};  // Inertia Tensor []
	BufferedReader br = new BufferedReader(new FileReader(InitialAttitude_File));
       String strLine;
       int j=0;
       try {
       while ((strLine = br.readLine()) != null )   {
	       	String[] tokens = strLine.split(" ");
	       	QVector[j][0] = Double.parseDouble(tokens[0]);         
	       	j++;
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}
       for(int i=0;i<QVector.length;i++) {
    	   //System.out.println(QVector[i][0]);
       }
       return QVector;
} 
//---------------------------------------------------------------------------------------------------
public static double[] readIntegratorInput(int Integrator) throws IOException{
	String INPUT_FILE = IntegratorInputPath[Integrator];
double[] readINP = new double[10];
	double InitialState = 0;
    FileInputStream fstream = null;
    try{
    fstream = new FileInputStream(INPUT_FILE);
    } catch(IOException eIO) { System.out.println(eIO);}
    DataInputStream in = new DataInputStream(fstream);
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
    int k = 0;
    try {
    while ((strLine = br.readLine()) != null )   {
    	String[] tokens = strLine.split(" ");
    	InitialState = Double.parseDouble(tokens[0]);
    	readINP[k]= InitialState;
    	k++;
    }
		fstream.close();
		in.close();
		br.close();
   // System.out.println("READ: Integrator setup successful.");
    } catch(NullPointerException eNPE) { System.out.println(eNPE); System.out.println("Error: Integrator setup read failed.");}
    return readINP;
}
//---------------------------------------------------------------------------------------------------
public static double[] readInput() {
	double InitialState = 0;
	double[] inputOut =new double[30];	
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
    try {
		while ((strLine = br.readLine()) != null )   {
			String[] tokens = strLine.split(" ");
			InitialState = Double.parseDouble(tokens[0]);
			try {
				inputOut[k]= InitialState;
			} catch(ArrayIndexOutOfBoundsException eIOOO) {
				System.out.println("Array index out of bounds detected");
			}
			//System.out.println("" +k+"   "+InitialState);
			k++;
		}
	} catch (NumberFormatException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    try {
		fstream.close();
        in.close();
        br.close();
	} catch (IOException e) {

		e.printStackTrace();
	}

    } catch(NullPointerException eNPE) { System.out.println(eNPE);}
    return inputOut;
} 
//---------------------------------------------------------------------------------------------------
public static double[] readAeroFile() {
	double[] result = new double[30];
	   	 BufferedReader br = null;
	try {
		br = new BufferedReader(new FileReader(Aero_file));
	} catch (FileNotFoundException e) {
		System.err.println("ERROR: Read aeroBasic.inp failed. BufferedReader error.");
		e.printStackTrace();
	}
	   	 String strLine;
  try { 
	  int k=0;
		      try {
				while ((strLine = br.readLine()) != null )   {
				  	String[] tokens = strLine.split(" ");
			  		if(tokens[0].isEmpty()) {
			  			result[k] = 0;
			  		} else {
			  			result[k]= Double.parseDouble(tokens[0]);
			  		}
k++;
				  }
			} catch (NumberFormatException | IOException e) {
				
				e.printStackTrace();
			}
  }catch(NullPointerException eNPE) { System.out.println(eNPE);}
  try {
	br.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
  return result; 
}
//---------------------------------------------------------------------------------------------------
public static double[] readSCFile() {
	double[] result = new double[30];
	   	 BufferedReader br = null;
	try {
		br = new BufferedReader(new FileReader(SC_file));
	} catch (FileNotFoundException e) {
		System.err.println("ERROR: Read aeroBasic.inp failed. BufferedReader error.");
		e.printStackTrace();
	}
	   	 String strLine;
try { 
	  int k=0;
		      try {
				while ((strLine = br.readLine()) != null )   {
				  	String[] tokens = strLine.split(" ");
			  		if(tokens[0].isEmpty()) {
			  			result[k] = 0;
			  		} else {
			  			result[k]= Double.parseDouble(tokens[0]);
			  		}
k++;
				  }
			} catch (NumberFormatException | IOException e) {
				
				e.printStackTrace();
			}
}catch(NullPointerException eNPE) { System.out.println(eNPE);}
try {
	br.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
return result; 
}
//---------------------------------------------------------------------------------------------------
public static double readSurfaceArea(double M0) {
	double SurfaceArea = 0;
	   	 BufferedReader br = null;
	try {
		br = new BufferedReader(new FileReader(SC_file));
	} catch (FileNotFoundException e) {
		System.err.println("ERROR: Read sc.inp failed. BufferedReader error.");
		e.printStackTrace();
	}
	   	 String strLine;
	   	 double A=0; double BC=0;
  try { 
	  int k=0;
		      try {
				while ((strLine = br.readLine()) != null )   {
				  	String[] tokens = strLine.split(" ");
				  	if(k==0) {
				  		if(tokens[0].isEmpty()) {
				  			A=0;
				  		} else {
				  			A = Double.parseDouble(tokens[0]);
				  		}
				  	} else if (k==1) {
				  		if(tokens[0].isEmpty()) {
				  			BC = 0;
				  		} else {
				  			BC= Double.parseDouble(tokens[0]);
				  		}
				  	}
k++;
				  }
			} catch (NumberFormatException | IOException e) {
				
				e.printStackTrace();
			}
		      if(A==0) {
		    	  	SurfaceArea = M0/BC;
		      } else {
		    	  	SurfaceArea = A;
		      }
  }catch(NullPointerException eNPE) { System.out.println(eNPE);}
  try {
	br.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
  return SurfaceArea; 
}
//---------------------------------------------------------------------------------------------------
public static double[] readPropulsionInput() throws IOException{
double[] readINP = new double[30];
	double InitialState = 0;
    FileInputStream fstream = null;
    try{
    fstream = new FileInputStream(PropulsionInputFile);
    } catch(IOException eIO) { System.out.println(eIO);}
    DataInputStream in = new DataInputStream(fstream);
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
    int k = 0;
    try {
    while ((strLine = br.readLine()) != null )   {
    	String[] tokens = strLine.split(" ");
    	try {
    	InitialState = Double.parseDouble(tokens[0]);
    	} catch (java.lang.NumberFormatException eNFE) {
    		System.out.println("ERROR: Read proplsion value failed. Index: "+k );
    	}
    	readINP[k]= InitialState;
    	k++;
    }
    fstream.close();
    in.close();
    br.close();
    
   // System.out.println("READ: Propulsion setup successful.");
    } catch(NullPointerException eNPE) { System.out.println(eNPE); System.out.println("Error: Propulsion setup read failed.");}
    return readINP;
}
//---------------------------------------------------------------------------------------------------
public static List<SequenceContent> READ_sequenceFile() throws IOException{	
	List<SequenceContent> SequenceSet = new ArrayList<SequenceContent>();
	BufferedReader br = new BufferedReader(new FileReader(sequenceFile));
   String strLine;
   String fcSeparator="\\|FlightControllerElements\\|";
   String eventSeparator="\\|EventManagementElements";
   String endSeparator="\\|EndElement\\|";
   try {
   while ((strLine = br.readLine()) != null )   {
       
       	String[] initSplit = strLine.split(fcSeparator);

       	String[] head = initSplit[0].split(" ");
       //System.out.pri
       	int  ID = Integer.parseInt(head[0]);
       	//String sequenceName = head[1];
       	int flightControllerIndex = Integer.parseInt(initSplit[1].split(" ")[1]);
       	String[] arr     = strLine.split(eventSeparator);
       	//System.out.println(arr[1]);
       	int eventIndex  = Integer.parseInt(arr[1].split(" ")[1]);
       	
       	String[] arr2   = strLine.split(endSeparator);
       	//System.out.println(arr2[1]);
       	int endIndex    = Integer.parseInt(arr2[1].split(" ")[1]);
       	double endValue = Double.parseDouble(arr2[1].split(" ")[2]);
       	
       	//System.out.println(ID+" "+sequenceName+" "+flightControllerIndex+" "+eventIndex+" "+endIndex+" "+endValue);
    	    SequenceContent sequenceContent = new SequenceContent();
    	    sequenceContent.setID(ID);
	    	//---------------------------------------------------------------------------------------------------
	    	//					Flight Controller  
	    	//---------------------------------------------------------------------------------------------------
    	    if(flightControllerIndex==0) {
    	    		// No Selection!
    	    } else if (flightControllerIndex==1) { // roll control
    	    	sequenceContent.addRollControl();
    	    } else if (flightControllerIndex==2) { // yaw control
    	    	sequenceContent.addYawControl();
    	    } else if (flightControllerIndex==3) { // pitch control
    	    	
    	    } else if (flightControllerIndex==4) { // roll stabilisation
    	    	sequenceContent.addRollControl();
    	    } else if (flightControllerIndex==5) { // full thrust
    	    	sequenceContent.addPrimaryThrustControl();
    	    } else if (flightControllerIndex==6) { // ascent controller
    	    	sequenceContent.addAscentController();
    	    } else if (flightControllerIndex==7) { // external Controller
    	    	String scriptName=""; // INSERT SCRIPT NAME method ->TBD!
    	    	sequenceContent.addExternalControl(scriptName);
    	    }
      	//---------------------------------------------------------------------------------------------------
      	//					Events
      	//---------------------------------------------------------------------------------------------------
    	    if(eventIndex==0) {
    	    	// No Selection!
    	    } else if (eventIndex==1) {
    	    	sequenceContent.addParachuteDeployment();
    	    } else if (eventIndex==2) {
    	    	sequenceContent.addParachuteSeparation();
    	    } else if (eventIndex==3) {
    	    	// Stage Separation tbd
    	    } else if (eventIndex==4) {
    	    	sequenceContent.addHeatShieldSeparation();
    	    }
        //---------------------------------------------------------------------------------------------------
        	//					Sequence End
        	//---------------------------------------------------------------------------------------------------   	    
    	    sequenceContent.setTriggerEnd(endIndex, endValue);
        //---------------------------------------------------------------------------------------------------
        	//					Add Sequence 
        	//---------------------------------------------------------------------------------------------------   	    
    	    SequenceSet.add(sequenceContent);
   }
   br.close();
   } catch(NullPointerException eNPE) { System.out.println(eNPE);}
   // Add additional sequence element to avoid reaching undefined space 
   SequenceContent sequenceContent = new SequenceContent();
   SequenceSet.add(sequenceContent);
 return SequenceSet; 
}
//---------------------------------------------------------------------------------------------------
public static String[] getIntegratorInputPath() {
	return IntegratorInputPath;
}
public static String getINERTIA_File() {
	return INERTIA_File;
}
public static String getInitialAttitude_File() {
	return InitialAttitude_File;
}
public static String getINPUT_FILE() {
	return INPUT_FILE;
}
public static String getPropulsionInputFile() {
	return PropulsionInputFile;
}
public static String getSC_file() {
	return SC_file;
}
public static String getERROR_File() {
	return ERROR_File;
}
public static String getEventHandler_File() {
	return EventHandler_File;
}
public static String getSEQUENCE_File() {
	return SEQUENCE_File;
}

}
