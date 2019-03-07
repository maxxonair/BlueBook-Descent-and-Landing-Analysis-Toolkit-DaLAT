package GUI; 
//-----------------------------------------------------------------------------------------------------------------------------------------
//															BlueBook DaLAT GUI
//
//   Descent and Landing Analysis Toolkit  - Version 0.1 ALPHA
//
//   Author: M. Braun
// 	 Date: 01/03/2019
//
//-----------------------------------------------------------------------------------------------------------------------------------------
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
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

import Model.atm_dataset;
import Sequence.SequenceElement;
import Simulator_main.SIM;
import Toolbox.TextAreaOutputStream;
import Toolbox.Tool;
import javax.swing.JFileChooser;
import Controller.LandingCurve;

public class Plotting_3DOF implements  ActionListener {
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Main Container Frame Elements
    //-----------------------------------------------------------------------------------------------------------------------------------------
	static String PROJECT_TITLE = "  BlueBook DaLAT-3DoF  V0.1 ALPHA";
    static int x_init = 1350;
    static int y_init = 860 ;
    public static JFrame MAIN_frame;
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Relative File Paths
    //-----------------------------------------------------------------------------------------------------------------------------------------
	public static String Elevation_File_RES4 	= "/ELEVATION/LRO_4.csv";
	public static String Elevation_File_RES16	= "/ELEVATION/LRO_16.csv";
	public static String Elevation_File_RES64 	= "/ELEVATION/LRO_16.csv";
	public static String Elevation_File_RES128 	= "/ELEVATION/LRO_128.csv";
	public static String LOCALELEVATIONFILE		= "/LocalElevation.inp";   //
    public static String Init_File   			= "/INP/init.inp" ;		    // Input: Initial state
    public static String RES_File    			= "/results.txt"  ;       	 	// Input: result file 
    public static String CTR_001_File			= "/CTRL/cntrl_1.inp";		    // Controller 01 input file 
    public static String Prop_File 	 			= "/INP/PROP/prop.inp";		// Main propulsion ystem input file 
    public static String SEQU_File		 		= "/SEQU.res";				// Sequence output file 
    public static String ICON_File   	 		= "/lib/BB_icon.png";
    public static String SEQUENCE_File   		= "/INP/sequence_1.inp"; 
	public static String MAP_EARTH				= "/MAPS/Earth_MAP.jpg";
	public static String MAP_MOON				= "/MAPS/Moon_MAP.jpg";
	public static String MAP_VENUS				= "/MAPS/Venus_MAP.jpg";
	public static String MAP_MARS				= "/MAPS/Mars_MAP.jpg";
	public static String MAP_SOUTHPOLE_MOON		= "/MAPS/Moon_South_Pole.jpg";
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //												Constants
    //----------------------------------------------------------------------------------------------------------------------------------------- 
    public static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;
    public static double kB    = 1.380650424e-23;              // Boltzmann constant                         [SI]    
    public static double G     = 1.48808E-34;
    public static int TARGET;  
    public static  double RM = 0; 		// Target planet radius
    public static int indx_target = 0;  // Target planet indx 
	static double deg = PI/180.0; 		//Convert degrees to radians
	static double rad = 180/PI; 		//Convert radians to degrees
	
	public static String BB_delimiter = " ";
	public static String CurrentWorkfile_Name = "";
	public static File CurrentWorkfile_Path = new File("");
    //-----------------------------------------------------------------------------------------------------------------------------------------
    //											Styles, Fonts, Colors
    //-----------------------------------------------------------------------------------------------------------------------------------------
    public static int gg = 235;
    public static Color l_c = new Color(0,0,0);    					// Label Color
   	public static Color bc_c = new Color(255,255,255);				// Background Color
   	public static Color w_c = new Color(gg,gg,gg);					// Box background color
   	public static Color t_c = new Color(255,255,255);				// Table background color
   	
