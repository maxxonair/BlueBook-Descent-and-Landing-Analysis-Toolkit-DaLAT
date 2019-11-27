package GUI.PropulsionDraw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

class ElementPopUpMenu extends JPopupMenu {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuItem anItem;
	//JMenuItem anItem2;
    public ElementPopUpMenu(ComponentElement component, Canvas canvas, BoxElement boxElement) {
        anItem = new JMenuItem("Delete");
        anItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Delete Component From Canvas
				canvas.remove(component);
				canvas.revalidate();
				canvas.repaint();
				// Delete Dependencies from canvas
				List<Relationship> relationships = canvas.getRelationships();
				for(int i=0;i<relationships.size();i++) {
					if(relationships.get(i).getParent()==component || relationships.get(i).getChild()==component) {
						relationships.remove(i);
					}
				}
				for(int i=0;i<canvas.getCanvasElements().size();i++) {
					if(component == canvas.getCanvasElements().get(i).getElement()) {
						canvas.getCanvasElements().remove(i);
					}
				}
				canvas.setRelationships(relationships);
				canvas.repaint();
			}
        	
        });
        add(anItem);
        //----------------------------------------------------------------
        JMenuItem anItem2 = new JMenuItem("Element Data");
        anItem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ElementDataWindow.showWindow(boxElement);
			}
        	
        });
        add(anItem2);
        //----------------------------------------------------------------
        ButtonGroup buttonGroup = new ButtonGroup();
        JMenu addLink = new JMenu("Add link with");
        List<JRadioButtonMenuItem> menuItems = new ArrayList<>();
        for(int i=0;i<canvas.getCanvasElements().size();i++) {
	        menuItems.add(new JRadioButtonMenuItem(""+canvas.getCanvasElements().get(i).getName()));
	        menuItems.get(i).addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
			            	for(int i=0;i<menuItems.size();i++) {
			            		if(menuItems.get(i).isSelected()) {
			            	            canvas.relate(component, canvas.getCanvasElements().get(i).getElement());
			            	            canvas.repaint();	
			            		}
			            	}
	             } });
	        buttonGroup.add(menuItems.get(i));
	        addLink.add(menuItems.get(i));
    		}
        add(addLink);
      //----------------------------------------------------------------
        ButtonGroup buttonGroup2 = new ButtonGroup();
        JMenu deleteLink = new JMenu("Delete link with");
        List<JRadioButtonMenuItem> menuItems2 = new ArrayList<>();
        for(int i=0;i<canvas.getCanvasElements().size();i++) {
	        menuItems2.add(new JRadioButtonMenuItem(""+canvas.getCanvasElements().get(i).getName()));
	        menuItems2.get(i).addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	List<Relationship> relationships = canvas.getRelationships();
			            	for(int i=0;i<menuItems2.size();i++) {
			            		if(menuItems2.get(i).isSelected()) {
			            			for(int j=0;j<relationships.size();j++) {
			        					if(relationships.get(j).getParent()==component && relationships.get(j).getChild()==canvas.getCanvasElements().get(i).getElement() 
			        					|| relationships.get(j).getChild()==component && relationships.get(j).getParent()==canvas.getCanvasElements().get(i).getElement()) {
			        						relationships.remove(j);
			        					}
			        					canvas.setRelationships(relationships);
			            	            canvas.repaint();
			            			}
			            		}
			            	}
	             } });
	        buttonGroup2.add(menuItems2.get(i));
	        deleteLink.add(menuItems2.get(i));
    		}
        add(deleteLink);
    }
}