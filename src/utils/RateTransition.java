package utils;

public class RateTransition {
	
	private double t;					// Time since last transport [s]
	
	private double dtIN;					// Input time delta [s]	
	private double dtOUT;				// Output time step [s]
	
	private double valuePrev;		    // Previous Transport value [-]
	private double valueNext;			// Next Transport value [-]
	
	//private double frequencyIN=0;		// Input Frequency[Hz]
	//private double frequencyOUT=0;		// Output Frequency [Hz]
	
	public RateTransition(double frequencyIN, double frequencyOUT) {		
		dtIN  = 1/frequencyIN;
		dtOUT = 1/frequencyOUT;
	}
	
	public void setIN(double value) {
		t+=dtIN;
		valueNext=value;
	}
	
	public double getOUT() {
		
		if(t > dtOUT) { // Switching time reached -> switch next 
			t=0;	
			valuePrev=valueNext;
			return valueNext;			
		} else {		    // Switching time not reached -> stay previous 
			return valuePrev;
		}

	}

}