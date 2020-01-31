package GUI;

public class FilePaths {
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Relative File Paths
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static String Init_File   			= System.getProperty("user.dir") + "/INP/init.inp" ;		    		// Input: Initial state
    public static String RES_File    			= System.getProperty("user.dir") + "/results.txt"  ;       	 	// Input: result file 

    public static String ICON_File   	 		= System.getProperty("user.dir") + "/images/BB_icon4.png";


    public static String CONTROLLER_File			= System.getProperty("user.dir") + "/CTRL/ctrl_main.inp";
	public static String MAP_EARTH				= System.getProperty("user.dir") + "/MAPS/Earth_MAP.jpg";
	public static String MAP_MOON				= System.getProperty("user.dir") + "/MAPS/Moon_MAP.jpg";
	public static String MAP_VENUS				= System.getProperty("user.dir") + "/MAPS/Venus_MAP.jpg";
	public static String MAP_MARS				= System.getProperty("user.dir") + "/MAPS/Mars_MAP.jpg";
	public static String MAP_SOUTHPOLE_MOON		= System.getProperty("user.dir") + "/MAPS/Moon_South_Pole.jpg";

    public static String sequenceFile 		    = System.getProperty("user.dir") + "/INP/sequenceFile.inp";
    public static String elementList 		    = System.getProperty("user.dir") + "/INP/geomElementList.inp";    
    public static String inputFile 		        = System.getProperty("user.dir") + "/INP/inputFile.inp";
    public static String starBackgroundFile     = System.getProperty("user.dir")+"/images/SurfaceTextures/milkyway.jpg";
}
