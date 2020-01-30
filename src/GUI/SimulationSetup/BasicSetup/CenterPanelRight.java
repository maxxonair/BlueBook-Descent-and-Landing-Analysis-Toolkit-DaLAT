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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import javax.swing.JTextField;
import javax.swing.border.Border;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import GUI.BlueBookVisual.CustomRenderer;
import GUI.FxElements.TargetWindow;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import utils.WriteInput;


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
	    private static JPanel targetWindow;
        final static JFXPanel targetWindowFxPanel = new JFXPanel();
	    private static JRadioButton SELECT_VelocityCartesian;   
	    private static JRadioButton SELECT_VelocitySpherical;
		
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
	    targetWindow.setSize(250, 250);
	    targetWindow.setLocation(417, verticalSizer + 25 * 0 -3  );
	    targetWindow.setBackground(Color.GREEN);
	    targetWindow.setForeground(labelColor);
	    targetWindow.setBorder(Mars_border);
	    targetWindow.setLayout(new BorderLayout());
	    mainPanel.add(targetWindow);
	    
	    JLabel LABEL_TARGETBODY = new JLabel("Target Body");
	    LABEL_TARGETBODY.setLocation(163, verticalSizer + 25 * 0   );
	    LABEL_TARGETBODY.setSize(150, 20);
	    LABEL_TARGETBODY.setBackground(backgroundColor);
	    LABEL_TARGETBODY.setForeground(labelColor);
	    mainPanel.add(LABEL_TARGETBODY);
	    
		 Target_chooser = new JComboBox(Target_Options);
		  //Target_chooser.setBackground(backgroundColor);
		  Target_chooser.setLocation(2, verticalSizer + 25 * 0 );
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
				WriteInput.writeInputFile(FilePaths.inputFile);
				 refreshTargetWindow();
			}
			  
		  });
		  mainPanel.add(Target_chooser);
	    
	    
	    JLabel LABEL_VCoordinateSystem = new JLabel("Select Coordinate System to solve the Velocity Vector");
	    LABEL_VCoordinateSystem.setLocation(5, verticalSizer + 25 * 1   );
	    LABEL_VCoordinateSystem.setSize(350, 20);
	    LABEL_VCoordinateSystem.setBackground(backgroundColor);
	    LABEL_VCoordinateSystem.setForeground(labelColor);
	    mainPanel.add(LABEL_VCoordinateSystem);
	    
	     SELECT_VelocityCartesian =new JRadioButton("Cartesian Velocity Coordinates");    
	     SELECT_VelocitySpherical =new JRadioButton("Spherical Velocity Coordinates");      
	    SELECT_VelocitySpherical.setLocation(5, verticalSizer + 25 * 2 );
	    SELECT_VelocitySpherical.setSize(220,20);
	    SELECT_VelocitySpherical.setBackground(backgroundColor);
	    SELECT_VelocitySpherical.setForeground(labelColor);
	    SELECT_VelocitySpherical.setFont(smallFont);
	    SELECT_VelocityCartesian.setLocation(5, verticalSizer + 25 * 3);
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
				WriteInput.writeInputFile(FilePaths.inputFile);
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
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	  	 
	   });
	   
	   JLabel LABEL_SelectDoF = new JLabel("Select Degrees of Freedom");
	   LABEL_SelectDoF.setLocation(5, verticalSizer + 25 * 4   );
	   LABEL_SelectDoF.setSize(350, 20);
	   LABEL_SelectDoF.setBackground(backgroundColor);
	   LABEL_SelectDoF.setForeground(labelColor);
	   mainPanel.add(LABEL_SelectDoF);
	   
	    SELECT_3DOF =new JRadioButton("3DOF Model");    
	    SELECT_6DOF =new JRadioButton("6DOF Model");      
	   SELECT_3DOF.setLocation(5, verticalSizer + 25 * 5 );
	   SELECT_3DOF.setSize(220,20);
	   SELECT_3DOF.setBackground(backgroundColor);
	   SELECT_3DOF.setForeground(labelColor);
	   SELECT_3DOF.setFont(smallFont);
	   SELECT_6DOF.setLocation(5, verticalSizer + 25 * 6);
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
				WriteInput.writeInputFile(FilePaths.inputFile);
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
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	 	 
	  }); 
	  
	  JLabel LABEL_ControllerFrequency = new JLabel("Set Control Loop Frequency [Hz]");
	  LABEL_ControllerFrequency.setLocation(INPUT_width, verticalSizer + 25 * 7 );
	  LABEL_ControllerFrequency.setSize(300, 20);
	  LABEL_ControllerFrequency.setBackground(backgroundColor);
	  LABEL_ControllerFrequency.setForeground(labelColor);
	  mainPanel.add(LABEL_ControllerFrequency);
	 
	 
	  
	   INPUT_ControllerFrequency = new JTextField(10);
	  INPUT_ControllerFrequency.setLocation(2, verticalSizer + 25 * 7);
	  INPUT_ControllerFrequency.setSize(INPUT_width-20, 20);
	  //INPUT_M0.setBackground(backgroundColor);
	  //INPUT_M0.setForeground(labelColor);
	  INPUT_ControllerFrequency.setHorizontalAlignment(JTextField.RIGHT);
	  INPUT_ControllerFrequency.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			setControllerFrequency(Double.parseDouble(INPUT_ControllerFrequency.getText()));
			WriteInput.writeInputFile(FilePaths.inputFile);
		}
		  
	  });
	  mainPanel.add(INPUT_ControllerFrequency);
	  /*
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
			//WriteInput.writeInputFile(BlueBookVisual.inputFile);
		}
		  
	  });
	  mainPanel.add(INPUT_GlobalFrequency);
	  */
	  JLabel LABEL_IntegTime = new JLabel("Set Simulated Time Frame [s]");
	  LABEL_IntegTime.setLocation(INPUT_width, verticalSizer + 25 * 8 );
	  LABEL_IntegTime.setSize(300, 20);
	  LABEL_IntegTime.setBackground(backgroundColor);
	  LABEL_IntegTime.setForeground(labelColor);
	  mainPanel.add(LABEL_IntegTime);
	 
	 
	  
	   INPUT_GlobalTime = new JTextField(10);
	  INPUT_GlobalTime.setLocation(2, verticalSizer + 25 * 8);
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
			WriteInput.writeInputFile(FilePaths.inputFile);
		}
		  
	  });
	  mainPanel.add(INPUT_GlobalTime);
	    
	  JLabel LABEL_InitAttitude = new JLabel("Initial Attitude Settings:");
	  LABEL_InitAttitude.setLocation(2, verticalSizer + 25 * 9 );
	  LABEL_InitAttitude.setSize(300, 20);
	  LABEL_InitAttitude.setBackground(backgroundColor);
	  LABEL_InitAttitude.setForeground(labelColor);
	  mainPanel.add(LABEL_InitAttitude);
	    
	//------------------------------------------------------------------------------------
	// Initial Attitude Setup 
	//
	  	AttitudeSetting aSetting = new AttitudeSetting();
		aSetting.getMainPanel().setLocation(2, verticalSizer + 25 * 10);
		aSetting.getMainPanel().setSize(900,450);
		mainPanel.add(aSetting.getMainPanel());

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
	
	/**
	 * Unit Tester
	 * @param args
	 */
	public static void main(String[] args) {
	    JFrame frame = new JFrame("Center Panel Right Setting Test ");
	    frame.setLayout(new BorderLayout());
	    
		CenterPanelRight setting = new CenterPanelRight();
		frame.add(setting.getMainPanel(), BorderLayout.CENTER);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}

}
