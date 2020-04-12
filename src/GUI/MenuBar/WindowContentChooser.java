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

import GUI.Dashboard.AttitudeView;
import GUI.Dashboard.ChartSetting;
import GUI.Dashboard.ConsoleClass;
import GUI.Dashboard.DashboardPlotArea;
import GUI.Dashboard.DashboardPlotPanel;
import GUI.Dashboard.Data2DPlot;
import GUI.Dashboard.Planet3DView;


public class WindowContentChooser {
	
	private int windowIndx;
	private JMenu menuItem;
	
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
         
         menuPoint = new JRadioButtonMenuItem("Console");
        // menuPoint.setForeground(labelColor);
         menuPoint.setFont(smallFont);
         if(DashboardPlotArea.getContentPanelList().get(windowIndx).getID()==indx) {
         	menuPoint.setSelected(true);
         }
         menuPoint.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
			                	   List<DashboardPlotPanel> contentPanelList = DashboardPlotArea.getContentPanelList();
				               //----------------------------------------------------------
			                	   List<ChartSetting> chartSetting = DashboardPlotArea.getChartSettings();
			                	   chartSetting.get(windowIndx).setType(3);
			                	   DashboardPlotArea.setChartSettings(chartSetting);
			                	   //----------------------------------------------------------
			                	   contentPanelList.set(windowIndx, (new ConsoleClass()) );
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
