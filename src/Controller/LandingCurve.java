

package Controller; 

public class LandingCurve {
	
	public static double squared(double input) {
		 double output=input*input;	return output; 
	}
	
public static double ParabolicLandingCurve(double v_init, double h_init, double v_touchdown, double h_is) {
	double TargetVelocity=0;
	double a=0;
	double b=0;
	b = h_init / ( 1- squared((v_init/v_touchdown)));
	a =   - 1 / squared(v_touchdown) * b; 
	TargetVelocity = Math.sqrt((h_is - b)/a);
	return TargetVelocity; 
}
	
	
}