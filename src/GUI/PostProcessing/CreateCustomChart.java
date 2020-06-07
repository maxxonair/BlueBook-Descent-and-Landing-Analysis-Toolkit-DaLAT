package GUI.PostProcessing;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

import com.apple.eawt.Application;

import GUI.BlueBookVisual;
import GUI.TableCellListener;


public class CreateCustomChart {

	private static JFrame MAIN_frame;
	private static String PROJECT_TITLE = BlueBookVisual.getPROJECT_TITLE()+ " Custom Data Plot";
	private static int xInit = 1200;
	private static int yInit = 750;
    public static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;
    
	static double deg2rad = PI/180.0; 		//Convert degrees to radians
	static double rad2deg = 180/PI; 		//Convert radians to degrees
	
    public static String RES_File    			= System.getProperty("user.dir") + "/results.txt"  ; 
	
    static Font small_font			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static Color bc_c = new Color(255,255,255);
    
    private static Crosshair xCrossHair;
    private static Crosshair yCrossHair;
	private static JFreeChart chartMain;
	private static XYSeriesCollection resultSet = new XYSeriesCollection();
	private static XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	private static ChartPanel chartPanel;
	static boolean chartMain_fd = true;
	private static JSplitPane splitPaneVertical;
	
	
	private static JTable tableContentSelect;
	private static DefaultTableModel modelContentSelect;
	public static String[] columnsContentSelect = 		{"ID",
														"Paramater",
														"Color"};
	@SuppressWarnings("rawtypes")
	public static JComboBox comboBoxContentSelect= new JComboBox(); 
	@SuppressWarnings("rawtypes")
	public static JComboBox comboBoxColorSelect= new JComboBox(); 
	public static String[] contentList = BlueBookVisual.getAxis_Option_NR();
	 static int c_contentSelect = 12;
	 static Object[] rowContentSelect = new Object[c_contentSelect];
    
	 
	 private static int xAxisContentIndx = 0;
	 
	 private static Color[] colorList = { Color.GRAY,
			 							  Color.RED,
			 							  Color.GREEN,
			 							  Color.BLUE,
			 							  Color.ORANGE,
			 							  Color.CYAN,
			 							  Color.YELLOW,
			 							  Color.WHITE
	 };
	 
