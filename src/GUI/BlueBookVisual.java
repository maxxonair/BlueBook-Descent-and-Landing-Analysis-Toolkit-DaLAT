package GUI; 
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
import java.awt.event.KeyEvent;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import javax.swing.*;  

import Model.atm_dataset;
import Sequence.SequenceElement;
import Simulator_main.RealTimeResultSet;
import GUI.FxElements.SpaceShipView3D;
import GUI.FxElements.SpaceShipView3DFrontPage;
import GUI.FxElements.TargetView3D;
import GUI.PostProcessing.CreateCustomChart;
import GUI.Settings.Settings;
import Toolbox.TextAreaOutputStream;
import Toolbox.Mathbox;
import Toolbox.ReadInput;
import VisualEngine.animation.AnimationSet;
import VisualEngine.engineLauncher.worldGenerator;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import Controller.LandingCurve;
import Controller.PitchCurve;

import com.apple.eawt.Application;

public class BlueBookVisual implements  ActionListener {
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Main Container Frame Elements
    //-----------------------------------------------------------------------------------------------------------------------------------------
	static String PROJECT_TITLE = "  BlueBook Descent and Landing Analysis Toolkit - V0.3 ALPHA";
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
    public static String ICON_File   	 		= "/images/BB_icon2.png";
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
	public static String CurrentWorkfile_Name = "";
	public static File CurrentWorkfile_Path = new File("");
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //											Styles, Fonts, Colors
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static int gg = 235;
    //public static Color labelColor = new Color(0,0,0);    					// Label Color
    public static Color labelColor = new Color(220,220,220);    					// Label Color
   	//public static Color backgroundColor = new Color(255,255,255);				// Background Color
   	public static Color backgroundColor = new Color(41,41,41);				// Background Color
   	public static Color w_c = new Color(gg,gg,gg);					// Box background color
   //	public static Color t_c = new Color(255,255,255);				// Table background color
   	public static Color t_c = new Color(61,61,61);				// Table background color
   	
   	
   	public static Color light_gray = new Color(230,230,230);
   	
    static DecimalFormat decf 		  = new DecimalFormat("#.#");
    static DecimalFormat decQuarternion =  new DecimalFormat("#.########");
    static DecimalFormat df_X4 		  = new DecimalFormat("#####.###");
    static DecimalFormat df_VelVector = new DecimalFormat("#.00000000");
    static Font menufont              = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12);
    static Font small_font			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    static Font labelfont_small       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
    static Font labelfont_verysmall   = new Font("Verdana", Font.BOLD, 7);
    static Font targetfont            = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    static Font HeadlineFont          = new Font("Georgia", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    public static DecimalFormat df 	  = new DecimalFormat();
    
    static List<JRadioButton> DragModelSet = new ArrayList<JRadioButton>();
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Variables and Container Arrays
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static int INTEGRATOR = 0; 
    public static String[] Integrator_Options = { "Dormand Prince 853 Integrator", 
    											  "Standard Runge Kutta Integrator" , 
    											  "Gragg-Bulirsch-Stoer Integrator", 
    											  "Adams-Bashforth Integrator"};
    public static String[] Target_Options = { "Earth", 
    									      "Moon" ,	
    									      "Mars", 	
    										  "Venus"};
    public static String[] TargetCurve_Options = { "",						// No target curve -> continous burn
    											   "Parabolic", 
    											   "SquareRoot" ,	
    											   "Linear" 	
			  };	
    public static String[] TargetCurve_Options_TVC = { 	"",						// No target curve -> continous burn
    														"SquareRoot" ,	
    														"Linear" 	
};
    public static String[] Axis_Option_NR = { "Time [s]",
    										  "Longitude [deg]", 
    										  "Latitude [deg]" ,
    										  "Altitude ref. Landing [m]", 
    										  "Altidue ref. mean [m]",
    										  "Radius [m]",
    										  "Velocity NED/ECEF [m/s]", 
    										  "Flight Path angle NED/ECEF [deg]", 
    										  "Local Azimuth NED/ECEF [deg]", 
    										  "Density [kg/m3]", 
    										  "Drag Force [N]", 
    										  "Lift Force [N]",
    										  "Side Force [N]", 
    										  "Gravitational Acc. -radial [m/s]", 
    										  "Gravitational acc. -north/south [m/s]",
    										  "Gravitational acc. - average [m/s]", 
    										  "Static temperature [K]", 
    										  "Mach [-]",
    										  "Heat capacity ratio", 
    										  "Gas constant", 
    										  "Static pressure [Pa]", 
    										  "Cd [-]", 
    										  "Cl [-]", 
    										  "Bank angle [deg]", 
    										  "Flowzone [-]",
    										  "Dynamic Pressure [Pa]",
    										  "Parachute Cd [-]",
    										  "Cdm [-]",
    										  "SC Mass [kg]",
    										  "Normalized Deceleartion [-]",
    										  "Total Engergy [J]",
    										  "Thrust CMD [%]",
    										  "Tank filling level [%]",
    										  "Thrust Force [N]", 
    										  "Thrust to mass [N/kg]",
    										  "Velocity horizontal [m/s]",
    										  "Velocity vertical [m/s]",
    										  "Accumulated Delta-v [m/s]",
    										  "Active Sequence ID [-]",
    										  "Groundtrack [km]",
    										  "TM CNTRL Error [m/s]",
    										  "TVC CNTRL Error [deg]",
    										  "CNTRL Time [s]",
    										  "Thrust Elevation [deg]",
    										  "Thrust Deviation [deg]",
    										  "Thrust Force x B [N]",
    										  "Thrust Force y B [N]",
    										  "Thrust Force z B [N]",
    										  "Vel NED/ECI [m/s]",
    										  "FPA NED/ECI [m/s",
    										  "AZ  NED/ECI [m/s]",
    										  "FPA_dot ",
    										  "Thrust Elevation Angel Change [deg/s]",
    										  "Engine Loss Indicator [true/false]", 
    										  "Velocity u NED/ECEF [m/s]",
    										  "Velocity v NED/ECEF [m/s]",
    										  "Velocity w NED/ECEF [m/s]",
    										  "Quaternion q1",
    										  "Quaternion q2",
    										  "Quaternion q3",
    										  "Quaternion q4",
    										  "Main engine ISP [s]",
    										  "Fx NED [N]",
    										  "Fy NED [N]",
    										  "Fz NED [N]",
    										  "Gx NED [m/s2]",
    										  "Gy NED [m/s2]",
    										  "Gz NED [m/s2]",
    										  "G total [m/s2]",
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
    										  "Angular Rate x B2NED [deg/s]",
    										  "Angular Rate y B2NED [deg/s]",
    										  "Angular Rate z B2NED [deg/s]",
    										  "Angular Momentum x B [Nm]",
    										  "Angular Momentum y B [Nm]",
    										  "Angular Momentum z B [Nm]",
    										  "X Roll Angle - Euler Phi [deg]",
    										  "Y Pitch Angle - Euler Theta [deg]",
    										  "Z Yaw Angle - Euler Psi [deg]",
    										  "Angle of Attack [deg]"
    										  };
    
    public static String[] Thrust_switch = { "Universal Module - 3 DoF / 6 DoF",
    										 "Ascent Module - 3 DoF",
    										 "Ascent Module - 6 DoF"
    };
    public static String[] LocalElevation_Resolution = { "4", 
			  											 "16" , 
			  											 "64", 
			  											 "128"};
    
    public static String[] EventHandler_Type = { "Time [s]", 
			  									 "Longitude [rad]" , 
			  									 "Latitude [rad]", 
			  									 "Altitude [m]",
			  									 "Velocity [m]",
			  									 "FPA [rad]",
			  									 "Azimuth [rad]",
    											     "SC Mass [kg]"};
    
	public static String[] COLUMS_SEQUENCE = {"ID", 
			 "Sequence END type", 
			 "Sequence END value", 
			 "Sequence type", 
			 "Sequence TL FC",		// Thrust Level Flight Controller
			 "TL FC target velocity [m/s]", 
			 "TL FC target altitude [m]", 
			 "TL FC target curve",
			 "Sequence TVC FC", 		// Thrust Vector Control Flight Controller 
			 "TVC FC Input Value 1 [-]", 
			 "TVC FC Input Value 2 [-]", 
			 "TVC FC target curve"};
	public static String[] COLUMS_CONTROLLER = {"ID",
												"Controller Type",
												"P gain",
												"I gain",
												"D gain",
												"MIN cmd",
												"MAX cmd"};
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

public static String[] SequenceENDType = {"Time [s]",
										  "Altitude [m]",
										  "Velocity [m/s]",
										  "FPA ref. Horizon [deg]"
};
public static String[] SequenceType = {"Coasting (No Thrust/ FC OFF)",
									   "Continous Thrust (FC OFF)",
									   "Controlled Thrust (FC ON)",
									   "Constrained Thrust (FC OFF)",
									   "Reference Angle Control (FC ON)",
									   "Full TVC Reference Trajectory Control (FC ON)"
};
public static String[] SequenceFC    = { ""};
public static String[] FCTargetCurve = { "Parabolic Velocity-Altitude",
										 "SquareRoot Velocity-Altitude",
										 "Hover Parabolic entry"
};
public static String[] SequenceTVCFC     = { ""};
public static String[] Vel_Frame_options = { "Cartesian Coordinate Frame (NED)",
											 "Spherical Coordinate Frame (NED)"};


    public static double h_init;
    public static double v_init;
    public static double v_touchdown;
	public static double Propellant_Mass=0;
	public static double M0;
	
	public static int VelocityCoordinateSystem;
	public static int DOF_System;
	public static double rotX=0;
	public static double rotY=0;
	public static double rotZ=0;
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												GUI Elements
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    static int extx_main = 1350;
    static int exty_main = 800; 
    public static boolean chartA3_fd=true;  
	static boolean Chart_MercatorMap4_fd = true;
	static boolean CHART_P1_DashBoardOverviewChart_fd = true;
    public static JTextArea textArea = new JTextArea();
    public static JFrame frame_CreateLocalElevationFile;
    public static TextAreaOutputStream  taOutputStream = new TextAreaOutputStream(textArea, "");     
    private static Crosshair xCrosshair_x;
    private static Crosshair yCrosshair_x; 
    public static Crosshair xCrosshair_A3_1,xCrosshair_A3_2,xCrosshair_A3_3,xCrosshair_A3_4,yCrosshair_A3_1,yCrosshair_A3_2,yCrosshair_A3_3,yCrosshair_A3_4;
    public static Crosshair xCrosshair_DashBoardOverviewChart_Altitude_Velocity, yCrosshair_DashBoardOverviewChart_Altitude_Velocity,xCrosshair_DashBoardOverviewChart_Time_FPA, yCrosshair_DashBoardOverviewChart_Time_FPA;
    public static JPanel PageX04_Dashboard;
    public static JPanel PageX04_Map;
    public static JPanel PageX04_3;
    public static JPanel PageX04_SimSetup; 
    public static JPanel PageX04_RawDATA; 
    public static JPanel PageX04_PolarMap;
    public static JPanel PolarMapContainer; 
    public static JPanel PageX04_GroundClearance; 
    public static JPanel P1_Plotpanel;
    public static JPanel P1_SidePanel; 
    public static JPanel PageX04_AttitudeSetup;
    public static JSplitPane SplitPane_Page1_Charts_horizontal; 
    public static JSplitPane SplitPane_Page1_Charts_vertical; 
    public static JSplitPane SplitPane_Page1_Charts_vertical2; 
    public static JFreeChart Chart_MercatorMap;
    public static JFreeChart Chart_GroundClearance;
	public static JFreeChart CHART_P1_DashBoardOverviewChart_Altitude_Velocity;
	public static JFreeChart CHART_P1_DashBoardOverviewChart_Time_FPA;
	public static JFreeChart chart_PolarMap;	  
	public static JFreeChart Chart_MercatorMap4;	
	public static ChartPanel ChartPanel_DashBoardOverviewChart_Altitude_Velocity; 
	public static ChartPanel ChartPanel_DashBoardOverviewChart_Time_FPA; 
	public static ChartPanel ChartPanel_DashBoardFlexibleChart;
    private static Crosshair xCH_DashboardFlexibleChart;
    private static Crosshair yCH_DashboardFlexibleChart;    
    static public JFreeChart chartA3_1,chartA3_2,chartA3_3,chartA3_4; 
    public static ChartPanel CP_A31,CP_A32,CP_A33,CP_A34;
	public static DefaultTableXYDataset CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity = new DefaultTableXYDataset();
	public static DefaultTableXYDataset CHART_P1_DashBoardOverviewChart_Dataset_Time_FPA = new DefaultTableXYDataset();
	public static DefaultTableXYDataset ResultSet_GroundClearance_FlightPath = new DefaultTableXYDataset();
	public static DefaultTableXYDataset ResultSet_GroundClearance_Elevation = new DefaultTableXYDataset();
	public static XYSeriesCollection ResultSet_FlexibleChart = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_1 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_2 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_3 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_4 = new XYSeriesCollection();
    public static JCheckBox INPUT_ISPMODEL; 
    public static JLabel INDICATOR_PageMap_LAT,INDICATOR_PageMap_LONG, INDICATOR_LAT,INDICATOR_LONG,INDICATOR_ALT,INDICATOR_VEL,INDICATOR_FPA,INDICATOR_AZI,INDICATOR_M0,INDICATOR_INTEGTIME, INDICATOR_TARGET;
    public static JTextField INPUT_LONG_Is,INPUT_LAT_Is,INPUT_ALT_Is,INPUT_VEL_Is,INPUT_FPA_Is,INPUT_AZI_Is;   // Input vector inertial Frame / spherical coordinates -> Is
    public static JTextField INPUT_LONG_Rs,INPUT_LAT_Rs,INPUT_ALT_Rs,INPUT_VEL_Rs,INPUT_FPA_Rs,INPUT_AZI_Rs;   // Input vector rotating Frame / spherical coordinates -> Rs
    public static JTextField INPUT_TIME, INPUT_RB; 
    public static JTextField INPUT_M0, INPUT_WRITETIME,INPUT_ISP,INPUT_PROPMASS,INPUT_THRUSTMAX,INPUT_THRUSTMIN,p42_inp14,p42_inp15,p42_inp16,p42_inp17;
    public static JTextField INPUT_PGAIN,INPUT_IGAIN,INPUT_DGAIN,INPUT_CTRLMAX,INPUT_CTRLMIN,INPUT_REFELEV,INPUT_ISPMIN, INPUT_SURFACEAREA, INPUT_BALLISTICCOEFFICIENT;
    public static JLabel INDICATOR_VTOUCHDOWN ,INDICATOR_DELTAV, INDICATOR_PROPPERC, INDICATOR_RESPROP, Error_Indicator,Module_Indicator;
    public static JLabel LABEL_IntegratorSetting_01, LABEL_IntegratorSetting_02, LABEL_IntegratorSetting_03, LABEL_IntegratorSetting_04, LABEL_IntegratorSetting_05; 
    public static JTextField INPUT_IntegratorSetting_01, INPUT_IntegratorSetting_02, INPUT_IntegratorSetting_03, INPUT_IntegratorSetting_04, INPUT_IntegratorSetting_05;
    public static JRadioButton RB_SurfaceArea, RB_BallisticCoefficient;
    public static JRadioButton SELECT_VelocitySpherical, SELECT_VelocityCartesian;
    public static JRadioButton SELECT_3DOF,SELECT_6DOF;
    public static int vel_frame_hist = 1; 
    public static JTextField INPUT_IXX, INPUT_IXY, INPUT_IXZ, INPUT_IYX, INPUT_IYY, INPUT_IYZ, INPUT_IZX, INPUT_IZY, INPUT_IZZ;
    public static JTextField INPUT_Quarternion1, INPUT_Quarternion2, INPUT_Quarternion3, INPUT_Quarternion4, INPUT_Euler1, INPUT_Euler2,INPUT_Euler3;
    public static JTextField INPUT_AngularRate_X, INPUT_AngularRate_Y, INPUT_AngularRate_Z;
    public static JSlider sliderEuler1;
    public static JSlider sliderEuler2;
    public static JSlider sliderEuler3;
    public static JPanel SpaceShip3DControlPanel ;
    public static TimerTask task_Update;
    public static JTextField ConstantCD_INPUT;
    
    
    static DefaultTableModel MODEL_RAWData;
    static JTable TABLE_RAWData; 
    
    
	 static int c_SEQUENCE = 12;
	 static Object[] ROW_SEQUENCE = new Object[c_SEQUENCE];
	 static DefaultTableModel MODEL_SEQUENCE;
	 static JTable TABLE_SEQUENCE;
	 
	 static int c_CONTROLLER = 12;
	 static Object[] ROW_CONTROLLER = new Object[c_SEQUENCE];
	 static DefaultTableModel MODEL_CONTROLLER;
	 static JTable TABLE_CONTROLLER;
	 
	 static int c_ERROR = 5;
	 static Object[] ROW_ERROR = new Object[c_ERROR];
	 static DefaultTableModel MODEL_ERROR;
	 static JTable TABLE_ERROR; 
	 
	 static int c_EventHanlder = 2;
	 static Object[] ROW_EventHandler = new Object[c_EventHanlder];
	 static DefaultTableModel MODEL_EventHandler;
	 static JTable TABLE_EventHandler;
		@SuppressWarnings("rawtypes")
		public static JComboBox EventHandlerTypeCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceENDTypeCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceTypeCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceFCCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox FCTargetCurveCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceTVCFCCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox TVCFCTargetCurveCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox ErrorTypeCombobox= new JComboBox(); 
	    @SuppressWarnings("rawtypes")
		public static JComboBox Target_chooser, Integrator_chooser,TargetCurve_chooser,AscentDescent_SwitchChooser;
    Border Earth_border = BorderFactory.createLineBorder(Color.BLUE, 1);
    Border Moon_border 	= BorderFactory.createLineBorder(Color.GRAY, 1);
    Border Mars_border 	= BorderFactory.createLineBorder(Color.ORANGE, 1);
    Border Venus_border = BorderFactory.createLineBorder(Color.GREEN, 1);
    public static JCheckBox p421_linp0;
    private static List<atm_dataset> Page03_storage = new ArrayList<atm_dataset>(); // |1| time |2| altitude |3| velocity
    static XYSeriesCollection ResultSet_MercatorMap = new XYSeriesCollection();
    static XYSeriesCollection ResultSet_PolarMap = new XYSeriesCollection();
    	static int page1_plot_y =380;
    	@SuppressWarnings("rawtypes")
    	public static JComboBox axis_chooser, axis_chooser2,axis_chooser3,axis_chooser4; 
    	
    	public static int thirdWindowIndx = 1;
    	
    	
    	public static List<Component> AeroLeftBarAdditionalComponents = new ArrayList<Component>();
     	public static List<RealTimeResultSet> resultSet = new ArrayList<RealTimeResultSet>();
	//-----------------------------------------------------------------------------
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
     // ---------------------------------------------------------------------------------
    	 //       Define Task (FileWatcher) Update Result Overview
    	 // ---------------------------------------------------------------------------------
    	  task_Update = new FileWatcher( new File(RES_File) ) {
    		    protected void onChange( File file ) {
    		      // here we code the action on a change
    		     // System.out.println( "File "+ file.getName() +" have change !" );
          		  UPDATE_Page01(true);
          		  if(thirdWindowIndx==1) {
          		refreshTargetView3D();
          		  }
            		refreshSpaceCraftView();
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
        
      	BackgroundMenuBar menuBar = new BackgroundMenuBar();
        //menuBar.setLocation(0, 0);
        menuBar.setColor(new Color(250,250,250));
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(1200, 25));
        MainGUI.add(menuBar, BorderLayout.NORTH);

        JTabbedPane Page04_subtabPane = (JTabbedPane) new JTabbedPane();
        Page04_subtabPane.setPreferredSize(new Dimension(extx_main, exty_main));
        Page04_subtabPane.setBackground(backgroundColor);
        Page04_subtabPane.setForeground(Color.BLACK);
        
     	ImageIcon icon_BlueBook = null;
     	ImageIcon icon_windowSelect = null;
     	ImageIcon icon_visualEngine = null;
     	ImageIcon icon_preProcessing =null;
     	ImageIcon icon_postProcessing =null;
     	ImageIcon icon_simulation =null;
     	int sizeUpperBar=20;
     	try {
		icon_BlueBook = new ImageIcon("images/BB_icon2.png","");
		icon_windowSelect = new ImageIcon("images/windowSelect.png","");
		icon_visualEngine = new ImageIcon("images/visualEngine.png","");
		icon_preProcessing = new ImageIcon("images/preprocessingIcon.png","");
		icon_postProcessing = new ImageIcon("images/postprocessingIcon.png","");
		icon_simulation = new ImageIcon("images/simulationIcon.jpg","");
     	if(OS_is==1) {
        	 icon_BlueBook = new ImageIcon(getScaledImage(icon_BlueBook.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_windowSelect = new ImageIcon(getScaledImage(icon_windowSelect.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_visualEngine = new ImageIcon(getScaledImage(icon_visualEngine.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_simulation = new ImageIcon(getScaledImage(icon_simulation.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_preProcessing = new ImageIcon(getScaledImage(icon_preProcessing.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_postProcessing = new ImageIcon(getScaledImage(icon_postProcessing.getImage(),sizeUpperBar,sizeUpperBar));
     	} else if(OS_is==2) {
     	//	For Windows image icons have to be resized
        	 icon_BlueBook = new ImageIcon(getScaledImage(icon_BlueBook.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_windowSelect = new ImageIcon(getScaledImage(icon_windowSelect.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_visualEngine = new ImageIcon(getScaledImage(icon_visualEngine.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_simulation = new ImageIcon(getScaledImage(icon_simulation.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_preProcessing = new ImageIcon(getScaledImage(icon_preProcessing.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_postProcessing = new ImageIcon(getScaledImage(icon_postProcessing.getImage(),sizeUpperBar,sizeUpperBar));
     	}
     	} catch (Exception e) {
     		System.err.println("Error: Loading image icons failed");
     	}
        //Build the first menu.
     	JMenu menu_BlueBook = new JMenu("BlueBook");
     	menu_BlueBook.setOpaque(true);
     	menu_BlueBook.setBackground(labelColor);
        menu_BlueBook.setFont(small_font);
        menu_BlueBook.setMnemonic(KeyEvent.VK_A);
        menu_BlueBook.setIcon(icon_BlueBook);
        menuBar.add(menu_BlueBook);
        JMenuItem menuItem_OpenResultfile = new JMenuItem("Open Resultfile                 "); 
        menuItem_OpenResultfile.setForeground(Color.gray);
        menuItem_OpenResultfile.setFont(small_font);
        menuItem_OpenResultfile.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_OpenResultfile);
        menuItem_OpenResultfile.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                  		
                    } });
        menu_BlueBook.addSeparator();
        JMenuItem menuItem_Import = new JMenuItem("Settings                "); 
        menuItem_Import.setForeground(labelColor);
        menuItem_Import.setFont(small_font);
        menuItem_Import.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_Import);
        menuItem_Import.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   Thread thread = new Thread(new Runnable() {
                 		    public void run() {
                 		    		// Create new window here 
                 		    try {
                 		    			Settings.main();
  							} catch (IOException e) {
  								System.err.println("Error: Loaden Real Time Simulation Setup Window Failed");
  								e.printStackTrace();
  							};
                 		    }
                 		});
                 		thread.start();
                    } });
        JMenuItem menuItem_Export = new JMenuItem("Results save as                "); 
        menuItem_Export.setForeground(labelColor);
        menuItem_Export.setFont(small_font);
        menuItem_Export.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_Export);
        menuItem_Export.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                     	File myfile;
  	        			myfile = new File(dir+"/RESULTS");
  		            	JFileChooser fileChooser = new JFileChooser(myfile);
	  		           	if (fileChooser.showSaveDialog(menuItem_Export) == JFileChooser.APPROVE_OPTION) {}
	  	                File file = fileChooser.getSelectedFile() ;
	  	                String filePath = file.getAbsolutePath();
	  	                filePath = filePath.replaceAll(RESULT_FileEnding, "");
	  	                File source = new File(RES_File);
	  	                File dest = new File(filePath+RESULT_FileEnding);
                	   try {
                	       FileUtils.copyFile(source, dest);
                	   } catch (IOException eIO) {System.out.println(eIO);}
                	   System.out.println("Result file "+file.getName()+" saved.");
                    } });
        menu_BlueBook.addSeparator();
        JMenuItem menuItem_Exit = new JMenuItem("Exit                  "); 
        menuItem_Exit.setForeground(labelColor);
        menuItem_Exit.setFont(small_font);
        menuItem_Exit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_Exit);
        menuItem_Exit.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   MAIN_frame.dispose();
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_SIM = new JMenu("Simulation");
        menu_SIM.setForeground(labelColor);
        menu_SIM.setBackground(backgroundColor);
        menu_SIM.setFont(small_font);
        menu_SIM.setIcon(icon_simulation);
        menu_SIM.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_SIM);
        JMenuItem menuItem_SimSettings = new JMenuItem("Run Simulation                 "); 
        menuItem_SimSettings.setForeground(labelColor);
        menuItem_SimSettings.setFont(small_font);
        menuItem_SimSettings.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, ActionEvent.ALT_MASK));
        menu_SIM.add(menuItem_SimSettings);
        menuItem_SimSettings.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
             		  System.out.println("Action: RUN SIMULATION");
      				try {
      					String line;
      					Process proc = Runtime.getRuntime().exec("java -jar SIM.jar");
      					InputStream in = proc.getInputStream();
      					InputStream err = proc.getErrorStream();
      					System.out.println(in);
      					System.out.println(err);
      					 BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      					  while ((line = input.readLine()) != null) {
      					    System.out.println(line);
      					  }
      					  //UPDATE_Page01();
      				} catch ( IOException e1) {
      					// TODO Auto-generated catch block
      					e1.printStackTrace();
      					System.out.println("Error:  " + e1);
      				} 
                    } });
        JMenuItem menuItem_Update = new JMenuItem("Update Data                 "); 
        menuItem_Update.setForeground(labelColor);
        menuItem_Update.setFont(small_font);
        menuItem_Update.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_U, ActionEvent.ALT_MASK));
        menu_SIM.add(menuItem_Update);
        menuItem_Update.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   UPDATE_Page01(true);
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_PreProcessing = new JMenu("PreProcessing");
        menu_PreProcessing.setForeground(labelColor);
        menu_PreProcessing.setBackground(backgroundColor);
        menu_PreProcessing.setFont(small_font);
        menu_PreProcessing.setIcon(icon_preProcessing);
        menu_PreProcessing.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_PreProcessing);
        
        JMenuItem menuItem_ImportScenario = new JMenuItem("Simulation Setup Open               "); 
        menuItem_ImportScenario.setForeground(labelColor);
        menuItem_ImportScenario.setFont(small_font);
        menuItem_ImportScenario.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_ImportScenario);
        menuItem_ImportScenario.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                      	File myfile;
   	        			myfile = new File(dir+"/CASES");
   		            	JFileChooser fileChooser = new JFileChooser(myfile);
   		           	if (fileChooser.showOpenDialog(menuItem_Export) == JFileChooser.APPROVE_OPTION) {}
   	                File file = fileChooser.getSelectedFile() ;
   	                String filePath = file.getAbsolutePath();
   	                filePath = filePath.replaceAll(CASE_FileEnding, "");
                       file = new File(filePath + CASE_FileEnding);
               		   CurrentWorkfile_Path = file;
                       CurrentWorkfile_Name = fileChooser.getSelectedFile().getName();
                       MAIN_frame.setTitle("" + PROJECT_TITLE + " | " + CurrentWorkfile_Name.split("[.]")[0]);
                       try {IMPORT_Case();} catch (IOException e1) {System.out.println(e1);}
   					System.out.println("File "+CurrentWorkfile_Name+" opened.");

                	   Page04_subtabPane.setSelectedIndex(1);
                    } });
        JMenuItem menuItem_ExportScenario = new JMenuItem("Simulation Setup Save as              "); 
        menuItem_ExportScenario.setForeground(labelColor);
        menuItem_ExportScenario.setFont(small_font);
        menuItem_ExportScenario.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_ExportScenario);
        menuItem_ExportScenario.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {                	   
                   	File myfile;
	        			myfile = new File(dir+"/CASES");
		            	JFileChooser fileChooser = new JFileChooser(myfile);
		           	if (fileChooser.showSaveDialog(menuItem_Export) == JFileChooser.APPROVE_OPTION) {}
	                File file = fileChooser.getSelectedFile() ;
	                String filePath = file.getAbsolutePath();
	                filePath = filePath.replaceAll(CASE_FileEnding, "");
                    file = new File(filePath + CASE_FileEnding);
            		    CurrentWorkfile_Path = file;
                    CurrentWorkfile_Name = fileChooser.getSelectedFile().getName();
                    MAIN_frame.setTitle("" + PROJECT_TITLE + " | " + CurrentWorkfile_Name.split("[.]")[0]);
						EXPORT_Case();
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_PostProcessing = new JMenu("PostProcessing");
        menu_PostProcessing.setForeground(labelColor);
        menu_PostProcessing.setBackground(backgroundColor);
        menu_PostProcessing.setFont(small_font);
        menu_PostProcessing.setIcon(icon_postProcessing);
        menu_PostProcessing.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_PostProcessing);
        
        JMenuItem menuItem_CreateLocalElevation = new JMenuItem("Create Custom Data Plot               "); 
        menuItem_CreateLocalElevation.setForeground(labelColor);
        menuItem_CreateLocalElevation.setFont(small_font);
        menuItem_CreateLocalElevation.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PostProcessing.add(menuItem_CreateLocalElevation);
        menuItem_CreateLocalElevation.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                	   Thread thread = new Thread(new Runnable() {
               		    public void run() {
               		    		// Create new window here 
               		    try {
               		    			CreateCustomChart.main();
							} catch (IOException e) {
								System.err.println("Error: Loaden Real Time Simulation Setup Window Failed");
								e.printStackTrace();
							};
               		    }
               		});
               		thread.start();
               		
                    } });
      //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_VisualEngine = new JMenu("Visual Engine");
        menu_VisualEngine.setForeground(labelColor);
        menu_VisualEngine.setBackground(backgroundColor);
        menu_VisualEngine.setFont(small_font);
        menu_VisualEngine.setIcon(icon_visualEngine);
        menu_VisualEngine.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_VisualEngine);
        
        JMenuItem menuItem_Open = new JMenuItem("Open VisualEngine                 "); 
        menuItem_Open.setForeground(Color.gray);
        menuItem_Open.setFont(small_font);
        menuItem_Open.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_VisualEngine.add(menuItem_Open);
        menuItem_Open.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	  /*
                	   Thread thread = new Thread(new Runnable() {
                		    public void run() {
                		    	 worldGenerator.launchVisualEngine();
                		    }
                		});
                		thread.start();
                		*/
                    } });
        
        JMenuItem menuItem_Animation = new JMenuItem("Create Animation from Results         "); 
        menuItem_Animation.setForeground(Color.gray);
        menuItem_Animation.setFont(small_font);
        menuItem_Animation.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_VisualEngine.add(menuItem_Animation);
        menuItem_Animation.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	  /*
                	   Thread thread = new Thread(new Runnable() {
                		    public void run() {
                		    	List<AnimationSet> animationSets = READ_AnimationData();
                		    	worldAnimation.launchVisualEngine(animationSets);
                		    }
                		});
                		thread.start();
                		*/
                    } });
        
        JMenuItem menuItem_RealTime = new JMenuItem("Open Real Time Simulation Demo     "); 
        menuItem_RealTime.setForeground(labelColor);
        menuItem_RealTime.setFont(small_font);
        menuItem_RealTime.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_VisualEngine.add(menuItem_RealTime);
        menuItem_RealTime.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	  
                	   Thread thread = new Thread(new Runnable() {
                		    public void run() {
                		    		// Create new window here 
                  				try {
                  					String line;
                  					Process proc = null;
                  					if(OS_is==1) {
                  						 proc = Runtime.getRuntime().exec("java -jar FlyMeToTheMoon_OSX.jar");
                  					} else if (OS_is==2){
                  						 proc = Runtime.getRuntime().exec("java -jar FlyMeToTheMoon_WIN.jar");
                  					}
                  					InputStream in = proc.getInputStream();
                  					InputStream err = proc.getErrorStream();
                  					System.out.println(in);
                  					System.out.println(err);
                  					 BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                  					  while ((line = input.readLine()) != null) {
                  					    System.out.println(line);
                  					  }
                  					  //UPDATE_Page01();
                  				} catch ( IOException e1) {
                  					// TODO Auto-generated catch block
                  					e1.printStackTrace();
                  					System.out.println("Error:  " + e1);
                  				} 
                		    }
                		});
                		thread.start();
                    } });

        JMenu menuItem_AddSpacecraft = new JMenu("Add Spacecraft                ");
        menuItem_AddSpacecraft.setForeground(Color.gray);
        //menuItem_AddSpacecraft.setBackground(backgroundColor);
        menuItem_AddSpacecraft.setFont(small_font);
        menuItem_AddSpacecraft.setMnemonic(KeyEvent.VK_A);
        menu_VisualEngine.add(menuItem_AddSpacecraft);
        ButtonGroup group_sc = new ButtonGroup();

        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem("Gemini");
        menuItem.setForeground(labelColor);
        menuItem.setFont(small_font);
        menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   worldGenerator.addEntity("gemini", "gray");
                    } });
        group_sc.add(menuItem);
        menuItem_AddSpacecraft.add(menuItem);

        menuItem = new JRadioButtonMenuItem("Mars Global Surveyor");
        menuItem.setForeground(labelColor);
        menuItem.setFont(small_font);
        menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   worldGenerator.addEntity("MGS", "gray");
                    } });
        group_sc.add(menuItem);
        menuItem_AddSpacecraft.add(menuItem);
        
        JMenu menuItem_setEnvironment = new JMenu("Set Environment               ");
        menuItem_setEnvironment.setForeground(Color.gray);
        //menuItem_setEnvironment.setBackground(backgroundColor);
        menuItem_setEnvironment.setFont(small_font);
        menuItem_setEnvironment.setMnemonic(KeyEvent.VK_A);
        menu_VisualEngine.add(menuItem_setEnvironment);
        ButtonGroup group_env = new ButtonGroup();
        
        menuItem = new JRadioButtonMenuItem("Space");
        menuItem.setForeground(labelColor);
        menuItem.setFont(small_font);
        menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                    } });
        group_env.add(menuItem);
        menuItem_setEnvironment.add(menuItem);

         menuItem = new JRadioButtonMenuItem("Earth");
         menuItem.setForeground(labelColor);
         menuItem.setFont(small_font);
         menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                 	   worldGenerator.addEntity("MGS", "gray");
                     } });
         group_env.add(menuItem);
        
        menuItem_setEnvironment.add(menuItem);
         menuItem = new JRadioButtonMenuItem("Moon");
         menuItem.setForeground(labelColor);
         menuItem.setFont(small_font);
         menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                 	   worldGenerator.addEntity("MGS", "gray");
                     } });
         group_env.add(menuItem);
        menuItem_setEnvironment.add(menuItem);
   //-------------------------------------------------------------------------     
        JMenu menu_Window = new JMenu();
        menu_Window.setText("Window");
       // menu_Window.setForeground(labelColor);
        //menu_Window.setBackground(backgroundColor);
        menu_Window.setForeground(Color.black);
       //menu_Window.setColor(labelColor);
        menu_Window.setFont(small_font);
        menu_Window.setIcon(icon_windowSelect);
        menu_Window.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_Window);
        
        JMenu menu_ThirdWindow = new JMenu("Set third window content");
        //menu_ThirdWindow.setForeground(labelColor);
        //menu_ThirdWindow.setBackground(backgroundColor);
        menu_ThirdWindow.setFont(small_font);
        menu_ThirdWindow.setMnemonic(KeyEvent.VK_A);
        menu_Window.add(menu_ThirdWindow);
        
        ButtonGroup thirdWindow = new ButtonGroup();
        
        menuItem = new JRadioButtonMenuItem("Flight Path Angle");
       //menuItem.setForeground(labelColor);
        menuItem.setFont(small_font);
        menuItem.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   try {
					   CreateChart_DashboardOverviewChart_Time_FPA();
					   SplitPane_Page1_Charts_vertical.setDividerLocation(500);
					   thirdWindowIndx=0;
					} catch (IOException e1) {
					     System.err.println("Error: Thrid window could not be creaeted");
					}
                    } });
        thirdWindow.add(menuItem);
        menu_ThirdWindow.add(menuItem);

         menuItem = new JRadioButtonMenuItem("3D Trajectory View");
        // menuItem.setForeground(labelColor);
         menuItem.setFont(small_font);
         menuItem.setSelected(true);
         menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

							createTargetView3D();
						SplitPane_Page1_Charts_vertical.setDividerLocation(500);
						thirdWindowIndx=1;
                    	       
                     } });
         thirdWindow.add(menuItem);
         menu_ThirdWindow.add(menuItem);
         
        JMenuItem menuItemSelect3D = new JMenuItem("Select 3D SpaceShip");
       // menuItemSelect3D.setForeground(labelColor);
         menuItemSelect3D.setFont(small_font);
         menuItemSelect3D.setForeground(Color.black);
         //menuItemSelect3D.setSelected(true);
         menuItemSelect3D.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	// Refresh Object file path
                    	// refresh SpaceShipView3D
                    	// refresh SpaceShipView3dFrontPage
                       	File myfile;
                		myfile = new File(System.getProperty("user.dir")+"/INP/SpacecraftModelLibrary/");
                    	JFileChooser fileChooser = new JFileChooser(myfile);
                   	if (fileChooser.showOpenDialog(menuItemSelect3D) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile() ;
                        String filePath = file.getAbsolutePath();
                        SpaceShipView3DFrontPage.setModelObjectPath(filePath);
                        refreshSpaceCraftView() ;
                   	}
                    	       
                     } });
         menu_Window.add(menuItemSelect3D);
         
      //--------------------------------------------------------------------------------------------------------------------------------
        PageX04_Dashboard = new JPanel();
        PageX04_Dashboard.setLocation(0, 0);
        PageX04_Dashboard.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_Dashboard.setLayout(new BorderLayout());
        PageX04_Dashboard.setBackground(backgroundColor);
        PageX04_Dashboard.setForeground(labelColor);
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
        PageX04_GroundClearance = new JPanel();
        PageX04_GroundClearance.setLocation(0, 0);
        PageX04_GroundClearance.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_GroundClearance.setLayout(new BorderLayout());
        PageX04_GroundClearance.setBackground(backgroundColor);
        PageX04_GroundClearance.setForeground(labelColor);
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

        // Page 4.1
        P1_SidePanel = new JPanel();
        P1_SidePanel.setLayout(null);
        P1_SidePanel.setPreferredSize(new Dimension(385, exty_main));
        P1_SidePanel.setBackground(backgroundColor);
        P1_SidePanel.setForeground(labelColor);
        PageX04_Dashboard.add(P1_SidePanel, BorderLayout.LINE_START);
        
        P1_Plotpanel = new JPanel();
        P1_Plotpanel.setLayout(new BorderLayout());
        P1_Plotpanel.setPreferredSize(new Dimension(900, exty_main-100));
        P1_Plotpanel.setBackground(backgroundColor);
        P1_Plotpanel.setForeground(Color.white);
        PageX04_Dashboard.add(P1_Plotpanel,BorderLayout.LINE_END);
        
        SplitPane_Page1_Charts_horizontal = new JSplitPane();
        SplitPane_Page1_Charts_horizontal.setOrientation(JSplitPane.VERTICAL_SPLIT );
        SplitPane_Page1_Charts_horizontal.setDividerLocation(0.35);
        SplitPane_Page1_Charts_horizontal.setDividerSize(3);
        SplitPane_Page1_Charts_horizontal.setUI(new BasicSplitPaneUI() {
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

        SplitPane_Page1_Charts_horizontal.addComponentListener(new ComponentListener(){

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
        SplitPane_Page1_Charts_horizontal.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, 
    		    new PropertyChangeListener() {
    		        @Override
    		        public void propertyChange(PropertyChangeEvent pce) {

    		        }
    		});
        SplitPane_Page1_Charts_horizontal.setDividerLocation(400);
       	P1_Plotpanel.add(SplitPane_Page1_Charts_horizontal, BorderLayout.CENTER);
        
       	
       	
        SplitPane_Page1_Charts_vertical = new JSplitPane();
       	//SplitPane_Page1_Charts.setPreferredSize(new Dimension(1000, 1000));
        SplitPane_Page1_Charts_vertical.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
       //	SplitPane_Page1_Charts.setForeground(labelColor);
       //	SplitPane_Page1_Charts.setBackground(Color.gray);
        SplitPane_Page1_Charts_vertical.setDividerSize(3);
        SplitPane_Page1_Charts_vertical.setUI(new BasicSplitPaneUI() {
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
        SplitPane_Page1_Charts_vertical.setDividerLocation(500);
        SplitPane_Page1_Charts_horizontal.add(SplitPane_Page1_Charts_vertical, JSplitPane.TOP);
        
        SplitPane_Page1_Charts_vertical2 = new JSplitPane();
       	//SplitPane_Page1_Charts.setPreferredSize(new Dimension(1000, 1000));
        SplitPane_Page1_Charts_vertical2.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
       //	SplitPane_Page1_Charts.setForeground(labelColor);
       //	SplitPane_Page1_Charts.setBackground(Color.gray);
        SplitPane_Page1_Charts_vertical2.setDividerSize(3);
        SplitPane_Page1_Charts_vertical2.setUI(new BasicSplitPaneUI() {
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
        SplitPane_Page1_Charts_vertical2.setDividerLocation(500);
        SplitPane_Page1_Charts_horizontal.add(SplitPane_Page1_Charts_vertical2, JSplitPane.BOTTOM);
       	
	    SpaceShip3DControlPanel = new JPanel();
		SpaceShip3DControlPanel.setLayout(new BorderLayout());
		//SpaceShip3DControlPanel.setSize(450, 400);
		SplitPane_Page1_Charts_vertical.add(SpaceShip3DControlPanel, JSplitPane.RIGHT);
		
	    JPanel TargetViewControlPanel = new JPanel();
	    TargetViewControlPanel.setLayout(new BorderLayout());
	    TargetViewControlPanel.setPreferredSize(new Dimension(450, 25));
	    TargetViewControlPanel.setBackground(backgroundColor);
	    SpaceShip3DControlPanel.add(TargetViewControlPanel, BorderLayout.PAGE_END);
	    
     	ImageIcon iconPlayPause =null;
     	 sizeUpperBar=25;
     	try {
     		iconPlayPause = new ImageIcon("images/playPauseIcon.png","");
     		iconPlayPause = new ImageIcon(getScaledImage(iconPlayPause.getImage(),sizeUpperBar,sizeUpperBar));
     	} catch (Exception e) {
     		System.err.println("Error: Dashboard control panel icons could not be loaded."); 
     	}
	    
        JButton ButtonTargetViewControlPlay = new JButton("");
        ButtonTargetViewControlPlay.setLocation(100, 0);
        ButtonTargetViewControlPlay.setSize(45,25);
        ButtonTargetViewControlPlay.setBackground(backgroundColor);
        ButtonTargetViewControlPlay.setOpaque(true);
        ButtonTargetViewControlPlay.setBorderPainted(false);
        ButtonTargetViewControlPlay.setIcon(iconPlayPause);
        ButtonTargetViewControlPlay.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
	                Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	                    			TargetView3D.playPauseAnimation();
	                    }
	                    });
        	}} );
	    TargetViewControlPanel.add(ButtonTargetViewControlPlay, BorderLayout.CENTER);
	    
	    JSlider SpeedSliderTargetViewControl = GuiComponents.getGuiSliderSpeed(small_font, 100, 0, 10, 40, labelColor, backgroundColor);
	    SpeedSliderTargetViewControl.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				double rotspeed = ((double) (SpeedSliderTargetViewControl.getValue()))/100;
				TargetView3D.targetBodyRotSpeed = rotspeed;
			}
	    	
	    });
	    TargetViewControlPanel.add(SpeedSliderTargetViewControl, BorderLayout.EAST);
	    
	    
		
        JScrollPane scrollPane_P1 = new JScrollPane(P1_SidePanel);
        scrollPane_P1.setPreferredSize(new Dimension(415, exty_main));
        scrollPane_P1.getVerticalScrollBar().setUnitIncrement(16);
        PageX04_Dashboard.add(scrollPane_P1, BorderLayout.LINE_START);
        JScrollPane scrollPane1_P1 = new JScrollPane(P1_Plotpanel);
        scrollPane1_P1.getVerticalScrollBar().setUnitIncrement(16);
        PageX04_Dashboard.add(scrollPane1_P1, BorderLayout.CENTER);
        
        int uy_p41 = 10 ; 
        int y_ext_vel = 10; 
      
        JLabel LABEL_LONG = new JLabel(" Longitude [deg]");
        LABEL_LONG.setLocation(65, uy_p41 + 0 );
        LABEL_LONG.setSize(150, 20);
        LABEL_LONG.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_LONG.setBorder(Moon_border);
        LABEL_LONG.setBackground(backgroundColor);
        LABEL_LONG.setForeground(labelColor);
        P1_SidePanel.add(LABEL_LONG);
        JLabel LABEL_LAT = new JLabel(" Latitude [deg]");
        LABEL_LAT.setLocation(65, uy_p41 + 25 );
        LABEL_LAT.setSize(150, 20);
        LABEL_LAT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_LAT.setBorder(Moon_border);
        LABEL_LAT.setBackground(backgroundColor);
        LABEL_LAT.setForeground(labelColor);
        P1_SidePanel.add(LABEL_LAT);
        JLabel LABEL_ALT = new JLabel(" Altitude [m]");
        LABEL_ALT.setLocation(65, uy_p41 + 50 );
        LABEL_ALT.setSize(150, 20);
        LABEL_ALT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_ALT.setBorder(Moon_border);
        LABEL_ALT.setBackground(backgroundColor);
        LABEL_ALT.setForeground(labelColor);
        P1_SidePanel.add(LABEL_ALT);
        
        
        JLabel LABEL_VEL = new JLabel(" Velocity [m/s]");
        LABEL_VEL.setLocation(65, uy_p41 + 75 + y_ext_vel);
        LABEL_VEL.setSize(150, 20);
        LABEL_VEL.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_VEL.setBorder(Moon_border);
        LABEL_VEL.setBackground(backgroundColor);
        LABEL_VEL.setForeground(labelColor);
        P1_SidePanel.add(LABEL_VEL);
        JLabel LABEL_FPA = new JLabel(" Flight Path Angle [deg]");
        LABEL_FPA.setLocation(65, uy_p41 + 100 + y_ext_vel);
        LABEL_FPA.setSize(150, 20);
        LABEL_FPA.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_FPA.setBorder(Moon_border);
        LABEL_FPA.setBackground(backgroundColor);
        LABEL_FPA.setForeground(labelColor);
        P1_SidePanel.add(LABEL_FPA);
        JLabel LABEL_AZI = new JLabel(" Azimuth [deg]");
        LABEL_AZI.setLocation(65, uy_p41 + 125 + y_ext_vel);
        LABEL_AZI.setSize(150, 20);
        LABEL_AZI.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_AZI.setBorder(Moon_border);
        LABEL_AZI.setBackground(backgroundColor);
        LABEL_AZI.setForeground(labelColor);
        P1_SidePanel.add(LABEL_AZI);
        
        
        JLabel LABEL_M0 = new JLabel(" Initial Mass [kg]");
        LABEL_M0.setLocation(65, uy_p41 + 150 + y_ext_vel*2);
        LABEL_M0.setSize(150, 20);
        LABEL_M0.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_M0.setBorder(Moon_border);
        LABEL_M0.setBackground(backgroundColor);
        LABEL_M0.setForeground(labelColor);
        P1_SidePanel.add(LABEL_M0);
        JLabel LABEL_INTEGTIME = new JLabel(" Integration Time [s]");
        LABEL_INTEGTIME.setLocation(65, uy_p41 + 175 + y_ext_vel*2);
        LABEL_INTEGTIME.setSize(150, 20);
        LABEL_INTEGTIME.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_INTEGTIME.setBorder(Moon_border);
        LABEL_INTEGTIME.setBackground(backgroundColor);
        LABEL_INTEGTIME.setForeground(labelColor);
        P1_SidePanel.add(LABEL_INTEGTIME);
        
        Error_Indicator = new JLabel("");
        Error_Indicator.setLocation(225, uy_p41 + 25 * 9);
        Error_Indicator.setSize(150, 20);
        //Error_Indicator.setBackground(backgroundColor);
        Error_Indicator.setFont(labelfont_small);
        Error_Indicator.setForeground(labelColor);
        P1_SidePanel.add(Error_Indicator);
        
        Module_Indicator = new JLabel("");
        Module_Indicator.setLocation(225, uy_p41 + 25 * 10);
        Module_Indicator.setSize(150, 20);
        Module_Indicator.setFont(labelfont_small);
        Module_Indicator.setForeground(labelColor);
        P1_SidePanel.add(Module_Indicator);
        
         INDICATOR_LONG = new JLabel();
        INDICATOR_LONG.setLocation(2, uy_p41 + 25 * 0 );
        INDICATOR_LONG.setSize(60, 20);
        INDICATOR_LONG.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        INDICATOR_LONG.setBorder(Moon_border);
        INDICATOR_LONG.setBackground(backgroundColor);
        INDICATOR_LONG.setForeground(labelColor);
        INDICATOR_LONG.setFont(small_font);
        P1_SidePanel.add(INDICATOR_LONG);
        INDICATOR_LAT = new JLabel();
        INDICATOR_LAT.setLocation(2, uy_p41 + 25 * 1 );
        INDICATOR_LAT.setSize(60, 20);
        INDICATOR_LAT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        INDICATOR_LAT.setBorder(Moon_border);
        INDICATOR_LAT.setBackground(backgroundColor);
        INDICATOR_LAT.setFont(small_font);
        INDICATOR_LAT.setForeground(labelColor);
        P1_SidePanel.add(INDICATOR_LAT);
         INDICATOR_ALT = new JLabel();
        INDICATOR_ALT.setLocation(2, uy_p41 + 25 * 2 );
        INDICATOR_ALT.setSize(60, 20);
        INDICATOR_ALT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        INDICATOR_ALT.setBorder(Moon_border);
        INDICATOR_ALT.setBackground(backgroundColor);
        INDICATOR_ALT.setFont(small_font);
        INDICATOR_ALT.setForeground(labelColor);
        P1_SidePanel.add(INDICATOR_ALT);
        INDICATOR_VEL = new JLabel();
        INDICATOR_VEL.setLocation(2, uy_p41 + 25 * 3 + y_ext_vel);
        INDICATOR_VEL.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        INDICATOR_VEL.setBorder(Moon_border);
        INDICATOR_VEL.setBackground(backgroundColor);
        INDICATOR_VEL.setFont(small_font);
        INDICATOR_VEL.setForeground(labelColor);
        INDICATOR_VEL.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_VEL);
        INDICATOR_FPA = new JLabel();
        INDICATOR_FPA.setLocation(2, uy_p41 + 25 * 4 + y_ext_vel);
        INDICATOR_FPA.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        INDICATOR_FPA.setBorder(Moon_border);
        INDICATOR_FPA.setBackground(backgroundColor);
        INDICATOR_FPA.setFont(small_font);
        INDICATOR_FPA.setForeground(labelColor);
        INDICATOR_FPA.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_FPA);
        INDICATOR_AZI = new JLabel();
        INDICATOR_AZI.setLocation(2, uy_p41 + 25 * 5 + y_ext_vel);
        INDICATOR_AZI.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        INDICATOR_AZI.setBorder(Moon_border);
        INDICATOR_AZI.setBackground(backgroundColor);
        INDICATOR_AZI.setForeground(labelColor);
        INDICATOR_AZI.setFont(small_font);
        INDICATOR_AZI.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_AZI);        
        INDICATOR_M0 = new JLabel();
        INDICATOR_M0.setLocation(2, uy_p41 + 25 * 6 + y_ext_vel*2);
        INDICATOR_M0.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        INDICATOR_M0.setBorder(Moon_border);
        INDICATOR_M0.setBackground(backgroundColor);
        INDICATOR_M0.setForeground(labelColor);
        INDICATOR_M0.setFont(small_font);
        INDICATOR_M0.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_M0);
        INDICATOR_INTEGTIME = new JLabel();
        INDICATOR_INTEGTIME.setLocation(2, uy_p41 + 25 * 7 + y_ext_vel*2);
        INDICATOR_INTEGTIME.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        INDICATOR_INTEGTIME.setBorder(Moon_border);
        INDICATOR_INTEGTIME.setBackground(backgroundColor);
        INDICATOR_INTEGTIME.setForeground(labelColor);
        INDICATOR_INTEGTIME.setFont(small_font);
        INDICATOR_INTEGTIME.setSize(60, 20);
       P1_SidePanel.add(INDICATOR_INTEGTIME);
       
       
       
       
       JLabel LABEL_TARGET = new JLabel("Target Body:");
       LABEL_TARGET.setLocation(5, uy_p41 + 25 * 9  );
       LABEL_TARGET.setSize(250, 20);
       LABEL_TARGET.setBackground(backgroundColor);
       LABEL_TARGET.setForeground(labelColor);
       P1_SidePanel.add(LABEL_TARGET);
       INDICATOR_TARGET = new JLabel();
       INDICATOR_TARGET.setLocation(2, uy_p41 + 25 * 10 );
       INDICATOR_TARGET.setText("");
       INDICATOR_TARGET.setSize(100, 25);
       INDICATOR_TARGET.setBackground(backgroundColor);
       INDICATOR_TARGET.setForeground(labelColor);
       INDICATOR_TARGET.setHorizontalAlignment(SwingConstants.CENTER);
       INDICATOR_TARGET.setVerticalTextPosition(JLabel.CENTER);
       INDICATOR_TARGET.setFont(targetfont);
       INDICATOR_TARGET.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
      P1_SidePanel.add(INDICATOR_TARGET);
        
      
      JLabel LABEL_VTOUCHDOWN = new JLabel("  Touchdown velocity [m/s]");
      LABEL_VTOUCHDOWN.setLocation(55, uy_p41 + 285  + 25 *0 );
      LABEL_VTOUCHDOWN.setSize(200, 20);
      LABEL_VTOUCHDOWN.setBackground(Color.black);
      LABEL_VTOUCHDOWN.setForeground(labelColor);
      LABEL_VTOUCHDOWN.setFont(small_font);
      P1_SidePanel.add(LABEL_VTOUCHDOWN);
      JLabel LABEL_DELTAV = new JLabel("  Total D-V [m/s]");
      LABEL_DELTAV.setLocation(55, uy_p41 + 285 + 25 *1 );
      LABEL_DELTAV.setSize(200, 20);
      LABEL_DELTAV.setBackground(Color.black);
      LABEL_DELTAV.setForeground(labelColor);
      LABEL_DELTAV.setFont(small_font);
      P1_SidePanel.add(LABEL_DELTAV);
      JLabel LABEL_PROPPERC = new JLabel("  Used Propellant [kg]");
      LABEL_PROPPERC.setLocation(270, uy_p41 + 285 + 25 *0 );
      LABEL_PROPPERC.setSize(200, 20);
      LABEL_PROPPERC.setBackground(Color.black);
      LABEL_PROPPERC.setForeground(labelColor);
      LABEL_PROPPERC.setFont(small_font);
      P1_SidePanel.add(LABEL_PROPPERC);
      JLabel LABEL_RESPROP = new JLabel("  Residual Propellant [%]");
      LABEL_RESPROP.setLocation(260, uy_p41 + 285 + 25 *1 );
      LABEL_RESPROP.setSize(200, 20);
      LABEL_RESPROP.setBackground(Color.black);
      LABEL_RESPROP.setForeground(labelColor);
      LABEL_RESPROP.setFont(small_font);
      P1_SidePanel.add(LABEL_RESPROP);
      
       INDICATOR_VTOUCHDOWN = new JLabel("");
      INDICATOR_VTOUCHDOWN.setLocation(5, uy_p41 + 285  + 25 *0 );
      INDICATOR_VTOUCHDOWN.setSize(50, 20);
      INDICATOR_VTOUCHDOWN.setBackground(Color.black);
      INDICATOR_VTOUCHDOWN.setForeground(labelColor);
      P1_SidePanel.add(INDICATOR_VTOUCHDOWN);
       INDICATOR_DELTAV = new JLabel("");
      INDICATOR_DELTAV.setLocation(5, uy_p41 + 285 + 25 *1 );
      INDICATOR_DELTAV.setSize(50, 20);
      INDICATOR_DELTAV.setBackground(Color.black);
      INDICATOR_DELTAV.setForeground(labelColor);
      P1_SidePanel.add(INDICATOR_DELTAV);
       INDICATOR_PROPPERC = new JLabel("");
      INDICATOR_PROPPERC.setLocation(225, uy_p41 + 285 + 25 *0 );
      INDICATOR_PROPPERC.setSize(50, 20);
      INDICATOR_PROPPERC.setBackground(Color.black);
      INDICATOR_PROPPERC.setForeground(labelColor);
      P1_SidePanel.add(INDICATOR_PROPPERC);
       INDICATOR_RESPROP = new JLabel("");
      INDICATOR_RESPROP.setLocation(225, uy_p41 + 285 + 25 *1 );
      INDICATOR_RESPROP.setSize(40, 20);
      INDICATOR_RESPROP.setBackground(Color.black);
      INDICATOR_RESPROP.setForeground(labelColor);
      P1_SidePanel.add(INDICATOR_RESPROP);

        JButton Button_RunSimulation = new JButton("Run Simulation");
        Button_RunSimulation.setLocation(240, uy_p41 + 25 * 0);
        Button_RunSimulation.setSize(145,25);
        Button_RunSimulation.setBackground(labelColor);
        Button_RunSimulation.setForeground(backgroundColor);
        Button_RunSimulation.setOpaque(true);
        Button_RunSimulation.setBorderPainted(false);
        Button_RunSimulation.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
        		  System.out.println("Action: RUN SIMULATION");
				try {
					String line;
					Process proc = Runtime.getRuntime().exec("java -jar SIM.jar");
					InputStream in = proc.getInputStream();
					InputStream err = proc.getErrorStream();
					System.out.println(in);
					System.out.println(err);
					 BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
					  while ((line = input.readLine()) != null) {
					    System.out.println(line);
					  }
					  //UPDATE_Page01();
				} catch ( IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("Error:  " + e1);
				} 
        	}} );
        P1_SidePanel.add(Button_RunSimulation);
 
