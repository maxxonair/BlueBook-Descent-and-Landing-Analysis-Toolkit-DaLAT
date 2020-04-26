package utils;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TextAreaOutputStream extends OutputStream {

   private final JTextArea textArea;
   private final StringBuilder sb = new StringBuilder();
   private String title;

   public TextAreaOutputStream(final JTextArea textArea2, String title) {
      this.textArea = textArea2;
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
               textArea.setSelectedTextColor(Color.red);
               textArea.append("\n");
               textArea.append("ERROR: Integrator FAILED. ");
               textArea.append("\n");
               textArea.append(text);
               textArea.append("\n");
               } else {
            	   
            	   textArea.append(text); 
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