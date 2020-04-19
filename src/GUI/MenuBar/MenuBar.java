package GUI.MenuBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;

import GUI.BlueBookVisual;
import GUI.FilePaths;
import GUI.Dashboard.DashboardPlotArea;
import GUI.GeometryModel.GeometryFrame;
import GUI.PostProcessing.CreateCustomChart;
import GUI.Settings.Settings;
import VisualEngine.engineLauncher.worldGenerator;
import utils.GuiReadInput;

public class MenuBar {
	//-------------------------------------------------------------------------------------------------------------
	
	private JMenuBar menuBar;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
    private Color labelColor;
    
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	   int OS_is=0;
	   private File CurrentWorkfilePath;
	   private String CurrentWorkfileName="";
	   private String caseFolder = System.getProperty("user.dir")  + "/CASES/";
	   private String inputFolder = System.getProperty("user.dir") + "/INP/";
	
	public MenuBar(){
		
		backgroundColor = BlueBookVisual.getBackgroundColor();
		labelColor = BlueBookVisual.getLabelColor();
		
		String dir = System.getProperty("user.dir");
		
   	 if(System.getProperty("os.name").contains("Mac")) {
		 OS_is = 1;
	 } else if(System.getProperty("os.name").contains("Win")) {
		 OS_is = 2;
	 } else if(System.getProperty("os.name").contains("Lin")) {
		 OS_is = 3;
	 }
		
     	ImageIcon icon_BlueBook = null;
     	ImageIcon icon_windowSelect = null;
     	ImageIcon icon_visualEngine = null;
     	ImageIcon icon_preProcessing =null;
     	ImageIcon icon_postProcessing =null;
     	ImageIcon icon_simulation =null;
     	int sizeUpperBar=20;
     	try {
		icon_BlueBook = new ImageIcon("images/homeIcon.png","");
		icon_windowSelect = new ImageIcon("images/windowSelect.png","");
		icon_visualEngine = new ImageIcon("images/visualEngine.png","");
		icon_preProcessing = new ImageIcon("images/preprocessingIcon.png","");
		icon_postProcessing = new ImageIcon("images/postprocessingIcon.png","");
		icon_simulation = new ImageIcon("images/simulationIcon.jpg","");
     	if(OS_is==1) {
        	 icon_BlueBook = new ImageIcon(getScaledImage(icon_BlueBook.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_windowSelect = new ImageIcon(getScaledImage(icon_windowSelect.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_visualEngine = new ImageIcon(getScaledImage(icon_visualEngine.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_simulation = new ImageIcon(getScaledImage(icon_simulation.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_preProcessing = new ImageIcon(getScaledImage(icon_preProcessing.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_postProcessing = new ImageIcon(getScaledImage(icon_postProcessing.getImage(),sizeUpperBar,sizeUpperBar));
     	} else if(OS_is==2) {
     	//	For Windows image icons have to be resized
        	 icon_BlueBook = new ImageIcon(getScaledImage(icon_BlueBook.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_windowSelect = new ImageIcon(getScaledImage(icon_windowSelect.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_visualEngine = new ImageIcon(getScaledImage(icon_visualEngine.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_simulation = new ImageIcon(getScaledImage(icon_simulation.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_preProcessing = new ImageIcon(getScaledImage(icon_preProcessing.getImage(),sizeUpperBar,sizeUpperBar));
        	 icon_postProcessing = new ImageIcon(getScaledImage(icon_postProcessing.getImage(),sizeUpperBar,sizeUpperBar));
     	}
     	} catch (Exception e) {
     		System.err.println("Error: Loading image icons failed");
     	}
		
     	
      	 menuBar = new JMenuBar();
        //menuBar.setLocation(0, 0);
       // menuBar.setColor(new Color(250,250,250));
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(1200, 25));
        
        //Build the first menu.
        JMenu mainMenu = new JMenu("BlueBook");
     	mainMenu.setOpaque(true);
     	mainMenu.setBackground(labelColor);
        mainMenu.setFont(smallFont);
        mainMenu.setMnemonic(KeyEvent.VK_A);
        mainMenu.setIcon(icon_BlueBook);
        menuBar.add(mainMenu);
        JMenuItem menuItem_OpenResultfile = new JMenuItem("Open Resultfile                 "); 
        menuItem_OpenResultfile.setForeground(Color.gray);
        menuItem_OpenResultfile.setFont(smallFont);
        menuItem_OpenResultfile.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        mainMenu.add(menuItem_OpenResultfile);
        menuItem_OpenResultfile.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                  		
                    } });
        mainMenu.addSeparator();
        JMenuItem menuItem_Import = new JMenuItem("Settings                "); 
        menuItem_Import.setForeground(labelColor);
        menuItem_Import.setFont(smallFont);
        menuItem_Import.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        mainMenu.add(menuItem_Import);
        menuItem_Import.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   Thread thread = new Thread(new Runnable() {
                 		    public void run() {
                 		    		// Create new window here 
                 		    try {
                 		    			Settings.main();
  							} catch (IOException e) {
  								System.err.println("Error: Loaden Real Time Simulation Setup Window Failed");
  								e.printStackTrace();
  							};
                 		    }
                 		});
                 		thread.start();
                    } });
        JMenuItem menuItem_Export = new JMenuItem("Results save as                "); 
        menuItem_Export.setForeground(labelColor);
        menuItem_Export.setFont(smallFont);
        menuItem_Export.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        mainMenu.add(menuItem_Export);
        menuItem_Export.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                     	File myfile;
  	        			myfile = new File(dir+"/RESULTS");
  		            	JFileChooser fileChooser = new JFileChooser(myfile);
	  		           	if (fileChooser.showSaveDialog(menuItem_Export) == JFileChooser.APPROVE_OPTION) {}
	  	                File file = fileChooser.getSelectedFile() ;
	  	                String filePath = file.getAbsolutePath();
	  	                filePath = filePath.replaceAll(BlueBookVisual.RESULT_FileEnding, "");
	  	                File source = new File(FilePaths.RES_File);
	  	                File dest = new File(filePath+BlueBookVisual.RESULT_FileEnding);
                	   try {
                	       FileUtils.copyFile(source, dest);
                	   } catch (IOException eIO) {System.out.println(eIO);}
                	   System.out.println("Result file "+file.getName()+" saved.");
                    } });
        mainMenu.addSeparator();
        JMenuItem menuItem_Exit = new JMenuItem("Exit                  "); 
        menuItem_Exit.setForeground(Color.BLACK);
        menuItem_Exit.setFont(smallFont);
        menuItem_Exit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        mainMenu.add(menuItem_Exit);
        menuItem_Exit.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   BlueBookVisual.MAIN_frame.dispose();
                    } });
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_SIM = new JMenu("Simulation");
        menu_SIM.setForeground(labelColor);
        menu_SIM.setBackground(backgroundColor);
        menu_SIM.setFont(smallFont);
        menu_SIM.setIcon(icon_simulation);
        menu_SIM.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_SIM);
        JMenuItem menuItem_SimSettings = new JMenuItem("Run Simulation                 "); 
        menuItem_SimSettings.setForeground(Color.BLACK);
        menuItem_SimSettings.setFont(smallFont);
        menuItem_SimSettings.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, ActionEvent.ALT_MASK));
        menu_SIM.add(menuItem_SimSettings);
        menuItem_SimSettings.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
             		  System.out.println("Action: RUN SIMULATION");
      				try {
      					String line;
      					Process proc = Runtime.getRuntime().exec("java -jar SIM.jar");
      					InputStream in = proc.getInputStream();
      					InputStream err = proc.getErrorStream();
      					System.out.println(in);
      					System.out.println(err);
      					 BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      					  while ((line = input.readLine()) != null) {
      					    System.out.println(line);
      					  }
      					  //UPDATE_Page01();
      				} catch ( IOException e1) {
      					// TODO Auto-generated catch block
      					e1.printStackTrace();
      					System.out.println("Error:  " + e1);
      				} 
                    } });
        JMenuItem menuItem_Update = new JMenuItem("Update Data                 "); 
        menuItem_Update.setForeground(Color.BLACK);
        menuItem_Update.setFont(smallFont);
        menuItem_Update.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_U, ActionEvent.ALT_MASK));
        menu_SIM.add(menuItem_Update);
        menuItem_Update.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   BlueBookVisual.update(true);
                    } });
        
       
        JMenuItem menuItem_Refresh = new JMenuItem("Reset Attitude                 "); 
        menuItem_Refresh.setForeground(Color.BLACK);
        menuItem_Refresh.setFont(smallFont);
        menu_SIM.add(menuItem_Refresh);
        menuItem_Refresh.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   DashboardPlotArea.updateDashboardPlotArea(DashboardPlotArea.getContentPanelList());
                    } });
        
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_PreProcessing = new JMenu("PreProcessing");
        menu_PreProcessing.setForeground(labelColor);
        menu_PreProcessing.setBackground(backgroundColor);
        menu_PreProcessing.setFont(smallFont);
        menu_PreProcessing.setIcon(icon_preProcessing);
        menu_PreProcessing.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_PreProcessing);
        
        JMenuItem menuItem_ImportScenario = new JMenuItem("Load Simulation from Cases               "); 
        menuItem_ImportScenario.setForeground(Color.black);
        menuItem_ImportScenario.setFont(smallFont);
        menuItem_ImportScenario.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_ImportScenario);
        menuItem_ImportScenario.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {

                	   loadFromCases();
   					//BlueBookVisual.Page04_subtabPane.setSelectedIndex(1);
                    } });
        menu_PreProcessing.addSeparator();
        JMenuItem menuItem_ExportScenario = new JMenuItem("Save As              "); 
        menuItem_ExportScenario.setForeground(Color.black);
        menuItem_ExportScenario.setFont(smallFont);
       // menuItem_ExportScenario.setAccelerator(KeyStroke.getKeyStroke(
       //         KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_ExportScenario);
        menuItem_ExportScenario.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {                	   
                	   	saveAs();
                    } });
        
        JMenuItem menuItem_ExportScenario2 = new JMenuItem("Save               "); 
        menuItem_ExportScenario2.setForeground(Color.black);
        menuItem_ExportScenario2.setFont(smallFont);
        menuItem_ExportScenario2.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_ExportScenario2);
        menuItem_ExportScenario2.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {                	   
                	   	save();
                    } });
        menu_PreProcessing.addSeparator();
        JMenuItem menuItem_Draw = new JMenuItem("Open Drawing Tool               "); 
        menuItem_Draw.setForeground(Color.black);
        menuItem_Draw.setFont(smallFont);
       // menuItem_Draw.setAccelerator(KeyStroke.getKeyStroke(
       //         KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_Draw);
        menuItem_Draw.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {                	   
		               		JFrame frame = new JFrame(BlueBookVisual.PROJECT_TITLE + " - Geometry model ");
			            		frame.setSize(1100,600);
			            		frame.setLayout(new BorderLayout());
			
			            		GeometryFrame window = new GeometryFrame();
			            		window.getMainPanel().setSize(500,500);
			            		frame.add(window.getMainPanel(), BorderLayout.CENTER);
			            		
			            		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		                    frame.setLocationRelativeTo(null);
		                    Point p = MouseInfo.getPointerInfo().getLocation() ;
		                    frame.setLocation(p);
		                    frame.setVisible(true);
                    } });
        //----------------
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_PostProcessing = new JMenu("PostProcessing");
        menu_PostProcessing.setForeground(labelColor);
        menu_PostProcessing.setBackground(backgroundColor);
        menu_PostProcessing.setFont(smallFont);
        menu_PostProcessing.setIcon(icon_postProcessing);
        menu_PostProcessing.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_PostProcessing);
        
        JMenuItem menuItem_CreateLocalElevation = new JMenuItem("Create Custom Data Plot               "); 
        menuItem_CreateLocalElevation.setForeground(Color.BLACK);
        menuItem_CreateLocalElevation.setFont(smallFont);
        menuItem_CreateLocalElevation.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PostProcessing.add(menuItem_CreateLocalElevation);
        menuItem_CreateLocalElevation.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                	   Thread thread = new Thread(new Runnable() {
               		    public void run() {
               		    		// Create new window here 
               		    try {
               		    	String[] args = {""};
               		    			CreateCustomChart.main(args);
							} catch (IOException e) {
								System.err.println("Error: Loaden Real Time Simulation Setup Window Failed");
								e.printStackTrace();
							};
               		    }
               		});
               		thread.start();
               		
                    } });
        JMenuItem menuItem_DataPlotter = new JMenuItem("Open BlueBook DataPlotter               "); 
        menuItem_DataPlotter.setForeground(Color.BLACK);
        menuItem_DataPlotter.setFont(smallFont);
        //menuItem_DataPlotter.setAccelerator(KeyStroke.getKeyStroke(
       //         KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PostProcessing.add(menuItem_DataPlotter);
        menuItem_DataPlotter.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                	   Thread thread = new Thread(new Runnable() {
               		    public void run() {
               		    	@SuppressWarnings("unused")
							Process proc = null;
               		    try {
               		    	proc = Runtime.getRuntime().exec("java -jar BlueBookPlot.jar");
							} catch (IOException e) {
								System.err.println("Error: Loaden Real Time Simulation Setup Window Failed");
								e.printStackTrace();
							};
               		    }
               		});
               		thread.start();
               		
                    } });
      //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_VisualEngine = new JMenu("Visual Engine");
        menu_VisualEngine.setForeground(labelColor);
        menu_VisualEngine.setBackground(backgroundColor);
        menu_VisualEngine.setFont(smallFont);
        menu_VisualEngine.setIcon(icon_visualEngine);
        menu_VisualEngine.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_VisualEngine);
        
        JMenuItem menuItem_Open = new JMenuItem("Open VisualEngine                 "); 
        menuItem_Open.setForeground(Color.gray);
        menuItem_Open.setFont(smallFont);
        menuItem_Open.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_VisualEngine.add(menuItem_Open);
        menuItem_Open.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	  /*
                	   Thread thread = new Thread(new Runnable() {
                		    public void run() {
                		    	 worldGenerator.launchVisualEngine();
                		    }
                		});
                		thread.start();
                		*/
                    } });
        
        JMenuItem menuItem_Animation = new JMenuItem("Create Animation from Results         "); 
        menuItem_Animation.setForeground(Color.gray);
        menuItem_Animation.setFont(smallFont);
        menuItem_Animation.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_VisualEngine.add(menuItem_Animation);
        menuItem_Animation.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	  /*
                	   Thread thread = new Thread(new Runnable() {
                		    public void run() {
                		    	List<AnimationSet> animationSets = READ_AnimationData();
                		    	worldAnimation.launchVisualEngine(animationSets);
                		    }
                		});
                		thread.start();
                		*/
                    } });
        
        JMenuItem menuItem_RealTime = new JMenuItem("Open Real Time Simulation Demo     "); 
        menuItem_RealTime.setForeground(Color.BLACK);
        menuItem_RealTime.setFont(smallFont);
        menuItem_RealTime.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_VisualEngine.add(menuItem_RealTime);
        menuItem_RealTime.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	  
                	   Thread thread = new Thread(new Runnable() {
                		    public void run() {
                		    		// Create new window here 
                  				try {
                  					String line;
                  					Process proc = null;
                  					if(OS_is==1) {
                  						 proc = Runtime.getRuntime().exec("java -jar FlyMeToTheMoon_OSX.jar");
                  					} else if (OS_is==2){
                  						 proc = Runtime.getRuntime().exec("java -jar FlyMeToTheMoon_WIN.jar");
                  					}
                  					InputStream in = proc.getInputStream();
                  					InputStream err = proc.getErrorStream();
                  					System.out.println(in);
                  					System.out.println(err);
                  					 BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                  					  while ((line = input.readLine()) != null) {
                  					    System.out.println(line);
                  					  }
                  					  //UPDATE_Page01();
                  				} catch ( IOException e1) {
                  					// TODO Auto-generated catch block
                  					e1.printStackTrace();
                  					System.out.println("Error:  " + e1);
                  				} 
                		    }
                		});
                		thread.start();
                    } });

        JMenu menuItem_AddSpacecraft = new JMenu("Add Spacecraft                ");
        menuItem_AddSpacecraft.setForeground(Color.gray);
        //menuItem_AddSpacecraft.setBackground(backgroundColor);
        menuItem_AddSpacecraft.setFont(smallFont);
        menuItem_AddSpacecraft.setMnemonic(KeyEvent.VK_A);
        menu_VisualEngine.add(menuItem_AddSpacecraft);
        ButtonGroup group_sc = new ButtonGroup();

        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem("Gemini");
        menuItem.setForeground(labelColor);
        menuItem.setFont(smallFont);
        menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   worldGenerator.addEntity("gemini", "gray");
                    } });
        group_sc.add(menuItem);
        menuItem_AddSpacecraft.add(menuItem);

        menuItem = new JRadioButtonMenuItem("Mars Global Surveyor");
        menuItem.setForeground(labelColor);
        menuItem.setFont(smallFont);
        menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   worldGenerator.addEntity("MGS", "gray");
                    } });
        group_sc.add(menuItem);
        menuItem_AddSpacecraft.add(menuItem);
        
        JMenu menuItem_setEnvironment = new JMenu("Set Environment               ");
        menuItem_setEnvironment.setForeground(Color.gray);
        //menuItem_setEnvironment.setBackground(backgroundColor);
        menuItem_setEnvironment.setFont(smallFont);
        menuItem_setEnvironment.setMnemonic(KeyEvent.VK_A);
        menu_VisualEngine.add(menuItem_setEnvironment);
        ButtonGroup group_env = new ButtonGroup();
        
        menuItem = new JRadioButtonMenuItem("Space");
        menuItem.setForeground(labelColor);
        menuItem.setFont(smallFont);
        menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   
                    } });
        group_env.add(menuItem);
        menuItem_setEnvironment.add(menuItem);

         menuItem = new JRadioButtonMenuItem("Earth");
         menuItem.setForeground(labelColor);
         menuItem.setFont(smallFont);
         menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                 	   worldGenerator.addEntity("MGS", "gray");
                     } });
         group_env.add(menuItem);
        
        menuItem_setEnvironment.add(menuItem);
         menuItem = new JRadioButtonMenuItem("Moon");
         menuItem.setForeground(labelColor);
         menuItem.setFont(smallFont);
         menuItem_AddSpacecraft.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                 	   worldGenerator.addEntity("MGS", "gray");
                     } });
         group_env.add(menuItem);
        menuItem_setEnvironment.add(menuItem);
   //-------------------------------------------------------------------------     
        JMenu menu_Window = new JMenu();
        menu_Window.setText("Window");
       // menu_Window.setForeground(labelColor);
        menu_Window.setBackground(backgroundColor);
        menu_Window.setForeground(Color.black);
       //menu_Window.setColor(labelColor);
        menu_Window.setFont(smallFont);
        menu_Window.setIcon(icon_windowSelect);
        menu_Window.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_Window);
        
        /**
         * 
         *  	Select window content
         * 
         */
        try {
	        for(int i=0;i<DashboardPlotArea.getContentPanelList().size();i++) {
		        	WindowContentChooser windowChooser = new WindowContentChooser(i);
		        	menu_Window.add(windowChooser.getMenuItem());
	        }
        } catch(Exception eww) {
        		System.err.println("Error: creating dashboard chart window select failed.");
        		System.err.println(eww);
        }
        //-----------------------------------------------------------------------
       JMenuItem menuItemSelect3D = new JMenuItem("Select 3D Spaceship File");
      // menuItemSelect3D.setForeground(labelColor);
        menuItemSelect3D.setFont(smallFont);
        menuItemSelect3D.setForeground(Color.black);
        //menuItemSelect3D.setSelected(true);
        menuItemSelect3D.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                   	// Refresh Object file path
                   	// refresh SpaceShipView3D
                   	// refresh SpaceShipView3dFrontPage
                   File myfile;
               		myfile = new File(System.getProperty("user.dir")+"/resourcs/models3D/");
                   	JFileChooser fileChooser = new JFileChooser(myfile);
                   //	fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.obj", "obj"));
                   //	fileChooser.setFileHidingEnabled(true);;
                   //	fileChooser.setFileFilter(new FileNameExtensionFilter("*.obj", "obj"));
                   	fileChooser.setFileFilter(new FileFilter() {

                   		   public String getDescription() {
                   		       return "Wavefront (*.obj)";
                   		   }

                   		   public boolean accept(File f) {
                   		       if (f.isDirectory()) {
                   		           return false;
                   		       } else {
                   		           String filename = f.getName().toLowerCase();
                   		           return filename.endsWith(".obj")  ;
                   		       }
                   		   }
                   		});
                  	if (fileChooser.showOpenDialog(menuItemSelect3D) == JFileChooser.APPROVE_OPTION) {
                  		
                  		File file = fileChooser.getSelectedFile() ;
                       String filePath = file.getAbsolutePath();
                       DashboardPlotArea.setModel3DFilePath(filePath);

                  	}
                   	       
                    } });
        menu_Window.add(menuItemSelect3D);
	}

	public JMenuBar getMainMenu() {
		return menuBar;
	}
	
    static Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    

	
	public class BackgroundMenuBar extends JMenuBar {
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

		private void loadFromCases() {
            Object[] possibilities = getCaseNames( caseFolder );
            String s = (String)JOptionPane.showInputDialog(
                                new JFrame(),
                                "Select Case to load:",
                                "BlueBook File System - Select existing case",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                possibilities,
                                "");

            //If a string was returned, say so.
            if ((s != null) && (s.length() > 0)) {
            		// Load File 
            		CurrentWorkfileName = s;
            		
	            	String source = caseFolder+"/"+CurrentWorkfileName+"/";
	            	String destination = inputFolder;

	            	copyFile(source ,  destination);
	            	
	            	updateBBFrameTitleAndReadInputFiles();
            }
		}
		
		private void saveAs() {
			
            String s = (String)JOptionPane.showInputDialog(
                    new JFrame(),
                    "Please enter a case name:",
                    "BlueBook File System - Save As",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null, "Enter Case Name");

				//If a string was returned, say so.
				if ((s != null) && (s.length() > 0)) {
					// Check if CurrentFileName exists
					if(fileExistsInFolder(s, caseFolder)) {
						// Dialog: Override?
	                    int n = JOptionPane.showConfirmDialog(
	                            new JFrame(), "This file does already exist. Do you wish to overwride the file?",
	                            "BlueBook File System - File Exists",
	                            JOptionPane.YES_NO_OPTION);
	                    if (n == JOptionPane.YES_OPTION) {
							CurrentWorkfileName =  s;
							updateBBFrameTitleAndReadInputFiles();
							String destination = caseFolder+"/"+CurrentWorkfileName+"/";
							copyFile(inputFolder, destination);
	                    } else if (n == JOptionPane.NO_OPTION) {
	                        // void 
	                    } else {
	                       // void 
	                    }
					} else {
						CurrentWorkfileName =  s;
						updateBBFrameTitleAndReadInputFiles();
						String destination = caseFolder+"/"+CurrentWorkfileName+"/";
						copyFile(inputFolder, destination);
					}
				}
			
		}
		
		private void copyFile(String source, String destination) {
			//String source = inputFolder;
			File srcDir = new File(source);

			//String destination = caseFolder+"/"+FileName+"/";
			File destDir = new File(destination);

			try {
			    FileUtils.copyDirectory(srcDir, destDir);
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		
		private boolean fileExistsInFolder(String fileName, String folder) {
			boolean exists= false;
			String[] directories = getCaseNames(folder);
			for(int i=0;i<directories.length;i++) {
				if(fileName.equals(directories[i])) {
					exists=true;
				}
			}
			return exists;
		}
		
		private String[] getCaseNames(String folder) {
			File file = new File(folder);
			String[] directories = file.list(new FilenameFilter() {
			  @Override
			  public boolean accept(File current, String name) {
			    return new File(current, name).isDirectory();
			  }
			});
			return directories;
		}
		
		private void save() {
			if(CurrentWorkfileName.length()==0) {
				// File Path not set 
				JOptionPane.showMessageDialog(new JFrame(""),
					    "No project setup yet. Please select an existing project or create a new one.", "BlueBook File System",
					    JOptionPane.WARNING_MESSAGE);
				// Message and link to save as 
				saveAs();
				
			} else {
				String destination = caseFolder+"/"+CurrentWorkfileName+"/";
				copyFile(inputFolder, destination);
			}			
		}

	public File getCurrentWorkfilePath() {
		return CurrentWorkfilePath;
	}

	public String getCurrentWorkfileName() {
		return CurrentWorkfileName;
	}
	
	private void updateBBFrameTitleAndReadInputFiles() {
		BlueBookVisual.MAIN_frame.setTitle(BlueBookVisual.PROJECT_TITLE + " - Scenario: " + CurrentWorkfileName);
		BlueBookVisual.update(true);
	      try {
			  GuiReadInput.readINP();	       
			  GuiReadInput.readSequenceFile();
	      } catch(Exception e) {
	    	  		System.out.println("ERROR: Reading input section after Case updated failed.");
	      }
	}
	/**
	 * 
	 * 
	 * Tester Unit
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Component Tester");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());

		MenuBar menu = new MenuBar();
		frame.add(menu.getMainMenu(), BorderLayout.NORTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
	}

}
