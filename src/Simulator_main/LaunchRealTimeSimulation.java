package Simulator_main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import FlightElement.SpaceShip;
import Model.SensorModel;
import Model.DataSets.ActuatorSet;
import Model.DataSets.AerodynamicSet;
import Model.DataSets.AtmosphereSet;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.ForceMomentumSet;
import Model.DataSets.GravitySet;
import Model.DataSets.MasterSet;
import Model.DataSets.SensorSet;
import Plotter.DataContainer;
import Plotter.DataSetXY;
import Plotter.Pair;
import Plotter.PlotXY;
import Sequence.MasterController;
import Sequence.SequenceContent;
import Simulator_main.DataSets.IntegratorData;
import Simulator_main.DataSets.RealTimeContainer;
import Simulator_main.DataSets.RealTimeResultSet;
import Simulator_main.DataSets.SimulatorInputSet;
import utils.CRateTransition;
import utils.Quaternion;
import utils.ReadInput;
import utils.SRateTransition;

public class LaunchRealTimeSimulation {
	
    public static double PI    = 3.141592653589793238462643383279;   // PI       [-] 
	static double deg2rad 	   = PI/180.0; 					    //Convert degrees to radians
	static double rad2deg 	   = 180.0/PI; 					    //Convert radians to degrees
	
    static DecimalFormat decFormat = new DecimalFormat("#.###");
    
    static SensorSet sensorSet = new SensorSet();
    
	static DataContainer dataContainer = new DataContainer();
	static DataSetXY dataSet =  new DataSetXY();
	static boolean isPlot=false;
	
