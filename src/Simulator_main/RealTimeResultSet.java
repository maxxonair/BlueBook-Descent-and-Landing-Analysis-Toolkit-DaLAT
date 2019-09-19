package Simulator_main;

public class RealTimeResultSet {
	
	private float altitude=0;	
	private float velocity=0;
	private float fpa=0;
	private float azi=(float) Math.toRadians(45);
	
	private float SCMass =0;
	
	private float eulerX=0;
	private float eulerY=0;
	private float eulerZ=0;
	
	private float AngulRateX=0;
	private float AngulRateY=0;
	private float AngulRateZ=0;
	
	private double[][] quarternions = {	{0},
										{0},
										{0},
										{0}}; 
	
	
	public double[][] getQuarternions() {
		return quarternions;
	}

	public void setQuarternions(double[][] quarternions) {
		this.quarternions = quarternions;
	}

	public float getEulerX() {
		return eulerX;
	}

	public void setEulerX(float eulerX) {
		this.eulerX = eulerX;
	}

	public float getEulerY() {
		return eulerY;
	}

	public void setEulerY(float eulerY) {
		this.eulerY = eulerY;
	}

	public float getEulerZ() {
		return eulerZ;
	}

	public void setEulerZ(float eulerZ) {
		this.eulerZ = eulerZ;
	}

	public float getAngulRateX() {
		return AngulRateX;
	}

	public void setAngulRateX(float angulRateX) {
		AngulRateX = angulRateX;
	}

	public float getAngulRateY() {
		return AngulRateY;
	}

	public void setAngulRateY(float angulRateY) {
		AngulRateY = angulRateY;
	}

	public float getAngulRateZ() {
		return AngulRateZ;
	}

	public void setAngulRateZ(float angulRateZ) {
		AngulRateZ = angulRateZ;
	}

	public float getSCMass() {
		return SCMass;
	}

	public void setSCMass(float sCMass) {
		SCMass = sCMass;
	}

	public RealTimeResultSet() {
		
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getFpa() {
		return fpa;
	}

	public void setFpa(float fpa) {
		this.fpa = fpa;
	}

	public float getAzi() {
		return azi;
	}

	public void setAzi(float azi) {
		this.azi = azi;
	}

}
