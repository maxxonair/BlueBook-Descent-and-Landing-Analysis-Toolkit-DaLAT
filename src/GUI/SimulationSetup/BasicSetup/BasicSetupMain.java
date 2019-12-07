package GUI.SimulationSetup.BasicSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import GUI.BlueBookVisual;

public class BasicSetupMain {
	
    private Color backgroundColor;	
    private int verticalSizer = 10 ;
    
    private static JPanel mainPanel;
	
	public BasicSetupMain() {
		
	    	backgroundColor = BlueBookVisual.getBackgroundColor();

        int SidePanel_Width = 380; 
		
	       mainPanel = new JPanel();
	      mainPanel.setLocation(0, verticalSizer + 26 * 38 );
	      mainPanel.setPreferredSize(new Dimension(1350, 1350));
	      mainPanel.setBackground(backgroundColor);
	      mainPanel.setForeground(Color.white);
	      mainPanel.setLayout(new BorderLayout());
		
	      SidePanelLeft basicSidePanelLeft = new SidePanelLeft();
	      CenterPanelRight basicCenterPanelRight = new CenterPanelRight();
	           
	      JScrollPane scrollPane_LEFT_InputSection = new JScrollPane(basicSidePanelLeft.getMainPanel(),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	      scrollPane_LEFT_InputSection.setPreferredSize(new Dimension(SidePanel_Width+25, BlueBookVisual.exty_main));
	      scrollPane_LEFT_InputSection.getVerticalScrollBar().setUnitIncrement(16);
	      mainPanel.add(scrollPane_LEFT_InputSection, BorderLayout.LINE_START);
	      JScrollPane scrollPane_RIGHT_InputSection = new JScrollPane(basicCenterPanelRight.getMainPanel(),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	      scrollPane_RIGHT_InputSection.getVerticalScrollBar().setUnitIncrement(16);
	      mainPanel.add(scrollPane_RIGHT_InputSection, BorderLayout.CENTER);
	}

	public static JPanel getMainPanel() {
		return mainPanel;
	}
		

}
