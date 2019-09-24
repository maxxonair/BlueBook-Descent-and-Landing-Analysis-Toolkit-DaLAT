package VisualEngine.entities;

import VisualEngine.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private TexturedModel model;
	private static Vector3f position;
	protected static float rotX;
	protected float rotY;
	protected float rotZ;
	private float scale;
	private float mouseSensitivity = 0.1f;
	private float mouseWheelSensitivity =0.001f;


	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.model = model;
		Entity.position = position;
		Entity.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz) {
		Entity.position.x += dx;
		Entity.position.y += dy;
		Entity.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		Entity.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	


	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public static Vector3f getPosition() {
		return position;
	}

	public static void setPosition(Vector3f position) {
		Entity.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public static void setRotX(float rotX) {
		Entity.rotX = rotX;
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


}
