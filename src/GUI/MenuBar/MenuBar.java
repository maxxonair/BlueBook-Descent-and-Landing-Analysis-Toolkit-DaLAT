package GUI.MenuBar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;

import GUI.BlueBookVisual;
import GUI.Dashboard.DashboardPlotArea;
import GUI.PostProcessing.CreateCustomChart;
import GUI.Settings.Settings;
import VisualEngine.engineLauncher.worldGenerator;

public class MenuBar {
	//-------------------------------------------------------------------------------------------------------------
	
	private BackgroundMenuBar menuBar;
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
	   private File CurrentWorkfile_Path;
	   private String CurrentWorkfile_Name="";
	
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
		
     	
      	 menuBar = new BackgroundMenuBar();
        //menuBar.setLocation(0, 0);
        menuBar.setColor(new Color(250,250,250));
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
	  	                File source = new File(BlueBookVisual.RES_File);
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
                	   BlueBookVisual.UPDATE_Page01(true);
                    } });
        
       
        JMenuItem menuItem_Refresh = new JMenuItem("Refresh Attitude                 "); 
        menuItem_Refresh.setForeground(Color.BLACK);
        menuItem_Refresh.setFont(smallFont);
        menu_SIM.add(menuItem_Refresh);
        menuItem_Refresh.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   DashboardPlotArea.updateDashboardPlotArea(DashboardPlotArea.getContentPanelList());
                    } });
        
        menuItem_SimSettings = new JMenuItem("Run RealTime Module              "); 
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
      					Process proc = Runtime.getRuntime().exec("java -jar SIM2.jar");
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
        //--------------------------------------------------------------------------------------------------------------------------------
        JMenu menu_PreProcessing = new JMenu("PreProcessing");
        menu_PreProcessing.setForeground(labelColor);
        menu_PreProcessing.setBackground(backgroundColor);
        menu_PreProcessing.setFont(smallFont);
        menu_PreProcessing.setIcon(icon_preProcessing);
        menu_PreProcessing.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu_PreProcessing);
        
        JMenuItem menuItem_ImportScenario = new JMenuItem("Simulation Setup Open               "); 
        menuItem_ImportScenario.setForeground(labelColor);
        menuItem_ImportScenario.setFont(smallFont);
        menuItem_ImportScenario.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_ImportScenario);
        menuItem_ImportScenario.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                      	File myfile;
   	        			myfile = new File(dir+"/CASES");
   		            	JFileChooser fileChooser = new JFileChooser(myfile);
   		           	if (fileChooser.showOpenDialog(menuItem_Export) == JFileChooser.APPROVE_OPTION) {}
   	                File file = fileChooser.getSelectedFile() ;
   	                String filePath = file.getAbsolutePath();
   	                filePath = filePath.replaceAll(BlueBookVisual.CASE_FileEnding, "");
                       file = new File(filePath + BlueBookVisual.CASE_FileEnding);
                       CurrentWorkfile_Path = file;
                      CurrentWorkfile_Name = fileChooser.getSelectedFile().getName();
                       BlueBookVisual.MAIN_frame.setTitle("" + BlueBookVisual.PROJECT_TITLE + " | " +CurrentWorkfile_Name.split("[.]")[0]);
         
   					System.out.println("File "+CurrentWorkfile_Name+" opened.");

   					//BlueBookVisual.Page04_subtabPane.setSelectedIndex(1);
                    } });
        JMenuItem menuItem_ExportScenario = new JMenuItem("Simulation Setup Save as              "); 
        menuItem_ExportScenario.setForeground(labelColor);
        menuItem_ExportScenario.setFont(smallFont);
        menuItem_ExportScenario.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menu_PreProcessing.add(menuItem_ExportScenario);
        menuItem_ExportScenario.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {                	   
                   	File myfile;
	        			myfile = new File(dir+"/CASES");
		            	JFileChooser fileChooser = new JFileChooser(myfile);
		           	if (fileChooser.showSaveDialog(menuItem_Export) == JFileChooser.APPROVE_OPTION) {}
	                File file = fileChooser.getSelectedFile() ;
	                String filePath = file.getAbsolutePath();
	                filePath = filePath.replaceAll(BlueBookVisual.CASE_FileEnding, "");
                    file = new File(filePath + BlueBookVisual.CASE_FileEnding);
                    CurrentWorkfile_Path = file;
                   CurrentWorkfile_Name = fileChooser.getSelectedFile().getName();
                    BlueBookVisual.MAIN_frame.setTitle("" + BlueBookVisual.PROJECT_TITLE + " | " +CurrentWorkfile_Name.split("[.]")[0]);
						//EXPORT_Case();
                    } });
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
        //menu_Window.setBackground(backgroundColor);
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
        
        
        for(int i=0;i<DashboardPlotArea.getContentPanelList().size();i++) {
	        	WindowContentChooser windowChooser = new WindowContentChooser(i);
	        	menu_Window.add(windowChooser.getMenuItem());
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
               		myfile = new File(System.getProperty("user.dir")+"/INP/SpacecraftModelLibrary/");
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

	public BackgroundMenuBar getMainMenu() {
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



	public File getCurrentWorkfile_Path() {
		return CurrentWorkfile_Path;
	}

	public String getCurrentWorkfile_Name() {
		return CurrentWorkfile_Name;
	}
	
	

}
