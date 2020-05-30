package GUI.BlueBookPlot.serviceFunctions;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import GUI.BlueBookPlot.DataStructures.InputFileSet;
import GUI.BlueBookPlot.main.BlueBookPlot;

public class PlotPanelManager {
	
	private JPanel plotPanel;
	private int plotNumber;
	
	private boolean isTwoPlotTruncated=false;
	
	private List<Object> plotPanelContent = new ArrayList<Object>();
	
	private static List<ChartSetting> chartSettings = new ArrayList<ChartSetting>();
	private static List<PlotElement> plotElments = new ArrayList<PlotElement>();
	
	public PlotPanelManager(int plotNumber) {
		this.plotNumber = plotNumber;
	}
	

	public JPanel createPlotPanel(List<String> variableList, List<InputFileSet> analysisFile) {
		initializePlotColor(analysisFile);
		for(int i=0;i<4;i++) {
			ChartSetting chartSetting = new ChartSetting();
			chartSetting.setX(0);
			chartSetting.setY(4);
	        chartSettings.add(chartSetting);
		}
		
        plotPanel = new JPanel();
        plotPanel.setLocation(0, 0);
        plotPanel.setPreferredSize(new Dimension(400, 400));
        plotPanel.setLayout(new BorderLayout());
        plotPanel.setBackground(BlueBookPlot.getBackgroundColor());
        plotPanel.setForeground(BlueBookPlot.getLabelColor());
        
        plotPanel = arrangePlots(plotPanel, variableList, analysisFile, plotNumber);
       
		
		return plotPanel;
		
	}
	
