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
	private  float currentVerticalSpeed   = 0;
	private  float currentHorizontalSpeed = 45;
	private  float currentSpeed           = (float) Math.sqrt(currentVerticalSpeed*currentVerticalSpeed + currentHorizontalSpeed*currentHorizontalSpeed);
	private  float currentTurnSpeed       = 0;
	
    private static boolean isThrust = false; 
    private static final float SCMass_init = 15000; 
    private static final float SCPropMass_init = 4000;
    private static float SCMass = SCMass_init;
    private static float SCPropMass = SCPropMass_init;
    private static float SCMainThrust =30000;
	
	//private float mouseSensitivity = 0.1f;
	//private float mouseWheelSensitivity =0.001f;

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
		float dx = (float) (distance * Math.sin(Math.toRadians(45)));
		float dz = (float) (distance * Math.cos(Math.toRadians(45)));
		super.increaseRotation(0, (float) Math.toDegrees(animationSet.getAngularRateY())*DisplayManager.getFrameTimeSeconds(), 0);
		super.increasePosition(dx, 0, dz);
		this.currentHorizontalSpeed = animationSet.getV_h();
		this.currentVerticalSpeed = animationSet.getV_v();
		super.increasePosition(0, currentVerticalSpeed * DisplayManager.getFrameTimeSeconds() , 0);

		//---------------------------------------------------------------------------------------
		//			Terrain collision detection
		//---------------------------------------------------------------------------------------
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z)+CG_Y;
		//System.out.println(terrainHeight);
		if(super.getPosition().y<(terrainHeight+CG_Y)) {
			 this.currentVerticalSpeed=0;
			 super.getPosition().y=terrainHeight+CG_Y;
		}
		this.currentSpeed = (float) Math.sqrt(this.currentHorizontalSpeed*this.currentHorizontalSpeed+this.currentVerticalSpeed*this.currentVerticalSpeed);
	}
	
	public void fly(Terrain terrain) {
		checkInputs();
		RealTimeResultSet realTimeResultSet = getRealTimeResultSet(DisplayManager.getFrameTimeSeconds());
		this.currentHorizontalSpeed = (float) (realTimeResultSet.getVelocity()*Math.cos(realTimeResultSet.getFpa()));
		this.currentVerticalSpeed   = (float) (realTimeResultSet.getVelocity()*Math.sin(realTimeResultSet.getFpa()));
		float distance = (float) (this.currentHorizontalSpeed *DisplayManager.getFrameTimeSeconds());
		float dx = (float) (distance * Math.sin(Spacecraft.getAzimuth()));
		float dz = (float) (distance * Math.cos(Spacecraft.getAzimuth()));
		super.increasePosition(dx, currentVerticalSpeed * DisplayManager.getFrameTimeSeconds() , dz);
		//super.increaseRotation(0, (float) Math.toDegrees(realTimeResultSet.getFpa()), 0);
		//super.setRotZ((float) (Math.toDegrees(realTimeResultSet.getFpa() + 90)));
		Spacecraft.setSCMass(realTimeResultSet.getSCMass());
		//---------------------------------------------------------------------------------------
		//			Terrain collision detection
		//---------------------------------------------------------------------------------------
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z)+CG_Y;
		//System.out.println(terrainHeight);
		if(super.getPosition().y<(terrainHeight+CG_Y)) {
			 this.currentVerticalSpeed=0;
			 super.getPosition().y=terrainHeight+CG_Y;
		}
		this.currentSpeed = realTimeResultSet.getVelocity();
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

	private void checkInputs() {
		/*
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentHorizontalSpeed = RUN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentHorizontalSpeed = - RUN_SPEED;
		} else {
			this.currentHorizontalSpeed = 0;
		}
		//System.out.println(this.currentHorizontalSpeed);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.currentTurnSpeed = -TURN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.currentTurnSpeed =  TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		*/
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Spacecraft.isThrust=true;
		} else {
			Spacecraft.isThrust=false;
		}
		
		
	}

	public  float getCurrentSpeed() {
		return currentSpeed;
	}

	public  float getCurrentVerticalSpeed() {
		return currentVerticalSpeed;
	}

	public  float getCurrentHorizontalSpeed() {
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

	public  RealTimeResultSet getRealTimeResultSet(float timestep) {
		RealTimeResultSet realTimeResultSet = new RealTimeResultSet();
		
	    double vel = 0;
	    double fpa = 0;
	    double azi = 0;
	    if(getCurrentSpeed()==0 || Double.isNaN(getCurrentSpeed()) || getCurrentSpeed()<0) {
	    	vel=30;
	    } else {
	    	 vel = getCurrentSpeed();
	     fpa = Math.asin(getCurrentVerticalSpeed()/getCurrentSpeed());
	     azi = Spacecraft.getAzimuth();
	    }
		
		int INTEGRATOR = 1;
		int target = 1;
		if(timestep<0.01f) {
			timestep = 0.01f;
			System.out.println("WARNING: timestep too small");
		} else {
			//System.out.println("Timestep: "+timestep);
		}
		double t =timestep;
		double dt_write = timestep;
		double reference_elevation=0;
		double SurfaceArea_INP=10;
	    double[][] InertiaTensorMatrix   =         {{   8000    ,    0       ,   0},
													{      0    ,    8000    ,   0},
													{      0    ,    0       ,   8000}};
	    double[][] Init_Quarternions= 	{{1},
									  	 {0},
									     {0},
									  	 {0}}; 
	    double thrust = 30000;
	    double rm = RealTimeSimulation.getRm();
	    double ref = RealTimeSimulation.getRef_ELEVATION();
		realTimeResultSet = RealTimeSimulation.Launch_Integrator(INTEGRATOR, target, 0, 0, this.getPosition().y+rm+ref, vel, fpa, 
				azi, Spacecraft.getSCMass(), t, dt_write, reference_elevation, SurfaceArea_INP, InertiaTensorMatrix, Init_Quarternions, 
				thrust, isThrust, Spacecraft.getScpropmassInit());
		Spacecraft.setSCPropMass(Spacecraft.getScpropmassInit()-(Spacecraft.SCMass_init-realTimeResultSet.getSCMass()));
		return realTimeResultSet;

	}

}
