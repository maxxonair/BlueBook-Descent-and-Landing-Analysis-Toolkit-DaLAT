package GUI;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import javafx.application.Platform;

public class GUISequenceElement {
	
    private static int globalLeftGap = 30; // Change later and make dependent!!
    private static int globalTopGap = 100; // Change later and make dependent!!
	
	private static int masterPanelHeight=0;
	
	private static int sidePanelWidth = 303;
	private static int elementHeight = 20;
	private static int upperGap =2 ;
	private static int lowerGap=2;
	private static int offsetX=3;
	private boolean isSelected=false;
	
	static List<Object> contentList = new ArrayList<Object>();
	static int numberOfEmptySpaces=0;
	private int sequenceID;
	
    static Border moonBorder 	= BorderFactory.createLineBorder(Color.GRAY, 1);
    static Border marsBorder 	= BorderFactory.createLineBorder(Color.ORANGE, 1); 
    
    JPanel masterPanel; 
    
    public GUISequenceElement(int sequenceID) {
    	this.sequenceID=sequenceID; 
    	masterPanel = createSequencePanel();
    }
	
	public JPanel createSequencePanel() {
		masterPanelHeight=0;
		for(int i=0;i<contentList.size();i++) {
			contentList.remove(i);
		}
		numberOfEmptySpaces=0;
		
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
		
        JButton SelectSequenceButton = new JButton("");
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
		        				setSelected(true); //.e.BlueBookVisual.getSequenceContentList().get(sequenceID).setSelected(true);	
	        				} else {
		        				masterPanel.setBorder(moonBorder);
		        				masterPanel.revalidate();
		        				masterPanel.repaint();
		        				//BlueBookVisual.getSequenceContentList().get(sequenceID).setSelected(false);
		        				setSelected(false);
	        				}
	        				
