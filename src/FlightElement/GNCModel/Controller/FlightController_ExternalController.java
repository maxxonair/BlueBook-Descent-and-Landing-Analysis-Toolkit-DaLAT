package FlightElement.GNCModel.Controller;

import java.io.IOException;

import FlightElement.SpaceShip;

public class FlightController_ExternalController extends FlightController{
	
	String pythonScript;
	
	public FlightController_ExternalController(String scriptName) {
		pythonScript = scriptName;
	}
	
	@Override
	public void setCommand(SpaceShip spaceShip) {
		
		try {
			//Process p = Runtime.getRuntime().exec("python "+pythonScript);
			Runtime.getRuntime().exec("python "+pythonScript);
			// READ FILE 
			
			// SET VALUES in controlCommandSet
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
