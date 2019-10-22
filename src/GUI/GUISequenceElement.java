package GUI;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import javafx.application.Platform;

public class GUISequenceElement {
	
	private static int masterPanelHeight=0;
	
	private static int sidePanelWidth = 300;
	private static int elementHeight = 20;
	private static int upperGap =2 ;
	private static int lowerGap=2;
	private static int offsetX=3;
	
	static List<Object> contentList = new ArrayList<Object>();
	static int numberOfEmptySpaces=0;
	static int ThissequenceID;
	
	public static JPanel getSequenceElement(int sequenceID) {

		ThissequenceID = sequenceID;
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(null);
		masterPanel.setBackground(BlueBookVisual.getBackgroundColor());
		//masterPanel.setBackground(Color.RED);
		masterPanel.setForeground(BlueBookVisual.getLabelColor());
		
		  JTextField SequenceName = new JTextField("AutoSequence"){
			    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				@Override public void setBorder(Border border) {
			        // No!
			    }
			};
	      SequenceName.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*0 );
	      SequenceName.setSize(250, 20);
	      SequenceName.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceName.setForeground(BlueBookVisual.getLabelColor());
	      SequenceName.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceName);
	      contentList.add(SequenceName);
	      
	        JButton AddSequenceElement = new JButton("plus");
	        AddSequenceElement.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*1);
	        AddSequenceElement.setSize(45,20);
	        AddSequenceElement.setBackground(BlueBookVisual.getBackgroundColor());
	        AddSequenceElement.setForeground(BlueBookVisual.getLabelColor());
	        AddSequenceElement.setOpaque(true);
	        AddSequenceElement.setBorderPainted(false);
	        AddSequenceElement.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) {
		                Platform.runLater(new Runnable() {
		                    @Override
		                    public void run() {
				      			   int height=0;
				      			   for(int i=0;i<BlueBookVisual.getSequenceList().size();i++) {
				      				   height+=BlueBookVisual.getSequenceList().get(0).getHeight();
				      			   }
		      			      BlueBookVisual.getSequenceList().add(GUISequenceElement.getSequenceElement(BlueBookVisual.getSequenceList().size()));
		      			      BlueBookVisual.getSequenceList().get(BlueBookVisual.getSequenceList().size()-1).setLocation(50*BlueBookVisual.getSequenceList().size(), height);
		    			      BlueBookVisual.SequenceLeftPanel.add(BlueBookVisual.getSequenceList().get(BlueBookVisual.getSequenceList().size()-1));
		                    }
		                    });
	        	}} );
	        masterPanel.add(AddSequenceElement);
	        
	        JButton DeleteSequenceElement = new JButton("minus");
	        DeleteSequenceElement.setLocation(offsetX+50, upperGap + (upperGap+lowerGap+elementHeight)*1);
	        DeleteSequenceElement.setSize(45,20);
	        DeleteSequenceElement.setBackground(BlueBookVisual.getBackgroundColor());
	        DeleteSequenceElement.setForeground(BlueBookVisual.getLabelColor());
	        DeleteSequenceElement.setOpaque(true);
	        DeleteSequenceElement.setBorderPainted(false);
	        DeleteSequenceElement.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) {
		                Platform.runLater(new Runnable() {
		                    @Override
		                    public void run() {
		                    	BlueBookVisual.SequenceLeftPanel.remove(BlueBookVisual.getSequenceList().get(ThissequenceID));
		                    }
		                    });
	        	}} );
	        masterPanel.add(DeleteSequenceElement);
		
	      JLabel SequenceID = new JLabel(""+sequenceID);
	      SequenceID.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*2 );
	      SequenceID.setSize(250, 20);
	      SequenceID.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceID.setForeground(BlueBookVisual.getLabelColor());
	      SequenceID.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceID);
	      contentList.add(SequenceID);
	      
	      
	      JLabel SequenceFCContent = new JLabel("Flight Controller:");
	      SequenceFCContent.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*3 );
	      SequenceFCContent.setSize(250, 20);
	      SequenceFCContent.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceFCContent.setForeground(BlueBookVisual.getLabelColor());
	      SequenceFCContent.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceFCContent);
	      contentList.add(SequenceFCContent);
	      
	      numberOfEmptySpaces++;
	      
	      JLabel SequenceEventContent = new JLabel("Events:");
	      SequenceEventContent.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*5 );
	      SequenceEventContent.setSize(250, 20);
	      SequenceEventContent.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceEventContent.setForeground(BlueBookVisual.getLabelColor());
	      SequenceEventContent.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceEventContent);
	      contentList.add(SequenceEventContent);
	      
	      numberOfEmptySpaces++;
	      
	      JLabel SequenceEndCondition = new JLabel("End Condition:");
	      SequenceEndCondition.setLocation(offsetX, upperGap + (upperGap+lowerGap+elementHeight)*7 );
	      SequenceEndCondition.setSize(250, 20);
	      SequenceEndCondition.setBackground(BlueBookVisual.getBackgroundColor());
	      SequenceEndCondition.setForeground(BlueBookVisual.getLabelColor());
	      SequenceEndCondition.setFont(BlueBookVisual.getSmall_font());
	      masterPanel.add(SequenceEndCondition);
	      contentList.add(SequenceEndCondition);
		
	      masterPanelHeight = upperGap + (upperGap+lowerGap+elementHeight)*(contentList.size()+numberOfEmptySpaces);
	     // System.out.println(masterPanelHeight);
			masterPanel.setSize(new Dimension(sidePanelWidth, masterPanelHeight));
		return masterPanel;
	}

	public static int getMasterPanelHeight() {
		return masterPanelHeight;
	}
	
	

}
