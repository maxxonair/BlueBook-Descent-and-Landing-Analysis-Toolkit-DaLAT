package Simulator_main.DataSets;

import java.util.ArrayList;
import java.util.List;

import Model.DataSets.MasterSet;

public class RealTimeContainer {

	List<MasterSet> realTimeSet = new ArrayList<MasterSet>();
	private RealTimeResultSet realTimeResultSet;
	
	public RealTimeContainer(){
		
	}
	

	public List<MasterSet> getRealTimeSet() {
		return realTimeSet;
	}


	public void setRealTimeSet(List<MasterSet> realTimeSet) {
		this.realTimeSet = realTimeSet;
	}


	public RealTimeResultSet getRealTimeResultSet() {
		return realTimeResultSet;
	}
	public void setRealTimeResultSet(RealTimeResultSet realTimeResultSet) {
		this.realTimeResultSet = realTimeResultSet;
	}
	
}
