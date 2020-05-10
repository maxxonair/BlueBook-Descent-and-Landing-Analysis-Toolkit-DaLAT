package GUI.BlueBookPlot.serviceFunctions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import GUI.BlueBookPlot.DataStructures.InputFileSet;
import GUI.BlueBookPlot.main.BlueBookPlot;


public class ResultFileSetWindow {

	private static JFrame frame;
	private static String PROJECT_TITLE = "Result File Importer";
	private static int xInit = 300;
	private static int yInit = 300;
	
	static DefaultTableModel tableModel = new DefaultTableModel();
	static JTable table;
	
    static Object[] row = new Object[3];
	
	public static String[] columnIdentifier = 		{"Color",
													 "Name",
												     "Result File Path"};
	
    static Font small_font			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static Color bc_c = new Color(255,255,255);
    
	public JPanel createContentPane () throws IOException{
	    	JPanel MainGUI = new JPanel();
	    	MainGUI = new JPanel();
	    	MainGUI.setBackground(BlueBookPlot.getBackgroundColor());
	    	MainGUI.setLayout(new BorderLayout());
	    	
	    	JPanel ButtonPanel = new JPanel();
	    	ButtonPanel = new JPanel();
	    	ButtonPanel.setBackground(BlueBookPlot.getBackgroundColor());
	    	ButtonPanel.setLayout(new BorderLayout());
	    	MainGUI.add(ButtonPanel, BorderLayout.SOUTH);
	    	
	    	
	    	JLabel delimiterLabel = new JLabel("List of displayed resultfilesets:");
	    	delimiterLabel.setSize(200,30);
	    	delimiterLabel.setBackground(BlueBookPlot.getBackgroundColor());
	    	delimiterLabel.setForeground(BlueBookPlot.getLabelColor());
	    	MainGUI.add(delimiterLabel, BorderLayout.PAGE_START);
	    	
	    	// Insert Table to display active resultsets 
	    	 table = new JTable(){
			   	 
		
				private static final long serialVersionUID = 1L;

				@Override
		    	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		            Component comp = super.prepareRenderer(renderer, row, col);
		           // String val_TLFC = (String) getModel().getValueAt(row, 1);

						if(col==0) {
							comp.setBackground(BlueBookPlot.getInputFileSet().get(row).getDataColor());
						} else {
							comp.setBackground(BlueBookPlot.getBackgroundColor());
						}
						
		          
		            
		            return comp;
		        }

		    };
		   // table.setFont(table_font);
		    
		     tableModel = new DefaultTableModel(){

				private static final long serialVersionUID = 1L;

				@Override
		        public boolean isCellEditable(int row, int column) {
		           //all cells false
					if (column == 0 ){
						return false;
					} else {
						return true; 
					}
		        }
		    }; 
		    
		    tableModel.setColumnIdentifiers(columnIdentifier);
		    table.setModel(tableModel);
		    
	    	
		    table.setBackground(BlueBookPlot.getBackgroundColor());
		    table.setForeground(BlueBookPlot.getLabelColor());
		    table.getTableHeader().setReorderingAllowed(false);
		    table.setRowHeight(45);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(50);
			table.getColumnModel().getColumn(1).setPreferredWidth(70);
			table.getColumnModel().getColumn(2).setPreferredWidth(1200);
			
			table.addMouseListener(new MouseAdapter() {
				  public void mouseClicked(MouseEvent e) {
				    if (e.getClickCount() == 1) {
				      JTable target = (JTable)e.getSource();
				      int row = target.getSelectedRow();
				      int column = target.getSelectedColumn();
				      if(column==0) {
				    	  //System.out.println(""+row);
							ColorChooser colorChooser = new ColorChooser();
							colorChooser.getColorSelection(row);
				      }
				    }
				  }
				});
			

			    ((JTable) table).setFillsViewportHeight(true);
		    
			    table.getTableHeader().setBackground(BlueBookPlot.getBackgroundColor());
			    table.getTableHeader().setForeground(BlueBookPlot.getLabelColor());

			    
			    JScrollPane table_ScrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			    table_ScrollPane.getVerticalScrollBar().setBackground(BlueBookPlot.getBackgroundColor());
			    table_ScrollPane.getHorizontalScrollBar().setBackground(BlueBookPlot.getBackgroundColor());
			    table_ScrollPane.setSize(new Dimension(300,200));
			  
			    table_ScrollPane.setBackground(BlueBookPlot.getBackgroundColor());
			    MainGUI.add(table_ScrollPane, BorderLayout.CENTER);

			    UpdateTableFromResultFileList();
	    	// Add button 
	    	
