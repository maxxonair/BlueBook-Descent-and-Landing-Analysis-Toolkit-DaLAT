package Model.DataSets;

public class AerodynamicSet {
    public   int flowzone=0; 								// Flow zone continuum - transitional - free molecular flwo
    public   double DragCoefficientContinuumFlow = 0; 	    // Drag coefficient in contiuum flow;
    public   double CdC=0; 									// Continuum Flow Drag Coefficient [-]
    public   double SurfaceArea =0;
    public   double AerodynamicBankAngle =0;
    public   double AerodynamicAngleOfAttack=0;
    public   double AngleOfSideslip=0; 
	public   double DragForce = 0;
	public   double SideForce = 0;
	public   double LiftForce = 0;
    public   double DragCoefficient=0;
    public   double C_SF=0;
    public   double LiftCoefficient=0;
    public   double SideForceCoefficient=0;
    
    public double DragCoefficientParachute=0;
    public double DragForceParachute=0;
    
    
    
    
	public double getDragCoefficientParachute() {
		return DragCoefficientParachute;
	}
	public void setDragCoefficientParachute(double dragCoefficientParachute) {
		DragCoefficientParachute = dragCoefficientParachute;
	}
	public double getDragForceParachute() {
		return DragForceParachute;
	}
	public void setDragForceParachute(double dragForceParachute) {
		DragForceParachute = dragForceParachute;
	}
	public double getSideForceCoefficient() {
		return SideForceCoefficient;
	}
	public void setSideForceCoefficient(double sideForceCoefficient) {
		SideForceCoefficient = sideForceCoefficient;
	}
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
	public   double getSurfaceArea() {
		return SurfaceArea;
	}
	public   void setSurfaceArea(double surfaceArea) {
		SurfaceArea = surfaceArea;
	}

	public double getAerodynamicBankAngle() {
		return AerodynamicBankAngle;
	}
	public void setAerodynamicBankAngle(double aerodynamicBankAngle) {
		AerodynamicBankAngle = aerodynamicBankAngle;
	}
	public double getAerodynamicAngleOfAttack() {
		return AerodynamicAngleOfAttack;
	}
	public void setAerodynamicAngleOfAttack(double aerodynamicAngleOfAttack) {
		AerodynamicAngleOfAttack = aerodynamicAngleOfAttack;
	}
	public   double getAngleOfSideslip() {
		return AngleOfSideslip;
	}
	public   void setAngleOfSideslip(double angleOfSideslip) {
		AngleOfSideslip = angleOfSideslip;
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
	public   double getCdC() {
		return CdC;
	}
	public   void setCdC(double cdC) {
		CdC = cdC;
	}
}
