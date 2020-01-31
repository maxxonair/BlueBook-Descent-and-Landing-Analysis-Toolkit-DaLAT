package GUI.AerdoynamicSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import GUI.GuiComponents;
import utils.WriteInput;



public class AerodynamicSetup {
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
	public static List<JRadioButton> DragModelSet = new ArrayList<JRadioButton>();
	public static List<Component> AeroLeftBarAdditionalComponents = new ArrayList<Component>();
	public static List<Component> AeroParachuteBarAdditionalComponents = new ArrayList<Component>();
	public static List<JRadioButton> ParachuteBulletPoints = new ArrayList<JRadioButton>();
    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
    private static int exty_main = 800; 
    public static JTextField ConstantCD_INPUT;
    public static JTextField ConstantParachuteCD_INPUT;
    public static JTextField  INPUT_RB;
	
	public AerodynamicSetup() {
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		
	      int INPUT_width = 110;
	      //int SidePanel_Width = 380; 
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

	     
	  
	      JScrollPane ScrollPaneAerodynamicInput = new JScrollPane(AerodynamicLeftPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	      ScrollPaneAerodynamicInput.setPreferredSize(new Dimension(405, exty_main));
	      ScrollPaneAerodynamicInput.getVerticalScrollBar().setUnitIncrement(16);
	      mainPanel.add(ScrollPaneAerodynamicInput, BorderLayout.LINE_START);
	      JScrollPane ScrollPaneAerodynamicInput2 = new JScrollPane(AerodynamicRightPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	      ScrollPaneAerodynamicInput2.setPreferredSize(new Dimension(405, exty_main));
	      ScrollPaneAerodynamicInput2.getVerticalScrollBar().setUnitIncrement(16);
	      mainPanel.add(ScrollPaneAerodynamicInput2, BorderLayout.CENTER);
	    
	      JLabel LABELdragModel = new JLabel("Select Aerodynamic Drag Model:");
	      LABELdragModel.setLocation(3, 5 + 25 * 0  );
	      LABELdragModel.setSize(190, 20);
	      LABELdragModel.setBackground(backgroundColor);
	      LABELdragModel.setForeground(labelColor);
	      LABELdragModel.setFont(smallFont);
	      LABELdragModel.setHorizontalAlignment(0);
	      AerodynamicDragPanel.add(LABELdragModel);
	      
	      ButtonGroup dragModelGroup = new ButtonGroup();  
		     
	      JRadioButton aeroButton = new JRadioButton("Constant drag coefficient");
	      aeroButton.setLocation(3, 5 + 25 * 1  );
	      aeroButton.setSize(190, 20);
	      aeroButton.setBackground(backgroundColor);
	      aeroButton.setForeground(labelColor);
	      aeroButton.setFont(smallFont);
	      aeroButton.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				WriteInput.writeInputFile(FilePaths.inputFile);
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
				      LABEL_CD.setFont(smallFont);
				      AeroLeftBarAdditionalComponents.add(LABEL_CD);
				      AerodynamicDragPanel.add(LABEL_CD);
					
			        ConstantCD_INPUT = new JTextField("");
			        ConstantCD_INPUT.setLocation(193, 5 + 25 * 2 );
			        ConstantCD_INPUT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        ConstantCD_INPUT.setBorder(BlueBookVisual.Moon_border);
			        ConstantCD_INPUT.setSize(100, 20);
			        ConstantCD_INPUT.setEditable(true);
			        ConstantCD_INPUT.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
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
	      aeroButton.setFont(smallFont);
	      DragModelSet.add(aeroButton);
	      aeroButton.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
					WriteInput.writeInputFile(FilePaths.inputFile);
					//------------------------------------------------------------------------
					int indx = getDragModelSetIndx();
					for(int i=0;i<AeroLeftBarAdditionalComponents.size();i++) {
						AerodynamicDragPanel.remove(AeroLeftBarAdditionalComponents.get(i));
					}
					AerodynamicDragPanel.revalidate();
					AerodynamicDragPanel.repaint();
					if(indx==1) {							
					     INPUT_RB = new JTextField(10);
					     INPUT_RB.setText("");
					     INPUT_RB.setLocation(193, 5 + 25 * 3);;
					     INPUT_RB.setSize(INPUT_width, 20);
					     INPUT_RB.setHorizontalAlignment(JTextField.RIGHT);
					     AeroLeftBarAdditionalComponents.add(INPUT_RB);
					     INPUT_RB.addFocusListener(new FocusListener() {

							@Override
							public void focusGained(FocusEvent arg0) { }

							@Override
							public void focusLost(FocusEvent e) {
								WriteInput.writeInputFile(FilePaths.inputFile);
							}
					   	  
					     });
					     AerodynamicDragPanel.add(INPUT_RB);
				        
					      JLabel LABEL_RB = new JLabel("Heat Shield Body Radius RB [m]");
					      LABEL_RB.setLocation(193, 5 + 25 * 2);
					      LABEL_RB.setSize(300, 20);
					      LABEL_RB.setFont(smallFont);
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
					WriteInput.writeInputFile(FilePaths.inputFile);
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
					      LABEL.setFont(smallFont);
					      AeroParachuteBarAdditionalComponents.add(LABEL);
					      AerodynamicParachuteOptionPanel.add(LABEL);
					      
					        ConstantParachuteCD_INPUT = new JTextField("");
					        ConstantParachuteCD_INPUT.setLocation(3, 5 + 25 * 2 );
					        ConstantParachuteCD_INPUT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
					        ConstantParachuteCD_INPUT.setBorder(BlueBookVisual.Moon_border);
					        ConstantParachuteCD_INPUT.setSize(100, 20);
					        ConstantParachuteCD_INPUT.setEditable(true);
					        ConstantParachuteCD_INPUT.addFocusListener(new FocusListener() {

								@Override
								public void focusGained(FocusEvent arg0) { }

								@Override
								public void focusLost(FocusEvent e) {
									WriteInput.writeInputFile(FilePaths.inputFile);
								}
						   	  
						     });
					        AeroParachuteBarAdditionalComponents.add(ConstantParachuteCD_INPUT);
					        AerodynamicParachuteOptionPanel.add(ConstantParachuteCD_INPUT); 
					
				}
		    	  
		      });
	      ParachuteBulletPoints.get(1).addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent arg0) {
					WriteInput.writeInputFile(FilePaths.inputFile);
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
					      LABEL.setFont(smallFont);
					      AeroParachuteBarAdditionalComponents.add(LABEL);
					      AerodynamicParachuteOptionPanel.add(LABEL);
					
				}
		    	  
		      });
	      
	      ParachuteBulletPoints.get(1).setSelected(true);
	      
	      
	      
	      
	
		
		
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

	public JPanel getMainPanel() {
		return mainPanel;
	}
	public static void main(String[] args) {
        
		JFrame frame = new JFrame("Component Tester - Aero Setup model ");
		frame.setSize(1100,600);
		frame.setLayout(new BorderLayout());

		AerodynamicSetup window = new AerodynamicSetup();
		window.getMainPanel().setSize(500,500);
		frame.add(window.getMainPanel(), BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        Point p = MouseInfo.getPointerInfo().getLocation() ;
        frame.setLocation(p);
        frame.setVisible(true);
		//frame.pack();
		 
		//new YourFrame();
	}
	
}
