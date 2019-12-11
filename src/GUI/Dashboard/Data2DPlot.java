package GUI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.BlueBookVisual;
import GUI.DataStructures.InputFileSet;

public class Data2DPlot extends DashboardPlotPanel {
	//-------------------------------------------------------------------------------------------------------------
	
	private JPanel mainPanel;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color backgroundColor;
    
	 static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 

    //-------------------------------------------------------------------------------------------------------------
    // Class Values:
	 private static List<InputFileSet> analysisFile = new ArrayList<InputFileSet>();
	 private int ID=1;
	 private PlotElement plotElement;
	 
	 
	public Data2DPlot(List<InputFileSet> analysisFile) {
		
		backgroundColor = BlueBookVisual.getBackgroundColor();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		//mainPanel.setSize(400,400);
		mainPanel.setBackground(backgroundColor);
		
		try {
			createChart(analysisFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void createChart(List<InputFileSet> analysisFile) throws IOException {
		ChartSetting chartSetting = new ChartSetting();
		chartSetting.setX(0);
		chartSetting.setY(6);
		List<String> variableList = new ArrayList<String>();
		for(int i=0;i<BlueBookVisual.Axis_Option_NR.length;i++) {
			variableList.add(BlueBookVisual.Axis_Option_NR[i]);
		}
         plotElement = new PlotElement(0, variableList, analysisFile, chartSetting);
        JPanel plotElementPanel = plotElement.createPlotElement(plotElement);

	   mainPanel.add(plotElementPanel, BorderLayout.CENTER);

	}

	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	@Override
	public int getID() {
		return ID;
	}

	public PlotElement getPlotElement() {
		return plotElement;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Component Tester");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());

		try {
			analysisFile = BlueBookVisual.readResultFileList(System.getProperty("user.dir") + "/results.txt" );
		Data2DPlot dataplot = new Data2DPlot(analysisFile);
		frame.add(dataplot.getMainPanel(), BorderLayout.CENTER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
	}

}
