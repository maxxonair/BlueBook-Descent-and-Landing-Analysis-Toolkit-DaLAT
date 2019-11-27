package GUI.PropulsionDraw;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import GUI.PropulsionDraw.ComponentMetaFileTypes.MetaDataLine;

public class ElementDataWindow {
	private static JFrame frame;
	public static void showWindow(BoxElement boxElement) {
	     frame = new JFrame("");	
	    frame.setSize(200, 200);
	    List<MetaDataLine> elementMetaList = boxElement.getMetaFile().getElementMetaList();
	    
	    for(MetaDataLine line : elementMetaList) {
		    	JPanel panel = CreateLine(line.name, ""+line.value, boxElement);
		    	frame.add(panel);
	    }
		frame.setLayout(new GridLayout(elementMetaList.size(),1));
	    //f.pack();
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setResizable(false);
	    frame.setVisible(true);

	}
	

		public static JPanel CreateLine(String description, String value, BoxElement boxElement) {
			JPanel panel = new JPanel();
			panel.setLayout(null);
			panel.setSize(200, 30);
			
			JLabel label = new JLabel(""+description);
			label.setSize(100, 25);
			label.setHorizontalAlignment(JLabel.LEFT);
			label.setLocation(0, 1);
			panel.add(label);
			
			JTextField text = new JTextField(""+value);
			text.setSize(100, 25);
			text.setLocation(100,1);
			text.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
						if(description.equals("Name")) {
							boxElement.setName(text.getText());
							boxElement.getMetaFile().setName(text.getText());
						} else {
							boxElement.getMetaFile().getMetaListUpdate(label.getText(), text.getText());
						}
						frame.dispose();
				}
				
			});
			text.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void focusLost(FocusEvent arg0) {
					if(description.equals("Name")) {
						boxElement.setName(text.getText());
						boxElement.getMetaFile().setName(text.getText());
					} else {
						boxElement.getMetaFile().getMetaListUpdate(label.getText(), text.getText());
					}
				}
				
			});
			panel.add(text);
			
			return panel;
		}


}
