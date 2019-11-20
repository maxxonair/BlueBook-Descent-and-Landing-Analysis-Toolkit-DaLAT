package GUI.PropulsionDraw;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

class BoxElement{
	
	private JComponent element ;
	
	
	public BoxElement(String imageFilePath) {
		element = new ComponentElement(imageFilePath);
		element.addMouseListener(new PopClickListener() {
			@Override
		    public void mousePressed(MouseEvent e) {
		        if (e.isPopupTrigger())
		            doPop(e);
		    }
			@Override
		    public void mouseReleased(MouseEvent e) {
		        if (e.isPopupTrigger())
		            doPop(e);
		    }

		    private void doPop(MouseEvent e) {
		        PopUpDemo menu = new PopUpDemo(element);
		        menu.show(e.getComponent(), e.getX(), e.getY());
		    }
		});
		element.setFocusable(true);
		element.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {

				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	}
	
	
	
	public JComponent getElement() {
		return element;
	}



	class PopClickListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {

	    }

	    public void mouseReleased(MouseEvent e) {

	    }


	}
}