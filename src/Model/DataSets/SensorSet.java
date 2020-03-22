package Model.DataSets;

import Simulator_main.DataSets.RealTimeResultSet;

public class SensorSet extends Object implements Cloneable {
	
	private RealTimeResultSet realTimeResultSet;
	private MasterSet masterSet;
	
	private double globalTime=0;
	private double controllerTime;
	
	
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
		this.realTimeResultSet = realTimeResultSet;
	}
	public MasterSet getMasterSet() {
		return masterSet;
	}
	public void setMasterSet(MasterSet masterSet) {
		this.masterSet = masterSet;
	} 

	
	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}

}
