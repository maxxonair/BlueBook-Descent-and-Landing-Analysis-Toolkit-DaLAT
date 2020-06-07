package Simulation.Model.DataSets;

public class HypersonicSet extends Object implements Cloneable {
	
	private double Cx=0;
	private double Cy=0;
	private double Cz=0;
	
	private double CL=0;
	private double CY=0;
	private double CD=0;
	
	private double LD=0;
	
	private double CA=0;
	private double CN=0;
	
	private double CMx=0;
	private double CMy=0;
	private double CMz=0;
	
	
	
	public double getCMx() {
		return CMx;
	}
	public void setCMx(double cMx) {
		CMx = cMx;
	}
	public double getCMy() {
		return CMy;
	}
	public void setCMy(double cMy) {
		CMy = cMy;
	}
	public double getCMz() {
		return CMz;
	}
	public void setCMz(double cMz) {
		CMz = cMz;
	}
	public double getCx() {
		return Cx;
	}
	public void setCx(double cx) {
		Cx = cx;
	}
	public double getCy() {
		return Cy;
	}
	public void setCy(double cy) {
		Cy = cy;
	}
	public double getCz() {
		return Cz;
	}
	public void setCz(double cz) {
		Cz = cz;
	}
	public double getCL() {
		return CL;
	}
	public void setCL(double cL) {
		CL = cL;
	}
	public double getCY() {
		return CY;
	}
	public void setCY(double cY) {
		CY = cY;
	}
	public double getCD() {
		return CD;
	}
	public void setCD(double cD) {
		CD = cD;
	}
	public double getLD() {
		return LD;
	}
	public void setLD(double lD) {
		LD = lD;
	}
	public double getCA() {
		return CA;
	}
	public void setCA(double cA) {
		CA = cA;
	}
	public double getCN() {
		return CN;
	}
	public void setCN(double cN) {
		CN = cN;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}

}
