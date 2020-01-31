package GUI.PropulsionSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import GUI.PropulsionDraw.PropulsionDrawEditor;
import utils.WriteInput;

public class PropulsionSetup {
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements
	private JPanel mainPanel;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
	private Color labelColor = Color.BLACK;
    
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    static Font HeadlineFont          = new Font("Georgia", Font.LAYOUT_LEFT_TO_RIGHT, 12);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	@SuppressWarnings("rawtypes")
	public JComboBox unitBox;
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
    public static JTextField INPUT_RCSXTHRUST, INPUT_RCSYTHRUST, INPUT_RCSZTHRUST, INPUT_RCSTANK, INPUT_RCSXISP, INPUT_RCSYISP,INPUT_RCSZISP;
	public static JTextField INPUT_ISP,INPUT_PROPMASS,INPUT_THRUSTMAX,INPUT_THRUSTMIN;
    public static JTextField INPUT_RCSX, INPUT_RCSY, INPUT_RCSZ, INPUT_ISPMIN; 
    public static JCheckBox INPUT_ISPMODEL; 
    
	public PropulsionSetup() {
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		
		
		  int uy_p41=10;
	      int INPUT_width = 110;
	      //int SidePanel_Width = 380; 
		//---------------------------------------------------------------------------------------------
        //                         Propulsion Definition Block
        //--------------------------------------------------------------------------------------------- 
      
	        JPanel propulsionSidePanel = new JPanel();
	        propulsionSidePanel.setPreferredSize(new Dimension(400, 800));
	        propulsionSidePanel.setBackground(backgroundColor);
	        propulsionSidePanel.setLayout(null);
	        mainPanel.add(propulsionSidePanel, BorderLayout.EAST);
	        
	        
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
        mainPanel.add(PropulsionEditor, BorderLayout.CENTER);
        
        
        JButton propEditorButton = new JButton("Open Full Size Editor");
        propEditorButton.setSize(200,30);
        propEditorButton.setLocation(0, uy_p41 + 25 * 1);
        propEditorButton.setBackground(backgroundColor);
        propEditorButton.setForeground(Color.BLACK);
        propEditorButton.setFont(smallFont);
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
      INPUT_ISP.setForeground(labelColor);
      INPUT_ISP.setHorizontalAlignment(JTextField.RIGHT);
      INPUT_ISP.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInput.writeInputFile(FilePaths.inputFile);
			WriteInput.writeInputFile(FilePaths.inputFile);
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
     INPUT_PROPMASS.setForeground(labelColor);
     INPUT_PROPMASS.setHorizontalAlignment(JTextField.RIGHT);
     INPUT_PROPMASS.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInput.writeInputFile(FilePaths.inputFile);
			WriteInput.writeInputFile(FilePaths.inputFile);
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
     INPUT_THRUSTMAX.setForeground(labelColor);
     INPUT_THRUSTMAX.setHorizontalAlignment(JTextField.RIGHT);
     INPUT_THRUSTMAX.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInput.writeInputFile(FilePaths.inputFile);
			WriteInput.writeInputFile(FilePaths.inputFile);
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
     INPUT_THRUSTMIN.setForeground(labelColor);
     INPUT_THRUSTMIN.setHorizontalAlignment(JTextField.RIGHT);
     INPUT_THRUSTMIN.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInput.writeInputFile(FilePaths.inputFile);
			WriteInput.writeInputFile(FilePaths.inputFile);
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
     INPUT_ISPMODEL.setForeground(labelColor);
     INPUT_ISPMODEL.addItemListener(new ItemListener() {
       	 public void itemStateChanged(ItemEvent e) {
       		WriteInput.writeInputFile(FilePaths.inputFile);
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
     INPUT_ISPMIN.setForeground(labelColor);
     INPUT_ISPMIN.setHorizontalAlignment(JTextField.RIGHT);
     INPUT_ISPMIN.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent e) {
			WriteInput.writeInputFile(FilePaths.inputFile);
			WriteInput.writeInputFile(FilePaths.inputFile);
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
	     INPUT_RCSX.setForeground(labelColor);
	     INPUT_RCSX.setHorizontalAlignment(JTextField.RIGHT);
	     INPUT_RCSX.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
			//	WriteInput.writeInputFile(FilePaths.inputFile);
				WriteInput.writeInputFile(FilePaths.inputFile);
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
	     INPUT_RCSY.setForeground(labelColor);
	     INPUT_RCSY.setHorizontalAlignment(JTextField.RIGHT);
	     INPUT_RCSY.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
			//	WriteInput.writeInputFile(FilePaths.inputFile);
				WriteInput.writeInputFile(FilePaths.inputFile);
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
	     INPUT_RCSZ.setForeground(labelColor);
	     INPUT_RCSZ.setHorizontalAlignment(JTextField.RIGHT);
	     INPUT_RCSZ.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
			//	WriteInput.writeInputFile(FilePaths.inputFile);
				WriteInput.writeInputFile(FilePaths.inputFile);
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
		     INPUT_RCSXTHRUST.setForeground(labelColor);
		     INPUT_RCSXTHRUST.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_RCSXTHRUST.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
				//	WriteInput.writeInputFile(FilePaths.inputFile);
					WriteInput.writeInputFile(FilePaths.inputFile);
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
		     INPUT_RCSYTHRUST.setForeground(labelColor);
		     INPUT_RCSYTHRUST.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_RCSYTHRUST.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
				//	WriteInput.writeInputFile(FilePaths.inputFile);
					WriteInput.writeInputFile(FilePaths.inputFile);
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
		     INPUT_RCSZTHRUST.setForeground(labelColor);
		     INPUT_RCSZTHRUST.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_RCSZTHRUST.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
				//	WriteInput.writeInputFile(FilePaths.inputFile);
					WriteInput.writeInputFile(FilePaths.inputFile);
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
		     INPUT_RCSTANK.setForeground(labelColor);
		     INPUT_RCSTANK.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_RCSTANK.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
				//	WriteInput.writeInputFile(FilePaths.inputFile);
					WriteInput.writeInputFile(FilePaths.inputFile);
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
		     INPUT_RCSXISP.setForeground(labelColor);
		     INPUT_RCSXISP.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_RCSXISP.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
				//	WriteInput.writeInputFile(FilePaths.inputFile);
					WriteInput.writeInputFile(FilePaths.inputFile);
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
		     INPUT_RCSYISP.setForeground(labelColor);
		     INPUT_RCSYISP.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_RCSYISP.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
				//	WriteInput.writeInputFile(FilePaths.inputFile);
					WriteInput.writeInputFile(FilePaths.inputFile);
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
		     INPUT_RCSZISP.setForeground(labelColor);
		     INPUT_RCSZISP.setHorizontalAlignment(JTextField.RIGHT);
		     INPUT_RCSZISP.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) { }

				@Override
				public void focusLost(FocusEvent e) {
				//	WriteInput.writeInputFile(FilePaths.inputFile);
					WriteInput.writeInputFile(FilePaths.inputFile);
				}
		   	  
		     });
		     RCSPanel.add(INPUT_RCSZISP);
     
		
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	
}
