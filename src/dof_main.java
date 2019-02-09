import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import Model.*;


import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import Simulator_main.EquationsOfMotion;; 
public class dof_main {
    public static double PI    = 3.14159265359;                // PI                                       [-]
    public static double kB    = 1.380650424e-23;              // Boltzmann constant                       [SI]                                [-]
    public static double G = 1.48808E-34;
    public static int TARGET; 


	double deg = PI/180.0; 		//Convert degrees to radians
	double rad = 180/PI; 		//Convert radians to degrees
	
	
	public static void Launch_Integrator(){
       // FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0E-8, 10E-5, 1.0E-20, 1.0E-20);
        FirstOrderIntegrator dp853 = new ClassicalRungeKuttaIntegrator(1.0E-3);
       // FirstOrderDifferentialEquations ode = new EoM_Ascent();
        FirstOrderDifferentialEquations ode = new EquationsOfMotion();
        //------------------------------
        TARGET = 1; 
        double t = 30;
		double m0 = 1000;
        double[] y = new double[7];
        //double[] f0 = new double[]{v0, gamma0, x0, h0, vD0, vG0};
// Position 
        y[0] = 0;
        y[1] = 0;
        y[2] = 1737000+10;
// Velocity
        y[3] = 1;
        y[4] = 0;
        y[5] = 0;
        
        y[6] = m0;

        StepHandler stepHandler = new StepHandler() {

            ArrayList<String> steps = new ArrayList<String>();

            public void init(double t0, double[] y0, double t) {

            }

            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                double   t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();

                if( t > steps.size() )
                    steps.add(t + " " + y[0] + " " + y[1] + " " + y[2]+ " " + y[3]+ " " + y[4] + " " + y[5]);
System.out.println(steps.size()+ "   " + y[3] );
                if(isLast) {
                    try{
        	        	//DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        	        	//Date date = new Date();
        	            String time = "" ;//+ dateFormat.format(date) ; 
                        PrintWriter writer = new PrintWriter(new File("results" + time + ".txt"), "UTF-8");
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
	
	
    public static void main(String[] args) {
    	 Launch_Integrator();
    	/*
    	List<atm_dataset> ATM_DATA = new ArrayList<atm_dataset>(); 
    	ATM_DATA =  AtmosphereModel.INITIALIZE_ATM_DATA(0);
    	for (int i =0; i< ATM_DATA.size();i++){
    		System.out.println("" + ATM_DATA.get(i).get_altitude() + " " + ATM_DATA.get(i).get_density());
    	}
    	*/
    }

}