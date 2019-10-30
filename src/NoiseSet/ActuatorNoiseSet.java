package NoiseSet;

public class ActuatorNoiseSet {
	
	private double PrimaryThrustNoise=0;
	private double RCSMomentumX=0;
	private double RCSMomentumY=0;
	private double RCSMomentumZ=0;
	
	public ActuatorNoiseSet() {
		
	}

	public double getPrimaryThrustNoise() {
		return PrimaryThrustNoise;
	}

	public void setPrimaryThrustNoise(double primaryThrustNoise) {
		PrimaryThrustNoise = primaryThrustNoise;
	}

	public double getRCSMomentumX() {
		return RCSMomentumX;
	}

	public void setRCSMomentumX(double rCSMomentumX) {
		RCSMomentumX = rCSMomentumX;
	}

	public double getRCSMomentumY() {
		return RCSMomentumY;
	}

	public void setRCSMomentumY(double rCSMomentumY) {
		RCSMomentumY = rCSMomentumY;
	}

	public double getRCSMomentumZ() {
		return RCSMomentumZ;
	}

	public void setRCSMomentumZ(double rCSMomentumZ) {
		RCSMomentumZ = rCSMomentumZ;
	}

	
}
