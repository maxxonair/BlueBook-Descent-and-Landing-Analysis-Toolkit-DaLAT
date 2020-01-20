package GUI.GeometryModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.GuiComponents;

public class ElementWindow {
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements
	private String windowTitle = "Customize Element";

	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	/*
    private Color backgroundColor;
    private Color labelColor = Color.BLACK;
    private Color canvasColor = Color.WHITE;
    */
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
     //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	int classType=0;
	String setUppderDiameter = "Set upper diameter";
	String setlowerDiamter = "Set lower diameter";
	String setDiamter = "Set diameter:";
	String setLength = "Set length:";
	
	public ElementWindow(int classType, int elementIndx) {
		
		this.classType = classType;
		
		JFrame frame = new JFrame(""+windowTitle);
		frame.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(400,150));
		mainPanel.setLayout(new GridLayout(2,2));
		frame.add(mainPanel, BorderLayout.CENTER);
		
		Canvas canvas = GeometryFrame.getCanvas();

			final int  indxN = canvas.getElementList().size();
			if(elementIndx==-1) { // add new element
				try {
					//indx = canvas.getElementList().size();
					int diameter = 150;
					if(canvas.getElementList().size()>0) {
						diameter = (int) canvas.getElementList().get(canvas.getElementList().size()-1).getDiameter1();
					} else {
						
					}
					canvas.addElement( diameter , diameter, 80. , classType) ;
					((JPanel) canvas).repaint();
				} catch (NullPointerException npe) {
					System.out.println(npe);
				}
			}

		int low =5;
		int high = 300;
		int midval = 100;
		int length = 150;
		
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		
		if(classType == 2) {
		        JLabel sliderDiameter1Label = new JLabel("Set upper diamter: ", JLabel.CENTER);
		        sliderDiameter1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		        p1.add(sliderDiameter1Label, BorderLayout.NORTH);
		        
		        int high2 = canvas.getHeight() -20;
		       
				
				JSlider sliderDiameter1 = GuiComponents.getGuiSlider(smallFont, length, low, midval, high2);
				sliderDiameter1.setValue(midval);
				sliderDiameter1.addChangeListener(new ChangeListener() {
		
					@Override
					public void stateChanged(ChangeEvent sliderDia) {
						List<Element> elementList = canvas.getElementList();
						double diameter = ((JSlider) sliderDia.getSource()).getValue();
						if(elementIndx==-1) {
						elementList.get(indxN).setDiameter1(diameter);
						} else {
							elementList.get(elementIndx).setDiameter1(diameter);	
						}
						canvas.setElementList(elementList);
						canvas.repaint();
					}
					
				});
				p1.add(sliderDiameter1, BorderLayout.CENTER);
				mainPanel.add(p1);
				
				
				JPanel p2 = new JPanel();
				p2.setLayout(new BorderLayout());
				
		        JLabel sliderDiameter2Label = new JLabel("Set lower diamter: ", JLabel.CENTER);
		        sliderDiameter2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		        p2.add(sliderDiameter2Label, BorderLayout.NORTH);
		
				JSlider sliderDiameter2 = GuiComponents.getGuiSlider(smallFont, length, low, midval, high2);
				sliderDiameter2.setValue(midval);
				sliderDiameter2.addChangeListener(new ChangeListener() {
		
					@Override
					public void stateChanged(ChangeEvent sliderDia) {
						List<Element> elementList = canvas.getElementList();
						double diameter = ((JSlider) sliderDia.getSource()).getValue();
						if(elementIndx==-1) {
						elementList.get(indxN).setDiameter2(diameter);
						} else {
							elementList.get(elementIndx).setDiameter2(diameter);	
						}
						canvas.setElementList(elementList);
						canvas.repaint();
					}
					
				});
				p2.add(sliderDiameter2, BorderLayout.CENTER);
				mainPanel.add(p2);
		} else {
	        JLabel sliderDiameter1Label = new JLabel("Set diamter: ", JLabel.CENTER);
	        sliderDiameter1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
	        p1.add(sliderDiameter1Label, BorderLayout.NORTH);
			
			JSlider sliderDiameter1 = GuiComponents.getGuiSlider(smallFont, length, low, midval, high);
			sliderDiameter1.setValue(midval);
			sliderDiameter1.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent sliderDia) {
					List<Element> elementList = canvas.getElementList();
					double diameter = ((JSlider) sliderDia.getSource()).getValue();
					if(elementIndx==-1) {
						elementList.get(indxN).setDiameter1(diameter);
						elementList.get(indxN).setDiameter2(diameter);
					} else {
						elementList.get(elementIndx).setDiameter1(diameter);	
						elementList.get(elementIndx).setDiameter2(diameter);	
					}
					canvas.setElementList(elementList);
					canvas.repaint();
				}
				
			});
			p1.add(sliderDiameter1, BorderLayout.CENTER);
			mainPanel.add(p1);
		}
		
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		
        JLabel sliderDiameter3Label = new JLabel("Set element length: ", JLabel.CENTER);
        sliderDiameter3Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        p3.add(sliderDiameter3Label, BorderLayout.NORTH);
		
		JSlider sliderLength = GuiComponents.getGuiSlider(smallFont, length, low, midval, 600);
		sliderLength.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent sliderDia) {
				List<Element> elementList = canvas.getElementList();
				double Length = ((JSlider) sliderDia.getSource()).getValue();
				if(elementIndx==-1) {
				elementList.get(indxN).setLength(Length);
				} else {
					elementList.get(elementIndx).setLength(Length);	
				}
				canvas.setElementList(elementList);
				canvas.repaint();
			}
			
		});
		p3.add(sliderLength, BorderLayout.CENTER);
		mainPanel.add(p3);
		
		if(elementIndx==-1) {
			List<Element> elementList = canvas.getElementList();
		JTextField nameField = new JTextField("Box "+elementList.size());
		nameField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {}

			@Override
			public void focusLost(FocusEvent arg0) {
				List<Element> elementList = canvas.getElementList();
				Element element = elementList.get(elementList.size()-1);
				element.setName(nameField.getText());
				elementList.set(elementList.size()-1, element);
				canvas.setElementList(elementList);
			}
			
		});
		mainPanel.add(nameField);
		
		} else {
			List<Element> elementList = canvas.getElementList();
			JTextField nameField = new JTextField(""+elementList.get(elementIndx).getName());
			nameField.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {}

				@Override
				public void focusLost(FocusEvent arg0) {
					List<Element> elementList = canvas.getElementList();
					Element element = elementList.get(elementIndx);
					element.setName(nameField.getText());
					elementList.set(elementIndx, element);
					canvas.setElementList(elementList);
				}
				
			});
			mainPanel.add(nameField);
		}
		
		if(elementIndx!=-1) {
			JButton deleteButton = new JButton("Delete Element");
			deleteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					List<Element> elementList = canvas.getElementList();
					elementList.remove(elementIndx);
					canvas.setElementList(elementList);		
					canvas.repaint();
					frame.dispose();
				}
				
			});
			mainPanel.add(deleteButton);
		}
		
		JButton button = new JButton("Ok");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();				
			}
			
		});
		button.setSize(250,25);
		mainPanel.add(button);
		
		button.setFocusable(true);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
       // Point p = MouseInfo.getPointerInfo().getLocation() ;
       // frame.setLocation(p);
        frame.setVisible(true);
        frame.pack();
	}
	
	
	public static void main(String[] args) {
		new ElementWindow(1 , -1);
	}

}
