package GUI.Dashboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import GUI.BlueBookVisual;
import GUI.RotatedIcon;
import GUI.TextIcon;




public class VariableList2 {
	
	private static String PROJECT_TITLE = " Axis Variable";
	private static int xInit = 190;
	private static int yInit = 650;
	
	private int elementHeight = 25;
	private int selectedIndx=0;
	
	private  String axisName;
	private JButton panel;
	//private int chartIndx=1;
	
	private PlotElement plotElement;
	
	public VariableList2(JButton panel, String axis, PlotElement plotElement) {
		this.panel = panel;
		this.axisName = axis; 
		this.plotElement = plotElement;
	}
	
	public void getVariableList(List<String> variableList) {

    JFrame.setDefaultLookAndFeelDecorated(false);
    JFrame frame = new JFrame("" +axisName+""+ PROJECT_TITLE);
    frame.setPreferredSize(new java.awt.Dimension(xInit+10,yInit));
    frame.setLayout(new BorderLayout());
    frame.pack();
    //frame.setLocationRelativeTo(null);
    //frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setResizable(false);
    frame.setAlwaysOnTop( true );
    Point location = MouseInfo.getPointerInfo().getLocation(); 
    int x = (int) location.getX();
    int y = (int) location.getY();
    frame.setLocation(x, y);
    //frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
    
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    //mainPanel.setPreferredSize(new Dimension(1000, 25));
    int panelHeight=25;
    mainPanel.setBackground(BlueBookVisual.getBackgroundColor());
    mainPanel.setLayout(null);
    int varY=0;
    JButton[] buttonGroup = new JButton[variableList.size()];
    for(int i=0;i<variableList.size();i++) {
	    	buttonGroup[i] = new JButton(variableList.get(i));
	    	buttonGroup[i].setSize(xInit,elementHeight);
	    	buttonGroup[i].setFont(BlueBookVisual.getSmall_font());
	    	buttonGroup[i].setBackground(BlueBookVisual.getBackgroundColor());
	    	buttonGroup[i].setForeground(BlueBookVisual.getLabelColor());
	    	buttonGroup[i].setOpaque(true);
	    	buttonGroup[i].setBorderPainted(false);
	    	buttonGroup[i].setLocation(0, varY);
	    	buttonGroup[i].addActionListener(new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Change Inicator Text when selected
					int indx = getIndx(variableList, arg0.getActionCommand());
					selectedIndx=indx;
					//System.out.println(indx);
					
					if(axisName.equals("y")) {
						// Update Axis Label
				      TextIcon t1 = new TextIcon(panel, arg0.getActionCommand(), TextIcon.Layout.HORIZONTAL);
				      RotatedIcon r1 = new RotatedIcon(t1, RotatedIcon.Rotate.UP);
				      panel.setIcon( r1 );
				       // Update Chart Setting and Update Chart
				      ChartSetting chartSetting = plotElement.getChartSetting();
				      chartSetting.setY(selectedIndx);
				      plotElement.setChartSetting(chartSetting);
				     // PlotPanelManager.getChartSettings().get(plotElement.getID()).setY(selectedIndx);
					} else if(axisName.equals("x")) {
				      TextIcon t1 = new TextIcon(panel, arg0.getActionCommand(), TextIcon.Layout.HORIZONTAL);
				      RotatedIcon r1 = new RotatedIcon(t1, RotatedIcon.Rotate.ABOUT_CENTER);
				      panel.setIcon( r1 );
				       // Update Chart Setting and Update Chart
				      ChartSetting chartSetting = plotElement.getChartSetting();
				      chartSetting.setX(selectedIndx);
				      plotElement.setChartSetting(chartSetting);
					 // PlotPanelManager.getChartSettings().get(plotElement.getID()).setX(selectedIndx);
					}

					plotElement.updateChart();
				      // Dispose frame
					frame.dispose();;
				}
	    		
	    	});
	    	mainPanel.setPreferredSize(new Dimension(xInit, panelHeight));
	    	mainPanel.add(buttonGroup[i]);
	    	varY+=elementHeight;
	    	panelHeight+=elementHeight;
    }
    
    JScrollPane scrollPane = new JScrollPane(mainPanel);
    scrollPane.setPreferredSize(new java.awt.Dimension(xInit,yInit));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    frame.add(scrollPane, BorderLayout.CENTER);

	}


	public int getSelectedIndx() {
		return selectedIndx;
	}
	
	public void setSelectedIndx(int selectedIndx) {
		this.selectedIndx = selectedIndx;
	}

	public static int getIndx(List<String> variableList, String value) {
		for(int i=0;i<variableList.size();i++) {
			if(value.equals(variableList.get(i))) {
				return i;
			}
		}
		return 0;
	}

}
