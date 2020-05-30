package FlightElement.Properties;

public class MassAndInertia {

	private double Mass=1;
	private double[][] InertiaTensorMatrix   = {{      1    ,    0    ,   0},
												{      0    ,    1    ,   0},
												{      0    ,    0    ,   1}};
	
	double CoM  = 0;		// Center of Mass (on the center line with respect to the set zero)
	
	public MassAndInertia() {
		
	}

	public double getMass() {
		return Mass;
	}

	public void setMass(double mass) {
		Mass = mass;
	}

	public double[][] getInertiaTensorMatrix() {
		return InertiaTensorMatrix;
	}

	public void setInertiaTensorMatrix(double[][] inertiaTensorMatrix) {
		InertiaTensorMatrix = inertiaTensorMatrix;
	}

	public double getCoM() {
		return CoM;
	}

	public void setCoM(double coM) {
		CoM = coM;
	}
	
	
}
