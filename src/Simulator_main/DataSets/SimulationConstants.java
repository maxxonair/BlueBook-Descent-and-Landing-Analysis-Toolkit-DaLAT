package Simulator_main.DataSets;

public class SimulationConstants {

	private double[][] DATA_MAIN = { // RM (average radius) [m] || Gravitational Parameter [m3/s2] || Rotational speed [rad/s] || Average Collision Diameter [m]
			{6371000,3.9860044189E14,7.2921150539E-5,1.6311e-9}, 	// Earth
			{1737400,4903E9,2.661861E-6,320},						// Moon (Earth)
			{3389500,4.2838372E13,7.0882711437E-5,1.6311e-9},		// Mars
			{0,0,0,0},												// Venus
	 };
	
	 private double Lt    = 0;    		// Average collision diameter (CO2)         [m]
	 private double mu    = 0;    	    // Standard gravitational constant (Mars)   [m3/s2]
	 private double rm    = 0;    	    // Planets average radius                   [m]
	 private double omega = 0 ;         // Planets rotational rate                  [rad/sec]
	
	public SimulationConstants(){

	}
	
	public void initConstants(int targetIndex) {
	    Lt    = DATA_MAIN[targetIndex][3];    	// Average collision diameter (CO2)         [m]
	    mu    = DATA_MAIN[targetIndex][1];    	// Standard gravitational constant          []
	    rm    = DATA_MAIN[targetIndex][0];    	// Planets mean radius                      [m]
	    omega = DATA_MAIN[targetIndex][2];		// Planets rotational speed     		    [rad/s]
	}

	public double getLt() {
		return Lt;
	}

	public double getMu() {
		return mu;
	}

	public double getRm() {
		return rm;
	}

	public double getOmega() {
		return omega;
	}
	
		
}
