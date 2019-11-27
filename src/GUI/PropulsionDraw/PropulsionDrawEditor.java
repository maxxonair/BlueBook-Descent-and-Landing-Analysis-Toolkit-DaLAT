package GUI.PropulsionDraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import GUI.BlueBookVisual;
import GUI.PostProcessing.CreateCustomChart.BackgroundMenuBar;
import GUI.PropulsionDraw.ComponentMetaFileTypes.MainEngineMetaFile;
import GUI.PropulsionDraw.ComponentMetaFileTypes.TankMetaFile;

public class PropulsionDrawEditor {
	
	
	static JFrame frame = new JFrame("Propulsion Block Draw Mk1");
	
	public Canvas Canvas;
	
	private static String imageRocketEngine = "images/propulsionElements/rocketEngine.png";
	private static String imageSingleThruster = "images/propulsionElements/singleThruster.png";
	private static String imageDualThruster = "images/propulsionElements/dualThruster.png";
	private static String imageQuadroThruster = "images/propulsionElements/quadroThruster.png";
	private static String imageFilter = "images/propulsionElements/filter.png";
	private static String imageSolenoidValve = "images/propulsionElements/solenoidValve.png";
	private static String imagePyroValve = "images/propulsionElements/pyroValve.png";
	private static String imageFillDrainValve = "images/propulsionElements/fillDrainValve.png";
	private static String imageCheckValve = "images/propulsionElements/checkValve.png";
	private static String imageReliefValve = "images/propulsionElements/reliefValve.png";
	private static String imagePressureTransducer = "images/propulsionElements/pressureTransducer.png";
	private static String imageOrifice = "images/propulsionElements/orifice.png";
	private static String imageBurstDisk = "images/propulsionElements/burstDisk.png";
	private static String imageTankBasic = "images/propulsionElements/tankBasic.png";
	
	static Font smallFont  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	
	public PropulsionDrawEditor() {
	    Canvas = new Canvas();
	}
	
