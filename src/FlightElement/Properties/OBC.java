package FlightElement.Properties;


// Onboard Computing Unit 
public class OBC {

	// Frequency of GNC execution [Hz]
	private double controllerFrequency=1;
	// Frequency of the environment simulation [Hz]:
	private double environmentFrequency=0;
	
	public OBC() {
		
	}

	public double getControllerFrequency() {
		return controllerFrequency;
	}

	public void setControllerFrequency(double controllerFrequency) {
		this.controllerFrequency = controllerFrequency;
	}

	public double getEnvironmentFrequency() {
		return environmentFrequency;
	}

	public void setEnvironmentFrequency(double environmentFrequency) {
		this.environmentFrequency = environmentFrequency;
	}
		
	
}
