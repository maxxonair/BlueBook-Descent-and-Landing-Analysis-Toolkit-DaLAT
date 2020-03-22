package FlightElement;

public class SpaceShip {

	private int SpaceShipID;
	private double Mass;
	private double[][] InertiaTensorMatrix   = {{      0    ,    0    ,   0},
												{      0    ,    0    ,   0},
												{      0    ,    0    ,   0}};
	
	
	Propulsion propulsion = new Propulsion();
	AeroElements aeroElements = new AeroElements();
	Sensors sensors = new Sensors();
	OBC oBC = new OBC();
	
	double CoM  = 0;		// Center of Mass (on the center line with respect to the set zero)
	double CoT  = 0;		// Center of thrust forces (on the center line with respect to the set zero)
	double CoP  = 0;    // Center of (aerodynamic) Pressure; 
	
	double vehicleLength = 0;  // Vehicle length [m]
	

	public double getCoP() {
		return CoP;
	}


	public void setCoP(double coP) {
		CoP = coP;
	}


	public SpaceShip() {
		super();
	}


	public int getSpaceShipID() {
		return SpaceShipID;
	}


	public void setSpaceShipID(int spaceShipID) {
		SpaceShipID = spaceShipID;
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


	public Propulsion getPropulsion() {
		return propulsion;
	}


	public void setPropulsion(Propulsion propulsion) {
		this.propulsion = propulsion;
	}


	public AeroElements getAeroElements() {
		return aeroElements;
	}


	public void setAeroElements(AeroElements aeroElements) {
		this.aeroElements = aeroElements;
	}


	public double getCoM() {
		return CoM;
	}


	public void setCoM(double coM) {
		CoM = coM;
	}


	public double getCoT() {
		return CoT;
	}


	public void setCoT(double coPr) {
		CoT = coPr;
	}


	public double getVehicleLength() {
		return vehicleLength;
	}


	public void setVehicleLength(double vehicleLength) {
		this.vehicleLength = vehicleLength;
	}


	public Sensors getSensors() {
		return sensors;
	}


	public OBC getoBC() {
		return oBC;
	}
	
	
}
