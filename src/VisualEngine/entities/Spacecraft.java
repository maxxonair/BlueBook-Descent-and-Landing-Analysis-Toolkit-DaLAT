package VisualEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import FlightElement.SpaceShip;
import Simulator_main.RealTimeResultSet;
import Simulator_main.RealTimeSimulation;
import VisualEngine.animation.AnimationSet;
import VisualEngine.models.TexturedModel;
import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.terrains.Terrain;

public class Spacecraft extends Entity {
	
	//private static final float TESTROCKET  =  40;
	
	private static float CG_Y = 0.9f;
	private static float azimuth=(float) Math.toRadians(45);
	

	@SuppressWarnings("unused")
	private  static  float currentVerticalSpeed   =   0;
	private  static  float currentHorizontalSpeed =  60;
	private  static  float currentSpeed           = (float) Math.sqrt(currentVerticalSpeed*currentVerticalSpeed + currentHorizontalSpeed*currentHorizontalSpeed);
    
    private static double[][] Thrust_Momentum = {{0},
												 {0},
												 {0}};
    
    private static double[][] MRCS = {{0},
			 						  {0},
			 						  {0}};
	
	private static double[][] PQR = {{0},
			 						 {0},
			 						 {0}}; 
	
	private static double[][] quarternions = {	{1},
												{0},
												{0},
												{0}}; 
	
	private static float animationScale =100;
	
	
    private static SpaceShip spaceShip;
    
    private static boolean isThrust = true; 
    private static float SCMass;
    private static float SCPropMass ;
    private static float SCMainThrust;

	public Spacecraft(SpaceShip spaceShip, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
			Spacecraft.spaceShip = spaceShip;
			
			Spacecraft.SCMass = (float) spaceShip.getMass();
			Spacecraft.SCPropMass = (float) spaceShip.getPropulsion().getPrimaryPropellant();
			Spacecraft.SCMainThrust = (float) spaceShip.getPropulsion().getPrimaryThrustMax();
			Spacecraft.MRCS = spaceShip.getPropulsion().getSecondaryMomentum();
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
		checkInputs();
		RealTimeResultSet realTimeResultSet = getRealTimeResultSet(DisplayManager.getFrameTimeSeconds());
		//---------------------------------------------------------------------------------------
		//					Translation
		//---------------------------------------------------------------------------------------
		Spacecraft.currentHorizontalSpeed = (float) (realTimeResultSet.getVelocity()*Math.cos(realTimeResultSet.getFpa()));
		Spacecraft.currentVerticalSpeed   = (float) (realTimeResultSet.getVelocity()*Math.sin(realTimeResultSet.getFpa()));
		float distance = (float) (Spacecraft.currentHorizontalSpeed *DisplayManager.getFrameTimeSeconds());
		float dx = (float) (distance * Math.sin(Spacecraft.getAzimuth()));
		float dz = (float) (distance * Math.cos(Spacecraft.getAzimuth()));
		super.increasePosition(dx, currentVerticalSpeed * DisplayManager.getFrameTimeSeconds() , dz);
		//---------------------------------------------------------------------------------------
		//			 		Rotation 
		//---------------------------------------------------------------------------------------
		super.increaseRotation((float) Math.toDegrees(Spacecraft.getPQR()[0][0])*DisplayManager.getFrameTimeSeconds(), 
							   (float) Math.toDegrees(Spacecraft.getPQR()[2][0])*DisplayManager.getFrameTimeSeconds(), 
							   (float) Math.toDegrees(Spacecraft.getPQR()[1][0])*DisplayManager.getFrameTimeSeconds());
		//---------------------------------------------------------------------------------------
		//			Terrain collision detection
		//---------------------------------------------------------------------------------------
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z)+CG_Y;
		//System.out.println(terrainHeight);
		if(super.getPosition().y<(terrainHeight+CG_Y)) {
			Spacecraft.currentVerticalSpeed=0;
			 super.getPosition().y=terrainHeight+CG_Y;
		}
		Spacecraft.currentSpeed = realTimeResultSet.getVelocity();
		//Spacecraft.setAzimuth(realTimeResultSet.getAzi());
	
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
			Spacecraft.isThrust=true;
		} else {
			Spacecraft.isThrust=false;
		}
		
		//----------------------------------------------------
		//				RCS - Rotation
		//----------------------------------------------------
	    double[][] ThrustMomentum = {{0},
				  					 {0},
				  					 {0}};
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
		     ThrustMomentum[2][0] = MRCS[2][0];
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			ThrustMomentum[2][0] = -MRCS[2][0];
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
		     ThrustMomentum[0][0] = MRCS[0][0];
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			ThrustMomentum[0][0] = -MRCS[0][0];
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
		     ThrustMomentum[1][0] = MRCS[1][0];
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			ThrustMomentum[1][0] = -MRCS[1][0];
		}
		Spacecraft.setThrust_Momentum(ThrustMomentum);
		//---------------------------------------------
		// 			Environment Inputs
		//---------------------------------------------
		if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
			Spacecraft.setPosition(new Vector3f(0,100,0));
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
	    double rm = RealTimeSimulation.getRm();
	    double ref = RealTimeSimulation.getRef_ELEVATION();
//Spacecraft.getPosition().y
		realTimeResultSet = RealTimeSimulation.Launch_Integrator(INTEGRATOR, target, 0, 0, Spacecraft.getPosition().y+rm+ref, vel, fpa, 
				azi, Spacecraft.getSCMass(), timestep, timestep, 0, Spacecraft.getQuarternions(), Spacecraft.getPQR(), 
			    isThrust, Thrust_Momentum, spaceShip);
		Spacecraft.setSCPropMass((float) (spaceShip.getPropulsion().getPrimaryPropellant()-(spaceShip.getMass()-realTimeResultSet.getSCMass())));
		double[][] intPQR = {{ realTimeResultSet.getPQR()[0][0]},
				 		    {  realTimeResultSet.getPQR()[1][0]},
				 		    {  realTimeResultSet.getPQR()[2][0]}}; 
		Spacecraft.setPQR(intPQR);
		Spacecraft.setQuarternions(realTimeResultSet.getQuarternions());
		Spacecraft.setSCMass(realTimeResultSet.getSCMass());
		System.out.println(realTimeResultSet.getSCMass());
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


	
	
}
