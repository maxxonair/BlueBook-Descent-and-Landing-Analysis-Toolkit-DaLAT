package GUI;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class SequencePanel{
	
	    Border SequenceElementBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
	    private static Color l_c = new Color(0,0,0);    					// Label Color
	    private static Color bc_c = new Color(255,255,255);				// Background Color
	
	private JPanel Main_Panel ; 
	private JLabel Main_Label ; 
	private static JComboBox Sequence_END_type; 
	private static JComboBox Sequence_type;
	private static JComboBox Sequence_FC_ID; 
	
    private static String[] Sequence_END_option = { "time [s]",						
			   "Altitude [m]", 
			   "Velocity [m/s]" 	
	};
    private static String[] Sequence_options = { "Coasting - no thrust",						
			   "Continous Thrust (no FC)", 
			   "Controlled Thrust (FC)" 	
	};
    private static String[] Sequence_FC_options = { "1"
	};
	
	private int 	sequence_ID;
	private int 	trigger_end_type;
	private double 	trigger_end_value;
	private int 	sequence_type;
	private int 	sequence_controller_ID; 
	
	public JPanel sequencePanel() {
    	JPanel Main_Panel = new JPanel();
    	Main_Panel = new JPanel();
    	Main_Panel.setBackground(bc_c);
    	Main_Panel.setBorder(SequenceElementBorder);
    	Main_Panel.setLayout(null);
    	
    	int uy_p41 = 3;
    	
        JLabel Main_Label = new JLabel("Longitude [deg]");
        Main_Label.setLocation(65, uy_p41 + 25 * 0 );
        Main_Label.setSize(250, 20);
        Main_Label.setBackground(Color.white);
        Main_Label.setForeground(Color.black);
        Main_Panel.add(Main_Label);
    	
    	
    	return Main_Panel;
	}
}