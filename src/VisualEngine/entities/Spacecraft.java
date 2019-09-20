package VisualEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import Simulator_main.RealTimeResultSet;
import Simulator_main.RealTimeSimulation;
import VisualEngine.animation.AnimationSet;
import VisualEngine.models.TexturedModel;
import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.terrains.Terrain;

public class Spacecraft extends Entity {
	
	private static final float TESTGRAVITY = -2;
	//private static final float TESTROCKET  =  40;
	
	private static float CG_Y = 0.9f;
	private static float azimuth=(float) Math.toRadians(45);
	

	@SuppressWarnings("unused")
	private  static  float currentVerticalSpeed   =   0;
	private  static  float currentHorizontalSpeed =  60;
	private  static  float currentSpeed           = (float) Math.sqrt(currentVerticalSpeed*currentVerticalSpeed + currentHorizontalSpeed*currentHorizontalSpeed);
	private  static  float currentTurnSpeed       =  0;
	
    private static boolean isThrust = false; 
    private static final float SCMass_init = 15000; 
    private static final float SCPropMass_init = 4000;
    private static float SCMass = SCMass_init;
    private static float SCPropMass = SCPropMass_init;
    private static float SCMainThrust =30000;
    
    private static double[][] Thrust_Momentum = {{0},
												 {0},
												 {1}};
	private static float AngulRateX=0;
	private static float AngulRateY=0;
	private static float AngulRateZ=0;
	
	private static double[][] quarternions = {	{1},
												{0},
												{0},
												{0}}; 
	
	private static float animationScale =100;
	
    static double[][] InertiaTensorMatrix   =         {{   8000    ,    0       ,   0},
												{      0    ,    8000    ,   0},
												{      0    ,    0       ,   8000}};
	
	//private float mouseSensitivity = 0.1f;
	//private float mouseWheelSensitivity =0.001f;
	private static boolean endTheShow = false;
	private static boolean nextTestFrame = false;

	public Spacecraft(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
		//this.setRotZ(90);
		//this.setRotY(90);
	}
	
	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentHorizontalSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		currentVerticalSpeed += TESTGRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, currentVerticalSpeed * DisplayManager.getFrameTimeSeconds() , 0);
		
		//---------------------------------------------------------------------------------------
		//			Terrain collision detection
		//---------------------------------------------------------------------------------------
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z)+CG_Y;
		//System.out.println(terrainHeight);
		if(super.getPosition().y<(terrainHeight+CG_Y)) {
			 currentVerticalSpeed=0;
			 super.getPosition().y=terrainHeight+CG_Y;
		}
		currentSpeed = (float) Math.sqrt(currentHorizontalSpeed*currentHorizontalSpeed+currentVerticalSpeed*currentVerticalSpeed);
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
		Spacecraft.setSCMass(realTimeResultSet.getSCMass());
		//---------------------------------------------------------------------------------------
		//			 		Rotation 
		//---------------------------------------------------------------------------------------
		Spacecraft.AngulRateX = (float) Math.toDegrees(realTimeResultSet.getAngulRateX());
		Spacecraft.AngulRateY = (float) Math.toDegrees(realTimeResultSet.getAngulRateZ());
		Spacecraft.AngulRateZ = (float) Math.toDegrees(realTimeResultSet.getAngulRateY());
		super.increaseRotation(Spacecraft.AngulRateX*DisplayManager.getFrameTimeSeconds(), 
							   Spacecraft.AngulRateY*DisplayManager.getFrameTimeSeconds(), 
							   Spacecraft.AngulRateZ*DisplayManager.getFrameTimeSeconds());
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
		Spacecraft.setAzimuth(realTimeResultSet.getAzi());
	
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

		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Spacecraft.isThrust=true;
		} else {
			Spacecraft.isThrust=false;
		}
		
		double rcs_z=0.01;
	    double[][] ThrustMomentum = {{0},
				  					 {0},
				  					 {0}};
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
		     ThrustMomentum[2][0] = rcs_z;
			//System.out.println("rcs+");
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			ThrustMomentum[2][0] = -rcs_z;
			//System.out.println("rcs-");
		}
		Spacecraft.setThrust_Momentum(ThrustMomentum);
		//---------------------------------------------
		// 			Environment Inputs
		//---------------------------------------------
		if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
			endTheShow=true;
		} else {
			endTheShow=false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			nextTestFrame=true;
		} else {
			nextTestFrame=false;
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

	public static float getScpropmassInit() {
		return SCPropMass_init;
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
		double t =timestep;
		double dt_write = timestep;
		double reference_elevation=0;
		double SurfaceArea_INP=10;
	    
	    double[][] PQR = {{0},{0},{0}};
	    PQR[0][0] = Spacecraft.getAngulRateX();
	    PQR[1][0] = Spacecraft.getAngulRateY();
	    PQR[2][0] = Spacecraft.getAngulRateZ();
	    double thrust = 30000;
	    double rm = RealTimeSimulation.getRm();
	    double ref = RealTimeSimulation.getRef_ELEVATION();
//Spacecraft.getPosition().y
		realTimeResultSet = RealTimeSimulation.Launch_Integrator(INTEGRATOR, target, 0, 0, Spacecraft.getPosition().y+rm+ref, vel, fpa, 
				azi, Spacecraft.getSCMass(), t, dt_write, reference_elevation, SurfaceArea_INP, Spacecraft.getInertiaTensorMatrix(), Spacecraft.getQuarternions(), 
				PQR, thrust, isThrust, Spacecraft.getScpropmassInit(), Spacecraft.getThrust_Momentum());
		Spacecraft.setSCPropMass(Spacecraft.getScpropmassInit()-(Spacecraft.SCMass_init-realTimeResultSet.getSCMass()));
		Spacecraft.setAngulRateX(realTimeResultSet.getAngulRateX());
		Spacecraft.setAngulRateY(realTimeResultSet.getAngulRateY());
		Spacecraft.setAngulRateZ(realTimeResultSet.getAngulRateZ());
		Spacecraft.setQuarternions(realTimeResultSet.getQuarternions());
		//System.out.println(realTimeResultSet.getAngulRateY());
		return realTimeResultSet;

	}

	public static float getAngulRateX() {
		return AngulRateX;
	}

	public static void setAngulRateX(float angulRateX) {
		AngulRateX = angulRateX;
	}

	public static float getAngulRateY() {
		return AngulRateY;
	}

	public static void setAngulRateY(float angulRateY) {
		AngulRateY = angulRateY;
	}

	public static float getAngulRateZ() {
		return AngulRateZ;
	}

	public static void setAngulRateZ(float angulRateZ) {
		AngulRateZ = angulRateZ;
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

	public static double[][] getInertiaTensorMatrix() {
		return InertiaTensorMatrix;
	}

	public void setInertiaTensorMatrix(double[][] inertiaTensorMatrix) {
		InertiaTensorMatrix = inertiaTensorMatrix;
	}

	public static boolean getisThrust() {
		return isThrust;
	}

	public static boolean isEndTheShow() {
		return endTheShow;
	}

	public static boolean isNextTestFrame() {
		return nextTestFrame;
	}


	
	
}
