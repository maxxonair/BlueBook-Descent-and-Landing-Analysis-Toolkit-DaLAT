package GUI.PropulsionDraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.apple.eawt.Application;

import GUI.BlueBookVisual;
import GUI.PostProcessing.CreateCustomChart.BackgroundMenuBar;

public class PropulsionDrawEditor {
	
	private static String frameTitle = "Propulsion Sub-system Editor -  Mk1";
	
	static JFrame frame = new JFrame(frameTitle);
	
	public static Canvas Canvas;
	private static ReadWrite readWrite;
	
	static PartsCatalogue partsCatalogue;
	static boolean isExit=true;

	private  String ReadWriteFilePath   = System.getProperty("user.dir") + "/INP/PropulsionSystem.piff";
	
	static Font smallFont  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	
	public PropulsionDrawEditor() {
	    Canvas = new Canvas();
	    readWrite = new ReadWrite(ReadWriteFilePath, Canvas);
	    Canvas.linkReadWrite(readWrite);
	    partsCatalogue = new PartsCatalogue(readWrite);
	    Canvas.linkPartsCatalogue(partsCatalogue);
	    readWrite.linkPartsCatalogue(partsCatalogue);
		 readWrite.readFile();
		 Canvas.getStatsPanel().updatePanel();
	}
	
  public static void main(String[] args) {
   // JFrame frame = new JFrame("Propulsion Block Draw Mk1");

    // by doing this, we prevent Swing from resizing
    // our nice component
    frame.setLayout(new BorderLayout());
    
    PropulsionDrawEditor propulsionDrawEditior = new PropulsionDrawEditor();
    JPanel panel = propulsionDrawEditior.getPropulsionDrawArea();
    frame.add(panel, BorderLayout.CENTER);
    
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
    
    JMenuItem itemNew = new JMenuItem("Create new");  
    itemNew.setFont(smallFont);
    itemNew.setMnemonic(KeyEvent.VK_A);
    itemNew.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
            File myfile;
        		myfile = new File(System.getProperty("user.dir")+"/INP/");
            	JFileChooser fileChooser = new JFileChooser(myfile);

           	if (fileChooser.showSaveDialog(itemNew) == JFileChooser.APPROVE_OPTION) {
           		File file = fileChooser.getSelectedFile() ;
           		try {
					file.createNewFile();
	           		String fileName = fileChooser.getSelectedFile().getName();
	           		fileName = fileName.replace(".piff","");
	           		frame.setTitle(frameTitle + " - Work file: "+fileName);
	           		
	                Canvas.getReadWrite().setReadWriteFilePath(fileChooser.getSelectedFile().getAbsolutePath());
	                
	                Canvas.deleteAllContent();
	                Canvas.getStatsPanel().updatePanel();
				} catch (IOException e) {
					System.out.println("Error: Create new file failed.");
				}
           		
           	}
			
		}
    	
    });
    menuMain.add(itemNew);
    
    JMenuItem itemSave = new JMenuItem("Save as");  
    itemSave.setFont(smallFont);
    itemSave.setMnemonic(KeyEvent.VK_A);
    itemSave.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
            File myfile;
        		myfile = new File(System.getProperty("user.dir")+"/INP/");
            	JFileChooser fileChooser = new JFileChooser(myfile);

           	if (fileChooser.showSaveDialog(itemSave) == JFileChooser.APPROVE_OPTION) {

           		File file = fileChooser.getSelectedFile() ;
                String filePath = file.getAbsolutePath();
                Path dst = Paths.get(filePath);;
                Path src = Paths.get(Canvas.getReadWrite().getReadWriteFilePath());

                try {
					Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					System.out.println("Error: Saving file failed. IOException.");
				}
           	}
			
		}
    	
    });
    menuMain.add(itemSave);
    
    JMenuItem itemImport = new JMenuItem("Select Work File");  
    itemImport.setFont(smallFont);
    itemImport.setMnemonic(KeyEvent.VK_A);
    itemImport.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
            File myfile;
        		myfile = new File(System.getProperty("user.dir")+"/INP/");
            	JFileChooser fileChooser = new JFileChooser(myfile);
            	fileChooser.setFileFilter(new FileFilter() {

            		
            		   public String getDescription() {
            		       return "Propulsion Input File Format (*.piff)";
            		   }

            		   public boolean accept(File f) {
            		       if (f.isDirectory()) {
            		           return false;
            		       } else {
            		           String filename = f.getName().toLowerCase();
            		           return filename.endsWith(".piff")  ;
            		       }
            		   }
            		});
           	if (fileChooser.showOpenDialog(itemImport) == JFileChooser.APPROVE_OPTION) {

           		String fileName = fileChooser.getSelectedFile().getName();
           		fileName = fileName.replace(".piff","");
           		frame.setTitle(frameTitle + " - Work file: "+fileName);
           		// Set new file path
                Canvas.getReadWrite().setReadWriteFilePath(fileChooser.getSelectedFile().getAbsolutePath());
                // Delete all existing content from canvas, read new file and import elements:
                Canvas.getReadWrite().readFile();
                Canvas.getStatsPanel().updatePanel();
                if(!Canvas.getReadWrite().isClearToWrite()) {
                	frame.setTitle(frameTitle + " - Work file: NO WORK FILE SET (Reading Error)");
                }

           	}
			
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
			Canvas.deleteAllContent();
			Canvas.getStatsPanel().updatePanel();
		}
    	
    });
    menuMain.add(item);
    

    frame.pack();
    if(isExit) { frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);}
    else { frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);}
    frame.setVisible(true);
    try {
   	BufferedImage myIcon = ImageIO.read(new File(partsCatalogue.getList().get(0).getLogoFilePath())); 
   	frame.setIconImage(myIcon);
    }catch( IOException eIIO) {System.out.println(eIIO);}    
    // Create taskbar icon - for mac 
   	 // Set Taskbar Icon for MacOS
    try {
    Application application = Application.getApplication();
    Image image = Toolkit.getDefaultToolkit().getImage(partsCatalogue.getList().get(0).getLogoFilePath());
    application.setDockIconImage(image);
    } catch(Exception e) {
   	 System.err.println("Taskbar icon could not be created");
    }
   
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
    
    JSplitPane splitPaneHorizontal = new JSplitPane();
    splitPaneHorizontal.setOrientation(JSplitPane.VERTICAL_SPLIT );
    splitPaneHorizontal.setBackground(BlueBookVisual.getBackgroundColor());
    splitPaneHorizontal.setDividerSize(3);
    splitPaneHorizontal.setUI(new BasicSplitPaneUI() {
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
    splitPaneHorizontal.setDividerLocation(700); 
   // splitPaneVertical.setDividerLocation(0.1);
    splitPaneVertical.add(splitPaneHorizontal, JSplitPane.LEFT);
    		
    
    JPanel OperatorPanel = new JPanel();
    //OperatorPanel.setSize(500, 200);
   // OperatorPanel.setLocation(0, 0);
    //OperatorPanel.setBackground(BlueBookVisual.getBackgroundColor());
    OperatorPanel.setLayout(new GridLayout(10,2));
    
    JScrollPane scrollPane = new JScrollPane(OperatorPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    //scrollPane.setPreferredSize(new Dimension(300,500));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    splitPaneHorizontal.add(scrollPane, JSplitPane.TOP);
    
    splitPaneHorizontal.add(Canvas.getStatsPanel().getPanel(), JSplitPane.BOTTOM);
    
    
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
	    	  Canvas.getStatsPanel().updatePanel();
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

public static void setExit(boolean isExit) {
	PropulsionDrawEditor.isExit = isExit;
}



}