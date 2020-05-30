

package FlightElement.GNCModel.Controller; 

public class LandingCurve {
	
	public static double squared(double input) {
		 double output=input*input;	return output; 
	}
	
public static double ParabolicLandingCurve(double v_init, double h_init, double ctrl_vel, double ctrl_alt, double h_is) {
	double TargetVelocity=0;
	// Parabolic curve : h(v) = a*squared(v) 
	// Star values refer to a shifted coordinate system (shift by ctrl_alt in altitude/ ctrl_vel in velocity direction)
	double h_init_star = h_init - ctrl_alt; 
	double v_init_star = v_init - ctrl_vel;
	double a =   h_init_star/squared(v_init_star); 
	double h_is_star = h_is - ctrl_alt;
	double v_target_star = Math.sqrt(h_is_star/a);
	TargetVelocity = v_target_star + ctrl_vel;
	return TargetVelocity; 
}
public static double SquarerootLandingCurve(double v_init, double h_init, double ctrl_vel, double ctrl_alt, double h_is) {
	double TargetVelocity=0;
	// Parabolic curve : h(v) = a*squareroot(v) 
	// Star values refer to a shifted coordinate system (shift by ctrl_alt in altitude/ ctrl_vel in velocity direction)
	double v_init_star = v_init - ctrl_vel; 
	double h_init_star = h_init - ctrl_alt; 
	double a =   h_init_star/Math.sqrt(v_init_star); 
	double h_is_star = h_is - ctrl_alt;
	double v_target_star = squared(h_is_star/a);
	TargetVelocity = v_target_star + ctrl_vel;
	return TargetVelocity; 
}
public static double LinearLandingCurve(double v_init, double h_init, double ctrl_vel, double ctrl_alt, double h_is) {
	double TargetVelocity=0;
	// Linear curve : h(v) = a*(v) 
	// Star values refer to a shifted coordinate system (shift by ctrl_alt in altitude/ ctrl_vel in velocity direction)
	double h_init_star = h_init - ctrl_alt; 
	double v_init_star = v_init - ctrl_vel;
	double a =   h_init_star/(v_init_star); 
	double h_is_star = h_is - ctrl_alt;
	double v_target_star = Math.sqrt(h_is_star/a);
	TargetVelocity = v_target_star + ctrl_vel;
	return TargetVelocity; 
}
public static double CircularLandingCurve() {
	double TargetVelocity=0;
	// TBD  
	return TargetVelocity;
}

	
	
}