package Simulator_main.DataSets;

import Simulation.Model.DataSets.MasterSet;
import utils.EulerAngle;
import utils.Quaternion;

public class RealTimeResultSet extends Object implements Cloneable{
	
	
	private double longitude=0;
	private double latitude=0;
	private double radius=0;

	private double   altitude=0;	
	private double  velocity=0;
	private double  fpa=0;
	private double  azi=0;
	
	private double SCMass =0;
	
	private Quaternion quaternion;
	
	private double[][] PQR = {{0},
			 				  {0},
			 				  {0}};
	
	private double[][] Thrust_NED = {{0},
			 						 {0},
			 						 {0}};
	
	private double[] CartesianPosECEF = {0,0,0};
	
	private double time =0;
	
	
	private MasterSet masterSet; 
	
	private IntegratorData integratorData;
		
	
	private double normalizedDeceleration=0;
	
	private double globalTime=0;
	
	private double groundtrack;
	
	private double primTankfillingLevelPerc;
	private double secTankfillingLevelPerc;
	
	private EulerAngle eulerAngle = new EulerAngle();
	

	public EulerAngle getEulerAngle() {
		return eulerAngle;
	}

	public void setEulerAngle(EulerAngle eulerAngle) {
		this.eulerAngle = eulerAngle;
	}

	public double getPrimTankfillingLevelPerc() {
		return primTankfillingLevelPerc;
	}

	public void setPrimTankfillingLevelPerc(double primTankfillingLevelPerc) {
		this.primTankfillingLevelPerc = primTankfillingLevelPerc;
	}

	public double getSecTankfillingLevelPerc() {
		return secTankfillingLevelPerc;
	}

	public void setSecTankfillingLevelPerc(double secTankfillingLevelPerc) {
		this.secTankfillingLevelPerc = secTankfillingLevelPerc;
	}

	public double getGroundtrack() {
		return groundtrack;
	}

	public void setGroundtrack(double groundtrack) {
		this.groundtrack = groundtrack;
	}

	public double getGlobalTime() {
		return globalTime;
	}

	public void setGlobalTime(double globalTime) {
		this.globalTime = globalTime;
	}

	public IntegratorData getIntegratorData() {
		return integratorData;
	}

	public void setIntegratorData(IntegratorData integratorData) {
		this.integratorData = integratorData;
	}


	public double getNormalizedDeceleration() {
		return normalizedDeceleration;
	}

	public void setNormalizedDeceleration(double normalizedDeceleration) {
		this.normalizedDeceleration = normalizedDeceleration;
	}

	public MasterSet getMasterSet() {
		return masterSet;
	}

	public void setMasterSet(MasterSet masterSet) {
		try {
			this.masterSet = (MasterSet) masterSet.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double[][] getThrust_NED() {
		return Thrust_NED;
	}

	public void setThrust_NED(double[][] thrust_NED) {
		Thrust_NED = thrust_NED;
	}

	public Quaternion getQuaternion() {
		return quaternion;
	}

	public void setQuaternion(Quaternion quaternions) {
		try {
			this.quaternion = (Quaternion) quaternions.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public double getSCMass() {
		return SCMass;
	}

	public void setSCMass(double sCMass) {
		SCMass = sCMass;
	}

	public RealTimeResultSet() {
		
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}


	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getFpa() {
		return fpa;
	}

	public void setFpa(double fpa) {
		this.fpa = fpa;
	}

	public double getAzi() {
		return azi;
	}

	public void setAzi(double azi) {
		this.azi = azi;
	}

	public  double[][] getPQR() {
		return PQR;
	}

	public  void setPQR(double[][] pQR) {
		PQR = pQR;
	}

	public double[] getCartesianPosECEF() {
		return CartesianPosECEF;
	}

	public void setCartesianPosECEF(double[] cartesianPosECEF) {
		CartesianPosECEF = cartesianPosECEF;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
}
