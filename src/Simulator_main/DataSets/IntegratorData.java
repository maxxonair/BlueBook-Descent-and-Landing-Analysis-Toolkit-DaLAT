package Simulator_main.DataSets;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerIntegrator;

import Simulator_main.StopCondition;

public class IntegratorData {
	
	private int targetBody;
	
	private double initLatitude;
	private double initLongitude;
	private double initRadius;
	
	private double initVelocity;
	private double initFpa;
	private double initAzimuth;
	
	private double initRotationalRateX;
	private double initRotationalRateY;
	private double initRotationalRateZ;
	
	
	private double maxIntegTime;
	private double integTimeStep;
	
	private double maxGlobalTime;
	
	private double globalTime=0;
	
	private double refElevation;
	
	List<StopCondition> IntegStopHandler = new ArrayList<StopCondition>();
	
	private int velocityVectorCoordSystem;
	private int degreeOfFreedom;
	
	double[] IntegInput;
	
	double ControllerFrequency=1;
	
	int IntegratorType;
	
	int AeroDragModel;
	int AeroParachuteModel;
	double ConstParachuteCd;
	
	private double groundtrack=0;
	
	private boolean isAtmosphereNoiseModel;
	private boolean isActuatorNoiseModel; 
	
	
	
	public boolean isActuatorNoiseModel() {
		return isActuatorNoiseModel;
	}

	public void setActuatorNoiseModel(boolean isActuatorNoiseModel) {
		this.isActuatorNoiseModel = isActuatorNoiseModel;
	}

	public boolean isAtmosphereNoiseModel() {
		return isAtmosphereNoiseModel;
	}

	public void setAtmosphereNoiseModel(boolean isAtmosphereNoiseModel) {
		this.isAtmosphereNoiseModel = isAtmosphereNoiseModel;
	}

	public double getGroundtrack() {
		return groundtrack;
	}

	public void setGroundtrack(double groundtrack) {
		this.groundtrack = groundtrack;
	}
	
	private double[][] initialQuarterions = {	{1},
			{0},
			{0},
			{0}}; 

	private double[][] angularRate = {	{1},
	{0},
	{0}}; 
	
	public double[][] getAngularRate() {
		return angularRate;
	}


	public double getGlobalTime() {
		return globalTime;
	}


	public void setGlobalTime(double globalTime) {
		this.globalTime = globalTime;
	}


	public void setAngularRate(double[][] angularRate) {
		this.angularRate = angularRate;
	}
	
	public double[][] getInitialQuarterions() {
		return initialQuarterions;
	}


	public void setInitialQuarterions(double[][] initialQuarterions) {
		this.initialQuarterions = initialQuarterions;
	}

	public int getAeroParachuteModel() {
		return AeroParachuteModel;
	}


	public void setAeroParachuteModel(int aeroParachuteModel) {
		AeroParachuteModel = aeroParachuteModel;
	}


	public double getConstParachuteCd() {
		return ConstParachuteCd;
	}


	public double getMaxGlobalTime() {
		return maxGlobalTime;
	}

	public void setMaxGlobalTime(double maxGlobalTime) {
		this.maxGlobalTime = maxGlobalTime;
	}

	public void setConstParachuteCd(double constParachuteCd) {
		ConstParachuteCd = constParachuteCd;
	}


	FirstOrderIntegrator Integrator;
	
	public double[] getIntegInput() {
		return IntegInput;
	}


	public double getInitRotationalRateX() {
		return initRotationalRateX;
	}


	public void setInitRotationalRateX(double initRotationalRateX) {
		this.initRotationalRateX = initRotationalRateX;
	}


	public double getInitRotationalRateY() {
		return initRotationalRateY;
	}


	public void setInitRotationalRateY(double initRotationalRateY) {
		this.initRotationalRateY = initRotationalRateY;
	}


	public double getInitRotationalRateZ() {
		return initRotationalRateZ;
	}


	public void setInitRotationalRateZ(double initRotationalRateZ) {
		this.initRotationalRateZ = initRotationalRateZ;
	}


