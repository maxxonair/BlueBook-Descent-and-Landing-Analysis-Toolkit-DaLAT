package GUI.Dashboard.Console;
import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

import javax.swing.text.*;


public class EditableConsole {	
	
    private JPanel mainPanel;
    private  JTextPane textPane;
    private PrintStream con;
    private DefaultStyledDocument doc;
    
    int maxLines = 1000;
    
    
    Color labelColor = new Color(77,255,195);    	// Label Color
   	Color backgroundColor = new Color(41,41,41);		// Background Color
   	
    Color[] keywordColor = {new Color(255,140,0), 
    							new Color(255,215,0),
    							new Color(255,0,0),
    							new Color(0,250,0),
    							new Color(0,191,255)};
    @SuppressWarnings("unused")
	private String[][] strKeywords = { {"complete", "protected", "SIMULATION", "Simulation", "Simulator"}, 		// Keywords
    									   {"Read","READ","Reading", "Write", "WRITE"},							 	// File/Info Read/Write processes
    									   {"Error","ERROR","error"},							  					// Errors and warnings
    									   {"Start","start","START","Launch","LAUNCH","launch"},	  					// Process start ups 
    									   {"0","1","2","3","4","5","6","7","8","9",".0",".1",".2",".3",".4",".5",".6",".7",".8",".9","0.","1.","2.","3.","4.","5.","6.","7.","8.","9."}};								// Numbers/Numerical data 
	 
	
	
    public EditableConsole () {
		mainPanel = new JPanel();
		mainPanel.setBackground(backgroundColor);
		mainPanel.setLayout(new BorderLayout());

        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
         doc = new DefaultStyledDocument() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
        
        
        con=new PrintStream(new TextAreaOutputStream(doc, maxLines));
        // Channel System.out to outputstream
        
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
	
	public DefaultStyledDocument getDoc() {
		return doc;
	}

	public void setDoc(DefaultStyledDocument doc) {
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

	public static void main (String args[]) {
		JFrame frame = new JFrame("Component Tester - Console ");
		frame.setSize(450,450);
		frame.setLayout(new BorderLayout());
		
		
		EditableConsole console = new EditableConsole();
        console.linkConPrintErr();
        console.linkConPrintOut();
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
	
}
