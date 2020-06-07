package utils.Plotter;

import java.util.ArrayList;
import java.util.List;

public class DataSetXY {
	
	private List<Pair> dataSet = new ArrayList<Pair>();
	
	private String name;
	
	public DataSetXY() {
		
	}
	
	public void addPair(Pair pair) {
		dataSet.add(pair);
	}

	public List<Pair> getDataSet() {
		return dataSet;
	}

	public void setDataSet(List<Pair> dataSet) {
		this.dataSet = dataSet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void voidDataset() {
		for(int i=dataSet.size()-1;i>=0;i--) {
			dataSet.remove(i);
		}
	}
	

}
