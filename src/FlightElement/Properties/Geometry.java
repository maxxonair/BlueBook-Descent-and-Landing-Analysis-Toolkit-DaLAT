package FlightElement.Properties;

public class Geometry {

	double vehicleLength = 0;  // Vehicle length [m]
	double CoT  = 0;		// Center of thrust forces (on the center line with respect to the set zero)
	
	
	public Geometry() {
		
	}


	public double getVehicleLength() {
		return vehicleLength;
	}


	public void setVehicleLength(double vehicleLength) {
		this.vehicleLength = vehicleLength;
	}


	public double getCoT() {
		return CoT;
	}


	public void setCoT(double coT) {
		CoT = coT;
	}
	
	
}
