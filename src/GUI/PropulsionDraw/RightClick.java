package GUI.PropulsionDraw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class PopUpDemo extends JPopupMenu {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuItem anItem;
    public PopUpDemo(JComponent component) {
        anItem = new JMenuItem("Delete");
        anItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DrawTest.Canvas.remove(component);
				DrawTest.Canvas.revalidate();
				DrawTest.Canvas.repaint();
			}
        	
        });
        add(anItem);
    }
}