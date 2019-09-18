package Model;

public class Gravity {

	private static double rm;
	private static int TARGET;
	private static double mu;
	
	public static double[][] getGravity3D(double[] x ,double[] r_ECEF_cartesian) {
		//------------------------------------------------------------------------------
		//     Full 3D gravity model (work TBD on complexity) 
		//------------------------------------------------------------------------------
		double[][] g;
		g = GravityModel.get_g_ECEF(x[2], r_ECEF_cartesian, rm, mu, TARGET);
		return g; 
	}
	
	public static double[] getGravity2D(double[] x) {
		//------------------------------------------------------------------------------
		//     simplified 2D atmosphere model (J2 only) 
		//------------------------------------------------------------------------------
		double[] g = {0.0,0.0};
		double gr, gn;
	    	gr = GravityModel.get_gr( x[2],  x[1],  rm,  mu, TARGET);
	    	gn = GravityModel.get_gn( x[2],  x[1],  rm,  mu, TARGET); 
	    g[0]=gr;
	    g[1]=gn;
	    return g; 
	}

	public static double getRm() {
		return rm;
	}

	public static void setRm(double rm) {
		Gravity.rm = rm;
	}

	public static int getTARGET() {
		return TARGET;
	}

	public static void setTARGET(int tARGET) {
		TARGET = tARGET;
	}

	public static double getMu() {
		return mu;
	}

	public static void setMu(double mu) {
		Gravity.mu = mu;
	}
	
	
}
