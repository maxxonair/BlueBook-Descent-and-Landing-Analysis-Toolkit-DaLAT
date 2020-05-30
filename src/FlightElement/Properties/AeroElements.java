package FlightElement.Properties;

public class AeroElements {
	
	private double SurfaceArea;				// Space Ship effective Surface area
	private double heatshieldRadius;			// Heat shield radius (capsule design)
	
	
	private double ParachuteSurfaceArea;
	
	private double ParachuteMass=0;
	
	private double HeatShieldMass=0;
	
	

	public AeroElements() {
		super();
	}
	
	

	public double getHeatShieldMass() {
		return HeatShieldMass;
	}



	public void setHeatShieldMass(double heatShieldMass) {
		HeatShieldMass = heatShieldMass;
	}



	public double getParachuteMass() {
		return ParachuteMass;
	}



	public void setParachuteMass(double parachuteMass) {
		ParachuteMass = parachuteMass;
	}



	public double getParachuteSurfaceArea() {
		return ParachuteSurfaceArea;
	}



	public void setParachuteSurfaceArea(double parachuteSurfaceArea) {
		ParachuteSurfaceArea = parachuteSurfaceArea;
	}



	public double getHeatshieldRadius() {
		return heatshieldRadius;
	}



	public void setHeatshieldRadius(double heatshieldRadius) {
		this.heatshieldRadius = heatshieldRadius;
	}



	public double getSurfaceArea() {
		return SurfaceArea;
	}

	public void setSurfaceArea(double surfaceArea) {
		SurfaceArea = surfaceArea;
	}
	
	

}
