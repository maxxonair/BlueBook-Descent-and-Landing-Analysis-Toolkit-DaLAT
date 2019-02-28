package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("unused")
public class ElevationReadTest{
	
	public static String Elevation_File = "\\ELEVATION\\LOLA_4.csv";
	public static String Elevation_File_mac = "/ELEVATION/LOLA_4.csv";
	public static String Elevation_File2 = "\\ELEVATION\\LOLA.csv";
	public static String Elevation_File2_mac = "/ELEVATION/LOLA.res4";
	public static boolean macrun = true; 
	public static long startTime;
	public static long k =0;
	public static double max_runtime = 2; 
	public static double columns = 0; 
	public static void ScanElevationFile(String InputFile) throws IOException {
		FileInputStream inputStream = null;
		Scanner sc  = null;
		String path = InputFile;
		k =0; 
		try {
		    inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        @SuppressWarnings("unused")
				String[] tokens = line.split(",");
		        //System.out.println(tokens[0]+"|"+tokens[1]+"|"+tokens[2]);
				long endTime   = System.nanoTime();
				long totalTime = endTime - startTime;
				double  totalTime_sec = (double) (totalTime * 1E-9);
				columns = tokens.length; 
				if(totalTime_sec>max_runtime) {System.out.println("Break");break;}
				k++;
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
	public static double GetLocalElevation(String InputFile, double longitude, double latitude) throws IOException {
		double ELEVATION = 0;
		int resolution = 4;
		int latitude_indx = (int) ((90-latitude)*resolution+1);
		int longitude_indx = (int) (longitude*resolution+1);
		FileInputStream inputStream = null;
		Scanner sc  = null;
		String path = InputFile;
		boolean TargetNOTReached=true; 
		k =0; 
		try {
		    inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
		    while (sc.hasNextLine() && TargetNOTReached) {
		        String line = sc.nextLine();
		        @SuppressWarnings("unused")
				String[] tokens = line.split(",");
		        columns = tokens.length; 
		        if(latitude_indx==k) {
		        	ELEVATION = Double.parseDouble(tokens[longitude_indx]);
		        	System.out.println(latitude_indx+"|"+longitude_indx+"|"+ELEVATION);
		        	TargetNOTReached=false;
		        	return ELEVATION;
		        }
				long endTime   = System.nanoTime();
				long totalTime = endTime - startTime;
				double  totalTime_sec = (double) (totalTime * 1E-9);
				if(totalTime_sec>max_runtime) {System.out.println("Break");break;}
				k++;
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
		return ELEVATION;
	}
	public static void BinaryScanTest(String InputFile) throws IOException {
		Path path = Paths.get(InputFile);
		byte[] fileContents =  Files.readAllBytes(path);
		for(int i=0;i<fileContents.length;i++) {
			System.out.println(""+fileContents[i]);
		}
	}
	
	public static void main(String[] args)  {
		 startTime = System.nanoTime();
	     if(macrun) {
	    	 String dir = System.getProperty("user.dir");
	    	 Elevation_File = dir + Elevation_File_mac ;
	    	 Elevation_File2 = dir + Elevation_File2_mac ;
	     }
		try {
			GetLocalElevation(Elevation_File,135,-65);
			//BinaryScanTest(Elevation_File);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		double  totalTime_sec = (double) (totalTime * 1E-9);
		double  totalTime_min = (double) totalTime_sec/60;
		System.out.println("Runtime: "+totalTime_sec+" seconds");
		//System.out.println(" "+(double) ((double) k/1000000)+" million lines read");
		//System.out.println(" "+(double) ((double) k/1000)+" thousand lines read");
		System.out.println(" "+(double) ((double) k/1)+"  lines read");
		System.out.println(" "+(double) ((double) columns/1)+"  columns per line read");
	}
	

}