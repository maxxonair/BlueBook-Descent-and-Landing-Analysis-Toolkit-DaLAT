package Simulation.NoiseSet;

public class AerodynamicNoiseSet extends Object implements Cloneable{

	private double dragCoeffNoise;
	private double liftCoeffNoise;
	private double sideCoeffNoise;
	private double parachuteDragCoeffNoise;
	
	
	public AerodynamicNoiseSet() {
		
	}
	

	public double getDragCoeffNoise() {
		return dragCoeffNoise;
	}



	public void setDragCoeffNoise(double dragCoeffNoise) {
		this.dragCoeffNoise = dragCoeffNoise;
	}



	public double getLiftCoeffNoise() {
		return liftCoeffNoise;
	}



	public void setLiftCoeffNoise(double liftCoeffNoise) {
		this.liftCoeffNoise = liftCoeffNoise;
	}



	public double getSideCoeffNoise() {
		return sideCoeffNoise;
	}



	public void setSideCoeffNoise(double sideCoeffNoise) {
		this.sideCoeffNoise = sideCoeffNoise;
	}



	public double getParachuteDragCoeffNoise() {
		return parachuteDragCoeffNoise;
	}



	public void setParachuteDragCoeffNoise(double parachuteDragCoeffNoise) {
		this.parachuteDragCoeffNoise = parachuteDragCoeffNoise;
	}



	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
}
