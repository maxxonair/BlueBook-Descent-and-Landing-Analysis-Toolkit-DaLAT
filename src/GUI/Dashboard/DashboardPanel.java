package GUI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import GUI.BlueBookVisual;

public class DashboardPanel {
	//-------------------------------------------------------------------------------------------------------------
	
	private JPanel mainPanel;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
    //private Color labelColor;
    
	   Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	
	public DashboardPanel(){
		
		backgroundColor = BlueBookVisual.getBackgroundColor();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		
        //-------------------------------------------------------------------------------------
        /**
         * 			Dashboard plotpanel area 
         */
        
	    DashboardPlotArea dashboardPlotArea = new DashboardPlotArea();
	    
		      
		    //-----------------------------------------------------------------------------------------
		      
		      DashboardLeftPanel dashboardLeftPanel = new DashboardLeftPanel();
		
        JScrollPane scrollPane_P1 = new JScrollPane(dashboardLeftPanel.getMainPanel());
        scrollPane_P1.setPreferredSize(new Dimension(415, 800));
        scrollPane_P1.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane_P1, BorderLayout.LINE_START);
        JScrollPane scrollPane1_P1 = new JScrollPane(dashboardPlotArea.getMainPanel());
        scrollPane1_P1.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(dashboardPlotArea.getMainPanel(), BorderLayout.CENTER);
		
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Component Tester");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());

		DashboardPanel dataplot = new DashboardPanel();
		frame.add(dataplot.getMainPanel(), BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
	}
}
