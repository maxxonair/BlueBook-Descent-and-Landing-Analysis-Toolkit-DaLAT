package GUI.PropulsionDraw.ComponentMetaFileTypes;


import java.util.List;

import GUI.PropulsionDraw.ReadWrite;

public class TankMetaFile extends ComponentMetaFile {
	
	private double volume;
	
	private String propellantType;
	
	private double pressureIs;
	private double operatingPressure;
	private double MEOP;
	private double meanTemperature;
	
	private double propellantMassCapacity;
	private double fillingLevel;

	public TankMetaFile(int elementType, ReadWrite readWrite) {
		super(elementType, readWrite);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Propellant [-]", ""+propellantType);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Volume [litre]", ""+volume);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Current Pressure [Pa]", ""+pressureIs);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Operating Pressure [Pa]", ""+operatingPressure);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "MEOP [Pa]", ""+MEOP);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Mean Temp. [K]", ""+meanTemperature);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Propellant Capacity [kg]", ""+propellantMassCapacity);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Filling Level [kg]", ""+fillingLevel);
	}

	public double getVolume() {
		return volume;
	}

	public void readMetaList() {
		List<MetaDataLine> list = super.getElementMetaList();
		for(MetaDataLine line : list) {
			if(line.name.equals("System mass [kg]")) {
				super.systemMass = Double.parseDouble(line.value);
			} else if(line.name.equals("Propellant [-]")) {
				propellantType = line.value;
			} else if (line.name.equals("Volume [litre]")) {
				volume = Double.parseDouble(line.value);
			} else if (line.name.equals("Current Pressure [Pa]")) {
				pressureIs = Double.parseDouble(line.value);
			} else if (line.name.equals("Operating Pressure [Pa]")) {
				operatingPressure = Double.parseDouble(line.value);
			} else if (line.name.equals("MEOP [Pa]")) {
				MEOP = Double.parseDouble(line.value);
			} else if (line.name.equals("Mean Temp. [K]")) {
				meanTemperature = Double.parseDouble(line.value);
			} else if (line.name.equals("Propellant Capacity [kg]")) {
				propellantMassCapacity = Double.parseDouble(line.value);
			} else if (line.name.equals("Filling Level [kg]")) {
				fillingLevel = Double.parseDouble(line.value);
			} 
		}
	}
	
	public void setVolume(double volume) {
		this.volume = volume;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Volume [litre]", ""+volume);
	}

	public String getPropellantType() {
		return propellantType;
	}

	public void setPropellantType(String propellantType) {
		this.propellantType = propellantType;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Propellant [-]", ""+propellantType);
	}

	public double getPressureIs() {
		return pressureIs;
	}

	public void setPressureIs(double pressureIs) {
		this.pressureIs = pressureIs;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Current Pressure [Pa]", ""+pressureIs);
	}

	public double getOperatingPressure() {
		return operatingPressure;
	}

	public void setOperatingPressure(double operatingPressure) {
		this.operatingPressure = operatingPressure;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Operating Pressure [Pa]", ""+operatingPressure);
	}

	public double getMEOP() {
		return MEOP;
	}

	public void setMEOP(double mEOP) {
		MEOP = mEOP;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "MEOP [Pa]", ""+MEOP);
	}

	public double getMeanTemperature() {
		return meanTemperature;
	}

	public void setMeanTemperature(double meanTemperature) {
		this.meanTemperature = meanTemperature;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Mean Temp. [K]", ""+meanTemperature);
	}

	public double getPropellantMassCapacity() {
		return propellantMassCapacity;
	}

	public void setPropellantMassCapacity(double propellantMassCapacity) {
		this.propellantMassCapacity = propellantMassCapacity;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Propellant Capacity [kg]", ""+propellantMassCapacity);
	}

	public double getFillingLevel() {
		return fillingLevel;
	}

	public void setFillingLevel(double fillingLevel) {
		this.fillingLevel = fillingLevel;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Filling Level [kg]", ""+fillingLevel);
	}
	


}
