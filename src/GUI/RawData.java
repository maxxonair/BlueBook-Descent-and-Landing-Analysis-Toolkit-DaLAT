package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Simulator_main.DataSets.RealTimeResultSet;
import utils.EulerAngle;
import utils.GuiReadInput;


public class RawData {
	//-------------------------------------------------------------------------------------------------------------
	// Private GUI Elements
	private JPanel mainPanel;
    private DefaultTableModel MODEL_RAWData;
    private JTable TABLE_RAWData; 
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
    @SuppressWarnings("unused")
	private Color labelColor;
    
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	Font labelfont_small       = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 9);

    DecimalFormat df_X4 		  = new DecimalFormat("#####.###");
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:	  
	//-------------------------------------------------------------------------------------------------------------
    // Panel Strings:

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
    
    public RawData() {
    		backgroundColor = BlueBookVisual.getBackgroundColor();
    		labelColor = BlueBookVisual.getLabelColor();
    		
    		mainPanel = new JPanel();
    		mainPanel.setLayout(new BorderLayout());
    		
    	       
    	    TABLE_RAWData = new JTable();	    
    	    MODEL_RAWData = new DefaultTableModel(){

    			private static final long serialVersionUID = 1L;

    			@Override
    	        public boolean isCellEditable(int row, int column) {
    	           //all cells false
    					return false;
    	        }
    	    }; 
    	    MODEL_RAWData.setColumnIdentifiers(BlueBookVisual.Axis_Option_NR);
    	    TABLE_RAWData.setModel(MODEL_RAWData);
    	    TABLE_RAWData.getTableHeader().setForeground(labelColor);
    	    TABLE_RAWData.setBackground(backgroundColor);
    	    TABLE_RAWData.setForeground(labelColor);
    	    TABLE_RAWData.getTableHeader().setReorderingAllowed(false);
    	    TABLE_RAWData.setRowHeight(18);
    		TABLE_RAWData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    		((JTable) TABLE_RAWData).setFillsViewportHeight(true);
    		TABLE_RAWData.getTableHeader().setBackground(backgroundColor);
    		TABLE_RAWData.getTableHeader().setForeground(labelColor);

    		    JScrollPane TABLE_RAWData_ScrollPane = new JScrollPane(TABLE_RAWData,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    		    TABLE_RAWData_ScrollPane.getVerticalScrollBar().setBackground(backgroundColor);
    		    TABLE_RAWData_ScrollPane.getHorizontalScrollBar().setBackground(backgroundColor);
    		    TABLE_RAWData_ScrollPane.setBackground(backgroundColor);
    		    mainPanel.add(TABLE_RAWData_ScrollPane);
           
           
           
    	
    }
    public void readRawData() {
    BlueBookVisual.resultSet.clear();
    try {
		BlueBookVisual.analysisFile = 	GuiReadInput.readResultFileList(FilePaths.RES_File);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    	// Delete all exisiting rows:
    	for(int j=MODEL_RAWData.getRowCount()-1;j>=0;j--) {MODEL_RAWData.removeRow(j);}
    	// Read all data from file: 
	    FileInputStream fstream = null;
		try{ fstream = new FileInputStream(FilePaths.RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	              DataInputStream in = new DataInputStream(fstream);
	              BufferedReader br = new BufferedReader(new InputStreamReader(in));
	              String strLine;
	              try {
							while ((strLine = br.readLine()) != null )   {
								Object[] tokens = strLine.split(" ");
							    MODEL_RAWData.addRow(tokens);
						     	RealTimeResultSet resultElement = new RealTimeResultSet();
							    double[] CartesianPosition = {Double.parseDouble((String) tokens[41]),
			 							   						Double.parseDouble((String) tokens[42]),
			 							   						Double.parseDouble((String) tokens[43])};
							    resultElement.setCartesianPosECEF(CartesianPosition);
							    EulerAngle intEul = new EulerAngle();
							    intEul.roll = Double.parseDouble((String) tokens[57]);
							    intEul.pitch = Double.parseDouble((String) tokens[58]);
							    intEul.yaw = Double.parseDouble((String) tokens[59]);
							    resultElement.setEulerAngle(intEul);
							    resultElement.setVelocity(Double.parseDouble((String) tokens[6]) );
							    resultElement.setTime(Double.parseDouble((String) tokens[0]));
							    resultElement.setFpa(Double.parseDouble((String) tokens[7]));
							    BlueBookVisual.resultSet.add(resultElement);
							  
							  }
			       fstream.close();
			       in.close();
			       br.close();

	              } catch (NullPointerException | IOException eNPE) { 
	            	  System.out.println("Read raw data, Nullpointerexception");
					}catch(IllegalArgumentException eIAE) {
					  System.out.println("Read raw data, illegal argument error");
					}
    }

	public JPanel getMainPanel() {
		return mainPanel;
	}
    
    public static void main(String[] args) {
	    JFrame frame = new JFrame("Center Panel Right Setting Test ");
	    frame.setLayout(new BorderLayout());
	    
		RawData data = new RawData();
		data.readRawData();
		
		frame.add(data.getMainPanel(), BorderLayout.CENTER);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
    }
}
