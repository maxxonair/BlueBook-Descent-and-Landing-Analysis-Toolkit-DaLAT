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
import Sequence.MasterController;
import Sequence.SequenceContent;
import Simulator_main.DataSets.IntegratorData;
import Simulator_main.DataSets.RealTimeContainer;
import Simulator_main.DataSets.RealTimeResultSet;
import Toolbox.ReadInput;

public class LaunchRealTimeSimulation {
	
    public static double PI    = 3.14159265359;                 // PI                                       [-] 
	static double deg2rad = PI/180.0; 		//Convert degrees to radians
	static double rad2deg = 180/PI; 		//Convert radians to degrees
	
    static DecimalFormat df_X4 = new DecimalFormat("#.###");
    
    static SensorSet sensorSet = new SensorSet();
	
    public static void main(String[] args) throws IOException {
    	String timeStamp = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime());
    	System.out.println("------------------------------------------");
    	System.out.println("	Simulation started - "+timeStamp);
    	System.out.println("------------------------------------------");
    	System.out.println("Start READ :");
    	System.out.println("------------------------------------------");
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
    	//------------------------------------------------------------------------------------------
    	//					Compile Integrator inputs from files:
    	//------------------------------------------------------------------------------------------
    	
    	List<SequenceContent> SequenceSet = new ArrayList<SequenceContent>();
    	for(int i=0;i<SequenceSet.size();i++) {
    	SequenceSet.remove(i);
    	}
    	SequenceContent sequenceContent = new SequenceContent();
    	sequenceContent.addRollControl();
    	//sequenceContent.addPrimaryThrustControl();
    //sequenceContent.addYawControl();
    	sequenceContent.setTriggerEnd(2, 400);
    	SequenceSet.add(sequenceContent);
     	SequenceContent sequenceContent2 = new SequenceContent();
    //sequenceContent2.addYawControl();
    	//sequenceContent2.addPrimaryThrustControl();
    	sequenceContent2.addParachuteDeployment();
    	sequenceContent2.setTriggerEnd(3, 1500);
    	SequenceSet.add(sequenceContent2);
    	SequenceContent sequenceContent3 = new SequenceContent();
    	sequenceContent3.addParachuteSeparation();
    	sequenceContent3.addPrimaryThrustControl();
    	sequenceContent3.setTriggerEnd(2, 10);
    	SequenceSet.add(sequenceContent3);
    	SequenceContent sequenceContent4 = new SequenceContent();
    	SequenceSet.add(sequenceContent4);
    	SequenceContent sequenceContent5 = new SequenceContent();
    	SequenceSet.add(sequenceContent5);
    	
    //	System.out.println(SequenceSet.size());
    	//------------------------------------------------------------------------------------------
	double[] inputOut = ReadInput.readInput();
	//------------------------------------------------------------------------------------------
	//					Compile Integrator inputs from files:
	//------------------------------------------------------------------------------------------
	    	double rm = SimulationCore.DATA_MAIN[(int) inputOut[9]][0];
	    	//--------------------------------------------------------------------------------------
	    	System.out.println("READ: Create SpaceShip");
	    	SpaceShip spaceShip = new SpaceShip();
	    	spaceShip.setInertiaTensorMatrix(ReadInput.readInertia());
	    	spaceShip.setMass(inputOut[6]);
	    	spaceShip.getAeroElements().setSurfaceArea(ReadInput.readSurfaceArea(inputOut[6]));
	    	spaceShip.getAeroElements().setHeatshieldRadius(ReadInput.readAeroFile()[2]);
	    	spaceShip.getPropulsion().setPrimaryISPMax(ReadInput.readPropulsionInput()[0]);
	    	spaceShip.getPropulsion().setPrimaryPropellant(ReadInput.readPropulsionInput()[1]);
	    	spaceShip.getPropulsion().setPrimaryThrustMax(ReadInput.readPropulsionInput()[2]);
	    	spaceShip.getPropulsion().setPrimaryThrustMin(ReadInput.readPropulsionInput()[3]);	    	
	    	spaceShip.getPropulsion().setRCSMomentumX(ReadInput.readPropulsionInput()[6]);
	    	spaceShip.getPropulsion().setRCSMomentumY(ReadInput.readPropulsionInput()[7]);
	    	spaceShip.getPropulsion().setRCSMomentumZ(ReadInput.readPropulsionInput()[8]);
	    	spaceShip.getPropulsion().setSecondaryPropellant(ReadInput.readPropulsionInput()[12]);
	    	spaceShip.getPropulsion().setSecondaryISP_RCS_X(ReadInput.readPropulsionInput()[13]);
	    	spaceShip.getPropulsion().setSecondaryISP_RCS_Y(ReadInput.readPropulsionInput()[14]);
	    	spaceShip.getPropulsion().setSecondaryISP_RCS_Z(ReadInput.readPropulsionInput()[15]);
	    	spaceShip.getPropulsion().setSecondaryThrust_RCS_X(ReadInput.readPropulsionInput()[9]);
	    	spaceShip.getPropulsion().setSecondaryThrust_RCS_Y(ReadInput.readPropulsionInput()[10]);
	    	spaceShip.getPropulsion().setSecondaryThrust_RCS_Z(ReadInput.readPropulsionInput()[11]);
	    	
