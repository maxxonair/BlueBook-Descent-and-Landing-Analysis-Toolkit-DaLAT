package FlightElement.GNCModel.Controller;

public class PID {

	public double P;
	public double I;
	public double D;
	
	public double min;
	public double max;
	
	public PID(double P, double I, double D, double min, double max) {
		this.P = P;
		this.I = I;
		this.D = D;
		
		this.min = min;
		this.max = max;
	}
		
}
