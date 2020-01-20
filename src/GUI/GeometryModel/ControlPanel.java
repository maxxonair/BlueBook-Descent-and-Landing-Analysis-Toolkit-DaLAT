package GUI.GeometryModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.BlueBookVisual;

public class ControlPanel {
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements
	private JPanel controlPanel;
	@SuppressWarnings("unused")
	private Canvas canvas;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
    @SuppressWarnings("unused")
	private Color labelColor = Color.BLACK;
    
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	@SuppressWarnings("rawtypes")
	public JComboBox elementBox;
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	@SuppressWarnings("unused")
	private String elementList;
	private String coneFrustumFilepath = System.getProperty("user.dir") + "/images/conicalFrustum.png";
	private String cylinderFilepath = System.getProperty("user.dir") + "/images/cylinder.png";
	
	public ControlPanel(Canvas canvas) {
		
		this.canvas = canvas;
		backgroundColor = BlueBookVisual.getBackgroundColor();
		
		controlPanel = new JPanel();
		controlPanel.setLayout(null);
		controlPanel.setSize(500, 200);
		controlPanel.setBackground(backgroundColor);
		
		JButton cylinder = new JButton("Add Cylinder");
		cylinder.setSize(100,100);
		cylinder.setLocation(5,5);
		cylinder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ElementWindow(1, -1);
			}
			
		});
	    ImageIcon imagec;
		imagec = new ImageIcon(cylinderFilepath,"");
		imagec = new ImageIcon(getScaledImage(imagec.getImage(),100,100));
		cylinder.setIcon(imagec);
		controlPanel.add(cylinder);	
		
		

		JButton Cone = new JButton("");
		Cone.setSize(100,100);
		Cone.setLocation(110,5);
		Cone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ElementWindow(2, -1);
			}
			
		});
		Cone.setBackground(Color.GRAY);
	    ImageIcon image;
		image = new ImageIcon(coneFrustumFilepath,"");
		image = new ImageIcon(getScaledImage(image.getImage(),100,100));
		Cone.setIcon(image);
		controlPanel.add(Cone);	
		
		
		@SuppressWarnings("rawtypes")
		JComboBox elementBox = new JComboBox();
		elementBox.setSize(100,25);
		elementBox.setLocation(220,5);
		elementBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
			
		});
		controlPanel.add(elementBox);

	}
	
	
	
	public JPanel getControlPanel() {
		return controlPanel;
	}

	static Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}

	/**
	 * 
	 * Test Unit
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Component Tester - Geometry model/Control Panel ");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());

		ControlPanel window = new ControlPanel(new Canvas());
		window.getControlPanel().setSize(500,500);
		frame.add(window.getControlPanel(), BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        Point p = MouseInfo.getPointerInfo().getLocation() ;
        frame.setLocation(p);
        frame.setVisible(true);
		//frame.pack();

	}
}
