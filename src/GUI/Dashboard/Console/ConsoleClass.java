package GUI.Dashboard.Console;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;


import GUI.Dashboard.DashboardPlotPanel;


public class ConsoleClass extends DashboardPlotPanel {
	
    private JPanel mainPanel;
    private  JTextPane textPane;
    private PrintStream con;
    private ConsoleStyledDocument doc;
    
    int maxLines = 1000;
    
    Color labelColor = new Color(77,255,195);    	// Label Color
   	Color backgroundColor = new Color(41,41,41);		// Background Color

    Color[] keywordColor = {new Color(255,140,0), 
			new Color(255,215,0),
			new Color(255,0,0),
			new Color(0,250,0),
			new Color(0,51,255),
			new Color(240,191,35)};

	Color numericalColor = new Color(0,191,255);
	
	private String[][] strKeywords = { {"SIMULATION", "Simulation", "Simulator", "RUN","simulation"}, 		// Keywords
						   {"Read","READ","Reading", "Write", "WRITE","Writing","Action", "Update", "Updated", "complete", "completed", "Warning", "WARNING", "warning"},							 	// File/Info Read/Write processes
						   {"Error","ERROR","error"},							  					// Errors and warnings
						   {"Start","start","START","Launch","LAUNCH","launch"},	  					// Process start ups 
						   {"Propulsion","PROP"},
						   {"AERO","Aerodynamic"}};								// Propulsion data 
	 
    private int ID=3; 
    protected int type=3;
    

    public ConsoleClass () {
		mainPanel = new JPanel();
		mainPanel.setBackground(backgroundColor);
		mainPanel.setLayout(new BorderLayout());
        
         doc = new ConsoleStyledDocument(strKeywords,  keywordColor, numericalColor);
        
        con=new PrintStream(new TextAreaOutputStream(doc, maxLines));

        //System.setOut(new PrintStream(taOutputStream));  
        textPane = new JTextPane(doc);
        textPane.setBackground(backgroundColor);
        textPane.setEditable(false);
        mainPanel.add(new JScrollPane(textPane));
    }
    

    public void setLabelColor(Color labelColor) {
		this.labelColor = labelColor;
		textPane.setForeground(labelColor);
		textPane.revalidate();
		textPane.repaint();
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		textPane.setBackground(backgroundColor);
		textPane.revalidate();
		textPane.repaint();
	}
	
	
	
	public ConsoleStyledDocument getDoc() {
		return doc;
	}

	public void setDoc(ConsoleStyledDocument doc) {
		this.doc = doc;
		textPane.removeAll();
		textPane.setDocument(doc);
		
		this.textPane.revalidate();
		this.textPane.repaint();
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}

	@SuppressWarnings("resource")
	public void setCON(PrintStream  con) {

		this.textPane.revalidate();
		this.textPane.repaint();
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}
	
	public void linkConPrintOut() {
        System.setOut(con);
	}
	
	public void linkConPrintErr() {
        System.setErr(con);
	}

	public PrintStream getCON() {
		return con;
	}

	@Override
	public int getID() {
		return ID;
	}
	@Override
	public int getType() {
		return type;
	}
	
	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	@Override 
	public void refresh() {

	}

/**
 * 
 * 
 * Unit Tester 
 * @param args
 */
	public static void main (String args[]) {
    	
		JFrame frame = new JFrame("Component Tester - Console ");
		frame.setSize(450,450);
		frame.setLayout(new BorderLayout());
        ConsoleClass console = new ConsoleClass();
        frame.add(console.getMainPanel());
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        Point p = MouseInfo.getPointerInfo().getLocation() ;
        frame.setLocation(p);
        frame.setVisible(true);
        
		for(int i=0;i<10;i++) {
			System.out.println("Output 2.215>55 test> Reading "+i+" protected ");
		}
		
		for(int i=100;i>0;i--) {
			System.out.println("INPUT test> Reading "+i+" error read ");
		}
    }
    
    
}