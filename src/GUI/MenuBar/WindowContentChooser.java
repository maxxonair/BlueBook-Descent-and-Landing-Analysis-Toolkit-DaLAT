package GUI.MenuBar;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import GUI.Dashboard.AttitudeView;
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
	                	   contentPanelList.set(windowIndx, (new Data2DPlot(DashboardPlotArea.getAnalysisFile())) );
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
	                	   contentPanelList.set(windowIndx, (new AttitudeView(DashboardPlotArea.getModel3DFilePath())) );
	                	   DashboardPlotArea.setContentPanelList(contentPanelList);
                    	       
                     } });
         thirdWindow.add(menuPoint);
         menuItem.add(menuPoint);
         indx++;
         
         menuPoint = new JRadioButtonMenuItem("Multiplot area");
        // menuPoint.setForeground(labelColor);
         menuPoint.setFont(smallFont);
         if(DashboardPlotArea.getContentPanelList().get(windowIndx).getID()==indx) {
         	menuPoint.setSelected(true);
         }
         menuPoint.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	/*
							try {
								JPanel Content = CreateCustomChart.createContentPane();
							  	   for(int i=0;i<SpaceShip3DControlPanelContent.size();i++) {
							   		  SpaceShip3DControlPanel.remove((Component) SpaceShip3DControlPanelContent.get(i));
							   	    }
							         SpaceShip3DControlPanel.add(Content,BorderLayout.CENTER);
							         SpaceShip3DControlPanelContent.add(Content);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						SplitPane_Page1_Charts_vertical.setDividerLocation(500);
						thirdWindowIndx=2;
						*/
                    	       
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
