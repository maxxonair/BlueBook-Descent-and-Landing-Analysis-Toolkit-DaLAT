package Simulator_main;

public class RealTimeResultSet {
	
	private float altitude=0;	
	private float velocity=0;
	private float fpa=0;
	private float azi=(float) Math.toRadians(45);
	
	private float SCMass =0;
	
	
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
