package GUI.Sequence;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import GUI.BlueBookVisual;
import utils.GuiReadInput;

public class SequencePanel {
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements
	private JPanel mainPanel;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
	private Color labelColor = Color.BLACK;
    
	public static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    static Font HeadlineFont          = new Font("Georgia", Font.LAYOUT_LEFT_TO_RIGHT, 12);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:

	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
    public static JPanel SequenceLeftPanel;
    public static JPanel SequenceProgressBar, FlexibleChartContentPanel, FlexibleChartContentPanel2;
    public static List<JLabel> sequenceProgressBarContent = new ArrayList<JLabel>();
 	public static List<GUISequenceElement> sequenceContentList = new ArrayList<GUISequenceElement>();
 	public static int sequenceDimensionWidth=1500;
    
    public SequencePanel() {
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		
		JPanel SequenceRightPanel = new JPanel();
		SequenceRightPanel.setLocation(0, 0);
		SequenceRightPanel.setBackground(backgroundColor);
		SequenceRightPanel.setForeground(labelColor); 
		SequenceRightPanel.setSize(400, 600);
		SequenceRightPanel.setLayout(null); 

	   
	    SequenceLeftPanel = new JPanel();
		SequenceLeftPanel.setLocation(0, 0);
		SequenceLeftPanel.setBackground(backgroundColor);
		SequenceLeftPanel.setForeground(labelColor);
		SequenceLeftPanel.setPreferredSize(new Dimension(sequenceDimensionWidth, 660));
		SequenceLeftPanel.setLayout(null); 
	    	
	  
	      JScrollPane ScrollPaneSequenceInput = new JScrollPane(SequenceLeftPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	    		  																	  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	      ScrollPaneSequenceInput.getVerticalScrollBar().setUnitIncrement(16);
	      ScrollPaneSequenceInput.getHorizontalScrollBar().setUnitIncrement(16);
	      mainPanel.add(ScrollPaneSequenceInput, BorderLayout.CENTER);

	      
		    SequenceProgressBar = new JPanel();
		    SequenceProgressBar.setLocation(0, 370);
		    SequenceProgressBar.setBackground(backgroundColor);
		    SequenceProgressBar.setForeground(labelColor);
		    SequenceProgressBar.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
		    SequenceProgressBar.setSize(sequenceDimensionWidth, 20);
		    SequenceProgressBar.setLayout(null); 
		    SequenceLeftPanel.add(SequenceProgressBar);
		    
		    JPanel SequenceControlBar = new JPanel();
		    SequenceControlBar.setLocation(5, 5);
		    SequenceControlBar.setBackground(backgroundColor);
		    SequenceControlBar.setForeground(labelColor);
		    SequenceControlBar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		    SequenceControlBar.setSize(500, 50);
		    SequenceControlBar.setLayout(null); 
		    SequenceLeftPanel.add(SequenceControlBar);
		    
		    JLabel SequenceTitle = new JLabel("Sequence Setup");
		    SequenceTitle.setLocation(2, 2);
		    SequenceTitle.setBackground(backgroundColor);
		    SequenceTitle.setForeground(labelColor);
		    SequenceTitle.setSize(150, 20);
		    SequenceControlBar.add(SequenceTitle);
		    
	        JButton SequenceToTheLeftButton = new JButton("");
	        SequenceToTheLeftButton.setLocation(5, 25);
	        SequenceToTheLeftButton.setSize(80,20);
	        SequenceToTheLeftButton.setForeground(BlueBookVisual.getBackgroundColor());
	        SequenceToTheLeftButton.setBackground(BlueBookVisual.getLabelColor());
	        SequenceToTheLeftButton.setOpaque(true);
	        SequenceToTheLeftButton.setBorderPainted(false);
	        SequenceToTheLeftButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
		
    				for(int i=0;i<sequenceContentList.size();i++) {
						if(sequenceContentList.get(i).isSelected()) {
							System.out.println("Sequence "+i+" selected.");
						}
						System.out.println(i+"|"+sequenceContentList.get(i).getSequenceID());
    				}
				} 
	        	
	        });
	        SequenceControlBar.add(SequenceToTheLeftButton);
		    
	      int globalLeftGap = 30;
	      int globalTopGap = 100;
	      GUISequenceElement sequenceElement1 = new GUISequenceElement(sequenceContentList.size(),"",0,0,0,0);
	      sequenceContentList.add(sequenceElement1);
	      sequenceContentList.get(0).getMasterPanel().setLocation(globalLeftGap, globalTopGap);
	      SequenceLeftPanel.add(sequenceContentList.get(0).getMasterPanel());
		
		
    }

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public static  List<JLabel> getSequenceProgressBarContent() {
		return sequenceProgressBarContent;
	}
	public static  List<GUISequenceElement> getSequenceContentList() {
		return sequenceContentList;
	}
	
	public static void resetSequenceContentList() {
		try {
			for(int i=sequenceContentList.size()-1;i>=0;i--) {
				SequenceLeftPanel.remove(sequenceContentList.get(i).getMasterPanel());
				sequenceContentList.remove(i);
			}
		} catch (Exception exp ) {
			
		}
	}
	
	/**
	 * Unit Tester 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("Component Tester");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());


		SequencePanel panel = new SequencePanel();
		panel.getMainPanel().setPreferredSize(new Dimension(1200,800));
		GuiReadInput.readSequenceFile();
		frame.add(panel.getMainPanel(), BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
	}
	
}
