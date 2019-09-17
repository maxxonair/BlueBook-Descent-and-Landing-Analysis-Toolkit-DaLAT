package VisualEngine.entities;

import VisualEngine.models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	protected float rotX;
	private float rotY;
	private float rotZ;
	private float scale;
	private float mouseSensitivity = 0.1f;
	private float mouseWheelSensitivity =0.001f;
	private float initX;
	private float initY;
	private float initZ;
	
	private int textureIndex = 0;

	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.initX=position.x;
		this.initY=position.y;
		this.initZ=position.z;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	public void move() {

		if(Keyboard.isKeyDown(Keyboard.KEY_P)){
			this.position.x=initX;
			this.position.y=initY;
			this.position.z=initZ;
			this.rotX=0;
			this.rotY=0;
			this.rotZ=0;
		}
if(!Keyboard.isKeyDown(Keyboard.KEY_B) && !Keyboard.isKeyDown(Keyboard.KEY_N)) {   // Keys B and N are reserved for camera movement (B - translation , N - rotation)
		if(Mouse.isButtonDown(0)){
			int value = Mouse.getDX();
			this.position.x-=value*mouseSensitivity;
		}
		if(Mouse.isButtonDown(0)){
			int value = Mouse.getDY();
			this.position.y+=value*mouseSensitivity;
		}
		if(Mouse.isButtonDown(1)){
			int value = Mouse.getDX();
			this.rotY+=value*mouseSensitivity;
		}
		if(Mouse.isButtonDown(1)){
			int value = Mouse.getDY();
			this.rotX-=value*mouseSensitivity;
		}
		int value = Mouse.getDWheel();
		this.position.z+=value*mouseWheelSensitivity;
}
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
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
    public float getTextureXOffset(){
        int column = textureIndex%model.getTexture().getNumberOfRows();
        return (float)column/(float)model.getTexture().getNumberOfRows();
    }
     
    public float getTextureYOffset(){
        int row = textureIndex/model.getTexture().getNumberOfRows();
        return (float)row/(float)model.getTexture().getNumberOfRows();
    }

}
