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
	int low =10;
	int high = 5000;
	int midval = 1000;
	int length = 1000;
	
	int s1Setting = midval;
	int s2Setting = midval;
	int s3Setting = midval;
	
	public ElementWindow(int classType, int elementIndx) {
		
		this.classType = classType;
		
		JFrame frame = new JFrame(""+windowTitle);
		frame.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(400,280));
		mainPanel.setLayout(new GridLayout(5,2));
		frame.add(mainPanel, BorderLayout.CENTER);
		
		Canvas canvas = GeometryFrame.getCanvas();
		
		JTextField sliderDiameter1Indicator = new JTextField();
		JTextField sliderDiameter2Indicator = new JTextField();
		JTextField sliderLengthIndicator    = new JTextField();

			final int  indxN = canvas.getElementList().size();
			if(elementIndx == -1) { // add new element
				try {
					//indx = canvas.getElementList().size();
					double diameter = 1;
					if(canvas.getElementList().size()>0) {
						diameter =  canvas.getElementList().get(canvas.getElementList().size()-1).getDiameter1();
					} else {
						
					}
					canvas.addElement( diameter , diameter, 1 , classType) ;
					((JPanel) canvas).repaint();
				} catch (NullPointerException npe) {
					System.out.println(npe);
				}
			} else {
				s1Setting = (int) (canvas.getElementList().get(elementIndx).getDiameter1() * 1000);
				s2Setting = (int) (canvas.getElementList().get(elementIndx).getDiameter2() * 1000);
				s3Setting = (int) (canvas.getElementList().get(elementIndx).getLength() * 1000);
				frame.setTitle(canvas.getElementList().get(elementIndx).getName());
				sliderDiameter1Indicator.setText(""+canvas.getElementList().get(elementIndx).getDiameter1());
				sliderDiameter2Indicator.setText(""+canvas.getElementList().get(elementIndx).getDiameter2());
				sliderLengthIndicator.setText(""+canvas.getElementList().get(elementIndx).getLength());
			}
			
			
			sliderDiameter1Indicator.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {}

				@Override
				public void focusLost(FocusEvent arg0) {
					List<Element> elementList = canvas.getElementList();
					double diameter = Double.parseDouble(sliderDiameter1Indicator.getText());
					if(elementIndx == -1) {
						elementList.get(indxN).setDiameter1(diameter);
					} else {
						elementList.get(elementIndx).setDiameter1(diameter);	
					}
					canvas.setElementList(elementList);
					//System.out.println(diameter);
					canvas.repaint();
					
				}
				
			});
			sliderDiameter2Indicator.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {}

				@Override
				public void focusLost(FocusEvent arg0) {
					List<Element> elementList = canvas.getElementList();
					double diameter = Double.parseDouble(sliderDiameter2Indicator.getText());
					if(elementIndx == -1) {
						elementList.get(indxN).setDiameter2(diameter);
					} else {
						elementList.get(elementIndx).setDiameter2(diameter);	
					}
					canvas.setElementList(elementList);
					//System.out.println(diameter);
					canvas.repaint();
					
				}
				
			});
			sliderLengthIndicator.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {}

				@Override
				public void focusLost(FocusEvent arg0) {
					List<Element> elementList = canvas.getElementList();
					double diameter = Double.parseDouble(sliderLengthIndicator.getText());
					if(elementIndx == -1) {
						elementList.get(indxN).setLength(diameter);
					} else {
						elementList.get(elementIndx).setLength(diameter);	
					}
					canvas.setElementList(elementList);
					//System.out.println(diameter);
					canvas.repaint();
					
				}
				
			});
			
		
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		
		if(classType == 2) {
		        JLabel sliderDiameter1Label = new JLabel("Set upper diamter: ", JLabel.CENTER);
		        sliderDiameter1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		        p1.add(sliderDiameter1Label, BorderLayout.NORTH);
		        
		        int unit = canvas.getUnit();
		        int high2 = ( (canvas.getHeight() -20)/unit)*1000;
		       
				
				JSlider sliderDiameter1 = GuiComponents.getGuiSlider(smallFont, length, low, midval, high2);
				sliderDiameter1.setPaintLabels(false);
				sliderDiameter1.setValue(s1Setting);
				sliderDiameter1.addChangeListener(new ChangeListener() {
		
					@Override
					public void stateChanged(ChangeEvent sliderDia) {
						List<Element> elementList = canvas.getElementList();
						double diameter = (double) (((JSlider) sliderDia.getSource()).getValue())/1000;
						if(elementIndx == -1) {
							elementList.get(indxN).setDiameter1(diameter);
						} else {
							elementList.get(elementIndx).setDiameter1(diameter);	
						}
						canvas.setElementList(elementList);
						//System.out.println(diameter);
						canvas.repaint();
						sliderDiameter1Indicator.setText(""+diameter);
					}
					
				});
				p1.add(sliderDiameter1, BorderLayout.CENTER);
				mainPanel.add(p1);
				mainPanel.add(sliderDiameter1Indicator);
				
				
				
				JPanel p2 = new JPanel();
				p2.setLayout(new BorderLayout());
				
		        JLabel sliderDiameter2Label = new JLabel("Set lower diamter: ", JLabel.CENTER);
		        sliderDiameter2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		        p2.add(sliderDiameter2Label, BorderLayout.NORTH);
		
				JSlider sliderDiameter2 = GuiComponents.getGuiSlider(smallFont, length, low, midval, high2);
				sliderDiameter2.setPaintLabels(false);
				sliderDiameter2.setValue(s2Setting);
				sliderDiameter2.addChangeListener(new ChangeListener() {
		
					@Override
					public void stateChanged(ChangeEvent sliderDia) {
						List<Element> elementList = canvas.getElementList();
						double diameter = (double) (((JSlider) sliderDia.getSource()).getValue())/1000;
						if(elementIndx==-1) {
						elementList.get(indxN).setDiameter2(diameter);
						} else {
							elementList.get(elementIndx).setDiameter2(diameter);	
						}
						canvas.setElementList(elementList);
						canvas.repaint();
						sliderDiameter2Indicator.setText(""+diameter);
					}
					
				});
				p2.add(sliderDiameter2, BorderLayout.CENTER);
				mainPanel.add(p2);
				mainPanel.add(sliderDiameter2Indicator);
		} else {
	        JLabel sliderDiameter1Label = new JLabel("Set diamter: ", JLabel.CENTER);
	        sliderDiameter1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
	        p1.add(sliderDiameter1Label, BorderLayout.NORTH);
	        
	        int unit = canvas.getUnit();
	        int high2 = ( (canvas.getHeight() -20)/unit)*1000;
	        
			JSlider sliderDiameter1 = GuiComponents.getGuiSlider(smallFont, length, low, midval, high2);
			sliderDiameter1.setPaintLabels(false);
			sliderDiameter1.setValue(s1Setting);
			sliderDiameter1.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent sliderDia) {
					List<Element> elementList = canvas.getElementList();
					double diameter = (double) (((JSlider) sliderDia.getSource()).getValue())/1000;
					if(elementIndx==-1) {
						elementList.get(indxN).setDiameter1(diameter);
						elementList.get(indxN).setDiameter2(diameter);
					} else {
						elementList.get(elementIndx).setDiameter1(diameter);	
						elementList.get(elementIndx).setDiameter2(diameter);	
					}
					canvas.setElementList(elementList);
					canvas.repaint();
					sliderDiameter1Indicator.setText(""+diameter);
				}
				
			});
			p1.add(sliderDiameter1, BorderLayout.CENTER);
			mainPanel.add(p1);
			mainPanel.add(sliderDiameter1Indicator);
		}
		
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		
        JLabel sliderDiameter3Label = new JLabel("Set element length: ", JLabel.CENTER);
        sliderDiameter3Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        p3.add(sliderDiameter3Label, BorderLayout.NORTH);
		
		JSlider sliderLength = GuiComponents.getGuiSlider(smallFont, length, low, midval, 10000);
		sliderLength.setPaintLabels(false);
		sliderLength.setValue(s3Setting);
		sliderLength.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent sliderDia) {
				List<Element> elementList = canvas.getElementList();
				double Length = (double) (((JSlider) sliderDia.getSource()).getValue())/1000;
				if(elementIndx==-1) {
				elementList.get(indxN).setLength(Length);
				} else {
					elementList.get(elementIndx).setLength(Length);	
				}
				canvas.setElementList(elementList);
				canvas.repaint();
				sliderLengthIndicator.setText(""+Length);
			}
			
		});
		p3.add(sliderLength, BorderLayout.CENTER);
		mainPanel.add(p3);
		mainPanel.add(sliderLengthIndicator);
		
		if(elementIndx==-1) {
			List<Element> elementList = canvas.getElementList();
		JTextField nameField = new JTextField("Box "+elementList.size());
		
		/**
		 *  		If <create new element> then <set default name> 
		 */
				if(elementIndx == -1 ) { 
					Element element = elementList.get(elementList.size()-1);
					element.setName("Box "+elementList.size());
					elementList.set(elementList.size()-1, element);
					canvas.setElementList(elementList);
				}
				
				
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
	
	/*
	public static void main(String[] args) {
		
		new ElementWindow(1 , -1);
	}
*/
}
