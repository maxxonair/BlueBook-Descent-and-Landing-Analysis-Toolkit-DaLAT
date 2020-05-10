package GUI.BlueBookPlot.serviceFunctions;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.BlueBookPlot.main.BlueBookPlot;

public class ColorChooser {
	
	private static Color color; 
	
	public ColorChooser() {
		
	}
	
	public void getColorSelection( int ID) {
		
        JFrame.setDefaultLookAndFeelDecorated(false);
        JFrame frame = new JFrame("Select Color" );
        //frame.setFont(smallFont);
        JColorChooser ColorChooser = new JColorChooser(BlueBookPlot.getBackgroundColor());
        ColorChooser.getSelectionModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				color = ColorChooser.getColor();
				//button.setText(""+color.getBlue());
				try {
				BlueBookPlot.getInputFileSet().get(ID).setDataColor(color);
				} catch (Exception e) {
					System.out.println("Error Setting Plot Color. Indx "+ID);
				}
				//System.out.println(color.getBlue()+"|"+color.getGreen()+"|"+color.getRed());
				frame.dispose();
			}
        	
        });
        ColorChooser.setSize(400, 400);
        frame.add(ColorChooser, BorderLayout.CENTER);
        
        //frame.add(button, BorderLayout.CENTER);
        
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

	public static void main(String[] args) {
		ColorChooser colorChooser = new ColorChooser();
		colorChooser.getColorSelection(0);
	}
}
