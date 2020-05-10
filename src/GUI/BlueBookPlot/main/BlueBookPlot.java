package GUI.BlueBookPlot.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.BlueBookPlot.DataStructures.InputFileSet;
import GUI.BlueBookPlot.menu.FileMenu;
import GUI.BlueBookPlot.menu.SettingMenu;
import GUI.BlueBookPlot.menu.SidePanel;
import GUI.BlueBookPlot.serviceFunctions.FileWatcher;
import GUI.BlueBookPlot.serviceFunctions.BackgroundMenuBar;
import GUI.BlueBookPlot.serviceFunctions.PlotPanelManager;
import GUI.BlueBookPlot.serviceFunctions.ResultFileSetWindow;
import GUI.Dashboard.DashboardPlotPanel;



public class BlueBookPlot extends DashboardPlotPanel{
	
	private static boolean isAutoUpdate = true;

	static private String PROJECT_TITLE = "  BlueBook Plotting Toolkit - Mk1";
    static private Font smallFont	= new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    
    static private boolean isDarkTemplate = true;
   	
    private static Color labelColor =  new Color(220,220,220);    
   	private static Color backgroundColor = new Color(41,41,41);
   	
    private static List<InputFileSet> inputFileSet  = new ArrayList<InputFileSet>()  ;
    
    private static String resultFileListPath = System.getProperty("user.dir") + "/dataSetList" ;
    
    private static String variableListPath  = System.getProperty("user.dir") + "/variableList"  ; 
    
    private static String iconFilePath  = System.getProperty("user.dir") + "/resourcs/BlueBookPlot/icon.png"  ; 
    
    private static String resultFileDelimiter=" ";
    
	private static Timer timer = new Timer();
    
    private static int plotNumber=1;
    private static PlotPanelManager plotPanelManager;
    
    private JPanel mainPanel;
    
	protected int type;
	private int ID;

    private static List<String> variableList = new ArrayList<String>();
    