    public static void main(String[] args) throws IOException {
    	String timeStamp = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("------------------------------------------");
    	System.out.println(""+timeStamp);
    	System.out.println("------------------------------------------");
    	System.out.println("Start READ :");
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
	    	//double rm = SimulationCore.DATA_MAIN[(int) inputOut[9]][0];
	    	//--------------------------------------------------------------------------------------
	    	System.out.println("READ: Create simulation input file");

	    	SimulatorInputSet simulatorInputSet = ReadInput.readINP();
	    	SpaceShip spaceShip =  simulatorInputSet.getSpaceShip();
	    	
	    	IntegratorData integratorData = simulatorInputSet.getIntegratorData();
	    	
	    	// Set maximum integration Time limit
    		double tGlobal = integratorData.getMaxGlobalTime();
    		// Frequency of the Simulation environment 
    		// Ensure:  environmentFrequency > Sensor/Actuator Frequency
    		double environmentFrequency = integratorData.getEnvironmentFrequency(); 
    		double tIncrement = 1/environmentFrequency; 

	    		integratorData.setDegreeOfFreedom(6);  // manual overwrite for now 
	    	
	    		// Set Environment model uncertainty settings 
	    		integratorData.getNoiseModel().setAtmosphereNoiseModel(true);
	    		//integratorData.getNoiseModel().setAerodynamicNoiseModel(true);
	    		//integratorData.getNoiseModel().setGravityNoiseModel(true);
	    		//integratorData.getNoiseModel().setRadiationNoiseModel(true);
	    		
	    		//integratorData.getNoiseModel().setSensorNoiseModel(true);
	    		integratorData.getNoiseModel().setActuatorNoiseModel(true);
	    		//--------------------------------------------------------------------------------------
	    		// Rate Transition blocks - Spacecraft 
	    		
	    		SRateTransition sensorRateTransition     = new SRateTransition(environmentFrequency, 
	    				spaceShip.getSensors().getSensorFrequency());
	    		
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
			    		
			    		realTimeContainer = RealTimeSimulationCore.launchIntegrator(
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

		    	    		realTimeContainer = RealTimeSimulationCore.launchIntegrator(
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
	    		    	   sensorSet = (SensorSet) sensorRateTransition.get(sensorSet);
	    		    	   
			  //---------------------------------------------------------------------------------------
			  //				  Add incremental integration result to write out file 
			  //---------------------------------------------------------------------------------------

	    		    	   for(int i=0;i<realTimeContainer.getRealTimeList().size();i++) {
	    		    		   integratorData.setGroundtrack(realTimeContainer.getRealTimeList().get(i).
	    		    				   getIntegratorData().getGroundtrack());
	    		    		   	steps = addOutputTimestepData(steps, realTimeContainer, integratorData, i);
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
	    		createWriteOut(steps);
	    		if(isPlot) {
	    		dataContainer.addDataSet(dataSet);
	    		PlotXY plot = new PlotXY();
	    		plot.plot(dataContainer);
	    		}
}
    
private static void createWriteOut(ArrayList<String> steps) {
        try{
            String resultpath="";
            	String dir = System.getProperty("user.dir");
            	resultpath = dir + "/results.txt";
            PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
            for(String step: steps) {
                writer.println(step);
            }
            System.out.println("WRITE: Result file. Done."); 
            System.out.println("------------------------------------------");
            writer.close();
        } catch(Exception e) {System.out.println("ERROR: Writing result file failed");System.out.println(e);};
}

private static ArrayList<String> addOutputTimestepData(ArrayList<String> steps, RealTimeContainer realTimeContainer, 
										 IntegratorData integratorData, int subIndx) {
	RealTimeResultSet realTimeResultSet = realTimeContainer.getRealTimeList().get(subIndx);
	MasterSet masterSet = realTimeContainer.getRealTimeList().get(subIndx).getMasterSet(); 
	AtmosphereSet atmosphereSet = masterSet.getAtmosphereSet();
	AerodynamicSet aerodynamicSet = masterSet.getAerodynamicSet();
	GravitySet gravitySet = masterSet.getGravitySet();
	ControlCommandSet controlCommandSet = masterSet.getControlCommandSet();
	ForceMomentumSet forceMomentumSet = masterSet.getForceMomentumSet();
	ActuatorSet actuatorSet = masterSet.getActuatorSet();
	SpaceShip spaceShip = masterSet.getSpaceShip();
	if(isPlot) {
	dataSet.addPair(new Pair((integratorData.getGlobalTime()+realTimeContainer.getRealTimeList().get(subIndx).getTime()), 
			masterSet.getSpaceShip().getMass()));
	//	dataSet.addPair(new Pair(realTimeResultSet.getAltitude(), 
	//			aerodynamicSet.getFlowzone()));
	dataContainer.setxAxisLabel("Time");
	dataContainer.setyAxisLabel("Noise");
	}
	
	Quaternion qVector = realTimeResultSet.getQuaternion();
	//qVector.normalize();
							  
	//System.out.println(realTimeContainer.getRealTimeSet().size());
	// integratorData.getGlobalTime()+realTimeContainer.getRealTimeList().get(subIndx).getTime())
	steps.add(realTimeResultSet.getGlobalTime() + " " + 
    			realTimeResultSet.getLongitude() + " " + 
    			realTimeResultSet.getLatitude() + " " + 
    			realTimeResultSet.getAltitude() + " " + 
    			realTimeResultSet.getAltitude()+ " " + 
    			realTimeResultSet.getRadius() + " " + 
    			realTimeResultSet.getVelocity()+ " " + 
    			realTimeResultSet.getFpa() + " " + 
    			realTimeResultSet.getAzi() + " " +     			
      		  atmosphereSet.getDensity() + " " + 
      		  atmosphereSet.getStaticTemperature()+ " " +
      		  atmosphereSet.getMach()+ " " +
      		  atmosphereSet.getGamma()+ " " +
      		  atmosphereSet.getGasConstant()+ " " +
      		  atmosphereSet.getStaticPressure()+ " " +
      		  atmosphereSet.getDynamicPressure()+ " " +
      		  aerodynamicSet.getFlowzone()+ " " +
      		  aerodynamicSet.getDragCoefficient()+ " " +
      		  aerodynamicSet.getLiftCoefficient()+ " " +
      		  aerodynamicSet.getSideForceCoefficient()+" "+
      		  aerodynamicSet.getDragForce() + " " +
      		  aerodynamicSet.getLiftForce() + " " +
      		  aerodynamicSet.getSideForce() + " " +
      		  aerodynamicSet.getAerodynamicAngleOfAttack()+" "+
      		  aerodynamicSet.getAerodynamicBankAngle()+ " " +     		
      		  gravitySet.getG_NED()[0][0]+" "+
      		  gravitySet.getG_NED()[1][0]+" "+
      		  gravitySet.getG_NED()[2][0]+" "+
      		  Math.sqrt(gravitySet.getG_NED()[0][0]*gravitySet.getG_NED()[0][0] + gravitySet.getG_NED()[1][0]*gravitySet.getG_NED()[1][0] + gravitySet.getG_NED()[2][0]*gravitySet.getG_NED()[2][0])+" "+     		  
      		  forceMomentumSet.getF_total_NED()[0][0]+" "+
    		  forceMomentumSet.getF_total_NED()[1][0]+" "+
    		  forceMomentumSet.getF_total_NED()[2][0]+" "+
    		  forceMomentumSet.getF_Aero_A()[0][0]+" "+
    		  forceMomentumSet.getF_Aero_A()[1][0]+" "+
    		  forceMomentumSet.getF_Aero_A()[2][0]+" "+
    		  forceMomentumSet.getF_Thrust_NED()[0][0]+" "+
    		  forceMomentumSet.getF_Thrust_NED()[1][0]+" "+
    		  forceMomentumSet.getF_Thrust_NED()[2][0]+" "+
    		  forceMomentumSet.getF_Gravity_NED()[0][0]+" "+
    		  forceMomentumSet.getF_Gravity_NED()[1][0]+" "+
    		  forceMomentumSet.getF_Gravity_NED()[2][0]+" "+
      		  realTimeResultSet.getCartesianPosECEF()[0]+" "+
      		  realTimeResultSet.getCartesianPosECEF()[1]+" "+
      		  realTimeResultSet.getCartesianPosECEF()[2]+" "+
      		  0 + " " + 
      		  0 + " " + 
      		  0 + " " +       	 	  
      		qVector.w+" "+
      		qVector.x+" "+
      		qVector.y+" "+
      		qVector.z+" "+
  		  realTimeResultSet.getPQR()[0][0]+" "+
  		  realTimeResultSet.getPQR()[1][0]+" "+
  		  realTimeResultSet.getPQR()[2][0]+" "+
		  forceMomentumSet.getM_total_NED()[0][0]+" "+
		  forceMomentumSet.getM_total_NED()[1][0]+" "+
		  forceMomentumSet.getM_total_NED()[2][0]+" "+
  	      realTimeResultSet.getEulerX()+" "+
  		  realTimeResultSet.getEulerY()+" "+
  	      realTimeResultSet.getEulerZ()+" "+
  		  realTimeResultSet.getMasterSet().getSpaceShip().getMass()+ " " +
  		  realTimeResultSet.getNormalizedDeceleration()+ " " +
  		  0+ " " + 
  		  realTimeResultSet.getVelocity()*Math.cos(realTimeResultSet.getFpa())+" "+
  		  realTimeResultSet.getVelocity()*Math.sin(realTimeResultSet.getFpa())+" "+
  		  //realTimeContainer.getRealTimeList().get(subIndx).getIntegratorData().getGroundtrack()/1000+" "+ 
  		  realTimeResultSet.getGroundtrack()/1000+" "+ 
  		  controlCommandSet.getActiveSequence()+" "+
  		  sensorSet.getControllerTime()+" "+
  		  aerodynamicSet.getDragCoefficientParachute()+" "+
		  aerodynamicSet.getDragForceParachute()+" "+
		  (controlCommandSet.getPrimaryThrustThrottleCmd()*100)+ " "+ 
		  (actuatorSet.getPrimaryThrust_is())+" "+
		  (actuatorSet.getPrimaryThrust_is()/realTimeResultSet.getSCMass())+" "+
		  realTimeResultSet.getMasterSet().getSpaceShip().getPropulsion().getPrimaryPropellantFillingLevel()/realTimeResultSet.getMasterSet().getSpaceShip().getPropulsion().getPrimaryPropellant()*100+" "+ 
		  actuatorSet.getPrimaryISP_is()+" "+
		  controlCommandSet.getMomentumRCS_X_cmd()+" "+
		  controlCommandSet.getMomentumRCS_Y_cmd()+" "+
		  controlCommandSet.getMomentumRCS_Z_cmd()+" "+
		  actuatorSet.getMomentumRCS_X_is()+" "+
		  actuatorSet.getMomentumRCS_Y_is()+" "+
		  actuatorSet.getMomentumRCS_Z_is()+" "+
		  realTimeResultSet.getMasterSet().getSpaceShip().getPropulsion().getSecondaryPropellantFillingLevel()/realTimeResultSet.getMasterSet().getSpaceShip().getPropulsion().getSecondaryPropellant()*100+" "+
  		  controlCommandSet.getTVC_alpha()+" "+
  		  controlCommandSet.getTVC_beta()+" "+
		  actuatorSet.getTVC_alpha()+" "+
  		  actuatorSet.getTVC_beta()+" "+
		  forceMomentumSet.getF_Thrust_B()[0][0]+" "+
		  forceMomentumSet.getF_Thrust_B()[1][0]+" "+
		  forceMomentumSet.getF_Thrust_B()[2][0]+" "+
  		  0+" "+
  		  0+" "+
  		  0+" "+
  		  spaceShip.getPropulsion().getMassFlowPrimary()+" "+
  		  (spaceShip.getPropulsion().getPrimaryPropellant()-spaceShip.getPropulsion().getPrimaryPropellantFillingLevel())+" "+
  		  (spaceShip.getPropulsion().getSecondaryPropellant()-spaceShip.getPropulsion().getSecondaryPropellantFillingLevel())+" "+
  		  0+" "+
  		  0+" "+
  		  0+" "+
  		  0+" "+
  		  spaceShip.getPropulsion().getAccumulatedDeltaVPrimary()+" "
  		  );
	return steps;	
}

}
