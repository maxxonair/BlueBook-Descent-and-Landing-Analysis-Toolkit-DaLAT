package FlightElement.ForceTorqueModel;

import FlightElement.SpaceShip;
import Simulation.Model.DataSets.PrimaryThrustChangeLog;
import Simulation.Noise.ActuatorNoiseModel;
import utils.UConst;
import Simulator_main.DataSets.IntegratorData;

public class ActuatorModel {
	private static boolean parachuteEjectionMark=true;
	private static boolean heatshieldEjectionMark=true;
	
	//private static double propTime=0;
	private  boolean isTransient=false; 
	private  double transientTime = 1.2;
	
	private  double currentTime;
	
	
	// Modelling Paramters - to be moved 
	//-------------------------------------------------------------------------------
	// Minimum executable Torque CMD - due to pulse width modulation minimum pulse time restriction
	private  double minRCS_Torque_CMD = 0.01;
	// Parameter to jump from 0 to minTorque when cmd torque is below minmum torque. Value of 0.6 sets 
	// the jump to 60 percent minTorque
	private  double minRCS_Torque_Cap_jump=0.7;
	private  boolean isRCS_ISP_PWM_model=true;
	private  boolean isRCS_Thrust_PWM_model=true;
	//-------------------------------------------------------------------------------
	private ActuatorNoiseModel actuatorNoiseModel;
	private long noiseSeed = 7785;
	
	static PrimaryThrustChangeLog primaryThrustChangeLog = new PrimaryThrustChangeLog();
	private SpaceShip spaceShip;
	
	public ActuatorModel(SpaceShip spaceShip) {
		this.spaceShip = spaceShip; 
		actuatorNoiseModel = new ActuatorNoiseModel(noiseSeed);
	}
	
