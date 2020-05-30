package FlightElement.Properties;

public class Properties {
	
	
	Geometry geometry = new Geometry();
	MassAndInertia massAndInertia = new MassAndInertia();
	Propulsion propulsion = new Propulsion();
	AeroElements aeroElements = new AeroElements();
	Sensors sensors = new Sensors();
	OBC oBC = new OBC();
	Sequence sequence = new Sequence();
	
	public Properties() {
		
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public MassAndInertia getMassAndInertia() {
		return massAndInertia;
	}

	public Propulsion getPropulsion() {
		return propulsion;
	}

	public AeroElements getAeroElements() {
		return aeroElements;
	}

	public Sensors getSensors() {
		return sensors;
	}

	public OBC getoBC() {
		return oBC;
	}

	public Sequence getSequence() {
		return sequence;
	}
	

}
