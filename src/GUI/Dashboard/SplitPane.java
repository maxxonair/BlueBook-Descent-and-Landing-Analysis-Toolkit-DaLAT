package GUI.Dashboard;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class SplitPane {
	
	public static JSplitPane getSplitPane(String orientation) {
        JSplitPane splitPane = new JSplitPane();
	        if(orientation.equals("horizontal")) {
	        		//splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
	                splitPane.setDividerLocation(600);
	        } else if(orientation.equals("vertical")) {
	        		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT );
	               // splitPane.setDividerLocation(400);
	        }
        splitPane.setDividerSize(3);
        splitPane.setUI(new BasicSplitPaneUI() {
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
        return splitPane;
	}

}
