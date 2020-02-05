package GUI;

public class FilePaths {
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Relative File Paths
    //-----------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 		Result file -> Main output file 
	 */
    public static String RES_File    			= System.getProperty("user.dir") + "/results.txt"  ;       	 	// Input: result file 
    /**
     * 		Input files
     */
    public static String sequenceFile 		    = System.getProperty("user.dir") + "/INP/sequenceFile.inp";
    public static String elementList 		    = System.getProperty("user.dir") + "/INP/geomElementList.inp";    
    public static String inputFile 		        = System.getProperty("user.dir") + "/INP/inputFile.inp";    
    /**
     * 
     * 		Maps 
     */
	public static String MAP_EARTH				= System.getProperty("user.dir") + "/MAPS/Earth_MAP.jpg";
	public static String MAP_MOON				= System.getProperty("user.dir") + "/MAPS/Moon_MAP.jpg";
	public static String MAP_VENUS				= System.getProperty("user.dir") + "/MAPS/Venus_MAP.jpg";
	public static String MAP_MARS				= System.getProperty("user.dir") + "/MAPS/Mars_MAP.jpg";
	public static String MAP_SOUTHPOLE_MOON		= System.getProperty("user.dir") + "/MAPS/Moon_South_Pole.jpg";
	
    public static String starBackgroundFile     = System.getProperty("user.dir")+"/images/SurfaceTextures/milkyway.jpg";
    /**
     * 		Images, Icons and logos 
     */
    	public static String ICON_File				= System.getProperty("user.dir") + "/images/BB_icon4.png";
    public static String iconPlusSequence		= "images/iconPlus.png";
    public static String iconMinusSequence		= "images/iconMinus.png";
}