//-----------------------------------------------------------------------------------------------
//								Console Window        
//-----------------------------------------------------------------------------------------------
        int console_size_x = 390;
        int console_size_y = 270; 
        JLabel LABEL_CONSOLE = new JLabel("Console:");
        LABEL_CONSOLE.setLocation(5, uy_p41 + 25 *17 );
        LABEL_CONSOLE.setSize(200, 20);
        LABEL_CONSOLE.setBackground(backgroundColor);
        LABEL_CONSOLE.setForeground(labelColor);
        P1_SidePanel.add(LABEL_CONSOLE);
        
        JPanel JP_EnginModel = new JPanel();
        JP_EnginModel.setSize(console_size_x,console_size_y);
        JP_EnginModel.setLocation(5, uy_p41 + 25 * 18);
        JP_EnginModel.setBackground(backgroundColor);
        JP_EnginModel.setForeground(labelColor);
         JP_EnginModel.setBorder(BorderFactory.createEmptyBorder(1,1,1,1)); 
        //JP_EnginModel.setBackground(Color.red);
        taOutputStream = null; 
        taOutputStream = new TextAreaOutputStream(textArea, ""); 
        textArea.setForeground(labelColor);
        textArea.setBackground(backgroundColor);
        JScrollPane JSP_EnginModel = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JSP_EnginModel.setBackground(backgroundColor);
        JSP_EnginModel.setForeground(labelColor);
        JSP_EnginModel.getVerticalScrollBar().setForeground(labelColor);
        JSP_EnginModel.getHorizontalScrollBar().setForeground(labelColor);
        JSP_EnginModel.getHorizontalScrollBar().setBackground(backgroundColor);
        JSP_EnginModel.getVerticalScrollBar().setBackground(backgroundColor);
        JSP_EnginModel.setOpaque(true);
        JSP_EnginModel.setPreferredSize(new Dimension(console_size_x-5,console_size_y-10));
        JSP_EnginModel.setLocation(5, 5);
        JP_EnginModel.add(JSP_EnginModel);
        System.setOut(new PrintStream(taOutputStream));       
//-----------------------------------------------------------------------------------------------
        P1_SidePanel.add(JP_EnginModel);
