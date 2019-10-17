package VisualEngine.entities;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Simulator_main.RealTimeSimulationCore;
import Simulator_main.DataSets.IntegratorData;
import Simulator_main.DataSets.RealTimeContainer;
import Simulator_main.DataSets.RealTimeResultSet;
import Toolbox.ReadInput;
import VisualEngine.animation.AnimationSet;
import VisualEngine.models.TexturedModel;
import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.terrains.Terrain;

public class Spacecraft extends Entity {
	
	//private static final float TESTROCKET  =  40;
	
	private static float CG_Y = 0.9f;
	private static float azimuth=(float) Math.toRadians(0);
	static double[] IntegINP ;
    public static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;
    
    public static double deg2rad = PI/180;
	@SuppressWarnings("unused")
	private  static  float currentVerticalSpeed   =   -5;
	private  static  float currentHorizontalSpeed =  60;
	private  static  float currentSpeed           = (float) Math.sqrt(currentVerticalSpeed*currentVerticalSpeed + currentHorizontalSpeed*currentHorizontalSpeed);
    
    private static double[][] Thrust_Momentum = {{0},
												 {0},
												 {0}};
	
	private static double[][] PQR = {{0},
			 						 {0},
			 						 {0}}; 
	
	private static double[][] quarternions = {	{0},
												{0},
												{0},
												{0}}; 
	
	private static float animationScale =100;
	
	private static double[][] Thrust_NED = {{0},
			     							{0},
			     							{0}};
	
	
    private static SpaceShip spaceShip;
    
    private static boolean isThrust = true; 
    private static float SCMass;
    private static float SCPropMass ;
    private static float SCMainThrust;
    private static IntegratorData integratorData = new IntegratorData();
    private static ControlCommandSet controlCommandSet = new ControlCommandSet();

	public Spacecraft(SpaceShip spaceShip, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
			Spacecraft.spaceShip = spaceShip;
			Spacecraft.quarternions = integratorData.getInitialQuarterions();
			Spacecraft.SCMass = (float) spaceShip.getMass();
			Spacecraft.SCPropMass = (float) spaceShip.getPropulsion().getPrimaryPropellant();
			Spacecraft.SCMainThrust = (float) spaceShip.getPropulsion().getPrimaryThrustMax();
			try {
				 IntegINP = ReadInput.readIntegratorInput(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    integratorData.setInitRadius(1737400+Spacecraft.getPosition().y);
	}
	
	
	public void animate(Terrain terrain, AnimationSet animationSet) {
		//super.increaseRotation(0, currentTurnSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		float distance = animationSet.getV_h()*DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(45)))*animationScale;
		float dz = (float) (distance * Math.cos(Math.toRadians(45)))*animationScale;
		super.increaseRotation(0, (float) Math.toDegrees(animationSet.getAngularRateY())*DisplayManager.getFrameTimeSeconds(), 0);
		Spacecraft.currentHorizontalSpeed = animationSet.getV_h();
		Spacecraft.currentVerticalSpeed = animationSet.getV_v();
		super.increasePosition(	dx, 
								currentVerticalSpeed * DisplayManager.getFrameTimeSeconds()*animationScale , 
								dz);

		//---------------------------------------------------------------------------------------
		//			Terrain collision detection
		//---------------------------------------------------------------------------------------
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z)+CG_Y;
		//System.out.println(terrainHeight);
		if(super.getPosition().y<(terrainHeight+CG_Y)) {
			Spacecraft.currentVerticalSpeed=0;
			 super.getPosition().y=terrainHeight+CG_Y;
		}
		Spacecraft.currentSpeed = (float) Math.sqrt(Spacecraft.currentHorizontalSpeed*Spacecraft.currentHorizontalSpeed+Spacecraft.currentVerticalSpeed*Spacecraft.currentVerticalSpeed);
	}
	
	public void fly(Terrain terrain) {
		controlCommandSet.setMomentumRCS_X_cmd(0);
		controlCommandSet.setMomentumRCS_Y_cmd(0);
		controlCommandSet.setMomentumRCS_Z_cmd(0);
		checkInputs();
		//---------------------------------------------------------------------------------------
		//			Terrain collision detection
		//---------------------------------------------------------------------------------------
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z)+CG_Y;
		//System.out.println(terrainHeight);
		if(super.getPosition().y<=(terrainHeight+CG_Y)) {
			//Spacecraft.currentVerticalSpeed=0;
			Spacecraft.currentHorizontalSpeed=0;
			 super.getPosition().y=terrainHeight+CG_Y;
		} else {
		RealTimeResultSet realTimeResultSet = getRealTimeResultSet(DisplayManager.getFrameTimeSeconds());
		//---------------------------------------------------------------------------------------
		//					Translation
		//---------------------------------------------------------------------------------------
		Spacecraft.currentHorizontalSpeed = (float) (realTimeResultSet.getVelocity()*Math.cos(realTimeResultSet.getFpa()));
		Spacecraft.currentVerticalSpeed   = (float) (realTimeResultSet.getVelocity()*Math.sin(realTimeResultSet.getFpa()));
		float distance = (float) (Spacecraft.currentHorizontalSpeed *DisplayManager.getFrameTimeSeconds());
		float dx = (float) (distance * Math.sin(Spacecraft.getAzimuth()));
		float dz = (float) (distance * Math.cos(Spacecraft.getAzimuth()));
		float dy = (float) (currentVerticalSpeed * DisplayManager.getFrameTimeSeconds());
		super.increasePosition(dx, dy , dz);
		//---------------------------------------------------------------------------------------
		//			 		Rotation 
		//---------------------------------------------------------------------------------------
		super.increaseRotation((float) Math.toDegrees(-Spacecraft.getPQR()[0][0])*DisplayManager.getFrameTimeSeconds(), 
							   (float) Math.toDegrees( Spacecraft.getPQR()[2][0])*DisplayManager.getFrameTimeSeconds(), 
							   (float) Math.toDegrees( Spacecraft.getPQR()[1][0])*DisplayManager.getFrameTimeSeconds() );  // Yaw axis

		Spacecraft.currentSpeed = (float) realTimeResultSet.getVelocity();
		//Spacecraft.setAzimuth(realTimeResultSet.getAzi());
		}
	}	
	
