package Simulator_main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Sequence.SequenceElement;
import Simulator_main.EquationsOfMotion_3DOF;

public class SIM implements ActionListener{
	
   	static double[] x_init =new double[20];	
   	public static String INPUT_FILE;
   	public static boolean eclipse_run = false;
    public static double PI    = 3.14159265359;                // PI                                       [-]
    public static double kB    = 1.380650424e-23;              // Boltzmann constant                         [SI]    
    public static double G = 1.48808E-34;
    public static int TARGET; 
	static double deg = PI/180.0; 		//Convert degrees to radians
	static double rad = 180/PI; 		//Convert radians to degrees
	
   	//System.out.println(INPUT_FILE);
	private static List<SequenceElement> SEQUENCE_DATA = new ArrayList<SequenceElement>(); 
	
	public static void UPDATE_SequenceElements(SequenceElement NewElement){	   
		   if (SEQUENCE_DATA.size()==0){
				  SIM.SEQUENCE_DATA.add(NewElement); 
		   } else {
			boolean element_exist = false   ;
			  for(int i=0; i<SEQUENCE_DATA.size(); i++){
				  int ID_LIST    = SEQUENCE_DATA.get(i).get_sequence_ID();
				  int ID_ELEMENT = NewElement.get_sequence_ID();
						  if (ID_LIST == ID_ELEMENT){
							  // item exists -> Update
							  SEQUENCE_DATA.get(i).Update(NewElement.get_sequence_ID(),NewElement.get_trigger_start_type(),NewElement.get_trigger_end_type(), NewElement.get_trigger_start_value(),NewElement.get_trigger_end_value(),NewElement.get_sequence_type(),NewElement.get_sequence_controller_ID());
							  element_exist = true;
						  } 
			  }
			if (element_exist == false ){
				  // New item -> add to list  
				SIM.SEQUENCE_DATA.add(NewElement);
			}	  
		   } 
	   }
	
    public static boolean READ_INPUT() throws IOException{
   String dir = System.getProperty("user.dir");
       	if(eclipse_run) {
       	   	 INPUT_FILE = dir+"/LandingSim-3DOF/INP/init.inp";} else {
       	   	 INPUT_FILE = ".\\INP\\init.inp";}
       	//System.out.println(dir);
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
        	//System.out.println("" +k+"   "+InitialState);
        	k++;
        }
        fstream.close();
        in.close();
        br.close();
        read_state = true;
        } catch(NullPointerException eNPE) { System.out.println(eNPE);}
        return read_state;
    }
    
    public static String[] READ_SEQUENCE() throws IOException{
    	 String[] OUTPUT =new String[20];	
    	 String dir = System.getProperty("user.dir");
    	 int val1=0;int val2=0;int val3=0;double val4=0;double val5=0;int val6=0;int val7=0; 
       	if(eclipse_run) {
       	   	 INPUT_FILE = dir+"/LandingSim-3DOF/INP/sequence_1.inp";} else {
       	   	 INPUT_FILE = ".\\INP\\sequence_1.inp";}
		BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
        String strLine;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String[] tokens = strLine.split(" ");
        	SequenceElement newSequenceElement = new SequenceElement( val1,  val2,  val3,  val4,  val5,  val6,  val7);
        	int sequence_ID 			= Integer.parseInt(tokens[0]);
        	int trigger_start_type 		= Integer.parseInt(tokens[1]);
        	int trigger_end_type 		= Integer.parseInt(tokens[2]);
        	double trigger_start_value 	= Double.parseDouble(tokens[3]);
        	double trigger_end_value 	= Double.parseDouble(tokens[4]);
        	int sequence_type		 	= Integer.parseInt(tokens[5]);
        	int sequence_controller_ID 	= Integer.parseInt(tokens[6]);
        	newSequenceElement.Update( sequence_ID,trigger_start_type,trigger_end_type,trigger_start_value,trigger_end_value,sequence_type,sequence_controller_ID);
            UPDATE_SequenceElements(newSequenceElement);
        }
        br.close();
        } catch(NullPointerException eNPE) { System.out.println(eNPE);}
        return OUTPUT;
    }

    public static void main(String[] args) throws IOException {
    	READ_SEQUENCE();
    	for(int i=0;i<SEQUENCE_DATA.size();i++) {
    		System.out.println(SEQUENCE_DATA.get(i).get_sequence_ID());
    	}
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
	boolean inp_read_success = READ_INPUT();
		if(inp_read_success) { 
	    	int INTEGRATOR=(int) x_init[8];
	    	int target=(int) x_init[9];
	    	int ctrl_targetcurve = (int) x_init[12];
	    	double rm = EquationsOfMotion_3DOF.DATA_MAIN[target][0];
	    	//System.out.println(target+" "+ rm);
			//System.out.println("Start init: \n"+INTEGRATOR+"\n"+target+"\n"+(x_init[0]*deg)+"\n"+(x_init[1]*deg)+"\n"+(x_init[2]+rm)+"\n"+x_init[3]+"\n"+(x_init[4]*deg)+"\n"+(x_init[5]*deg)+"\n"+(x_init[6])+"\n"+x_init[7]+"\n End init \n");
			EquationsOfMotion_3DOF.Launch_Integrator(INTEGRATOR, target, x_init[0]*deg, x_init[1]*deg, x_init[2]+rm, x_init[3], x_init[4]*deg, x_init[5]*deg, x_init[6], x_init[7], x_init[10],x_init[11],ctrl_targetcurve, SEQUENCE_DATA);
		}else {
			System.out.println("Reading Input file failed. Integrator Stop.");
		}
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}