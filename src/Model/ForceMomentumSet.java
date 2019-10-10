package Model;

public class ForceMomentumSet {
	
	  private double[][] F_Aero_A      = {{0},{0},{0}};						// Aerodynamic Force with respect to Aerodynamic coordinate frame [N]
	  double[][] F_Aero_NED    = {{0},{0},{0}};						// Aerodynamic Force with respect to NED frame [N]
	  double[][] F_Thrust_B    = {{0},{0},{0}};						// Thrust Force in body fixed system     [N]
	  double[][] F_Thrust_NED  = {{0},{0},{0}};						// Thrust Force in NED frame    		 [N]
	  double[][] F_Gravity_G   = {{0},{0},{0}};						// Gravity Force in ECEF coordinates     [N]
	  double[][] F_Gravity_NED = {{0},{0},{0}};						// Gravity Force in NED Frame            [N]
	  double[][] F_total_NED   = {{0},{0},{0}};						// Total force vector in NED coordinates [N]
	
	  double[][] M_Aero_NED      = {{0},{0},{0}};
	  double[][] M_Thrust_NED    = {{0},{0},{0}};
	  double[][] M_total_NED     = {{0},{0},{0}};
	
	  double[][] M_Aero_A      = {{0},{0},{0}};
	  double[][] M_Aero_B      = {{0},{0},{0}};
	  double[][] M_Thrust_B    = {{0},{0},{0}};
	  
	  double thrustTotal =0;
	  double RCSThrustX=0;
	  double RCSThrustY=0;
	  double RCSThrustZ=0;
	  
	  
	 
	public double getRCSThrustX() {
		return RCSThrustX;
	}

	public void setRCSThrustX(double rCSThrustX) {
		RCSThrustX = rCSThrustX;
	}

	public double getRCSThrustY() {
		return RCSThrustY;
	}

	public void setRCSThrustY(double rCSThrustY) {
		RCSThrustY = rCSThrustY;
	}

	public double getRCSThrustZ() {
		return RCSThrustZ;
	}

	public void setRCSThrustZ(double rCSThrustZ) {
		RCSThrustZ = rCSThrustZ;
	}

	public double[][] getM_total_NED() {
		return M_total_NED;
	}

	public void setM_total_NED(double[][] m_total_NED) {
		M_total_NED = m_total_NED;
	}

	public double getThrustTotal() {
		return thrustTotal;
	}

	public void setThrustTotal(double thrustTotal) {
		this.thrustTotal = thrustTotal;
	}

	public ForceMomentumSet() {
		
	}
	
	public  double[][] getF_Aero_A() {
		return F_Aero_A;
	}
	public  void setF_Aero_A(double[][] f_Aero_A) {
		F_Aero_A = f_Aero_A;
	}
	public  double[][] getF_Aero_NED() {
		return F_Aero_NED;
	}
	public  void setF_Aero_NED(double[][] f_Aero_NED) {
		F_Aero_NED = f_Aero_NED;
	}
	public  double[][] getF_Thrust_B() {
		return F_Thrust_B;
	}
	public  void setF_Thrust_B(double[][] f_Thrust_B) {
		F_Thrust_B = f_Thrust_B;
	}
	public  double[][] getF_Thrust_NED() {
		return F_Thrust_NED;
	}
	public  void setF_Thrust_NED(double[][] f_Thrust_NED) {
		F_Thrust_NED = f_Thrust_NED;
	}
	public  double[][] getF_Gravity_G() {
		return F_Gravity_G;
	}
	public  void setF_Gravity_G(double[][] f_Gravity_G) {
		F_Gravity_G = f_Gravity_G;
	}
	public  double[][] getF_Gravity_NED() {
		return F_Gravity_NED;
	}
	public  void setF_Gravity_NED(double[][] f_Gravity_NED) {
		F_Gravity_NED = f_Gravity_NED;
	}
	public  double[][] getF_total_NED() {
		return F_total_NED;
	}
	public  void setF_total_NED(double[][] f_total_NED) {
		F_total_NED = f_total_NED;
	}
	public  double[][] getM_Aero_NED() {
		return M_Aero_NED;
	}
	public  void setM_Aero_NED(double[][] m_Aero_NED) {
		M_Aero_NED = m_Aero_NED;
	}
	public  double[][] getM_Thrust_NED() {
		return M_Thrust_NED;
	}
	public  void setM_Thrust_NED(double[][] m_Thrust_NED) {
		M_Thrust_NED = m_Thrust_NED;
	}
	public  double[][] getM_Aero_A() {
		return M_Aero_A;
	}
	public  void setM_Aero_A(double[][] m_Aero_A) {
		M_Aero_A = m_Aero_A;
	}
	public  double[][] getM_Aero_B() {
		return M_Aero_B;
	}
	public  void setM_Aero_B(double[][] m_Aero_B) {
		M_Aero_B = m_Aero_B;
	}
	public  double[][] getM_Thrust_B() {
		return M_Thrust_B;
	}
	public  void setM_Thrust_B(double[][] m_Thrust_B) {
		M_Thrust_B = m_Thrust_B;
	}
	
	

}
