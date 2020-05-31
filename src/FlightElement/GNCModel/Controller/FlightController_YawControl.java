package FlightElement.GNCModel.Controller;

import FlightElement.SpaceShip;

public class FlightController_YawControl extends FlightController{

	
	
	public FlightController_YawControl() {
		
	}
	
	@Override
	public void setCommand(SpaceShip spaceShip) {
		   double CTRL_ERROR =  spaceShip.getSensorModel().getSensorSet().getRealTimeResultSet().getEulerAngle().yaw ;     

		   	 double RCS_Z_CMD = -  PID_01.PID_001(CTRL_ERROR,1/spaceShip.getProperties().getoBC().getControllerFrequency(), 0.8, 0.000, 2, 1, -1);

		   	spaceShip.getgNCModel().getControlCommandSet().setMomentumRCS_Z_cmd(RCS_Z_CMD);

	}
}