	public static float getSCMass() {
		return SCMass;
	}

	public static void setSCMass(float sCMass) {
		SCMass = sCMass;
	}

	public static float getSCMainThrust() {
		return SCMainThrust;
	}

	public static void setSCMainThrust(float sCMainThrust) {
		SCMainThrust = sCMainThrust;
	}

	public static void checkInputs() {
		//----------------------------------------------------
		//				Primary - Thrust
		//----------------------------------------------------
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Spacecraft.controlCommandSet.setPrimaryThrustThrottleCmd(1);
			//Spacecraft.isThrust=true;
		} else {
			//Spacecraft.isThrust=false;
			Spacecraft.controlCommandSet.setPrimaryThrustThrottleCmd(0);
		}
		
		//----------------------------------------------------
		//				RCS - Rotation
		//----------------------------------------------------
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {				// Yaw 
		     controlCommandSet.setMomentumRCS_Z_cmd(-1);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			controlCommandSet.setMomentumRCS_Z_cmd(1);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {				// Pitch 
		     controlCommandSet.setMomentumRCS_X_cmd(-1);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			controlCommandSet.setMomentumRCS_X_cmd(1);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {				// Roll 
		     controlCommandSet.setMomentumRCS_Y_cmd(-1);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			controlCommandSet.setMomentumRCS_Y_cmd(1);
		}
		//---------------------------------------------
		// 			Environment Inputs
		//---------------------------------------------
		if(Keyboard.isKeyDown(Keyboard.KEY_O)) {
			Spacecraft.setPosition(new Vector3f(1000,200,0));
			//Spacecraft.setRotX(-25);
			currentVerticalSpeed=-5;
			currentHorizontalSpeed=60;
			Spacecraft.setQuarternions(integratorData.getInitialQuarterions());
		}
	}

	public static  float getCurrentSpeed() {
		return currentSpeed;
	}

	public static   float getCurrentVerticalSpeed() {
		return currentVerticalSpeed;
	}

	public static   float getCurrentHorizontalSpeed() {
		return currentHorizontalSpeed;
	}
	
	public static  float getAzimuth() {
		return azimuth;
	}

	public static  void setAzimuth(float azimuth) {
		Spacecraft.azimuth = azimuth;
	}
	
	
	
	public static float getSCPropMass() {
		return SCPropMass;
	}

	public static void setSCPropMass(float sCPropMass) {
		SCPropMass = sCPropMass;
	}

	public static RealTimeResultSet getRealTimeResultSet(float timestep) {
		RealTimeResultSet realTimeResultSet = new RealTimeResultSet();
		
	    double vel = 0;
	    double fpa = 0;
	    double azi = 0;
	    if(Spacecraft.getCurrentSpeed()==0 || Double.isNaN(getCurrentSpeed()) || getCurrentSpeed()<0) {
	    	vel=0.1;
	    } else {
	    	 vel = getCurrentSpeed();
	     fpa = Math.asin(getCurrentVerticalSpeed()/getCurrentSpeed());
	     azi = Spacecraft.getAzimuth();
	    }
		
		int INTEGRATOR = 1;
		int target = 1;
		if(timestep<0.001f) {
			timestep = 0.001f;
			System.out.println("WARNING: timestep too small");
		} else {
			//System.out.println("Timestep: "+timestep);
		}
//Spacecraft.getPosition().y
	  /*
		realTimeResultSet = RealTimeSimulation.Launch_Integrator(INTEGRATOR, target, 0, 0, Spacecraft.getPosition().y+rm+ref, vel, fpa, 
				azi, Spacecraft.getSCMass(), timestep, timestep, 0, Spacecraft.getQuarternions(), Spacecraft.getPQR(), 
			    isThrust, Thrust_Momentum, spaceShip);
			    */
		Spacecraft.controlCommandSet.setTVC_alpha(-90*deg2rad);
	    integratorData.setDegreeOfFreedom(6);
	    integratorData.setMaxIntegTime(timestep);
	    integratorData.setIntegTimeStep(timestep);
	    integratorData.setTargetBody(target);
	    integratorData.setInitRadius(Spacecraft.getPosition().y);
	    integratorData.setInitLongitude(0);
	    integratorData.setInitLatitude(0);
	    integratorData.setInitVelocity(vel);
	    integratorData.setInitFpa(fpa);
	    integratorData.setInitAzimuth(azi);
	    integratorData.setIntegInput(IntegINP);	
	    integratorData.setIntegratorType(INTEGRATOR);
	    integratorData.setAeroDragModel(0);
	    integratorData.setInitRadius(1737400+Spacecraft.getPosition().y);
	    integratorData.setInitialQuarterions(Spacecraft.getQuarternions());
	    integratorData.setAngularRate(Spacecraft.getPQR());
		//System.out.println(controlCommandSet.getPrimaryThrustThrottleCmd());
	    RealTimeContainer realTimeContainer = new RealTimeContainer();
	    realTimeContainer = RealTimeSimulationCore.launchIntegrator(integratorData, spaceShip, controlCommandSet);
		realTimeResultSet = realTimeContainer.getRealTimeResultSet();
	    Spacecraft.setSCPropMass((float) (spaceShip.getPropulsion().getPrimaryPropellant()-(spaceShip.getMass()-realTimeResultSet.getSCMass())));
		Spacecraft.setPQR(realTimeResultSet.getPQR());
		Spacecraft.setQuarternions(realTimeResultSet.getQuarternions());
		Spacecraft.setSCMass((float) realTimeResultSet.getSCMass());
		Spacecraft.setAzimuth((float) realTimeResultSet.getAzi());
		Spacecraft.setThrust_NED(realTimeResultSet.getThrust_NED());
		//System.out.println(realTimeResultSet.getSCMass());
		return realTimeResultSet;

	}


	public static double[][] getQuarternions() {
		return quarternions;
	}

	public static void setQuarternions(double[][] quarternions) {
		Spacecraft.quarternions = quarternions;
	}

	public static double[][] getThrust_Momentum() {
		return Thrust_Momentum;
	}

	public static void setThrust_Momentum(double[][] thrust_Momentum) {
		Thrust_Momentum = thrust_Momentum;
	}


	public static boolean getisThrust() {
		return isThrust;
	}


	public static double[][] getPQR() {
		return PQR;
	}

	public static void setPQR(double[][] pQR) {
		PQR = pQR;
	}


	public double[][] getThrust_NED() {
		return Thrust_NED;
	}


	public static void setThrust_NED(double[][] thrust_NED) {
		Thrust_NED = thrust_NED;
	}


	
	
}
