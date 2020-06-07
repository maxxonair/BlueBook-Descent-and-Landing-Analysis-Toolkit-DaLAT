package Simulation.Model.Atmosphere;
public class atm_dataset {
	private double  altitude;
	private double density;
	private double temperature;
	private double gasconstant;
	private double gamma;
	//----------------------------------------------------------------------------------------------------------
	public atm_dataset(double altitude, double density, double temperature, double gasconstant, double gamma){
		this.altitude = altitude;
		this.density = density;
		this.temperature = temperature;
		this.gasconstant = gasconstant;
		this.gamma = gamma; 
	}
	//----------------------------------------------------------------------------------------------------------
	public double get_altitude() {
		return altitude;
	}
	public double get_density() {
		return density;
	}
	public double get_temperature(){
		return temperature;
	}
	public double get_gasconstant(){
		return gasconstant;
	}
	public double get_gamma(){
		return gamma; 
	}
	//----------------------------------------------------------------------------------------------------------
	public void set_altidue(double NewValue){
		altitude = NewValue;
	}
	public void set_density(double NewValue){
		density = NewValue;
	}
	public void set_temperature(double NewValue){
		temperature = NewValue;
	}
	public void set_gasconstant(double NewValue){
		gasconstant = NewValue;
	}
	public void set_gamma(double NewValue){
		gamma = NewValue;
	}
}