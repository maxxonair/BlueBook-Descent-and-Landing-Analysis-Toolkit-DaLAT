package FlightElement.State;

public class Propulsion {
	
	private double propellantLevelIsPrimary = 0 ;     // current total propellant mass [kg]
	private double propellantLevelIsSecondary = 0;    // current total propellant mass [kg]
	
	public Propulsion() {
		
	}

	public double getPropellantLevelIsPrimary() {
		return propellantLevelIsPrimary;
	}

	public void setPropellantLevelIsPrimary(double propellantLevelIsPrimary) {
		this.propellantLevelIsPrimary = propellantLevelIsPrimary;
	}

	public double getPropellantLevelIsSecondary() {
		return propellantLevelIsSecondary;
	}

	public void setPropellantLevelIsSecondary(double propellantLevelIsSecondary) {
		this.propellantLevelIsSecondary = propellantLevelIsSecondary;
	}

}