	        JButton addButton = new JButton("add");
	        addButton.setSize(65,25);
	        addButton.setBackground(BlueBookPlot.getBackgroundColor());
	        addButton.setForeground(BlueBookPlot.getLabelColor());
	        addButton.setFont(BlueBookPlot.getSmallFont());
	        addButton.setOpaque(true);
	        addButton.setBorderPainted(false);
	        addButton.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) {
	        		  try {
						ImportSetupWindow.createAndShowGUI();
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        		  
	        	}} );
	        ButtonPanel.add(addButton, BorderLayout.WEST);
	    	
	    	// Delete Button 
	        
	    	JPanel deleteButtonPanel = new JPanel();
	    	deleteButtonPanel = new JPanel();
	    	deleteButtonPanel.setBackground(BlueBookPlot.getBackgroundColor());
	    	deleteButtonPanel.setLayout(new BorderLayout());
	    	ButtonPanel.add(deleteButtonPanel, BorderLayout.EAST);
	    	
	        JButton deleteButton = new JButton("delete");
	        deleteButton.setSize(65,25);
	        deleteButton.setBackground(BlueBookPlot.getBackgroundColor());
	        deleteButton.setForeground(BlueBookPlot.getLabelColor());
	        deleteButton.setFont(BlueBookPlot.getSmallFont());
	        deleteButton.setOpaque(true);
	        deleteButton.setBorderPainted(false);
	        deleteButton.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) {
	        		  DeleteSequence(); 
	        		  UpdateResultFileListFromTable();
	        		  BlueBookPlot.getPlotPanelManager().refresh(BlueBookPlot.getVariableList(), BlueBookPlot.getResultFilePath());
	        	}} );
	        deleteButtonPanel.add(deleteButton, BorderLayout.WEST);
	        
	        JButton deleteAllButton = new JButton("delete All");
	        deleteAllButton.setSize(65,25);
	        deleteAllButton.setBackground(BlueBookPlot.getBackgroundColor());
	        deleteAllButton.setForeground(BlueBookPlot.getLabelColor());
	        deleteAllButton.setFont(BlueBookPlot.getSmallFont());
	        deleteAllButton.setOpaque(true);
	        deleteAllButton.setBorderPainted(false);
	        deleteAllButton.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) {
	        		  DeleteAllSequence(); 
	        		  UpdateResultFileListFromTable();
	        		  BlueBookPlot.getPlotPanelManager().refresh(BlueBookPlot.getVariableList(), BlueBookPlot.getResultFilePath());
	        	}} );
	        deleteButtonPanel.add(deleteAllButton, BorderLayout.EAST);
	        
			return MainGUI;
	
	
	}
	
	public static void UpdateTableFromResultFileList() {
		List<InputFileSet> list = BlueBookPlot.getInputFileSet();
		for(int j=table.getRowCount()-1;j>=0;j--) {tableModel.removeRow(j);}
		
		for(int i=0;i<list.size();i++) {
			//row[0] = new ButtonElement(i).getButton();
			row[1] = list.get(i).getInputDataFileName() ;
	    		row[2] = list.get(i).getInputDataFilePath() ;
	    	tableModel.addRow(row);
		}
		
	}
	
    public static void DeleteSequence() {
    	int j = table.getSelectedRow();
    	if (j >= 0){tableModel.removeRow(j);}
    }
    
    public static void DeleteAllSequence() {
    	for(int i=tableModel.getRowCount()-1;i>=0;i--) {
    tableModel.removeRow(i);
    	}
    }
    
    public static void UpdateResultFileListFromTable() {
    	List<InputFileSet> list = BlueBookPlot.getInputFileSet();
    	for(int i=list.size()-1;i>=0;i--) {
    		list.remove(i);
    	}
    	for(int i=0;i<tableModel.getRowCount();i++) {
    		//Color color = tableModel.get
    		String name = (String) tableModel.getValueAt(i, 1);
    		String value = (String) tableModel.getValueAt(i, 2);
    		InputFileSet newInputFileSet = new InputFileSet();
    		//newInputFileSet.setDataColor(color);
    		newInputFileSet.setInputDataFileName(name);
    		newInputFileSet.setInputDataFilePath(value);
    		list.add(newInputFileSet);
    		}
    BlueBookPlot.setInputFileSet(list);
    }
    
	
	public static void createAndShowGUI() throws IOException {
		
        JFrame.setDefaultLookAndFeelDecorated(false);
        frame = new JFrame("" + PROJECT_TITLE);
        frame.setFont(small_font);
        ResultFileSetWindow demo = new ResultFileSetWindow();
        JPanel tp = demo.createContentPane();
       // tp.setPreferredSize(new java.awt.Dimension(xInit,yInit));
        frame.add(tp, BorderLayout.CENTER);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setMaximumSize(new java.awt.Dimension(1200, yInit));
        frame.setMinimumSize(new java.awt.Dimension(xInit, yInit));
        frame.setSize(new Dimension(650, yInit));
        //frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        frame.setVisible(true);
        // Create Icon image  -  top left for windows
    }
    
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException  e) {System.out.println(e);}
            }
        });
    }
    
}


