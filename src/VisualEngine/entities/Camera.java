package VisualEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(){}
	
	
	public void move(){
		float sensitivity =0.5f;
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			position.z+= sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			position.z-= sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			position.y+= sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			position.y-= sensitivity;
		}
		
		// Position reset
		if(Keyboard.isKeyDown(Keyboard.KEY_P)){
			position.x = 0;
			position.y = 0;
			position.z = 0;
		}
		
	}
	

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	

}
