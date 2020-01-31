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
// 							- Updates: Controller organised in dedicated table. 
//
//-----------------------------------------------------------------------------------------------------------------------------------------
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;


import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.RenderingHints;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import GUI.GeometryModel.GeometryFrame;
import GUI.Maps.MapSetting;
import GUI.Maps.MercatorMap;
import GUI.Maps.PolarMap;
import GUI.MenuBar.MenuBar;

public class BlueBookVisual implements  ActionListener {
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Main Container Frame Elements
    //-----------------------------------------------------------------------------------------------------------------------------------------
	public static String PROJECT_TITLE = "  BlueBook Descent and Landing Analysis Toolkit - V0.3 ALPHA";
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
    public static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;
    public static double kB    = 1.380650424e-23;              // Boltzmann constant                         [SI]    
    public static double G     = 1.48808E-34;
    public static int TARGET;  
    public static  double RM = 0; 		// Target planet radius
    public static int indx_target = 0;  // Target planet indx 
	static double deg2rad = PI/180.0; 		//Convert degrees to radians
	static double rad2deg = 180/PI; 		//Convert radians to degrees
	
    public static double[][] DATA_MAIN = { // RM (average radius) [m] || Gravitational Parameter [m3/s2] || Rotational speed [rad/s] || Average Collision Diameter [m]
			{6371000,3.9860044189E14,7.2921150539E-5,1.6311e-9}, 	// Earth
			{1737400,4903E9,2.661861E-6,320},						// Moon (Earth)
			{3389500,4.2838372E13,7.0882711437E-5,1.6311e-9},		// Mars
			{0,0,0,0},												// Venus
	 };
	
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
    //static Font menufont              = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12);
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
    										  "Quaternion q1",
    										  "Quaternion q2",
    										  "Quaternion q3",
    										  "Quaternion q4",
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
    
    public static String[] EventHandler_Type = { "Time [s]", 
			  									 "Longitude [rad]" , 
			  									 "Latitude [rad]", 
			  									 "Altitude [m]",
			  									 "Velocity [m]",
			  									 "FPA [rad]",
			  									 "Azimuth [rad]",
    											     "SC Mass [kg]"};


	public static String[] COLUMS_ERROR = 		{"ID",
												"Error Type",
												"Trigger Value []",
												"Error Value"};
	public static String[] ErrorType = { "Thrust",
										 "TVC Lock", 
										 "Event Delay"
			
	};
public static String[] COLUMS_EventHandler = {"Event Type",
							 "Event Value"
};



	
	public static double rotX=0;
	public static double rotY=0;
	public static double rotZ=0;
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Main GUI Elements
    //----------------------------------------------------------------------------------------------------------------------------------------- 
	private int uy_p41 = 10 ;
    public static int extx_main = 1350;
    public static int exty_main = 800; 
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												GUI Elements
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    public static JPanel dashboardPanel;

    public static JPanel PageX04_SimSetup; 
    public static JPanel PageX04_AttitudeSetup;

    

    public static JTextField INPUT_M0, INPUT_WRITETIME,p42_inp14,p42_inp15,p42_inp16,p42_inp17;
    public static JTextField INPUT_PGAIN,INPUT_IGAIN,INPUT_DGAIN,INPUT_CTRLMAX,INPUT_CTRLMIN;
    

    public static JTextField INPUT_IXX, INPUT_IXY, INPUT_IXZ, INPUT_IYX, INPUT_IYY, INPUT_IYZ, INPUT_IZX, INPUT_IZY, INPUT_IZZ;


    public static TimerTask task_Update;
    public static JPanel SequenceLeftPanel;
    public static JPanel SequenceProgressBar, FlexibleChartContentPanel, FlexibleChartContentPanel2;
    public static List<JLabel> sequenceProgressBarContent = new ArrayList<JLabel>();



    public static Border Earth_border = BorderFactory.createLineBorder(Color.BLUE, 1);
    public static Border Moon_border 	= BorderFactory.createLineBorder(Color.GRAY, 1);
    public static Border Mars_border 	= BorderFactory.createLineBorder(Color.ORANGE, 1);
    public static Border Venus_border = BorderFactory.createLineBorder(Color.GREEN, 1);
    public static JCheckBox p421_linp0;


    	static int page1_plot_y =380;
    	@SuppressWarnings("rawtypes")
    	public static JComboBox axis_chooser3,axis_chooser4; 
    	public static int thirdWindowIndx = 1;
    	
    	public static MercatorMap mercatorMap;
    	public static PolarMap polarMap;
    	private static RawData rawData;
   
    	
    	
