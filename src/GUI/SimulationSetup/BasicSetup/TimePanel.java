package GUI.SimulationSetup.BasicSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import DateTime.AbsoluteTime;
import GUI.FilePaths;
import utils.WriteInput;

public class TimePanel {

	private JPanel datePanel;
	private JPanel resultPanel;
	private JPanel timePanel;
	
	private int labelWidth=50;
	private int textWidth=50;
	
	private AbsoluteTime aTime;
	
	private int year=0;
	private int month=0;
	private int day=0;
	private int hour=0;
	private int minute=0;
	private int second=0;
	private int millisecond=0;
	
	private List<JPanel> inputPanels;
	private List<Integer> timeValues;
	
	Color backgroundColor;
	Color labelColor;
	
	public TimePanel(Color backgroundColor, Color labelColor){
		
		inputPanels =  new ArrayList<>();
		timeValues = new ArrayList<>();
		
		this.backgroundColor = backgroundColor;
		this.labelColor = labelColor;
		
		timePanel=new JPanel();
		timePanel.setLayout(new GridLayout(2,1));
		timePanel.setSize(300, 100);
		timePanel.setBackground(backgroundColor);
		
		datePanel = new JPanel();
		datePanel.setLayout(new GridLayout(2,3));
		datePanel.setBackground(backgroundColor);
		
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout(2,1));
		resultPanel.setBackground(backgroundColor);
		
		aTime = new AbsoluteTime(year, month, day, hour, minute, second, millisecond);
		
		JPanel yPanel = createPanelElement( datePanel,  "Year",  "") ;
		inputPanels.add(yPanel);
		timeValues.add(year);
		
		JPanel mPanel = createPanelElement( datePanel,  "Month",  "") ;
		inputPanels.add(mPanel);
		timeValues.add(month);
		JPanel dPanel = createPanelElement( datePanel,  "Day",  "") ;
		inputPanels.add(dPanel);
		timeValues.add(day);
		createPanelElement( datePanel );
		
		
		
		JPanel hPanel = createPanelElement( datePanel,  "Hour",  "") ;
		inputPanels.add(hPanel);
		timeValues.add(hour);
		JPanel MPanel = createPanelElement( datePanel,  "Min",  "") ;
		inputPanels.add(MPanel);
		timeValues.add(minute);	
		JPanel sPanel = createPanelElement( datePanel,  "Sec",  "") ;
		inputPanels.add(sPanel);
		timeValues.add(second);
		JPanel msPanel = createPanelElement( datePanel,  "MilSec",  "") ;
		inputPanels.add(msPanel);
		timeValues.add(millisecond);
		

		
		JPanel j2Time = createPanelElement( resultPanel,  "Time - J2000 Epoch - UTC standard :",  "") ;
		inputPanels.add(j2Time);
		
		JPanel utcString = createPanelElement( resultPanel,  "Uniform converted UTC time string: ",  "") ;
		inputPanels.add(utcString);
		((JTextComponent) utcString.getComponent(1)).setEditable(false);
		
