package FlightElement.SensorModel;

import Simulation.Model.DataSets.MasterSet;
import Simulator_main.DataSets.RealTimeResultSet;

public class SensorSet extends Object implements Cloneable {
	
	private RealTimeResultSet realTimeResultSet;
	private MasterSet masterSet;
	
	private double globalTime=0;
	private double controllerTime;
	
	public SensorSet() {
		
	}	
	
	public double getControllerTime() {
		return controllerTime;
	}
	public void setControllerTime(double controllerTime) {
		this.controllerTime = controllerTime;
	}
	public double getGlobalTime() {
		return globalTime;
	}
	public void setGlobalTime(double globalTime) {
		this.globalTime = globalTime;
	}
	public RealTimeResultSet getRealTimeResultSet() {
		return realTimeResultSet;
	}
	public void setRealTimeResultSet(RealTimeResultSet realTimeResultSet) {
		try {
			this.realTimeResultSet = (RealTimeResultSet) realTimeResultSet.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public MasterSet getMasterSet() {
		return masterSet;
	}
	public void setMasterSet(MasterSet masterSet) {
		try {
			this.masterSet = (MasterSet) masterSet.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	
	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}

}
