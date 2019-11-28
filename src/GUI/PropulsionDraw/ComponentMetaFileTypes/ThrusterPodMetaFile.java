package GUI.PropulsionDraw.ComponentMetaFileTypes;

import GUI.PropulsionDraw.ReadWrite;

public class ThrusterPodMetaFile extends ComponentMetaFile {

	
	private double thrust;
	private double ISP;
	private double O2F;
	
	private double momentum; 
	
	private double pulseTimeMax;
	private double pulseTimeMin;
	
	public ThrusterPodMetaFile(int elementType, ReadWrite readWrite) {
		super(elementType, readWrite);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Thrust [N]", ""+thrust);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "ISP [s]", ""+ISP);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "O/F [-]", ""+O2F);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Momentum [Nm]", ""+momentum);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Max. Pulse Time [ms]", ""+pulseTimeMax);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Min. Pulse Time [ms]", ""+pulseTimeMin);
	}

	public double getThrust() {
		return thrust;
	}

	public void setThrust(double thrust) {
		this.thrust = thrust;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Thrust [N]", ""+thrust);
	}

	public double getISP() {
		return ISP;
	}

	public void setISP(double iSP) {
		ISP = iSP;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "ISP [s]", ""+ISP);
	}

	public double getO2F() {
		return O2F;
	}

	public void setO2F(double o2f) {
		O2F = o2f;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "O/F [-]", ""+O2F);
	}

	public double getMomentum() {
		return momentum;
	}

	public void setMomentum(double momentum) {
		this.momentum = momentum;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Momentum [Nm]", ""+momentum);
	}

	public double getPulseTimeMax() {
		return pulseTimeMax;
	}

	public void setPulseTimeMax(double pulseTimeMax) {
		this.pulseTimeMax = pulseTimeMax;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Max. Pulse Time [ms]", ""+pulseTimeMax);
	}

	public double getPulseTimeMin() {
		return pulseTimeMin;
	}

	public void setPulseTimeMin(double pulseTimeMin) {
		this.pulseTimeMin = pulseTimeMin;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Min. Pulse Time [ms]", ""+pulseTimeMin);
	}
	
	

}
