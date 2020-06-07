package FlightElement.ForceTorqueModel;

import FlightElement.SpaceShip;
import Simulation.NoiseSet.ActuatorNoiseSet;

public class ActuatorSet extends Object implements Cloneable {
	
	private double primaryThrust_is=0;
	private double MomentumRCS_X_is=0;
	private double MomentumRCS_Y_is=0;
	private double MomentumRCS_Z_is=0;
	
	private double primaryISP_is=0;
	private double RCS_X_ISP=0;
	private double RCS_Y_ISP=0;
	private double RCS_Z_ISP=0;
	
	private double primaryPropellant_is=0;
	private double RcsPropellant_is=0;
	
    // TVC control angles: 
    public double TVC_alpha =0;					// TVC angle alpha [rad]
    public double TVC_beta  =0;					// TVC angle beta [rad]
    
    
    private boolean ParachuteDeployed=false;
    private boolean ParachuteEject=false;
    private boolean HeatShieldEject=false;
    
    private ActuatorNoiseSet actuatorNoiseSet = new ActuatorNoiseSet();
    
    private SpaceShip spaceShip;
    
    
    public ActuatorSet(SpaceShip spaceShip) {
    	this.spaceShip = spaceShip;
    }
        
    
	public boolean isHeatShieldEject() {
		return HeatShieldEject;
	}


	public void setHeatShieldEject(boolean heatShieldEject) {
		HeatShieldEject = heatShieldEject;
	}


	public ActuatorNoiseSet getActuatorNoiseSet() {
		return actuatorNoiseSet;
	}


	public void setActuatorNoiseSet(ActuatorNoiseSet actuatorNoiseSet) {
		try {
			this.actuatorNoiseSet = (ActuatorNoiseSet) actuatorNoiseSet.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public boolean isParachuteEject() {
		return ParachuteEject;
	}


	public void setParachuteEject(boolean parachuteEject) {
		ParachuteEject = parachuteEject;
	}


	public boolean isParachuteDeployed() {
		return ParachuteDeployed;
	}


	public void setParachuteDeployed(boolean parachuteDeployed) {
		ParachuteDeployed = parachuteDeployed;
	}


	public double getTVC_alpha() {
		return TVC_alpha;
	}


	public void setTVC_alpha(double tVC_alpha) {
		TVC_alpha = tVC_alpha;
	}


	public double getTVC_beta() {
		return TVC_beta;
	}


	public void setTVC_beta(double tVC_beta) {
		TVC_beta = tVC_beta;
	}




	public double getPrimaryISP_is() {
		return primaryISP_is;
	}


	public void setPrimaryISP_is(double primaryISP_is) {
		this.primaryISP_is = primaryISP_is;
	}


	public double getRCS_X_ISP() {
		return RCS_X_ISP;
	}


	public void setRCS_X_ISP(double rCS_X_ISP) {
		RCS_X_ISP = rCS_X_ISP;
	}


	public double getRCS_Y_ISP() {
		return RCS_Y_ISP;
	}


	public void setRCS_Y_ISP(double rCS_Y_ISP) {
		RCS_Y_ISP = rCS_Y_ISP;
	}


	public double getRCS_Z_ISP() {
		return RCS_Z_ISP;
	}


	public void setRCS_Z_ISP(double rCS_Z_ISP) {
		RCS_Z_ISP = rCS_Z_ISP;
	}


	public double getPrimaryPropellant_is() {
		return primaryPropellant_is;
	}


	public void setPrimaryPropellant_is(double primaryPropellant_is) {
		this.primaryPropellant_is = primaryPropellant_is;
	}


	public double getRcsPropellant_is() {
		return RcsPropellant_is;
	}


	public void setRcsPropellant_is(double rcsPropellant_is) {
		RcsPropellant_is = rcsPropellant_is;
	}


	public ActuatorSet() {
		super();
	}


	public double getPrimaryThrust_is() {
		return primaryThrust_is;
	}


	public void setPrimaryThrust_is(double primaryThrust_is) {
		this.primaryThrust_is = primaryThrust_is;
	}


	public double getMomentumRCS_X_is() {
		return MomentumRCS_X_is;
	}


	public void setMomentumRCS_X_is(double momentumRCS_X_is) {
		MomentumRCS_X_is = momentumRCS_X_is;
	}


	public double getMomentumRCS_Y_is() {
		return MomentumRCS_Y_is;
	}


	public void setMomentumRCS_Y_is(double momentumRCS_Y_is) {
		MomentumRCS_Y_is = momentumRCS_Y_is;
	}


	public double getMomentumRCS_Z_is() {
		return MomentumRCS_Z_is;
	}


	public void setMomentumRCS_Z_is(double momentumRCS_Z_is) {
		MomentumRCS_Z_is = momentumRCS_Z_is;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
}