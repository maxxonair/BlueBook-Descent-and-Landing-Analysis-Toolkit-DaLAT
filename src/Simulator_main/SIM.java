package Simulator_main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import Simulator_main.EquationsOfMotion_3DOF;

public class SIM implements ActionListener{
	
   	static double[] x_init =new double[8];	
   	public static String INPUT_FILE;
   	public static boolean eclipse_run = false;
    public static double PI    = 3.14159265359;                // PI                                       [-]
    public static double kB    = 1.380650424e-23;              // Boltzmann constant                         [SI]    
    public static double G = 1.48808E-34;
    public static int TARGET; 
	static double deg = PI/180.0; 		//Convert degrees to radians
	static double rad = 180/PI; 		//Convert radians to degrees
	
   	//System.out.println(INPUT_FILE);
	
    public static boolean READ_INPUT() throws IOException{
   String dir = System.getProperty("user.dir");
       	if(eclipse_run) {
       	   	 INPUT_FILE = dir+"/LandingSim-3DOF/inp.txt";} else {
       	   	 INPUT_FILE = "inp.txt";}
    	double InitialState = 0;
    	boolean read_state = false;
	    FileInputStream fstream = null;
	    try{
	    	fstream = new FileInputStream(INPUT_FILE);
	    } catch(IOException eIO) { System.out.println(eIO);}
        DataInputStream in = new DataInputStream(fstream);
        @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int k = 0;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String[] tokens = strLine.split(" ");
        	InitialState = Double.parseDouble(tokens[0]);
        	x_init[k]= InitialState;
        	System.out.println("" +k+"   "+InitialState);
        	k++;
        }
        read_state = true;
        } catch(NullPointerException eNPE) { System.out.println(eNPE);}
        return read_state;
    }

    public static void main(String[] args) throws IOException {
    	//-----------------------------------------
    	//          INTEGRATOR
    //	0 Dormand Prince 853 Integrator
    //  1 Standard Runge Kutta Integrator
    //  2 Gragg-Bulirsch-Stoer Integrator
    //  3 Adams-Bashforth Integrator
    	//-----------------------------------------
    	//           target
    	//  0 Earth
    	//  1 Moon
    	//  2 Mars
    	//  3 Venus
    	//-----------------------------------------
        //final String dir = System.getProperty("user.dir");
        //System.out.println("current dir = " + dir);
    	double rm = EquationsOfMotion_3DOF.SET_Constants(TARGET);
    	int INTEGRATOR=0;
    	int target=1;
	boolean inp_read_success = READ_INPUT();
		if(inp_read_success) { 
			EquationsOfMotion_3DOF.Launch_Integrator(INTEGRATOR, target, x_init[0]*deg, x_init[1]*deg, x_init[2]+rm, x_init[3], x_init[4]*deg, x_init[5]*deg, x_init[6]*deg, x_init[7]);
		}else {
			System.out.println("Reading Input file failed. Integrator Stop.");
		}
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}