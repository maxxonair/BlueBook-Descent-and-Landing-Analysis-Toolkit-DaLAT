package GUI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import GUI.BlueBookVisual;
import GUI.FxElements.SpaceShipView3DFrontPage;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class AttitudeView extends DashboardPlotPanel{
	
	//-------------------------------------------------------------------------------------------------------------
	
	 private JPanel mainPanel;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
     private Color backgroundColor;
    // private Color labelColor;
    
	  Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	  private int ID = 3;
		 private SpaceShipView3DFrontPage spaceShipView;
		 //private String objectFilePath="";
	
	public AttitudeView(String objectFilePath) {
		backgroundColor = BlueBookVisual.getBackgroundColor();
		//labelColor = BlueBookVisual.getLabelColor();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		
		createSpaceshipView();
	
	}
	
	public void createSpaceshipView() {
        final JFXPanel fxPanel = new JFXPanel();
        //fxPanel.setSize(400,350);
        mainPanel.add(fxPanel,BorderLayout.CENTER);
        spaceShipView = new SpaceShipView3DFrontPage();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	spaceShipView.start(fxPanel, DashboardPlotArea.getModel3DFilePath());
            }
       });
      
	}
	
	@Override
	public void refresh() {
		mainPanel.removeAll();
        final JFXPanel fxPanel = new JFXPanel();
        mainPanel.add(fxPanel, BorderLayout.CENTER);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	spaceShipView.model.getChildren().removeAll();
            	spaceShipView.coordinateSystem.getChildren().removeAll();
            	//SpaceShipView3DFrontPage.root.getChildren().removeAll();
            	spaceShipView.start(fxPanel, DashboardPlotArea.getModel3DFilePath());
            	System.out.println(DashboardPlotArea.getModel3DFilePath());
            }
       });
      
	}

	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
	

}