		timePanel.add(datePanel);
		timePanel.add(resultPanel);
	}
	
	public static void main(String[] args) {
		// Unit Tester :
		JFrame frame = new JFrame("Component Tester");
		//frame.setSize(400,400);
		frame.setLayout(new BorderLayout());
		
		TimePanel panel = new TimePanel(Color.black, Color.WHITE);
		frame.add(panel.getTimePanel(), BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
	}
	
	private JPanel createPanelElement(JPanel parent, String label, String text) {
		JPanel panel = new JPanel();
		panel.setBackground(backgroundColor);
		//panel.setSize(400, 20);
		panel.setLayout(new BorderLayout());
		
		createLabelElement( panel,  label); 
		
		createInputElement( panel,  text);
		
		parent.add(panel);
			
		return panel;
	}
	
	private JPanel createPanelElement(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setBackground(backgroundColor);
		//panel.setSize(400, 20);
		panel.setLayout(new BorderLayout());
		
		createLabelElement( panel,  ""); 
		
		parent.add(panel);
			
		return panel;
	}
	
	private JLabel createLabelElement(JPanel parent, String input) {
	      JLabel label = new JLabel(input);
	      //label.setMinimumSize(new Dimension(20, 20));
	      //label.getHorizontalAlignment();
	      label.setBackground(backgroundColor);
	      label.setForeground(labelColor);
	      parent.add(label, BorderLayout.WEST);
	      return label;
	}

	
	private JTextField createInputElement(JPanel parent, String text) {
	     JTextField textField = new JTextField(10);
	     textField.setMinimumSize(new Dimension(30, 20));
	     textField.setHorizontalAlignment(JTextField.LEFT);
	     textField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) { }

			@Override
			public void focusLost(FocusEvent e) {
				int i = 0;
				for(JPanel panel : inputPanels) {
					if(panel == parent && i<7) {
						int value =0; 
						try {
							value = Integer.parseInt((((JTextField) panel.getComponent(1)).getText()));
						} catch (NumberFormatException e2) {
							// add error message
						}
						timeValues.set(i, value );
					}
					i++;
				}
				updateATime();
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	  
	      });
	     textField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int i = 0;
				for(JPanel panel : inputPanels) {
					if(panel == parent && i<7) {
						int value =0; 
						try {
							value = Integer.parseInt((((JTextField) panel.getComponent(1)).getText()));
						} catch (NumberFormatException e) {
							// add error message
						}
						timeValues.set(i, value );
					}
					i++;
				}
				updateATime();
				WriteInput.writeInputFile(FilePaths.inputFile);
			}
	    	 
	     });
	     parent.add(textField, BorderLayout.CENTER);
	     return textField;
	}

	public int getLabelWidth() {
		return labelWidth;
	}

	public void setLabelWidth(int labelWidth) {
		this.labelWidth = labelWidth;
		datePanel.repaint();
	}

	public int getTextWidth() {
		return textWidth;
	}

	public void setTextWidth(int textWidth) {
		this.textWidth = textWidth;
		datePanel.repaint();
	}

	public JPanel getDatePanel() {
		return datePanel;
	}
	
	public JPanel getTimePanel() {
		return timePanel;
	}
	
	private void updateATime(){
		for(int i=0;i<timeValues.size();i++) {
			if(i==0) {
				this.setYear(timeValues.get(0));
			} else if (i==1) {
				this.setMonth((int) timeValues.get(1));
			} else if (i==2) {
				this.setDay(timeValues.get(2));
			} else if (i==3) {
				this.setHour(timeValues.get(3));
			} else if (i==4) {
				this.setMinute(timeValues.get(4));
			} else if (i==5) {
				this.setSecond(timeValues.get(5));
			} else if (i==6) {
				this.setMillisecond(timeValues.get(6));
			}
		}
		((JTextField) inputPanels.get(7).getComponent(1)).setText(""+aTime.getJ2000());
		((JTextField) inputPanels.get(8).getComponent(1)).setText(""+aTime.getUtcString());
		
	}
	
	public void updateTimeFromString(String utcString) {
		
		String[] date = utcString.split(" ")[0].split(":");
		String[] time = utcString.split(" ")[1].split(":");

		try {
			this.setYear(Integer.parseInt(date[0]));
			this.setMonth(Integer.parseInt(date[1]));
			this.setDay(Integer.parseInt(date[2]));
			
			this.setHour(Integer.parseInt(time[0]));
			this.setMinute(Integer.parseInt(time[1]));
			this.setSecond(Integer.parseInt(time[2]));
			
			((JTextField) inputPanels.get(0).getComponent(1)).setText(""+year);
			((JTextField) inputPanels.get(1).getComponent(1)).setText(""+month);
			((JTextField) inputPanels.get(2).getComponent(1)).setText(""+day);
			
			((JTextField) inputPanels.get(3).getComponent(1)).setText(""+hour);
			((JTextField) inputPanels.get(4).getComponent(1)).setText(""+minute);
			((JTextField) inputPanels.get(5).getComponent(1)).setText(""+second);
			
			((JTextField) inputPanels.get(7).getComponent(1)).setText(""+aTime.getJ2000());
			((JTextField) inputPanels.get(8).getComponent(1)).setText(""+aTime.getUtcString());
		} catch (ArrayIndexOutOfBoundsException exception) {
			System.out.println("ERROR/TimePanel: Reading time string failed. Format error");
		}
	}

	public AbsoluteTime getaTime() {
		return aTime;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

	public int getMillisecond() {
		return millisecond;
	}

	public void setDatePanel(JPanel datePanel) {
		this.datePanel = datePanel;
	}

	public void setaTime(AbsoluteTime aTime) {
		this.aTime = aTime;
	}

	public void setYear(int year) {
		this.year = year;
		aTime.setYear(year);
	}

	public void setMonth(int month) {
		this.month = month;
		aTime.setMonth(month);
	}

	public void setDay(int day) {
		this.day = day;
		aTime.setDay(day);
	}

	public void setHour(int hour) {
		this.hour = hour;
		aTime.setHour(hour);
	}

	public void setMinute(int minute) {
		this.minute = minute;
		aTime.setMinute(minute);
	}

	public void setSecond(int second) {
		this.second = second;
		aTime.setSecond(second);
	}

	public void setMillisecond(int millisecond) {
		this.millisecond = millisecond;
		aTime.setMillisecond(millisecond);
	}
	
	public long getJ2000Time() {
		return aTime.getJ2000();
	}
	
}
