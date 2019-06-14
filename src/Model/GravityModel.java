package Model;

import static java.lang.Math.*;


public class GravityModel  {

	
	   public static double[][] DATA_GRAVITY = { // RM (average radius) [m] || Gravitational Parameter [m3/s2] || Rotational speed [rad/s] || Average Collision Diameter [m]
				{1.9554537E-3,0,0}, 						// Earth
				{9.0880835E-5,0,0},				// Moon (Earth) GRAIL data
				{0,0,0},									// Mars
				{0,0,0,0},											// Venus
		};
	
	
	static double       J2    = 0;             			  // First Jeofreys? Constant                 [??]
    static double       J3    = 0;                        // Second ...
    static double       J4    = 0;                        // Third ...
    
	public static void SET_Constants(int TARGET){
	    J2   = DATA_GRAVITY[TARGET][0];    	// Average collision diameter (CO2)         [m]
	    J3   = DATA_GRAVITY[TARGET][1];    	// Standard gravitational constant (Mars)   [m3/s2]
	    J4   = DATA_GRAVITY[TARGET][2];    	// Planets average radius                   [m]
}
    
    public static double get_gr(double r, double lat, double rm, double mu, int TARGET)                          // Gravity acceleration in radial direction [m/s�]
    {
    	SET_Constants(TARGET);
    	
    double phi = PI/2-lat;             // Phi                                      [rad]
    double gr;                    // Gravitational acceleration in radial -> _r and north-south -> _n direction
    gr = mu * ( 1 - 1.5 * J2 * ( 3 * cos(phi) * cos(phi) - 1) * (rm/r) * (rm/r) - 2 * J3 * cos(phi) * (5 * cos(phi) * cos(phi) - 3) * (rm/r) * (rm/r) * (rm/r) - (5/8) * J4 * (35 * cos(phi) * cos(phi) * cos(phi) * cos(phi) - 30 * cos(phi) * cos(phi) + 3) * (rm/r) * (rm/r) * (rm/r) * (rm/r) )/(r * r);
    //System.out.println(J2 + "|" + J3 + "|" + J4 + "|" + mu + "|" + rm+ "|" + gr);
    return gr;
    }
    public static double get_gn(double r, double lat, double rm,double mu, int TARGET)        // Gravity in north-south direction [m/s�]
    {
    	SET_Constants(TARGET);
    double phi = PI/2-lat;             // Phi                                      [rad]
    double gn;                    // Gravitational acceleration in radial -> _r and north-south -> _n direction
    gn = -3 * mu * sin(phi) * cos(phi) * (rm/r) * (rm/r) * (J2 + 0.5 * J3 * ( 5*cos(phi) *cos(phi) -1) * (rm/r)/cos(phi) + (5/6) * J4 * ( 7 * cos(phi) * cos(phi) - 1) * (rm/r) * (rm/r) ) /(r * r);
    return gn;
    }
    public static double get_gw(double r, double lat, double rm,double mu, int TARGET)
    {
    	SET_Constants(TARGET);
       double gw = 0;
        return gw;
    }
    
    public static double[][] get_g(double r, double longitude, double latitude, double rm, double mu, int TARGET) {
    	double[][] g = new double[3][1] ; 
    	SET_Constants(TARGET);
    	double g_phi = 0;
    	double g_r = 0 ;
    	 double phi = PI/2-latitude; 
    	g_r = mu * ( 1 - 1.5 * J2 * ( 3 * cos(phi) * cos(phi) - 1) * (rm/r) * (rm/r) - 2 * J3 * cos(phi) * (5 * cos(phi) * cos(phi) - 3) * (rm/r) * (rm/r) * (rm/r) - (5/8) * J4 * (35 * cos(phi) * cos(phi) * cos(phi) * cos(phi) - 30 * cos(phi) * cos(phi) + 3) * (rm/r) * (rm/r) * (rm/r) * (rm/r) )/(r * r);
    	g_phi = -3 * mu * sin(phi) * cos(phi) * (rm/r) * (rm/r) * (J2 + 0.5 * J3 * ( 5*cos(phi) *cos(phi) -1) * (rm/r)/cos(phi) + (5/6) * J4 * ( 7 * cos(phi) * cos(phi) - 1) * (rm/r) * (rm/r) ) /(r * r);
    	
    	
    	g[2][0] =  g_phi*Math.cos(phi)*Math.cos(longitude) + g_r*Math.sin(phi)*Math.cos(longitude);
    	g[1][0] =  g_phi*Math.cos(phi)*Math.sin(longitude) + g_r*Math.sin(phi)*Math.sin(longitude);
    	g[0][0] =  g_phi*Math.sin(phi) + g_r* Math.cos(phi);
    	
    	return g; 
    }
    public static double[][] get_g_ECEF(double r ,double[] r_cartesian , double rm, double mu, int TARGET) {
    	double[][] GRAVITY_VECTOR = new double[3][1]; 
    	SET_Constants(TARGET);
    	//double longitude = r_spherical[0];
    	//double latitude  = r_spherical[1];

    	double x = r_cartesian[0];
    	double y = r_cartesian[1];
    	double z = r_cartesian[2];
    	
    	// double phi = PI/2-latitude; 
    	// double g_r = mu * ( 1 - 1.5 * J2 * ( 3 * cos(phi) * cos(phi) - 1) * (rm/r) * (rm/r) - 2 * J3 * cos(phi) * (5 * cos(phi) * cos(phi) - 3) * (rm/r) * (rm/r) * (rm/r) - (5/8) * J4 * (35 * cos(phi) * cos(phi) * cos(phi) * cos(phi) - 30 * cos(phi) * cos(phi) + 3) * (rm/r) * (rm/r) * (rm/r) * (rm/r) )/(r * r);
    	// double g_phi = -3 * mu * sin(phi) * cos(phi) * (rm/r) * (rm/r) * (J2 + 0.5 * J3 * ( 5*cos(phi) *cos(phi) -1) * (rm/r)/cos(phi) + (5/6) * J4 * ( 7 * cos(phi) * cos(phi) - 1) * (rm/r) * (rm/r) ) /(r * r);
    	
    	 double Q = 1 + 3*J2/2*(rm/r)*(rm/r) * (1-5*(z*z)/(r*r)) + 5*J3/2*(rm/r)*(rm/r)*(rm/r)*(3-7*z*z/(r*r))*z/r - 35*J4/8*(rm/r)*(rm/r)*(rm/r)*(rm/r)*(9*z*z*z*z/(r*r*r*r)-6*z*z/(r*r)+3/7);
    	     	 
    	 GRAVITY_VECTOR[0][0] = -mu*x/(r*r*r)*Q;
    	 GRAVITY_VECTOR[1][0] = -mu*y/(r*r*r)*Q;
    	 GRAVITY_VECTOR[2][0] = -mu/(r*r)*(((1 + 3*J2/2*(rm/r)*(rm/r)*(3-5*(z/r)*(z/r)))*z/r) + (5*J3/2*(rm/r)*(rm/r)*(rm/r)*(6*z*z/(r*r)-7*(z/r)*(z/r)*(z/r)*(z/r)-3/5)) + (-35*J4/8*(rm/r)*(rm/r)*(rm/r)*(rm/r)*(15/7-10*(z/r)*(z/r)+9*(z/r)*(z/r)*(z/r)*(z/r))*(z/r)));
    	 
    	return GRAVITY_VECTOR;
    }
    
}