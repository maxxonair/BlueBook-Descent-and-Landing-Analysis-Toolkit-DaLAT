package VisualEngine.engineTester;


import org.lwjgl.util.vector.Vector3f;

import FlightElement.SpaceShip;
import Simulator_main.DataSets.RealTimeResultSet;
import VisualEngine.entities.Spacecraft;
import VisualEngine.models.TexturedModel;


public class RealTimeTester {
	
    static double[][] InertiaTensorMatrix   =         {{   8000    ,    0       ,   0},
			{      0    ,    8000    ,   0},
			{      0    ,    0       ,   8000}};

	public static void main(String[] args) {
		TexturedModel staticModel = null;	
		Vector3f startPostion = new Vector3f(0,150,0);
		SpaceShip spaceShip = new SpaceShip();
		spaceShip.setMass(15000);
		spaceShip.getPropulsion().setPrimaryPropellant(300);
		spaceShip.setInertiaTensorMatrix(InertiaTensorMatrix);
		spaceShip.getPropulsion().setPrimaryISPMax(330);
		spaceShip.getPropulsion().setPrimaryThrustMax(30000);
		Spacecraft spacecraft = new Spacecraft(spaceShip, staticModel, startPostion,0,45,0,1);	
		int k=0;
		while(k<1000) {
		@SuppressWarnings("static-access")
		RealTimeResultSet realTimeResultSet = spacecraft.getRealTimeResultSet(0.1f);
		System.out.println(realTimeResultSet.getAltitude());
		System.out.println(realTimeResultSet.getVelocity());
		//System.out.println(realTimeResultSet.getFpa());
		//System.out.println(realTimeResultSet.getAzi());
		//System.out.println(realTimeResultSet.getPQR()[0][0]);
		//System.out.println(realTimeResultSet.getPQR()[1][0]);
		//System.out.println(spacecraft.getPQR()[2][0]);
		//System.out.println(realTimeResultSet.getPQR()[2][0]);
		//System.out.println(realTimeResultSet.getQuarternions()[0][0]);
		//System.out.println(realTimeResultSet.getQuarternions()[1][0]);
		//System.out.println(realTimeResultSet.getQuarternions()[2][0]);
		//System.out.println(realTimeResultSet.getQuarternions()[3][0]);
		System.out.println(realTimeResultSet.getSCMass());
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
