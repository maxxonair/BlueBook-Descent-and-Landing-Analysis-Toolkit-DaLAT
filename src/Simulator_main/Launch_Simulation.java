package Simulator_main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import FlightElement.SpaceShip;
import Simulator_main.Simulation;
import Toolbox.ReadInput;

public class Launch_Simulation {
	
    public static double PI    = 3.14159265359;                 // PI                                       [-] 
	static double deg2rad = PI/180.0; 		//Convert degrees to radians
	static double rad2deg = 180/PI; 		//Convert radians to degrees
	
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
	double[] inputOut = ReadInput.readInput();
	//-----------------------------------------------------------------------------------------------------------------------------
	//												Compile Integrator inputs from files:
	//-----------------------------------------------------------------------------------------------------------------------------
	    	double rm = Simulation.DATA_MAIN[(int) inputOut[9]][0];
	    	List<StopCondition> STOP_Handler = ReadInput.readEventHandler( rm, inputOut[11]) ;
	    	System.out.println("READ: "+STOP_Handler.size()+" EventHandler found.");
	    	//--------------------------------------------------------------------------------------
	    	System.out.println("READ: Create SpaceShip");
	    	double[] propRead;
	    	propRead = ReadInput.readPropulsionInput();
	    	SpaceShip spaceShip = new SpaceShip();
	    	spaceShip.setInertiaTensorMatrix(ReadInput.readInertia());
	    	spaceShip.setMass(inputOut[6]);
	    	spaceShip.setInitialQuarterions(ReadInput.readInitialAttitude());
	    	spaceShip.getAeroElements().setSurfaceArea(ReadInput.readSurfaceArea(inputOut[6]));
	    	spaceShip.getAeroElements().setHeatshieldRadius(ReadInput.readAeroFile()[2]);
	    	spaceShip.getPropulsion().setPrimaryISPMax(propRead[0]);
	    	spaceShip.getPropulsion().setPrimaryPropellant(propRead[1]);
	    	spaceShip.getPropulsion().setPrimaryThrustMax(propRead[2]);
	    	spaceShip.getPropulsion().setPrimaryThrustMin(propRead[3]);
	    	if((int) propRead[4]==1) {
	    		spaceShip.getPropulsion().setIsPrimaryThrottleModel(true);
	    		spaceShip.getPropulsion().setPrimaryISPMin(propRead[5]);
	    	} else {
	    		spaceShip.getPropulsion().setIsPrimaryThrottleModel(false);
	    	}
	    	//--------------------------------------------------------------------------------------
	    	System.out.println("READ: Create IntegratorData");
			double[] IntegINP = ReadInput.readIntegratorInput((int) inputOut[8]);
	    	IntegratorData integratorData = new IntegratorData();
	    		integratorData.setIntegInput(IntegINP);				// ! Must be called before .setIntegratorType !
	    		integratorData.setIntegratorType((int) inputOut[8]);
	    		integratorData.setTargetBody((int) inputOut[9]);
	    		integratorData.setInitLongitude(inputOut[0]*deg2rad);
	    		integratorData.setInitLatitude(inputOut[1]*deg2rad);
	    		integratorData.setInitRadius(inputOut[2]+inputOut[11]+rm);
	    		integratorData.setInitVelocity(inputOut[3]);
	    		integratorData.setInitFpa(inputOut[4]*deg2rad);
	    		integratorData.setInitAzimuth(inputOut[5]*deg2rad);
	    		integratorData.setIntegStopHandler(STOP_Handler);
	    		integratorData.setIntegTimeStep(inputOut[10]);
	    		integratorData.setMaxIntegTime(inputOut[7]);
	    		integratorData.setRefElevation(inputOut[11]);
	    		integratorData.setVelocityVectorCoordSystem((int) inputOut[13]);
	    		integratorData.setInitRotationalRateX(inputOut[15]);
	    		integratorData.setInitRotationalRateY(inputOut[16]);
	    		integratorData.setInitRotationalRateZ(inputOut[17]);
	    		integratorData.setAeroDragModel((int) ReadInput.readAeroFile()[0]); 
	    		//integratorData.setDegreeOfFreedom((int) inputOut[14]);
	    		integratorData.setDegreeOfFreedom(6);
	    		//-----------------------------------------------------------------------------------------------------------------------------
	    		//												6 Degree of Freedom - Universal module
	    		//-----------------------------------------------------------------------------------------------------------------------------
	    		System.out.println("Simulator set and running");
	    		Simulation.launchIntegrator(
	    												    integratorData,
	    												    ReadInput.readSequence(),	   // Sequence data set	LIST			     [-]
														ReadInput.readErrorFile(),	   // Error file to model partial system failres [-] 
														spaceShip				       // SpaceShip data file                  [-]
	    				);
}

}