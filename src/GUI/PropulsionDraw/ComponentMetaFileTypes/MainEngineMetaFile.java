package GUI.PropulsionDraw.ComponentMetaFileTypes;




public class MainEngineMetaFile extends ComponentMetaFile {
	
	private double thrust;
	private double ISP;
	private double O2F;
	

	public MainEngineMetaFile(int ID) {
		super(ID);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Thrust [N]", ""+thrust);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "ISP [s]", ""+ISP);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "O/F [-]", ""+O2F);
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
	

}
