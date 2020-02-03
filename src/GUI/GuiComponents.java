package GUI;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

public class GuiComponents {
	public static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	@SuppressWarnings("unchecked")
	public static JSlider getGuiSlider(Font font, int length, int low, int midval, int high) {
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
	
	public static JSlider getGuiSliderSpeed(Font font, int length, int low, int midval, int high, Color foregroundColor, Color backgroundColor) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL);
        
        slider.setSize(length,40);
        slider.setMaximum(high);
        slider.setMinimum(low);
        slider.setMajorTickSpacing((high-low)/10);
        slider.setPaintTicks(true);
        slider.setBackground(Color.WHITE);
        slider.setForeground(Color.BLACK);
       // slider.setPaintLabels(true);
 
        slider.setValue(8);
        
		return slider;
	}
	
	
	public static JPanel getdynamicList(JPanel targetPanel, String title, String[] bulletPointTitles ,
								 List<JRadioButton> contentList) {
		
	      JLabel labelTitle = new JLabel(title);
	      labelTitle.setLocation(3, 5 + 25 * 0  );
	      labelTitle.setSize(190, 20);
	      labelTitle.setBackground(BlueBookVisual.getBackgroundColor());
	      labelTitle.setForeground(BlueBookVisual.getLabelColor());
	      labelTitle.setFont(smallFont);
	      //labelTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
	      targetPanel.add(labelTitle);
 
	      ButtonGroup buttonGroup = new ButtonGroup();  
		  for(int i=0;i<bulletPointTitles.length;i++) {
			      JRadioButton bulletPoint = new JRadioButton(bulletPointTitles[i]);
			      bulletPoint.setLocation(3, 30 + 25 * i );
			      bulletPoint.setSize(190, 20);
			      bulletPoint.setBackground(BlueBookVisual.getBackgroundColor());
			      bulletPoint.setForeground(BlueBookVisual.getLabelColor());
			      bulletPoint.setFont(smallFont);

			      contentList.add(bulletPoint);
			      targetPanel.add(bulletPoint);
			      buttonGroup.add(bulletPoint);
		  }
		
		return targetPanel;
	}

}	
