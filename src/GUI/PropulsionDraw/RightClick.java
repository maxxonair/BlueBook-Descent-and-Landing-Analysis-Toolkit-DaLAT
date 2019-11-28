package GUI.PropulsionDraw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    	/*
    	for(BoxElement element : canvas.getCanvasElements()) {
    		System.out.println(element.getName()+"|"+element.getMetaFile().getID());
    	}
    	*/
        //----------------------------------------------------------------
        JMenuItem anItem2 = new JMenuItem("Element Data");
        anItem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ElementDataWindow.showWindow(boxElement);
			}
        	
        });
        add(anItem2);
        addSeparator();
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
			            	            canvas.relate(boxElement, canvas.getCanvasElements().get(i));
			            	            canvas.repaint();
			            	            canvas.getReadWrite().writeFile();
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
        List<Relationship> relationships = canvas.getRelationships();
        UUID thisID = boxElement.getMetaFile().getID();
        
        
        for(int i=0;i<canvas.getCanvasElements().size();i++) {
        	
	        	if(isRelationShip(relationships, boxElement, canvas.getCanvasElements().get(i))) { // Check if relationship exists
	        		
	        		BoxElement currentElement = canvas.getCanvasElements().get(i);
	        		UUID partnerID = canvas.getCanvasElements().get(i).getMetaFile().getID();
	        		JRadioButtonMenuItem element =new JRadioButtonMenuItem(""+currentElement.getName()); 
			        menuItems2.add(element);
			        element.addActionListener(new ActionListener() {
			            public void actionPerformed(ActionEvent e) {
			            	List<Relationship> relationships = canvas.getRelationships();

					         for(int j=0;j<relationships.size();j++) {
					            				UUID parentID = relationships.get(j).getParent().getMetaFile().getID();
					            				UUID childID = relationships.get(j).getChild().getMetaFile().getID();
					            				System.out.println(thisID+"|"+partnerID);
					            				System.out.println(parentID+"|"+childID);
					            				System.out.println("-----------------------------------");
					        					if(parentID.equals(thisID) && childID.equals(partnerID) || parentID.equals(partnerID) && childID.equals(thisID) ) {
					        						
					        						System.out.println("delete "+relationships.get(j).getParent().getName());
					        						relationships.remove(j);	
					        					}
					            	}
					            	
		        					canvas.setRelationships(relationships);
	        						canvas.getReadWrite().writeFile();
		            	            canvas.repaint();
			             } });
			        buttonGroup2.add(element);
			        deleteLink.add(element);
	        	}
    		}
        add(deleteLink);
        //----------------------------------------------------------------
        addSeparator();
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
					if(relationships.get(i).getParent().getElement()==component || relationships.get(i).getChild().getElement()==component) {
						relationships.remove(i);
						canvas.getReadWrite().writeFile();
					}
				}
				for(int i=0;i<canvas.getCanvasElements().size();i++) {
					if(component == canvas.getCanvasElements().get(i).getElement()) {
						canvas.getCanvasElements().remove(i);
						canvas.getReadWrite().writeFile();
					}
				}
				canvas.setRelationships(relationships);
				canvas.repaint();
			}
        	
        });
        add(anItem);
    }
    
    private boolean isRelationShip(List<Relationship> relationships, BoxElement component1, BoxElement component2) {
	    	for(Relationship rel : relationships) {
		    	if(rel.getParent()==component1 && rel.getChild()==component2 || rel.getChild()==component1 && rel.getParent()==component2) {
		    		return true; 
		    	}
	    	}
	    	return false;
    }
}