    static DecimalFormat decf = new DecimalFormat("#.#");
    static DecimalFormat df_X4 = new DecimalFormat("#####.###");
    static Font menufont            = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12);
    static Font labelfont_small     = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
    static Font labelfont_verysmall = new Font("Verdana", Font.BOLD, 7);
    static Font targetfont          = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    static Font HeadlineFont          = new Font("Georgia", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    public static DecimalFormat df = new DecimalFormat();
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
    public static String[] Axis_Option_NR = { "Time [s]",
    										  "Longitude [deg]", 
    										  "Latitude [deg]" ,
    										  "Altitude ref. Landing [m]", 
    										  "Altidue ref. mean [m]",
    										  "Radius [m]",
    										  "Velocity [m/s]", 
    										  "Flight Path angle [deg]", 
    										  "Local Azimuth [deg]", 
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
    										  "Thrust Force [N]", 
    										  "Cdm [-]",
    										  "SC Mass [kg]",
    										  "Normalized Deceleartion [-]",
    										  "Total Engergy [J]",
    										  "Thrust CMD [%]",
    										  "Tank filling level [%]",
    										  "Thrust [N]", 
    										  "Thrust to mass [N/kg]",
    										  "Velocity horizontal [m/s]",
    										  "Velocity vertical [m/s]",
    										  "Accumulated Delta-v [m/s]",
    										  "Active Sequence ID [-]",
    										  "Groundtrack [km]",
    										  "CNTRL Error [m/s]",
    										  "CNTRL Time [s]"
    										  };
    
    public static String[] Thrust_switch = { "Decelerate",
    										 "Accelerate"
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
    public static double h_init;
    public static double v_init;
    public static double v_touchdown;
	public static double Propellant_Mass=0;
	public static double M0;
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
    private static Crosshair xCrosshair_DashBoardOverviewChart;
    private static Crosshair yCrosshair_DashBoardOverviewChart; 
    public static Crosshair xCrosshair_A3_1,xCrosshair_A3_2,xCrosshair_A3_3,xCrosshair_A3_4,yCrosshair_A3_1,yCrosshair_A3_2,yCrosshair_A3_3,yCrosshair_A3_4;
    public static JPanel PageX04_Dashboard;
    public static JPanel PageX04_Map;
    public static JPanel PageX04_3;
    public static JPanel PageX04_SimSetup; 
    public static JPanel PageX04_PolarMap;
    public static JPanel PolarMapContainer; 
    public static JPanel PageX04_GroundClearance; 
    public static JPanel P1_Plotpanel;
    public static JPanel P1_SidePanel; 
    public static JSplitPane SplitPane_Page1_Charts; 
    static JFreeChart Chart_MercatorMap;
    static JFreeChart Chart_GroundClearance;
	static JFreeChart CHART_P1_DashBoardOverviewChart;
	static JFreeChart chart_PolarMap;	  
	static JFreeChart Chart_MercatorMap4;	
	static ChartPanel ChartPanel_DashBoardOverviewChart; 
	static ChartPanel ChartPanel_DashBoardFlexibleChart;
    private static Crosshair xCrosshair_DashboardFlexibleChart;
    private static Crosshair yCrosshair_DashboardFlexibleChart;    
    static public JFreeChart chartA3_1,chartA3_2,chartA3_3,chartA3_4; 
    public static ChartPanel CP_A31,CP_A32,CP_A33,CP_A34;
	public static DefaultTableXYDataset CHART_P1_DashBoardOverviewChart_Dataset = new DefaultTableXYDataset();
	public static DefaultTableXYDataset ResultSet_GroundClearance_FlightPath = new DefaultTableXYDataset();
	public static DefaultTableXYDataset ResultSet_GroundClearance_Elevation = new DefaultTableXYDataset();
	public static XYSeriesCollection ResultSet_FlexibleChart = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_1 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_2 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_3 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_4 = new XYSeriesCollection();
    public static JLabel INDICATOR_PageMap_LAT,INDICATOR_PageMap_LONG, INDICATOR_LAT,INDICATOR_LONG,INDICATOR_ALT,INDICATOR_VEL,INDICATOR_FPA,INDICATOR_AZI,INDICATOR_M0,INDICATOR_INTEGTIME, INDICATOR_TARGET;
    public static JTextField INPUT_LONG_Is,INPUT_LAT_Is,INPUT_ALT_Is,INPUT_VEL_Is,INPUT_FPA_Is,INPUT_AZI_Is;   // Input vector inertial Frame / spherical coordinates -> Is
    public static JTextField INPUT_LONG_Rs,INPUT_LAT_Rs,INPUT_ALT_Rs,INPUT_VEL_Rs,INPUT_FPA_Rs,INPUT_AZI_Rs;   // Input vector rotating Frame / spherical coordinates -> Rs
    public static JTextField INPUT_M0, INPUT_WRITETIME,INPUT_ISP,INPUT_PROPMASS,INPUT_THRUSTMAX,INPUT_THRUSTMIN,p42_inp14,p42_inp15,p42_inp16,p42_inp17;
    public static JTextField INPUT_PGAIN,INPUT_IGAIN,INPUT_DGAIN,INPUT_CTRLMAX,INPUT_CTRLMIN,INPUT_REFELEV;
    public static JLabel INDICATOR_VTOUCHDOWN ,INDICATOR_DELTAV, INDICATOR_PROPPERC, INDICATOR_RESPROP;
	public static String[] COLUMS_SEQUENCE = {"ID", 
			 					 "Sequence END type", 
			 					 "Sequence END value", 
			 					 "Sequence type", 
			 					 "Sequence FC",
			 					 "FC target velocity", 
			 					 "FC target altitude", 
			 					 "FC target curve"};
	public static String[] COLUMS_EventHandler = {"Event Type",
												  "Event Value"
	};
	 static int c_SEQUENCE = 8;
	 static Object[] ROW_SEQUENCE = new Object[c_SEQUENCE];
	 static DefaultTableModel MODEL_SEQUENCE;
	 static JTable TABLE_SEQUENCE;
	 
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
		public static JComboBox Target_chooser, Integrator_chooser,TargetCurve_chooser,ThrustSwitch_chooser;
		
		public static String[] SequenceENDType = {"Time [s]",
												  "Altitude [m]",
												  "Velocity [m/s]"
		};
		public static String[] SequenceType = {"Coasting (No Thrust)",
											   "Continous (unregulated) Thrust",
											   "Controlled Thrust (FC ON)",
											   "Constrained Thrust (FC OFF)"
		};
		public static String[] SequenceFC    = { "Flight Controller 1"};
		public static String[] FCTargetCurve = {"Parabolic Velocity-Altitude",
												"SquareRoot Velocity-Altitude",
												"Hover Parabolic entry"
		};
    
    Border Earth_border = BorderFactory.createLineBorder(Color.BLUE, 1);
    Border Moon_border 	= BorderFactory.createLineBorder(Color.GRAY, 1);
    Border Mars_border 	= BorderFactory.createLineBorder(Color.ORANGE, 1);
    Border Venus_border = BorderFactory.createLineBorder(Color.GREEN, 1);
    public static JCheckBox p421_linp0;
    private static List<atm_dataset> Page03_storage = new ArrayList<atm_dataset>(); // |1| time |2| altitude |3| velocity
    static XYSeriesCollection ResultSet_MercatorMap = new XYSeriesCollection();
    	static int page1_plot_y =380;
    	@SuppressWarnings("rawtypes")
		public static JComboBox axis_chooser, axis_chooser2,axis_chooser3,axis_chooser4; 
	//-----------------------------------------------------------------------------
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JPanel createContentPane () throws IOException{
    	JPanel MainGUI = new JPanel();
    	MainGUI = new JPanel();
    	MainGUI.setBackground(bc_c);
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
    	// ---------------------------------------------------------------------------------
    //       The following function contains all GUI elements of the main window
    // ---------------------------------------------------------------------------------
    	decf.setMaximumFractionDigits(1);
    	decf.setMinimumFractionDigits(1);
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();
        //menuBar.setLocation(0, 0);
        menuBar.setBackground(bc_c);
        menuBar.setPreferredSize(new Dimension(1200, 20));
        MainGUI.add(menuBar, BorderLayout.NORTH);

        JTabbedPane Page04_subtabPane = (JTabbedPane) new JTabbedPane();
        Page04_subtabPane.setPreferredSize(new Dimension(extx_main, exty_main));
        Page04_subtabPane.setBackground(bc_c);
        Page04_subtabPane.setForeground(l_c);
        //Build the first menu.
        JMenu menu_BlueBook = new JMenu("BlueBook");
        menu_BlueBook.setForeground(l_c);
        menu_BlueBook.setBackground(bc_c);
        menu_BlueBook.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_BlueBook);
        JMenuItem menuItem_OpenResultfile = new JMenuItem("Open Resultfile                 "); 
        menuItem_OpenResultfile.setForeground(Color.gray);
        menuItem_OpenResultfile.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_OpenResultfile);
        menuItem_OpenResultfile.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                    } });
        menu_BlueBook.addSeparator();
        JMenuItem menuItem_Import = new JMenuItem("                "); 
        menuItem_Import.setForeground(Color.gray);
        menuItem_Import.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_Import);
        menuItem_Import.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                    } });
        JMenuItem menuItem_Export = new JMenuItem("Results save as                "); 
        menuItem_Export.setForeground(Color.black);
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
	  	                filePath = filePath.replaceAll(".DaLAT", "");
	  	                File source = new File(RES_File);
	  	                File dest = new File(filePath+".DaLAT");
                	   try {
                	       FileUtils.copyFile(source, dest);
                	   } catch (IOException eIO) {System.out.println(eIO);}
                	   System.out.println("Result file "+file.getName()+" saved.");
                    } });
        menu_BlueBook.addSeparator();
        JMenuItem menuItem_Exit = new JMenuItem("Exit                  "); 
        menuItem_Exit.setForeground(Color.black);
        menuItem_Exit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_BlueBook.add(menuItem_Exit);
        menuItem_Exit.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   MAIN_frame.dispose();
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_SIM = new JMenu("Simulation");
        menu_SIM.setForeground(l_c);
        menu_SIM.setBackground(bc_c);
        menu_SIM.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_SIM);
        JMenuItem menuItem_SimSettings = new JMenuItem("Settings                 "); 
        menuItem_SimSettings.setForeground(Color.gray);
        menuItem_SimSettings.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_SIM.add(menuItem_SimSettings);
        menuItem_SimSettings.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_PreProcessing = new JMenu("PreProcessing");
        menu_PreProcessing.setForeground(l_c);
        menu_PreProcessing.setBackground(bc_c);
        menu_PreProcessing.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_PreProcessing);
        
        JMenuItem menuItem_ImportScenario = new JMenuItem("Simulation Setup Open               "); 
        menuItem_ImportScenario.setForeground(Color.black);
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
   	                filePath = filePath.replaceAll(".DaLAT", "");
                       file = new File(filePath + ".DaLAT");
               		   CurrentWorkfile_Path = file;
                       CurrentWorkfile_Name = fileChooser.getSelectedFile().getName();
                       MAIN_frame.setTitle("" + PROJECT_TITLE + " | " + CurrentWorkfile_Name.split("[.]")[0]);
                       try {IMPORT_Case();} catch (IOException e1) {System.out.println(e1);}
   					System.out.println("File "+CurrentWorkfile_Name+" opened.");

                	   Page04_subtabPane.setSelectedIndex(1);
                    } });
        JMenuItem menuItem_ExportScenario = new JMenuItem("Simulation Setup Save as              "); 
        menuItem_ExportScenario.setForeground(Color.black);
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
	                filePath = filePath.replaceAll(".DaLAT", "");
                    file = new File(filePath + ".DaLAT");
            		    CurrentWorkfile_Path = file;
                    CurrentWorkfile_Name = fileChooser.getSelectedFile().getName();
                    MAIN_frame.setTitle("" + PROJECT_TITLE + " | " + CurrentWorkfile_Name.split("[.]")[0]);
                    try {
						EXPORT_Case();
					} catch (FileNotFoundException e1) {
							System.out.println("File Not Found.");
					}
					System.out.println("File "+CurrentWorkfile_Name+" saved.");
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_PostProcessing = new JMenu("PostProcessing");
        menu_PostProcessing.setForeground(l_c);
        menu_PostProcessing.setBackground(bc_c);
        menu_PostProcessing.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_PostProcessing);
        
        JMenuItem menuItem_CreateLocalElevation = new JMenuItem("Create Local Elevation File               "); 
        menuItem_CreateLocalElevation.setForeground(Color.black);
        menuItem_CreateLocalElevation.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PostProcessing.add(menuItem_CreateLocalElevation);
        menuItem_CreateLocalElevation.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
	                   frame_CreateLocalElevationFile = new JFrame(PROJECT_TITLE);
		               	
	                   //Create and set up the content pane.
	                   Plotting_3DOF demo = new Plotting_3DOF();
	                   try {
	                	   frame_CreateLocalElevationFile.setContentPane(demo.WINDOW_CreateLocalElevationFile());
	                	  // frame_SEMR_NewEntry.pack();
	                   } catch (IOException e1) {
						// TODO Auto-generated catch block
	                	   e1.printStackTrace();
	                   } catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	                  // frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                   frame_CreateLocalElevationFile.setSize(330, 90);
	                   frame_CreateLocalElevationFile.setVisible(true);
	                   frame_CreateLocalElevationFile.setResizable(false);
	                   frame_CreateLocalElevationFile.setLocationRelativeTo(null);
	                   try {
	                       BufferedImage myImage = ImageIO.read(new File(ICON_File));
	                       frame_CreateLocalElevationFile.setIconImage(myImage);  
	                       }catch(IIOException eIIO) {
	                      	 System.out.println(eIIO);
	                       } catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    } });


        PageX04_Dashboard = new JPanel();
        PageX04_Dashboard.setLocation(0, 0);
        PageX04_Dashboard.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_Dashboard.setLayout(new BorderLayout());
        PageX04_Dashboard.setBackground(bc_c);
        PageX04_Dashboard.setForeground(l_c);
        PageX04_Map = new JPanel();
        PageX04_Map.setLocation(0, 0);
        PageX04_Map.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_Map.setLayout(new BorderLayout());
        PageX04_Map.setBackground(bc_c);
        PageX04_Map.setForeground(l_c);
        PageX04_3 = new JPanel();
        PageX04_3.setLocation(0, 0);
        PageX04_3.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_3.setLayout(new BorderLayout());
        PageX04_3.setBackground(bc_c);
        PageX04_3.setForeground(l_c);
        PageX04_SimSetup = new JPanel(); 
        PageX04_SimSetup.setLocation(0, 0);
        PageX04_SimSetup.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_SimSetup.setLayout(new BorderLayout());
        PageX04_SimSetup.setBackground(bc_c);
        PageX04_SimSetup.setForeground(l_c);
        PageX04_PolarMap = new JPanel();
        PageX04_PolarMap.setLocation(0, 0);
        PageX04_PolarMap.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_PolarMap.setLayout(new BorderLayout());
        PageX04_PolarMap.setBackground(bc_c);
        PageX04_PolarMap.setForeground(l_c);
        PageX04_GroundClearance = new JPanel();
        PageX04_GroundClearance.setLocation(0, 0);
        PageX04_GroundClearance.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_GroundClearance.setLayout(new BorderLayout());
        PageX04_GroundClearance.setBackground(bc_c);
        PageX04_GroundClearance.setForeground(l_c);

        // Page 4.1
        P1_SidePanel = new JPanel();
        P1_SidePanel.setLayout(null);
        P1_SidePanel.setPreferredSize(new Dimension(405, exty_main));
        P1_SidePanel.setBackground(bc_c);
        P1_SidePanel.setForeground(l_c);
        PageX04_Dashboard.add(P1_SidePanel, BorderLayout.LINE_START);
        
        P1_Plotpanel = new JPanel();
        P1_Plotpanel.setLayout(new BorderLayout());
        P1_Plotpanel.setPreferredSize(new Dimension(900, exty_main));
        P1_Plotpanel.setBackground(bc_c);
        P1_Plotpanel.setForeground(Color.white);
        PageX04_Dashboard.add(P1_Plotpanel,BorderLayout.LINE_END);
        
        SplitPane_Page1_Charts = new JSplitPane();
       	//SplitPane_Page1_Charts.setPreferredSize(new Dimension(1000, 1000));
       	SplitPane_Page1_Charts.setOrientation(JSplitPane.VERTICAL_SPLIT );
       	SplitPane_Page1_Charts.setDividerLocation(0.5);
       //	SplitPane_Page1_Charts.setForeground(Color.black);
       //	SplitPane_Page1_Charts.setBackground(Color.gray);
       	SplitPane_Page1_Charts.setDividerSize(3);
       	SplitPane_Page1_Charts.setUI(new BasicSplitPaneUI() {
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

       	SplitPane_Page1_Charts.addComponentListener(new ComponentListener(){

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
       	SplitPane_Page1_Charts.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, 
    		    new PropertyChangeListener() {
    		        @Override
    		        public void propertyChange(PropertyChangeEvent pce) {

    		        }
    		});
       	SplitPane_Page1_Charts.setDividerLocation(500);
       	P1_Plotpanel.add(SplitPane_Page1_Charts, BorderLayout.CENTER);
        
        JScrollPane scrollPane_P1 = new JScrollPane(P1_SidePanel);
        scrollPane_P1.setPreferredSize(new Dimension(415, exty_main));
        scrollPane_P1.getVerticalScrollBar().setUnitIncrement(16);
        PageX04_Dashboard.add(scrollPane_P1, BorderLayout.LINE_START);
        JScrollPane scrollPane1_P1 = new JScrollPane(P1_Plotpanel);
        scrollPane1_P1.getVerticalScrollBar().setUnitIncrement(16);
        PageX04_Dashboard.add(scrollPane1_P1, BorderLayout.CENTER);
        
        int uy_p41 = 10 ; 
        JLabel LABEL_LONG = new JLabel("Longitude [deg]");
        LABEL_LONG.setLocation(65, uy_p41 + 0 );
        LABEL_LONG.setSize(250, 20);
        LABEL_LONG.setBackground(Color.white);
        LABEL_LONG.setForeground(Color.black);
        P1_SidePanel.add(LABEL_LONG);
        JLabel LABEL_LAT = new JLabel("Latitude [deg]");
        LABEL_LAT.setLocation(65, uy_p41 + 25 );
        LABEL_LAT.setSize(250, 20);
        LABEL_LAT.setBackground(Color.white);
        LABEL_LAT.setForeground(Color.black);
        P1_SidePanel.add(LABEL_LAT);
        JLabel LABEL_ALT = new JLabel("Altitude [m]");
        LABEL_ALT.setLocation(65, uy_p41 + 50 );
        LABEL_ALT.setSize(250, 20);
        LABEL_ALT.setBackground(Color.white);
        LABEL_ALT.setForeground(Color.black);
        P1_SidePanel.add(LABEL_ALT);
        JLabel LABEL_VEL = new JLabel("Velocity [m/s]");
        LABEL_VEL.setLocation(65, uy_p41 + 75 );
        LABEL_VEL.setSize(250, 20);
        LABEL_VEL.setBackground(Color.white);
        LABEL_VEL.setForeground(Color.black);
        P1_SidePanel.add(LABEL_VEL);
        JLabel LABEL_FPA = new JLabel("Flight Path angle [deg]");
        LABEL_FPA.setLocation(65, uy_p41 + 100 );
        LABEL_FPA.setSize(250, 20);
        LABEL_FPA.setBackground(Color.white);
        LABEL_FPA.setForeground(Color.black);
        P1_SidePanel.add(LABEL_FPA);
        JLabel LABEL_AZI = new JLabel("Azimuth [deg]");
        LABEL_AZI.setLocation(65, uy_p41 + 125 );
        LABEL_AZI.setSize(250, 20);
        LABEL_AZI.setBackground(Color.white);
        LABEL_AZI.setForeground(Color.black);
        P1_SidePanel.add(LABEL_AZI);
        JLabel LABEL_M0 = new JLabel("Initial mass [kg]");
        LABEL_M0.setLocation(65, uy_p41 + 150 );
        LABEL_M0.setSize(250, 20);
        LABEL_M0.setBackground(Color.white);
        LABEL_M0.setForeground(Color.black);
        P1_SidePanel.add(LABEL_M0);
        JLabel LABEL_INTEGTIME = new JLabel("Integration time [s]");
        LABEL_INTEGTIME.setLocation(65, uy_p41 + 175 );
        LABEL_INTEGTIME.setSize(250, 20);
        LABEL_INTEGTIME.setBackground(Color.white);
        LABEL_INTEGTIME.setForeground(Color.black);
        P1_SidePanel.add(LABEL_INTEGTIME);
        
         INDICATOR_LONG = new JLabel();
        INDICATOR_LONG.setLocation(2, uy_p41 + 25 * 0 );
        INDICATOR_LONG.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_LONG);
        INDICATOR_LAT = new JLabel();
        INDICATOR_LAT.setLocation(2, uy_p41 + 25 * 1 );
        INDICATOR_LAT.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_LAT);
         INDICATOR_ALT = new JLabel();
        INDICATOR_ALT.setLocation(2, uy_p41 + 25 * 2 );
        INDICATOR_ALT.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_ALT);
        INDICATOR_VEL = new JLabel();
        INDICATOR_VEL.setLocation(2, uy_p41 + 25 * 3 );
        INDICATOR_VEL.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_VEL);
        INDICATOR_FPA = new JLabel();
        INDICATOR_FPA.setLocation(2, uy_p41 + 25 * 4 );
        INDICATOR_FPA.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_FPA);
        INDICATOR_AZI = new JLabel();
        INDICATOR_AZI.setLocation(2, uy_p41 + 25 * 5 );
        INDICATOR_AZI.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_AZI);        
        INDICATOR_M0 = new JLabel();
        INDICATOR_M0.setLocation(2, uy_p41 + 25 * 6 );
        INDICATOR_M0.setSize(60, 20);
        P1_SidePanel.add(INDICATOR_M0);
        INDICATOR_INTEGTIME = new JLabel();
        INDICATOR_INTEGTIME.setLocation(2, uy_p41 + 25 * 7 );
        INDICATOR_INTEGTIME.setSize(60, 20);
       P1_SidePanel.add(INDICATOR_INTEGTIME);
       
       JLabel LABEL_TARGET = new JLabel("Target Body:");
       LABEL_TARGET.setLocation(5, uy_p41 + 25 * 9  );
       LABEL_TARGET.setSize(250, 20);
       LABEL_TARGET.setBackground(Color.white);
       LABEL_TARGET.setForeground(Color.black);
       P1_SidePanel.add(LABEL_TARGET);
       INDICATOR_TARGET = new JLabel();
       INDICATOR_TARGET.setLocation(2, uy_p41 + 25 * 10 );
       INDICATOR_TARGET.setText("");
       INDICATOR_TARGET.setSize(100, 25);
       INDICATOR_TARGET.setHorizontalAlignment(SwingConstants.CENTER);
       INDICATOR_TARGET.setVerticalTextPosition(JLabel.CENTER);
       INDICATOR_TARGET.setFont(targetfont);
       INDICATOR_TARGET.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
      P1_SidePanel.add(INDICATOR_TARGET);
        
      
      JLabel LABEL_VTOUCHDOWN = new JLabel("Touchdown velocity [m/s]");
      LABEL_VTOUCHDOWN.setLocation(55, uy_p41 + 285  + 25 *0 );
      LABEL_VTOUCHDOWN.setSize(200, 20);
      LABEL_VTOUCHDOWN.setBackground(Color.black);
      LABEL_VTOUCHDOWN.setForeground(Color.black);
      P1_SidePanel.add(LABEL_VTOUCHDOWN);
      JLabel LABEL_DELTAV = new JLabel("Total D-V [m/s]");
      LABEL_DELTAV.setLocation(55, uy_p41 + 285 + 25 *1 );
      LABEL_DELTAV.setSize(200, 20);
      LABEL_DELTAV.setBackground(Color.black);
      LABEL_DELTAV.setForeground(Color.black);
      P1_SidePanel.add(LABEL_DELTAV);
      JLabel LABEL_PROPPERC = new JLabel("Used Propellant [kg]");
      LABEL_PROPPERC.setLocation(270, uy_p41 + 285 + 25 *0 );
      LABEL_PROPPERC.setSize(200, 20);
      LABEL_PROPPERC.setBackground(Color.black);
      LABEL_PROPPERC.setForeground(Color.black);
      P1_SidePanel.add(LABEL_PROPPERC);
      JLabel LABEL_RESPROP = new JLabel("Residual Propellant [%]");
      LABEL_RESPROP.setLocation(260, uy_p41 + 285 + 25 *1 );
      LABEL_RESPROP.setSize(200, 20);
      LABEL_RESPROP.setBackground(Color.black);
      LABEL_RESPROP.setForeground(Color.black);
      P1_SidePanel.add(LABEL_RESPROP);
      
       INDICATOR_VTOUCHDOWN = new JLabel("");
      INDICATOR_VTOUCHDOWN.setLocation(5, uy_p41 + 285  + 25 *0 );
      INDICATOR_VTOUCHDOWN.setSize(50, 20);
      INDICATOR_VTOUCHDOWN.setBackground(Color.black);
      INDICATOR_VTOUCHDOWN.setForeground(Color.black);
      P1_SidePanel.add(INDICATOR_VTOUCHDOWN);
       INDICATOR_DELTAV = new JLabel("");
      INDICATOR_DELTAV.setLocation(5, uy_p41 + 285 + 25 *1 );
      INDICATOR_DELTAV.setSize(50, 20);
      INDICATOR_DELTAV.setBackground(Color.black);
      INDICATOR_DELTAV.setForeground(Color.black);
      P1_SidePanel.add(INDICATOR_DELTAV);
       INDICATOR_PROPPERC = new JLabel("");
      INDICATOR_PROPPERC.setLocation(225, uy_p41 + 285 + 25 *0 );
      INDICATOR_PROPPERC.setSize(50, 20);
      INDICATOR_PROPPERC.setBackground(Color.black);
      INDICATOR_PROPPERC.setForeground(Color.black);
      P1_SidePanel.add(INDICATOR_PROPPERC);
       INDICATOR_RESPROP = new JLabel("");
      INDICATOR_RESPROP.setLocation(225, uy_p41 + 285 + 25 *1 );
      INDICATOR_RESPROP.setSize(40, 20);
      INDICATOR_RESPROP.setBackground(Color.black);
      INDICATOR_RESPROP.setForeground(Color.black);
      P1_SidePanel.add(INDICATOR_RESPROP);

        JButton ButtonUpdate = new JButton("Update");
        ButtonUpdate.setLocation(250, uy_p41 + 30 * 0);
        ButtonUpdate.setSize(145,25);
        ButtonUpdate.setBackground(Color.white);
        ButtonUpdate.setForeground(Color.black);
        ButtonUpdate.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
        		  UPDATE_Page01();
        	}} );
        P1_SidePanel.add(ButtonUpdate);
        
        JButton Button_RunSimulation = new JButton("Run Simulation");
        Button_RunSimulation.setLocation(250, uy_p41 + 30 * 1);
        Button_RunSimulation.setSize(145,25);
        Button_RunSimulation.setBackground(Color.white);
        Button_RunSimulation.setForeground(Color.black);
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
					  UPDATE_Page01();
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
        JLabel LABEL_CONSOLE = new JLabel("Console:");
        LABEL_CONSOLE.setLocation(5, uy_p41 + 25 *17 );
        LABEL_CONSOLE.setSize(200, 20);
        LABEL_CONSOLE.setBackground(Color.black);
        LABEL_CONSOLE.setForeground(Color.black);
        P1_SidePanel.add(LABEL_CONSOLE);
        
        JPanel JP_EnginModel = new JPanel();
        JP_EnginModel.setSize(390,200);
        JP_EnginModel.setLocation(5, uy_p41 + 25 * 18);
         JP_EnginModel.setBorder(BorderFactory.createEmptyBorder(1,1,1,1)); 
        //JP_EnginModel.setBackground(Color.red);
        taOutputStream = null; 
        taOutputStream = new TextAreaOutputStream(textArea, ""); 
        JScrollPane JSP_EnginModel = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JSP_EnginModel.setPreferredSize(new Dimension(395-10,200-10));
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
      LABEL_XAxis.setBackground(Color.white);
      LABEL_XAxis.setForeground(Color.black);
      P1_SidePanel.add(LABEL_XAxis);
      JLabel LABEL_YAxis = new JLabel("Y-Axis");
      LABEL_YAxis.setLocation(200, uy_p41 + 25 * 14 );
      LABEL_YAxis.setSize(150, 20);
      LABEL_YAxis.setHorizontalAlignment(0);
      LABEL_YAxis.setBackground(Color.white);
      LABEL_YAxis.setForeground(Color.black);
      P1_SidePanel.add(LABEL_YAxis);
	  axis_chooser = new JComboBox(Axis_Option_NR);
	  axis_chooser.setBackground(Color.white);
	  axis_chooser2 = new JComboBox(Axis_Option_NR);
	  axis_chooser2.setBackground(Color.white);
      axis_chooser2.setLocation(210, uy_p41 + 25 * 15);
      //axis_chooser2.setPreferredSize(new Dimension(150,25));
      axis_chooser2.setSize(180,25);
      axis_chooser2.setSelectedIndex(3);
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
      
      int INPUT_width = 120;
      int SidePanel_Width = 505; 
      JPanel P2_SidePanel = new JPanel();
      P2_SidePanel.setLayout(null);
      //P2_SidePanel.setPreferredSize(new Dimension(SidePanel_Width, exty_main+400));
      P2_SidePanel.setSize(SidePanel_Width, exty_main+400);
      P2_SidePanel.setBackground(bc_c);
      P2_SidePanel.setForeground(l_c);
      
      JPanel P2_SidePanel1 = new JPanel();
      P2_SidePanel1.setLayout(new BorderLayout());
      P2_SidePanel1.setPreferredSize(new Dimension(405, exty_main+400));
      P2_SidePanel1.setBackground(bc_c);
      P2_SidePanel1.setForeground(l_c);
      
      JScrollPane scrollPane_P2 = new JScrollPane(P2_SidePanel);
      scrollPane_P2.setPreferredSize(new Dimension(405, exty_main));
      scrollPane_P2.getVerticalScrollBar().setUnitIncrement(16);
      PageX04_SimSetup.add(scrollPane_P2, BorderLayout.LINE_START);
      JScrollPane scrollPane1_P2 = new JScrollPane(P2_SidePanel1);
      scrollPane1_P2.getVerticalScrollBar().setUnitIncrement(16);
      //scrollPane1_P2.setPreferredSize(new Dimension(405, exty_main));
      PageX04_SimSetup.add(scrollPane1_P2, BorderLayout.CENTER);
      //int uy_p41 = 10 ;
      JLabel LABEL_InitState = new JLabel("Initial State");
      LABEL_InitState.setLocation(5, uy_p41 + 25 * 0  );
      LABEL_InitState.setSize(350, 20);
      LABEL_InitState.setBackground(Color.white);
      LABEL_InitState.setForeground(Color.black);
      LABEL_InitState.setFont(HeadlineFont);
      LABEL_InitState.setHorizontalAlignment(0);
      P2_SidePanel.add(LABEL_InitState);
      
      JLabel LABEL_InertialFrame = new JLabel("Inertial Frame");
      LABEL_InertialFrame.setLocation(2, uy_p41 + 25 * 1 );
      LABEL_InertialFrame.setSize(INPUT_width, 20);
      LABEL_InertialFrame.setHorizontalAlignment(JLabel.CENTER);
      LABEL_InertialFrame.setBackground(Color.white);
      LABEL_InertialFrame.setForeground(Color.black);
      P2_SidePanel.add(LABEL_InertialFrame);
      JLabel LABEL_RotatingFrame = new JLabel("Rotating Frame");
      LABEL_RotatingFrame.setLocation(2+INPUT_width+5, uy_p41 + 25 * 1  );
      LABEL_RotatingFrame.setSize(INPUT_width, 20);
      LABEL_RotatingFrame.setHorizontalAlignment(JLabel.CENTER);
      LABEL_RotatingFrame.setBackground(Color.white);
      LABEL_RotatingFrame.setForeground(Color.black);
      P2_SidePanel.add(LABEL_RotatingFrame);
      
      JLabel LABEL_longitude = new JLabel("Longitude [deg]");
      LABEL_longitude.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 2  );
      LABEL_longitude.setSize(250, 20);
      LABEL_longitude.setBackground(Color.white);
      LABEL_longitude.setForeground(Color.black);
      P2_SidePanel.add(LABEL_longitude);
      JLabel LABEL_latitude = new JLabel("Latitude [deg]");
      LABEL_latitude.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 3 );
      LABEL_latitude.setSize(250, 20);
      LABEL_latitude.setBackground(Color.white);
      LABEL_latitude.setForeground(Color.black);
      P2_SidePanel.add(LABEL_latitude);
      JLabel LABEL_altitude = new JLabel("Altitude [m]");
      LABEL_altitude.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 4);
      LABEL_altitude.setSize(250, 20);
      LABEL_altitude.setBackground(Color.white);
      LABEL_altitude.setForeground(Color.black);
      P2_SidePanel.add(LABEL_altitude);
      
      JLabel LABEL_referenceelevation = new JLabel("Ref. Elevation [m]");
      LABEL_referenceelevation.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 5 );
      LABEL_referenceelevation.setSize(250, 20);
      LABEL_referenceelevation.setBackground(Color.white);
      LABEL_referenceelevation.setForeground(Color.black);
      P2_SidePanel.add(LABEL_referenceelevation);
      
      JLabel LABEL_velocity = new JLabel("Velocity [m/s]");
      LABEL_velocity.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 7 );
      LABEL_velocity.setSize(250, 20);
      LABEL_velocity.setBackground(Color.white);
      LABEL_velocity.setForeground(Color.black);
      P2_SidePanel.add(LABEL_velocity);
      JLabel LABEL_fpa = new JLabel("Flight Path angle [deg]");
      LABEL_fpa.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 8);
      LABEL_fpa.setSize(250, 20);
      LABEL_fpa.setBackground(Color.white);
      LABEL_fpa.setForeground(Color.black);
      P2_SidePanel.add(LABEL_fpa);
      JLabel LABEL_azimuth = new JLabel("Azimuth [deg]");
      LABEL_azimuth.setLocation(2+(INPUT_width+5)*2, uy_p41 + 25 * 9 );
      LABEL_azimuth.setSize(250, 20);
      LABEL_azimuth.setBackground(Color.white);
      LABEL_azimuth.setForeground(Color.black);
      P2_SidePanel.add(LABEL_azimuth);

      INPUT_LONG_Is = new JTextField(10);
      INPUT_LONG_Is.setLocation(2, uy_p41 + 25 * 2 );
      INPUT_LONG_Is.setSize(INPUT_width, 20);
      INPUT_LONG_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_LONG_Is.addActionListener(new ActionListener() {
    		  public void actionPerformed( ActionEvent e )
    		  	{ 
					WRITE_INIT();
					INPUT_LONG_Is.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    		}});
      P2_SidePanel.add(INPUT_LONG_Is);
      INPUT_LAT_Is = new JTextField(10);
      INPUT_LAT_Is.setLocation(2, uy_p41 + 25 * 3 );
      INPUT_LAT_Is.setSize(INPUT_width, 20);
      INPUT_LAT_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_LAT_Is.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_LAT_Is.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_LAT_Is);
      INPUT_ALT_Is = new JTextField(10);
      INPUT_ALT_Is.setLocation(2, uy_p41 + 25 * 4 );
      INPUT_ALT_Is.setSize(INPUT_width, 20);
      INPUT_ALT_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_ALT_Is.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_ALT_Is.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_ALT_Is);   
      INPUT_REFELEV = new JTextField(10);
      INPUT_REFELEV.setLocation(2+INPUT_width+5, uy_p41 + 25 * 5 );
      INPUT_REFELEV.setSize(INPUT_width, 20);
      INPUT_REFELEV.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_REFELEV.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_REFELEV.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_REFELEV); 
      INPUT_VEL_Is = new JTextField(10);
      INPUT_VEL_Is.setLocation(2, uy_p41 + 25 * 7 );
      INPUT_VEL_Is.setText("1");
      INPUT_VEL_Is.setSize(INPUT_width, 20);
      INPUT_VEL_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_VEL_Is.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_VEL_Is.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_VEL_Is);
      INPUT_FPA_Is = new JTextField(10);
      INPUT_FPA_Is.setLocation(2, uy_p41 + 25 * 8 );
      INPUT_FPA_Is.setText("0");
      INPUT_FPA_Is.setSize(INPUT_width, 20);
      INPUT_FPA_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_FPA_Is.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_FPA_Is.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_FPA_Is);
      INPUT_AZI_Is = new JTextField(10);
      INPUT_AZI_Is.setLocation(2, uy_p41 + 25 * 9 );
      INPUT_AZI_Is.setSize(INPUT_width, 20);
      INPUT_AZI_Is.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_AZI_Is.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_AZI_Is.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_AZI_Is);
      //---------------------------------------------------------
      INPUT_LONG_Rs = new JTextField(10);
      INPUT_LONG_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 2 );
      INPUT_LONG_Rs.setSize(INPUT_width, 20);
      INPUT_LONG_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_LONG_Rs.addActionListener(new ActionListener() {
    		  public void actionPerformed( ActionEvent e )
    		  	{ 
					WRITE_INIT();
					INPUT_LONG_Rs.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    		}});
      INPUT_LONG_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			
		}
    	  
      });
      P2_SidePanel.add(INPUT_LONG_Rs);
      INPUT_LAT_Rs = new JTextField(10);
      INPUT_LAT_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 3 );
      INPUT_LAT_Rs.setSize(INPUT_width, 20);
      INPUT_LAT_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_LAT_Rs.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_LAT_Rs.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_LAT_Rs);
      INPUT_ALT_Rs = new JTextField(10);
      INPUT_ALT_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 4 );
      INPUT_ALT_Rs.setSize(INPUT_width, 20);
      INPUT_ALT_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_ALT_Rs.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_ALT_Is.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_ALT_Rs);    
      INPUT_VEL_Rs = new JTextField(10);
      INPUT_VEL_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 7 );
      INPUT_VEL_Rs.setText("1");
      INPUT_VEL_Rs.setSize(INPUT_width, 20);
      INPUT_VEL_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_VEL_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			Rotating2Inertial();
		}
    	  
      });
      INPUT_VEL_Rs.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_VEL_Rs.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_VEL_Rs);
      INPUT_FPA_Rs = new JTextField(10);
      INPUT_FPA_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 8 );
      INPUT_FPA_Rs.setText("0");
      INPUT_FPA_Rs.setSize(INPUT_width, 20);
      INPUT_FPA_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_FPA_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			Rotating2Inertial();
		}
    	  
      });
      INPUT_FPA_Rs.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_FPA_Rs.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_FPA_Rs);
      INPUT_AZI_Rs = new JTextField(10);
      INPUT_AZI_Rs.setLocation(2+INPUT_width+5, uy_p41 + 25 * 9 );
      INPUT_AZI_Rs.setSize(INPUT_width, 20);
      INPUT_AZI_Rs.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_AZI_Rs.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			Rotating2Inertial();
		}
    	  
      });
      INPUT_AZI_Rs.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_AZI_Rs.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(INPUT_AZI_Rs);
      //--------------------------------------------------------------------------------
	  // Integrator definition panel       
      JPanel IntegratorInputPanel = new JPanel();
      IntegratorInputPanel.setLocation(0, uy_p41 + 25 * 12 );
      IntegratorInputPanel.setSize(SidePanel_Width, 470);
      IntegratorInputPanel.setBackground(Color.white);
      IntegratorInputPanel.setForeground(Color.white);
      IntegratorInputPanel.setLayout(null);
      P2_SidePanel.add(IntegratorInputPanel);
      
      JSeparator Separator_Page2_1 = new JSeparator();
      Separator_Page2_1.setLocation(0, uy_p41 + 25 * 0 );
      Separator_Page2_1.setSize(SidePanel_Width, 1);
      Separator_Page2_1.setBackground(Color.black);
      Separator_Page2_1.setForeground(Color.black);
      IntegratorInputPanel.add(Separator_Page2_1);
      
      JLabel LABEL_IntegSetting = new JLabel("Integrator Settings");
      LABEL_IntegSetting.setLocation(0, uy_p41 + 10 * 1 );
      LABEL_IntegSetting.setSize(400, 20);
      LABEL_IntegSetting.setBackground(Color.white);
      LABEL_IntegSetting.setForeground(Color.black);
      LABEL_IntegSetting.setFont(HeadlineFont);
      LABEL_IntegSetting.setHorizontalAlignment(0);
      IntegratorInputPanel.add(LABEL_IntegSetting);
   
      JLabel LABEL_writetime = new JLabel("Write time step [s]");
      LABEL_writetime.setLocation(65, uy_p41 + 25 * 3 );
      LABEL_writetime.setSize(250, 20);
      LABEL_writetime.setBackground(Color.white);
      LABEL_writetime.setForeground(Color.black);
      IntegratorInputPanel.add(LABEL_writetime);

     INPUT_WRITETIME = new JTextField(10);
     INPUT_WRITETIME.setLocation(2, uy_p41 + 25 * 3 );
     INPUT_WRITETIME.setSize(60, 20);
     INPUT_WRITETIME.setHorizontalAlignment(JTextField.RIGHT);
     INPUT_WRITETIME.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
		}});
     IntegratorInputPanel.add(INPUT_WRITETIME);

    JLabel LABEL_TARGETBODY = new JLabel("Target Body");
    LABEL_TARGETBODY.setLocation(163, uy_p41 + 25 * 4   );
    LABEL_TARGETBODY.setSize(150, 20);
    LABEL_TARGETBODY.setBackground(Color.white);
    LABEL_TARGETBODY.setForeground(Color.black);
    IntegratorInputPanel.add(LABEL_TARGETBODY);
    
	  Target_chooser = new JComboBox(Target_Options);
	  Target_chooser.setBackground(Color.white);
	  Target_chooser.setLocation(2, uy_p41 + 25 * 4 );
	  Target_chooser.setSize(150,25);
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
	  Integrator_chooser.setBackground(Color.white);
	  Integrator_chooser.setLocation(2, uy_p41 + 25 * 6 );
	  Integrator_chooser.setSize(380,25);
	  Integrator_chooser.setSelectedIndex(3);
	  Integrator_chooser.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
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
	  
	    JLabel LABEL_EventHandler = new JLabel("Stop conditions:");
	    LABEL_EventHandler.setLocation(2, uy_p41 + 25 * 8   );
	    LABEL_EventHandler.setSize(150, 20);
	    LABEL_EventHandler.setBackground(Color.white);
	    LABEL_EventHandler.setForeground(Color.black);
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
	    TABLE_EventHandler.setBackground(Color.white);
	    int tablewidth_EventHandler = 385;
	    int tableheight_EventHandler = 230;
	  // ((JTable) TABLE_SEQUENCE).setFillsViewportHeight(true);
	    TABLE_EventHandler.setBackground(Color.white);
	    TABLE_EventHandler.setForeground(Color.black);
	    TABLE_EventHandler.setSize(tablewidth_EventHandler, tableheight_EventHandler);
	    TABLE_EventHandler.getTableHeader().setReorderingAllowed(false);
	    TABLE_EventHandler.setRowHeight(35);

		    TableColumn EventHandlerType_colum   		 = 	    TABLE_EventHandler.getColumnModel().getColumn(0);
		    TableColumn EventHandlerValue_column 	     = 	    TABLE_EventHandler.getColumnModel().getColumn(1);

		    EventHandlerType_colum.setPreferredWidth(300);
		    EventHandlerValue_column.setPreferredWidth(100);

		    TABLE_EventHandler.getTableHeader().setBackground(Color.white);
		    TABLE_EventHandler.getTableHeader().setForeground(Color.black);
	    
	    EventHandlerTypeCombobox.setBackground(Color.white);
	    try {
	    for (int i=0;i<EventHandler_Type.length;i++) {EventHandlerTypeCombobox.addItem(EventHandler_Type[i]);}
	    } catch(NullPointerException eNPE) {
	    	System.out.println(eNPE);
	    }
	    EventHandlerType_colum.setCellEditor(new DefaultCellEditor(EventHandlerTypeCombobox));
	   

	    
	    JScrollPane TABLE_EventHandler_ScrollPane = new JScrollPane(TABLE_EventHandler,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    TABLE_EventHandler_ScrollPane.getVerticalScrollBar().setBackground(Color.white);
	    TABLE_EventHandler_ScrollPane.getHorizontalScrollBar().setBackground(Color.white);
	    TABLE_EventHandler_ScrollPane.setBackground(Color.white);
	    TABLE_EventHandler_ScrollPane.setSize(tablewidth_EventHandler,tableheight_EventHandler);
	    TABLE_EventHandler_ScrollPane.setLocation(2, uy_p41 + 25 * 9 );
	    TABLE_EventHandler_ScrollPane.setOpaque(false);
	    IntegratorInputPanel.add(TABLE_EventHandler_ScrollPane);
	    
    	ROW_EventHandler[0] = ""+EventHandler_Type[0];
    	ROW_EventHandler[1] = "";
    	MODEL_EventHandler.addRow(ROW_EventHandler);
    	
    	ROW_EventHandler[0] = ""+EventHandler_Type[1];
    	ROW_EventHandler[1] = "";
    	MODEL_EventHandler.addRow(ROW_EventHandler);

      //--------------------------------------------------------------------------------
	  // Spacecraft definition panel 
	  
      JPanel SpaceCraftInputPanel = new JPanel();
      SpaceCraftInputPanel.setLocation(0, uy_p41 + 26 * 30 );
      SpaceCraftInputPanel.setSize(SidePanel_Width, 600);
      SpaceCraftInputPanel.setBackground(Color.white);
      SpaceCraftInputPanel.setForeground(Color.white);
      SpaceCraftInputPanel.setLayout(null);
      P2_SidePanel.add(SpaceCraftInputPanel);
      
      JSeparator Separator_Page2_2 = new JSeparator();
      Separator_Page2_2.setLocation(0, 0 );
      Separator_Page2_2.setSize(SidePanel_Width, 1);
      Separator_Page2_2.setBackground(Color.black);
      Separator_Page2_2.setForeground(Color.black);
      SpaceCraftInputPanel.add(Separator_Page2_2);

	  // Space intended for advanced integrator settings 
      JLabel LABEL_SpaceCraftSettings = new JLabel("Spacecraft Settings");
      LABEL_SpaceCraftSettings.setLocation(0, uy_p41 + 10 * 0  );
      LABEL_SpaceCraftSettings.setSize(400, 20);
      LABEL_SpaceCraftSettings.setBackground(Color.white);
      LABEL_SpaceCraftSettings.setForeground(Color.black);
      LABEL_SpaceCraftSettings.setFont(HeadlineFont);
      LABEL_SpaceCraftSettings.setHorizontalAlignment(0);
      SpaceCraftInputPanel.add(LABEL_SpaceCraftSettings);
      JLabel LABEL_Minit = new JLabel("Initial mass [kg]");
      LABEL_Minit.setLocation(INPUT_width+5, uy_p41 + 25 * 1 );
      LABEL_Minit.setSize(250, 20);
      LABEL_Minit.setBackground(Color.white);
      LABEL_Minit.setForeground(Color.black);
      SpaceCraftInputPanel.add(LABEL_Minit);
      JLabel LABEL_ME_ISP = new JLabel("Main propulsion system ISP [s]");
      LABEL_ME_ISP.setLocation(INPUT_width+5, uy_p41 + 25 * 3 );
      LABEL_ME_ISP.setSize(300, 20);
      LABEL_ME_ISP.setBackground(Color.white);
      LABEL_ME_ISP.setForeground(Color.black);
      SpaceCraftInputPanel.add(LABEL_ME_ISP);
      JLabel LABEL_ME_PropMass = new JLabel("Main propulsion system propellant mass [kg]");
      LABEL_ME_PropMass.setLocation(INPUT_width+5, uy_p41 + 25 * 4);
      LABEL_ME_PropMass.setSize(300, 20);
      LABEL_ME_PropMass.setBackground(Color.white);
      LABEL_ME_PropMass.setForeground(Color.black);
      SpaceCraftInputPanel.add(LABEL_ME_PropMass);
      JLabel LABEL_ME_Thrust_max = new JLabel("Main propulsion system max. Thrust [N]");
      LABEL_ME_Thrust_max.setLocation(INPUT_width+5, uy_p41 + 25 * 5 );
      LABEL_ME_Thrust_max.setSize(300, 20);
      LABEL_ME_Thrust_max.setBackground(Color.white);
      LABEL_ME_Thrust_max.setForeground(Color.black);
      SpaceCraftInputPanel.add(LABEL_ME_Thrust_max);
      JLabel LABEL_ME_Thrust_min = new JLabel("Main Propulsion system min. Thrust [N]");
      LABEL_ME_Thrust_min.setLocation(INPUT_width+5, uy_p41 + 25 * 6 );
      LABEL_ME_Thrust_min.setSize(300, 20);
      LABEL_ME_Thrust_min.setBackground(Color.white);
      LABEL_ME_Thrust_min.setForeground(Color.black);
      SpaceCraftInputPanel.add(LABEL_ME_Thrust_min);
	 
      
      INPUT_M0 = new JTextField(10);
      INPUT_M0.setLocation(2, uy_p41 + 25 * 1 );
      INPUT_M0.setSize(INPUT_width, 20);
      INPUT_M0.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_M0.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  INPUT_M0.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      SpaceCraftInputPanel.add(INPUT_M0);
      INPUT_ISP = new JTextField(10);
      INPUT_ISP.setLocation(2, uy_p41 + 25 * 3 );
      INPUT_ISP.setSize(INPUT_width, 20);
      INPUT_ISP.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_ISP.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ WRITE_PROP();}});
      SpaceCraftInputPanel.add(INPUT_ISP);
     INPUT_PROPMASS = new JTextField(10);
     INPUT_PROPMASS.setLocation(2, uy_p41 + 25 * 4);
     INPUT_PROPMASS.setSize(INPUT_width, 20);
     INPUT_PROPMASS.setHorizontalAlignment(JTextField.RIGHT);
     INPUT_PROPMASS.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ WRITE_PROP();}});
     SpaceCraftInputPanel.add(INPUT_PROPMASS);        
     INPUT_THRUSTMAX = new JTextField(10);
     INPUT_THRUSTMAX.setLocation(2, uy_p41 + 25 * 5 );
     INPUT_THRUSTMAX.setSize(INPUT_width, 20);
     INPUT_THRUSTMAX.setHorizontalAlignment(JTextField.RIGHT);
     INPUT_THRUSTMAX.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ WRITE_PROP();}});
     SpaceCraftInputPanel.add(INPUT_THRUSTMAX);
     INPUT_THRUSTMIN = new JTextField(10);
     INPUT_THRUSTMIN.setLocation(2, uy_p41 + 25 * 6 );;
     INPUT_THRUSTMIN.setSize(INPUT_width, 20);
     INPUT_THRUSTMIN.setHorizontalAlignment(JTextField.RIGHT);
     INPUT_THRUSTMIN.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ WRITE_PROP();}});
     SpaceCraftInputPanel.add(INPUT_THRUSTMIN);
     
	  ThrustSwitch_chooser = new JComboBox(Thrust_switch);
	  ThrustSwitch_chooser.setBackground(Color.white);
	  ThrustSwitch_chooser.setLocation(2, uy_p41 + 25 * 8 );
	  ThrustSwitch_chooser.setSize(150,25);
	  ThrustSwitch_chooser.setSelectedIndex(0);
	  ThrustSwitch_chooser.addActionListener(new ActionListener() { 
   	  public void actionPerformed(ActionEvent e) {
   		
   	  }
 	  } );
	  ThrustSwitch_chooser.addFocusListener(new FocusListener() {

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
	  SpaceCraftInputPanel.add(ThrustSwitch_chooser);
	  //-------------------------------------------- 
	  //   Right side :
    JPanel P2_ControllerPane = new JPanel();
    P2_ControllerPane.setLayout(null);
    P2_ControllerPane.setPreferredSize(new Dimension((exty_main+400),290));
    P2_ControllerPane.setBackground(bc_c);
    P2_ControllerPane.setForeground(l_c);
    
    JPanel P2_SequenceMAIN = new JPanel();
    P2_SequenceMAIN.setLayout(new BorderLayout());
   // P2_SequenceMAIN.setPreferredSize(new Dimension(900, 400));
    P2_SequenceMAIN.setBackground(bc_c);
    P2_SequenceMAIN.setForeground(l_c);

    JScrollPane scrollPane_Controller = new JScrollPane(P2_ControllerPane);
    //scrollPane_Controller.setSize(405, exty_main);
    scrollPane_Controller.getVerticalScrollBar().setUnitIncrement(16);
    P2_SidePanel1.add(scrollPane_Controller, BorderLayout.CENTER);
    
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
          //  JTextField textfield= super.getComponent();
            try {
            	if (value.equals(""+SequenceType[2])) {
                    comp.setBackground(Color.orange);
                    comp.setForeground(Color.black);
            	} else if (value.equals(""+SequenceType[0])) {
                    comp.setBackground(Color.white);
                    comp.setForeground(Color.black);
            	} else if (value.equals(""+SequenceType[1])) {
                    comp.setBackground(Color.blue);
                    comp.setForeground(Color.white);
            	} else if (value.equals(""+SequenceType[3])) {
                    comp.setBackground(Color.green);
                    comp.setForeground(Color.black);
            	} else {
                    comp.setBackground(Color.white);
                    comp.setForeground(Color.black);
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
			if (column == 0 ){
				return false;
			} else {
				return true; 
			}
        }
    }; 
    MODEL_SEQUENCE.setColumnIdentifiers(COLUMS_SEQUENCE);
    TABLE_SEQUENCE.setModel(MODEL_SEQUENCE);
    TABLE_SEQUENCE.setBackground(Color.white);
    int tablewidth3 = 900;
    int tableheight3 = 400;
  // ((JTable) TABLE_SEQUENCE).setFillsViewportHeight(true);
    TABLE_SEQUENCE.setBackground(Color.white);
    TABLE_SEQUENCE.setForeground(Color.black);
    TABLE_SEQUENCE.setSize(tablewidth3, tableheight3);
    TABLE_SEQUENCE.getTableHeader().setReorderingAllowed(false);
    TABLE_SEQUENCE.setRowHeight(45);
    
   // TABLE_SEQUENCE.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	    TableColumn SequID_colum   			 = TABLE_SEQUENCE.getColumnModel().getColumn(0);
	    TableColumn SequENDTypeColumn 	     = TABLE_SEQUENCE.getColumnModel().getColumn(1);
	    TableColumn SequENDValColumn  		 = TABLE_SEQUENCE.getColumnModel().getColumn(2);
	    TableColumn SequTypeColumn 	   		 = TABLE_SEQUENCE.getColumnModel().getColumn(3);
	    TableColumn SequenceFCColumn 	  	 = TABLE_SEQUENCE.getColumnModel().getColumn(4);
	    TableColumn FCvelColumn 	   		 = TABLE_SEQUENCE.getColumnModel().getColumn(5);
	    TableColumn FCaltColumn	   			 = TABLE_SEQUENCE.getColumnModel().getColumn(6);
	    TableColumn FCtargetCurveColumn    	 = TABLE_SEQUENCE.getColumnModel().getColumn(7);

	    SequID_colum.setPreferredWidth(50);
	    SequENDTypeColumn.setPreferredWidth(100);
	    SequENDValColumn.setPreferredWidth(100);
	    SequTypeColumn.setPreferredWidth(180);
	    SequenceFCColumn.setPreferredWidth(180);
	    FCvelColumn.setPreferredWidth(150);
	    FCaltColumn.setPreferredWidth(150);
	    FCtargetCurveColumn.setPreferredWidth(150); 
    
    TABLE_SEQUENCE.getTableHeader().setBackground(Color.white);
    TABLE_SEQUENCE.getTableHeader().setForeground(Color.black);
    
    SequenceENDTypeCombobox.setBackground(Color.white);
    try {
    for (int i=0;i<SequenceENDType.length;i++) {
    	SequenceENDTypeCombobox.addItem(SequenceENDType[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    SequENDTypeColumn.setCellEditor(new DefaultCellEditor(SequenceENDTypeCombobox));
    
    SequenceTypeCombobox.setBackground(Color.white);
    try {
    for (int i=0;i<SequenceType.length;i++) {
    	SequenceTypeCombobox.addItem(SequenceType[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    SequTypeColumn.setCellEditor(new DefaultCellEditor(SequenceTypeCombobox));
    
    SequenceFCCombobox.setBackground(Color.white);
    try {
    for (int i=0;i<SequenceFC.length;i++) {
    	SequenceFCCombobox.addItem(SequenceFC[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    SequenceFCColumn.setCellEditor(new DefaultCellEditor(SequenceFCCombobox));
    
    FCTargetCurveCombobox.setBackground(Color.white);
    try {
    for (int i=0;i<FCTargetCurve.length;i++) {
    	FCTargetCurveCombobox.addItem(FCTargetCurve[i]);
    }
    } catch(NullPointerException eNPE) {
    	System.out.println(eNPE);
    }
    FCtargetCurveColumn.setCellEditor(new DefaultCellEditor(FCTargetCurveCombobox));
    
    JScrollPane TABLE_SEQUENCE_ScrollPane = new JScrollPane(TABLE_SEQUENCE,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    TABLE_SEQUENCE_ScrollPane.getVerticalScrollBar().setBackground(Color.white);
    TABLE_SEQUENCE_ScrollPane.getHorizontalScrollBar().setBackground(Color.white);
    TABLE_SEQUENCE_ScrollPane.setBackground(Color.white);
    TABLE_SEQUENCE_ScrollPane.setSize(tablewidth3,tableheight3);
    TABLE_SEQUENCE_ScrollPane.setOpaque(false);
    P2_SequenceMAIN.add(TABLE_SEQUENCE_ScrollPane, BorderLayout.PAGE_START);
    
    JPanel SequenceControlPanel = new JPanel();
    SequenceControlPanel.setLayout(null);
    SequenceControlPanel.setPreferredSize(new Dimension(400, 60));
    SequenceControlPanel.setBackground(Color.white);
    SequenceControlPanel.setForeground(l_c);
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
    
    JButton BUTTON_ImportSequence = new JButton("Import Sequence");
    BUTTON_ImportSequence.setLocation(625, 5);
    BUTTON_ImportSequence.setSize(145,25);
    BUTTON_ImportSequence.setEnabled(false);
    BUTTON_ImportSequence.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  
    	  } } );
    SequenceControlPanel.add(BUTTON_ImportSequence);
    
    JButton BUTTON_ExportSequence = new JButton("Export Sequence");
    BUTTON_ExportSequence.setLocation(625, 32);
    BUTTON_ExportSequence.setSize(145,25);
    BUTTON_ExportSequence.setEnabled(false);
    BUTTON_ExportSequence.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
    		  
    	  } } );
    SequenceControlPanel.add(BUTTON_ExportSequence);
    
  //-----------------------------------------------------------------------------------------------------------------------------
    JScrollPane scrollPane_Sequence = new JScrollPane(P2_SequenceMAIN,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane_Sequence.setSize(305, 400);
    scrollPane_Sequence.getVerticalScrollBar().setUnitIncrement(16);
    P2_SidePanel1.add(P2_SequenceMAIN, BorderLayout.PAGE_START);
    
	  p421_linp0 = new JCheckBox("Controller 001 ON/OFF");
	  p421_linp0.setLocation(2, uy_p41 + 25 * 0 );
	  p421_linp0.setBackground(Color.green);
	  p421_linp0.setSize(250, 20);
	  p421_linp0.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			 WRITE_CTRL_01();
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			 WRITE_CTRL_01();
		}
		  
	  });
	  p421_linp0.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			WRITE_CTRL_01();
			 if(p421_linp0.isSelected()) {p421_linp0.setBackground(Color.green);}else {p421_linp0.setBackground(Color.red);}
		}
		  
	  });
	  
	  P2_ControllerPane.add(p421_linp0);
	  
      JLabel LABEL_ctrl_PGAIN = new JLabel("P Gain [-]");
      LABEL_ctrl_PGAIN.setLocation(65, uy_p41 + 25 * 1 );
      LABEL_ctrl_PGAIN.setSize(250, 20);
      LABEL_ctrl_PGAIN.setBackground(Color.white);
      LABEL_ctrl_PGAIN.setForeground(Color.black);
      P2_ControllerPane.add(LABEL_ctrl_PGAIN);
      JLabel LABEL_ctrl_IGAIN = new JLabel("I Gain [-]");
      LABEL_ctrl_IGAIN.setLocation(65, uy_p41 + 25 * 2 );
      LABEL_ctrl_IGAIN.setSize(250, 20);
      LABEL_ctrl_IGAIN.setBackground(Color.white);
      LABEL_ctrl_IGAIN.setForeground(Color.black);
      P2_ControllerPane.add(LABEL_ctrl_IGAIN);
      JLabel LABEL_ctrl_DGAIN = new JLabel("D Gain [-]");
      LABEL_ctrl_DGAIN.setLocation(65, uy_p41 + 25 * 3 );
      LABEL_ctrl_DGAIN.setSize(250, 20);
      LABEL_ctrl_DGAIN.setBackground(Color.white);
      LABEL_ctrl_DGAIN.setForeground(Color.black);
      P2_ControllerPane.add(LABEL_ctrl_DGAIN);
      JLabel LABEL_ctrl_max = new JLabel("Maximum Output CMD [-]");
      LABEL_ctrl_max.setLocation(65, uy_p41 + 25 * 5 );
      LABEL_ctrl_max.setSize(250, 20);
      LABEL_ctrl_max.setBackground(Color.white);
      LABEL_ctrl_max.setForeground(Color.black);
      P2_ControllerPane.add(LABEL_ctrl_max);
      JLabel LABEL_ctrl_min = new JLabel("Minimum Output CMD [-]");
      LABEL_ctrl_min.setLocation(65, uy_p41 + 25 * 6 );
      LABEL_ctrl_min.setSize(250, 20);
      LABEL_ctrl_min.setBackground(Color.white);
      LABEL_ctrl_min.setForeground(Color.black);
      P2_ControllerPane.add(LABEL_ctrl_min);

      INPUT_PGAIN = new JTextField(10);
      INPUT_PGAIN.setLocation(2, uy_p41 + 25 * 1 );
      INPUT_PGAIN.setText("0");
      INPUT_PGAIN.setSize(60, 20);
      INPUT_PGAIN.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(INPUT_PGAIN);
      INPUT_IGAIN = new JTextField(10);
      INPUT_IGAIN.setLocation(2, uy_p41 + 25 * 2 );
      INPUT_IGAIN.setText("0");
      INPUT_IGAIN.setSize(60, 20);
      INPUT_IGAIN.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(INPUT_IGAIN);
      INPUT_DGAIN = new JTextField(10);
      INPUT_DGAIN.setLocation(2, uy_p41 + 25 * 3 );
      INPUT_DGAIN.setText("10");
      INPUT_DGAIN.setSize(60, 20);
      INPUT_DGAIN.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(INPUT_DGAIN);
       INPUT_CTRLMAX = new JTextField(10);
       INPUT_CTRLMAX.setLocation(2, uy_p41 + 25 * 5 );
       INPUT_CTRLMAX.setText("1");
       INPUT_CTRLMAX.setSize(60, 20);
      INPUT_CTRLMAX.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(INPUT_CTRLMAX);
       INPUT_CTRLMIN = new JTextField(10);
      INPUT_CTRLMIN.setLocation(2, uy_p41 + 25 * 6 );
      INPUT_CTRLMIN.setText("0");
      INPUT_CTRLMIN.setSize(60, 20);
      INPUT_CTRLMIN.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(INPUT_CTRLMIN);
        //-----------------------------------------------------------------------------------------
        // Page 4.3
        //-----------------------------------------------------------------------------------------
		JPanel SouthPanel = new JPanel();
		SouthPanel.setLayout(null);
		//mainPanelh1.setLocation(0, 0);
		SouthPanel.setBackground(Color.white);
		SouthPanel.setForeground(Color.black);
		SouthPanel.setPreferredSize(new Dimension(1200, 120));
		PageX04_Map.add(SouthPanel, BorderLayout.SOUTH);
	    
        int uy2 = 10; 

       
        JLabel LABEL_PageMapLONG = new JLabel("Longitude [deg]");
        LABEL_PageMapLONG.setLocation(425, uy2 + 0 );
        LABEL_PageMapLONG.setSize(250, 20);
        LABEL_PageMapLONG.setBackground(Color.white);
        LABEL_PageMapLONG.setForeground(Color.black);
        SouthPanel.add(LABEL_PageMapLONG);
        JLabel LABEL_PageMapLAT = new JLabel("Latitude [deg]");
        LABEL_PageMapLAT.setLocation(825, uy2 + 0 );
        LABEL_PageMapLAT.setSize(250, 20);
        LABEL_PageMapLAT.setBackground(Color.white);
        LABEL_PageMapLAT.setForeground(Color.black);
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
       PolarMapContainer.setBackground(bc_c);
       PageX04_PolarMap.add(PolarMapContainer, BorderLayout.CENTER);
	//-------------------------------------------------------------------------------------------------------------------------------
        // Create Charts:
        CreateChart_DashboardOverviewChart(RM);
     	CreateChart_DashBoardFlexibleChart();
     	CreateChart_MercatorMap();
     	CreateChart_PolarMap();
     	CreateChart_GroundClearance();
     	// Create Tabs:
        Page04_subtabPane.addTab("Dashboard" , null, PageX04_Dashboard, null);
        Page04_subtabPane.addTab("Simulation Setup"+"\u2713", null, PageX04_SimSetup, null);
        Page04_subtabPane.addTab("Map" , null, PageX04_Map, null);
        Page04_subtabPane.addTab("Polar Map" , null, PageX04_PolarMap, null);
        Page04_subtabPane.addTab("GroundClearance" , null, PageX04_GroundClearance, null);
        //Page04_subtabPane.addTab("Results" , null, PageX04_3, null);
        MainGUI.add(Page04_subtabPane);
        Page04_subtabPane.setSelectedIndex(0);
    		CreateChart_A01();
    		try {
				SET_MAP(indx_target);
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1);
				System.out.println("ERROR: Loading map failed.");
			}
        //------------------------------------------------------------------------
    		UPDATE_Page01();
    		READ_SEQUENCE();
        /*
        tabbedPane.addTab("3"+"|"+"DOF" , null, mainPanelX4, null);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_4);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        TabPanel.add(tabbedPane);
        tabbedPane.setSelectedIndex(0);
        */
       // tabbedPane.addTab("3|DOF" , null, mainPanel4, null);
       // tabbedPane.setMnemonicAt(0, KeyEvent.VK_4);
       // MainGUI.setSize(x_init,y_init);
        MainGUI.setOpaque(true);
        return MainGUI;
	}
    public void actionPerformed(ActionEvent e)  {
    	
    }
    
    public static void Rotating2Inertial() {
    	double vel_rotating = Double.parseDouble(INPUT_VEL_Rs.getText());
    	double fpa_rotating = Double.parseDouble(INPUT_FPA_Rs.getText())*deg;
    	double azi_rotating = Double.parseDouble(INPUT_AZI_Rs.getText())*deg;
    	double lat_rotating = Double.parseDouble(INPUT_LAT_Rs.getText())*deg;
    	double rm = 1737400;
    	double omega = 2.661861E-6;
    	double radius = Double.parseDouble(INPUT_ALT_Rs.getText())+rm;
    	double azimuth_intertFrame = Math.atan((vel_rotating+Math.cos(fpa_rotating)*Math.sin(azi_rotating)+omega*radius*Math.cos(lat_rotating))/(vel_rotating*Math.cos(fpa_rotating)*Math.cos(azi_rotating)));
    	double fpa_inertFrame = Math.acos(Math.sin(fpa_rotating)*Math.cos(azimuth_intertFrame)/(Math.cos(fpa_rotating)*Math.cos(azi_rotating)));
    	double vel_inertFrame = vel_rotating*Math.sin(fpa_rotating)/Math.sin(fpa_inertFrame);
    	INPUT_AZI_Is.setText(""+azimuth_intertFrame*rad);
    	INPUT_FPA_Is.setText(""+fpa_inertFrame*rad);
    	INPUT_VEL_Is.setText(""+vel_inertFrame);		
    }
    
    public static void Inertial2Rotating() {
    	
    }
    
    public static void AddSequence() {
    	int NumberOfSequences = MODEL_SEQUENCE.getRowCount();
    	ROW_SEQUENCE[0] = ""+NumberOfSequences;
    	ROW_SEQUENCE[1] = ""+SequenceENDType[0];
    	ROW_SEQUENCE[2] = "0";
    	ROW_SEQUENCE[3] = ""+SequenceType[0];
    	ROW_SEQUENCE[4] = ""+SequenceFC[0];
    	ROW_SEQUENCE[5] = "1";
    	ROW_SEQUENCE[6] = "1";
    	ROW_SEQUENCE[7] = ""+FCTargetCurve[0];	
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
            					for(int k=0;k<SequenceFC.length;k++) { if(str_val.equals(SequenceFC[k])){val=k+1;} }
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
            				} 
            		   }
            			wr.write(row+System.getProperty( "line.separator" ));
                }
                wr.close(); 
            } catch (IOException eIO){
            	System.out.println(eIO);
            }
    }

    
    public void UPDATE_Page01(){
		  try {
			READ_INPUT();
			SET_MAP(indx_target);
		} catch (IOException | URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    	CHART_P1_DashBoardOverviewChart_Dataset.removeAllSeries();
	    	try {
	    	CHART_P1_DashBoardOverviewChart_Dataset = AddDataset_DashboardOverviewChart(RM);
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
	    		
	    	}
	    	ResultSet_MercatorMap.removeAllSeries();
	    	try {
	    	ResultSet_MercatorMap = AddDataset_MAP();
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
      	    		System.out.println("Updated");
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
    	ROW_SEQUENCE[1] = ""+SequenceENDType[trigger_end_type];
    	ROW_SEQUENCE[2] = ""+trigger_end_value;
    	ROW_SEQUENCE[3] = ""+SequenceType[sequence_type-1];
    	ROW_SEQUENCE[4] = ""+SequenceFC[sequence_controller_ID-1];
    	ROW_SEQUENCE[5] = ""+ctrl_target_vel;
    	ROW_SEQUENCE[6] = ""+ctrl_target_alt;
    	ROW_SEQUENCE[7] = ""+FCTargetCurve[ctrl_target_curve-1];	
    	MODEL_SEQUENCE.addRow(ROW_SEQUENCE);
    	for(int i=0;i<MODEL_SEQUENCE.getRowCount();i++) {MODEL_SEQUENCE.setValueAt(""+i,i, 0);}
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
        		ThrustSwitch_chooser.setSelectedIndex(Integ_indx);
		    }
        	k++;
        }
        in.close();
        br.close();
        fstream.close();
        } catch (NullPointerException eNPE) { System.out.println(eNPE);}
      //------------------------------------------------------------------
      // Read from CTRL
      try {
          fstream = new FileInputStream(CTR_001_File);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading ctrl.inp failed.");} 
    DataInputStream in3 = new DataInputStream(fstream);
    @SuppressWarnings("resource")
		BufferedReader br3 = new BufferedReader(new InputStreamReader(in3));
    String strLine3;
    k = 0;
    try {
    while ((strLine3 = br3.readLine()) != null )   {
    	String[] tokens = strLine3.split(" ");
    	InitialState = Double.parseDouble(tokens[0]);
      if (k==0){
      int ctr_on_off = (int) InitialState; 
		if(ctr_on_off==1) {
			p421_linp0.setSelected(true);
		} else {
			p421_linp0.setSelected(false);
		}
		if(p421_linp0.isSelected()) {p421_linp0.setBackground(Color.green);}else {p421_linp0.setBackground(Color.red);}
    	} else if (k==1){
    		INPUT_PGAIN.setText(df_X4.format(InitialState)); 
    	//System.out.println(RM);
    	} else if (k==2){
    		INPUT_IGAIN.setText(df_X4.format(InitialState));
    	} else if (k==3){
    		INPUT_DGAIN.setText(df_X4.format(InitialState));
    	} else if (k==4){
    		INPUT_CTRLMAX.setText(decf.format(InitialState));
    	} else if (k==5){
    		INPUT_CTRLMIN.setText(decf.format(InitialState));
    	} else if (k==6){

    	} else if (k==7){

    	}
    	k++;
    }
    in3.close();
    br3.close();
    fstream.close();
    } catch (NullPointerException eNPE) { System.out.println(eNPE);}
    //------------------------------------------------------------------
    // Read from PROP
    try {
        fstream = new FileInputStream(Prop_File);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading env.inp failed.");} 
  in3 = new DataInputStream(fstream);
  @SuppressWarnings("resource")
  BufferedReader br4 = new BufferedReader(new InputStreamReader(in3));
  k = 0;
  try {
  while ((strLine3 = br4.readLine()) != null )   {
  	String[] tokens = strLine3.split(" ");
  	InitialState = Double.parseDouble(tokens[0]);
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

  	} else if (k==5){

  	} else if (k==6){

  	} else if (k==7){

  	}
  	k++;
  }
  in3.close();
  br4.close();
  fstream.close();
  } catch (NullPointerException eNPE) { System.out.println(eNPE);}  
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
		            } else if (i==12) {
		            	rr =  ThrustSwitch_chooser.getSelectedIndex() ;
		                wr.write(rr+System.getProperty( "line.separator" ));	
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
    
    public static void   WRITE_EventHandler() {
    	
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

        			} else if (i == 5 ){

            		} 
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
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
       				      tokens[7]+" "
       	);
       }
       br.close();
       } catch(NullPointerException | IOException eNPE) { System.out.println(eNPE);}
       return SEQUENCE_DATA;
    }
	public static double[][] FIND_ctrl_init_cond() throws IOException{
	   	   List<SequenceElement> SEQUENCE_DATA = new ArrayList<SequenceElement>(); 
	   	    SEQUENCE_DATA = SIM.READ_SEQUENCE();
	   	    double[][] INIT_CONDITIONS = new double[4][SEQUENCE_DATA.size()];
	   	    for (int i=0;i<SEQUENCE_DATA.size();i++) {
	   	    	
	   	    }
	   	    
	   	    return INIT_CONDITIONS;
	}
