package GUI.InertiaGeometry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.Border;
import GUI.BlueBookVisual;
import GUI.FilePaths;
import GUI.GeometryModel.GeometryFrame;
import utils.WriteInput;

public class InertiaGeometry {
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

	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
    public static JTextField INPUT_IXX, INPUT_IXY, INPUT_IXZ, INPUT_IYX, INPUT_IYY, INPUT_IYZ, INPUT_IZX, INPUT_IZY, INPUT_IZZ;
    public static JTextField INPUT_Mass;
    
    public InertiaGeometry() {
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
		mainPanel = new JPanel();
		//mainPanel.setLayout(new BorderLayout());
		mainPanel.setLayout(null);
		mainPanel.setBackground(backgroundColor);
		
		 // Mass and Inertia  
	      int INPUT_width = 110;
	      int SidePanel_Width = 380; 
	      int uy_p41 = 10;
	      
			JPanel InertiaxPanel = new JPanel();
			InertiaxPanel.setLocation(0, 80);
			InertiaxPanel.setBackground(backgroundColor);
			InertiaxPanel.setForeground(labelColor);
			InertiaxPanel.setSize(400, 600);
			InertiaxPanel.setLayout(null); 
			mainPanel.add(InertiaxPanel);
	      
	    JSeparator Separator_Page2_2 = new JSeparator();
	    Separator_Page2_2.setLocation(0, 0 );
	    Separator_Page2_2.setSize(SidePanel_Width, 1);
	    Separator_Page2_2.setBackground(labelColor);
	    Separator_Page2_2.setForeground(labelColor);
	    mainPanel.add(Separator_Page2_2);

	    
		GeometryFrame window = new GeometryFrame();
		window.getMainPanel().setSize(1000,700);
		window.getMainPanel().setLocation(400, 5);
		mainPanel.add(window.getMainPanel());
		
			int box_size_x = 60;
			int box_size_y = 25;
			int gap_size_x =  4;
			int gap_size_y =  15;
			
	    JLabel LABEL_SpaceCraftSettings = new JLabel("Spacecraft Settings");
	    LABEL_SpaceCraftSettings.setLocation(0, uy_p41 + 10 * 0  );
	    LABEL_SpaceCraftSettings.setSize(400, 20);
	    LABEL_SpaceCraftSettings.setBackground(backgroundColor);
	    LABEL_SpaceCraftSettings.setForeground(labelColor);
	    LABEL_SpaceCraftSettings.setFont(HeadlineFont);
	    LABEL_SpaceCraftSettings.setHorizontalAlignment(0);
	    mainPanel.add(LABEL_SpaceCraftSettings);
	    
	    JLabel LABEL_Minit = new JLabel("Initial mass [kg]");
	    LABEL_Minit.setLocation(INPUT_width+5, uy_p41 + 25 * 1 );
	    LABEL_Minit.setSize(250, 20);
	    LABEL_Minit.setBackground(backgroundColor);
	    LABEL_Minit.setForeground(labelColor);
	    mainPanel.add(LABEL_Minit);
	    
	    INPUT_Mass = new JTextField(10){
			    /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					@Override public void setBorder(Border border) {
				        // No!
				    }
				};
				INPUT_Mass.setLocation(2, uy_p41 + 25 * 1 );
				INPUT_Mass.setSize(INPUT_width-20, 20);
				INPUT_Mass.setBackground(backgroundColor);
				INPUT_Mass.setForeground(labelColor);
				INPUT_Mass.setHorizontalAlignment(JTextField.RIGHT);
				INPUT_Mass.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				WriteInput.writeInputFile(FilePaths.inputFile);
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	  	  
	    });
	    mainPanel.add(INPUT_Mass);
			        
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
					InertiaMatrixPanel.setBorder(BlueBookVisual.Moon_border);
					InertiaxPanel.add(InertiaMatrixPanel);
					
			        String path2 = "images/momentOfInertia.png";
			        File file2 = new File(path2);
			        try {
			        BufferedImage image3 = ImageIO.read(file2);
			        JLabel label2 = new JLabel(new ImageIcon(image3));
			        label2.setSize(320,240);
			        label2.setLocation(5, 125);
			        label2.setBorder(BlueBookVisual.Moon_border);
			        InertiaMatrixPanel.add(label2);
			        } catch (Exception e) {
			        	System.err.println("Error: SpaceShip Setup/Aerodynamik - could not load image");
			        }

					
			         INPUT_IXX = new JTextField();
			        INPUT_IXX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*0);
			        INPUT_IXX.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_IXX.setBorder(BlueBookVisual.Moon_border);
			        INPUT_IXX.setSize(box_size_x, box_size_y);
			        INPUT_IXX.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
						}
				    	  
				      });
			        InertiaMatrixPanel.add(INPUT_IXX);	 
			        
				      JLabel LABEL_IXX = new JLabel("Ixx");
				      LABEL_IXX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*0 - 15);
				      LABEL_IXX.setSize(box_size_x, 20);
				      LABEL_IXX.setBackground(backgroundColor);
				      LABEL_IXX.setForeground(labelColor);
				      LABEL_IXX.setFont(smallFont);
				      LABEL_IXX.setHorizontalAlignment(0);
				      InertiaMatrixPanel.add(LABEL_IXX);
	 
			         INPUT_IXY = new JTextField("0");
			        INPUT_IXY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*0);
			        INPUT_IXY.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_IXY.setBorder(BlueBookVisual.Moon_border);
			        INPUT_IXY.setSize(box_size_x, box_size_y);
			        INPUT_IXY.setEditable(false);
			        INPUT_IXY.setBackground(Color.lightGray);
			        INPUT_IXY.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
						}
				    	  
				      });
			        InertiaMatrixPanel.add(INPUT_IXY);	 
			        
				      JLabel LABEL_IXY = new JLabel("Ixy");
				      LABEL_IXY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*0 - 15);
				      LABEL_IXY.setSize(box_size_x, 20);
				      LABEL_IXY.setBackground(backgroundColor);
				      LABEL_IXY.setForeground(labelColor);
				      LABEL_IXY.setFont(smallFont);
				      LABEL_IXY.setHorizontalAlignment(0);
				      InertiaMatrixPanel.add(LABEL_IXY);
			        
			         INPUT_IXZ = new JTextField();
			        INPUT_IXZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*0);
			        INPUT_IXZ.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_IXZ.setBorder(BlueBookVisual.Moon_border);
			        INPUT_IXZ.setSize(box_size_x, box_size_y);
			        INPUT_IXZ.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
						}
				    	  
				      });
			        InertiaMatrixPanel.add(INPUT_IXZ);	
			        
				      JLabel LABEL_IXZ = new JLabel("Ixz");
				      LABEL_IXZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*0 - 15);
				      LABEL_IXZ.setSize(box_size_x, 20);
				      LABEL_IXZ.setBackground(backgroundColor);
				      LABEL_IXZ.setForeground(labelColor);
				      LABEL_IXZ.setFont(smallFont);
				      LABEL_IXZ.setHorizontalAlignment(0);
				      InertiaMatrixPanel.add(LABEL_IXZ);
			        
			         INPUT_IYX = new JTextField("0");
			        INPUT_IYX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*1);
			        INPUT_IYX.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_IYX.setBorder(BlueBookVisual.Moon_border);
			        INPUT_IYX.setSize(box_size_x, box_size_y);
			        INPUT_IYX.setEditable(false);
			        INPUT_IYX.setBackground(Color.lightGray);
			        INPUT_IYX.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
						}
				    	  
				      });
			        InertiaMatrixPanel.add(INPUT_IYX);	
			        
				      JLabel LABEL_IYX = new JLabel("Iyx");
				      LABEL_IYX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*1 - 15);
				      LABEL_IYX.setSize(box_size_x, 20);
				      LABEL_IYX.setBackground(backgroundColor);
				      LABEL_IYX.setForeground(labelColor);
				      LABEL_IYX.setFont(smallFont);
				      LABEL_IYX.setHorizontalAlignment(0);
				      InertiaMatrixPanel.add(LABEL_IYX);
	 
			         INPUT_IYY = new JTextField();
			        INPUT_IYY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*1);
			        INPUT_IYY.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_IYY.setBorder(BlueBookVisual.Moon_border);
			        INPUT_IYY.setSize(box_size_x, box_size_y);
			        INPUT_IYY.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
						}
				    	  
				      });
			        InertiaMatrixPanel.add(INPUT_IYY);
			        
				      JLabel LABEL_IYY = new JLabel("Iyy");
				      LABEL_IYY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*1 - 15);
				      LABEL_IYY.setSize(box_size_x, 20);
				      LABEL_IYY.setBackground(backgroundColor);
				      LABEL_IYY.setForeground(labelColor);
				      LABEL_IYY.setFont(smallFont);
				      LABEL_IYY.setHorizontalAlignment(0);
				      InertiaMatrixPanel.add(LABEL_IYY);
			        
			         INPUT_IYZ = new JTextField("0");
			        INPUT_IYZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*1);
			        INPUT_IYZ.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_IYZ.setBorder(BlueBookVisual.Moon_border);
			        INPUT_IYZ.setSize(box_size_x, box_size_y);
			        INPUT_IYZ.setEditable(false);
			        INPUT_IYZ.setBackground(Color.lightGray);
			        INPUT_IYZ.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
						}
				    	  
				      });
			        InertiaMatrixPanel.add(INPUT_IYZ);	
			        
				      JLabel LABEL_IYZ = new JLabel("Iyz");
				      LABEL_IYZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*1 - 15);
				      LABEL_IYZ.setSize(box_size_x, 20);
				      LABEL_IYZ.setBackground(Color.gray);
				      LABEL_IYZ.setForeground(labelColor);
				      LABEL_IYZ.setFont(smallFont);
				      LABEL_IYZ.setHorizontalAlignment(0);
				      InertiaMatrixPanel.add(LABEL_IYZ);
			        
			        INPUT_IZX = new JTextField();
			        INPUT_IZX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*2);
			        INPUT_IZX.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_IZX.setBorder(BlueBookVisual.Moon_border);
			        INPUT_IZX.setSize(box_size_x, box_size_y);
			        INPUT_IZX.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
						}
				    	  
				      });
			        InertiaMatrixPanel.add(INPUT_IZX);	 
			        
				      JLabel LABEL_IZX = new JLabel("Izx");
				      LABEL_IZX.setLocation(gap_size_x+(box_size_x + gap_size_x)*0, gap_size_y + (gap_size_y + box_size_y)*2 - 15);
				      LABEL_IZX.setSize(box_size_x, 20);
				      LABEL_IZX.setBackground(backgroundColor);
				      LABEL_IZX.setForeground(labelColor);
				      LABEL_IZX.setFont(smallFont);
				      LABEL_IZX.setHorizontalAlignment(0);
				      InertiaMatrixPanel.add(LABEL_IZX);
	 
			         INPUT_IZY = new JTextField("0");
			        INPUT_IZY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*2);
			        INPUT_IZY.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_IZY.setBorder(BlueBookVisual.Moon_border);
			        INPUT_IZY.setSize(box_size_x, box_size_y);
			        INPUT_IZY.setEditable(false);
			        INPUT_IZY.setBackground(Color.lightGray);
			        INPUT_IZY.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
						}
				    	  
				      });
			        InertiaMatrixPanel.add(INPUT_IZY);	
			        
				      JLabel LABEL_IZY = new JLabel("Izy");
				      LABEL_IZY.setLocation(gap_size_x+(box_size_x + gap_size_x)*1, gap_size_y + (gap_size_y + box_size_y)*2 - 15);
				      LABEL_IZY.setSize(box_size_x, 20);
				      LABEL_IZY.setBackground(backgroundColor);
				      LABEL_IZY.setForeground(labelColor);
				      LABEL_IZY.setFont(smallFont);
				      LABEL_IZY.setHorizontalAlignment(0);
				      InertiaMatrixPanel.add(LABEL_IZY);

			        
			        INPUT_IZZ = new JTextField();
			        INPUT_IZZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*2);
			        INPUT_IZZ.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			        INPUT_IZZ.setBorder(BlueBookVisual.Moon_border);
			        INPUT_IZZ.setSize(box_size_x, box_size_y);
			        INPUT_IZZ.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent arg0) { }

						@Override
						public void focusLost(FocusEvent e) {
							WriteInput.writeInputFile(FilePaths.inputFile);
						}
				    	  
				      });
			        InertiaMatrixPanel.add(INPUT_IZZ);	
			        
				      JLabel LABEL_IZZ = new JLabel("Izz");
				      LABEL_IZZ.setLocation(gap_size_x+(box_size_x + gap_size_x)*2, gap_size_y + (gap_size_y + box_size_y)*2 - 15);
				      LABEL_IZZ.setSize(box_size_x, 20);
				      LABEL_IZZ.setBackground(backgroundColor);
				      LABEL_IZZ.setForeground(labelColor);
				      LABEL_IZZ.setFont(smallFont);
				      LABEL_IZZ.setHorizontalAlignment(0);
				      InertiaMatrixPanel.add(LABEL_IZZ);
				      
				      
			        
		
    }

	public JPanel getMainPanel() {
		return mainPanel;
	}
    /**
     * 
     * 
     * Unit Tester
     * @param args
     */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Component Tester");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());

		InertiaGeometry panel = new InertiaGeometry();
		panel.getMainPanel().setPreferredSize(new Dimension(1200,800));
		frame.add(panel.getMainPanel(), BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
	}
}
