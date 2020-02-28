package GUI.SimulationSetup.BasicSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import GUI.GuiComponents;
import GUI.FxElements.SpaceShipView3D;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import utils.Mathbox;
import utils.Quaternion;
import utils.WriteInput;

public class AttitudeSetting {
    //-------------------------------------------------------------------------------------------------------------
	private JPanel mainPanel;
	
    private Color backgroundColor;
    private Color labelColor;
    
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
	 
	   private static Border Moon_border 	= BorderFactory.createLineBorder(Color.GRAY, 1);
	//------------------------------------------------------------------------------------------------------------- 
	    private static JTextField INPUT_Euler1, INPUT_Euler2, INPUT_Euler3;
	    public static JTextField INPUT_Quarternion1, INPUT_Quarternion2, INPUT_Quarternion3, INPUT_Quarternion4;
	    public static JSlider sliderEuler1, sliderEuler2, sliderEuler3;
	    
	    
	public AttitudeSetting() {
		
    	backgroundColor = BlueBookVisual.getBackgroundColor();
    	labelColor = BlueBookVisual.getLabelColor();
		
	     mainPanel = new JPanel();
	   // mainPanel.setLayout(new BorderLayout());
	    mainPanel.setPreferredSize(new Dimension(900, 450));
	    mainPanel.setBackground(backgroundColor);
	    mainPanel.setForeground(labelColor);
	    mainPanel.setLayout(null);
	    
	    //---------------------------------------------------------
	    JPanel InitialAttitudePanel = new JPanel();
		InitialAttitudePanel.setLayout(null);
		InitialAttitudePanel.setLocation(2, 5);
		InitialAttitudePanel.setBackground(backgroundColor);
		InitialAttitudePanel.setForeground(labelColor);
		InitialAttitudePanel.setBorder(Moon_border);
		InitialAttitudePanel.setSize(400, 400);
		mainPanel.add(InitialAttitudePanel);
		
		
	      JLabel LABEL_Quarternions =  newLabel("Quarternion Representation");
	      LABEL_Quarternions.setLocation(2, 2);
	      LABEL_Quarternions.setSize(150, 20);
	      InitialAttitudePanel.add(LABEL_Quarternions);
		
	      JLabel LABEL_Quarternion1 = newLabel("Quarternion e1");
	      LABEL_Quarternion1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*0 - 15+45);
	      LABEL_Quarternion1.setSize(box_size_InitialAttitude_x, 20);
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
				try {
				WriteInput.writeInputFile(FilePaths.inputFile);
				} catch (Exception ee ) {
					System.err.print(ee);
				}
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
				WriteInput.writeInputFile(FilePaths.inputFile);
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
				WriteInput.writeInputFile(FilePaths.inputFile);
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
				WriteInput.writeInputFile(FilePaths.inputFile);
				//------------------------------------------------------------------------------
				Quaternion qvector= new Quaternion(Double.parseDouble(INPUT_Quarternion1.getText()),
									 Double.parseDouble(INPUT_Quarternion2.getText()),
									 Double.parseDouble(INPUT_Quarternion3.getText()),
									 Double.parseDouble(INPUT_Quarternion4.getText()) );
				double[][] EulerAngles = Mathbox.Quaternions2Euler(qvector);
				DecimalFormat numberFormat = new DecimalFormat("#.0000000");
				INPUT_Euler1.setText(numberFormat.format(EulerAngles[0][0]*rad2deg));
				INPUT_Euler2.setText(numberFormat.format(EulerAngles[1][0]*rad2deg));
				INPUT_Euler3.setText(numberFormat.format(EulerAngles[2][0]*rad2deg));
				//------------------------------------------------------------------------------
			}
	    	  
	      });
	    InitialAttitudePanel.add(INPUT_Quarternion4);
	    
	    
		JLabel rotLeg1 = newLabel("Rotational Sequence 123:");
		rotLeg1.setLocation(200, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*4 +45);
		rotLeg1.setSize(150, 20);
	    InitialAttitudePanel.add(rotLeg1);
	    
	      JLabel LABEL_Euler = newLabel("Euler Angle Representation");
	      LABEL_Euler.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*4 +45);
	      LABEL_Euler.setSize(150, 20);
	      InitialAttitudePanel.add(LABEL_Euler);
	    
	      JLabel LABEL_Euler1 = newLabel("Euler E1 - Roll [deg]");
	      LABEL_Euler1.setLocation(gap_size_x+(box_size_InitialAttitude_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_InitialAttitude_y)*5 - 15+45);
	      LABEL_Euler1.setSize(box_size_InitialAttitude_x, 20);
	      InitialAttitudePanel.add(LABEL_Euler1);

	      double sliderFactor = 100;
	      
	         sliderEuler1 = GuiComponents.getGuiSlider(smallFont, (int) (box_size_InitialAttitude_x*1.9), (int) (-180*sliderFactor), 0 ,(int) (180*sliderFactor), sliderFactor);
	         sliderEuler2 = GuiComponents.getGuiSlider(smallFont, (int) (box_size_InitialAttitude_x*1.9), (int) (-90*sliderFactor), 0 ,(int) (90*sliderFactor), sliderFactor);
	         sliderEuler3 = GuiComponents.getGuiSlider(smallFont, (int) (box_size_InitialAttitude_x*1.9), (int) (-180*sliderFactor), 0 ,(int) (180*sliderFactor), sliderFactor);
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
				Quaternion q = Mathbox.Euler2Quarternions(Euler);
				INPUT_Quarternion1.setText(""+decQuarternion.format(q.w));
				INPUT_Quarternion2.setText(""+decQuarternion.format(q.x));
				INPUT_Quarternion3.setText(""+decQuarternion.format(q.y));
				INPUT_Quarternion4.setText(""+decQuarternion.format(q.z));
				WriteInput.writeInputFile(FilePaths.inputFile);
				INPUT_Euler1.setText(""+sliderEuler1.getValue()/sliderFactor);
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
						Quaternion q = Mathbox.Euler2Quarternions(Euler);
						INPUT_Quarternion1.setText(""+decQuarternion.format(q.w));
						INPUT_Quarternion2.setText(""+decQuarternion.format(q.x));
						INPUT_Quarternion3.setText(""+decQuarternion.format(q.y));
						INPUT_Quarternion4.setText(""+decQuarternion.format(q.z));
						WriteInput.writeInputFile(FilePaths.inputFile);
						INPUT_Euler2.setText(""+sliderEuler2.getValue()/sliderFactor);
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
						Quaternion q = Mathbox.Euler2Quarternions(Euler);
						INPUT_Quarternion1.setText(""+decQuarternion.format(q.w));
						INPUT_Quarternion2.setText(""+decQuarternion.format(q.x));
						INPUT_Quarternion3.setText(""+decQuarternion.format(q.y));
						INPUT_Quarternion4.setText(""+decQuarternion.format(q.z));
						WriteInput.writeInputFile(FilePaths.inputFile);
						INPUT_Euler3.setText(""+sliderEuler3.getValue()/sliderFactor);
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
				WriteInput.writeInputFile(FilePaths.inputFile);
				sliderEuler1.setValue((int) (Integer.parseInt(INPUT_Euler1.getText())*sliderFactor) );
			}
	    	  
	      });
	    INPUT_Euler1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
				if( Double.parseDouble(INPUT_Euler1.getText())>=-90 && Double.parseDouble(INPUT_Euler1.getText())<=90) {
				sliderEuler1.setValue((int) (Double.parseDouble(INPUT_Euler1.getText())*sliderFactor) );
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
				WriteInput.writeInputFile(FilePaths.inputFile);
				sliderEuler2.setValue((int) (Integer.parseInt(INPUT_Euler2.getText())*sliderFactor));
			}
	    	  
	      });
	    INPUT_Euler2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
				if( Double.parseDouble(INPUT_Euler2.getText())>=-90 && Double.parseDouble(INPUT_Euler2.getText())<=90) {
				sliderEuler2.setValue((int) (Double.parseDouble(INPUT_Euler2.getText())*sliderFactor) );
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
				WriteInput.writeInputFile(FilePaths.inputFile);
				sliderEuler3.setValue((int) (Integer.parseInt(INPUT_Euler3.getText())*sliderFactor));
			}
	    	  
	      });
	    INPUT_Euler3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				WriteInput.writeInputFile(FilePaths.inputFile);
				if( Double.parseDouble(INPUT_Euler3.getText())>=-90 && Double.parseDouble(INPUT_Euler3.getText())<=90) {
				sliderEuler3.setValue((int) (Double.parseDouble(INPUT_Euler3.getText())*sliderFactor) );
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
		mainPanel.add(SpaceShip3DPanel);
		
		
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
	
	private JLabel newLabel(String labelContent) {
		JLabel label = new JLabel(labelContent);
		label.setSize(150, 20);
		label.setBackground(backgroundColor);
		label.setForeground(labelColor);
		label.setFont(smallFont);
		label.setHorizontalAlignment(0);
		return label;
	}
	
	public static void main(String[] args) {
	    JFrame frame = new JFrame("Attitude Setting Test ");
	    frame.setLayout(new BorderLayout());
	    
		AttitudeSetting setting = new AttitudeSetting();
		frame.add(setting.getMainPanel(), BorderLayout.CENTER);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}
}