	    	spaceShip.getAeroElements().setParachuteSurfaceArea(ReadInput.readSCFile()[2]);
	    	
	    	if((int) ReadInput.readPropulsionInput()[4]==1) {
	    		spaceShip.getPropulsion().setIsPrimaryThrottleModel(true);
	    		spaceShip.getPropulsion().setPrimaryISPMin(ReadInput.readPropulsionInput()[5]);
	    	} else {
	    		spaceShip.getPropulsion().setIsPrimaryThrottleModel(false);
	    	}
	    	//-------------------------------------------------------------------------------------
	    	System.out.println("READ: Create IntegratorData");
			double[] IntegINP = ReadInput.readIntegratorInput((int) inputOut[8]);
	    	IntegratorData integratorData = new IntegratorData();
 	
	    	
    		double tGlobal = 1000;//inputOut[7];
    		double Frequency = 4;
    		double tIncrement = 1/Frequency; 
    		
    		
    		
    		    IntegINP[0]=tIncrement;
	    		integratorData.setIntegInput(IntegINP);	// !!! Must be called BEFORE .setIntegratorType !!!
	    		integratorData.setIntegratorType(1);
	    		integratorData.setTargetBody((int) inputOut[9]);
	    		
	    		integratorData.setInitLongitude(inputOut[0]*deg2rad);
	    		integratorData.setInitLatitude(inputOut[1]*deg2rad);
	    		integratorData.setInitRadius(inputOut[2]+inputOut[11]+rm);    		
	    		
	    		integratorData.setInitVelocity(inputOut[3]);
	    		integratorData.setInitFpa(inputOut[4]*deg2rad);
	    		integratorData.setInitAzimuth(inputOut[5]*deg2rad);
	    		
	    		integratorData.setVelocityVectorCoordSystem((int) inputOut[13]);

	    		integratorData.setInitRotationalRateX(inputOut[15]*deg2rad);
	    		integratorData.setInitRotationalRateY(inputOut[16]*deg2rad);
	    		integratorData.setInitRotationalRateZ(inputOut[17]*deg2rad);	    		

	    		integratorData.setRefElevation(inputOut[11]);
	    		
	    		integratorData.setInitialQuarterions(ReadInput.readInitialAttitude());
	    		integratorData.setAeroDragModel((int) ReadInput.readAeroFile()[0]); 
	    		integratorData.setAeroParachuteModel((int) ReadInput.readAeroFile()[3]);
	    		integratorData.setConstParachuteCd((double) ReadInput.readAeroFile()[4]);
	    		//integratorData.setDegreeOfFreedom((int) inputOut[14]);
	    		integratorData.setDegreeOfFreedom(6);
		    	//--------------------------------------------------------------------------------------
	    		ControlCommandSet controlCommandSet = new ControlCommandSet(); 
	    		//--------------------------------------------------------------------------------------
	    		
	    		RealTimeResultSet realTimeResultSet = new RealTimeResultSet();	    		
	    		
	    		integratorData.setIntegTimeStep(tIncrement);
	    		integratorData.setMaxIntegTime(tIncrement);
	    		
	    	    ArrayList<String> steps = new ArrayList<String>();
    			RealTimeContainer realTimeContainer = new RealTimeContainer();		
    			
		        long startTime   = System.nanoTime();	
		       // System.out.println(spaceShip.getPropulsion().getPrimaryThrustMax());
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
		    	    		
