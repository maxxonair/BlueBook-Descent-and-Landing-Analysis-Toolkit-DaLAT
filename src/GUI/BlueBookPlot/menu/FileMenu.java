package GUI.BlueBookPlot.menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import GUI.BlueBookPlot.main.BlueBookPlot;
import GUI.BlueBookPlot.serviceFunctions.ResultFileSetWindow;

public class FileMenu {

	
	public static JMenu create() {
        JMenu menuPoint = new JMenu("File");
        menuPoint.setForeground(BlueBookPlot.getLabelColor());
        menuPoint.setBackground(BlueBookPlot.getBackgroundColor());
        menuPoint.setFont(BlueBookPlot.getSmallFont());
        menuPoint.setMnemonic(KeyEvent.VK_A);

        JMenuItem itemResultFileSet = new JMenuItem("Manage Data Files  "); 
        itemResultFileSet.setForeground(Color.black);
        itemResultFileSet.setFont(BlueBookPlot.getSmallFont());
      //  itemSelectVariableList.setAccelerator(KeyStroke.getKeyStroke(
      //          KeyEvent.VK_A, ActionEvent.ALT_MASK));
        menuPoint.add(itemResultFileSet);
        itemResultFileSet.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   try {
						ResultFileSetWindow.createAndShowGUI();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    } });
        
        JMenuItem itemSelectVariableList = new JMenuItem("Select VariableList  "); 
        itemSelectVariableList.setForeground(Color.black);
        itemSelectVariableList.setFont(BlueBookPlot.getSmallFont());
      //  itemSelectVariableList.setAccelerator(KeyStroke.getKeyStroke(
      //          KeyEvent.VK_A, ActionEvent.ALT_MASK));
        menuPoint.add(itemSelectVariableList);
        itemSelectVariableList.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   BlueBookPlot.selectVariableList(itemSelectVariableList);
                    } });
        

       return menuPoint;        
	}
}
