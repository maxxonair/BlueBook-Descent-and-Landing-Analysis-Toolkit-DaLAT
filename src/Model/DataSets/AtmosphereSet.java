package Model.DataSets;

public class AtmosphereSet {

    public   double DynamicPressure=0;
    public   double Density =0;
    public   double gamma =0;
    public   double GasConstant=0;
    public   double Mach=0;
    public   double StaticTemperature=0;
    public   double StaticPressure = 0 ;      				//   pressure [Pa]
    public   double PressureCoefficient = 0;				//  

    
    

	public   double getDynamicPressure() {
		return DynamicPressure;
	}
	public   void setDynamicPressure(double dynamicPressure) {
		DynamicPressure = dynamicPressure;
	}
	public   double getDensity() {
		return Density;
	}
	public   void setDensity(double density) {
		Density = density;
	}
	public   double getGamma() {
		return gamma;
	}
	public   void setGamma(double gamma) {
		this.gamma = gamma;
	}
	public   double getGasConstant() {
		return GasConstant;
	}
	public   void setGasConstant(double gasConstant) {
		GasConstant = gasConstant;
	}
	public   double getMach() {
		return Mach;
	}
	public   void setMach(double mach) {
		Mach = mach;
	}
	public   double getStaticTemperature() {
		return StaticTemperature;
	}
	public   void setStaticTemperature(double  Temperature) {
		StaticTemperature =  Temperature;
	}
	public   double getStaticPressure() {
		return StaticPressure;
	}
	public   void setStaticPressure(double  Pressure) {
		StaticPressure =  Pressure;
	}
	public   double getPressureCoefficient() {
		return PressureCoefficient;
	}
	public   void setPressureCoefficient(double pressureCoefficient) {
		PressureCoefficient = pressureCoefficient;
	}
    
}