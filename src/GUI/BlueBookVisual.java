package GUI; 
import java.awt.AlphaComposite;
//-----------------------------------------------------------------------------------------------------------------------------------------
//															BlueBook DaLAT Graphical User Interface
//
//   Descent and Landing Analysis Toolkit  - Version 0.2 ALPHA
//
//   Author: M. Braun
// 	 Date: 01/03/2019
//
//
//           Version 0.2 
// 							- Updates: Controller organized in dedicated table. 
//
//-----------------------------------------------------------------------------------------------------------------------------------------
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;


import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.RenderingHints;
import java.awt.SplashScreen;
import java.awt.Toolkit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.*;  
import Simulator_main.DataSets.RealTimeResultSet;
import GUI.PropulsionSetup.PropulsionSetup;
import GUI.Sequence.SequencePanel;
import GUI.SimulationSetup.BasicSetup.BasicSetupMain;
import GUI.SimulationSetup.BasicSetup.CenterPanelRight;
import GUI.SimulationSetup.BasicSetup.SidePanelLeft;
import utils.GuiReadInput;
import utils.WriteInput;
import com.apple.eawt.Application;

import GUI.AerdoynamicSetup.AerodynamicSetup;
import GUI.AerdoynamicSetup.AerodynamicSetupSpacecraft;
import GUI.Dashboard.DashboardPanel;
import GUI.Dashboard.DashboardPlotArea;
import GUI.DataStructures.InputFileSet;
import GUI.GNC.GNCPanel;
import GUI.Gravity.GravityPanel;
import GUI.InertiaGeometry.InertiaGeometry;
import GUI.Maps.MapSetting;
import GUI.Maps.MercatorMap;
import GUI.Maps.PolarMap;
import GUI.MenuBar.MenuBar;
import GUI.NoiseModel.NoiseErrorPanel;

public class BlueBookVisual  {
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Main Container Frame Elements
    //-----------------------------------------------------------------------------------------------------------------------------------------
	public static String PROJECT_TITLE = "  BlueBook Descent and Landing Analysis Toolkit - Mark 1 v1.1";
	static boolean darkTheme = true; 
	static boolean isSplashAnimation = true;
    static int x_init = 1350;
    static int y_init = 860 ;
    public static JFrame MAIN_frame;
    
    public static String CASE_FileEnding   = ".case";
    public static String RESULT_FileEnding = ".res";
    public static int OS_is = 1; 
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Constants
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    public static double G     = 1.48808E-34;
    public static int TARGET;  
    public static  double RM = 0; 					// Target planet radius
    public static int indx_target = 0;  				// Target planet indx 
	
	public static String BB_delimiter = " ";
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //											Styles, Fonts, Colors
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static int gg = 235;
    public static Color labelColor = new Color(220,220,220);    					// Label Color
   	public static Color backgroundColor = new Color(41,41,41);				    // Background Color
   	public static Color valueColor =  new Color(65,105,225);  	
   	public static Color light_gray = new Color(230,230,230);
   	
    public static DecimalFormat decf 		  = new DecimalFormat("#.#");
    public static DecimalFormat decAngularRate =  new DecimalFormat("##.####");
    public static DecimalFormat df_X4 		  = new DecimalFormat("#####.###");
    public static DecimalFormat df_VelVector = new DecimalFormat("#.00000000");
    
    public static Font small_font			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static Font labelfont_small       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
    //static Font labelfont_verysmall   = new Font("Verdana", Font.BOLD, 7);
    
