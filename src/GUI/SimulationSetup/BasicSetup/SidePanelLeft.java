package GUI.SimulationSetup.BasicSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import utils.GuiReadInput;
import utils.WriteInput;

public class SidePanelLeft {
	
	private JPanel mainPanel;
	
	int SidePanel_Width=380;
	
	@SuppressWarnings("rawtypes")
	public static  JComboBox  Integrator_chooser;
	
    public  String[] Integrator_Options = { "Dormand Prince 853 Integrator", 
			  "Standard Runge Kutta Integrator" , 
			  "Gragg-Bulirsch-Stoer Integrator", 
			  "Adams-Bashforth Integrator"};
    
    public  String[] Vel_Frame_options = { "Cartesian Coordinate Frame (NED)",
	 "Spherical Coordinate Frame (NED)"};
	
    public  static JTextField INPUT_LONG_Is,INPUT_LAT_Is,INPUT_ALT_Is,INPUT_VEL_Is,INPUT_FPA_Is,INPUT_AZI_Is, INPUT_REFELEV;   // Input vector inertial Frame / spherical coordinates -> Is
    public static  JTextField INPUT_LONG_Rs;   // Input vector rotating Frame / spherical coordinates -> Rs

	public static JTextField INPUT_LAT_Rs;

	public static JTextField INPUT_ALT_Rs;

	public static JTextField INPUT_VEL_Rs;

	public static JTextField INPUT_FPA_Rs;

	public static JTextField INPUT_AZI_Rs;
    public static   JLabel LABEL_IntegratorSetting_01;

	public static JLabel LABEL_IntegratorSetting_02;

	public static JLabel LABEL_IntegratorSetting_03;

	public static JLabel LABEL_IntegratorSetting_04;

	public static JLabel LABEL_IntegratorSetting_05; 
    public static  JTextField INPUT_IntegratorSetting_01;

	public static JTextField INPUT_IntegratorSetting_02;

	public static JTextField INPUT_IntegratorSetting_03;

	public static JTextField INPUT_IntegratorSetting_04;

	public static JTextField INPUT_IntegratorSetting_05;
	
    public static  JTextField INPUT_AngularRate_X;

	public static JTextField INPUT_AngularRate_Y;

	public static JTextField INPUT_AngularRate_Z;
	
	public static TimePanel timePanel;
    
    private  int vel_frame_hist = 1; 
    
    private double PI = 3.141592653589793238462643383279502884197169399375105820974944592307816406;
	 double deg2rad = PI/180.0; 		//Convert degrees to radians
	 double rad2deg = 180/PI;
	
