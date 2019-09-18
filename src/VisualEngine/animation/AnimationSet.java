package VisualEngine.animation;

public class AnimationSet {

	private float x;
	private float y;
	private float z;
	
	private float v_h;
	private float v_v;
	
	private float time;
	
	private float azimuth;
	
	private float alt_init;
	
	private float rotX;
	private float rotY;
	private float rotZ;
	
	private float AngularRateX;
	private float AngularRateY;
	private float AngularRateZ;
	

	public float getAngularRateX() {
		return AngularRateX;
	}

	public void setAngularRateX(float angularRateX) {
		AngularRateX = angularRateX;
	}

	public float getAngularRateY() {
		return AngularRateY;
	}

	public void setAngularRateY(float angularRateY) {
		AngularRateY = angularRateY;
	}

	public float getAngularRateZ() {
		return AngularRateZ;
	}

	public void setAngularRateZ(float angularRateZ) {
		AngularRateZ = angularRateZ;
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

	public float getAlt_init() {
		return alt_init;
	}

	public void setAlt_init(float alt_init) {
		this.alt_init = alt_init;
	}

	public float getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(float azimuth) {
		this.azimuth = azimuth;
	}

	public AnimationSet() {

	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getV_h() {
		return v_h;
	}

	public void setV_h(float v_h) {
		this.v_h = v_h;
	}

	public float getV_v() {
		return v_v;
	}

	public void setV_v(float v_v) {
		this.v_v = v_v;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}
	
	
}
