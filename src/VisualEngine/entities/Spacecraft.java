package VisualEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import VisualEngine.animation.AnimationSet;
import VisualEngine.models.TexturedModel;
import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.terrains.Terrain;

public class Spacecraft extends Entity {
	
	private static final float RUN_SPEED = 10;
	private static final float TURN_SPEED =160; 
	private static final float TESTGRAVITY = -20;
	private static final float TESTROCKET =  10;
	
	private static float CG_Y = 0.9f;
	
	@SuppressWarnings("unused")
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float currentVerticalSpeed = 0;
	private float currentHorizontalSpeed = 0;
	
	//private float mouseSensitivity = 0.1f;
	//private float mouseWheelSensitivity =0.001f;

	public Spacecraft(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
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
		checkInputs();
		//super.increaseRotation(0, currentTurnSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		float distance = animationSet.getV_h()*DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
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
	
	private void pulse() {
		this.currentVerticalSpeed = TESTROCKET;
	}
	
	private void checkInputs() {
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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			pulse();
		} 
		
		
	}

	public float getCurrentSpeed() {
		return currentSpeed;
	}

	public float getCurrentVerticalSpeed() {
		return currentVerticalSpeed;
	}

	public float getCurrentHorizontalSpeed() {
		return currentHorizontalSpeed;
	}

}