	 private static String[] colorNames = { "gray",
			  "red",
			  "green",
			  "blue",
			  "orange",
			  "cyan",
			  "yellow",
			  "white"
};
	 
	 
	@SuppressWarnings("unchecked")
	public static JPanel createContentPane () throws IOException{
	    	JPanel MainGUI = new JPanel();
	    	MainGUI = new JPanel();
	    	MainGUI.setBackground(BlueBookVisual.getBackgroundColor());
	    	MainGUI.setLayout(new BorderLayout());
	    	
	    	BackgroundMenuBar menuBar = new BackgroundMenuBar();
	        menuBar.setColor(new Color(250,250,250));
	        menuBar.setOpaque(true);
	        menuBar.setPreferredSize(new Dimension(1200, 25));
	        MainGUI.add(menuBar, BorderLayout.NORTH);

	        JMenu menuMain = new JMenu("PostProcessing");
	        menuMain.setForeground(BlueBookVisual.getLabelColor());
	        menuMain.setBackground(BlueBookVisual.getBackgroundColor());
	        menuMain.setFont(small_font);
	        menuMain.setMnemonic(KeyEvent.VK_A);
	        menuBar.add(menuMain);
	        
	        JMenu menu_Xaxis = new JMenu("Set common X axis");
	        //menu_ThirdWindow.setForeground(labelColor);
	        //menu_ThirdWindow.setBackground(backgroundColor);
	        menu_Xaxis.setFont(small_font);
	        menu_Xaxis.setMnemonic(KeyEvent.VK_A);
	        menuMain.add(menu_Xaxis);
	        
	        ButtonGroup thirdWindow = new ButtonGroup();
	        
	        for(int i=0;i<contentList.length;i++) {
	        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(contentList[i]);
	       //menuItem.setForeground(labelColor);
	        final int indxIs = i;
	        menuItem.setFont(small_font);
	        menuItem.addActionListener(new ActionListener() {
	                   public void actionPerformed(ActionEvent e) {
	                		   xAxisContentIndx=indxIs;
	                		   UpdateChart();
	                    } });
	        thirdWindow.add(menuItem);
	        menu_Xaxis.add(menuItem);
	        }
	        
	        menuMain.addSeparator();
	        JMenuItem menuItem_Exit = new JMenuItem("Exit                  "); 
	        menuItem_Exit.setForeground(Color.BLACK);
	        menuItem_Exit.setFont(small_font);
	        menuItem_Exit.setAccelerator(KeyStroke.getKeyStroke(
	                KeyEvent.VK_S, ActionEvent.ALT_MASK));
	        menuMain.add(menuItem_Exit);
	        menuItem_Exit.addActionListener(new ActionListener() {
	                   public void actionPerformed(ActionEvent e) {
	                	   MAIN_frame.dispose();
	                    } });
	        
	        
	        splitPaneVertical = new JSplitPane();
	        splitPaneVertical.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
	        splitPaneVertical.setBackground(BlueBookVisual.getBackgroundColor());
	        splitPaneVertical.setDividerSize(3);
	        splitPaneVertical.setUI(new BasicSplitPaneUI() {
	               @SuppressWarnings("serial")
	   			public BasicSplitPaneDivider createDefaultDivider() {
	               return new BasicSplitPaneDivider(this) {
	                   @SuppressWarnings("unused")
	   				public void setBorder( Border b) {
	                   }
	                   @Override
	                       public void paint(Graphics g) {
	                       g.setColor(Color.gray);
	                       g.fillRect(0, 0, getSize().width, getSize().height);
	                           super.paint(g);
	                       }
	               };
	               }
	           });
	        splitPaneVertical.setDividerLocation(1200); 
	       // splitPaneVertical.setDividerLocation(0.1);
	        MainGUI.add(splitPaneVertical);
	        
	        CreatechartMain();
	        
	        JPanel dashboardPanel = new JPanel();
	        dashboardPanel.setBackground(BlueBookVisual.getBackgroundColor());
	        dashboardPanel.setLayout(new BorderLayout());
	        splitPaneVertical.add(dashboardPanel, JSplitPane.RIGHT);
	        
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.setBackground(BlueBookVisual.getBackgroundColor());
	        buttonPanel.setPreferredSize(new Dimension(600,30));
	        buttonPanel.setLayout(null);
	        dashboardPanel.add(buttonPanel, BorderLayout.PAGE_START);
	        
	        
	        JButton buttonAdd = new JButton("Add");
	        buttonAdd.setLocation(5 +55*0 ,  5 );
	        buttonAdd.setSize(45,20);
	        buttonAdd.setEnabled(true);
	        buttonAdd.setForeground(Color.BLACK);
	        buttonAdd.setBackground(BlueBookVisual.getBackgroundColor());
	        buttonAdd.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) { 
	        		  if(modelContentSelect.getRowCount()<8) {
			        		  rowContentSelect[1] = ""+contentList[0];
			        		  modelContentSelect.addRow(rowContentSelect); 
			        		  UpdateChart();
			        		  //System.out.println(modelContentSelect.getRowCount());
			        		  	for(int i=0;i<modelContentSelect.getRowCount();i++) {
				        	    		//modelContentSelect.setValueAt(""+i,i, 0);
				    		    		String select = (String) modelContentSelect.getValueAt(i, 2);
				    		    		//System.out.println(select);
				    		    		int indxIs = 0 ;
					    		    		for(int k=0;k<contentList.length;k++) {
					    		    			if(contentList[k].equals(select)) {indxIs=k;break;}
					    		    		}
				    		    		//System.out.println(indxIs);
				        	    		renderer.setSeriesPaint( i , colorList[indxIs]);
			        	    		}
	        		  }
	        	  } } );
	        buttonPanel.add(buttonAdd);
	        
	        JButton buttonDelete = new JButton("Delete");
	        buttonDelete.setLocation(5 +55*1 ,  5);
	        buttonDelete.setSize(45,20);
	        buttonDelete.setEnabled(true);
	        buttonDelete.setForeground(Color.BLACK);
	        buttonDelete.setBackground(BlueBookVisual.getBackgroundColor());
	        buttonDelete.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) { 
	        	    	int j = tableContentSelect.getSelectedRow();
	        	    	if (j >= 0){modelContentSelect.removeRow(j);}
	        	    	for(int i=0;i<modelContentSelect.getRowCount();i++) {
	        	    		modelContentSelect.setValueAt(""+i,i, 0);
	        	    		}
	        	    	UpdateChart();
	        	  
	        	  } } );
	        buttonPanel.add(buttonDelete);
	        
	        JButton buttonDeleteAll  = new JButton("Delete All");
	        buttonDeleteAll.setLocation(5 +55*2 ,  5);
	        buttonDeleteAll.setSize(75,20);
	        buttonDeleteAll.setEnabled(true);
	        buttonDeleteAll.setForeground(Color.BLACK);
	        buttonDeleteAll.setBackground(BlueBookVisual.getBackgroundColor());
	        buttonDeleteAll.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) { 
	        		for(int i=0;i<modelContentSelect.getRowCount();i++){modelContentSelect.removeRow(i);}
	        		for(int i=0;i<modelContentSelect.getRowCount();i++){modelContentSelect.removeRow(i);}
	        	    	UpdateChart();        	  
	        	  } } );
	        buttonPanel.add(buttonDeleteAll);
	        
	        //------------------------------------------------------------
	        tableContentSelect = new JTable(){
			   	 
		    	/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
		    	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		            Component comp = super.prepareRenderer(renderer, row, col);
		           // String val_TLFC = (String) getModel().getValueAt(row, 1);


		           // comp.setFont(table_font);
		            
		            return comp;
		        }
		    };
		   // TABLE_SEQUENCE.setFont(table_font);
		    
			Action action5 = new AbstractAction()
		    {
		        /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
		       {
					UpdateChart();
	        		  //System.out.println(modelContentSelect.getRowCount());
	        	    	for(int i=0;i<modelContentSelect.getRowCount();i++) {
	        	    		modelContentSelect.setValueAt(""+i,i, 0);
	    		    		String select = (String) modelContentSelect.getValueAt(i, 2);
	    		    		//System.out.println(select);
	    		    		int indxIs = 0 ;
	    		    		for(int k=0;k<contentList.length;k++) {
	    		    			if(colorNames[k].equals(select)) {indxIs=k;break;}
	    		    		}
	    		    		//System.out.println(indxIs);
	        	    		renderer.setSeriesPaint( i , colorList[indxIs]);
	        	    		}
		       }
		    };
		    @SuppressWarnings("unused")
			TableCellListener tcl5 = new TableCellListener(tableContentSelect, action5);
		    modelContentSelect = new DefaultTableModel(){

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
		    modelContentSelect.setColumnIdentifiers(columnsContentSelect);
		    tableContentSelect.setModel(modelContentSelect);
		    tableContentSelect.setBackground(BlueBookVisual.getBackgroundColor());
		    tableContentSelect.setForeground(BlueBookVisual.getLabelColor());
		    tableContentSelect.setOpaque(false);
		    tableContentSelect.getTableHeader().setReorderingAllowed(false);
		    tableContentSelect.setRowHeight(45);


			    TableColumn contentSelectID   		= tableContentSelect.getColumnModel().getColumn(0);
			    TableColumn contentSelectParameter 	= tableContentSelect.getColumnModel().getColumn(1);
			    TableColumn contentSelectColor  		= tableContentSelect.getColumnModel().getColumn(2);

			    
			    tableContentSelect.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			    contentSelectID.setPreferredWidth(20);
			    contentSelectParameter.setPreferredWidth(100);
			    contentSelectColor.setPreferredWidth(50);

			    
			    ((JTable) tableContentSelect).setFillsViewportHeight(true);
		    
			    tableContentSelect.getTableHeader().setBackground(BlueBookVisual.getBackgroundColor());
			    tableContentSelect.getTableHeader().setForeground(BlueBookVisual.getLabelColor());
	
			    comboBoxContentSelect.setBackground(BlueBookVisual.getBackgroundColor());
			    try {
			    for (int i=0;i<contentList.length;i++) {
			    	comboBoxContentSelect.addItem(contentList[i]);
			    }
			    } catch(NullPointerException eNPE) {
			    	System.out.println(eNPE);
			    }
			    comboBoxContentSelect.setRenderer(new CustomRenderer());
			    comboBoxContentSelect.setMaximumRowCount(20);
			    contentSelectParameter.setCellEditor(new DefaultCellEditor(comboBoxContentSelect));
			    
			    comboBoxColorSelect.setBackground(BlueBookVisual.getBackgroundColor());
			    try {
			    for (int i=0;i<colorNames.length;i++) {
			    	comboBoxColorSelect.addItem(colorNames[i]);
			    }
			    } catch(NullPointerException eNPE) {
			    	System.out.println(eNPE);
			    }
			    comboBoxColorSelect.setRenderer(new CustomRenderer());
			    comboBoxColorSelect.setMaximumRowCount(20);
			    contentSelectColor.setCellEditor(new DefaultCellEditor(comboBoxColorSelect));
			    
			    JScrollPane tableContentSelect_ScrollPane = new JScrollPane(tableContentSelect,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			    tableContentSelect_ScrollPane.getVerticalScrollBar().setBackground(BlueBookVisual.getBackgroundColor());
			    tableContentSelect_ScrollPane.getHorizontalScrollBar().setBackground(BlueBookVisual.getBackgroundColor());
			    tableContentSelect_ScrollPane.setBackground(BlueBookVisual.getBackgroundColor());
			    tableContentSelect_ScrollPane.getViewport().setBackground(BlueBookVisual.getBackgroundColor());
			    dashboardPanel.add(tableContentSelect_ScrollPane, BorderLayout.CENTER);
	        
	//-------------------------------------------------------------------------------        
			return MainGUI;
	}
	
	private static void createAndShowGUI() throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(false);
        MAIN_frame = new JFrame("" + PROJECT_TITLE);
        MAIN_frame.setFont(small_font);
        @SuppressWarnings("unused")
		CreateCustomChart demo = new CreateCustomChart();
        JPanel tp = CreateCustomChart.createContentPane();
        tp.setPreferredSize(new java.awt.Dimension(xInit,yInit));
        MAIN_frame.add(tp, BorderLayout.CENTER);
        MAIN_frame.pack();
        //MAIN_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MAIN_frame.setLocationRelativeTo(null);
        //MAIN_frame.setExtendedState(MAIN_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        MAIN_frame.setVisible(true);
        MAIN_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        MAIN_frame.setExtendedState(MAIN_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        //splitPaneVertical.setDividerLocation(1600); 
        // Create Icon image  -  top left for windows
         try {
        	BufferedImage myIcon = ImageIO.read(new File(BlueBookVisual.getICON_File())); 
        	MAIN_frame.setIconImage(myIcon);
         }catch(IIOException eIIO) {System.out.println(eIIO);}    
         // Create taskbar icon - for mac 
         try {
         Application application = Application.getApplication();
         Image image = Toolkit.getDefaultToolkit().getImage(BlueBookVisual.getICON_File());
         application.setDockIconImage(image);
         } catch(Exception e) {
        	 System.err.println("Taskbar icon could not be created");
         }
    }
	
	
	public static void CreatechartMain() throws IOException {
	    //-----------------------------------------------------------------------------------
	    chartMain = ChartFactory.createScatterPlot("", "", "", resultSet, PlotOrientation.VERTICAL, false, false, false); 
		XYPlot plot = (XYPlot)chartMain.getXYPlot(); 
		for(int i=0;i<20;i++) {
		    plot.setRenderer(i, renderer); 
		    renderer.setSeriesPaint( i , BlueBookVisual.getLabelColor());
		}
		chartMain.setBackgroundPaint(BlueBookVisual.getBackgroundColor());
		Font font3 = new Font("Dialog", Font.PLAIN, 12); 	
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelPaint(BlueBookVisual.getLabelColor());
		plot.getDomainAxis().setLabelPaint(BlueBookVisual.getLabelColor());
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(BlueBookVisual.getBackgroundColor());
		plot.setDomainGridlinePaint(BlueBookVisual.getLabelColor());
		plot.setRangeGridlinePaint(BlueBookVisual.getLabelColor()); 
		//chartMain.getLegend().setBackgroundPaint(BlueBookVisual.getBackgroundColor());
		//chartMain.getLegend().setItemPaint(BlueBookVisual.getLabelColor());
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		//domainAxis.setInverted(true);
		
		JPanel PlotPanel_X44 = new JPanel();
		PlotPanel_X44.setLayout(new BorderLayout());
		//PlotPanel_X44.setPreferredSize(new Dimension(900, page1_plot_y));
		PlotPanel_X44.setBackground(BlueBookVisual.getBackgroundColor());
		
		//Shape cross = ShapeUtilities.createDiagonalCross(1, 1) ;
	    double size = 2.0;
	    double delta = size / 2.0;
		Shape dot = new Ellipse2D.Double(-delta, -delta, size, size);
		for(int i=0;i<20;i++) {
			renderer.setSeriesShape(i, dot);	
		}

	
		chartPanel = new ChartPanel(chartMain);
		chartPanel.setMaximumDrawHeight(50000);
		chartPanel.setMaximumDrawWidth(50000);
		chartPanel.setMinimumDrawHeight(0);
		chartPanel.setMinimumDrawWidth(0);
		chartPanel.setMouseWheelEnabled(true);
	    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
	    xCrossHair = new Crosshair(Double.NaN, Color.RED, new BasicStroke(0f));
	    xCrossHair.setLabelVisible(true);
	    xCrossHair.setLabelPaint(BlueBookVisual.getLabelColor());
	    xCrossHair.setLabelBackgroundPaint(BlueBookVisual.getLabelColor());
	    yCrossHair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
	    yCrossHair.setLabelVisible(true);
	    yCrossHair.setLabelBackgroundPaint(BlueBookVisual.getLabelColor());
	    chartPanel.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
	            Rectangle2D dataArea = CreateCustomChart.chartPanel.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);
	            double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
	            CreateCustomChart.xCrossHair.setValue(x);
	            CreateCustomChart.yCrossHair.setValue(y);
	        }
	});
	    crosshairOverlay.addDomainCrosshair(xCrossHair);
	    crosshairOverlay.addRangeCrosshair(yCrossHair);
	    chartPanel.addOverlay(crosshairOverlay);
	   PlotPanel_X44.add(chartPanel,BorderLayout.PAGE_START);
	   splitPaneVertical.add(chartPanel, JSplitPane.LEFT);
		//jPanel4.validate();	
		chartMain_fd = false;
	}

	public static void UpdateChart(){
	    	resultSet.removeAllSeries();
	   // 	System.out.println("delete All");
	    	for(int j=tableContentSelect.getRowCount()-1;j>=0;j--) {
		    	try {
		    		String select = (String) modelContentSelect.getValueAt(j, 1);
		    		//System.out.println(select);
		    		int indxIs = 0 ;
		    		for(int k=0;k<contentList.length;k++) {
		    			if(contentList[k].equals(select)) {indxIs=k;break;}
		    		}
		    		//System.out.println(indxIs);
		    		resultSet.addSeries(addSeries(indxIs, select)); 
		    	
		    	} catch(ArrayIndexOutOfBoundsException | IOException eFNF2) {
		    	}
	    	}
    		chartMain.getXYPlot().getDomainAxis().setAttributedLabel(
    				String.valueOf(contentList[xAxisContentIndx]));
	}
	
	
	public static XYSeries addSeries(int indxY, String key) throws IOException , IIOException, FileNotFoundException, ArrayIndexOutOfBoundsException{
			  XYSeries xyseries10 = new XYSeries(""+key, false, true); 
        FileInputStream fstream = null;
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        try {
	                  while ((strLine = br.readLine()) != null )   {
				            String[] tokens = strLine.split(" ");
				            double xx=0; double yy=0; 
				            if(xAxisContentIndx==3) {
				             xx = Double.parseDouble(tokens[xAxisContentIndx]); } else {
				            	 String x_axis_label = String.valueOf(contentList[xAxisContentIndx]);
				            	 boolean isangle = x_axis_label.indexOf("[deg]") !=-1? true: false;
				            	 boolean isangle2 = x_axis_label.indexOf("[deg/s]") !=-1? true: false;
				            	 if(isangle || isangle2) {xx = Double.parseDouble(tokens[xAxisContentIndx])*rad2deg;} else {
				            		 		  xx = Double.parseDouble(tokens[xAxisContentIndx]);} 
				            	 }
				            if(indxY==3) {
				             yy = Double.parseDouble(tokens[indxY]);} else {
				            	 String x_axis_label = String.valueOf(contentList[indxY]);
				            	 boolean isangle = x_axis_label.indexOf("[deg]") !=-1? true: false;
				            	 boolean isangle2 = x_axis_label.indexOf("[deg/s]") !=-1? true: false;
				            	 if(isangle || isangle2) {yy = Double.parseDouble(tokens[indxY])*rad2deg;} else {
				             yy = Double.parseDouble(tokens[indxY]);	}
				             }
				         	xyseries10.add(xx , yy);
			           }
 in.close();
 br.close();
        } catch (NullPointerException eNPE) { 
      	 // System.out.println(eNPE);
      	  }
return xyseries10;
}
    
	
    public static void main(String[] agrs) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException  e) {System.out.println(e);}
            }
        });
    }
    
	public static class BackgroundMenuBar extends JMenuBar {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Color bgColor=Color.WHITE;

	    public void setColor(Color color) {
	        bgColor=color;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setColor(bgColor);
	        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

	    }


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