     	public static  List<RealTimeResultSet> resultSet = new ArrayList<RealTimeResultSet>();
     	public static  List<GUISequenceElement> sequenceContentList = new ArrayList<GUISequenceElement>();
     	public static 				int sequenceDimensionWidth=1500;
     	
     	
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
       // UIManager.put("Menu.background", labelColor);
      //  UIManager.put("MenuItem.background", labelColor);
        UIManager.put("Menu.opaque", true);
        

        JTabbedPane Page04_subtabPane = (JTabbedPane) new JTabbedPane();
        Page04_subtabPane.setPreferredSize(new Dimension(extx_main, exty_main));
        Page04_subtabPane.setBackground(backgroundColor);
        Page04_subtabPane.setForeground(Color.BLACK);
        
       
      //--------------------------------------------------------------------------------------------------------------------------------
        dashboardPanel = new JPanel();
        dashboardPanel.setLocation(0, 0);
        dashboardPanel.setPreferredSize(new Dimension(extx_main, exty_main));
        dashboardPanel.setLayout(new BorderLayout());
        dashboardPanel.setBackground(backgroundColor);
        dashboardPanel.setForeground(labelColor);

        PageX04_SimSetup = new JPanel(); 
        PageX04_SimSetup.setLocation(0, 0);
        PageX04_SimSetup.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_SimSetup.setLayout(new BorderLayout());
        PageX04_SimSetup.setBackground(backgroundColor);
        PageX04_SimSetup.setForeground(labelColor);
        PageX04_AttitudeSetup = new JPanel();
        PageX04_AttitudeSetup.setLocation(0, 0);
        PageX04_AttitudeSetup.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_AttitudeSetup.setLayout(new BorderLayout());
        PageX04_AttitudeSetup.setBackground(backgroundColor);
        PageX04_AttitudeSetup.setForeground(labelColor);


        /**
         * 
         * 			Initialize Dashboard Panel
         * 
         * 
         */
        
        DashboardPanel dashboardPanel = new DashboardPanel();
        
        
        /**
         * 
         *  			Setup main menu bar 
         * 
         * 
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
//-------------------------------------------------------------------------------------------	    
      // Main panels for each page 
		
	      
			JPanel SequenceSetupPanel = new JPanel();
			SequenceSetupPanel.setLocation(0, 0);
			SequenceSetupPanel.setBackground(backgroundColor);
			SequenceSetupPanel.setForeground(labelColor);
			SequenceSetupPanel.setPreferredSize(new Dimension(1350, 1350));
			SequenceSetupPanel.setLayout(new BorderLayout());
			
			JPanel NoiseSetupPanel = new JPanel();
			NoiseSetupPanel.setLocation(0, 0);
			NoiseSetupPanel.setBackground(backgroundColor);
			NoiseSetupPanel.setForeground(labelColor);
			NoiseSetupPanel.setPreferredSize(new Dimension(1350, 1350));
			NoiseSetupPanel.setLayout(new BorderLayout());
			
			JPanel gravityModelPanel = new JPanel();
			gravityModelPanel.setLocation(0, 0);
			gravityModelPanel.setBackground(backgroundColor);
			gravityModelPanel.setForeground(labelColor);
			gravityModelPanel.setPreferredSize(new Dimension(1350, 1350));
			gravityModelPanel.setLayout(new BorderLayout());
			
			//---------------------------------------------------------------------------------------
			/**			Create Setup panel:
			 * comprising:
			 * 				- SidePanelLeft
			 * 				- CenterPanelRight
			 * 				-> Parent constructor: BasicSetup Panel 
			 */
			//---------------------------------------------------------------------------------------
			
			
			
			@SuppressWarnings("unused")
			BasicSetupMain basicSetupMasterPanel = new BasicSetupMain();
			
