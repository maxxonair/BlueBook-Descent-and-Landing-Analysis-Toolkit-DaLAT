package FlightElement.State;

import Simulator_main.CoordinateTransformation;
import utils.EulerAngle;

public class State {
	
	private  double valDt=0;
	
	private double[] xIS;
	private double tIS;
	private double globalTime=0;
	private double sequenceTime=0;
	private double[] V_NED_ECEF_spherical;
	private double[] r_ECEF_cartesian ;
	private double[] r_ECEF_spherical;
	
	private CoordinateTransformation coordinateTransformation = new CoordinateTransformation();
	private Propulsion propulsion = new Propulsion();
	private EulerAngle eulerAngle      = new EulerAngle();
	
	private  double localElevation=0;
	
	
	public State() {
		
	}


	public double[] getxIS() {
		return xIS;
	}


	public void setxIS(double[] xIS) {
		this.xIS = xIS;
	}


	public double gettIS() {
		return tIS;
	}


	public void settIS(double tIS) {
		this.tIS = tIS;
	}


	public double getGlobalTime() {
		return globalTime;
	}


	public void setGlobalTime(double globalTime) {
		this.globalTime = globalTime;
	}


	public double getSequenceTime() {
		return sequenceTime;
	}


	public void setSequenceTime(double sequenceTime) {
		this.sequenceTime = sequenceTime;
	}


	public double[] getV_NED_ECEF_spherical() {
		return V_NED_ECEF_spherical;
	}


	public void setV_NED_ECEF_spherical(double[] v_NED_ECEF_spherical) {
		V_NED_ECEF_spherical = v_NED_ECEF_spherical;
	}


	public double[] getR_ECEF_cartesian() {
		return r_ECEF_cartesian;
	}


	public void setR_ECEF_cartesian(double[] r_ECEF_cartesian) {
		this.r_ECEF_cartesian = r_ECEF_cartesian;
	}


	public double[] getR_ECEF_spherical() {
		return r_ECEF_spherical;
	}


	public void setR_ECEF_spherical(double[] r_ECEF_spherical) {
		this.r_ECEF_spherical = r_ECEF_spherical;
	}


	public CoordinateTransformation getCoordinateTransformation() {
		return coordinateTransformation;
	}


	public void setCoordinateTransformation(CoordinateTransformation coordinateTransformation) {
		this.coordinateTransformation = coordinateTransformation;
	}


	public Propulsion getPropulsion() {
		return propulsion;
	}


	public void setPropulsion(Propulsion propulsion) {
		this.propulsion = propulsion;
	}


	public EulerAngle getEulerAngle() {
		return eulerAngle;
	}


	public void setEulerAngle(EulerAngle eulerAngle) {
		this.eulerAngle = eulerAngle;
	}


	public double getLocalElevation() {
		return localElevation;
	}


	public void setLocalElevation(double localElevation) {
		this.localElevation = localElevation;
	}


	public double getValDt() {
		return valDt;
	}


	public void setValDt(double valDt) {
		this.valDt = valDt;
	}
	
	
	
}
