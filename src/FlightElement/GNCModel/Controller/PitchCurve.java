

package FlightElement.GNCModel.Controller; 

public class PitchCurve {
	
	public static double squared(double input) {
		 double output=input*input;	return output; 
	}
	
public static double SquareRootPitchCurve(double ctr_init_x, double ctr_init_y, double ctr_end_x, double ctr_end_y, double x_is) {
	double TargetFPA=0;
	// Linear curve : y(x) = a*sqrt(x) + b
	double b = ctr_init_y;
	if(ctr_end_x !=  0) {
	double a = (ctr_end_y - ctr_init_y)/Math.sqrt(ctr_end_x);
    TargetFPA = a * Math.sqrt(x_is) + b; }
	return TargetFPA; 
}
public static double LinearPitchCurve(double ctr_init_x, double ctr_init_y, double ctr_end_x, double ctr_end_y, double x_is) {
	double TargetFPA=0;
	// Linear curve : y(x) = a*(x) + b
	double b = ctr_init_y;
	if(ctr_end_x !=  0) {
	double a = (ctr_end_y - ctr_init_y)/ctr_end_x;
    TargetFPA = a * x_is + b; }
	return TargetFPA; 
}
}