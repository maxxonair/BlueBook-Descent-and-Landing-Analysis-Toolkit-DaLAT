package VisualEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float defaultX ;
	private float defaultY ;
	private float defaultZ ;
	
	private Vector3f position = new Vector3f(defaultX,defaultY,defaultZ);
	private float pitch;
	private float yaw;
	private float roll;
	
	private float mouseSensitivity = 0.1f;
	private float mouseWheelSensitivity =0.001f;
	
	public Camera(Vector3f position, float yaw, float pitch, float roll){
		this.position.x=position.x;
		this.position.y=position.y;
		this.position.z=position.z;
		this.defaultX=position.x;
		this.defaultY=position.y;
		this.defaultZ=position.z;
		this.yaw=yaw;
		this.pitch=pitch;
		this.roll=roll;
	}
	
	
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
			//System.out.println(""+yaw);			
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


	public void setPosition(Vector3f position) {
		this.position = position;
	}


	public void setPitch(float pitch) {
		this.pitch = pitch;
	}


	public void setYaw(float yaw) {
		this.yaw = yaw;
	}


	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	

}
