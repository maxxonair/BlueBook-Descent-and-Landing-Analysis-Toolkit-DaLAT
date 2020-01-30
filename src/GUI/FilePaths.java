package GUI;

public class FilePaths {
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Relative File Paths
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static String Init_File   			= System.getProperty("user.dir") + "/INP/init.inp" ;		    		// Input: Initial state
    public static String RES_File    			= System.getProperty("user.dir") + "/results.txt"  ;       	 	// Input: result file 
    public static String CTR_001_File			= System.getProperty("user.dir") + "/CTRL/cntrl_1.inp";		    // Controller 01 input file 
    public static String Prop_File 	 			= System.getProperty("user.dir") + "/INP/PROP/prop.inp";			// Main propulsion ystem input file 
    public static String SEQU_File		 		= System.getProperty("user.dir") + "/SEQU.res";					// Sequence output file 
    public static String SC_file 				= System.getProperty("user.dir") + "/INP/SC/sc.inp";
    public static String ICON_File   	 		= System.getProperty("user.dir") + "/images/BB_icon4.png";
    public static String ERROR_File 				= System.getProperty("user.dir") + "/INP/ErrorFile.inp";
    public static String SEQUENCE_File   		= System.getProperty("user.dir") + "/INP/sequence_1.inp"; 
    public static String CONTROLLER_File			= System.getProperty("user.dir") + "/CTRL/ctrl_main.inp";
	public static String MAP_EARTH				= System.getProperty("user.dir") + "/MAPS/Earth_MAP.jpg";
	public static String MAP_MOON				= System.getProperty("user.dir") + "/MAPS/Moon_MAP.jpg";
	public static String MAP_VENUS				= System.getProperty("user.dir") + "/MAPS/Venus_MAP.jpg";
	public static String MAP_MARS				= System.getProperty("user.dir") + "/MAPS/Mars_MAP.jpg";
	public static String MAP_SOUTHPOLE_MOON		= System.getProperty("user.dir") + "/MAPS/Moon_South_Pole.jpg";
	public static String EventHandler_File		= System.getProperty("user.dir") + "/INP/eventhandler.inp";
	public static String INTEG_File_01 			= System.getProperty("user.dir") + "/INP/INTEG/00_DormandPrince853Integrator.inp";
	public static String INTEG_File_02 			= System.getProperty("user.dir") + "/INP/INTEG/01_ClassicalRungeKuttaIntegrator.inp";
	public static String INTEG_File_03 			= System.getProperty("user.dir") + "/INP/INTEG/02_GraggBulirschStoerIntegrator.inp";
	public static String INTEG_File_04 			= System.getProperty("user.dir") + "/INP/INTEG/03_AdamsBashfordIntegrator.inp";
	public static String INERTIA_File 		    = System.getProperty("user.dir") + "/INP/INERTIA.inp";
	public static String InitialAttitude_File   = System.getProperty("user.dir") + "/INP/INITIALATTITUDE.inp";
    public static String Aero_file 		        = System.getProperty("user.dir") + "/INP/AERO/aeroBasic.inp";
    public static String sequenceFile 		    = System.getProperty("user.dir") + "/INP/sequenceFile.inp";
    public static String elementList 		    = System.getProperty("user.dir") + "/INP/geomElementList.inp";    
    public static String inputFile 		        = System.getProperty("user.dir") + "/INP/inputFile.inp";
}