    static Font HeadlineFont          = new Font("Georgia", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    public static DecimalFormat df 	  = new DecimalFormat();
    

    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Variables and Container Arrays
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static int INTEGRATOR = 0; 
    public static String[] Target_Options = { "Earth", 
    									          "Moon" ,	
    									          "Mars", 	
    										      "Venus"};

    public static String[] Axis_Option_NR = { "Time [s]",
    										  "Longitude [deg]", 
    										  "Latitude [deg]" ,
    										  "Altitude ref. Landing [m]", 
    										  "Altidue ref. mean [m]",
    										  "Radius [m]",
    										  "Velocity (ref. ECEF) [m/s]", 
    										  "Flight Path inclination angle [deg]", 
    										  "Flight Path azimuth angle [deg]", 
    										  "Density [kg/m3]", 
    										  "Static temperature [K]", 
    										  "Mach [-]",
    										  "Heat capacity ratio", 
    										  "Gas constant", 
    										  "Static pressure [Pa]",
    										  "Dynamic Pressure [Pa]",
    										  "Flowzone [-]",
    										  "Aerodynamic Drag Coefficient Cd [-]", 
    										  "Aerodynamic Lift Coefficient Cl [-]",
    										  "Aerodynamic Sideslip Coefficient Cy [-]",
    										  "Aerodynamic Drag Force [N]", 
    										  "Aerodynamic Lift Force [N]",
    										  "Aerodynamic Side Force [N]", 
    										  "Aerodynamic angle of attack [deg]", 
    										  "Aerodynamic bank angle [deg]", 
    										  "Gx NED [m/s2]",
    										  "Gy NED [m/s2]",
    										  "Gz NED [m/s2]",
    										  "G total [m/s2]", 
    										  "Fx NED [N]",
    										  "Fy NED [N]",
    										  "Fz NED [N]",
    										  "Force Aero x NED [N]",
    										  "Force Aero y NED [N]",
    										  "Force Aero z NED [N]",
    										  "Force Thrust x NED [N]",
    										  "Force Thrust y NED [N]",
    										  "Force Thrust z NED [N]",
    										  "Force Gravity x NED [N]",
    										  "Force Gravity y NED [N]",
    										  "Force Gravity z NED [N]",
    										  "Position x ECEF [m]",
    										  "Position y ECEF [m]",
    										  "Position z ECEF [m]",
    										  "Velocity u NED/ECEF [m/s]",
    										  "Velocity v NED/ECEF [m/s]",
    										  "Velocity w NED/ECEF [m/s]",
    										  "Quaternion qw",
    										  "Quaternion qx",
    										  "Quaternion qy",
    										  "Quaternion qz",
    										  "Angular Rate x B2NED [deg/s]",
    										  "Angular Rate y B2NED [deg/s]",
    										  "Angular Rate z B2NED [deg/s]",
    										  "Angular Momentum x B [Nm]",
    										  "Angular Momentum y B [Nm]",
    										  "Angular Momentum z B [Nm]",
    										  "X Roll Angle - Euler Phi [deg]",
    										  "Y Pitch Angle - Euler Theta [deg]",
    										  "Z Yaw Angle - Euler Psi [deg]",
    										  "SC Mass [kg]",
    										  "Normalized Deceleartion [-]",
    										  "Total Engergy [J]",
    										  "Velocity horizontal [m/s]",
    										  "Velocity vertical [m/s]",
    										  "Groundtrack [km]",
    										  "Active Sequence ID [-]",
    										  "CNTRL Time [s]",    										  
    										  "Parachute Cd [-]",
    										  "Drag Force Parachute [N]",
    										  "Primary Thrust CMD [%]",
    										  "Primary Thrust Force [N]", 
    										  "Primary Thrust to mass [N/kg]",
    										  "Primary Tank filling level [%]",
    										  "Primary ISP [s]",
    										  "RCS Momentum X CMD [-]",
    										  "RCS Momentum Y CMD [-]",
    										  "RCS Momentum Z CMD [-]",
    										  "RCS Momentum X [Nm]",
    										  "RCS Momentum Y [Nm]",
    										  "RCS Momentum Z [Nm]",
    										  "RCS tank filling level [%]",
    										  "TVC alpha cmd [-]",
    										  "TVC beta cmd [-]",
    										  "TVC alpha [deg]",
    										  "TVC beta [deg]",
    										  "Thrust Force x B [N]",
    										  "Thrust Force y B [N]",
    										  "Thrust Force z B [N]",
    										  "Vel NED/ECI [m/s]",
    										  "FPA NED/ECI [m/s",
    										  "AZI  NED/ECI [m/s]",
    										  "Primary Propulsion Propellant flow rate [kg/s]",
    										  "Used Propellant Primary [kg]",
    										  "Used Propellant Secondary [kg]",
    										  "DeltV RCS X [m/s]",
    										  "DeltV RCS Y [m/s]",
    										  "DeltV RCS Z [m/s]",
    										  "DeltV RCS   [m/s]",
    										  "DeltaV Primary [m/s]"
    										  };

	public static double rotX=0;
	public static double rotY=0;
	public static double rotZ=0;
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Main GUI Elements
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    public static int extx_main = 1350;
    public static int exty_main = 800; 
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												GUI Elements
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    public static TimerTask task_Update;

    public static Border Earth_border = BorderFactory.createLineBorder(Color.BLUE, 1);
    public static Border Moon_border 	= BorderFactory.createLineBorder(Color.GRAY, 1);
    public static Border Mars_border 	= BorderFactory.createLineBorder(Color.ORANGE, 1);
    public static Border Venus_border = BorderFactory.createLineBorder(Color.GREEN, 1);
    public static JCheckBox p421_linp0;

    	static int page1_plot_y =380;
    	public static int thirdWindowIndx = 1;
    	
    	public static MercatorMap mercatorMap;
    	public static PolarMap polarMap;
    	private static RawData rawData;
    	
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												GUI Panels
    //-----------------------------------------------------------------------------------------------------------------------------------------    	
    	 static DashboardPanel dashboardPanel;
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Content Lists
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    public static  List<RealTimeResultSet> resultSet = new ArrayList<RealTimeResultSet>();
    static List<InputFileSet> analysisFile = new ArrayList<InputFileSet>();
	//-----------------------------------------------------------------------------
	public JPanel createContentPane () throws IOException{
    	JPanel MainGUI = new JPanel();
    	MainGUI = new JPanel();
    	MainGUI.setBackground(backgroundColor);
    	MainGUI.setLayout(new BorderLayout());

    	// System.out.println(System.getProperty("os.name"));
    	 if(System.getProperty("os.name").contains("Mac")) {
    		 OS_is = 1;
    	 } else if(System.getProperty("os.name").contains("Win")) {
    		 OS_is = 2;
    	 } else if(System.getProperty("os.name").contains("Lin")) {
    		 OS_is = 3;
    	 }
    	 
    	   	if(!darkTheme) {
    	        labelColor = new Color(20,20,20);    					// Label Color
    	        backgroundColor = new Color(241,241,241);				// Background Color
    	     }
     // ---------------------------------------------------------------------------------
    	 //       Define Task (FileWatcher) Update Result Overview
    	 // ---------------------------------------------------------------------------------
    	  task_Update = new FileWatcher( new File(FilePaths.RES_File) ) {
    		    protected void onChange( File file ) {

          		  update(true);
          		  DashboardPlotArea.updateDashboardPlotArea(DashboardPlotArea.getContentPanelList());

    		    }
    		  };
    	   Timer timer = new Timer();
    	  // repeat the check every second
    	   timer.schedule( task_Update , new Date(), 1000 );
    	// ---------------------------------------------------------------------------------
    //       The following function contains all GUI elements of the main window
    // ---------------------------------------------------------------------------------
    	decf.setMaximumFractionDigits(1);
    	decf.setMinimumFractionDigits(1);
        //Create the menu bar.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (UnsupportedLookAndFeelException ex) {
        }
        UIManager.put("MenuBar.background", Color.green);
        UIManager.put("Menu.opaque", true);
        

        JTabbedPane Page04_subtabPane = (JTabbedPane) new JTabbedPane();
        Page04_subtabPane.setPreferredSize(new Dimension(extx_main, exty_main));
        Page04_subtabPane.setBackground(backgroundColor);
        Page04_subtabPane.setForeground(Color.BLACK);

      //--------------------------------------------------------------------------------------------------------------------------------
        JPanel PageX04_SimSetup = new JPanel(); 
        PageX04_SimSetup.setLocation(0, 0);
        PageX04_SimSetup.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_SimSetup.setLayout(new BorderLayout());
        PageX04_SimSetup.setBackground(backgroundColor);
        PageX04_SimSetup.setForeground(labelColor);
        JPanel PageX04_AttitudeSetup = new JPanel();
        PageX04_AttitudeSetup.setLocation(0, 0);
        PageX04_AttitudeSetup.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_AttitudeSetup.setLayout(new BorderLayout());
        PageX04_AttitudeSetup.setBackground(backgroundColor);
        PageX04_AttitudeSetup.setForeground(labelColor);


        /**
         * 
         * 			Initialize Dashboard Panel
         */
        
        dashboardPanel = new DashboardPanel();
        

        /**
         * 
         *  			Setup main menu bar 
         */
        
        MenuBar menuBar = new MenuBar();
        MainGUI.add(menuBar.getMainMenu(), BorderLayout.NORTH);
        
        
	  //-----------------------------------------------------------------------------------------
	  // 										Page 4.2
	  //-----------------------------------------------------------------------------------------
      JTabbedPane TabPane_SimulationSetup = (JTabbedPane) new JTabbedPane();
      TabPane_SimulationSetup.setPreferredSize(new Dimension(extx_main, exty_main));
      TabPane_SimulationSetup.setBackground(backgroundColor);
      TabPane_SimulationSetup.setForeground(labelColor);
			//---------------------------------------------------------------------------------------
			/**			Create Setup panel:
			 * comprising:
			 * 				- SidePanelLeft
			 * 				- CenterPanelRight
			 * 				-> Parent constructor: BasicSetup Panel 
			 */
			//-----------------------------------------------------------------------------------------
		    // ---->>>>>                       TAB: Basic Setup
			//-----------------------------------------------------------------------------------------	
				BasicSetupMain basicSetupMain = new BasicSetupMain();
			//-----------------------------------------------------------------------------------------
		    // ---->>>>>                       TAB: Sequence Setup
			//-----------------------------------------------------------------------------------------				
				SequencePanel sequencePanel = new SequencePanel();
			//-----------------------------------------------------------------------------------------
			// ---->>>>>                       TAB: Aerodynamic Setup 
			//-----------------------------------------------------------------------------------------	
				AerodynamicSetup aeroSetup = new AerodynamicSetup();
			//-----------------------------------------------------------------------------------------
			// ---->>>>>                       TAB: Gravity Setup 
			//-----------------------------------------------------------------------------------------	
				GravityPanel gravityPanel = new GravityPanel();
			//-----------------------------------------------------------------------------------------
			// ---->>>>>                       TAB: Noise and Error Setup  
			//-----------------------------------------------------------------------------------------	
				NoiseErrorPanel noiseErrorPanel = new NoiseErrorPanel();
			//---------------------------------------------------------------------------------------
			/**
			 * 						Create Setup area tabbed pane structure 

			 */
			//---------------------------------------------------------------------------------------
	     	ImageIcon icon_setup2 = new ImageIcon("images/setup2.png","");
	     	ImageIcon icon_inertia = new ImageIcon("images/inertia.png","");
	     	ImageIcon icon_aerodynamic = new ImageIcon("images/aerodynamic.png","");
	     	if(OS_is==2) {
	     		// Resize image icons for Windows 
	         	 int size=10;
	         	icon_setup2 = new ImageIcon(getScaledImage(icon_setup2.getImage(),size,size));
	         	icon_inertia = new ImageIcon(getScaledImage(icon_inertia.getImage(),size,size));
	         	icon_aerodynamic = new ImageIcon(getScaledImage(icon_aerodynamic.getImage(),size,size));
	      }
	      
	     	TabPane_SimulationSetup.addTab("Basic Setup" , icon_setup2, basicSetupMain.getMainPanel(), null);
	     	TabPane_SimulationSetup.addTab("Sequence Setup" , icon_setup2, sequencePanel.getMainPanel(), null);
	     	TabPane_SimulationSetup.addTab("Aerodynamic Setup" , icon_aerodynamic, aeroSetup.getMainPanel(), null);
	     	TabPane_SimulationSetup.addTab("Gravity Setup" , icon_setup2, gravityPanel.getMainPanel(), null);
	     	TabPane_SimulationSetup.addTab("Noise and Error Model Setup" , null, noiseErrorPanel.getMainPanel(), null);
	     	PageX04_SimSetup.add(TabPane_SimulationSetup);
	     	
		TabPane_SimulationSetup.setSelectedIndex(0);
		TabPane_SimulationSetup.setFont(small_font);
		TabPane_SimulationSetup.setForeground(Color.black);
 //-----------------------------------------------------------------------------------------------------------------------------
 //                								   SpaceShipt Setup Area 
 //-----------------------------------------------------------------------------------------------------------------------------
				//-----------------------------------------------------------------------------------------
			    // ---->>>>>             TAB: Mass, Inertia and Geometry Setup 
				//-----------------------------------------------------------------------------------------
				InertiaGeometry inertiaGeometry = null;
				try {
							 inertiaGeometry = new InertiaGeometry();
				} catch (Exception expInertGeo) {
					System.out.println("Error: Creating Inertia/Geomtry pane failed.");
				}
    				//-----------------------------------------------------------------------------------------
    			    // ---->>>>>                       TAB: Propulsion Setup 
    				//-----------------------------------------------------------------------------------------		    	    
    					PropulsionSetup propulsionSetup = new PropulsionSetup();
				//-----------------------------------------------------------------------------------------
			    // ---->>>>>                       TAB: Aerodynamic Setup 
				//-----------------------------------------------------------------------------------------		    
    					AerodynamicSetupSpacecraft aeroSetupSpacecraft = new AerodynamicSetupSpacecraft();
				//-----------------------------------------------------------------------------------------
			    // ---->>>>>                       TAB: GNC Setup 
				//-----------------------------------------------------------------------------------------	
    					GNCPanel gncPanel = new GNCPanel();
		//-----------------------------------------------------------------------------------------
	    // ---->>>>>                       TAB: Spacecraft Definition
		//-----------------------------------------------------------------------------------------			    
			      
			    // Main (SUB) tabbed Pane for this page
		        JTabbedPane TabPane_SCDefinition = (JTabbedPane) new JTabbedPane();
		        TabPane_SCDefinition.setPreferredSize(new Dimension(extx_main, exty_main));
		        TabPane_SCDefinition.setBackground(backgroundColor);
		        TabPane_SCDefinition.setForeground(Color.BLACK);
		//-------------------------------------------------------------------------------------------	    				
			     	if(OS_is==2) {
			     		// Resize image icons for Windows 
			         	 int size=10;
			         	icon_setup2 = new ImageIcon(getScaledImage(icon_setup2.getImage(),size,size));
			         	icon_inertia = new ImageIcon(getScaledImage(icon_inertia.getImage(),size,size));
			         	icon_aerodynamic = new ImageIcon(getScaledImage(icon_aerodynamic.getImage(),size,size));
			      }
			      
				TabPane_SCDefinition.addTab("Mass, Inertia and Geometry" , icon_setup2, inertiaGeometry.getMainPanel(), null);
				TabPane_SCDefinition.addTab("Propulsion" , icon_inertia, propulsionSetup.getMainPanel(), null);
				TabPane_SCDefinition.addTab("Aerodynamic" , icon_aerodynamic, aeroSetupSpacecraft.getMainPanel(), null);
				TabPane_SCDefinition.addTab("GNC" , icon_aerodynamic, gncPanel.getMainPanel(), null);
				PageX04_AttitudeSetup.add(TabPane_SCDefinition);
		        TabPane_SCDefinition.setSelectedIndex(0);
		        TabPane_SCDefinition.setFont(small_font);
				
	    //-------------------------------------------------------------------------------------------	

        //-----------------------------------------------------------------------------------------
        // 		Create map classes 
        //-----------------------------------------------------------------------------------------
		
		 mercatorMap = new MercatorMap();

         polarMap = new PolarMap();
 	  //-----------------------------------------------------------------------------------------
 	  // 										Page: Raw Data
 	  //-----------------------------------------------------------------------------------------
          rawData = new RawData();
	  //---------------------------------------------------------------------

     	mercatorMap.CreateChart_MercatorMap();
     	polarMap.CreateChart_PolarMap();

     	//---------------------------------------------------------------------
     	// Prepare icons
     	ImageIcon icon_dashboard = null;
     	ImageIcon icon_scSetup = null;
     	ImageIcon icon_setup = null;
     	ImageIcon icon_data = null;
     	ImageIcon icon_map = null;
     	if(OS_is==1) {
     	 icon_dashboard = new ImageIcon("images/homeIcon.png","");
     	 icon_scSetup = new ImageIcon("images/startup.png","");
     	 icon_setup = new ImageIcon("images/setup.png","");
     	 icon_data = new ImageIcon("images/data.png","");
     	 icon_map = new ImageIcon("images/map.png","");
     	} else if(OS_is==2) {
     	//	For Windows image icons have to be resized
        	 icon_dashboard = new ImageIcon("images/homeIcon.png","");
         icon_scSetup = new ImageIcon("images/startup.png","");
         icon_setup = new ImageIcon("images/setup.png","");
         icon_data = new ImageIcon("images/data.png","");
         icon_map = new ImageIcon("images/map.png","");
         	 int size=10;
     		icon_dashboard = new ImageIcon(getScaledImage(icon_dashboard.getImage(),size,size));
     		icon_scSetup = new ImageIcon(getScaledImage(icon_scSetup.getImage(),size,size));
     		icon_setup = new ImageIcon(getScaledImage(icon_setup.getImage(),size,size));
     		icon_data = new ImageIcon(getScaledImage(icon_data.getImage(),size,size));
     		icon_map = new ImageIcon(getScaledImage(icon_map.getImage(),size,size));
     	}
     	// Create Tabs:
        Page04_subtabPane.addTab("Dashboard" , icon_dashboard, dashboardPanel.getMainPanel(), null);
        Page04_subtabPane.addTab("Simulation Setup"+"\u2713", icon_setup, PageX04_SimSetup, null);
        Page04_subtabPane.addTab("SpaceShip Setup"+"\u2713", icon_scSetup, PageX04_AttitudeSetup, null);
        Page04_subtabPane.addTab("Raw Data", icon_data, rawData.getMainPanel(), null);
        Page04_subtabPane.addTab("Map" , icon_map, mercatorMap.getMercatorMapPanel(), null);
        Page04_subtabPane.addTab("Polar Map" , icon_map, polarMap.getPolarMapPanel(), null);

        Page04_subtabPane.setFont(small_font);
        Page04_subtabPane.setBackground(Color.black);
        Page04_subtabPane.setForeground(Color.black);
        MainGUI.add(Page04_subtabPane);
        Page04_subtabPane.setSelectedIndex(0);

        
        // The following prevents long load up times when opening the GUI 
    		try {     long filesize = 	new File(FilePaths.RES_File).length()/1000000;
			    	    if(filesize<10) {
			    	    		MapSetting.setMap(indx_target);
			    	    }
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1);
				System.out.println("ERROR: Loading map failed.");
			}
        //------------------------------------------------------------------------
    		/**
    		 * Larger result file sizes lead to annoyingly long start ups 
    		 * The following function checks the file size before the input and switches
    		 * to a partial import for large files
    		 * The full import can be triggered manually by the user once the program
    		 * has started fully. 
    		 */
    		// Check file size 
    		try {
		    	    long filesize = 	new File(FilePaths.RES_File).length()/1000000;
		    	    try {
			    	    if(filesize<10) {
			    	    		update(true);
			    	    } else {
			    	    		update(false);
			    	    	System.out.println("Full data import supressed. Filesize prohibits fast startup.");
			    	    }
		    	    		/*
		    	    		 *  		Master input variable import
		    	    		 */
		       	    GuiReadInput.readINP();
		       	    /*
		       	     *      Import of chart settings 
		       	     */
		       	    DashboardPlotArea.setAnalysisFile(analysisFile);
		       	    /**
		       	     * 		Set center body index and load maps and 3D content textures accordingly 
		       	     */
		       	    DashboardPlotArea.setTargetIndx(indx_target);
		       	    /** 
		       	     * 		
		       	     */
		       	    CenterPanelRight.createTargetWindow();
		       	    /**
		       	     * 
		       	     */
		       	    SidePanelLeft.Update_IntegratorSettings();
		       	    /**
		       	     * 		Read sequence file content and create sequence GUI
		       	     */
			    	      try {
			    	    	  	GuiReadInput.readSequenceFile();
			    	      } catch(Exception e) {
			    	    	  	System.out.println("ERROR: Reading sequenceFile.inp failed.");
			    	      }
		    	    } catch(Exception excpM) {
		    	    	System.out.println("Error: Reading input failed. ");
		    	    	System.out.println(excpM);
		    	    }
    		} catch (Exception exp ) {
    			System.out.println("Error: Load data unsuccessful. Corrupt result file.");
    			System.out.println(exp);
    		}
        MainGUI.setOpaque(true);
        return MainGUI;
	}

   
    public static Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
    
   
	public static DashboardPanel getDashboardPanel() {
		return dashboardPanel;
	}


	public static void update(boolean fullImport){
		  try {
			GuiReadInput.readINP();
			GuiReadInput.readSequenceFile();
			if(fullImport) {
				rawData.readRawData();
				MapSetting.setMap(indx_target);
			}
		} catch (IOException | URISyntaxException e2) {
			System.out.println("Error: update/readINP failed");
		}

		  if(fullImport) {

			  mercatorMap.update();

      	    		String timeStamp = new SimpleDateFormat("yyyy / MM / dd - HH : mm : ss").format(Calendar.getInstance().getTime());
      	    		System.out.println("Updated "+timeStamp);
		  }
	  } 
    static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] comps = {" .", " ..", " ...", " ....", " .....", " ......", " ......."};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,100,150,40);
        g.setPaintMode();
        g.setColor(labelColor);
        g.drawString("Loading "+comps[(frame/15)%7]+"..", 120, 600);
       // Image image = Toolkit.getDefaultToolkit().getImage(FilePaths.ICON_File);
       // g.drawImage(image, frame, frame, null, null);
    }
   
	private static void createAndShowGUI() throws IOException {
		
		boolean splashLoad=true;
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            splashLoad=false;
           // return;
        } else {
	        Graphics2D g = splash.createGraphics();
	        if (g == null) {
	            System.out.println("g is null");
	           // return;
	        }
	        if(isSplashAnimation) {
		        for(int i=0; i<100; i++) {
		            renderSplashFrame(g, i);
		            splash.update();
		            try {
		                Thread.sleep(10);
		            }
		            catch(InterruptedException e) {
		            }
		        }
	        }
        }

        JFrame.setDefaultLookAndFeelDecorated(false);
        MAIN_frame = new JFrame("" + PROJECT_TITLE);
        MAIN_frame.setFont(small_font);
        BlueBookVisual demo = new BlueBookVisual();
        JPanel tp = demo.createContentPane();
        tp.setPreferredSize(new java.awt.Dimension(x_init, y_init));
        /**
         *  		Switch to allow to write inputFile.inp
         *  		Prevents lost input data during start up 
         *  		Do not touch
         */
        		WriteInput.setWritable(true); 
        	//------------------------------------------------------------
        MAIN_frame.add(tp, BorderLayout.CENTER);
        MAIN_frame.pack();
        MAIN_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        if(splashLoad) { splash.close();}
        
        MAIN_frame.setLocationRelativeTo(null);
        MAIN_frame.setExtendedState(MAIN_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        MAIN_frame.setVisible(true);
        // Create Icon image  -  top left for windows
         try {
        	BufferedImage myIcon = ImageIO.read(new File(FilePaths.ICON_File)); 
        	MAIN_frame.setIconImage(myIcon);
         }catch(IIOException eIIO) {}    
         // Create taskbar icon - for mac 
         if(OS_is==1) {
        	 // Set Taskbar Icon for MacOS
         try {
         Application application = Application.getApplication();
         Image image = Toolkit.getDefaultToolkit().getImage(FilePaths.ICON_File);
         application.setDockIconImage(image);
         } catch(Exception e) {
        	 System.err.println("Taskbar icon could not be created");
         }
         }
    }
	    
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException  e) {System.out.println(e);}
            }
        });
    }

		public static String getPROJECT_TITLE() {
		return PROJECT_TITLE;
	}
		public static int RETURN_TARGET(){
			return TARGET; 
		}
		public static String getICON_File() {
			return FilePaths.ICON_File;
		}
		public static double getRM() {
			return RM;
		}
		public static List<RealTimeResultSet> getResultSet() {
			return resultSet;
		}
		
		
		public static Color getLabelColor() {
			return labelColor;
		}
		public static Color getBackgroundColor() {
			return backgroundColor;
		}

		
		
		public static String[] getAxis_Option_NR() {
			return Axis_Option_NR;
		}
		public static Font getSmall_font() {
			return small_font;
		}
	
}
class ColorArrowUI extends BasicComboBoxUI {

    public static ComboBoxUI createUI(JComponent c) {
        return new ColorArrowUI();
    }

    @Override protected JButton createArrowButton() {
        return new BasicArrowButton(
            BasicArrowButton.EAST,
            Color.GRAY, Color.magenta,
            Color.yellow, Color.blue);
    }
}