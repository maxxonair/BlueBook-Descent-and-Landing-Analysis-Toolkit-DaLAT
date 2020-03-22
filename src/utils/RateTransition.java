package utils;

public class RateTransition {
	
	private double t;					// Time since last transport [s]
	
	private double dtIN;					// Input time delta [s]	
	private double dtOUT;				// Output time step [s]
	
	private ObjectFile valuePrev;		    // Previous Transport value [-]
	private ObjectFile valueNext;			// Next Transport value [-]
	
	//private double frequencyIN=0;		// Input Frequency[Hz]
	//private double frequencyOUT=0;		// Output Frequency [Hz]
	
	public RateTransition(double frequencyIN, double frequencyOUT) {		
		dtIN  = 1/frequencyIN;
		dtOUT = 1/frequencyOUT;
	}

	public Object get(ObjectFile value) {
		t+=dtIN;

			try {
				valueNext=(ObjectFile) value.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		if(t > dtOUT) { // Switching time reached -> switch next 
			t=0;	
			System.out.println(t);

				try {
					valuePrev=(ObjectFile) valueNext.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return valueNext;			
		} else {		    // Switching time not reached -> stay previous
			if (valuePrev==null) {

					try {
						valuePrev=(ObjectFile) valueNext.clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				
			}
			return valuePrev;
		}
	}

}