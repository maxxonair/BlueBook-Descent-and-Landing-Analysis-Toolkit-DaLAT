package GUI.Dashboard.Console;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class TextAreaOutputStream
extends OutputStream
{

// *************************************************************************************************
// INSTANCE MEMBERS
// *************************************************************************************************

private byte[]                          oneByte;                                                    // array for write(int val);
private Appender                        appender;                                                   // most recent action

public TextAreaOutputStream(DefaultStyledDocument txtara) {
    this(txtara,1000);
    }

public TextAreaOutputStream(DefaultStyledDocument txtara, int maxlin) {
    if(maxlin<1) { throw new IllegalArgumentException("TextAreaOutputStream maximum lines must be positive (value="+maxlin+")"); }
    oneByte=new byte[1];
    appender=new Appender(txtara,maxlin);
    }

/** Clear the current console text area. */
public synchronized void clear() {
    if(appender!=null) { appender.clear(); }
    }

public synchronized void close() {
    appender=null;
    }

public synchronized void flush() {
    }

public synchronized void write(int val) {
    oneByte[0]=(byte)val;
    write(oneByte,0,1);
    }

public synchronized void write(byte[] ba) {
    write(ba,0,ba.length);
    }

public synchronized void write(byte[] ba,int str,int len) {
    if(appender!=null) { appender.append(bytesToString(ba,str,len)); }
    }

static private String bytesToString(byte[] ba, int str, int len) {
    try { return new String(ba,str,len,"UTF-8"); } catch(UnsupportedEncodingException thr) { return new String(ba,str,len); } // all JVMs are required to support UTF-8
    }

// *************************************************************************************************
// STATIC MEMBERS
// *************************************************************************************************

    static class Appender
    implements Runnable
    {
    private final DefaultStyledDocument             textArea;
    private final int                   maxLines;                                                   // maximum lines allowed in text area
    private final LinkedList<Integer>   lengths;                                                    // length of lines within text area
    private final List<String>          values;                                                     // values waiting to be appended

    private int                         curLength;                                                  // length of current line
    private boolean                     clear;
    private boolean                     queue;

    Appender(DefaultStyledDocument txtara, int maxlin) {
        textArea =txtara;
        maxLines =maxlin;
        lengths  =new LinkedList<Integer>();
        values   =new ArrayList<String>();

        curLength=0;
        clear    =false;
        queue    =true;
        }

    synchronized void append(String val) {
        values.add(val);
        if(queue) { queue=false; EventQueue.invokeLater(this); }
        }

    synchronized void clear() {
        clear=true;
        curLength=0;
        lengths.clear();
        values.clear();
        if(queue) { queue=false; EventQueue.invokeLater(this); }
        }

    // MUST BE THE ONLY METHOD THAT TOUCHES textArea!
    public synchronized void run() {
        if(clear) { try {
			textArea.insertString(0,"",null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} }
        for(String val: values) {
            curLength+=val.length();
            if(val.endsWith(EOL1) || val.endsWith(EOL2)) {
                if(lengths.size()>=maxLines) { 
                	try {
					textArea.replace(0,lengths.removeFirst(),val,null);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} }
                lengths.addLast(curLength);
                curLength=0;
                }
            try {
				textArea.insertString(0,val,null);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            }
        values.clear();
        clear =false;
        queue =true;
        }

    static private final String         EOL1="\n";
    static private final String         EOL2=System.getProperty("line.separator",EOL1);
    }
}
