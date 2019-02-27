package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ElevationReadTest{
	
	public static String Elevation_File = "\\ELEVATION\\MOON_Elevation.xyz.csv";
	public static String Elevation_File_mac = "/ELEVATION/MOON_Elevation.xyz.csv";
	public static boolean macrun = true; 
	
	public static void ScanElevationFile(String InputFile) throws IOException {
		FileInputStream inputStream = null;
		Scanner sc  = null;
		String path = InputFile;
		try {
		    inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        String[] tokens = line.split(" ");
		       // System.out.println(tokens[0]+"|"+tokens[1]+"|"+tokens[2]);
		    }
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}	
	}
	
	public static void main(String[] args)  {
		long startTime = System.nanoTime();
	     if(macrun) {
	    	 String dir = System.getProperty("user.dir");
	    	 Elevation_File = dir + Elevation_File_mac ;
	     }
		try {
			ScanElevationFile(Elevation_File);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("finished: "+totalTime);
	}
	

}