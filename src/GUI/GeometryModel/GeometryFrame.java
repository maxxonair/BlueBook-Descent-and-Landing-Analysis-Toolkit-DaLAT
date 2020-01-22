package GUI.GeometryModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.BlueBookVisual;

public class GeometryFrame {
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements
	private JPanel mainPanel;
	private static Canvas canvas;
	private ControlPanel controlPanel;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
    @SuppressWarnings("unused")
	private Color labelColor = Color.BLACK;
    private Color canvasColor = Color.WHITE;
    
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	@SuppressWarnings("rawtypes")
	public JComboBox unitBox;
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	private static double CoM=0;
	private static double CoPr=0;
	private static double CoP=0;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GeometryFrame() {
		backgroundColor = BlueBookVisual.getBackgroundColor();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		mainPanel.setSize(500,500);

	    canvas = new Canvas();
		canvas.setLayout(null);
		canvas.setBackground(canvasColor);
		mainPanel.add(canvas, BorderLayout.CENTER);
		
		unitBox = new JComboBox(canvas.getStrUnits());
		unitBox.setPreferredSize(new Dimension(100,25) );
		unitBox.setLocation(100,100);
		unitBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(unitBox.getSelectedIndex());
			}
			
		});
		canvas.add(unitBox);
		
		controlPanel = new ControlPanel(canvas);
		//controlPanel.getControlPanel().setLayout(null);
		controlPanel.getControlPanel().setPreferredSize(new Dimension(500, 200));
		mainPanel.add(controlPanel.getControlPanel(), BorderLayout.SOUTH);
		
	}
	
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

/**
 * 
 * Component Test Unit 
 * @param args
 */
	public static void main(String[] args) {
        
		JFrame frame = new JFrame("Component Tester - Geometry model ");
		frame.setSize(1100,600);
		frame.setLayout(new BorderLayout());

		GeometryFrame window = new GeometryFrame();
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


public static Canvas getCanvas() {
	return canvas;
}


public static double getCoM() {
	return CoM;
}


public static void setCoM(double coM) {
	CoM = coM;
	ControlPanel.CoMInput.setText(""+coM);
	canvas.setCoM(Double.parseDouble(ControlPanel.CoMInput.getText()));
	try {
		BlueBookVisual.WRITE_SC();
	} catch (Exception e) {
		System.out.println(e);
	}
}


public static double getCoPr() {
	return CoPr;
	
}


public static void setCoPr(double coPr) {
	CoPr = coPr;
	ControlPanel.CoPrInput.setText(""+coPr);
	canvas.setCoPr(Double.parseDouble(ControlPanel.CoPrInput.getText()));
	try {
		BlueBookVisual.WRITE_SC();
	} catch (Exception e) {
		System.out.println(e);
	}
}


public static double getCoP() {
	return CoP;
}


public static void setCoP(double coP) {
	CoP = coP;
	try {
		BlueBookVisual.WRITE_SC();
	} catch (Exception e) {
		System.out.println(e);
	}
}
	
	
}
