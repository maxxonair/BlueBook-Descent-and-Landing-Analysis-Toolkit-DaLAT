package utils;

import java.text.DecimalFormat;

import Model.DataSets.SensorSet;

public class test {

	static DecimalFormat decFormat = new DecimalFormat("##.#");
	
	public static void main(String[] args) {
		double freqIN=10;
		double freqOUT=1;
		
		SRateTransition rateTransition = new SRateTransition(freqIN, freqOUT);
		SensorSet set = new SensorSet();
		double val=0;
		double t=0;
		
		while(t<10) {
			SensorSet intSet = (SensorSet) (rateTransition.get(set));
			System.out.println(decFormat.format(t)+" - "+decFormat.format(val)+" - "+
			decFormat.format(intSet.getControllerTime())+" | ");
			val += 1;
			set.setControllerTime(val);
			t   +=0.1;
		}
		
	}
}
