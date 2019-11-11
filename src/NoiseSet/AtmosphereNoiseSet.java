package NoiseSet;

public class AtmosphereNoiseSet {

	private double densityNoise=0;
	private double staticTemperatureNoise=0;
	private double gammaNoise=0;
	
	private double staticTemperatureMaximumExpectedVariation=30;
	
	
	
	public double getStaticTemperatureMaximumExpectedVariation() {
		return staticTemperatureMaximumExpectedVariation;
	}

	public void setStaticTemperatureMaximumExpectedVariation(double staticTemperatureMaximumExpectedVariation) {
		this.staticTemperatureMaximumExpectedVariation = staticTemperatureMaximumExpectedVariation;
	}

	public AtmosphereNoiseSet() {
		
	}

	public double getDensityNoise() {
		return densityNoise;
	}

	public void setDensityNoise(double densityNoise) {
		this.densityNoise = densityNoise;
	}

	public double getStaticTemperatureNoise() {
		return staticTemperatureNoise;
	}

	public void setStaticTemperatureNoise(double staticTemperatureNoise) {
		this.staticTemperatureNoise = staticTemperatureNoise;
	}

	public double getGammaNoise() {
		return gammaNoise;
	}

	public void setGammaNoise(double gammaNoise) {
		this.gammaNoise = gammaNoise;
	}
	
	
}
