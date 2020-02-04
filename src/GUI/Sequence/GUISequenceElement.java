package GUI.Sequence;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import GUI.BlueBookVisual;
import javafx.application.Platform;
import utils.WriteInput;

public class GUISequenceElement {
	
    private static int globalLeftGap = 30; // Change later and make dependent!!
    private static int globalTopGap = 100; // Change later and make dependent!!
	
	private static int sidePanelWidth = 303;
	private static int elementHeight = 20;
	private static int upperGap =2 ;
	private static int lowerGap=2;
	private static int offsetX=3;
	private boolean isSelected=false;
	
	static int masterPanelHeight=0;
	private int sequenceID;
	
    static Border moonBorder 	= BorderFactory.createLineBorder(Color.GRAY, 1);
    static Border marsBorder 	= BorderFactory.createLineBorder(Color.ORANGE, 1); 
    
    JPanel masterPanel; 
    
    private String[] elementEvents = {" ",
    									  "Parachute Deployment",
    									  "Parachute Separation",
    									  "Stage Separation",
    									  "Heat Shield Separation"
    };
    
    private String[] elementEnd = {
    								  "Global Time [s]",
    								  "Sequence Time [s]",
    								  "Velocity [m/s]",
    								  "Altitude [m]"
};
    
    private String[] flightController = {" ",
    										  "Roll control",
										  "yaw control",
										  "pitch control",
										  "roll stabilisation",
										  "Thrust full",
										  "Ascent Control",
										  "Descent Control",
										  "External Controller"
};
    
    @SuppressWarnings("rawtypes")
	private JComboBox eventSelect; 
    @SuppressWarnings("rawtypes")
	private JComboBox endSelect; 
    @SuppressWarnings("rawtypes")
	private JComboBox flightControllerSelect;
    
    private JTextField valueEnd;
    private JButton SelectSequenceButton;
    
