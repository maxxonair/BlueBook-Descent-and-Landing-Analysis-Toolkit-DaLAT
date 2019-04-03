package Simulator_main;

import org.apache.commons.math3.ode.events.EventHandler;

public class StopCondition{
	
	public  EventHandler StopHandler ;
	public double val_is;
	private double val_condition;
	private int ConditionOption;
	private double rm ; 
	private double LocalElevation; 
	@SuppressWarnings("unused")
	public double t;
	@SuppressWarnings("unused")
	public double[] y; 
	
	public StopCondition(double val_condition, int ConditionOption, double RM, double LocalElevation) {
		this.val_condition = val_condition;
		this.ConditionOption = ConditionOption; 
		this.rm = RM;
		this.LocalElevation = LocalElevation; 
	}
	
	public void Update_StopCondition(double t, double[]x) {
		this.t = t;
		this.y = x; 
			 if(ConditionOption==0) {this.val_is = t;}							// Time condition
		else if(ConditionOption==1) {this.val_is = x[0];}						// Longitude condition
		else if(ConditionOption==2) {this.val_is = x[1];}						// Latitude condition
		else if(ConditionOption==3) {this.val_is = x[2]-rm-LocalElevation;}		// Radius condition
		else if(ConditionOption==4) {this.val_is = x[3];}						// velocity condition
		else if(ConditionOption==5) {this.val_is = x[4];}						// FPA condition
		else if(ConditionOption==6) {this.val_is = x[5];}						// Azimuth condition
		else if(ConditionOption==7) {this.val_is = x[6];}						// Mass condition
	}
	
	@SuppressWarnings("unused")
	public  void create_StopHandler() {
		StopHandler =new EventHandler() {
			@Override
			public double g(double t, double[] y) {
				// TODO Auto-generated method stub
				return (val_is - val_condition);

			}
			public Action eventOccurred(double t, double[] y, boolean increasing) {
				  return Action.STOP;
				}


			@Override
			public void init(double arg0, double[] arg1, double arg2) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void resetState(double arg0, double[] arg1) {
				// TODO Auto-generated method stub
				
			}
        	
        };
	}
	@SuppressWarnings("unused")
	public  EventHandler get_StopHandler() {
		return StopHandler; 
	}
	
	public double get_val_condition() {
		return val_condition;
	}
	public double get_val_is() {
		return val_is; 
	}
	public int get_ConditionOption() {
		return ConditionOption; 
	}
	
}