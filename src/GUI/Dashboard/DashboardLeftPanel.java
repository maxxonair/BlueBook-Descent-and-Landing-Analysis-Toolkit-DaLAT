package GUI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GUI.BlueBookVisual;
import GUI.Dashboard.Console.ConsoleClass;


public class DashboardLeftPanel {
	//-------------------------------------------------------------------------------------------------------------
	
	private JPanel mainPanel;
	
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):
    Color backgroundColor;
    Color labelColor;
    
    static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    static Font labelfont_small       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);
    static Font targetfont            = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 14);
    
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
    public static JLabel INDICATOR_VTOUCHDOWN ,INDICATOR_DELTAV, INDICATOR_PROPPERC, INDICATOR_RESPROP, Error_Indicator,Module_Indicator;
    public static JLabel  INDICATOR_LAT,
    INDICATOR_LONG,
    INDICATOR_ALT,
    INDICATOR_VEL,
    INDICATOR_FPA,
    INDICATOR_AZI,
    INDICATOR_M0,
    INDICATOR_INTEGTIME, 
    INDICATOR_TARGET,
    INDICATOR_PRIMTANKFIL,
    INDICATOR_SECMTANKFIL;
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 
    private ConsoleClass console;
    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
    private int uy_p41 = 5 ; 
    private int y_ext_vel = 10; 
    //-------------------------------------------------------------------------------------------------------------
	public DashboardLeftPanel() {
		
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
        // Page 4.1
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(385, 750));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setForeground(labelColor);

        createLabel(0, " Longitude [deg]", mainPanel);
        createLabel(1, " Latitude [deg]", mainPanel);
        createLabel(2, " Altitude [m]", mainPanel);
        createLabel(3, " Velocity [m/s]", mainPanel);
        createLabel(4, " Flight Path Angle [deg]", mainPanel);
        createLabel(5, " Azimuth [deg]", mainPanel);
        createLabel(6, " Initial Mass [kg]", mainPanel);
        createLabel(7, " Integration Time [s]", mainPanel);
        createLabel(8, " Prim. Tank filling level [%]", mainPanel);
        createLabel(9, " Sec. Tank filling level [%]", mainPanel);
        
        Error_Indicator = new JLabel("");
        Error_Indicator.setLocation(225, uy_p41 + 25 * 9);
        Error_Indicator.setSize(150, 20);
        //Error_Indicator.setBackground(backgroundColor);
        Error_Indicator.setFont(labelfont_small);
        Error_Indicator.setForeground(labelColor);
        mainPanel.add(Error_Indicator);
        
        Module_Indicator = new JLabel("");
        Module_Indicator.setLocation(225, uy_p41 + 25 * 10);
        Module_Indicator.setSize(150, 20);
        Module_Indicator.setFont(labelfont_small);
        Module_Indicator.setForeground(labelColor);
        mainPanel.add(Module_Indicator);
        
         INDICATOR_LONG = createIndicator(0);
        mainPanel.add(INDICATOR_LONG);
        
        INDICATOR_LAT = createIndicator(1);
        mainPanel.add(INDICATOR_LAT);
        
        INDICATOR_ALT = createIndicator(2);
        mainPanel.add(INDICATOR_ALT);
        
        INDICATOR_VEL = createIndicator(3);
        mainPanel.add(INDICATOR_VEL);
        
        INDICATOR_FPA = createIndicator(4);
        mainPanel.add(INDICATOR_FPA);
        
        INDICATOR_AZI = createIndicator(5);
        mainPanel.add(INDICATOR_AZI); 
        
        INDICATOR_M0 = createIndicator(6);
        mainPanel.add(INDICATOR_M0);
        
        INDICATOR_INTEGTIME = createIndicator(7);
        mainPanel.add(INDICATOR_INTEGTIME);
       
        INDICATOR_PRIMTANKFIL = createIndicator(8);
        mainPanel.add(INDICATOR_PRIMTANKFIL);
        
        INDICATOR_SECMTANKFIL = createIndicator(9);
        mainPanel.add(INDICATOR_SECMTANKFIL);
       
       
       JLabel LABEL_TARGET = new JLabel("Target Body:");
       LABEL_TARGET.setLocation(155, uy_p41 + 25 * 13  );
       LABEL_TARGET.setSize(250, 20);
       LABEL_TARGET.setBackground(backgroundColor);
       LABEL_TARGET.setForeground(labelColor);
       mainPanel.add(LABEL_TARGET);
       INDICATOR_TARGET = new JLabel();
       INDICATOR_TARGET.setLocation(152, uy_p41 + 25 * 14 );
       INDICATOR_TARGET.setText("");
       INDICATOR_TARGET.setSize(100, 25);
       INDICATOR_TARGET.setBackground(backgroundColor);
       INDICATOR_TARGET.setForeground(labelColor);
       INDICATOR_TARGET.setHorizontalAlignment(SwingConstants.CENTER);
       INDICATOR_TARGET.setVerticalTextPosition(JLabel.CENTER);
       INDICATOR_TARGET.setFont(targetfont);
       INDICATOR_TARGET.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
       mainPanel.add(INDICATOR_TARGET);
        
      
      JLabel LABEL_VTOUCHDOWN = new JLabel("  Touchdown velocity [m/s]");
      LABEL_VTOUCHDOWN.setLocation(55, uy_p41 + 285  + 25 *4 );
      LABEL_VTOUCHDOWN.setSize(200, 20);
      LABEL_VTOUCHDOWN.setBackground(Color.black);
      LABEL_VTOUCHDOWN.setForeground(labelColor);
      LABEL_VTOUCHDOWN.setFont(smallFont);
      mainPanel.add(LABEL_VTOUCHDOWN);
      JLabel LABEL_DELTAV = new JLabel("  Total D-V [m/s]");
      LABEL_DELTAV.setLocation(55, uy_p41 + 285 + 25 *5 );
      LABEL_DELTAV.setSize(200, 20);
      LABEL_DELTAV.setBackground(Color.black);
      LABEL_DELTAV.setForeground(labelColor);
      LABEL_DELTAV.setFont(smallFont);
      mainPanel.add(LABEL_DELTAV);
      JLabel LABEL_PROPPERC = new JLabel("  Used Propellant [kg]");
      LABEL_PROPPERC.setLocation(270, uy_p41 + 285 + 25 *4 );
      LABEL_PROPPERC.setSize(200, 20);
      LABEL_PROPPERC.setBackground(Color.black);
      LABEL_PROPPERC.setForeground(labelColor);
      LABEL_PROPPERC.setFont(smallFont);
      mainPanel.add(LABEL_PROPPERC);
      JLabel LABEL_RESPROP = new JLabel("  Residual Propellant [%]");
      LABEL_RESPROP.setLocation(260, uy_p41 + 285 + 25 *5 );
      LABEL_RESPROP.setSize(200, 20);
      LABEL_RESPROP.setBackground(Color.black);
      LABEL_RESPROP.setForeground(labelColor);
      LABEL_RESPROP.setFont(smallFont);
      mainPanel.add(LABEL_RESPROP);
      
       INDICATOR_VTOUCHDOWN = new JLabel("");
      INDICATOR_VTOUCHDOWN.setLocation(5, uy_p41 + 285  + 25 *4 );
      INDICATOR_VTOUCHDOWN.setSize(50, 20);
      INDICATOR_VTOUCHDOWN.setBackground(Color.black);
      INDICATOR_VTOUCHDOWN.setForeground(labelColor);
      mainPanel.add(INDICATOR_VTOUCHDOWN);
       INDICATOR_DELTAV = new JLabel("");
      INDICATOR_DELTAV.setLocation(5, uy_p41 + 285 + 25 *5 );
      INDICATOR_DELTAV.setSize(50, 20);
      INDICATOR_DELTAV.setBackground(Color.black);
      INDICATOR_DELTAV.setForeground(labelColor);
      mainPanel.add(INDICATOR_DELTAV);
       INDICATOR_PROPPERC = new JLabel("");
      INDICATOR_PROPPERC.setLocation(225, uy_p41 + 285 + 25 *4 );
      INDICATOR_PROPPERC.setSize(50, 20);
      INDICATOR_PROPPERC.setBackground(Color.black);
      INDICATOR_PROPPERC.setForeground(labelColor);
      mainPanel.add(INDICATOR_PROPPERC);
       INDICATOR_RESPROP = new JLabel("");
      INDICATOR_RESPROP.setLocation(225, uy_p41 + 285 + 25 *5 );
      INDICATOR_RESPROP.setSize(40, 20);
      INDICATOR_RESPROP.setBackground(Color.black);
      INDICATOR_RESPROP.setForeground(labelColor);
      mainPanel.add(INDICATOR_RESPROP);
      //-----------------------------------------------------------------------------------------------
      //								Console Window        
      //-----------------------------------------------------------------------------------------------
      console = new ConsoleClass();
      console.getMainPanel().setSize(385,190);
      console.getMainPanel().setLocation(5, uy_p41 + 285 + 25 *7);
      console.setBackgroundColor(backgroundColor);
      console.linkConPrintOut(); // Display System.out.println()
     // console.linkConPrintErr(); // Display System.out.err()
      console.setLabelColor(labelColor);
      //-----------------------------------------------------------------------------------------------
      mainPanel.add(console.getMainPanel());
      //-----------------------------------------------------------------------------------------------
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	private JLabel createIndicator(int colIndx) {
        JLabel label = new JLabel();
        label.setLocation(2, uy_p41 + 25 * colIndx + y_ext_vel*2);
        label.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        label.setBackground(backgroundColor);
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setForeground(labelColor);
        label.setFont(smallFont);
        label.setSize(60, 20);
        return label;
	}
	
	private JLabel createLabel(int colIndx, String title, JPanel parentPanel) {
        JLabel label = new JLabel(title);
        label.setLocation(65, uy_p41 + 25 * colIndx + y_ext_vel*2);
        label.setSize(150, 20);
        label.setFont(smallFont);
        label.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_INTEGTIME.setBorder(Moon_border);
        label.setBackground(backgroundColor);
        label.setForeground(labelColor);
        parentPanel.add(label);
        return label;
	}	

	public ConsoleClass getConsole() {
		return console;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Component Tester - Geometry model ");
		frame.setSize(300,800);
		frame.setLayout(new BorderLayout());

		DashboardLeftPanel panel = new DashboardLeftPanel();
		panel.getMainPanel().setSize(500,500);
		frame.add(panel.getMainPanel(), BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        Point p = MouseInfo.getPointerInfo().getLocation() ;
        frame.setLocation(p);
        frame.setVisible(true);
	}
}
