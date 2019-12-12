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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.BlueBookVisual;
import GUI.GuiComponents;
import GUI.BlueBookVisual.CustomRenderer;
import GUI.FxElements.SpaceShipView3D;
import GUI.FxElements.TargetWindow;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import utils.Mathbox;

public class CenterPanelRight {
	    //-------------------------------------------------------------------------------------------------------------
		private JPanel mainPanel;
		
	    private Color backgroundColor;
	    private Color labelColor;
	    
	    private int verticalSizer = 10 ; 
	    private int INPUT_width = 110;
	    
	    	private double PI = 3.141592653589793238462643383279502884197169399375105820974944592307816406;
		double deg2rad = PI/180.0; 		//Convert degrees to radians
		double rad2deg = 180/PI;
    
	  	int box_size_InitialAttitude_x = 130;
	  	int box_size_InitialAttitude_y = 25;
		int box_size_x = 60;
		int box_size_y = 25;
		int gap_size_x =  4;
		int gap_size_y =  15;
		//-------------------------------------------------------------------------------------------------------------
		// Formatting valules (Fonts, Borders, decimalLayouts etc):
	    static DecimalFormat decQuarternion =  new DecimalFormat("#.########");
	    static DecimalFormat decf 		  = new DecimalFormat("#.#");
		
		 static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
		
	    private static Border Earth_border = BorderFactory.createLineBorder(Color.BLUE, 1);
	    private static Border Moon_border 	= BorderFactory.createLineBorder(Color.GRAY, 1);
	    private static Border Mars_border 	= BorderFactory.createLineBorder(Color.ORANGE, 1);
	    private static Border Venus_border = BorderFactory.createLineBorder(Color.GREEN, 1);
	    
