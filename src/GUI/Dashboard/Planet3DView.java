package GUI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.BlueBookVisual;
import GUI.GuiComponents;
import GUI.FxElements.TargetView3D;
import Simulator_main.DataSets.RealTimeResultSet;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class Planet3DView extends DashboardPlotPanel {
	//-------------------------------------------------------------------------------------------------------------
	
	private JPanel mainPanel;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
    private Color labelColor;
    
	   Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	 private TargetView3D targetView;
	 List<RealTimeResultSet> resultSet;
	 private int ID=2;
	
	public Planet3DView(List<RealTimeResultSet> resultSet) {
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
		this.resultSet = resultSet;
		super.type = 1;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		
		
	    JPanel TargetViewControlPanel = new JPanel();
	    TargetViewControlPanel.setLayout(new BorderLayout());
	    //TargetViewControlPanel.setPreferredSize(new Dimension(450, 25));
	    TargetViewControlPanel.setBackground(backgroundColor);
	    mainPanel.add(TargetViewControlPanel, BorderLayout.PAGE_END);
	    
     	ImageIcon iconPlayPause =null;
     	int sizeUpperBar=25;
     	try {
     		iconPlayPause = new ImageIcon("images/playPauseIcon.png","");
     		iconPlayPause = new ImageIcon(getScaledImage(iconPlayPause.getImage(),sizeUpperBar,sizeUpperBar));
     	} catch (Exception e) {
     		System.err.println("Error: Dashboard control panel icons could not be loaded."); 
     	}
     	
     	createTargetView3D();
	    
        JButton ButtonTargetViewControlPlay = new JButton("");
        //ButtonTargetViewControlPlay.setLocation(100, 0);
        //ButtonTargetViewControlPlay.setSize(45,25);
        ButtonTargetViewControlPlay.setMinimumSize(new Dimension(20,20));
        ButtonTargetViewControlPlay.setBackground(backgroundColor);
        ButtonTargetViewControlPlay.setOpaque(true);
        ButtonTargetViewControlPlay.setBorderPainted(false);
        ButtonTargetViewControlPlay.setIcon(iconPlayPause);
        ButtonTargetViewControlPlay.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
	                Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	                    	targetView.playPauseAnimation();
	                    }
	                    });
        	}} );
	    TargetViewControlPanel.add(ButtonTargetViewControlPlay, BorderLayout.CENTER);
	    
	    JSlider SpeedSliderTargetViewControl = GuiComponents.getGuiSliderSpeed(smallFont, 100, 0, 10, 40, labelColor, backgroundColor);
	    SpeedSliderTargetViewControl.setMinimumSize(new Dimension(20,20));
	    SpeedSliderTargetViewControl.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				double rotspeed = ((double) (SpeedSliderTargetViewControl.getValue()))/100;
				targetView.targetBodyRotSpeed = rotspeed;
			}
	    	
	    });
	    TargetViewControlPanel.add(SpeedSliderTargetViewControl, BorderLayout.EAST);
		
		
		
	}
	
	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
	public void createTargetView3D() {
        final JFXPanel fxPanel = new JFXPanel();
        //fxPanel.setSize(400,350);
        mainPanel.add(fxPanel,BorderLayout.CENTER);
        targetView = new TargetView3D(2, resultSet);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	targetView.start(fxPanel);
            }
       });
      
	}
	

	@Override
	public void refresh() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	targetView.TargetBodyGroup.getChildren().removeAll();
            	targetView.refreshTargetGroup(DashboardPlotArea.getTargetIndx());
            }
       });
      
	}
	
	@Override
	public int getID() {
		return ID;
	}

	public void main(String[] args) {
		JFrame frame = new JFrame("Component Tester");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());

		Planet3DView dataplot = new Planet3DView(resultSet);
		frame.add(dataplot.getMainPanel(), BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
	}

}
