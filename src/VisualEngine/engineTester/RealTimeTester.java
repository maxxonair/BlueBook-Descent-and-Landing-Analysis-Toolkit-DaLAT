package VisualEngine.engineTester;


import org.lwjgl.util.vector.Vector3f;

import Simulator_main.RealTimeResultSet;
import VisualEngine.entities.Spacecraft;
import VisualEngine.models.TexturedModel;


public class RealTimeTester {
	
	
	public static void main(String[] args) {
		TexturedModel staticModel = null;	
		Vector3f startPostion = new Vector3f(0,150,0);
		Spacecraft spacecraft = new Spacecraft(staticModel, startPostion,0,45,0,1);	
		int k=0;
		while(k<1000) {
		RealTimeResultSet realTimeResultSet = spacecraft.getRealTimeResultSet(0.1f);
		//System.out.println(realTimeResultSet.getAltitude());
		//System.out.println(realTimeResultSet.getVelocity());
		//System.out.println(realTimeResultSet.getFpa());
		//System.out.println(realTimeResultSet.getAzi());
		System.out.println(realTimeResultSet.getAngulRateX());
		System.out.println(realTimeResultSet.getAngulRateY());
		System.out.println(realTimeResultSet.getAngulRateZ());
		//System.out.println(realTimeResultSet.getQuarternions()[0][0]);
		//System.out.println(realTimeResultSet.getQuarternions()[1][0]);
		//System.out.println(realTimeResultSet.getQuarternions()[2][0]);
		//System.out.println(realTimeResultSet.getQuarternions()[3][0]);
		System.out.println("----------------------------------");
		//System.out.println(realTimeResultSet.getSCMass());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		k++;
		}
	}

}
