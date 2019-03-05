package Simulator_main;

import org.apache.commons.math3.ode.events.EventHandler;

public class StopCondition{
	
	private EventHandler StopHandler ;
	private double val_is ;
	private double val_condition;
	
	public StopCondition(double val_is, double val_condition) {
		this.val_is = val_is;
		this.val_condition = val_condition;
	}
	
	public void Update_StopCondition(double val_is) {
		this.val_is = val_is;
	}
	
	@SuppressWarnings("unused")
	public  void create_StopHandler() {
		StopHandler =new EventHandler() {
			@Override
			public double g(double t, double[] y) {
				// TODO Auto-generated method stub
				return val_is - val_condition; // 
				//return 0;
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
	
}