package Simulator_main.DataSets;

import java.util.ArrayList;
import java.util.List;


public class RealTimeContainer extends Object implements Cloneable {

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
		try {
			this.realTimeResultSet = (RealTimeResultSet) realTimeResultSet.clone();
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
