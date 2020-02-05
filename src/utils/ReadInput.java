package utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import FlightElement.SpaceShip;
import GUI.FilePaths;
import GUI.Dashboard.ChartSetting;
import Sequence.SequenceContent;
import Sequence.SequenceElement;
import Simulator_main.DataSets.IntegratorData;
import Simulator_main.DataSets.SimulatorInputSet;

public class ReadInput {
/**
 * 
 * 		File path setting for each input file: 
 * 		The following file path setting will be used for the Read AND the Write class
 * 
 */
	
    public static double PI    = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;                 // PI                                       [-] 
	static double deg2rad 	   = PI/180.0; 					    //Convert degrees to radians
	static double rad2deg 	   = 180/PI; 					    //Convert radians to degrees
	
	private static boolean integratorSettingFlag=false;
	
	private static int integratorSetting=1;
	static double[] IntegInput =  new double[5];
	
	public static double[][] q_vector        = {{0},
												{0},
												{0},
												{0}}; 							// Quarternion vector
	
	static double[][] InertiaTensor   = {{   0 ,  0  ,   0},
			  					  {   0 ,  0  ,   0},
			  					  {   0 ,  0  ,   0}};  // Inertia Tensor []
	
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
    public static String dashboardSettingFile 		    = System.getProperty("user.dir") + "/INP/GUI/dashboardSetting.inp";
//------------------------------------------------------	-----------------------------------------------------
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
			if(k==12) {
				// Ignore Date 
				inputOut[12]=0;
			} else {
				String[] tokens = strLine.split(" ");
				InitialState = Double.parseDouble(tokens[0]);
				try {
					inputOut[k]= InitialState;
				} catch(ArrayIndexOutOfBoundsException eIOOO) {
					System.out.println("Array index out of bounds detected");
				}
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
public static List<SequenceContent> readSequenceFile() throws IOException{	
	List<SequenceContent> SequenceSet = new ArrayList<SequenceContent>();
	BufferedReader br = new BufferedReader(new FileReader(sequenceFile));
   String strLine;
   String fcSeparator="\\|FlightControllerElements\\|";
   String eventSeparator="\\|EventManagementElements";
   String endSeparator="\\|EndElement\\|";
   try {
   while ((strLine = br.readLine()) != null )   {
       
       	String[] initSplit = strLine.split(fcSeparator);
       	
       	System.out.println("Content"+initSplit);

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
    	    	sequenceContent.addPitchControl();
    	    } else if (flightControllerIndex==4) { // roll stabilisation
    	    	sequenceContent.addRollControl();
    	    } else if (flightControllerIndex==5) { // full thrust
    	    	sequenceContent.addPrimaryThrustControl();
    	    } else if (flightControllerIndex==6) { // ascent controller
    	    	sequenceContent.addAscentController();
    	    } else if (flightControllerIndex==7) {
    	    	sequenceContent.addDescentController();
    	    } else if (flightControllerIndex==8) { // external Controller
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
    	    String testString = ""+sequenceContent.getID();
    	    if(!testString.equals("")) {
    	    		SequenceSet.add(sequenceContent);
    	    }
   }
   br.close();
   } catch(NullPointerException eNPE) { System.out.println(eNPE);}
   // Add additional sequence element to avoid reaching undefined space 
   SequenceContent sequenceContent = new SequenceContent();
   SequenceSet.add(sequenceContent);
 return SequenceSet; 
}
//---------------------------------------------------------------------------------------------------
public static List<ChartSetting> readChartLayout(int numberOfCharts) throws IOException {
	List<ChartSetting> settings = new ArrayList<>();
	for(int i=0;i<numberOfCharts;i++) {
		settings.add(new ChartSetting());
	}
    FileInputStream fstream = null;
    try{
    		fstream = new FileInputStream(dashboardSettingFile);
    } catch(IOException eIO) { System.out.println(eIO);}
    DataInputStream in = new DataInputStream(fstream);
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
    int k = 0;
    try {
    while ((strLine = br.readLine()) != null )   {
    	String[] tokens = strLine.split(" ");

    	try {
    	settings.get(k).type = Integer.parseInt(tokens[0]);
    	} catch (java.lang.NumberFormatException eNFE) {
    		System.out.println("ERROR: Read Dashboard chart Setting failed. Index: "+k );
    	}
    	try {
    	settings.get(k).x 	 = Integer.parseInt(tokens[1]);
    	} catch (java.lang.NumberFormatException eNFE) {
    		System.out.println("ERROR: Read Dashboard chart Setting failed. Index: "+k );
    	}
    	try {
    	settings.get(k).y 	 = Integer.parseInt(tokens[2]);
    	} catch (java.lang.NumberFormatException eNFE) {
    		System.out.println("ERROR: Read Dashboard chart Setting failed. Index: "+k );
    	}
    	k++;
    }
    fstream.close();
    in.close();
    br.close();
    
   // System.out.println("READ: Propulsion setup successful.");
    } catch(NullPointerException eNPE) { System.out.println(eNPE); System.out.println("Error: Read Dashboard chart Setting failed.");}
    return settings;
}
//---------------------------------------------------------------------------------------------------
public static SimulatorInputSet readINP() throws IOException {
    FileInputStream fstream = null;
    integratorSettingFlag=false;
    try{
    fstream = new FileInputStream(FilePaths.inputFile);
    } catch(IOException eIO) { System.out.println(eIO);}
    
    
    DataInputStream in = new DataInputStream(fstream);
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
    
    SpaceShip spaceShip = new SpaceShip(); 
    IntegratorData integratorData = new IntegratorData(); 
    SimulatorInputSet simulatorInputSet = new SimulatorInputSet();
    
    try {
		    while ((strLine = br.readLine()) != null )   {
		    	String[] tokens = strLine.split(" ");
		    	String identifier ="";
		    	double value=0;
				    	try {
				    		identifier = tokens[0];
				    		value = Double.parseDouble(tokens[1]);
				    	} catch (java.lang.NumberFormatException eNFE) {  }
				  spaceShip =   	checkSpaceShip(identifier, value, spaceShip);
				  integratorData = checkIntegratorData(identifier, value, integratorData);
		    }
    fstream.close();
    in.close();
    br.close();

   // System.out.println("READ: Propulsion setup successful.");
    } catch(NullPointerException eNPE) { System.out.println(eNPE); System.out.println("Error: ReadInput/readINP failed.");}
    /**
     * 	
     * 				Finalize data package
     */
    try {
	    integratorData.setInitialQuarterions(q_vector);
	    if(integratorSettingFlag) {
			integratorData.setIntegInput(IntegInput);	// !!! Must be called BEFORE .setIntegratorType !!!
			integratorData.setIntegratorType(integratorSetting);
	    }
	    spaceShip.setInertiaTensorMatrix(InertiaTensor);
	    simulatorInputSet.setSpaceShip(spaceShip);
	    simulatorInputSet.setIntegratorData(integratorData);
    } catch (Exception ext) {
    		System.out.println("Error: ReadInput/readINP finilizing data package failed.");
    		System.out.println(ext);
    }
    return simulatorInputSet;
}

private static SpaceShip checkSpaceShip(String identifier, double value, SpaceShip spaceShip) {
	if(identifier.equals("Init_Mass")) {
		spaceShip.setMass(value);
	} else if (identifier.equals("SC_COM")) {
		spaceShip.setCoM(value);
	} else if (identifier.equals("SC_COT")) {
		spaceShip.setCoT(value);
	} else if (identifier.equals("SC_COP")) {
		spaceShip.setCoP(value);
	} else if (identifier.equals("SC_Height")) {
		spaceShip.setVehicleLength(value);
	} else if (identifier.equals("SC_ParDiam")) {
		spaceShip.getAeroElements().setParachuteSurfaceArea(PI*(value*value/4));
	} else if (identifier.equals("SC_BodyRadius")) {
		spaceShip.getAeroElements().setHeatshieldRadius(value/2);
	} else if (identifier.equals("SC_ParMass")) {
    		spaceShip.getAeroElements().setParachuteMass(value);
	} else if (identifier.equals("SC_HeatShieldMass")) {
		spaceShip.getAeroElements().setHeatShieldMass(value);
	} else if (identifier.equals("SC_SurfArea")) {
		spaceShip.getAeroElements().setSurfaceArea(PI*(value*value/4));
	} else if (identifier.equals("Init_IXX")) {
		InertiaTensor[0][0] = value;
	} else if (identifier.equals("Init_IXY")) {
		InertiaTensor[0][1] = value;
	} else if (identifier.equals("Init_IXZ")) {
		InertiaTensor[0][2] = value;
	} else if (identifier.equals("Init_IYX")) {
		InertiaTensor[1][0] = value;
	} else if (identifier.equals("Init_IYY")) {
		InertiaTensor[1][1] = value;
	} else if (identifier.equals("Init_IYZ")) {
		InertiaTensor[1][2] = value;
	} else if (identifier.equals("Init_IZX")) {
		InertiaTensor[2][0] = value;
	} else if (identifier.equals("Init_IZY")) {
		InertiaTensor[2][1] = value;
	} else if (identifier.equals("Init_IZZ")) {
		InertiaTensor[2][2] = value;
	} else if (identifier.equals("SC_MainISP")) {
		spaceShip.getPropulsion().setPrimaryISPMax(value);
	} else if (identifier.equals("SC_MainProp")) {
		spaceShip.getPropulsion().setPrimaryPropellant(value);
	} else if (identifier.equals("SC_MainThrustMax")) {
		spaceShip.getPropulsion().setPrimaryThrustMax(value);
	} else if (identifier.equals("SC_MainThrustMin")) {
		spaceShip.getPropulsion().setPrimaryThrustMin(value);
	} else if (identifier.equals("SC_RCSMomX")) {
		spaceShip.getPropulsion().setRCSMomentumX(value);
	} else if (identifier.equals("SC_RCSMomY")) {
		spaceShip.getPropulsion().setRCSMomentumY(value);
	} else if (identifier.equals("SC_RCSMomZ")) {
		spaceShip.getPropulsion().setRCSMomentumZ(value);
	} else if (identifier.equals("SC_RCSProp")) {
		spaceShip.getPropulsion().setSecondaryPropellant(value);
	} else if (identifier.equals("SC_RCSISPX")) {
		spaceShip.getPropulsion().setSecondaryISP_RCS_X(value);
	} else if (identifier.equals("SC_RCSISPY")) {
		spaceShip.getPropulsion().setSecondaryISP_RCS_Y(value);
	} else if (identifier.equals("SC_RCSISPZ")) {
		spaceShip.getPropulsion().setSecondaryISP_RCS_Z(value);
	} else if (identifier.equals("SC_RCSThrustX")) {
		spaceShip.getPropulsion().setSecondaryThrust_RCS_X(value);
	} else if (identifier.equals("SC_RCSThrustY")) {
		spaceShip.getPropulsion().setSecondaryThrust_RCS_Y(value);
	} else if (identifier.equals("SC_RCSThrustZ")) {
		spaceShip.getPropulsion().setSecondaryThrust_RCS_Z(value);
	} else if (identifier.equals("SC_MainISPModel")) {
    		if((int) value ==1) {
    			spaceShip.getPropulsion().setIsPrimaryThrottleModel(true);
    		} else {
    			spaceShip.getPropulsion().setIsPrimaryThrottleModel(false);	
    		}
	} else if (identifier.equals("SC_MainISPMin")) {
		spaceShip.getPropulsion().setPrimaryISPMin(value);
	} 
	
	return spaceShip;
}

private static IntegratorData checkIntegratorData(String identifier, double value, IntegratorData integratorData) {
	if(identifier.equals("Init_LONG")) {
		integratorData.setInitLongitude(value*deg2rad);
	} else if (identifier.equals("Init_LAT")) {
		integratorData.setInitLatitude(value*deg2rad);
	} else if (identifier.equals("Init_RAD")) {
		integratorData.setInitRadius(value);
	} else if (identifier.equals("Init_VEL")) {
		integratorData.setInitVelocity(value);
	} else if (identifier.equals("Init_FPA")) {
		integratorData.setInitFpa(value*deg2rad);
	} else if (identifier.equals("Init_AZI")) {
		integratorData.setInitAzimuth(value*deg2rad);
	} else if (identifier.equals("Integ_MaxTime")) {
		integratorData.setMaxGlobalTime(value);
		integratorData.setGlobalTime(0);
	} else if (identifier.equals("Integ_Integrator")) {
		integratorSetting = (int) value;
		integratorSettingFlag=true;
	} else if (identifier.equals("Integ_VelVector")) {
		integratorData.setVelocityVectorCoordSystem((int) value);	
	} else if (identifier.equals("Env_CenterBody")) {
		integratorData.setTargetBody((int) value);	
	} else if (identifier.equals("Env_RefElev")) {
		integratorData.setRefElevation( value);	
	} else if (identifier.equals("Env_CenterBody")) {
		integratorData.setTargetBody((int) value);	
	} else if (identifier.equals("Env_DragModel")) {
		integratorData.setAeroDragModel((int) value);	
	} else if (identifier.equals("Env_ParModel")) {
		integratorData.setAeroParachuteModel((int) value);	
	} else if (identifier.equals("Env_ParCD")) {
		integratorData.setConstParachuteCd( value);	
	} else if (identifier.equals("Integ_DoF")) {
		integratorData.setDegreeOfFreedom((int) value);	
	} else if (identifier.equals("Init_AngRateX")) {
		integratorData.setInitRotationalRateX(value*deg2rad);	
	} else if (identifier.equals("Init_AngRateY")) {
		integratorData.setInitRotationalRateY(value*deg2rad);	
	} else if (identifier.equals("Init_AngRateZ")) {
		integratorData.setInitRotationalRateZ(value*deg2rad);	
	} else if (identifier.equals("Init_QuartW")) {
		q_vector[0][0] = value;	
	} else if (identifier.equals("Init_QuartX")) {
		q_vector[1][0] = value;	
	} else if (identifier.equals("Init_QuartY")) {
		q_vector[2][0] = value;	
	} else if (identifier.equals("Init_QuartZ")) {
		q_vector[3][0] = value;	
	} else if (identifier.equals("Integ_853_MinStep")) {
		IntegInput[0] = value;	
	} else if (identifier.equals("Integ_853_MaxStep")) {
		IntegInput[1] = value;	
	} else if (identifier.equals("Integ_853_AbsTol")) {
		IntegInput[2] = value;	
	} else if (identifier.equals("Integ_853_RelTol")) {
		IntegInput[3] = value;	
	} else if (identifier.equals("Integ_RungKut_Step")) {
		IntegInput[0] = value;	
	} else if (identifier.equals("Integ_GraBul_MinStep")) {
		IntegInput[0] = value;	
	} else if (identifier.equals("Integ_GraBul_MaxStep")) {
		IntegInput[1] = value;	
	} else if (identifier.equals("Integ_GraBul_AbsTol")) {
		IntegInput[2] = value;	
	} else if (identifier.equals("Integ_GraBul_RelTol")) {
		IntegInput[3] = value;	
	} else if (identifier.equals("Integ_AdBash_Steps")) {
		IntegInput[0] = value;	
	} else if (identifier.equals("Integ_AdBash_MinStep")) {
		IntegInput[1] = value;	
	} else if (identifier.equals("Integ_AdBash_MaxStep")) {
		IntegInput[2] = value;	
	} else if (identifier.equals("Integ_AdBash_AbsTol")) {
		IntegInput[3] = value;	
	} else if (identifier.equals("Integ_AdBash_RelTol")) {
		IntegInput[4] = value;	
	} else if (identifier.equals("Integ_Frequency")) {
		integratorData.setControllerFrequency(value);	
		integratorData.setMaxIntegTime(1/value);
	} 
	
	return integratorData;
}
/**
 * Test Unit
 * @param args
 */
public static void main(String[] args) {
	try {
		readINP();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
