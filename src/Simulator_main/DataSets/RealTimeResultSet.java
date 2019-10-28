package Simulator_main.DataSets;

import FlightElement.SpaceShip;
import Model.DataSets.MasterSet;

public class RealTimeResultSet {
	
	
	private double longitude=0;
	private double latitude=0;
	private double radius=0;

	private double   altitude=0;	
	private double  velocity=0;
	private double  fpa=0;
	private double  azi=0;
	
	private double SCMass =0;
	
	private double eulerX=0;
	private double eulerY=0;
	private double eulerZ=0;
	
	private double[][] quarternions = {	{0},
										{0},
										{0},
										{0}}; 
	
	private double[][] PQR = {{0},
			 				  {0},
			 				  {0}};
	
	private double[][] Thrust_NED = {{0},
			 						 {0},
			 						 {0}};
	
	private double[][] CartesianPosECEF = {{0},
			 							   {0},
			 							   {0}};
	
	private double time =0;
	
	
	private MasterSet masterSet; 
	
	private SpaceShip spaceShip; 
	
	private IntegratorData integratorData;
	
	private double normalizedDeceleration=0;
	
	

	public IntegratorData getIntegratorData() {
		return integratorData;
	}

	public void setIntegratorData(IntegratorData integratorData) {
		this.integratorData = integratorData;
	}

	public SpaceShip getSpaceShip() {
		return spaceShip;
	}

	public void setSpaceShip(SpaceShip spaceShip) {
		this.spaceShip = spaceShip;
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
		this.masterSet = masterSet;
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

	public double[][] getQuarternions() {
		return quarternions;
	}

	public void setQuarternions(double[][] quarternions) {
		this.quarternions = quarternions;
	}

	public double getEulerX() {
		return eulerX;
	}

	public void setEulerX(double eulerX) {
		this.eulerX = eulerX;
	}

	public double getEulerY() {
		return eulerY;
	}

	public void setEulerY(double eulerY) {
		this.eulerY = eulerY;
	}

	public double getEulerZ() {
		return eulerZ;
	}

	public void setEulerZ(double eulerZ) {
		this.eulerZ = eulerZ;
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

	public double[][] getCartesianPosECEF() {
		return CartesianPosECEF;
	}

	public void setCartesianPosECEF(double[][] cartesianPosECEF) {
		CartesianPosECEF = cartesianPosECEF;
	}

}
