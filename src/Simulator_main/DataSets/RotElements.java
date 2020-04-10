package Simulator_main.DataSets;

public class RotElements {

	// Equation Elements for angular velocity equations: 
	
	 public double det_I = 0;
	 public double EE_I01 = 0;
	 public double EE_I02 = 0;
	 public double EE_I03 = 0;
	 public double EE_I04 = 0;
	 public double EE_I05 = 0;
	 public double EE_I06 = 0;
	
	 public double EE_P_pp = 0;
	 public double EE_P_pq = 0;
	 public double EE_P_pr = 0;
	 public double EE_P_qq = 0;
	 public double EE_P_qr = 0;
	 public double EE_P_rr = 0;
	 public double EE_P_x  = 0;
	 public double EE_P_y  = 0;
	 public double EE_P_z  = 0;
	
	 public double EE_Q_pp = 0;
	 public double EE_Q_pq = 0;
	 public double EE_Q_pr = 0;
	 public double EE_Q_qq = 0;
	 public double EE_Q_qr = 0;
	 public double EE_Q_rr = 0;
	 public double EE_Q_x  = 0;
	 public double EE_Q_y  = 0;
	 public double EE_Q_z  = 0;
	
	 public double EE_R_pp = 0;
	 public double EE_R_pq = 0;
	 public double EE_R_pr = 0;
	 public double EE_R_qq = 0;
	 public double EE_R_qr = 0;
	 public double EE_R_rr = 0;
	 public double EE_R_x  = 0;
	 public double EE_R_y  = 0;
	 public double EE_R_z  = 0;
	
	public void RotElement() {
		
	}
	
	public  void Set_AngularVelocityEquationElements(double[] x, double[][] InertiaTensor) {
		double Ixx = InertiaTensor[0][0];
		double Iyy = InertiaTensor[1][1];
		double Izz = InertiaTensor[2][2];
		double Ixy = InertiaTensor[0][1];
		double Ixz = InertiaTensor[0][2];
		//  double Iyx = InertiaTensor[][];
		double Iyz = InertiaTensor[2][1];
		//double Izx = InertiaTensor[][];
		//double Izy = InertiaTensor[][];
		 det_I = Ixx*Iyy*Izz - 2*Ixy*Ixz*Iyz - Ixx*Iyz*Iyz - Iyy*Ixz*Ixz - Izz*Iyz*Iyz;
		 EE_I01 = Iyy*Izz - Iyz*Iyz;
		 EE_I02 = Ixy*Izz + Iyz*Ixz;
		 EE_I03 = Ixy*Iyz + Iyy*Ixz;
		 EE_I04 = Ixx*Izz - Ixz*Ixz;
		 EE_I05 = Ixx*Iyz + Ixy*Ixz;
		 EE_I06 = Ixx*Iyy - Ixy*Ixy;
		
		 EE_P_pp = -(Ixz*EE_I02 - Ixy*EE_I03)/det_I;
		 EE_P_pq =  (Ixz*EE_I01 - Iyz*EE_I02 - (Iyy - Ixx)*EE_I03)/det_I;
		 EE_P_pr = -(Ixy*EE_I01 + (Ixx-Izz)*EE_I02 - Iyz*EE_I03)/det_I;
		 EE_P_qq =  (Iyz*EE_I01 - Ixy*EE_I03)/det_I;
		 EE_P_qr = -((Izz-Iyy)*EE_I01 - Ixy*EE_I02 + Ixz*EE_I03)/det_I;
		 EE_P_rr = -(Iyz*EE_I01 - Ixz*EE_I02)/det_I;
		 EE_P_x  =   EE_I01/det_I;
		 EE_P_y  =   EE_I02/det_I;
		 EE_P_z  =   EE_I03/det_I;
		
		 EE_Q_pp = -(Ixz*EE_I02 - Ixy*EE_I05)/det_I;
		 EE_Q_pq =  (Ixz*EE_I02 - Iyz*EE_I04 - (Iyy - Ixx)*EE_I05)/det_I;
		 EE_Q_pr = -(Ixy*EE_I02 + (Ixx-Izz)*EE_I04 - Iyz*EE_I05)/det_I;
		 EE_Q_qq =  (Iyz*EE_I02 - Ixy*EE_I05)/det_I;
		 EE_Q_qr = -((Izz-Iyy)*EE_I02 - Ixy*EE_I04 + Ixz*EE_I05)/det_I;
		 EE_Q_rr = -(Iyz*EE_I02 - Ixz*EE_I04)/det_I;
		 EE_Q_x  = EE_I02/det_I;
		 EE_Q_y  = EE_I04/det_I;
		 EE_Q_z  = EE_I05/det_I;
		
		 EE_R_pp = -(Ixz*EE_I05 - Ixy*EE_I06)/det_I;
		 EE_R_pq =  (Ixz*EE_I03 - Iyz*EE_I05 - (Iyy - Ixx)*EE_I06)/det_I;
		 EE_R_pr = -(Ixy*EE_I03 + (Ixx-Izz)*EE_I05 - Iyz*EE_I06)/det_I;
		 EE_R_qq =  (Iyz*EE_I03 - Ixy*EE_I06)/det_I;
		 EE_R_qr = -((Izz-Iyy)*EE_I03 - Ixy*EE_I05 + Ixz*EE_I06)/det_I;
		 EE_R_rr = -(Iyz*EE_I03 - Ixz*EE_I05)/det_I;
		 EE_R_x  = EE_I03/det_I;
		 EE_R_y  = EE_I05/det_I;
		 EE_R_z  = EE_I06/det_I;
		
	}
}
