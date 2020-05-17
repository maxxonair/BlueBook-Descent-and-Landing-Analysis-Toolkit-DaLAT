package utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import GUI.AerdoynamicSetup.AerodynamicSetup;
import GUI.AerdoynamicSetup.AerodynamicSetupSpacecraft;
import GUI.Dashboard.DashboardLeftPanel;
import GUI.DataStructures.InputFileSet;
import GUI.GeometryModel.GeometryFrame;
import GUI.InertiaGeometry.InertiaGeometry;
import GUI.PropulsionSetup.PropulsionSetup;
import GUI.Sequence.GUISequenceElement;
import GUI.Sequence.SequencePanel;
import GUI.SimulationSetup.BasicSetup.AttitudeSetting;
import GUI.SimulationSetup.BasicSetup.CenterPanelRight;
import GUI.SimulationSetup.BasicSetup.SidePanelLeft;
import Simulator_main.DataSets.RealTimeResultSet;
import Simulator_main.DataSets.SimulationConstants;

public class GuiReadInput {
	private static double PI    = 3.1415926535897932384626;     // PI      [-] 
	private static boolean integratorSettingFlag=false;
	
    public static List<RealTimeResultSet> READ_ResultSet() {
    	List<RealTimeResultSet> resultSet = new ArrayList<RealTimeResultSet>();
    	resultSet.clear();
    	// Read all data from file: 
    	double resPropPerc =0;
    	double totalDv =0;
    	double m0=0;
    	double m=0;
	    FileInputStream fstream = null;
		try{ fstream = new FileInputStream(FilePaths.RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	              DataInputStream in = new DataInputStream(fstream);
	              BufferedReader br = new BufferedReader(new InputStreamReader(in));
	              String strLine;
	              
	              try {
	            	  int k=0;
							while ((strLine = br.readLine()) != null )   {
								Object[] tokens = strLine.split(" ");
						     	RealTimeResultSet resultElement = new RealTimeResultSet();
							    double[] CartesianPosition = {Double.parseDouble((String) tokens[41]),
			 							   						Double.parseDouble((String) tokens[42]),
			 							   						Double.parseDouble((String) tokens[43])};
							    Quaternion quat = new Quaternion(Double.parseDouble((String) tokens[47]),
							    					   Double.parseDouble((String) tokens[48]),
							    					   Double.parseDouble((String) tokens[49]),
							    					   Double.parseDouble((String) tokens[50]) );
							    resultElement.setQuaternion(quat);
							    resultElement.setCartesianPosECEF(CartesianPosition);
							    EulerAngle intEul = new EulerAngle();
							    intEul.roll = Double.parseDouble((String) tokens[57]);
							    intEul.pitch = Double.parseDouble((String) tokens[58]);
							    intEul.yaw = Double.parseDouble((String) tokens[59]);
							    resultElement.setEulerAngle(intEul);
							    resultElement.setVelocity(Double.parseDouble((String) tokens[6]) );
							    resultElement.setAltitude(Double.parseDouble((String) tokens[4]));
							    resultElement.setTime(Double.parseDouble((String) tokens[0]));
							    resultElement.setFpa(Double.parseDouble((String) tokens[7]));
							    resultElement.setAzi(Double.parseDouble((String) tokens[8]));
							    resultElement.setLongitude(Double.parseDouble((String) tokens[1]));
							    resultElement.setLatitude(Double.parseDouble((String) tokens[2]));
							    resultElement.setSCMass(Double.parseDouble((String) tokens[60]));
							    resultElement.setPrimTankfillingLevelPerc(Double.parseDouble((String) tokens[73]));
							    resultElement.setSecTankfillingLevelPerc(Double.parseDouble((String) tokens[81]));
							    if(k==0) {
							    	m0 = Double.parseDouble((String) tokens[60]);
							    }
							    m=Double.parseDouble((String) tokens[60]);
							    resPropPerc = Double.parseDouble((String) tokens[73]);
							    totalDv = Double.parseDouble((String) tokens[99]);
							    
							    resultSet.add(resultElement);
							  k++;
							  }
			       fstream.close();
			       in.close();
			       br.close();
			       DashboardLeftPanel.INDICATOR_RESPROP.setText(""+BlueBookVisual.decf.format(resPropPerc));
			       DashboardLeftPanel.INDICATOR_DELTAV.setText(""+BlueBookVisual.decf.format(totalDv));
			       DashboardLeftPanel.INDICATOR_PROPPERC.setText(""+BlueBookVisual.decf.format(m0 - m));
			       
	              } catch (NullPointerException | IOException eNPE) { 
	            	  		//System.out.println("Read raw data, Nullpointerexception");
					}catch(IllegalArgumentException eIAE) {
					  System.out.println("Read raw data, illegal argument error");
					}
	 return resultSet;
    }
//------------------------------------------------------------------------    
    public static List<InputFileSet> readResultFileList(String filePath) throws IOException{
    	List<InputFileSet> newInputFileSetList = new ArrayList<InputFileSet>();

   		      	InputFileSet newInputFileSet = new InputFileSet();
   		      	newInputFileSet.setInputDataFilePath(filePath);
   		      	newInputFileSetList.add(newInputFileSet);

    return newInputFileSetList;
    }
    public static void readSequenceFile() throws IOException{
    	SequencePanel.resetSequenceContentList();
		BufferedReader br = new BufferedReader(new FileReader(FilePaths.sequenceFile));
    	BlueBookVisual.getDashboardPanel().getDashboardLeftPanel().getSequenceIndicator().clearDocument();
       String strLine;
       String fcSeparator="\\|FlightControllerElements\\|";
       String validationString = "FlightControllerElements";
       String eventSeparator="\\|EventManagementElements";
       String endSeparator="\\|EndElement\\|";
       int sequenceID=0;
       try {
       while ((strLine = br.readLine()) != null )   {
		       if( strLine.contains(validationString) ) {		// Check for valid input line - skip empty lines 
		    	   BlueBookVisual.getDashboardPanel().getDashboardLeftPanel().getSequenceIndicator().addContentString(strLine);
		    	   
				       	String[] initSplit = strLine.split(fcSeparator);
			
				       	String[] head = initSplit[0].split(" ");
				       // 	int  ID = Integer.parseInt(head[0]);
				       	String sequenceName = head[1];
				       	int flightControllerIndex = Integer.parseInt(initSplit[1].split(" ")[1]);
				       	String[] arr     = strLine.split(eventSeparator);
				       	//System.out.println(arr[1]);
				       	int eventIndex  = Integer.parseInt(arr[1].split(" ")[1]);
				       	
				       	String[] arr2   = strLine.split(endSeparator);
				       	//System.out.println(arr2[1]);
				       	int endIndex    = Integer.parseInt(arr2[1].split(" ")[1]);
				       	double endValue = Double.parseDouble(arr2[1].split(" ")[2]);
				       		
				       	GUISequenceElement.addGUISequenceElment(sequenceID, sequenceName, flightControllerIndex, eventIndex, endIndex, endValue);
				       	
		     }
	   sequenceID++;
       }

       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}

   }
//------------------------------------------------------------------------
    public static void readINP() {
    	GeometryFrame.getCanvas().readElementList();
    	
    	// Clear Input file indicator>
    	BlueBookVisual.getDashboardPanel().getDashboardLeftPanel().getInputIndicator().clearDocument();
    	
    List<double[]> integratorSettings = new ArrayList<>();
    integratorSettingFlag =false;
    integratorSettings.add(new double[4]);	// 853
    integratorSettings.add(new double[1]);	// Runge Kutta
    integratorSettings.add(new double[4]);	// GragBul
    integratorSettings.add(new double[5]);	// AdBash
    double[][] inertiaTensor = new double[3][3];
    int targetIndx = -1 ;
    double initRadius = -1;
        FileInputStream fstream = null;
        try{
        fstream = new FileInputStream(FilePaths.inputFile);
        } catch(IOException eIO) { System.out.println(eIO);}
        
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        
        try {
    		    while ((strLine = br.readLine()) != null )   {
    		    	String[] tokens = strLine.split(" ");
    		    	String identifier ="";
    		    	double value=0;

    				    	try {
    				    		identifier = tokens[0];
    				    		value = Double.parseDouble(tokens[1]);
    				    	} catch (java.lang.NumberFormatException eNFE) {  }
    				    	
    	    		    	// Add Input File line to Indicator 
    	    		    	BlueBookVisual.getDashboardPanel().getDashboardLeftPanel().getInputIndicator().addContentString(strLine);
    	    		    	
    				    	read_InitLONG( identifier, value);
    				    	read_InitLAT( identifier, value);
    		   initRadius = read_InitRAD( identifier, value, initRadius);
    				    	read_InitVEL( identifier, value);
    				    	read_InitFPA( identifier, value);
    				    	read_InitAZI( identifier, value);
    				    	read_InitMass( identifier, value);
    				    	read_Init_QuartW( identifier, value);
    				    	read_Init_QuartX( identifier, value);
    				    	read_Init_QuartY( identifier, value);
    				    	read_Init_QuartZ( identifier, value);
    				    	read_InitAngRateX( identifier, value);
    				    	read_InitAngRateY( identifier, value);
    				    	read_InitAngRateZ( identifier, value);
    				    	read_InitTime( identifier, value);
		    inertiaTensor = read_Init_IXX( identifier, value, inertiaTensor);
		    inertiaTensor = read_Init_IXY( identifier, value, inertiaTensor);
		    inertiaTensor = read_Init_IXZ( identifier, value, inertiaTensor);
		    inertiaTensor = read_Init_IYX( identifier, value, inertiaTensor);
		    inertiaTensor = read_Init_IYY( identifier, value, inertiaTensor);
		    inertiaTensor = read_Init_IYZ( identifier, value, inertiaTensor);
		    inertiaTensor = read_Init_IZX( identifier, value, inertiaTensor);
		    inertiaTensor = read_Init_IZY( identifier, value, inertiaTensor);
		    inertiaTensor = read_Init_IZZ( identifier, value, inertiaTensor);
    				    	
    				    	read_IntegMaxTime( identifier, value);
    				    	read_IntegIntegrator( identifier, value);
    				    	read_IntegFrequency( identifier, value);
    				    	read_IntegVelVector( identifier, value);
    				    	read_IntegDoF( identifier, value);
	    integratorSettings = read_Integ_853_MinStep( identifier, value, integratorSettings);
    	integratorSettings = read_Integ_853_MaxStep( identifier, value, integratorSettings);
    	integratorSettings = read_Integ_853_AbsTol( identifier, value, integratorSettings);
    	integratorSettings = read_Integ_853_RelTol( identifier, value, integratorSettings);
    	
    	integratorSettings = read_Integ_RungKut_Step( identifier, value, integratorSettings);
    	
    	integratorSettings = read_Integ_GraBul_MinStep( identifier, value, integratorSettings);   				    	
        integratorSettings = read_Integ_GraBul_MaxStep( identifier, value, integratorSettings);
    	integratorSettings = read_Integ_GraBul_AbsTol( identifier, value, integratorSettings);
    	integratorSettings = read_Integ_GraBul_RelTol( identifier, value, integratorSettings);
    	
    	integratorSettings = read_Integ_AdBash_Steps( identifier, value, integratorSettings);
    	integratorSettings = read_Integ_AdBash_MinStep( identifier, value, integratorSettings);
    	integratorSettings = read_Integ_AdBash_MaxStep( identifier, value, integratorSettings);
    	integratorSettings = read_Integ_AdBash_AbsTol( identifier, value, integratorSettings);
    	integratorSettings = read_Integ_AdBash_RelTol( identifier, value, integratorSettings);
    				    
    				    	read_EnvRefElev( identifier, value);
    		   targetIndx = read_EnvCenterBody( identifier, value, targetIndx);
    				    	read_Env_DragModel( identifier, value);
    				    	read_Env_ConstCD( identifier, value);
    				    	
    				    	read_SC_MainProp( identifier, value);
    				    	read_SC_MainISP( identifier, value);
    				    	read_SC_MainThrustMax( identifier, value);
    				    	read_SC_MainThrustMin( identifier, value);
    				    	read_SC_MainISPModel( identifier, value);
    				    	read_SC_MainISPMin( identifier, value);
    				    	read_SC_RCSMomX( identifier, value);
    				    	read_SC_RCSMomY( identifier, value);
    				    	read_SC_RCSMomZ( identifier, value);
    				    	read_SC_RCSThrustX( identifier, value);
    				    	read_SC_RCSThrustY( identifier, value);
    				    	read_SC_RCSThrustZ( identifier, value);
    				    	read_SC_RCSProp( identifier, value);
    				    	read_SC_RCSISPX( identifier, value);
    				    	read_SC_RCSISPY( identifier, value);
    				    	read_SC_RCSISPZ( identifier, value);
    				    	read_SC_COM( identifier, value);
    				    	read_SC_COT( identifier, value);
    				    	read_SC_COP( identifier, value);
    				    	
    				  
    				    	
    		    }
        fstream.close();
        in.close();
        br.close();
        /**
         * 		Finalize reading setup 
         */
        set_SurfaceAreaAndBallisticCoefficient();
        if(integratorSettingFlag) {
        		read_SetIntegratorSettings( integratorSettings,  SidePanelLeft.Integrator_chooser.getSelectedIndex());
        }
        set_InitALT(initRadius, targetIndx);		// Set altitude with initial radius and target index 
        passOverInertiaTensor(inertiaTensor);
        } catch(NullPointerException eNPE) { System.out.println(eNPE); System.out.println("Error: GuiReadInput/readINP failed.");} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	private static void read_InitLONG(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_LONG")) {
             	DashboardLeftPanel.INDICATOR_LONG.setText(BlueBookVisual.decf.format(value));
            		SidePanelLeft.INPUT_LONG_Rs.setText(BlueBookVisual.df_X4.format(value));
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_InitLAT(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_LAT")) {
             	DashboardLeftPanel.INDICATOR_LAT.setText(BlueBookVisual.decf.format(value));
            		SidePanelLeft.INPUT_LAT_Rs.setText(BlueBookVisual.df_X4.format(value));
    		}
    	} catch(Exception exp) {}
    }
    
    private static double read_InitRAD(String identifier, double value, double initRad) {
    	try {
    		if(identifier.equals("Init_RAD")) {
    			initRad =value;
    		}
    	} catch(Exception exp) {}
    	return initRad;
    }
    
    private static void set_InitALT(double value, int targetIndx) {
    	if(value != -1 && targetIndx != -1) {
			SimulationConstants constants = new SimulationConstants();
			constants.initConstants(targetIndx);
			double radius = constants.getRm();
	    	DashboardLeftPanel.INDICATOR_ALT.setText(BlueBookVisual.decf.format( value-radius));
			SidePanelLeft.INPUT_ALT_Rs.setText(BlueBookVisual.decf.format( value-radius));
    	}
    }
    
    private static void read_InitVEL(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_VEL")) {
             	DashboardLeftPanel.INDICATOR_VEL.setText(BlueBookVisual.decf.format(value));
            		SidePanelLeft.INPUT_VEL_Rs.setText(BlueBookVisual.df_X4.format(value));
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_InitFPA(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_FPA")) {
             	DashboardLeftPanel.INDICATOR_FPA.setText(BlueBookVisual.decf.format(value));
            		SidePanelLeft.INPUT_FPA_Rs.setText(BlueBookVisual.df_X4.format(value));
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_InitAZI(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_AZI")) {
             	DashboardLeftPanel.INDICATOR_AZI.setText(BlueBookVisual.decf.format(value));
            		SidePanelLeft.INPUT_AZI_Rs.setText(BlueBookVisual.df_X4.format(value));
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_InitMass(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_Mass")) {
    	    		DashboardLeftPanel.INDICATOR_M0.setText(BlueBookVisual.decf.format(value));
    	    		InertiaGeometry.INPUT_Mass.setText(BlueBookVisual.decf.format(value));
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_IntegMaxTime(String identifier, double value) {
    	try {
    		if(identifier.equals("Integ_MaxTime")) {
    			DashboardLeftPanel.INDICATOR_INTEGTIME.setText(BlueBookVisual.decf.format(value));
    			CenterPanelRight.setGlobalTime(value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_IntegIntegrator(String identifier, double value) {
    	try {
    		if(identifier.equals("Integ_Integrator")) {
    			SidePanelLeft.Integrator_chooser.setSelectedIndex((int) value);
    			integratorSettingFlag=true;
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_Init_QuartW(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_QuartW")) {
    			AttitudeSetting.INPUT_Quarternion1.setText(""+value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_Init_QuartX(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_QuartX")) {
    			AttitudeSetting.INPUT_Quarternion2.setText(""+value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_Init_QuartY(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_QuartY")) {
    			AttitudeSetting.INPUT_Quarternion3.setText(""+value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_Init_QuartZ(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_QuartZ")) {
    			AttitudeSetting.INPUT_Quarternion4.setText(""+value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static int read_EnvCenterBody(String identifier, double value, int targetIndx) {
    	try {
    		if(identifier.equals("Env_CenterBody")) {
    			int Target_indx = (int) value;

    			DashboardLeftPanel.INDICATOR_TARGET.setText(BlueBookVisual.Target_Options[Target_indx]);
    			CenterPanelRight.setTargetIndx(Target_indx);
    			targetIndx = Target_indx;
    			BlueBookVisual.indx_target = Target_indx;
    	        if(Target_indx==0) {
    	        	DashboardLeftPanel.INDICATOR_TARGET.setBorder(BlueBookVisual.Earth_border);
    	        } else if(Target_indx==1){
    	        	DashboardLeftPanel.INDICATOR_TARGET.setBorder(BlueBookVisual.Moon_border);
    	        } else if(Target_indx==2){
    	        	DashboardLeftPanel.INDICATOR_TARGET.setBorder(BlueBookVisual.Mars_border);
    	        } else if(Target_indx==3){
    	        	DashboardLeftPanel.INDICATOR_TARGET.setBorder(BlueBookVisual.Venus_border);
    	        }
    		}
    	} catch(Exception exp) {}
    	return targetIndx;
    }
    
    private static void read_IntegFrequency(String identifier, double value) {
    	try {
    		if(identifier.equals("Integ_Frequency")) {
    				CenterPanelRight.setGlobalFrequency(value);
    				CenterPanelRight.setControllerFrequency(value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_EnvRefElev(String identifier, double value) {
    	try {
    		if(identifier.equals("Env_RefElev")) {
    			SidePanelLeft.INPUT_REFELEV.setText(BlueBookVisual.decf.format(value));       // Reference Elevation
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_InitTime(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_Time")) {
    			//SidePanelLeft.timePanel.upda	
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_IntegVelVector(String identifier, double value) {
    	try {
    		if(identifier.equals("Integ_VelVector")) {
    		 	CenterPanelRight.setVelocityCoordinateSystem((int) value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_IntegDoF(String identifier, double value) {
    	try {
    		if(identifier.equals("Integ_DoF")) {
    		 	CenterPanelRight.setDOF_System((int) value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_InitAngRateX(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_AngRateX")) {
    			SidePanelLeft.INPUT_AngularRate_X.setText(BlueBookVisual.decAngularRate.format(value));
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_InitAngRateY(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_AngRateY")) {
    			SidePanelLeft.INPUT_AngularRate_Y.setText(BlueBookVisual.decAngularRate.format(value));
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_InitAngRateZ(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_AngRateZ")) {
    			SidePanelLeft.INPUT_AngularRate_Z.setText(BlueBookVisual.decAngularRate.format(value));
    		}
    	} catch(Exception exp) {}
    }
     
    private static void read_SC_MainProp(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_MainProp")) {
    			PropulsionSetup.INPUT_PROPMASS.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_MainThrustMax(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_MainThrustMax")) {
    			PropulsionSetup.INPUT_THRUSTMAX.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_MainThrustMin(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_MainThrustMin")) {
    			PropulsionSetup.INPUT_THRUSTMIN.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_MainISP(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_MainISP")) {
    			PropulsionSetup.INPUT_ISP.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_MainISPModel(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_MainISPModel")) {
    	  		if((int) value == 1) {
    	  			PropulsionSetup.INPUT_ISPMODEL.setSelected(true);
    	  		}else {
    	  			PropulsionSetup.INPUT_ISPMODEL.setSelected(false);
    	  			}
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_MainISPMin(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_MainISPMin")) {
    			PropulsionSetup.INPUT_ISPMIN.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSMomX(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSMomX")) {
    			PropulsionSetup.INPUT_RCSX.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSMomY(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSMomY")) {
    			PropulsionSetup.INPUT_RCSY.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSMomZ(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSMomZ")) {
    			PropulsionSetup.INPUT_RCSZ.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSThrustX(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSThrustX")) {
    			PropulsionSetup.INPUT_RCSXTHRUST.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSThrustY(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSThrustY")) {
    			PropulsionSetup.INPUT_RCSYTHRUST.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSThrustZ(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSThrustZ")) {
    			PropulsionSetup.INPUT_RCSZTHRUST.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSProp(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSProp")) {
    			PropulsionSetup.INPUT_RCSTANK.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSISPX(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSISPX")) {
    			PropulsionSetup.INPUT_RCSXISP.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSISPY(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSISPY")) {
    			PropulsionSetup.INPUT_RCSYISP.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_RCSISPZ(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_RCSISPZ")) {
    			PropulsionSetup.INPUT_RCSZISP.setText(BlueBookVisual.df_X4.format(value)); 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_Env_DragModel(String identifier, double value) {
    	try {
    		if(identifier.equals("Env_DragModel")) {
    			  int index = (int) value; 
    				for(int j=0;j<AerodynamicSetup.DragModelSet.size();j++) {
    					if(j==index) {
    						AerodynamicSetup.DragModelSet.get(j).setSelected(true);
    					}
    				} 
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_Env_ConstCD(String identifier, double value) {
    	try {
    		if(identifier.equals("Env_ConstCD")) {
    			AerodynamicSetup.ConstantCD_INPUT.setText(""+value); 
    		}
    	} catch(Exception exp) {}
    }

    
    private static void read_SetIntegratorSettings(List<double[]> integratorSettings, int integratorIndx) {
    	try {
    		double[] settings =  integratorSettings.get(integratorIndx);
    		for(int i=0; i<settings.length;i++) {
    					if(i==0) {
    						SidePanelLeft.INPUT_IntegratorSetting_01.setText(""+settings[i]);	
    			} else if (i==1) {
    						SidePanelLeft.INPUT_IntegratorSetting_02.setText(""+settings[i]);	
    			} else if (i==2) {
    						SidePanelLeft.INPUT_IntegratorSetting_03.setText(""+settings[i]);	
    			} else if (i==3) {
    						SidePanelLeft.INPUT_IntegratorSetting_04.setText(""+settings[i]);	
    			} else if (i==4) {
    						SidePanelLeft.INPUT_IntegratorSetting_05.setText(""+settings[i]);	
    			} 
    		}
    	} catch(Exception exp) {}
    }
    
    
    private static void set_SurfaceAreaAndBallisticCoefficient() {
    	try {
    			double diameter = GeometryFrame.getDiameter();
    			AerodynamicSetupSpacecraft.INPUT_SURFACEAREA.setEditable(true);
    			AerodynamicSetupSpacecraft.INPUT_BALLISTICCOEFFICIENT.setEditable(false);
    			AerodynamicSetupSpacecraft.INPUT_SURFACEAREA.setText(""+BlueBookVisual.df_X4.format(PI*diameter*diameter/4)); 
    	} catch (Exception exp) {
    		System.out.println("surface diam failed");
    	}
    }
    
    private static void read_SC_COM(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_COM")) {
    			GeometryFrame.setCoM(value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_COT(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_COT")) {
    			GeometryFrame.setCoT(value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_SC_COP(String identifier, double value) {
    	try {
    		if(identifier.equals("SC_COP")) {
    			GeometryFrame.setCoP(value);
    		}
    	} catch(Exception exp) {}
    }
    
    private static List<double[]> read_Integ_853_MinStep(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_853_MinStep")) {
    			settings.get(0)[0] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_853_MaxStep(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_853_MaxStep")) {
    			settings.get(0)[1] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_853_AbsTol(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_853_AbsTol")) {
    			settings.get(0)[2] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_853_RelTol(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_853_RelTol")) {
    			settings.get(0)[3] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_RungKut_Step(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_RungKut_Step")) {
    			settings.get(1)[0] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }

    private static List<double[]> read_Integ_GraBul_MinStep(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_GraBul_MinStep")) {
    			settings.get(2)[0] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_GraBul_MaxStep(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_GraBul_MaxStep")) {
    			settings.get(2)[1] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_GraBul_AbsTol(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_GraBul_AbsTol")) {
    			settings.get(2)[2] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_GraBul_RelTol(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_GraBul_RelTol")) {
    			settings.get(2)[3] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_AdBash_Steps(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_AdBash_Steps")) {
    			settings.get(3)[0] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_AdBash_MinStep(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_AdBash_MinStep")) {
    			settings.get(3)[1] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_AdBash_MaxStep(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_AdBash_MaxStep")) {
    			settings.get(3)[2] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_AdBash_AbsTol(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_AdBash_AbsTol")) {
    			settings.get(3)[3] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static List<double[]> read_Integ_AdBash_RelTol(String identifier, double value, List<double[]> settings) {
    	try {
    		if(identifier.equals("Integ_AdBash_RelTol")) {
    			settings.get(3)[4] = value;
    		}
    	} catch(Exception exp) {}
    	return settings;
    }
    
    private static void passOverInertiaTensor(double[][] tensor) {
    	try {
    		InertiaGeometry.INPUT_IXX.setText(""+tensor[0][0]);
    		InertiaGeometry.INPUT_IXY.setText(""+tensor[0][1]);
    		InertiaGeometry.INPUT_IXZ.setText(""+tensor[0][2]);
    		InertiaGeometry.INPUT_IYX.setText(""+tensor[1][0]);
       		InertiaGeometry.INPUT_IYY.setText(""+tensor[1][1]);
       		InertiaGeometry.INPUT_IYZ.setText(""+tensor[1][2]);
       		InertiaGeometry.INPUT_IZX.setText(""+tensor[2][0]);
       		InertiaGeometry.INPUT_IZY.setText(""+tensor[2][1]);
       		InertiaGeometry.INPUT_IZZ.setText(""+tensor[2][2]);
    	} catch (Exception exp) {
    		
    	}
    }
    
    private static double[][] read_Init_IXX(String identifier, double value, double[][] tensor) {
    	try {
    		if(identifier.equals("Init_IXX")) {
    			tensor[0][0] = value;
    		}
    	} catch(Exception exp) {}
    	return tensor;
    }
    
    private static double[][] read_Init_IXY(String identifier, double value, double[][] tensor) {
    	try {
    		if(identifier.equals("Init_IXY")) {
    			tensor[0][1] = value;
    		}
    	} catch(Exception exp) {}
    	return tensor;
    }
    
    private static double[][] read_Init_IXZ(String identifier, double value, double[][] tensor) {
    	try {
    		if(identifier.equals("Init_IXZ")) {
    			tensor[0][2] = value;
    		}
    	} catch(Exception exp) {}
    	return tensor;
    }
    
    private static double[][] read_Init_IYX(String identifier, double value, double[][] tensor) {
    	try {
    		if(identifier.equals("Init_IYX")) {
    			tensor[1][0] = value;
    		}
    	} catch(Exception exp) {}
    	return tensor;
    }
    
    private static double[][] read_Init_IYY(String identifier, double value, double[][] tensor) {
    	try {
    		if(identifier.equals("Init_IYY")) {
    			tensor[1][1] = value;
    		}
    	} catch(Exception exp) {}
    	return tensor;
    }
    
    private static double[][] read_Init_IYZ(String identifier, double value, double[][] tensor) {
    	try {
    		if(identifier.equals("Init_IYZ")) {
    			tensor[1][2] = value;
    		}
    	} catch(Exception exp) {}
    	return tensor;
    }
    
    private static double[][] read_Init_IZX(String identifier, double value, double[][] tensor) {
    	try {
    		if(identifier.equals("Init_IZX")) {
    			tensor[2][0] = value;
    		}
    	} catch(Exception exp) {}
    	return tensor;
    }
    
    private static double[][] read_Init_IZY(String identifier, double value, double[][] tensor) {
    	try {
    		if(identifier.equals("Init_IZY")) {
    			tensor[2][1] = value;
    		}
    	} catch(Exception exp) {}
    	return tensor;
    }
    
    private static double[][] read_Init_IZZ(String identifier, double value, double[][] tensor) {
    	try {
    		if(identifier.equals("Init_IZZ")) {
    			tensor[2][2] = value;
    		}
    	} catch(Exception exp) {}
    	return tensor;
    }
}
