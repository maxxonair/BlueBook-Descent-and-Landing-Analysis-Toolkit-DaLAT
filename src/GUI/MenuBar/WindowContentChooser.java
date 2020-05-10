package GUI.MenuBar;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import GUI.BlueBookPlot.main.BlueBookPlot;
import GUI.Dashboard.AttitudeView;
import GUI.Dashboard.ChartSetting;
import GUI.Dashboard.DashboardPlotArea;
import GUI.Dashboard.DashboardPlotPanel;
import GUI.Dashboard.Data2DPlot;
import GUI.Dashboard.Planet3DView;
import GUI.Dashboard.Console.ConsoleClass;

public class WindowContentChooser {
	
	private int windowIndx;
	private JMenu menuItem;
	
	private JRadioButtonMenuItem consoleItem;
	
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
	
	
	public WindowContentChooser(int windowIndx) {
		this.windowIndx = windowIndx;
		
         menuItem = new JMenu("Set window "+(windowIndx+1)+" content");
        //menu_ThirdWindow.setForeground(labelColor);
        //menu_ThirdWindow.setBackground(backgroundColor);
         menuItem.setFont(smallFont);
         menuItem.setMnemonic(KeyEvent.VK_A);
        
        ButtonGroup thirdWindow = new ButtonGroup();
        int indx=1;
        
        JRadioButtonMenuItem menuPoint = new JRadioButtonMenuItem("2D chart");
       //menuPoint.setForeground(labelColor);
        if(DashboardPlotArea.getContentPanelList().get(windowIndx).getID()==indx) {
        	menuPoint.setSelected(true);
        }
        menuPoint.setFont(smallFont);
        menuPoint.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
	                	  // System.out.println("Action: "+windowIndx);
                	   	   System.out.println("Chart panel added to field "+windowIndx);
	                	   List<DashboardPlotPanel> contentPanelList = DashboardPlotArea.getContentPanelList();
		               //----------------------------------------------------------
	                	   List<ChartSetting> chartSetting = DashboardPlotArea.getChartSettings();
	                	   chartSetting.get(windowIndx).setType(0);
	                	   DashboardPlotArea.setChartSettings(chartSetting);
	                	   //----------------------------------------------------------
	                	   contentPanelList.set(windowIndx, (new Data2DPlot(windowIndx, DashboardPlotArea.getAnalysisFile())) );
	                	   DashboardPlotArea.setContentPanelList(contentPanelList);
                    } });
        thirdWindow.add(menuPoint);
        menuItem.add(menuPoint);
        indx++;
        
         menuPoint = new JRadioButtonMenuItem("3D planetary environment");
        // menuPoint.setForeground(labelColor);
         menuPoint.setFont(smallFont);
         if(DashboardPlotArea.getContentPanelList().get(windowIndx).getID()==indx) {
         	menuPoint.setSelected(true);
         }
         menuPoint.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    		System.out.println("Planetary environment added to field "+windowIndx);
	                	   List<DashboardPlotPanel> contentPanelList = DashboardPlotArea.getContentPanelList();
		                	  //----------------------------------------------------------
	                	   List<ChartSetting> chartSetting = DashboardPlotArea.getChartSettings();
	                	   chartSetting.get(windowIndx).setType(1);
	                	   DashboardPlotArea.setChartSettings(chartSetting);
	                	   //----------------------------------------------------------
	                	   contentPanelList.set(windowIndx, (new Planet3DView(DashboardPlotArea.getResultSet())) );
	                	   DashboardPlotArea.setContentPanelList(contentPanelList);
                    	       
                     } });
         thirdWindow.add(menuPoint);
         menuItem.add(menuPoint);
         indx++;
         
         menuPoint = new JRadioButtonMenuItem("3D attitude indicator");
        // menuPoint.setForeground(labelColor);
         menuPoint.setFont(smallFont);
         if(DashboardPlotArea.getContentPanelList().get(windowIndx).getID()==indx) {
         	menuPoint.setSelected(true);
         }
         menuPoint.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    		System.out.println("Attitude environment added to field "+windowIndx);
	                	   List<DashboardPlotPanel> contentPanelList = DashboardPlotArea.getContentPanelList();
	                	  //----------------------------------------------------------
	                	   List<ChartSetting> chartSetting = DashboardPlotArea.getChartSettings();
	                	   chartSetting.get(windowIndx).setType(2);
	                	   DashboardPlotArea.setChartSettings(chartSetting);
	                	   //----------------------------------------------------------
	                	   contentPanelList.set(windowIndx, (new AttitudeView(DashboardPlotArea.getModel3DFilePath())) );
	                	   DashboardPlotArea.setContentPanelList(contentPanelList);
                    	       
                     } });
         thirdWindow.add(menuPoint);
         menuItem.add(menuPoint);
         indx++;
         
         consoleItem = new JRadioButtonMenuItem("Console");
        	 	consoleItem.setForeground(Color.BLACK);
         consoleItem.setFont(smallFont);
         if(DashboardPlotArea.getContentPanelList().get(windowIndx).getID()==indx) {
        	 	consoleItem.setSelected(true);
         }
         consoleItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    				System.out.println("Console added to field "+windowIndx);
                    				List<DashboardPlotPanel> contentPanelList = DashboardPlotArea.getContentPanelList();
				               //----------------------------------------------------------
			                	   List<ChartSetting> chartSetting = DashboardPlotArea.getChartSettings();
			                	   chartSetting.get(windowIndx).setType(3);
			                	   DashboardPlotArea.setChartSettings(chartSetting);
			                	   //---------------------------------------------------------- 
							   ConsoleClass dashboardConsole = new ConsoleClass();	
							   dashboardConsole.setDoc(DashboardPlotArea.getMasterConsole().getDoc()); // Link output stream to main console
			                	   contentPanelList.set(windowIndx, dashboardConsole );
			                	   DashboardPlotArea.setContentPanelList(contentPanelList);
                     } });
         thirdWindow.add(consoleItem);
         menuItem.add(consoleItem);
         indx++;
         
         menuPoint = new JRadioButtonMenuItem("BlueBookPlot");
        // menuPoint.setForeground(labelColor);
         menuPoint.setFont(smallFont);
         if(DashboardPlotArea.getContentPanelList().get(windowIndx).getID()==indx) {
         	menuPoint.setSelected(true);
         }
         menuPoint.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    		System.out.println("BlueBookPlot Module added to field "+windowIndx);
	                	   List<DashboardPlotPanel> contentPanelList = DashboardPlotArea.getContentPanelList();
	                	  //----------------------------------------------------------
	                	   List<ChartSetting> chartSetting = DashboardPlotArea.getChartSettings();
	                	   int chartTypeID=4;
	                	   chartSetting.get(windowIndx).setType(chartTypeID);
	                	   DashboardPlotArea.setChartSettings(chartSetting);
	                	   //----------------------------------------------------------
	                	   contentPanelList.set(windowIndx, (new BlueBookPlot()) );
	                	   DashboardPlotArea.setContentPanelList(contentPanelList);
                    	       
                     } });
         thirdWindow.add(menuPoint);
         menuItem.add(menuPoint);
         indx++;
         
         menuPoint = new JRadioButtonMenuItem("Multiplot area");
         menuPoint.setForeground(Color.GRAY);
         menuPoint.setFont(smallFont);
         if(DashboardPlotArea.getContentPanelList().get(windowIndx).getID()==indx) {
         	menuPoint.setSelected(true);
         }
         menuPoint.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                    	       
                     } });
         thirdWindow.add(menuPoint);
         menuItem.add(menuPoint);
         indx++;

	}

	public int getWindowIndx() {
		return windowIndx;
	}

	public JMenu getMenuItem() {
		return menuItem;
	}


}
