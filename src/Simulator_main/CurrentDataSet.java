package Simulator_main;

import java.util.ArrayList;
import java.util.List;

import Sequence.SequenceElement;

public class CurrentDataSet {
	
	private  double valDt=0;
	private  double RM;
	private  double localElevation=0;
	private  double Lt;
	private  double[] xIS;
	private  double tIS;
	private  int TARGET;
	double[] V_NED_ECEF_spherical;
	public double[] r_ECEF_cartesian ;
	double[] r_ECEF_spherical;
	private  List<SequenceElement> SEQUENCE_DATA_main 					 = new ArrayList<SequenceElement>(); 
	private double mu;
	private CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
	
	private double propellantLevelIsPrimary = 0 ; 
	private double propellantLevelIsSecondary = 0;
	
	public  double[][] EulerAngle      = {{0},{0},{0}};
	
	

	public  double[][] getEulerAngle() {
		return EulerAngle;
	}
	public  void setEulerAngle(double[][] eulerAngle) {
		EulerAngle = eulerAngle;
	}
	public double getPropellantLevelIsPrimary() {
		return propellantLevelIsPrimary;
	}
	public void setPropellantLevelIsPrimary(double propellantLevelIsPrimary) {
		this.propellantLevelIsPrimary = propellantLevelIsPrimary;
	}
	public double getPropellantLevelIsSecondary() {
		return propellantLevelIsSecondary;
	}
	public void setPropellantLevelIsSecondary(double propellantLevelIsSecondary) {
		this.propellantLevelIsSecondary = propellantLevelIsSecondary;
	}
	public CoordinateTransformation getCoordinateTransformation() {
		return coordinateTransformation;
	}
	public void setCoordinateTransformation(CoordinateTransformation coordinateTransformation) {
		this.coordinateTransformation = coordinateTransformation;
	}
	public double getMu() {
		return mu;
	}
	public void setMu(double mu) {
		this.mu = mu;
	}
	public  double[] getR_ECEF_cartesian() {
		return r_ECEF_cartesian;
	}
	public void setR_ECEF_cartesian(double[] r_ECEF_cartesian) {
		this.r_ECEF_cartesian = r_ECEF_cartesian;
	}
	public int getTARGET() {
		return TARGET;
	}
	public void setTARGET(int tARGET) {
		TARGET = tARGET;
	}
	public double[] getV_NED_ECEF_spherical() {
		return V_NED_ECEF_spherical;
	}
	public void setV_NED_ECEF_spherical(double[] v_NED_ECEF_spherical) {
		V_NED_ECEF_spherical = v_NED_ECEF_spherical;
	}
	public double[] getR_ECEF_spherical() {
		return r_ECEF_spherical;
	}
	public void setR_ECEF_spherical(double[] r_ECEF_spherical) {
		this.r_ECEF_spherical = r_ECEF_spherical;
	}
	public  List<SequenceElement> getSEQUENCE_DATA_main() {
		return SEQUENCE_DATA_main;
	}
	public  void setSEQUENCE_DATA_main(List<SequenceElement> sEQUENCE_DATA_main) {
		SEQUENCE_DATA_main = sEQUENCE_DATA_main;
	}
	public  double getLt() {
		return Lt;
	}
	public  void setLt(double lt) {
		Lt = lt;
	}
	public  double[] getxIS() {
		return xIS;
	}
	public  void setxIS(double[] xIS) {
		this.xIS = xIS;
	}
	public  double gettIS() {
		return tIS;
	}
	public  void settIS(double tIS) {
		this.tIS = tIS;
	}
	public  double getValDt() {
		return valDt;
	}
	public  void setValDt(double valDt) {
		this.valDt = valDt;
	}
	public  double getRM() {
		return RM;
	}
	public  void setRM(double rM) {
		RM = rM;
	}
	public  double getLocalElevation() {
		return localElevation;
	}
	public  void setLocalElevation(double localElevation) {
		this.localElevation = localElevation;
	}
	
	

}
