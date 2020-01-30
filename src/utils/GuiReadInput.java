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
import GUI.Dashboard.DashboardLeftPanel;
import GUI.DataStructures.InputFileSet;
import GUI.GeometryModel.GeometryFrame;
import GUI.SimulationSetup.BasicSetup.AttitudeSetting;
import GUI.SimulationSetup.BasicSetup.CenterPanelRight;
import GUI.SimulationSetup.BasicSetup.SidePanelLeft;
import Simulator_main.RealTimeSimulationCore;
import Simulator_main.DataSets.RealTimeResultSet;

public class GuiReadInput {

	
    public static List<RealTimeResultSet> READ_ResultSet() {
    	List<RealTimeResultSet> resultSet = new ArrayList<RealTimeResultSet>();
    	resultSet.clear();
    	// Read all data from file: 
	    FileInputStream fstream = null;
		try{ fstream = new FileInputStream(FilePaths.RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	              DataInputStream in = new DataInputStream(fstream);
	              BufferedReader br = new BufferedReader(new InputStreamReader(in));
	              String strLine;
	              try {
							while ((strLine = br.readLine()) != null )   {
								Object[] tokens = strLine.split(" ");
						     	RealTimeResultSet resultElement = new RealTimeResultSet();
							    double[] CartesianPosition = {Double.parseDouble((String) tokens[41]),
			 							   						Double.parseDouble((String) tokens[42]),
			 							   						Double.parseDouble((String) tokens[43])};
							    resultElement.setCartesianPosECEF(CartesianPosition);
							    resultElement.setEulerX(Double.parseDouble((String) tokens[57]));
							    resultElement.setEulerY(Double.parseDouble((String) tokens[58]));
							    resultElement.setEulerZ(Double.parseDouble((String) tokens[59]));
							    resultElement.setVelocity(Double.parseDouble((String) tokens[6]) );
							    resultElement.setTime(Double.parseDouble((String) tokens[0]));
							    resultElement.setFpa(Double.parseDouble((String) tokens[7]));
							    resultSet.add(resultElement);
							  
							  }
			       fstream.close();
			       in.close();
			       br.close();

	              } catch (NullPointerException | IOException eNPE) { 
	            	  System.out.println("Read raw data, Nullpointerexception");
					}catch(IllegalArgumentException eIAE) {
					  System.out.println("Read raw data, illegal argument error");
					}
	 return resultSet;
    }
    
    public static List<InputFileSet> readResultFileList(String filePath) throws IOException{
    	List<InputFileSet> newInputFileSetList = new ArrayList<InputFileSet>();

   		      	InputFileSet newInputFileSet = new InputFileSet();
   		      	newInputFileSet.setInputDataFilePath(filePath);
   		      	newInputFileSetList.add(newInputFileSet);

    return newInputFileSetList;
    }
    
    public static void READ_INPUT() throws IOException{
    	double InitialState = 0;
        int k = 0;
       	FileInputStream fstream = null; 
       	boolean isInit=true;
       if(isInit) {	
    	   			try {
    	   				fstream = new FileInputStream(FilePaths.Init_File);
    	   			} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading init.inp failed.");} 
        DataInputStream in = new DataInputStream(fstream);
        @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
   
        try {
        while ((strLine = br.readLine()) != null )   {
        	String fullLine = strLine;
        //	System.out.println(fullLine);
        	String[] tokens = strLine.split(" ");
        if (k==0){
        	InitialState = Double.parseDouble(tokens[0]);
         	DashboardLeftPanel.INDICATOR_LONG.setText(BlueBookVisual.decf.format(InitialState));
        		SidePanelLeft.INPUT_LONG_Rs.setText(BlueBookVisual.df_X4.format(InitialState));
        	} else if (k==1){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_LAT.setText(BlueBookVisual.decf.format( InitialState));
        		SidePanelLeft.INPUT_LAT_Rs.setText(BlueBookVisual.df_X4.format( InitialState));
        	} else if (k==2){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_ALT.setText(BlueBookVisual.decf.format( InitialState));
        		SidePanelLeft.INPUT_ALT_Rs.setText(BlueBookVisual.decf.format( InitialState));
        	} else if (k==3){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_VEL.setText(BlueBookVisual.decf.format(InitialState));
        		SidePanelLeft.INPUT_VEL_Rs.setText(BlueBookVisual.decf.format(InitialState));
        	} else if (k==4){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_FPA.setText(BlueBookVisual.decf.format(InitialState));
        		SidePanelLeft.INPUT_FPA_Rs.setText(BlueBookVisual.df_X4.format(InitialState));
        	} else if (k==5){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_AZI.setText(BlueBookVisual.decf.format(InitialState));
        		SidePanelLeft.INPUT_AZI_Rs.setText(BlueBookVisual.df_X4.format(InitialState));
        	} else if (k==6){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_M0.setText(BlueBookVisual.decf.format(InitialState));
            	BlueBookVisual.INPUT_M0.setText(BlueBookVisual.decf.format(InitialState));
        	} else if (k==7){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_INTEGTIME.setText(BlueBookVisual.decf.format(InitialState));
        		CenterPanelRight.setGlobalTime(InitialState);
        		 //MODEL_EventHandler.setValueAt(BlueBookVisual.decf.format(InitialState), 0, 1);
        	} else if (k==8){
            	InitialState = Double.parseDouble(tokens[0]);
        		int Integ_indx = (int) InitialState;
        		SidePanelLeft.Integrator_chooser.setSelectedIndex(Integ_indx);
        } else if (k==9){
        	InitialState = Double.parseDouble(tokens[0]);
        		int Target_indx = (int) InitialState;
        		BlueBookVisual.indx_target = (int) InitialState; 
        		BlueBookVisual.RM = BlueBookVisual.DATA_MAIN[BlueBookVisual.indx_target][0];
        		DashboardLeftPanel.INDICATOR_TARGET.setText(BlueBookVisual.Target_Options[BlueBookVisual.indx_target]);
        		CenterPanelRight.setTargetIndx(Target_indx);
                if(BlueBookVisual.indx_target==0) {
                	DashboardLeftPanel.INDICATOR_TARGET.setBorder(BlueBookVisual.Earth_border);
                } else if(BlueBookVisual.indx_target==1){
                	DashboardLeftPanel.INDICATOR_TARGET.setBorder(BlueBookVisual.Moon_border);
                } else if(BlueBookVisual.indx_target==2){
                	DashboardLeftPanel.INDICATOR_TARGET.setBorder(BlueBookVisual.Mars_border);
                } else if(BlueBookVisual.indx_target==3){
                	DashboardLeftPanel.INDICATOR_TARGET.setBorder(BlueBookVisual.Venus_border);
                }
            } else if (k==10){
            	//InitialState = Double.parseDouble(tokens[0]);
	            //	INPUT_WRITETIME.setText(BlueBookVisual.decf.format(InitialState)); // write dt
	        	InitialState = Double.parseDouble(tokens[0]);
		    	CenterPanelRight.setGlobalFrequency(InitialState);
            } else if (k==11){
            	InitialState = Double.parseDouble(tokens[0]);
            	SidePanelLeft.INPUT_REFELEV.setText(BlueBookVisual.decf.format(InitialState));       // Reference Elevation
		    } else if (k==12) {
		    	    // Time format :
		    		String UTCTime = fullLine;
		    		//System.out.println("handover string: "+UTCTime);
		    		SidePanelLeft.timePanel.updateTimeFromString(UTCTime);	    	
		    } else if (k==13) {
	        	InitialState = Double.parseDouble(tokens[0]);
			    	int Integ_indx = (int) InitialState;
			    	CenterPanelRight.setVelocityCoordinateSystem(Integ_indx);
		    } else if (k==14) {
	        	InitialState = Double.parseDouble(tokens[0]);
			    	int Integ_indx = (int) InitialState;
			    	CenterPanelRight.setDOF_System(Integ_indx);
		    } else if (k==15) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	SidePanelLeft.INPUT_AngularRate_X.setText(BlueBookVisual.decAngularRate.format(InitialState));
		    } else if (k==16) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	SidePanelLeft.INPUT_AngularRate_Y.setText(BlueBookVisual.decAngularRate.format(InitialState));
		    } else if (k==17) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	SidePanelLeft.INPUT_AngularRate_Z.setText(BlueBookVisual.decAngularRate.format(InitialState));
		    } else if(k==18) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    CenterPanelRight.setControllerFrequency(InitialState);
		    }else if(k==19) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	CenterPanelRight.setGlobalFrequency(InitialState);
		    }
        	k++;
        }
        in.close();
        br.close();
        fstream.close();
        } catch (NullPointerException eNPE) { System.out.println(eNPE);}
       } else {
    	   	GuiReadInput.readINP();
       }
       	//int k=0;
    //------------------------------------------------------------------
    // Read from PROP
    try {
        fstream = new FileInputStream(FilePaths.Prop_File);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading prop.inp failed.");} 
  DataInputStream in3 = new DataInputStream(fstream);
  @SuppressWarnings("resource")
  BufferedReader br4 = new BufferedReader(new InputStreamReader(in3));
  k = 0;
  String strLine3;
  try {
  while ((strLine3 = br4.readLine()) != null )   {
  	String[] tokens = strLine3.split(" ");
  	if(tokens[0].isEmpty()==false) {
  	InitialState = Double.parseDouble(tokens[0]);
  	} else {
  		InitialState =0; 
  	}
    if (k==0){
    	BlueBookVisual.INPUT_ISP.setText(BlueBookVisual.df_X4.format(InitialState)); 
  	} else if (k==1){
  		BlueBookVisual.INPUT_PROPMASS.setText(BlueBookVisual.df_X4.format(InitialState)); 
  	//System.out.println(RM);
  	} else if (k==2){
  		BlueBookVisual.INPUT_THRUSTMAX.setText(BlueBookVisual.df_X4.format(InitialState));
  	} else if (k==3){
  		BlueBookVisual.INPUT_THRUSTMIN.setText(BlueBookVisual.df_X4.format(InitialState)); 
  	} else if (k==4){
  		int value = (int) InitialState; 
  		if(value==1) {BlueBookVisual.INPUT_ISPMODEL.setSelected(true);}else {BlueBookVisual.INPUT_ISPMODEL.setSelected(false);}
  	} else if (k==5){
  		BlueBookVisual.INPUT_ISPMIN.setText(BlueBookVisual.df_X4.format(InitialState)); 
  	} else if (k==6){
  		BlueBookVisual.INPUT_RCSX.setText(BlueBookVisual.df_X4.format(InitialState)); 
  	} else if (k==7){
  		BlueBookVisual.INPUT_RCSY.setText(BlueBookVisual.df_X4.format(InitialState));
  	} else if (k==8) {
  		BlueBookVisual.INPUT_RCSZ.setText(BlueBookVisual.df_X4.format(InitialState));
  	} else if (k==9) {
  		BlueBookVisual.INPUT_RCSXTHRUST.setText(BlueBookVisual.df_X4.format(InitialState));
  	} else if (k==10) {
  		BlueBookVisual.INPUT_RCSYTHRUST.setText(BlueBookVisual.df_X4.format(InitialState));
  	} else if (k==11) {
  		BlueBookVisual.INPUT_RCSZTHRUST.setText(BlueBookVisual.df_X4.format(InitialState));
  	} else if(k==12) {
  		BlueBookVisual.INPUT_RCSTANK.setText(BlueBookVisual.df_X4.format(InitialState));
  	} else if(k==13) {
  		BlueBookVisual.INPUT_RCSXISP.setText(BlueBookVisual.df_X4.format(InitialState));
  	} else if(k==14) {
  		BlueBookVisual.INPUT_RCSYISP.setText(BlueBookVisual.df_X4.format(InitialState));
  	} else if(k==15) {
  		BlueBookVisual.INPUT_RCSZISP.setText(BlueBookVisual.df_X4.format(InitialState));
  	}
  	k++;
  }
  in3.close();
  br4.close();
  fstream.close();
  } catch (NullPointerException eNPE) { System.out.println(eNPE);}  
  //------------------------------------------------------------------
  // Read from AERO
  try {
      fstream = new FileInputStream(FilePaths.Aero_file);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading aeroBasic.inp failed.");} 
DataInputStream in55 = new DataInputStream(fstream);
@SuppressWarnings("resource")
BufferedReader br55 = new BufferedReader(new InputStreamReader(in55));
k = 0;
String strLine55;
try {
while ((strLine55 = br55.readLine()) != null )   {
	String[] tokens = strLine55.split(" ");
	if(tokens[0].isEmpty()==false) {
	InitialState = Double.parseDouble(tokens[0]);
	} else {
		InitialState =0; 
	}
    if (k==0){
	  int index = (int) InitialState; 
		for(int j=0;j<BlueBookVisual.DragModelSet.size();j++) {
			if(j==index) {
				BlueBookVisual.DragModelSet.get(j).setSelected(true);
			}
		}
	} else if (k==1){
		BlueBookVisual.ConstantCD_INPUT.setText(""+InitialState);
	//System.out.println(RM);
	} else if (k==2){
		BlueBookVisual.INPUT_RB.setText(""+(InitialState)); 
	} else if (k==5) {
		
	}
	k++;
}
in55.close();
br55.close();
fstream.close();
} catch (NullPointerException eNPE) { System.out.println(eNPE);}  
//--------------------------------------------------------------------------------------------------------
  //--------------------------------------------------------------------------------------------------------
  // Integrator settings 
  //--------------------------------------------------------------------------------------------------------
	String integ_file = null;  
	if(	SidePanelLeft.Integrator_chooser.getSelectedIndex()==0) {
		integ_file = FilePaths.INTEG_File_01; 
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==1) {
		integ_file = FilePaths.INTEG_File_02; 
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==2) {
		integ_file = FilePaths.INTEG_File_03; ;
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==3) {
		integ_file = FilePaths.INTEG_File_04; 
	}
    try {
        fstream = new FileInputStream(integ_file);
} catch(IOException | NullPointerException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading integrator input failed. " + integ_file);} 
  in3 = new DataInputStream(fstream);
  @SuppressWarnings("resource")
  BufferedReader br5 = new BufferedReader(new InputStreamReader(in3));
  k = 0;
  try {
  while ((strLine3 = br5.readLine()) != null )   {
  	String[] tokens = strLine3.split(" ");
  	InitialState = Double.parseDouble(tokens[0]);
    if (k==0){
    		SidePanelLeft.INPUT_IntegratorSetting_01.setText(""+(InitialState)); 
  	} else if (k==1){
  		SidePanelLeft.INPUT_IntegratorSetting_02.setText(""+(InitialState)); 
  	} else if (k==2){
  		SidePanelLeft.INPUT_IntegratorSetting_03.setText(""+(InitialState)); 
  	} else if (k==3){
  		SidePanelLeft.INPUT_IntegratorSetting_04.setText(""+(InitialState)); 
  	} else if (k==4){
  		SidePanelLeft.INPUT_IntegratorSetting_05.setText(""+(InitialState)); 
  	} else if (k==5){

  	} else if (k==6){

  	} else if (k==7){

  	}
  	k++;
  }
  in3.close();
  br5.close();
  fstream.close();
  } catch (NullPointerException eNPE) { System.out.println(eNPE);}  
  //-------------------------------------------------------------------------------------------------------------------
  try {
      fstream = new FileInputStream(FilePaths.SC_file);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading integrator input failed. " + integ_file);} 
in3 = new DataInputStream(fstream);
@SuppressWarnings("resource")
BufferedReader br6 = new BufferedReader(new InputStreamReader(in3));
k = 0;
try {
while ((strLine3 = br6.readLine()) != null )   {
	String[] tokens = strLine3.split(" ");
	InitialState = Double.parseDouble(tokens[0]);
  if (k==0){
	  BlueBookVisual.INPUT_SURFACEAREA.setText(""+(InitialState)); 
  		if(InitialState!=0) {
  			BlueBookVisual.RB_SurfaceArea.setSelected(true);
  			BlueBookVisual.INPUT_SURFACEAREA.setEditable(true);
  			BlueBookVisual.INPUT_BALLISTICCOEFFICIENT.setEditable(false);	
  		}
	} else if (k==1){
		BlueBookVisual.INPUT_BALLISTICCOEFFICIENT.setText(""+(InitialState)); 
  		if(InitialState!=0) {
  			BlueBookVisual.RB_BallisticCoefficient.setSelected(true);
  			BlueBookVisual.INPUT_SURFACEAREA.setEditable(false);
  			BlueBookVisual.INPUT_BALLISTICCOEFFICIENT.setEditable(true);	
  		}
	} else if (k==2){
		
	} else if (k==3){
		GeometryFrame.setCoM(InitialState);
	} else if (k==4){
		GeometryFrame.setCoT(InitialState);
	} else if (k==5){
		GeometryFrame.setCoP(InitialState);
	} else if (k==6){

	} else if (k==7){

	}
	k++;
}
BlueBookVisual.EvaluateSurfaceAreaSetup() ;
in3.close();
br5.close();
fstream.close();
} catch (NullPointerException eNPE) { System.out.println(eNPE);} 
    }
//------------------------------------------------------------------------
    public static void READ_INERTIA() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(FilePaths.INERTIA_File));
	       String strLine;
	       int j=0;
	       try {
	       while ((strLine = br.readLine()) != null )   {
		       	String[] tokens = strLine.split(" ");
		       	if(j==0) {
		       		BlueBookVisual.INPUT_IXX.setText(tokens[0]);
		       		BlueBookVisual.INPUT_IXY.setText(tokens[1]);
		       		BlueBookVisual.INPUT_IXZ.setText(tokens[2]);
		       	} else if (j==1) {
		       		BlueBookVisual.INPUT_IYX.setText(tokens[0]);
		       		BlueBookVisual.INPUT_IYY.setText(tokens[1]);
		       		BlueBookVisual.INPUT_IYZ.setText(tokens[2]);
		       	} else if (j==2) {
		       		BlueBookVisual.INPUT_IZX.setText(tokens[0]);
		       		BlueBookVisual.INPUT_IZY.setText(tokens[1]);
		       		BlueBookVisual.INPUT_IZZ.setText(tokens[2]);
		       	}
		       	
		       	j++;
	       }
	       br.close();
	       } catch(NullPointerException eNPE) { System.out.println(eNPE);}
    }
//------------------------------------------------------------------------
    public static void READ_INTEG() {
  	  //--------------------------------------------------------------------------------------------------------
  	  // 			Integrator settings 
  	  //--------------------------------------------------------------------------------------------------------
  		String integ_file = null;
  		 FileInputStream  fstream = null; 
  		if(	SidePanelLeft.Integrator_chooser.getSelectedIndex()==0) {
  			integ_file = FilePaths.INTEG_File_01; 
  		} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==1) {
  			integ_file = FilePaths.INTEG_File_02; 
  		} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==2) {
  			integ_file = FilePaths.INTEG_File_03; ;
  		} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==3) {
  			integ_file = FilePaths.INTEG_File_04; 
  		}
  	    try {
  	         fstream = new FileInputStream(integ_file);
  	} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading integrator input failed. " + integ_file);} 
  	  DataInputStream in3 = new DataInputStream(fstream);
  	  @SuppressWarnings("resource")
  	  BufferedReader br5 = new BufferedReader(new InputStreamReader(in3));
  	  int k = 0;
  	  String strLine3="" ; 
  	  try {
  	  try {
			while ((strLine3 = br5.readLine()) != null )   {
			  	String[] tokens = strLine3.split(" ");
			  	double InitialState = Double.parseDouble(tokens[0]);
			    if (k==0){
			    		SidePanelLeft.INPUT_IntegratorSetting_01.setText(""+(InitialState)); 
			  	} else if (k==1){
			  		SidePanelLeft.INPUT_IntegratorSetting_02.setText(""+(InitialState)); 
			  	} else if (k==2){
			  		SidePanelLeft.INPUT_IntegratorSetting_03.setText(""+(InitialState)); 
			  	} else if (k==3){
			  		SidePanelLeft.INPUT_IntegratorSetting_04.setText(""+(InitialState)); 
			  	} else if (k==4){
			  		SidePanelLeft.INPUT_IntegratorSetting_05.setText(""+(InitialState)); 
			  	} else if (k==5){

			  	} else if (k==6){

			  	} else if (k==7){

			  	}
			  	k++;
			  }
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	  try {
  		  in3.close();
	    	  br5.close();
	    	  fstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	  } catch (NullPointerException eNPE) { System.out.println(eNPE);}  	
  }
//------------------------------------------------------------------------ 
  public static void READ_InitialAttitude() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(FilePaths.InitialAttitude_File));
	       String strLine;
	       int j=0;
	       try {
	       while ((strLine = br.readLine()) != null )   {
		       	String[] tokens = strLine.split(" ");
		       	if(j==0) {
		       		AttitudeSetting.INPUT_Quarternion1.setText(tokens[0]);
		       	} else if (j==1) {
		       		AttitudeSetting.INPUT_Quarternion2.setText(tokens[0]);
		       	} else if (j==2) {
		       		AttitudeSetting.INPUT_Quarternion3.setText(tokens[0]);
		       	} else if (j==3) {
		       		AttitudeSetting.INPUT_Quarternion4.setText(tokens[0]);
		       	}	       	
		       	j++;
	       }
	       br.close();
	       } catch(NullPointerException eNPE) { System.out.println(eNPE);}
  }
