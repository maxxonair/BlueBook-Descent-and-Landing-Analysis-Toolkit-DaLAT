package GUI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.BlueBookVisual;
import GUI.FxElements.AttitudeFx;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import utils.Quaternion;

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
	  String objectFilePath;
    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	  private int ID = 3;
	  private static AttitudeFx spaceShipView;
	  private static Quaternion quatTemp;
	
	public AttitudeView(String objectFilePath) {
		backgroundColor = BlueBookVisual.getBackgroundColor();
		//labelColor = BlueBookVisual.getLabelColor();
		super.type = 2;
		this.objectFilePath = objectFilePath;
		quatTemp = new Quaternion(1,0,0,0);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		mainPanel.addComponentListener(new ResizeListener() {
			@Override
		    public void componentResized(ComponentEvent e) {

				try {
		        spaceShipView.updateFrameSize( mainPanel.getWidth(),  mainPanel.getHeight());
				} catch (NullPointerException exp ) {
					
				}
		    }
		});
		mainPanel.setSize(450,450);
		
		createSpaceshipView(objectFilePath);
	
	}
	
	public void createSpaceshipView(String objectFilePath) {
        final JFXPanel fxPanel = new JFXPanel();
        fxPanel.setSize(450,450);
        mainPanel.add(fxPanel,BorderLayout.CENTER);
        spaceShipView = new AttitudeFx();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	try {
            	spaceShipView.start(fxPanel, objectFilePath);
            	} catch (Exception runExcp){
            		System.out.println(runExcp);
            	}
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
            	//SpaceShipView3DFrontPage.root.getChildren().removeAll();
            	spaceShipView.start(fxPanel, DashboardPlotArea.getModel3DFilePath());
            	//System.out.println(DashboardPlotArea.getModel3DFilePath());
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
	
	public AttitudeFx getSpaceShipView() {
		return spaceShipView;
	}
	
	
	public static Quaternion getQuatTemp() {
		return quatTemp;
	}

	public static void setQuatTemp(Quaternion quatTemp) {
		AttitudeView.quatTemp = quatTemp;
		spaceShipView.modelRotation(quatTemp);
	}

	/**
	 * 
	 * Unit Tester
	 * @param args
	 */
	public static void main(String[] args) {
		 String Model3DFilePath=System.getProperty("user.dir") + "/resourcs/models3D/millenium-falcon.obj";
		JFrame frame = new JFrame("Unit Tester");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());

		AttitudeView view = new AttitudeView(Model3DFilePath);
		frame.add(view.getMainPanel(), BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
	}

}
class ResizeListener extends ComponentAdapter {
    public void componentResized(ComponentEvent e) {
        // Recalculate the variable you mentioned
    }
    
    
}
