package GUI.PropulsionDraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import GUI.BlueBookVisual;
import GUI.PostProcessing.CreateCustomChart.BackgroundMenuBar;

public class PropulsionDrawEditor {
	
	
	static JFrame frame = new JFrame("Propulsion Block Draw Mk1");
	
	public static Canvas Canvas;
	private static ReadWrite readWrite;
	
	PartsCatalogue partsCatalogue;

	private  String ReadWriteFilePath   = System.getProperty("user.dir") + "/INP/PropulsionSystem.inp";
	
	static Font smallFont  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	
	public PropulsionDrawEditor() {
	    Canvas = new Canvas();
	    readWrite = new ReadWrite(ReadWriteFilePath, Canvas);
	    Canvas.linkReadWrite(readWrite);
	    partsCatalogue = new PartsCatalogue(readWrite);
	    Canvas.linkPartsCatalogue(partsCatalogue);
	    readWrite.linkPartsCatalogue(partsCatalogue);
		 readWrite.readFile();
	}
	
  public static void main(String[] args) {
   // JFrame frame = new JFrame("Propulsion Block Draw Mk1");

    // by doing this, we prevent Swing from resizing
    // our nice component
    frame.setLayout(new BorderLayout());
    
	BackgroundMenuBar menuBar = new BackgroundMenuBar();
    menuBar.setColor(new Color(250,250,250));
    menuBar.setOpaque(true);
    menuBar.setPreferredSize(new Dimension(1200, 25));
    frame.add(menuBar, BorderLayout.NORTH);

    JMenu menuMain = new JMenu("Editor");
    menuMain.setForeground(BlueBookVisual.getLabelColor());
    menuMain.setBackground(BlueBookVisual.getBackgroundColor());
    menuMain.setFont(smallFont);
    menuMain.setMnemonic(KeyEvent.VK_A);
    menuBar.add(menuMain);
    
    JMenuItem itemSave = new JMenuItem("Save as");  
    itemSave.setFont(smallFont);
    itemSave.setMnemonic(KeyEvent.VK_A);
    itemSave.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    });
    menuMain.add(itemSave);
    
    JMenuItem itemImport = new JMenuItem("Import");  
    itemImport.setFont(smallFont);
    itemImport.setMnemonic(KeyEvent.VK_A);
    itemImport.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    });
    menuMain.add(itemImport);
    menuMain.addSeparator();
    
    JMenuItem item = new JMenuItem("Delete All");  
    item.setFont(smallFont);
    item.setMnemonic(KeyEvent.VK_A);
    item.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			for(int i=Canvas.getCanvasElements().size()-1;i>=0;i--) {
				Canvas.remove(Canvas.getCanvasElements().get(i).getElement());
				Canvas.getCanvasElements().remove(i);
			}
			for(int i=Canvas.getRelationships().size()-1;i>=0;i--) {
				Canvas.getRelationships().remove(i);
			}
			readWrite.writeFile();
			Canvas.repaint();
		}
    	
    });
    menuMain.add(item);
    
    PropulsionDrawEditor propulsionDrawEditior = new PropulsionDrawEditor();
    JPanel panel = propulsionDrawEditior.getPropulsionDrawArea();
    
    //frame.setSize(900, 600);
    frame.add(panel, BorderLayout.CENTER);

    frame.pack();
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
    frame.addComponentListener(new ComponentAdapter() 
    {  
            public void componentResized(ComponentEvent evt) {
            	propulsionDrawEditior.getCanvas().resizeBackgroundImage();
            }
    });
    propulsionDrawEditior.getCanvas().resizeBackgroundImage();
  }
public static JFrame getFrame() {
	return frame;
}
public static void setFrame(JFrame frame) {
	PropulsionDrawEditor.frame = frame;
}

public JPanel getPropulsionDrawArea() {
	JPanel mainPanel = new JPanel();
	mainPanel.setSize(600, 800);
	mainPanel.setLayout(new BorderLayout());
	
	
    JSplitPane splitPaneVertical = new JSplitPane();
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
    splitPaneVertical.setDividerLocation(200); 
   // splitPaneVertical.setDividerLocation(0.1);
    mainPanel.add(splitPaneVertical, BorderLayout.CENTER);
    		
    
    JPanel OperatorPanel = new JPanel();
    //OperatorPanel.setSize(500, 200);
   // OperatorPanel.setLocation(0, 0);
    //OperatorPanel.setBackground(BlueBookVisual.getBackgroundColor());
    OperatorPanel.setLayout(new GridLayout(10,2));
    
    JScrollPane scrollPane = new JScrollPane(OperatorPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    //scrollPane.setPreferredSize(new Dimension(300,500));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    splitPaneVertical.add(scrollPane, JSplitPane.LEFT);
    
    
    	    int type=0;
    	    for(CatalogueElement element : partsCatalogue.getList()) {
    	    ButtonElement tankButton = getElement(element.getName(), element.getLogoFilePath(), type);
    	    OperatorPanel.add(tankButton);
    	    type++;
    	    }

    	    Canvas.setSize(new Dimension(500, 500));
    	    Canvas.setBorder(new LineBorder(Color.BLACK, 1));
    	    Canvas.setOpaque(false);
    	    Canvas.setLayout(null);
    	    splitPaneVertical.add(Canvas,  JSplitPane.RIGHT);
    	    
    	    mainPanel.addComponentListener(new ComponentAdapter() 
	    {  
	            public void componentResized(ComponentEvent evt) {
	            	Canvas.resizeBackgroundImage();
	            }
	    });
    	    
    	    
    	    return mainPanel;
}

public ButtonElement getElement(String elementName, String logoFilePath, int type ) {
	    ButtonElement buttonElement = new ButtonElement(logoFilePath);
	    buttonElement.setLocation(0, 0);
	    buttonElement.setSize(100,100);
	    buttonElement.addMouseListener(new MouseListener() {

	      @Override
	      public void mouseClicked(MouseEvent e) { 
	    	  

	      }

	      @Override
	      public void mousePressed(MouseEvent e) {
	    	  Canvas.addElement(readWrite,  type) ;
	    	  readWrite.writeFile();
	      }

	      @Override
	      public void mouseReleased(MouseEvent e) {
	    	  
	      }

	      @Override
	      public void mouseEntered(MouseEvent e) { }

	      @Override
	      public void mouseExited(MouseEvent e) { }
	      

	    });

	    return buttonElement;
}

public Canvas getCanvas() {
	return Canvas;
}



}