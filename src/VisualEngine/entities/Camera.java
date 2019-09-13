package VisualEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float defaultX =100;
	private float defaultY =20;
	private float defaultZ =240;
	
	private Vector3f position = new Vector3f(defaultX,defaultY,defaultZ);
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
			position.x = defaultX;
			position.y = defaultY;
			position.z = defaultZ;
			roll=0;
			pitch=0;
			yaw=130;
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
