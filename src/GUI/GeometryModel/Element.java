package GUI.GeometryModel;

public class Element {
	
	private String name;
	
	private double diameter1;
	private double diameter2;
	private double length;
	
	private double elementClass=0;
	
	public Element() {
		
	}

	public double getDiameter1() {
		return diameter1;
	}

	public void setDiameter1(double diameter) {
		this.diameter1 = diameter;
	}
	
	

	public double getElementClass() {
		return elementClass;
	}

	public void setElementClass(double elementClass) {
		this.elementClass = elementClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDiameter2() {
		return diameter2;
	}

	public void setDiameter2(double diameter2) {
		this.diameter2 = diameter2;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}
	

}