//------------------------------------------------------------------------
    public static void readINP() {

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
    				    	read_InitLONG( identifier, value);
    				    	read_InitLAT( identifier, value);
    				    	read_InitRAD( identifier, value);
    				    	read_InitVEL( identifier, value);
    				    	read_InitFPA( identifier, value);
    				    	read_InitAZI( identifier, value);
    				    	read_InitMass( identifier, value);
    				    	read_IntegMaxTime( identifier, value);
    				    	read_IntegIntegrator( identifier, value);
    				    	read_EnvCenterBody( identifier, value);
    				    	read_IntegFrequency( identifier, value);
    				    	read_EnvRefElev( identifier, value);
    				    	read_InitTime( identifier, value);
    				    	read_IntegVelVector( identifier, value);
    				    	read_IntegDoF( identifier, value);
    				    	read_InitAngRateX( identifier, value);
    				    	read_InitAngRateY( identifier, value);
    				    	read_InitAngRateZ( identifier, value);
    		    }
        fstream.close();
        in.close();
        br.close();

       // System.out.println("READ: Propulsion setup successful.");
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
    
    private static void read_InitRAD(String identifier, double value) {
    	try {
    		if(identifier.equals("Init_RAD")) {
    			int target=0;
    			try {
    				target = CenterPanelRight.getTargetIndx();
    			} catch (Exception exction) {System.out.println(exction);}
    	double radius = RealTimeSimulationCore.DATA_MAIN[target][0];
                	DashboardLeftPanel.INDICATOR_ALT.setText(BlueBookVisual.decf.format( value-radius));
            		SidePanelLeft.INPUT_ALT_Rs.setText(BlueBookVisual.decf.format( value-radius));
    		}
    	} catch(Exception exp) {}
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
    			BlueBookVisual.INPUT_M0.setText(BlueBookVisual.decf.format(value));
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
    		}
    	} catch(Exception exp) {}
    }
    
    private static void read_EnvCenterBody(String identifier, double value) {
    	try {
    		if(identifier.equals("Env_CenterBody")) {
    			int Target_indx = (int) value;

    			DashboardLeftPanel.INDICATOR_TARGET.setText(BlueBookVisual.Target_Options[Target_indx]);
    			CenterPanelRight.setTargetIndx(Target_indx);
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
/*
 *     try {
        fstream = new FileInputStream(FilePaths.Prop_File);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading prop.inp failed.");} 
  DataInputStream in3 = new DataInputStream(fstream);
  @SuppressWarnings("resource")
  BufferedReader br4 = new BufferedReader(new InputStreamReader(in3));
  k = 0;
  String strLine3;
  try {
  while ((strLine3 = br4.readLine()) != null )   {
  	String[] tokens = strLine3.split(" ");
  	if(tokens[0].isEmpty()==false) {
  	InitialState = Double.parseDouble(tokens[0]);
  	} else {
  		InitialState =0; 
  	}
    if (k==0){
    	INPUT_ISP.setText(df_X4.format(InitialState)); 
  	} else if (k==1){
  		INPUT_PROPMASS.setText(df_X4.format(InitialState)); 
  	//System.out.println(RM);
  	} else if (k==2){
  		INPUT_THRUSTMAX.setText(df_X4.format(InitialState));
  	} else if (k==3){
  		INPUT_THRUSTMIN.setText(df_X4.format(InitialState)); 
  	} else if (k==4){
  		int value = (int) InitialState; 
  		if(value==1) {INPUT_ISPMODEL.setSelected(true);}else {INPUT_ISPMODEL.setSelected(false);}
  	} else if (k==5){
  		INPUT_ISPMIN.setText(df_X4.format(InitialState)); 
  	} else if (k==6){
  		INPUT_RCSX.setText(df_X4.format(InitialState)); 
  	} else if (k==7){
  		INPUT_RCSY.setText(df_X4.format(InitialState));
  	} else if (k==8) {
  		INPUT_RCSZ.setText(df_X4.format(InitialState));
  	} else if (k==9) {
  		INPUT_RCSXTHRUST.setText(df_X4.format(InitialState));
  	} else if (k==10) {
  		INPUT_RCSYTHRUST.setText(df_X4.format(InitialState));
  	} else if (k==11) {
  		INPUT_RCSZTHRUST.setText(df_X4.format(InitialState));
  	} else if(k==12) {
  		INPUT_RCSTANK.setText(df_X4.format(InitialState));
  	} else if(k==13) {
  		INPUT_RCSXISP.setText(df_X4.format(InitialState));
  	} else if(k==14) {
  		INPUT_RCSYISP.setText(df_X4.format(InitialState));
  	} else if(k==15) {
  		INPUT_RCSZISP.setText(df_X4.format(InitialState));
  	}
  	k++;
  }
  in3.close();
  br4.close();
  fstream.close();
  } catch (NullPointerException eNPE) { System.out.println(eNPE);}  
  //------------------------------------------------------------------
  // Read from AERO
  try {
      fstream = new FileInputStream(FilePaths.Aero_file);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading aeroBasic.inp failed.");} 
DataInputStream in55 = new DataInputStream(fstream);
@SuppressWarnings("resource")
BufferedReader br55 = new BufferedReader(new InputStreamReader(in55));
k = 0;
String strLine55;
try {
while ((strLine55 = br55.readLine()) != null )   {
	String[] tokens = strLine55.split(" ");
	if(tokens[0].isEmpty()==false) {
	InitialState = Double.parseDouble(tokens[0]);
	} else {
		InitialState =0; 
	}
    if (k==0){
	  int index = (int) InitialState; 
		for(int j=0;j<DragModelSet.size();j++) {
			if(j==index) {
				DragModelSet.get(j).setSelected(true);
			}
		}
	} else if (k==1){
		ConstantCD_INPUT.setText(""+InitialState);
	//System.out.println(RM);
	} else if (k==2){
		INPUT_RB.setText(""+(InitialState)); 
	} else if (k==5) {
		
	}
	k++;
}
in55.close();
br55.close();
fstream.close();
} catch (NullPointerException eNPE) { System.out.println(eNPE);}  
//--------------------------------------------------------------------------------------------------------
  //--------------------------------------------------------------------------------------------------------
  // Integrator settings 
  //--------------------------------------------------------------------------------------------------------
	String integ_file = null;  
	if(	SidePanelLeft.Integrator_chooser.getSelectedIndex()==0) {
		integ_file = FilePaths.INTEG_File_01; 
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==1) {
		integ_file = FilePaths.INTEG_File_02; 
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==2) {
		integ_file = FilePaths.INTEG_File_03; ;
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==3) {
		integ_file = FilePaths.INTEG_File_04; 
	}
    try {
        fstream = new FileInputStream(integ_file);
} catch(IOException | NullPointerException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading integrator input failed. " + integ_file);} 
  in3 = new DataInputStream(fstream);
  @SuppressWarnings("resource")
  BufferedReader br5 = new BufferedReader(new InputStreamReader(in3));
  k = 0;
  try {
  while ((strLine3 = br5.readLine()) != null )   {
  	String[] tokens = strLine3.split(" ");
  	InitialState = Double.parseDouble(tokens[0]);
    if (k==0){
    		SidePanelLeft.INPUT_IntegratorSetting_01.setText(""+(InitialState)); 
  	} else if (k==1){
  		SidePanelLeft.INPUT_IntegratorSetting_02.setText(""+(InitialState)); 
  	} else if (k==2){
  		SidePanelLeft.INPUT_IntegratorSetting_03.setText(""+(InitialState)); 
  	} else if (k==3){
  		SidePanelLeft.INPUT_IntegratorSetting_04.setText(""+(InitialState)); 
  	} else if (k==4){
  		SidePanelLeft.INPUT_IntegratorSetting_05.setText(""+(InitialState)); 
  	} else if (k==5){

  	} else if (k==6){

  	} else if (k==7){

  	}
  	k++;
  }
  in3.close();
  br5.close();
  fstream.close();
  } catch (NullPointerException eNPE) { System.out.println(eNPE);}  
  //-------------------------------------------------------------------------------------------------------------------
  try {
      fstream = new FileInputStream(FilePaths.SC_file);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading integrator input failed. " + integ_file);} 
in3 = new DataInputStream(fstream);
@SuppressWarnings("resource")
BufferedReader br6 = new BufferedReader(new InputStreamReader(in3));
k = 0;
try {
while ((strLine3 = br6.readLine()) != null )   {
	String[] tokens = strLine3.split(" ");
	InitialState = Double.parseDouble(tokens[0]);
  if (k==0){
  		INPUT_SURFACEAREA.setText(""+(InitialState)); 
  		if(InitialState!=0) {
  			RB_SurfaceArea.setSelected(true);
  			INPUT_SURFACEAREA.setEditable(true);
  			INPUT_BALLISTICCOEFFICIENT.setEditable(false);	
  		}
	} else if (k==1){
		INPUT_BALLISTICCOEFFICIENT.setText(""+(InitialState)); 
  		if(InitialState!=0) {
  			RB_BallisticCoefficient.setSelected(true);
  			INPUT_SURFACEAREA.setEditable(false);
  			INPUT_BALLISTICCOEFFICIENT.setEditable(true);	
  		}
	} else if (k==2){
		
	} else if (k==3){
		GeometryFrame.setCoM(InitialState);
	} else if (k==4){
		GeometryFrame.setCoT(InitialState);
	} else if (k==5){
		GeometryFrame.setCoP(InitialState);
	} else if (k==6){

	} else if (k==7){

	}
	k++;
 */
}
