package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import GUI.AerdoynamicSetup.AerodynamicSetup;
import GUI.AerdoynamicSetup.AerodynamicSetupSpacecraft;
import GUI.Dashboard.ChartSetting;
import GUI.GeometryModel.GeometryFrame;
import GUI.PropulsionSetup.PropulsionSetup;
import GUI.SimulationSetup.BasicSetup.AttitudeSetting;
import GUI.SimulationSetup.BasicSetup.CenterPanelRight;
import GUI.SimulationSetup.BasicSetup.SidePanelLeft;
import Simulator_main.RealTimeSimulationCore;

public class WriteInput {

	private static boolean isWritable=false;
	private static String delimiter = " ";
	
    public static void writeDashboradSetting(List<ChartSetting> chartSetting) {

        try {
            File fac = new File(ReadInput.dashboardSettingFile);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }

            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i< chartSetting.size(); i++)
            {
            	wr.write(chartSetting.get(i).type+BlueBookVisual.BB_delimiter+chartSetting.get(i).x+BlueBookVisual.BB_delimiter+chartSetting.get(i).y+System.getProperty( "line.separator" ));
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static void WRITE_SequenceFile() {
        try {
            File fac = new File(FilePaths.sequenceFile);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            String fcSeparator="|FlightControllerElements|";
            String eventSeparator="|EventManagementElements";
            String endSeparator="|EndElement|";
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<BlueBookVisual.getSequenceContentList().size(); i++)
            {
        		int ID = BlueBookVisual.getSequenceContentList().get(i).getSequenceID();
            	String sequenceContent = ID+" "+
						 			 BlueBookVisual.getSequenceContentList().get(i).getSequenceName()+" "+
            							 fcSeparator	+" "+ 
            							 BlueBookVisual.getSequenceContentList().get(i).getFlightControllerSelect().getSelectedIndex()+" "+
            							 fcSeparator	+" "+ 
            							 eventSeparator+" "+ 
            							 BlueBookVisual.getSequenceContentList().get(i).getEventSelect().getSelectedIndex()+" "+
            							 eventSeparator+" "+ 
            							 endSeparator+" "+ 
            							 BlueBookVisual.getSequenceContentList().get(i).getEndSelect().getSelectedIndex()+" "+
            							 BlueBookVisual.getSequenceContentList().get(i).getValueEnd().getText()+" "
            							 +endSeparator+" ";
            	
            	wr.write(sequenceContent+System.getProperty( "line.separator" ));
		    }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static boolean isWritable() {
		return isWritable;
	}

	public static void setWritable(boolean isWritable) {
		WriteInput.isWritable = isWritable;
	}

	public static void writeInputFile(String initFilePath) {
    	if(isWritable) {
        try {
            File fac = new File(initFilePath);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }


            FileWriter fileWriter = new FileWriter(fac);
            /**
             * 
             * 		List of variables to write 
             * 
             *///---------------------------------------------------
            /**
             * 
             * 
             * 		Initial State parameters
             */
            		// position vector
	            	write_InitLONG(fileWriter, delimiter);
	            	write_InitLAT(fileWriter, delimiter);
	            	write_InitRAD(fileWriter, delimiter);
	            	// velocity vector
	            	write_InitVEL(fileWriter, delimiter);
	            	write_InitFPA(fileWriter, delimiter);
	            	write_InitAZI(fileWriter, delimiter);
	            	
	            	write_InitMASS(fileWriter, delimiter);
	            	write_InitTIME(fileWriter, delimiter);
	            	// attitude vector
	            	write_InitQuartW(fileWriter, delimiter);
	            	write_InitQuartX(fileWriter, delimiter);
	            	write_InitQuartY(fileWriter, delimiter);
	            	write_InitQuartZ(fileWriter, delimiter);
	            	// rotaional rates
	            	write_InitAngRateX(fileWriter, delimiter);
	            	write_InitAngRateY(fileWriter, delimiter);
	            	write_InitAngRateZ(fileWriter, delimiter);
	            	// Inertia Tensor
	            	write_InitInertia(fileWriter,  delimiter);
	            	/**
	            	 * 
	            	 * 		Integrator Parameters
	            	 */
	            	write_IntegMaxTime(fileWriter,  delimiter);
	            	write_IntegIntegrator(fileWriter,  delimiter);
	            	write_IntegVelVector( fileWriter, delimiter);
	            	write_IntegFrequency( fileWriter, delimiter);
	            	write_IntegDoF( fileWriter, delimiter);
	            	write_Integ853_MinStep( fileWriter, delimiter);
	            	write_Integ853_MaxStep( fileWriter, delimiter);
	            	write_Integ853_AbsTol( fileWriter, delimiter);
	            	write_Integ853_RelTol( fileWriter, delimiter);
	            	write_IntegRungKut_Step( fileWriter, delimiter);
	            	write_IntegGraBul_MinStep( fileWriter, delimiter);
	            	write_IntegGraBul_MiaxStep( fileWriter, delimiter);
	            	write_IntegGraBul_AbsTol( fileWriter, delimiter);
	            	write_IntegGraBul_RelTol( fileWriter, delimiter);
	            	write_IntegAdBash_Steps( fileWriter, delimiter);
	            	write_IntegAdBash_MinStep( fileWriter, delimiter);
	            	write_IntegAdBash_MaxStep( fileWriter, delimiter);
	            	write_IntegAdBash_AbsTol( fileWriter, delimiter);
	            	write_IntegAdBash_RelTol( fileWriter, delimiter);	            	
	            	/**
	            	 * 
	            	 * 		Environmental Parameters
	            	 */
	            	write_Env_CenterBody( fileWriter, delimiter);
	            	write_Env_RefElev( fileWriter, delimiter);
	            	write_Env_DragModel( fileWriter, delimiter);
	            	write_Env_ConstCD( fileWriter, delimiter);
	            	write_Env_ParModel( fileWriter, delimiter);
	            	write_Env_ParCD( fileWriter, delimiter);	            	
	            	/**
	            	 * 
	            	 * 		Spacecraft parameters
	            	 */
	            	write_SC_SurfArea( fileWriter, delimiter);
	            	write_SC_ParDiam( fileWriter, delimiter);
	            	write_SC_COM( fileWriter, delimiter);
	            	write_SC_COT( fileWriter, delimiter);
	            	write_SC_COP( fileWriter, delimiter);
	            	write_SC_Height( fileWriter, delimiter);
	            	write_SC_BodyRadius( fileWriter, delimiter);
	            	write_SC_MainISP( fileWriter, delimiter);
	            	write_SC_MainProp( fileWriter, delimiter);
	            	write_SC_MainThrustMax( fileWriter, delimiter);
	            	write_SC_MainThrustMin( fileWriter, delimiter);
	            	write_SC_MainISPModel( fileWriter, delimiter);
	            	write_SC_MainISPMin( fileWriter, delimiter);
	            	write_SC_RCSMomX( fileWriter, delimiter);
	            	write_SC_RCSMomY( fileWriter, delimiter);
	            	write_SC_RCSMomZ( fileWriter, delimiter);
	            	write_SC_RCSThrustX( fileWriter, delimiter);
	            	write_SC_RCSThrustY( fileWriter, delimiter);
	            	write_SC_RCSThrustZ( fileWriter, delimiter);
	            	write_SC_RCSProp( fileWriter, delimiter);
	            	write_SC_RCSISPX( fileWriter, delimiter);
	            	write_SC_RCSISPY( fileWriter, delimiter);
	            	write_SC_RCSISPZ( fileWriter, delimiter);
	            	
	            	
            fileWriter.close();
        } catch (IOException eIO) {
        		System.out.println("Error: Writing input file failed.");
            	System.out.println(eIO);
        }
    	}
    }
    
    private static FileWriter write_InitLONG(FileWriter fileWriter, String delimiter) throws IOException {
    	
    		String identifier = "Init_LONG";
    		double value =0;
    		try {
		 value = Double.parseDouble(	SidePanelLeft.INPUT_LONG_Rs.getText()) ;
    		} catch (Exception exp) {}
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_InitLAT(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_LAT";
	double value =0;
	try {
	 value = Double.parseDouble(	SidePanelLeft.INPUT_LAT_Rs.getText()) ;
	} catch (Exception exp ) {}
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitRAD(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_RAD";
	int target=0;
			try {
				target = CenterPanelRight.getTargetIndx();
			} catch (Exception exction) { }
	double radius = RealTimeSimulationCore.DATA_MAIN[target][0];
	double value =0;
	try {
	 value = radius + Double.parseDouble(	SidePanelLeft.INPUT_ALT_Rs.getText()) ;
	} catch (Exception exp ) {}
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitVEL(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_VEL";
	double value =0;
	try {
	 value = Double.parseDouble(	SidePanelLeft.INPUT_VEL_Rs.getText()) ;
	} catch (Exception exp ) {}
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitFPA(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_FPA";
	double value =0;
	try {
	 value = Double.parseDouble(	SidePanelLeft.INPUT_FPA_Rs.getText()) ;
	} catch (Exception exp) {}
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitAZI(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_AZI";
	double value =0;
	try {
	 value = Double.parseDouble(	SidePanelLeft.INPUT_AZI_Rs.getText()) ;
	} catch (Exception exp ) {}
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitMASS(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_Mass";
	double value =0;
	try {
	 value = Double.parseDouble(BlueBookVisual.INPUT_M0.getText()) ;
	} catch (Exception exp) {}
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitTIME(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_Time";
	double value =0;
	try {
	 //value = CenterPanelRight.getGlobalTime() ;
	 SidePanelLeft.timePanel.getJ2000Time();
	} catch (Exception exp ) {}
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitQuartW(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_QuartW";
	double value = 0 ;
	 if(!AttitudeSetting.INPUT_Quarternion1.getText().equals("")) {
		  value = Double.parseDouble(AttitudeSetting.INPUT_Quarternion1.getText()); 
	 }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitQuartX(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_QuartX";
	double value = 0 ;
	 if(!AttitudeSetting.INPUT_Quarternion2.getText().equals("")) {
		  value = Double.parseDouble(AttitudeSetting.INPUT_Quarternion2.getText()); 
	 }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitQuartY(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_QuartY";
	double value = 0 ;
	 if(!AttitudeSetting.INPUT_Quarternion3.getText().equals("")) {
		  value = Double.parseDouble(AttitudeSetting.INPUT_Quarternion3.getText()); 
	 }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitQuartZ(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_QuartZ";
	double value = 0 ;
	 if(!AttitudeSetting.INPUT_Quarternion4.getText().equals("")) {
		  value = Double.parseDouble(AttitudeSetting.INPUT_Quarternion4.getText()); 
	 }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitAngRateX(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_AngRateX";
	double value =0;
	try {
		value = Double.parseDouble(	SidePanelLeft.INPUT_AngularRate_X.getText()) ;
	} catch (Exception exp ) {
		
	}
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitAngRateY(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_AngRateY";
	double value =0;
	try{
		value = Double.parseDouble(	SidePanelLeft.INPUT_AngularRate_Y.getText()) ;
	} catch (Exception exp) {}
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitAngRateZ(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_AngRateZ";
	
	double value =0;
	try{
		value = Double.parseDouble(	SidePanelLeft.INPUT_AngularRate_Z.getText()) ;
	} catch(Exception exp) {}
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_InitInertia(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Init_IXX";
	try {
	double value = Double.parseDouble(BlueBookVisual.INPUT_IXX.getText());  ;
	fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	 identifier = "Init_IXY";
	 value = Double.parseDouble(BlueBookVisual.INPUT_IXY.getText());  ;
	fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	 identifier = "Init_IXZ";
	 value = Double.parseDouble(BlueBookVisual.INPUT_IXZ.getText());  ;
	fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	 identifier = "Init_IYX";
	 value = Double.parseDouble(BlueBookVisual.INPUT_IYX.getText());  ;
	fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	 identifier = "Init_IYY";
	 value = Double.parseDouble(BlueBookVisual.INPUT_IYY.getText());  ;
	fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	 identifier = "Init_IYZ";
	 value = Double.parseDouble(BlueBookVisual.INPUT_IYZ.getText());  ;
	fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	 identifier = "Init_IZX";
	 value = Double.parseDouble(BlueBookVisual.INPUT_IZX.getText());  ;
	fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	 identifier = "Init_IZY";
	 value = Double.parseDouble(BlueBookVisual.INPUT_IZY.getText());  ;
	fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	 identifier = "Init_IZZ";
	 value = Double.parseDouble(BlueBookVisual.INPUT_IZZ.getText());  ;
	fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	} catch (Exception exp) {}
	return fileWriter;
    }
    
    private static FileWriter write_IntegMaxTime(FileWriter fileWriter, String delimiter) throws IOException {
    	try {
	String identifier = "Integ_MaxTime";
	double  value = CenterPanelRight.getGlobalTime() ;
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	} catch (Exception exp ) {}
	return fileWriter;
    }
    
    private static FileWriter write_IntegIntegrator(FileWriter fileWriter, String delimiter) throws IOException {
    	try {
	String identifier = "Integ_Integrator";
	double value = SidePanelLeft.Integrator_chooser.getSelectedIndex() ;
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	} catch (Exception exp ) {}
	return fileWriter;
    }
    
    private static FileWriter write_IntegVelVector(FileWriter fileWriter, String delimiter) throws IOException {
    	try {
	String identifier = "Integ_VelVector";
	double value = CenterPanelRight.getVelocityCoordinateSystem() ;
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	} catch (Exception exp ) {}
	return fileWriter;
    }
    
    private static FileWriter write_IntegDoF(FileWriter fileWriter, String delimiter) throws IOException {
    	try {
	String identifier = "Integ_DoF";
	double value = CenterPanelRight.getDOF_System() ;
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	} catch (Exception exp ) {}
	return fileWriter;
    }
    
    private static FileWriter write_IntegFrequency(FileWriter fileWriter, String delimiter) throws IOException {
    	try {
	String identifier = "Integ_Frequency";
	double value = CenterPanelRight.getControllerFrequency() ;
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	} catch (Exception exp ) {}
	return fileWriter;
    }
    
    private static FileWriter write_Integ853_MinStep(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Integ_853_MinStep";
	double value = 0 ;
	try {
		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_01.getText());
	} catch (Exception exction) { }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_Integ853_MaxStep(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Integ_853_MaxStep";
	double value = 0 ;
	try {
		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_02.getText());
	} catch (Exception exction) { }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_Integ853_AbsTol(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Integ_853_AbsTol";
	double value = 0 ;
	try {
		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_03.getText());
	} catch (Exception exction) { }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_Integ853_RelTol(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Integ_853_RelTol";
	double value = 0 ;
	try {
		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_04.getText());
	} catch (Exception exction) { }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_IntegRungKut_Step(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Integ_RungKut_Step";
	double value = 0 ;
	try {
		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_01.getText());
	} catch (Exception exction) { }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_IntegGraBul_MinStep(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Integ_GraBul_MinStep";
	double value = 0 ;
	try {
		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_01.getText());
	} catch (Exception exction) { }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_IntegGraBul_MiaxStep(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Integ_GraBul_MaxStep";
	double value = 0 ;
	try {
		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_02.getText());
	} catch (Exception exction) { }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_IntegGraBul_AbsTol(FileWriter fileWriter, String delimiter) throws IOException {
    	
	String identifier = "Integ_GraBul_AbsTol";
	double value = 0 ;
	try {
		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_03.getText());
	} catch (Exception exction) { }
	 
		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	
	return fileWriter;
    }
    
    private static FileWriter write_IntegGraBul_RelTol(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Integ_GraBul_RelTol";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_04.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_IntegAdBash_Steps(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Integ_AdBash_Steps";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_01.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_IntegAdBash_MinStep(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Integ_AdBash_MinStep";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_02.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_IntegAdBash_MaxStep(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Integ_AdBash_MaxStep";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_03.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_IntegAdBash_AbsTol(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Integ_AdBash_AbsTol";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_04.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_IntegAdBash_RelTol(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Integ_AdBash_RelTol";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_05.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_Env_CenterBody(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Env_CenterBody";
    	double value = 0 ;
    	try {
    		value = CenterPanelRight.getTargetIndx();
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_Env_RefElev(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Env_RefElev";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(SidePanelLeft.INPUT_REFELEV.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_Env_DragModel(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Env_DragModel";
    	double value = 0 ;
    	try {
    		value = AerodynamicSetup.getDragModelSetIndx();
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_Env_ConstCD(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Env_ConstCD";
    	double value = 1.4 ;
    	try {
    		value = Double.parseDouble(AerodynamicSetup.ConstantCD_INPUT.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_Env_ParModel(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Env_ParModel";
    	double value = 0 ;
    	try {
    		value = AerodynamicSetup.getParachuteModelSetIndx();
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_Env_ParCD(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "Env_ParCD";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(AerodynamicSetup.ConstantParachuteCD_INPUT.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_SurfArea(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_SurfArea";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(AerodynamicSetupSpacecraft.INPUT_SURFACEAREA.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_ParDiam(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_ParDiam";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(AerodynamicSetupSpacecraft.INPUT_ParachuteDiameter.getText());
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_COM(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_COM";
    	double value = 0 ;
    	try {
    		value = GeometryFrame.getCoM();
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_COT(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_COT";
    	double value = 0 ;
    	try {
    		value = GeometryFrame.getCoT();
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_COP(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_COP";
    	double value = 0 ;
    	try {
    		value = GeometryFrame.getCoP();
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_Height(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_Height";
    	double value = 0 ;
    	try {
    		value = GeometryFrame.getLength();
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_BodyRadius(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_BodyRadius";
    	double value = 0 ;
    	try {
    		value = GeometryFrame.getDiameter()/2;
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_MainISP(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_MainISP";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(PropulsionSetup.INPUT_ISP.getText()) ;
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_MainProp(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_MainProp";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(PropulsionSetup.INPUT_PROPMASS.getText()) ;
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
    private static FileWriter write_SC_MainThrustMax(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_MainThrustMax";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(PropulsionSetup.INPUT_THRUSTMAX.getText()) ;
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
    
   private static FileWriter write_SC_MainThrustMin(FileWriter fileWriter, String delimiter) throws IOException {
    	
    	String identifier = "SC_MainThrustMin";
    	double value = 0 ;
    	try {
    		value = Double.parseDouble(PropulsionSetup.INPUT_THRUSTMIN.getText()) ;
    	} catch (Exception exction) { }
    	 
    		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
    	
    	return fileWriter;
    }
   
   private static FileWriter write_SC_MainISPModel(FileWriter fileWriter, String delimiter) throws IOException {
   	try {
   	String identifier = "SC_MainISPModel";
   	double value = 0 ;
	if(PropulsionSetup.INPUT_ISPMODEL.isSelected()) {
		value = 1;
	} else {
		value=0;
	}
   	 
   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	} catch (Exception exp ) {}
   	return fileWriter;
   }
   
   private static FileWriter write_SC_MainISPMin(FileWriter fileWriter, String delimiter) throws IOException {
   	
   	String identifier = "SC_MainISPMin";
   	double value = 0 ;
   	try {
   		value = Double.parseDouble(PropulsionSetup.INPUT_ISPMIN.getText()) ;
   	} catch (Exception exction) { }
   	 
   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
   	
   	return fileWriter;
   }
   
   private static FileWriter write_SC_RCSMomX(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSMomX";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSX.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
	   }
   
   private static FileWriter write_SC_RCSMomY(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSMomY";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSY.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
   }
   
   private static FileWriter write_SC_RCSMomZ(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSMomZ";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSZ.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
   }
   
   private static FileWriter write_SC_RCSThrustX(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSThrustX";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSXTHRUST.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
   }
   
   private static FileWriter write_SC_RCSThrustY(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSThrustY";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSYTHRUST.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
  }
   
  private static FileWriter write_SC_RCSThrustZ(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSThrustZ";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSZTHRUST.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
 }
  
  private static FileWriter write_SC_RCSProp(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSProp";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSTANK.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
  }
  
  private static FileWriter write_SC_RCSISPX(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSISPX";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSXISP.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
  }
  
  private static FileWriter write_SC_RCSISPY(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSISPY";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSYISP.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
  }
  
  private static FileWriter write_SC_RCSISPZ(FileWriter fileWriter, String delimiter) throws IOException {
	   	
	   	String identifier = "SC_RCSISPZ";
	   	double value = 0 ;
	   	try {
	   		value = Double.parseDouble(PropulsionSetup.INPUT_RCSZISP.getText()) ;
	   	} catch (Exception exction) { }
	   	 
	   		fileWriter.write(identifier+delimiter+value+System.getProperty( "line.separator" ));
	   	
	   	return fileWriter;
  }
}
