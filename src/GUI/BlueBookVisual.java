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
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;

import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import javax.swing.*;  

import Sequence.SequenceElement;
import Simulator_main.DataSets.RealTimeResultSet;
import GUI.PropulsionDraw.PropulsionDrawEditor;
import GUI.SimulationSetup.BasicSetup.AttitudeSetting;
import GUI.SimulationSetup.BasicSetup.BasicSetupMain;
import GUI.SimulationSetup.BasicSetup.CenterPanelRight;
import GUI.SimulationSetup.BasicSetup.SidePanelLeft;
import VisualEngine.animation.AnimationSet;
import utils.ReadInput;

import com.apple.eawt.Application;

import GUI.Dashboard.DashboardLeftPanel;
import GUI.Dashboard.DashboardPanel;
import GUI.Dashboard.DashboardPlotArea;
import GUI.DataStructures.InputFileSet;
import GUI.MenuBar.MenuBar;

public class BlueBookVisual implements  ActionListener {
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Main Container Frame Elements
    //-----------------------------------------------------------------------------------------------------------------------------------------
	public static String PROJECT_TITLE = "  BlueBook Descent and Landing Analysis Toolkit - V0.3 ALPHA";
	static boolean darkTheme = true; 
    static int x_init = 1350;
    static int y_init = 860 ;
    public static JFrame MAIN_frame;
    
    public static String CASE_FileEnding   = ".case";
    public static String RESULT_FileEnding = ".res";
    public static int OS_is = 1; 
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Relative File Paths
    //-----------------------------------------------------------------------------------------------------------------------------------------
	public static String Elevation_File_RES4 	= "/ELEVATION/LRO_4.csv";
	public static String Elevation_File_RES16	= "/ELEVATION/LRO_16.csv";
	public static String Elevation_File_RES64 	= "/ELEVATION/LRO_16.csv";
	public static String Elevation_File_RES128 	= "/ELEVATION/LRO_128.csv";
	public static String LOCALELEVATIONFILE		= "/LocalElevation.inp";   		//
    public static String Init_File   			= "/INP/init.inp" ;		    		// Input: Initial state
    public static String RES_File    			= "/results.txt"  ;       	 	// Input: result file 
    public static String CTR_001_File			= "/CTRL/cntrl_1.inp";		    // Controller 01 input file 
    public static String Prop_File 	 			= "/INP/PROP/prop.inp";			// Main propulsion ystem input file 
    public static String SEQU_File		 		= "/SEQU.res";					// Sequence output file 
    public static String SC_file 				= "/INP/SC/sc.inp";
    public static String ICON_File   	 		= "/images/BB_icon4.png";
    public static String ERROR_File 				= "/INP/ErrorFile.inp";
    public static String SEQUENCE_File   		= "/INP/sequence_1.inp"; 
    public static String CONTROLLER_File			= "CTRL/ctrl_main.inp";
	public static String MAP_EARTH				= "/MAPS/Earth_MAP.jpg";
	public static String MAP_MOON				= "/MAPS/Moon_MAP.jpg";
	public static String MAP_VENUS				= "/MAPS/Venus_MAP.jpg";
	public static String MAP_MARS				= "/MAPS/Mars_MAP.jpg";
	public static String MAP_SOUTHPOLE_MOON		= "/MAPS/Moon_South_Pole.jpg";
	public static String EventHandler_File		= "/INP/eventhandler.inp";
	public static String INTEG_File_01 			= "/INP/INTEG/00_DormandPrince853Integrator.inp";
	public static String INTEG_File_02 			= "/INP/INTEG/01_ClassicalRungeKuttaIntegrator.inp";
	public static String INTEG_File_03 			= "/INP/INTEG/02_GraggBulirschStoerIntegrator.inp";
	public static String INTEG_File_04 			= "/INP/INTEG/03_AdamsBashfordIntegrator.inp";
	public static String INERTIA_File 		    = "/INP/INERTIA.inp";
	public static String InitialAttitude_File   = "/INP/INITIALATTITUDE.inp";
    public static String Aero_file 		        = System.getProperty("user.dir") + "/INP/AERO/aeroBasic.inp";
    public static String sequenceFile 		    = System.getProperty("user.dir") + "/INP/sequenceFile.inp";
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
	//public static String CurrentWorkfile_Name = "";
	//public static File CurrentWorkfile_Path = new File("");
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //											Styles, Fonts, Colors
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static int gg = 235;
    //public static Color labelColor = new Color(0,0,0);    					    // Label Color
    public static Color labelColor = new Color(220,220,220);    					// Label Color
   	public static Color backgroundColor = new Color(41,41,41);				    // Background Color

   	public static Color valueColor =  new Color(65,105,225);
   	//public static Color valueColor2 =  new Color(255,140,0);
   	//public static Color w_c = new Color(gg,gg,gg);					            // Box background color
   //	public static Color t_c = new Color(255,255,255);				        // Table background color
   	//public static Color t_c = new Color(61,61,61);				                // Table background color
   	
   	
   	public static Color light_gray = new Color(230,230,230);
   	
    static DecimalFormat decf 		  = new DecimalFormat("#.#");
    static DecimalFormat decAngularRate =  new DecimalFormat("##.####");
    static DecimalFormat df_X4 		  = new DecimalFormat("#####.###");
    static DecimalFormat df_VelVector = new DecimalFormat("#.00000000");
    //static Font menufont              = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12);
    static Font small_font			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    static Font labelfont_small       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
    //static Font labelfont_verysmall   = new Font("Verdana", Font.BOLD, 7);
    
