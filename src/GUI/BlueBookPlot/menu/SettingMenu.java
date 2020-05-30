package GUI.BlueBookPlot.menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import GUI.BlueBookPlot.main.BlueBookPlot;

public class SettingMenu {

	
	public static JMenu create() {
		
        JMenu menuPoint = new JMenu("Settings");
        menuPoint.setForeground(BlueBookPlot.getLabelColor());
        menuPoint.setBackground(BlueBookPlot.getBackgroundColor());
        menuPoint.setFont(BlueBookPlot.getSmallFont());
        menuPoint.setMnemonic(KeyEvent.VK_A);
		
		
        JMenu menuItemColor = new JMenu("Set Theme               ");
        menuItemColor.setForeground(Color.black);
        //menuItemColor.setBackground(backgroundColor);
        menuItemColor.setFont(BlueBookPlot.getSmallFont());
        menuItemColor.setMnemonic(KeyEvent.VK_A);
        menuPoint.add(menuItemColor);
        ButtonGroup group = new ButtonGroup();
        
        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem("Dark Theme");
        menuItem.setForeground(BlueBookPlot.getBackgroundColor());
        menuItem.setFont(BlueBookPlot.getSmallFont());
        menuItem.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   BlueBookPlot.setGUIColors(true);
                    } });
        group.add(menuItem);
        menuItemColor.add(menuItem);
        menuItem.setSelected(true);
        
        menuItem = new JRadioButtonMenuItem("Bright Theme");
        menuItem.setForeground(BlueBookPlot.getBackgroundColor());
        menuItem.setFont(BlueBookPlot.getSmallFont());
        menuItem.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                	   BlueBookPlot.setGUIColors(false);
                    } });
        group.add(menuItem);
        menuItemColor.add(menuItem);
        
        
        return menuPoint;
	}
}