	        				for(int i=0;i<BlueBookVisual.getSequenceContentList().size();i++) {
	        					if(i!=sequenceID) {
	        						BlueBookVisual.getSequenceContentList().get(i).getMasterPanel().setBorder(moonBorder);
	        						BlueBookVisual.getSequenceContentList().get(i).setSelected(false);
	        					}
	        				}

	                    }

	                    });
        	}} );
        masterPanel.add(SelectSequenceButton);


		
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
	      numberOfEmptySpaces++;
	      
	      
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
		                    		   int newSequenceID = BlueBookVisual.getSequenceContentList().size();
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
				          BlueBookVisual.getSequenceContentList().add(guiSequenceElement);
		      			  BlueBookVisual.getSequenceContentList().get(newSequenceID).getMasterPanel().setLocation(locationX, locationY);
		    			      BlueBookVisual.SequenceLeftPanel.add(BlueBookVisual.getSequenceContentList().get(newSequenceID).getMasterPanel());
		    			      BlueBookVisual.SequenceLeftPanel.revalidate();
		    			      BlueBookVisual.SequenceLeftPanel.repaint();
		    			      
		    			      int labelx=40;
		    			      int labelLocationX = (int) (globalLeftGap + (2 * sidePanelWidth * newSequenceID)/3 + sidePanelWidth /2 - labelx/2) ;
		    			      JLabel IDlabel = new JLabel(""+newSequenceID);
		    			      IDlabel.setLocation(labelLocationX, 0 );
		    			      IDlabel.setSize(labelx, 20);
		    			      IDlabel.setBorder(marsBorder);
		    			      IDlabel.setHorizontalAlignment(JLabel.CENTER);
		    			      IDlabel.setBackground(BlueBookVisual.getBackgroundColor());
		    			      IDlabel.setForeground(BlueBookVisual.getLabelColor());
		    			      IDlabel.setFont(BlueBookVisual.getSmall_font());
		    			      BlueBookVisual.SequenceProgressBar.add(IDlabel);
		    			      BlueBookVisual.getSequenceProgressBarContent().add(IDlabel);
		    			      BlueBookVisual.SequenceProgressBar.revalidate();
		    			      BlueBookVisual.SequenceProgressBar.repaint();
		    			      resizeCanvas();
		                    }
		                    });
	        	}} );
	        masterPanel.add(AddSequenceElement);
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
		                    	BlueBookVisual.SequenceLeftPanel.remove(BlueBookVisual.getSequenceContentList().get(sequenceID).getMasterPanel());
			    			    BlueBookVisual.getSequenceContentList().remove(sequenceID);
		                    	BlueBookVisual.SequenceLeftPanel.revalidate();
			    			      BlueBookVisual.SequenceLeftPanel.repaint();
			    			      // Remove respective progress bar entry 
			    			      BlueBookVisual.SequenceProgressBar.remove(BlueBookVisual.getSequenceProgressBarContent().get(sequenceID));
			    			      BlueBookVisual.getSequenceProgressBarContent().remove(sequenceID);
			    			      BlueBookVisual.SequenceProgressBar.revalidate();
			    			      BlueBookVisual.SequenceProgressBar.repaint();
			    			      //-----------------------------------------------------------------
			    			      // Revalidate :
			    			      for(int i=0;i<BlueBookVisual.getSequenceContentList().size();i++) {
						      			 BlueBookVisual.getSequenceContentList().get(i).setSequenceID(i);
						      			// SequenceID.setText(""+BlueBookVisual.getSequenceContentList().get(i).getSequenceID());
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

				      			  BlueBookVisual.getSequenceContentList().get(i).getMasterPanel().setLocation(locationX, locationY);
				      			  BlueBookVisual.getSequenceContentList().get(i).setSequenceID(i);
			                    	  BlueBookVisual.SequenceLeftPanel.revalidate();
				    			      BlueBookVisual.SequenceLeftPanel.repaint();
				    			      int labelx=40;
				    			      int labelLocationX = (int) (globalLeftGap + (2 * sidePanelWidth * i)/3 + sidePanelWidth /2 - labelx/2) ;
				    			      
				    			      BlueBookVisual.getSequenceProgressBarContent().get(i).setLocation(labelLocationX, 0);
				    			      BlueBookVisual.getSequenceProgressBarContent().get(i).setText(""+i);
				    			      BlueBookVisual.SequenceProgressBar.revalidate();
				    			      BlueBookVisual.SequenceProgressBar.repaint();
				    			      resizeCanvas();
			    			      }
		                    	}
		                    }
		                    });
	        	}} );
	        masterPanel.add(DeleteSequenceElement);
	        }
	      
	      JLabel SequenceFCContent = new JLabel("Flight Controller:");
	      SequenceFCContent.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*4 );
	      SequenceFCContent.setSize(250, 20);
	      SequenceFCContent.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceFCContent.setForeground(BlueBookVisual.getLabelColor());
	      SequenceFCContent.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceFCContent);
	      contentList.add(SequenceFCContent);
	      
	      numberOfEmptySpaces++;
	      
	      JLabel SequenceEventContent = new JLabel("Events:");
	      SequenceEventContent.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*6 );
	      SequenceEventContent.setSize(250, 20);
	      SequenceEventContent.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceEventContent.setForeground(BlueBookVisual.getLabelColor());
	      SequenceEventContent.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceEventContent);
	      contentList.add(SequenceEventContent);
	      
	      numberOfEmptySpaces++;
	      
	      JLabel SequenceEndCondition = new JLabel("End Condition:");
	      SequenceEndCondition.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*8 );
	      SequenceEndCondition.setSize(250, 20);
	      SequenceEndCondition.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceEndCondition.setForeground(BlueBookVisual.getLabelColor());
	      SequenceEndCondition.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceEndCondition);
	      contentList.add(SequenceEndCondition);
	      
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
	      BlueBookVisual.SequenceProgressBar.add(IDlabel);
	      BlueBookVisual.getSequenceProgressBarContent().add(IDlabel);
	      BlueBookVisual.SequenceProgressBar.revalidate();
	      BlueBookVisual.SequenceProgressBar.repaint();
	      }
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
	
	public void resizeCanvas() {
	      if(BlueBookVisual.getSequenceContentList().size()>7) {
	    	  int newDimension = (int) (BlueBookVisual.sequenceDimensionWidth * BlueBookVisual.getSequenceContentList().size()/7);
	    	  BlueBookVisual.SequenceLeftPanel.setPreferredSize(new Dimension(newDimension, 850));
	    	  BlueBookVisual.SequenceProgressBar.setSize(newDimension, 20);
		      BlueBookVisual.SequenceLeftPanel.revalidate();
		      BlueBookVisual.SequenceLeftPanel.repaint();
		      BlueBookVisual.SequenceProgressBar.revalidate();
		      BlueBookVisual.SequenceProgressBar.repaint();
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
	
}