//-----------------------------------------------------------------------------------------------

      JLabel LABEL_XAxis = new JLabel("X-Axis");
      LABEL_XAxis.setLocation(5, uy_p41 + 25 * 14 );
      LABEL_XAxis.setSize(150, 20);
      LABEL_XAxis.setHorizontalAlignment(0);
      LABEL_XAxis.setBackground(backgroundColor);
      LABEL_XAxis.setForeground(labelColor);
      P1_SidePanel.add(LABEL_XAxis);
      JLabel LABEL_YAxis = new JLabel("Y-Axis");
      LABEL_YAxis.setLocation(200, uy_p41 + 25 * 14 );
      LABEL_YAxis.setSize(150, 20);
      LABEL_YAxis.setHorizontalAlignment(0);
      LABEL_YAxis.setBackground(backgroundColor);
      LABEL_YAxis.setForeground(labelColor);
      P1_SidePanel.add(LABEL_YAxis);
	  axis_chooser = new JComboBox(Axis_Option_NR);
	 // axis_chooser.setBackground(backgroundColor);
	  //axis_chooser.setForeground(labelColor);
	  axis_chooser.setRenderer(new CustomRenderer());
	  axis_chooser2 = new JComboBox(Axis_Option_NR);
	  //axis_chooser2.setForeground(labelColor);
	  //axis_chooser2.setBackground(backgroundColor);
	  axis_chooser2.setRenderer(new CustomRenderer());
      axis_chooser2.setLocation(210, uy_p41 + 25 * 15);
      //axis_chooser2.setPreferredSize(new Dimension(150,25));
      axis_chooser2.setSize(180,25);
      axis_chooser2.setSelectedIndex(3);
      axis_chooser2.setMaximumRowCount(20);
      axis_chooser.setMaximumRowCount(20);
      axis_chooser2.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
    		  Update_DashboardFlexibleChart();
    	  }
  	  } );
      axis_chooser.setLocation(5, uy_p41 + 25 * 15);
      //axis_chooser.setPreferredSize(new Dimension(150,25));
      axis_chooser.setSize(180,25);
      axis_chooser.setSelectedIndex(0);
      axis_chooser.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
    		  Update_DashboardFlexibleChart();
    	  }
  	  } );
      P1_SidePanel.add(axis_chooser);
      P1_SidePanel.add(axis_chooser2);
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
		
	      JPanel BasicAndControllerPanel = new JPanel();
	      BasicAndControllerPanel.setLocation(0, uy_p41 + 26 * 38 );
	      BasicAndControllerPanel.setPreferredSize(new Dimension(1350, 1350));
	      BasicAndControllerPanel.setBackground(backgroundColor);
	      BasicAndControllerPanel.setForeground(Color.white);
	      BasicAndControllerPanel.setLayout(new BorderLayout());
			
		
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
	      
	     	TabPane_SimulationSetup.addTab("Basic and Controller Setup" , icon_setup2, BasicAndControllerPanel, null);
	     	TabPane_SimulationSetup.addTab("Aerodynamic" , icon_aerodynamic, AerodynamicSetupPanel, null);
	     	PageX04_SimSetup.add(TabPane_SimulationSetup);
		TabPane_SimulationSetup.setSelectedIndex(0);
		TabPane_SimulationSetup.setFont(small_font);
		
		
      int INPUT_width = 120;
      int SidePanel_Width = 380; 
      JPanel PANEL_LEFT_InputSection = new JPanel();
      PANEL_LEFT_InputSection.setLayout(null);
      PANEL_LEFT_InputSection.setPreferredSize(new Dimension(SidePanel_Width, 1350));
      PANEL_LEFT_InputSection.setBackground(backgroundColor);
      PANEL_LEFT_InputSection.setForeground(labelColor);
      
      JPanel PANEL_RIGHT_InputSection = new JPanel();
      PANEL_RIGHT_InputSection.setLayout(new BorderLayout());
      PANEL_RIGHT_InputSection.setPreferredSize(new Dimension(405, exty_main));
      PANEL_RIGHT_InputSection.setBackground(backgroundColor);
      PANEL_RIGHT_InputSection.setForeground(labelColor);
      
      JScrollPane scrollPane_LEFT_InputSection = new JScrollPane(PANEL_LEFT_InputSection,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      scrollPane_LEFT_InputSection.setPreferredSize(new Dimension(405, exty_main));
      scrollPane_LEFT_InputSection.getVerticalScrollBar().setUnitIncrement(16);
      BasicAndControllerPanel.add(scrollPane_LEFT_InputSection, BorderLayout.LINE_START);
      JScrollPane scrollPane_RIGHT_InputSection = new JScrollPane(PANEL_RIGHT_InputSection,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      scrollPane_RIGHT_InputSection.getVerticalScrollBar().setUnitIncrement(16);
      BasicAndControllerPanel.add(scrollPane_RIGHT_InputSection, BorderLayout.CENTER);
      //---------------------------------------------------------------------------------------------
      //                       Initial State Definition Block
      //---------------------------------------------------------------------------------------------
      JPanel PANEL_InitialState = new JPanel();
      PANEL_InitialState.setLayout(null);
      PANEL_InitialState.setSize(SidePanel_Width, 430);
      PANEL_InitialState.setLocation(0,0);
      PANEL_InitialState.setBackground(backgroundColor);
      PANEL_InitialState.setForeground(labelColor);
      PANEL_LEFT_InputSection.add(PANEL_InitialState);
      
      JLabel LABEL_InitState = new JLabel("Initial State");
      LABEL_InitState.setLocation(5, uy_p41 + 25 * 0  );
      LABEL_InitState.setSize(350, 20);
      LABEL_InitState.setBackground(backgroundColor);
      LABEL_InitState.setForeground(labelColor);
      LABEL_InitState.setFont(HeadlineFont);
      LABEL_InitState.setHorizontalAlignment(0);
      PANEL_InitialState.add(LABEL_InitState);
      
      JLabel LABEL_InertialFrame = new JLabel("Inertial Frame [ECI]");
      LABEL_InertialFrame.setLocation(2, uy_p41 + 25 * 1 );
      LABEL_InertialFrame.setSize(INPUT_width, 20);
      LABEL_InertialFrame.setHorizontalAlignment(JLabel.CENTER);
      LABEL_InertialFrame.setBackground(backgroundColor);
      LABEL_InertialFrame.setForeground(labelColor);
      LABEL_InertialFrame.setFont(small_font);
      PANEL_InitialState.add(LABEL_InertialFrame);
      JLabel LABEL_RotatingFrame = new JLabel("Rotating Frame [ECEF]");
      LABEL_RotatingFrame.setLocation(2+INPUT_width+5, uy_p41 + 25 * 1  );
      LABEL_RotatingFrame.setSize(INPUT_width+40, 20);
      LABEL_RotatingFrame.setHorizontalAlignment(JLabel.CENTER);
      LABEL_RotatingFrame.setBackground(backgroundColor);
      LABEL_RotatingFrame.setForeground(labelColor);
      LABEL_RotatingFrame.setFont(small_font);
      PANEL_InitialState.add(LABEL_RotatingFrame);
      
      JLabel LABEL_longitude = new JLabel("Longitude [deg]");
      LABEL_longitude.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 2  );
      LABEL_longitude.setSize(250, 20);
      LABEL_longitude.setBackground(backgroundColor);
      LABEL_longitude.setForeground(labelColor);
      LABEL_longitude.setFont(small_font);
      PANEL_InitialState.add(LABEL_longitude);
      JLabel LABEL_latitude = new JLabel("Latitude [deg]");
      LABEL_latitude.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 3 );
      LABEL_latitude.setSize(250, 20);
      LABEL_latitude.setBackground(backgroundColor);
      LABEL_latitude.setForeground(labelColor);
      LABEL_latitude.setFont(small_font);
      PANEL_InitialState.add(LABEL_latitude);
      JLabel LABEL_altitude = new JLabel("Altitude [m]");
      LABEL_altitude.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 4);
      LABEL_altitude.setSize(250, 20);
      LABEL_altitude.setBackground(backgroundColor);
      LABEL_altitude.setForeground(labelColor);
      LABEL_altitude.setFont(small_font);
      PANEL_InitialState.add(LABEL_altitude);
      
      JLabel LABEL_referenceelevation = new JLabel("Ref. Elevation [m]");
      LABEL_referenceelevation.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 5 );
      LABEL_referenceelevation.setSize(250, 20);
      LABEL_referenceelevation.setBackground(backgroundColor);
      LABEL_referenceelevation.setForeground(labelColor);
      LABEL_referenceelevation.setFont(small_font);
      PANEL_InitialState.add(LABEL_referenceelevation);
      
      JLabel LABEL_velocity = new JLabel("Velocity [m/s]");
      LABEL_velocity.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 8 );
      LABEL_velocity.setSize(250, 20);
      LABEL_velocity.setBackground(backgroundColor);
      LABEL_velocity.setForeground(labelColor);
      LABEL_velocity.setFont(small_font);;
      PANEL_InitialState.add(LABEL_velocity);
      JLabel LABEL_fpa = new JLabel("Flight Path angle [deg]");
      LABEL_fpa.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 9);
      LABEL_fpa.setSize(250, 20);
      LABEL_fpa.setBackground(backgroundColor);
      LABEL_fpa.setForeground(labelColor);
      LABEL_fpa.setFont(small_font);
      PANEL_InitialState.add(LABEL_fpa);
      JLabel LABEL_azimuth = new JLabel("Azimuth [deg]");
      LABEL_azimuth.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 10 );
      LABEL_azimuth.setSize(250, 20);
      LABEL_azimuth.setBackground(backgroundColor);
      LABEL_azimuth.setFont(small_font);
      LABEL_azimuth.setForeground(labelColor);
      PANEL_InitialState.add(LABEL_azimuth);

      INPUT_LONG_Is = new JTextField(10);
      INPUT_LONG_Is.setLocation(2, uy_p41 + 25 * 2 );
      INPUT_LONG_Is.setSize(INPUT_width, 20);
      INPUT_LONG_Is.setEnabled(false);
      INPUT_LONG_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_LONG_Is.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			Inertial2Rotating();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_LONG_Is);
      INPUT_LAT_Is = new JTextField(10);
      INPUT_LAT_Is.setLocation(2, uy_p41 + 25 * 3 );
      INPUT_LAT_Is.setSize(INPUT_width, 20);
      INPUT_LAT_Is.setEnabled(false);
      INPUT_LAT_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_LAT_Is.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			Inertial2Rotating();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_LAT_Is);
      INPUT_ALT_Is = new JTextField(10);
      INPUT_ALT_Is.setLocation(2, uy_p41 + 25 * 4 );
      INPUT_ALT_Is.setSize(INPUT_width, 20);
      INPUT_ALT_Is.setEnabled(false);
      INPUT_ALT_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_ALT_Is.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			Inertial2Rotating();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_ALT_Is);   
      INPUT_REFELEV = new JTextField(10);
      INPUT_REFELEV.setLocation(2+INPUT_width+5, uy_p41 + 25 * 5 );
      INPUT_REFELEV.setSize(INPUT_width, 20);
      INPUT_REFELEV.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_REFELEV.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			WRITE_INIT();
		
		}
    	  
      });
      PANEL_InitialState.add(INPUT_REFELEV); 
      INPUT_VEL_Is = new JTextField(10);
      INPUT_VEL_Is.setLocation(2, uy_p41 + 25 * 8 );
      INPUT_VEL_Is.setText("1");
      INPUT_VEL_Is.setSize(INPUT_width, 20);
      INPUT_VEL_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_VEL_Is.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			Inertial2Rotating();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_VEL_Is);
      INPUT_FPA_Is = new JTextField(10);
      INPUT_FPA_Is.setLocation(2, uy_p41 + 25 * 9 );
      INPUT_FPA_Is.setText("0");
      INPUT_FPA_Is.setSize(INPUT_width, 20);
      INPUT_FPA_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_FPA_Is.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			Inertial2Rotating();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_FPA_Is);
      INPUT_AZI_Is = new JTextField(10);
      INPUT_AZI_Is.setLocation(2, uy_p41 + 25 * 10 );
      INPUT_AZI_Is.setSize(INPUT_width, 20);
      INPUT_AZI_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_AZI_Is.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			Inertial2Rotating();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_AZI_Is);
      //---------------------------------------------------------
      INPUT_LONG_Rs = new JTextField(10);
      INPUT_LONG_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 2 );
      INPUT_LONG_Rs.setSize(INPUT_width, 20);
      INPUT_LONG_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_LONG_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			Rotating2Inertial();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_LONG_Rs);
      INPUT_LAT_Rs = new JTextField(10);
      INPUT_LAT_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 3 );
      INPUT_LAT_Rs.setSize(INPUT_width, 20);
      INPUT_LAT_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_LAT_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			Rotating2Inertial();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_LAT_Rs);
      INPUT_ALT_Rs = new JTextField(10);
      INPUT_ALT_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 4 );
      INPUT_ALT_Rs.setSize(INPUT_width, 20);
      INPUT_ALT_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_ALT_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			Rotating2Inertial();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_ALT_Rs);    
      
      
	  JComboBox VelocityFrame_chooser = new JComboBox(Vel_Frame_options);
	//  VelocityFrame_chooser.setBackground(backgroundColor);
	  VelocityFrame_chooser.setLocation(2+INPUT_width+5, uy_p41 + 25 * 7);
	  VelocityFrame_chooser.setSize(380-(2+INPUT_width+5),20);
	  VelocityFrame_chooser.setSelectedIndex(1);
	  VelocityFrame_chooser.setRenderer(new CustomRenderer());
	  VelocityFrame_chooser.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
    		 
  			if(VelocityFrame_chooser.getSelectedIndex()==0) {
				LABEL_velocity.setText("u [m/s]");
				LABEL_fpa.setText("v [m/s]");
				LABEL_azimuth.setText("w [m/s]");
			} else {
				LABEL_velocity.setText("v [m/s]");
				LABEL_fpa.setText("flight path angle [deg]");
				LABEL_azimuth.setText("local azimuth [deg]");	
			}
  			if(VelocityFrame_chooser.getSelectedIndex()==vel_frame_hist) {
  				// do nothing
  			} else if(VelocityFrame_chooser.getSelectedIndex()==0 && vel_frame_hist==1) {
  				// Spherical to Cartesian
  				double[] X = new double[3];
  				X[0]= Double.parseDouble(INPUT_VEL_Rs.getText());
  				X[1]= Double.parseDouble(INPUT_FPA_Rs.getText())*deg2rad;
  				X[2]= Double.parseDouble(INPUT_AZI_Rs.getText())*deg2rad;
  				double[] res =  Spherical2Cartesian(X);
  				INPUT_VEL_Rs.setText(""+String.format("%.5f",  res[0]));
  				INPUT_FPA_Rs.setText(""+String.format("%.5f",  res[1]));
  				INPUT_AZI_Rs.setText(""+String.format("%.5f",  res[2]));
  				vel_frame_hist=VelocityFrame_chooser.getSelectedIndex();
  			} else if(VelocityFrame_chooser.getSelectedIndex()==1 && vel_frame_hist==0) {
  				// Cartesian to Spherical 
  				double[] X = new double[3];
  				X[0]= Double.parseDouble(INPUT_VEL_Rs.getText());
  				X[1]= Double.parseDouble(INPUT_FPA_Rs.getText());
  				X[2]= Double.parseDouble(INPUT_AZI_Rs.getText());
  				X =  Cartesian2Spherical(X);
  				INPUT_VEL_Rs.setText(""+String.format("%.5f",  X[0]));
  				INPUT_FPA_Rs.setText(""+String.format("%.5f",  X[1]*rad2deg));
  				INPUT_AZI_Rs.setText(""+String.format("%.5f",  X[2]*rad2deg));
  				vel_frame_hist=VelocityFrame_chooser.getSelectedIndex();
  			}
    	  }
    	  
  	  } );
	  PANEL_InitialState.add(VelocityFrame_chooser);
      
      
      INPUT_VEL_Rs = new JTextField(10);
      INPUT_VEL_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 8 );
      INPUT_VEL_Rs.setSize(INPUT_width, 20);
      INPUT_VEL_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_VEL_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			Rotating2Inertial();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_VEL_Rs);
      INPUT_FPA_Rs = new JTextField(10);
      INPUT_FPA_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 9 );
      INPUT_FPA_Rs.setSize(INPUT_width, 20);
      INPUT_FPA_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_FPA_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			Rotating2Inertial();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_FPA_Rs);
      INPUT_AZI_Rs = new JTextField(10);
      INPUT_AZI_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 10 );
      INPUT_AZI_Rs.setSize(INPUT_width, 20);
      INPUT_AZI_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_AZI_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			Rotating2Inertial();
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_AZI_Rs);
      
      JLabel LABEL_AngularRate = new JLabel("Initial Angular Rate: ");
      LABEL_AngularRate.setLocation(2, uy_p41 + 25 * 12);
      LABEL_AngularRate.setSize(150, 20);
      LABEL_AngularRate.setBackground(backgroundColor);
      LABEL_AngularRate.setForeground(labelColor);
      PANEL_InitialState.add(LABEL_AngularRate);
      
      JLabel LABEL_AngularRateX = new JLabel("Body X [deg/s]");
      LABEL_AngularRateX.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 13 );
      LABEL_AngularRateX.setSize(250, 20);
      LABEL_AngularRateX.setBackground(backgroundColor);
      LABEL_AngularRateX.setForeground(labelColor);
      LABEL_AngularRateX.setFont(small_font);;
      PANEL_InitialState.add(LABEL_AngularRateX);
      JLabel LABEL_AngularRateY = new JLabel("Body Y [deg/s]");
      LABEL_AngularRateY.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 14);
      LABEL_AngularRateY.setSize(250, 20);
      LABEL_AngularRateY.setBackground(backgroundColor);
      LABEL_AngularRateY.setForeground(labelColor);
      LABEL_AngularRateY.setFont(small_font);
      PANEL_InitialState.add(LABEL_AngularRateY);
      JLabel LABEL_AngularRateZ = new JLabel("Body Z [deg/s]");
      LABEL_AngularRateZ.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 15 );
      LABEL_AngularRateZ.setSize(250, 20);
      LABEL_AngularRateZ.setBackground(backgroundColor);
      LABEL_AngularRateZ.setFont(small_font);
      LABEL_AngularRateZ.setForeground(labelColor);
      PANEL_InitialState.add(LABEL_AngularRateZ);
      
      INPUT_AngularRate_X = new JTextField(10);
      INPUT_AngularRate_X.setLocation(2+INPUT_width+5, uy_p41 + 25 * 13 );
      INPUT_AngularRate_X.setSize(INPUT_width, 20);
      INPUT_AngularRate_X.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_AngularRate_X.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			//WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_AngularRate_X);
      
      INPUT_AngularRate_Y = new JTextField(10);
      INPUT_AngularRate_Y.setLocation(2+INPUT_width+5, uy_p41 + 25 * 14 );
      INPUT_AngularRate_Y.setSize(INPUT_width, 20);
      INPUT_AngularRate_Y.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_AngularRate_Y.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			//WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_AngularRate_Y);
      
      INPUT_AngularRate_Z = new JTextField(10);
      INPUT_AngularRate_Z.setLocation(2+INPUT_width+5, uy_p41 + 25 * 15 );
      INPUT_AngularRate_Z.setSize(INPUT_width, 20);
      INPUT_AngularRate_Z.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_AngularRate_Z.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			//WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_AngularRate_Z);
      
      JLabel LABEL_Time = new JLabel("Time: ");
      LABEL_Time.setLocation(2, uy_p41 + 25 * 16);
      LABEL_Time.setSize(50, 20);
      LABEL_Time.setBackground(backgroundColor);
      LABEL_Time.setForeground(labelColor);
      PANEL_InitialState.add(LABEL_Time);
      
      INPUT_TIME = new JTextField(10);
      INPUT_TIME.setLocation(55, uy_p41 + 25 * 16 );
      INPUT_TIME.setSize(INPUT_width*2, 20);
      INPUT_TIME.setHorizontalAlignment(JTextField.LEFT);
      INPUT_TIME.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			WRITE_INIT();
		}
    	  
      });
      PANEL_InitialState.add(INPUT_TIME);
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
      INPUT_TIME.setText(timeStamp);
      //---------------------------------------------------------------------------------------------
      //                         Integrator Definition Block
      //---------------------------------------------------------------------------------------------       
      JPanel IntegratorInputPanel = new JPanel();
      IntegratorInputPanel.setLocation(0, uy_p41 + 25 * 18 );
      IntegratorInputPanel.setSize(SidePanel_Width, 825);
      IntegratorInputPanel.setBackground(backgroundColor);
      IntegratorInputPanel.setForeground(Color.white);
      IntegratorInputPanel.setLayout(null);
      PANEL_LEFT_InputSection.add(IntegratorInputPanel);
      
      JSeparator Separator_Page2_1 = new JSeparator();
      Separator_Page2_1.setLocation(0, uy_p41 + 25 * 0 );
      Separator_Page2_1.setSize(SidePanel_Width, 1);
      Separator_Page2_1.setBackground(Color.black);
      Separator_Page2_1.setForeground(labelColor);
      IntegratorInputPanel.add(Separator_Page2_1);
      
      JLabel LABEL_IntegSetting = new JLabel("Integrator Settings");
      LABEL_IntegSetting.setLocation(0, uy_p41 + 25 * 0 );
      LABEL_IntegSetting.setSize(400, 20);
      LABEL_IntegSetting.setBackground(backgroundColor);
      LABEL_IntegSetting.setForeground(labelColor);
      LABEL_IntegSetting.setFont(HeadlineFont);
      LABEL_IntegSetting.setHorizontalAlignment(0);
      IntegratorInputPanel.add(LABEL_IntegSetting);
      
	  AscentDescent_SwitchChooser = new JComboBox(Thrust_switch);
	 // AscentDescent_SwitchChooser.setBackground(backgroundColor);
	  AscentDescent_SwitchChooser.setLocation(2, uy_p41 + 26 * 1 );
	  AscentDescent_SwitchChooser.setSize(250,25);
	  AscentDescent_SwitchChooser.setRenderer(new CustomRenderer());
	  AscentDescent_SwitchChooser.setSelectedIndex(0);
	  AscentDescent_SwitchChooser.addActionListener(new ActionListener() { 
   	  public void actionPerformed(ActionEvent e) {
   		Module_Indicator.setText(""+AscentDescent_SwitchChooser.getSelectedItem()); 
   	  }
 	  } );
	  AscentDescent_SwitchChooser.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			 WRITE_INIT();
		}
		  
	  });
	  IntegratorInputPanel.add(AscentDescent_SwitchChooser);
   
      JLabel LABEL_writetime = new JLabel("Write time step [s]");
      LABEL_writetime.setLocation(65, uy_p41 + 25 * 3 );
      LABEL_writetime.setSize(250, 20);
      LABEL_writetime.setBackground(backgroundColor);
      LABEL_writetime.setForeground(labelColor);
      IntegratorInputPanel.add(LABEL_writetime);

     INPUT_WRITETIME = new JTextField(10);
     INPUT_WRITETIME.setLocation(2, uy_p41 + 25 * 3 );
     INPUT_WRITETIME.setSize(60, 20);
     INPUT_WRITETIME.setHorizontalAlignment(JTextField.RIGHT);
     INPUT_WRITETIME.addFocusListener(new FocusListener() {

 		@Override
 		public void focusGained(FocusEvent arg0) { }

 		@Override
 		public void focusLost(FocusEvent e) {
 			// TODO Auto-generated method stub
 			WRITE_INIT();
 		}
     	  
       });
     IntegratorInputPanel.add(INPUT_WRITETIME);

    JLabel LABEL_TARGETBODY = new JLabel("Target Body");
    LABEL_TARGETBODY.setLocation(163, uy_p41 + 25 * 5   );
    LABEL_TARGETBODY.setSize(150, 20);
    LABEL_TARGETBODY.setBackground(backgroundColor);
    LABEL_TARGETBODY.setForeground(labelColor);
    IntegratorInputPanel.add(LABEL_TARGETBODY);
    
	  Target_chooser = new JComboBox(Target_Options);
	 // Target_chooser.setBackground(backgroundColor);
	  Target_chooser.setLocation(2, uy_p41 + 25 * 5 );
	  Target_chooser.setSize(150,25);
	  Target_chooser.setRenderer(new CustomRenderer());
	  Target_chooser.setSelectedIndex(3);
	  Target_chooser.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
    		
    	  }
  	  } );
	  Target_chooser.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			 WRITE_INIT();
		}
		  
	  });
	  IntegratorInputPanel.add(Target_chooser);
	  
	  Integrator_chooser = new JComboBox(Integrator_Options);
	  //Integrator_chooser.setBackground(backgroundColor);
	  Integrator_chooser.setLocation(2, uy_p41 + 25 * 6 );
	  Integrator_chooser.setSize(380,25);
	  Integrator_chooser.setRenderer(new CustomRenderer());
	  Integrator_chooser.setSelectedIndex(3);
	  Integrator_chooser.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
    		  Update_IntegratorSettings();
    		  READ_INTEG();
    	  }
  	  } );
	  Integrator_chooser.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			 WRITE_INIT();
		}
		  
	  });
	  IntegratorInputPanel.add(Integrator_chooser);
	  //------------------------------------------------------------------------------------------------------------------
      LABEL_IntegratorSetting_01 = new JLabel("");
      LABEL_IntegratorSetting_01.setLocation(65, uy_p41 + 25 * 8 );
      LABEL_IntegratorSetting_01.setSize(250, 20);
      LABEL_IntegratorSetting_01.setBackground(backgroundColor);
      LABEL_IntegratorSetting_01.setForeground(labelColor);
      IntegratorInputPanel.add(LABEL_IntegratorSetting_01);
      INPUT_IntegratorSetting_01 = new JTextField(10);
      INPUT_IntegratorSetting_01.setLocation(2, uy_p41 + 25 * 8 );
      INPUT_IntegratorSetting_01.setSize(60, 20);
      INPUT_IntegratorSetting_01.setHorizontalAlignment(JTextField.LEFT);
      INPUT_IntegratorSetting_01.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			WRITE_INTEG();
		}
    	  
      });
      IntegratorInputPanel.add(INPUT_IntegratorSetting_01);
      LABEL_IntegratorSetting_02 = new JLabel("");
      LABEL_IntegratorSetting_02.setLocation(65, uy_p41 + 25 * 9 );
      LABEL_IntegratorSetting_02.setSize(250, 20);
      LABEL_IntegratorSetting_02.setBackground(backgroundColor);
      LABEL_IntegratorSetting_02.setForeground(labelColor);
      IntegratorInputPanel.add(LABEL_IntegratorSetting_02);
      INPUT_IntegratorSetting_02 = new JTextField(10);
      INPUT_IntegratorSetting_02.setLocation(2, uy_p41 + 25 * 9 );
      INPUT_IntegratorSetting_02.setSize(60, 20);
      INPUT_IntegratorSetting_02.setHorizontalAlignment(JTextField.LEFT);
      INPUT_IntegratorSetting_02.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			WRITE_INTEG();
		}
    	  
      });
      IntegratorInputPanel.add(INPUT_IntegratorSetting_02);
      LABEL_IntegratorSetting_03 = new JLabel("");
      LABEL_IntegratorSetting_03.setLocation(65, uy_p41 + 25 * 10 );
      LABEL_IntegratorSetting_03.setSize(250, 20);
      LABEL_IntegratorSetting_03.setBackground(backgroundColor);
      LABEL_IntegratorSetting_03.setForeground(labelColor);
      IntegratorInputPanel.add(LABEL_IntegratorSetting_03);
      INPUT_IntegratorSetting_03 = new JTextField(10);
      INPUT_IntegratorSetting_03.setLocation(2, uy_p41 + 25 * 10 );
      INPUT_IntegratorSetting_03.setSize(60, 20);
      INPUT_IntegratorSetting_03.setHorizontalAlignment(JTextField.LEFT);
      INPUT_IntegratorSetting_03.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			WRITE_INTEG();
		}
    	  
      });
      IntegratorInputPanel.add(INPUT_IntegratorSetting_03);
      LABEL_IntegratorSetting_04 = new JLabel("");
      LABEL_IntegratorSetting_04.setLocation(65, uy_p41 + 25 * 11 );
      LABEL_IntegratorSetting_04.setSize(250, 20);
      LABEL_IntegratorSetting_04.setBackground(backgroundColor);
      LABEL_IntegratorSetting_04.setForeground(labelColor);
      IntegratorInputPanel.add(LABEL_IntegratorSetting_04);
      INPUT_IntegratorSetting_04 = new JTextField(10);
      INPUT_IntegratorSetting_04.setLocation(2, uy_p41 + 25 * 11 );
      INPUT_IntegratorSetting_04.setSize(60, 20);
      INPUT_IntegratorSetting_04.setHorizontalAlignment(JTextField.LEFT);
      INPUT_IntegratorSetting_04.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			WRITE_INTEG();
		}
    	  
      });
      IntegratorInputPanel.add(INPUT_IntegratorSetting_04);
      LABEL_IntegratorSetting_05 = new JLabel("");
      LABEL_IntegratorSetting_05.setLocation(65, uy_p41 + 25 * 12 );
      LABEL_IntegratorSetting_05.setSize(250, 20);
      LABEL_IntegratorSetting_05.setBackground(backgroundColor);
      LABEL_IntegratorSetting_05.setForeground(labelColor);
      IntegratorInputPanel.add(LABEL_IntegratorSetting_05);
      INPUT_IntegratorSetting_05 = new JTextField(10);
      INPUT_IntegratorSetting_05.setLocation(2, uy_p41 + 25 * 12 );
      INPUT_IntegratorSetting_05.setSize(60, 20);
      INPUT_IntegratorSetting_05.setHorizontalAlignment(JTextField.LEFT);
      INPUT_IntegratorSetting_05.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			WRITE_INTEG();
		}
    	  
      });
      
      IntegratorInputPanel.add(INPUT_IntegratorSetting_05); 
      
    //------------------------------------------------------------------------------------------------------------------
    // Coordinate System and Degree of Freedom Setup 
      
      
      JLabel LABEL_VCoordinateSystem = new JLabel("Select Coordinate System to solve the Velocity Vector");
      LABEL_VCoordinateSystem.setLocation(5, uy_p41 + 25 * 13   );
      LABEL_VCoordinateSystem.setSize(350, 20);
      LABEL_VCoordinateSystem.setBackground(backgroundColor);
      LABEL_VCoordinateSystem.setForeground(labelColor);
      IntegratorInputPanel.add(LABEL_VCoordinateSystem);
      
      SELECT_VelocityCartesian =new JRadioButton("Cartesian Velocity Coordinates");    
      SELECT_VelocitySpherical =new JRadioButton("Spherical Velocity Coordinates");      
      SELECT_VelocitySpherical.setLocation(5, uy_p41 + 25 * 14 );
      SELECT_VelocitySpherical.setSize(220,20);
      SELECT_VelocitySpherical.setBackground(backgroundColor);
      SELECT_VelocitySpherical.setForeground(labelColor);
      SELECT_VelocitySpherical.setFont(small_font);
      SELECT_VelocityCartesian.setLocation(5, uy_p41 + 25 * 15);
      SELECT_VelocityCartesian.setSize(220,20);
      SELECT_VelocityCartesian.setBackground(backgroundColor);
      SELECT_VelocityCartesian.setFont(small_font);
     ButtonGroup bg_velocity=new ButtonGroup();    
     bg_velocity.add(SELECT_VelocitySpherical);
     bg_velocity.add(SELECT_VelocityCartesian); 
     IntegratorInputPanel.add(SELECT_VelocitySpherical);
     IntegratorInputPanel.add(SELECT_VelocityCartesian);
     SELECT_VelocitySpherical.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(SELECT_VelocitySpherical.isSelected()) {
				VelocityCoordinateSystem = 1;
			} else if (SELECT_VelocityCartesian.isSelected()) {
				VelocityCoordinateSystem = 2;
			}
			WRITE_INIT();
		}
     });
     SELECT_VelocityCartesian.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(SELECT_VelocitySpherical.isSelected()) {
				VelocityCoordinateSystem = 1;
			} else if (SELECT_VelocityCartesian.isSelected()) {
				VelocityCoordinateSystem = 2;
			}
			WRITE_INIT();
		}
    	 
     });
     
     JLabel LABEL_SelectDoF = new JLabel("Select Degrees of Freedom");
     LABEL_SelectDoF.setLocation(5, uy_p41 + 25 * 16   );
     LABEL_SelectDoF.setSize(350, 20);
     LABEL_SelectDoF.setBackground(backgroundColor);
     LABEL_SelectDoF.setForeground(labelColor);
     IntegratorInputPanel.add(LABEL_SelectDoF);
     
     SELECT_3DOF =new JRadioButton("3DOF Model");    
     SELECT_6DOF =new JRadioButton("6DOF Model");      
     SELECT_3DOF.setLocation(5, uy_p41 + 25 * 17 );
     SELECT_3DOF.setSize(220,20);
     SELECT_3DOF.setBackground(backgroundColor);
     SELECT_3DOF.setForeground(labelColor);
     SELECT_3DOF.setFont(small_font);
     SELECT_6DOF.setLocation(5, uy_p41 + 25 * 18);
     SELECT_6DOF.setSize(220,20);
     SELECT_6DOF.setBackground(backgroundColor);
     SELECT_6DOF.setFont(small_font);
    ButtonGroup bg_dof=new ButtonGroup();    
    bg_dof.add(SELECT_3DOF);
    bg_dof.add(SELECT_6DOF); 
    IntegratorInputPanel.add(SELECT_3DOF);
    IntegratorInputPanel.add(SELECT_6DOF);
    SELECT_3DOF.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(SELECT_3DOF.isSelected()) {
				DOF_System = 3;
			} else if (SELECT_6DOF.isSelected()) {
				DOF_System = 6;
			}
			WRITE_INIT();
		}
   	 
    });
    SELECT_6DOF.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(SELECT_3DOF.isSelected()) {
				DOF_System = 3;
			} else if (SELECT_6DOF.isSelected()) {
				DOF_System = 6;
			}
			WRITE_INIT();
		}
   	 
    });      
	  //------------------------------------------------------------------------------------------------------------------
    // Solver Stop conditions
	    JLabel LABEL_EventHandler = new JLabel("Solver stop conditions:");
	    LABEL_EventHandler.setLocation(2, uy_p41 + 25 * 23   );
	    LABEL_EventHandler.setSize(150, 20);
	    LABEL_EventHandler.setBackground(backgroundColor);
	    LABEL_EventHandler.setForeground(labelColor);
	    IntegratorInputPanel.add(LABEL_EventHandler);

	    TABLE_EventHandler = new JTable();
	   // TABLE_SEQUENCE.setFont(table_font);
	    
		Action action_EventHandler = new AbstractAction()
	    {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
	        {// Action
				  WRITE_INIT();	
				  WRITE_EventHandler();
				  System.out.println("write eventhandler file.");
	        }
	    };
	    @SuppressWarnings("unused")
		TableCellListener tcl_EventHandler = new TableCellListener(TABLE_EventHandler, action_EventHandler);
	    MODEL_EventHandler = new DefaultTableModel(){

			private static final long serialVersionUID = 1L;

			@Override
	        public boolean isCellEditable(int row, int column) {
	           //all cells false
				if (row == 0 && column == 0){
					return false;
				} else {
					return true; 
				}
	        }
	    }; 
	
	    MODEL_EventHandler.setColumnIdentifiers(COLUMS_EventHandler);
	    TABLE_EventHandler.setModel(MODEL_EventHandler);
	    TABLE_EventHandler.setBackground(backgroundColor);
	    int tablewidth_EventHandler = 385;
	    int tableheight_EventHandler = 230;
	  // ((JTable) TABLE_SEQUENCE).setFillsViewportHeight(true);
	    TABLE_EventHandler.setBackground(backgroundColor);
	    TABLE_EventHandler.setForeground(labelColor);
	    TABLE_EventHandler.setSize(tablewidth_EventHandler, tableheight_EventHandler);
	    TABLE_EventHandler.getTableHeader().setReorderingAllowed(false);
	    TABLE_EventHandler.setRowHeight(35);

		    TableColumn EventHandlerType_colum   		 = 	    TABLE_EventHandler.getColumnModel().getColumn(0);
		    TableColumn EventHandlerValue_column 	     = 	    TABLE_EventHandler.getColumnModel().getColumn(1);

		    EventHandlerType_colum.setPreferredWidth(300);
		    EventHandlerValue_column.setPreferredWidth(100);

		    TABLE_EventHandler.getTableHeader().setBackground(backgroundColor);
		    TABLE_EventHandler.getTableHeader().setForeground(labelColor);
	    
	    EventHandlerTypeCombobox.setBackground(backgroundColor);
	    try {
	    for (int i=0;i<EventHandler_Type.length;i++) {EventHandlerTypeCombobox.addItem(EventHandler_Type[i]);}
	    } catch(NullPointerException eNPE) {
	    	System.out.println(eNPE);
	    }
	    EventHandlerType_colum.setCellEditor(new DefaultCellEditor(EventHandlerTypeCombobox));
	   

	    
	    JScrollPane TABLE_EventHandler_ScrollPane = new JScrollPane(TABLE_EventHandler,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    TABLE_EventHandler_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
	    TABLE_EventHandler_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
	    TABLE_EventHandler_ScrollPane.setBackground(backgroundColor);
	    TABLE_EventHandler_ScrollPane.setSize(tablewidth_EventHandler,tableheight_EventHandler);
	    TABLE_EventHandler_ScrollPane.setLocation(2, uy_p41 + 25 * 24 );
	    TABLE_EventHandler_ScrollPane.setOpaque(false);
	    IntegratorInputPanel.add(TABLE_EventHandler_ScrollPane);
	    
    	ROW_EventHandler[0] = ""+EventHandler_Type[0];
    	ROW_EventHandler[1] = "";
    	MODEL_EventHandler.addRow(ROW_EventHandler);

    	
        JButton BUTTON_AddEventHandler = new JButton("Add");
        BUTTON_AddEventHandler.setLocation(155, uy_p41 + 25 * 23);
        BUTTON_AddEventHandler.setSize(65,20);
        BUTTON_AddEventHandler.setEnabled(true);
        BUTTON_AddEventHandler.setForeground(labelColor);
        BUTTON_AddEventHandler.setBackground(backgroundColor);
        BUTTON_AddEventHandler.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        	    	ROW_EventHandler[0] = ""+EventHandler_Type[1];
        	    	ROW_EventHandler[1] = "0";
        	    	MODEL_EventHandler.addRow(ROW_EventHandler); 
        	    	WRITE_EventHandler();
        	  } } );
        IntegratorInputPanel.add(BUTTON_AddEventHandler);
        
        JButton BUTTON_DeleteEventHandler = new JButton("Delete");
        BUTTON_DeleteEventHandler.setLocation(225, uy_p41 + 25 * 23);
        BUTTON_DeleteEventHandler.setSize(75,20);
        BUTTON_DeleteEventHandler.setEnabled(true);
        BUTTON_DeleteEventHandler.setForeground(labelColor);
        BUTTON_DeleteEventHandler.setBackground(backgroundColor);
        BUTTON_DeleteEventHandler.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        	    	int j = TABLE_EventHandler.getSelectedRow();
        	    	if(j!=0) { // Time stopper set by default -> cannot be deleted 
        	    	if (j >= 0){MODEL_EventHandler.removeRow(j);}
        	    	}
        	    	WRITE_EventHandler();
        	  } } );
        IntegratorInputPanel.add(BUTTON_DeleteEventHandler);

 
	  //   Right side :
    JPanel P2_ControllerPane = new JPanel();
   // P2_ControllerPane.setLayout(null);
    //P2_ControllerPane.setPreferredSize(new Dimension((exty_main+400),290));
    P2_ControllerPane.setBackground(backgroundColor);
    P2_ControllerPane.setForeground(labelColor);
    
    JPanel P2_SequenceMAIN = new JPanel();
    P2_SequenceMAIN.setLayout(new BorderLayout());
   // P2_SequenceMAIN.setPreferredSize(new Dimension(900, 400));
    P2_SequenceMAIN.setBackground(backgroundColor);
    P2_SequenceMAIN.setForeground(labelColor);
    PANEL_RIGHT_InputSection.add(P2_SequenceMAIN, BorderLayout.PAGE_START);
    //-----------------------------------------------------------------------------------------------------------------------------
    //                  Sequence table 
    //-----------------------------------------------------------------------------------------------------------------------------
    TABLE_SEQUENCE = new JTable(){
   	 
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
    	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
            Component comp = super.prepareRenderer(renderer, row, col);
            Object value = getModel().getValueAt(row, col);
            String val_TLFC = (String) getModel().getValueAt(row, 4);
            String val_TVCFC = (String) getModel().getValueAt(row, 8);
          //  JTextField textfield= super.getComponent();
            try {
            	if (value.equals(""+SequenceType[2])) {
                    comp.setBackground(Color.orange);
                    comp.setForeground(labelColor);
            	} else if (value.equals(""+SequenceType[0])) {
                    comp.setBackground(backgroundColor);
                    comp.setForeground(labelColor);
            	} else if (value.equals(""+SequenceType[1])) {
                    comp.setBackground(Color.blue);
                    comp.setForeground(Color.white);
            	} else if (value.equals(""+SequenceType[3])) {
                    comp.setBackground(Color.green);
                    comp.setForeground(labelColor);
            	} else if (value.equals(""+SequenceType[4])) {
                    comp.setBackground(Color.CYAN);
                    comp.setForeground(labelColor);
            	} else if (value.equals(""+SequenceType[5])) {
                    comp.setBackground(Color.red);
                    comp.setForeground(Color.white);
            	} else if (col==9&&val_TVCFC.equals(""+SequenceTVCFC[0])){
                    comp.setBackground(light_gray);
                    comp.setForeground(Color.gray);
            	}else if (col==10&&val_TVCFC.equals(""+SequenceTVCFC[0])){
                    comp.setBackground(light_gray);
                    comp.setForeground(Color.gray);
            	} else if (col==11&&val_TVCFC.equals(""+SequenceTVCFC[0])){
                    comp.setBackground(light_gray);
                    comp.setForeground(Color.gray);
            	} else if (col==5&&val_TLFC.equals(""+SequenceFC[0])){
                    comp.setBackground(light_gray);
                    comp.setForeground(Color.gray);
            	} else if (col==6&&val_TLFC.equals(""+SequenceFC[0])){
                    comp.setBackground(light_gray);
                    comp.setForeground(Color.gray);
            	} else if (col==7&&val_TLFC.equals(""+SequenceFC[0])){
                    comp.setBackground(light_gray);
                    comp.setForeground(Color.gray);
            	}  else {
                    comp.setBackground(backgroundColor);
                    comp.setForeground(labelColor);
            	}         
            } catch (NullPointerException e) {
            	
            }
           // comp.setFont(table_font);
            
            return comp;
        }
    };
   // TABLE_SEQUENCE.setFont(table_font);
    
	Action action3 = new AbstractAction()
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
        {WriteSequenceINP();}
    };
    @SuppressWarnings("unused")
	TableCellListener tcl3 = new TableCellListener(TABLE_SEQUENCE, action3);
    MODEL_SEQUENCE = new DefaultTableModel(){

		private static final long serialVersionUID = 1L;

		@Override
        public boolean isCellEditable(int row, int column) {
           //all cells false
			String val_TVCFC = (String) MODEL_SEQUENCE.getValueAt(row, 8);
			String val_TLFC = (String) MODEL_SEQUENCE.getValueAt(row, 4);
			if (column == 0 ){
				return false;
			} else if (column==9 && val_TVCFC.equals(SequenceTVCFCCombobox.getItemAt(0))) {
				return false; 
			} else if (column==10 && val_TVCFC.equals(SequenceTVCFCCombobox.getItemAt(0))) {
				return false; 
			} else if (column==11 && val_TVCFC.equals(SequenceTVCFCCombobox.getItemAt(0))) {
				return false; 
			} else if (column==5 && val_TLFC.equals(SequenceFCCombobox.getItemAt(0))) {
				return false; 
			} else if (column==6 && val_TLFC.equals(SequenceFCCombobox.getItemAt(0))) {
				return false; 
			} else if (column==7 && val_TLFC.equals(SequenceFCCombobox.getItemAt(0))) {
				return false; 
			} else {
				return true; 
			}
        }
    }; 
    MODEL_SEQUENCE.setColumnIdentifiers(COLUMS_SEQUENCE);
    TABLE_SEQUENCE.setModel(MODEL_SEQUENCE);
    TABLE_SEQUENCE.setBackground(backgroundColor);
    TABLE_SEQUENCE.setBackground(backgroundColor);
    TABLE_SEQUENCE.setForeground(labelColor);
    TABLE_SEQUENCE.getTableHeader().setReorderingAllowed(false);
    TABLE_SEQUENCE.setRowHeight(45);
    
   // TABLE_SEQUENCE.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	    TableColumn SequID_colum   			 = TABLE_SEQUENCE.getColumnModel().getColumn(0);
	    TableColumn SequENDTypeColumn 	     = TABLE_SEQUENCE.getColumnModel().getColumn(1);
	    TableColumn SequENDValColumn  		 = TABLE_SEQUENCE.getColumnModel().getColumn(2);
	    TableColumn SequTypeColumn 	   		 = TABLE_SEQUENCE.getColumnModel().getColumn(3);
	    TableColumn SequenceFCColumn 	  	 = TABLE_SEQUENCE.getColumnModel().getColumn(4);
	    TableColumn FCvelColumn 	   		     = TABLE_SEQUENCE.getColumnModel().getColumn(5);
	    TableColumn FCaltColumn	   			 = TABLE_SEQUENCE.getColumnModel().getColumn(6);
	    TableColumn FCtargetCurveColumn    	 = TABLE_SEQUENCE.getColumnModel().getColumn(7);
	    
	    TableColumn TVCFCColumn    	 	     = TABLE_SEQUENCE.getColumnModel().getColumn(8);
	    TableColumn TVCFCxColumn    	 	     = TABLE_SEQUENCE.getColumnModel().getColumn(9);
	    TableColumn TVCFCyColumn    	 	     = TABLE_SEQUENCE.getColumnModel().getColumn(10);
	    TableColumn TVCFCtargetCurveColumn   = TABLE_SEQUENCE.getColumnModel().getColumn(11);

	    
	    TABLE_SEQUENCE.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    SequID_colum.setPreferredWidth(50);
	    SequENDTypeColumn.setPreferredWidth(100);
	    SequENDValColumn.setPreferredWidth(100);
	    SequTypeColumn.setPreferredWidth(200);
	    SequenceFCColumn.setPreferredWidth(150);
	    FCvelColumn.setPreferredWidth(130);
	    FCaltColumn.setPreferredWidth(130);
	    FCtargetCurveColumn.setPreferredWidth(150); 
	    
	    TVCFCColumn.setPreferredWidth(150);
	    TVCFCxColumn.setPreferredWidth(130);
	    TVCFCyColumn.setPreferredWidth(130);
	    TVCFCtargetCurveColumn.setPreferredWidth(150); 
	    
	    ((JTable) TABLE_SEQUENCE).setFillsViewportHeight(true);
    
    TABLE_SEQUENCE.getTableHeader().setBackground(backgroundColor);
    TABLE_SEQUENCE.getTableHeader().setForeground(labelColor);
    
    SequenceENDTypeCombobox.setBackground(backgroundColor);
    try {
    for (int i=0;i<SequenceENDType.length;i++) {
    	SequenceENDTypeCombobox.addItem(SequenceENDType[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    SequENDTypeColumn.setCellEditor(new DefaultCellEditor(SequenceENDTypeCombobox));
    
    SequenceTypeCombobox.setBackground(backgroundColor);
    try {
    for (int i=0;i<SequenceType.length;i++) {
    	SequenceTypeCombobox.addItem(SequenceType[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    SequTypeColumn.setCellEditor(new DefaultCellEditor(SequenceTypeCombobox));
    
    SequenceFCCombobox.setBackground(backgroundColor);
    try {
    for (int i=0;i<SequenceFC.length;i++) {
    	SequenceFCCombobox.addItem(SequenceFC[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    SequenceFCColumn.setCellEditor(new DefaultCellEditor(SequenceFCCombobox));
    
    FCTargetCurveCombobox.setBackground(backgroundColor);
    try {
    for (int i=0;i<FCTargetCurve.length;i++) {
    	FCTargetCurveCombobox.addItem(FCTargetCurve[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    FCtargetCurveColumn.setCellEditor(new DefaultCellEditor(FCTargetCurveCombobox));
    
    SequenceTVCFCCombobox.setBackground(backgroundColor);
    try {
    for (int i=0;i<SequenceTVCFC.length;i++) {
    	SequenceTVCFCCombobox.addItem(SequenceTVCFC[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    TVCFCColumn.setCellEditor(new DefaultCellEditor(SequenceTVCFCCombobox));
    
    TVCFCTargetCurveCombobox.setBackground(backgroundColor);
    try {
    for (int i=0;i<TargetCurve_Options_TVC.length;i++) {
    	TVCFCTargetCurveCombobox.addItem(TargetCurve_Options_TVC[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    TVCFCtargetCurveColumn.setCellEditor(new DefaultCellEditor(TVCFCTargetCurveCombobox));
    
    JScrollPane TABLE_SEQUENCE_ScrollPane = new JScrollPane(TABLE_SEQUENCE,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    //TABLE_SEQUENCE_ScrollPane.setLayout(null);
    TABLE_SEQUENCE_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
    TABLE_SEQUENCE_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
    TABLE_SEQUENCE_ScrollPane.setBackground(backgroundColor);
    //TABLE_SEQUENCE_ScrollPane.setSize(tablewidth3,tableheight3);
    //TABLE_SEQUENCE_ScrollPane.setOpaque(false);
    P2_SequenceMAIN.add(TABLE_SEQUENCE_ScrollPane, BorderLayout.PAGE_START);
    
    JPanel SequenceControlPanel = new JPanel();
    SequenceControlPanel.setLayout(null);
    SequenceControlPanel.setPreferredSize(new Dimension(400, 60));
    SequenceControlPanel.setBackground(backgroundColor);
    SequenceControlPanel.setForeground(labelColor);
    P2_SequenceMAIN.add(SequenceControlPanel, BorderLayout.PAGE_END);

    JButton BUTTON_AddSequence = new JButton("Add Sequence");
    BUTTON_AddSequence.setLocation(5, 5);
    BUTTON_AddSequence.setSize(145,25);
    BUTTON_AddSequence.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  AddSequence();
    	  } } );
    SequenceControlPanel.add(BUTTON_AddSequence);
    JButton BUTTON_DeleteSequence = new JButton("Delete Sequence");
    BUTTON_DeleteSequence.setLocation(5, 32);
    BUTTON_DeleteSequence.setSize(145,25);
    BUTTON_DeleteSequence.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  DeleteSequence();
    	  } } );
    SequenceControlPanel.add(BUTTON_DeleteSequence);
    
    JButton BUTTON_UpSequence = new JButton("Sequence Up");
    BUTTON_UpSequence.setLocation(155, 5);
    BUTTON_UpSequence.setSize(145,25);
    BUTTON_UpSequence.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  UpSequence();
    	  } } );
    SequenceControlPanel.add(BUTTON_UpSequence);
    JButton BUTTON_RemoveAllSequence = new JButton("Remove All");
    BUTTON_RemoveAllSequence.setLocation(325, 5);
    BUTTON_RemoveAllSequence.setSize(145,25);
    BUTTON_RemoveAllSequence.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  DeleteAllSequence();
    	  } } );
    SequenceControlPanel.add(BUTTON_RemoveAllSequence);
    JButton BUTTON_DownSequence = new JButton("Sequence down");
    BUTTON_DownSequence.setLocation(155, 32);
    BUTTON_DownSequence.setSize(145,25);
    BUTTON_DownSequence.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  DownSequence();
    	  } } );
    SequenceControlPanel.add(BUTTON_DownSequence);
    JButton BUTTON_AddFC = new JButton("Add Flight Controller");
    BUTTON_AddFC.setLocation(325, 32);
    BUTTON_AddFC.setSize(145,25);
    BUTTON_AddFC.setEnabled(false);
    BUTTON_AddFC.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  
    	  } } );
    SequenceControlPanel.add(BUTTON_AddFC);
    
    JButton BUTTON_ImportFC = new JButton("Import Flight Controller");
    BUTTON_ImportFC.setLocation(475, 5);
    BUTTON_ImportFC.setSize(145,25);
    BUTTON_ImportFC.setEnabled(false);
    BUTTON_ImportFC.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  
    	  } } );
    SequenceControlPanel.add(BUTTON_ImportFC);
    
    JButton BUTTON_ExportFC = new JButton("Export Flight Controller");
    BUTTON_ExportFC.setLocation(475, 32);
    BUTTON_ExportFC.setSize(145,25);
    BUTTON_ExportFC.setEnabled(false);
    BUTTON_ExportFC.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  
    	  } } );
    SequenceControlPanel.add(BUTTON_ExportFC);
    
    
    JButton BUTTON_AddController = new JButton("Add Controller");
    BUTTON_AddController.setLocation(655, 5);
    BUTTON_AddController.setSize(145,25);
    BUTTON_AddController.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  AddController();
    	  } } );
    SequenceControlPanel.add(BUTTON_AddController);
    JButton BUTTON_DeleteController = new JButton("Delete Controller");
    BUTTON_DeleteController.setLocation(655, 32);
    BUTTON_DeleteController.setSize(145,25);
    BUTTON_DeleteController.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  DeleteController();
    	  } } );
    SequenceControlPanel.add(BUTTON_DeleteController);
    
    JButton BUTTON_AddError = new JButton("Add Error");
    BUTTON_AddError.setLocation(805, 5);
    BUTTON_AddError.setSize(145,25);
    BUTTON_AddError.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  AddError();
    	  } } );
    SequenceControlPanel.add(BUTTON_AddError);
    JButton BUTTON_DeleteError = new JButton("Delete Error");
    BUTTON_DeleteError.setLocation(805, 32);
    BUTTON_DeleteError.setSize(145,25);
    BUTTON_DeleteError.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  DeleteError();
    	  } } );
    SequenceControlPanel.add(BUTTON_DeleteError);
    //-----------------------------------------------------------------------------------------------------------------------------
    //                  Sequence table 
    //-----------------------------------------------------------------------------------------------------------------------------
    JPanel Pane_Sequence = new JPanel();
    Pane_Sequence.setLocation(0, 0);
    Pane_Sequence.setPreferredSize(new Dimension(900, 200));
    Pane_Sequence.setLayout(new BorderLayout());
    Pane_Sequence.setBackground(backgroundColor);
    Pane_Sequence.setForeground(labelColor);
    PANEL_RIGHT_InputSection.add(Pane_Sequence, BorderLayout.CENTER);

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
    
    
    
    
    
    
	    TABLE_CONTROLLER = new JTable(){
	   	 
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
	    
		Action action4 = new AbstractAction()
	    {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
	        {WriteControllerINP();}
	    };
	    @SuppressWarnings("unused")
		TableCellListener tcl4 = new TableCellListener(TABLE_CONTROLLER, action4);
	    MODEL_CONTROLLER = new DefaultTableModel(){

			private static final long serialVersionUID = 1L;

			@Override
	        public boolean isCellEditable(int row, int column) {
	           //all cells false
				String Ctrl_type = (String) MODEL_CONTROLLER.getValueAt(row, 1);
				if (column == 0 ){
					return false;
				} else if (column==5 && Ctrl_type.equals("1") ){
					return false; 
				}  else if (column==6 && Ctrl_type.equals("1") ){
					return false; 
				}  else {
					return true; 
				}
	        }
	    }; 
	    MODEL_CONTROLLER.setColumnIdentifiers(COLUMS_CONTROLLER);
	    TABLE_CONTROLLER.setModel(MODEL_CONTROLLER);
	    TABLE_CONTROLLER.setBackground(backgroundColor);
	    TABLE_CONTROLLER.setBackground(backgroundColor);
	    TABLE_CONTROLLER.setForeground(labelColor);
	    TABLE_CONTROLLER.getTableHeader().setReorderingAllowed(false);
	    TABLE_CONTROLLER.setRowHeight(45);


		    TableColumn CtrId_colum   			 = TABLE_CONTROLLER.getColumnModel().getColumn(0);
		    TableColumn CtrType_column 	    	 = TABLE_CONTROLLER.getColumnModel().getColumn(1);
		    TableColumn CtrPGain_column  		 = TABLE_CONTROLLER.getColumnModel().getColumn(2);
		    TableColumn CtrIGain_column 	     = TABLE_CONTROLLER.getColumnModel().getColumn(3);
		    TableColumn CtrDGain_column 	  	 = TABLE_CONTROLLER.getColumnModel().getColumn(4);
		    TableColumn CtrMin_column 	  		 = TABLE_CONTROLLER.getColumnModel().getColumn(5);
		    TableColumn CtrMax_column 	  		 = TABLE_CONTROLLER.getColumnModel().getColumn(6);

		    
		    TABLE_CONTROLLER.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    CtrId_colum.setPreferredWidth(50);
		    CtrType_column.setPreferredWidth(100);
		    CtrPGain_column.setPreferredWidth(100);
		    CtrIGain_column.setPreferredWidth(100);
		    CtrDGain_column.setPreferredWidth(100);
		    CtrMin_column.setPreferredWidth(120);
		    CtrMax_column.setPreferredWidth(120);

		    
		    ((JTable) TABLE_CONTROLLER).setFillsViewportHeight(true);
	    
		    TABLE_CONTROLLER.getTableHeader().setBackground(backgroundColor);
		    TABLE_CONTROLLER.getTableHeader().setForeground(labelColor);
		    
		    JScrollPane TABLE_CONTROLLER_ScrollPane = new JScrollPane(TABLE_CONTROLLER,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		    //TABLE_SEQUENCE_ScrollPane.setLayout(null);
		    TABLE_CONTROLLER_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
		    TABLE_CONTROLLER_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
		    TABLE_CONTROLLER_ScrollPane.setBackground(backgroundColor);
		    //TABLE_SEQUENCE_ScrollPane.setSize(tablewidth3,tableheight3);
		    //TABLE_SEQUENCE_ScrollPane.setOpaque(false);
		   // P2_ControllerPane.add(TABLE_CONTROLLER_ScrollPane, BorderLayout.CENTER);
		    //PANEL_RIGHT_InputSection.add(TABLE_CONTROLLER_ScrollPane, BorderLayout.CENTER);
		    SplitPane_Page2_Charts_HorizontalSplit.add(TABLE_CONTROLLER_ScrollPane, JSplitPane.LEFT);
		    
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
					Update_ErrorIndicator();
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
			    
			    ErrorTypeCombobox.setBackground(backgroundColor);
			    try {
			    for (int i=0;i<ErrorType.length;i++) {
			    	ErrorTypeCombobox.addItem(ErrorType[i]);
			    }
			    } catch(NullPointerException eNPE) {
			    	System.out.println(eNPE);
			    }
			    ErrorType_column.setCellEditor(new DefaultCellEditor(ErrorTypeCombobox));
			    
			    JScrollPane TABLE_ERROR_ScrollPane = new JScrollPane(TABLE_ERROR,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			    TABLE_ERROR_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
			    TABLE_ERROR_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
			    TABLE_ERROR_ScrollPane.setBackground(backgroundColor);
			    SplitPane_Page2_Charts_HorizontalSplit.add(TABLE_ERROR_ScrollPane, JSplitPane.RIGHT);
			    
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
			      AerodynamicLeftPanel.add(LABELdragModel);
			      
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
							AerodynamicLeftPanel.remove(AeroLeftBarAdditionalComponents.get(i));
						}
						AerodynamicLeftPanel.revalidate();
						AerodynamicLeftPanel.repaint();
						if(indx==0) {	
						      JLabel LABEL_CD = new JLabel("Set constant CD value [-]");
						      LABEL_CD.setLocation(193, 5 + 25 * 1);
						      LABEL_CD.setSize(300, 20);
						      LABEL_CD.setBackground(backgroundColor);
						      LABEL_CD.setForeground(labelColor);
						      LABEL_CD.setFont(small_font);
						      AeroLeftBarAdditionalComponents.add(LABEL_CD);
						      AerodynamicLeftPanel.add(LABEL_CD);
							
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
					        AerodynamicLeftPanel.add(ConstantCD_INPUT);       
						}
						//------------------------------------------------------------------------
					}
			    	  
			      });
			      //aeroButton.setSelected(true);
			      DragModelSet.add(aeroButton);
			      //aeroButton.setHorizontalAlignment(0);
			      AerodynamicLeftPanel.add(aeroButton);
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
								AerodynamicLeftPanel.remove(AeroLeftBarAdditionalComponents.get(i));
							}
							AerodynamicLeftPanel.revalidate();
							AerodynamicLeftPanel.repaint();
							if(indx==1) {							
							     INPUT_RB = new JTextField(10);
							     double value = readFromFile(SC_file, 2);
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
										WRITE_SC();
									}
							   	  
							     });
							     AerodynamicLeftPanel.add(INPUT_RB);
						        
							      JLabel LABEL_RB = new JLabel("Heat Shield Body Radius RB [m]");
							      LABEL_RB.setLocation(193, 5 + 25 * 2);
							      LABEL_RB.setSize(300, 20);
							      LABEL_RB.setFont(small_font);
							      LABEL_RB.setBackground(backgroundColor);
							      LABEL_RB.setForeground(labelColor);
							      AeroLeftBarAdditionalComponents.add(LABEL_RB);
						       // RB_INPUT.setBackground(Color.lightGray);
						        AerodynamicLeftPanel.add(LABEL_RB);       
							}
							//------------------------------------------------------------------------
					}
			    	  
			      });
			     // aeroButton.setHorizontalAlignment(0);
			      AerodynamicLeftPanel.add(aeroButton);
			      dragModelGroup.add(aeroButton);

			      // System.out.println(dragModelGroup.getSelection().);
		//-----------------------------------------------------------------------------------------
	    // ---->>>>>                       TAB: Spacecraft Definition
		//-----------------------------------------------------------------------------------------			    
			    
			    // Main (SUB) tabbed Pane for this page
		        JTabbedPane TabPane_SCDefinition = (JTabbedPane) new JTabbedPane();
		        TabPane_SCDefinition.setPreferredSize(new Dimension(extx_main, exty_main));
		        TabPane_SCDefinition.setBackground(backgroundColor);
		        TabPane_SCDefinition.setForeground(Color.BLACK);
		//-------------------------------------------------------------------------------------------	    
		        // Main panels for each page 
				JPanel InertiaxPanel = new JPanel();
				InertiaxPanel.setLocation(0, 0);
				InertiaxPanel.setBackground(backgroundColor);
				InertiaxPanel.setForeground(labelColor);
				InertiaxPanel.setSize(400, 600);
				InertiaxPanel.setLayout(null); 
	    		
			      JPanel PropulsionInputPanel = new JPanel();
			      PropulsionInputPanel.setLocation(0, uy_p41 + 26 * 38 );
			      PropulsionInputPanel.setSize(SidePanel_Width, 750);
			      PropulsionInputPanel.setBackground(backgroundColor);
			      PropulsionInputPanel.setForeground(Color.white);
			      PropulsionInputPanel.setLayout(null);
			
				
			     	if(OS_is==2) {
			     		// Resize image icons for Windows 
			         	 int size=10;
			         	icon_setup2 = new ImageIcon(getScaledImage(icon_setup2.getImage(),size,size));
			         	icon_inertia = new ImageIcon(getScaledImage(icon_inertia.getImage(),size,size));
			         	icon_aerodynamic = new ImageIcon(getScaledImage(icon_aerodynamic.getImage(),size,size));
			      }
			      
				TabPane_SCDefinition.addTab("Basic" , icon_setup2, PropulsionInputPanel, null);
				TabPane_SCDefinition.addTab("Attitude and Inertia" , icon_inertia, InertiaxPanel, null);
				TabPane_SCDefinition.addTab("Aerodynamic" , icon_aerodynamic, AerodynamicInputPanel, null);
				PageX04_AttitudeSetup.add(TabPane_SCDefinition);
		        TabPane_SCDefinition.setSelectedIndex(0);
		        TabPane_SCDefinition.setFont(small_font);
				
	    //-------------------------------------------------------------------------------------------	
		 // Inertia and Attitude       
		        
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
				
				int box_size_x = 60;
				int box_size_y = 25;
				int gap_size_x =  4;
				int gap_size_y =  15;
				
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
			      
			      //------------------------------------------------------------------------------------
			      // Initial 
			      
			      int box_size_InitialAttitude_x = 130;
			      int box_size_InitialAttitude_y = 25;
			      
					JPanel InitialAttitudePanel = new JPanel();
					InitialAttitudePanel.setLayout(null);
					InitialAttitudePanel.setLocation(350, 10);
					InitialAttitudePanel.setBackground(backgroundColor);
					InitialAttitudePanel.setForeground(labelColor);
					InitialAttitudePanel.setBorder(Moon_border);
					InitialAttitudePanel.setSize(400, 400);
					InertiaxPanel.add(InitialAttitudePanel);
					
				      JLabel LABEL_Quarternions = new JLabel("Quarternion Representation");
				      LABEL_Quarternions.setLocation(2, 2);
				      LABEL_Quarternions.setSize(150, 20);
				      LABEL_Quarternions.setBackground(backgroundColor);
				      LABEL_Quarternions.setForeground(labelColor);
				      LABEL_Quarternions.setFont(small_font);
				      LABEL_Quarternions.setHorizontalAlignment(0);
				      InitialAttitudePanel.add(LABEL_Quarternions);
					
				      JLabel LABEL_Quarternion1 = new JLabel("Quarternion e1");
				      LABEL_Quarternion1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*0 - 15+45);
				      LABEL_Quarternion1.setSize(box_size_InitialAttitude_x, 20);
				      LABEL_Quarternion1.setBackground(backgroundColor);
				      LABEL_Quarternion1.setForeground(labelColor);
				      LABEL_Quarternion1.setFont(small_font);
				      LABEL_Quarternion1.setHorizontalAlignment(0);
				      InitialAttitudePanel.add(LABEL_Quarternion1);

			        
			        INPUT_Quarternion1 = new JTextField();
			        INPUT_Quarternion1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*0+45);
			        INPUT_Quarternion1.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_Quarternion1.setBorder(Moon_border);
			        INPUT_Quarternion1.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
			        INPUT_Quarternion1.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInitialAttitude();
							//------------------------------------------------------------------------------
							double[][] qvector= {{Double.parseDouble(INPUT_Quarternion1.getText())},
												 {Double.parseDouble(INPUT_Quarternion2.getText())},
												 {Double.parseDouble(INPUT_Quarternion3.getText())},
												 {Double.parseDouble(INPUT_Quarternion4.getText())}};
							double[][] EulerAngles = Mathbox.Quaternions2Euler2(qvector);
							DecimalFormat numberFormat = new DecimalFormat("#.0000000");
							INPUT_Euler1.setText(numberFormat.format(EulerAngles[0][0]*rad2deg));
							INPUT_Euler2.setText(numberFormat.format(EulerAngles[1][0]*rad2deg));
							INPUT_Euler3.setText(numberFormat.format(EulerAngles[2][0]*rad2deg));
							//------------------------------------------------------------------------------
						}
				    	  
				      });
			        InitialAttitudePanel.add(INPUT_Quarternion1);	
			        
				      JLabel LABEL_Quarternion2 = new JLabel("Quarternion e2");
				      LABEL_Quarternion2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*1 - 15+45);
				      LABEL_Quarternion2.setSize(box_size_InitialAttitude_x, 20);
				      LABEL_Quarternion2.setBackground(backgroundColor);
				      LABEL_Quarternion2.setForeground(labelColor);
				      LABEL_Quarternion2.setFont(small_font);
				      LABEL_Quarternion2.setHorizontalAlignment(0);
				      InitialAttitudePanel.add(LABEL_Quarternion2);

			        
			        INPUT_Quarternion2 = new JTextField();
			        INPUT_Quarternion2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*1+45);
			        INPUT_Quarternion2.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_Quarternion2.setBorder(Moon_border);
			        INPUT_Quarternion2.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
			        INPUT_Quarternion2.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInitialAttitude();
							//------------------------------------------------------------------------------
							double[][] qvector= {{Double.parseDouble(INPUT_Quarternion1.getText())},
												 {Double.parseDouble(INPUT_Quarternion2.getText())},
												 {Double.parseDouble(INPUT_Quarternion3.getText())},
												 {Double.parseDouble(INPUT_Quarternion4.getText())}};
							double[][] EulerAngles = Mathbox.Quaternions2Euler2(qvector);
							DecimalFormat numberFormat = new DecimalFormat("#.0000000");
							INPUT_Euler1.setText(numberFormat.format(EulerAngles[0][0]*rad2deg));
							INPUT_Euler2.setText(numberFormat.format(EulerAngles[1][0]*rad2deg));
							INPUT_Euler3.setText(numberFormat.format(EulerAngles[2][0]*rad2deg));
							//------------------------------------------------------------------------------
						}
				    	  
				      });
			        InitialAttitudePanel.add(INPUT_Quarternion2);
			        
				      JLabel LABEL_Quarternion3 = new JLabel("Quarternion e3");
				      LABEL_Quarternion3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*2 - 15+45);
				      LABEL_Quarternion3.setSize(box_size_InitialAttitude_x, 20);
				      LABEL_Quarternion3.setBackground(backgroundColor);
				      LABEL_Quarternion3.setForeground(labelColor);
				      LABEL_Quarternion3.setFont(small_font);
				      LABEL_Quarternion3.setHorizontalAlignment(0);
				      InitialAttitudePanel.add(LABEL_Quarternion3);

			        
			        INPUT_Quarternion3 = new JTextField();
			        INPUT_Quarternion3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*2+45);
			        INPUT_Quarternion3.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_Quarternion3.setBorder(Moon_border);
			        INPUT_Quarternion3.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
			        INPUT_Quarternion3.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInitialAttitude();
							//------------------------------------------------------------------------------
							double[][] qvector= {{Double.parseDouble(INPUT_Quarternion1.getText())},
												 {Double.parseDouble(INPUT_Quarternion2.getText())},
												 {Double.parseDouble(INPUT_Quarternion3.getText())},
												 {Double.parseDouble(INPUT_Quarternion4.getText())}};
							double[][] EulerAngles = Mathbox.Quaternions2Euler2(qvector);
							DecimalFormat numberFormat = new DecimalFormat("#.0000000");
							INPUT_Euler1.setText(numberFormat.format(EulerAngles[0][0]*rad2deg));
							INPUT_Euler2.setText(numberFormat.format(EulerAngles[1][0]*rad2deg));
							INPUT_Euler3.setText(numberFormat.format(EulerAngles[2][0]*rad2deg));
							//------------------------------------------------------------------------------
						}
				    	  
				      });
			        InitialAttitudePanel.add(INPUT_Quarternion3);
			        
				      JLabel LABEL_Quarternion4 = new JLabel("Quarternion e4");
				      LABEL_Quarternion4.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*3 - 15+45);
				      LABEL_Quarternion4.setSize(box_size_InitialAttitude_x, 20);
				      LABEL_Quarternion4.setBackground(backgroundColor);
				      LABEL_Quarternion4.setForeground(labelColor);
				      LABEL_Quarternion4.setFont(small_font);
				      LABEL_Quarternion4.setHorizontalAlignment(0);
				      InitialAttitudePanel.add(LABEL_Quarternion4);

			        
			        INPUT_Quarternion4 = new JTextField();
			        INPUT_Quarternion4.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*3+45);
			        INPUT_Quarternion4.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_Quarternion4.setBorder(Moon_border);
			        INPUT_Quarternion4.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
			        INPUT_Quarternion4.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInitialAttitude();
							//------------------------------------------------------------------------------
							double[][] qvector= {{Double.parseDouble(INPUT_Quarternion1.getText())},
												 {Double.parseDouble(INPUT_Quarternion2.getText())},
												 {Double.parseDouble(INPUT_Quarternion3.getText())},
												 {Double.parseDouble(INPUT_Quarternion4.getText())}};
							double[][] EulerAngles = Mathbox.Quaternions2Euler2(qvector);
							DecimalFormat numberFormat = new DecimalFormat("#.0000000");
							INPUT_Euler1.setText(numberFormat.format(EulerAngles[0][0]*rad2deg));
							INPUT_Euler2.setText(numberFormat.format(EulerAngles[1][0]*rad2deg));
							INPUT_Euler3.setText(numberFormat.format(EulerAngles[2][0]*rad2deg));
							//------------------------------------------------------------------------------
						}
				    	  
				      });
			        InitialAttitudePanel.add(INPUT_Quarternion4);
			        
			        
				      JLabel LABEL_Euler = new JLabel("Euler Angle Representation");
				      LABEL_Euler.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*4 +45);
				      LABEL_Euler.setSize(150, 20);
				      LABEL_Euler.setBackground(backgroundColor);
				      LABEL_Euler.setForeground(labelColor);
				      LABEL_Euler.setFont(small_font);
				      LABEL_Euler.setHorizontalAlignment(0);
				      InitialAttitudePanel.add(LABEL_Euler);
			        
				      JLabel LABEL_Euler1 = new JLabel("Euler E1 - Roll [deg]");
				      LABEL_Euler1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*5 - 15+45);
				      LABEL_Euler1.setSize(box_size_InitialAttitude_x, 20);
				      LABEL_Euler1.setBackground(backgroundColor);
				      LABEL_Euler1.setForeground(labelColor);
				      LABEL_Euler1.setFont(small_font);
				      LABEL_Euler1.setHorizontalAlignment(0);
				      InitialAttitudePanel.add(LABEL_Euler1);

				         sliderEuler1 = GuiComponents.getGuiSlider(small_font, (int) (box_size_InitialAttitude_x*1.9), -180, 0 ,180);
				         sliderEuler2 = GuiComponents.getGuiSlider(small_font, (int) (box_size_InitialAttitude_x*1.9), -90, 0 ,90);
				         sliderEuler3 = GuiComponents.getGuiSlider(small_font, (int) (box_size_InitialAttitude_x*1.9), -180, 0 ,180);
				        sliderEuler1.setValue(0);
				        sliderEuler2.setValue(0);
				        sliderEuler3.setValue(0);
				        sliderEuler1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*5-10+45);
				       
				       sliderEuler1.addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent arg0) {
							
							double[][] Euler = {{0},{0},{0}};
							Euler[0][0] = Double.parseDouble(INPUT_Euler1.getText());
							Euler[1][0] = Double.parseDouble(INPUT_Euler2.getText());
							Euler[2][0] = Double.parseDouble(INPUT_Euler3.getText());
							double[][] Quarternions = Mathbox.Euler2Quarternions(Euler);
							INPUT_Quarternion1.setText(""+decQuarternion.format(Quarternions[0][0]));
							INPUT_Quarternion2.setText(""+decQuarternion.format(Quarternions[1][0]));
							INPUT_Quarternion3.setText(""+decQuarternion.format(Quarternions[2][0]));
							INPUT_Quarternion4.setText(""+decQuarternion.format(Quarternions[3][0]));

							INPUT_Euler1.setText(""+sliderEuler1.getValue());
						}
				    	   
				       });
				        InitialAttitudePanel.add(sliderEuler1);
				       
				        sliderEuler2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*6-10+45);
					       sliderEuler2.addChangeListener(new ChangeListener() {

								@Override
								public void stateChanged(ChangeEvent arg0) {
									
									double[][] Euler = {{0},{0},{0}};
									Euler[0][0] = Double.parseDouble(INPUT_Euler1.getText());
									Euler[1][0] = Double.parseDouble(INPUT_Euler2.getText());
									Euler[2][0] = Double.parseDouble(INPUT_Euler3.getText());
									double[][] Quarternions = Mathbox.Euler2Quarternions(Euler);
									INPUT_Quarternion1.setText(""+decQuarternion.format(Quarternions[0][0]));
									INPUT_Quarternion2.setText(""+decQuarternion.format(Quarternions[1][0]));
									INPUT_Quarternion3.setText(""+decQuarternion.format(Quarternions[2][0]));
									INPUT_Quarternion4.setText(""+decQuarternion.format(Quarternions[3][0]));
									
									INPUT_Euler2.setText(""+sliderEuler2.getValue());
								}
						    	   
						       });
				        InitialAttitudePanel.add(sliderEuler2);
				        
				        sliderEuler3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*7-10+45);
					       sliderEuler3.addChangeListener(new ChangeListener() {

								@Override
								public void stateChanged(ChangeEvent arg0) {
									
									double[][] Euler = {{0},{0},{0}};
									Euler[0][0] = Double.parseDouble(INPUT_Euler1.getText());
									Euler[1][0] = Double.parseDouble(INPUT_Euler2.getText());
									Euler[2][0] = Double.parseDouble(INPUT_Euler3.getText());
									double[][] Quarternions = Mathbox.Euler2Quarternions(Euler);
									INPUT_Quarternion1.setText(""+decQuarternion.format(Quarternions[0][0]));
									INPUT_Quarternion2.setText(""+decQuarternion.format(Quarternions[1][0]));
									INPUT_Quarternion3.setText(""+decQuarternion.format(Quarternions[2][0]));
									INPUT_Quarternion4.setText(""+decQuarternion.format(Quarternions[3][0]));
									
									INPUT_Euler3.setText(""+sliderEuler3.getValue());
								}
						    	   
						       });
				        InitialAttitudePanel.add(sliderEuler3);
			        
			        INPUT_Euler1 = new JTextField();
			        INPUT_Euler1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*5+45);
			        INPUT_Euler1.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_Euler1.setBorder(Moon_border);
			        INPUT_Euler1.setText("0");
			        INPUT_Euler1.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
			        INPUT_Euler1.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInitialAttitude();
							sliderEuler1.setValue(Integer.parseInt(INPUT_Euler1.getText()));
						}
				    	  
				      });
			        INPUT_Euler1.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							WriteInitialAttitude();
							if( Double.parseDouble(INPUT_Euler1.getText())>=-90 && Double.parseDouble(INPUT_Euler1.getText())<=90) {
							sliderEuler1.setValue((int) Double.parseDouble(INPUT_Euler1.getText()));
							}
						}
			        	
			        });
			        InitialAttitudePanel.add(INPUT_Euler1);
			        
			        
				      JLabel LABEL_Euler2 = new JLabel("Euler E2 - Pitch [deg]");
				      LABEL_Euler2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*6 - 15+45);
				      LABEL_Euler2.setSize(box_size_InitialAttitude_x, 20);
				      LABEL_Euler2.setBackground(backgroundColor);
				      LABEL_Euler2.setForeground(labelColor);
				      LABEL_Euler2.setFont(small_font);
				      LABEL_Euler2.setHorizontalAlignment(0);
				      InitialAttitudePanel.add(LABEL_Euler2);

			        
			        INPUT_Euler2 = new JTextField();
			        INPUT_Euler2.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*6+45);
			        INPUT_Euler2.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_Euler2.setBorder(Moon_border);
			        INPUT_Euler2.setText("0");
			        INPUT_Euler2.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
			        INPUT_Euler2.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInitialAttitude();
							sliderEuler2.setValue(Integer.parseInt(INPUT_Euler2.getText()));
						}
				    	  
				      });
			        INPUT_Euler2.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							WriteInitialAttitude();
							if( Double.parseDouble(INPUT_Euler2.getText())>=-90 && Double.parseDouble(INPUT_Euler2.getText())<=90) {
							sliderEuler2.setValue((int) Double.parseDouble(INPUT_Euler2.getText()));
							}
						}
			        	
			        });
			        InitialAttitudePanel.add(INPUT_Euler2);
			        
			        
				      JLabel LABEL_Euler3 = new JLabel("Euler E3 - Yaw [deg]");
				      LABEL_Euler3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*7 - 15+45);
				      LABEL_Euler3.setSize(box_size_InitialAttitude_x, 20);
				      LABEL_Euler3.setBackground(backgroundColor);
				      LABEL_Euler3.setForeground(labelColor);
				      LABEL_Euler3.setFont(small_font);
				      LABEL_Euler3.setHorizontalAlignment(0);
				      InitialAttitudePanel.add(LABEL_Euler3);

			        
			        INPUT_Euler3 = new JTextField();
			        INPUT_Euler3.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*7+45);
			        INPUT_Euler3.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_Euler3.setBorder(Moon_border);
			        INPUT_Euler3.setText("0");
			        INPUT_Euler3.setSize(box_size_InitialAttitude_x, box_size_InitialAttitude_y);
			        INPUT_Euler3.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInitialAttitude();
							sliderEuler3.setValue(Integer.parseInt(INPUT_Euler3.getText()));
						}
				    	  
				      });
			        INPUT_Euler3.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							WriteInitialAttitude();
							if( Double.parseDouble(INPUT_Euler3.getText())>=-90 && Double.parseDouble(INPUT_Euler3.getText())<=90) {
							sliderEuler3.setValue((int) Double.parseDouble(INPUT_Euler3.getText()));
							}
						}
			        	
			        });
			        InitialAttitudePanel.add(INPUT_Euler3);
			        
					JPanel SpaceShip3DPanel = new JPanel();
					SpaceShip3DPanel.setLayout(new BorderLayout());
					SpaceShip3DPanel.setLocation(765, 10);
					//SpaceShip3DPanel.setBackground(backgroundColor);
					//SpaceShip3DPanel.setForeground(labelColor);
					SpaceShip3DPanel.setSize(450, 400);
					SpaceShip3DPanel.setBorder(Moon_border);
					InertiaxPanel.add(SpaceShip3DPanel);
					
					
			        final JFXPanel fxPanel = new JFXPanel();
			        SpaceShip3DPanel.add(fxPanel, BorderLayout.CENTER);
			        Platform.runLater(new Runnable() {
			            @Override
			            public void run() {
			            	SpaceShipView3D.start(fxPanel);
			            }
			       });
		        //---------------------------------------------------------------------------------------------
		        //                         Propulsion Definition Block
		        //--------------------------------------------------------------------------------------------- 
			 		      
		      JSeparator Separator_Page2_2 = new JSeparator();
		      Separator_Page2_2.setLocation(0, 0 );
		      Separator_Page2_2.setSize(SidePanel_Width, 1);
		      Separator_Page2_2.setBackground(Color.black);
		      Separator_Page2_2.setForeground(labelColor);
		      PropulsionInputPanel.add(Separator_Page2_2);

		        String path3 = "images/mercuryBlueprint.png";
		        File file3 = new File(path3);
		        try {
		        BufferedImage image4 = ImageIO.read(file3);
		        JLabel label3 = new JLabel(new ImageIcon(image4));
		        label3.setSize(560,430);
		        label3.setLocation(435, 5);
		        label3.setBorder(Moon_border);
		        PropulsionInputPanel.add(label3);
		        } catch (Exception e) {
		        	System.err.println("Error: SpaceShip Setup/Basic Setup - could not load image");
		        }
			  // Space intended for advanced integrator settings 
		      JLabel LABEL_SpaceCraftSettings = new JLabel("Spacecraft Settings");
		      LABEL_SpaceCraftSettings.setLocation(0, uy_p41 + 10 * 0  );
		      LABEL_SpaceCraftSettings.setSize(400, 20);
		      LABEL_SpaceCraftSettings.setBackground(backgroundColor);
		      LABEL_SpaceCraftSettings.setForeground(labelColor);
		      LABEL_SpaceCraftSettings.setFont(HeadlineFont);
		      LABEL_SpaceCraftSettings.setHorizontalAlignment(0);
		      PropulsionInputPanel.add(LABEL_SpaceCraftSettings);
		      JLabel LABEL_Minit = new JLabel("Initial mass [kg]");
		      LABEL_Minit.setLocation(INPUT_width+5, uy_p41 + 25 * 1 );
		      LABEL_Minit.setSize(250, 20);
		      LABEL_Minit.setBackground(backgroundColor);
		      LABEL_Minit.setForeground(labelColor);
		      PropulsionInputPanel.add(LABEL_Minit);
		      JLabel LABEL_ME_ISP = new JLabel("Main propulsion system ISP [s]");
		      LABEL_ME_ISP.setLocation(INPUT_width+5, uy_p41 + 25 * 3 );
		      LABEL_ME_ISP.setSize(300, 20);
		      LABEL_ME_ISP.setBackground(backgroundColor);
		      LABEL_ME_ISP.setForeground(labelColor);
		      PropulsionInputPanel.add(LABEL_ME_ISP);
		      JLabel LABEL_ME_PropMass = new JLabel("Main propulsion system propellant mass [kg]");
		      LABEL_ME_PropMass.setLocation(INPUT_width+5, uy_p41 + 25 * 4);
		      LABEL_ME_PropMass.setSize(300, 20);
		      LABEL_ME_PropMass.setBackground(backgroundColor);
		      LABEL_ME_PropMass.setForeground(labelColor);
		      PropulsionInputPanel.add(LABEL_ME_PropMass);
		      JLabel LABEL_ME_Thrust_max = new JLabel("Main propulsion system max. Thrust [N]");
		      LABEL_ME_Thrust_max.setLocation(INPUT_width+5, uy_p41 + 25 * 5 );
		      LABEL_ME_Thrust_max.setSize(300, 20);
		      LABEL_ME_Thrust_max.setBackground(backgroundColor);
		      LABEL_ME_Thrust_max.setForeground(labelColor);
		      PropulsionInputPanel.add(LABEL_ME_Thrust_max);
		      JLabel LABEL_ME_Thrust_min = new JLabel("Main Propulsion system min. Thrust [N]");
		      LABEL_ME_Thrust_min.setLocation(INPUT_width+5, uy_p41 + 25 * 6 );
		      LABEL_ME_Thrust_min.setSize(300, 20);
		      LABEL_ME_Thrust_min.setBackground(backgroundColor);
		      LABEL_ME_Thrust_min.setForeground(labelColor);
		      PropulsionInputPanel.add(LABEL_ME_Thrust_min);
		      
		      JLabel LABEL_ME_ISP_Model = new JLabel("Include dynamic ISP model in throttled state");
		      LABEL_ME_ISP_Model.setLocation(INPUT_width+5, uy_p41 + 25 * 7 );
		      LABEL_ME_ISP_Model.setSize(300, 20);
		      LABEL_ME_ISP_Model.setBackground(backgroundColor);
		      LABEL_ME_ISP_Model.setForeground(labelColor);
		      PropulsionInputPanel.add(LABEL_ME_ISP_Model);
		      
		      JLabel LABEL_ME_ISP_min = new JLabel("ISP for maximum throttled state [s]");
		      LABEL_ME_ISP_min.setLocation(INPUT_width+5, uy_p41 + 25 * 8 );
		      LABEL_ME_ISP_min.setSize(300, 20);
		      LABEL_ME_ISP_min.setBackground(backgroundColor);
		      LABEL_ME_ISP_min.setForeground(labelColor);
		      PropulsionInputPanel.add(LABEL_ME_ISP_min);
		     
			 
		      
		      INPUT_M0 = new JTextField(10);
		      INPUT_M0.setLocation(2, uy_p41 + 25 * 1 );
		      INPUT_M0.setSize(INPUT_width, 20);
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
		      PropulsionInputPanel.add(INPUT_M0);
		      INPUT_ISP = new JTextField(10);
		      INPUT_ISP.setLocation(2, uy_p41 + 25 * 3 );
		      INPUT_ISP.setSize(INPUT_width, 20);
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
		      PropulsionInputPanel.add(INPUT_ISP);
		     INPUT_PROPMASS = new JTextField(10);
		     INPUT_PROPMASS.setLocation(2, uy_p41 + 25 * 4);
		     INPUT_PROPMASS.setSize(INPUT_width, 20);
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
		     PropulsionInputPanel.add(INPUT_PROPMASS);        
		     INPUT_THRUSTMAX = new JTextField(10);
		     INPUT_THRUSTMAX.setLocation(2, uy_p41 + 25 * 5 );
		     INPUT_THRUSTMAX.setSize(INPUT_width, 20);
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
		     PropulsionInputPanel.add(INPUT_THRUSTMAX);
		     INPUT_THRUSTMIN = new JTextField(10);
		     INPUT_THRUSTMIN.setLocation(2, uy_p41 + 25 * 6 );;
		     INPUT_THRUSTMIN.setSize(INPUT_width, 20);
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
		     PropulsionInputPanel.add(INPUT_THRUSTMIN);
		     
		     INPUT_ISPMODEL = new JCheckBox();
		     INPUT_ISPMODEL.setLocation(INPUT_width+5-20, uy_p41 + 25 * 7+2);
		     INPUT_ISPMODEL.setSize(15, 15);
		     INPUT_ISPMODEL.setSelected(true);
		     INPUT_ISPMODEL.addItemListener(new ItemListener() {
		       	 public void itemStateChanged(ItemEvent e) {
		       		WRITE_PROP();
		       	 }
		                  });
		     INPUT_ISPMODEL.setHorizontalAlignment(0);
		     PropulsionInputPanel.add(INPUT_ISPMODEL);
		     
		     
		     INPUT_ISPMIN = new JTextField(10);
		     INPUT_ISPMIN.setLocation(2, uy_p41 + 25 * 8 );;
		     INPUT_ISPMIN.setSize(INPUT_width, 20);
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
		     PropulsionInputPanel.add(INPUT_ISPMIN);
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
		      
		      
			     INPUT_SURFACEAREA = new JTextField(10);
			     INPUT_BALLISTICCOEFFICIENT = new JTextField(10);

			     INPUT_SURFACEAREA.setLocation(2, uy_p41 + 25 * 1 );;
			     INPUT_SURFACEAREA.setSize(INPUT_width, 20);
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
				     
		      
		        String path = "images/milleniumSchlieren2.png";
		        File file = new File(path);
		        try {
		        BufferedImage image2 = ImageIO.read(file);
		        JLabel label = new JLabel(new ImageIcon(image2));
		        label.setSize(300,260);
		        label.setLocation(5, uy_p41 + 25 * 6);
		        label.setBorder(Moon_border);
		        AerodynamicInputPanel.add(label);
		        } catch (Exception e) {
		        	System.err.println("Error: SpaceShip Setup/Aerodynamik - could not load image");
		        }

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
       CreateChart_DashboardOverviewChart_Altitude_Velocity(RM);
       //CreateChart_DashboardOverviewChart_Time_FPA();

     	CreateChart_DashBoardFlexibleChart();
     	createChart_3DRotation();
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
     	 icon_dashboard = new ImageIcon("images/comet.png","");
     	 icon_scSetup = new ImageIcon("images/startup.png","");
     	 icon_setup = new ImageIcon("images/setup.png","");
     	 icon_data = new ImageIcon("images/data.png","");
     	 icon_map = new ImageIcon("images/map.png","");
     	} else if(OS_is==2) {
     	//	For Windows image icons have to be resized
        	 icon_dashboard = new ImageIcon("images/comet.png","");
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
        Page04_subtabPane.addTab("Dashboard" , icon_dashboard, PageX04_Dashboard, null);
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
    	       createTargetView3D();
    		READ_CONTROLLER();
  	      		UpdateFC_LIST();
    		READ_SEQUENCE();
    		READ_ERROR();
    		READ_INERTIA() ;
    		READ_InitialAttitude();
    		Update_ErrorIndicator();
    	      Rotating2Inertial();
    	      Update_IntegratorSettings();

        MainGUI.setOpaque(true);
        return MainGUI;
	}
    public void actionPerformed(ActionEvent e)  {
    	
    }
    
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
    public static void Rotating2Inertial() {
    	try {
    	double vel_rotating = Double.parseDouble(INPUT_VEL_Rs.getText());
    	double fpa_rotating = Double.parseDouble(INPUT_FPA_Rs.getText())*deg2rad;
    	double azi_rotating = Double.parseDouble(INPUT_AZI_Rs.getText())*deg2rad;
    	double lat_rotating = Double.parseDouble(INPUT_LAT_Rs.getText())*deg2rad;
    	double rm = DATA_MAIN[indx_target][0];
    	double omega = DATA_MAIN[indx_target][2];
    	double radius = Double.parseDouble(INPUT_ALT_Rs.getText())+rm;
    	double azimuth_inertFrame = Math.atan((vel_rotating*Math.cos(fpa_rotating)*Math.sin(azi_rotating)+omega*radius+Math.cos(lat_rotating))/(vel_rotating*Math.cos(fpa_rotating)*Math.cos(azi_rotating)));
    	double fpa_inertFrame = Math.atan(Math.tan(fpa_rotating)*Math.cos(azimuth_inertFrame)/Math.cos(azi_rotating));
    	double vel_inertFrame = vel_rotating * Math.sin(fpa_rotating)/Math.sin(fpa_inertFrame);
    	INPUT_AZI_Is.setText(""+df_VelVector.format(azimuth_inertFrame*rad2deg));
    	INPUT_FPA_Is.setText(""+df_VelVector.format(fpa_inertFrame*rad2deg));
    	INPUT_VEL_Is.setText(""+df_VelVector.format(vel_inertFrame));
    	} catch(java.lang.NumberFormatException enfe) {System.out.println(enfe);}
    }
    
    @SuppressWarnings("unchecked")
	public static void UpdateFC_LIST() {
    	try {
    	SequenceTVCFCCombobox.removeAllItems();
    	SequenceFCCombobox.removeAllItems();
    	} catch(NullPointerException | java.lang.ArrayIndexOutOfBoundsException eNPE) {
    		System.out.println("ERROR: Removing Combobox items failed");
    	}
    	SequenceTVCFCCombobox.addItem("");
    	SequenceFCCombobox.addItem("");
    	for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {
    		SequenceTVCFCCombobox.addItem("PID "+(i+1));
    		SequenceFCCombobox.addItem("PID "+(i+1));
    		}
    }

    public static void Inertial2Rotating() {
    	try {
    	double vel_inert = Double.parseDouble(INPUT_VEL_Is.getText());
    	double fpa_inert = Double.parseDouble(INPUT_FPA_Is.getText())*deg2rad;
    	double azi_inert = Double.parseDouble(INPUT_AZI_Is.getText())*deg2rad;
    	double lat_rotating = Double.parseDouble(INPUT_LAT_Rs.getText())*deg2rad;
    	double rm = DATA_MAIN[indx_target][0];
    	double omega = DATA_MAIN[indx_target][2];
    	double radius = Double.parseDouble(INPUT_ALT_Rs.getText())+rm;
    	double azimuth_rotFrame   = Math.atan(Math.tan(azi_inert)-omega*radius*Math.cos(lat_rotating)/(vel_inert*Math.cos(fpa_inert)*Math.cos(azi_inert)));
    	double fpa_rotFrame 	  = Math.atan(Math.tan(fpa_inert)*Math.cos(azimuth_rotFrame)/Math.cos(azi_inert));
    	double vel_rotFrame	      = vel_inert*Math.sin(fpa_inert)/Math.sin(fpa_rotFrame);
    	INPUT_AZI_Rs.setText(""+df_VelVector.format(azimuth_rotFrame*rad2deg));
    	INPUT_FPA_Rs.setText(""+df_VelVector.format(fpa_rotFrame*rad2deg));
    	INPUT_VEL_Rs.setText(""+df_VelVector.format(vel_rotFrame));
    	} catch(java.lang.NumberFormatException enfe) {System.out.println(enfe);}
    }
    
    public static void AddSequence() {
    	int NumberOfSequences = MODEL_SEQUENCE.getRowCount();
    	ROW_SEQUENCE[0]  = ""+NumberOfSequences;
    	ROW_SEQUENCE[1]  = ""+SequenceENDType[0];
    	ROW_SEQUENCE[2]  = "0";
    	ROW_SEQUENCE[3]  = ""+SequenceType[0];
    	ROW_SEQUENCE[4]  = ""+SequenceFC[0];
    	ROW_SEQUENCE[5]  = "1";
    	ROW_SEQUENCE[6]  = "1";
    	ROW_SEQUENCE[7]  = ""+FCTargetCurve[0];	
    	ROW_SEQUENCE[8]  = ""+SequenceTVCFC[0];
    	ROW_SEQUENCE[9]  = "1";
    	ROW_SEQUENCE[10] = "1";
    	ROW_SEQUENCE[11] = ""+TargetCurve_Options_TVC[0];	
    	MODEL_SEQUENCE.addRow(ROW_SEQUENCE);
    	
    	for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);}    	
    	WriteSequenceINP();
    }
    
    public static void DeleteSequence() {
    	int j = TABLE_SEQUENCE.getSelectedRow();
    	if (j >= 0){MODEL_SEQUENCE.removeRow(j);}
    	for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);}
    	
    	WriteSequenceINP();
    }
    public static void DeleteAllSequence() {
    	for(int j=MODEL_SEQUENCE.getRowCount()-1;j>=0;j--) {MODEL_SEQUENCE.removeRow(j);}
    	WriteSequenceINP();
    }
    
    public static void UpSequence() {
        int[] rows2 = TABLE_SEQUENCE.getSelectedRows();
        MODEL_SEQUENCE.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]-1);
        TABLE_SEQUENCE.setRowSelectionInterval(rows2[0]-1, rows2[rows2.length-1]-1);
        for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);}
        
        WriteSequenceINP();
    }
    
    public static void DownSequence() {
        int[] rows2 = TABLE_SEQUENCE.getSelectedRows();
        MODEL_SEQUENCE.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]+1);
        TABLE_SEQUENCE.setRowSelectionInterval(rows2[0]+1, rows2[rows2.length-1]+1);
        for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);}
        
        WriteSequenceINP();
    }
    
    public static void AddController() {
    	int NumberOfSequences = MODEL_CONTROLLER.getRowCount();
    	ROW_CONTROLLER[0]  = ""+NumberOfSequences;
    	ROW_CONTROLLER[1]  = "0";
    	ROW_CONTROLLER[2]  = "1";
    	ROW_CONTROLLER[3]  = "1";
    	ROW_CONTROLLER[4]  = "1";
    	ROW_CONTROLLER[5]  = "0";
    	ROW_CONTROLLER[6]  = "1";

    	MODEL_CONTROLLER.addRow(ROW_CONTROLLER);
    	
    	for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);}    	
    	WriteControllerINP();
    	UpdateFC_LIST();
    }
    
    public static void DeleteController() {
    	int j = TABLE_CONTROLLER.getSelectedRow();
    	if (j >= 0){MODEL_CONTROLLER.removeRow(j);}
    	for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);}
    	
    	WriteControllerINP();
    	UpdateFC_LIST();
    }
    public static void DeleteAllController() {
    	for(int j=TABLE_CONTROLLER.getRowCount()-1;j>=0;j--) {MODEL_CONTROLLER.removeRow(j);}
    	WriteControllerINP();
    	UpdateFC_LIST();
    }
    
    public static void UpController() {
        int[] rows2 = TABLE_CONTROLLER.getSelectedRows();
        MODEL_CONTROLLER.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]-1);
        TABLE_CONTROLLER.setRowSelectionInterval(rows2[0]-1, rows2[rows2.length-1]-1);
        for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);}
        
        WriteControllerINP();
        UpdateFC_LIST();
    }
    
    public static void DownController() {
        int[] rows2 = TABLE_SEQUENCE.getSelectedRows();
        MODEL_CONTROLLER.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]+1);
        TABLE_CONTROLLER.setRowSelectionInterval(rows2[0]+1, rows2[rows2.length-1]+1);
        for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);}
        
        WriteControllerINP();
        UpdateFC_LIST();
    }
    public static void AddError() {
    	int NumberOfSequences = MODEL_ERROR.getRowCount();
    	ROW_ERROR[0]  = ""+NumberOfSequences;
    	ROW_ERROR[1]  = ""+ErrorType[0];
    	ROW_ERROR[2]  = "0";
    	ROW_ERROR[3]  = "0";

    	MODEL_ERROR.addRow(ROW_ERROR);
    	
    	for(int i=0;i<MODEL_ERROR.getRowCount();i++) {MODEL_ERROR.setValueAt(""+(i+1),i, 0);}    	
    	WriteErrorINP();
    	Update_ErrorIndicator();
    }
    
    public static void DeleteError() {
    	int j = TABLE_ERROR.getSelectedRow();
    	if (j >= 0){MODEL_ERROR.removeRow(j);}
    	for(int i=0;i<MODEL_ERROR.getRowCount();i++) {MODEL_ERROR.setValueAt(""+(i+1),i, 0);}
    	
    	WriteErrorINP();
    	Update_ErrorIndicator();
    }
    
    public static void Update_ErrorIndicator() {
    	if(MODEL_ERROR.getRowCount()>0) {
    		Error_Indicator.setText("Induced Error ON");
    		Error_Indicator.setBackground(Color.red);
    		Error_Indicator.setForeground(Color.red);
    	} else {
    		Error_Indicator.setText("Induced Error OFF");
    		Error_Indicator.setBackground(backgroundColor);
    		Error_Indicator.setForeground(labelColor);
    	}
    	Module_Indicator.setText(""+AscentDescent_SwitchChooser.getSelectedItem()); 
    }
    public static void Update_IntegratorSettings() {
    	if(Integrator_chooser.getSelectedIndex()==0) {
    		// Dormand Prince 853 Integrator 
    		// 4 Inputs 
    		LABEL_IntegratorSetting_01.setText("Min. step [s]");
    		LABEL_IntegratorSetting_02.setText("Max. step [s]");
    		LABEL_IntegratorSetting_03.setText("Abs. Tolerance []");
    		LABEL_IntegratorSetting_04.setText("Rel. Tolerance []");
    		LABEL_IntegratorSetting_05.setText("");
    		//-------------------------------------------------------
    		INPUT_IntegratorSetting_05.setText("");
    		INPUT_IntegratorSetting_01.setEditable(true);
    		INPUT_IntegratorSetting_02.setEditable(true);
    		INPUT_IntegratorSetting_03.setEditable(true);
    		INPUT_IntegratorSetting_04.setEditable(true);
    		INPUT_IntegratorSetting_05.setEditable(false);
    		
    	} else if (Integrator_chooser.getSelectedIndex()==1) {
    		// Standard Runge Kutta Integrator 
    		// 1 input 
    		LABEL_IntegratorSetting_01.setText("Step size [s]");
    		LABEL_IntegratorSetting_02.setText("");
    		LABEL_IntegratorSetting_03.setText("");
    		LABEL_IntegratorSetting_04.setText("");
    		LABEL_IntegratorSetting_05.setText("");
    		//-------------------------------------------------------
    		INPUT_IntegratorSetting_02.setText("");
    		INPUT_IntegratorSetting_03.setText("");
    		INPUT_IntegratorSetting_04.setText("");
    		INPUT_IntegratorSetting_05.setText("");
    		INPUT_IntegratorSetting_01.setEditable(true);
    		INPUT_IntegratorSetting_02.setEditable(false);
    		INPUT_IntegratorSetting_03.setEditable(false);
    		INPUT_IntegratorSetting_04.setEditable(false);
    		INPUT_IntegratorSetting_05.setEditable(false);
    	} else if (Integrator_chooser.getSelectedIndex()==2) {
    		// Gragg Bulirsch Stoer Integrator 
    		// 4 Inputs 
    		LABEL_IntegratorSetting_01.setText("Min. step [s]");
    		LABEL_IntegratorSetting_02.setText("Max. step [s]");
    		LABEL_IntegratorSetting_03.setText("Abs. Tolerance []");
    		LABEL_IntegratorSetting_04.setText("Rel. Tolerance []");
    		LABEL_IntegratorSetting_05.setText("");
    		//-------------------------------------------------------
    		INPUT_IntegratorSetting_05.setText("");
    		INPUT_IntegratorSetting_01.setEditable(true);
    		INPUT_IntegratorSetting_02.setEditable(true);
    		INPUT_IntegratorSetting_03.setEditable(true);
    		INPUT_IntegratorSetting_04.setEditable(true);
    		INPUT_IntegratorSetting_05.setEditable(false);
    	} else if (Integrator_chooser.getSelectedIndex()==3) {
    		// Adams Bashford Integrator 
    		// 5 Inputs 
    		LABEL_IntegratorSetting_01.setText("Steps [-]");
    		LABEL_IntegratorSetting_02.setText("Min. step [s]");
    		LABEL_IntegratorSetting_03.setText("Max. step [s]");
    		LABEL_IntegratorSetting_04.setText("Abs. Tolerance []");
    		LABEL_IntegratorSetting_05.setText("Rel. Tolerance []");
    		INPUT_IntegratorSetting_01.setEditable(true);
    		INPUT_IntegratorSetting_02.setEditable(true);
    		INPUT_IntegratorSetting_03.setEditable(true);
    		INPUT_IntegratorSetting_04.setEditable(true);
    		INPUT_IntegratorSetting_05.setEditable(true);
    	} else {
    		System.out.println("Selected integrator not recognized");
    		LABEL_IntegratorSetting_01.setText("");
    		LABEL_IntegratorSetting_02.setText("");
    		LABEL_IntegratorSetting_03.setText("");
    		LABEL_IntegratorSetting_04.setText("");
    		LABEL_IntegratorSetting_05.setText("");
    		//-------------------------------------------------------
    		INPUT_IntegratorSetting_01.setText("");
    		INPUT_IntegratorSetting_02.setText("");
    		INPUT_IntegratorSetting_03.setText("");
    		INPUT_IntegratorSetting_04.setText("");
    		INPUT_IntegratorSetting_05.setText("");
    		INPUT_IntegratorSetting_01.setEditable(false);
    		INPUT_IntegratorSetting_02.setEditable(false);
    		INPUT_IntegratorSetting_03.setEditable(false);
    		INPUT_IntegratorSetting_04.setEditable(false);
    		INPUT_IntegratorSetting_05.setEditable(false);
    	}
		LABEL_IntegratorSetting_05.requestFocusInWindow();
    }
    public void UPDATE_Page01(boolean fullImport){
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
		  if(fullImport) {
	    	CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity.removeAllSeries();
	    	CHART_P1_DashBoardOverviewChart_Dataset_Time_FPA.removeAllSeries();
	    	try {
	    	CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity = AddDataset_DashboardOverviewChart(RM);
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
	    		System.out.println(eFNF2);
	    	System.out.println("ERROR: Dashboard chart could not be created. Add dataset failed");
	    	}
	    	ResultSet_MercatorMap.removeAllSeries();
	    	try {
	    	ResultSet_MercatorMap = AddDataset_Mercator_MAP();
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
	    		
	    	}
	    	Update_DashboardFlexibleChart();
      	    		try {
	        	    		result11_A3_1.removeAllSeries();
	        	    		result11_A3_2.removeAllSeries();
	        	    		result11_A3_3.removeAllSeries();
	        	    		result11_A3_4.removeAllSeries();
							UpdateChart_A01();
						} catch (ArrayIndexOutOfBoundsException | NullPointerException | IOException
								| URISyntaxException  e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
      	    		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
      	    		System.out.println("Updated "+timeStamp);
		  }
	  } 

    public static void READ_SEQUENCE() throws IOException{	
		BufferedReader br = new BufferedReader(new FileReader(SEQUENCE_File));
       String strLine;
       try {
       while ((strLine = br.readLine()) != null )   {
	       	String[] tokens = strLine.split(" ");
	       	int sequence_ID 			= Integer.parseInt(tokens[0]);
	       	int trigger_end_type 		= Integer.parseInt(tokens[1]);
	       	double trigger_end_value 	= Double.parseDouble(tokens[2]);
	       	int sequence_type		 	= Integer.parseInt(tokens[3]);
	       	int sequence_controller_ID 	= Integer.parseInt(tokens[4]);
	       	double ctrl_target_vel      = Double.parseDouble(tokens[5]);
	       	double ctrl_target_alt 		= Double.parseDouble(tokens[6]);
	       	int ctrl_target_curve       = Integer.parseInt(tokens[7]);
	    	ROW_SEQUENCE[0] = ""+sequence_ID;
	       	try {
	    	ROW_SEQUENCE[1] = ""+SequenceENDType[trigger_end_type];
	       	} catch ( java.lang.ArrayIndexOutOfBoundsException eAU) {System.out.println("ERROR: Reading sequence file - index out of bounds. " );}
	    	ROW_SEQUENCE[2] = ""+trigger_end_value;
	       	try {
	    	ROW_SEQUENCE[3] = ""+SequenceType[sequence_type-1];
	       	} catch ( java.lang.ArrayIndexOutOfBoundsException eAU) {System.out.println("ERROR: Reading sequence file - index out of bounds. " );}
	       	try {
	    	ROW_SEQUENCE[4] = ""+SequenceFCCombobox.getItemAt(sequence_controller_ID-1);
	       	} catch ( java.lang.ArrayIndexOutOfBoundsException eAU) {System.out.println("ERROR: Reading sequence file - index out of bounds. " );}
	    	ROW_SEQUENCE[5] = ""+ctrl_target_vel;
	    	ROW_SEQUENCE[6] = ""+ctrl_target_alt;
	       	try {
	    	ROW_SEQUENCE[7] = ""+FCTargetCurve[ctrl_target_curve-1];		
	       	} catch ( java.lang.ArrayIndexOutOfBoundsException eAU) {System.out.println("ERROR: Reading sequence file - index out of bounds. " );}
				       	try {
					       	int TVCsequence_controller_ID   = Integer.parseInt(tokens[8]);
					       	double ctrl_target_x_TVC        = Double.parseDouble(tokens[9]);
					       	double ctrl_target_y_TVC 		= Double.parseDouble(tokens[10]);
					       	int ctrl_TVC_target_curve       = Integer.parseInt(tokens[11]);
					    	ROW_SEQUENCE[8]  = "" + SequenceTVCFCCombobox.getItemAt(TVCsequence_controller_ID-1);
					    	ROW_SEQUENCE[9]  = "" + ctrl_target_x_TVC;
					    	ROW_SEQUENCE[10] = "" + ctrl_target_y_TVC*rad2deg;
					    	ROW_SEQUENCE[11] = "" + TargetCurve_Options_TVC[ctrl_TVC_target_curve-1];
				       	} catch(java.lang.ArrayIndexOutOfBoundsException eAIOOBE) {System.out.println("No TVC controller found in Sequence file.");}
	       	
				    	MODEL_SEQUENCE.addRow(ROW_SEQUENCE);
	    	for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);} // Update numbering
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}

   }
    
    public static void READ_CONTROLLER() throws IOException{	
		BufferedReader br = new BufferedReader(new FileReader(CONTROLLER_File));
       String strLine;
       try {
       while ((strLine = br.readLine()) != null )   {
	       	String[] tokens = strLine.split(" ");
	    	ROW_CONTROLLER[0] = ""+Integer.parseInt(tokens[0]);
	    	ROW_CONTROLLER[1] = ""+Integer.parseInt(tokens[1]);
	    	ROW_CONTROLLER[2] = ""+Double.parseDouble(tokens[2]);
	    	ROW_CONTROLLER[3] = ""+Double.parseDouble(tokens[3]);
	    	ROW_CONTROLLER[4] = ""+Double.parseDouble(tokens[4]);
	    	ROW_CONTROLLER[5] = ""+Double.parseDouble(tokens[5]);
	    	ROW_CONTROLLER[6] = ""+Double.parseDouble(tokens[6]);
				    	MODEL_CONTROLLER.addRow(ROW_CONTROLLER);
	    	for(int i=0;i<MODEL_CONTROLLER.getRowCount();i++) {MODEL_CONTROLLER.setValueAt(""+(i+1),i, 0);} // Update numbering
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}

   }
    
    public static void READ_ERROR() throws IOException{	
		BufferedReader br = new BufferedReader(new FileReader(ERROR_File));
       String strLine;
       try {
       while ((strLine = br.readLine()) != null )   {
	       	String[] tokens = strLine.split(" ");
	    	ROW_ERROR[0] = "0";
	    	try {
	    	ROW_ERROR[1] = ""+ ErrorType[Integer.parseInt(tokens[0])];
	    	} catch(java.lang.ArrayIndexOutOfBoundsException eA) { System.out.println("Read Error file failed: Array Index out of Bounds.");}
	    	ROW_ERROR[2] = ""+Double.parseDouble(tokens[1]);
	    	ROW_ERROR[3] = ""+Double.parseDouble(tokens[2]);
				    	MODEL_ERROR.addRow(ROW_ERROR);
	    	for(int i=0;i<MODEL_ERROR.getRowCount();i++) {MODEL_ERROR.setValueAt(""+(i+1),i, 0);} // Update numbering
       }
       br.close();
       } catch(NullPointerException eNPE) { System.out.println(eNPE);}

   }
    
    public void READ_INERTIA() throws NumberFormatException, IOException {
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
    public void READ_INPUT() throws IOException{
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
        	String[] tokens = strLine.split(" ");
        	InitialState = Double.parseDouble(tokens[0]);
            if (k==0){
        		INDICATOR_LONG.setText(decf.format(InitialState));
        		INPUT_LONG_Rs.setText(df_X4.format(InitialState));
        	} else if (k==1){
        		INDICATOR_LAT.setText(decf.format( InitialState));
        		INPUT_LAT_Rs.setText(df_X4.format( InitialState));
        	} else if (k==2){
        		INDICATOR_ALT.setText(decf.format( InitialState));
        		INPUT_ALT_Rs.setText(decf.format( InitialState));
        		h_init = InitialState;
        	} else if (k==3){
        		INDICATOR_VEL.setText(decf.format(InitialState));
        		INPUT_VEL_Rs.setText(decf.format(InitialState));
        		v_init = InitialState;
        	} else if (k==4){
        		INDICATOR_FPA.setText(decf.format(InitialState));
        		INPUT_FPA_Rs.setText(df_X4.format(InitialState));
        	} else if (k==5){
        		INDICATOR_AZI.setText(decf.format(InitialState));
        		INPUT_AZI_Rs.setText(df_X4.format(InitialState));
        	} else if (k==6){
        		INDICATOR_M0.setText(decf.format(InitialState));
        		INPUT_M0.setText(decf.format(InitialState));
        		M0=InitialState;
        	} else if (k==7){
        		INDICATOR_INTEGTIME.setText(decf.format(InitialState));
        		//INPUT_INTEGTIME.setText(decf.format(InitialState));
        		 MODEL_EventHandler.setValueAt(decf.format(InitialState), 0, 1);
        	} else if (k==8){
        		int Integ_indx = (int) InitialState;
        		Integrator_chooser.setSelectedIndex(Integ_indx);
        } else if (k==9){
        		int Target_indx = (int) InitialState;
        		indx_target = (int) InitialState; 
        		RM = DATA_MAIN[indx_target][0];
        		INDICATOR_TARGET.setText(Target_Options[indx_target]);
        		Target_chooser.setSelectedIndex(Target_indx);
                if(indx_target==0) {
                	INDICATOR_TARGET.setBorder(Earth_border);
                } else if(indx_target==1){
                	INDICATOR_TARGET.setBorder(Moon_border);
                } else if(indx_target==2){
                	INDICATOR_TARGET.setBorder(Mars_border);
                } else if(indx_target==3){
                	INDICATOR_TARGET.setBorder(Venus_border);
                }
            } else if (k==10){
	            	INPUT_WRITETIME.setText(decf.format(InitialState)); // write dt
            } else if (k==11){
	            	INPUT_REFELEV.setText(decf.format(InitialState));  // Reference Elevation
		    } else if (k==12) {
	        		int Integ_indx = (int) InitialState;
	        		AscentDescent_SwitchChooser.setSelectedIndex(Integ_indx);
		    } else if (k==13) {
			    	int Integ_indx = (int) InitialState;
			    	if(Integ_indx==1) {
			    	SELECT_VelocitySpherical.setSelected(true);
			    	}else {
			    SELECT_VelocityCartesian.setSelected(true);
			    	}
		    } else if (k==14) {
			    	int Integ_indx = (int) InitialState;
			    	if(Integ_indx==3) {
			    		SELECT_3DOF.setSelected(true);
			    	}else if(Integ_indx==6){
			    		SELECT_6DOF.setSelected(true);
			    	}
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
  		Propellant_Mass=InitialState;
  	} else if (k==4){
  		int value = (int) InitialState; 
  		if(value==1) {INPUT_ISPMODEL.setSelected(true);}else {INPUT_ISPMODEL.setSelected(false);}
  	} else if (k==5){
  		INPUT_ISPMIN.setText(df_X4.format(InitialState)); 
  	} else if (k==6){

  	} else if (k==7){

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

	//System.out.println(RM);
	} else if (k==2){

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
	if(Integrator_chooser.getSelectedIndex()==0) {
		integ_file = INTEG_File_01; 
	} else if (Integrator_chooser.getSelectedIndex()==1) {
		integ_file = INTEG_File_02; 
	} else if (Integrator_chooser.getSelectedIndex()==2) {
		integ_file = INTEG_File_03; ;
	} else if (Integrator_chooser.getSelectedIndex()==3) {
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
    	INPUT_IntegratorSetting_01.setText(""+(InitialState)); 
  	} else if (k==1){
  		INPUT_IntegratorSetting_02.setText(""+(InitialState)); 
  	} else if (k==2){
  		INPUT_IntegratorSetting_03.setText(""+(InitialState)); 
  	} else if (k==3){
  		INPUT_IntegratorSetting_04.setText(""+(InitialState)); 
  	} else if (k==4){
  		INPUT_IntegratorSetting_05.setText(""+(InitialState)); 
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
		INPUT_RB.setText(""+(InitialState)); 
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
							    double[][] CartesianPosition = {{Double.parseDouble((String) tokens[78])},
			 							   						{Double.parseDouble((String) tokens[79])},
			 							   						{Double.parseDouble((String) tokens[80])}};
							    resultElement.setCartesianPosECEF(CartesianPosition);
							    resultElement.setEulerX(Float.parseFloat((String) tokens[87]));
							    resultElement.setEulerY(Float.parseFloat((String) tokens[88]));
							    resultElement.setEulerZ(Float.parseFloat((String) tokens[89]));
							    resultElement.setVelocity(Float.parseFloat((String) tokens[6]) );
							    resultElement.setTime(Float.parseFloat((String) tokens[0]));
							    resultElement.setFpa(Float.parseFloat((String) tokens[7]));
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
    
    public static void READ_INTEG() {
    	  //--------------------------------------------------------------------------------------------------------
    	  // Integrator settings 
    	  //--------------------------------------------------------------------------------------------------------
    		String integ_file = null;
    		 FileInputStream  fstream = null; 
    		if(Integrator_chooser.getSelectedIndex()==0) {
    			integ_file = INTEG_File_01; 
    		} else if (Integrator_chooser.getSelectedIndex()==1) {
    			integ_file = INTEG_File_02; 
    		} else if (Integrator_chooser.getSelectedIndex()==2) {
    			integ_file = INTEG_File_03; ;
    		} else if (Integrator_chooser.getSelectedIndex()==3) {
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
			    	INPUT_IntegratorSetting_01.setText(""+(InitialState)); 
			  	} else if (k==1){
			  		INPUT_IntegratorSetting_02.setText(""+(InitialState)); 
			  	} else if (k==2){
			  		INPUT_IntegratorSetting_03.setText(""+(InitialState)); 
			  	} else if (k==3){
			  		INPUT_IntegratorSetting_04.setText(""+(InitialState)); 
			  	} else if (k==4){
			  		INPUT_IntegratorSetting_05.setText(""+(InitialState)); 
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
		       		INPUT_Quarternion1.setText(tokens[0]);
		       	} else if (j==1) {
		       		INPUT_Quarternion2.setText(tokens[0]);
		       	} else if (j==2) {
		       		INPUT_Quarternion3.setText(tokens[0]);
		       	} else if (j==3) {
		       		INPUT_Quarternion4.setText(tokens[0]);
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
    		if(tokens[0].isEmpty()==false) {
    		 InitialState = Double.parseDouble(tokens[0]);
    		} else {
    			InitialState =0; 
    		}
    	 	if (k==indx){
    		  result = InitialState;
    		} 
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
	        Update_ErrorIndicator();
	     } catch (IOException eIO){
	     	System.out.println(eIO);
	     }
	}
	public static void WriteSequenceINP() {
	        try {
	            File fac = new File(SEQUENCE_File);
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
	            FileWriter wr = new FileWriter(fac);
	            for (int i=0; i<MODEL_SEQUENCE.getRowCount(); i++)
	            {
	        			String row ="";
	        			for(int j=0;j<MODEL_SEQUENCE.getColumnCount();j++) {
	        				if(j==0) {
	        					String val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					row = row + val + " ";
	        				}  else if(j==1) {
	        					String str_val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					int val = 0 ; 
	        					for(int k=0;k<SequenceENDType.length;k++) { if(str_val.equals(SequenceENDType[k])){val=k;} }
	        					row = row + val + " ";
	        				} else if(j==2) {
	        					String val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					row = row + val + " ";
	        				} else if(j==3) {
	        					String str_val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					int val = 0 ; 
	        					for(int k=0;k<SequenceType.length;k++) { if(str_val.equals(SequenceType[k])){val=k+1;} }
	        					row = row + val + " ";
	        				} else if(j==4) {
	        					String str_val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					int val = 0 ; 
	        					//System.out.println(""+SequenceFC.length);
	        					try {
	        					for(int k=0;k<SequenceFCCombobox.getItemCount();k++) { if(str_val.equals(SequenceFCCombobox.getItemAt(k) )){val=k+1;} }
	        					} catch (NullPointerException eNPE) {System.out.println(eNPE);}
	        					row = row + val + " ";
	        				} else if(j==5) {
	        					String val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					row = row + val + " ";
	        				} else if(j==6) {
	        					String val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					row = row + val + " ";
	        				} else if(j==7) {
	        					String str_val =  (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					int val = 0 ; 
	        					for(int k=0;k<FCTargetCurve.length;k++) { if(str_val.equals(FCTargetCurve[k])){val=k+1;} }
	        					row = row + val + " ";
	        				} else if(j==8) {
	        					String str_val =  "";
	        					try {
	        					str_val = (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					} catch (java.lang.NumberFormatException eNFE) {
	        						System.out.println(eNFE);
	        					}
	        					int val = 0 ; 
	        					try {
	        					for(int k=0;k<SequenceTVCFCCombobox.getItemCount();k++) { if(str_val.equals(SequenceTVCFCCombobox.getItemAt(k))){val=k+1;} }
	        					} catch (NullPointerException eNPE) {System.out.println(eNPE);}
	        					row = row + val + " ";
	        				} else if(j==9) {
	        					double val = 0 ; 
	        					try {
	        					 val =  Double.parseDouble((String) MODEL_SEQUENCE.getValueAt(i, j));
	        					} catch (java.lang.NumberFormatException | NullPointerException eNFE) {
	        						System.out.println(eNFE);
	        					}
	        					row = row + val + " ";
	        				} else if(j==10) {
	        					double val = 0 ; 
	        					try {
	        					 val =  Double.parseDouble((String) MODEL_SEQUENCE.getValueAt(i, j))*deg2rad;
	        					} catch (java.lang.NumberFormatException | NullPointerException eNFE) {
	        						System.out.println(eNFE);
	        					}
	        					row = row + val + " ";
	        				} else if(j==11) {
	        					String str_val =  "";
	        					try {
	        					str_val = (String) MODEL_SEQUENCE.getValueAt(i, j);
	        					} catch (java.lang.NumberFormatException eNFE) {System.out.println(eNFE);}
	        					int val=0;
	        					try {
	        					for(int k=0;k<TargetCurve_Options_TVC.length;k++) { if(str_val.equals(TargetCurve_Options_TVC[k])){val=k+1;} }
	        					} catch (NullPointerException eNPE) {System.out.println(eNPE);}
	        					row = row + val + " ";
	        				} 
	        		   }
	        			wr.write(row+System.getProperty( "line.separator" ));
	            }
	            wr.close(); 
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
					 if(INPUT_Quarternion1.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(INPUT_Quarternion1.getText()); 
					 }
				} else if(i==1) {
					 if(INPUT_Quarternion2.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(INPUT_Quarternion2.getText()); 
					 }
				}else if(i==2) {
					 if(INPUT_Quarternion3.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(INPUT_Quarternion3.getText()); 
					 }
				} else if(i==3) {
					 if(INPUT_Quarternion4.getText().equals("")) {
						 value =0;
					 } else {
						 value = Double.parseDouble(INPUT_Quarternion4.getText()); 
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
	public static void WriteControllerINP() {
	        try {
	            File fac = new File(CONTROLLER_File);
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
	            FileWriter wr = new FileWriter(fac);
	            for (int i=0; i<MODEL_CONTROLLER.getRowCount(); i++)
	            {
	        			String row ="";
	        			for(int j=0;j<MODEL_CONTROLLER.getColumnCount();j++) {
	    					String val =  (String) MODEL_CONTROLLER.getValueAt(i, j);
	    					row = row + val + " ";
	        		   }
	        			wr.write(row+System.getProperty( "line.separator" ));
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
            for (int i = 0; i<=15; i++)
            {
        		if (i == 0 ){
        			r = Double.parseDouble(INPUT_LONG_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==1 ){
        			r = Double.parseDouble(INPUT_LAT_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
        			r = Double.parseDouble(INPUT_ALT_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
        			r = Double.parseDouble(INPUT_VEL_Rs.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i == 4 ){
            		r = Double.parseDouble(INPUT_FPA_Rs.getText()) ;
            		wr.write(r+System.getProperty( "line.separator" ));	
        			} else if (i == 5 ){
                	r = Double.parseDouble(INPUT_AZI_Rs.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} else if (i == 6 ){
                	r = Double.parseDouble(INPUT_M0.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} else if (i == 7 ){
                    r = Double.parseDouble((String) MODEL_EventHandler.getValueAt( 0, 1)) ;
                    wr.write(r+System.getProperty( "line.separator" ));	
		    		} else if (i == 8 ){
		            rr =  Integrator_chooser.getSelectedIndex() ;
		            wr.write(rr+System.getProperty( "line.separator" ));	
		    		} else if (i == 9 ){
		            rr =  Target_chooser.getSelectedIndex() ;
		            wr.write(rr+System.getProperty( "line.separator" ));	
		    		} else if (i == 10 ){
		            r = Double.parseDouble(INPUT_WRITETIME.getText())  ; // delta-t write out
		            wr.write(r+System.getProperty( "line.separator" ));	
		    		} else if (i == 11 ){
			        r = Double.parseDouble(INPUT_REFELEV.getText())  ; // Reference elevation
			        wr.write(r+System.getProperty( "line.separator" ));	
		        } else if (i == 12) {
		            	rr =  AscentDescent_SwitchChooser.getSelectedIndex() ;
		            wr.write(rr+System.getProperty( "line.separator" ));	
		        } else if (i == 13) {
	                wr.write(VelocityCoordinateSystem+System.getProperty( "line.separator" ));	
		        } else if (i == 14) {
	                wr.write(DOF_System+System.getProperty( "line.separator" ));	
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
        			} else if (i ==2 ) {
        				if(INPUT_RB.getText().isEmpty()) {
		        			r = 0;
		        			wr.write(r+System.getProperty( "line.separator" ));
        				}else {
		        			r = Double.parseDouble(INPUT_RB.getText()) ;
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
    	if(Integrator_chooser.getSelectedIndex()==0) {
    		integ_file = INTEG_File_01; 
    		steps = 4; 
    	} else if (Integrator_chooser.getSelectedIndex()==1) {
    		integ_file = INTEG_File_02; 
    		steps =1;
    	} else if (Integrator_chooser.getSelectedIndex()==2) {
    		integ_file = INTEG_File_03; 
    		steps =4;
    	} else if (Integrator_chooser.getSelectedIndex()==3) {
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
        			r = Double.parseDouble(INPUT_IntegratorSetting_01.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==1) {
        			r = Double.parseDouble(INPUT_IntegratorSetting_02.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==2) {
        			r = Double.parseDouble(INPUT_IntegratorSetting_03.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==3) {
        			r = Double.parseDouble(INPUT_IntegratorSetting_04.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
            	} else if (i==4) {
        			r = Double.parseDouble(INPUT_IntegratorSetting_05.getText()) ;
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
            for (int i = 0; i<=12; i++)
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
            		} 
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
public static void IMPORT_Case() throws IOException {
	BufferedReader br = new BufferedReader(new FileReader(CurrentWorkfile_Path));
	DeleteAllSequence();
	DeleteAllController();
    String strLine;
    int indx_init=0;
    int indx_prop=0;
    int indx_sc=0;
    int data_column=2;              // Data column of one column data files [-]
    @SuppressWarnings("unused")
	int k = 0 ; 
    while ((strLine = br.readLine()) != null )   {
    	String[] tokens = strLine.split(" ");
    	if(tokens[0].equals("|INIT|")) {
				            if (indx_init==0){INPUT_LONG_Rs.setText(df_X4.format(Double.parseDouble(tokens[data_column])));
				  	 } else if (indx_init==1){INPUT_LAT_Rs.setText(df_X4.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==2){INPUT_ALT_Rs.setText(decf.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==3){INPUT_VEL_Rs.setText(decf.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==4){INPUT_FPA_Rs.setText(df_X4.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==5){INPUT_AZI_Rs.setText(df_X4.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==6){INPUT_M0.setText(decf.format(Double.parseDouble(tokens[data_column])));
				 	 } else if (indx_init==7){MODEL_EventHandler.setValueAt(tokens[data_column], 0, 1);
				 	 } else if (indx_init==8){Integrator_chooser.setSelectedIndex(Integer.parseInt(tokens[data_column]));
				     } else if (indx_init==9){Target_chooser.setSelectedIndex(Integer.parseInt(tokens[data_column]));
				     } else if (indx_init==10){INPUT_WRITETIME.setText(decf.format(Double.parseDouble(tokens[data_column])));
				     } else if (indx_init==11){INPUT_REFELEV.setText(decf.format(Double.parseDouble(tokens[data_column])));
					 } else if (indx_init==12){AscentDescent_SwitchChooser.setSelectedIndex(Integer.parseInt(tokens[data_column]));
					 } 					     
        indx_init++;
    	} else if(tokens[0].equals("|PROP|")) {
			            if (indx_prop==0){INPUT_ISP.setText(decf.format(Double.parseDouble(tokens[data_column])));
			  	 } else if (indx_prop==1){INPUT_PROPMASS.setText(decf.format(Double.parseDouble(tokens[data_column])));
			 	 } else if (indx_prop==2){INPUT_THRUSTMAX.setText(decf.format(Double.parseDouble(tokens[data_column])));
			 	 } else if (indx_prop==3){INPUT_THRUSTMIN.setText(decf.format(Double.parseDouble(tokens[data_column])));
			 	 } 			     
		indx_prop++;
	    } else if(tokens[0].equals("|SC|")) {
            if (indx_sc==0){INPUT_SURFACEAREA.setText(decf.format(Double.parseDouble(tokens[data_column])));
  	 } else if (indx_sc==1){INPUT_BALLISTICCOEFFICIENT.setText(decf.format(Double.parseDouble(tokens[data_column])));
 	 } else if (indx_sc==2){
 	 } else if (indx_sc==3){
 	 } 			     
            indx_sc++;
	    } 	else if(tokens[0].equals("|CTRL|")) {
	    	ROW_CONTROLLER[0] = ""+Integer.parseInt(tokens[1]);
	    	ROW_CONTROLLER[1] = ""+Integer.parseInt(tokens[2]);
	    	ROW_CONTROLLER[2] = ""+Double.parseDouble(tokens[3]);
	    	ROW_CONTROLLER[3] = ""+Double.parseDouble(tokens[4]);
	    	ROW_CONTROLLER[4] = ""+Double.parseDouble(tokens[5]);
	    	ROW_CONTROLLER[5] = ""+Double.parseDouble(tokens[6]);
	    	ROW_CONTROLLER[6] = ""+Double.parseDouble(tokens[7]);
	    	MODEL_CONTROLLER.addRow(ROW_CONTROLLER);
	    	WriteControllerINP();
	    	UpdateFC_LIST(); 
	    } else if(tokens[0].equals("|SEQU|")) {
       	int sequence_ID 				= Integer.parseInt(tokens[1]);
       	int trigger_end_type 			= Integer.parseInt(tokens[2]);
       	double trigger_end_value 		= Double.parseDouble(tokens[3]);
       	int sequence_type		 		= Integer.parseInt(tokens[4]);
       	int sequence_controller_ID 		= Integer.parseInt(tokens[5]);
       	double ctrl_target_vel      	= Double.parseDouble(tokens[6]);
       	double ctrl_target_alt 			= Double.parseDouble(tokens[7]);
       	int ctrl_target_curve       	= Integer.parseInt(tokens[8]);
       	try {
       	int sequence_TVCcontroller_ID 	= Integer.parseInt(tokens[9]);
       	double TVCctrl_target_t      	= Double.parseDouble(tokens[10]);
       	double TVCctrl_target_fpa 		= Double.parseDouble(tokens[11]);
       	int ctrl_TVC_target_curve    	= Integer.parseInt(tokens[12]);
	    	ROW_SEQUENCE[8]  = ""+SequenceTVCFCCombobox.getItemAt(sequence_TVCcontroller_ID-1);
	    	ROW_SEQUENCE[9]  = ""+TVCctrl_target_t;
	    	ROW_SEQUENCE[10] = ""+TVCctrl_target_fpa;
	    	ROW_SEQUENCE[11] = ""+TargetCurve_Options_TVC[ctrl_TVC_target_curve-1];	
       	} catch (java.lang.ArrayIndexOutOfBoundsException eAIOOBE) {System.out.println("No TVC controller found in Sequence file.");}
    	ROW_SEQUENCE[0] = ""+sequence_ID;
    	ROW_SEQUENCE[1] = ""+SequenceENDType[trigger_end_type];
    	ROW_SEQUENCE[2] = ""+trigger_end_value;
    	ROW_SEQUENCE[3] = ""+SequenceType[sequence_type-1];
    	ROW_SEQUENCE[4] = ""+SequenceFCCombobox.getItemAt(sequence_controller_ID-1);
    	ROW_SEQUENCE[5] = ""+ctrl_target_vel;
    	ROW_SEQUENCE[6] = ""+ctrl_target_alt;
    	ROW_SEQUENCE[7] = ""+FCTargetCurve[ctrl_target_curve-1];	
    	MODEL_SEQUENCE.addRow(ROW_SEQUENCE);
    	WriteSequenceINP();
	 }
    k++;
    }
    br.close();    
    WRITE_INIT();
    WRITE_PROP();
}
public static void EXPORT_Case() {
	if ( CurrentWorkfile_Name.isEmpty()==false) {
		File file = CurrentWorkfile_Path;
        PrintWriter os = null;
		try {
			os = new PrintWriter(file);
		} catch (FileNotFoundException e) {System.out.println(e);}
	
    	for (int i = 0; i < 12; i++) {  // 					init.inp
        os.print("|INIT|" + BB_delimiter);
                       if (i==0) {os.print("|LONGITUDE[DEG]|"+ BB_delimiter+INPUT_LONG_Rs.getText());
            	} else if (i==1) {os.print("|LATITUDE[DEG]|"+ BB_delimiter+INPUT_LAT_Rs.getText());
            	} else if (i==2) {os.print("|ALTITUDE[m]|"+ BB_delimiter+INPUT_ALT_Rs.getText());
            	} else if (i==3) {os.print("|VELOCITY[m/s]|"+ BB_delimiter+INPUT_VEL_Rs.getText());
            	} else if (i==4) {os.print("|FPA[DEG]|"+ BB_delimiter+INPUT_FPA_Rs.getText());
            	} else if (i==5) {os.print("|AZIMUTH[DEG]|"+ BB_delimiter+INPUT_AZI_Rs.getText());
            	} else if (i==6) {os.print("|INITMASS[kg]|"+ BB_delimiter+INPUT_M0.getText());
            	} else if (i==7) {os.print("|INTEGTIME[s]|"+ BB_delimiter+MODEL_EventHandler.getValueAt( 0, 1));
            	} else if (i==8) {os.print("|INTEG[-]|"+ BB_delimiter+Integrator_chooser.getSelectedIndex());
                } else if (i==9) {os.print("|TARGET[-]|"+ BB_delimiter+Target_chooser.getSelectedIndex());
                } else if (i==10){os.print("|WRITET[s]|"+ BB_delimiter+INPUT_WRITETIME.getText());
                } else if (i==11){os.print("|REFELEVEVATION[m]|"+ BB_delimiter+INPUT_REFELEV.getText());
    		    } else if (i==11){os.print("|ThrustSwitch[-]|"+ BB_delimiter+AscentDescent_SwitchChooser.getSelectedIndex());
    		    } 
                os.print(BB_delimiter);
    	os.println("");
    	}
    	
    	for (int i = 0; i < 10; i++) {  // 					prop.inp
    		os.print("|PROP|" + BB_delimiter);
		               if (i==0){os.print("|ISP[s]|"+ BB_delimiter+INPUT_ISP.getText());
		     	} else if (i==1){os.print("|PROPMASS[kg]|"+ BB_delimiter+INPUT_PROPMASS.getText());
		     	} else if (i==2){os.print("|THRUSTMAX[N]|"+ BB_delimiter+INPUT_THRUSTMAX.getText());
		     	} else if (i==3){os.print("|THRUSTMIN[N]|"+ BB_delimiter+INPUT_THRUSTMIN.getText());
		     	} else if (i==4){if(INPUT_ISPMODEL.isSelected()) {os.print("|ISPMODEL[-]|"+ BB_delimiter+1);} else {os.print("|ISPMODEL[-]|"+ BB_delimiter+0);}
		     	} else if (i==5){os.print("|ISPMIN[s]|"+ BB_delimiter+INPUT_ISPMIN.getText());
		    	}
	        os.println("");
    	}
        for (int  row2 = 0; row2 < MODEL_CONTROLLER.getRowCount(); row2++) {  // 					Sequence.inp
    		os.print("|CTRL|" + BB_delimiter);
    	    for (int col = 0; col < MODEL_CONTROLLER.getColumnCount(); col++) {
		        os.print(MODEL_CONTROLLER.getValueAt(row2, col)+ BB_delimiter);
    	    	}
    	    os.println("");  	    
    }	
    	for (int row = 0; row < MODEL_SEQUENCE.getRowCount(); row++) {  // 					Sequence.inp
    		os.print("|SEQU|" + BB_delimiter);
    	    for (int col = 0; col < MODEL_SEQUENCE.getColumnCount(); col++) {
    	    	if (col==1) {
    	    		String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
    	    		int val=0;
    	    		for(int k=0;k<SequenceENDType.length;k++) {if(str_val.equals(SequenceENDType[k])){val=k;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else if (col==3) {
    	    		String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
    	    		int val=0;
    	    		for(int k=0;k<SequenceType.length;k++) {if(str_val.equals(SequenceType[k])){val=k+1;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else if (col==4) {
    	    		String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
    	    		int val=0;
    	    		for(int k=0;k<SequenceFCCombobox.getItemCount();k++) {if(str_val.equals(SequenceFCCombobox.getItemAt(k))){val=k+1;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else if (col==7) {
    	    		String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
    	    		int val=0;
    	    		for(int k=0;k<FCTargetCurve.length;k++) {if(str_val.equals(FCTargetCurve[k])){val=k+1;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else if (col==8) {
        	    	String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
        	    	int val=0;
        	    	for(int k=0;k<SequenceTVCFCCombobox.getItemCount();k++) {if(str_val.equals(SequenceTVCFCCombobox.getItemAt(k))){val=k+1;}}
        	    	os.print(val+ BB_delimiter);
    	    	} else if (col==11) {
            	    String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
            	    int val=0;
            	    for(int k=0;k<TargetCurve_Options_TVC.length;k++) {if(str_val.equals(TargetCurve_Options_TVC[k])){val=k+1;}}
            	    os.print(val+ BB_delimiter);
    	    	} else {
		        os.print(MODEL_SEQUENCE.getValueAt(row, col)+ BB_delimiter);
    	    	}
    	    }
    	    os.println("");
    	}
    
    	for (int i = 0; i < 3; i++) {  // 					sc.inp
    		os.print("|SC|" + BB_delimiter);
		               if (i==0){
		            	   double r=0;
	        				if(INPUT_SURFACEAREA.getText().isEmpty()) {
			        			r = 0;
	        				}else {
			        			r = Double.parseDouble(INPUT_SURFACEAREA.getText()) ;
	        				}
		            	   os.print("|SURFACEAREA[m2]|"+ BB_delimiter+r);
		     	} else if (i==1){
		            	   double r=0;
	        				if(INPUT_BALLISTICCOEFFICIENT.getText().isEmpty()) {
			        			r = 0;
	        				}else {
			        			r = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText()) ;
	        				}
		            	   os.print("|BC[Kgm-2]|"+ BB_delimiter+r);
		     	} else if (i==2){
		     	} else if (i==3){
		     	} else if (i==4){
		     	} else if (i==5){
		    	}
	        os.println("");
    	}
       os.close();   
       System.out.println("File "+CurrentWorkfile_Name+" saved.");
	}
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
	
	public static DefaultTableXYDataset AddDataset_DashboardOverviewChart(double RM) throws IOException , FileNotFoundException, ArrayIndexOutOfBoundsException{
		ArrayList<String> SEQUENCE_DATA = new ArrayList<String>();
		SEQUENCE_DATA = Read_SEQU();
		
	   	XYSeries xyseries10 = new XYSeries("Target Trajectory", false, false); 
	   	XYSeries xyseries11 = new XYSeries("Trajectory", false, false); 
	   	
	   	XYSeries xyseries_FPA_is = new XYSeries("Flight Path Angle", false, false);
	   	XYSeries xyseries_FPA_cmd = new XYSeries("Flight Path Angle IDEAL", false, false);
	   	
	    FileInputStream fstream = null;
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	              DataInputStream in = new DataInputStream(fstream);
	              BufferedReader br = new BufferedReader(new InputStreamReader(in));
	              String strLine;
	              
	              try {
	              while ((strLine = br.readLine()) != null )   {
		           String[] tokens = strLine.split(" ");
		           double x = Double.parseDouble(tokens[6]);
		           double y = Double.parseDouble(tokens[3]);
		           
		           double t = Double.parseDouble(tokens[0]);
		           double fpa = Double.parseDouble(tokens[7])*rad2deg;
		           
		           double t_sequence = Double.parseDouble(tokens[41]);
		           
		           int active_sequence = Integer.parseInt(tokens[38]);
		           double xx=0;
		           double fpa_cmd = 0;
		           if(AscentDescent_SwitchChooser.getSelectedIndex()==0) {
		           INDICATOR_VTOUCHDOWN.setText(""+decf.format(Double.parseDouble(tokens[6])));
		           } else {
		           INDICATOR_VTOUCHDOWN.setText("-");   
		           }
		           INDICATOR_DELTAV.setText(""+decf.format(Double.parseDouble(tokens[37])));
		           INDICATOR_PROPPERC.setText(""+(decf.format(M0-Double.parseDouble(tokens[28])))); 
		           INDICATOR_RESPROP.setText(""+decf.format(Double.parseDouble(tokens[32])));
		           int active_sequ_type =0; double ctrl_vinit=0; double ctrl_hinit=0; double ctrl_vel=0; double ctrl_alt=0;int ctrlabelColorurve=0; double ctrl_fpa_init=0;int ctrl_TVC_curve=0;
		           double ctrl_t_end=0; double ctrl_fpa_end =0;
		           try {
		           String[] sequ_tokens  	 = SEQUENCE_DATA.get(active_sequence).split(" ");
		            active_sequ_type  	 	 = Integer.parseInt(sequ_tokens[1]);
		            ctrl_vinit 	 			 = Double.parseDouble(sequ_tokens[3]);
		            ctrl_hinit 	 			 = Double.parseDouble(sequ_tokens[4]);
		            ctrl_vel 	    	     = Double.parseDouble(sequ_tokens[5]);
		            ctrl_alt 		 		 = Double.parseDouble(sequ_tokens[6]);
		            ctrlabelColorurve           	 = Integer.parseInt(sequ_tokens[7]);
		            ctrl_TVC_curve		 	 = Integer.parseInt(sequ_tokens[8]);
		            ctrl_fpa_init			 = Double.parseDouble(sequ_tokens[9]);
		            ctrl_t_end				 = Double.parseDouble(sequ_tokens[10]);
		            ctrl_fpa_end			 = Double.parseDouble(sequ_tokens[11]);
		            
		           } catch (java.lang.IndexOutOfBoundsException eIOBE){
		        	   //System.out.println(eIOBE);
		        	   }
		           
		    		    if  (ctrl_TVC_curve==0) {
		    		    	xyseries_FPA_cmd.add(t  , 0); 
		   		        } else if (ctrl_TVC_curve==1) {
		   		        	fpa_cmd =    PitchCurve.SquareRootPitchCurve( 0,  ctrl_fpa_init, ctrl_t_end, ctrl_fpa_end, t_sequence);
		   		        	if(Double.isNaN(fpa_cmd)) {fpa_cmd=0;}
		  		             try { xyseries_FPA_cmd.add(t  , fpa_cmd*deg2rad); } catch(org.jfree.data.general.SeriesException eSE) {
		  		            	// System.out.println(eSE);
		  		            	 }
		    		    } else if (ctrl_TVC_curve==2) {
		    		    		fpa_cmd =   PitchCurve.LinearPitchCurve( 0,  ctrl_fpa_init, ctrl_t_end, ctrl_fpa_end, t_sequence);
		    		    		if(Double.isNaN(fpa_cmd)) {fpa_cmd=0;}
		    		    	 		try { xyseries_FPA_cmd.add(t , fpa_cmd*deg2rad); } catch(org.jfree.data.general.SeriesException eSE) {
		    		    	 			//System.out.println(eSE);
		    		    	 			}
		    		    } 
		           
		           //--------------------------------------------------------
		           if(active_sequ_type==3) { // Controlled Propulsive flight
		    		    if  (ctrlabelColorurve==0) {
		    		    		 xyseries10.add(x  , 0); 
		   		        } else if (ctrlabelColorurve==1) {
		    		         xx =    LandingCurve.ParabolicLandingCurve(ctrl_vinit, ctrl_hinit, ctrl_vel, ctrl_alt, y);
		    		         //if(Double.isNaN(xx)) {xx=0;}
		  		             try { xyseries10.add(xx  , y); } catch(org.jfree.data.general.SeriesException eSE) {
		  		            	 //System.out.println(eSE);
		  		            	 }
		    		    } else if (ctrlabelColorurve==2) {
		    		    	 	xx =   LandingCurve.SquarerootLandingCurve(ctrl_vinit, ctrl_hinit, ctrl_vel, ctrl_alt, y);
		    		    	 	//if(Double.isNaN(xx)) {xx=0;}
		    		    	 		try { xyseries10.add(xx  , y); } catch(org.jfree.data.general.SeriesException eSE) {
		    		    	 			//System.out.println(eSE);
		    		    	 			}
		    		    } else if (ctrlabelColorurve==3) {
		    		    	 	xx =   LandingCurve.LinearLandingCurve(ctrl_vinit, ctrl_hinit, ctrl_vel, ctrl_alt, y);
		    		    	 	//if(Double.isNaN(xx)) {xx=0;}
		    		    	 		try { xyseries10.add(xx  , y); } catch(org.jfree.data.general.SeriesException eSE) {
		    		    	 			//System.out.println(eSE);
		    		    	 			}
		    		    } 
		           } else {try {xyseries10.add(x  , 0);  } catch(org.jfree.data.general.SeriesException eSE) {
		        	   //System.out.println(eSE);
		        	   }}
		           
		           try {xyseries11.add(x  , y);} catch(org.jfree.data.general.SeriesException eSE) {
		        	   //System.out.println(eSE);
		        	   }
		           //double noise = Noise.PerlinNoise.noise1((float) t);
		           try {xyseries_FPA_is.add(t,fpa);} catch(org.jfree.data.general.SeriesException eSE) {
		        	   //System.out.println(eSE);
		        	   }
		        	   
		           }
	              
	       fstream.close();
	       in.close();
	       br.close();
		    CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity.addSeries(xyseries11); 
		   // CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity.addSeries(xyseries10);
		    
		    CHART_P1_DashBoardOverviewChart_Dataset_Time_FPA.addSeries(xyseries_FPA_is);
		   // CHART_P1_DashBoardOverviewChart_Dataset_Time_FPA.addSeries(xyseries_FPA_cmd);
		    
	              } catch (NullPointerException  eNPE) { 
	            	  //System.out.println(eNPE);
	            	  System.out.println("Dashboard chart, Nullpointerexception");
					}catch(IllegalArgumentException eIAE) {
						System.out.println("Dashboard chart, illegal argument error");
						}
					
	    return CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity;
	   }
	
	public static void createTargetView3D() {
        final JFXPanel fxPanel = new JFXPanel();
        //fxPanel.setSize(400,350);
        SpaceShip3DControlPanel.add(fxPanel,BorderLayout.CENTER);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	TargetView3D.start(fxPanel,indx_target);
            }
       });
      
	}
	
	public static void refreshTargetView3D() {
		//SplitPane_Page1_Charts_vertical.remo
        final JFXPanel fxPanel = new JFXPanel();
        SpaceShip3DControlPanel.add(fxPanel,BorderLayout.CENTER);
        SplitPane_Page1_Charts_vertical.setDividerLocation(500);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	TargetView3D.TargetBodyGroup.getChildren().removeAll();
            	TargetView3D.refreshTargetGroup(indx_target);
            }
       });
      
	}
	
	public static void refreshSpaceCraftView() {
		//SplitPane_Page1_Charts_vertical.remo
        final JFXPanel fxPanel = new JFXPanel();
        SplitPane_Page1_Charts_vertical2.add(fxPanel,JSplitPane.RIGHT);
        SplitPane_Page1_Charts_vertical2.setDividerLocation(500);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	SpaceShipView3DFrontPage.model.getChildren().removeAll();
            	SpaceShipView3DFrontPage.coordinateSystem.getChildren().removeAll();
            	//SpaceShipView3DFrontPage.root.getChildren().removeAll();
            	SpaceShipView3DFrontPage.start(fxPanel);
            }
       });
      
	}
	
	public static void CreateChart_DashboardOverviewChart_Altitude_Velocity(double RM) throws IOException {
		//CHART_P1_DashBoardOverviewChart = ChartFactory.createScatterPlot("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity, PlotOrientation.VERTICAL, true, false, false); 
		CHART_P1_DashBoardOverviewChart_Altitude_Velocity = ChartFactory.createStackedXYAreaChart("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity);//("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity, PlotOrientation.VERTICAL, true, false, false); 
		XYPlot plot = (XYPlot)CHART_P1_DashBoardOverviewChart_Altitude_Velocity.getXYPlot(); 
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    plot.setRenderer(0, renderer); 
	    renderer.setSeriesPaint( 0 , labelColor );	
	    CHART_P1_DashBoardOverviewChart_Altitude_Velocity.setBackgroundPaint(backgroundColor);
		Font font3 = new Font("Dialog", Font.PLAIN, 12); 	
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelPaint(labelColor);
		plot.getDomainAxis().setLabelPaint(labelColor);
		plot.setForegroundAlpha(0.8f);
		plot.setBackgroundPaint(backgroundColor);
		plot.setDomainGridlinePaint(labelColor);
		plot.setRangeGridlinePaint(labelColor); 
		CHART_P1_DashBoardOverviewChart_Altitude_Velocity.getLegend().setBackgroundPaint(backgroundColor);
		CHART_P1_DashBoardOverviewChart_Altitude_Velocity.getLegend().setItemPaint(labelColor);;
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		//domainAxis.setInverted(true);
		
		//Shape cross = ShapeUtilities.createDiagonalCross(1, 1) ;
	    double size = 2.0;
	    double size2 = 1.0;
	    double delta = size / 2.0;
	    double delta2 = size2 / 2.0;
		Shape dot1 = new Ellipse2D.Double(-delta, -delta, size, size);
		Shape dot2 = new Ellipse2D.Double(-delta2, -delta2, size2, size2);
		renderer.setSeriesShape(0, dot1);
		renderer.setSeriesShape(1, dot2);
		
		JPanel PlotPanel_X43 = new JPanel();
		PlotPanel_X43.setLayout(new BorderLayout());
		PlotPanel_X43.setPreferredSize(new Dimension(900, page1_plot_y));
		PlotPanel_X43.setBackground(backgroundColor);
	
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity = new ChartPanel(CHART_P1_DashBoardOverviewChart_Altitude_Velocity);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMaximumDrawHeight(50000);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMaximumDrawWidth(50000);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMinimumDrawHeight(0);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMinimumDrawWidth(0);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setMouseWheelEnabled(true);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.setPreferredSize(new Dimension(900, page1_plot_y));
	    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
	     xCrosshair_DashBoardOverviewChart_Altitude_Velocity = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	     xCrosshair_DashBoardOverviewChart_Altitude_Velocity.setLabelVisible(true);
	     yCrosshair_DashBoardOverviewChart_Altitude_Velocity = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	     yCrosshair_DashBoardOverviewChart_Altitude_Velocity.setLabelVisible(true);
	    crosshairOverlay.addDomainCrosshair(xCrosshair_DashBoardOverviewChart_Altitude_Velocity);
	    crosshairOverlay.addRangeCrosshair(yCrosshair_DashBoardOverviewChart_Altitude_Velocity);
		ChartPanel_DashBoardOverviewChart_Altitude_Velocity.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
	            Rectangle2D dataArea = BlueBookVisual.ChartPanel_DashBoardOverviewChart_Altitude_Velocity.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);
	            
	            double max = xAxis.getUpperBound();
	            double min = xAxis.getLowerBound();
	            int indx = (int) ( (1- x/(max-min))*resultSet.size());
		            if(indx>0 && indx<resultSet.size()) {
		            TargetView3D.prepareSpacecraft(indx);
		            		if(thirdWindowIndx==0) {
			            BlueBookVisual.xCrosshair_DashBoardOverviewChart_Time_FPA.setValue(resultSet.get(indx).getTime());
			            BlueBookVisual.yCrosshair_DashBoardOverviewChart_Time_FPA.setValue(resultSet.get(indx).getFpa()*rad2deg);
						}
		            }
	            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
	            BlueBookVisual.xCrosshair_DashBoardOverviewChart_Altitude_Velocity.setValue(x);
	            BlueBookVisual.yCrosshair_DashBoardOverviewChart_Altitude_Velocity.setValue(y);
	        }
	});
	    ChartPanel_DashBoardOverviewChart_Altitude_Velocity.addOverlay(crosshairOverlay);
	   PlotPanel_X43.add(ChartPanel_DashBoardOverviewChart_Altitude_Velocity,BorderLayout.PAGE_START);
	   // P1_Plotpanel.add(PlotPanel_X43,BorderLayout.PAGE_START);
	   SplitPane_Page1_Charts_vertical.add(ChartPanel_DashBoardOverviewChart_Altitude_Velocity, JSplitPane.LEFT);
	   //P1_Plotpanel.add(ChartPanel_DashBoardOverviewChart,BorderLayout.LINE_START);
		//jPanel4.validate();	
		CHART_P1_DashBoardOverviewChart_fd = false;
	}
	
	public static void CreateChart_DashboardOverviewChart_Time_FPA() throws IOException {
		//CHART_P1_DashBoardOverviewChart = ChartFactory.createScatterPlot("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity, PlotOrientation.VERTICAL, true, false, false); 
	    CHART_P1_DashBoardOverviewChart_Time_FPA = ChartFactory.createStackedXYAreaChart("", "Time [s]", "Flight Path Angle [deg] ", CHART_P1_DashBoardOverviewChart_Dataset_Time_FPA);//("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset_Altitude_Velocity, PlotOrientation.VERTICAL, true, false, false); 
		XYPlot plot = (XYPlot)CHART_P1_DashBoardOverviewChart_Time_FPA.getXYPlot(); 
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    plot.setRenderer(0, renderer); 
	    renderer.setSeriesPaint( 0 , labelColor );	
	    CHART_P1_DashBoardOverviewChart_Time_FPA.setBackgroundPaint(backgroundColor);
		Font font3 = new Font("Dialog", Font.PLAIN, 12); 	
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setAxisLinePaint(labelColor);
		plot.getRangeAxis().setLabelPaint(labelColor);
		plot.getDomainAxis().setLabelPaint(labelColor);
		plot.getDomainAxis().setAxisLinePaint(labelColor);
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(backgroundColor);
		plot.setDomainGridlinePaint(labelColor);
		plot.setRangeGridlinePaint(labelColor); 
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		CHART_P1_DashBoardOverviewChart_Time_FPA.getLegend().setBackgroundPaint(backgroundColor);
		CHART_P1_DashBoardOverviewChart_Time_FPA.getLegend().setItemPaint(labelColor);
		//final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		//domainAxis.setInverted(true);
	    double size = 2.0;
	    double size2 = 1.0;
	    double delta = size / 2.0;
	    double delta2 = size2 / 2.0;
		Shape dot = new Ellipse2D.Double(-delta, -delta, size, size);
		Shape dot2 = new Ellipse2D.Double(-delta2, -delta2, size2, size2);
		renderer.setSeriesShape(0, dot);
		renderer.setSeriesShape(1, dot2);
		
		JPanel PlotPanel_X43 = new JPanel();
		PlotPanel_X43.setLayout(new BorderLayout());
		PlotPanel_X43.setPreferredSize(new Dimension(900, page1_plot_y));
		PlotPanel_X43.setBackground(backgroundColor);
	
		ChartPanel_DashBoardOverviewChart_Time_FPA = new ChartPanel(CHART_P1_DashBoardOverviewChart_Time_FPA);
		ChartPanel_DashBoardOverviewChart_Time_FPA.setMaximumDrawHeight(50000);
		ChartPanel_DashBoardOverviewChart_Time_FPA.setMaximumDrawWidth(50000);
		ChartPanel_DashBoardOverviewChart_Time_FPA.setMinimumDrawHeight(0);
		ChartPanel_DashBoardOverviewChart_Time_FPA.setMinimumDrawWidth(0);
		ChartPanel_DashBoardOverviewChart_Time_FPA.setMouseWheelEnabled(true);
		ChartPanel_DashBoardOverviewChart_Time_FPA.setPreferredSize(new Dimension(900, page1_plot_y));
	    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
	    xCrosshair_DashBoardOverviewChart_Time_FPA = new Crosshair(0, Color.GRAY, new BasicStroke(0f));
	    xCrosshair_DashBoardOverviewChart_Time_FPA.setLabelVisible(true);
	     yCrosshair_DashBoardOverviewChart_Time_FPA = new Crosshair(0, Color.GRAY, new BasicStroke(0f));
	     yCrosshair_DashBoardOverviewChart_Time_FPA.setLabelVisible(true);
	    crosshairOverlay.addDomainCrosshair(xCrosshair_DashBoardOverviewChart_Time_FPA);
	    crosshairOverlay.addRangeCrosshair(yCrosshair_DashBoardOverviewChart_Time_FPA);
		ChartPanel_DashBoardOverviewChart_Time_FPA.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
	            Rectangle2D dataArea = BlueBookVisual.ChartPanel_DashBoardOverviewChart_Time_FPA.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);
	            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
	            BlueBookVisual.xCrosshair_DashBoardOverviewChart_Time_FPA.setValue(x);
	            BlueBookVisual.yCrosshair_DashBoardOverviewChart_Time_FPA.setValue(y);
	        }
	});
	    
	    ChartPanel_DashBoardOverviewChart_Time_FPA.addOverlay(crosshairOverlay);
	   PlotPanel_X43.add(ChartPanel_DashBoardOverviewChart_Time_FPA,BorderLayout.PAGE_START);
	   // P1_Plotpanel.add(PlotPanel_X43,BorderLayout.PAGE_START);
	   SpaceShip3DControlPanel.add(ChartPanel_DashBoardOverviewChart_Time_FPA, BorderLayout.PAGE_START);
	   //P1_Plotpanel.add(ChartPanel_DashBoardOverviewChart,BorderLayout.LINE_START);
		//jPanel4.validate();	
		CHART_P1_DashBoardOverviewChart_fd = false;
	}
	public static void CreateChart_DashBoardFlexibleChart() throws IOException {
		//result1.removeAllSeries();
		try {
		ResultSet_FlexibleChart = AddDataset_DashboardFlexibleChart(4,3);
		} catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF2) {
			
		}
	    //-----------------------------------------------------------------------------------
	    Chart_MercatorMap4 = ChartFactory.createScatterPlot("", "", "", ResultSet_FlexibleChart, PlotOrientation.VERTICAL, false, false, false); 
		XYPlot plot = (XYPlot)Chart_MercatorMap4.getXYPlot(); 
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    plot.setRenderer(0, renderer); 
	    renderer.setSeriesPaint( 0 , labelColor);	
		Chart_MercatorMap4.setBackgroundPaint(backgroundColor);
		Font font3 = new Font("Dialog", Font.PLAIN, 12); 	
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelPaint(labelColor);
		plot.getDomainAxis().setLabelPaint(labelColor);
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(backgroundColor);
		plot.setDomainGridlinePaint(labelColor);
		plot.setRangeGridlinePaint(labelColor); 
		//Chart_MercatorMap4.getLegend().setBackgroundPaint(backgroundColor);
		//Chart_MercatorMap4.getLegend().setItemPaint(labelColor);
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		//domainAxis.setInverted(true);
		
		JPanel PlotPanel_X44 = new JPanel();
		PlotPanel_X44.setLayout(new BorderLayout());
		//PlotPanel_X44.setPreferredSize(new Dimension(900, page1_plot_y));
		PlotPanel_X44.setBackground(backgroundColor);
		
		//Shape cross = ShapeUtilities.createDiagonalCross(1, 1) ;
	    double size = 2.0;
	    double delta = size / 2.0;
		Shape dot = new Ellipse2D.Double(-delta, -delta, size, size);
		renderer.setSeriesShape(0, dot);

	
		ChartPanel_DashBoardFlexibleChart = new ChartPanel(Chart_MercatorMap4);
		ChartPanel_DashBoardFlexibleChart.setMaximumDrawHeight(50000);
		ChartPanel_DashBoardFlexibleChart.setMaximumDrawWidth(50000);
		ChartPanel_DashBoardFlexibleChart.setMinimumDrawHeight(0);
		ChartPanel_DashBoardFlexibleChart.setMinimumDrawWidth(0);
		ChartPanel_DashBoardFlexibleChart.setMouseWheelEnabled(true);
		//ChartPanel_DashBoardFlexibleChart.setPreferredSize(new Dimension(900, page1_plot_y));
		ChartPanel_DashBoardFlexibleChart.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
	            Rectangle2D dataArea = BlueBookVisual.ChartPanel_DashBoardFlexibleChart.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);
	            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
	            BlueBookVisual.xCH_DashboardFlexibleChart.setValue(x);
	            BlueBookVisual.yCH_DashboardFlexibleChart.setValue(y);
	        }
	});
	    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
	    xCH_DashboardFlexibleChart = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    xCH_DashboardFlexibleChart.setLabelVisible(true);
	    yCH_DashboardFlexibleChart = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    yCH_DashboardFlexibleChart.setLabelVisible(true);
	    crosshairOverlay.addDomainCrosshair(xCH_DashboardFlexibleChart);
	    crosshairOverlay.addRangeCrosshair(yCH_DashboardFlexibleChart);
	    ChartPanel_DashBoardFlexibleChart.addOverlay(crosshairOverlay);
	   PlotPanel_X44.add(ChartPanel_DashBoardFlexibleChart,BorderLayout.PAGE_START);
	    //P1_Plotpanel.add(PlotPanel_X44,BorderLayout.LINE_END);
	    //P1_Plotpanel.add(ChartPanel_DashBoardFlexibleChart,BorderLayout.CENTER);
	   SplitPane_Page1_Charts_vertical2.add(ChartPanel_DashBoardFlexibleChart, JSplitPane.LEFT);
		//jPanel4.validate();	
		Chart_MercatorMap4_fd = false;
	}
	public static void Update_DashboardFlexibleChart(){
	    	ResultSet_FlexibleChart.removeAllSeries();
	    	try {
	    	ResultSet_FlexibleChart = AddDataset_DashboardFlexibleChart(axis_chooser.getSelectedIndex(),axis_chooser2.getSelectedIndex());
	    	Chart_MercatorMap4.getXYPlot().getDomainAxis().setAttributedLabel(String.valueOf(axis_chooser.getSelectedItem()));
	    	Chart_MercatorMap4.getXYPlot().getRangeAxis().setAttributedLabel(String.valueOf(axis_chooser2.getSelectedItem()));
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
	    	}
	}
	
	public static void createChart_3DRotation() {
		JPanel SpaceShip3DPanel = new JPanel();
		SpaceShip3DPanel.setLayout(new BorderLayout());
		SpaceShip3DPanel.setLocation(765, 10);
		//SpaceShip3DPanel.setBackground(backgroundColor);
		//SpaceShip3DPanel.setForeground(labelColor);
		SpaceShip3DPanel.setSize(450, 400);
		//SpaceShip3DPanel.setBorder(Moon_border);
		
        final JFXPanel fxPanel = new JFXPanel();
        SpaceShip3DPanel.add(fxPanel, BorderLayout.CENTER);
        SplitPane_Page1_Charts_vertical2.add(SpaceShip3DPanel, JSplitPane.RIGHT);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	SpaceShipView3DFrontPage.start(fxPanel);
            }
       });
	}
	public static XYSeriesCollection AddDataset_DashboardFlexibleChart(int x, int y) throws IOException , IIOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
	   			  XYSeries xyseries10 = new XYSeries("", false, true); 
	              FileInputStream fstream = null;
	      		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	              DataInputStream in = new DataInputStream(fstream);
	              BufferedReader br = new BufferedReader(new InputStreamReader(in));
	              String strLine;
	              try {
			                  while ((strLine = br.readLine()) != null )   {
						            String[] tokens = strLine.split(" ");
						            double xx=0; double yy=0; 
						            if(x==3) {
						             xx = Double.parseDouble(tokens[x]); } else {
						            	 String x_axis_label = String.valueOf(axis_chooser.getSelectedItem());
						            	 boolean isangle = x_axis_label.indexOf("[deg]") !=-1? true: false;
						            	 boolean isangle2 = x_axis_label.indexOf("[deg/s]") !=-1? true: false;
						            	 if(isangle || isangle2) {xx = Double.parseDouble(tokens[x])*rad2deg;} else {
						            		 		  xx = Double.parseDouble(tokens[x]);} 
						            	 }
						            if(y==3) {
						             yy = Double.parseDouble(tokens[y]);} else {
						            	 String x_axis_label = String.valueOf(axis_chooser2.getSelectedItem());
						            	 boolean isangle = x_axis_label.indexOf("[deg]") !=-1? true: false;
						            	 boolean isangle2 = x_axis_label.indexOf("[deg/s]") !=-1? true: false;
						            	 if(isangle || isangle2) {yy = Double.parseDouble(tokens[y])*rad2deg;} else {
						             yy = Double.parseDouble(tokens[y]);	}
						             }
						         	xyseries10.add(xx , yy);
					           }
	       in.close();
	    ResultSet_FlexibleChart.addSeries(xyseries10); 
	              } catch (NullPointerException eNPE) { 
	            	 // System.out.println(eNPE);
	            	  }
	    return ResultSet_FlexibleChart;
	   }
	public void SET_MAP(int TARGET) throws URISyntaxException, IOException{
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
	public static DefaultTableXYDataset AddDataset_GroundClearance() throws IOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
       	XYSeries xyseries_FlightPath = new XYSeries("Flight Path", false, false); 
       	XYSeries xyseries_Delta = new XYSeries("Ground clearance", false, false); 
       	XYSeries xyseries_Elevation = new XYSeries("Local Elevation", false, false); 
       	@SuppressWarnings("unused")
		Random rand = new Random();
            FileInputStream fstream = null;
            FileInputStream fstream_LocalElev=null; 
            		try{ fstream = new FileInputStream(RES_File);fstream_LocalElev = new FileInputStream(LOCALELEVATIONFILE);} catch(IOException eIO) { System.out.println(eIO);}
                  DataInputStream in = new DataInputStream(fstream);
                  DataInputStream in2 = new DataInputStream(fstream_LocalElev);
                  BufferedReader br = new BufferedReader(new InputStreamReader(in));
                  BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
                  String strLine;
                  String strLine_2;
                  try {
                  while ((strLine = br.readLine()) != null )   {
                   strLine_2 =  br2.readLine();
		           String[] tokens = strLine.split(" ");
		           String[] tokens2 = strLine_2.split(" ");
		           double y = Double.parseDouble(tokens[4]);     			 // Altitude 	[m]
		           double x = Double.parseDouble(tokens[40]);	 			 // Groundtrack [m]
		           try{
		           xyseries_FlightPath.add(x,y);
		           } catch ( org.jfree.data.general.SeriesException eSE){
		        	 // System.out.println(eSE); 
		           }
		           try{
		           double local_elevation = Double.parseDouble(tokens2[0]);
		           xyseries_Elevation.add(x,local_elevation);
		           xyseries_Delta.add(x,y-local_elevation);
		           } catch ( org.jfree.data.general.SeriesException eSE){
		        	  //System.out.println(eSE); 
		           }
                  }
           in.close();
           br.close();
           fstream.close();
           ResultSet_GroundClearance_FlightPath.addSeries(xyseries_FlightPath); 
           ResultSet_GroundClearance_FlightPath.addSeries(xyseries_Delta); 
           ResultSet_GroundClearance_Elevation.addSeries(xyseries_Elevation); 
                  } catch(NullPointerException eNPI) { System.out.print(eNPI); }
        return ResultSet_GroundClearance_FlightPath;          
       }
	public static void CreateChart_GroundClearance() throws IOException{
		ResultSet_GroundClearance_FlightPath.removeAllSeries();
		ResultSet_GroundClearance_Elevation.removeAllSeries();
		 try {
			 ResultSet_GroundClearance_FlightPath = AddDataset_GroundClearance(); 
		        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {System.out.println(" Error read for plot X40");}
		        Chart_GroundClearance = ChartFactory.createXYAreaChart("", "Ground Track [km]", "Altitude/Elevation [m] ", ResultSet_GroundClearance_FlightPath, PlotOrientation.VERTICAL, true, false, false); 
				XYPlot plot = (XYPlot)Chart_GroundClearance.getXYPlot(); 
				StackedXYAreaRenderer renderer_Area = new StackedXYAreaRenderer( );
		        XYItemRenderer renderer_Line = new StandardXYItemRenderer();
		        renderer_Line.setSeriesPaint(0,Color.black);
		        renderer_Line.setSeriesPaint(1,Color.orange);
		        renderer_Area.setSeriesPaint(0,Color.gray);

		        plot.setRenderer(0, renderer_Line);  
 
		        plot.setDataset(1, ResultSet_GroundClearance_Elevation);
		        plot.setRenderer(1, renderer_Area);
			
		        Chart_GroundClearance.setBackgroundPaint(Color.white);
				
		        plot.getDomainAxis().setLabelFont(labelfont_small);
		        plot.getRangeAxis().setLabelFont(labelfont_small);
				
		       final XYPlot plot2 = (XYPlot) Chart_GroundClearance.getPlot();
		       plot2.setForegroundAlpha(0.5f);
		       plot2.setBackgroundPaint(Color.white);
		       plot2.setDomainGridlinePaint(Color.black);
		       plot2.setRangeGridlinePaint(new Color(220,220,220));

		       ValueAxis domain2 = plot.getDomainAxis();
		       //domain2.setRange(-180, 180);
		       domain2.setInverted(true);
		       // change the auto tick unit selection to integer units only...
		       final NumberAxis rangeAxis2 = (NumberAxis) plot2.getRangeAxis();
		       rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		       //rangeAxis2.setRange(-90, 90);
		       ChartPanel CPXX4 = new ChartPanel(Chart_GroundClearance);
		       CPXX4.setBackground(backgroundColor);
		       //CPXX4.setDomainZoomable(false);
		       //CPXX4.setRangeZoomable(false);
		       CPXX4.setMaximumDrawHeight(50000);
		       CPXX4.setMaximumDrawWidth(50000);
		       CPXX4.setMinimumDrawHeight(0);
		       CPXX4.setMinimumDrawWidth(0);
		       CPXX4.setPreferredSize(new Dimension(1300, 660));
		       PageX04_GroundClearance.add(CPXX4, BorderLayout.CENTER);	
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
       //System.out.println(local_elevation);
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
	/*
    public JPanel WINDOW_CreateLocalElevationFile() throws IOException, SQLException{
    	//---------------------------------------------------
    	// 				Data Select 
    	//---------------------------------------------------
    	// data_select == 1 => Export Requirements
    	// data_select == 2 => Export Change Log
    	//---------------------------------------------------
	   	JPanel MainGUI = new JPanel();
	   	MainGUI = new JPanel();
	   	//MainGUI.setLayout(new BorderLayout());
	   	MainGUI.setLayout(null);
	   	MainGUI.setBackground(backgroundColor);	
   		int extx = 370;
   		int exty = 400;
	  //----------------------------------------------------------------
   		
        JLabel Title = new JLabel("Select Resolution: ");
        Title.setLocation(5, 2 );
        Title.setSize(250, 15);
        Title.setBackground(backgroundColor);
        Title.setForeground(labelColor);
        MainGUI.add(Title);
        
	  	  @SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox ResolutionChooser = new JComboBox(LocalElevation_Resolution);
	  	ResolutionChooser.setLocation(30,20);
	  	ResolutionChooser.setSize(230,25);
	  	ResolutionChooser.setSelectedIndex(0);
	  	ResolutionChooser.setBorder(BorderFactory.createLineBorder(Color.black));
	  	ResolutionChooser.addActionListener(new ActionListener() { 
	    	  public void actionPerformed(ActionEvent e) {
	    		  // Get selected File Resolution from ResolutionChooser:
	    		  int Resoltuion = Integer.parseInt((String) ResolutionChooser.getSelectedItem());
	    		  // Create Elevation File:
	    		  
              	try {
						CreateLocalElevationFile(Resoltuion);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
              	// Update Ground Clearance Chart with generated data:
              	ResultSet_GroundClearance_FlightPath.removeAllSeries();
        		ResultSet_GroundClearance_Elevation.removeAllSeries();
       		 try {
       			 ResultSet_GroundClearance_FlightPath = AddDataset_GroundClearance(); 
       		        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {System.out.println(" Error read for plot X40");} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
       		 // Dispose frame
       		frame_CreateLocalElevationFile.dispose();
	    	  }});
			  	  MainGUI.add(ResolutionChooser); 
	   	//----------------------------------------------------------------
	    //MainGUI.setOpaque(true);
        MainGUI.setSize(extx,exty);
	    return MainGUI;
    }
    */
	public static List<atm_dataset> INITIALIZE_Page03_storage_DATA() throws URISyntaxException{
    	   try{ // Temperature
    	       	FileInputStream fstream = null; 
    	       	try {
    	       	              fstream = new FileInputStream(RES_File);
    	       	} catch(IOException eIIO) { System.out.println(eIIO); } 
    		          DataInputStream in = new DataInputStream(fstream);
    		          BufferedReader br = new BufferedReader(new InputStreamReader(in));
    		          String strLine;
    		          int k = 0;
    		          while ((strLine = br.readLine()) != null )   {
			    if (k==0){
			    // Head line -> skip 	
			    } else {
    		        	  double time = 0;
    		        	  double velocity = 0 ; 

    		   String[] tokens = strLine.split(" ");
    		   time = Double.parseDouble(tokens[0]);		// Altitude
    		   velocity = Double.parseDouble(tokens[4]);		// density
    		   atm_dataset insert = new atm_dataset( time,  velocity, 0, 0,  0); 
    		   Page03_storage.add(insert);
    }
    		   k++;
    		   }
    		   in.close();
    		   }catch (Exception e){
    		     System.err.println("Error: " + e.getMessage());
    		   }
    	   return Page03_storage;	
    }
    
    public static void UpdateChart_A01() throws IOException , IIOException, FileNotFoundException, ArrayIndexOutOfBoundsException, NullPointerException, URISyntaxException{
    	result11_A3_1.removeAllSeries();
    	result11_A3_2.removeAllSeries();
    	result11_A3_3.removeAllSeries();
    	result11_A3_4.removeAllSeries();
    	
    	Page03_storage.removeAll(Page03_storage);
    	INITIALIZE_Page03_storage_DATA();
    	
       	XYSeries xyseries10 = new XYSeries("", false, true); 
       	XYSeries xyseries20 = new XYSeries("", false, true); 
       	XYSeries xyseries30 = new XYSeries("", false, true); 
       	XYSeries xyseries40 = new XYSeries("", false, true); 
       	FileInputStream fstream = null; 
try { fstream = new FileInputStream(RES_File);  } catch(IOException eIIO) { System.out.println(eIIO); } 
              DataInputStream in = new DataInputStream(fstream);
              BufferedReader br = new BufferedReader(new InputStreamReader(in));
              String strLine;
              try {
              while ((strLine = br.readLine()) != null )   {
	           String[] tokens = strLine.split(" ");
	           double x1 = Double.parseDouble(tokens[4]);
	           double y1 = Double.parseDouble(tokens[3])-RM;
	           
	           double x2 = Double.parseDouble(tokens[0]);
	           double y2 = Double.parseDouble(tokens[3])-RM;
	          
	           double x3 = Double.parseDouble(tokens[0]);
	           double y3 = Double.parseDouble(tokens[5])*rad2deg;
	           
	           double x4 = 0 , y4=0;
	           if (chartA3_fd==true){
	           x4 = Double.parseDouble(tokens[0]);
	           y4 = Double.parseDouble(tokens[Axis_Option_NR.length-2]);
	           } else {
		       x4 = Double.parseDouble(tokens[axis_chooser3.getSelectedIndex()]);
		       y4 = Double.parseDouble(tokens[axis_chooser4.getSelectedIndex()]);   
		       chartA3_4.getXYPlot().getDomainAxis().setLabel(Axis_Option_NR[axis_chooser3.getSelectedIndex()]);
		       chartA3_4.getXYPlot().getRangeAxis().setLabel(Axis_Option_NR[axis_chooser4.getSelectedIndex()]);
	           }

	         	xyseries10.add(x1 , y1);
	         	xyseries20.add(x2 , y2);
	         	xyseries30.add(x3 , y3);
	         	xyseries40.add(x4 , y4);
	         	//System.out.println(x1);
	           }
       in.close();
       result11_A3_1.addSeries(xyseries10); 
       result11_A3_2.addSeries(xyseries20);
       result11_A3_3.addSeries(xyseries30);
       result11_A3_4.addSeries(xyseries40);
              } catch (NullPointerException eNPE) { System.out.println(eNPE);}
    }
    
    
	public static void CreateChart_A01() {
	
				try {
					try {
						UpdateChart_A01();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (ArrayIndexOutOfBoundsException | NullPointerException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	    	
	    	try {
	    		Page03_storage.removeAll(Page03_storage);
				INITIALIZE_Page03_storage_DATA();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				System.out.println("storage list init error");
				e.printStackTrace();
			}
	        //-----------------------------------------------------------------------------------
	    	//AddDataset_101();
	        //-----------------------------------------------------------------------------------
	    	String x_str_01 = "Velocity [m/s]";
	    	String y_str_01 = "Altitude [m]";
	    	String x_str_02 = "Time [s]";
	    	String y_str_02 = "Altitude [m]";
	    	String x_str_03 = "Time [s]";
	    	String y_str_03 = "Flight Path Angle [deg]";
	    	String x_str_04 = "Time [s]";
	    	String y_str_04 = "Normaliced Acceleration [-]";
	    	//int xplot = 670;
	    	int yplot = 350; 
			JPanel TopPanel = new JPanel();
			TopPanel.setLayout(new BorderLayout());
			TopPanel.setPreferredSize(new Dimension(extx_main, yplot));
			TopPanel.setBackground(backgroundColor);
			JPanel BottomPanel = new JPanel();
			BottomPanel.setLayout(new BorderLayout());
			BottomPanel.setPreferredSize(new Dimension(extx_main, yplot));
			BottomPanel.setBackground(backgroundColor);
	    	
			JPanel PlotPanel_01 = new JPanel();
			PlotPanel_01.setLayout(new BorderLayout());
			//PlotPanel_01.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_01.setBackground(backgroundColor);
			JPanel PlotPanel_02 = new JPanel();
			PlotPanel_02.setLayout(new BorderLayout());
			//PlotPanel_02.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_02.setBackground(backgroundColor);
			JPanel PlotPanel_03 = new JPanel();
			PlotPanel_03.setLayout(new BorderLayout());
			//PlotPanel_03.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_03.setBackground(backgroundColor);
			JPanel PlotPanel_04 = new JPanel();
			PlotPanel_04.setLayout(new BorderLayout());
			//PlotPanel_04.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_04.setBackground(backgroundColor);
			
			JPanel Midpanel = new JPanel();
			Midpanel.setLayout(null);
			//Midpanel.setPreferredSize(new Dimension(155, 300));
			Midpanel.setSize(155,300);
			Midpanel.setBackground(backgroundColor);
			BottomPanel.add(Midpanel, BorderLayout.CENTER);
			
		      JLabel p41_linp8 = new JLabel("X-Axis");
		      p41_linp8.setLocation(5, 10 + 25 * 1 );
		      //p41_linp8.setPreferredSize(new Dimension(150, 20));
		      p41_linp8.setHorizontalAlignment(0);
		      p41_linp8.setSize(150,20);
		      p41_linp8.setBackground(backgroundColor);
		      p41_linp8.setForeground(labelColor);
		      Midpanel.add(p41_linp8);
		      JLabel p41_linp9 = new JLabel("Y-Axis");
		      p41_linp9.setLocation(5, 10 + 25 * 4 );
		      //p41_linp9.setPreferredSize(new Dimension(150, 20));
		      p41_linp9.setSize(150, 20);
		      p41_linp9.setHorizontalAlignment(0);
		      p41_linp9.setBackground(backgroundColor);
		      p41_linp9.setForeground(labelColor);
		      Midpanel.add(p41_linp9);
			  axis_chooser3 = new JComboBox<Object>(Axis_Option_NR);
			  axis_chooser4 = new JComboBox<Object>(Axis_Option_NR);
		      axis_chooser4.setLocation(5, 10 + 25 * 5);
		      //axis_chooser2.setPreferredSize(new Dimension(150,25));
		     // axis_chooser4.setPreferredSize(new Dimension(150,25));
		      axis_chooser4.setSize(150,25);
		      axis_chooser4.setSelectedIndex(28);
		      axis_chooser4.addActionListener(new ActionListener() { 
		    	  public void actionPerformed(ActionEvent e) {
		    		  try {
						UpdateChart_A01();
					} catch (ArrayIndexOutOfBoundsException | NullPointerException | IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	  }
		  	  } );
		      axis_chooser3.setLocation(5, 10 + 25 * 2);
		      //axis_chooser.setPreferredSize(new Dimension(150,25));
		      //axis_chooser3.setPreferredSize(new Dimension(150,25));
		      axis_chooser3.setSize(150,25);
		      axis_chooser3.setSelectedIndex(0);
		      axis_chooser3.addActionListener(new ActionListener() { 
		    	  public void actionPerformed(ActionEvent e) {
		    		  try {
						UpdateChart_A01();
					} catch (ArrayIndexOutOfBoundsException | NullPointerException | IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	  }
		  	  } );
		      Midpanel.add(axis_chooser3);
		      Midpanel.add(axis_chooser4);
	//--------------------------------------------------------------------------------------------------------------------------------------
	    	chartA3_1 = ChartFactory.createScatterPlot("", x_str_01, y_str_01, result11_A3_1, PlotOrientation.VERTICAL, false, false, false); 
	        XYLineAndShapeRenderer renderer131 = new XYLineAndShapeRenderer( );
	        renderer131.setSeriesPaint( 0 , Color.BLACK );
			Font font3 = new Font("Dialog", Font.PLAIN, 12); 
			renderer131.setSeriesPaint( 2 , Color.gray );
			chartA3_1.getXYPlot().getDomainAxis().setLabelFont(font3);
			chartA3_1.getXYPlot().getRangeAxis().setLabelFont(font3);
			chartA3_1.getXYPlot().setRenderer(0, renderer131); 
			chartA3_1.setBackgroundPaint(Color.white);
			chartA3_1.getXYPlot().setForegroundAlpha(0.5f);
			chartA3_1.getXYPlot().setBackgroundPaint(Color.white);
			chartA3_1.getXYPlot().setDomainGridlinePaint(new Color(220,220,220));
			chartA3_1.getXYPlot().setRangeGridlinePaint(new Color(220,220,220));
			chartA3_1.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			CP_A31 = new ChartPanel(chartA3_1);
			//CP_A31.setSize(586,350);
			//CP_A31.setLocation(2, 5);
			TopPanel.add(CP_A31,BorderLayout.CENTER);
	
	        //-----------------------------------------------------------------------------------
	    	chartA3_2 = ChartFactory.createScatterPlot("", x_str_02, y_str_02, result11_A3_2, PlotOrientation.VERTICAL, false, false, false); 
	    	 XYLineAndShapeRenderer renderer132 = new XYLineAndShapeRenderer( );
	    	renderer132.setSeriesPaint( 0 , Color.BLACK );	
			chartA3_2.getXYPlot().getDomainAxis().setLabelFont(font3);
			chartA3_2.getXYPlot().getRangeAxis().setLabelFont(font3);
			chartA3_2.getXYPlot().setRenderer(0, renderer132); 
			chartA3_2.setBackgroundPaint(Color.white);
			chartA3_2.getXYPlot().setForegroundAlpha(0.5f);
			chartA3_2.getXYPlot().setBackgroundPaint(Color.white);
			chartA3_2.getXYPlot().setDomainGridlinePaint(new Color(220,220,220));
			chartA3_2.getXYPlot().setRangeGridlinePaint(new Color(220,220,220));
			chartA3_2.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			CP_A32 = new ChartPanel(chartA3_2);
			//CP_A32.setSize(736,350);
			//CP_A32.setLocation(590, 5);
			TopPanel.add(CP_A32,BorderLayout.EAST);
			//PageX04_3.add(PlotPanel_02);
	        //-----------------------------------------------------------------------------------
	    	chartA3_3 = ChartFactory.createScatterPlot("", x_str_03, y_str_03, result11_A3_3, PlotOrientation.VERTICAL, false, false, false); 
	    	 XYLineAndShapeRenderer renderer133 = new XYLineAndShapeRenderer( );
	    	 renderer133.setSeriesPaint( 0 , Color.BLACK );
	    	 renderer133.setSeriesPaint( 2 , Color.gray );
	        chartA3_3.getXYPlot().getDomainAxis().setLabelFont(font3);
	        chartA3_3.getXYPlot().getRangeAxis().setLabelFont(font3);
	        chartA3_3.getXYPlot().setRenderer(0, renderer133); 
	        chartA3_3.setBackgroundPaint(Color.white);
	        chartA3_3.getXYPlot().setForegroundAlpha(0.5f);
	        chartA3_3.getXYPlot().setBackgroundPaint(Color.white);
	        chartA3_3.getXYPlot().setDomainGridlinePaint(new Color(220,220,220));
	        chartA3_3.getXYPlot().setRangeGridlinePaint(new Color(220,220,220));
	        chartA3_3.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        CP_A33 = new ChartPanel(chartA3_3);
	        //CP_A33.setSize(586,350);
	        //CP_A33.setLocation(2, 370);
	        BottomPanel.add(CP_A33,BorderLayout.WEST);
			//PageX04_3.add(PlotPanel_03);
			//-----------------------------------------------------------------------------------
	        chartA3_4 = ChartFactory.createScatterPlot("", x_str_04, y_str_04, result11_A3_4, PlotOrientation.VERTICAL, false, false, false); 
			XYPlot xyplot = (XYPlot)chartA3_4.getPlot(); 
	        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	        xyplot.setRenderer(0, renderer); 
	        renderer.setSeriesPaint( 0 , Color.BLACK );
	        chartA3_4.setBackgroundPaint(Color.white);		
			xyplot.getDomainAxis().setLabelFont(font3);
			xyplot.getRangeAxis().setLabelFont(font3);
			
			final XYPlot plot = (XYPlot) chartA3_4.getPlot();
			plot.setForegroundAlpha(0.5f);
			//plot.setBackgroundPaint(new Color(238,238,238));
			plot.setBackgroundPaint(Color.white);
			plot.setDomainGridlinePaint(new Color(220,220,220));
			plot.setRangeGridlinePaint(new Color(220,220,220));
			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());	
		    //plot.setDataset(0, result11_A3_4);
	
		    
		    //XYLineAndShapeRenderer splinerenderer_1 = new XYLineAndShapeRenderer();
		    //splinerenderer_1.setSeriesPaint(0, Color.BLACK);
	
	
	
			    CP_A34 = new ChartPanel(chartA3_4);
			    //CP_A34.setSize(780,360);
			    //CP_A34.setLocation(588, 370);
			    BottomPanel.add(CP_A34,BorderLayout.EAST);
			   // PageX04_3.add(CP_A34);
	
	        CP_A31.addChartMouseListener(new ChartMouseListener() {
	            @Override
	            public void chartMouseClicked(ChartMouseEvent event) {
	                // Update inforboard
	               // Rectangle2D dataArea = BB_AddOn_3DOF.CP_A31.getScreenDataArea();
	                //JFreeChart chart = event.getChart();
	             //   XYPlot plot = chartA3_1.getXYPlot();
	              //  ValueAxis xAxis = plot.getDomainAxis();
	            //    double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	              //          RectangleEdge.BOTTOM);
	              //  double y = DatasetUtilities.findYValue(chartA3_1.getXYPlot().getDataset(), 0, x);
	               // BlueBook_main.xCrosshair_A3_1.setValue(x);
	               // BlueBook_main.yCrosshair_A3_1.setValue(y);
	                //===================================================
	                //double xx = xCrosshair_A3_1.getValue();
	              //  double yy = DatasetUtilities.findYValue((chartA3_2.getXYPlot()).getDataset(), 0, x);
	                //BlueBook_main.xCrosshair_A3_2.setValue(xx);
	               // BlueBook_main.yCrosshair_A3_2.setValue(yy);
	                //===================================================
	                //double xxx = xCrosshair_A3_1.getValue();
	               // double yyy = DatasetUtilities.findYValue((chartA3_3.getXYPlot()).getDataset(), 0, x);
	               // BlueBook_main.xCrosshair_A3_3.setValue(xxx);
	               // BlueBook_main.yCrosshair_A3_3.setValue(yyy);
	                //===================================================
	                //double xxxx = xCrosshair_A3_1.getValue();
	               // double yyyy = DatasetUtilities.findYValue((chartA3_4.getXYPlot()).getDataset(), 0, x);
	               // BlueBook_main.xCrosshair_A3_4.setValue(xxxx);
	               // BlueBook_main.yCrosshair_A3_4.setValue(yyyy);
	                //===================================================
	            	
	            }
	
	            @Override
	            public void chartMouseMoved(ChartMouseEvent event) {
	                Rectangle2D dataArea = BlueBookVisual.CP_A31.getScreenDataArea();
	                //JFreeChart chart = event.getChart();
	                XYPlot plot = chartA3_1.getXYPlot();
	                ValueAxis xAxis = plot.getDomainAxis();
	                double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                        RectangleEdge.BOTTOM);
	                double y = DatasetUtilities.findYValue(chartA3_1.getXYPlot().getDataset(), 0, x);
	                BlueBookVisual.xCrosshair_A3_1.setValue(x);
	                BlueBookVisual.yCrosshair_A3_1.setValue(y);
	                //===================================================
	                double time = get_time(x);
	                double xx = time ;//xCrosshair_A3_1.getValue();
	                double yy = DatasetUtilities.findYValue((chartA3_2.getXYPlot()).getDataset(), 0, time);
	                BlueBookVisual.xCrosshair_A3_2.setValue(xx);
	                BlueBookVisual.yCrosshair_A3_2.setValue(yy);
	                //===================================================
	                double xxx = time ; xCrosshair_A3_1.getValue();
	                double yyy = DatasetUtilities.findYValue((chartA3_3.getXYPlot()).getDataset(), 0, time);
	                BlueBookVisual.xCrosshair_A3_3.setValue(xxx);
	                BlueBookVisual.yCrosshair_A3_3.setValue(yyy);
	                //===================================================
	                double xxxx = time ; // xCrosshair_A3_1.getValue();
	                double yyyy = DatasetUtilities.findYValue((chartA3_4.getXYPlot()).getDataset(), 0, time);
	                BlueBookVisual.xCrosshair_A3_4.setValue(xxxx);
	                BlueBookVisual.yCrosshair_A3_4.setValue(yyyy);
	                //===================================================
	            }
	    });
	        CrosshairOverlay crosshairOverlay3 = new CrosshairOverlay();
	        xCrosshair_A3_1 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        xCrosshair_A3_1.setLabelVisible(true);
	        yCrosshair_A3_1 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        yCrosshair_A3_1.setLabelVisible(true);
	        crosshairOverlay3.addDomainCrosshair(xCrosshair_A3_1);
	        crosshairOverlay3.addRangeCrosshair(yCrosshair_A3_1);
	        CP_A31.addOverlay(crosshairOverlay3);
	      //===================================================
	        CrosshairOverlay crosshairOverlay4 = new CrosshairOverlay();
	        xCrosshair_A3_2 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        xCrosshair_A3_2.setLabelVisible(true);
	        yCrosshair_A3_2 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        yCrosshair_A3_2.setLabelVisible(true);
	        crosshairOverlay4.addDomainCrosshair(xCrosshair_A3_2);
	        crosshairOverlay4.addRangeCrosshair(yCrosshair_A3_2);
	        CP_A32.addOverlay(crosshairOverlay4); 
	        //===================================================
	        CrosshairOverlay crosshairOverlay5 = new CrosshairOverlay();
	        xCrosshair_A3_3 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        xCrosshair_A3_3.setLabelVisible(true);
	        yCrosshair_A3_3 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        yCrosshair_A3_3.setLabelVisible(true);
	        crosshairOverlay5.addDomainCrosshair(xCrosshair_A3_3);
	        crosshairOverlay5.addRangeCrosshair(yCrosshair_A3_3);
	        CP_A33.addOverlay(crosshairOverlay5); 
	        //===================================================
	        CrosshairOverlay crosshairOverlay6 = new CrosshairOverlay();
	        xCrosshair_A3_4 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        xCrosshair_A3_4.setLabelVisible(true);
	        yCrosshair_A3_4 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	        yCrosshair_A3_4.setLabelVisible(true);
	        crosshairOverlay6.addDomainCrosshair(xCrosshair_A3_4);
	        crosshairOverlay6.addRangeCrosshair(yCrosshair_A3_4);
	        CP_A34.addOverlay(crosshairOverlay6); 
	        //===================================================
	        chartA3_fd = false;
			PageX04_3.add(TopPanel, BorderLayout.CENTER);
			PageX04_3.add(BottomPanel, BorderLayout.PAGE_END);
	    }
	public static double get_time(double velocity) {
		double time = 0;
		int leng = Page03_storage.size();
		double data_x[] = new double[leng];
		double data_y[] = new double[leng];
			for (int i = 0;i<leng;i++){
				data_y[i] = Page03_storage.get(i).get_altitude();  // time 
				data_x[i] = Page03_storage.get(i).get_density();   // velocity
			}
		time = Mathbox.LinearInterpolate( data_x , data_y , velocity);
//System.out.println(velocity + " | " + time);
		return time;
	}
    
	private static void createAndShowGUI() throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(false);
        MAIN_frame = new JFrame("" + PROJECT_TITLE);
        MAIN_frame.setFont(small_font);
        BlueBookVisual demo = new BlueBookVisual();
        JPanel tp = demo.createContentPane();
        tp.setPreferredSize(new java.awt.Dimension(x_init, y_init));
        MAIN_frame.add(tp, BorderLayout.CENTER);
        MAIN_frame.pack();
        MAIN_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		public class BackgroundMenu extends JMenu {
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
}