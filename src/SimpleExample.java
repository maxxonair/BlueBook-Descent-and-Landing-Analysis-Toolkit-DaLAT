import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import static java.lang.Math.*;

public class SimpleExample {

    public static void main(String[] args) {

        FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0E-8, 10E-5, 1.0E-20, 1.0E-20);
        FirstOrderDifferentialEquations ode = new CometSun();

        double G = 1.48808E-34;
        double sunMass = 1.9891E30;

        double a = 1;
        double e = 0.0;
        double rp = a*(1-e);
        double v0 = sqrt(G*sunMass*(2/rp-1/a));
        double t = 2*PI*sqrt(pow(a,3)/(G*sunMass));

        double[] y = new double[6];
// Position 
        y[0] = -rp;
        y[1] = 0;
        y[2] = 0;
// Velocity
        y[3] = 0;
        y[4] = v0;
        y[5] = 0;

        StepHandler stepHandler = new StepHandler() {

            ArrayList<String> steps = new ArrayList<String>();

            public void init(double t0, double[] y0, double t) {

            }

            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                double   t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();

                if( t > steps.size() )
                    steps.add(t + " " + y[0] + " " + y[1] + " " + y[2]);

                if(isLast) {
                    try{
                        PrintWriter writer = new PrintWriter(new File("results.txt"), "UTF-8");
                        for(String step: steps) {
                            writer.println(step);
                        }
                        writer.close();
                    } catch(Exception e) {};
                }
            }
        };
        dp853.addStepHandler(stepHandler);

        dp853.integrate(ode, 0.0, y, t, y);

        System.out.println("done");
    }

}