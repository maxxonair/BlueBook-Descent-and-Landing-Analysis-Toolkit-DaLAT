package GUI.Dashboard.Console;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ConsoleStyledDocument extends DefaultStyledDocument {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    int maxLines = 1000;
    
    Color labelColor = new Color(77,255,195);    	// Label Color
   	Color backgroundColor = new Color(41,41,41);		// Background Color
   	
    Color[] keywordColor ;
    
    Color numericalColor;
    
    private String[][] strKeywords ;							
	
	
	String[] strKey ;
	 List<AttributeSet> attrList ;
	 final StyleContext cont = StyleContext.getDefaultStyleContext();
	 final AttributeSet attrLabel = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, labelColor);
	 AttributeSet numAttr ;
  
	  public ConsoleStyledDocument(String[][] strKeywords, Color[] keywordColor, Color numericalColor ) {
	      this.strKeywords = strKeywords;
	      this.keywordColor = keywordColor;
	      this.numericalColor = numericalColor; 
	      
	      attrList = new ArrayList<AttributeSet>();
	      for(int i=0;i<strKeywords.length;i++) { 
	     	 	AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, keywordColor[i]);
	     	 	attrList.add(attr);
	      }
	      numAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, numericalColor);
	      
	     strKey = new String[strKeywords.length];
	     for(int i=0;i<strKeywords.length;i++) { 
	         strKey[i] = "(\\W)*(";
	         	for (int j=0;j<strKeywords[i].length;j++) { if(j<strKeywords[i].length-1) {strKey[i] += strKeywords[i][j] +"|";} else {strKey[i] += strKeywords[i][j];}}
	         strKey[i] += ")";
	     	}
	  }
	

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
               setCharacterAttributes(wordL, wordR - wordL, numAttr, false);
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
}