    static Font HeadlineFont          = new Font("Georgia", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    public static DecimalFormat df 	  = new DecimalFormat();
    
    static List<JRadioButton> DragModelSet = new ArrayList<JRadioButton>();
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
    										  "TM CNTRL Error [m/s]",
    										  "TVC CNTRL Error [deg]",
    										  "Thrust Elevation [deg]",
    										  "Thrust Elevation Angel Rate [deg/s]",
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
    static int extx_main = 1350;
    public static int exty_main = 800; 
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												GUI Elements
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    
    public static boolean chartA3_fd=true;  
	static boolean Chart_DashBoardFlexibleChart_fd = true;
	static boolean CHART_P1_DashBoardOverviewChart_fd = true;
  
       
    private static Crosshair xCrosshair_x;
    private static Crosshair yCrosshair_x; 
    
   
    public static JPanel dashboardPanel;
    public static JPanel PageX04_Map;
    public static JPanel PageX04_3;
    public static JPanel PageX04_SimSetup; 
    public static JPanel PageX04_RawDATA; 
    public static JPanel PageX04_PolarMap;
    public static JPanel PolarMapContainer; 
    public static JPanel P1_Plotpanel; 
    public static JPanel PageX04_AttitudeSetup;

    
    
    public static JFreeChart Chart_MercatorMap;
    public static JFreeChart Chart_GroundClearance;
	public static JFreeChart CHART_P1_DashBoardOverviewChart_Altitude_Velocity;
	public static JFreeChart CHART_P1_DashBoardOverviewChart_Time_FPA;
	public static JFreeChart chart_PolarMap;	  
	public static JFreeChart Chart_DashBoardFlexibleChart;	
	public static ChartPanel ChartPanel_DashBoardOverviewChart_Altitude_Velocity; 
	public static ChartPanel ChartPanel_DashBoardOverviewChart_Time_FPA; 
	public static ChartPanel ChartPanel_DashBoardFlexibleChart;    
    static public JFreeChart chartA3_1,chartA3_2,chartA3_3,chartA3_4; 
    public static ChartPanel CP_A31,CP_A32,CP_A33,CP_A34;
	public static XYSeriesCollection CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity = new XYSeriesCollection();
	public static DefaultTableXYDataset CHART_P1_DashBoardOverviewChart_Dataset_Time_FPA = new DefaultTableXYDataset();
	public static DefaultTableXYDataset ResultSet_GroundClearance_FlightPath = new DefaultTableXYDataset();
	public static DefaultTableXYDataset ResultSet_GroundClearance_Elevation = new DefaultTableXYDataset();
	public static XYSeriesCollection ResultSet_FlexibleChart = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_1 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_2 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_3 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_4 = new XYSeriesCollection();
    public static JCheckBox INPUT_ISPMODEL; 
    public static JTextField INPUT_RCSX, INPUT_RCSY, INPUT_RCSZ;
    static JLabel INDICATOR_PageMap_LAT,INDICATOR_PageMap_LONG;
    public static JTextField  INPUT_RB; 
    public static JTextField INPUT_M0, INPUT_WRITETIME,INPUT_ISP,INPUT_PROPMASS,INPUT_THRUSTMAX,INPUT_THRUSTMIN,p42_inp14,p42_inp15,p42_inp16,p42_inp17;
    public static JTextField INPUT_PGAIN,INPUT_IGAIN,INPUT_DGAIN,INPUT_CTRLMAX,INPUT_CTRLMIN,INPUT_ISPMIN, INPUT_SURFACEAREA, INPUT_BALLISTICCOEFFICIENT;
    
    public static JRadioButton RB_SurfaceArea, RB_BallisticCoefficient;
    public static JTextField INPUT_IXX, INPUT_IXY, INPUT_IXZ, INPUT_IYX, INPUT_IYY, INPUT_IYZ, INPUT_IZX, INPUT_IZY, INPUT_IZZ;


    public static TimerTask task_Update;
    public static JTextField ConstantCD_INPUT;
    public static JTextField ConstantParachuteCD_INPUT, INPUT_ParachuteDiameter;
    public static JPanel SequenceLeftPanel;
    public static JTextField INPUT_RCSXTHRUST, INPUT_RCSYTHRUST, INPUT_RCSZTHRUST, INPUT_RCSTANK, INPUT_RCSXISP, INPUT_RCSYISP,INPUT_RCSZISP;
    public static JPanel SequenceProgressBar, FlexibleChartContentPanel, FlexibleChartContentPanel2;
    public static List<JLabel> sequenceProgressBarContent = new ArrayList<JLabel>();
    static DefaultTableModel MODEL_RAWData;
    static JTable TABLE_RAWData; 

    
    

	 

	 
	 static int c_ERROR = 5;
	 static Object[] ROW_ERROR = new Object[c_ERROR];
	 static DefaultTableModel MODEL_ERROR;
	 static JTable TABLE_ERROR; 
	 
	 static int c_EventHanlder = 2;
	 static Object[] ROW_EventHandler = new Object[c_EventHanlder];
	 static DefaultTableModel MODEL_EventHandler;

    static Border Earth_border = BorderFactory.createLineBorder(Color.BLUE, 1);
    static Border Moon_border 	= BorderFactory.createLineBorder(Color.GRAY, 1);
    static Border Mars_border 	= BorderFactory.createLineBorder(Color.ORANGE, 1);
    static Border Venus_border = BorderFactory.createLineBorder(Color.GREEN, 1);
    public static JCheckBox p421_linp0;

    static XYSeriesCollection ResultSet_MercatorMap = new XYSeriesCollection();
    static XYSeriesCollection ResultSet_PolarMap = new XYSeriesCollection();
    	static int page1_plot_y =380;
    	@SuppressWarnings("rawtypes")
    	public static JComboBox axis_chooser3,axis_chooser4; 
    	public static int thirdWindowIndx = 1;
    	
    	
    	public static List<Component> AeroLeftBarAdditionalComponents = new ArrayList<Component>();
    	public static List<Component> AeroParachuteBarAdditionalComponents = new ArrayList<Component>();
    	public static List<JRadioButton> ParachuteBulletPoints = new ArrayList<JRadioButton>();
     	public static  List<RealTimeResultSet> resultSet = new ArrayList<RealTimeResultSet>();
     	public static  List<GUISequenceElement> sequenceContentList = new ArrayList<GUISequenceElement>();
     	public static 				int sequenceDimensionWidth=1500;
     	
     	
     	private static List<InputFileSet> analysisFile = new ArrayList<InputFileSet>();
	//-----------------------------------------------------------------------------
	public JPanel createContentPane () throws IOException{
    	JPanel MainGUI = new JPanel();
    	MainGUI = new JPanel();
    	MainGUI.setBackground(backgroundColor);
    	MainGUI.setLayout(new BorderLayout());
    	//---------------------------------------------------------------------------------------
    	//                Initialize paths from relative to absolute file paths
    	//---------------------------------------------------------------------------------------
    	 String dir = System.getProperty("user.dir");
    	 
    	 Init_File = dir + Init_File ;
    	 RES_File  = dir + RES_File  ;
    	 CTR_001_File  = dir + CTR_001_File  ;
    	 Prop_File  = dir + Prop_File  ;
    	 SEQU_File = dir + SEQU_File; 
    	 ICON_File = dir + ICON_File; 
    	 SEQUENCE_File = dir +SEQUENCE_File; 
    	 ERROR_File = dir + ERROR_File; 
    	 SC_file = dir + SC_file;
    	 Elevation_File_RES4 = dir + Elevation_File_RES4 ;
    	 Elevation_File_RES16 = dir + Elevation_File_RES16 ;
    	 Elevation_File_RES64 = dir + Elevation_File_RES64 ;
    	 Elevation_File_RES128 = dir + Elevation_File_RES128 ;
    	 LOCALELEVATIONFILE = dir + LOCALELEVATIONFILE;
    	 MAP_MARS = dir + MAP_MARS;
    	 MAP_EARTH = dir + MAP_EARTH;
    	 MAP_MOON = dir + MAP_MOON;
    	 MAP_VENUS = dir + MAP_VENUS;
    	 MAP_SOUTHPOLE_MOON = dir + MAP_SOUTHPOLE_MOON;
    	 EventHandler_File = dir + EventHandler_File;
    	 INTEG_File_01 = dir + INTEG_File_01;
    	 INTEG_File_02 = dir + INTEG_File_02;
    	 INTEG_File_03 = dir + INTEG_File_03;
    	 INTEG_File_04 = dir + INTEG_File_04; 
    	 INERTIA_File  = dir + INERTIA_File;
    	 InitialAttitude_File = dir + InitialAttitude_File;
    	 

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
    	  task_Update = new FileWatcher( new File(RES_File) ) {
    		    protected void onChange( File file ) {

          		  UPDATE_Page01(true);
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
        PageX04_Map = new JPanel();
        PageX04_Map.setLocation(0, 0);
        PageX04_Map.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_Map.setLayout(new BorderLayout());
        PageX04_Map.setBackground(backgroundColor);
        PageX04_Map.setForeground(labelColor);
        PageX04_3 = new JPanel();
        PageX04_3.setLocation(0, 0);
        PageX04_3.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_3.setLayout(new BorderLayout());
        PageX04_3.setBackground(backgroundColor);
        PageX04_3.setForeground(labelColor);
        PageX04_SimSetup = new JPanel(); 
        PageX04_SimSetup.setLocation(0, 0);
        PageX04_SimSetup.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_SimSetup.setLayout(new BorderLayout());
        PageX04_SimSetup.setBackground(backgroundColor);
        PageX04_SimSetup.setForeground(labelColor);
        PageX04_PolarMap = new JPanel();
        PageX04_PolarMap.setLocation(0, 0);
        PageX04_PolarMap.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_PolarMap.setLayout(new BorderLayout());
        PageX04_PolarMap.setBackground(backgroundColor);
        PageX04_PolarMap.setForeground(labelColor);
        PageX04_RawDATA = new JPanel();
        PageX04_RawDATA.setLocation(0, 0);
        PageX04_RawDATA.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_RawDATA.setLayout(new BorderLayout());
        PageX04_RawDATA.setBackground(backgroundColor);
        PageX04_RawDATA.setForeground(labelColor);
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
		JPanel AerodynamicSetupPanel = new JPanel();
		AerodynamicSetupPanel.setLocation(0, 0);
		AerodynamicSetupPanel.setBackground(backgroundColor);
		AerodynamicSetupPanel.setForeground(labelColor);
		AerodynamicSetupPanel.setSize(400, 600);
		AerodynamicSetupPanel.setLayout(new BorderLayout()); 
		
	      
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
	     	TabPane_SimulationSetup.addTab("Aerodynamic Setup" , icon_aerodynamic, AerodynamicSetupPanel, null);
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
			    // ---->>>>>                       TAB: Aerodynamic Setup Sim sided
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
							// TODO Auto-generated method stub
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
			    // ---->>>>>                       TAB: Aerodynamic Setup Sim sided
				//-----------------------------------------------------------------------------------------		    
			    
				JPanel AerodynamicRightPanel = new JPanel();
				AerodynamicRightPanel.setLocation(0, 0);
				AerodynamicRightPanel.setBackground(backgroundColor);
				AerodynamicRightPanel.setForeground(labelColor);
				AerodynamicRightPanel.setSize(400, 600);
				AerodynamicRightPanel.setLayout(null); 

			   
				JPanel AerodynamicLeftPanel = new JPanel();
			    AerodynamicLeftPanel.setLocation(0, 0);
			    AerodynamicLeftPanel.setBackground(backgroundColor);
			    AerodynamicLeftPanel.setForeground(labelColor);
			    AerodynamicLeftPanel.setSize(400, 600);
			    AerodynamicLeftPanel.setLayout(null); 
			    
				JPanel AerodynamicDragPanel = new JPanel();
				AerodynamicDragPanel.setLocation(0, 0);
				AerodynamicDragPanel.setBackground(backgroundColor);
				AerodynamicDragPanel.setForeground(labelColor);
				AerodynamicDragPanel.setSize(400, 150);
				AerodynamicDragPanel.setLayout(null); 
				AerodynamicLeftPanel.add(AerodynamicDragPanel);
				
			    JPanel AerodynamicParachutePanel = new JPanel();
				AerodynamicParachutePanel.setLocation(0, (int) AerodynamicDragPanel.getSize().getHeight());
				AerodynamicParachutePanel.setBackground(backgroundColor);
				AerodynamicParachutePanel.setForeground(labelColor);
				AerodynamicParachutePanel.setSize(190, 300);
				AerodynamicParachutePanel.setLayout(null); 
				AerodynamicLeftPanel.add(AerodynamicParachutePanel);
				
			    JPanel AerodynamicParachuteOptionPanel = new JPanel();
			    AerodynamicParachuteOptionPanel.setLocation(190, (int) AerodynamicDragPanel.getSize().getHeight());
			    AerodynamicParachuteOptionPanel.setBackground(backgroundColor);
			    AerodynamicParachuteOptionPanel.setForeground(labelColor);
			    AerodynamicParachuteOptionPanel.setSize(210, 300);
			    AerodynamicParachuteOptionPanel.setLayout(null); 
				AerodynamicLeftPanel.add(AerodynamicParachuteOptionPanel);

			      
			      JPanel AerodynamicInputPanel = new JPanel();
			      AerodynamicInputPanel.setLocation(0, uy_p41 + 26 * 38 );
			      AerodynamicInputPanel.setSize(SidePanel_Width, 750);
			      AerodynamicInputPanel.setBackground(backgroundColor);
			      AerodynamicInputPanel.setForeground(Color.white);
			      AerodynamicInputPanel.setLayout(null);	
			  
			      JScrollPane ScrollPaneAerodynamicInput = new JScrollPane(AerodynamicLeftPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			      ScrollPaneAerodynamicInput.setPreferredSize(new Dimension(405, exty_main));
			      ScrollPaneAerodynamicInput.getVerticalScrollBar().setUnitIncrement(16);
			      AerodynamicSetupPanel.add(ScrollPaneAerodynamicInput, BorderLayout.LINE_START);
			      JScrollPane ScrollPaneAerodynamicInput2 = new JScrollPane(AerodynamicRightPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			      ScrollPaneAerodynamicInput2.setPreferredSize(new Dimension(405, exty_main));
			      ScrollPaneAerodynamicInput2.getVerticalScrollBar().setUnitIncrement(16);
			      AerodynamicSetupPanel.add(ScrollPaneAerodynamicInput2, BorderLayout.CENTER);
			    
			      JLabel LABELdragModel = new JLabel("Select Aerodynamic Drag Model:");
			      LABELdragModel.setLocation(3, 5 + 25 * 0  );
			      LABELdragModel.setSize(190, 20);
			      LABELdragModel.setBackground(backgroundColor);
			      LABELdragModel.setForeground(labelColor);
			      LABELdragModel.setFont(small_font);
			      LABELdragModel.setHorizontalAlignment(0);
			      AerodynamicDragPanel.add(LABELdragModel);
			      
			      ButtonGroup dragModelGroup = new ButtonGroup();  
				     
			      JRadioButton aeroButton = new JRadioButton("Constant drag coefficient");
			      aeroButton.setLocation(3, 5 + 25 * 1  );
			      aeroButton.setSize(190, 20);
			      aeroButton.setBackground(backgroundColor);
			      aeroButton.setForeground(labelColor);
			      aeroButton.setFont(small_font);
			      aeroButton.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent arg0) {
						WRITE_AERO();
						//------------------------------------------------------------------------
						int indx = getDragModelSetIndx();
						for(int i=0;i<AeroLeftBarAdditionalComponents.size();i++) {
							AerodynamicDragPanel.remove(AeroLeftBarAdditionalComponents.get(i));
						}
						AerodynamicDragPanel.revalidate();
						AerodynamicDragPanel.repaint();
						if(indx==0) {	
						      JLabel LABEL_CD = new JLabel("Set constant CD value [-]");
						      LABEL_CD.setLocation(193, 5 + 25 * 1);
						      LABEL_CD.setSize(300, 20);
						      LABEL_CD.setBackground(backgroundColor);
						      LABEL_CD.setForeground(labelColor);
						      LABEL_CD.setFont(small_font);
						      AeroLeftBarAdditionalComponents.add(LABEL_CD);
						      AerodynamicDragPanel.add(LABEL_CD);
							
					        ConstantCD_INPUT = new JTextField(""+readFromFile(Aero_file,1));
					        ConstantCD_INPUT.setLocation(193, 5 + 25 * 2 );
					        ConstantCD_INPUT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
					        ConstantCD_INPUT.setBorder(Moon_border);
					        ConstantCD_INPUT.setSize(100, 20);
					        ConstantCD_INPUT.setEditable(true);
					        ConstantCD_INPUT.addFocusListener(new FocusListener() {

								@Override
								public void focusGained(FocusEvent arg0) { }

								@Override
								public void focusLost(FocusEvent e) {
									WRITE_AERO();
								}
						   	  
						     });
					        AeroLeftBarAdditionalComponents.add(ConstantCD_INPUT);
					       // RB_INPUT.setBackground(Color.lightGray);
					        AerodynamicDragPanel.add(ConstantCD_INPUT);       
						}
						//------------------------------------------------------------------------
					}
			    	  
			      });
			      //aeroButton.setSelected(true);
			      DragModelSet.add(aeroButton);
			      //aeroButton.setHorizontalAlignment(0);
			      AerodynamicDragPanel.add(aeroButton);
			      dragModelGroup.add(aeroButton);
			       aeroButton = new JRadioButton("Hypersonic Panel Model");
			      aeroButton.setLocation(3, 5 + 25 * 2  );
			      aeroButton.setSize(190, 20);
			      aeroButton.setBackground(backgroundColor);
			      aeroButton.setForeground(labelColor);
			      aeroButton.setFont(small_font);
			      DragModelSet.add(aeroButton);
			      aeroButton.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent arg0) {
							WRITE_AERO();
							//------------------------------------------------------------------------
							int indx = getDragModelSetIndx();
							for(int i=0;i<AeroLeftBarAdditionalComponents.size();i++) {
								AerodynamicDragPanel.remove(AeroLeftBarAdditionalComponents.get(i));
							}
							AerodynamicDragPanel.revalidate();
							AerodynamicDragPanel.repaint();
							if(indx==1) {							
							     INPUT_RB = new JTextField(10);
							     double value = readFromFile(Aero_file, 2);
							     INPUT_RB.setText(""+value);
							     INPUT_RB.setLocation(193, 5 + 25 * 3);;
							     INPUT_RB.setSize(INPUT_width, 20);
							     INPUT_RB.setHorizontalAlignment(JTextField.RIGHT);
							     AeroLeftBarAdditionalComponents.add(INPUT_RB);
							     INPUT_RB.addFocusListener(new FocusListener() {

									@Override
									public void focusGained(FocusEvent arg0) { }

									@Override
									public void focusLost(FocusEvent e) {
										WRITE_AERO();
									}
							   	  
							     });
							     AerodynamicDragPanel.add(INPUT_RB);
						        
							      JLabel LABEL_RB = new JLabel("Heat Shield Body Radius RB [m]");
							      LABEL_RB.setLocation(193, 5 + 25 * 2);
							      LABEL_RB.setSize(300, 20);
							      LABEL_RB.setFont(small_font);
							      LABEL_RB.setBackground(backgroundColor);
							      LABEL_RB.setForeground(labelColor);
							      AeroLeftBarAdditionalComponents.add(LABEL_RB);
						       // RB_INPUT.setBackground(Color.lightGray);
							      AerodynamicDragPanel.add(LABEL_RB);       
							}
							//------------------------------------------------------------------------
					}
			    	  
			      });
			     // aeroButton.setHorizontalAlignment(0);
			      AerodynamicDragPanel.add(aeroButton);
			      dragModelGroup.add(aeroButton);

			      // System.out.println(dragModelGroup.getSelection().);
			      String[] titles = {"Constant Drag Coefficient", "Mach model"};
			      AerodynamicParachutePanel = GuiComponents.getdynamicList(AerodynamicParachutePanel, 
			    		  "Set Parachute drag model" , titles, ParachuteBulletPoints);
			      