public static void IMPORT_Case() throws IOException {
	BufferedReader br = new BufferedReader(new FileReader(CurrentWorkfile_Path));
	DeleteAllSequence();
    String strLine;
    int indx_init=0;
    int indx_prop=0;
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
					 } else if (indx_init==12){ThrustSwitch_chooser.setSelectedIndex(Integer.parseInt(tokens[data_column]));
					 } 					     
        indx_init++;
    	} else if(tokens[0].equals("|PROP|")) {
			            if (indx_prop==0){INPUT_ISP.setText(decf.format(Double.parseDouble(tokens[data_column])));
			  	 } else if (indx_prop==1){INPUT_PROPMASS.setText(decf.format(Double.parseDouble(tokens[data_column])));
			 	 } else if (indx_prop==2){INPUT_THRUSTMAX.setText(decf.format(Double.parseDouble(tokens[data_column])));
			 	 } else if (indx_prop==3){INPUT_THRUSTMIN.setText(decf.format(Double.parseDouble(tokens[data_column])));
			 	 } 			     
		indx_prop++;
	    }  else if(tokens[0].equals("|SEQU|")) {
			       	int sequence_ID 			= Integer.parseInt(tokens[1]);
			       	int trigger_end_type 		= Integer.parseInt(tokens[2]);
			       	double trigger_end_value 	= Double.parseDouble(tokens[3]);
			       	int sequence_type		 	= Integer.parseInt(tokens[4]);
			       	int sequence_controller_ID 	= Integer.parseInt(tokens[5]);
			       	double ctrl_target_vel      = Double.parseDouble(tokens[6]);
			       	double ctrl_target_alt 		= Double.parseDouble(tokens[7]);
			       	int ctrl_target_curve       = Integer.parseInt(tokens[8]);
			    	ROW_SEQUENCE[0] = ""+sequence_ID;
			    	ROW_SEQUENCE[1] = ""+SequenceENDType[trigger_end_type];
			    	ROW_SEQUENCE[2] = ""+trigger_end_value;
			    	ROW_SEQUENCE[3] = ""+SequenceType[sequence_type-1];
			    	ROW_SEQUENCE[4] = ""+SequenceFC[sequence_controller_ID-1];
			    	ROW_SEQUENCE[5] = ""+ctrl_target_vel;
			    	ROW_SEQUENCE[6] = ""+ctrl_target_alt;
			    	ROW_SEQUENCE[7] = ""+FCTargetCurve[ctrl_target_curve-1];	
			    	MODEL_SEQUENCE.addRow(ROW_SEQUENCE);
			    	WriteSequenceINP();
	    }
    k++;
    }
    WRITE_INIT();
    WRITE_PROP();
    br.close();
}
public static void EXPORT_Case() throws FileNotFoundException {
	if ( CurrentWorkfile_Name.isEmpty()==false) {
        File file = CurrentWorkfile_Path ; 
        PrintWriter os;
		os = new PrintWriter(file);
	
    	for (int i = 0; i < 12; i++) {  // 					init.inp
        os.print("|INIT|" + BB_delimiter);
                       if (i==0){os.print("|LONGITUDE[DEG]|"+ BB_delimiter+INPUT_LONG_Rs.getText());
            	} else if (i==1){os.print("|LATITUDE[DEG]|"+ BB_delimiter+INPUT_LAT_Rs.getText());
            	} else if (i==2){os.print("|ALTITUDE[m]|"+ BB_delimiter+INPUT_ALT_Rs.getText());
            	} else if (i==3){os.print("|VELOCITY[m/s]|"+ BB_delimiter+INPUT_VEL_Rs.getText());
            	} else if (i==4){os.print("|FPA[DEG]|"+ BB_delimiter+INPUT_FPA_Rs.getText());
            	} else if (i==5){os.print("|AZIMUTH[DEG]|"+ BB_delimiter+INPUT_AZI_Rs.getText());
            	} else if (i==6){os.print("|INITMASS[kg]|"+ BB_delimiter+INPUT_M0.getText());
            	} else if (i==7){os.print("|INTEGTIME[s]|"+ BB_delimiter+MODEL_EventHandler.getValueAt( 0, 1));
            	} else if (i==8){os.print("|INTEG[-]|"+ BB_delimiter+Integrator_chooser.getSelectedIndex());
                } else if (i==9){os.print("|TARGET[-]|"+ BB_delimiter+Target_chooser.getSelectedIndex());
                } else if (i==10){os.print("|WRITET[s]|"+ BB_delimiter+INPUT_WRITETIME.getText());
                } else if (i==11){os.print("|REFELEVEVATION[m]|"+ BB_delimiter+INPUT_REFELEV.getText());
    		    } else if (i==11){os.print("|ThrustSwitch[-]|"+ BB_delimiter+ThrustSwitch_chooser.getSelectedIndex());
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
		     	} else if (i==4){
		     	} else if (i==5){
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
    	    		for(int k=0;k<SequenceFC.length;k++) {if(str_val.equals(SequenceFC[k])){val=k+1;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else if (col==7) {
    	    		String str_val = (String) MODEL_SEQUENCE.getValueAt(row, col);
    	    		int val=0;
    	    		for(int k=0;k<FCTargetCurve.length;k++) {if(str_val.equals(FCTargetCurve[k])){val=k+1;}}
    	    		os.print(val+ BB_delimiter);
    	    	} else {
		        os.print(MODEL_SEQUENCE.getValueAt(row, col)+ BB_delimiter);
    	    	}
    	    }
    	    os.println("");
    	}
       os.close();
	}
}
	public static DefaultTableXYDataset AddDataset_DashboardOverviewChart(double RM) throws IOException , FileNotFoundException, ArrayIndexOutOfBoundsException{
		ArrayList<String> SEQUENCE_DATA = new ArrayList<String>();
		SEQUENCE_DATA = Read_SEQU();
	   	XYSeries xyseries10 = new XYSeries("Target Trajectory", false, false); 
	   	XYSeries xyseries11 = new XYSeries("Trajectory", false, false); 
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
		           int active_sequence = Integer.parseInt(tokens[39]);
		           double xx=0;
		           INDICATOR_VTOUCHDOWN.setText(""+decf.format(Double.parseDouble(tokens[6])));
		           INDICATOR_DELTAV.setText(""+decf.format(Double.parseDouble(tokens[38])));
		           INDICATOR_PROPPERC.setText(""+(decf.format(M0-Double.parseDouble(tokens[29])))); 
		           INDICATOR_RESPROP.setText(""+decf.format(Double.parseDouble(tokens[33])));
		           int active_sequ_type =0; double ctrl_vinit=0; double ctrl_hinit=0; double ctrl_vel=0; double ctrl_alt=0;int ctrl_curve=0;
		           try {
		           String[] sequ_tokens  = SEQUENCE_DATA.get(active_sequence).split(" ");
		            active_sequ_type  = Integer.parseInt(sequ_tokens[1]);
		            ctrl_vinit 	 = Double.parseDouble(sequ_tokens[3]);
		            ctrl_hinit 	 = Double.parseDouble(sequ_tokens[4]);
		            ctrl_vel 	     = Double.parseDouble(sequ_tokens[5]);
		            ctrl_alt 		 = Double.parseDouble(sequ_tokens[6]);
		            ctrl_curve        = Integer.parseInt(sequ_tokens[7]);
		           } catch (java.lang.IndexOutOfBoundsException eIOBE){
		        	   System.out.println(eIOBE);
		           }
		           //System.out.println(ctrl_vinit+ " | "+ ctrl_hinit);
		          // System.out.println(active_sequence+ " | "+ ctrl_curve);
		           if(active_sequ_type==3) { // Controlled Propulsive flight
		    		    if  (ctrl_curve==0) {
		    		    	xyseries10.add(x  , 0); 
		   		        } else if (ctrl_curve==1) {
		    		         xx =    LandingCurve.ParabolicLandingCurve(ctrl_vinit, ctrl_hinit, ctrl_vel, ctrl_alt, y);
		  		             try { xyseries10.add(xx  , y); } catch(org.jfree.data.general.SeriesException eSE) {System.out.println(eSE);}
		    		    } else if (ctrl_curve==2) {
		    		    	 xx =   LandingCurve.SquarerootLandingCurve(ctrl_vinit, ctrl_hinit, ctrl_vel, ctrl_alt, y);
		    		    	 		try { xyseries10.add(xx  , y); } catch(org.jfree.data.general.SeriesException eSE) {System.out.println(eSE);}
		    		    } else if (ctrl_curve==3) {
		    		    	 xx =   LandingCurve.LinearLandingCurve(ctrl_vinit, ctrl_hinit, ctrl_vel, ctrl_alt, y);
		    		    	 		try { xyseries10.add(xx  , y); } catch(org.jfree.data.general.SeriesException eSE) {System.out.println(eSE);}
		    		    } 
		           } else {
		        	   try {
		        	   xyseries10.add(x  , 0);  
		        	   } catch(org.jfree.data.general.SeriesException eSE) {
		        		   System.out.println(eSE);
		        	   }
		           }
		           //System.out.println(xx+ " | "+ y);
		           try { 
		           xyseries11.add(x  , y);
		           } catch(org.jfree.data.general.SeriesException eSE) {
		        	   System.out.println(eSE);
		           }
		           }
	              
	       fstream.close();
	       in.close();
	       br.close();
		    CHART_P1_DashBoardOverviewChart_Dataset.addSeries(xyseries11); 
		    CHART_P1_DashBoardOverviewChart_Dataset.addSeries(xyseries10);
	              } catch (NullPointerException | IllegalArgumentException eNPE) { System.out.println(eNPE);}
	    return CHART_P1_DashBoardOverviewChart_Dataset;
	   }
	
	public static void CreateChart_DashboardOverviewChart(double RM) throws IOException {
		//result1.removeAllSeries();
		//try {
		//CHART_P1_DashBoardOverviewChart_Dataset = AddDataset_X43(RM);
		//} catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF2) {
		//	System.out.println(eFNF2);
		//}
	    //-----------------------------------------------------------------------------------
		//CHART_P1_DashBoardOverviewChart = ChartFactory.createScatterPlot("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset, PlotOrientation.VERTICAL, true, false, false); 
	    CHART_P1_DashBoardOverviewChart = ChartFactory.createStackedXYAreaChart("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset);//("", "Velocity [m/s]", "Altitude [m] ", CHART_P1_DashBoardOverviewChart_Dataset, PlotOrientation.VERTICAL, true, false, false); 
		XYPlot plot = (XYPlot)CHART_P1_DashBoardOverviewChart.getXYPlot(); 
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    plot.setRenderer(0, renderer); 
	    renderer.setSeriesPaint( 0 , Color.BLACK );	
		CHART_P1_DashBoardOverviewChart.setBackgroundPaint(Color.white);
		Font font3 = new Font("Dialog", Font.PLAIN, 12); 	
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(new Color(220,220,220));
		plot.setRangeGridlinePaint(new Color(220,220,220)); 
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		//domainAxis.setInverted(true);
		
		JPanel PlotPanel_X43 = new JPanel();
		PlotPanel_X43.setLayout(new BorderLayout());
		PlotPanel_X43.setPreferredSize(new Dimension(900, page1_plot_y));
		PlotPanel_X43.setBackground(Color.white);
	
		ChartPanel_DashBoardOverviewChart = new ChartPanel(CHART_P1_DashBoardOverviewChart);
		ChartPanel_DashBoardOverviewChart.setMaximumDrawHeight(50000);
		ChartPanel_DashBoardOverviewChart.setMaximumDrawWidth(50000);
		ChartPanel_DashBoardOverviewChart.setMinimumDrawHeight(0);
		ChartPanel_DashBoardOverviewChart.setMinimumDrawWidth(0);
		ChartPanel_DashBoardOverviewChart.setMouseWheelEnabled(true);
		ChartPanel_DashBoardOverviewChart.setPreferredSize(new Dimension(900, page1_plot_y));
		ChartPanel_DashBoardOverviewChart.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
	            Rectangle2D dataArea = Plotting_3DOF.ChartPanel_DashBoardOverviewChart.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);
	            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
	            Plotting_3DOF.xCrosshair_DashBoardOverviewChart.setValue(x);
	            Plotting_3DOF.yCrosshair_DashBoardOverviewChart.setValue(y);
	        }
	});
	    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
	    xCrosshair_DashBoardOverviewChart = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    xCrosshair_DashBoardOverviewChart.setLabelVisible(true);
	    yCrosshair_DashBoardOverviewChart = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    yCrosshair_DashBoardOverviewChart.setLabelVisible(true);
	    crosshairOverlay.addDomainCrosshair(xCrosshair_DashBoardOverviewChart);
	    crosshairOverlay.addRangeCrosshair(yCrosshair_DashBoardOverviewChart);
	    ChartPanel_DashBoardOverviewChart.addOverlay(crosshairOverlay);
	   PlotPanel_X43.add(ChartPanel_DashBoardOverviewChart,BorderLayout.PAGE_START);
	   // P1_Plotpanel.add(PlotPanel_X43,BorderLayout.PAGE_START);
	   SplitPane_Page1_Charts.add(ChartPanel_DashBoardOverviewChart, JSplitPane.TOP);
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
	    renderer.setSeriesPaint( 0 , Color.BLACK );	
		Chart_MercatorMap4.setBackgroundPaint(Color.white);
		Font font3 = new Font("Dialog", Font.PLAIN, 12); 	
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(new Color(220,220,220));
		plot.setRangeGridlinePaint(new Color(220,220,220)); 
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		//domainAxis.setInverted(true);
		
		JPanel PlotPanel_X44 = new JPanel();
		PlotPanel_X44.setLayout(new BorderLayout());
		//PlotPanel_X44.setPreferredSize(new Dimension(900, page1_plot_y));
		PlotPanel_X44.setBackground(Color.white);
	
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
	            Rectangle2D dataArea = Plotting_3DOF.ChartPanel_DashBoardFlexibleChart.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);
	            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
	            Plotting_3DOF.xCrosshair_DashboardFlexibleChart.setValue(x);
	            Plotting_3DOF.yCrosshair_DashboardFlexibleChart.setValue(y);
	        }
	});
	    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
	    xCrosshair_DashboardFlexibleChart = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    xCrosshair_DashboardFlexibleChart.setLabelVisible(true);
	    yCrosshair_DashboardFlexibleChart = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    yCrosshair_DashboardFlexibleChart.setLabelVisible(true);
	    crosshairOverlay.addDomainCrosshair(xCrosshair_DashboardFlexibleChart);
	    crosshairOverlay.addRangeCrosshair(yCrosshair_DashboardFlexibleChart);
	    ChartPanel_DashBoardFlexibleChart.addOverlay(crosshairOverlay);
	   PlotPanel_X44.add(ChartPanel_DashBoardFlexibleChart,BorderLayout.PAGE_START);
	    //P1_Plotpanel.add(PlotPanel_X44,BorderLayout.LINE_END);
	    //P1_Plotpanel.add(ChartPanel_DashBoardFlexibleChart,BorderLayout.CENTER);
	    SplitPane_Page1_Charts.add(ChartPanel_DashBoardFlexibleChart, JSplitPane.BOTTOM);
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
						            	 if(isangle) {xx = Double.parseDouble(tokens[x])*rad;} else {
						            		 		  xx = Double.parseDouble(tokens[x]);} 
						            	 }
						            if(y==3) {
						             yy = Double.parseDouble(tokens[y]);} else {
						            	 String x_axis_label = String.valueOf(axis_chooser2.getSelectedItem());
						            	 boolean isangle = x_axis_label.indexOf("[deg]") !=-1? true: false;
						            	 if(isangle) {yy = Double.parseDouble(tokens[y])*rad;} else {
						             yy = Double.parseDouble(tokens[y]);	}
						             }
						         	xyseries10.add(xx , yy);
					           }
	       in.close();
	    ResultSet_FlexibleChart.addSeries(xyseries10); 
	              } catch (NullPointerException eNPE) { System.out.println(eNPE);}
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
				  System.out.println(eIIO);
				  System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if(TARGET==3){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(MAP_VENUS));
		         plot2.setBackgroundImage(myImage); 
		  } catch(IIOException eIIO) {
			  System.out.println(eIIO);
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
		        	  System.out.println(eSE); 
		           }
		           try{
		           double local_elevation = Double.parseDouble(tokens2[0]);
		           xyseries_Elevation.add(x,local_elevation);
		           xyseries_Delta.add(x,y-local_elevation);
		           } catch ( org.jfree.data.general.SeriesException eSE){
		        	  System.out.println(eSE); 
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
		       CPXX4.setBackground(Color.white);
		       //CPXX4.setDomainZoomable(false);
		       //CPXX4.setRangeZoomable(false);
		       CPXX4.setMaximumDrawHeight(50000);
		       CPXX4.setMaximumDrawWidth(50000);
		       CPXX4.setMinimumDrawHeight(0);
		       CPXX4.setMinimumDrawWidth(0);
		       CPXX4.setPreferredSize(new Dimension(1300, 660));
		       PageX04_GroundClearance.add(CPXX4, BorderLayout.CENTER);	
	}
	public static XYSeriesCollection AddDataset_MAP() throws IOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
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
	public static void CreateChart_MercatorMap() throws IOException{
		 try {
		        ResultSet_MercatorMap = AddDataset_MAP(); 
		        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {
		        	System.out.println(" Error read for plot X40");
		        }

		        Chart_MercatorMap = ChartFactory.createScatterPlot("", "Longitude [deg]", "Latitude [deg] ", ResultSet_MercatorMap, PlotOrientation.VERTICAL, false, false, false); 
				XYPlot plot = (XYPlot)Chart_MercatorMap.getXYPlot(); 
		        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
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
		       CPXX4.setBackground(Color.white);
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
		               Plotting_3DOF.xCrosshair_x.setValue(x);
		               Plotting_3DOF.yCrosshair_x.setValue(y);
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
		ResultSet_MercatorMap.removeAllSeries();
        try {
        ResultSet_MercatorMap = AddDataset_MAP(); 
        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {
        	System.out.println(" Error read for plot X40");
        }

        chart_PolarMap = ChartFactory.createPolarChart("", ResultSet_MercatorMap, false, false, false);
  
		PolarPlot plot =  (PolarPlot) chart_PolarMap.getPlot();
		//PolarItemRenderer renderer = new PolarItemRenderer();
       // plot.setRenderer(0,  renderer);  
	
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
       CPXX4.setBackground(Color.white);
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
       double longitude = Double.parseDouble(tokens[1])*rad;     // Longitude 	[deg]
       double latitude  = Double.parseDouble(tokens[2])*rad;     // Latitude 	[deg]
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
	   	MainGUI.setBackground(Color.white);	
   		int extx = 370;
   		int exty = 400;
	  //----------------------------------------------------------------
   		
        JLabel Title = new JLabel("Select Resolution: ");
        Title.setLocation(5, 2 );
        Title.setSize(250, 15);
        Title.setBackground(Color.white);
        Title.setForeground(Color.black);
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
	           double y3 = Double.parseDouble(tokens[5])*rad;
	           
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
			TopPanel.setBackground(Color.white);
			JPanel BottomPanel = new JPanel();
			BottomPanel.setLayout(new BorderLayout());
			BottomPanel.setPreferredSize(new Dimension(extx_main, yplot));
			BottomPanel.setBackground(Color.white);
	    	
			JPanel PlotPanel_01 = new JPanel();
			PlotPanel_01.setLayout(new BorderLayout());
			//PlotPanel_01.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_01.setBackground(Color.white);
			JPanel PlotPanel_02 = new JPanel();
			PlotPanel_02.setLayout(new BorderLayout());
			//PlotPanel_02.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_02.setBackground(Color.white);
			JPanel PlotPanel_03 = new JPanel();
			PlotPanel_03.setLayout(new BorderLayout());
			//PlotPanel_03.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_03.setBackground(Color.white);
			JPanel PlotPanel_04 = new JPanel();
			PlotPanel_04.setLayout(new BorderLayout());
			//PlotPanel_04.setPreferredSize(new Dimension(xplot, yplot));
			PlotPanel_04.setBackground(Color.white);
			
			JPanel Midpanel = new JPanel();
			Midpanel.setLayout(null);
			//Midpanel.setPreferredSize(new Dimension(155, 300));
			Midpanel.setSize(155,300);
			Midpanel.setBackground(Color.white);
			BottomPanel.add(Midpanel, BorderLayout.CENTER);
			
		      JLabel p41_linp8 = new JLabel("X-Axis");
		      p41_linp8.setLocation(5, 10 + 25 * 1 );
		      //p41_linp8.setPreferredSize(new Dimension(150, 20));
		      p41_linp8.setHorizontalAlignment(0);
		      p41_linp8.setSize(150,20);
		      p41_linp8.setBackground(Color.white);
		      p41_linp8.setForeground(Color.black);
		      Midpanel.add(p41_linp8);
		      JLabel p41_linp9 = new JLabel("Y-Axis");
		      p41_linp9.setLocation(5, 10 + 25 * 4 );
		      //p41_linp9.setPreferredSize(new Dimension(150, 20));
		      p41_linp9.setSize(150, 20);
		      p41_linp9.setHorizontalAlignment(0);
		      p41_linp9.setBackground(Color.white);
		      p41_linp9.setForeground(Color.black);
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
	                Rectangle2D dataArea = Plotting_3DOF.CP_A31.getScreenDataArea();
	                //JFreeChart chart = event.getChart();
	                XYPlot plot = chartA3_1.getXYPlot();
	                ValueAxis xAxis = plot.getDomainAxis();
	                double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                        RectangleEdge.BOTTOM);
	                double y = DatasetUtilities.findYValue(chartA3_1.getXYPlot().getDataset(), 0, x);
	                Plotting_3DOF.xCrosshair_A3_1.setValue(x);
	                Plotting_3DOF.yCrosshair_A3_1.setValue(y);
	                //===================================================
	                double time = get_time(x);
	                double xx = time ;//xCrosshair_A3_1.getValue();
	                double yy = DatasetUtilities.findYValue((chartA3_2.getXYPlot()).getDataset(), 0, time);
	                Plotting_3DOF.xCrosshair_A3_2.setValue(xx);
	                Plotting_3DOF.yCrosshair_A3_2.setValue(yy);
	                //===================================================
	                double xxx = time ; xCrosshair_A3_1.getValue();
	                double yyy = DatasetUtilities.findYValue((chartA3_3.getXYPlot()).getDataset(), 0, time);
	                Plotting_3DOF.xCrosshair_A3_3.setValue(xxx);
	                Plotting_3DOF.yCrosshair_A3_3.setValue(yyy);
	                //===================================================
	                double xxxx = time ; // xCrosshair_A3_1.getValue();
	                double yyyy = DatasetUtilities.findYValue((chartA3_4.getXYPlot()).getDataset(), 0, time);
	                Plotting_3DOF.xCrosshair_A3_4.setValue(xxxx);
	                Plotting_3DOF.yCrosshair_A3_4.setValue(yyyy);
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
		time = Tool.LinearInterpolate( data_x , data_y , velocity);
//System.out.println(velocity + " | " + time);
		return time;
	}
    
	private static void createAndShowGUI() throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(false);
        MAIN_frame = new JFrame("" + PROJECT_TITLE);
        Plotting_3DOF demo = new Plotting_3DOF();
        JPanel tp = demo.createContentPane();
        tp.setPreferredSize(new java.awt.Dimension(x_init, y_init));
        MAIN_frame.add(tp, BorderLayout.CENTER);
        MAIN_frame.pack();
        MAIN_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MAIN_frame.setLocationRelativeTo(null);
        MAIN_frame.setExtendedState(MAIN_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        MAIN_frame.setVisible(true);
         try {
        	BufferedImage myImage = ImageIO.read(new File(ICON_File)); 
        	MAIN_frame.setIconImage(myImage);
         }catch(IIOException eIIO) {System.out.println(eIIO);}         
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

		public static int RETURN_TARGET(){
			return TARGET; 
		}
}