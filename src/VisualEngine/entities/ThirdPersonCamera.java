package VisualEngine.entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class ThirdPersonCamera {
	/*
	private float defaultX =0;
	private float defaultY =0;
	private float defaultZ =0;
	*/
	private float distanceFromSpacecraft = 20;
	private float angleAroundSpacecraft = 0 ;
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch=20;
	private float yaw=0;
	private float roll=0;
	
	private float mouseSensitivity = 0.1f;
	private float mouseWheelSensitivity =0.001f;
	
	private Spacecraft spacecraft;
	
	public ThirdPersonCamera(Spacecraft spacecraft) {
		 this.spacecraft = spacecraft;
	}

	
	public void move(){
		calculateZoom();
		calculatePitch();
		calculateAngleAroundSpacecrfat();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition( horizontalDistance,  verticalDistance);	
		this.yaw = 180 - (spacecraft.getRotY() + angleAroundSpacecraft);
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromSpacecraft * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromSpacecraft * Math.sin (Math.toRadians(pitch)));
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = spacecraft.getRotY() + angleAroundSpacecraft;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ  = (float) (horizontalDistance * Math.cos (Math.toRadians(theta)));
		position.x = spacecraft.getPosition().x - offsetX;
		position.y = spacecraft.getPosition().y + verticalDistance;
		position.z = spacecraft.getPosition().z - offsetZ;
	}
	
	private void calculateZoom() {
		 float zoomLevel = Mouse.getDWheel() *  mouseWheelSensitivity; 
		 distanceFromSpacecraft -= zoomLevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(0)) {
			 float pitchChange =  Mouse.getDY()*mouseSensitivity;
			 pitch -= pitchChange;
		} 
	}
	
	private void  calculateAngleAroundSpacecrfat() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX()*mouseSensitivity; 
			angleAroundSpacecraft -= angleChange;
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
