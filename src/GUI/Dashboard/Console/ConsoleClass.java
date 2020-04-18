package GUI.Dashboard.Console;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.*;

import GUI.Dashboard.DashboardPlotPanel;


public class ConsoleClass extends DashboardPlotPanel{
	
    private JPanel mainPanel;
    private  JTextPane textPane;
    private  TextAreaOutputStream  taOutputStream ; 
    
    Color labelColor = new Color(77,255,195);    	// Label Color
   	Color backgroundColor = new Color(41,41,41);		// Background Color
   	
    Color[] keywordColor = {new Color(255,140,0), 
    							new Color(255,215,0),
    							new Color(255,0,0),
    							new Color(0,250,0),
    							new Color(0,191,255)};
    
    private String[][] strKeywords = { {"complete", "protected", "SIMULATION", "Simulation", "Simulator"}, 		// Keywords
    									   {"Read","READ","Reading", "Write", "WRITE"},							 	// File/Info Read/Write processes
    									   {"Error","ERROR","error"},							  					// Errors and warnings
    									   {"Start","start","START","Launch","LAUNCH","launch"},	  					// Process start ups 
    									   {"0"}};								// Numbers/Numerical data 
	 
    private int ID=3; 
    protected int type=3;
    
    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    public ConsoleClass () {
		mainPanel = new JPanel();
		mainPanel.setBackground(backgroundColor);
		mainPanel.setLayout(new BorderLayout());

        final StyleContext cont = StyleContext.getDefaultStyleContext();
         List<AttributeSet> attrList = new ArrayList<AttributeSet>();
         for(int i=0;i<strKeywords.length;i++) { 
        	 	AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, keywordColor[i]);
        	 	attrList.add(attr);
         }
        final AttributeSet attrLabel = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, labelColor);

        String[] strKey = new String[strKeywords.length];
        for(int i=0;i<strKeywords.length;i++) { 
            strKey[i] = "(\\W)*(";
            	for (int j=0;j<strKeywords[i].length;j++) { if(j<strKeywords[i].length-1) {strKey[i] += strKeywords[i][j] +"|";} else {strKey[i] += strKeywords[i][j];}}
            strKey[i] += ")";
        	}
        
        DefaultStyledDocument doc = new DefaultStyledDocument(){
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                int length = getLength();
                super.insertString(length, str, a);
                int thisOffset = length;

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, thisOffset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, thisOffset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                    	boolean matchFound = false;
                    	// Check for Keywords:
                    	for(int i=0;i<strKeywords.length;i++) { 
                    			if (text.substring(wordL, wordR).matches(strKey[i])) {
                                    setCharacterAttributes(wordL, wordR - wordL, attrList.get(i), false);
        	                        		matchFound = true; 
                        		} 
                    	}
                    	// Check for numerical data:
        		        boolean numeric = true;
        		        String testString="";
        		        try {
        		        		 testString = text.substring(wordL, wordR);
        		            @SuppressWarnings("unused")
							Double num = Double.parseDouble(testString);
        		        } catch (NumberFormatException e) {
        		            numeric = false;
        		        }
        		        if(numeric) {
    		        			System.err.println(testString);
                        setCharacterAttributes(wordL, wordR - wordL, attrList.get(4), false);
                        matchFound = true;
        		        } 
        		        // If no keywords found > set standard:
                    	if(matchFound == false) {
                       setCharacterAttributes(wordL, wordR - wordL, attrLabel, false);
                    	}
                        wordL = wordR;
                    	}
                    wordR++;
                }
               // textPane.setCaretPosition(0);
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);
                boolean matchFound = false;
            		for(int i=0;i<strKeywords.length;i++) { 
	            		if (text.substring(before, after).matches(strKey[i])) {
	            			setCharacterAttributes(before, after - before, attrList.get(i), false);
	            			matchFound = true;
	            		} 
            		}
            		if(matchFound == false) {
	                    setCharacterAttributes(before, after - before, attrLabel, false);
	            }
            }
        };
        // doc.setDocumentFilter(filter); // Have a look at this to filter user inputs & implement non editable 
        
        // Add output stream to document field
        
        taOutputStream = new TextAreaOutputStream(doc, ""); 
        // Channel System.out to outputstream
        System.setOut(new PrintStream(taOutputStream));  
        //doc.dump(new PrintStream(taOutputStream));
        
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
		mainPanel.revalidate();
		mainPanel.repaint();
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
    
    public class TextAreaOutputStream extends OutputStream {

    	   private final DefaultStyledDocument textArea;
    	   private final StringBuilder sb = new StringBuilder();
    	   private String title;

    	   public TextAreaOutputStream(final DefaultStyledDocument doc, String title) {
    	      this.textArea = doc;
    	      this.title = title;
    	      sb.append(title + "> ");
    	   }

    	   @Override
    	   public void flush() {
    	   }

    	   @Override
    	   public void close() {
    	   }

    	   @Override
    	   public void write(int b) throws IOException {

    	      if (b == '\r')
    	         return;

    	      if (b == '\n') {
    	         final String text = sb.toString() + "\n";
    	         SwingUtilities.invokeLater(new Runnable() {
    	            public void run() {
    	               boolean isFound = text.indexOf("commons.math3.exception")!=-1? true: false; 
    	               if(isFound) {
    	            	   /*
    	               textArea.setSelectedTextColor(Color.red);
    	               textArea.append("\n");
    	               textArea.append("ERROR: Integrator FAILED. ");
    	               textArea.append("\n");
    	               textArea.append(text);
    	               textArea.append("\n");
    	               */
    	               } else {
    	            	   
    	            	   //textArea.append(text); 
    	            	   try {
							textArea.insertString (0, text, null) ;
							//textArea.dump(text);
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    	               }
    	            }
    	         });
    	         sb.setLength(0);
    	         sb.append(title + "> ");
    	         return;
    	      }

    	      sb.append((char) b);
    	   }
    	}
}