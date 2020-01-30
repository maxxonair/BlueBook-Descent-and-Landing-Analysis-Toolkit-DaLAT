package Simulator_main.DataSets;

import java.util.ArrayList;
import java.util.List;


public class RealTimeContainer {

	List<RealTimeResultSet> realTimeList = new ArrayList<RealTimeResultSet>();
	private RealTimeResultSet realTimeResultSet;
	
	public RealTimeContainer(){
		
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
