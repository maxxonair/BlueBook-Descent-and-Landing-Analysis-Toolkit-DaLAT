package GUI; 

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
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
import org.jfree.chart.plot.XYPlot;
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
import Controller.LandingCurve;

public class Plotting_3DOF implements  ActionListener {
	static //-----------------------------------------------------------------------------	
	String PROJECT_TITLE = "  BlueBook 3DOF V0.1";
    static int x_init = 1350;
    static int y_init = 860 ;
    
    public static DecimalFormat df = new DecimalFormat();
    
    public static JTextArea textArea = new JTextArea();
    public static TextAreaOutputStream  taOutputStream = new TextAreaOutputStream(textArea, ""); 
    
    public static String Init_File   	 = "\\INP\\init.inp" ;			// Input: Initial state
    public static String Env_File    	 = "\\INP\\env.inp"  ;  		// Input: target and environment
    public static String RES_File        = "\\results.txt"   ; 			// Input; result file
    public static String CTR_001_File    = "\\CTRL\\cntrl_1.inp";		// Controller 01 input file 
    public static String Prop_File   	 = "\\INP\\PROP\\prop.inp";		// Main propulsion system input file
    public static String SEQU_File		 = "\\SEQU.res";				// Sequence output file 
    public static String ICON_File       = "\\lib\\BB_icon.png";		// Logo png file path 
    public static String SEQUENCE_File   = "\\INP\\sequence_1.inp";  	// Sequence definition file 
    public static String Init_File_mac   = "/INP/init.inp" ;		    // Input: Initial state
    public static String Env_File_mac    = "/INP/env.inp"  ;  			// Input: target and environment
    public static String RES_File_mac    = "/results.txt"  ;       	 	// Input: result file 
    public static String CTR_001_File_mac= "/CTRL/cntrl_1.inp";		    // Controller 01 input file 
    public static String Prop_File_mac 	 = "/INP/PROP/prop.inp";		// Main propulsion ystem input file 
    public static String SEQU_File_mac		 = "/SEQU.res";				// Sequence output file 
    public static String ICON_File_mac   	 = "/lib/BB_icon.png";
    public static String SEQUENCE_File_mac   = "/INP/sequence_1.inp"; 
    public static boolean ShowWorkDirectory = true; 
    public static boolean macrun = true;
    
    public static double PI = 3.14159;
    
    public static int gg = 235;
    public static Color l_c = new Color(0,0,0);    					// Label Color
   	public static Color bc_c = new Color(255,255,255);				// Background Color
   	public static Color w_c = new Color(gg,gg,gg);					// Box background color
   	public static Color t_c = new Color(255,255,255);				// Table background color
   	