	public void setIntegInput(double[] integInput) {
		IntegInput = integInput;
	}


	public int getIntegratorType() {
		return IntegratorType;
	}


	public int getAeroDragModel() {
		return AeroDragModel;
	}


	public void setAeroDragModel(int aeroDragModel) {
		AeroDragModel = aeroDragModel;
	}


	public void setIntegratorType(int integratorType) {
		IntegratorType = integratorType;
		if (IntegratorType == 1) {
			Integrator = new ClassicalRungeKuttaIntegrator(IntegInput[0]);
		} else if (IntegratorType == 0) {
			Integrator = new DormandPrince853Integrator(IntegInput[0], IntegInput[1], IntegInput[2], IntegInput[3]);
		} else if (IntegratorType ==2){
			Integrator = new GraggBulirschStoerIntegrator(IntegInput[0], IntegInput[1], IntegInput[2], IntegInput[3]);
		} else if (IntegratorType == 3){
			Integrator = new AdamsBashforthIntegrator((int) IntegInput[0], IntegInput[1], IntegInput[2], IntegInput[3], IntegInput[4]);
		} else {
			// Default Value
			System.out.println("Integrator index out of range");
			System.out.println("Fallback to standard setting: DormandPrince853Integrator(1.0e-8, 1.0, 1.0e-10, 1.0e-10)");
			Integrator = new DormandPrince853Integrator(1.0e-8, 1.0, 1.0e-10, 1.0e-10);
		}
	}


	public int getTargetBody() {
		return targetBody;
	}


	public void setTargetBody(int targetBody) {
		this.targetBody = targetBody;
	}


	public FirstOrderIntegrator getIntegrator() {
		return Integrator;
	}

	
	public IntegratorData() {
		super();
	}


	public double getInitLatitude() {
		return initLatitude;
	}


	public void setInitLatitude(double initLatitude) {
		this.initLatitude = initLatitude;
	}


	public double getInitLongitude() {
		return initLongitude;
	}


	public void setInitLongitude(double initLongitude) {
		this.initLongitude = initLongitude;
	}


	public double getInitRadius() {
		return initRadius;
	}


	public void setInitRadius(double initRadius) {
		this.initRadius = initRadius;
	}


	public double getInitVelocity() {
		return initVelocity;
	}


	public void setInitVelocity(double initVelocity) {
		this.initVelocity = initVelocity;
	}


	public double getInitFpa() {
		return initFpa;
	}


	public void setInitFpa(double initFpa) {
		this.initFpa = initFpa;
	}


	public double getInitAzimuth() {
		return initAzimuth;
	}


	public void setInitAzimuth(double initAzimuth) {
		this.initAzimuth = initAzimuth;
	}


	public double getMaxIntegTime() {
		return maxIntegTime;
	}


	public void setMaxIntegTime(double maxIntegTime) {
		this.maxIntegTime = maxIntegTime;
	}


	public double getIntegTimeStep() {
		return integTimeStep;
	}


	public void setIntegTimeStep(double integTimeStep) {
		this.integTimeStep = integTimeStep;
	}


	public double getRefElevation() {
		return refElevation;
	}


	public void setRefElevation(double refElevation) {
		this.refElevation = refElevation;
	}


	public List<StopCondition> getIntegStopHandler() {
		return IntegStopHandler;
	}


	public void setIntegStopHandler(List<StopCondition> integStopHandler) {
		IntegStopHandler = integStopHandler;
	}


	public int getVelocityVectorCoordSystem() {
		return velocityVectorCoordSystem;
	}


	public void setVelocityVectorCoordSystem(int velocityVectorCoordSystem) {
		this.velocityVectorCoordSystem = velocityVectorCoordSystem;
	}


	public int getDegreeOfFreedom() {
		return degreeOfFreedom;
	}


	public void setDegreeOfFreedom(int degreeOfFreedom) {
		this.degreeOfFreedom = degreeOfFreedom;
	}

	public double getControllerFrequency() {
		return ControllerFrequency;
	}

	public void setControllerFrequency(double controllerFrequency) {
		ControllerFrequency = controllerFrequency;
	}
	
}