	public ActuatorSet getActuatorSet(IntegratorData integratorData) {
		
	    currentTime = integratorData.getGlobalTime() + spaceShip.getState().gettIS();

		ActuatorSet actuatorSet = new ActuatorSet();
		//double deltaPropellant = spaceShip.getMass() - currentDataSet.getxIS()[6];
		if(integratorData.getNoiseModel().isActuatorNoiseModel()) {
			actuatorNoiseModel.setPrimaryThrustNoise(actuatorSet.getActuatorNoiseSet());
			actuatorNoiseModel.setRCSXNoise(actuatorSet.getActuatorNoiseSet());
			actuatorNoiseModel.setRCSYNoise(actuatorSet.getActuatorNoiseSet());
			actuatorNoiseModel.setRCSZNoise(actuatorSet.getActuatorNoiseSet());
		} else {
			actuatorSet.getActuatorNoiseSet().setPrimaryThrustNoise(0);
			actuatorSet.getActuatorNoiseSet().setRCSMomentumX(0);
			actuatorSet.getActuatorNoiseSet().setRCSMomentumY(0);
			actuatorSet.getActuatorNoiseSet().setRCSMomentumZ(0);
		}
		
		//double primaryPropellant = spaceShip.getProperties().getPropulsion().getPrimaryPropellant()-deltaPropellant;
		double primaryPropellant = spaceShip.getProperties().getPropulsion().getPrimaryPropellantFillingLevel();
		//System.out.println(primaryPropellant);
		// Set Propellant in tanks: 
		actuatorSet.setPrimaryPropellant_is(primaryPropellant);
		// Set ISP
		if(spaceShip.getProperties().getPropulsion().isPrimaryThrottleModel()) {actuatorSet.setPrimaryISP_is(ThrottleMODEL_get_ISP(spaceShip, 
				spaceShip.getgNCModel().getControlCommandSet().getPrimaryThrustThrottleCmd()));} else {
    				actuatorSet.setPrimaryISP_is(spaceShip.getProperties().getPropulsion().getPrimaryISPMax());
    			}
		
		/**
		 * 
		 *  Set Main Thrust 
		 */
		if(primaryPropellant>0) {
			double thrustIs =0;;
			updatePrimaryThrustChangeLog(spaceShip.getgNCModel().getControlCommandSet().getPrimaryThrustThrottleCmd(), integratorData);
			if(primaryThrustChangeLog.isChange()) {isTransient=true;}
			if( isTransient && isTransientTime(integratorData, primaryThrustChangeLog) ) {
				//System.out.println(primaryThrustChangeLog.getPropTime());
				double ThrustRunUp = getMainEngineResponseDelay( primaryThrustChangeLog.getPropTime(),  transientTime,  primaryThrustChangeLog.getCMD_NEW(),  primaryThrustChangeLog.getCMD_OLD());
				double thrustNominal = ThrustRunUp * spaceShip.getProperties().getPropulsion().getPrimaryThrustMax();
				thrustIs = thrustNominal;
			} else {
				double thrustNominal = spaceShip.getgNCModel().getControlCommandSet().getPrimaryThrustThrottleCmd()*spaceShip.getProperties().getPropulsion().getPrimaryThrustMax();
				double randomThrustVariation = thrustNominal * actuatorSet.getActuatorNoiseSet().getPrimaryThrustNoise();
				 thrustIs = thrustNominal + randomThrustVariation;
			}
			//thrustIs = getMainEngineResponseDelay(double timeSinceCMD, double timeToThrustLevel);
		actuatorSet.setPrimaryThrust_is(thrustIs);
		} else {
			actuatorSet.setPrimaryThrust_is(0);
		}
		
		/*
		 * 		TVC Angle Action  
		 */
		actuatorSet.setTVC_alpha((spaceShip.getgNCModel().getControlCommandSet().getTVC_alpha() * spaceShip.getProperties().getPropulsion().getTvc_alpha_MAX_deg()) * UConst.PI/180);
		actuatorSet.setTVC_beta((spaceShip.getgNCModel().getControlCommandSet().getTVC_beta() * spaceShip.getProperties().getPropulsion().getTvc_beta_MAX_deg()) * UConst.PI/180);
		
		/*
		 * 		RCS Torque Command B-X axis 
		 */
		if(!Double.isNaN(spaceShip.getgNCModel().getControlCommandSet().getMomentumRCS_X_cmd()*spaceShip.getProperties().getPropulsion().getRCSMomentumX())) {
			
			 double cmdMin = minRCS_Torque_CMD;  // M	inimum Torque cmd 
			 double momentumCMD=spaceShip.getgNCModel().getControlCommandSet().getMomentumRCS_X_cmd();
			 // Minimum Pulse width check 
			 if(Math.abs(momentumCMD)<cmdMin && isRCS_Thrust_PWM_model) {
				 // If torque cmd is larger than 60 percent of the minimum achievable torque, minimum torque is commanded 
				 if(Math.abs(momentumCMD)/cmdMin > minRCS_Torque_Cap_jump) {
					 momentumCMD=cmdMin*Math.abs(momentumCMD)/momentumCMD;
				 } else {
					 momentumCMD=0;
				 }
			 } 
			 
			double ISP=spaceShip.getProperties().getPropulsion().getSecondaryISP_RCS_X();
			double momentumNominal = momentumCMD*spaceShip.getProperties().getPropulsion().getRCSMomentumX(); 
			double momentumNoise = actuatorSet.getActuatorNoiseSet().getRCSMomentumX()*momentumNominal;
			double momentumIs = momentumNominal + momentumNoise; 
			actuatorSet.setMomentumRCS_X_is(momentumIs);
			actuatorSet.setRCS_X_ISP(ISP);
			
		} else {
			actuatorSet.setMomentumRCS_X_is(0);	
			actuatorSet.setRCS_X_ISP(0);	
			System.out.println("ERROR: Roll control failed - reset RCS X axis cmd");
		}
		
		/*
		 * 		RCS Torque Command B-Y axis
		 */
		if(!Double.isNaN(spaceShip.getgNCModel().getControlCommandSet().getMomentumRCS_Y_cmd()*spaceShip.getProperties().getPropulsion().getRCSMomentumY())) {
			 double cmdMin = minRCS_Torque_CMD;
			 double momentumCMD=spaceShip.getgNCModel().getControlCommandSet().getMomentumRCS_Y_cmd();
			 // Minimum Pulse width check
			 
			 if(Math.abs(momentumCMD)<cmdMin && isRCS_Thrust_PWM_model) {
				 // If torque cmd is larger than 60 percent of the minimum achievable torque, minimum torque is commanded 
				 if(Math.abs(momentumCMD)/cmdMin > minRCS_Torque_Cap_jump) {
					 momentumCMD=cmdMin*Math.abs(momentumCMD)/momentumCMD;
				 } else {
					 momentumCMD=0;
				 }
			 } 
			 
			 double nominalISP =spaceShip.getProperties().getPropulsion().getSecondaryISP_RCS_Y();
			double ISP = PulseWidthModulationModel_get_ISP(nominalISP, momentumCMD);
			actuatorSet.setRCS_Y_ISP(ISP);
			double momentumNominal = momentumCMD*spaceShip.getProperties().getPropulsion().getRCSMomentumY(); 
			double momentumNoise   = actuatorSet.getActuatorNoiseSet().getRCSMomentumY()*momentumNominal;
			double momentumIs      = momentumNominal + momentumNoise; 
			actuatorSet.setMomentumRCS_Y_is(momentumIs);
				
		} else {
			actuatorSet.setMomentumRCS_Y_is(0);
			actuatorSet.setRCS_Y_ISP(0);
			System.out.println("ERROR: Pitch control failed - reset RCS Y axis cmd");
		}
		
		/*
		 * 		RCS Torque Command B-Z axis 
		 */
		if(!Double.isNaN(spaceShip.getgNCModel().getControlCommandSet().getMomentumRCS_Z_cmd()*spaceShip.getProperties().getPropulsion().getRCSMomentumZ())) {
			
			 double cmdMin = minRCS_Torque_CMD;
			 double momentumCMD=spaceShip.getgNCModel().getControlCommandSet().getMomentumRCS_Z_cmd();
			 // Minimum Pulse width check 
			 if(Math.abs(momentumCMD)<cmdMin && isRCS_Thrust_PWM_model) {
				 // If torque cmd is larger than 60 percent of the minimum achievable torque, minimum torque is commanded 
				 if(Math.abs(momentumCMD)/cmdMin > minRCS_Torque_Cap_jump) {
					 momentumCMD=cmdMin*Math.abs(momentumCMD)/momentumCMD;
				 } else {
					 momentumCMD=0;
				 }
			 } 
			double ISP=spaceShip.getProperties().getPropulsion().getSecondaryISP_RCS_Z();
			double momentumNominal = momentumCMD*spaceShip.getProperties().getPropulsion().getRCSMomentumZ(); 
			double momentumNoise = actuatorSet.getActuatorNoiseSet().getRCSMomentumZ()*momentumNominal;
			double momentumIs = momentumNominal + momentumNoise; 
			actuatorSet.setMomentumRCS_Z_is(momentumIs);
			actuatorSet.setRCS_Z_ISP(ISP);
			
		} else {
			actuatorSet.setMomentumRCS_Z_is(0);
			actuatorSet.setRCS_Z_ISP(0);
			System.out.println("ERROR: Yaw control failed - reset RCS Z axis cmd");
		}
		
		/*
		 * 		Parachute Deployment 
		 */
		if(spaceShip.getgNCModel().getControlCommandSet().isParachuteDeployedCMD()) {
			actuatorSet.setParachuteDeployed(true);
		}
		
		/*
		 * 		Parachute Ejection
		 */
		if(spaceShip.getgNCModel().getControlCommandSet().isParachuteEjectCMD()) {
			if(parachuteEjectionMark) {
				spaceShip.getProperties().getMassAndInertia().setMass(spaceShip.getProperties().getMassAndInertia().getMass() - spaceShip.getProperties().getAeroElements().getParachuteMass());
				//System.err.println("EJECT");
				parachuteEjectionMark=false;
			}
			actuatorSet.setParachuteEject(true);
		}
		
		/*
		 * 		Heat Shield Ejection 
		 */
		if(spaceShip.getgNCModel().getControlCommandSet().isHeatShieldEjectionCMD()) {
			if(heatshieldEjectionMark) {
				spaceShip.getProperties().getMassAndInertia().setMass(spaceShip.getProperties().getMassAndInertia().getMass() - spaceShip.getProperties().getAeroElements().getHeatShieldMass());
				heatshieldEjectionMark=false;
			}
			actuatorSet.setHeatShieldEject(true);
		}

		return actuatorSet;
	}