		    	    		integratorData.setInitialQuarterions(realTimeResultSet.getQuarternions());
		    	    		integratorData.setInitRotationalRateX(realTimeResultSet.getPQR()[0][0]);
		    	    		integratorData.setInitRotationalRateY(realTimeResultSet.getPQR()[1][0]);
		    	    		integratorData.setInitRotationalRateZ(realTimeResultSet.getPQR()[2][0]);

		    	    		
		    	    		integratorData.setGlobalTime(tIS);
		    	   //---------------------------------------------------------------------------------------
		    	   //				  Get Master Controller Commands
		    	   //---------------------------------------------------------------------------------------  		
		    	    		controlCommandSet = MasterController.createMasterCommand(controlCommandSet, 
		    	    	realTimeContainer, realTimeResultSet.getSpaceShip(), sensorSet, SequenceSet, Frequency);
		    	   //---------------------------------------------------------------------------------------
		    	   //				  Start incremental integration
		    	   //---------------------------------------------------------------------------------------		
		    	    		realTimeContainer = RealTimeSimulationCore.launchIntegrator(
								integratorData, 
								realTimeResultSet.getSpaceShip(),				 
								controlCommandSet
		    																			);	
	    			}
	  	      //---------------------------------------------------------------------------------------
	    	      //				       Create Sensor Data
	    	      //---------------------------------------------------------------------------------------
	    		  sensorSet.setMasterSet(realTimeContainer.getRealTimeSet().get(realTimeContainer.
	    				  getRealTimeSet().size() - 1));
	    		  sensorSet.setRealTimeResultSet(realTimeResultSet);
	    		  sensorSet.setGlobalTime(tIS);
	    		    	   SensorModel.addVelocitySensorUncertainty(sensorSet,  4);
	    		    			//SensorModel.addAltitudeSensorUncertainty(sensorSet,  5);
	    		    	   SensorModel.addIMUGiro(sensorSet, 0.5);
			  //---------------------------------------------------------------------------------------
			  //				  Add incremental integration result to write out file 
			  //---------------------------------------------------------------------------------------	
	    			steps = addStep(steps, realTimeContainer.getRealTimeResultSet(), integratorData);
	  	      //---------------------------------------------------------------------------------------
	  		  //				  Implement Stop Handler here: 
	  	      //---------------------------------------------------------------------------------------	
		    			if(realTimeResultSet.getAltitude()<0) {
		    				break;
		    			} 
		  	  //---------------------------------------------------------------------------------------
		  	  //				  Implement Stop Handler here: 
		  	  //---------------------------------------------------------------------------------------	
		        double primaryDeltaVIncrement =realTimeContainer.getRealTimeSet().
		        		get(realTimeContainer.getRealTimeSet().size()-1).
		        		getActuatorSet().getPrimaryISP_is()*9.80665*Math.abs(realTimeContainer.
		        				getRealTimeResultSet().getSpaceShip().getPropulsion().getMassFlowPrimary())/
		        		realTimeContainer.getRealTimeResultSet().getSCMass()*tIncrement;
		         
		        
		       realTimeContainer.getRealTimeResultSet().getSpaceShip().getPropulsion().setAccumulatedDeltaVPrimary(
		    	   realTimeContainer.getRealTimeResultSet().getSpaceShip().getPropulsion().getAccumulatedDeltaVPrimary()+primaryDeltaVIncrement);
	    		}
		  	//---------------------------------------------------------------------------------------
		  	//				  Generate total time to integrate the problem
		  	//---------------------------------------------------------------------------------------	
				long endTime   = System.nanoTime();
				long totalTime = endTime - startTime;
				double  totalTime_sec = (double) (totalTime * 1E-9);  
		        System.out.println("Runtime: "+df_X4.format(totalTime_sec)+" seconds.");
		  	//---------------------------------------------------------------------------------------
		    //				  Create Result File
		  	//---------------------------------------------------------------------------------------	
	    		createWriteOut(steps);
}
    
private static void createWriteOut(ArrayList<String> steps) {
        try{
        	//DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        	//Date date = new Date();
           // String time = "" ;//+ dateFormat.format(date) ; 
            String resultpath="";
            	String dir = System.getProperty("user.dir");
            	resultpath = dir + "/results.txt";
            PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
            for(String step: steps) {
                writer.println(step);
            }
            System.out.println("WRITE: Result file. Done.");         
            writer.close();
        } catch(Exception e) {System.out.println("ERROR: Writing result file failed");System.out.println(e);};
}

