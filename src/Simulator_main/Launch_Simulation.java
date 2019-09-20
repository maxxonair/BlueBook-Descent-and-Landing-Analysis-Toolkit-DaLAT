package Simulator_main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import FlightElement.SpaceShip;
import Sequence.SequenceElement;
import Simulator_main.Simulation;
import Toolbox.Mathbox;

public class Launch_Simulation implements ActionListener{
	
   	static double[] x_init =new double[20];	
    public static double PI    = 3.14159265359;                 // PI                                       [-]
    public static double kB    = 1.380650424e-23;               // Boltzmann constant                         [SI]    
    public static double G     = 1.48808E-34; 
    public static int TARGET; 
	static double deg2rad = PI/180.0; 		//Convert degrees to radians
	static double rad2deg = 180/PI; 		//Convert radians to degrees
	
	public static String INERTIA_File 				= "/INP/INERTIA.inp";
	public static String InitialAttitude_File       = "/INP/INITIALATTITUDE.inp";
	public static String INPUT_FILE                 = "/INP/init.inp";
	public static String PropulsionInputFile        = "/INP/PROP/prop.inp"  ;  		// Input: target and environment
   	//System.out.println(INPUT_FILE);
   	public static String[] IntegratorInputPath = {"/INP/INTEG/00_DormandPrince853Integrator.inp",
			  "/INP/INTEG/01_ClassicalRungeKuttaIntegrator.inp",
			  "/INP/INTEG/02_GraggBulirschStoerIntegrator.inp",
			  "/INP/INTEG/03_AdamsBashfordIntegrator.inp"
};
	
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
        	try {
        	x_init[k]= InitialState;
        	} catch(ArrayIndexOutOfBoundsException eIOOO) {
        		System.out.println("Array index out of bounds detected");
        	}
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
    public static double[]  READ_ErrorFile() throws IOException{
    	double[] engine_toff=new double[3];
    	engine_toff[0]=1;
	   	 String dir = System.getProperty("user.dir");
 	   	 INPUT_FILE = dir+"/INP/ErrorFile.inp";
 	   	 BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
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
    public static double READ_SCFile(double M0) throws IOException{
    	double SurfaceArea = 0;
	   	 String dir = System.getProperty("user.dir");
 	   	 INPUT_FILE = dir+"/INP/SC/sc.inp";
 	   	 BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
 	   	 String strLine;
 	   	 double A=0; double BC=0;
      try { 
    	  int k=0;
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
			      if(A==0) {
			    	  	SurfaceArea = M0/BC;
			      } else {
			    	  	SurfaceArea = A;
			      }
	  }catch(NullPointerException eNPE) { System.out.println(eNPE);}
	  br.close();
	  return SurfaceArea; 
}
    public static double[][] READ_INERTIA() throws NumberFormatException, IOException {
	double[][] InertiaTensor   = {{   0 ,  0  ,   0},
			    					  {   0 ,  0  ,   0},
			    					  {   0 ,  0  ,   0}};  // Inertia Tensor []
   	 String dir = System.getProperty("user.dir");
   	 INERTIA_File  = dir + INERTIA_File;
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
    
    public static double[][] READ_INITIALATTITUDE() throws NumberFormatException, IOException {
	double[][] QVector   = {{   0 },
			    			    {   0 },
			    			    {   0 },
			    				{   0 }};  // Inertia Tensor []
   	 String dir = System.getProperty("user.dir");
   	InitialAttitude_File  = dir + InitialAttitude_File;
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

    public static void main(String[] args) throws IOException {
    	String timeStamp = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("------------------------------------------");
    	System.out.println("	Simulation started - "+timeStamp);
    	System.out.println("------------------------------------------");
    	System.out.println("Start READ :");
    	System.out.println("------------------------------------------");
      	 String dir = System.getProperty("user.dir");
         PropulsionInputFile = dir + PropulsionInputFile;
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
	    	double[] error_file = READ_ErrorFile();
	    	double rm = Simulation.DATA_MAIN[(int) x_init[9]][0];
	    	List<StopCondition> STOP_Handler = READ_EventHandler( rm, x_init[11]) ;
	    	System.out.println("READ: "+STOP_Handler.size()+" EventHandler found.");
	    	//--------------------------------------------------------------------------------------
	    	System.out.println("READ: Create SpaceShip");
	    	double[] propRead;
	    	propRead = Mathbox.READ_PROPULSION_INPUT(PropulsionInputFile);
	    	SpaceShip spaceShip = new SpaceShip();
	    	spaceShip.setInertiaTensorMatrix(READ_INERTIA());
	    	spaceShip.setMass(x_init[6]);
	    	spaceShip.setInitialQuarterions(READ_INITIALATTITUDE());
	    	spaceShip.getAeroElements().setSurfaceArea(READ_SCFile(x_init[6]));
	    	spaceShip.getPropulsion().setPrimaryISPMax(propRead[0]);
	    	spaceShip.getPropulsion().setPrimaryPropellant(propRead[1]);
	    	spaceShip.getPropulsion().setPrimaryThrustMax(propRead[2]);
	    	spaceShip.getPropulsion().setPrimaryThrustMin(propRead[3]);
	    	if((int) propRead[4]==1) {
	    		spaceShip.getPropulsion().setIsPrimaryThrottleModel(true);
	    		spaceShip.getPropulsion().setPrimaryISPMin(propRead[5]);
	    	} else {
	    		spaceShip.getPropulsion().setIsPrimaryThrottleModel(false);
	    	}
	    	//--------------------------------------------------------------------------------------
	    	System.out.println("READ: Create IntegratorData");
			String IntegInput = dir + IntegratorInputPath[(int) x_init[8]];
			double[] IntegINP = Mathbox.READ_INTEGRATOR_INPUT(IntegInput);
	    	IntegratorData integratorData = new IntegratorData();
	    		integratorData.setIntegInput(IntegINP);				// ! Must be called before .setIntegratorType !
	    		integratorData.setIntegratorType((int) x_init[8]);
	    		integratorData.setTargetBody((int) x_init[9]);
	    		integratorData.setInitLongitude(x_init[0]*deg2rad);
	    		integratorData.setInitLatitude(x_init[1]*deg2rad);
	    		integratorData.setInitRadius(x_init[2]+x_init[11]+rm);
	    		integratorData.setInitVelocity(x_init[3]);
	    		integratorData.setInitFpa(x_init[4]*deg2rad);
	    		integratorData.setInitAzimuth(x_init[5]*deg2rad);
	    		integratorData.setIntegStopHandler(STOP_Handler);
	    		integratorData.setIntegTimeStep(x_init[10]);
	    		integratorData.setMaxIntegTime(x_init[7]);
	    		integratorData.setRefElevation(x_init[11]);
	    		integratorData.setVelocityVectorCoordSystem((int) x_init[13]);
	    		integratorData.setDegreeOfFreedom((int) x_init[14]);
	    		//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
	    		//												6 Degree of Freedom Universial module
	    		//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
	    		System.out.println("Simulator set and running");
	    		//System.out.println(""+SurfaceArea);
	    		Simulation.launchIntegrator(
	    												    integratorData,
														SEQUENCE_DATA,			 	 // Sequence data set	LIST			     [-]
														error_file,					 // Error file to model partial system failres [-] 
														spaceShip				     // SpaceShip data file                  [-]
	    				);
}
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}