	 private  double ThrottleMODEL_get_ISP(SpaceShip spaceShip, double Throttle_CMD) {
		 // Simulates the effect of decaying ISP during main engine throttle
		 	double IspOut=0;
		 	if(Throttle_CMD>1 || Throttle_CMD<0) {
		 		System.out.println("ERROR: ISP model - throttle command out of range" );
		 		IspOut =  spaceShip.getProperties().getPropulsion().getPrimaryISPMax();
		 	} else if (spaceShip.getProperties().getPropulsion().getPrimaryISPMin()>spaceShip.getProperties().getPropulsion().getPrimaryISPMax()) {
		 		System.out.println("ERROR: ISP model - minimum ISP is larger than maximum ISP" );
		 		IspOut=spaceShip.getProperties().getPropulsion().getPrimaryISPMax();
		 	} else if (spaceShip.getProperties().getPropulsion().getPrimaryISPMin()<0 || spaceShip.getProperties().getPropulsion().getPrimaryISPMax()<0) {
		 		System.out.println("ERROR: ISP model - ISP below 0");
		 		IspOut=0; 
		 	} else {
		 		double m = (spaceShip.getProperties().getPropulsion().getPrimaryISPMax() - spaceShip.getProperties().getPropulsion().getPrimaryISPMin())/(1);
		 		double n = spaceShip.getProperties().getPropulsion().getPrimaryISPMax() - m ; 
		 		IspOut = m * Throttle_CMD + n; 
		 	}
		 	return IspOut; 
		 }
	 
