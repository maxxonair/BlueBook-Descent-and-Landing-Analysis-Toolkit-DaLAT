import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

import static java.lang.Math.*;

public class CometSun implements FirstOrderDifferentialEquations {

    double G = 1.48808E-34;
    double sunMass = 1.9891E30;

    public int getDimension() {
        return 6;
    }

    public void computeDerivatives(double t, double[] y, double[] yp) {
    	double yy0 = y[0]*y[0];
    	double yy1 = y[1]*y[1];
    	double yy2 = y[2]*y[2];
    	double coeff = G*sunMass/pow(yy0+yy1+yy2,3.0/2.0);

        yp[0] = y[3];
        yp[1] = y[4];
        yp[2] = y[5];

        yp[3] = -coeff*y[0];
        yp[4] = -coeff*y[1];
        yp[5] = -coeff*y[2];
    }

}