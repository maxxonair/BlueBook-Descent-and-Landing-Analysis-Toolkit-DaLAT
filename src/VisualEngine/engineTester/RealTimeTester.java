package VisualEngine.engineTester;

import Simulator_main.RealTimeResultSet;
import VisualEngine.entities.Spacecraft;


public class RealTimeTester {
	
	public static void main(String[] args){
		RealTimeResultSet realTimeResultSet = null ;//= Spacecraft.getRealTimeResultSet(0.1f);
		System.out.println(realTimeResultSet.getAltitude());
		System.out.println(realTimeResultSet.getVelocity());
		System.out.println(realTimeResultSet.getFpa());
		System.out.println(realTimeResultSet.getAzi());
	}

}