  public static void main(String[] args) {
   // JFrame frame = new JFrame("Propulsion Block Draw Mk1");

    // by doing this, we prevent Swing from resizing
    // our nice component
    frame.setLayout(new BorderLayout());
    
	BackgroundMenuBar menuBar = new BackgroundMenuBar();
    menuBar.setColor(new Color(250,250,250));
    menuBar.setOpaque(true);
    menuBar.setPreferredSize(new Dimension(1200, 25));
    frame.add(menuBar, BorderLayout.NORTH);

    JMenu menuMain = new JMenu("File");
    menuMain.setForeground(BlueBookVisual.getLabelColor());
    menuMain.setBackground(BlueBookVisual.getBackgroundColor());
    menuMain.setFont(smallFont);
    menuMain.setMnemonic(KeyEvent.VK_A);
    menuBar.add(menuMain);
    
    PropulsionDrawEditor propulsionDrawEditior = new PropulsionDrawEditor();
    JPanel panel = propulsionDrawEditior.getPropulsionDrawArea();
    
    //frame.setSize(900, 600);
    frame.add(panel, BorderLayout.CENTER);

    frame.pack();
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
    frame.addComponentListener(new ComponentAdapter() 
    {  
            public void componentResized(ComponentEvent evt) {
            	propulsionDrawEditior.getCanvas().resizeBackgroundImage();
            }
    });
    propulsionDrawEditior.getCanvas().resizeBackgroundImage();
  }
public static JFrame getFrame() {
	return frame;
}
public static void setFrame(JFrame frame) {
	PropulsionDrawEditor.frame = frame;
}

public JPanel getPropulsionDrawArea() {
	JPanel mainPanel = new JPanel();
	mainPanel.setSize(600, 800);
	mainPanel.setLayout(new BorderLayout());
	
    JSplitPane splitPaneVertical = new JSplitPane();
    splitPaneVertical.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
    splitPaneVertical.setBackground(BlueBookVisual.getBackgroundColor());
    splitPaneVertical.setDividerSize(3);
    splitPaneVertical.setUI(new BasicSplitPaneUI() {
           @SuppressWarnings("serial")
			public BasicSplitPaneDivider createDefaultDivider() {
           return new BasicSplitPaneDivider(this) {
               @SuppressWarnings("unused")
				public void setBorder( Border b) {
               }
               @Override
                   public void paint(Graphics g) {
                   g.setColor(Color.gray);
                   g.fillRect(0, 0, getSize().width, getSize().height);
                       super.paint(g);
                   }
           };
           }
       });
    splitPaneVertical.setDividerLocation(200); 
   // splitPaneVertical.setDividerLocation(0.1);
    mainPanel.add(splitPaneVertical, BorderLayout.CENTER);
    		
    
    JPanel OperatorPanel = new JPanel();
    OperatorPanel.setSize(500, 200);
    OperatorPanel.setLocation(0, 0);
    //OperatorPanel.setBackground(BlueBookVisual.getBackgroundColor());
    OperatorPanel.setLayout(new GridLayout(14,1));
    
    JScrollPane scrollPane = new JScrollPane(OperatorPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    //scrollPane.setPreferredSize(new Dimension(300,500));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    splitPaneVertical.add(scrollPane, JSplitPane.LEFT);
    	    
    	    
    	    ButtonElement tankButton = getElement("Tank", imageTankBasic, 2);
    	    OperatorPanel.add(tankButton);
    	    
    	    ButtonElement rocketEngine = getElement("Main Engine", imageRocketEngine, 1);
    	    OperatorPanel.add(rocketEngine);
    	    
    	    ButtonElement singleThruster = getElement("Thruster", imageSingleThruster, 3);
    	    OperatorPanel.add(singleThruster);
    	    
    	    ButtonElement dualThruster = getElement("Thruster", imageDualThruster, 3);
    	    OperatorPanel.add(dualThruster);
    	    
    	    ButtonElement quadroThruster = getElement("Thruster", imageQuadroThruster, 3);
    	    OperatorPanel.add(quadroThruster);
    	    
    	    ButtonElement solenoidValve = getElement("Solenoid Valve", imageSolenoidValve, 4);
    	    OperatorPanel.add(solenoidValve);
    	    
    	    ButtonElement pyroValve = getElement("Pyro Valve", imagePyroValve, 4);
    	    OperatorPanel.add(pyroValve);
    	    
    	    ButtonElement reliefValve = getElement("Relief Valve", imageReliefValve, 4);
    	    OperatorPanel.add(reliefValve);
    	    
    	    ButtonElement fillDrainValve = getElement("Fill/Drain Valve", imageFillDrainValve, 4);
    	    OperatorPanel.add(fillDrainValve);
    	    
    	    ButtonElement checkValve = getElement("Check Valve", imageCheckValve, 4);
    	    OperatorPanel.add(checkValve);
    	    
    	    ButtonElement filter = getElement("Filter", imageFilter, 5);
    	    OperatorPanel.add(filter);
    	    
    	    ButtonElement orifice = getElement("Orifice", imageOrifice, 5);
    	    OperatorPanel.add(orifice);
    	    
    	    ButtonElement burstDisk = getElement("Burst Disk", imageBurstDisk, 5);
    	    OperatorPanel.add(burstDisk);
    	    
    	    ButtonElement pressureTransducer = getElement("PressureTransducer", imagePressureTransducer, 5);
    	    OperatorPanel.add(pressureTransducer);
    	    
    	    Canvas.setSize(new Dimension(500, 500));
    	    Canvas.setBorder(new LineBorder(Color.BLACK, 1));
    	    Canvas.setOpaque(false);
    	    Canvas.setLayout(null);
    	    splitPaneVertical.add(Canvas,  JSplitPane.RIGHT);
    	    
    	    return mainPanel;
}

public ButtonElement getElement(String elementName, String logoFilePath, int type ) {
	    ButtonElement buttonElement = new ButtonElement(logoFilePath);
	    buttonElement.setLocation(0, 0);
	    buttonElement.setSize(100,100);
	    buttonElement.addMouseListener(new MouseListener() {

	      @Override
	      public void mouseClicked(MouseEvent e) { 
	    	  

	      }

	      @Override
	      public void mousePressed(MouseEvent e) {
	    	  if(type ==1) { // Main Engine
		    	  BoxElement Engine = new BoxElement(elementName, logoFilePath, Canvas);
		    	  Canvas.addMainEngine(Engine, 50, 50);
		    	  MainEngineMetaFile engineFile = new MainEngineMetaFile(Engine.getMetaFile().getID());
		    	  Engine.setMetaFile(engineFile);
		    	  Canvas.repaint();
	    	  } else if (type ==2) { // Tank
		    	  BoxElement Tank = new BoxElement(elementName, logoFilePath, Canvas);
		    	  Canvas.addMainEngine(Tank, 50, 50);
		    	  TankMetaFile engineFile = new TankMetaFile(Tank.getMetaFile().getID());
		    	  Tank.setMetaFile(engineFile);
		    	  Canvas.repaint();
	    	  } else if (type ==3) { // Thruster
		    	  BoxElement Engine = new BoxElement(elementName, logoFilePath, Canvas);
		    	  Canvas.addMainEngine(Engine, 50, 50);
		    	  MainEngineMetaFile engineFile = new MainEngineMetaFile(Engine.getMetaFile().getID());
		    	  Engine.setMetaFile(engineFile);
		    	  Canvas.repaint();
	    	  } else if (type ==4) { // Valve 
		    	  BoxElement Engine = new BoxElement(elementName, logoFilePath, Canvas);
		    	  Canvas.addMainEngine(Engine, 50, 50);
		    	  //MainEngineMetaFile engineFile = new MainEngineMetaFile(Engine.getMetaFile().getID());
		    	 // Engine.setMetaFile(engineFile);
		    	  Canvas.repaint();
	    	  } else if(type ==5)  { // Plumbing element
		    	  BoxElement Engine = new BoxElement(elementName, logoFilePath, Canvas);
		    	  Canvas.addMainEngine(Engine, 50, 50);
		    	  //MainEngineMetaFile engineFile = new MainEngineMetaFile(Engine.getMetaFile().getID());
		    	 // Engine.setMetaFile(engineFile);
		    	  Canvas.repaint(); 
	    	  }
	      }

	      @Override
	      public void mouseReleased(MouseEvent e) {
	    	  
	      }

	      @Override
	      public void mouseEntered(MouseEvent e) { }

	      @Override
	      public void mouseExited(MouseEvent e) { }
	      

	    });
	    buttonElement.addComponentListener(new ComponentAdapter() 
	    {  
	            public void componentResized(ComponentEvent evt) {
	            	Canvas.resizeBackgroundImage();
	            }
	    });
	    return buttonElement;
}

public Canvas getCanvas() {
	return Canvas;
}



}