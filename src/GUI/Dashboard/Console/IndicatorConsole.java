package GUI.Dashboard.Console;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

import javax.swing.text.*;

public class IndicatorConsole  {
	
    private JPanel mainPanel;
    private  JTextPane textPane;
    private PrintStream con;
    
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
	 
    protected int type=3;
    

    public IndicatorConsole () {
		mainPanel = new JPanel();
		mainPanel.setBackground(backgroundColor);
		mainPanel.setLayout(new BorderLayout());
        
		ConsoleStyledDocument doc = new ConsoleStyledDocument(strKeywords, keywordColor, numericalColor);
        //con=new PrintStream(new TextAreaOutputStream(doc, maxLines));
        //System.setOut(new PrintStream(taOutputStream));  
        
        textPane = new JTextPane(doc);
        textPane.setBackground(backgroundColor);
        textPane.setEditable(false);
        mainPanel.add(new JScrollPane(textPane));
    }
    
    public void addContentString(String contentString) {
    	int endPosition = textPane.getDocument().getEndPosition().getOffset();
    	String insertString = contentString + " \n ";
    	try {
			textPane.getDocument().insertString(endPosition, insertString, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
    	
		this.textPane.revalidate();
		this.textPane.repaint();
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
    }
    
    public void clearDocument() {
    		textPane.removeAll();
    		ConsoleStyledDocument doc = new ConsoleStyledDocument(strKeywords,  keywordColor, numericalColor);
    		textPane.setDocument(doc);
    		this.textPane.revalidate();
    		this.textPane.repaint();
    		this.mainPanel.revalidate();
    		this.mainPanel.repaint();
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
	

	@SuppressWarnings("resource")
	public void setCON(PrintStream  con) {

		this.textPane.revalidate();
		this.textPane.repaint();
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}
	


	public PrintStream getCON() {
		return con;
	}

	

public JPanel getMainPanel() {
		return mainPanel;
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
		IndicatorConsole console = new IndicatorConsole();
        frame.add(console.getMainPanel());
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        Point p = MouseInfo.getPointerInfo().getLocation() ;
        frame.setLocation(p);
        frame.setVisible(true);
        
        console.addContentString("This is  a test");
        
        console.clearDocument();
        
        console.addContentString("Error occured 0.2");
        
		for(int i=0;i<10;i++) {
			//System.out.println("Output 2.215>55 test> Reading "+i+" protected ");
		}
		
    }
	
	
			   
}