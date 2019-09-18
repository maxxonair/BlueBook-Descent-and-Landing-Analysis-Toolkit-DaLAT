package Model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import Toolbox.Mathbox;


public class AtmosphereModel  {
//-------------------------------------------------------------------------------
//  %
//INPUT Files                                   %
//  %
//-------------------------------------------------------------------------------
public static String file_atm   = ""  ;
public static String DELIMITER = ",";
double is_value = 0 ; 
private static List<atm_dataset> ATM_DATA = new ArrayList<atm_dataset>(); 

public static double kB    = 1.380650424e-23;              // Boltzmann constant                         [SI]    
public static double PI    = 3.14159265359;                // Pi                                         [-]

public static double sigma = 1.6311e-9;     // Average collision diameter (<- check that again)

public static void  Set_File_Paths(int TARGET) throws URISyntaxException{
	String dir = System.getProperty("user.dir");
    if ( TARGET == 0){
    	file_atm = dir + "/ATM/atm_EARTH.csv";}
    if (  TARGET == 1 ){
    file_atm = dir + "/ATM/atm_MOON.csv";}
	if (  TARGET == 2 ){
	file_atm = dir + "/ATM/atm_MARS.csv";}
}

public static List<atm_dataset> INITIALIZE_ATM_DATA(int TARGET) throws URISyntaxException{
	Set_File_Paths( TARGET);
	   try{ // Temperature
		          FileInputStream fstream = new FileInputStream(file_atm);
		          DataInputStream in = new DataInputStream(fstream);
		          BufferedReader br = new BufferedReader(new InputStreamReader(in));
		          String strLine;
		          int k = 0;
		          while ((strLine = br.readLine()) != null )   {
if (k==0){
// Head line -> skip 	
} else {
		        	  double altitude = 0 ;
		        	  double density = 0;
		        	  double temperature = 0 ; 
		        	  double gasconstant = 0 ; 
		        	  double gamma = 0 ;

		   String[] tokens = strLine.split(DELIMITER);
		   altitude = Double.parseDouble(tokens[0]);		// Altitude
		   temperature = Double.parseDouble(tokens[2]); // Temperature 
		   density = Double.parseDouble(tokens[1]);		// density
		   gasconstant = Double.parseDouble(tokens[3]); // Gas cosntant 
		   gamma = Double.parseDouble(tokens[4]);		// Gamma 

		   atm_dataset ATM_DATASET = new atm_dataset( altitude,  density,  temperature,  gasconstant,  gamma); 
		   AtmosphereModel.ATM_DATA.add(ATM_DATASET);
		   //System.out.println(ATM_DATASET.get_altitude() + " | " + ATM_DATASET.get_density());
		   //Record record = new Record(tokens[0],tokens[1],tokens[2]);//process record , etc
}
		   k++;
		   }
		   fstream.close();
		   in.close();
		   br.close();
		   }catch (Exception e){
		     System.err.println("Error: " + e.getMessage());
		   }
	   return ATM_DATA;	
}

//----------------------------------------------------------------------------------------------------------------------------
//
//                                        Calculate Drag with three flowzone approach
//                                      Free molecular -> transitional -> Contiuum flow
//
//----------------------------------------------------------------------------------------------------------------------------
public static double load_Drag(double vel, double h, double P, double T, double CdC, double Lt, double R)
{
double CD = 0;
//double Lt = read_data(file_cship,2);
double Kn = kB * T / ( Math.sqrt(2) * PI * sigma * sigma * P * Lt );
if(Kn<0.1){
//               Continuum flow        <---------------
CD=CdC;
}
if(Kn>0.1 && Kn<10){
//               Transtional zone      <---------------
double S = vel / Math.sqrt(2 * R * T);
double Cdfm= 1.75 + Math.sqrt(PI)/(2 * S);
CD= CdC + ( Cdfm - CdC ) * ( 1/3 * Math.log10( Kn / Math.sin( PI / 6 ) ) * 0.5113 ) ;
}
if(Kn>10){
//               Free molecular zone   <---------------
double S = vel / Math.sqrt(2 * R * T);
CD= 1.75 + Math.sqrt(PI)/(2 * S);
}
return CD;
}
//----------------------------------------------------------------------------------------------------------------------------
public static int calc_flowzone( double vel, double h , double P , double T, double Lt)
{
double Kn = kB * T / ( Math.sqrt(2) * PI * sigma * sigma * P * Lt );
int flowzone = 0;
if(Kn<0.1){
flowzone = 1;
}
if(Kn>0.1 && Kn<10){
flowzone = 2;
}
if(Kn>10){
flowzone = 3;
}
return flowzone;
}

public static double get_CdC(double h, double AoA){
	double CdC = 1.55 ; 
	
	
	return CdC; 
}

public static double atm_read(int variable, double altitude) {
	double atm_read = 0;
	int leng = ATM_DATA.size();
	double data_x[] = new double[leng];
	double data_y[] = new double[leng];
	if (variable == 1){
		for (int i = 0;i<leng;i++){
			data_x[i] = ATM_DATA.get(i).get_altitude();
			data_y[i] = ATM_DATA.get(i).get_density();
			//System.out.println(leng + " | " + ATM_DATA.get(i).get_density());
		}
	} else if (variable == 2){
		for (int i = 0;i<leng;i++){
			data_x[i] = ATM_DATA.get(i).get_altitude();
			data_y[i] = ATM_DATA.get(i).get_temperature();
		}
	} else if (variable == 3){
		for (int i = 0;i<leng;i++){
			data_x[i] = ATM_DATA.get(i).get_altitude();
			data_y[i] = ATM_DATA.get(i).get_gasconstant();
		}
	} else if (variable == 4){
		for (int i = 0;i<leng;i++){
			data_x[i] = ATM_DATA.get(i).get_altitude();
			data_y[i] = ATM_DATA.get(i).get_gamma();
		}
	}
	atm_read = Mathbox.LinearInterpolate( data_x , data_y , altitude);
	return atm_read;
}

}