		    //-------------
		    /**
		        
		        Create Aerodynamic Setup page 
		    */
		      AerodynamicSetup aeroSetup = new AerodynamicSetup();
			//---------------------------------------------------------------------------------------
			/**
			 * 		Create Setup area tabbed pane structure 
			 * 
			 * 
			 * 
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
	      
	     	TabPane_SimulationSetup.addTab("Basic Setup" , icon_setup2, BasicSetupMain.getMainPanel(), null);
	     	TabPane_SimulationSetup.addTab("Sequence Setup" , icon_setup2, SequenceSetupPanel, null);
	     	TabPane_SimulationSetup.addTab("Aerodynamic Setup" , icon_aerodynamic, aeroSetup.getMainPanel(), null);
	     	TabPane_SimulationSetup.addTab("Gravity Setup" , icon_setup2, gravityModelPanel, null);
	     	TabPane_SimulationSetup.addTab("Noise and Error Model Setup" , null, NoiseSetupPanel, null);
	     	PageX04_SimSetup.add(TabPane_SimulationSetup);
		TabPane_SimulationSetup.setSelectedIndex(0);
		TabPane_SimulationSetup.setFont(small_font);
		TabPane_SimulationSetup.setForeground(Color.black);
		//---------------------------------------------------------------------------------------
		
      int INPUT_width = 110;
      int SidePanel_Width = 380; 
      
    //------------------------------------------------------------------------------------------------------------------
	  //   Right side :
    JPanel P2_ControllerPane = new JPanel();
   // P2_ControllerPane.setLayout(null);
    //P2_ControllerPane.setPreferredSize(new Dimension((exty_main+400),290));
    P2_ControllerPane.setBackground(backgroundColor);
    P2_ControllerPane.setForeground(labelColor);
    
    //-----------------------------------------------------------------------------------------------------------------------------
    //                  Additional Setup 
    //-----------------------------------------------------------------------------------------------------------------------------

    

				//-----------------------------------------------------------------------------------------
			    // ---->>>>>                       TAB: Aerodynamic Setup 
				//-----------------------------------------------------------------------------------------		    
			    
    				AerodynamicSetupSpacecraft aeroSetupSpacecraft = new AerodynamicSetupSpacecraft();
    				
    				
    				//-----------------------------------------------------------------------------------------
    			    // ---->>>>>                       TAB: Propulsion Setup 
    				//-----------------------------------------------------------------------------------------		    
    			    
    				PropulsionSetup propulsionSetup = new PropulsionSetup();
			    
    
				//-----------------------------------------------------------------------------------------
			    // ---->>>>>                       TAB: Aerodynamic Setup Sim side
				//-----------------------------------------------------------------------------------------	
				JPanel SequenceRightPanel = new JPanel();
				SequenceRightPanel.setLocation(0, 0);
				SequenceRightPanel.setBackground(backgroundColor);
				SequenceRightPanel.setForeground(labelColor); 
				SequenceRightPanel.setSize(400, 600);
				SequenceRightPanel.setLayout(null); 

			   
			    SequenceLeftPanel = new JPanel();
				SequenceLeftPanel.setLocation(0, 0);
				SequenceLeftPanel.setBackground(backgroundColor);
				SequenceLeftPanel.setForeground(labelColor);
				SequenceLeftPanel.setPreferredSize(new Dimension(sequenceDimensionWidth, 660));
				SequenceLeftPanel.setLayout(null); 
			    	
			  
			      JScrollPane ScrollPaneSequenceInput = new JScrollPane(SequenceLeftPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			    		  																	  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			      ScrollPaneSequenceInput.getVerticalScrollBar().setUnitIncrement(16);
			      ScrollPaneSequenceInput.getHorizontalScrollBar().setUnitIncrement(16);
			      SequenceSetupPanel.add(ScrollPaneSequenceInput, BorderLayout.CENTER);

			      
				    SequenceProgressBar = new JPanel();
				    SequenceProgressBar.setLocation(0, 370);
				    SequenceProgressBar.setBackground(backgroundColor);
				    SequenceProgressBar.setForeground(labelColor);
				    SequenceProgressBar.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
				    SequenceProgressBar.setSize(sequenceDimensionWidth, 20);
				    SequenceProgressBar.setLayout(null); 
				    SequenceLeftPanel.add(SequenceProgressBar);
				    
				    JPanel SequenceControlBar = new JPanel();
				    SequenceControlBar.setLocation(5, 5);
				    SequenceControlBar.setBackground(backgroundColor);
				    SequenceControlBar.setForeground(labelColor);
				    SequenceControlBar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				    SequenceControlBar.setSize(500, 50);
				    SequenceControlBar.setLayout(null); 
				    SequenceLeftPanel.add(SequenceControlBar);
				    
				    JLabel SequenceTitle = new JLabel("Sequence Setup");
				    SequenceTitle.setLocation(2, 2);
				    SequenceTitle.setBackground(backgroundColor);
				    SequenceTitle.setForeground(labelColor);
				    SequenceTitle.setSize(150, 20);
				    SequenceControlBar.add(SequenceTitle);
				    
			        JButton SequenceToTheLeftButton = new JButton("");
			        SequenceToTheLeftButton.setLocation(5, 25);
			        SequenceToTheLeftButton.setSize(80,20);
			        SequenceToTheLeftButton.setForeground(BlueBookVisual.getBackgroundColor());
			        SequenceToTheLeftButton.setBackground(BlueBookVisual.getLabelColor());
			        SequenceToTheLeftButton.setOpaque(true);
			        SequenceToTheLeftButton.setBorderPainted(false);
			        SequenceToTheLeftButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
				
	        				for(int i=0;i<sequenceContentList.size();i++) {
								if(sequenceContentList.get(i).isSelected()) {
        							System.out.println("Sequence "+i+" selected.");
        						}
								System.out.println(i+"|"+sequenceContentList.get(i).getSequenceID());
	        				}
						} 
			        	
			        });
			        SequenceControlBar.add(SequenceToTheLeftButton);
				    
			      int globalLeftGap = 30;
			      int globalTopGap = 100;
			      GUISequenceElement sequenceElement1 = new GUISequenceElement(sequenceContentList.size());
			      sequenceContentList.add(sequenceElement1);
			      sequenceContentList.get(0).getMasterPanel().setLocation(globalLeftGap, globalTopGap);
			      SequenceLeftPanel.add(sequenceContentList.get(0).getMasterPanel());

			 
			      
		//-----------------------------------------------------------------------------------------
	    // ---->>>>>                       TAB: Spacecraft Definition
		//-----------------------------------------------------------------------------------------			    
			      
			    // Main (SUB) tabbed Pane for this page
		        JTabbedPane TabPane_SCDefinition = (JTabbedPane) new JTabbedPane();
		        TabPane_SCDefinition.setPreferredSize(new Dimension(extx_main, exty_main));
		        TabPane_SCDefinition.setBackground(backgroundColor);
		        TabPane_SCDefinition.setForeground(Color.BLACK);
		//-------------------------------------------------------------------------------------------	    

	    		
			      JPanel massAndInertiaPanel = new JPanel();
			    //  massAndInertiaPanel.setLocation(0, uy_p41 + 26 * 38 );
			      massAndInertiaPanel.setSize(SidePanel_Width, 550);
			      massAndInertiaPanel.setBackground(backgroundColor);
			      massAndInertiaPanel.setForeground(labelColor);
			      massAndInertiaPanel.setLayout(null);
			      
					JPanel InertiaxPanel = new JPanel();
					InertiaxPanel.setLocation(0, 80);
					InertiaxPanel.setBackground(backgroundColor);
					InertiaxPanel.setForeground(labelColor);
					InertiaxPanel.setSize(400, 600);
					InertiaxPanel.setLayout(null); 
					massAndInertiaPanel.add(InertiaxPanel);
			      
			      JPanel guidanceNavigationAndControlPanel = new JPanel();
			     // propulsionInputPanel.setLocation(0, uy_p41 + 26 * 38 );
			     // propulsionInputPanel.setSize(SidePanel_Width, 350);
			      guidanceNavigationAndControlPanel.setBackground(backgroundColor);
			      guidanceNavigationAndControlPanel.setForeground(labelColor);
			      guidanceNavigationAndControlPanel.setLayout(null);
			
				
			     	if(OS_is==2) {
			     		// Resize image icons for Windows 
			         	 int size=10;
			         	icon_setup2 = new ImageIcon(getScaledImage(icon_setup2.getImage(),size,size));
			         	icon_inertia = new ImageIcon(getScaledImage(icon_inertia.getImage(),size,size));
			         	icon_aerodynamic = new ImageIcon(getScaledImage(icon_aerodynamic.getImage(),size,size));
			      }
			      
				TabPane_SCDefinition.addTab("Mass, Inertia and Geometry" , icon_setup2, massAndInertiaPanel, null);
				TabPane_SCDefinition.addTab("Propulsion" , icon_inertia, propulsionSetup.getMainPanel(), null);
				TabPane_SCDefinition.addTab("Aerodynamic" , icon_aerodynamic, aeroSetupSpacecraft.getMainPanel(), null);
				TabPane_SCDefinition.addTab("GNC" , icon_aerodynamic, guidanceNavigationAndControlPanel, null);
				PageX04_AttitudeSetup.add(TabPane_SCDefinition);
		        TabPane_SCDefinition.setSelectedIndex(0);
		        TabPane_SCDefinition.setFont(small_font);
				
	    //-------------------------------------------------------------------------------------------	
		 // Mass and Inertia  
		        
	 		      
    JSeparator Separator_Page2_2 = new JSeparator();
    Separator_Page2_2.setLocation(0, 0 );
    Separator_Page2_2.setSize(SidePanel_Width, 1);
    Separator_Page2_2.setBackground(Color.black);
    Separator_Page2_2.setForeground(labelColor);
    massAndInertiaPanel.add(Separator_Page2_2);

    
	GeometryFrame window = new GeometryFrame();
	window.getMainPanel().setSize(1000,700);
	window.getMainPanel().setLocation(400, 5);
	massAndInertiaPanel.add(window.getMainPanel());
	
		int box_size_x = 60;
		int box_size_y = 25;
		int gap_size_x =  4;
		int gap_size_y =  15;
		
    JLabel LABEL_SpaceCraftSettings = new JLabel("Spacecraft Settings");
    LABEL_SpaceCraftSettings.setLocation(0, uy_p41 + 10 * 0  );
    LABEL_SpaceCraftSettings.setSize(400, 20);
    LABEL_SpaceCraftSettings.setBackground(backgroundColor);
    LABEL_SpaceCraftSettings.setForeground(labelColor);
    LABEL_SpaceCraftSettings.setFont(HeadlineFont);
    LABEL_SpaceCraftSettings.setHorizontalAlignment(0);
    massAndInertiaPanel.add(LABEL_SpaceCraftSettings);
    JLabel LABEL_Minit = new JLabel("Initial mass [kg]");
    LABEL_Minit.setLocation(INPUT_width+5, uy_p41 + 25 * 1 );
    LABEL_Minit.setSize(250, 20);
    LABEL_Minit.setBackground(backgroundColor);
    LABEL_Minit.setForeground(labelColor);
    massAndInertiaPanel.add(LABEL_Minit);
    
    INPUT_M0 = new JTextField(10){
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override public void setBorder(Border border) {
			        // No!
			    }
			};
    INPUT_M0.setLocation(2, uy_p41 + 25 * 1 );
    INPUT_M0.setSize(INPUT_width-20, 20);
    INPUT_M0.setBackground(backgroundColor);
    INPUT_M0.setForeground(labelColor);
    INPUT_M0.setHorizontalAlignment(JTextField.RIGHT);
    INPUT_M0.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInput.writeInputFile(FilePaths.inputFile);
			WriteInput.writeInputFile(FilePaths.inputFile);
		}
  	  
    });
    massAndInertiaPanel.add(INPUT_M0);
		        
			      JSeparator Separator_Inertia = new JSeparator();
			      Separator_Inertia.setLocation(0, 0 );
			      Separator_Inertia.setSize(SidePanel_Width, 1);
			      Separator_Inertia.setBackground(Color.black);
			      Separator_Inertia.setForeground(labelColor);
			      InertiaxPanel.add(Separator_Inertia);

				  // Space intended for advanced integrator settings 
			      JLabel LABEL_InertiaTensor = new JLabel("Inertia Tensor [kg m\u00b2 ] ");
			      LABEL_InertiaTensor.setLocation(0, uy_p41 + 10 * 0  );
			      LABEL_InertiaTensor.setSize(190, 20);
			      LABEL_InertiaTensor.setBackground(backgroundColor);
			      LABEL_InertiaTensor.setForeground(labelColor);
			      LABEL_InertiaTensor.setFont(HeadlineFont);
			      LABEL_InertiaTensor.setHorizontalAlignment(0);
			      InertiaxPanel.add(LABEL_InertiaTensor);
		        
		        
				JPanel InertiaMatrixPanel = new JPanel();
				InertiaMatrixPanel.setLayout(null);
				InertiaMatrixPanel.setLocation(10, 40);
				InertiaMatrixPanel.setBackground(backgroundColor);
				InertiaMatrixPanel.setForeground(labelColor);
				InertiaMatrixPanel.setSize(330, 370);
				InertiaMatrixPanel.setBorder(Moon_border);
				InertiaxPanel.add(InertiaMatrixPanel);
				
		        String path2 = "images/momentOfInertia.png";
		        File file2 = new File(path2);
		        try {
		        BufferedImage image3 = ImageIO.read(file2);
		        JLabel label2 = new JLabel(new ImageIcon(image3));
		        label2.setSize(320,240);
		        label2.setLocation(5, 125);
		        label2.setBorder(Moon_border);
		        InertiaMatrixPanel.add(label2);
		        } catch (Exception e) {
		        	System.err.println("Error: SpaceShip Setup/Aerodynamik - could not load image");
		        }

				
		         INPUT_IXX = new JTextField();
		        INPUT_IXX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*0);
		        INPUT_IXX.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IXX.setBorder(Moon_border);
		        INPUT_IXX.setSize(box_size_x, box_size_y);
		        INPUT_IXX.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteInput.writeInputFile(FilePaths.inputFile);
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IXX);	 
		        
			      JLabel LABEL_IXX = new JLabel("Ixx");
			      LABEL_IXX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*0 - 15);
			      LABEL_IXX.setSize(box_size_x, 20);
			      LABEL_IXX.setBackground(backgroundColor);
			      LABEL_IXX.setForeground(labelColor);
			      LABEL_IXX.setFont(small_font);
			      LABEL_IXX.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IXX);
 
		         INPUT_IXY = new JTextField("0");
		        INPUT_IXY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*0);
		        INPUT_IXY.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IXY.setBorder(Moon_border);
		        INPUT_IXY.setSize(box_size_x, box_size_y);
		        INPUT_IXY.setEditable(false);
		        INPUT_IXY.setBackground(Color.lightGray);
		        INPUT_IXY.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteInput.writeInputFile(FilePaths.inputFile);
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IXY);	 
		        
			      JLabel LABEL_IXY = new JLabel("Ixy");
			      LABEL_IXY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*0 - 15);
			      LABEL_IXY.setSize(box_size_x, 20);
			      LABEL_IXY.setBackground(backgroundColor);
			      LABEL_IXY.setForeground(labelColor);
			      LABEL_IXY.setFont(small_font);
			      LABEL_IXY.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IXY);
		        
		         INPUT_IXZ = new JTextField();
		        INPUT_IXZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*0);
		        INPUT_IXZ.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IXZ.setBorder(Moon_border);
		        INPUT_IXZ.setSize(box_size_x, box_size_y);
		        INPUT_IXZ.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteInput.writeInputFile(FilePaths.inputFile);
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IXZ);	
		        
			      JLabel LABEL_IXZ = new JLabel("Ixz");
			      LABEL_IXZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*0 - 15);
			      LABEL_IXZ.setSize(box_size_x, 20);
			      LABEL_IXZ.setBackground(backgroundColor);
			      LABEL_IXZ.setForeground(labelColor);
			      LABEL_IXZ.setFont(small_font);
			      LABEL_IXZ.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IXZ);
		        
		         INPUT_IYX = new JTextField("0");
		        INPUT_IYX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*1);
		        INPUT_IYX.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IYX.setBorder(Moon_border);
		        INPUT_IYX.setSize(box_size_x, box_size_y);
		        INPUT_IYX.setEditable(false);
		        INPUT_IYX.setBackground(Color.lightGray);
		        INPUT_IYX.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteInput.writeInputFile(FilePaths.inputFile);
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IYX);	
		        
			      JLabel LABEL_IYX = new JLabel("Iyx");
			      LABEL_IYX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*1 - 15);
			      LABEL_IYX.setSize(box_size_x, 20);
			      LABEL_IYX.setBackground(backgroundColor);
			      LABEL_IYX.setForeground(labelColor);
			      LABEL_IYX.setFont(small_font);
			      LABEL_IYX.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IYX);
 
		         INPUT_IYY = new JTextField();
		        INPUT_IYY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*1);
		        INPUT_IYY.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IYY.setBorder(Moon_border);
		        INPUT_IYY.setSize(box_size_x, box_size_y);
		        INPUT_IYY.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteInput.writeInputFile(FilePaths.inputFile);
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IYY);
		        
			      JLabel LABEL_IYY = new JLabel("Iyy");
			      LABEL_IYY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*1 - 15);
			      LABEL_IYY.setSize(box_size_x, 20);
			      LABEL_IYY.setBackground(backgroundColor);
			      LABEL_IYY.setForeground(labelColor);
			      LABEL_IYY.setFont(small_font);
			      LABEL_IYY.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IYY);
		        
		         INPUT_IYZ = new JTextField("0");
		        INPUT_IYZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*1);
		        INPUT_IYZ.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IYZ.setBorder(Moon_border);
		        INPUT_IYZ.setSize(box_size_x, box_size_y);
		        INPUT_IYZ.setEditable(false);
		        INPUT_IYZ.setBackground(Color.lightGray);
		        INPUT_IYZ.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteInput.writeInputFile(FilePaths.inputFile);
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IYZ);	
		        
			      JLabel LABEL_IYZ = new JLabel("Iyz");
			      LABEL_IYZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*1 - 15);
			      LABEL_IYZ.setSize(box_size_x, 20);
			      LABEL_IYZ.setBackground(Color.gray);
			      LABEL_IYZ.setForeground(labelColor);
			      LABEL_IYZ.setFont(small_font);
			      LABEL_IYZ.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IYZ);
		        
		        INPUT_IZX = new JTextField();
		        INPUT_IZX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*2);
		        INPUT_IZX.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IZX.setBorder(Moon_border);
		        INPUT_IZX.setSize(box_size_x, box_size_y);
		        INPUT_IZX.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteInput.writeInputFile(FilePaths.inputFile);
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IZX);	 
		        
			      JLabel LABEL_IZX = new JLabel("Izx");
			      LABEL_IZX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*2 - 15);
			      LABEL_IZX.setSize(box_size_x, 20);
			      LABEL_IZX.setBackground(backgroundColor);
			      LABEL_IZX.setForeground(labelColor);
			      LABEL_IZX.setFont(small_font);
			      LABEL_IZX.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IZX);
 
		         INPUT_IZY = new JTextField("0");
		        INPUT_IZY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*2);
		        INPUT_IZY.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IZY.setBorder(Moon_border);
		        INPUT_IZY.setSize(box_size_x, box_size_y);
		        INPUT_IZY.setEditable(false);
		        INPUT_IZY.setBackground(Color.lightGray);
		        INPUT_IZY.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteInput.writeInputFile(FilePaths.inputFile);
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IZY);	
		        
			      JLabel LABEL_IZY = new JLabel("Izy");
			      LABEL_IZY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*2 - 15);
			      LABEL_IZY.setSize(box_size_x, 20);
			      LABEL_IZY.setBackground(backgroundColor);
			      LABEL_IZY.setForeground(labelColor);
			      LABEL_IZY.setFont(small_font);
			      LABEL_IZY.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IZY);

		        
		        INPUT_IZZ = new JTextField();
		        INPUT_IZZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*2);
		        INPUT_IZZ.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		        INPUT_IZZ.setBorder(Moon_border);
		        INPUT_IZZ.setSize(box_size_x, box_size_y);
		        INPUT_IZZ.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WriteInput.writeInputFile(FilePaths.inputFile);
					}
			    	  
			      });
		        InertiaMatrixPanel.add(INPUT_IZZ);	
		        
			      JLabel LABEL_IZZ = new JLabel("Izz");
			      LABEL_IZZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*2 - 15);
			      LABEL_IZZ.setSize(box_size_x, 20);
			      LABEL_IZZ.setBackground(backgroundColor);
			      LABEL_IZZ.setForeground(labelColor);
			      LABEL_IZZ.setFont(small_font);
			      LABEL_IZZ.setHorizontalAlignment(0);
			      InertiaMatrixPanel.add(LABEL_IZZ);
			      
			      
		        
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
    		 * The following function checks the filesize before the input and switches
    		 * to a partial import for large files
    		 * The full import can be triggered manually by the user once the program
    		 * has started fully. 
    		 */
    		// Check filesize 
    	    long filesize = 	new File(FilePaths.RES_File).length()/1000000;
    	    try {
    	    if(filesize<10) {
    	    		update(true);
    	    } else {
    	    		update(false);
    	    	System.out.println("Full data import supressed. Filesize prohibits fast startup.");
    	    }
       	    GuiReadInput.readINP();
       	    DashboardPlotArea.setAnalysisFile(analysisFile);
       	    DashboardPlotArea.setTargetIndx(indx_target);
       	    CenterPanelRight.createTargetWindow();

    		SidePanelLeft.Update_IntegratorSettings();

    	      try {
    	      READ_sequenceFile();
    	      } catch(Exception e) {
    	    	  System.out.println("ERROR: Reading sequenceFile.inp failed.");
    	      }
    	    } catch(Exception excpM) {
    	    	System.out.println("Error: Reading input failed. ");
    	    	System.out.println(excpM);
    	    }
        MainGUI.setOpaque(true);
        return MainGUI;
	}
    public void actionPerformed(ActionEvent e)  {
    	
    }
    
    static Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
	public static void update(boolean fullImport){
		  try {
			GuiReadInput.readINP();
			if(fullImport) {
				rawData.readRawData();
				MapSetting.setMap(indx_target);
			}
		} catch (IOException | URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		  if(fullImport) {

			  mercatorMap.update();

      	    		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
      	    		System.out.println("Updated "+timeStamp);
		  }
	  } 

    
    

    
    public static void READ_sequenceFile() throws IOException{	
		BufferedReader br = new BufferedReader(new FileReader(FilePaths.sequenceFile));
       String strLine;
       String fcSeparator="\\|FlightControllerElements\\|";
       String eventSeparator="\\|EventManagementElements";
       String endSeparator="\\|EndElement\\|";
       int sequenceID=0;
       try {
       while ((strLine = br.readLine()) != null )   {
	       
	       	String[] initSplit = strLine.split(fcSeparator);

	       	String[] head = initSplit[0].split(" ");
	       //System.out.pri
	       //	int  ID = Integer.parseInt(head[0]);
	       	String sequenceName = head[1];
	       	int flightControllerIndex = Integer.parseInt(initSplit[1].split(" ")[1]);
	       	String[] arr     = strLine.split(eventSeparator);
	       	//System.out.println(arr[1]);
	       	int eventIndex  = Integer.parseInt(arr[1].split(" ")[1]);
	       	
	       	String[] arr2   = strLine.split(endSeparator);
	       	//System.out.println(arr2[1]);
	       	int endIndex    = Integer.parseInt(arr2[1].split(" ")[1]);
	       	double endValue = Double.parseDouble(arr2[1].split(" ")[2]);
	       	
	       //	System.out.println(ID+" "+sequenceName+" "+flightControllerIndex+" "+eventIndex+" "+endIndex+" "+endValue);
	       	
	       	if(sequenceID!=0) {
	       		GUISequenceElement.addGUISequenceElment();
	       	} 
	       	sequenceContentList.get(sequenceID).setSequenceName(sequenceName);
       		sequenceContentList.get(sequenceID).setFlightControllerSelectIndex(flightControllerIndex);
       		sequenceContentList.get(sequenceID).setEventSelectIndx(eventIndex);
       		sequenceContentList.get(sequenceID).setEndSelectIndex(endIndex);
       		sequenceContentList.get(sequenceID).setValueEnd(""+endValue);
	       	sequenceID++;
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}

   }
    

    
    

    public static double readFromFile(String file, int indx) {
    	FileInputStream fstream = null;
    	double result=0;
    	  try {
    	      fstream = new FileInputStream(file);
    	} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading from file failed.");} 
    	DataInputStream in55 = new DataInputStream(fstream);
    	@SuppressWarnings("resource")
    	BufferedReader br55 = new BufferedReader(new InputStreamReader(in55));
    	int k = 0;
    	double InitialState = 0;
    	String strLine55;
    	try {
    	while ((strLine55 = br55.readLine()) != null )   {
    		String[] tokens = strLine55.split(" ");
    		if(!tokens[0].isEmpty()) {
    		 InitialState = Double.parseDouble(tokens[0]);
    		} else {
    			InitialState =0; 
    			//System.out.println("isempty  "+indx+"|"+k);
    		}
    	 	if (k==indx){
    		  result = InitialState;
    		} 
    	 	//System.out.println(k+"|"+indx);
    		k++;
    	}
    	in55.close();
    	br55.close();
    	fstream.close();
    	} catch (NullPointerException eNPE) { System.out.println(eNPE);} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	return result;
    }





	public static double[] Spherical2Cartesian(double[] X) {
	double[] result = new double[3];
	result[0]  =  X[0] * Math.cos(X[1]) * Math.cos(X[2]);
	result[1]  =  X[0] * Math.cos(X[1]) * Math.sin(X[2]);
	result[2]  = -X[0] * Math.sin(X[1]);
	
	// Filter small errors from binary conversion: 
	for(int i=0;i<result.length;i++) {if(Math.abs(result[i])<1E-9) {result[i]=0; }}
	return result; 
	}
	
	public static double[] Cartesian2Spherical(double[] X) {
	double[] result = new double[3];
	result[1] = -Math.atan(X[2]/(Math.sqrt(X[0]*X[0] + X[1]*X[1])));
	result[0] = Math.sqrt(X[0]*X[0] + X[1]*X[1] + X[2]*X[2]);
	result[2] = Math.atan2(X[1],X[0]);

	// Filter small errors from binary conversion: 
	for(int i=0;i<result.length;i++) {if(Math.abs(result[i])<1E-9) {result[i]=0; }}
	return result; 
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
         }catch(IIOException eIIO) {System.out.println(eIIO);}    
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



		
		
		public static class CustomRenderer extends DefaultListCellRenderer {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value,
			        int index, boolean isSelected, boolean cellHasFocus) {
			    super.getListCellRendererComponent(list, value, index, isSelected,
			            cellHasFocus);
			    setBackground(backgroundColor);
			    setForeground(labelColor);     
			    return this;
			}  
		}

		public static String[] getAxis_Option_NR() {
			return Axis_Option_NR;
		}
		public static Font getSmall_font() {
			return small_font;
		}
		public static  List<JLabel> getSequenceProgressBarContent() {
			return sequenceProgressBarContent;
		}
		public static  List<GUISequenceElement> getSequenceContentList() {
			return sequenceContentList;
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