	public BlueBookPlot() {
        plotPanelManager = new PlotPanelManager(1);

        try {
			inputFileSet = readResultFileList(resultFileListPath);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.addHierarchyBoundsListener(new HierarchyBoundsListener(){

			@Override
			public void ancestorMoved(HierarchyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void ancestorResized(HierarchyEvent arg0) {
				boolean status = plotPanelManager.isTwoPlotTruncated();
				if( mainPanel.getSize().width < mainPanel.getSize().height && !status) {
					plotPanelManager.setTwoPlotTruncated(true);
					plotPanelManager.refresh(variableList, inputFileSet);	
				} else if(mainPanel.getSize().width > mainPanel.getSize().height && status) {
					plotPanelManager.setTwoPlotTruncated(false);
					plotPanelManager.refresh(variableList, inputFileSet);		
				}
			}           
        });
        //frame.setPreferredSize(new java.awt.Dimension(x_init, y_init));
        //------------------------------------------------------------------
        try {
        	variableList = readVariableList(System.getProperty("user.dir")+"/variableList");
		} catch (IOException e1) {
			System.err.println("ERROR: Variable list not recognized");
		}       
        
      	BackgroundMenuBar menuBar = new BackgroundMenuBar();
        menuBar.setColor(new Color(250,250,250));
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(1200, 25));
        mainPanel.add(menuBar, BorderLayout.NORTH);
               
        menuBar.add(FileMenu.create());
        
        menuBar.add(SettingMenu.create());
        
        mainPanel.add(SidePanel.create(), BorderLayout.WEST);
        
        JPanel plotPanel = plotPanelManager.createPlotPanel(variableList, inputFileSet);
        mainPanel.add(plotPanel, BorderLayout.CENTER);        
        
         // ---------------------------------------------------------------------------------
         //       Define Task (FileWatcher) Update Result Overview
    	 	// ---------------------------------------------------------------------------------
         if(isAutoUpdate) {
	         for(int i=0;i<inputFileSet.size();i++) {
			    	  try {
					FileWatcher task_Update = new FileWatcher( new File(inputFileSet.get(i).getInputDataFilePath()) ) {
			    		    protected void onChange( File file ) {
			    		    	plotPanelManager.refresh(variableList, inputFileSet);
			    		    }
			    		  };
			    	  
			     	  // repeat the check every second
			     	   timer.schedule( task_Update , new Date(), 1000 );
			    	  } catch (Exception e) {
			    		  System.err.println("ERROR: FileWatcher failed.");
			    	  }
	         }
         }
        //------------------------------------------------------------------
           setGUIColors(true);
           
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        //final URL imageResource = BlueBookPlot.getClassLoader().getResource("resources/images/icon.gif");
        final Image image = defaultToolkit.getImage(iconFilePath);

        //this is new since JDK 9
        final Taskbar taskbar = Taskbar.getTaskbar();

        try {
            //set icon for mac os (and other systems which do support this method)
            taskbar.setIconImage(image);
        } catch (final UnsupportedOperationException e) {
            System.out.println("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        } catch (Exception e) {
        	
        }

	}
	
	public static void setInputFileSet(List<InputFileSet> inputFileSet) {
		BlueBookPlot.inputFileSet = inputFileSet;
		writeResultFileList();
		updateTimer();
	}



	public static Color getLabelColor() {
		return labelColor;
	}

	public static Color getBackgroundColor() {
		return backgroundColor;
	}

	public static Font getSmallFont() {
		return smallFont;
	}

    
    public static void selectResultFile(Component itemSelectResult) {
        File myfile = new File(System.getProperty("user.dir"));
        	JFileChooser fileChooser = new JFileChooser(myfile);
        	fileChooser.setMultiSelectionEnabled(true);
       	if (fileChooser.showOpenDialog(itemSelectResult) == JFileChooser.APPROVE_OPTION) {
       		
       		//File file = fileChooser.getSelectedFile() ;
       		File[] files = fileChooser.getSelectedFiles();
       		for(int i=0;i<files.length;i++) {
       		String path = files[i].getAbsolutePath();
       		InputFileSet newInputFileSet = new InputFileSet();
       		newInputFileSet.setInputDataFilePath(path);
       		inputFileSet.add(newInputFileSet);
       		}
       		plotPanelManager.refresh(variableList, inputFileSet);
       		ResultFileSetWindow.UpdateTableFromResultFileList();      		
       	}
       	plotPanelManager.refresh(variableList, inputFileSet);
       	writeResultFileList();
       	updateTimer();
    }
    
    public static void selectVariableList(Component itemSelectVariableList) {
        File myfile = new File(System.getProperty("user.dir"));
        	JFileChooser fileChooser = new JFileChooser(myfile);
       	if (fileChooser.showOpenDialog(itemSelectVariableList) == JFileChooser.APPROVE_OPTION) {
       		
       		File file = fileChooser.getSelectedFile() ;
       		variableListPath = file.getAbsolutePath();
       		for(int i=variableList.size()-1;i>=0;i--) {
       			variableList.remove(i);
       		}
       		try {
				variableList = readVariableList(variableListPath);
	       		plotPanelManager.refresh(variableList, inputFileSet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	}
    }
    
    public static List<String> readVariableList(String filePath) throws IOException {
    	List<String> variableList = new ArrayList<String>();
      	 BufferedReader br = new BufferedReader(new FileReader(filePath));
       	 String strLine;
     try { 
    		      while ((strLine = br.readLine()) != null )   {
    		    	  String after = strLine.trim().replaceAll(" +", " ");
    		      	if(!after.isEmpty()) {
    		      	variableList.add(after);
    		      	}
    		      }
     }catch(NullPointerException eNPE) { System.out.println(eNPE);}
     br.close();
     if(variableList.size()==0) {
    	 System.err.println("ERROR: Variable List empty. Return 0");
     }
     return variableList;
    }
    
    public static List<InputFileSet> readResultFileList(String filePath) throws IOException{
    	List<InputFileSet> newInputFileSetList = new ArrayList<InputFileSet>();
    	try {
     	 BufferedReader br = new BufferedReader(new FileReader(filePath));
      	 String strLine;
    try { 
   		      while ((strLine = br.readLine()) != null )   {
   		    	  String after = strLine.trim().replaceAll(" +", " ");
   		      	if(!after.isEmpty()) {
   		      	InputFileSet newInputFileSet = new InputFileSet();
   		      	newInputFileSet.setInputDataFilePath(after);
   		     newInputFileSetList.add(newInputFileSet);
   		      	}
   		      }
    }catch(NullPointerException eNPE) { System.out.println(eNPE);}
    br.close();
    if(newInputFileSetList.size()==0) {
   	 System.err.println("ERROR: Variable List empty. Return 0");
    }
    	} catch (Exception e) {
    		
    	}
    return newInputFileSetList;
    }
    
    public static void updateTimer() {
	    	if(isAutoUpdate) {
		    	timer.cancel();
		    	timer.purge();
		        for(int i=0;i<inputFileSet.size();i++) {
				    	  try {
						FileWatcher task_Update = new FileWatcher( new File(inputFileSet.get(i).getInputDataFilePath()) ) {
				    		    protected void onChange( File file ) {
				    		    	plotPanelManager.refresh(variableList, inputFileSet);
				    		    }
				    		  };
				    	  
				     	  // repeat the check every second
				     	   timer.schedule( task_Update , new Date(), 1000 );
				    	  } catch (Exception e) {
				    		  System.err.println("ERROR: FileWatcher failed.");
				    		  System.out.println(e);
				    	  }
		        }
	    	}
    }
    
    public static void writeResultFileList() {
    	System.out.println("write res file list.");
        try {
            File fac = new File(resultFileListPath);
            if (!fac.exists())
            {
                fac.createNewFile();
            } else {
            	fac.delete();
            	fac.createNewFile();
            }
            FileWriter wr = new FileWriter(fac);

	            for(InputFileSet temp : inputFileSet) {
	            		wr.write(temp.getInputDataFilePath()+System.getProperty( "line.separator" ));
	            }
            wr.close();
            } catch (IOException eIO) {
            	System.out.println("ERROR: Result file list could not be created.");
            }
    }

    
    public static void setGUIColors(boolean value) {
    	isDarkTemplate = value;
        if(isDarkTemplate) {
            labelColor = new Color(220,220,220);    
            backgroundColor = new Color(41,41,41);
          } else {
            labelColor =  Color.BLACK;    
          	  backgroundColor = Color.white;
          }
        plotPanelManager.refresh(variableList, inputFileSet);
    }

	public static boolean isDarkTemplate() {
		return isDarkTemplate;
	}

	public static int getPlotNumber() {
		return plotNumber;
	}

	public static void setPlotNumber(int plotNumber) {
		BlueBookPlot.plotNumber = plotNumber;
	}

	public static PlotPanelManager getPlotPanelManager() {
		return plotPanelManager;
	}

	public static List<InputFileSet> getResultFilePath() {
		return inputFileSet;
	}

	public static List<String> getVariableList() {
		return variableList;
	}

	public static void setResultFileDelimiter(String resultFileDelimiter) {
		BlueBookPlot.resultFileDelimiter = resultFileDelimiter;
	}

	public static String getResultFileDelimiter() {
		return resultFileDelimiter;
	}
	
	public int getID() {
		return ID;
	}

	public int getType() {
		return type;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public static List<InputFileSet> getInputFileSet() {
		return inputFileSet;
	}
	
	/**
	 * 
	 * 
	 * Unit Tester 
	 * @param args
	 */
	public static void main(String[] args) {
		
        JFrame.setDefaultLookAndFeelDecorated(false);
        JFrame frame = new JFrame("" + PROJECT_TITLE);
        frame.setFont(smallFont);        
        
        BlueBookPlot blueBookPlot = new BlueBookPlot();	
		
		frame.add(blueBookPlot.getMainPanel(), BorderLayout.CENTER);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
	    
	    final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
	    final Image image = defaultToolkit.getImage(iconFilePath);
	    //set icon for windows os (and other systems which do support this method)
	    frame.setIconImage(image);
	    
	    frame.setVisible(true);
	    
	}
	
}

