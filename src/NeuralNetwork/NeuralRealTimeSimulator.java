package NeuralNetwork;

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
import Simulator_main.RealTimeSimulationCore;
import Simulator_main.DataSets.IntegratorData;
import Simulator_main.DataSets.RealTimeContainer;
import Simulator_main.DataSets.RealTimeResultSet;
import utils.ReadInput;

public class NeuralRealTimeSimulator {
	
    public static double PI    = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;                 // PI                                       [-] 
	static double deg2rad 	   = PI/180.0; 					    //Convert degrees to radians
	static double rad2deg 	   = 180/PI; 					    //Convert radians to degrees
	
    static DecimalFormat df_X4 = new DecimalFormat("#.###");
    
    static SensorSet sensorSet = new SensorSet();
    
	static DataContainer dataContainer = new DataContainer();
	static DataSetXY dataSet =  new DataSetXY();
	static boolean isPlot=false;
	static boolean isWriteOut=false;
	
    double[][] resultSet = new double[61][2];

	
	public  double[][] getResultSet() {
	return resultSet;
}

	public NeuralRealTimeSimulator() {
		
	}
	
    public void run(double Kp, double Ki, double Kd) throws IOException {
    	for(int i=0;i<=60;i++) {
    		resultSet[i][0]=i;
    	}
    	String timeStamp = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime());
  //  	System.out.println("------------------------------------------");
    //	System.out.println(""+timeStamp);
   // 	System.out.println("------------------------------------------");
   // 	System.out.println("Start READ :");
    //	System.out.println("------------------------------------------");
    	//------------------------------------------------------------------------------------------
    	//					Compile Integrator inputs from files:
    	//------------------------------------------------------------------------------------------
    	
    //	List<SequenceContent> SequenceSet = ReadInput.READ_sequenceFile();
    	
    	List<SequenceContent> SequenceSet = new ArrayList<SequenceContent>();
    	
	    SequenceContent sequenceContent = new SequenceContent();
	    sequenceContent.setID(0);
	    sequenceContent.setTriggerEnd(0, 60);
	    sequenceContent.addRollControl();
	    sequenceContent.getControllerSets().get(0).setKp(Kp);
	    sequenceContent.getControllerSets().get(0).setKi(Ki);
	    sequenceContent.getControllerSets().get(0).setKd(Kd);
	    SequenceSet.add(sequenceContent);
    	
    	//------------------------------------------------------------------------------------------
	double[] inputOut = ReadInput.readInput();
	//------------------------------------------------------------------------------------------
	//					Compile Integrator inputs from files:
	//------------------------------------------------------------------------------------------
	    	double rm = RealTimeSimulationCore.DATA_MAIN[(int) inputOut[9]][0];
	    	//--------------------------------------------------------------------------------------
	   // 	System.out.println("READ: Create SpaceShip");
	    	SpaceShip spaceShip = new SpaceShip();
	    	double[][] InertiaTensor   = {{   2000 ,  0  ,   0},
					  				  {   0 ,  2000  ,   0},
					  				  {   0 ,  0  ,   1000}};  // Inertia Tensor []
	    	spaceShip.setInertiaTensorMatrix(InertiaTensor);
	    	spaceShip.setMass(1000);
	    	spaceShip.getAeroElements().setSurfaceArea(120);
	    	spaceShip.getAeroElements().setHeatshieldRadius(2.3);
	    	spaceShip.getAeroElements().setParachuteMass(70);
	    	spaceShip.getAeroElements().setHeatShieldMass(120);
	    	spaceShip.getPropulsion().setPrimaryISPMax(310);
	    	spaceShip.getPropulsion().setPrimaryPropellant(200);
	    	spaceShip.getPropulsion().setPrimaryThrustMax(6000);
	    	spaceShip.getPropulsion().setPrimaryThrustMin(6000);	    	
	    	spaceShip.getPropulsion().setRCSMomentumX(200);
	    	spaceShip.getPropulsion().setRCSMomentumY(200);
	    	spaceShip.getPropulsion().setRCSMomentumZ(100);
	    	spaceShip.getPropulsion().setSecondaryPropellant(60);
	    	spaceShip.getPropulsion().setSecondaryISP_RCS_X(280);
	    	spaceShip.getPropulsion().setSecondaryISP_RCS_Y(280);
	    	spaceShip.getPropulsion().setSecondaryISP_RCS_Z(280);
	    	spaceShip.getPropulsion().setSecondaryThrust_RCS_X(200);
	    	spaceShip.getPropulsion().setSecondaryThrust_RCS_Y(200);
	    	spaceShip.getPropulsion().setSecondaryThrust_RCS_Z(200);
	    	