    public GUISequenceElement(int sequenceID) {
    	this.sequenceID=sequenceID; 
    	masterPanel = createSequencePanel();
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JPanel createSequencePanel() {
	   masterPanelHeight=0;
		 List<Object> contentList = new ArrayList<Object>();
		int numberOfEmptySpaces=0;
		
     	ImageIcon iconPlus =null;
     	ImageIcon iconMinus =null;
     	int sizeUpperBar=20;
     	try {
     		iconPlus = new ImageIcon("images/iconPlus.png","");
     		iconMinus = new ImageIcon("images/iconMinus.png","");
     		iconPlus = new ImageIcon(BlueBookVisual.getScaledImage(iconPlus.getImage(),sizeUpperBar,sizeUpperBar));
     		iconMinus = new ImageIcon(BlueBookVisual.getScaledImage(iconMinus.getImage(),sizeUpperBar,sizeUpperBar));
     	} catch (Exception e) {
     		System.err.println("Error: Loading image icons failed");
     	}
	    masterPanel = new JPanel();
		masterPanel.setLayout(null);
		masterPanel.setBackground(BlueBookVisual.getBackgroundColor());
		masterPanel.setForeground(BlueBookVisual.getLabelColor());
		masterPanel.setBorder(moonBorder);
		masterPanel.setLocation(0,0);
		
        SelectSequenceButton = new JButton("");
        SelectSequenceButton.setLocation(0, upperGap + (upperGap+lowerGap+elementHeight)*0);
        SelectSequenceButton.setSize(sidePanelWidth,20);
        SelectSequenceButton.setForeground(BlueBookVisual.getBackgroundColor());
        SelectSequenceButton.setBackground(BlueBookVisual.getLabelColor());
        SelectSequenceButton.setOpaque(true);
        SelectSequenceButton.setBorderPainted(false);
        SelectSequenceButton.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
	                Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	     
	        				if(!isSelected()) {
		        				masterPanel.setBorder(marsBorder);
		        				masterPanel.revalidate();
		        				masterPanel.repaint();
		        				setSelected(true); //.e.SequencePanel.getSequenceContentList().get(sequenceID).setSelected(true);	
	        				} else {
		        				masterPanel.setBorder(moonBorder);
		        				masterPanel.revalidate();
		        				masterPanel.repaint();
		        				//SequencePanel.getSequenceContentList().get(sequenceID).setSelected(false);
		        				setSelected(false);
	        				}
	        				
	        				for(int i=0;i<SequencePanel.getSequenceContentList().size();i++) {
	        					if(i!=sequenceID) {
	        						SequencePanel.getSequenceContentList().get(i).getMasterPanel().setBorder(moonBorder);
	        						SequencePanel.getSequenceContentList().get(i).setSelected(false);
	        					}
	        				}

	                    }

	                    });
        	}} );
        masterPanel.add(SelectSequenceButton);
	      contentList.add(SelectSequenceButton);

		
		  JTextField SequenceName = new JTextField("AutoSequence"){
			    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override public void setBorder(Border border) {
			        // No!
			    }
			};
	      SequenceName.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*1 );
	      SequenceName.setSize(250, 20);
	      SelectSequenceButton.setText("AutoSequence");
	      SequenceName.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceName.setForeground(BlueBookVisual.getLabelColor());
	      SequenceName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				SelectSequenceButton.setText(SequenceName.getText());
				WriteInput.WRITE_SequenceFile();
			}
	    	  
	      });
	      SequenceName.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceName);
	      contentList.add(SequenceName);
	      /*
	      JLabel SequenceID = new JLabel(""+sequenceID);
	      SequenceID.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*3 );
	      SequenceID.setSize(250, 20);
	      SequenceID.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceID.setForeground(BlueBookVisual.getLabelColor());
	      SequenceID.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceID);
	      contentList.add(SequenceID);
	      */
	  

	        JButton AddSequenceElement = new JButton("");
	        AddSequenceElement.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*2);
	        AddSequenceElement.setSize(30,20);
	        AddSequenceElement.setBackground(BlueBookVisual.getBackgroundColor());
	        AddSequenceElement.setForeground(Color.black);
	        AddSequenceElement.setFont(BlueBookVisual.getSmall_font());
	        AddSequenceElement.setOpaque(true);
	        AddSequenceElement.setBorderPainted(false);
	        AddSequenceElement.setIcon(iconPlus);
	        AddSequenceElement.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) {
		                Platform.runLater(new Runnable() {
		                    @Override
		                    public void run() {
		                    	addGUISequenceElment();
		                    }
		                    });
	        	}} );
	        masterPanel.add(AddSequenceElement);
	        contentList.add(AddSequenceElement);
	        if(sequenceID!=0) {
	        JButton DeleteSequenceElement = new JButton("");
	        DeleteSequenceElement.setLocation(offsetX+60, upperGap + (upperGap+lowerGap+elementHeight)*2);
	        DeleteSequenceElement.setSize(30,20);
	        DeleteSequenceElement.setBackground(BlueBookVisual.getBackgroundColor());
	        DeleteSequenceElement.setForeground(BlueBookVisual.getBackgroundColor());
	        DeleteSequenceElement.setFont(BlueBookVisual.getSmall_font());
	        DeleteSequenceElement.setOpaque(true);
	        DeleteSequenceElement.setBorderPainted(false);
	        DeleteSequenceElement.setIcon(iconMinus);
	        DeleteSequenceElement.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) {
		                Platform.runLater(new Runnable() {
		                    @Override
		                    public void run() {
		                    	if(sequenceID!=0) {
		                    		// Remove Content Element from Canvas
		                    	SequencePanel.SequenceLeftPanel.remove(SequencePanel.getSequenceContentList().get(sequenceID).getMasterPanel());
			    			    SequencePanel.getSequenceContentList().remove(sequenceID);
		                    	SequencePanel.SequenceLeftPanel.revalidate();
			    			      SequencePanel.SequenceLeftPanel.repaint();
			    			      // Remove respective progress bar entry 
			    			      SequencePanel.SequenceProgressBar.remove(SequencePanel.getSequenceProgressBarContent().get(sequenceID));
			    			      SequencePanel.getSequenceProgressBarContent().remove(sequenceID);
			    			      SequencePanel.SequenceProgressBar.revalidate();
			    			      SequencePanel.SequenceProgressBar.repaint();
			    			      //-----------------------------------------------------------------
			    			      // Revalidate :
			    			      for(int i=0;i<SequencePanel.getSequenceContentList().size();i++) {
						      			 SequencePanel.getSequenceContentList().get(i).setSequenceID(i);
						      			// SequenceID.setText(""+SequencePanel.getSequenceContentList().get(i).getSequenceID());
						   // System.out.println(i);
						      			// masterPanel.revalidate();
						      			// masterPanel.repaint();
				      			   int locationX=0;
				      			   int locationY=0;
				      			   if(isOdd(i)) {
				      				   locationY = 400;
				      			   } else {
				      				   locationY=globalTopGap;
				      			   }
				      			   locationX = (int) (globalLeftGap + (2 * sidePanelWidth * i)/3);

				      			  SequencePanel.getSequenceContentList().get(i).getMasterPanel().setLocation(locationX, locationY);
				      			  SequencePanel.getSequenceContentList().get(i).setSequenceID(i);
			                    	  SequencePanel.SequenceLeftPanel.revalidate();
				    			      SequencePanel.SequenceLeftPanel.repaint();
				    			      int labelx=40;
				    			      int labelLocationX = (int) (globalLeftGap + (2 * sidePanelWidth * i)/3 + sidePanelWidth /2 - labelx/2) ;
				    			      
				    			      SequencePanel.getSequenceProgressBarContent().get(i).setLocation(labelLocationX, 0);
				    			      SequencePanel.getSequenceProgressBarContent().get(i).setText(""+i);
				    			      SequencePanel.SequenceProgressBar.revalidate();
				    			      SequencePanel.SequenceProgressBar.repaint();
				    			      resizeCanvas();
				    			      WriteInput.WRITE_SequenceFile();
			    			      }
		                    	}
		                    }
		                    });
	        	}} );
	        masterPanel.add(DeleteSequenceElement);
		      //contentList.add(DeleteSequenceElement);
	        }
	        numberOfEmptySpaces++;
	      JLabel SequenceFCContent = new JLabel("Flight Controller:");
	      SequenceFCContent.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*3 );
	      SequenceFCContent.setSize(250, 20);
	      SequenceFCContent.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceFCContent.setForeground(BlueBookVisual.getLabelColor());
	      SequenceFCContent.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceFCContent);
	      contentList.add(SequenceFCContent);
	      
	      flightControllerSelect = new JComboBox(flightController);
	      flightControllerSelect.setRenderer(new CustomRenderer());
	      flightControllerSelect.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*4);
	      flightControllerSelect.setSize(250,20);
	      flightControllerSelect.setSelectedIndex(0);
	      flightControllerSelect.setMaximumRowCount(20);
	      flightControllerSelect.setMaximumRowCount(20);
	      flightControllerSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				WriteInput.WRITE_SequenceFile();
			}
	    	  
	      });
	      masterPanel.add(flightControllerSelect);
	      contentList.add(flightControllerSelect);
	    
	      
	      JLabel SequenceEventContent = new JLabel("Events:");
	      SequenceEventContent.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*6 );
	      SequenceEventContent.setSize(250, 20);
	      SequenceEventContent.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceEventContent.setForeground(BlueBookVisual.getLabelColor());
	      SequenceEventContent.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceEventContent);
	      contentList.add(SequenceEventContent);
	      
	      eventSelect = new JComboBox(elementEvents);
	      eventSelect.setRenderer(new CustomRenderer());
	      eventSelect.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*7);
	      eventSelect.setSize(250,20);
	      eventSelect.setSelectedIndex(0);
	      eventSelect.setMaximumRowCount(20);
	      eventSelect.setMaximumRowCount(20);
	      eventSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				WriteInput.WRITE_SequenceFile();
			}
	    	  
	      });
	      masterPanel.add(eventSelect);
	      contentList.add(eventSelect);
	      
	      JLabel SequenceEndCondition = new JLabel("End Condition:");
	      SequenceEndCondition.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*8 );
	      SequenceEndCondition.setSize(250, 20);
	      SequenceEndCondition.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceEndCondition.setForeground(BlueBookVisual.getLabelColor());
	      SequenceEndCondition.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceEndCondition);
	      contentList.add(SequenceEndCondition);
	      
	      JLabel relationLabel = new JLabel(">");
	      relationLabel.setLocation(offsetX+150, upperGap + (upperGap+lowerGap+elementHeight)*9 );
	      relationLabel.setSize(40, 20);
	      relationLabel.setBackground(BlueBookVisual.getBackgroundColor());
	      relationLabel.setForeground(BlueBookVisual.getLabelColor());
	     // relationLabel.setFont(BlueBookVisual.getSmall_font());
	      relationLabel.setHorizontalAlignment(JLabel.CENTER);
	      masterPanel.add(relationLabel);
	      
	      valueEnd = new JTextField("0");
	      valueEnd.setLocation(offsetX+190, upperGap + (upperGap+lowerGap+elementHeight)*9 );
	      valueEnd.setSize(60, 20);
	      valueEnd.setBackground(BlueBookVisual.getBackgroundColor());
	      valueEnd.setForeground(BlueBookVisual.getLabelColor());
	      valueEnd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				WriteInput.WRITE_SequenceFile();
			}
	    	  
	      });
	      masterPanel.add(valueEnd);
	      
		  endSelect = new JComboBox(elementEnd);
		  endSelect.setRenderer(new CustomRenderer());
		  endSelect.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*9);
		  endSelect.setSize(150,20);
		  endSelect.setSelectedIndex(0);
		  endSelect.setMaximumRowCount(20);
		  endSelect.setMaximumRowCount(20);
		  endSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int indx = getEndSelect().getSelectedIndex();
				if(indx==0 || indx==1) {
					relationLabel.setText(">");
				} else if(indx==2 || indx==3) {
					relationLabel.setText("<");
				}
				WriteInput.WRITE_SequenceFile();
			} 
	    	  
	      });
	      masterPanel.add(endSelect);
	      contentList.add(endSelect);
	      
	      numberOfEmptySpaces++;
	      
	      if(sequenceID==0) {
	      int labelx=40;
	      int labelLocationX = (int) (globalLeftGap + (2 * sidePanelWidth * sequenceID)/3 + sidePanelWidth /2 - labelx/2) ;
	      JLabel IDlabel = new JLabel(""+sequenceID);
	      IDlabel.setLocation(labelLocationX, 0 );
	      IDlabel.setSize(labelx, 20);
	      IDlabel.setBorder(marsBorder);
	      IDlabel.setHorizontalAlignment(JLabel.CENTER);
	      IDlabel.setBackground(BlueBookVisual.getBackgroundColor());
	      IDlabel.setForeground(BlueBookVisual.getLabelColor());
	      IDlabel.setFont(BlueBookVisual.getSmall_font());
	      SequencePanel.SequenceProgressBar.add(IDlabel);
	      SequencePanel.getSequenceProgressBarContent().add(IDlabel);
	      SequencePanel.SequenceProgressBar.revalidate();
	      SequencePanel.SequenceProgressBar.repaint();
	      }
	      //----------------------------------------------------------------------------------------------------
	      // 					 Resizing content Element 
	      //----------------------------------------------------------------------------------------------------
	      masterPanelHeight = upperGap + (upperGap+lowerGap+elementHeight)*(contentList.size()+numberOfEmptySpaces);
	     // System.out.println(masterPanelHeight);
			masterPanel.setSize(new Dimension(sidePanelWidth, masterPanelHeight));
		return masterPanel;
	}

	public static int getMasterPanelHeight() {
		return masterPanelHeight;
	}

	
	public static boolean isOdd(int input) {
		boolean result=false;
		if ( (input & 1) == 0 ) { result=false; } else { result=true; }
		return result;
	}
	
	public static void addGUISequenceElment() {
		   int newSequenceID = SequencePanel.getSequenceContentList().size();
	   int locationX=0;
	   int locationY=0;
	   if(isOdd(newSequenceID)) {
		   locationY = 400;
	   } else {
		   locationY=globalTopGap;
	   }
	   locationX = (int) (globalLeftGap + (2 * sidePanelWidth * newSequenceID)/3);
//System.out.println(isOdd(newSequenceID)+"|"+newSequenceID+"|"+locationX+" | "+locationY);
GUISequenceElement guiSequenceElement = new GUISequenceElement(newSequenceID);
SequencePanel.getSequenceContentList().add(guiSequenceElement);
SequencePanel.getSequenceContentList().get(newSequenceID).getMasterPanel().setLocation(locationX, locationY);
SequencePanel.SequenceLeftPanel.add(SequencePanel.getSequenceContentList().get(newSequenceID).getMasterPanel());
SequencePanel.SequenceLeftPanel.revalidate();
SequencePanel.SequenceLeftPanel.repaint();

int labelx=40;
int labelLocationX = (int) (globalLeftGap + (2 * sidePanelWidth * newSequenceID)/3 + sidePanelWidth /2 - labelx/2) ;
JLabel IDlabel = new JLabel(""+newSequenceID);
IDlabel.setLocation(labelLocationX, 0 );
IDlabel.setSize(labelx, 20);
IDlabel.setBorder(marsBorder);
IDlabel.setHorizontalAlignment(JLabel.CENTER);
IDlabel.setBackground(BlueBookVisual.getBackgroundColor());
IDlabel.setForeground(BlueBookVisual.getLabelColor());
IDlabel.setFont(SequencePanel.smallFont);
SequencePanel.SequenceProgressBar.add(IDlabel);
SequencePanel.getSequenceProgressBarContent().add(IDlabel);
SequencePanel.SequenceProgressBar.revalidate();
SequencePanel.SequenceProgressBar.repaint();
resizeCanvas();
WriteInput.WRITE_SequenceFile();
	}
	
	public static void resizeCanvas() {
	      if(SequencePanel.getSequenceContentList().size()>7) {
	    	  int newDimension = (int) (SequencePanel.sequenceDimensionWidth * SequencePanel.getSequenceContentList().size()/7);
	    	  SequencePanel.SequenceLeftPanel.setPreferredSize(new Dimension(newDimension, 850));
	    	  SequencePanel.SequenceProgressBar.setSize(newDimension, 20);
		      SequencePanel.SequenceLeftPanel.revalidate();
		      SequencePanel.SequenceLeftPanel.repaint();
		      SequencePanel.SequenceProgressBar.revalidate();
		      SequencePanel.SequenceProgressBar.repaint();
	      }
	}

	public void setSequenceID(int sequenceID) {
		this.sequenceID = sequenceID; 
	}
	
	public int getSequenceID() {
		return sequenceID;
	}

	public boolean isSelected() {
		return isSelected;
	}


	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public JPanel getMasterPanel() {
		return masterPanel;
	}

	public String[] getElementEvents() {
		return elementEvents;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox getEndSelect() {
		return endSelect;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox getFlightControllerSelect() {
		return flightControllerSelect;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox getEventSelect() {
		return eventSelect;
	}

	public JTextField getValueEnd() {
		return valueEnd;
	}

	public void setEventSelectIndx(int indx) {
		this.eventSelect.setSelectedIndex(indx);;
	}

	public void setEndSelectIndex(int indx) {
		this.endSelect.setSelectedIndex(indx);;
	}

	public void setFlightControllerSelectIndex(int index) {
		this.flightControllerSelect.setSelectedIndex(index);;
	}

	public void setValueEnd(String text) {
		this.valueEnd.setText(text);;
	}
	
	public String getSequenceName() {
		String st = SelectSequenceButton.getText();
		st = st.replaceAll("\\s+","");
		if(st.isEmpty()) {st="AutoSequence";}
		return st;	
	}
	
	public void setSequenceName(String name) {
		SelectSequenceButton.setText(name);
	}
	
    
	public static class CustomRenderer extends DefaultListCellRenderer {

		
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value,
		        int index, boolean isSelected, boolean cellHasFocus) {
		    super.getListCellRendererComponent(list, value, index, isSelected,
		            cellHasFocus);
		    setBackground(BlueBookVisual.getBackgroundColor());
		    setForeground(BlueBookVisual.getLabelColor());     
		    return this;
		}  
	}
	
}
