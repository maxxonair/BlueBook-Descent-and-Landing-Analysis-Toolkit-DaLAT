package GUI;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;



public class GuiComponents {
	
	static JSlider getGuiSlider(Font font, int length, int low, int midval, int high) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL);
        
        slider.setSize(length,40);
        slider.setMaximum(high);
        slider.setMinimum(low);
        slider.setMajorTickSpacing((high-low)/18);
        slider.setPaintTicks(true);
        slider.setBackground(Color.WHITE);
        slider.setForeground(Color.BLACK);
        slider.setPaintLabels(true);
        
        @SuppressWarnings("rawtypes")
		Hashtable position = new Hashtable();
        
        JLabel left = new JLabel("");
        left.setText(""+low);
        left.setFont(font);
        position.put(low, left);
        
        
        JLabel mid = new JLabel("");
        mid.setText(""+midval);
        mid.setFont(font);
        position.put(0, mid);
        
        
        JLabel right = new JLabel("");
        right.setText("+"+high);
        right.setFont(font);
        position.put(high, right);
        
        
        slider.setValue(0);
        slider.setLabelTable(position);
        
		return slider;
	}

}
