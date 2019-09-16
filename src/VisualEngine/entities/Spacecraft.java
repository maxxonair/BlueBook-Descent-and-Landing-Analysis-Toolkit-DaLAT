package VisualEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import VisualEngine.models.TexturedModel;
import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.terrains.Terrain;

public class Spacecraft extends Entity {
	
	private static final float RUN_SPEED = 10;
	private static final float TURN_SPEED =160; 
	private static final float TESTGRAVITY = -20;
	private static final float TESTROCKET =  10;
	
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float currentVerticalSpeed = 0;

	public Spacecraft(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}
	
	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		currentVerticalSpeed += TESTGRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, currentVerticalSpeed * DisplayManager.getFrameTimeSeconds() , 0);
		
		//---------------------------------------------------------------------------------------
		//			Terrain collision detection
		//---------------------------------------------------------------------------------------
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		System.out.println(terrainHeight);
		if(super.getPosition().y<terrainHeight) {
			 currentVerticalSpeed=0;
			 super.getPosition().y=terrainHeight;
		}
	}
	
	private void pulse() {
		this.currentVerticalSpeed = TESTROCKET;
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = RUN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = - RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		//System.out.println(this.currentSpeed);
		
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

}
