package GUI.Dashboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import GUI.BlueBookVisual;
import utils.TextAreaOutputStream;

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
    public static JLabel  INDICATOR_LAT,INDICATOR_LONG,INDICATOR_ALT,INDICATOR_VEL,INDICATOR_FPA,INDICATOR_AZI,INDICATOR_M0,INDICATOR_INTEGTIME, INDICATOR_TARGET;
    public static JTextArea textArea = new JTextArea();
    private static TextAreaOutputStream  taOutputStream = new TextAreaOutputStream(textArea, ""); 
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 
    
    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
    
    //-------------------------------------------------------------------------------------------------------------
	public DashboardLeftPanel() {
		
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
        // Page 4.1
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(385, 800));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setForeground(labelColor);

        
        int uy_p41 = 10 ; 
        int y_ext_vel = 10; 
      
        JLabel LABEL_LONG = new JLabel(" Longitude [deg]");
        LABEL_LONG.setLocation(65, uy_p41 + 0 );
        LABEL_LONG.setSize(150, 20);
        LABEL_LONG.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_LONG.setBorder(Moon_border);
        LABEL_LONG.setBackground(backgroundColor);
        LABEL_LONG.setForeground(labelColor);
        LABEL_LONG.setFont(smallFont);
        mainPanel.add(LABEL_LONG);
        JLabel LABEL_LAT = new JLabel(" Latitude [deg]");
        LABEL_LAT.setLocation(65, uy_p41 + 25 );
        LABEL_LAT.setSize(150, 20);
        LABEL_LAT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        LABEL_LAT.setFont(smallFont);
        //LABEL_LAT.setBorder(Moon_border);
        LABEL_LAT.setBackground(backgroundColor);
        LABEL_LAT.setForeground(labelColor);
        mainPanel.add(LABEL_LAT);
        JLabel LABEL_ALT = new JLabel(" Altitude [m]");
        LABEL_ALT.setLocation(65, uy_p41 + 50 );
        LABEL_ALT.setSize(150, 20);
        LABEL_ALT.setFont(smallFont);
        LABEL_ALT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_ALT.setBorder(Moon_border);
        LABEL_ALT.setBackground(backgroundColor);
        LABEL_ALT.setForeground(labelColor);
        mainPanel.add(LABEL_ALT);
        
        
        JLabel LABEL_VEL = new JLabel(" Velocity [m/s]");
        LABEL_VEL.setLocation(65, uy_p41 + 75 + y_ext_vel);
        LABEL_VEL.setSize(150, 20);
        LABEL_VEL.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        LABEL_VEL.setFont(smallFont);
        //LABEL_VEL.setBorder(Moon_border);
        LABEL_VEL.setBackground(backgroundColor);
        LABEL_VEL.setForeground(labelColor);
        mainPanel.add(LABEL_VEL);
        JLabel LABEL_FPA = new JLabel(" Flight Path Angle [deg]");
        LABEL_FPA.setLocation(65, uy_p41 + 100 + y_ext_vel);
        LABEL_FPA.setSize(150, 20);
        LABEL_FPA.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        LABEL_FPA.setFont(smallFont);
        //LABEL_FPA.setBorder(Moon_border);
        LABEL_FPA.setBackground(backgroundColor);
        LABEL_FPA.setForeground(labelColor);
        mainPanel.add(LABEL_FPA);
        JLabel LABEL_AZI = new JLabel(" Azimuth [deg]");
        LABEL_AZI.setLocation(65, uy_p41 + 125 + y_ext_vel);
        LABEL_AZI.setSize(150, 20);
        LABEL_AZI.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        LABEL_AZI.setFont(smallFont);
        //LABEL_AZI.setBorder(Moon_border);
        LABEL_AZI.setBackground(backgroundColor);
        LABEL_AZI.setForeground(labelColor);
        mainPanel.add(LABEL_AZI);
        
        
        JLabel LABEL_M0 = new JLabel(" Initial Mass [kg]");
        LABEL_M0.setLocation(65, uy_p41 + 150 + y_ext_vel*2);
        LABEL_M0.setSize(150, 20);
        LABEL_M0.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_M0.setBorder(Moon_border);
        LABEL_M0.setFont(smallFont);
        LABEL_M0.setBackground(backgroundColor);
        LABEL_M0.setForeground(labelColor);
        mainPanel.add(LABEL_M0);
        JLabel LABEL_INTEGTIME = new JLabel(" Integration Time [s]");
        LABEL_INTEGTIME.setLocation(65, uy_p41 + 175 + y_ext_vel*2);
        LABEL_INTEGTIME.setSize(150, 20);
        LABEL_INTEGTIME.setFont(smallFont);
        LABEL_INTEGTIME.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //LABEL_INTEGTIME.setBorder(Moon_border);
        LABEL_INTEGTIME.setBackground(backgroundColor);
        LABEL_INTEGTIME.setForeground(labelColor);
        mainPanel.add(LABEL_INTEGTIME);
        
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
        
         INDICATOR_LONG = new JLabel();
        INDICATOR_LONG.setLocation(2, uy_p41 + 25 * 0 );
        INDICATOR_LONG.setSize(60, 20);
        INDICATOR_LONG.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
       // INDICATOR_LONG.setBorder(Moon_border);
        INDICATOR_LONG.setBackground(backgroundColor);
        INDICATOR_LONG.setForeground(labelColor);
        INDICATOR_LONG.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_LONG.setFont(smallFont);
        mainPanel.add(INDICATOR_LONG);
        INDICATOR_LAT = new JLabel();
        INDICATOR_LAT.setLocation(2, uy_p41 + 25 * 1 );
        INDICATOR_LAT.setSize(60, 20);
        INDICATOR_LAT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_LAT.setBorder(Moon_border);
        INDICATOR_LAT.setBackground(backgroundColor);
        INDICATOR_LAT.setFont(smallFont);
        INDICATOR_LAT.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_LAT.setForeground(labelColor);
        mainPanel.add(INDICATOR_LAT);
         INDICATOR_ALT = new JLabel();
        INDICATOR_ALT.setLocation(2, uy_p41 + 25 * 2 );
        INDICATOR_ALT.setSize(60, 20);
        INDICATOR_ALT.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_ALT.setBorder(Moon_border);
        INDICATOR_ALT.setBackground(backgroundColor);
        INDICATOR_ALT.setFont(smallFont);
        INDICATOR_ALT.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_ALT.setForeground(labelColor);
        mainPanel.add(INDICATOR_ALT);
        INDICATOR_VEL = new JLabel();
        INDICATOR_VEL.setLocation(2, uy_p41 + 25 * 3 + y_ext_vel);
        INDICATOR_VEL.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_VEL.setBorder(Moon_border);
        INDICATOR_VEL.setBackground(backgroundColor);
        INDICATOR_VEL.setFont(smallFont);
        INDICATOR_VEL.setForeground(labelColor);
        INDICATOR_VEL.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_VEL.setSize(60, 20);
        mainPanel.add(INDICATOR_VEL);
        INDICATOR_FPA = new JLabel();
        INDICATOR_FPA.setLocation(2, uy_p41 + 25 * 4 + y_ext_vel);
        INDICATOR_FPA.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_FPA.setBorder(Moon_border);
        INDICATOR_FPA.setBackground(backgroundColor);
        INDICATOR_FPA.setFont(smallFont);
        INDICATOR_FPA.setForeground(labelColor);
        INDICATOR_FPA.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_FPA.setSize(60, 20);
        mainPanel.add(INDICATOR_FPA);
        INDICATOR_AZI = new JLabel();
        INDICATOR_AZI.setLocation(2, uy_p41 + 25 * 5 + y_ext_vel);
        INDICATOR_AZI.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_AZI.setBorder(Moon_border);
        INDICATOR_AZI.setBackground(backgroundColor);
        INDICATOR_AZI.setForeground(labelColor);
        INDICATOR_AZI.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_AZI.setFont(smallFont);
        INDICATOR_AZI.setSize(60, 20);
        mainPanel.add(INDICATOR_AZI);        
        INDICATOR_M0 = new JLabel();
        INDICATOR_M0.setLocation(2, uy_p41 + 25 * 6 + y_ext_vel*2);
        INDICATOR_M0.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_M0.setBorder(Moon_border);
        INDICATOR_M0.setBackground(backgroundColor);
        INDICATOR_M0.setForeground(labelColor);
        INDICATOR_M0.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_M0.setFont(smallFont);
        INDICATOR_M0.setSize(60, 20);
        mainPanel.add(INDICATOR_M0);
        INDICATOR_INTEGTIME = new JLabel();
        INDICATOR_INTEGTIME.setLocation(2, uy_p41 + 25 * 7 + y_ext_vel*2);
        INDICATOR_INTEGTIME.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        //INDICATOR_INTEGTIME.setBorder(Moon_border);
        INDICATOR_INTEGTIME.setBackground(backgroundColor);
        INDICATOR_INTEGTIME.setHorizontalAlignment(JLabel.RIGHT);
        INDICATOR_INTEGTIME.setForeground(labelColor);
        INDICATOR_INTEGTIME.setFont(smallFont);
        INDICATOR_INTEGTIME.setSize(60, 20);
       mainPanel.add(INDICATOR_INTEGTIME);
       
       
       
       
       JLabel LABEL_TARGET = new JLabel("Target Body:");
       LABEL_TARGET.setLocation(5, uy_p41 + 25 * 9  );
       LABEL_TARGET.setSize(250, 20);
       LABEL_TARGET.setBackground(backgroundColor);
       LABEL_TARGET.setForeground(labelColor);
       mainPanel.add(LABEL_TARGET);
       INDICATOR_TARGET = new JLabel();
       INDICATOR_TARGET.setLocation(2, uy_p41 + 25 * 10 );
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
      LABEL_VTOUCHDOWN.setLocation(55, uy_p41 + 285  + 25 *0 );
      LABEL_VTOUCHDOWN.setSize(200, 20);
      LABEL_VTOUCHDOWN.setBackground(Color.black);
      LABEL_VTOUCHDOWN.setForeground(labelColor);
      LABEL_VTOUCHDOWN.setFont(smallFont);
      mainPanel.add(LABEL_VTOUCHDOWN);
      JLabel LABEL_DELTAV = new JLabel("  Total D-V [m/s]");
      LABEL_DELTAV.setLocation(55, uy_p41 + 285 + 25 *1 );
      LABEL_DELTAV.setSize(200, 20);
      LABEL_DELTAV.setBackground(Color.black);
      LABEL_DELTAV.setForeground(labelColor);
      LABEL_DELTAV.setFont(smallFont);
      mainPanel.add(LABEL_DELTAV);
      JLabel LABEL_PROPPERC = new JLabel("  Used Propellant [kg]");
      LABEL_PROPPERC.setLocation(270, uy_p41 + 285 + 25 *0 );
      LABEL_PROPPERC.setSize(200, 20);
      LABEL_PROPPERC.setBackground(Color.black);
      LABEL_PROPPERC.setForeground(labelColor);
      LABEL_PROPPERC.setFont(smallFont);
      mainPanel.add(LABEL_PROPPERC);
      JLabel LABEL_RESPROP = new JLabel("  Residual Propellant [%]");
      LABEL_RESPROP.setLocation(260, uy_p41 + 285 + 25 *1 );
      LABEL_RESPROP.setSize(200, 20);
      LABEL_RESPROP.setBackground(Color.black);
      LABEL_RESPROP.setForeground(labelColor);
      LABEL_RESPROP.setFont(smallFont);
      mainPanel.add(LABEL_RESPROP);
      
       INDICATOR_VTOUCHDOWN = new JLabel("");
      INDICATOR_VTOUCHDOWN.setLocation(5, uy_p41 + 285  + 25 *0 );
      INDICATOR_VTOUCHDOWN.setSize(50, 20);
      INDICATOR_VTOUCHDOWN.setBackground(Color.black);
      INDICATOR_VTOUCHDOWN.setForeground(labelColor);
      mainPanel.add(INDICATOR_VTOUCHDOWN);
       INDICATOR_DELTAV = new JLabel("");
      INDICATOR_DELTAV.setLocation(5, uy_p41 + 285 + 25 *1 );
      INDICATOR_DELTAV.setSize(50, 20);
      INDICATOR_DELTAV.setBackground(Color.black);
      INDICATOR_DELTAV.setForeground(labelColor);
      mainPanel.add(INDICATOR_DELTAV);
       INDICATOR_PROPPERC = new JLabel("");
      INDICATOR_PROPPERC.setLocation(225, uy_p41 + 285 + 25 *0 );
      INDICATOR_PROPPERC.setSize(50, 20);
      INDICATOR_PROPPERC.setBackground(Color.black);
      INDICATOR_PROPPERC.setForeground(labelColor);
      mainPanel.add(INDICATOR_PROPPERC);
       INDICATOR_RESPROP = new JLabel("");
      INDICATOR_RESPROP.setLocation(225, uy_p41 + 285 + 25 *1 );
      INDICATOR_RESPROP.setSize(40, 20);
      INDICATOR_RESPROP.setBackground(Color.black);
      INDICATOR_RESPROP.setForeground(labelColor);
      mainPanel.add(INDICATOR_RESPROP);
      //-----------------------------------------------------------------------------------------------
      //								Console Window        
      //-----------------------------------------------------------------------------------------------
        int console_size_x = 390;
        int console_size_y = 270; 
        JLabel LABEL_CONSOLE = new JLabel("Console:");
        LABEL_CONSOLE.setLocation(5, uy_p41 + 25 *17 );
        LABEL_CONSOLE.setSize(200, 20);
        LABEL_CONSOLE.setBackground(backgroundColor);
        LABEL_CONSOLE.setForeground(labelColor);
        mainPanel.add(LABEL_CONSOLE);
        
        JPanel JP_EnginModel = new JPanel();
        JP_EnginModel.setSize(console_size_x,console_size_y);
        JP_EnginModel.setLocation(5, uy_p41 + 25 * 18);
        JP_EnginModel.setBackground(backgroundColor);
        JP_EnginModel.setForeground(labelColor);
         JP_EnginModel.setBorder(BorderFactory.createEmptyBorder(1,1,1,1)); 
        //JP_EnginModel.setBackground(Color.red);
        taOutputStream = null; 
        taOutputStream = new TextAreaOutputStream(textArea, ""); 
        textArea.setForeground(labelColor);
        textArea.setBackground(backgroundColor);
        JScrollPane JSP_EnginModel = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JSP_EnginModel.setBackground(backgroundColor);
        JSP_EnginModel.setForeground(labelColor);
        JSP_EnginModel.getVerticalScrollBar().setForeground(labelColor);
        JSP_EnginModel.getHorizontalScrollBar().setForeground(labelColor);
        JSP_EnginModel.getHorizontalScrollBar().setBackground(backgroundColor);
        JSP_EnginModel.getVerticalScrollBar().setBackground(backgroundColor);
        JSP_EnginModel.setOpaque(true);
        JSP_EnginModel.setPreferredSize(new Dimension(console_size_x-5,console_size_y-10));
        JSP_EnginModel.setLocation(5, 5);
        JP_EnginModel.add(JSP_EnginModel);
        System.setOut(new PrintStream(taOutputStream));       
        //-----------------------------------------------------------------------------------------------
        mainPanel.add(JP_EnginModel);
        //-----------------------------------------------------------------------------------------------
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	

}