	    	spaceShip.getAeroElements().setParachuteSurfaceArea(20);

	    	spaceShip.getPropulsion().setIsPrimaryThrottleModel(false);
	    	//-------------------------------------------------------------------------------------
			double[] IntegINP = ReadInput.readIntegratorInput((int) inputOut[8]);
	    	IntegratorData integratorData = new IntegratorData();
 	
	    	
    		double tGlobal = 60;
    		double Frequency = 2;
    		double tIncrement = 1/Frequency; 
    		
    		

	    		integratorData.setIntegInput(IntegINP);	// !!! Must be called BEFORE .setIntegratorType !!!
	    		integratorData.setIntegratorType((int) inputOut[8]);
	    		integratorData.setTargetBody((int) inputOut[9]);
	    		
	    		integratorData.setInitLongitude(0*deg2rad);
	    		integratorData.setInitLatitude(0*deg2rad);
	    		integratorData.setInitRadius(150000+rm);    		
	    		
	    		integratorData.setInitVelocity(4000);
	    		integratorData.setInitFpa(0);
	    		integratorData.setInitAzimuth(0*deg2rad);
	    		
	    		integratorData.setVelocityVectorCoordSystem((int) inputOut[13]);

	    		integratorData.setInitRotationalRateX(60*deg2rad);
	    		integratorData.setInitRotationalRateY(0*deg2rad);
	    		integratorData.setInitRotationalRateZ(0*deg2rad);	    		

	    		integratorData.setRefElevation(0);
	    		
	    		double[][] QVector   = {{   1 },
	    			    					{   0 },
	    			    					{   0 },
	    			    					{   0 }};  // Inertia Tensor []
	    		integratorData.setInitialQuarterions(QVector);
	    		integratorData.setAeroDragModel((int) 0); 
	    		integratorData.setAeroParachuteModel((int) 0);
	    		integratorData.setConstParachuteCd((double) 0.6);
	    		//integratorData.setDegreeOfFreedom((int) inputOut[14]);
	    		integratorData.setDegreeOfFreedom(6);
	    		
	    		integratorData.setAtmosphereNoiseModel(true);
	    		integratorData.setActuatorNoiseModel(true);
		    	//--------------------------------------------------------------------------------------
	    		ControlCommandSet controlCommandSet = new ControlCommandSet(); 
	    		//--------------------------------------------------------------------------------------
	    		
	    		RealTimeResultSet realTimeResultSet = new RealTimeResultSet();	    		
	    		
	    		integratorData.setIntegTimeStep(0.1);
	    		integratorData.setMaxIntegTime(tIncrement);
	    		
	    	    ArrayList<String> steps = new ArrayList<String>();
	    		