    static DecimalFormat decf = new DecimalFormat("#.#");
    static DecimalFormat df_X4 = new DecimalFormat("#.###");
    Font menufont            = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 12);
    Font labelfont_small     = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
    Font labelfont_verysmall = new Font("Verdana", Font.BOLD, 7);
    Font targetfont          = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    Font HeadlineFont          = new Font("Georgia", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    
    public static JFrame MAIN_frame;
    
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
    											   "Parabolic Hover" 	
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
    										  "Active Sequence ID [-]"
    										  };
 
    public static double h_init;
    public static double v_init;
    public static double v_touchdown;    
    private static Crosshair xCrosshair_x;
    private static Crosshair yCrosshair_x;   
    public static JPanel PageX04_1;
    public static JPanel PageX04_2;
    public static JPanel PageX04_3;
    public static JPanel PageX04_4;   
    public static JPanel P1_Plotpanel;
    public static JPanel P1_SidePanel;        
    static int extx_main = 1350;
    static int exty_main = 800;     
    static JFreeChart chartX4;
	static JFreeChart chartX43;
	static boolean chartX43_fd = true;	
	static DefaultTableXYDataset resultX43 = new DefaultTableXYDataset();
	static ChartPanel CPX43;
    private static Crosshair xCrosshair_X43;
    private static Crosshair yCrosshair_X43;    
	static JFreeChart chartX44;
	static boolean chartX44_fd = true;	
	static XYSeriesCollection resultX44 = new XYSeriesCollection();
	static ChartPanel CPX44;
    private static Crosshair xCrosshair_X44;
    private static Crosshair yCrosshair_X44;    
    static public JFreeChart chartA3_1,chartA3_2,chartA3_3,chartA3_4; 
    public static ChartPanel CP_A31,CP_A32,CP_A33,CP_A34;
    public static XYSeriesCollection result11_A3_1 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_2 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_3 = new XYSeriesCollection();
    public static XYSeriesCollection result11_A3_4 = new XYSeriesCollection();
    public static Crosshair xCrosshair_A3_1,xCrosshair_A3_2,xCrosshair_A3_3,xCrosshair_A3_4,yCrosshair_A3_1,yCrosshair_A3_2,yCrosshair_A3_3,yCrosshair_A3_4;
    public static boolean chartA3_fd=true;     
    public static JLabel p41_inp1,p41_inp2,p41_inp3,p41_inp4,p41_inp5,p41_inp6,p41_inp7,p41_inp8, p41_inp9;
    public static JTextField p42_inp1,p42_inp2,p42_inp3,p42_inp4,p42_inp5,p42_inp6,p42_inp7,p42_inp8, p42_inp9,p42_inp10,p42_inp11,p42_inp12,p42_inp13,p42_inp14,p42_inp15,p42_inp16,p42_inp17;
    public static JTextField p421_inp1,p421_inp2,p421_inp3,p421_inp4,p421_inp5,p421_inp6,p421_inp7,p421_inp8,p421_inp9;
    
	 static String[] columns3 = {"ID", 
			 					 "Sequence END type", 
			 					 "Sequence END value", 
			 					 "Sequence type", 
			 					 "Sequence FC",
			 					 "FC target velocity", 
			 					 "FC target altitude", 
			 					 "FC target curve"};
	 static int c3 = 8;
	 static Object[] row3 = new Object[c3];
	 static DefaultTableModel model3;
	 static JTable table3;
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceENDTypeCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceTypeCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox SequenceFCCombobox = new JComboBox();
		@SuppressWarnings("rawtypes")
		public static JComboBox FCTargetCurveCombobox = new JComboBox();
		
		public static String[] SequenceENDType = {"Time [s]",
												  "Altitude [m]",
												  "Velocity [m/s]"
		};
		public static String[] SequenceType = {"Coasting (No Thrust)",
											   "Continous (unregulated) Thrust",
											   "Controlled Thrust (FC ON)",
											   "Constrained Thrust (FC OFF)"
		};
		public static String[] SequenceFC = { "Flight Controller 1"};
		public static String[] FCTargetCurve = {"Parabolic Velocity-Altitude",
												"SquareRoot Velocity-Altitude",
												"Hover Parabolic entry"
		};
    
    @SuppressWarnings("rawtypes")
	public static JComboBox Target_chooser, Integrator_chooser,TargetCurve_chooser;
    
    Border Earth_border = BorderFactory.createLineBorder(Color.BLUE, 5);
    Border Moon_border 	= BorderFactory.createLineBorder(Color.GRAY, 5);
    Border Mars_border 	= BorderFactory.createLineBorder(Color.ORANGE, 5);
    Border Venus_border = BorderFactory.createLineBorder(Color.GREEN, 5);
    
    public static JCheckBox p421_linp0;
    
    
    private static List<atm_dataset> Page03_storage = new ArrayList<atm_dataset>(); // |1| time |2| altitude |3| velocity
    
    static XYSeriesCollection resultX40 = new XYSeriesCollection();
    

       // public static double PI    = 3.14159265359;                // PI                                       [-]
        public static double kB    = 1.380650424e-23;              // Boltzmann constant                         [SI]    
        public static double G     = 1.48808E-34;
        public static int TARGET; 
        
        public static  double RM = 0; 		// Target planet radius
        public static int indx_target = 0;  // Target planet indx 


    	static double deg = PI/180.0; 		//Convert degrees to radians
    	static double rad = 180/PI; 		//Convert radians to degrees
    	
    	static int page1_plot_y =380;
    	@SuppressWarnings("rawtypes")
		public static JComboBox axis_chooser, axis_chooser2,axis_chooser3,axis_chooser4; 
    	

		//public static List<atm_dataset> ATM_DATA = new ArrayList<atm_dataset>(); 

		
	
	//-----------------------------------------------------------------------------
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JPanel createContentPane () throws IOException{
    	JPanel MainGUI = new JPanel();
    	MainGUI = new JPanel();
    	MainGUI.setBackground(bc_c);
    	MainGUI.setLayout(new BorderLayout());

    	// init rm:
     if(ShowWorkDirectory) {}
     if(macrun) {
    	 String dir = System.getProperty("user.dir");
    	 Init_File = dir + Init_File_mac ;
    	 Env_File  = dir + Env_File_mac  ; 
    	 RES_File  = dir + RES_File_mac  ;
    	 CTR_001_File  = dir + CTR_001_File_mac  ;
    	 Prop_File  = dir + Prop_File_mac  ;
    	 SEQU_File = dir + SEQU_File_mac; 
    	 ICON_File = dir + ICON_File_mac; 
    	 SEQUENCE_File = dir +SEQUENCE_File_mac; 
    	 //System.out.println(Init_File);
     }
    	// ---------------------------------------------------------------------------------
        //           Page 04 - 3 DOF
        // ---------------------------------------------------------------------------------
    	decf.setMaximumFractionDigits(1);
    	decf.setMinimumFractionDigits(1);
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();
        //menuBar.setLocation(0, 0);
        menuBar.setBackground(bc_c);
        menuBar.setPreferredSize(new Dimension(1200, 20));
        MainGUI.add(menuBar, BorderLayout.NORTH);

        //Build the first menu.
        JMenu menu_SEMR = new JMenu("File");
        menu_SEMR.setForeground(l_c);
        menu_SEMR.setBackground(bc_c);
        menu_SEMR.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_SEMR);
        
        JMenuItem menuItem2 = new JMenuItem("Exit                  "); 
        menuItem2.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_SEMR.add(menuItem2);
        menuItem2.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   MAIN_frame.dispose();
                    } });
        
        JMenu menu_SIM = new JMenu("Sim");
        menu_SIM.setForeground(l_c);
        menu_SIM.setBackground(bc_c);
        menu_SIM.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_SIM);
        
        JTabbedPane Page04_subtabPane = (JTabbedPane) new JTabbedPane();
        Page04_subtabPane.setPreferredSize(new Dimension(extx_main, exty_main));
        Page04_subtabPane.setBackground(bc_c);
        Page04_subtabPane.setForeground(l_c);

        
        PageX04_1 = new JPanel();
        PageX04_1.setLocation(0, 0);
        PageX04_1.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_1.setLayout(new BorderLayout());
        PageX04_1.setBackground(bc_c);
        PageX04_1.setForeground(l_c);
        PageX04_2 = new JPanel();
        PageX04_2.setLocation(0, 0);
        PageX04_2.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_2.setLayout(new BorderLayout());
        PageX04_2.setBackground(bc_c);
        PageX04_2.setForeground(l_c);
        PageX04_3 = new JPanel();
        PageX04_3.setLocation(0, 0);
        PageX04_3.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_3.setLayout(new BorderLayout());
        PageX04_3.setBackground(bc_c);
        PageX04_3.setForeground(l_c);
        PageX04_4 = new JPanel(); 
        PageX04_4.setLocation(0, 0);
        PageX04_4.setPreferredSize(new Dimension(extx_main, exty_main));
        PageX04_4.setLayout(new BorderLayout());
        PageX04_4.setBackground(bc_c);
        PageX04_4.setForeground(l_c);
        
        
        
        // Page 4.1
        
        P1_SidePanel = new JPanel();
        P1_SidePanel.setLayout(null);
        P1_SidePanel.setPreferredSize(new Dimension(405, exty_main));
        P1_SidePanel.setBackground(bc_c);
        P1_SidePanel.setForeground(l_c);
        PageX04_1.add(P1_SidePanel, BorderLayout.LINE_START);
        
        P1_Plotpanel = new JPanel();
        P1_Plotpanel.setLayout(new BorderLayout());
        P1_Plotpanel.setPreferredSize(new Dimension(900, exty_main));
        P1_Plotpanel.setBackground(bc_c);
        P1_Plotpanel.setForeground(l_c);
        PageX04_1.add(P1_Plotpanel,BorderLayout.LINE_END);
        
        JScrollPane scrollPane_P1 = new JScrollPane(P1_SidePanel);
        scrollPane_P1.setPreferredSize(new Dimension(415, exty_main));
        scrollPane_P1.getVerticalScrollBar().setUnitIncrement(16);
        PageX04_1.add(scrollPane_P1, BorderLayout.LINE_START);
        JScrollPane scrollPane1_P1 = new JScrollPane(P1_Plotpanel);
        scrollPane1_P1.getVerticalScrollBar().setUnitIncrement(16);
        PageX04_1.add(scrollPane1_P1, BorderLayout.CENTER);
        
        int uy_p41 = 10 ; 
        JLabel p41_linp1 = new JLabel("Longitude [deg]");
        p41_linp1.setLocation(65, uy_p41 + 0 );
        p41_linp1.setSize(250, 20);
        p41_linp1.setBackground(Color.white);
        p41_linp1.setForeground(Color.black);
        P1_SidePanel.add(p41_linp1);
        JLabel p41_linp2 = new JLabel("Latitude [deg]");
        p41_linp2.setLocation(65, uy_p41 + 25 );
        p41_linp2.setSize(250, 20);
        p41_linp2.setBackground(Color.white);
        p41_linp2.setForeground(Color.black);
        P1_SidePanel.add(p41_linp2);
        JLabel p41_linp3 = new JLabel("Altitude [m]");
        p41_linp3.setLocation(65, uy_p41 + 50 );
        p41_linp3.setSize(250, 20);
        p41_linp3.setBackground(Color.white);
        p41_linp3.setForeground(Color.black);
        P1_SidePanel.add(p41_linp3);
        JLabel p41_linp4 = new JLabel("Velocity [m/s]");
        p41_linp4.setLocation(65, uy_p41 + 75 );
        p41_linp4.setSize(250, 20);
        p41_linp4.setBackground(Color.white);
        p41_linp4.setForeground(Color.black);
        P1_SidePanel.add(p41_linp4);
        JLabel p41_linp5 = new JLabel("Flight Path angle [deg]");
        p41_linp5.setLocation(65, uy_p41 + 100 );
        p41_linp5.setSize(250, 20);
        p41_linp5.setBackground(Color.white);
        p41_linp5.setForeground(Color.black);
        P1_SidePanel.add(p41_linp5);
        JLabel p41_linp6 = new JLabel("Azimuth [deg]");
        p41_linp6.setLocation(65, uy_p41 + 125 );
        p41_linp6.setSize(250, 20);
        p41_linp6.setBackground(Color.white);
        p41_linp6.setForeground(Color.black);
        P1_SidePanel.add(p41_linp6);
        JLabel p41_linp7 = new JLabel("Initial mass [kg]");
        p41_linp7.setLocation(65, uy_p41 + 150 );
        p41_linp7.setSize(250, 20);
        p41_linp7.setBackground(Color.white);
        p41_linp7.setForeground(Color.black);
        P1_SidePanel.add(p41_linp7);
        JLabel p41_linp10 = new JLabel("Integration time [s]");
        p41_linp10.setLocation(65, uy_p41 + 175 );
        p41_linp10.setSize(250, 20);
        p41_linp10.setBackground(Color.white);
        p41_linp10.setForeground(Color.black);
        P1_SidePanel.add(p41_linp10);
        
         p41_inp1 = new JLabel();
        p41_inp1.setLocation(2, uy_p41 + 25 * 0 );
        p41_inp1.setSize(60, 20);
        P1_SidePanel.add(p41_inp1);
         p41_inp2 = new JLabel();
        p41_inp2.setLocation(2, uy_p41 + 25 * 1 );
        p41_inp2.setSize(60, 20);
        P1_SidePanel.add(p41_inp2);
         p41_inp3 = new JLabel();
        p41_inp3.setLocation(2, uy_p41 + 25 * 2 );
        p41_inp3.setSize(60, 20);
        P1_SidePanel.add(p41_inp3);
         p41_inp4 = new JLabel();
        p41_inp4.setLocation(2, uy_p41 + 25 * 3 );
        p41_inp4.setSize(60, 20);
        P1_SidePanel.add(p41_inp4);
         p41_inp5 = new JLabel();
        p41_inp5.setLocation(2, uy_p41 + 25 * 4 );
        p41_inp5.setSize(60, 20);
        P1_SidePanel.add(p41_inp5);
         p41_inp6 = new JLabel();
        p41_inp6.setLocation(2, uy_p41 + 25 * 5 );
        p41_inp6.setSize(60, 20);
        P1_SidePanel.add(p41_inp6);        
         p41_inp7 = new JLabel();
        p41_inp7.setLocation(2, uy_p41 + 25 * 6 );
        p41_inp7.setSize(60, 20);
        P1_SidePanel.add(p41_inp7);
        p41_inp8 = new JLabel();
        p41_inp8.setLocation(2, uy_p41 + 25 * 7 );
        p41_inp8.setSize(60, 20);
       P1_SidePanel.add(p41_inp8);
       
       p41_inp9 = new JLabel();
       p41_inp9.setLocation(2, uy_p41 + 25 * 9 );
       p41_inp9.setText("");
       p41_inp9.setSize(100, 40);
       p41_inp9.setHorizontalAlignment(SwingConstants.CENTER);
       p41_inp9.setVerticalTextPosition(JLabel.CENTER);
       p41_inp9.setFont(targetfont);
       p41_inp9.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
      P1_SidePanel.add(p41_inp9);
        

        JButton ButtonUpdate = new JButton("Update");
        ButtonUpdate.setLocation(250, uy_p41 + 25 * 0);
        ButtonUpdate.setSize(145,25);
        ButtonUpdate.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
        		  UPDATE_Page01();
        	}} );
        P1_SidePanel.add(ButtonUpdate);
        
        JButton Button_RunSimulation = new JButton("Run Simulation");
        Button_RunSimulation.setLocation(250, uy_p41 + 25 * 7);
        Button_RunSimulation.setSize(145,25);
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
        JPanel JP_EnginModel = new JPanel();
        JP_EnginModel.setSize(390,200);
        JP_EnginModel.setLocation(5, uy_p41 + 25 * 17);
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

      JLabel p41_linp8 = new JLabel("X-Axis");
      p41_linp8.setLocation(5, uy_p41 + 25 * 14 );
      p41_linp8.setSize(150, 20);
      p41_linp8.setHorizontalAlignment(0);
      p41_linp8.setBackground(Color.white);
      p41_linp8.setForeground(Color.black);
      P1_SidePanel.add(p41_linp8);
      JLabel p41_linp9 = new JLabel("Y-Axis");
      p41_linp9.setLocation(200, uy_p41 + 25 * 14 );
      p41_linp9.setSize(150, 20);
      p41_linp9.setHorizontalAlignment(0);
      p41_linp9.setBackground(Color.white);
      p41_linp9.setForeground(Color.black);
      P1_SidePanel.add(p41_linp9);
	  axis_chooser = new JComboBox(Axis_Option_NR);
	  axis_chooser.setBackground(Color.white);
	  axis_chooser2 = new JComboBox(Axis_Option_NR);
	  axis_chooser2.setBackground(Color.white);
      axis_chooser2.setLocation(200, uy_p41 + 25 * 15);
      //axis_chooser2.setPreferredSize(new Dimension(150,25));
      axis_chooser2.setSize(150,25);
      axis_chooser2.setSelectedIndex(3);
      axis_chooser2.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
    		  Update_X44();
    	  }
  	  } );
      axis_chooser.setLocation(5, uy_p41 + 25 * 15);
      //axis_chooser.setPreferredSize(new Dimension(150,25));
      axis_chooser.setSize(150,25);
      axis_chooser.setSelectedIndex(0);
      axis_chooser.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
    		  Update_X44();
    	  }
  	  } );
      P1_SidePanel.add(axis_chooser);
      P1_SidePanel.add(axis_chooser2);
 //-----------------------------------------------------------------------------------------
 // 										Page 4.2
 //-----------------------------------------------------------------------------------------
      JPanel P2_SidePanel = new JPanel();
      P2_SidePanel.setLayout(null);
      P2_SidePanel.setPreferredSize(new Dimension(405, exty_main+400));
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
      PageX04_4.add(scrollPane_P2, BorderLayout.LINE_START);
      JScrollPane scrollPane1_P2 = new JScrollPane(P2_SidePanel1);
      scrollPane1_P2.getVerticalScrollBar().setUnitIncrement(16);
      //scrollPane1_P2.setPreferredSize(new Dimension(405, exty_main));
      PageX04_4.add(scrollPane1_P2, BorderLayout.CENTER);
      //int uy_p41 = 10 ;
      JLabel LABEL_InitState = new JLabel("Initial State");
      LABEL_InitState.setLocation(5, uy_p41 + 25 * 0  );
      LABEL_InitState.setSize(250, 20);
      LABEL_InitState.setBackground(Color.white);
      LABEL_InitState.setForeground(Color.black);
      LABEL_InitState.setFont(HeadlineFont);
      LABEL_InitState.setHorizontalAlignment(0);
      P2_SidePanel.add(LABEL_InitState);
      JLabel LABEL_longitude = new JLabel("Longitude [deg]");
      LABEL_longitude.setLocation(65, uy_p41 + 25 * 1  );
      LABEL_longitude.setSize(250, 20);
      LABEL_longitude.setBackground(Color.white);
      LABEL_longitude.setForeground(Color.black);
      P2_SidePanel.add(LABEL_longitude);
      JLabel LABEL_latitude = new JLabel("Latitude [deg]");
      LABEL_latitude.setLocation(65, uy_p41 + 25 * 2 );
      LABEL_latitude.setSize(250, 20);
      LABEL_latitude.setBackground(Color.white);
      LABEL_latitude.setForeground(Color.black);
      P2_SidePanel.add(LABEL_latitude);
      JLabel LABEL_altitude = new JLabel("Altitude [deg]");
      LABEL_altitude.setLocation(65, uy_p41 + 25 * 3 );
      LABEL_altitude.setSize(250, 20);
      LABEL_altitude.setBackground(Color.white);
      LABEL_altitude.setForeground(Color.black);
      P2_SidePanel.add(LABEL_altitude);
      
      JLabel LABEL_velocity = new JLabel("Velocity [m/s]");
      LABEL_velocity.setLocation(65, uy_p41 + 25 * 5 );
      LABEL_velocity.setSize(250, 20);
      LABEL_velocity.setBackground(Color.white);
      LABEL_velocity.setForeground(Color.black);
      P2_SidePanel.add(LABEL_velocity);
      JLabel LABEL_fpa = new JLabel("Flight Path angle [deg]");
      LABEL_fpa.setLocation(65, uy_p41 + 25 * 6);
      LABEL_fpa.setSize(250, 20);
      LABEL_fpa.setBackground(Color.white);
      LABEL_fpa.setForeground(Color.black);
      P2_SidePanel.add(LABEL_fpa);
      JLabel LABEL_azimuth = new JLabel("Azimuth [deg]");
      LABEL_azimuth.setLocation(65, uy_p41 + 25 * 7 );
      LABEL_azimuth.setSize(250, 20);
      LABEL_azimuth.setBackground(Color.white);
      LABEL_azimuth.setForeground(Color.black);
      P2_SidePanel.add(LABEL_azimuth);
      
      JSeparator Separator_Page2_1 = new JSeparator();
      Separator_Page2_1.setLocation(0, uy_p41 + 27 * 8 );
      Separator_Page2_1.setSize(400, 1);
      Separator_Page2_1.setBackground(Color.black);
      Separator_Page2_1.setForeground(Color.black);
      P2_SidePanel.add(Separator_Page2_1);
      
      JLabel LABEL_Minit = new JLabel("Initial mass [kg]");
      LABEL_Minit.setLocation(65, uy_p41 + 25 * 17 );
      LABEL_Minit.setSize(250, 20);
      LABEL_Minit.setBackground(Color.white);
      LABEL_Minit.setForeground(Color.black);
      P2_SidePanel.add(LABEL_Minit);
      JLabel LABEL_integtime = new JLabel("Integration time [s]");
      LABEL_integtime.setLocation(65, uy_p41 + 25 * 10 );
      LABEL_integtime.setSize(250, 20);
      LABEL_integtime.setBackground(Color.white);
      LABEL_integtime.setForeground(Color.black);
      P2_SidePanel.add(LABEL_integtime);
      JLabel LABEL_writetime = new JLabel("Write time step [s]");
      LABEL_writetime.setLocation(65, uy_p41 + 25 * 11 );
      LABEL_writetime.setSize(250, 20);
      LABEL_writetime.setBackground(Color.white);
      LABEL_writetime.setForeground(Color.black);
      P2_SidePanel.add(LABEL_writetime);
      JLabel LABEL_IntegSetting = new JLabel("Integrator Settings");
      LABEL_IntegSetting.setLocation(5, uy_p41 + 25 * 9 );
      LABEL_IntegSetting.setSize(250, 20);
      LABEL_IntegSetting.setBackground(Color.white);
      LABEL_IntegSetting.setForeground(Color.black);
      LABEL_IntegSetting.setFont(HeadlineFont);
      LABEL_IntegSetting.setHorizontalAlignment(0);
      P2_SidePanel.add(LABEL_IntegSetting);
      
      p42_inp1 = new JTextField(10);
      p42_inp1.setLocation(2, uy_p41 + 25 * 1 );
      p42_inp1.setSize(60, 20);
      p42_inp1.addActionListener(new ActionListener() {
    		  public void actionPerformed( ActionEvent e )
    		  	{ 
					WRITE_INIT();
				  p42_inp1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    		}});

      P2_SidePanel.add(p42_inp1);
      p42_inp2 = new JTextField(10);
      p42_inp2.setLocation(2, uy_p41 + 25 * 2 );
      p42_inp2.setSize(60, 20);
      p42_inp2.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  p42_inp2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(p42_inp2);
       p42_inp3 = new JTextField(10);
      p42_inp3.setLocation(2, uy_p41 + 25 * 3 );
      p42_inp3.setSize(60, 20);
      p42_inp3.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  p42_inp3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(p42_inp3);
      
       p42_inp4 = new JTextField(10);
      p42_inp4.setLocation(2, uy_p41 + 25 * 5 );
      p42_inp4.setText("1");
      p42_inp4.setSize(60, 20);
      p42_inp4.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  p42_inp4.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(p42_inp4);
       p42_inp5 = new JTextField(10);
      p42_inp5.setLocation(2, uy_p41 + 25 * 6 );
      p42_inp5.setText("0");
      p42_inp5.setSize(60, 20);
      p42_inp5.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  p42_inp5.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(p42_inp5);
       p42_inp6 = new JTextField(10);
      p42_inp6.setLocation(2, uy_p41 + 25 * 7 );
      p42_inp6.setSize(60, 20);
      p42_inp6.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  p42_inp6.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(p42_inp6);
      
       p42_inp7 = new JTextField(10);
      p42_inp7.setLocation(2, uy_p41 + 25 * 17 );
      p42_inp7.setSize(60, 20);
      p42_inp7.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  p42_inp7.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
      P2_SidePanel.add(p42_inp7);
      p42_inp8 = new JTextField(10);
      p42_inp8.setLocation(2, uy_p41 + 25 * 10 );
      p42_inp8.setSize(60, 20);
      p42_inp8.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
			  p42_inp8.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}});
     P2_SidePanel.add(p42_inp8);
     p42_inp13 = new JTextField(10);
     p42_inp13.setLocation(2, uy_p41 + 25 * 11 );
     p42_inp13.setSize(60, 20);
     p42_inp13.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ 
			  WRITE_INIT();
		}});
    P2_SidePanel.add(p42_inp13);

	  Target_chooser = new JComboBox(Target_Options);
	  Target_chooser.setBackground(Color.white);
	  Target_chooser.setLocation(2, uy_p41 + 25 * 12 );
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
	  P2_SidePanel.add(Target_chooser);
	  
	  Integrator_chooser = new JComboBox(Integrator_Options);
	  Integrator_chooser.setBackground(Color.white);
	  Integrator_chooser.setLocation(2, uy_p41 + 25 * 14 );
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
	  P2_SidePanel.add(Integrator_chooser);
	  
      JSeparator Separator_Page2_2 = new JSeparator();
      Separator_Page2_2.setLocation(0, uy_p41 + 26 * 15 );
      Separator_Page2_2.setSize(400, 1);
      Separator_Page2_2.setBackground(Color.black);
      Separator_Page2_2.setForeground(Color.black);
      P2_SidePanel.add(Separator_Page2_2);
	  
	  // Space intended for advanced integrator settings 
      JLabel LABEL_SpaceCraftSettings = new JLabel("Spacecraft Settings");
      LABEL_SpaceCraftSettings.setLocation(5, uy_p41 + 25 * 16  );
      LABEL_SpaceCraftSettings.setSize(250, 20);
      LABEL_SpaceCraftSettings.setBackground(Color.white);
      LABEL_SpaceCraftSettings.setForeground(Color.black);
      LABEL_SpaceCraftSettings.setFont(HeadlineFont);
      LABEL_SpaceCraftSettings.setHorizontalAlignment(0);
      P2_SidePanel.add(LABEL_SpaceCraftSettings);

      JLabel LABEL_ME_ISP = new JLabel("Main propulsion system ISP [s]");
      LABEL_ME_ISP.setLocation(65, uy_p41 + 25 * 18 );
      LABEL_ME_ISP.setSize(300, 20);
      LABEL_ME_ISP.setBackground(Color.white);
      LABEL_ME_ISP.setForeground(Color.black);
      P2_SidePanel.add(LABEL_ME_ISP);
      JLabel LABEL_ME_PropMass = new JLabel("Main propulsion system propellant mass [kg]");
      LABEL_ME_PropMass.setLocation(65, uy_p41 + 25 * 19);
      LABEL_ME_PropMass.setSize(300, 20);
      LABEL_ME_PropMass.setBackground(Color.white);
      LABEL_ME_PropMass.setForeground(Color.black);
      P2_SidePanel.add(LABEL_ME_PropMass);
      JLabel LABEL_ME_Thrust_max = new JLabel("Main propulsion system max. Thrust [N]");
      LABEL_ME_Thrust_max.setLocation(65, uy_p41 + 25 * 20 );
      LABEL_ME_Thrust_max.setSize(300, 20);
      LABEL_ME_Thrust_max.setBackground(Color.white);
      LABEL_ME_Thrust_max.setForeground(Color.black);
      P2_SidePanel.add(LABEL_ME_Thrust_max);
      JLabel LABEL_ME_Thrust_min = new JLabel("Main Propulsion system min. Thrust [N]");
      LABEL_ME_Thrust_min.setLocation(65, uy_p41 + 25 * 21 );
      LABEL_ME_Thrust_min.setSize(300, 20);
      LABEL_ME_Thrust_min.setBackground(Color.white);
      LABEL_ME_Thrust_min.setForeground(Color.black);
      P2_SidePanel.add(LABEL_ME_Thrust_min);
	  
      p42_inp9 = new JTextField(10);
     p42_inp9.setLocation(2, uy_p41 + 25 * 18 );
     p42_inp9.setSize(60, 20);
     p42_inp9.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ WRITE_PROP();}});
     P2_SidePanel.add(p42_inp9);
      p42_inp10 = new JTextField(10);
     p42_inp10.setLocation(2, uy_p41 + 25 * 19);
     p42_inp10.setSize(60, 20);
     p42_inp10.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ WRITE_PROP();}});
     P2_SidePanel.add(p42_inp10);        
      p42_inp11 = new JTextField(10);
     p42_inp11.setLocation(2, uy_p41 + 25 * 20 );
     p42_inp11.setSize(60, 20);
     p42_inp11.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ WRITE_PROP();}});
     P2_SidePanel.add(p42_inp11);
     p42_inp12 = new JTextField(10);
     p42_inp12.setLocation(2, uy_p41 + 25 * 21 );;
     p42_inp12.setSize(60, 20);
     p42_inp12.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ WRITE_PROP();}});
    P2_SidePanel.add(p42_inp12);
	  //-------------------------------------------- Right side 
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
    table3 = new JTable(){
   	 
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
   // table3.setFont(table_font);
    
	Action action3 = new AbstractAction()
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
        { WriteSequenceINP();}
    };
    @SuppressWarnings("unused")
	TableCellListener tcl3 = new TableCellListener(table3, action3);
    model3 = new DefaultTableModel(){

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
    model3.setColumnIdentifiers(columns3);
    table3.setModel(model3);
    table3.setBackground(Color.white);
    int tablewidth3 = 900;
    int tableheight3 = 400;
  // ((JTable) table3).setFillsViewportHeight(true);
    table3.setBackground(Color.white);
    table3.setForeground(Color.black);
    table3.setSize(tablewidth3, tableheight3);
    table3.getTableHeader().setReorderingAllowed(false);
    table3.setRowHeight(45);
    
   // table3.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	    TableColumn SequID_colum   			 = table3.getColumnModel().getColumn(0);
	    TableColumn SequENDTypeColumn 	     = table3.getColumnModel().getColumn(1);
	    TableColumn SequENDValColumn  		 = table3.getColumnModel().getColumn(2);
	    TableColumn SequTypeColumn 	   		 = table3.getColumnModel().getColumn(3);
	    TableColumn SequenceFCColumn 	  	 = table3.getColumnModel().getColumn(4);
	    TableColumn FCvelColumn 	   		 = table3.getColumnModel().getColumn(5);
	    TableColumn FCaltColumn	   			 = table3.getColumnModel().getColumn(6);
	    TableColumn FCtargetCurveColumn    	 = table3.getColumnModel().getColumn(7);

	    SequID_colum.setPreferredWidth(50);
	    SequENDTypeColumn.setPreferredWidth(100);
	    SequENDValColumn.setPreferredWidth(100);
	    SequTypeColumn.setPreferredWidth(180);
	    SequenceFCColumn.setPreferredWidth(180);
	    FCvelColumn.setPreferredWidth(150);
	    FCaltColumn.setPreferredWidth(150);
	    FCtargetCurveColumn.setPreferredWidth(150); 
    
    table3.getTableHeader().setBackground(Color.white);
    table3.getTableHeader().setForeground(Color.black);
    
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
    
    JScrollPane spTable3 = new JScrollPane(table3,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    spTable3.getVerticalScrollBar().setBackground(Color.white);
    spTable3.getHorizontalScrollBar().setBackground(Color.white);
    spTable3.setBackground(Color.white);
    spTable3.setSize(tablewidth3,tableheight3);
    spTable3.setOpaque(false);
    P2_SequenceMAIN.add(spTable3, BorderLayout.PAGE_START);
    
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
      JLabel LABEL_ctrl_add = new JLabel("Touchdown velocity [m/s]");
      LABEL_ctrl_add.setLocation(65, uy_p41 + 25 * 8 );
      LABEL_ctrl_add.setSize(250, 20);
      LABEL_ctrl_add.setBackground(Color.white);
      LABEL_ctrl_add.setForeground(Color.black);
      P2_ControllerPane.add(LABEL_ctrl_add);
      JLabel LABEL_ctrl_add2 = new JLabel("Hover Altitude [m]");
      LABEL_ctrl_add2.setLocation(65, uy_p41 + 25 * 9 );
      LABEL_ctrl_add2.setSize(250, 20);
      LABEL_ctrl_add2.setBackground(Color.white);
      LABEL_ctrl_add2.setForeground(Color.black);
      P2_ControllerPane.add(LABEL_ctrl_add2);
	  
      p421_inp1 = new JTextField(10);
      p421_inp1.setLocation(2, uy_p41 + 25 * 1 );
      p421_inp1.setText("0");
      p421_inp1.setSize(60, 20);
      p421_inp1.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(p421_inp1);
      p421_inp2 = new JTextField(10);
      p421_inp2.setLocation(2, uy_p41 + 25 * 2 );
      p421_inp2.setText("0");
      p421_inp2.setSize(60, 20);
      p421_inp2.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(p421_inp2);
       p421_inp3 = new JTextField(10);
      p421_inp3.setLocation(2, uy_p41 + 25 * 3 );
      p421_inp3.setText("10");
      p421_inp3.setSize(60, 20);
      p421_inp3.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(p421_inp3);
       p421_inp4 = new JTextField(10);
      p421_inp4.setLocation(2, uy_p41 + 25 * 5 );
      p421_inp4.setText("1");
      p421_inp4.setSize(60, 20);
      p421_inp4.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(p421_inp4);
       p421_inp5 = new JTextField(10);
      p421_inp5.setLocation(2, uy_p41 + 25 * 6 );
      p421_inp5.setText("0");
      p421_inp5.setSize(60, 20);
      p421_inp5.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_CTRL_01();}});
      P2_ControllerPane.add(p421_inp5);
       p421_inp6 = new JTextField(10);
      p421_inp6.setLocation(2, uy_p41 + 25 * 8 );
      p421_inp6.setText("0");
      p421_inp6.setSize(60, 20);
      p421_inp6.addActionListener(new ActionListener() {
 		  public void actionPerformed( ActionEvent e )
 		  	{ WRITE_INIT();}});
      P2_ControllerPane.add(p421_inp6); 
      p421_inp7 = new JTextField(10);
     p421_inp7.setLocation(2, uy_p41 + 25 * 9 );
     p421_inp7.setText("0");
     p421_inp7.setSize(60, 20);
     p421_inp7.addActionListener(new ActionListener() {
		  public void actionPerformed( ActionEvent e )
		  	{ WRITE_INIT();}});
     P2_ControllerPane.add(p421_inp7); 
      
	  TargetCurve_chooser = new JComboBox(TargetCurve_Options);
	  TargetCurve_chooser.setBackground(Color.white);
	  TargetCurve_chooser.setLocation(2, uy_p41 + 25 * 11 );
	  TargetCurve_chooser.setSize(150,25);
	  TargetCurve_chooser.setSelectedIndex(0);
	  TargetCurve_chooser.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) {
    		
    	  }
  	  } );
	  TargetCurve_chooser.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			 if(TargetCurve_chooser.getSelectedIndex()==0||TargetCurve_chooser.getSelectedIndex()==1) {
				 p421_inp6.setEditable(false);
				 p421_inp6.setEditable(true);
			 } else if (TargetCurve_chooser.getSelectedIndex()==2){
				 p421_inp6.setEditable(true);
				 p421_inp6.setEditable(false); 
			 }
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			 WRITE_INIT();
			 if(TargetCurve_chooser.getSelectedIndex()==0||TargetCurve_chooser.getSelectedIndex()==1) {
				 p421_inp6.setEditable(false);
				 p421_inp6.setEditable(true);
			 } else if (TargetCurve_chooser.getSelectedIndex()==2){
				 p421_inp6.setEditable(true);
				 p421_inp6.setEditable(false); 
			 }
		}
		  
	  });
	  P2_ControllerPane.add(TargetCurve_chooser);
        //-----------------------------------------------------------------------------------------
        // Page 4.3
        //-----------------------------------------------------------------------------------------
		JPanel SouthPanel = new JPanel();
		SouthPanel.setLayout(null);
		//mainPanelh1.setLocation(0, 0);
		SouthPanel.setBackground(Color.white);
		SouthPanel.setForeground(Color.black);
		SouthPanel.setPreferredSize(new Dimension(1200, 120));
		PageX04_2.add(SouthPanel, BorderLayout.SOUTH);
	    
        int uy2 = 10; 

       
        JLabel linp21 = new JLabel("Longitude [deg]");
        linp21.setLocation(425, uy2 + 0 );
        linp21.setSize(250, 20);
        linp21.setBackground(Color.white);
        linp21.setForeground(Color.black);
        SouthPanel.add(linp21);
        JLabel linp22 = new JLabel("Latitude [deg]");
        linp22.setLocation(825, uy2 + 0 );
        linp22.setSize(250, 20);
        linp22.setBackground(Color.white);
        linp22.setForeground(Color.black);
        SouthPanel.add(linp22);	
        
        JTextField inp21 = new JTextField(10);
        inp21.setLocation(425, uy2 + 30 );
        inp21.setText("");
        inp21.setSize(80, 20);
        SouthPanel.add(inp21);
        JTextField inp22 = new JTextField(10);
        inp22.setLocation(825, uy2 + 30 );
        inp22.setText("");
        inp22.setSize(80, 20);
        SouthPanel.add(inp22);
        
        
        
        try {
        resultX40 = AddDataset_MAP(); 
        } catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF) {
        	System.out.println(" Error read for plot X40");
        }

        chartX4 = ChartFactory.createScatterPlot("", "Longitude [deg]", "Latitude [deg] ", resultX40, PlotOrientation.VERTICAL, false, false, false); 
		XYPlot plot = (XYPlot)chartX4.getXYPlot(); 
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        plot.setRenderer(0, renderer);  
	
        chartX4.setBackgroundPaint(Color.white);
		
        plot.getDomainAxis().setLabelFont(labelfont_small);
        plot.getRangeAxis().setLabelFont(labelfont_small);
		
       final XYPlot plot2 = (XYPlot) chartX4.getPlot();
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
       ChartPanel CPXX4 = new ChartPanel(chartX4);
       CPXX4.setBackground(Color.white);
       CPXX4.setDomainZoomable(false);
       CPXX4.setRangeZoomable(false);
		//CP2.setMouseWheelEnabled(tru
       CPXX4.addChartMouseListener(new ChartMouseListener() {
           @Override
           public void chartMouseClicked(ChartMouseEvent event) {
        	   Rectangle2D dataArea2 = CPXX4.getScreenDataArea();
               Point2D p = CPXX4.translateScreenToJava2D(event.getTrigger().getPoint());
               double x = chartX4.getXYPlot().getDomainAxis().java2DToValue(event.getTrigger().getX(), dataArea2, RectangleEdge.BOTTOM);
               double y = plot2.getRangeAxis().java2DToValue(p.getY(), dataArea2, plot2.getRangeAxisEdge());
               inp21.setText("" + df_X4.format(x));
               inp22.setText("" + df_X4.format(y));
           }

           @Override
           public void chartMouseMoved(ChartMouseEvent event) {
        	   Rectangle2D dataArea2 = CPXX4.getScreenDataArea();
        	   Point2D p = CPXX4.translateScreenToJava2D(event.getTrigger().getPoint());
               ValueAxis xAxis2 = chartX4.getXYPlot().getDomainAxis();
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
       PageX04_2.add(CPXX4, BorderLayout.CENTER);
	
       
        // Page 4.3
     	CreateChart_X43(RM);
     	CreateChart_X44();
        Page04_subtabPane.addTab("Dashboard" , null, PageX04_1, null);
        Page04_subtabPane.addTab("Simulation Setup"+"\u2713", null, PageX04_4, null);
        Page04_subtabPane.addTab("Map" , null, PageX04_2, null);
        Page04_subtabPane.addTab("Results" , null, PageX04_3, null);
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
    
    public static void AddSequence() {
    	int NumberOfSequences = model3.getRowCount();
    	row3[0] = ""+NumberOfSequences;
    	row3[1] = ""+SequenceENDType[0];
    	row3[2] = "0";
    	row3[3] = ""+SequenceType[0];
    	row3[4] = ""+SequenceFC[0];
    	row3[5] = "1";
    	row3[6] = "1";
    	row3[7] = ""+FCTargetCurve[0];	
    	model3.addRow(row3);
    	
    	for(int i=0;i<model3.getRowCount();i++) {model3.setValueAt(""+i,i, 0);}
    }
    
    public static void DeleteSequence() {
    	int j = table3.getSelectedRow();
    	if (j >= 0){model3.removeRow(j);}
    	for(int i=0;i<model3.getRowCount();i++) {model3.setValueAt(""+i,i, 0);}
    }
    
    public static void UpSequence() {
        int[] rows2 = table3.getSelectedRows();
        model3.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]-1);
        table3.setRowSelectionInterval(rows2[0]-1, rows2[rows2.length-1]-1);
        for(int i=0;i<model3.getRowCount();i++) {model3.setValueAt(""+i,i, 0);}
    }
    
    public static void DownSequence() {
        int[] rows2 = table3.getSelectedRows();
        model3.moveRow(rows2[0],rows2[rows2.length-1],rows2[0]+1);
        table3.setRowSelectionInterval(rows2[0]+1, rows2[rows2.length-1]+1);
        for(int i=0;i<model3.getRowCount();i++) {model3.setValueAt(""+i,i, 0);}
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
                for (int i=0; i<model3.getRowCount(); i++)
                {
            			String row ="";
            			for(int j=0;j<model3.getColumnCount();j++) {
            				if(j==0) {
            					String val =  (String) model3.getValueAt(i, j);
            					row = row + val + " ";
            				}  else if(j==1) {
            					String str_val =  (String) model3.getValueAt(i, j);
            					int val = 0 ; 
            					for(int k=0;k<SequenceENDType.length;k++) { if(str_val.equals(SequenceENDType[k])){val=k;} }
            					row = row + val + " ";
            				} else if(j==2) {
            					String val =  (String) model3.getValueAt(i, j);
            					row = row + val + " ";
            				} else if(j==3) {
            					String str_val =  (String) model3.getValueAt(i, j);
            					int val = 0 ; 
            					for(int k=0;k<SequenceType.length;k++) { if(str_val.equals(SequenceType[k])){val=k+1;} }
            					row = row + val + " ";
            				} else if(j==4) {
            					String str_val =  (String) model3.getValueAt(i, j);
            					int val = 0 ; 
            					for(int k=0;k<SequenceFC.length;k++) { if(str_val.equals(SequenceFC[k])){val=k+1;} }
            					row = row + val + " ";
            				} else if(j==5) {
            					String val =  (String) model3.getValueAt(i, j);
            					row = row + val + " ";
            				} else if(j==6) {
            					String val =  (String) model3.getValueAt(i, j);
            					row = row + val + " ";
            				} else if(j==7) {
            					String str_val =  (String) model3.getValueAt(i, j);
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
	    	resultX43.removeAllSeries();
	    	try {
	    	resultX43 = AddDataset_X43(RM);
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
	    		
	    	}
	    	resultX40.removeAllSeries();
	    	try {
	    	resultX40 = AddDataset_MAP();
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
	    		
	    	}
	    	Update_X44();
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
    	row3[0] = ""+sequence_ID;
    	row3[1] = ""+SequenceENDType[trigger_end_type];
    	row3[2] = ""+trigger_end_value;
    	row3[3] = ""+SequenceType[sequence_type-1];
    	row3[4] = ""+SequenceFC[sequence_controller_ID-1];
    	row3[5] = ""+ctrl_target_vel;
    	row3[6] = ""+ctrl_target_alt;
    	row3[7] = ""+FCTargetCurve[ctrl_target_curve-1];	
    	model3.addRow(row3);
    	for(int i=0;i<model3.getRowCount();i++) {model3.setValueAt(""+i,i, 0);}
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
        		p41_inp1.setText(decf.format(InitialState));
        		p42_inp1.setText(decf.format(InitialState));
        	} else if (k==1){
        		p41_inp2.setText(decf.format( InitialState));
        		p42_inp2.setText(decf.format( InitialState));
        	} else if (k==2){
        		p41_inp3.setText(decf.format( InitialState));
        		p42_inp3.setText(decf.format( InitialState));
        		h_init = InitialState;
        	} else if (k==3){
        		p41_inp4.setText(decf.format(InitialState));
        		p42_inp4.setText(decf.format(InitialState));
        		v_init = InitialState;
        	} else if (k==4){
        		p41_inp5.setText(decf.format(InitialState));
        		p42_inp5.setText(decf.format(InitialState));
        	} else if (k==5){
        		p41_inp6.setText(decf.format(InitialState));
        		p42_inp6.setText(decf.format(InitialState));
        	} else if (k==6){
        		p41_inp7.setText(decf.format(InitialState));
        		p42_inp7.setText(decf.format(InitialState));
        	} else if (k==7){
        		p41_inp8.setText(decf.format(InitialState));
        		p42_inp8.setText(decf.format(InitialState));
        	} else if (k==8){
        		int Integ_indx = (int) InitialState;
        		Integrator_chooser.setSelectedIndex(Integ_indx);
            } else if (k==9){
        		int Target_indx = (int) InitialState;
        		Target_chooser.setSelectedIndex(Target_indx);
            } else if (k==10){
            	p42_inp13.setText(decf.format(InitialState)); // write dt
            } else if (k==11){
            	v_touchdown = InitialState;
            	p421_inp6.setText(decf.format(InitialState));
		    } else if (k==12) {
		    	int Integ_indx = (int) InitialState;
		    	TargetCurve_chooser.setSelectedIndex(Integ_indx);
		    	if(TargetCurve_chooser.getSelectedIndex()==0||TargetCurve_chooser.getSelectedIndex()==1) {
		    		p421_inp6.setText(decf.format(v_touchdown));
		    	} else if (TargetCurve_chooser.getSelectedIndex()==2) {
		    		p421_inp7.setText(decf.format(v_touchdown));
		    	}
		    }
        	k++;
        }
        in.close();
        br.close();
        fstream.close();
        } catch (NullPointerException eNPE) { System.out.println(eNPE);}
        //------------------------------------------------------------------
        // Read from env.inp
        try {
            fstream = new FileInputStream(Env_File);
} catch(IOException eIIO) { System.out.println(eIIO); System.out.println("ERROR: Reading env.inp failed.");} 
      DataInputStream in2 = new DataInputStream(fstream);
      @SuppressWarnings("resource")
		BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
      String strLine2;
      k = 0;
      try {
      while ((strLine2 = br2.readLine()) != null )   {
      	String[] tokens = strLine2.split(" ");
      	InitialState = Double.parseDouble(tokens[0]);
        if (k==0){
        indx_target = (int) InitialState; 
        p41_inp9.setText(Target_Options[indx_target]);
        Target_chooser.setSelectedIndex(indx_target);
        if(indx_target==0) {
        	p41_inp9.setBorder(Earth_border);
        } else if(indx_target==1){
        	p41_inp9.setBorder(Moon_border);
        } else if(indx_target==2){
        	p41_inp9.setBorder(Mars_border);
        } else if(indx_target==3){
        	p41_inp9.setBorder(Venus_border);
        }
      	} else if (k==1){
      		RM = InitialState; 
      	//System.out.println(RM);
      	} else if (k==2){
      	
      	} else if (k==3){
      		
      	} else if (k==4){
      		
      	} else if (k==5){
      	
      	} else if (k==6){

      	} else if (k==7){

      	}
      	k++;
      }
      in2.close();
      br2.close();
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
    		p421_inp1.setText(df_X4.format(InitialState)); 
    	//System.out.println(RM);
    	} else if (k==2){
    		p421_inp2.setText(df_X4.format(InitialState));
    	} else if (k==3){
    		p421_inp3.setText(df_X4.format(InitialState));
    	} else if (k==4){
    		p421_inp4.setText(decf.format(InitialState));
    	} else if (k==5){
    		p421_inp5.setText(decf.format(InitialState));
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
    	p42_inp9.setText(df_X4.format(InitialState)); 
  	} else if (k==1){
  		p42_inp10.setText(df_X4.format(InitialState)); 
  	//System.out.println(RM);
  	} else if (k==2){
  		p42_inp11.setText(df_X4.format(InitialState));
  	} else if (k==3){
  		p42_inp12.setText(df_X4.format(InitialState)); 
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

    public void WRITE_INIT() {
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
            for (int i = 0; i<=14; i++)
            {
        		if (i == 0 ){
        			r = Double.parseDouble(p42_inp1.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==1 ){
        			r = Double.parseDouble(p42_inp2.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
        			r = Double.parseDouble(p42_inp3.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
        			r = Double.parseDouble(p42_inp4.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i == 4 ){
            		r = Double.parseDouble(p42_inp5.getText()) ;
            		wr.write(r+System.getProperty( "line.separator" ));	
        			} else if (i == 5 ){
                	r = Double.parseDouble(p42_inp6.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} else if (i == 6 ){
                	r = Double.parseDouble(p42_inp7.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} else if (i == 7 ){
                    r = Double.parseDouble(p42_inp8.getText()) ;
                    wr.write(r+System.getProperty( "line.separator" ));	
		    		} else if (i == 8 ){
		            rr =  Integrator_chooser.getSelectedIndex() ;
		            wr.write(rr+System.getProperty( "line.separator" ));	
		    		} else if (i == 9 ){
		            rr =  Target_chooser.getSelectedIndex() ;
		            wr.write(rr+System.getProperty( "line.separator" ));	
		    		} else if (i == 10 ){
		            r = Double.parseDouble(p42_inp13.getText())  ; // delta-t write out
		            wr.write(r+System.getProperty( "line.separator" ));	
		    		} else if (i == 11 ){
		    			if(TargetCurve_chooser.getSelectedIndex()==0||TargetCurve_chooser.getSelectedIndex()==1) {
		            r = Double.parseDouble(p421_inp6.getText()) ; // v_touchdown
		            wr.write(r+System.getProperty( "line.separator" ));	
		    			} else if (TargetCurve_chooser.getSelectedIndex()==0) {
				    r = Double.parseDouble(p421_inp7.getText()) ; // v_touchdown
				    wr.write(r+System.getProperty( "line.separator" ));		
		    			}
		            } else if (i==12) {
		            	rr = TargetCurve_chooser.getSelectedIndex();
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
        			r = Double.parseDouble(p421_inp1.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
        			r = Double.parseDouble(p421_inp2.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
        			r = Double.parseDouble(p421_inp3.getText()) ;
        			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i == 4 ){
            		r = Double.parseDouble(p421_inp4.getText()) ;
            		wr.write(r+System.getProperty( "line.separator" ));	
        			} else if (i == 5 ){
                	r = Double.parseDouble(p421_inp5.getText()) ;
                	wr.write(r+System.getProperty( "line.separator" ));	
            		} 
		            }               
            wr.close();
            } catch (IOException eIO) {
            	System.out.println(eIO);
            }
    }
    
    
    public void WRITE_PROP() {
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
            			r = Double.parseDouble(p42_inp9.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==1 ){
            			r = Double.parseDouble(p42_inp10.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==2 ){
            			r = Double.parseDouble(p42_inp11.getText()) ;
            			wr.write(r+System.getProperty( "line.separator" ));
        			} else if (i ==3 ){
            			r = Double.parseDouble(p42_inp12.getText()) ;
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
    
    public void SET_MAP(int TARGET) throws URISyntaxException, IOException{
    	final XYPlot plot2 = (XYPlot) chartX4.getPlot();
		  if (TARGET==0){ 
			  try {
		         BufferedImage myImage = ImageIO.read(new File(".\\MAPS\\Earth_MAP.jpg"));
		         plot2.setBackgroundImage(myImage);  
			  } catch(IIOException eIIO) {
				  System.out.println(eIIO);System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if (TARGET==1){
			  try {
		         BufferedImage myImage = ImageIO.read(new File( ".\\MAPS\\Moon_MAP.jpg"));
		         plot2.setBackgroundImage(myImage);  
			  } catch(IIOException eIIO) {
				  System.out.println(eIIO);System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if(TARGET==2){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(".\\MAPS\\Mars_MAP.jpg"));
		         plot2.setBackgroundImage(myImage); 
			  } catch(IIOException eIIO) {
				  System.out.println(eIIO);
				  System.out.println("ERROR: Reading maps failed.");
			  }
		  } else if(TARGET==3){
			  try {
		         BufferedImage myImage = ImageIO.read(new File(".\\MAPS\\Venus_MAP.jpg"));
		         plot2.setBackgroundImage(myImage); 
		  } catch(IIOException eIIO) {
			  System.out.println(eIIO);
			  System.out.println("ERROR: Reading maps failed.");
		  }
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
        resultX40.addSeries(xyseries10); 
                  } catch(NullPointerException eNPI) { System.out.print(eNPI); }
        return resultX40;
                 
       }
	
	
	public static DefaultTableXYDataset AddDataset_X43(double RM) throws IOException , FileNotFoundException, ArrayIndexOutOfBoundsException{
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
		           String[] sequ_tokens  = SEQUENCE_DATA.get(active_sequence).split(" ");
		           int active_sequ_type  = Integer.parseInt(sequ_tokens[1]);
		           double ctrl_vinit 	 = Double.parseDouble(sequ_tokens[3]);
		           double ctrl_hinit 	 = Double.parseDouble(sequ_tokens[4]);
		           double ctrl_vel 	     = Double.parseDouble(sequ_tokens[5]);
		           double ctrl_alt 		 = Double.parseDouble(sequ_tokens[6]);
		           int ctrl_curve        = Integer.parseInt(sequ_tokens[7]);
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
		    		    	 //System.out.println(xx+ " | "+ y);
		    		    	 try { xyseries10.add(xx  , y); } catch(org.jfree.data.general.SeriesException eSE) {System.out.println(eSE);}
		    		    } else if (ctrl_curve==3) {
		    		    	 xx =   LandingCurve.Parabolic2Hover(ctrl_vinit, ctrl_hinit, ctrl_vel, y);
		    		    	 try { xyseries10.add(xx  , y); } catch(org.jfree.data.general.SeriesException eSE) {System.out.println(eSE);}
		    		    } 
		           } else {
		        	   xyseries10.add(x  , 0);  
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
		    resultX43.addSeries(xyseries11); 
		    resultX43.addSeries(xyseries10);
	              } catch (NullPointerException | IllegalArgumentException eNPE) { System.out.println(eNPE);}
	    return resultX43;
	   }
	
	public static double[][] FIND_ctrl_init_cond() throws IOException{
	   	   List<SequenceElement> SEQUENCE_DATA = new ArrayList<SequenceElement>(); 
	   	    SEQUENCE_DATA = SIM.READ_SEQUENCE();
	   	    double[][] INIT_CONDITIONS = new double[4][SEQUENCE_DATA.size()];
	   	    for (int i=0;i<SEQUENCE_DATA.size();i++) {
	   	    	
	   	    }
	   	    
	   	    return INIT_CONDITIONS;
	}
	public static void CreateChart_X43(double RM) throws IOException {
		//result1.removeAllSeries();
		//try {
		//resultX43 = AddDataset_X43(RM);
		//} catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF2) {
		//	System.out.println(eFNF2);
		//}
	    //-----------------------------------------------------------------------------------
		//chartX43 = ChartFactory.createScatterPlot("", "Velocity [m/s]", "Altitude [m] ", resultX43, PlotOrientation.VERTICAL, true, false, false); 
	    chartX43 = ChartFactory.createStackedXYAreaChart("", "Velocity [m/s]", "Altitude [m] ", resultX43);//("", "Velocity [m/s]", "Altitude [m] ", resultX43, PlotOrientation.VERTICAL, true, false, false); 
		XYPlot plot = (XYPlot)chartX43.getXYPlot(); 
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    plot.setRenderer(0, renderer); 
	    renderer.setSeriesPaint( 0 , Color.BLACK );	
		chartX43.setBackgroundPaint(Color.white);
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
	
		CPX43 = new ChartPanel(chartX43);
		CPX43.setMouseWheelEnabled(true);
		CPX43.setPreferredSize(new Dimension(900, page1_plot_y));
		CPX43.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
	            Rectangle2D dataArea = Plotting_3DOF.CPX43.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);
	            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
	            Plotting_3DOF.xCrosshair_X43.setValue(x);
	            Plotting_3DOF.yCrosshair_X43.setValue(y);
	        }
	});
	    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
	    xCrosshair_X43 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    xCrosshair_X43.setLabelVisible(true);
	    yCrosshair_X43 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    yCrosshair_X43.setLabelVisible(true);
	    crosshairOverlay.addDomainCrosshair(xCrosshair_X43);
	    crosshairOverlay.addRangeCrosshair(yCrosshair_X43);
	    CPX43.addOverlay(crosshairOverlay);
	   PlotPanel_X43.add(CPX43,BorderLayout.PAGE_START);
	    P1_Plotpanel.add(PlotPanel_X43,BorderLayout.PAGE_START);
	   //P1_Plotpanel.add(CPX43,BorderLayout.LINE_START);
		//jPanel4.validate();	
		chartX43_fd = false;
	}
	public static void CreateChart_X44() throws IOException {
		//result1.removeAllSeries();
		try {
		resultX44 = AddDataset_X44(4,3);
		} catch(FileNotFoundException | ArrayIndexOutOfBoundsException eFNF2) {
			
		}
	    //-----------------------------------------------------------------------------------
	    chartX44 = ChartFactory.createScatterPlot("", "", "", resultX44, PlotOrientation.VERTICAL, false, false, false); 
		XYPlot plot = (XYPlot)chartX44.getXYPlot(); 
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    plot.setRenderer(0, renderer); 
	    renderer.setSeriesPaint( 0 , Color.BLACK );	
		chartX44.setBackgroundPaint(Color.white);
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
	
		CPX44 = new ChartPanel(chartX44);
		CPX44.setMouseWheelEnabled(true);
		//CPX44.setPreferredSize(new Dimension(900, page1_plot_y));
		CPX44.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
	            Rectangle2D dataArea = Plotting_3DOF.CPX44.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);
	            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
	            Plotting_3DOF.xCrosshair_X44.setValue(x);
	            Plotting_3DOF.yCrosshair_X44.setValue(y);
	        }
	});
	    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
	    xCrosshair_X44 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    xCrosshair_X44.setLabelVisible(true);
	    yCrosshair_X44 = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    yCrosshair_X44.setLabelVisible(true);
	    crosshairOverlay.addDomainCrosshair(xCrosshair_X44);
	    crosshairOverlay.addRangeCrosshair(yCrosshair_X44);
	    CPX44.addOverlay(crosshairOverlay);
	   PlotPanel_X44.add(CPX44,BorderLayout.PAGE_START);
	    //P1_Plotpanel.add(PlotPanel_X44,BorderLayout.LINE_END);
	    P1_Plotpanel.add(CPX44,BorderLayout.CENTER);
		//jPanel4.validate();	
		chartX44_fd = false;
	}
	public static void Update_X44(){
	    	resultX44.removeAllSeries();
	    	try {
	    	resultX44 = AddDataset_X44(axis_chooser.getSelectedIndex(),axis_chooser2.getSelectedIndex());
	    	chartX44.getXYPlot().getDomainAxis().setAttributedLabel(String.valueOf(axis_chooser.getSelectedItem()));
	    	chartX44.getXYPlot().getRangeAxis().setAttributedLabel(String.valueOf(axis_chooser2.getSelectedItem()));
	    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
	    	}
	}
	
	public static XYSeriesCollection AddDataset_X44(int x, int y) throws IOException , IIOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
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
        resultX44.addSeries(xyseries10); 
                  } catch (NullPointerException eNPE) { System.out.println(eNPE);}
        return resultX44;
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
	
    private static void createAndShowGUI() throws IOException, URISyntaxException{

       // JFrame.setDefaultLookAndFeelDecorated(true);
        MAIN_frame = new JFrame("" + PROJECT_TITLE);
        //frame.setLayout(BB_BL);
        //Create and set up the content pane.
        Plotting_3DOF demo = new Plotting_3DOF();
        JPanel tp = demo.createContentPane();
        tp.setPreferredSize(new java.awt.Dimension(x_init, y_init));
        //frame.setContentPane(demo.createContentPane());
        
        MAIN_frame.add(tp, BorderLayout.CENTER);
        MAIN_frame.pack();
        MAIN_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(x_init, y_init);
        MAIN_frame.setVisible(true);
        MAIN_frame.setLocationRelativeTo(null);
         try {
         BufferedImage myImage = ImageIO.read(new File(ICON_File));
         MAIN_frame.setIconImage(myImage);  
         }catch(IIOException eIIO) {
        	 System.out.println(eIIO);
         }
    }
    
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }

		public static int RETURN_TARGET(){
			return TARGET; 
		}
}