	    public static String[] Target_Options = { "Earth", 
			      "Moon" ,	
			      "Mars", 	
				  "Venus"};
	    //-------------------------------------------------------------------------------------------------------------
	    // Global GUI components:
	    private static JTextField INPUT_Euler1, INPUT_Euler2, INPUT_Euler3;
	    public static JTextField INPUT_Quarternion1, INPUT_Quarternion2, INPUT_Quarternion3, INPUT_Quarternion4;
	    private static JPanel targetWindow;
        final static JFXPanel targetWindowFxPanel = new JFXPanel();
	    private static JRadioButton SELECT_VelocityCartesian;   
	    private static JRadioButton SELECT_VelocitySpherical;
		public static JSlider sliderEuler1, sliderEuler2, sliderEuler3;
		private 	 static   JRadioButton SELECT_3DOF;    
		private  static	JRadioButton SELECT_6DOF;
		private static JTextField INPUT_ControllerFrequency;
		private static JTextField INPUT_GlobalTime;
		@SuppressWarnings("rawtypes")
		public static JComboBox Target_chooser ;
		//-------------------------------------------------------------------------------------------------------------
        // Content Lists 
        public static List<Object> targetWindowContent = new ArrayList<Object>();
        //-------------------------------------------------------------------------------------------------------------
	    // Class Values:
	    private static int targetIndx=0;
	    private static int VelocityCoordinateSystem;
	    private static int DOF_System;
	    private static double globalTime;
	    private static double controllerFrequency;
	    private static double globalFrequency;
	    //-------------------------------------------------------------------------------------------------------------
	    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CenterPanelRight() {
		
    	backgroundColor = BlueBookVisual.getBackgroundColor();
    	labelColor = BlueBookVisual.getLabelColor();


	     mainPanel = new JPanel();
	    mainPanel.setLayout(new BorderLayout());
	    mainPanel.setPreferredSize(new Dimension(960, 900));
	    mainPanel.setBackground(backgroundColor);
	    mainPanel.setForeground(labelColor);
	    mainPanel.setLayout(null);
	    
	    targetWindow = new JPanel();
	    targetWindow.setSize(300, 300);
	    targetWindow.setLocation(540, verticalSizer + 25 * 0   );
	    targetWindow.setBackground(Color.RED);
	    targetWindow.setForeground(labelColor);
	    targetWindow.setBorder(Mars_border);
	    targetWindow.setLayout(new BorderLayout());
	    mainPanel.add(targetWindow);
	    
	    JLabel LABEL_TARGETBODY = new JLabel("Target Body");
	    LABEL_TARGETBODY.setLocation(163, verticalSizer + 25 * 2   );
	    LABEL_TARGETBODY.setSize(150, 20);
	    LABEL_TARGETBODY.setBackground(backgroundColor);
	    LABEL_TARGETBODY.setForeground(labelColor);
	    mainPanel.add(LABEL_TARGETBODY);
	    
		 Target_chooser = new JComboBox(Target_Options);
		  //Target_chooser.setBackground(backgroundColor);
		  Target_chooser.setLocation(2, verticalSizer + 25 * 2 );
		  Target_chooser.setSize(150,25);
		  Target_chooser.setRenderer(new CustomRenderer());
		  Target_chooser.setSelectedIndex(3);
		  Target_chooser.addActionListener(new ActionListener() { 
	    	  public void actionPerformed(ActionEvent e) {
	  			targetIndx= Target_chooser.getSelectedIndex();
				 refreshTargetWindow();
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
				targetIndx= Target_chooser.getSelectedIndex();
				 BlueBookVisual.WRITE_INIT();
				 refreshTargetWindow();
			}
			  
		  });
		  mainPanel.add(Target_chooser);
	    
	    
	    JLabel LABEL_VCoordinateSystem = new JLabel("Select Coordinate System to solve the Velocity Vector");
	    LABEL_VCoordinateSystem.setLocation(5, verticalSizer + 25 * 3   );
	    LABEL_VCoordinateSystem.setSize(350, 20);
	    LABEL_VCoordinateSystem.setBackground(backgroundColor);
	    LABEL_VCoordinateSystem.setForeground(labelColor);
	    mainPanel.add(LABEL_VCoordinateSystem);
	    
	     SELECT_VelocityCartesian =new JRadioButton("Cartesian Velocity Coordinates");    
	     SELECT_VelocitySpherical =new JRadioButton("Spherical Velocity Coordinates");      
	    SELECT_VelocitySpherical.setLocation(5, verticalSizer + 25 * 4 );
	    SELECT_VelocitySpherical.setSize(220,20);
	    SELECT_VelocitySpherical.setBackground(backgroundColor);
	    SELECT_VelocitySpherical.setForeground(labelColor);
	    SELECT_VelocitySpherical.setFont(smallFont);
	    SELECT_VelocityCartesian.setLocation(5, verticalSizer + 25 * 5);
	    SELECT_VelocityCartesian.setSize(220,20);
	    SELECT_VelocityCartesian.setBackground(backgroundColor);
	    SELECT_VelocityCartesian.setForeground(labelColor);
	    SELECT_VelocityCartesian.setFont(smallFont);
	   ButtonGroup bg_velocity=new ButtonGroup();    
	   bg_velocity.add(SELECT_VelocitySpherical);
	   bg_velocity.add(SELECT_VelocityCartesian); 
	   mainPanel.add(SELECT_VelocitySpherical);
	   mainPanel.add(SELECT_VelocityCartesian);
	   SELECT_VelocitySpherical.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(SELECT_VelocitySpherical.isSelected()) {
					VelocityCoordinateSystem = 1;
				} else if (SELECT_VelocityCartesian.isSelected()) {
					VelocityCoordinateSystem = 2;
				}
				BlueBookVisual.WRITE_INIT();
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
				BlueBookVisual.WRITE_INIT();
			}
	  	 
	   });
	   
	   JLabel LABEL_SelectDoF = new JLabel("Select Degrees of Freedom");
	   LABEL_SelectDoF.setLocation(5, verticalSizer + 25 * 6   );
	   LABEL_SelectDoF.setSize(350, 20);
	   LABEL_SelectDoF.setBackground(backgroundColor);
	   LABEL_SelectDoF.setForeground(labelColor);
	   mainPanel.add(LABEL_SelectDoF);
	   
	    SELECT_3DOF =new JRadioButton("3DOF Model");    
	    SELECT_6DOF =new JRadioButton("6DOF Model");      
	   SELECT_3DOF.setLocation(5, verticalSizer + 25 * 7 );
	   SELECT_3DOF.setSize(220,20);
	   SELECT_3DOF.setBackground(backgroundColor);
	   SELECT_3DOF.setForeground(labelColor);
	   SELECT_3DOF.setFont(smallFont);
	   SELECT_6DOF.setLocation(5, verticalSizer + 25 * 8);
	   SELECT_6DOF.setSize(220,20);
	   SELECT_6DOF.setBackground(backgroundColor);
	   SELECT_6DOF.setForeground(labelColor);
	   SELECT_6DOF.setFont(smallFont);
	  ButtonGroup bg_dof=new ButtonGroup();    
	  bg_dof.add(SELECT_3DOF);
	  bg_dof.add(SELECT_6DOF); 
	  mainPanel.add(SELECT_3DOF);
	  mainPanel.add(SELECT_6DOF);
	  SELECT_3DOF.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(SELECT_3DOF.isSelected()) {
					DOF_System = 3;
				} else if (SELECT_6DOF.isSelected()) {
					DOF_System = 6;
				}
				BlueBookVisual.WRITE_INIT();
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
				BlueBookVisual.WRITE_INIT();
			}
	 	 
	  }); 
	  
	  JLabel LABEL_ControllerFrequency = new JLabel("Set Control Loop Frequency [Hz]");
	  LABEL_ControllerFrequency.setLocation(INPUT_width, verticalSizer + 25 * 10 );
	  LABEL_ControllerFrequency.setSize(300, 20);
	  LABEL_ControllerFrequency.setBackground(backgroundColor);
	  LABEL_ControllerFrequency.setForeground(labelColor);
	  mainPanel.add(LABEL_ControllerFrequency);
	 
	 
	  
	   INPUT_ControllerFrequency = new JTextField(10);
	  INPUT_ControllerFrequency.setLocation(2, verticalSizer + 25 * 10);
	  INPUT_ControllerFrequency.setSize(INPUT_width-20, 20);
	  //INPUT_M0.setBackground(backgroundColor);
	  //INPUT_M0.setForeground(labelColor);
	  INPUT_ControllerFrequency.setHorizontalAlignment(JTextField.RIGHT);
	  INPUT_ControllerFrequency.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			BlueBookVisual.WRITE_INIT();
		}
		  
	  });
	  mainPanel.add(INPUT_ControllerFrequency);
	  
	  JLabel LABEL_GlobalFrequency = new JLabel("Set Global Result Frequency [Hz]");
	  LABEL_GlobalFrequency.setLocation(INPUT_width, verticalSizer + 25 * 11 );
	  LABEL_GlobalFrequency.setSize(300, 20);
	  LABEL_GlobalFrequency.setBackground(backgroundColor);
	  LABEL_GlobalFrequency.setForeground(labelColor);
	  mainPanel.add(LABEL_GlobalFrequency);
	 
	 
	  
	  JTextField INPUT_GlobalFrequency = new JTextField(10);
	  INPUT_GlobalFrequency.setLocation(2, verticalSizer + 25 * 11);
	  INPUT_GlobalFrequency.setSize(INPUT_width-20, 20);
	  //INPUT_M0.setBackground(backgroundColor);
	  //INPUT_M0.setForeground(labelColor);
	  INPUT_GlobalFrequency.setEditable(false);
	  INPUT_GlobalFrequency.setHorizontalAlignment(JTextField.RIGHT);
	  INPUT_GlobalFrequency.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			//BlueBookVisual.WRITE_INIT();
		}
		  
	  });
	  mainPanel.add(INPUT_GlobalFrequency);
	  
	  JLabel LABEL_IntegTime = new JLabel("Set Maximum Simulation Time [s]");
	  LABEL_IntegTime.setLocation(INPUT_width, verticalSizer + 25 * 12 );
	  LABEL_IntegTime.setSize(300, 20);
	  LABEL_IntegTime.setBackground(backgroundColor);
	  LABEL_IntegTime.setForeground(labelColor);
	  mainPanel.add(LABEL_IntegTime);
	 
	 
	  
	   INPUT_GlobalTime = new JTextField(10);
	  INPUT_GlobalTime.setLocation(2, verticalSizer + 25 * 12);
	  INPUT_GlobalTime.setSize(INPUT_width-20, 20);
	  //INPUT_M0.setBackground(backgroundColor);
	  //INPUT_M0.setForeground(labelColor);
	  INPUT_GlobalTime.setHorizontalAlignment(JTextField.RIGHT);
	  INPUT_GlobalTime.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			try {
				globalTime = Double.parseDouble(INPUT_GlobalTime.getText());
			} catch (Exception exception) {
				System.out.println("ERROR/CenterPanelRight: Reading Gloabl Time failed - value set to 0.");
				globalTime=0;
			}
			BlueBookVisual.WRITE_INIT();
		}
		  
	  });
	  mainPanel.add(INPUT_GlobalTime);
	    
	  JLabel LABEL_InitAttitude = new JLabel("Initial Attitude:");
	  LABEL_InitAttitude.setLocation(2, verticalSizer + 25 * 13 );
	  LABEL_InitAttitude.setSize(300, 20);
	  LABEL_InitAttitude.setBackground(backgroundColor);
	  LABEL_InitAttitude.setForeground(labelColor);
	  mainPanel.add(LABEL_InitAttitude);
	    
	//------------------------------------------------------------------------------------
	  // Initial 
	  
		JPanel InitialAttitudePanelMain = new JPanel();
		InitialAttitudePanelMain.setLayout(null);
		InitialAttitudePanelMain.setLocation(2, verticalSizer + 25 * 14);
		InitialAttitudePanelMain.setBackground(backgroundColor);
		InitialAttitudePanelMain.setForeground(labelColor);
		//InitialAttitudePanelMain.setBorder(Moon_border);
		InitialAttitudePanelMain.setSize(900, 430);
		mainPanel.add(InitialAttitudePanelMain);
	  
	  
		JPanel InitialAttitudePanel = new JPanel();
		InitialAttitudePanel.setLayout(null);
		InitialAttitudePanel.setLocation(2, 5);
		InitialAttitudePanel.setBackground(backgroundColor);
		InitialAttitudePanel.setForeground(labelColor);
		InitialAttitudePanel.setBorder(Moon_border);
		InitialAttitudePanel.setSize(400, 400);
		InitialAttitudePanelMain.add(InitialAttitudePanel);
		
	      JLabel LABEL_Quarternions = new JLabel("Quarternion Representation");
	      LABEL_Quarternions.setLocation(2, 2);
	      LABEL_Quarternions.setSize(150, 20);
	      LABEL_Quarternions.setBackground(backgroundColor);
	      LABEL_Quarternions.setForeground(labelColor);
	      LABEL_Quarternions.setFont(smallFont);
	      LABEL_Quarternions.setHorizontalAlignment(0);
	      InitialAttitudePanel.add(LABEL_Quarternions);
		
	      JLabel LABEL_Quarternion1 = new JLabel("Quarternion e1");
	      LABEL_Quarternion1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*0 - 15+45);
	      LABEL_Quarternion1.setSize(box_size_InitialAttitude_x, 20);
	      LABEL_Quarternion1.setBackground(backgroundColor);
	      LABEL_Quarternion1.setForeground(labelColor);
	      LABEL_Quarternion1.setFont(smallFont);
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
				BlueBookVisual.WriteInitialAttitude();
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
	      LABEL_Quarternion2.setFont(smallFont);
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
				BlueBookVisual.WriteInitialAttitude();
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
	      LABEL_Quarternion3.setFont(smallFont);
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
				BlueBookVisual.WriteInitialAttitude();
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
	      LABEL_Quarternion4.setFont(smallFont);
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
				BlueBookVisual.WriteInitialAttitude();
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
	      LABEL_Euler.setFont(smallFont);
	      LABEL_Euler.setHorizontalAlignment(0);
	      InitialAttitudePanel.add(LABEL_Euler);
	    
	      JLabel LABEL_Euler1 = new JLabel("Euler E1 - Roll [deg]");
	      LABEL_Euler1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*5 - 15+45);
	      LABEL_Euler1.setSize(box_size_InitialAttitude_x, 20);
	      LABEL_Euler1.setBackground(backgroundColor);
	      LABEL_Euler1.setForeground(labelColor);
	      LABEL_Euler1.setFont(smallFont);
	      LABEL_Euler1.setHorizontalAlignment(0);
	      InitialAttitudePanel.add(LABEL_Euler1);

	         sliderEuler1 = GuiComponents.getGuiSlider(smallFont, (int) (box_size_InitialAttitude_x*1.9), -180, 0 ,180);
	         sliderEuler2 = GuiComponents.getGuiSlider(smallFont, (int) (box_size_InitialAttitude_x*1.9), -90, 0 ,90);
	         sliderEuler3 = GuiComponents.getGuiSlider(smallFont, (int) (box_size_InitialAttitude_x*1.9), -180, 0 ,180);
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
				BlueBookVisual.WriteInitialAttitude();
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
						BlueBookVisual.WriteInitialAttitude();
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
						BlueBookVisual.WriteInitialAttitude();
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
				BlueBookVisual.WriteInitialAttitude();
				sliderEuler1.setValue(Integer.parseInt(INPUT_Euler1.getText()));
			}
	    	  
	      });
	    INPUT_Euler1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				BlueBookVisual.WriteInitialAttitude();
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
	      LABEL_Euler2.setFont(smallFont);
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
				BlueBookVisual.WriteInitialAttitude();
				sliderEuler2.setValue(Integer.parseInt(INPUT_Euler2.getText()));
			}
	    	  
	      });
	    INPUT_Euler2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				BlueBookVisual.WriteInitialAttitude();
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
	      LABEL_Euler3.setFont(smallFont);
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
				BlueBookVisual.WriteInitialAttitude();
				sliderEuler3.setValue(Integer.parseInt(INPUT_Euler3.getText()));
			}
	    	  
	      });
	    INPUT_Euler3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				BlueBookVisual.WriteInitialAttitude();
				if( Double.parseDouble(INPUT_Euler3.getText())>=-90 && Double.parseDouble(INPUT_Euler3.getText())<=90) {
				sliderEuler3.setValue((int) Double.parseDouble(INPUT_Euler3.getText()));
				}
			}
	    	
	    });
	    InitialAttitudePanel.add(INPUT_Euler3);
	    
		JPanel SpaceShip3DPanel = new JPanel();
		SpaceShip3DPanel.setLayout(new BorderLayout());
		SpaceShip3DPanel.setLocation(415, 5);
		//SpaceShip3DPanel.setBackground(backgroundColor);
		//SpaceShip3DPanel.setForeground(labelColor);
		SpaceShip3DPanel.setSize(450, 400);
		SpaceShip3DPanel.setBorder(Moon_border);
		InitialAttitudePanelMain.add(SpaceShip3DPanel);
		
		
	    final JFXPanel fxPanel = new JFXPanel();
	    SpaceShip3DPanel.add(fxPanel, BorderLayout.CENTER);
	    Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	SpaceShipView3D indicator = new SpaceShipView3D();
	        	indicator.start(fxPanel);
	        }
	   });	
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setLabelColor(Color labelColor) {
		this.labelColor = labelColor;
	}
	
	public static void refreshTargetWindow() {
		if(targetIndx==0) {
			targetWindow.setBorder(Earth_border);
		} else if( targetIndx==1) {
			targetWindow.setBorder(Moon_border);
		} else if( targetIndx==2) {
			targetWindow.setBorder(Mars_border);
		} else if( targetIndx==3) {
			targetWindow.setBorder(Venus_border);
		} 
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	TargetWindow.TargetBodyGroup.getChildren().removeAll();
           	TargetWindow.refreshTargetGroup(targetIndx);
        
            }
       });
        targetWindow.revalidate();
        targetWindow.repaint();
        targetWindowFxPanel.revalidate();
        targetWindowFxPanel.repaint();
	}
	
	public static void createTargetWindow() {
	  	   for(int i=0;i<targetWindowContent.size();i++) {
	  		 targetWindowContent.remove((Component) targetWindowContent.get(i));
	   	    }
     //fxPanel.setSize(400,350);
     targetWindow.add(targetWindowFxPanel,BorderLayout.CENTER);
     targetWindowContent.add(targetWindowFxPanel);
     Platform.runLater(new Runnable() {
         @Override
         public void run() {
         	TargetWindow.start(targetWindowFxPanel,targetIndx);
         }
    });
   
	}

	public static int getTargetIndx() {
		return targetIndx;
	}

	public static int getVelocityCoordinateSystem() {
		return VelocityCoordinateSystem;
	}

	public static int getDOF_System() {
		return DOF_System;
	}

	public static double getGlobalTime() {
		return globalTime;
	}

	public static double getControllerFrequency() {
		return controllerFrequency;
	}

	public static void setControllerFrequency(double controllerFrequency) {
		CenterPanelRight.controllerFrequency = controllerFrequency;
		INPUT_ControllerFrequency.setText(decf.format(controllerFrequency));
	}

	public static double getGlobalFrequency() {
		return globalFrequency;
	}

	public static void setGlobalFrequency(double globalFrequency) {
		CenterPanelRight.globalFrequency = globalFrequency;
	}

	public static void setTargetIndx(int targetIndx) {
		CenterPanelRight.targetIndx = targetIndx;
		Target_chooser.setSelectedIndex(targetIndx);
	}

	public static void setVelocityCoordinateSystem(int velocityCoordinateSystem) {
		VelocityCoordinateSystem = velocityCoordinateSystem;
	    	if(velocityCoordinateSystem==1) {
	    		SELECT_VelocitySpherical.setSelected(true);
	    	}else {
	    		SELECT_VelocityCartesian.setSelected(true);
	    	}
	}

	public static void setDOF_System(int dOF_System) {
		DOF_System = dOF_System;
    	if(DOF_System==3) {
    		SELECT_3DOF.setSelected(true);
    	}else if(DOF_System==6){
    		SELECT_6DOF.setSelected(true);
    	}
	}

	public static void setGlobalTime(double globalTime) {
		CenterPanelRight.globalTime = globalTime;
		INPUT_GlobalTime.setText(decf.format(globalTime));
	}
	

}
