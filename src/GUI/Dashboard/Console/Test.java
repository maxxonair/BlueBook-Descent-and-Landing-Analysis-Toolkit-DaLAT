package GUI.Dashboard.Console;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.text.*;

import GUI.Dashboard.Console.ConsoleClass.TextAreaOutputStream;

public class Test {	
	
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
    									   {"0","1","2","3","4","5","6","7","8","9",".0",".1",".2",".3",".4",".5",".6",".7",".8",".9","0.","1.","2.","3.","4.","5.","6.","7.","8.","9."}};								// Numbers/Numerical data 
	 
    private int ID=3; 
	
	
    public Test () {
		mainPanel = new JPanel();
		mainPanel.setBackground(backgroundColor);
		mainPanel.setLayout(new BorderLayout());

        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                int length = getLength();
                super.insertString(length, str, a);

                String text = getText(0, getLength());
                
                int thisOffset = length;
                
                int before = findLastNonWordChar(text, thisOffset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, thisOffset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches("(\\W)*(private|public|protected)"))
                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove (int offs, int len) throws BadLocationException {
            		int thisOffset = getLength();
                super.remove(thisOffset, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, thisOffset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, thisOffset);

                if (text.substring(before, after).matches("(\\W)*(private|public|protected)")) {
                    setCharacterAttributes(before, after - before, attr, false);
                } else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                }
            }
        };
        
        
        taOutputStream = new TextAreaOutputStream(doc, ""); 
        // Channel System.out to outputstream
        System.setOut(new PrintStream(taOutputStream));  
        
        JTextPane txt = new JTextPane(doc);
        mainPanel.add(new JScrollPane(txt));
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

	private int findLastNonWordChar (String text, int index) {
	    while (--index >= 0) {
	        if (String.valueOf(text.charAt(index)).matches("\\W")) {
	            break;
	        }
	    }
	    return index;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public static void main (String args[]) {
		JFrame frame = new JFrame("Component Tester - Console ");
		frame.setSize(450,450);
		frame.setLayout(new BorderLayout());
		
		
        Test console = new Test();
        frame.add(console.getMainPanel());
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        Point p = MouseInfo.getPointerInfo().getLocation() ;
        frame.setLocation(p);
        frame.setVisible(true);
        
		for(int i=0;i<10;i++) {
			System.out.println("Block 1> Reading "+i+" protected ");
		}
		
		for(int i=20;i>0;i--) {
			System.out.println("Block 2> Reading "+i+" private read ");
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
 	            	   
 	       
							try {
								textArea.insertString (0, text, null) ;
							} catch (BadLocationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//textArea.dump(text);
	
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
