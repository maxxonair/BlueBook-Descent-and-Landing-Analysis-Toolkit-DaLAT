package Simulator_main;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import FlightElement.SpaceShip;
import Model.SensorModel;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;
import Sequence.MasterController;
import Sequence.SequenceContent;
import Simulator_main.DataSets.IntegratorData;
import Simulator_main.DataSets.RealTimeContainer;
import Simulator_main.DataSets.RealTimeResultSet;
import Simulator_main.DataSets.SimulatorInputSet;
import utils.CRateTransition;
import utils.ReadInput;
import utils.SRateTransition;

public class LaunchRealTimeSimulation {
	
    public static double PI    = 3.141592653589793238462643383279;   // PI       [-] 
	static double deg2rad 	   = PI/180.0; 					    		 //Convert degrees to radians
	static double rad2deg 	   = 180.0/PI; 					    		 //Convert radians to degrees
	
    static DecimalFormat decFormat = new DecimalFormat("#.###");    
	static boolean isPlot=false;
	
    public static void main(String[] args) throws IOException {
    	String timeStamp = new SimpleDateFormat("dd / MM / yy   HH : mm : ss").format(Calendar.getInstance().getTime());
    	System.out.println("------------------------------------------");
    	System.out.println(""+timeStamp);
    	System.out.println("------------------------------------------");
    	System.out.println("Reading :");
    	System.out.println("------------------------------------------");
    	//------------------------------------------------------------------------------------------
    	//					Compile Integrator inputs from files:
    	//------------------------------------------------------------------------------------------   	
    	List<SequenceContent> SequenceSet = ReadInput.readSequenceFile();    	
    	//------------------------------------------------------------------------------------------
	//double[] inputOut = ReadInput.readInput();
	//------------------------------------------------------------------------------------------
	//					Compile Integrator inputs from files:
	//------------------------------------------------------------------------------------------
	    	System.out.println("Read: Create simulation input file");

	    	SimulatorInputSet simulatorInputSet = ReadInput.readINP();
	    	SpaceShip spaceShip = simulatorInputSet.getSpaceShip();	    	
	    	IntegratorData integratorData = simulatorInputSet.getIntegratorData();
	    	OutputModel outputModel = new OutputModel(isPlot);
	    	
	    	// Set maximum integration Time limit
    		double tGlobal = integratorData.getMaxGlobalTime();
    		// Frequency of the Simulation environment 
    		// Ensure:  environmentFrequency > Sensor/Actuator Frequency
    		double environmentFrequency = integratorData.getEnvironmentFrequency(); 
    		double tIncrement = 1/environmentFrequency; 

	    		integratorData.setDegreeOfFreedom(6);  // manual override for now 
	    	
	    		// Set Environment model uncertainty settings 
	    		integratorData.getNoiseModel().setAtmosphereNoiseModel(true);
	    		//integratorData.getNoiseModel().setAerodynamicNoiseModel(true); // TBD
	    		//integratorData.getNoiseModel().setGravityNoiseModel(true);		 // TBD
	    		//integratorData.getNoiseModel().setRadiationNoiseModel(true);   // TBD
	    		
	    		//integratorData.getNoiseModel().setSensorNoiseModel(true);      // TBD
	    		integratorData.getNoiseModel().setActuatorNoiseModel(true);
	    		//--------------------------------------------------------------------------------------
	    		 SensorSet sensorSet = new SensorSet();
	    		// Rate Transition blocks - Spacecraft 
	    		spaceShip.getoBC().setControllerFrequency(10);
	    		spaceShip.getSensors().setSensorFrequency(20);
	    		// Sensor Rate Transition 
	    		SRateTransition sensorRateTransition     = new SRateTransition(environmentFrequency, 
	    				spaceShip.getSensors().getSensorFrequency());
	    		// Controller Rate Transition 
	    		CRateTransition controllerRateTransition = new CRateTransition(environmentFrequency, 
	    				spaceShip.getoBC().getControllerFrequency());
		    	//--------------------------------------------------------------------------------------
	    		ControlCommandSet controlCommandSet = new ControlCommandSet(); 
	    		//--------------------------------------------------------------------------------------	    		
	    		RealTimeResultSet realTimeResultSet = new RealTimeResultSet();	    		
	    		//--------------------------------------------------------------------------------------	 
	    	    ArrayList<String> steps = new ArrayList<String>();
    			RealTimeContainer realTimeContainer = new RealTimeContainer();	
    			//--------------------------------------------------------------------------------------	 
		        long startTime   = System.nanoTime();	
		    	System.out.println("------------------------------------------");
		    	System.out.println("Start SIMULATION :");
		    	System.out.println("------------------------------------------");
for(double tIS=0;tIS<tGlobal;tIS+=tIncrement) {
		    		
	    		//---------------------------------------------------------------------------------------
	    		//				  6 Degree of Freedom - Universal RealTime module
	    		//---------------------------------------------------------------------------------------
	    			if (tIS==0) {
			    		System.out.println("Simulator set and running");
			    		
			    		realTimeContainer = SimulationCore.launchIntegrator(
			    												    integratorData, 
																spaceShip,				 
																controlCommandSet
			    				);
	    			} else {
	    	    		//---------------------------------------------------------------------------------------
	    	    		//				  Update integration input for the new time step
	    	    		//---------------------------------------------------------------------------------------
		    			realTimeResultSet = realTimeContainer.getRealTimeResultSet();
		    				
		    	    		integratorData.setInitLongitude(realTimeResultSet.getLongitude());
		    	    		integratorData.setInitLatitude(realTimeResultSet.getLatitude());
		    	    		integratorData.setInitRadius(realTimeResultSet.getRadius());
		    	    		
		    	    		integratorData.setInitVelocity(realTimeResultSet.getVelocity());
		    	    		integratorData.setInitFpa(realTimeResultSet.getFpa());
		    	    		integratorData.setInitAzimuth(realTimeResultSet.getAzi());
		    	    		
		    	    		integratorData.setInitialQuaternion(realTimeResultSet.getQuaternion());
		    	    		integratorData.setInitRotationalRateX(realTimeResultSet.getPQR()[0][0]);
		    	    		integratorData.setInitRotationalRateY(realTimeResultSet.getPQR()[1][0]);
		    	    		integratorData.setInitRotationalRateZ(realTimeResultSet.getPQR()[2][0]);
		    	    		
		    	    		integratorData.setGlobalTime(tIS);
		    	   //---------------------------------------------------------------------------------------
		    	   //				    Get Master Controller Commands
		    	   //---------------------------------------------------------------------------------------  		

		    	    	    ControlCommandSet intSetOut = (ControlCommandSet) controllerRateTransition.get(
		    	    	    	MasterController.createMasterCommand(controlCommandSet, realTimeContainer, 
		    	    	    			realTimeResultSet.getMasterSet().getSpaceShip(), sensorSet, 
				    	    	    SequenceSet, environmentFrequency));
		    	    	    
		    	    	           try {
								controlCommandSet = (ControlCommandSet) intSetOut.clone();
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    	    		
		    	   //---------------------------------------------------------------------------------------
		    	   //				  Start incremental integration
		    	   //---------------------------------------------------------------------------------------	
		    	    		realTimeContainer = SimulationCore.launchIntegrator(
								integratorData, 
								realTimeResultSet.getMasterSet().getSpaceShip(),				 
								controlCommandSet
		    																			);	
	    			}
	  	      //---------------------------------------------------------------------------------------
	    	      //				       Create Sensor Data
	    	      //---------------------------------------------------------------------------------------	    		
	    		  sensorSet.setMasterSet(realTimeContainer.getRealTimeList().get(realTimeContainer.
	    				  getRealTimeList().size() - 1).getMasterSet());
	    		  sensorSet.setRealTimeResultSet(realTimeResultSet);
	    		  sensorSet.setGlobalTime(tIS);
	    		    	   SensorModel.addVelocitySensorUncertainty(sensorSet,  4);
	    		    			//SensorModel.addAltitudeSensorUncertainty(sensorSet,  5);
	    		    	   SensorModel.addIMUGiro(sensorSet, 0.5);  
	    		    	   
	    		    	   // Set Rate Transition 	
	    		    	   
		    	    	    SensorSet intSenOut = (SensorSet) sensorRateTransition.get(sensorSet);	    		    	   
	    	    	           try {
	    	    	        	   		sensorSet = (SensorSet) intSenOut.clone();
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
			  //---------------------------------------------------------------------------------------
			  //				  Add incremental integration result to write out file 
			  //---------------------------------------------------------------------------------------

	    		    	   for(int i=0;i<realTimeContainer.getRealTimeList().size();i++) {
	    		    		   integratorData.setGroundtrack(realTimeContainer.getRealTimeList().get(i).
	    		    				   getIntegratorData().getGroundtrack());
	    		    		   	steps = outputModel.addOutputTimestepData(steps, realTimeContainer, integratorData, sensorSet,  i);
	    		    		   //	System.out.println(i+"|"+realTimeContainer.getRealTimeList().get(i).getGlobalTime());    		   	
	    		    	   }
	  	      //---------------------------------------------------------------------------------------
	  		  //				  Implement Stop Handler here: 
	  	      //---------------------------------------------------------------------------------------	
		    			if(realTimeResultSet.getAltitude()<0) {
		    				break;
		    			} 
		  	  //---------------------------------------------------------------------------------------
		  	  //				  Approximate Delta-V per controller step 
		  	  //---------------------------------------------------------------------------------------	
		        double primaryDeltaVIncrement =realTimeContainer.getRealTimeList().
		        		get(realTimeContainer.getRealTimeList().size()-1).getMasterSet().
		        		getActuatorSet().getPrimaryISP_is()*9.80665*Math.abs(realTimeContainer.
		        				getRealTimeResultSet().getMasterSet().getSpaceShip().getPropulsion().getMassFlowPrimary())/
		        		realTimeContainer.getRealTimeResultSet().getSCMass()*tIncrement;
		         
		        
		       realTimeContainer.getRealTimeResultSet().getMasterSet().getSpaceShip().getPropulsion().setAccumulatedDeltaVPrimary(
		    	   realTimeContainer.getRealTimeResultSet().getMasterSet().getSpaceShip().getPropulsion().getAccumulatedDeltaVPrimary()+primaryDeltaVIncrement);
	    		}
		  	//---------------------------------------------------------------------------------------
		  	//				  Generate total time to integrate the problem
		  	//---------------------------------------------------------------------------------------	
				long endTime   = System.nanoTime();
				long totalTime = endTime - startTime;
				double  totalTime_sec = (double) (totalTime * 1E-9);  
		        System.out.println("Runtime: "+decFormat.format(totalTime_sec)+" seconds.");
		  	//---------------------------------------------------------------------------------------
		    //				 				 Create Result File
		  	//---------------------------------------------------------------------------------------	
	    		outputModel.createWriteOut(steps);
}

}
