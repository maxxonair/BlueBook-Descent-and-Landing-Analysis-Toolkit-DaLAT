package Simulator_main.DataSets;

import java.util.ArrayList;
import java.util.List;

import Model.DataSets.MasterSet;

public class RealTimeContainer {

	List<MasterSet> masterList = new ArrayList<MasterSet>();
	List<RealTimeResultSet> realTimeList = new ArrayList<RealTimeResultSet>();
	private RealTimeResultSet realTimeResultSet;
	
	public RealTimeContainer(){
		
	}
	

	public List<MasterSet> getMasterList() {
		return masterList;
	}


	public void setMasterList(List<MasterSet> realTimeSet) {
		this.masterList = realTimeSet;
	}


	public List<RealTimeResultSet> getRealTimeList() {
		return realTimeList;
	}


	public void setRealTimeList(List<RealTimeResultSet> realTimeList) {
		this.realTimeList = realTimeList;
	}


	public RealTimeResultSet getRealTimeResultSet() {
		return realTimeResultSet;
	}
	public void setRealTimeResultSet(RealTimeResultSet realTimeResultSet) {
		this.realTimeResultSet = realTimeResultSet;
	}
	
}