    static Font HeadlineFont          = new Font("Georgia", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    
    static DecimalFormat df_VelVector = new DecimalFormat("#.00000000");
    
    Color backgroundColor;
    Color labelColor;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SidePanelLeft() {
		//---------------------------------------------------------------------------------------------
	      //                       Initial State Definition Block
	      //---------------------------------------------------------------------------------------------
        int verticalSizer = 10 ; 
        int INPUT_width = 110;
        
        	backgroundColor = BlueBookVisual.getBackgroundColor();
        	labelColor = BlueBookVisual.getLabelColor();
        		
	      mainPanel = new JPanel();
	      mainPanel.setLayout(null);
	      mainPanel.setPreferredSize(new Dimension(SidePanel_Width, 900));
	      mainPanel.setLocation(0,0);
	      mainPanel.setBackground(backgroundColor);
	      mainPanel.setForeground(labelColor);
	      
	      JLabel LABEL_InitState = new JLabel("Initial State");
	      LABEL_InitState.setLocation(5, verticalSizer + 25 * 0  );
	      LABEL_InitState.setSize(350, 20);
	      LABEL_InitState.setBackground(backgroundColor);
	      LABEL_InitState.setForeground(labelColor);
	      LABEL_InitState.setFont(HeadlineFont);
	      LABEL_InitState.setHorizontalAlignment(0);
	      mainPanel.add(LABEL_InitState);
	      
	      JLabel LABEL_InertialFrame = new JLabel("Inertial Frame [ECI]");
	      LABEL_InertialFrame.setLocation(2, verticalSizer + 25 * 1 );
	      LABEL_InertialFrame.setSize(INPUT_width, 20);
	      LABEL_InertialFrame.setHorizontalAlignment(JLabel.CENTER);
	      LABEL_InertialFrame.setBackground(backgroundColor);
	      LABEL_InertialFrame.setForeground(labelColor);
	      LABEL_InertialFrame.setFont(BlueBookVisual.getSmall_font());
	      mainPanel.add(LABEL_InertialFrame);
	      JLabel LABEL_RotatingFrame = new JLabel("Rotating Frame [ECEF]");
	      LABEL_RotatingFrame.setLocation(2+INPUT_width+5, verticalSizer + 25 * 1  );
	      LABEL_RotatingFrame.setSize(INPUT_width+40, 20);
	      LABEL_RotatingFrame.setHorizontalAlignment(JLabel.CENTER);
	      LABEL_RotatingFrame.setBackground(backgroundColor);
	      LABEL_RotatingFrame.setForeground(labelColor);
	      LABEL_RotatingFrame.setFont(BlueBookVisual.getSmall_font());
	      mainPanel.add(LABEL_RotatingFrame);
	      
	      JLabel LABEL_longitude = new JLabel("Longitude [deg]");
	      LABEL_longitude.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 2  );
	      LABEL_longitude.setSize(250, 20);
	      LABEL_longitude.setBackground(backgroundColor);
	      LABEL_longitude.setForeground(labelColor);
	      LABEL_longitude.setFont(BlueBookVisual.getSmall_font());
	      mainPanel.add(LABEL_longitude);
	      JLabel LABEL_latitude = new JLabel("Latitude [deg]");
	      LABEL_latitude.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 3 );
	      LABEL_latitude.setSize(250, 20);
	      LABEL_latitude.setBackground(backgroundColor);
	      LABEL_latitude.setForeground(labelColor);
	      LABEL_latitude.setFont(BlueBookVisual.getSmall_font());
	      mainPanel.add(LABEL_latitude);
	      JLabel LABEL_altitude = new JLabel("Altitude [m]");
	      LABEL_altitude.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 4);
	      LABEL_altitude.setSize(250, 20);
	      LABEL_altitude.setBackground(backgroundColor);
	      LABEL_altitude.setForeground(labelColor);
	      LABEL_altitude.setFont(BlueBookVisual.getSmall_font());
	      mainPanel.add(LABEL_altitude);
	      
	      JLabel LABEL_referenceelevation = new JLabel("Ref. Elevation [m]");
	      LABEL_referenceelevation.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 5 );
	      LABEL_referenceelevation.setSize(250, 20);
	      LABEL_referenceelevation.setBackground(backgroundColor);
	      LABEL_referenceelevation.setForeground(labelColor);
	      LABEL_referenceelevation.setFont(BlueBookVisual.getSmall_font());
	      mainPanel.add(LABEL_referenceelevation);
	      
	      JLabel LABEL_velocity = new JLabel("Velocity [m/s]");
	      LABEL_velocity.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 8 );
	      LABEL_velocity.setSize(250, 20);
	      LABEL_velocity.setBackground(backgroundColor);
	      LABEL_velocity.setForeground(labelColor);
	      LABEL_velocity.setFont(BlueBookVisual.getSmall_font());;
	      mainPanel.add(LABEL_velocity);
	      JLabel LABEL_fpa = new JLabel("Flight Path angle [deg]");
	      LABEL_fpa.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 9);
	      LABEL_fpa.setSize(250, 20);
	      LABEL_fpa.setBackground(backgroundColor);
	      LABEL_fpa.setForeground(labelColor);
	      LABEL_fpa.setFont(BlueBookVisual.getSmall_font());
	      mainPanel.add(LABEL_fpa);
	      JLabel LABEL_azimuth = new JLabel("Azimuth [deg]");
	      LABEL_azimuth.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 10 );
	      LABEL_azimuth.setSize(250, 20);
	      LABEL_azimuth.setBackground(backgroundColor);
	      LABEL_azimuth.setFont(BlueBookVisual.getSmall_font());
	      LABEL_azimuth.setForeground(labelColor);
	      mainPanel.add(LABEL_azimuth);

	      INPUT_LONG_Is = new JTextField(10);
	      INPUT_LONG_Is.setLocation(2, verticalSizer + 25 * 2 );
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
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_LONG_Is);
	      INPUT_LAT_Is = new JTextField(10);
	      INPUT_LAT_Is.setLocation(2, verticalSizer + 25 * 3 );
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
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_LAT_Is);
	      INPUT_ALT_Is = new JTextField(10);
	      INPUT_ALT_Is.setLocation(2, verticalSizer + 25 * 4 );
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
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_ALT_Is);   
	      INPUT_REFELEV = new JTextField(10);
	      INPUT_REFELEV.setLocation(2+INPUT_width+5, verticalSizer + 25 * 5 );
	      INPUT_REFELEV.setSize(INPUT_width, 20);
	      INPUT_REFELEV.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_REFELEV.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
			
			}
	    	  
	      });
	      mainPanel.add(INPUT_REFELEV); 
	      INPUT_VEL_Is = new JTextField(10);
	      INPUT_VEL_Is.setLocation(2, verticalSizer + 25 * 8 );
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
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_VEL_Is);
	      INPUT_FPA_Is = new JTextField(10);
	      INPUT_FPA_Is.setLocation(2, verticalSizer + 25 * 9 );
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
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_FPA_Is);
	      INPUT_AZI_Is = new JTextField(10);
	      INPUT_AZI_Is.setLocation(2, verticalSizer + 25 * 10 );
	      INPUT_AZI_Is.setSize(INPUT_width, 20);
	      INPUT_AZI_Is.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_AZI_Is.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				Inertial2Rotating();
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_AZI_Is);
	      //---------------------------------------------------------
	      INPUT_LONG_Rs = new JTextField(10);
	      INPUT_LONG_Rs.setLocation(2+INPUT_width+5, verticalSizer + 25 * 2 );
	      INPUT_LONG_Rs.setSize(INPUT_width, 20);
	      INPUT_LONG_Rs.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_LONG_Rs.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				Rotating2Inertial();
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_LONG_Rs);
	      INPUT_LAT_Rs = new JTextField(10);
	      INPUT_LAT_Rs.setLocation(2+INPUT_width+5, verticalSizer + 25 * 3 );
	      INPUT_LAT_Rs.setSize(INPUT_width, 20);
	      INPUT_LAT_Rs.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_LAT_Rs.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				Rotating2Inertial();
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_LAT_Rs);
	      INPUT_ALT_Rs = new JTextField(10);
	      INPUT_ALT_Rs.setLocation(2+INPUT_width+5, verticalSizer + 25 * 4 );
	      INPUT_ALT_Rs.setSize(INPUT_width, 20);
	      INPUT_ALT_Rs.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_ALT_Rs.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				Rotating2Inertial();
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_ALT_Rs);    
	      
	      
		JComboBox VelocityFrame_chooser = new JComboBox(Vel_Frame_options);
		//  VelocityFrame_chooser.setBackground(backgroundColor);
		  VelocityFrame_chooser.setLocation(2+INPUT_width+5, verticalSizer + 25 * 7);
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
		  mainPanel.add(VelocityFrame_chooser);
	      
	      
	      INPUT_VEL_Rs = new JTextField(10);
	      INPUT_VEL_Rs.setLocation(2+INPUT_width+5, verticalSizer + 25 * 8 );
	      INPUT_VEL_Rs.setSize(INPUT_width, 20);
	      INPUT_VEL_Rs.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_VEL_Rs.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				Rotating2Inertial();
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_VEL_Rs);
	      INPUT_FPA_Rs = new JTextField(10);
	      INPUT_FPA_Rs.setLocation(2+INPUT_width+5, verticalSizer + 25 * 9 );
	      INPUT_FPA_Rs.setSize(INPUT_width, 20);
	      INPUT_FPA_Rs.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_FPA_Rs.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				Rotating2Inertial();
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_FPA_Rs);
	      INPUT_AZI_Rs = new JTextField(10);
	      INPUT_AZI_Rs.setLocation(2+INPUT_width+5, verticalSizer + 25 * 10 );
	      INPUT_AZI_Rs.setSize(INPUT_width, 20);
	      INPUT_AZI_Rs.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_AZI_Rs.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				Rotating2Inertial();
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_AZI_Rs);
	      
	      JLabel LABEL_AngularRate = new JLabel("Initial Angular Rate: ");
	      LABEL_AngularRate.setLocation(2, verticalSizer + 25 * 12);
	      LABEL_AngularRate.setSize(150, 20);
	      LABEL_AngularRate.setBackground(backgroundColor);
	      LABEL_AngularRate.setForeground(labelColor);
	      mainPanel.add(LABEL_AngularRate);
	      
	      JLabel LABEL_AngularRateX = new JLabel("Body X [deg/s]");
	      LABEL_AngularRateX.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 13 );
	      LABEL_AngularRateX.setSize(250, 20);
	      LABEL_AngularRateX.setBackground(backgroundColor);
	      LABEL_AngularRateX.setForeground(labelColor);
	      LABEL_AngularRateX.setFont(BlueBookVisual.getSmall_font());;
	      mainPanel.add(LABEL_AngularRateX);
	      JLabel LABEL_AngularRateY = new JLabel("Body Y [deg/s]");
	      LABEL_AngularRateY.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 14);
	      LABEL_AngularRateY.setSize(250, 20);
	      LABEL_AngularRateY.setBackground(backgroundColor);
	      LABEL_AngularRateY.setForeground(labelColor);
	      LABEL_AngularRateY.setFont(BlueBookVisual.getSmall_font());
	      mainPanel.add(LABEL_AngularRateY);
	      JLabel LABEL_AngularRateZ = new JLabel("Body Z [deg/s]");
	      LABEL_AngularRateZ.setLocation(2+(INPUT_width+5)*2, verticalSizer + 25 * 15 );
	      LABEL_AngularRateZ.setSize(250, 20);
	      LABEL_AngularRateZ.setBackground(backgroundColor);
	      LABEL_AngularRateZ.setFont(BlueBookVisual.getSmall_font());
	      LABEL_AngularRateZ.setForeground(labelColor);
	      mainPanel.add(LABEL_AngularRateZ);
	      
	      INPUT_AngularRate_X = new JTextField(10);
	      INPUT_AngularRate_X.setLocation(2+INPUT_width+5, verticalSizer + 25 * 13 );
	      INPUT_AngularRate_X.setSize(INPUT_width, 20);
	      INPUT_AngularRate_X.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_AngularRate_X.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_AngularRate_X);
	      
	      INPUT_AngularRate_Y = new JTextField(10);
	      INPUT_AngularRate_Y.setLocation(2+INPUT_width+5, verticalSizer + 25 * 14 );
	      INPUT_AngularRate_Y.setSize(INPUT_width, 20);
	      INPUT_AngularRate_Y.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_AngularRate_Y.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_AngularRate_Y);
	      
	      INPUT_AngularRate_Z = new JTextField(10);
	      INPUT_AngularRate_Z.setLocation(2+INPUT_width+5, verticalSizer + 25 * 15 );
	      INPUT_AngularRate_Z.setSize(INPUT_width, 20);
	      INPUT_AngularRate_Z.setHorizontalAlignment(JTextField.RIGHT);
	      INPUT_AngularRate_Z.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      mainPanel.add(INPUT_AngularRate_Z);
	      
	      //---------------------------------------------------------------------------------------------
	      //                       			  Time Input 
	      //--------------------------------------------------------------------------------------------- 
	      
	      JLabel LABEL_Time = new JLabel(" - Initial Time - UTC standard- ");
	      LABEL_Time.setLocation(2, verticalSizer + 25 * 17);
	      LABEL_Time.setSize(300, 20);
	      LABEL_Time.setHorizontalAlignment(SwingConstants.RIGHT);
	      LABEL_Time.setBackground(backgroundColor);
	      LABEL_Time.setForeground(labelColor);
	      mainPanel.add(LABEL_Time);
	      
	      timePanel = new TimePanel(backgroundColor, labelColor);
	      timePanel.getTimePanel().setLocation(2, verticalSizer + 25 * 19);   
	      timePanel.getTimePanel().setSize(SidePanel_Width, 100);
	      mainPanel.add(timePanel.getTimePanel());
	      	      	      
	      //---------------------------------------------------------------------------------------------
	      //                         Integrator Definition Block
	      //---------------------------------------------------------------------------------------------       
	      JPanel IntegratorInputPanel = new JPanel();
	      IntegratorInputPanel.setLocation(2, verticalSizer + 25 * 24 );
	      IntegratorInputPanel.setSize(SidePanel_Width, 825);
	      IntegratorInputPanel.setBackground(backgroundColor);
	      IntegratorInputPanel.setForeground(Color.white);
	      IntegratorInputPanel.setLayout(null);
	      mainPanel.add(IntegratorInputPanel);
	      
	      JSeparator Separator_Page2_1 = new JSeparator();
	      Separator_Page2_1.setLocation(0, verticalSizer + 3 );
	      Separator_Page2_1.setSize(SidePanel_Width, 1);
	      Separator_Page2_1.setBackground(Color.black);
	      Separator_Page2_1.setForeground(labelColor);
	      IntegratorInputPanel.add(Separator_Page2_1);
	      
	      JLabel LABEL_IntegSetting = new JLabel("Integrator Settings");
	      LABEL_IntegSetting.setLocation(0, verticalSizer + 25 * 0 );
	      LABEL_IntegSetting.setSize(400, 20);
	      LABEL_IntegSetting.setBackground(backgroundColor);
	      LABEL_IntegSetting.setForeground(labelColor);
	      LABEL_IntegSetting.setFont(HeadlineFont);
	      LABEL_IntegSetting.setHorizontalAlignment(0);
	      IntegratorInputPanel.add(LABEL_IntegSetting);

		  
		  Integrator_chooser = new JComboBox(Integrator_Options);
		  //Integrator_chooser.setBackground(backgroundColor);
		  Integrator_chooser.setLocation(2, verticalSizer + 25 * 1 );
		  Integrator_chooser.setSize(380,25);
		  Integrator_chooser.setRenderer(new CustomRenderer());
		  Integrator_chooser.setSelectedIndex(3);
		  Integrator_chooser.addActionListener(new ActionListener() { 
	    	  public void actionPerformed(ActionEvent e) {
	    		  Update_IntegratorSettings();
	    		  GuiReadInput.readINP();;
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
				 WriteInput.writeInputFile(FilePaths.inputFile);
			}
			  
		  });
		  IntegratorInputPanel.add(Integrator_chooser);
		  //------------------------------------------------------------------------------------------------------------------
	      LABEL_IntegratorSetting_01 = new JLabel("");
	      LABEL_IntegratorSetting_01.setLocation(65, verticalSizer + 25 * 3 );
	      LABEL_IntegratorSetting_01.setSize(250, 20);
	      LABEL_IntegratorSetting_01.setBackground(backgroundColor);
	      LABEL_IntegratorSetting_01.setForeground(labelColor);
	      IntegratorInputPanel.add(LABEL_IntegratorSetting_01);
	      INPUT_IntegratorSetting_01 = new JTextField(10){
			    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override public void setBorder(Border border) {
			        // No!
			    }
			};
	      INPUT_IntegratorSetting_01.setLocation(2, verticalSizer + 25 * 3 );
	      INPUT_IntegratorSetting_01.setSize(60, 20);
	      INPUT_IntegratorSetting_01.setBackground(backgroundColor);
	      INPUT_IntegratorSetting_01.setForeground(labelColor);
	      INPUT_IntegratorSetting_01.setHorizontalAlignment(JTextField.LEFT);
	      INPUT_IntegratorSetting_01.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      IntegratorInputPanel.add(INPUT_IntegratorSetting_01);
	      LABEL_IntegratorSetting_02 = new JLabel("");
	      LABEL_IntegratorSetting_02.setLocation(65, verticalSizer + 25 * 4 );
	      LABEL_IntegratorSetting_02.setSize(250, 20);
	      LABEL_IntegratorSetting_02.setBackground(backgroundColor);
	      LABEL_IntegratorSetting_02.setForeground(labelColor);
	      IntegratorInputPanel.add(LABEL_IntegratorSetting_02);
	      INPUT_IntegratorSetting_02 = new JTextField(10){
			    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override public void setBorder(Border border) {
			        // No!
			    }
			};
	      INPUT_IntegratorSetting_02.setLocation(2, verticalSizer + 25 * 4 );
	      INPUT_IntegratorSetting_02.setSize(60, 20);
	      INPUT_IntegratorSetting_02.setBackground(backgroundColor);
	      INPUT_IntegratorSetting_02.setForeground(labelColor);
	      INPUT_IntegratorSetting_02.setHorizontalAlignment(JTextField.LEFT);
	      INPUT_IntegratorSetting_02.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      IntegratorInputPanel.add(INPUT_IntegratorSetting_02);
	      LABEL_IntegratorSetting_03 = new JLabel("");
	      LABEL_IntegratorSetting_03.setLocation(65, verticalSizer + 25 * 5 );
	      LABEL_IntegratorSetting_03.setSize(250, 20);
	      LABEL_IntegratorSetting_03.setBackground(backgroundColor);
	      LABEL_IntegratorSetting_03.setForeground(labelColor);
	      IntegratorInputPanel.add(LABEL_IntegratorSetting_03);
	      INPUT_IntegratorSetting_03 = new JTextField(10){
			    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override public void setBorder(Border border) {
			        // No!
			    }
			};
	      INPUT_IntegratorSetting_03.setLocation(2, verticalSizer + 25 * 5 );
	      INPUT_IntegratorSetting_03.setSize(60, 20);
	      INPUT_IntegratorSetting_03.setBackground(backgroundColor);
	      INPUT_IntegratorSetting_03.setForeground(labelColor);
	      INPUT_IntegratorSetting_03.setHorizontalAlignment(JTextField.LEFT);
	      INPUT_IntegratorSetting_03.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      IntegratorInputPanel.add(INPUT_IntegratorSetting_03);
	      LABEL_IntegratorSetting_04 = new JLabel("");
	      LABEL_IntegratorSetting_04.setLocation(65, verticalSizer + 25 * 6 );
	      LABEL_IntegratorSetting_04.setSize(250, 20);
	      LABEL_IntegratorSetting_04.setBackground(backgroundColor);
	      LABEL_IntegratorSetting_04.setForeground(labelColor);
	      IntegratorInputPanel.add(LABEL_IntegratorSetting_04);
	      INPUT_IntegratorSetting_04 = new JTextField(10){
			    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override public void setBorder(Border border) {
			        // No!
			    }
			};
	      INPUT_IntegratorSetting_04.setLocation(2, verticalSizer + 25 * 6 );
	      INPUT_IntegratorSetting_04.setSize(60, 20);
	      INPUT_IntegratorSetting_04.setBackground(backgroundColor);
	      INPUT_IntegratorSetting_04.setForeground(labelColor);
	      INPUT_IntegratorSetting_04.setHorizontalAlignment(JTextField.LEFT);
	      INPUT_IntegratorSetting_04.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      IntegratorInputPanel.add(INPUT_IntegratorSetting_04);
	      LABEL_IntegratorSetting_05 = new JLabel("");
	      LABEL_IntegratorSetting_05.setLocation(65, verticalSizer + 25 * 7 );
	      LABEL_IntegratorSetting_05.setSize(250, 20);
	      LABEL_IntegratorSetting_05.setBackground(backgroundColor);
	      LABEL_IntegratorSetting_05.setForeground(labelColor);
	      IntegratorInputPanel.add(LABEL_IntegratorSetting_05);
	      INPUT_IntegratorSetting_05 = new JTextField(10){
			    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override public void setBorder(Border border) {
			        // No!
			    }
			};
	      INPUT_IntegratorSetting_05.setLocation(2, verticalSizer + 25 * 7 );
	      INPUT_IntegratorSetting_05.setSize(60, 20);
	      INPUT_IntegratorSetting_05.setBackground(backgroundColor);
	      INPUT_IntegratorSetting_05.setForeground(labelColor);
	      INPUT_IntegratorSetting_05.setHorizontalAlignment(JTextField.LEFT);
	      INPUT_IntegratorSetting_05.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	      
	      IntegratorInputPanel.add(INPUT_IntegratorSetting_05); 
	      
	      Rotating2Inertial();
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
	
    public static  void Update_IntegratorSettings() {
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

    public  void Inertial2Rotating() {
    	try {
    	double vel_inert = Double.parseDouble(INPUT_VEL_Is.getText());
    	double fpa_inert = Double.parseDouble(INPUT_FPA_Is.getText())*deg2rad;
    	double azi_inert = Double.parseDouble(INPUT_AZI_Is.getText())*deg2rad;
    	double lat_rotating = Double.parseDouble(INPUT_LAT_Rs.getText())*deg2rad;
    	double rm = BlueBookVisual.DATA_MAIN[BlueBookVisual.indx_target][0];
    	double omega = BlueBookVisual.DATA_MAIN[BlueBookVisual.indx_target][2];
    	double radius = Double.parseDouble(INPUT_ALT_Rs.getText())+rm;
    	double azimuth_rotFrame   = Math.atan(Math.tan(azi_inert)-omega*radius*Math.cos(lat_rotating)/(vel_inert*Math.cos(fpa_inert)*Math.cos(azi_inert)));
    	double fpa_rotFrame 	  = Math.atan(Math.tan(fpa_inert)*Math.cos(azimuth_rotFrame)/Math.cos(azi_inert));
    	double vel_rotFrame	      = vel_inert*Math.sin(fpa_inert)/Math.sin(fpa_rotFrame);
    	INPUT_AZI_Rs.setText(""+df_VelVector.format(azimuth_rotFrame*rad2deg));
    	INPUT_FPA_Rs.setText(""+df_VelVector.format(fpa_rotFrame*rad2deg));
    	INPUT_VEL_Rs.setText(""+df_VelVector.format(vel_rotFrame));
    	} catch(java.lang.NumberFormatException enfe) {System.out.println(enfe);}
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
	
    public void Rotating2Inertial() {
    	try {
    	double vel_rotating = Double.parseDouble(INPUT_VEL_Rs.getText());
    	double fpa_rotating = Double.parseDouble(INPUT_FPA_Rs.getText())*deg2rad;
    	double azi_rotating = Double.parseDouble(INPUT_AZI_Rs.getText())*deg2rad;
    	double lat_rotating = Double.parseDouble(INPUT_LAT_Rs.getText())*deg2rad;
    	double rm = BlueBookVisual.DATA_MAIN[BlueBookVisual.indx_target][0];
    	double omega = BlueBookVisual.DATA_MAIN[BlueBookVisual.indx_target][2];
    	double radius = Double.parseDouble(INPUT_ALT_Rs.getText())+rm;
    	double azimuth_inertFrame = Math.atan((vel_rotating*Math.cos(fpa_rotating)*Math.sin(azi_rotating)+omega*radius+Math.cos(lat_rotating))/(vel_rotating*Math.cos(fpa_rotating)*Math.cos(azi_rotating)));
    	double fpa_inertFrame = Math.atan(Math.tan(fpa_rotating)*Math.cos(azimuth_inertFrame)/Math.cos(azi_rotating));
    	double vel_inertFrame = vel_rotating * Math.sin(fpa_rotating)/Math.sin(fpa_inertFrame);
    	INPUT_AZI_Is.setText(""+df_VelVector.format(azimuth_inertFrame*rad2deg));
    	INPUT_FPA_Is.setText(""+df_VelVector.format(fpa_inertFrame*rad2deg));
    	INPUT_VEL_Is.setText(""+df_VelVector.format(vel_inertFrame));
    	} catch(java.lang.NumberFormatException enfe) {System.out.println(enfe);}
    }
    
	public static void main(String[] args) {
		// Unit Tester :
		JFrame frame = new JFrame("Component Tester");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());
		
		SidePanelLeft panel = new SidePanelLeft();
		frame.add(panel.getMainPanel(), BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
	}
	
	public static class CustomRenderer extends DefaultListCellRenderer {

		
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value,
		        int index, boolean isSelected, boolean cellHasFocus) {
		    super.getListCellRendererComponent(list, value, index, isSelected,
		            cellHasFocus);
		    setBackground(BlueBookVisual.getBackgroundColor());
		    setForeground(BlueBookVisual.getLabelColor());     
		    return this;
		}  
	}
	
}
