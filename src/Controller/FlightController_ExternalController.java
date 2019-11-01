package Controller;

import java.io.IOException;

import FlightElement.SpaceShip;
import Model.DataSets.ControlCommandSet;
import Model.DataSets.SensorSet;

public class FlightController_ExternalController extends FlightController{
	
	String pythonScript;
	
	public FlightController_ExternalController(String scriptName) {
		pythonScript = scriptName;
	}
	
	@Override
	public ControlCommandSet getCommand(ControlCommandSet controlCommandSet, 
			SensorSet sensorSet, SpaceShip spaceShip, double CtrlFrequency) {
		
		try {
			//Process p = Runtime.getRuntime().exec("python "+pythonScript);
			Runtime.getRuntime().exec("python "+pythonScript);
			// READ FILE 
			
			// SET VALUES in controlCommandSet
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return controlCommandSet;
	}

}
