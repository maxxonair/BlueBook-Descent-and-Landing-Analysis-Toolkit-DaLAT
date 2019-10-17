package FlightElement;

public class SpaceShip {

	private int SpaceShipID;
	private double Mass;
	private double[][] InertiaTensorMatrix   = {{      0    ,    0    ,   0},
												{      0    ,    0    ,   0},
												{      0    ,    0    ,   0}};
	
	
	Propulsion propulsion = new Propulsion();
	AeroElements aeroElements = new AeroElements();
	
	


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
	
	
}
