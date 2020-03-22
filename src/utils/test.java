package utils;

import java.text.DecimalFormat;

import Model.DataSets.ControlCommandSet;

public class test {

	static DecimalFormat decFormat = new DecimalFormat("##.#");
	
	public static void main(String[] args) {
		double freqIN=10;
		double freqOUT=1;
		
		CRateTransition rateTransition = new CRateTransition(freqIN, freqOUT);
		ControlCommandSet set = new ControlCommandSet();
		double val=0;
		double t=0;
		
		while(t<10) {
			ControlCommandSet intSet = (ControlCommandSet) (rateTransition.get(set));
			System.out.println(decFormat.format(t)+" - "+decFormat.format(val)+" - "+
			decFormat.format(intSet.getMomentumRCS_X_cmd())+" | ");
			val += 1;
			set.setMomentumRCS_X_cmd(val);
			t   +=0.1;
		}
		
	}
}
