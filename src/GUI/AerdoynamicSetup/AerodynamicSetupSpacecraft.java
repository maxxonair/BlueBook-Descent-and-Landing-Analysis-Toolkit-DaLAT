package GUI.AerdoynamicSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import utils.WriteInput;

public class AerodynamicSetupSpacecraft {
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements
	private JPanel mainPanel;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
	private Color labelColor = Color.BLACK;

    
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	@SuppressWarnings("rawtypes")
	public JComboBox unitBox;
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	public static JTextField INPUT_ParachuteDiameter, INPUT_SURFACEAREA, INPUT_BALLISTICCOEFFICIENT;;
    public static JRadioButton RB_SurfaceArea, RB_BallisticCoefficient;
    
	public AerodynamicSetupSpacecraft() {
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		
		int INPUT_width = 110; int uy_p41=10;
		//----------------------------------------------------------------------------------
	     //						Aerodynamic Input
	     //----------------------------------------------------------------------------------
	     
	      JLabel LABEL_SurfaceArea = new JLabel("S/C Surface Area [m\u00b2]");
	      LABEL_SurfaceArea.setLocation(INPUT_width+35, uy_p41 + 25 * 1 );
	      LABEL_SurfaceArea.setSize(300, 20);
	      LABEL_SurfaceArea.setBackground(backgroundColor);
	      LABEL_SurfaceArea.setForeground(labelColor);
	      mainPanel.add(LABEL_SurfaceArea);
	      
	      JLabel LABEL_BallisticCoefficient = new JLabel("Ballistic Coefficient [kg/m\u00b2]");
	      LABEL_BallisticCoefficient.setLocation(INPUT_width+35, uy_p41 + 25 * 2 );
	      LABEL_BallisticCoefficient.setSize(300, 20);
	      LABEL_BallisticCoefficient.setBackground(backgroundColor);
	      LABEL_BallisticCoefficient.setForeground(labelColor);
	      mainPanel.add(LABEL_BallisticCoefficient);
	      

		     //System.out.println(readFromFile(FilePaths.Aero_file, 2)+" | "+value); 
		     INPUT_ParachuteDiameter = new JTextField(""){
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
		     INPUT_ParachuteDiameter.setForeground(labelColor);
		     INPUT_ParachuteDiameter.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_ParachuteDiameter.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WriteInput.writeInputFile(FilePaths.inputFile);
				}
		   	  
		     });
		     mainPanel.add(INPUT_ParachuteDiameter);
		     
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
		     INPUT_SURFACEAREA.setForeground(labelColor);
		     INPUT_SURFACEAREA.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_SURFACEAREA.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WriteInput.writeInputFile(FilePaths.inputFile);
					EvaluateSurfaceAreaSetup() ;
				}
		   	  
		     });
		     mainPanel.add(INPUT_SURFACEAREA);
		     
		     INPUT_BALLISTICCOEFFICIENT.setLocation(2, uy_p41 + 25 * 2 );
		     INPUT_BALLISTICCOEFFICIENT.setSize(INPUT_width, 20);
		     INPUT_BALLISTICCOEFFICIENT.setBackground(backgroundColor);
		     INPUT_BALLISTICCOEFFICIENT.setForeground(labelColor);
		     INPUT_BALLISTICCOEFFICIENT.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_BALLISTICCOEFFICIENT.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
					WriteInput.writeInputFile(FilePaths.inputFile);
					System.out.println("write ballistic");
					EvaluateSurfaceAreaSetup() ;
				}
		   	  
		     });
		     mainPanel.add(INPUT_BALLISTICCOEFFICIENT);
		     
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
			     mainPanel.add(RB_SurfaceArea);
			     mainPanel.add(RB_BallisticCoefficient);
			     RB_SurfaceArea.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						if(RB_SurfaceArea.isSelected()) {
							double BC = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText());
							double mass = Double.parseDouble(BlueBookVisual.INPUT_M0.getText());
				    		INPUT_SURFACEAREA.setText(""+String.format("%.2f",mass/BC));
				    		INPUT_BALLISTICCOEFFICIENT.setText("");
				    		
				    		INPUT_SURFACEAREA.setEditable(true);
				    		INPUT_BALLISTICCOEFFICIENT.setEditable(false);	
				    		
						} else if (RB_BallisticCoefficient.isSelected()) {
							double surfacearea = Double.parseDouble(INPUT_SURFACEAREA.getText());
							double mass = Double.parseDouble(BlueBookVisual.INPUT_M0.getText());
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
					
						if(RB_SurfaceArea.isSelected()) {
							double BC = Double.parseDouble(INPUT_BALLISTICCOEFFICIENT.getText());
							double mass = Double.parseDouble(BlueBookVisual.INPUT_M0.getText());
				    		INPUT_SURFACEAREA.setText(""+String.format("%.2f",mass/BC));
				    		INPUT_BALLISTICCOEFFICIENT.setText("");
				    		
				    		INPUT_SURFACEAREA.setEditable(true);
				    		INPUT_BALLISTICCOEFFICIENT.setEditable(false);	
				    		
						} else if (RB_BallisticCoefficient.isSelected()) {
							double surfacearea = Double.parseDouble(INPUT_SURFACEAREA.getText());
							double mass = Double.parseDouble(BlueBookVisual.INPUT_M0.getText());
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
			      mainPanel.add(LABEL_Parachute);
			      

	      
	        String path = "images/milleniumSchlieren2.png";
	        File file = new File(path);
	        try {
	        BufferedImage image2 = ImageIO.read(file);
	        JLabel label = new JLabel(new ImageIcon(image2));
	        label.setSize(300,290);
	        label.setLocation(5, uy_p41 + 25 * 6);
	        label.setBorder(BlueBookVisual.Moon_border);
	        mainPanel.add(label);
	        } catch (Exception e) {
	        	System.err.println("Error: SpaceShip Setup/Aerodynamik - could not load image");
	        }
     
	}
	
	
	
	
    public JPanel getMainPanel() {
		return mainPanel;
	}

	public void EvaluateSurfaceAreaSetup() {
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
}