    			RealTimeContainer realTimeContainer = new RealTimeContainer();	

    			
		        long startTime   = System.nanoTime();	
		  //  	System.out.println("------------------------------------------");
		   // 	System.out.println("Start SIMULATION :");
		   // 	System.out.println("------------------------------------------");
for(double tIS=0;tIS<tGlobal;tIS+=tIncrement) {
	//controlCommandSet
	    		//---------------------------------------------------------------------------------------
	    		//				  6 Degree of Freedom - Universal RealTime module
	    		//---------------------------------------------------------------------------------------
	    			if (tIS==0) {
			    		//System.out.println("Simulator set and running");
			    		
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
	    	    	for(int i=0;i<=60;i++) {
	    	    		if(tIS==i) {
	    	    			resultSet[i][1]=realTimeContainer.getRealTimeResultSet().getPQR()[0][0];	
	    	    		}
	    	    	}
	    		  sensorSet.setMasterSet(realTimeContainer.getMasterList().get(realTimeContainer.
	    				  getMasterList().size() - 1));
	    		  sensorSet.setRealTimeResultSet(realTimeResultSet);
	    		  sensorSet.setGlobalTime(tIS);
	    		    	   SensorModel.addVelocitySensorUncertainty(sensorSet,  4);
	    		    			//SensorModel.addAltitudeSensorUncertainty(sensorSet,  5);
	    		    	   SensorModel.addIMUGiro(sensorSet, 0.5);   
			  //---------------------------------------------------------------------------------------
			  //				  Add incremental integration result to write out file 
			  //---------------------------------------------------------------------------------------
	    		    	   if(isWriteOut) {
	    		    	   for(int i=0;i<realTimeContainer.getMasterList().size();i++) {
	    		    		   integratorData.setGroundtrack(realTimeContainer.getRealTimeList().get(i).
	    		    				   getIntegratorData().getGroundtrack());
	    		    		   	steps = addStep(steps, realTimeContainer, integratorData, i);
	    		    		   	
	    		    		   	
	    		    	   }
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
		        double primaryDeltaVIncrement =realTimeContainer.getMasterList().
		        		get(realTimeContainer.getMasterList().size()-1).
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
		       // System.out.println("Runtime: "+df_X4.format(totalTime_sec)+" seconds.");
		  	//---------------------------------------------------------------------------------------
		    //				  Create Result File
		  	//---------------------------------------------------------------------------------------	
		        if(isWriteOut) {
		        		createWriteOut(steps);
		        }
	    		if(isPlot) {
	    		dataContainer.addDataSet(dataSet);
	    		//PlotXY.plot(dataContainer);
	    		}
}
  
    
    
private static void createWriteOut(ArrayList<String> steps) {
        try{
            String resultpath="";
            	String dir = System.getProperty("user.dir");
            	resultpath = dir + "/AiTrim.txt";
            PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
            for(String step: steps) {
                writer.println(step);
            }
           // System.out.println("WRITE: Result file. Done."); 
           // System.out.println("------------------------------------------");
            writer.close();
        } catch(Exception e) {System.out.println("ERROR: Writing result file failed");System.out.println(e);};
}


private static ArrayList<String> addStep(ArrayList<String> steps, RealTimeContainer realTimeContainer, 
										 IntegratorData integratorData, int subIndx) {
	RealTimeResultSet realTimeResultSet = realTimeContainer.getRealTimeList().get(subIndx);
	MasterSet masterSet = realTimeContainer.getMasterList().get(subIndx); 
	AtmosphereSet atmosphereSet = masterSet.getAtmosphereSet();
	AerodynamicSet aerodynamicSet = masterSet.getAerodynamicSet();
	GravitySet gravitySet = masterSet.getGravitySet();
	ControlCommandSet controlCommandSet = masterSet.getControlCommandSet();
	ForceMomentumSet forceMomentumSet = masterSet.getForceMomentumSet();
	ActuatorSet actuatorSet = masterSet.getActuatorSet();
	if(isPlot) {
	dataSet.addPair(new Pair((integratorData.getGlobalTime()+realTimeContainer.getRealTimeList().get(subIndx).getTime()), 
			masterSet.getSpaceShip().getMass()));
	//	dataSet.addPair(new Pair(realTimeResultSet.getAltitude(), 
	//			aerodynamicSet.getFlowzone()));
	dataContainer.setxAxisLabel("Time");
	dataContainer.setyAxisLabel("Noise");
	}
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
  		  realTimeContainer.getRealTimeList().get(subIndx).getIntegratorData().getGroundtrack()/1000+" "+ 	  
  		  controlCommandSet.getActiveSequence()+" "+
  		  sensorSet.getControllerTime()+" "+
  		  aerodynamicSet.getDragCoefficientParachute()+" "+
		  aerodynamicSet.getDragForceParachute()+" "+
		  (controlCommandSet.getPrimaryThrustThrottleCmd()*100)+ " "+ 
		  (actuatorSet.getPrimaryThrust_is())+" "+
		  (actuatorSet.getPrimaryThrust_is()/realTimeResultSet.getSCMass())+" "+
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
