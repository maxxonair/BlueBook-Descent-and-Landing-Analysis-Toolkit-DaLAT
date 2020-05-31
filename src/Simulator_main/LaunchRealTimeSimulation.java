package Simulator_main;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import FlightElement.SpaceShip;
import FlightElement.GNCModel.ControlCommandSet;
import FlightElement.SensorModel.SensorSet;
import Simulator_main.DataSets.IntegratorData;
import Simulator_main.DataSets.RealTimeContainer;
import Simulator_main.DataSets.RealTimeResultSet;
import Simulator_main.DataSets.SimulatorInputSet;
import utils.CRateTransition;
import utils.ReadInput;
import utils.SRateTransition;

public class LaunchRealTimeSimulation {
	
	
    static DecimalFormat decFormat = new DecimalFormat("#.###");    
	static boolean isPlot=false;
	
    public static void main(String[] args) throws IOException {
    	String timeStamp = new SimpleDateFormat("dd / MM / yy   HH : mm : ss").format(Calendar.getInstance().getTime());
    	System.out.println("------------------------------------------");
    	System.out.println(""+timeStamp);
    	System.out.println("------------------------------------------");
    	System.out.println("Reading ... ");
    	System.out.println("------------------------------------------");
    	//------------------------------------------------------------------------------------------
		//double[] inputOut = ReadInput.readInput();
		//------------------------------------------------------------------------------------------
		//					Compile Integrator inputs from files:
		//------------------------------------------------------------------------------------------
	    	System.out.println("Read: Create simulation input file");
	    	SimulatorInputSet simulatorInputSet = ReadInput.readINP();
	    	//------------------------------------------------------------------------------------------
			//		>>> Create Spacecraft Model 
			//------------------------------------------------------------------------------------------
	    	SpaceShip spaceShip = simulatorInputSet.getSpaceShip();	   
	    	//------------------------------------------------------------------------------------------
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
	    		integratorData.getNoiseModel().setAerodynamicNoiseModel(true); 
	    		//integratorData.getNoiseModel().setGravityNoiseModel(true);		 // TBD
	    		//integratorData.getNoiseModel().setRadiationNoiseModel(true);   	 // TBD
	    		
	    		//integratorData.getNoiseModel().setSensorNoiseModel(true);      	 // TBD
	    		integratorData.getNoiseModel().setActuatorNoiseModel(true);
	    		//--------------------------------------------------------------------------------------
	        	spaceShip.getProperties().getSequence().setSequenceSet(ReadInput.readSequenceFile());
	    		// Rate Transition blocks - Spacecraft 
	    		spaceShip.getProperties().getoBC().setControllerFrequency(10);
	    		spaceShip.getProperties().getSensors().setSensorFrequency(20);
	    		// Sensor Rate Transition 
	    		SRateTransition sensorRateTransition     = new SRateTransition(environmentFrequency, 
	    				spaceShip.getProperties().getSensors().getSensorFrequency());
	    		// Controller Rate Transition 
	    		CRateTransition controllerRateTransition = new CRateTransition(environmentFrequency, 
	    				spaceShip.getProperties().getoBC().getControllerFrequency());
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
			    		
			    		realTimeContainer = SimulationCore.launchIntegrator(integratorData, spaceShip);
	    			} else {
	    	    		//---------------------------------------------------------------------------------------
	    	    		//				  Update integration input for the new time step
	    	    		//---------------------------------------------------------------------------------------
		    			realTimeResultSet = realTimeContainer.getRealTimeResultSet();
		    				
		    	    		spaceShip.getState().setInitLongitude(realTimeResultSet.getLongitude());
		    	    		spaceShip.getState().setInitLatitude(realTimeResultSet.getLatitude());
		    	    		spaceShip.getState().setInitRadius(realTimeResultSet.getRadius());
		    	    		
		    	    		spaceShip.getState().setInitVelocity(realTimeResultSet.getVelocity());
		    	    		spaceShip.getState().setInitFpa(realTimeResultSet.getFpa());
		    	    		spaceShip.getState().setInitAzimuth(realTimeResultSet.getAzi());
		    	    		
		    	    		spaceShip.getState().setInitialQuaternion(realTimeResultSet.getQuaternion());
		    	    		spaceShip.getState().setInitRotationalRateX(realTimeResultSet.getPQR()[0][0]);
		    	    		spaceShip.getState().setInitRotationalRateY(realTimeResultSet.getPQR()[1][0]);
		    	    		spaceShip.getState().setInitRotationalRateZ(realTimeResultSet.getPQR()[2][0]);
		    	    		
		    	    		integratorData.setGlobalTime(tIS);
		    	   //---------------------------------------------------------------------------------------
		    	   //				    Get Master Controller Commands
		    	   //---------------------------------------------------------------------------------------  		

		    	    	    ControlCommandSet intSetOut = (ControlCommandSet) controllerRateTransition.get(
		    	    	    	spaceShip.getgNCModel().getMasterController().createMasterCommand(spaceShip));
		    	    	    
	    	    	           try {
							 spaceShip.getgNCModel().setControlCommandSet( (ControlCommandSet) intSetOut.clone() );
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    	    	    
		    	   //---------------------------------------------------------------------------------------
		    	   //				  Start incremental integration
		    	   //---------------------------------------------------------------------------------------	
		    	    		realTimeContainer = SimulationCore.launchIntegrator(
								integratorData, 
								realTimeResultSet.getMasterSet().getSpaceShip()
		    																			);	
	    			}
	    		  //---------------------------------------------------------------------------------------
	    	      //				       Create Sensor Data
	    	      //---------------------------------------------------------------------------------------	    		
	    		  spaceShip.getSensorModel().getSensorSet().setMasterSet(realTimeContainer.getRealTimeList().get(realTimeContainer.
	    				  getRealTimeList().size() - 1).getMasterSet());
	    		  
	    		  spaceShip.getSensorModel().getSensorSet().setRealTimeResultSet(realTimeResultSet);
	    		  spaceShip.getSensorModel().getSensorSet().setGlobalTime(tIS);
	    		    	   spaceShip.getSensorModel().addVelocitySensorUncertainty(4);
	    		    			//SensorModel.addAltitudeSensorUncertainty(spaceShip.getSensorMode().getSensorSet(),  5);
	    		    	   spaceShip.getSensorModel().addIMUGiro(0.5);  
	    		    	   
	    		    	   // Set Rate Transition 	
	    		    	   
		    	    	    SensorSet intSenOut = (SensorSet) sensorRateTransition.get(spaceShip.getSensorModel().getSensorSet());	    		    	   
	    	    	           try {
	    	    	        	   		spaceShip.getSensorModel().setSensorSet( (SensorSet) intSenOut.clone() );
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
	    		    		   	steps = outputModel.addOutputTimestepData(steps, realTimeContainer, integratorData, spaceShip.getSensorModel().getSensorSet(),  i);
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
		        				getRealTimeResultSet().getMasterSet().getSpaceShip().getProperties().getPropulsion().getMassFlowPrimary())/
		        		realTimeContainer.getRealTimeResultSet().getSCMass()*tIncrement;
		         
		        
		       realTimeContainer.getRealTimeResultSet().getMasterSet().getSpaceShip().getProperties().getPropulsion().setAccumulatedDeltaVPrimary(
		    	   realTimeContainer.getRealTimeResultSet().getMasterSet().getSpaceShip().getProperties().getPropulsion().getAccumulatedDeltaVPrimary()+primaryDeltaVIncrement);
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
