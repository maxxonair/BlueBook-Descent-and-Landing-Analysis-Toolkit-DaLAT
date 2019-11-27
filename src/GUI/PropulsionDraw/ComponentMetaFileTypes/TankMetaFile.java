package GUI.PropulsionDraw.ComponentMetaFileTypes;


public class TankMetaFile extends ComponentMetaFile {
	
	private double volume;

	public TankMetaFile(int ID) {
		super(ID);
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Volume [litre]", ""+volume);
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
		super.elementMetaList = super.updateMetaDataLine(super.elementMetaList, "Volume [litre]", ""+volume);
	}
	


}
