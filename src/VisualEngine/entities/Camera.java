package VisualEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float defaultX =350;
	private float defaultY =20;
	private float defaultZ =10;
	
	private Vector3f position = new Vector3f(defaultX,defaultY,defaultZ);
	private float pitch=0;
	private float yaw=180;
	private float roll=0;
	
	private float mouseSensitivity = 0.1f;
	private float mouseWheelSensitivity =0.001f;
	
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
			position.x-= sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			position.x+= sensitivity;
		}
		
		// Position reset
		if(Keyboard.isKeyDown(Keyboard.KEY_P)){
			position.x = defaultX;
			position.y = defaultY;
			position.z = defaultZ;
			roll=0;
			pitch=0;
			yaw=180;
		}
		//
		if(Keyboard.isKeyDown(Keyboard.KEY_B)) {
				this.position.x+=Mouse.getDX()*mouseSensitivity;
				this.position.y+=Mouse.getDY()*mouseSensitivity;	
				this.position.z+=Mouse.getDWheel()*mouseWheelSensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_N)) {
			yaw+=Mouse.getDX()*mouseSensitivity;
			pitch+=Mouse.getDY()*mouseSensitivity;
			System.out.println(""+yaw);
			
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


	public float getMouseSensitivity() {
		return mouseSensitivity;
	}


	public void setMouseSensitivity(float mouseSensitivity) {
		this.mouseSensitivity = mouseSensitivity;
	}


	public float getMouseWheelSensitivity() {
		return mouseWheelSensitivity;
	}


	public void setMouseWheelSensitivity(float mouseWheelSensitivity) {
		this.mouseWheelSensitivity = mouseWheelSensitivity;
	}
	
	

}