	private JPanel arrangePlots(JPanel plotPanel, List<String> variableList, List<InputFileSet> analysisFile, int plotNumber) {
		
		for(int i=0;i<plotElments.size();i++) {
			try {
				plotElments.remove(i);
			} catch(ArrayIndexOutOfBoundsException e) {
				//System.err.println("ERROR: Failed to remove plot panel content. Array index out of bounds.");
			}
		}
	

		if(plotNumber==1) {
			ChartSetting chartSetting = new ChartSetting();
			chartSetting.setX(chartSettings.get(0).getX());
			chartSetting.setY(chartSettings.get(0).getY());
	        PlotElement plotElement = new PlotElement(0, variableList, analysisFile, chartSetting);
	        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
	        plotPanel.add(plotElementPanel);
	        plotPanelContent.add(plotElementPanel);
	        plotElments.add(plotElement);
		} else if(plotNumber==2) {
			JSplitPane splitPane;
			if(!isTwoPlotTruncated) {
				 splitPane = SplitPane.getSplitPane("horizontal");
			} else {
				 splitPane = SplitPane.getSplitPane("vertical");	
			}
		        for(int i=0;i<plotNumber;i++) {
		        	ChartSetting chartSetting = new ChartSetting();
		    		chartSetting.setX(chartSettings.get(i).getX());
		    		chartSetting.setY(chartSettings.get(i).getY());
		        	if(i==0) {
				        PlotElement plotElement = new PlotElement(i, variableList, analysisFile, chartSetting);
				        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
				        if(!isTwoPlotTruncated) {
				        	splitPane.add(plotElementPanel, JSplitPane.LEFT);
				        } else {
				        	splitPane.add(plotElementPanel, JSplitPane.TOP);
				        }
				        	plotElments.add(plotElement);
		        	} else if(i==1) {
				        PlotElement plotElement = new PlotElement(i, variableList, analysisFile, chartSetting);
				        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
				        if(!isTwoPlotTruncated) {
				        	splitPane.add(plotElementPanel, JSplitPane.RIGHT);
				        } else {
				        	splitPane.add(plotElementPanel, JSplitPane.BOTTOM);
				        }
				        	plotElments.add(plotElement);
		        	}
		        }
		        	splitPane.setOneTouchExpandable(true);
		        	if(!isTwoPlotTruncated) {
		        		splitPane.setDividerLocation(650);
		        	} else {
		        		splitPane.setDividerLocation(370);
		        	}
		        //	splitPane.setDividerLocation(0.5);
		        plotPanel.add(splitPane);
		        plotPanelContent.add(splitPane);
	        
		} else if(plotNumber==3) {
	        JSplitPane splitPane1 = SplitPane.getSplitPane("horizontal");
	        JSplitPane splitPane2 = SplitPane.getSplitPane("vertical");
	        splitPane2.add(splitPane1,JSplitPane.TOP);
	        for(int i=0;i<plotNumber;i++) {
	        	ChartSetting chartSetting = new ChartSetting();
	    		chartSetting.setX(chartSettings.get(i).getX());
	    		chartSetting.setY(chartSettings.get(i).getY());
	        	if(i==0) {
			        PlotElement plotElement = new PlotElement(i, variableList, analysisFile, chartSetting);
			        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
			        	splitPane1.add(plotElementPanel, JSplitPane.LEFT);
			        	plotElments.add(plotElement);
	        	} else if(i==1) {
			        PlotElement plotElement = new PlotElement(i, variableList, analysisFile, chartSetting);
			        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
			        	splitPane1.add(plotElementPanel, JSplitPane.RIGHT);
			        	plotElments.add(plotElement);
	        	} else if(i==2) {
			        PlotElement plotElement = new PlotElement(i, variableList, analysisFile, chartSetting);
			        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
			        	splitPane2.add(plotElementPanel, JSplitPane.BOTTOM);
			        	plotElments.add(plotElement);
	        	}
	        	
	        }
	        splitPane1.setDividerLocation(650);
	        splitPane2.setDividerLocation(370);
	        plotPanel.add(splitPane2);
	        plotPanelContent.add(splitPane2);
		} else if(plotNumber==4) {
	        JSplitPane splitPane1 = SplitPane.getSplitPane("horizontal");
	        JSplitPane splitPane2 = SplitPane.getSplitPane("vertical");
	        JSplitPane splitPane3 = SplitPane.getSplitPane("horizontal");
	        splitPane2.add(splitPane1,JSplitPane.TOP);
	        splitPane2.add(splitPane3,JSplitPane.BOTTOM);
	        for(int i=0;i<plotNumber;i++) {
	        	ChartSetting chartSetting = new ChartSetting();
	    		chartSetting.setX(chartSettings.get(i).getX());
	    		chartSetting.setY(chartSettings.get(i).getY());
	        	if(i==0) {
			        PlotElement plotElement = new PlotElement(i, variableList, analysisFile, chartSetting);
			        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
			        	splitPane1.add(plotElementPanel, JSplitPane.LEFT);
			        	plotElments.add(plotElement);
	        	} else if(i==1) {
			        PlotElement plotElement = new PlotElement(i, variableList, analysisFile, chartSetting);
			        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
			        	splitPane1.add(plotElementPanel, JSplitPane.RIGHT);
			        	plotElments.add(plotElement);
	        	} else if(i==2) {
			        PlotElement plotElement = new PlotElement(i, variableList, analysisFile, chartSetting);
			        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
			        	splitPane3.add(plotElementPanel, JSplitPane.LEFT);
			        	plotElments.add(plotElement);
	        	} else if (i==3) {
			        PlotElement plotElement = new PlotElement(i, variableList, analysisFile, chartSetting);
			        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);
			        	splitPane3.add(plotElementPanel, JSplitPane.RIGHT);
			        	plotElments.add(plotElement);
	        	}
	        }
	        splitPane1.setDividerLocation(650);
	        splitPane2.setDividerLocation(370);
	        splitPane3.setDividerLocation(650);
	        plotPanel.add(splitPane2);
	        plotPanelContent.add(splitPane2);
		} else {
			System.err.println("ERROR: Could not arrange Plots. Plot number is out of range.");
		}
		return plotPanel;
	}
	
	public void refresh(List<String> variableList, List<InputFileSet> analysisFile) {
		// Delete all content from Plot Panel 
		for(int i=0;i<plotPanelContent.size();i++) {
			try {
			plotPanel.remove(i);
			} catch(ArrayIndexOutOfBoundsException e) {
				//System.err.println("ERROR: Failed to remove plot panel content. Array index out of bounds.");
			}
		}
		//Add new content to empty canvas
		plotPanel = arrangePlots(plotPanel, variableList, analysisFile, plotNumber) ;
		// Re-validate Panel 
			plotPanel.revalidate();
			plotPanel.repaint();
	}
	

	public int getPlotNumber() {
		return plotNumber;
	}

	public void setPlotNumber(int plotNumber) {
		this.plotNumber = plotNumber;
	}

	public static List<ChartSetting> getChartSettings() {
		return chartSettings;
	}

	public static void setChartSettings(List<ChartSetting> chartSettings) {
		PlotPanelManager.chartSettings = chartSettings;
	}

	public static List<PlotElement> getPlotElments() {
		return plotElments;
	}

	public void setTwoPlotTruncated(boolean isTwoPlotTruncated) {
		this.isTwoPlotTruncated = isTwoPlotTruncated;
	}
	
	
	public List<InputFileSet> initializePlotColor(List<InputFileSet> analysisFile){
		for(int i=0;i<analysisFile.size();i++) {
			analysisFile.get(i).setDataColor(BlueBookPlot.getLabelColor());
		}
		return analysisFile;
	}

	public boolean isTwoPlotTruncated() {
		return isTwoPlotTruncated;
	}
	
	
}