			      ParachuteBulletPoints.get(0).addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent arg0) {
							WRITE_AERO();
							for(int i=0;i<AeroParachuteBarAdditionalComponents.size();i++) {
								AerodynamicParachuteOptionPanel.remove(AeroParachuteBarAdditionalComponents.get(i));
							}
							AerodynamicParachuteOptionPanel.revalidate();
							AerodynamicParachuteOptionPanel.repaint();

							      JLabel LABEL = new JLabel("Set constant CD value [-]");
							      LABEL.setLocation(3, 30 + 25 * 0);
							      LABEL.setSize(210, 20);
							      LABEL.setBackground(backgroundColor);
							      LABEL.setForeground(labelColor);
							      LABEL.setFont(small_font);
							      AeroParachuteBarAdditionalComponents.add(LABEL);
							      AerodynamicParachuteOptionPanel.add(LABEL);
							      
							        ConstantParachuteCD_INPUT = new JTextField(""+readFromFile(Aero_file,4));
							        ConstantParachuteCD_INPUT.setLocation(3, 5 + 25 * 2 );
							        ConstantParachuteCD_INPUT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
							        ConstantParachuteCD_INPUT.setBorder(Moon_border);
							        ConstantParachuteCD_INPUT.setSize(100, 20);
							        ConstantParachuteCD_INPUT.setEditable(true);
							        ConstantParachuteCD_INPUT.addFocusListener(new FocusListener() {

										@Override
										public void focusGained(FocusEvent arg0) { }

										@Override
										public void focusLost(FocusEvent e) {
											WRITE_AERO();
										}
								   	  
								     });
							        AeroParachuteBarAdditionalComponents.add(ConstantParachuteCD_INPUT);
							        AerodynamicParachuteOptionPanel.add(ConstantParachuteCD_INPUT); 
							
						}
				    	  
				      });
			      ParachuteBulletPoints.get(1).addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent arg0) {
							WRITE_AERO();
							for(int i=0;i<AeroParachuteBarAdditionalComponents.size();i++) {
								AerodynamicParachuteOptionPanel.remove(AeroParachuteBarAdditionalComponents.get(i));
							}
							AerodynamicParachuteOptionPanel.revalidate();
							AerodynamicParachuteOptionPanel.repaint();

							      JLabel LABEL = new JLabel("Select 1D model [-]");
							      LABEL.setLocation(3, 30 + 25 * 1);
							      LABEL.setSize(300, 20);
							      LABEL.setBackground(backgroundColor);
							      LABEL.setForeground(labelColor);
							      LABEL.setFont(small_font);
							      AeroParachuteBarAdditionalComponents.add(LABEL);
							      AerodynamicParachuteOptionPanel.add(LABEL);
							
						}
				    	  
				      });
			      
			      ParachuteBulletPoints.get(1).setSelected(true);
			      
			      
			      
			      
			      
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
					
					
			      JPanel propulsionInputPanel = new JPanel();
			     // propulsionInputPanel.setLocation(0, uy_p41 + 26 * 38 );
			     // propulsionInputPanel.setSize(SidePanel_Width, 350);
			      propulsionInputPanel.setBackground(backgroundColor);
			      propulsionInputPanel.setForeground(labelColor);
			      propulsionInputPanel.setLayout(new BorderLayout());
			      
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
			      
				TabPane_SCDefinition.addTab("Mass and Inertia" , icon_setup2, massAndInertiaPanel, null);
				TabPane_SCDefinition.addTab("Propulsion" , icon_inertia, propulsionInputPanel, null);
				TabPane_SCDefinition.addTab("Aerodynamic" , icon_aerodynamic, AerodynamicInputPanel, null);
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

      String path3 = "images/mercuryBlueprint.png";
      File file3 = new File(path3);
      try {
      BufferedImage image4 = ImageIO.read(file3);
      JLabel label3 = new JLabel(new ImageIcon(image4));
      label3.setSize(560,430);
      label3.setLocation(435, 5);
      label3.setBorder(Moon_border);
      massAndInertiaPanel.add(label3);
      } catch (Exception e) {
      	System.err.println("Error: SpaceShip Setup/Basic Setup - could not load image");
      }
	  // Space intended for advanced integrator settings 
      
      
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
    INPUT_M0.setForeground(Color.RED);
    INPUT_M0.setHorizontalAlignment(JTextField.RIGHT);
    INPUT_M0.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WRITE_INIT();
			WRITE_PROP();
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
						WriteINERTIA();
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
						WriteINERTIA();
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
						WriteINERTIA();
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
						WriteINERTIA();
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
						WriteINERTIA();
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
						WriteINERTIA();
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
						WriteINERTIA();
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
						WriteINERTIA();
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
						WriteINERTIA();
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
			      
			      
		        //---------------------------------------------------------------------------------------------
		        //                         Propulsion Definition Block
		        //--------------------------------------------------------------------------------------------- 
		      
			        JPanel propulsionSidePanel = new JPanel();
			        propulsionSidePanel.setPreferredSize(new Dimension(400, 800));
			        propulsionSidePanel.setBackground(backgroundColor);
			        propulsionSidePanel.setLayout(null);
			        propulsionInputPanel.add(propulsionSidePanel, BorderLayout.EAST);
			        
			        
			    PropulsionDrawEditor propulsionDrawEditior = new PropulsionDrawEditor();
		        JPanel PropulsionEditor = propulsionDrawEditior.getPropulsionDrawArea();
		        //PropulsionEditor.setSize(600, 500);
		        //PropulsionEditor.setLocation(400, 30);
		        PropulsionEditor.addComponentListener(new ComponentAdapter() 
		        {  
		            public void componentResized(ComponentEvent evt) {
		            	propulsionDrawEditior.getCanvas().resizeBackgroundImage();
		            }
		    });
		        propulsionInputPanel.add(PropulsionEditor, BorderLayout.CENTER);
		        
		        
		        JButton propEditorButton = new JButton("Open Full Size Editor");
		        propEditorButton.setSize(200,30);
		        propEditorButton.setLocation(0, uy_p41 + 25 * 1);
		        propEditorButton.setBackground(backgroundColor);
		        propEditorButton.setForeground(Color.BLACK);
		        propEditorButton.setFont(small_font);
		        propEditorButton.addActionListener(new ActionListener() {
		        	 public void actionPerformed(ActionEvent e)
		        	  {
	                	   Thread thread = new Thread(new Runnable() {
	                  		    public void run() {
	                  		    		PropulsionDrawEditor.setExit(false);
	                  		       	PropulsionDrawEditor.main(null);

	                  		    }
	                  		});
	                  		thread.start();
		        		 
		        	  }
		        });
		        propulsionSidePanel.add(propEditorButton);
		      
		      JLabel LABEL_PrimarySettings = new JLabel("Primary Propulsion System Settings");
		      LABEL_PrimarySettings.setLocation(0, uy_p41 + 25 * 3 );
		      LABEL_PrimarySettings.setSize(400, 20);
		      LABEL_PrimarySettings.setBackground(backgroundColor);
		      LABEL_PrimarySettings.setForeground(labelColor);
		      LABEL_PrimarySettings.setFont(HeadlineFont);
		      LABEL_PrimarySettings.setHorizontalAlignment(JLabel.LEFT);
		      propulsionSidePanel.add(LABEL_PrimarySettings);
		      
		      
		      JLabel LABEL_ME_ISP = new JLabel("Main propulsion system ISP [s]");
		      LABEL_ME_ISP.setLocation(INPUT_width+5, uy_p41 + 25 * 4 );
		      LABEL_ME_ISP.setSize(300, 20);
		      LABEL_ME_ISP.setBackground(backgroundColor);
		      LABEL_ME_ISP.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_ISP);
		      JLabel LABEL_ME_PropMass = new JLabel("Main propulsion system propellant mass [kg]");
		      LABEL_ME_PropMass.setLocation(INPUT_width+5, uy_p41 + 25 * 5);
		      LABEL_ME_PropMass.setSize(300, 20);
		      LABEL_ME_PropMass.setBackground(backgroundColor);
		      LABEL_ME_PropMass.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_PropMass);
		      JLabel LABEL_ME_Thrust_max = new JLabel("Main propulsion system max. Thrust [N]");
		      LABEL_ME_Thrust_max.setLocation(INPUT_width+5, uy_p41 + 25 * 6 );
		      LABEL_ME_Thrust_max.setSize(300, 20);
		      LABEL_ME_Thrust_max.setBackground(backgroundColor);
		      LABEL_ME_Thrust_max.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_Thrust_max);
		      JLabel LABEL_ME_Thrust_min = new JLabel("Main Propulsion system min. Thrust [N]");
		      LABEL_ME_Thrust_min.setLocation(INPUT_width+5, uy_p41 + 25 * 7 );
		      LABEL_ME_Thrust_min.setSize(300, 20);
		      LABEL_ME_Thrust_min.setBackground(backgroundColor);
		      LABEL_ME_Thrust_min.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_Thrust_min);
		      
		      JLabel LABEL_ME_ISP_Model = new JLabel("Include dynamic ISP model in throttled state");
		      LABEL_ME_ISP_Model.setLocation(INPUT_width+5, uy_p41 + 25 * 8 );
		      LABEL_ME_ISP_Model.setSize(300, 20);
		      LABEL_ME_ISP_Model.setBackground(backgroundColor);
		      LABEL_ME_ISP_Model.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_ISP_Model);
		      
		      JLabel LABEL_ME_ISP_min = new JLabel("ISP for maximum throttled state [s]");
		      LABEL_ME_ISP_min.setLocation(INPUT_width+5, uy_p41 + 25 * 9 );
		      LABEL_ME_ISP_min.setSize(300, 20);
		      LABEL_ME_ISP_min.setBackground(backgroundColor);
		      LABEL_ME_ISP_min.setForeground(labelColor);
		      propulsionSidePanel.add(LABEL_ME_ISP_min);
		     
			 
		      INPUT_ISP = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		      INPUT_ISP.setLocation(2, uy_p41 + 25 * 4 );
		      INPUT_ISP.setSize(INPUT_width-20, 20);
		      INPUT_ISP.setBackground(backgroundColor);
		      INPUT_ISP.setForeground(valueColor);
		      INPUT_ISP.setHorizontalAlignment(JTextField.RIGHT);
		      INPUT_ISP.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		    	  
		      });
		      propulsionSidePanel.add(INPUT_ISP);
		     INPUT_PROPMASS = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_PROPMASS.setLocation(2, uy_p41 + 25 * 5);
		     INPUT_PROPMASS.setSize(INPUT_width-20, 20);
		     INPUT_PROPMASS.setBackground(backgroundColor);
		     INPUT_PROPMASS.setForeground(valueColor);
		     INPUT_PROPMASS.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_PROPMASS.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		   	  
		     });
		     propulsionSidePanel.add(INPUT_PROPMASS);        
		     INPUT_THRUSTMAX = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_THRUSTMAX.setLocation(2, uy_p41 + 25 * 6 );
		     INPUT_THRUSTMAX.setSize(INPUT_width-20, 20);
		     INPUT_THRUSTMAX.setBackground(backgroundColor);
		     INPUT_THRUSTMAX.setForeground(valueColor);
		     INPUT_THRUSTMAX.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_THRUSTMAX.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		   	  
		     });
		     propulsionSidePanel.add(INPUT_THRUSTMAX);
		     INPUT_THRUSTMIN = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_THRUSTMIN.setLocation(2, uy_p41 + 25 * 7 );;
		     INPUT_THRUSTMIN.setSize(INPUT_width-20, 20);
		     INPUT_THRUSTMIN.setBackground(backgroundColor);
		     INPUT_THRUSTMIN.setForeground(valueColor);
		     INPUT_THRUSTMIN.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_THRUSTMIN.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		   	  
		     });
		     propulsionSidePanel.add(INPUT_THRUSTMIN);
		     
		     INPUT_ISPMODEL = new JCheckBox(){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_ISPMODEL.setLocation(INPUT_width+5-20, uy_p41 + 25 * 8+2);
		     INPUT_ISPMODEL.setSize(15, 15);
		     INPUT_ISPMODEL.setSelected(true);
		     INPUT_ISPMODEL.setBackground(backgroundColor);
		     INPUT_ISPMODEL.setForeground(valueColor);
		     INPUT_ISPMODEL.addItemListener(new ItemListener() {
		       	 public void itemStateChanged(ItemEvent e) {
		       		WRITE_PROP();
		       	 }
		                  });
		     INPUT_ISPMODEL.setHorizontalAlignment(0);
		     propulsionSidePanel.add(INPUT_ISPMODEL);
		     
		     
		     INPUT_ISPMIN = new JTextField(10){
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

						@Override public void setBorder(Border border) {
					        // No!
					    }
					};
		     INPUT_ISPMIN.setLocation(2, uy_p41 + 25 * 9 );;
		     INPUT_ISPMIN.setSize(INPUT_width-20, 20);
		     INPUT_ISPMIN.setBackground(backgroundColor);
		     INPUT_ISPMIN.setForeground(valueColor);
		     INPUT_ISPMIN.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_ISPMIN.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WRITE_INIT();
					WRITE_PROP();
				}
		   	  
		     });
		     propulsionSidePanel.add(INPUT_ISPMIN);
		     
		        
				JPanel RCSPanel = new JPanel();
				RCSPanel.setLocation(3, 300);
				RCSPanel.setBackground(backgroundColor);
				RCSPanel.setSize(400, 350);
				RCSPanel.setLayout(null);
				propulsionSidePanel.add(RCSPanel);
				
		      JLabel LABEL_RCSSettings = new JLabel("Reaction Control System Settings");
		      LABEL_RCSSettings.setLocation(0, uy_p41 + 10 * 0  );
		      LABEL_RCSSettings.setSize(400, 20);
		      LABEL_RCSSettings.setBackground(backgroundColor);
		      LABEL_RCSSettings.setForeground(labelColor);
		      LABEL_RCSSettings.setFont(HeadlineFont);
		      LABEL_RCSSettings.setHorizontalAlignment(JLabel.LEFT);
		      RCSPanel.add(LABEL_RCSSettings);
		      
		      JLabel LABEL_RcsX = new JLabel("Momentum RCS X axis [Nm]");
		      LABEL_RcsX.setLocation(INPUT_width+1, uy_p41 + 25 * 2 );
		      LABEL_RcsX.setSize(250, 20);
		      LABEL_RcsX.setBackground(backgroundColor);
		      LABEL_RcsX.setForeground(labelColor);
		      RCSPanel.add(LABEL_RcsX);
		      
		      JLabel LABEL_RcsY = new JLabel("Momentum RCS Y axis [Nm]");
		      LABEL_RcsY.setLocation(INPUT_width+1, uy_p41 + 25 * 3 );
		      LABEL_RcsY.setSize(250, 20);
		      LABEL_RcsY.setBackground(backgroundColor);
		      LABEL_RcsY.setForeground(labelColor);
		      RCSPanel.add(LABEL_RcsY);
		      
		      JLabel LABEL_RcsZ = new JLabel("Momentum RCS Z axis [Nm]");
		      LABEL_RcsZ.setLocation(INPUT_width+1, uy_p41 + 25 * 4 );
		      LABEL_RcsZ.setSize(250, 20);
		      LABEL_RcsZ.setBackground(backgroundColor);
		      LABEL_RcsZ.setForeground(labelColor);
		      RCSPanel.add(LABEL_RcsZ);
		      
			     INPUT_RCSX = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_RCSX.setLocation(2, uy_p41 + 25 * 2 );;
			     INPUT_RCSX.setSize(INPUT_width-20, 20);
			     INPUT_RCSX.setBackground(backgroundColor);
			     INPUT_RCSX.setForeground(valueColor);
			     INPUT_RCSX.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_RCSX.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
					//	WRITE_INIT();
						WRITE_PROP();
					}
			   	  
			     });
			     RCSPanel.add(INPUT_RCSX);
			     
			     INPUT_RCSY = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_RCSY.setLocation(2, uy_p41 + 25 * 3 );;
			     INPUT_RCSY.setSize(INPUT_width-20, 20);
			     INPUT_RCSY.setBackground(backgroundColor);
			     INPUT_RCSY.setForeground(valueColor);
			     INPUT_RCSY.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_RCSY.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
					//	WRITE_INIT();
						WRITE_PROP();
					}
			   	  
			     });
			     RCSPanel.add(INPUT_RCSY);
			     
			     INPUT_RCSZ = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_RCSZ.setLocation(2, uy_p41 + 25 * 4 );;
			     INPUT_RCSZ.setSize(INPUT_width-20, 20);
			     INPUT_RCSZ.setBackground(backgroundColor);
			     INPUT_RCSZ.setForeground(valueColor);
			     INPUT_RCSZ.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_RCSZ.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
					//	WRITE_INIT();
						WRITE_PROP();
					}
			   	  
			     });
			     RCSPanel.add(INPUT_RCSZ);
			     
			     
			      JLabel LABEL_RcsXThrust = new JLabel("RCS X Thrust [N]");
			      LABEL_RcsXThrust.setLocation(INPUT_width+1, uy_p41 + 25 * 6 );
			      LABEL_RcsXThrust.setSize(250, 20);
			      LABEL_RcsXThrust.setBackground(backgroundColor);
			      LABEL_RcsXThrust.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsXThrust);
			      
			      JLabel LABEL_RcsYThrust = new JLabel("RCS Y Thrust [N]");
			      LABEL_RcsYThrust.setLocation(INPUT_width+1, uy_p41 + 25 * 7 );
			      LABEL_RcsYThrust.setSize(250, 20);
			      LABEL_RcsYThrust.setBackground(backgroundColor);
			      LABEL_RcsYThrust.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsYThrust);
			      
			      JLabel LABEL_RcsZThrust = new JLabel("RCS Z Thrust [N]");
			      LABEL_RcsZThrust.setLocation(INPUT_width+1, uy_p41 + 25 * 8 );
			      LABEL_RcsZThrust.setSize(250, 20);
			      LABEL_RcsZThrust.setBackground(backgroundColor);
			      LABEL_RcsZThrust.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsZThrust);
			      
			      JLabel LABEL_RcsTank = new JLabel("Secondary Propulsion Tank [kg]");
			      LABEL_RcsTank.setLocation(INPUT_width+1, uy_p41 + 25 * 10 );
			      LABEL_RcsTank.setSize(250, 20);
			      LABEL_RcsTank.setBackground(backgroundColor);
			      LABEL_RcsTank.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsTank);
			      
			      JLabel LABEL_RcsXISP= new JLabel("RCS ISP X axis Thruster [s]");
			      LABEL_RcsXISP.setLocation(INPUT_width+1, uy_p41 + 25 * 11 );
			      LABEL_RcsXISP.setSize(250, 20);
			      LABEL_RcsXISP.setBackground(backgroundColor);
			      LABEL_RcsXISP.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsXISP);
			      
			      JLabel LABEL_RcsYISP= new JLabel("RCS ISP Y axis Thruster [s]");
			      LABEL_RcsYISP.setLocation(INPUT_width+1, uy_p41 + 25 * 12 );
			      LABEL_RcsYISP.setSize(250, 20);
			      LABEL_RcsYISP.setBackground(backgroundColor);
			      LABEL_RcsYISP.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsYISP);
			      
			      JLabel LABEL_RcsZISP= new JLabel("RCS ISP Z axis Thruster [s]");
			      LABEL_RcsZISP.setLocation(INPUT_width+1, uy_p41 + 25 * 13 );
			      LABEL_RcsZISP.setSize(250, 20);
			      LABEL_RcsZISP.setBackground(backgroundColor);
			      LABEL_RcsZISP.setForeground(labelColor);
			      RCSPanel.add(LABEL_RcsZISP);
			      
				     INPUT_RCSXTHRUST = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSXTHRUST.setLocation(2, uy_p41 + 25 * 6 );;
				     INPUT_RCSXTHRUST.setSize(INPUT_width-20, 20);
				     INPUT_RCSXTHRUST.setBackground(backgroundColor);
				     INPUT_RCSXTHRUST.setForeground(valueColor);
				     INPUT_RCSXTHRUST.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSXTHRUST.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSXTHRUST);
				     
				     INPUT_RCSYTHRUST = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSYTHRUST.setLocation(2, uy_p41 + 25 * 7 );;
				     INPUT_RCSYTHRUST.setSize(INPUT_width-20, 20);
				     INPUT_RCSYTHRUST.setBackground(backgroundColor);
				     INPUT_RCSYTHRUST.setForeground(valueColor);
				     INPUT_RCSYTHRUST.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSYTHRUST.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSYTHRUST);
				     
				     INPUT_RCSZTHRUST = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSZTHRUST.setLocation(2, uy_p41 + 25 * 8 );;
				     INPUT_RCSZTHRUST.setSize(INPUT_width-20, 20);
				     INPUT_RCSZTHRUST.setBackground(backgroundColor);
				     INPUT_RCSZTHRUST.setForeground(valueColor);
				     INPUT_RCSZTHRUST.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSZTHRUST.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSZTHRUST);
				     
				     INPUT_RCSTANK = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSTANK.setLocation(2, uy_p41 + 25 * 10 );;
				     INPUT_RCSTANK.setSize(INPUT_width-20, 20);
				     INPUT_RCSTANK.setBackground(backgroundColor);
				     INPUT_RCSTANK.setForeground(valueColor);
				     INPUT_RCSTANK.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSTANK.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSTANK);
				     
				     INPUT_RCSXISP = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSXISP.setLocation(2, uy_p41 + 25 * 11 );
				     INPUT_RCSXISP.setSize(INPUT_width-20, 20);
				     INPUT_RCSXISP.setBackground(backgroundColor);
				     INPUT_RCSXISP.setForeground(valueColor);
				     INPUT_RCSXISP.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSXISP.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSXISP);
				     
				     INPUT_RCSYISP = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSYISP.setLocation(2, uy_p41 + 25 * 12 );
				     INPUT_RCSYISP.setSize(INPUT_width-20, 20);
				     INPUT_RCSYISP.setBackground(backgroundColor);
				     INPUT_RCSYISP.setForeground(valueColor);
				     INPUT_RCSYISP.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSYISP.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSYISP);
				     
				     INPUT_RCSZISP = new JTextField(10){
						    /**
							 * 
							 */
							private static final long serialVersionUID = 1L;

								@Override public void setBorder(Border border) {
							        // No!
							    }
							};
				     INPUT_RCSZISP.setLocation(2, uy_p41 + 25 * 13 );;
				     INPUT_RCSZISP.setSize(INPUT_width-20, 20);
				     INPUT_RCSZISP.setBackground(backgroundColor);
				     INPUT_RCSZISP.setForeground(valueColor);
				     INPUT_RCSZISP.setHorizontalAlignment(JTextField.RIGHT);
				     INPUT_RCSZISP.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
						//	WRITE_INIT();
							WRITE_PROP();
						}
				   	  
				     });
				     RCSPanel.add(INPUT_RCSZISP);
		     //----------------------------------------------------------------------------------
		     //						Aerodynamic Input
		     //----------------------------------------------------------------------------------
		     
		      JLabel LABEL_SurfaceArea = new JLabel("S/C Surface Area [m\u00b2]");
		      LABEL_SurfaceArea.setLocation(INPUT_width+35, uy_p41 + 25 * 1 );
		      LABEL_SurfaceArea.setSize(300, 20);
		      LABEL_SurfaceArea.setBackground(backgroundColor);
		      LABEL_SurfaceArea.setForeground(labelColor);
		      AerodynamicInputPanel.add(LABEL_SurfaceArea);
		      
		      JLabel LABEL_BallisticCoefficient = new JLabel("Ballistic Coefficient [kg/m\u00b2]");
		      LABEL_BallisticCoefficient.setLocation(INPUT_width+35, uy_p41 + 25 * 2 );
		      LABEL_BallisticCoefficient.setSize(300, 20);
		      LABEL_BallisticCoefficient.setBackground(backgroundColor);
		      LABEL_BallisticCoefficient.setForeground(labelColor);
		      AerodynamicInputPanel.add(LABEL_BallisticCoefficient);
		      
			     double value = Math.sqrt(4/PI*readFromFile(SC_file, 2));
			     //System.out.println(readFromFile(Aero_file, 2)+" | "+value); 
			     INPUT_ParachuteDiameter = new JTextField(""+decAngularRate.format(value)){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_ParachuteDiameter.setLocation(2, uy_p41 + 25 * 4);
			     INPUT_ParachuteDiameter.setSize(INPUT_width, 20);
			     INPUT_ParachuteDiameter.setBackground(backgroundColor);
			     INPUT_ParachuteDiameter.setForeground(valueColor);
			     INPUT_ParachuteDiameter.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_ParachuteDiameter.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WRITE_SC();
					}
			   	  
			     });
			     AerodynamicInputPanel.add(INPUT_ParachuteDiameter);
			     
			     INPUT_SURFACEAREA = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};
			     INPUT_BALLISTICCOEFFICIENT = new JTextField(10){
					    /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

							@Override public void setBorder(Border border) {
						        // No!
						    }
						};

			     INPUT_SURFACEAREA.setLocation(2, uy_p41 + 25 * 1 );;
			     INPUT_SURFACEAREA.setSize(INPUT_width, 20);
			     INPUT_SURFACEAREA.setBackground(backgroundColor);
			     INPUT_SURFACEAREA.setForeground(valueColor);
			     INPUT_SURFACEAREA.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_SURFACEAREA.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WRITE_SC();
						EvaluateSurfaceAreaSetup() ;
					}
			   	  
			     });
			     AerodynamicInputPanel.add(INPUT_SURFACEAREA);
			     
			     INPUT_BALLISTICCOEFFICIENT.setLocation(2, uy_p41 + 25 * 2 );
			     INPUT_BALLISTICCOEFFICIENT.setSize(INPUT_width, 20);
			     INPUT_BALLISTICCOEFFICIENT.setBackground(backgroundColor);
			     INPUT_BALLISTICCOEFFICIENT.setForeground(valueColor);
			     INPUT_BALLISTICCOEFFICIENT.setHorizontalAlignment(JTextField.RIGHT);
			     INPUT_BALLISTICCOEFFICIENT.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) { }

					@Override
					public void focusLost(FocusEvent e) {
						WRITE_SC();
						EvaluateSurfaceAreaSetup() ;
					}
			   	  
			     });
			     AerodynamicInputPanel.add(INPUT_BALLISTICCOEFFICIENT);
			     
			      RB_SurfaceArea =new JRadioButton("");    
			      RB_BallisticCoefficient =new JRadioButton("");    
			     //r1.setBounds(75,50,100,30);    
			      RB_SurfaceArea.setLocation(INPUT_width+5, uy_p41 + 25 * 1 );
			      RB_SurfaceArea.setSize(22,22);
			      RB_SurfaceArea.setForeground(labelColor);
			      RB_SurfaceArea.setBackground(backgroundColor);
			     //r2.setBounds(75,100,100,30); 
			      RB_BallisticCoefficient.setLocation(INPUT_width+5, uy_p41 + 25 * 2 );
			      RB_BallisticCoefficient.setSize(22,22);
			      RB_BallisticCoefficient.setBackground(backgroundColor);
			      

				     ButtonGroup bg=new ButtonGroup();    
				     bg.add(RB_SurfaceArea);bg.add(RB_BallisticCoefficient); 
				     AerodynamicInputPanel.add(RB_SurfaceArea);
				     AerodynamicInputPanel.add(RB_BallisticCoefficient);
				     RB_SurfaceArea.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							if(RB_SurfaceArea.isSelected()) {
								double BC = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText());
								double mass = Double.parseDouble(INPUT_M0.getText());
					    		INPUT_SURFACEAREA.setText(""+String.format("%.2f",mass/BC));
					    		INPUT_BALLISTICCOEFFICIENT.setText("");
					    		
					    		INPUT_SURFACEAREA.setEditable(true);
					    		INPUT_BALLISTICCOEFFICIENT.setEditable(false);	
					    		
							} else if (RB_BallisticCoefficient.isSelected()) {
								double surfacearea = Double.parseDouble(INPUT_SURFACEAREA.getText());
								double mass = Double.parseDouble(INPUT_M0.getText());
					    		INPUT_SURFACEAREA.setText("");
							INPUT_BALLISTICCOEFFICIENT.setText(""+String.format("%.2f", mass/surfacearea));
							
					    		INPUT_SURFACEAREA.setEditable(false);
					    		INPUT_BALLISTICCOEFFICIENT.setEditable(true);	
							}
							
						}
				    	 
				     });
				     RB_BallisticCoefficient.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							if(RB_SurfaceArea.isSelected()) {
								double BC = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText());
								double mass = Double.parseDouble(INPUT_M0.getText());
					    		INPUT_SURFACEAREA.setText(""+String.format("%.2f",mass/BC));
					    		INPUT_BALLISTICCOEFFICIENT.setText("");
					    		
					    		INPUT_SURFACEAREA.setEditable(true);
					    		INPUT_BALLISTICCOEFFICIENT.setEditable(false);	
					    		
							} else if (RB_BallisticCoefficient.isSelected()) {
								double surfacearea = Double.parseDouble(INPUT_SURFACEAREA.getText());
								double mass = Double.parseDouble(INPUT_M0.getText());
					    		INPUT_SURFACEAREA.setText("");
							INPUT_BALLISTICCOEFFICIENT.setText(""+String.format("%.2f", mass/surfacearea));
							
					    		INPUT_SURFACEAREA.setEditable(false);
					    		INPUT_BALLISTICCOEFFICIENT.setEditable(true);	
					    		
							}
						}
				    	 
				     });
				     				     
				      JLabel LABEL_Parachute= new JLabel("Parachute Diameter [m]");
				      LABEL_Parachute.setLocation(INPUT_width+35, uy_p41 + 25 * 4 );
				      LABEL_Parachute.setSize(250, 20);
				      LABEL_Parachute.setBackground(backgroundColor);
				      LABEL_Parachute.setForeground(labelColor);
				      AerodynamicInputPanel.add(LABEL_Parachute);
				      
 
		      
		        String path = "images/milleniumSchlieren2.png";
		        File file = new File(path);
		        try {
		        BufferedImage image2 = ImageIO.read(file);
		        JLabel label = new JLabel(new ImageIcon(image2));
		        label.setSize(300,290);
		        label.setLocation(5, uy_p41 + 25 * 6);
		        label.setBorder(Moon_border);
		        AerodynamicInputPanel.add(label);
		        } catch (Exception e) {
		        	System.err.println("Error: SpaceShip Setup/Aerodynamik - could not load image");
		        }

		        
		        //--------------------------------------------------------------------------------------------------------	        
		        JPanel SequenceControlPanel = new JPanel();
		        SequenceControlPanel.setLayout(null);
		        SequenceControlPanel.setPreferredSize(new Dimension(400, 60));
		        SequenceControlPanel.setBackground(backgroundColor);
		        SequenceControlPanel.setForeground(labelColor);
		        guidanceNavigationAndControlPanel.add(SequenceControlPanel, BorderLayout.PAGE_START);


		        JButton BUTTON_AddController = new JButton("Add Controller");
		        BUTTON_AddController.setLocation(655, 5);
		        BUTTON_AddController.setSize(145,25);
		        BUTTON_AddController.addActionListener(new ActionListener() { 
		        	  public void actionPerformed(ActionEvent e) { 
		        		 // AddController();
		        	  } } );
		        SequenceControlPanel.add(BUTTON_AddController);
		        JButton BUTTON_DeleteController = new JButton("Delete Controller");
		        BUTTON_DeleteController.setLocation(655, 32);
		        BUTTON_DeleteController.setSize(145,25);
		        BUTTON_DeleteController.addActionListener(new ActionListener() { 
		        	  public void actionPerformed(ActionEvent e) { 
		        		 // DeleteController();
		        	  } } );
		        SequenceControlPanel.add(BUTTON_DeleteController);
		        
		        JButton BUTTON_AddError = new JButton("Add Error");
		        BUTTON_AddError.setLocation(805, 5);
		        BUTTON_AddError.setSize(145,25);
		        BUTTON_AddError.addActionListener(new ActionListener() { 
		        	  public void actionPerformed(ActionEvent e) { 
		        		 // AddError();
		        	  } } );
		        SequenceControlPanel.add(BUTTON_AddError);
		        JButton BUTTON_DeleteError = new JButton("Delete Error");
		        BUTTON_DeleteError.setLocation(805, 32);
		        BUTTON_DeleteError.setSize(145,25);
		        BUTTON_DeleteError.addActionListener(new ActionListener() { 
		        	  public void actionPerformed(ActionEvent e) { 
		        		 // DeleteError();
		        	  } } );
		        SequenceControlPanel.add(BUTTON_DeleteError);
		        //-----------------------------------------------------------------------------------------------------------------------------
		        //                  Sequence table 
		        //-----------------------------------------------------------------------------------------------------------------------------
		        JPanel Pane_Sequence = new JPanel();
		        Pane_Sequence.setLocation(0, 0);
		        Pane_Sequence.setPreferredSize(new Dimension(900, 250));
		        Pane_Sequence.setLayout(new BorderLayout());
		        Pane_Sequence.setBackground(backgroundColor);
		        Pane_Sequence.setForeground(labelColor);
		        guidanceNavigationAndControlPanel.add(Pane_Sequence, BorderLayout.PAGE_END);

		        JSplitPane SplitPane_Page2_Charts_HorizontalSplit = new JSplitPane();
		        SplitPane_Page2_Charts_HorizontalSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
		        SplitPane_Page2_Charts_HorizontalSplit.setDividerLocation(0.4);
		        SplitPane_Page2_Charts_HorizontalSplit.setDividerSize(3);
		        SplitPane_Page2_Charts_HorizontalSplit.setUI(new BasicSplitPaneUI() {
		               @SuppressWarnings("serial")
		    			public BasicSplitPaneDivider createDefaultDivider() {
		               return new BasicSplitPaneDivider(this) {
		                   @SuppressWarnings("unused")
		    				public void setBorder( Border b) {
		                   }

		                   @Override
		                       public void paint(Graphics g) {
		                       g.setColor(Color.gray);
		                       g.fillRect(0, 0, getSize().width, getSize().height);
		                           super.paint(g);
		                       }
		               };
		               }
		           });

		        SplitPane_Page2_Charts_HorizontalSplit.addComponentListener(new ComponentListener(){

		    			@Override
		    			public void componentHidden(ComponentEvent arg0) {
		    				// TODO Auto-generated method stub
		    				
		    			}

		    			@Override
		    			public void componentMoved(ComponentEvent arg0) {
		    				// TODO Auto-generated method stub
		    				//System.out.println("Line moved");	
		    				
		    			}

		    			@Override
		    			public void componentResized(ComponentEvent arg0) {
		    				// TODO Auto-generated method stub

		    			}

		    			@Override
		    			public void componentShown(ComponentEvent arg0) {
		    				// TODO Auto-generated method stub
		    				
		    			}
		       
		       	});
		        SplitPane_Page2_Charts_HorizontalSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, 
		    		    new PropertyChangeListener() {
		    		        @Override
		    		        public void propertyChange(PropertyChangeEvent pce) {

		    		        }
		    		});
		        SplitPane_Page2_Charts_HorizontalSplit.setDividerLocation(700);
		        Pane_Sequence.add(SplitPane_Page2_Charts_HorizontalSplit, BorderLayout.CENTER);
		        
		        
		        
		        
		        

		    		    
		    		    //---------------------------------------------------------------------------------------------
		    		    TABLE_ERROR = new JTable(){
		    			   	 
		    		    	/**
		    				 * 
		    				 */
		    				private static final long serialVersionUID = 1L;

		    				@Override
		    		    	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		    		            Component comp = super.prepareRenderer(renderer, row, col);
		    		           // String val_TLFC = (String) getModel().getValueAt(row, 1);


		    		           // comp.setFont(table_font);
		    		            
		    		            return comp;
		    		        }
		    		    };
		    		   // TABLE_SEQUENCE.setFont(table_font);
		    		    
		    			Action action5 = new AbstractAction()
		    		    {
		    		        /**
		    				 * 
		    				 */
		    				private static final long serialVersionUID = 1L;

		    				public void actionPerformed(ActionEvent e)
		    		       {
		    					WriteErrorINP();	
		    					//Update_ErrorIndicator();
		    		       }
		    		    };
		    		    @SuppressWarnings("unused")
		    			TableCellListener tcl5 = new TableCellListener(TABLE_ERROR, action5);
		    		    MODEL_ERROR = new DefaultTableModel(){

		    				private static final long serialVersionUID = 1L;

		    				@Override
		    		        public boolean isCellEditable(int row, int column) {
		    		           //all cells false
		    					if (column == 0 ){
		    						return false;
		    					} else {
		    						return true; 
		    					}
		    		        }
		    		    }; 
		    		    MODEL_ERROR.setColumnIdentifiers(COLUMS_ERROR);
		    		    TABLE_ERROR.setModel(MODEL_ERROR);
		    		    TABLE_ERROR.setBackground(backgroundColor);
		    		    TABLE_ERROR.setBackground(backgroundColor);
		    		    TABLE_ERROR.setForeground(labelColor);
		    		    TABLE_ERROR.getTableHeader().setReorderingAllowed(false);
		    		    TABLE_ERROR.setRowHeight(45);


		    			    TableColumn ErrorID_colum   			 = TABLE_ERROR.getColumnModel().getColumn(0);
		    			    TableColumn ErrorType_column 	    	 = TABLE_ERROR.getColumnModel().getColumn(1);
		    			    TableColumn ErrorTrigger_column  		 = TABLE_ERROR.getColumnModel().getColumn(2);
		    			    TableColumn ErrorValue_column  			 = TABLE_ERROR.getColumnModel().getColumn(3);
		    			    
		    			    TABLE_ERROR.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    			    ErrorID_colum.setPreferredWidth(50);
		    			    ErrorType_column.setPreferredWidth(100);
		    			    ErrorTrigger_column.setPreferredWidth(100);
		    			    ErrorValue_column.setPreferredWidth(100);


		    			    
		    			    ((JTable) TABLE_ERROR).setFillsViewportHeight(true);
		    		    
		    			    TABLE_ERROR.getTableHeader().setBackground(backgroundColor);
		    			    TABLE_ERROR.getTableHeader().setForeground(labelColor);
		    			    
		    		
		    			 
		    			    
		    			    JScrollPane TABLE_ERROR_ScrollPane = new JScrollPane(TABLE_ERROR,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		    			    TABLE_ERROR_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
		    			    TABLE_ERROR_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
		    			    TABLE_ERROR_ScrollPane.setBackground(backgroundColor);
		    			    SplitPane_Page2_Charts_HorizontalSplit.add(TABLE_ERROR_ScrollPane, JSplitPane.RIGHT);

        //-----------------------------------------------------------------------------------------
        // Page 4.3
        //-----------------------------------------------------------------------------------------
		JPanel SouthPanel = new JPanel();
		SouthPanel.setLayout(null);
		//mainPanelh1.setLocation(0, 0);
		SouthPanel.setBackground(backgroundColor);
		SouthPanel.setForeground(labelColor);
		SouthPanel.setPreferredSize(new Dimension(1200, 120));
		PageX04_Map.add(SouthPanel, BorderLayout.SOUTH);
	    
        int uy2 = 10; 

       
        JLabel LABEL_PageMapLONG = new JLabel("Longitude [deg]");
        LABEL_PageMapLONG.setLocation(425, uy2 + 0 );
        LABEL_PageMapLONG.setSize(250, 20);
        LABEL_PageMapLONG.setBackground(backgroundColor);
        LABEL_PageMapLONG.setForeground(labelColor);
        SouthPanel.add(LABEL_PageMapLONG);
        JLabel LABEL_PageMapLAT = new JLabel("Latitude [deg]");
        LABEL_PageMapLAT.setLocation(825, uy2 + 0 );
        LABEL_PageMapLAT.setSize(250, 20);
        LABEL_PageMapLAT.setBackground(backgroundColor);
        LABEL_PageMapLAT.setForeground(labelColor);
        SouthPanel.add(LABEL_PageMapLAT);	
        
         INDICATOR_PageMap_LONG = new JLabel();
        INDICATOR_PageMap_LONG.setLocation(425, uy2 + 30 );
        INDICATOR_PageMap_LONG.setText("");
        INDICATOR_PageMap_LONG.setSize(80, 20);
        SouthPanel.add(INDICATOR_PageMap_LONG);
         INDICATOR_PageMap_LAT = new JLabel();
        INDICATOR_PageMap_LAT.setLocation(825, uy2 + 30 );
        INDICATOR_PageMap_LAT.setText("");
        INDICATOR_PageMap_LAT.setSize(80, 20);
        SouthPanel.add(INDICATOR_PageMap_LAT);

       PolarMapContainer = new JPanel(new GridBagLayout());
       PolarMapContainer.setLocation(0, 0);
       PolarMapContainer.setPreferredSize(new Dimension(extx_main, exty_main));
       PolarMapContainer.setBackground(backgroundColor);
       PageX04_PolarMap.add(PolarMapContainer, BorderLayout.CENTER);
 	  //-----------------------------------------------------------------------------------------
 	  // 										Page: Raw Data
 	  //-----------------------------------------------------------------------------------------
       
	    TABLE_RAWData = new JTable();	    
	    MODEL_RAWData = new DefaultTableModel(){

			private static final long serialVersionUID = 1L;

			@Override
	        public boolean isCellEditable(int row, int column) {
	           //all cells false
					return false;
	        }
	    }; 
	    MODEL_RAWData.setColumnIdentifiers(Axis_Option_NR);
	    TABLE_RAWData.setModel(MODEL_RAWData);
	    TABLE_RAWData.setBackground(backgroundColor);
	    TABLE_RAWData.setBackground(backgroundColor);
	    TABLE_RAWData.setForeground(labelColor);
	    TABLE_RAWData.getTableHeader().setReorderingAllowed(false);
	    TABLE_RAWData.setRowHeight(18);
		TABLE_RAWData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		((JTable) TABLE_RAWData).setFillsViewportHeight(true);
		TABLE_RAWData.getTableHeader().setBackground(backgroundColor);
		TABLE_RAWData.getTableHeader().setForeground(labelColor);

		    JScrollPane TABLE_RAWData_ScrollPane = new JScrollPane(TABLE_RAWData,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		    TABLE_RAWData_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
		    TABLE_RAWData_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
		    TABLE_RAWData_ScrollPane.setBackground(backgroundColor);
		    PageX04_RawDATA.add(TABLE_RAWData_ScrollPane);
       
       
       
	//-------------------------------------------------------------------------------------------------------------------------------
        // Create Charts:
       //CreateChart_DashboardOverviewChart_Altitude_Velocity();
       //CreateChart_DashboardOverviewChart_Time_FPA();

     	//CreateChart_DashBoardFlexibleChart();
     	//createChart_3DRotation();
     	CreateChart_MercatorMap();
     	CreateChart_PolarMap();
     	
     	//CreateChart_GroundClearance();
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
        Page04_subtabPane.addTab("Raw Data", icon_data, PageX04_RawDATA, null);
        Page04_subtabPane.addTab("Map" , icon_map, PageX04_Map, null);
        Page04_subtabPane.addTab("Polar Map" , icon_map, PageX04_PolarMap, null);
       // Page04_subtabPane.addTab("GroundClearance" , null, PageX04_GroundClearance, null);
        //Page04_subtabPane.addTab("Results" , null, PageX04_3, null);
        Page04_subtabPane.setFont(small_font);
        Page04_subtabPane.setBackground(Color.black);
        Page04_subtabPane.setForeground(Color.black);
        MainGUI.add(Page04_subtabPane);
        Page04_subtabPane.setSelectedIndex(0);
    		//CreateChart_A01();
        // The following prevents long load up times when opening the GUI 
    		try {     long filesize = 	new File(RES_File).length()/1000000;
			    	    if(filesize<10) {
							SET_MAP(indx_target);
			    	    }
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1);
				System.out.println("ERROR: Loading map failed.");
			}
        //------------------------------------------------------------------------
        	// Check filesize 
    	    long filesize = 	new File(RES_File).length()/1000000;
    	    if(filesize<10) {
    		UPDATE_Page01(true);
    	    } else {
    	    	UPDATE_Page01(false);
    	    	System.out.println("Full data import supressed. Filesize prohibits fast startup.");
    	    }
    	    READ_INPUT();
    	    DashboardPlotArea.setAnalysisFile(analysisFile);
    	    DashboardPlotArea.setTargetIndx(indx_target);
    	   // dashboardPlotArea.updateDashboardPlotArea(dashboardPlotArea.getContentPanelList());
    	      // createTargetView3D();
    	       CenterPanelRight.createTargetWindow();
   	       
    	       
    		READ_INERTIA() ;
    		READ_InitialAttitude();
    		//Update_ErrorIndicator();
    		  SidePanelLeft.Update_IntegratorSettings();
    	     // Update_DashboardFlexibleChart2();
    	      try {
    	      READ_sequenceFile();
    	      } catch(Exception e) {
    	    	  System.out.println("ERROR: Reading sequenceFile.inp failed.");
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
    
	public static void UPDATE_Page01(boolean fullImport){
		  try {
			READ_INPUT();
			if(fullImport) {
			READ_RAWDATA();
			SET_MAP(indx_target);
			}
		} catch (IOException | URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	   // 	Update_DashboardFlexibleChart();
	   // 	Update_DashboardFlexibleChart2();
		  if(fullImport) {

	    	ResultSet_MercatorMap.removeAllSeries();
	    	try {
	    	ResultSet_MercatorMap = AddDataset_Mercator_MAP();
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {}

      	    		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
      	    		System.out.println("Updated "+timeStamp);
		  }
	  } 

    
    

    
    public static void READ_sequenceFile() throws IOException{	
		BufferedReader br = new BufferedReader(new FileReader(sequenceFile));
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
    
    public static void READ_INERTIA() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(INERTIA_File));
	       String strLine;
	       int j=0;
	       try {
	       while ((strLine = br.readLine()) != null )   {
		       	String[] tokens = strLine.split(" ");
		       	if(j==0) {
		       		INPUT_IXX.setText(tokens[0]);
		       		INPUT_IXY.setText(tokens[1]);
		       		INPUT_IXZ.setText(tokens[2]);
		       	} else if (j==1) {
		       		INPUT_IYX.setText(tokens[0]);
		       		INPUT_IYY.setText(tokens[1]);
		       		INPUT_IYZ.setText(tokens[2]);
		       	} else if (j==2) {
		       		INPUT_IZX.setText(tokens[0]);
		       		INPUT_IZY.setText(tokens[1]);
		       		INPUT_IZZ.setText(tokens[2]);
		       	}
		       	
		       	j++;
	       }
	       br.close();
	       } catch(NullPointerException eNPE) { System.out.println(eNPE);}
    }
    public static void READ_INPUT() throws IOException{
    	double InitialState = 0;
       	FileInputStream fstream = null; 
try {
              fstream = new FileInputStream(Init_File);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading init.inp failed.");} 
        DataInputStream in = new DataInputStream(fstream);
        @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int k = 0;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String fullLine = strLine;
        //	System.out.println(fullLine);
        	String[] tokens = strLine.split(" ");
        if (k==0){
        	InitialState = Double.parseDouble(tokens[0]);
         	DashboardLeftPanel.INDICATOR_LONG.setText(decf.format(InitialState));
        		SidePanelLeft.INPUT_LONG_Rs.setText(df_X4.format(InitialState));
        	} else if (k==1){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_LAT.setText(decf.format( InitialState));
        		SidePanelLeft.INPUT_LAT_Rs.setText(df_X4.format( InitialState));
        	} else if (k==2){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_ALT.setText(decf.format( InitialState));
        		SidePanelLeft.INPUT_ALT_Rs.setText(decf.format( InitialState));
        	} else if (k==3){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_VEL.setText(decf.format(InitialState));
        		SidePanelLeft.INPUT_VEL_Rs.setText(decf.format(InitialState));
        	} else if (k==4){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_FPA.setText(decf.format(InitialState));
        		SidePanelLeft.INPUT_FPA_Rs.setText(df_X4.format(InitialState));
        	} else if (k==5){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_AZI.setText(decf.format(InitialState));
        		SidePanelLeft.INPUT_AZI_Rs.setText(df_X4.format(InitialState));
        	} else if (k==6){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_M0.setText(decf.format(InitialState));
        		INPUT_M0.setText(decf.format(InitialState));
        	} else if (k==7){
            	InitialState = Double.parseDouble(tokens[0]);
            	DashboardLeftPanel.INDICATOR_INTEGTIME.setText(decf.format(InitialState));
        		CenterPanelRight.setGlobalTime(InitialState);
        		 //MODEL_EventHandler.setValueAt(decf.format(InitialState), 0, 1);
        	} else if (k==8){
            	InitialState = Double.parseDouble(tokens[0]);
        		int Integ_indx = (int) InitialState;
        		SidePanelLeft.Integrator_chooser.setSelectedIndex(Integ_indx);
        } else if (k==9){
        	InitialState = Double.parseDouble(tokens[0]);
        		int Target_indx = (int) InitialState;
        		indx_target = (int) InitialState; 
        		RM = DATA_MAIN[indx_target][0];
        		DashboardLeftPanel.INDICATOR_TARGET.setText(Target_Options[indx_target]);
        		CenterPanelRight.setTargetIndx(Target_indx);
                if(indx_target==0) {
                	DashboardLeftPanel.INDICATOR_TARGET.setBorder(Earth_border);
                } else if(indx_target==1){
                	DashboardLeftPanel.INDICATOR_TARGET.setBorder(Moon_border);
                } else if(indx_target==2){
                	DashboardLeftPanel.INDICATOR_TARGET.setBorder(Mars_border);
                } else if(indx_target==3){
                	DashboardLeftPanel.INDICATOR_TARGET.setBorder(Venus_border);
                }
            } else if (k==10){
            	//InitialState = Double.parseDouble(tokens[0]);
	            //	INPUT_WRITETIME.setText(decf.format(InitialState)); // write dt
	        	InitialState = Double.parseDouble(tokens[0]);
		    	CenterPanelRight.setGlobalFrequency(InitialState);
            } else if (k==11){
            	InitialState = Double.parseDouble(tokens[0]);
            	SidePanelLeft.INPUT_REFELEV.setText(decf.format(InitialState));       // Reference Elevation
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
		    	SidePanelLeft.INPUT_AngularRate_X.setText(decAngularRate.format(InitialState));
		    } else if (k==16) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	SidePanelLeft.INPUT_AngularRate_Y.setText(decAngularRate.format(InitialState));
		    } else if (k==17) {
	        	InitialState = Double.parseDouble(tokens[0]);
		    	SidePanelLeft.INPUT_AngularRate_Z.setText(decAngularRate.format(InitialState));
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

    //------------------------------------------------------------------
    // Read from PROP
    try {
        fstream = new FileInputStream(Prop_File);
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
      fstream = new FileInputStream(Aero_file);
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
		integ_file = INTEG_File_01; 
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==1) {
		integ_file = INTEG_File_02; 
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==2) {
		integ_file = INTEG_File_03; ;
	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==3) {
		integ_file = INTEG_File_04; 
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
    		SidePanelLeft.	INPUT_IntegratorSetting_01.setText(""+(InitialState)); 
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
      fstream = new FileInputStream(SC_file);
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

	} else if (k==4){
	
	} else if (k==5){

	} else if (k==6){

	} else if (k==7){

	}
	k++;
}
EvaluateSurfaceAreaSetup() ;
in3.close();
br5.close();
fstream.close();
} catch (NullPointerException eNPE) { System.out.println(eNPE);} 
    }
    
    public static void READ_RAWDATA() {
    	resultSet.clear();
    try {
		analysisFile = 	readResultFileList(RES_File);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    	// Delete all exisiting rows:
    	for(int j=MODEL_RAWData.getRowCount()-1;j>=0;j--) {MODEL_RAWData.removeRow(j);}
    	// Read all data from file: 
	    FileInputStream fstream = null;
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	              DataInputStream in = new DataInputStream(fstream);
	              BufferedReader br = new BufferedReader(new InputStreamReader(in));
	              String strLine;
	              try {
							while ((strLine = br.readLine()) != null )   {
								Object[] tokens = strLine.split(" ");
							    MODEL_RAWData.addRow(tokens);
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
    }
    
    public static List<RealTimeResultSet> READ_ResultSet(String RES_File) {
    	List<RealTimeResultSet> resultSet = new ArrayList<RealTimeResultSet>();
    	resultSet.clear();
    	// Read all data from file: 
	    FileInputStream fstream = null;
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
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
    
    public static void READ_INTEG() {
    	  //--------------------------------------------------------------------------------------------------------
    	  // Integrator settings 
    	  //--------------------------------------------------------------------------------------------------------
    		String integ_file = null;
    		 FileInputStream  fstream = null; 
    		if(	SidePanelLeft.Integrator_chooser.getSelectedIndex()==0) {
    			integ_file = INTEG_File_01; 
    		} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==1) {
    			integ_file = INTEG_File_02; 
    		} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==2) {
    			integ_file = INTEG_File_03; ;
    		} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==3) {
    			integ_file = INTEG_File_04; 
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
    
    public static void READ_InitialAttitude() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(InitialAttitude_File));
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
    
    public static List<AnimationSet>  READ_AnimationData() {
     int indx_time=0;
     int indx_vel =6;
    	 int indx_fpa =7;
    	 int indx_azi =8;
    	 float init_alt=0;
   	 List<AnimationSet> animationSets= new ArrayList<AnimationSet>();
	FileInputStream fstream = null;
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
   	// Scan for specifin information 
	     fstream = null;
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	               in = new DataInputStream(fstream);
	               br = new BufferedReader(new InputStreamReader(in));
	              try {
	              int indx=0;
							while ((strLine = br.readLine()) != null )   {
								Object[] tokens = strLine.split(" ");

							    if(indx==0){init_alt=Float.parseFloat((String) tokens[4]);}
							    indx++;
							    }
			       fstream.close();
			       in.close();
			       br.close();

	              } catch (NullPointerException | IOException eNPE) { 
	            	  System.out.println("Read raw data, Nullpointerexception");
					}catch(IllegalArgumentException eIAE) {
					  System.out.println("Read raw data, illegal argument error");
					}
	//----------------------------------------------------------------------------------------------
	             // System.out.println(init_x+" | "+init_y+" | "+init_z+" | "); 
	      	    fstream = null;
	    		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	               in = new DataInputStream(fstream);
	               br = new BufferedReader(new InputStreamReader(in));
	               strLine=null;
	    	              try {
	    							while ((strLine = br.readLine()) != null )   {
	    								Object[] tokens = strLine.split(" ");
	    							    AnimationSet animationSet = new AnimationSet();
	    							    animationSet.setTime(Float.parseFloat((String) tokens[indx_time]));
	    							    float velocity = Float.parseFloat((String) tokens[indx_vel]);
	    							    float fpa = Float.parseFloat((String) tokens[indx_fpa]);
	    							    float azi = Float.parseFloat((String) tokens[indx_azi]);
	    							    float v_v = (float) (velocity * Math.sin(fpa));
	    							    float v_h = (float) (velocity * Math.cos(fpa));
	    							    //System.out.println(v_v+" | "+init_alt);
	    							    animationSet.setV_h(v_h);
	    							    animationSet.setV_v(v_v);
	    							    animationSet.setAzimuth(azi);
	    							    animationSet.setAlt_init(init_alt);
	    							    animationSets.add(animationSet);	    							  }
	    			       fstream.close();
	    			       in.close();
	    			       br.close();

	    	              } catch (NullPointerException | IOException eNPE) { 
	    	            	  System.out.println("Read raw data, Nullpointerexception");
	    					}catch(IllegalArgumentException eIAE) {
	    					  System.out.println("Read raw data, illegal argument error");
	    					}
						return animationSets;
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

    public static ArrayList<String> Read_SEQU(){
	ArrayList<String> SEQUENCE_DATA = new ArrayList<String>();
	 try {
		BufferedReader br = new BufferedReader(new FileReader(SEQU_File));
	   String strLine;
	   while ((strLine = br.readLine()) != null )   {
	   	String[] tokens = strLine.split(" ");
	   	SEQUENCE_DATA.add(tokens[0]+" "+
	   				      tokens[1]+" "+
	   				      tokens[2]+" "+
	   				      tokens[3]+" "+
	   				      tokens[4]+" "+
	   				      tokens[5]+" "+
	   				      tokens[6]+" "+
	   				      tokens[7]+" "+
	   				      tokens[8]+" "+
	   				      tokens[9]+" "+
	   				      tokens[10]+" "+
	   				      tokens[11]+" "
	   	);
	   }
	   br.close();
	   } catch(NullPointerException | IOException eNPE) { System.out.println(eNPE);System.out.println("ERROR: Read SEQU.res failed. ");}
	   return SEQUENCE_DATA;
	}
    
    public static List<InputFileSet> readResultFileList(String filePath) throws IOException{
    	List<InputFileSet> newInputFileSetList = new ArrayList<InputFileSet>();

   		      	InputFileSet newInputFileSet = new InputFileSet();
   		      	newInputFileSet.setInputDataFilePath(filePath);
   		      	newInputFileSetList.add(newInputFileSet);

    return newInputFileSetList;
    }
	public static void WriteErrorINP() {
	    try {
	        File fac = new File(ERROR_File);
	        if (!fac.exists())
	        {
	            fac.createNewFile();
	        } else {
	        	fac.delete();
	        	fac.createNewFile();
	        }
	        FileWriter wr = new FileWriter(fac);
	        for (int i=0; i<MODEL_ERROR.getRowCount(); i++)
	        {
	        	String error_type 		= (String) MODEL_ERROR.getValueAt(i, 1);
	        	for(int k=0;k<ErrorType.length;k++) { if(error_type.equals(ErrorType[k])){error_type=""+k;} }
	        	String error_trigger 	= (String) MODEL_ERROR.getValueAt(i, 2);
	        	String error_value 		= (String) MODEL_ERROR.getValueAt(i, 3); 
	        	wr.write(error_type+" "+error_trigger+" "+error_value+System.getProperty( "line.separator" ));
	        }
	        wr.close(); 
	       // Update_ErrorIndicator();
	     } catch (IOException eIO){
	     	System.out.println(eIO);
	     }
	}
	
	
	public static void WriteInitialAttitude() {
        try {
            File fac = new File(InitialAttitude_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            FileWriter wr = new FileWriter(fac);
            for (int i=0; i<4; i++)
            {
        			double value =0;
				if(i==0) { 
					 if(AttitudeSetting.INPUT_Quarternion1.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(AttitudeSetting.INPUT_Quarternion1.getText()); 
					 }
				} else if(i==1) {
					 if(AttitudeSetting.INPUT_Quarternion2.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(AttitudeSetting.INPUT_Quarternion2.getText()); 
					 }
				}else if(i==2) {
					 if(AttitudeSetting.INPUT_Quarternion3.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(AttitudeSetting.INPUT_Quarternion3.getText()); 
					 }
				} else if(i==3) {
					 if(AttitudeSetting.INPUT_Quarternion4.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(AttitudeSetting.INPUT_Quarternion4.getText()); 
					 } 
				}
        			wr.write(value+System.getProperty( "line.separator" ));
            }
            wr.close(); 
        } catch (IOException eIO){
        	System.out.println(eIO);
        }
	}
	public static void WriteINERTIA() {
	    try {
	        File fac = new File(INERTIA_File);
	        if (!fac.exists())
	        {
	            fac.createNewFile();
	        } else {
	        	fac.delete();
	        	fac.createNewFile();
	        }
	        FileWriter wr = new FileWriter(fac);
	        for (int j=0;j<3;j++) {
					 if(j==0) {
						 double Ixx = 0;
						 double Ixy = 0; 
						 double Ixz = 0;
						 if(INPUT_IXX.getText().equals("")) {
							 Ixx =0;
						 } else {
							 Ixx = Double.parseDouble(INPUT_IXX.getText()); 
						 }
						 if(INPUT_IXY.getText().equals("")) {
							 Ixy =0;
						 } else {
							 Ixy = Double.parseDouble(INPUT_IXY.getText());
						 }
						 if(INPUT_IXZ.getText().equals("")) {
							 Ixz =0;
						 } else {
							 Ixz = Double.parseDouble(INPUT_IXZ.getText());
						 }
					     wr.write(Ixx+" "+Ixy+" "+Ixz+System.getProperty( "line.separator" ));
					 } else if (j==1) {
						 double Iyx = 0; 
						 double Iyy = 0; 
						 double Iyz = 0;
						 if(INPUT_IYX.getText().equals("")) {
							 Iyx =0;
						 } else {
							 Iyx = Double.parseDouble(INPUT_IYX.getText());
						 }
						 if(INPUT_IYY.getText().equals("")) {
							 Iyy =0;
						 } else {
							 Iyy = Double.parseDouble(INPUT_IYY.getText());
						 }
						 if(INPUT_IYZ.getText().equals("")) {
							 Iyz =0;
						 } else {
							 Iyz = Double.parseDouble(INPUT_IYZ.getText());
						 }
					     wr.write(Iyx+" "+Iyy+" "+Iyz+System.getProperty( "line.separator" )); 
					 } else if (j==2 ) {
						 double Izx = 0;
						 double Izy = 0;
						 double Izz = 0;
						 if(INPUT_IZX.getText().equals("")) {
							 Izx =0;
						 } else {
							 Izx = Double.parseDouble(INPUT_IZX.getText());
						 }
						 if(INPUT_IZY.getText().equals("")) {
							 Izy =0;
						 } else {
							 Izy = Double.parseDouble(INPUT_IZY.getText()); 
						 }
						 if(INPUT_IZZ.getText().equals("")) {
							 Izz =0;
						 } else {
							 Izz = Double.parseDouble(INPUT_IZZ.getText()); 
						 }
					     wr.write(Izx+" "+Izy+" "+Izz+System.getProperty( "line.separator" ));
					 }
	        }
	        wr.close(); 
	
	     } catch (IOException eIO){
	     	System.out.println(eIO);
	     }
	}

	public static void WRITE_INIT() {
        try {
            File fac = new File(Init_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //System.out.println("\n----------------------------------");
            //System.out.println("The file has been created.");
            //System.out.println("------------------------------------");
            double r = 0;
            int rr=0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=30; i++)
            {
        		if (i == 0 ){
        			r = Double.parseDouble(	SidePanelLeft.INPUT_LONG_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==1 ){
        			r = Double.parseDouble(	SidePanelLeft.INPUT_LAT_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
        			r = Double.parseDouble(	SidePanelLeft.INPUT_ALT_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
        			r = Double.parseDouble(	SidePanelLeft.INPUT_VEL_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i == 4 ){
            		r = Double.parseDouble(	SidePanelLeft.INPUT_FPA_Rs.getText()) ;
            		wr.write(r+System.getProperty( "line.separator" ));	
        			} else if (i == 5 ){
                	r = Double.parseDouble(	SidePanelLeft.INPUT_AZI_Rs.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} else if (i == 6 ){
                	r = Double.parseDouble(INPUT_M0.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} else if (i == 7 ){     
                wr.write(CenterPanelRight.getGlobalTime()+System.getProperty( "line.separator" ));	
		    		} else if (i == 8 ){
		            rr =  	SidePanelLeft.Integrator_chooser.getSelectedIndex() ;
		            wr.write(rr+System.getProperty( "line.separator" ));	
		    		} else if (i == 9 ){
		            wr.write(CenterPanelRight.getTargetIndx()+System.getProperty( "line.separator" ));	
		    		} else if (i == 10 ){
		            // r = 0;//Double.parseDouble(INPUT_WRITETIME.getText())  ; // delta-t write out
		            wr.write(0+System.getProperty( "line.separator" ));	
		    		} else if (i == 11 ){
			        r = Double.parseDouble(SidePanelLeft.INPUT_REFELEV.getText())  ; // Reference elevation
			        wr.write(r+System.getProperty( "line.separator" ));	
		        } else if (i == 12) {
	    				String timeString = SidePanelLeft.timePanel.getaTime().getUtcString();
		            wr.write(timeString+System.getProperty( "line.separator" ));	
		        } else if(i == 13) {
	                wr.write(CenterPanelRight.getVelocityCoordinateSystem()+System.getProperty( "line.separator" ));	
		        } else if(i == 14) {
	                wr.write(CenterPanelRight.getDOF_System()+System.getProperty( "line.separator" ));	
		        } else if(i == 15) {
		        	double rate = Double.parseDouble(	SidePanelLeft.INPUT_AngularRate_X.getText());
		        	wr.write(rate+System.getProperty( "line.separator" ));	
		        } else if(i == 16) {
		        	double rate = Double.parseDouble(	SidePanelLeft.INPUT_AngularRate_Y.getText());
		        	wr.write(rate+System.getProperty( "line.separator" ));	
		        } else if(i == 17) {
		        	double rate = Double.parseDouble(	SidePanelLeft.INPUT_AngularRate_Z.getText());
		        	wr.write(rate+System.getProperty( "line.separator" ));	
		        } else if(i==18) {
		        	wr.write(CenterPanelRight.getControllerFrequency()+System.getProperty( "line.separator" ));
		        } else if(i==19) {
		        	wr.write(CenterPanelRight.getGlobalFrequency()+System.getProperty( "line.separator" ));
		        }
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    public void WRITE_CTRL_01() {
        try {
            File fac = new File(CTR_001_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //System.out.println("\n----------------------------------");
            //System.out.println("The file has been created.");
            //System.out.println("------------------------------------");
            double r = 0;
            int rr=0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=12; i++)
            {
        		if (i == 0 ){
        			if(p421_linp0.isSelected()) {
        				rr=1;
        			wr.write(rr+System.getProperty( "line.separator" )); } else {
        				rr=0;
        		    wr.write(rr+System.getProperty( "line.separator" ));	
        			}
        			} else if (i ==1 ){
        			r = Double.parseDouble(INPUT_PGAIN.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
        			r = Double.parseDouble(INPUT_IGAIN.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
        			r = Double.parseDouble(INPUT_DGAIN.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i == 4 ){
            		r = Double.parseDouble(INPUT_CTRLMAX.getText()) ;
            		wr.write(r+System.getProperty( "line.separator" ));	
        			} else if (i == 5 ){
                	r = Double.parseDouble(INPUT_CTRLMIN.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} 
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static void WRITE_SC() {
    try {
            File fac = new File(SC_file);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //System.out.println("\n----------------------------------");
            //System.out.println("The file has been created.");
            //System.out.println("------------------------------------");
            double r = 0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=15; i++)
            {
        			if (i == 0 ){
        				if(INPUT_SURFACEAREA.getText().isEmpty()) {
		        			r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			r = Double.parseDouble(INPUT_SURFACEAREA.getText()) ;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			} else if (i ==1 ){
        				if(INPUT_BALLISTICCOEFFICIENT.getText().isEmpty()) {
		        			r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			r = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText()) ;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			} else if (i==2) {
        				try {
        				if(INPUT_ParachuteDiameter.getText().isEmpty()) {
		        			 r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			double d = Double.parseDouble(INPUT_ParachuteDiameter.getText()) ;
		        			double area = PI/4*d*d;
		        			wr.write(area+System.getProperty( "line.separator" ));
        				}
        				} catch (NullPointerException e) {
		        			 r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			}
            }
            wr.close();
      } catch (IOException eIO) {
      System.out.println(eIO);
      }
    }
    
    public static void WRITE_AERO() {
    try {
            File fac = new File(Aero_file);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=15; i++)
            {
        			if (i == 0 ){
        					int indx = getDragModelSetIndx();
	        			   wr.write(indx+System.getProperty( "line.separator" ));
        			} else if ( i == 1 ) {
        				double CD = 1.4;
        				try {
        			    CD = Double.parseDouble(ConstantCD_INPUT.getText());
        				wr.write(CD+System.getProperty( "line.separator" ));
        				} catch(NullPointerException e) {
            				wr.write(CD+System.getProperty( "line.separator" ));
            			}
        			} else if ( i == 2 ) {
        				try {
        				if(INPUT_RB.getText().isEmpty()) {
		        			double r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			double r = Double.parseDouble(INPUT_RB.getText()) ;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        				} catch (NullPointerException e) {
		        			double r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			} else if(i == 3 ) {
    					int indx = getParachuteModelSetIndx();

	        			   wr.write(indx+System.getProperty( "line.separator" ));
        			} else if(i == 4 ) {
        				try {
        				if(ConstantParachuteCD_INPUT.getText().isEmpty()) {
		        			double r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			double r = Double.parseDouble(ConstantParachuteCD_INPUT.getText()) ;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        				} catch (NullPointerException e) {
		        			double r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}
        			} else if (i==5) {

        			}
            }
            wr.close();
      } catch (IOException eIO) {
      System.out.println(eIO);
      }
    }
    
    public static void WRITE_INTEG() {
    	String integ_file = null;  
    	int steps =0 ; 
    	if(	SidePanelLeft.Integrator_chooser.getSelectedIndex()==0) {
    		integ_file = INTEG_File_01; 
    		steps = 4; 
    	} else if (	SidePanelLeft.Integrator_chooser.getSelectedIndex()==1) {
    		integ_file = INTEG_File_02; 
    		steps =1;
    	} else if (SidePanelLeft.Integrator_chooser.getSelectedIndex()==2) {
    		integ_file = INTEG_File_03; 
    		steps =4;
    	} else if (SidePanelLeft.Integrator_chooser.getSelectedIndex()==3) {
    		integ_file = INTEG_File_04; 
    		steps =5;
    	}
        try {
            File fac = new File(integ_file);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            double r = 0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<steps; i++)
            {
            		   if(i==0) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_01.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==1) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_02.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==2) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_03.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==3) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_04.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==4) {
        			r = Double.parseDouble(SidePanelLeft.INPUT_IntegratorSetting_05.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} 
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static void   WRITE_EventHandler() {
        try {
            File fac = new File(EventHandler_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //-----------------------------------------------------
            FileWriter wr = new FileWriter(fac);
            for (int i=0; i<MODEL_EventHandler.getRowCount(); i++){
	            	int EventType =0;
		            	for(int j=0;j<EventHandler_Type.length;j++) {
		            		if(MODEL_EventHandler.getValueAt(i, 0).equals(EventHandler_Type[j])) {EventType =j;}
		            	}
	            	double EventValue = Double.parseDouble((String) MODEL_EventHandler.getValueAt(i, 1));
	            	wr.write(EventType+BB_delimiter+EventValue+BB_delimiter+System.getProperty( "line.separator" ));	
			}               
	            wr.close();
            } catch (IOException eIO) {System.out.println(eIO);}
    }
    
    public static void WRITE_PROP() {
        try {
            File fac = new File(Prop_File);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            //System.out.println("\n----------------------------------");
            //System.out.println("The file has been created.");
            //System.out.println("------------------------------------");
            double r = 0;
            FileWriter wr = new FileWriter(fac);
            for (int i = 0; i<=30; i++)
            {
        			if (i == 0 ){
            			r = Double.parseDouble(INPUT_ISP.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==1 ){
            			r = Double.parseDouble(INPUT_PROPMASS.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
            			r = Double.parseDouble(INPUT_THRUSTMAX.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
            			r = Double.parseDouble(INPUT_THRUSTMIN.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i == 4 ){
        					if(INPUT_ISPMODEL.isSelected()) {
        						wr.write(1+System.getProperty( "line.separator" ));
        					} else {
        						wr.write(0+System.getProperty( "line.separator" ));
        					}
        			} else if (i == 5 ){
        				try {
        					if (INPUT_ISPMIN.equals("")) {
        						r = Double.parseDouble(INPUT_ISP.getText()) ;
        					} else {
        						r = Double.parseDouble(INPUT_ISPMIN.getText()) ;
        					}
            			wr.write(r+System.getProperty( "line.separator" ));
        				} catch (java.lang.NumberFormatException eNFE) {
        					wr.write(""+System.getProperty( "line.separator" ));	
        				}
            		} else if( i == 6 ) {
            			r = Double.parseDouble(INPUT_RCSX.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 7 ) {
            			r = Double.parseDouble(INPUT_RCSY.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 8 ) {
            			r = Double.parseDouble(INPUT_RCSZ.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 9 ) {
            			r = Double.parseDouble(INPUT_RCSXTHRUST.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 10 ) {
            			r = Double.parseDouble(INPUT_RCSYTHRUST.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 11 ) {
            			r = Double.parseDouble(INPUT_RCSZTHRUST.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 12 ) {
            			r = Double.parseDouble(INPUT_RCSTANK.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 13 ) {
            			r = Double.parseDouble(INPUT_RCSXISP.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 14 ) {
            			r = Double.parseDouble(INPUT_RCSYISP.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		} else if( i == 15 ) {
            			r = Double.parseDouble(INPUT_RCSZISP.getText());
            			wr.write(r+System.getProperty( "line.separator" ));
            		}
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static void WRITE_SequenceFile() {
        try {
            File fac = new File(sequenceFile);
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
            for (int i = 0; i<sequenceContentList.size(); i++)
            {
        		int ID = sequenceContentList.get(i).getSequenceID();
            	String sequenceContent = ID+" "+
						 			 sequenceContentList.get(i).getSequenceName()+" "+
            							 fcSeparator	+" "+ 
            							 sequenceContentList.get(i).getFlightControllerSelect().getSelectedIndex()+" "+
            							 fcSeparator	+" "+ 
            							 eventSeparator+" "+ 
            							 sequenceContentList.get(i).getEventSelect().getSelectedIndex()+" "+
            							 eventSeparator+" "+ 
            							 endSeparator+" "+ 
            							 sequenceContentList.get(i).getEndSelect().getSelectedIndex()+" "+
            							 sequenceContentList.get(i).getValueEnd().getText()+" "
            							 +endSeparator+" ";
            	
            	wr.write(sequenceContent+System.getProperty( "line.separator" ));
		    }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    public static int getDragModelSetIndx() {
		int k=0;
		for(int j=0;j<DragModelSet.size();j++) {
			if(DragModelSet.get(j).isSelected()) {
				k=j;
			}
		}
		return k;
    }

    public static int getParachuteModelSetIndx() {
		int k=0;
		for(int j=0;j<ParachuteBulletPoints.size();j++) {
			if(ParachuteBulletPoints.get(j).isSelected()) {
				k=j;
			}
		}
		return k;
    }
    public static void EvaluateSurfaceAreaSetup() {
	    	if(INPUT_SURFACEAREA.getText().equals("0")) {	    		
	    		INPUT_SURFACEAREA.setText("");
	    		INPUT_SURFACEAREA.setEditable(false);
	    		INPUT_BALLISTICCOEFFICIENT.setEditable(true);
	    	} else if (INPUT_BALLISTICCOEFFICIENT.getText().equals("0")) {
	    		INPUT_BALLISTICCOEFFICIENT.setText("");
	    		INPUT_SURFACEAREA.setEditable(true);
	    		INPUT_BALLISTICCOEFFICIENT.setEditable(false);	    		
	    	}
    	}
    
    public static double[][] FIND_ctrl_init_cond() throws IOException{
	   	   List<SequenceElement> SEQUENCE_DATA = new ArrayList<SequenceElement>(); 
	   	    SEQUENCE_DATA = ReadInput.readSequence();
	   	    double[][] INIT_CONDITIONS = new double[4][SEQUENCE_DATA.size()];
	   	    for (int i=0;i<SEQUENCE_DATA.size();i++) {
	   	    	
	   	    }
	   	    
	   	    return INIT_CONDITIONS;
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

	public static void SET_MAP(int TARGET) throws URISyntaxException, IOException{
		final XYPlot plot2 = (XYPlot) Chart_MercatorMap.getPlot();
		final PolarPlot plot_polar = (PolarPlot) chart_PolarMap.getPlot();
		  if (TARGET==0){ 
			  try {
		         BufferedImage myImage = ImageIO.read(new File(MAP_EARTH));
		         plot2.setBackgroundImage(myImage);  
			  } catch(IIOException eIIO) {
				  System.out.println(eIIO);System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if (TARGET==1){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(MAP_MOON));
		         BufferedImage myImage_Polar = ImageIO.read(new File(MAP_SOUTHPOLE_MOON));
		         plot2.setBackgroundImage(myImage);  
		         plot_polar.setBackgroundImage(myImage_Polar);
			  } catch(IIOException eIIO) {
				  System.out.println(eIIO);System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if(TARGET==2){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(MAP_MARS));
		         plot2.setBackgroundImage(myImage); 
			  } catch(IIOException eIIO) {
				  //System.out.println(eIIO);
				  System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if(TARGET==3){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(MAP_VENUS));
		         plot2.setBackgroundImage(myImage); 
		  } catch(IIOException eIIO) {
			  //System.out.println(eIIO);
			  System.out.println("ERROR: Reading maps failed.");
		  }
		  }
	}
	
	
	public static XYSeriesCollection AddDataset_Mercator_MAP() throws IOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
       	XYSeries xyseries10 = new XYSeries("", false, true); 

            FileInputStream fstream = null;
            		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
                  DataInputStream in = new DataInputStream(fstream);
                  BufferedReader br = new BufferedReader(new InputStreamReader(in));
                  String strLine;
                  try {
                  while ((strLine = br.readLine()) != null )   {
		           String[] tokens = strLine.split(" ");
		           double y = Double.parseDouble(tokens[2])*180/PI;  // Latitude [deg[
		           double x = Double.parseDouble(tokens[1])*180/PI;	 // Longitude [deg]
		           while (x>180 || x<-180 || y>90 || y<-90){
		           if (x>180){
		        	   x=x-360;
		           } else if (x<-180){
		        	   x=x+360;
		           }
		           if (y>90){
		        	   y=y-180;
		           } else if (y<-90){
		        	   y=y+180;
		           }
		           }
		           //System.out.println(x + " | " + y);
		         	xyseries10.add(x,y);
		           }
           in.close();
        ResultSet_MercatorMap.addSeries(xyseries10); 
                  } catch(NullPointerException eNPI) { System.out.print(eNPI); }
        return ResultSet_MercatorMap;          
       }
	public static XYSeriesCollection AddDataset_Polar_MAP() throws IOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
       	XYSeries xyseries10 = new XYSeries("", false, true); 

            FileInputStream fstream = null;
            		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
                  DataInputStream in = new DataInputStream(fstream);
                  BufferedReader br = new BufferedReader(new InputStreamReader(in));
                  String strLine;
                  try {
                  while ((strLine = br.readLine()) != null )   {
		           String[] tokens = strLine.split(" ");
		           double y = Double.parseDouble(tokens[2])*180/PI;  // Latitude [deg[
		           double x = Double.parseDouble(tokens[1])*180/PI;	 // Longitude [deg]
		           
		           while (x>360 || x<0 || y>90 || y<-90){
				                  if (x > 360){x=x-360;
				           } else if (x <   0){x=x+360; }
				           
				                  if (y>90){ y=y-180;
				           } else if (y<-90){ y=y+180;}
		           }
		           //System.out.println(x + " | " + y);
		         	xyseries10.add(x,y);
		           }
           in.close();
           ResultSet_PolarMap.addSeries(xyseries10); 
                  } catch(NullPointerException eNPI) { System.out.print(eNPI); }
        return ResultSet_PolarMap;          
       }
	public static void CreateChart_MercatorMap() throws IOException{
		 try {
		        ResultSet_MercatorMap = AddDataset_Mercator_MAP(); 
		        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {
		        	System.out.println(" Error read for plot X40");
		        }

		        Chart_MercatorMap = ChartFactory.createScatterPlot("", "Longitude [deg]", "Latitude [deg] ", ResultSet_MercatorMap, PlotOrientation.VERTICAL, false, false, false); 
				XYPlot plot = (XYPlot)Chart_MercatorMap.getXYPlot(); 
		        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
		        
			    double size = 2.0;
			    double delta = size / 2.0;
				Shape dot = new Ellipse2D.Double(-delta, -delta, size, size);
				renderer.setSeriesShape(0, dot);
				renderer.setSeriesLinesVisible(0, false);
		        plot.setRenderer(0, renderer);
		        
		        Chart_MercatorMap.setBackgroundPaint(Color.white);
				
		        plot.getDomainAxis().setLabelFont(labelfont_small);
		        plot.getRangeAxis().setLabelFont(labelfont_small);
				
		       final XYPlot plot2 = (XYPlot) Chart_MercatorMap.getPlot();
		       plot2.setForegroundAlpha(0.5f);
		       plot2.setBackgroundPaint(Color.white);
		       plot2.setDomainGridlinePaint(Color.black);
		       plot2.setRangeGridlinePaint(new Color(220,220,220));

		       ValueAxis domain2 = plot.getDomainAxis();
		       domain2.setRange(-180, 180);
		       domain2.setInverted(false);
		       // change the auto tick unit selection to integer units only...
		       final NumberAxis rangeAxis2 = (NumberAxis) plot2.getRangeAxis();
		       rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		       rangeAxis2.setRange(-90, 90);
		       ChartPanel CPXX4 = new ChartPanel(Chart_MercatorMap);
		       CPXX4.setBackground(backgroundColor);
		       CPXX4.setDomainZoomable(false);
		       CPXX4.setRangeZoomable(false);
		       CPXX4.setMaximumDrawHeight(50000);
		       CPXX4.setMaximumDrawWidth(50000);
		       CPXX4.setMinimumDrawHeight(0);
		       CPXX4.setMinimumDrawWidth(0);
				//CP2.setMouseWheelEnabled(tru
		       CPXX4.addChartMouseListener(new ChartMouseListener() {
		           @Override
		           public void chartMouseClicked(ChartMouseEvent event) {
		        	   Rectangle2D dataArea2 = CPXX4.getScreenDataArea();
		               Point2D p = CPXX4.translateScreenToJava2D(event.getTrigger().getPoint());
		               double x = Chart_MercatorMap.getXYPlot().getDomainAxis().java2DToValue(event.getTrigger().getX(), dataArea2, RectangleEdge.BOTTOM);
		               double y = plot2.getRangeAxis().java2DToValue(p.getY(), dataArea2, plot2.getRangeAxisEdge());
		               INDICATOR_PageMap_LONG.setText("" + df_X4.format(x));
		               INDICATOR_PageMap_LAT.setText("" + df_X4.format(y));
		           }

		           @Override
		           public void chartMouseMoved(ChartMouseEvent event) {
		        	   Rectangle2D dataArea2 = CPXX4.getScreenDataArea();
		        	   Point2D p = CPXX4.translateScreenToJava2D(event.getTrigger().getPoint());
		               ValueAxis xAxis2 = Chart_MercatorMap.getXYPlot().getDomainAxis();
		               double x = xAxis2.java2DToValue(event.getTrigger().getX(), dataArea2, RectangleEdge.BOTTOM);
		              // double y = yAxis2.java2DToValue(event.getTrigger().getYOnScreen(), dataArea3, RectangleEdge.BOTTOM);
		               double y = plot2.getRangeAxis().java2DToValue(p.getY(), dataArea2, plot2.getRangeAxisEdge());
		               BlueBookVisual.xCrosshair_x.setValue(x);
		               BlueBookVisual.yCrosshair_x.setValue(y);
		           }
		   });
		       CrosshairOverlay crosshairOverlay2 = new CrosshairOverlay();
		       xCrosshair_x = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		       xCrosshair_x.setLabelVisible(true);
		       yCrosshair_x = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		       yCrosshair_x.setLabelVisible(true);
		       crosshairOverlay2.addDomainCrosshair(xCrosshair_x);
		       crosshairOverlay2.addRangeCrosshair(yCrosshair_x);
		       CPXX4.addOverlay(crosshairOverlay2);
		       CPXX4.setPreferredSize(new Dimension(1300, 660));
		       PageX04_Map.add(CPXX4, BorderLayout.CENTER);	
	}
	
	public static void CreateChart_PolarMap() throws IOException {
		ResultSet_PolarMap.removeAllSeries();
        try {
        	ResultSet_PolarMap = AddDataset_Polar_MAP(); 
        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {
        	System.out.println(" Error read for plot X40");
        }

        chart_PolarMap = ChartFactory.createPolarChart("", ResultSet_PolarMap, false, false, false);
  
		PolarPlot plot =  (PolarPlot) chart_PolarMap.getPlot();
		MyDefaultPolarItemRenderer renderer = new MyDefaultPolarItemRenderer();
		renderer.setSeriesFillPaint(0, Color.red);
        plot.setRenderer(0, renderer);
	
		chart_PolarMap.setBackgroundPaint(Color.white);

		
        plot.getAxis().setLabelFont(labelfont_small);
   
       ValueAxis domain2 = plot.getAxis();
       domain2.setRange(-90, -70);
       domain2.setInverted(false);
       domain2.setTickLabelPaint(Color.white);
       plot.setAngleGridlinePaint(Color.white);
       plot.setAngleLabelPaint(Color.white);
       // change the auto tick unit selection to integer units only...
       
       ChartPanel CPXX4 = new ChartPanel(chart_PolarMap);
       CPXX4.setBackground(backgroundColor);
       CPXX4.setLayout(new BorderLayout());
       CPXX4.setDomainZoomable(false);
       CPXX4.setRangeZoomable(false);
       CPXX4.setPreferredSize(new Dimension(800, 800));
       CPXX4.setMaximumDrawHeight(50000);
       CPXX4.setMaximumDrawWidth(50000);
       CPXX4.setMinimumDrawHeight(0);
       CPXX4.setMinimumDrawWidth(0);
       JPanel innerPanel = new JPanel();
       innerPanel.setLayout(new BorderLayout());
       innerPanel.setPreferredSize(new Dimension(800, 800));
       innerPanel.add(CPXX4, BorderLayout.CENTER);
       PolarMapContainer.add(innerPanel);
       PolarMapContainer.addComponentListener(new ComponentAdapter() {
           @Override
           public void componentResized(ComponentEvent e) {
        	   KeepAspectRatio_Map(innerPanel, PolarMapContainer);
           }
       });
	}
	
	private static void KeepAspectRatio_Map(JPanel innerPanel, JPanel container) {
        int w = container.getWidth();
        int h = container.getHeight();
        int size =  Math.min(w, h);
        innerPanel.setPreferredSize(new Dimension(size, size));
        container.revalidate();
    }
	
	public static void CreateLocalElevationFile(int Resolution) throws IOException {
        FileInputStream fstream = null;
        ArrayList<String> steps = new ArrayList<String>();
        String Elevation_File="";
        if(Resolution==4) {
        	Elevation_File = Elevation_File_RES4;
        } else if (Resolution==16) {
        	Elevation_File = Elevation_File_RES16;
        } else if (Resolution==64) {
        	Elevation_File = Elevation_File_RES64;
        } else if (Resolution==128) {
        	Elevation_File = Elevation_File_RES128;
        }
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      try {
      while ((strLine = br.readLine()) != null )   {
       String[] tokens = strLine.split(" ");
       double longitude = Double.parseDouble(tokens[1])*rad2deg;     // Longitude 	[deg]
       double latitude  = Double.parseDouble(tokens[2])*rad2deg;     // Latitude 	[deg]
       double local_elevation = GetLocalElevation(Elevation_File, longitude, latitude);

       steps.add(local_elevation+" ");
      }
      	String resultpath="";
      	String dir = System.getProperty("user.dir");
      	resultpath = dir + "/LocalElevation.inp";
      PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
      for(String step: steps) {
          writer.println(step);
      }
      System.out.println("Write: Local Elevation File. ");

		writer.close();
		in.close();
		br.close();
		fstream.close();
      } catch(NullPointerException eNPI) { System.out.print(eNPI); }
		
	}
	@SuppressWarnings("resource")
	public static double GetLocalElevation(String InputFile, double longitude, double latitude) throws IOException {
		double ELEVATION = 0;
		int resolution = 4;
		int latitude_indx = (int) ((90-latitude)*resolution+1);
		int longitude_indx = (int) (longitude*resolution+1);
		FileInputStream inputStream = null;
		Scanner sc  = null;
		String path = InputFile;
		long k =0; 
		double max_runtime = 2; 
		long startTime = System.nanoTime();
		boolean TargetNOTReached=true; 
		File Input = new File(InputFile);
		try {
		    inputStream = new FileInputStream(path);
		    LineIterator it = FileUtils.lineIterator(Input, "UTF-8");
		  //  sc = new Scanner(inputStream, "UTF-8");
		    while (it.hasNext() && TargetNOTReached) {
		        String line = it.nextLine();
		        if(latitude_indx==k) {
					String[] tokens = line.split(",");
		        	ELEVATION = Double.parseDouble(tokens[longitude_indx]);
		        	TargetNOTReached=false;
		        	it.close();
		        	return ELEVATION;
		        }
				long endTime   = System.nanoTime();
				long totalTime = endTime - startTime;
				double  totalTime_sec = (double) (totalTime * 1E-9);
				if(totalTime_sec>max_runtime) {break;}
				k++;
		    }
		    // note that Scanner suppresses exceptions
		    /*
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    } */
		    it.close();
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
		return ELEVATION;
	}


    
    static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] comps = {"foo", "bar", "baz"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.GREEN);
        g.drawString("Loading "+comps[(frame/5)%3]+"...", 120, 150);
        Image image = Toolkit.getDefaultToolkit().getImage(BlueBookVisual.ICON_File);
        g.drawImage(image, frame, frame, Color.RED, null);
    }
    
    
	private static void createAndShowGUI() throws IOException {
		/*
		JWindow window = new JWindow();
		JLabel label = new JLabel("", new ImageIcon(ICON_File), SwingConstants.CENTER);
		label.setSize(500, 500);
		label.setVisible(true);
		window.getContentPane().add(label, BorderLayout.CENTER);
		//window.setBounds(800, 150, 800, 200);
		window.setSize(600,500);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		 */
		
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
		


        JFrame.setDefaultLookAndFeelDecorated(false);
        MAIN_frame = new JFrame("" + PROJECT_TITLE);
        MAIN_frame.setFont(small_font);
        BlueBookVisual demo = new BlueBookVisual();
        JPanel tp = demo.createContentPane();
        tp.setPreferredSize(new java.awt.Dimension(x_init, y_init));
        MAIN_frame.add(tp, BorderLayout.CENTER);
        MAIN_frame.pack();
        MAIN_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        if(splashLoad) { splash.close();}
        
        MAIN_frame.setLocationRelativeTo(null);
        MAIN_frame.setExtendedState(MAIN_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        MAIN_frame.setVisible(true);
        // Create Icon image  -  top left for windows
         try {
        	BufferedImage myIcon = ImageIO.read(new File(ICON_File)); 
        	MAIN_frame.setIconImage(myIcon);
         }catch(IIOException eIIO) {System.out.println(eIIO);}    
         // Create taskbar icon - for mac 
         if(OS_is==1) {
        	 // Set Taskbar Icon for MacOS
         try {
         Application application = Application.getApplication();
         Image image = Toolkit.getDefaultToolkit().getImage(ICON_File);
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
			return ICON_File;
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


		public class BackgroundMenuBar extends JMenuBar {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Color bgColor=Color.WHITE;

		    public void setColor(Color color) {
		        bgColor=color;
		    }

		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setColor(bgColor);
		        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

		    }
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