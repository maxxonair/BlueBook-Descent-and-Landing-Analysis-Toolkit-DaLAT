package GUI.RealTimeSimulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.apple.eawt.Application;

import GUI.BlueBookVisual;

public class RealTimeVisual {

	private static JFrame MAIN_frame;
	private static String PROJECT_TITLE = BlueBookVisual.getPROJECT_TITLE()+ " Real Time Simulation Setup";
	private static int xInit = 800;
	private static int yInit = 800;
	
    static Font small_font			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static Color bc_c = new Color(255,255,255);
    
	public JPanel createContentPane () throws IOException{
	    	JPanel MainGUI = new JPanel();
	    	MainGUI = new JPanel();
	    	MainGUI.setBackground(bc_c);
	    	MainGUI.setLayout(new BorderLayout());
			return MainGUI;
	
	
	}
	
	private static void createAndShowGUI() throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(false);
        MAIN_frame = new JFrame("" + PROJECT_TITLE);
        MAIN_frame.setFont(small_font);
        RealTimeVisual demo = new RealTimeVisual();
        JPanel tp = demo.createContentPane();
        tp.setPreferredSize(new java.awt.Dimension(xInit,yInit));
        MAIN_frame.add(tp, BorderLayout.CENTER);
        MAIN_frame.pack();
        //MAIN_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MAIN_frame.setLocationRelativeTo(null);
        //MAIN_frame.setExtendedState(MAIN_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        MAIN_frame.setVisible(true);
        // Create Icon image  -  top left for windows
         try {
        	BufferedImage myIcon = ImageIO.read(new File(BlueBookVisual.getICON_File())); 
        	MAIN_frame.setIconImage(myIcon);
         }catch(IIOException eIIO) {System.out.println(eIIO);}    
         // Create taskbar icon - for mac 
         try {
         Application application = Application.getApplication();
         Image image = Toolkit.getDefaultToolkit().getImage(BlueBookVisual.getICON_File());
         application.setDockIconImage(image);
         } catch(Exception e) {
        	 System.err.println("Taskbar icon could not be created");
         }
    }
    
    public static void main() throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException  e) {System.out.println(e);}
            }
        });
    }
}
