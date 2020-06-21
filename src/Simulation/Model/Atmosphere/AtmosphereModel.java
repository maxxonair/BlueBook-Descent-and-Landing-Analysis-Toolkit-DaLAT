package Simulation.Model.Atmosphere;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import FlightElement.SpaceShip;
import Simulation.Model.DataSets.AtmosphereSet;
import Simulation.Noise.AtmosphereNoiseModel;
import Simulator_main.DataSets.IntegratorData;
import utils.Mathbox;


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


public static double sigma = 1.6311e-9;     // Average collision diameter (<- TBC)

private static long noiseSeed = 5567;

private static AtmosphereNoiseModel atmoshpereNoiseModl = new AtmosphereNoiseModel(noiseSeed);


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
			  // System.err.println("Error initialising ATM data.");
			   //System.err.println(e);
		   }
	   return ATM_DATA;	
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

public static AtmosphereSet getAtmosphereSet(SpaceShip spaceShip, IntegratorData integratorData) {
	AtmosphereSet atmosphereSet = new AtmosphereSet();
	double altitude = spaceShip.getState().getxIS()[2] - spaceShip.getTarget().getRM() ; 
	if (altitude>160000 || spaceShip.getTarget().getTARGET() == 1){ // In space conditions: 
		// Set atmosphere properties to zero: 
		atmosphereSet.setDensity(0);  						    // Density 							[kg/m3]
		atmosphereSet.setDynamicPressure(0);  					// Dynamic pressure 				[Pa]
    		atmosphereSet.setStaticTemperature(0); 				// Temperature 						[K]
    		atmosphereSet.setGamma(0);  						// Heat capacity ratio 				[-]
    		atmosphereSet.setGasConstant(0);	 				// Gas constant 					[J/kgK]
    		atmosphereSet.setMach(0); 	  						// Mach number 						[-]
      	//----------------------------------------------------------------------------------------------
	} else { // In atmosphere conditions (if any)

		if(integratorData.getNoiseModel().isAtmosphereNoiseModel()) {
			atmoshpereNoiseModl.setDensityNoise(atmosphereSet, altitude);
			atmoshpereNoiseModl.setStaticTemperatureNoise(atmosphereSet, altitude);
		} else {
			atmosphereSet.getAtmosphereNoiseSet().setDensityNoise(0);
			atmosphereSet.getAtmosphereNoiseSet().setStaticTemperatureNoise(0);
		}
		
		double density = AtmosphereModel.atm_read(1, altitude)+AtmosphereModel.atm_read(1, altitude)*atmosphereSet.getAtmosphereNoiseSet().getDensityNoise();
		double staticTemperature = AtmosphereModel.atm_read(2, altitude) + atmosphereSet.getAtmosphereNoiseSet().getStaticTemperatureNoise();
		if(staticTemperature<0) {staticTemperature=0;}
		double gamma = AtmosphereModel.atm_read(4, altitude);
		double gasConstant = AtmosphereModel.atm_read(3, altitude );
		atmosphereSet.setDensity(density);        													 // density                         [kg/m3]
		atmosphereSet.setDynamicPressure(0.5 * atmosphereSet.getDensity() * ( spaceShip.getState().getV_NED_ECEF_spherical()[0] * spaceShip.getState().getV_NED_ECEF_spherical()[0]));     // Dynamic pressure                [Pa]
		atmosphereSet.setStaticTemperature(staticTemperature); 
		atmosphereSet.setGamma(gamma) ;              	        										// Heat capacity ratio			   [-]
		atmosphereSet.setGasConstant(gasConstant);                       								// Gas Constant                    [J/kgK]
		atmosphereSet.setStaticPressure(density*gasConstant*staticTemperature);   																			// Ambient pressure 			   [Pa]
		atmosphereSet.setMach(spaceShip.getState().getV_NED_ECEF_spherical()[0] / Math.sqrt( staticTemperature * gamma * gasConstant));                   		 			// Mach number                     [-]   		             						// Parachute Drag coefficient      [-]
        // Continous/Transition/Free molecular flow [-]
	}
return atmosphereSet; 
}

}