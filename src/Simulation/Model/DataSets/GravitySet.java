package Simulation.Model.DataSets;

public class GravitySet extends Object implements Cloneable {
		
	double[][] g_NED = {{0},{0},{0}};  // Gravity vector in NED frame 				[m/s�]
		
	public GravitySet() {
		
	}
	
	public  double[][] getG_NED() {
		return g_NED;
	}
	public  void setG_NED(double[][] g_NED) {
		this.g_NED = g_NED;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
}
