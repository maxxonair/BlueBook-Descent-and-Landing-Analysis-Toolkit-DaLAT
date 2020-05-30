package GUI.BlueBookPlot.serviceFunctions;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import GUI.BlueBookPlot.main.BlueBookPlot;


public class ImportSetupWindow {

	private static JFrame frame;
	private static String PROJECT_TITLE = "File Delimiter";
	private static int xInit = 300;
	private static int yInit = 80;
	
	private String[] delimiterList = {" ",
									  ";",
									  ",",
									  "$",
	};
	
    static Font small_font			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static Color bc_c = new Color(255,255,255);
    
	public JPanel createContentPane () throws IOException{
	    	JPanel MainGUI = new JPanel();
	    	MainGUI = new JPanel();
	    	MainGUI.setBackground(BlueBookPlot.getBackgroundColor());
	    	MainGUI.setLayout(new BorderLayout());
	    	
	    	
	    	JLabel delimiterLabel = new JLabel("Select File Delimiter:");
	    	delimiterLabel.setSize(200,30);
	    	delimiterLabel.setBackground(BlueBookPlot.getBackgroundColor());
	    	delimiterLabel.setForeground(BlueBookPlot.getLabelColor());
	    	MainGUI.add(delimiterLabel);
	    	
	    	@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox delimiterChoice = new JComboBox(delimiterList);
	    	delimiterChoice.setSelectedIndex(1);
	    	delimiterChoice.setSize(200,30);
	    	delimiterChoice.setLocation(30,30);
	    	delimiterChoice.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					BlueBookPlot.selectResultFile((Component) delimiterChoice);
					BlueBookPlot.setResultFileDelimiter(delimiterList[delimiterChoice.getSelectedIndex()]);
					frame.dispose();
				}
	    		
	    	});
	    	MainGUI.add(delimiterChoice);
	    	
	        
			return MainGUI;
	
	
	}
	
	public static void createAndShowGUI() throws IOException {
		
        JFrame.setDefaultLookAndFeelDecorated(false);
        frame = new JFrame("" + PROJECT_TITLE);
        frame.setFont(small_font);
        ImportSetupWindow demo = new ImportSetupWindow();
        JPanel tp = demo.createContentPane();
        tp.setPreferredSize(new java.awt.Dimension(xInit,yInit));
        frame.add(tp, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        //frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        frame.setVisible(true);
        // Create Icon image  -  top left for windows
    }
    
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException  e) {System.out.println(e);}
            }
        });
    }
}

