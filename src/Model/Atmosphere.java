package Model;

public class Atmosphere {
	public   double DragForce = 0;
	public   double SideForce = 0;
	public   double LiftForce = 0;
    public   double DragCoefficient=0;
    public   double C_SF=0;
    public   double LiftCoefficient=0;
    public   double DynamicPressure=0;
    public   double SurfaceArea =0;
    public   double BankAngle =0;
    public   double AngleOfAttack=0;
    public   double AngleOfSideslip=0; 
    public   double Density =0;
    public   double gamma =0;
    public   double GasConstant=0;
    public   double Mach=0;
    public   double StaticTemperature=0;
    public   double CdC=0; 
    public   double StaticPressure = 0 ;      				//   pressure [Pa]
    public   double PressureCoefficient = 0;				//  
    public   int flowzone=0; 								// Flow zone continuum - transitional - free molecular flwo
    public   double DragCoefficientContinuumFlow = 0; 	    // Drag coefficient in contiuum flow;
    
    
	public   double getDragForce() {
		return DragForce;
	}
	public   void setDragForce(double dragForce) {
		DragForce = dragForce;
	}
	public   double getSideForce() {
		return SideForce;
	}
	public   void setSideForce(double sideForce) {
		SideForce = sideForce;
	}
	public   double getLiftForce() {
		return LiftForce;
	}
	public   void setLiftForce(double liftForce) {
		LiftForce = liftForce;
	}
	public   double getDragCoefficient() {
		return DragCoefficient;
	}
	public   void setDragCoefficient(double dragCoefficient) {
		DragCoefficient = dragCoefficient;
	}
	public   double getC_SF() {
		return C_SF;
	}
	public   void setC_SF(double c_SF) {
		C_SF = c_SF;
	}
	public   double getLiftCoefficient() {
		return LiftCoefficient;
	}
	public   void setLiftCoefficient(double liftCoefficient) {
		LiftCoefficient = liftCoefficient;
	}
	public   double getDynamicPressure() {
		return DynamicPressure;
	}
	public   void setDynamicPressure(double dynamicPressure) {
		DynamicPressure = dynamicPressure;
	}
	public   double getSurfaceArea() {
		return SurfaceArea;
	}
	public   void setSurfaceArea(double surfaceArea) {
		SurfaceArea = surfaceArea;
	}
	public   double getBankAngle() {
		return BankAngle;
	}
	public   void setBankAngle(double bankAngle) {
		BankAngle = bankAngle;
	}
	public   double getAngleOfAttack() {
		return AngleOfAttack;
	}
	public   void setAngleOfAttack(double angleOfAttack) {
		AngleOfAttack = angleOfAttack;
	}
	public   double getAngleOfSideslip() {
		return AngleOfSideslip;
	}
	public   void setAngleOfSideslip(double angleOfSideslip) {
		AngleOfSideslip = angleOfSideslip;
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
	public   double getCdC() {
		return CdC;
	}
	public   void setCdC(double cdC) {
		CdC = cdC;
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
	public   int getFlowzone() {
		return flowzone;
	}
	public   void setFlowzone(int flowzone) {
		this.flowzone = flowzone;
	}
	public   double getDragCoefficientContinuumFlow() {
		return DragCoefficientContinuumFlow;
	}
	public   void setDragCoefficientContinuumFlow(double dragCoefficientContinuumFlow) {
		DragCoefficientContinuumFlow = dragCoefficientContinuumFlow;
	}
    
    
    
}
