package utils;

import FlightElement.GNCModel.ControlCommandSet;

public class CRateTransition {
	private double t;					// Time since last transport [s]
	
	private double dtIN;					// Input time delta [s]	
	private double dtOUT;				// Output time step [s]
	
	private ControlCommandSet valuePrev;		    // Previous Transport value [-]
	private ControlCommandSet valueNext;			// Next Transport value [-]
	
	//private double frequencyIN=0;		// Input Frequency[Hz]
	//private double frequencyOUT=0;		// Output Frequency [Hz]
	
	public CRateTransition(double frequencyIN, double frequencyOUT) {		
		dtIN  = 1/frequencyIN;
		dtOUT = 1/frequencyOUT;
	}

	public Object get(ControlCommandSet value) {
		t+=dtIN;
		
				try {
					valueNext=(ControlCommandSet) value.clone();
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


		
		if(t >= dtOUT) { // Switching time reached -> switch next 
			t=0;	
				try {
					valuePrev=(ControlCommandSet) valueNext.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return valueNext;			
		} else {		    // Switching time not reached -> stay previous
			if (valuePrev==null) {

					try {
						valuePrev=(ControlCommandSet) valueNext.clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				
			}
			return valuePrev;
		}
	}
}