	 private  double PulseWidthModulationModel_get_ISP(double ISP_nominal, double TorqueCMD) {
		 // Simulates the reduced ISP during RCS pulse width modulation with short pulses
		 double IspOut =0;
		 double ISP_max = ISP_nominal;
		 double ISP_min = 0.8*ISP_max;		// Rough approximation 
		 if(TorqueCMD > 1 || TorqueCMD < -1) {
			 	System.out.println("Error: PulseWidthModulationModel failed. CMD out of range.");
			 	IspOut =0;
		 } else {
		 		double m = (ISP_max - ISP_min);
		 		double n =  ISP_min ; 
		 		IspOut = Double.valueOf(m * TorqueCMD + n); 
		 }	
		 if(isRCS_ISP_PWM_model) {
			 return IspOut;
		 } else {
			 return ISP_nominal;
		 }
	 }
	  
	 	private   PrimaryThrustChangeLog updatePrimaryThrustChangeLog(double CMD, IntegratorData integratorData) {
	 		if(CMD!=primaryThrustChangeLog.getCMD_NEW()) {
	 			primaryThrustChangeLog.setTimeStamp(currentTime);
	 			primaryThrustChangeLog.setCMD_OLD(primaryThrustChangeLog.getCMD_NEW());
	 			primaryThrustChangeLog.setCMD_NEW(CMD);
	 			primaryThrustChangeLog.setChange(true);
	 		} else {
	 			primaryThrustChangeLog.setChange(false);
	 		}
	 		return primaryThrustChangeLog;
	 	}
	 	
	 	private  boolean isTransientTime(IntegratorData integratorData, PrimaryThrustChangeLog primaryThrustChangeLog) {
	 		primaryThrustChangeLog.setPropTime( currentTime - primaryThrustChangeLog.getTimeStamp() );	
	 		if(primaryThrustChangeLog.getPropTime() < transientTime) {
	 			isTransient=true;
	 			return true;
	 		} else {
	 			isTransient=false;
	 			return false;
	 		}
	 	}
	 
		private  double getMainEngineResponseDelay(double timeSinceCMD, double timeToThrustLevel, double CMDThrustLevel, double OldThrustLevel) {
			// Simulates the delay between ignition and full thrust during the main engine start-up
			if(timeSinceCMD<timeToThrustLevel) {
				double y=0;
				double x=0;
				if(OldThrustLevel>CMDThrustLevel) {
					double amplitude =	OldThrustLevel - CMDThrustLevel;
					 x = timeToThrustLevel - timeSinceCMD;
					 y = OldThrustLevel - amplitude/( 1 + Math.pow((x/(timeSinceCMD)),-2)) ;
				} else {
					double amplitude =	CMDThrustLevel - OldThrustLevel ;
					 x = timeSinceCMD;
					 y = OldThrustLevel + amplitude/( 1 + Math.pow((x/(timeToThrustLevel-x)),-2));	
				}
				return y;
			} else {
				return 1;
			}
		}
}
