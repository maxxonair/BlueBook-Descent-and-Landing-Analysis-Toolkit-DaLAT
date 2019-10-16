package FlightElement;

public class AeroElements {
	
	private double SurfaceArea;				// Space Ship effective Surface area
	private double heatshieldRadius;			// Heat shield radius (capsule design)
	
	
	private double ParachuteSurfaceArea;
	
	

	public AeroElements() {
		super();
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