private static ArrayList<String> addStep(ArrayList<String> steps, RealTimeResultSet realTimeResultSet, 
										 IntegratorData integratorData) {
	MasterSet masterSet = realTimeResultSet.getMasterSet(); 
	AtmosphereSet atmosphereSet = masterSet.getAtmosphereSet();
	AerodynamicSet aerodynamicSet = masterSet.getAerodynamicSet();
	GravitySet gravitySet = masterSet.getGravitySet();
	ControlCommandSet controlCommandSet = masterSet.getControlCommandSet();
	ForceMomentumSet forceMomentumSet = masterSet.getForceMomentumSet();
	ActuatorSet actuatorSet = masterSet.getActuatorSet();
	
	steps.add(integratorData.getGlobalTime() + " " + 
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
      		  realTimeResultSet.getCartesianPosECEF()[0][0]+" "+
      		  realTimeResultSet.getCartesianPosECEF()[1][0]+" "+
      		  realTimeResultSet.getCartesianPosECEF()[2][0]+" "+
      		  0 + " " + 
      		  0 + " " + 
      		  0 + " " +       	 	  
  		  realTimeResultSet.getQuarternions()[0][0]+" "+
  		  realTimeResultSet.getQuarternions()[1][0]+" "+
  		  realTimeResultSet.getQuarternions()[2][0]+" "+
  		  realTimeResultSet.getQuarternions()[3][0]+" "+
  		  realTimeResultSet.getPQR()[0][0]+" "+
  		  realTimeResultSet.getPQR()[1][0]+" "+
  		  realTimeResultSet.getPQR()[2][0]+" "+
		  forceMomentumSet.getM_total_NED()[0][0]+" "+
		  forceMomentumSet.getM_total_NED()[1][0]+" "+
		  forceMomentumSet.getM_total_NED()[2][0]+" "+
  	      realTimeResultSet.getEulerX()+" "+
  		  realTimeResultSet.getEulerY()+" "+
  	      realTimeResultSet.getEulerZ()+" "+
  		  realTimeResultSet.getSpaceShip().getMass()+ " " +
  		  realTimeResultSet.getNormalizedDeceleration()+ " " +
  		  0+ " " + 
  		  realTimeResultSet.getVelocity()*Math.cos(realTimeResultSet.getFpa())+" "+
  		  realTimeResultSet.getVelocity()*Math.sin(realTimeResultSet.getFpa())+" "+
  		  0+" "+ 	  
  		  controlCommandSet.getActiveSequence()+" "+
  		  sensorSet.getControllerTime()+" "+
  		  aerodynamicSet.getDragCoefficientParachute()+" "+
		  aerodynamicSet.getDragForceParachute()+" "+
		  (controlCommandSet.getPrimaryThrustThrottleCmd()*100)+ " "+ 
		  (forceMomentumSet.getThrustTotal())+" "+
		  (forceMomentumSet.getThrustTotal()/realTimeResultSet.getSCMass())+" "+
		  realTimeResultSet.getSpaceShip().getPropulsion().getPrimaryPropellantFillingLevel()/realTimeResultSet.getSpaceShip().getPropulsion().getPrimaryPropellant()*100+" "+ 
		  actuatorSet.getPrimaryISP_is()+" "+
		  controlCommandSet.getMomentumRCS_X_cmd()+" "+
		  controlCommandSet.getMomentumRCS_Y_cmd()+" "+
		  controlCommandSet.getMomentumRCS_Z_cmd()+" "+
		  actuatorSet.getMomentumRCS_X_is()+" "+
		  actuatorSet.getMomentumRCS_Y_is()+" "+
		  actuatorSet.getMomentumRCS_Z_is()+" "+
		  realTimeResultSet.getSpaceShip().getPropulsion().getSecondaryPropellantFillingLevel()/realTimeResultSet.getSpaceShip().getPropulsion().getSecondaryPropellant()*100+" "+
  		  0+" "+
  		  0+" "+
  		  0+" "+
  		  0+" "+
		  forceMomentumSet.getF_Thrust_B()[0][0]+" "+
		  forceMomentumSet.getF_Thrust_B()[1][0]+" "+
		  forceMomentumSet.getF_Thrust_B()[2][0]+" "+
  		  0+" "+
  		  0+" "+
  		  0+" "+
  		  realTimeResultSet.getSpaceShip().getPropulsion().getMassFlowPrimary()+" "+
  		  (realTimeResultSet.getSpaceShip().getPropulsion().getPrimaryPropellant()-realTimeResultSet.getSpaceShip().getPropulsion().getPrimaryPropellantFillingLevel())+" "+
  		  (realTimeResultSet.getSpaceShip().getPropulsion().getSecondaryPropellant()-realTimeResultSet.getSpaceShip().getPropulsion().getSecondaryPropellantFillingLevel())+" "+
  		  0+" "+
  		  0+" "+
  		  0+" "+
  		  0+" "+
  		  realTimeResultSet.getSpaceShip().getPropulsion().getAccumulatedDeltaVPrimary()+" "
  		  );
	return steps;	
}


}
